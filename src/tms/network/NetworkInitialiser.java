package tms.network;

import tms.util.InvalidNetworkException;

import java.io.IOError;
import java.io.IOException;

public class NetworkInitialiser {
    // Delimiter used to separate individual pieces of data on a single line
    public static final String LINE_INFO_SEPARATOR = "";
    // Delimiter used to separate individual elements in a variable-length list
    // on a single line
    public static final String LINE_LIST_SEPARATOR = "";
    // TODO: Update the value of the LINE_INFO_SEPARATOR;

    /**
     * */
    public NetworkInitialiser(){

    }

    /**
     * Loads a saved Network from the file with the given filename.
     *
     * Network files have the following structure. Square brackets indicate that
     * the data inside them is optional. For example, a route does not
     * necessarily need a speed sign (speedSignSpeed).
     *
     * See the demo network for an example (demo.txt).
     *
     * ; This is a comment. It should be ignored.
     * numIntersections
     * numRoutes
     * yellowTime
     * intersectionId[:duration:sequence,of,intersection,ids]
     * ... (more intersections)
     * intersectionFromId:intersectionToId:defaultSpeed:numSensors[:speedSignSpeed]
     * SENSORTYPE:threshold:list,of,data,values
     * ... (more routes and sensors)
     *
     * A network file is invalid if any of the following conditions are true:
     *
     *     The number of intersections specified is not equal to the number of
     *     intersections read from the file.
     *     The number of routes specified does not match the number read from
     *     the file.
     *     The number of sensors specified for a route does not match the number
     *     read from the line below.
     *     An intersection referenced by another intersection does not exist,
     *     or no route exists between them.
     *     Two or more intersections have the same identifier string.
     *     Two or more routes have the same starting and ending intersections.
     *     A sensor type that is not one of the three provided demo sensors.
     *     A route contains sensors of the same type.
     *     The traffic light yellow time is less than one (1).
     *     A traffic light duration is less than the traffic light yellow time
     *     plus one (1).
     *     Any numeric value that should be positive is less than zero. This
     *     includes:
     *         route speeds
     *         speed sign speeds
     *         sensor thresholds (also, cannot be zero)
     *         sensor data values
     *     The colon-delimited format is violated, i.e. there are more/fewer
     *     colons than expected.
     *     Any numeric value fails to be parsed.
     *     The file contains any more than two (2) newline characters at the
     *     end of the file.
     * @param fileName name of the file from which to load the network
     * @return the Network loaded from the file
     * @throws IOException any IOExceptions encountered when reading the file
     * are bubbled up
     * @throws InvalidNetworkException if the file format of the given file
     * is invalid.
     */
    public static Network loadNetwork(String fileName) throws IOException,
            InvalidNetworkException {
        return null;
    }
}
