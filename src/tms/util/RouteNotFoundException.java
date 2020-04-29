package tms.util;

/**
 * Exception thrown when a request is made for a route that does not exist.
 * @ass1
 */
public class RouteNotFoundException extends Exception {
    /**
     * Constructs a normal RouteNotFoundException with no error message or
     * cause.
     *
     * @see Exception#Exception()
     * @ass1
     */
    public RouteNotFoundException() {
        super();
    }

    /**
     * Constructs a RouteNotFoundException that contains a helpful message
     * detailing why the exception occurred.
     * <p>
     * <b>Note: </b>implementing this constructor is <b>optional</b>. It has
     * only been included to ensure your code will compile if you give your
     * exception a message when throwing it. This practice can be useful for
     * debugging purposes.
     * <p>
     * <b>Important: </b>do not write JUnit tests that expect a valid
     * implementation of the assignment to have a certain error message, as the
     * official solution will use different messages to those you are expecting,
     * if any at all.
     *
     * @param message detail message
     * @see Exception#Exception(String)
     * @ass1
     */
    public RouteNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a RouteNotFoundException that contains a helpful message
     * detailing why the exception occurred and a cause of the exception.
     * <p>
     * <b>Note: </b>implementing this constructor is <b>optional</b>. It has
     * only been included to ensure your code will compile if you give your
     * exception a message when throwing it. This practice can be useful for
     * debugging purposes.
     * <p>
     * <b>Important: </b>do not write JUnit tests that expect a valid
     * implementation of the assignment to have a certain error message, as the
     * official solution will use different messages to those you are expecting,
     * if any at all.
     *
     * @param message detail message
     * @param err cause of the exception
     * @see Exception#Exception(String, Throwable)
     * @ass1
     */
    public RouteNotFoundException(String message, Throwable err) {
        super(message, err);
    }
}
