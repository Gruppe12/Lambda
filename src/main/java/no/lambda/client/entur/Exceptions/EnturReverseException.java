package no.lambda.client.entur.Exceptions;

public class EnturReverseException extends RuntimeException{
    public EnturReverseException(){
        super();
    }

    public EnturReverseException(String message){
        super(message);
    }

    public EnturReverseException(String message, Throwable cause){
        super(message, cause);
    }

    public EnturReverseException(Throwable cause){
        super(cause);
    }


}
