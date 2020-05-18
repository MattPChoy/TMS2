package tms.network;

import tms.sensors.*;
import tms.util.*;

import java.io.*;
import java.util.*;

public class NetworkInitialiser {
    /** Delimiter used to separate individual pieces of data on a single line */
    public static final String LINE_INFO_SEPARATOR = ":";
    /** Delimiter used to separate individual elements in a variable-length list
        on a single line */
    public static final String LINE_LIST_SEPARATOR = ",";


    public NetworkInitialiser() {

    }

    /**
     * Loads a saved Network from the file with the given filename.
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
     * intersectionFromId:intersectionToId:defaultSpeed:numSensors [:speedSignSpeed]
     * SENSORTYPE:threshold:list,of,data,values
     * ... (more routes and sensors)
     *
     * A network file is invalid if any of the following conditions are true:
     *
     * The number of intersections specified is not equal to the number of
     * intersections read from the file.
     * The number of routes specified does not match the number read from the
     * file.
     * The number of sensors specified for a route does not match the number
     * read from the line below.
     * An intersection referenced by a route does not exist.
     * An intersection has an invalid ID according to
     * Network.createIntersection(String).
     * Two or more intersections have the same identifier string.
     * Two or more routes have the same starting and ending intersections,
     * e.g. a route X→Y and another route X→Y. A route is allowed to end at its
     * starting intersection, i.e. X→X is allowed.
     * A sensor type that is not one of the three provided demo sensors.
     * A route contains sensors of the same type.
     * The traffic light yellow time is less than one (1).
     * A traffic light duration is less than the traffic light yellow time plus
     * one (1).
     * For intersections with traffic lights:
     * The traffic light order for an intersection is not a permutation of that
     * intersection's incoming routes.
     * The traffic light order for an intersection is empty.
     * Any numeric value that should be non-negative is less than zero. This
     * includes:
     * route speeds
     * speed sign speeds
     * sensor thresholds (also, cannot be zero)
     * sensor data values
     * The colon-delimited format is violated, i.e. there are more/fewer colons
     * than expected.
     * Any numeric value fails to be parsed.
     * An empty line occurs where a non-empty line is expected.
     * The file contains any more than two (2) newline characters at the end of
     * the file.
     * @param filename name of the file from which to load the network
     * @return the Network loaded from the file,
     * @throws IOException any IOExceptions encountered when reading the file
     * are bubbled up.
     * @throws InvalidNetworkException if the gile format of the given file
     * is invalid.
     */
    public static Network loadNetwork(String filename) throws IOException,
            InvalidNetworkException {
        Network n = new Network();

        // Parse the network as a file.
        List<String> file = read(filename);

        // Make sure that the first 3 lines are valid constant values;
        // Number of intersections must be non-negative
        // Number of routes must be non-negative
        // YellowTime must be positive (non-negative and non-zero)
        validateConstants(file);
        n.setYellowTime(getYellowTime(file));

        // Create intersections, then routes, sensors and traffic lights
        addIntersections(file, n);
        addRoutes(file, n);
        addSensors(file, n);

        addLights(file, n);
        addSpeedSign(file, n);

        checkNumRoutes(file, n);
        checkNumIntersections(file, n);
        checkNumSensors(file, n);
        return n;
    }

    private static void validateConstants(List<String> file)
            throws InvalidNetworkException {
        for (int lineNumber = 1; lineNumber < 4; lineNumber++){
            String line = file.get(lineNumber-1);
            if (!isInteger(line)){
                throw new InvalidNetworkException(
                        "Argument cannot be converted to an integer (" + line
                                + ")"
                );
            }
            int value = Integer.parseInt(line);
            if (value < 0){
                // Number of intersections, routes and yellowTime can't be
                // negative
                throw new InvalidNetworkException(
                        "Invalid negative value (" + value + ")"
                );
            }
            if (lineNumber == 3 && value == 0){
                // Yellow time cannot be 0
                throw new InvalidNetworkException(
                        "Invalid value - yellowTime cannot be 0 (" + value + ")"
                );
            }
        }
    }

