package webtest;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import no.lambda.controller.PlanTripController;
import no.lambda.presentation.javalin.TripsAPI;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;


// ---- Eksempel på weblayer test ----


public class TripsApiWebLayerTest {

    private Javalin createApp() {
        PlanTripController controller = Mockito.mock(PlanTripController.class);
        Javalin app = Javalin.create();
        TripsAPI.configure(app, controller);
        return app;
    }

    @Test
    public void getTrips_Returns400_ForInvalidTime() {
        Javalin app = createApp();

        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(
                    "/api/trips?from=Oslo&to=Bergen&time=not_a_time&arriveBy=false"
            );
            assertEquals(400, response.code());
        });
    }

    @Test
    public void getTrips_Returns400_ForBlankFrom() {
        Javalin app = createApp();

        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(
                    "/api/trips?from=%20%20%20&to=Bergen&time=2025-10-23T19:37:25.123+02:00&arriveBy=false"
            );
            assertEquals(400, response.code());
        });
    }
}
