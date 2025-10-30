package no.lambda.client.entur.Exceptions;

public class EnturGeocoderException extends RuntimeException{

    public EnturGeocoderException(){
        super();
    }

    public EnturGeocoderException(String message){
        super(message);
    }

    public EnturGeocoderException(String message, Throwable cause){
        super(message, cause);
    }

    public EnturGeocoderException(Throwable cause){
        super(cause);
    }

}