    private static void addIntersections(List<String> file, Network n)
            throws InvalidNetworkException {
        // We can loop through the file until we get to a line which is not a
        // valid intersection string. This will lead to one of two cases:
        // 1) The line is an invalid string -> throw InvalidNetworkException
        // 2) The line is a route string -> Move on to the next one.
        // Let both cases be handled by the addRoute() method.
        for (int i = 3; i < file.size(); i++){
            String line = file.get(i);
            if (validIntersectionDefinition(line)){
                String intersectionID = line.split(LINE_INFO_SEPARATOR)[0];
                boolean intersectionExists = true;
                try{
                    n.findIntersection(intersectionID);
                } catch (IntersectionNotFoundException e){
                    intersectionExists = false;
                }

                if (!intersectionExists){
                    n.createIntersection(intersectionID);
                } else throw new InvalidNetworkException("Duplicate " +
                        "Intersection ID");


                System.out.println("+ intersection " + intersectionID);
            } else return;
            // Don't throw as we might have a (valid) route
            // definition. Just exit the function and let the addRoutes()
            // method handle it.
        }
    }

    /**
     * A helper method which parses the file and adds Routes (and
     * speed signs) appropriately.
     *
     * NOTE: Assuming that the first line of the file is line 1, the line
     * number on which the first route is (for a valid network) will be given
     * by (4 + i) where i is the number of intersections.
     * @param file object to parse
     * @param n    network to add to / to use for comparison
     * @throws InvalidNetworkException
     */
    private static void addRoutes(List<String> file, Network n) throws InvalidNetworkException {
        int numberOfIntersections = n.getIntersections().size();
        int start = 4 + numberOfIntersections;
        String from, to;

        for (int lineNumber = start; lineNumber <= file.size(); lineNumber++){
            String line = file.get(lineNumber - 1);
            boolean validRoute = validRouteDefinition(line);
            boolean validSensor = validSensorDefinition(line);
            boolean valid = validRoute || validSensor;

            if (valid){
                String[] components = line.split(LINE_INFO_SEPARATOR);
                if (validRoute){
                    from = components[0];
                    to  = components[1];
                    int defaultSpeed = Integer.parseInt(components[2]);

                    // Check to see whether intersections with id 'to' and
                    // 'from' exist.
                    try{
                        n.findIntersection(to);
                        n.findIntersection(from);
                    } catch (IntersectionNotFoundException e){
                        throw new InvalidNetworkException(
                                "Route is invalid as intersection(s) '" +
                                        to + "', '" + from + "' have not been " +
                                        "instantiated."
                        );
                    }

                    boolean routeExists = true;

                    try{
                        n.getConnection(from, to);
                    } catch (RouteNotFoundException e){
                        routeExists = false;
                    } catch (IntersectionNotFoundException e){
                        throw new InvalidNetworkException("Logic error");
                    }

                    if (routeExists){
                        throw new InvalidNetworkException(
                                "Route already exists from " + from + " to "
                                        + to
                        );
                    }

                    try{
                        System.out.println("+ Route from " + from + " to " + to);
                        n.connectIntersections(from, to, defaultSpeed);
                    } catch (IntersectionNotFoundException e){
                        throw new InvalidNetworkException("Logic error "
                            + e.getMessage() + " (" + from + ", " + to + ")");
                    }
                }
            }
        }
    }

