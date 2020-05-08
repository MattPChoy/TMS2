package tms.util;

public class InvalidNetworkException extends Exception{
    public InvalidNetworkException(){
        super();
    }

    public InvalidNetworkException(String message){
        super(message);
    }

    public InvalidNetworkException(String message, Throwable error){
        super(message, error);
    }
}
