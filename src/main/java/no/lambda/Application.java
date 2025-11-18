package no.lambda;

import io.javalin.Javalin;

import no.lambda.controller.PlanTripController;
import no.lambda.presentation.javalin.*;



public class Application {

    public static void main(String[] args) throws Exception {

        // This methode contains all the logic for our application
        startWebsite();
    }

    // Logic for Starting
    public static void startWebsite() throws Exception{

        // The bridge between our Javalin REST API and the business logic (Service-level)
        var _controller = new PlanTripController();

        // Makes the app object based on the HTML/CSS/JS in the public folder
        // We connect it a port, and start hosting it.
        Javalin app = JavalinServer.create().start(8080);

        // Takes the app-object and configures the necessary rules for it,
        // such as Error-handling, Users etc.
        ServerConfig.registerCommon(app);

        // Starts up and creates the API for the Autocomplete functionality
        AutocompleteAPI.configure(app, _controller);

        // Starts up and creates the API for the Trips-API functionality.
        // This contains the logic for providing routes for trips.
        TripsAPI.configure(app, _controller);

        // Starts up and creates the API for ALL favorite functionality.
        // Such as add favorite, remove favorite, get all favorites from a use, and
        // check if something is a favorite or not.
        FavoritesApi.configure(app, _controller);
    }
}