    private static void addSensors(List<String> file, Network n)
            throws InvalidNetworkException {
        String from = null, to = null;

        for (String line : file){
            String[] components = line.split(LINE_INFO_SEPARATOR);

            if (validRouteDefinition(line)){
                from = components[0];
                to = components[1];
            } else if (validSensorDefinition(line)){
                String type = components[0];
                int threshold = Integer.parseInt(components[1]);
                int[] data = parseSensorData(components[2]);

                Sensor sensorToAdd;

                switch (type){
                    case "PP":
                        sensorToAdd = new DemoPressurePad(data, threshold);
                        break;
                    case "SC":
                        sensorToAdd = new DemoSpeedCamera(data, threshold);
                        break;
                    case "VC":
                        sensorToAdd = new DemoVehicleCount(data, threshold);
                        break;
                    default:
                        throw new InvalidNetworkException("Invalid sensor " +
                                "type");
                }

                boolean duplicateSensor = false;

                try{
                    List<Sensor> existingSensors =
                            n.getConnection(from, to).getSensors();

                    for (Sensor s : existingSensors){
                        String existingType =
                                s.toString().split(LINE_INFO_SEPARATOR)[0];

                        if (existingType.equals(type)){
                            // Same type as existing sensor
                            duplicateSensor = true;
                        }
                    }
                } catch (IntersectionNotFoundException | RouteNotFoundException e){
                    throw new InvalidNetworkException();
                }

                if (duplicateSensor){
                    throw new InvalidNetworkException("Duplicate " +
                            "sensor (Caught before added)");
                }

                try{
                    n.addSensor(from, to, sensorToAdd);
                    System.out.println("+ sensor            [" + from + "] ->" +
                            " [" + to + "] " + sensorToAdd.toString());
                } catch (IntersectionNotFoundException | RouteNotFoundException e){
                    throw new InvalidNetworkException();
                } catch (DuplicateSensorException e){
                    throw new InvalidNetworkException("Duplicate sensor");
                }
            }
        }
    }

    /**
     * Intersections with traffic lights should be in the format:
     * $id$ : $duration$ : $delimited$ , $order$ , $of$, $ids$
     * @param file data to iterate through
     * @param n network object
     */
    private static void addLights(List<String> file, Network n)
            throws InvalidNetworkException {
        for (int i = 3; i < file.size(); i++){
            String line = file.get(i);
            if (validIntersectionDefinition(line)){
                String[] components = line.split(LINE_INFO_SEPARATOR);
                if (components.length == 3){
                    // Is a traffic light, we have already validated the
                    // string by running it through validIntersectionDefinition.

                    List<String> order = parseLightOrder(components[2], n);
                    String id = components[0];
                    int duration = Integer.parseInt(components[1]);

                    if (duration < getYellowTime(file) + 1){
                        throw new InvalidNetworkException(
                                "Invalid duration value (" + duration + ")."
                        );
                    }

                    try{
                        n.addLights(id, duration, order);
                        System.out.println("+ TrafficLight [" + id + ", "
                                + duration + ", " + order + "].");
                    } catch (IntersectionNotFoundException
                            | InvalidOrderException e){
                        throw new InvalidNetworkException(
                                "Invalid parameters for adding traffic light ("
                                + e.getMessage() + ")"
                        );
                    }
                }
            }
        }
    }

    public static void addSpeedSign(List<String> file, Network n) throws InvalidNetworkException {
        for (int i = 3; i < file.size(); i++){
            String line = file.get(i);
            String[] components = line.split(LINE_INFO_SEPARATOR);
            if (validRouteDefinition(line) && components.length == 5){
                String from = components[0];
                String to = components[1];
                int defaultSpeed;

                try{
                    defaultSpeed = Integer.parseInt(components[4]);
                } catch (NumberFormatException e){
                    throw new InvalidNetworkException("Invalid default speed");
                }

                if (defaultSpeed < 0){
                    // Is negative
                    throw new InvalidNetworkException(
                            "Invalid default speed (" + defaultSpeed + ")"
                    );
                }


                // Has speed sign
                try{
                    n.addSpeedSign(from, to, defaultSpeed);
                } catch (IntersectionNotFoundException
                        | RouteNotFoundException e){
                    throw new InvalidNetworkException("Logic error!");
                }

            }
        }
    }

    private static void checkNumIntersections(List<String> file, Network n)
        throws InvalidNetworkException {
        int expected = getNumberOfIntersections(file);
        int actual = n.getIntersections().size();

        if (expected != actual){
            throw new InvalidNetworkException("Mismatch between number of " +
                    "intersections defined and declared");
        }
    }

    private static void checkNumRoutes(List<String> file, Network n)
            throws InvalidNetworkException {
        int expected = getNumberOfRoutes(file);
        int actual = getNumberOfInstantiatedRoutes(n);

        if (expected != actual){
            throw new InvalidNetworkException("Mismatch between number of " +
                    "intersections defined and declared");
        }
    }

    private static int getNumberOfInstantiatedRoutes(Network n){
        String networkToString = n.toString();
        String[] lines = networkToString.split(System.lineSeparator());
        int numberOfInstantiatedRoutes = 0;

        for (String line : lines){
            if (validRouteDefinition(line)){
                numberOfInstantiatedRoutes++;
            }
        }

        return numberOfInstantiatedRoutes;
    }

