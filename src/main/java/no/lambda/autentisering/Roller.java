package no.lambda.autentisering;
import io.javalin.security.RouteRole;


// Dette er bare roller, uhh kinda weird implemntation Javalin but okay.
// Den gjør at Javalin vet om disse roller og tillater bruk
public enum Roller implements RouteRole{
    OPEN,
    LOGGED_IN
}
