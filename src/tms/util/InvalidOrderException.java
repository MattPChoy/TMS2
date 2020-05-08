package tms.util;

public class InvalidOrderException extends Exception{
    public InvalidOrderException(){
        super();
    }

    public InvalidOrderException(String message){
        super(message);
    }

    public InvalidOrderException(String message, Throwable error){
        super(message, error);
    }

}