    public static void checkNumSensors(List<String> file, Network n)
        throws InvalidNetworkException{

        for (String line : file){
            if (validRouteDefinition(line)){
                String[] components = line.split(LINE_INFO_SEPARATOR);

                String from = components[0];
                String to = components[1];

                int numSensorsFromDefinition = Integer.parseInt(
                        components[3]
                );

                int numSensors;

                try{
                    numSensors = n.getConnection(from, to).getSensors().size();
                } catch (IntersectionNotFoundException |
                        RouteNotFoundException e){
                    throw new InvalidNetworkException("Logic error");
                }

                if (numSensors != numSensorsFromDefinition){
                    throw new InvalidNetworkException("Mismatch between " +
                            "defined number of sensors and instantiated " +
                            "number of sensors");
                } else System.out.println(
                        "Number of sensors for (" + line + ") is ("
                                + numSensors + ")"
                );
            }
        }
    }

    public static List<String> parseLightOrder(String inputString, Network n)
            throws InvalidNetworkException {
        String[] parsedOrder = inputString.split(LINE_LIST_SEPARATOR);
        List<String> order = new ArrayList<>();

        for (String intersectionID : parsedOrder){
            if (validIntersectionID(intersectionID)){

                try{
                    n.findIntersection(intersectionID);
                } catch (IntersectionNotFoundException e){
                    throw new InvalidNetworkException(
                            "Intersection cannot be found in network ("
                                    + intersectionID + ")"
                    );
                }

                order.add(intersectionID);

            } else throw new InvalidNetworkException(
                    "Invalid ID. Cannot be the ID of an intersection. ("
                            + intersectionID + ")"
            );
        }

        return order;
    }

    private static int[] parseSensorData(String inputString)
            throws InvalidNetworkException {
        String[] unformattedData = inputString.split(LINE_LIST_SEPARATOR);
        int[] data = new int[unformattedData.length];

        for (int i = 0; i < unformattedData.length; i++){
            String datum = unformattedData[i];
            if (isInteger(datum)){
                data[i] = Integer.parseInt(datum);
            } else throw new InvalidNetworkException("Data to be parsed is " +
                    "not an integer! (" + datum + ")");
        }

        return data;
    }

    private static boolean validSensorDefinition(String sensorString){
        String[] components = sensorString.split(LINE_INFO_SEPARATOR);

        // Incorrect number of delimiters, incorrect sensor definition;
        if (components.length != 3) return false;

        String type = components[0], threshold = components[1], data =
                components[2];

        boolean validType = false, validThreshold, validData = true;

        // Add to this list when creating a new sensor type;
        String[] sensorTypes = new String[] {"PP", "SC", "VC"};
        for (String sensorType : sensorTypes){
            if (type.equals(sensorType)) {
                validType = true;
                break;
            }
        }

        validThreshold = isPositive(threshold);

        for (String datum : data.split(LINE_LIST_SEPARATOR)){
            if (!isNonNeg(datum)){
                validData = false;
            }
        }

        // Is only valid if all three sections are true;
        return validType && validThreshold && validData;
    }

    /**
     *
     * @param routeString string to be validated
     * @return true if the string passed is a valid way of defining a route.
     */
    private static boolean validRouteDefinition(String routeString) {
        String[] components = routeString.split(LINE_INFO_SEPARATOR);

        if (components.length == 4 || components.length == 5){
            // Correct length
            String from = components[0];
            String to = components[1];
            if (validIntersectionID(from) && validIntersectionID(to)){
                // Now validate the default speed and number of sensors
                String unformattedDefaultSpeed = components[2];
                String unformattedNumOfSensors = components[3];

                if (isNonNeg(unformattedDefaultSpeed)
                        && isNonNeg(unformattedNumOfSensors)){
                    if (components.length == 5){
                        // Has traffic light
                        String unformattedSpeedSignSpeed = components[4];
                        if (isNonNeg(unformattedSpeedSignSpeed)){
                            // It's fine, return true
                            return true;
                        } else{
                            System.out.println("- route spd is negative");
                        }
                    } else return true;

                }

            }
        }
        return false;
    }

