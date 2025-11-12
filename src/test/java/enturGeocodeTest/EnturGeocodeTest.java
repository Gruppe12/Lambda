package enturGeocodeTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.lambda.client.entur.Exceptions.EnturGeocoderException;
import no.lambda.client.entur.Geocoder.EnturGeocoderClient;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

    public class EnturGeocodeTest {

        private static MockWebServer server;

        @BeforeAll
        public static void start() throws IOException {
            server = new MockWebServer();
            server.start();
        }

        @AfterAll
        public static void stop() throws IOException {
            server.shutdown();
        }

        private EnturGeocoderClient newClient() {
            String baseUrl = server.url("/geocoder/v1/autocomplete").toString();
            return new EnturGeocoderClient(
                    new OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build(),
                    new ObjectMapper(),
                    baseUrl,
                    "LambdaTechAS-SkoleProsjekt_HIOF2025"
            );
        }

        //Denne testen er for å sjekke om EnturGeocoderClient klarer å parse response body fra API-et riktig.
        @Test
        void geoCode_parsesRealEnturPayload_minimalFields() throws Exception {
            // Forkortet til 2 features for testens skyld. Dette er ekte payload fra API-et
            String body = """
        {
          "geocoding": { "version": "0.2" },
          "type": "FeatureCollection",
          "features": [
            {
              "type": "Feature",
              "geometry": { "type": "Point", "coordinates": [11.384822, 59.119946] },
              "properties": {
                "id": "NSR:GroupOfStopPlaces:33",
                "county": "Østfold",
                "label": "Halden"
              }
            },
            {
              "type": "Feature",
              "geometry": { "type": "Point", "coordinates": [10.62056, 59.885534] },
              "properties": {
                "id": "NSR:StopPlace:3634",
                "county": "Akershus",
                "label": "Halden, Bærum",
                "category": ["onstreetBus"]
              }
            }
          ],
          "bbox": [9.68, 58.96, 11.49, 60.15]
        }
        """;


            server.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody(body));

            //Tester så om EnturGeocoderClient klarer å parse responsen riktit til en GeoHit objekt.
            EnturGeocoderClient client = newClient();
            var hits = client.geoCode("Halden");

            //Verifiser mapping
            assertNotNull(hits);
            assertEquals(2, hits.size());

            var h0 = hits.get(0);
            assertEquals("Halden", h0.label());
            assertEquals("Østfold", h0.County());
            assertEquals(59.119946, h0.latitude(), 1e-6);
            assertEquals(11.384822, h0.longitude(), 1e-6);
            assertEquals("NSR:GroupOfStopPlaces:33", h0.placeId());

            var h1 = hits.get(1);
            assertEquals("Halden, Bærum", h1.label());
            assertEquals("Akershus", h1.County());
            assertEquals(59.885534, h1.latitude(), 1e-6);
            assertEquals(10.62056, h1.longitude(), 1e-6);
            assertEquals("NSR:StopPlace:3634", h1.placeId());

            // Verifiser at requesten ble sendt riktig
            RecordedRequest req = server.takeRequest();
            assertEquals("GET", req.getMethod());
            assertEquals("/geocoder/v1/autocomplete", req.getRequestUrl().encodedPath());
            assertEquals("LambdaTechAS-SkoleProsjekt_HIOF2025", req.getHeader("ET-Client-Name"));

            // Query-params: lang=no&size=10&text=<URL-encodet>
            var qp = req.getRequestUrl().queryParameterNames();
            assertTrue(qp.contains("lang"));
            assertTrue(qp.contains("size"));
            assertTrue(qp.contains("text"));
            assertEquals("no", req.getRequestUrl().queryParameter("lang"));
            assertEquals("10", req.getRequestUrl().queryParameter("size"));
            assertEquals("Halden", req.getRequestUrl().queryParameter("text")); // MockWebServer viser allerede dekodet verdi
        }

        //Denne testen er for å sjekke om riktig Exception blir kastet om body hear tom features
        @Test
        void geoCode_noFeatures_throwsDomainException() {
            String empty = """
        {
          "geocoding": { "version": "0.2" },
          "type": "FeatureCollection",
          "features": []
        }
        """;
            server.enqueue(new MockResponse().setResponseCode(200).setBody(empty));

            EnturGeocoderClient client = newClient();
            EnturGeocoderException ex = assertThrows(EnturGeocoderException.class,
                    () -> client.geoCode("ukjent sted"));
            assertTrue(ex.getMessage().toLowerCase().contains("no geocoding results"));
        }


        @Test
        void geoCode_httpError_throwsDomainException() {
            server.enqueue(new MockResponse().setResponseCode(503).setBody("Service Unavailable"));
            EnturGeocoderClient client = newClient();
            assertThrows(EnturGeocoderException.class, () -> client.geoCode("Halden"));
        }

}
