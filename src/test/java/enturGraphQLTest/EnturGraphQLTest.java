package enturGraphQLTest;

import no.lambda.client.entur.Exceptions.EnturGraphQLExceptions;
import no.lambda.client.entur.GraphQL.EnturGraphQLClient;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

public class EnturGraphQLTest {
    private static MockWebServer server;

    @BeforeAll
    public static void start() throws IOException{
        server = new MockWebServer();
        server.start();
    }

    @AfterAll
    public static void stop() throws IOException{
        server.shutdown();
    }

    private EnturGraphQLClient newClient(){
        String baseUrl = server.url("/journey-planner/v3/graphql").toString();
        return new EnturGraphQLClient(
                baseUrl,
                new OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build(),
                null,
                "LambdaTechAS-SkoleProsjekt_HIOF2025"
        );
    }

    @Test
    //tester om responsen fra EnturGraphQLClient blir korrekt mappet
    void execute_parsesRealEnturGraphQLPayload() throws InterruptedException {

        String body = """
                {
                  "data": {
                    "trip": {
                      "tripPatterns": [
                        {
                          "expectedStartTime": "2025-10-20T12:10:17+02:00",
                          "expectedEndTime": "2025-10-20T12:57:28+02:00",
                          "duration": 2831,
                          "walkDistance": 1087.32,
                          "legs": [
                            {
                              "mode": "foot",
                              "distance": 45.51,
                              "line": null
                            },
                            {
                              "mode": "rail",
                              "distance": 42250.97,
                              "line": {
                                "id": "VYG:Line:RE20",
                                "publicCode": "RE20"
                              }
                            },
                            {
                              "mode": "foot",
                              "distance": 1041.81,
                              "line": null
                            }
                          ]
                        }
                      ]
                    }
                  }
                }
                """;
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(body)
        );

        EnturGraphQLClient client = newClient();

        var dto = client.execute("query MyTrip { trip { ... } }", Map.of("from", "A", "to", "B"));
        var trip = dto.data.trip.tripPatterns.get(0);
        assertEquals(1, dto.data.trip.tripPatterns.size());

        assertNotNull(dto);

        assertEquals(2831, trip.duration);
        assertEquals(1087.32, trip.walkDistance, 1e-6);

        assertEquals("2025-10-20T12:10:17+02:00", trip.expectedStartTime);
        assertEquals("2025-10-20T12:57:28+02:00", trip.expectedEndTime);

        assertEquals(3, trip.legs.size());

        assertEquals("foot", trip.legs.get(0).mode);

        assertEquals("rail", trip.legs.get(1).mode);
        assertEquals("VYG:Line:RE20", trip.legs.get(1).line.id);
        assertEquals("RE20", trip.legs.get(1).line.publicCode);

        assertEquals("foot", trip.legs.get(2).mode);

        RecordedRequest request = server.takeRequest();
        assertTrue(request.getHeader("Content-Type").startsWith("application/json"));
        assertEquals("LambdaTechAS-SkoleProsjekt_HIOF2025", request.getHeader("ET-Client-Name"));
        assertEquals("/journey-planner/v3/graphql", request.getRequestUrl().encodedPath());
        assertEquals("POST", request.getMethod());

    }

    @Test
        //tester at HTTP-feil kaster EnturGraphQLExceptions
    void execute_httpError_throwsCustomException() {
        server.enqueue(new MockResponse().setResponseCode(502).setBody("Bad Gateway"));

        EnturGraphQLClient client = newClient();
        assertThrows(EnturGraphQLExceptions.class,
                () -> client.execute("query {}", Map.of()));
    }
}
