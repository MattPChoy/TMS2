package tms.display;

/**
 * Enum to represent the possible actions for buttons in the GUI.
 * <p>
 * <b>WARNING: </b> You do <b>not</b> need to implement the values() or
 * valueOf(String) methods as part of the assignments. These methods are
 * automatically generated however still appear in the Javadoc.
 * @ass2_given
 */
public enum ButtonOptions {
    /**
     * Adds a new intersection to the network.
     */
    ADD_INTERSECTION,
    /**
     * Adds a new sensor to a route.
     */
    ADD_SENSOR,
    /**
     * Adds a new electronic speed sign to a route.
     */
    ADD_SIGN,
    /**
     * Changes the displayed speed limit of a route's electronic speed sign.
     */
    SET_SPEED,
    /**
     * Creates a one-way connecting route between two intersections.
     */
    ADD_CONN,
    /**
     * Creates a two-way connecting route between two intersections,
     * one route in each direction.
     */
    ADD_TWO_WAY_CONN,
    /**
     * Adds a set of traffic lights to an intersection.
     */
    ADD_LIGHT,
    /**
     * Changes the duration of each green-yellow cycle for a set of traffic
     * lights at an intersection.
     */
    CHANGE_LIGHT_DURATION,
    /**
     * Reduces the speed limit of incoming routes to an intersection, or to
     * a route's origin intersection.
     */
    REDUCE_SPEED,
    /**
     * Saves the current network to a file for later use.
     */
    SAVE,
    /**
     * Pauses the process of time being elapsed.
     */
    PAUSE,
}
