package no.lambda.presentation.javalin;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;


public class JavalinServer {

    public static Javalin create() {
        return Javalin.create(config -> config.staticFiles.add("/public", Location.CLASSPATH));
    }
}
