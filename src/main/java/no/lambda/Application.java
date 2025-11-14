package no.lambda;

import io.javalin.Javalin;

import no.lambda.controller.PlanTripController;
import no.lambda.presentation.javalin.*;



public class Application {

    public static void main(String[] args) throws Exception {
        // Logikken for oppstart av nettside i Javalin
        startWebsite();
    }

    // Logikken for oppstart av nettsiden
    public static void startWebsite() throws Exception{

        // Lager kontroller for SOMETHING
        var _controller = new PlanTripController();

        // Makes the app object based on the HTML/CSS/JS in the public folder
        // We connect it a port, and start hosting it.
        Javalin app = JavalinServer.create().start(8080);

        ServerConfig.registerCommon(app);

        // ---- API-er ----

        AutocompleteAPI.configure(app, _controller);

        TripsAPI.configure(app, _controller);

        // -- With logg-in

        FavoritesApi.configure(app, _controller);
    }
}

