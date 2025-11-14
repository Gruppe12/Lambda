package no.lambda.presentation.javalin;

import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.validation.ValidationException;

import static no.lambda.autentisering.Inlogging.getUserRole;

public class ServerConfig {

    public static void registerCommon(Javalin app) {

        // sjekker inlogging
        app.beforeMatched(ctx -> {
            // Dette gjør at Mennesker kan se ui, whoops :P
            if (ctx.routeRoles().isEmpty()) {
                return;
            }

            var role = getUserRole(ctx);

//            if (ctx.routeRoles().contains(Roller.OPEN)){
//                return;
//            } else

            if (!ctx.routeRoles().contains(role)) {
                throw new UnauthorizedResponse();
            }
        });

        app.exception(ValidationException.class, (e, ctx) -> ctx.status(400).json(e.getErrors()));

    }
}