    private static int getNumberOfIntersections(List<String> file)
            throws InvalidNetworkException {
        int numberOfIntersections = Integer.parseInt(file.get(0));
        if (numberOfIntersections >= 0){
            return numberOfIntersections;
        } throw new InvalidNetworkException("Invalid number of intersections");
    }

    private static int getNumberOfRoutes(List<String> file){
        return Integer.parseInt(file.get(1));
    }

    private static int getYellowTime(List<String> file){
        return Integer.parseInt(file.get(2));
    }

    private static boolean validIntersectionDefinition(String intersectionStr){
        String[] components = intersectionStr.split(LINE_INFO_SEPARATOR);
        int length = components.length;
        if (length == 1 || length == 3){
            String id = components[0];
            if (validIntersectionID(id)){
                if (length == 3){
                    String duration = components[1], order = components[2];

                    if (isPositive(duration)){
                        for (String s : order.split(LINE_LIST_SEPARATOR)){
                            if (!validIntersectionID(s)){
                                return false;
                            }
                        }
                        // Case for intersection with lights
                        return true;
                    }
                    // Case for intersection without lights.
                } else return true;
            }
        }
        return false;
    }

    private static boolean validIntersectionID(String intersectionID){
        String[] forbiddenIDs = {"PP", "SC", "VC"};

        for (String forbiddenID : forbiddenIDs){
            if (intersectionID.equals(forbiddenID)){
                return false;
            }
        }

        return isNotWhitespace(intersectionID);
    }

    private static boolean isPositive(String toCompare){
        return isNonNeg(toCompare) && Integer.parseInt(toCompare) != 0;
    }

    private static boolean isNonNeg(String toCompare){
        return isInteger(toCompare) && Integer.parseInt(toCompare) >= 0;
    }

    private static boolean isInteger(String toCompare){
        try{
            Integer.parseInt(toCompare);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    /**
     * A method to determine whether a string is completely comprised of
     * whitespace characters. Whitespace characters are defined as:
     * 1) Tab character (\t)
     * 2) Space character ( )
     * 3) New line character (Given by System.lineSeparator())
     * @param str the string to compare
     * @return true if contains at least one non whitespace character. False
     * otherwise.
     */
    private static boolean isNotWhitespace(String str){
        return (str.trim().length() != 0);
    }

    private static List<String> read(String filename) throws IOException,
            InvalidNetworkException{
        List<String> file = new ArrayList<>();
        final char startOfComment = ';';
        Scanner scanner = new Scanner(new File(filename));

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.length() == 0) {
                // To avoid NPE below, check line length.
                file.add(System.lineSeparator());
            } else if (line.charAt(0) != startOfComment) {
                // Don't add lines which are comments.
                if (validIntersectionDefinition(line) ||
                    validRouteDefinition(line) ||
                    validSensorDefinition(line) ||
                    isInteger(line)){
                    file.add(line);
                } else {
                    throw new InvalidNetworkException(
                            "Invalid line (" + line + ")"
                    );
                }
            }
        }
        // Check that the only blank lines are on the last two (optionally)
        checkBlankLines(file);

        // Check that file is at least 3 lines long (length of empty network).
        if (file.size() < 3) {
            throw new InvalidNetworkException(
                    "File must be at least 3 lines long. (" + file.size()+ ") "
                    + filename
            );
        }

        return file;
    }

    /**
     * A method which checks that the only blank lines in the file are
     * (optionally) the last two lines.
     * @param file the file object to check through
     * @throws InvalidNetworkException If there are blank lines at incorrect
     * locations.
     */
    private static void checkBlankLines(List<String> file)
            throws InvalidNetworkException {
        for (int lineNumber = 1; lineNumber < file.size(); lineNumber++){
            String line = file.get(lineNumber - 1);

            if (line.equals(System.lineSeparator())){
                if (lineNumber <= file.size() - 2){
                    throw new InvalidNetworkException(
                            "Invalid Linebreak at line " + lineNumber + " " +
                                    "with file size + " + file.size()
                    );
                } else System.out.println(lineNumber + "|" + (file.size() - 2));
            }
        }
    }
}
