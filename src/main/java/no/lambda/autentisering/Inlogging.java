package no.lambda.autentisering;
import io.javalin.http.Context;

public class Inlogging {
    public static Integer getUserId(Context ctx) {
        String brukerId = ctx.header("Bruker-id");
        if (brukerId == null){
            return null;
        }

        return Integer.parseInt(brukerId);
    }

    public static Roller getUserRole(Context ctx) {
        if (getUserId(ctx) == null) {
            return Roller.OPEN;
        } else {
            return Roller.LOGGED_IN;
        }
    }


// hvis vi får onklig brukere i databasen :|
// Det er muligens annerledes logikk ved bruk av google og/eller vipps inloggin API.

//    public static Roller getUserId(Context ctx) {
            // i'm not gonna use Okt but your free too
//        String sessionId = ctx.cookie("Session-id");
//        if (sessionId == null){
//            return null;
//        }
//
//        // Database logikk til å få brukerId
//
//        return brukerId;
//
//    }
}
