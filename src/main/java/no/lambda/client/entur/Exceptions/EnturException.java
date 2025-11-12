package no.lambda.client.entur.Exceptions;

public class EnturException extends RuntimeException{

    public EnturException(String message, Throwable cause){
        super(message,cause);
    }
    public EnturException(String message){
        super(message);
    }

}
