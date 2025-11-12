package no.lambda.client.entur.Exceptions;

public class EnturGraphQLExceptions extends RuntimeException{


    public EnturGraphQLExceptions(){
        super();
    }

    public EnturGraphQLExceptions(String message){
        super(message);
    }

    public EnturGraphQLExceptions(String message, Throwable cause){
        super(message, cause);
    }

    public EnturGraphQLExceptions(Throwable cause){
        super(cause);
    }



}
