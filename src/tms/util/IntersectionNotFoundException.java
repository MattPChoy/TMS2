package tms.util;

public class IntersectionNotFoundException extends Exception{
    public IntersectionNotFoundException(){
        super();
    }

    public IntersectionNotFoundException(String message){
        super(message);
    }

    public IntersectionNotFoundException(String message, Throwable error){
        super(message, error);
    }
}
