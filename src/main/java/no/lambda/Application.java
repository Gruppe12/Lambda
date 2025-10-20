package no.lambda;
import io.javalin.http.staticfiles.Location;

import io.javalin.Javalin;


public class Application {

    public static void main(String[] args) {

        // Makes the app object based on the HTML/CSS/JS in the public folder
        // We connect it a port, and start hosting it.
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(8080);
    }


}
