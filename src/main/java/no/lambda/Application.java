package no.lambda;
import io.javalin.http.staticfiles.Location;

import io.javalin.Javalin;


public class Application {

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(8080);
    }


}
