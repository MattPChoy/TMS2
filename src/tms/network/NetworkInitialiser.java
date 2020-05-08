package tms.network;

import tms.intersection.Intersection;
import tms.route.Route;
import tms.sensors.DemoPressurePad;
import tms.sensors.DemoSpeedCamera;
import tms.sensors.DemoVehicleCount;
import tms.sensors.Sensor;
import tms.util.*;

import java.io.*;
import java.util.*;

public class NetworkInitialiser {
    // Delimiter used to separate individual pieces of data on a single line
    public static final String LINE_INFO_SEPARATOR = ":";
    // Delimiter used to separate individual elements in a variable-length list
    // on a single line
    public static final String LINE_LIST_SEPARATOR = ",";
    // TODO: Update the value of the LINE_INFO_SEPARATOR, LINE_LIST_SEPARATOR;


    public NetworkInitialiser() {

    }

    /**
     * Loads a saved Network from the file with the given filename.
     * <p>
     * Network files have the following structure. Square brackets indicate that
     * the data inside them is optional. For example, a route does not
     * necessarily need a speed sign (speedSignSpeed).
     * <p>
     * See the demo network for an example (demo.txt).
     * <p>
     * ; This is a comment. It should be ignored.
     * numIntersections
     * numRoutes
     * yellowTime
     * intersectionId[:duration:sequence,of,intersection,ids]
     * ... (more intersections)
     * intersectionFromId:intersectionToId:defaultSpeed:numSensors[:speedSignSpeed]
     * SENSORTYPE:threshold:list,of,data,values
     * ... (more routes and sensors)
     * <p>
     * A network file is invalid if any of the following conditions are true:
     * <p>
     * The number of intersections specified is not equal to the number of
     * intersections read from the file.
     * The number of routes specified does not match the number read from
     * the file.
     * The number of sensors specified for a route does not match the number
     * read from the line below.
     * An intersection referenced by another intersection does not exist,
     * or no route exists between them.
     * Two or more intersections have the same identifier string.
     * Two or more routes have the same starting and ending intersections.
     * A sensor type that is not one of the three provided demo sensors.
     * A route contains sensors of the same type.
     * The traffic light yellow time is less than one (1).
     * A traffic light duration is less than the traffic light yellow time
     * plus one (1).
     * Any numeric value that should be positive is less than zero. This
     * includes:
     * route speeds
     * speed sign speeds
     * sensor thresholds (also, cannot be zero)
     * sensor data values
     * The colon-delimited format is violated, i.e. there are more/fewer
     * colons than expected.
     * Any numeric value fails to be parsed.
     * The file contains any more than two (2) newline characters at the
     * end of the file.
     *
     * @param fileName name of the file from which to load the network
     * @return the Network loaded from the file
     * @throws IOException             any IOExceptions encountered when reading the file
     *                                 are bubbled up
     * @throws InvalidNetworkException if the file format of the given file
     *                                 is invalid.
     */
    public static Network loadNetwork(String fileName) throws IOException,
            InvalidNetworkException {

        Network network = new Network();
        List<String> file = read(fileName); // Parse the file as a string
        // list so that we can manipulate it. Each element of the variable
        // represents a new line in the text file.

        // Populate the constants
        int numberOfIntersections = getIntersectionCount(file);
        int numberOfRoutes = getRouteCount(file);
        int yellowTime = getYellowTime(file);

        addIntersections(file, network);
        System.out.println("===== AddRoutes() =====");
        addRoutes(file, network);

        System.out.println("===== Network.toString() =====");
        System.out.println(
                network.toString()
        );
        System.out.println("==============================");

        return network;
    }

    public static List<String> read(String filename)
            throws IOException, InvalidNetworkException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);

        List<String> output = new ArrayList<>(); // The list representation of the
        // text file with comments removed.

        while (scanner.hasNextLine()){
            String line = scanner.nextLine();

            if (line.length() == 0){
                output.add(line);
            } else if (line.charAt(0) != ';'){
                output.add(line);
            }
        }

        checkEOFBlankLines(output);

        return output;
    }

    /**
     * A method to check that there are less than 2 blank lines at EOF.
     * @param file list representation of file
     * @throws InvalidNetworkException if there are more than 2 blank lines
     * at EOF.
     */
    public static void checkEOFBlankLines(List<String> file)
            throws InvalidNetworkException{
        int numberOfBlankLinesAtEOF = 0;
        boolean current = true;

        for (int i = file.size() - 1; i > 0; i--){
            if (file.get(i).length() == 0 && current){
                numberOfBlankLinesAtEOF++;
            } else {
                current = false;
            }
        }

        if (numberOfBlankLinesAtEOF > 1){
            throw new InvalidNetworkException(
                    "Too many blank lines at the end of the file: "
                            + numberOfBlankLinesAtEOF + " blank lines."
            );
        }
    }

    /**
     * A method to parse the file list and return the number of intersections
     * specified in the file. (Line 0)
     * @param file the String list representation of the file
     * @return number of intersections (int)
     * @throws InvalidNetworkException if number of routes cannot be parsed
     *      * as an integer.
     */
    public static int getIntersectionCount(List<String> file)
        throws InvalidNetworkException{
        int numberOfIntersections = 0;

        try{
            numberOfIntersections = Integer.parseInt(file.get(0));
        } catch (NumberFormatException e){
            throw new InvalidNetworkException("Cannot parse line 0 as an " +
                    "integer.");
        }

        return numberOfIntersections;
    }

    /**
     * A method to parse the file list and return the number of routes
     * specified in the file. (Line 1)
     * @param file the String list representation of the file
     * @return number of routes (int)
     * @throws InvalidNetworkException if number of routes cannot be parsed
     * as an integer.
     */
    public static int getRouteCount(List<String> file)
            throws InvalidNetworkException{
        int numberOfRoutes = 0;

        try{
            numberOfRoutes = Integer.parseInt(file.get(1));
        } catch (NumberFormatException e){
            throw new InvalidNetworkException("Cannot parse line 1 as an " +
                    "integer.");
        }

        return numberOfRoutes;
    }

    /**
     * A method to parse the file list and return the network's specified
     * yellow time (Line 3)
     * @param file the String list representation of the file.
     * @return yellowTime of route (in seconds, as an integer)
     * @throws InvalidNetworkException if yellow time is invalid, or cannot
     * be parsed as an integer.
     */
    public static int getYellowTime(List<String> file) throws InvalidNetworkException {
        int yellowTime = 0;

        try{
            yellowTime = Integer.parseInt(file.get(2));
        } catch (NumberFormatException e){
            throw new InvalidNetworkException("Cannot parse line 1 as an " +
                    "integer.");
        }

        if (yellowTime < 1){
            throw new InvalidNetworkException("Invalid yellow time");
        }

        return yellowTime;
    }

    /**
     * A method to parse the file list and return the details of the
     * required intersection objects.
     * @param file the string representation of the data file
     * @param network the network object for which to add the intersections
     * @throws InvalidNetworkException if id is invalid, or as thrown by any
     * respective method.
     */
    public static void addIntersections(List<String> file, Network network)
            throws InvalidNetworkException {
        int numberOfIntersections = getIntersectionCount(file);
        int numberOfLines = file.size() - 1;
        boolean current = true;
        List<String> trafficLightMetadata = new ArrayList<>();

        for (int lineNumber = 3; lineNumber < numberOfLines; lineNumber++){
            String line = file.get(lineNumber);
            int numberOfColons = countDelimiterInstances(line,
                    LINE_INFO_SEPARATOR);
            if (numberOfColons == 0){
                // Network by itself.
                System.out.println("+ intersection " + line);
                network.createIntersection(line);

            }
            else if (numberOfColons == 2 && current){
                String intersectionID = line.split(LINE_INFO_SEPARATOR)[0];
                if (isValidIntersectionID(intersectionID)){
                    // Not a sensor
                    System.out.println("+ intersection " + intersectionID);
                    network.createIntersection(intersectionID);
                    trafficLightMetadata.add(line);
                } else throw new InvalidNetworkException(
                        "The intersection ID provided, '" + intersectionID +
                                "' is invalid."
                );
                // TODO confirm that intersectionID!= SC, VC, PP

            } else current = false; // Intersections must be grouped together.
        }

        // Now test that the number of intersections match
        if (numberOfIntersections != network.getIntersections().size()){
            throw new InvalidNetworkException("Number of intersections " +
                    "declared does not match text file.");
        }
    }

    public static void addTrafficLights(List<String> trafficLightMetadata,
                                        Network network)
            throws InvalidNetworkException {


        // Now create the traffic lights
        for (String data : trafficLightMetadata){
            String[] metadata = data.split(LINE_INFO_SEPARATOR);
            String intersectionID = metadata[0];
            int duration = getTrafficLightDuration(metadata);
            Intersection target = getIntersection(network, intersectionID);
            List<String> order = getTrafficLightOrder(metadata);

            try{
                network.addLights(intersectionID, duration, order);
            } catch (InvalidOrderException e){
                throw new InvalidNetworkException("Invalid order parameter " +
                        order.toString() + " [" + e.getMessage() + "].");
            } catch (IntersectionNotFoundException e){
                throw new InvalidNetworkException("Could not find " +
                        "intersection" + intersectionID);
            }
        }
    }

    /**
     * A method which returns a list of intersections used for instantiating
     * a traffic light on a given route.
     * @param metadata the data containing the string IDs of the intersections.
     */
    public static List<String> getTrafficLightOrder(String[] metadata){
        return new ArrayList<>(
                Arrays.asList(
                        metadata[2].split(LINE_LIST_SEPARATOR)
                )
        );
    }

    /**
     * A method that counts the instances of any delimiter character.
     * @param string the string for which to compare
     * @param delimiter the delimiter to count the instances of
     * @returns number of occurrences of the delimiter in the string.
     */
    public static int countDelimiterInstances(String string, String delimiter){
        return (string.length() - string.replace(
                delimiter, "").length());
    }

    /**
     * A method that wraps the Network.getIntersection() method and recasts
     * the IntersectionNotFound exception as an InvalidNetworkException. This
     * should never be called unless there is a logical error in the code
     * that I have written.
     * @return Intersection target.
     * @throws InvalidNetworkException when an intersection that should have
     * been instantiated isn't
     */
    public static Intersection getIntersection(Network network, String id)
            throws InvalidNetworkException {
        Intersection target;

        try{
            target = network.getIntersection(id);
        } catch (IntersectionNotFoundException e){
            throw new InvalidNetworkException("Target does not match. " +
                    "Logical error.");
        }

        if (target == null){
            throw new InvalidNetworkException("Target is null. Logical error.");
        }

        return target;

    }

    /**
     * A method to get the duration of the traffic light from traffic light
     * metadata (Line split using LINE_INFO_SEPARATOR as the delimiter)
     * @param metadata the metadata to process
     */
    public static int getTrafficLightDuration(String[] metadata) throws InvalidNetworkException {
        try{
            return Integer.parseInt(metadata[1]);
        } catch (NumberFormatException e){
            throw new InvalidNetworkException(
              "Intersection " + metadata[0] + " has invalid duration " + metadata[1]
            );
        }
    }

    /**
     * A method to parse the text file and add routes to the network. Note
     * that this method also adds sensors to the route.
     *
     * @param file the string representation of the data file to pass
     * @param network the network to which to add the routes.
     */
    public static void addRoutes(List<String> file,
                                 Network network) throws InvalidNetworkException {

        List<Integer> rangeToIterate = getRange(file, SectionName.ROUTES);
        int lowerBound = rangeToIterate.get(0);
        int upperBound = rangeToIterate.get(1);

        validateRouteCount(file);
        Route previous = null;
        String from = null, to = null; // Initialise it here so it can be used
        // by the sensors section but not be cleared for every line.

        for (int i = lowerBound; i < upperBound; i++){
            String line = file.get(i);
            String[] splits = line.split(LINE_INFO_SEPARATOR);


            if (isRouteString(line)){
                from = splits[0];
                to = splits[1];
                int defaultSpeed = Integer.parseInt(splits[2]);
                try{
                    network.getIntersection(from);
                    network.getIntersection(to);
                } catch (IntersectionNotFoundException e){
                    throw new InvalidNetworkException("Intersection cannot be" +
                            " found.");
                }

                try{
                    network.connectIntersections(from, to, defaultSpeed);
                } catch (IntersectionNotFoundException e){
                    throw new InvalidNetworkException("Logic error!");
                }
                System.out.println("+ route " + from +
                        ":" + to + ":" + defaultSpeed);
                try {
                    network.getRoute(from, to);
                } catch (IntersectionNotFoundException
                        | RouteNotFoundException e){
                    throw new InvalidNetworkException("Logic error!");
                }
            }

            if (isSensorString(line)){
                String type = splits[0];
                int threshold = Integer.parseInt(splits[1]);
                String dataString = splits[2];
                int[] data = parseDelimitedString(dataString,
                        LINE_LIST_SEPARATOR);
                Sensor sensor;


                switch (type) {
                    case "PP":
                        sensor = new DemoPressurePad(
                                data, threshold
                        );
                        break;
                    case "SC":
                        sensor = new DemoSpeedCamera(
                                data, threshold
                        );
                        break;
                    case "VC":
                        sensor = new DemoVehicleCount(
                                data, threshold
                        );
                        break;
                    default:
                        throw new InvalidNetworkException("Logic error!");
                }

                try {
                    network.addSensor(from, to, sensor);
                } catch (DuplicateSensorException e) {
                    throw new InvalidNetworkException(
                            "Sensor " + type + " already exists");
                } catch (IntersectionNotFoundException e) {
                    throw new InvalidNetworkException(
                            "Either " + from + " or " + to + " " +
                                    "(intersections) could not be found"
                    );
                } catch (RouteNotFoundException e) {
                    throw new InvalidNetworkException(
                        "Route " + from + ":" + to + "(intersections)" +
                                    "could not be found"
                    );
                }

                System.out.println("+ " + type + ":" + threshold + dataString);

            }



        }
    }

    /**
     * A method to validate that the number of declared routes matches the
     * number of routes defined in the text file.
     * @param file the string representation of the text file.
     * @throws InvalidNetworkException if number of declared routes does not
     * match the number of routes defined in the text file.
     */
    public static void validateRouteCount(List<String> file)
            throws InvalidNetworkException{
        int numberOfRoutes = 0;
        for (SectionName sectionName : getLineCategories(file)){
            if (sectionName == SectionName.ROUTES){
                numberOfRoutes++;
            }
        }

        if (numberOfRoutes != getRouteCount(file)){
            throw new InvalidNetworkException("Number of routes does not " +
                    "match : " + numberOfRoutes + " | " + getRouteCount(file));
        }
    }

    /**
     * A method which iterates through the datafile
     * @param file the string representation of the textfile.
     * @return a list containing the "category" enum of each line
     * @ensures lineCategories.size() == file.size();
     */
    public static List<SectionName> getLineCategories(List<String> file)
            throws InvalidNetworkException {
        List<SectionName> lineCategories = new ArrayList<>();

        int numberOfLinesInFile = file.size() - 1;

        for (int lineNum = 0; lineNum <= numberOfLinesInFile; lineNum++){
            String line = file.get(lineNum);

            if (isInteger(line)){
                lineCategories.add(SectionName.CONSTANTS);
            } else if (isIntersectionString(line)){
                lineCategories.add(SectionName.INTERSECTIONS);
            } else if (isRouteString(line)){
                lineCategories.add(SectionName.ROUTES);
            } else if (isSensorString(line)){
                lineCategories.add(SectionName.SENSORS);
            } else{
                throw new InvalidNetworkException("Line does not match any " +
                        "known format");
            }
        }

        return lineCategories;
    }

    /**
     * A method to find the bounds of each partition
     */
    public static List<Integer> getRange(List<String> file,
            SectionName name) throws InvalidNetworkException {

        List<SectionName> lineCategories = getLineCategories(file);

        int numberOfLinesInFile = file.size() - 1;

        List<Integer> range = new ArrayList<>();
        range.add(-1); // Start
        range.add(-1); // End
        boolean current = true;

        switch (name){
            case CONSTANTS:
//                System.out.println("Finding constants range");
                range.set(0,0);
                range.set(1,2);
                break;
            case INTERSECTIONS:
//                System.out.println("Finding intersections range");
                range.set(0,3); // Lower bound
                for (int i = 3; i < numberOfLinesInFile; i++){
                    SectionName sectionName = lineCategories.get(i);
                    if (sectionName != SectionName.INTERSECTIONS){
                        range.set(1, i-1); // Set to the previous
                        break;
                    }
                }
                break;
            case ROUTES:
//                System.out.println("Finding routes range");
                int startingIndex =
                        getRange(file, SectionName.INTERSECTIONS).get(1) + 1;
                range.set(0, startingIndex); // Lower bound
                for (int i = startingIndex; i < numberOfLinesInFile; i++){
                    SectionName sectionName = lineCategories.get(i);
//                    System.out.println(file.get(i));
                    if (sectionName != SectionName.ROUTES &&
                        sectionName != SectionName.SENSORS){
                        range.set(1, i-1); // Set to the previous
                        break;
                    }
                }
                if (range.get(1) == - 1){
                    // We got to the end and it's all valid;
                    range.set(1, numberOfLinesInFile);
                }
                break;

        }

        return range;
    }

     enum SectionName {
        CONSTANTS,
        INTERSECTIONS,
        ROUTES,
        SENSORS
    }

    /**
     * A method to determine whether a given input string is a valid format
     * for a sensor. Note that it does not check whether the value of fromID
     * or toID is a valid route string.
     * @param input the input string to compare
     * @return true if string could be an intersection, false if there is a
     * semantic error with the string.
     */
    public static boolean isSensorString(String input){
        int delimiterInstances = countDelimiterInstances(input,
                LINE_INFO_SEPARATOR);

        if (delimiterInstances != 2){
//            System.out.println("Incorrect number of delimiters - " +
//                    delimiterInstances + " '" + input + "'");
            return false;
        }

        int inputLength = input.length() - 1;
        if (input.substring(0,1).equals(LINE_INFO_SEPARATOR)
                || input.substring(inputLength, inputLength+1).equals(LINE_INFO_SEPARATOR)
                || input.contains(LINE_INFO_SEPARATOR + LINE_INFO_SEPARATOR)){
            // If the string's first or last character is the delimiter
            // Or if there are two consecutive delimiters, we know that
            // one parameter is empty
//            System.out.println("Compartment empty");
            return false;
        }

        String[] splits = input.split(LINE_INFO_SEPARATOR);

        String sensorType = splits[0];
        String threshold = splits[1];
        String dataValues = splits[2];

        if (!sensorType.equals("SC")
                && !sensorType.equals("PP")
                && !sensorType.equals("VC")){
//            System.out.println("Invalid sensor type");
            return false;
        }

        if (!isInteger(threshold)){
//            System.out.println("Threshold value " + threshold + " not an " +
//                    "integer");
            return false;
        }

        if (Integer.parseInt(threshold) < 0){
//            System.out.println("Threshold value " + threshold + " is negative");
            return false;
        }

        String[] data = dataValues.split(LINE_LIST_SEPARATOR);

        for (String datum : data){
            try{
                Integer.parseInt(datum);
            } catch (NumberFormatException e){
//                System.out.println("Data point " + datum + " is not an " +
//                        "integer");
                return false;
            }

            if (Integer.parseInt(datum) < 0){
//                System.out.println("Data point " + datum + " is negative");
                return false;
            }
        }

        return true;
    }

    public static int[] parseDelimitedString(String input, String delimiter) throws InvalidNetworkException {
        String[] processedInput = input.split(delimiter);
        int numberOfInputs = processedInput.length;

        int[] output = new int[numberOfInputs];

        for (int i = 0; i <numberOfInputs - 1; i++){
            String datum = processedInput[i];

            if (isInteger(datum)){
                output[i] = Integer.parseInt(processedInput[i]);
            } else {
                throw new InvalidNetworkException("Character at " + i + ", " +
                        processedInput[i] + " is not an integer");
            }
        }

        return output;
    }

    /**
     * A method to determine whether a given input string is a valid format
     * for a route. Note that it does not check whether the value of fromID
     * or toID is a valid route string.
     * @param input the input string to compare
     * @return true if string could be an intersection, false if there is a
     * semantic error with the string.
     */
    public static boolean isRouteString(String input){
        int delimiterInstances = countDelimiterInstances(input,
                LINE_INFO_SEPARATOR);

        if (delimiterInstances == 3 || delimiterInstances == 4) {
            // fromID : toID : defaultSpeed : numSensors

            String[] splits = input.split(LINE_INFO_SEPARATOR);
            int inputLength = input.length() - 1;
            if (input.substring(0,1).equals(LINE_INFO_SEPARATOR)
                    || input.substring(inputLength, inputLength+1).equals(LINE_INFO_SEPARATOR)
                    || input.contains(LINE_INFO_SEPARATOR + LINE_INFO_SEPARATOR)){
                // If the string's first or last character is the delimiter
                // Or if there are two consecutive delimiters, we know that
                // one parameter is empty
//                System.out.println("Compartment empty");
                return false;
            }

            String defaultSpeed = splits[2];
            String numSensors = splits[3];

            if (!isInteger(defaultSpeed)) {
//                System.out.println("Non-integer default speed " + defaultSpeed);
                return false;
            }

            if (Integer.parseInt(defaultSpeed) < 0){
//                System.out.println("Default speed " + defaultSpeed + "is " +
//                        "negative");
                return false;
            }

            if (!isInteger(numSensors)){
//                System.out.println("Non-integer number of sensors "
//                        + numSensors);
                return false;
            }

            if (delimiterInstances == 4){
                String speedSignSpeed = splits[4];
                if (!isInteger(speedSignSpeed)){
//                    System.out.println("SpeedSignSpeed not integer");
                    return false;
                }
                if ((Integer.parseInt(speedSignSpeed) < 0)){
                    // If negative
//                    System.out.println("SpeedSignSpeed invalid bounds"
//                            + speedSignSpeed);
                    return false;
                }

                // If the speed sign speed is positive or negative.
            }

            String fromID = splits[0];
            String toID = splits[1];

            if (!(isValidIntersectionID(fromID)
                    && isValidIntersectionID(toID))){
//                System.out.println("Invalid to or from id" + fromID + "|" + toID);
                return false;
            }

            return true;
        }
//        System.out.println("Invalid number of delimiters " + delimiterInstances);
        return false;
    }


    /**
     * A method to determine whether a given input string is a valid format
     * for an intersection. Note that it does not check whether the traffic
     * lights order parameter contains intersections that exist.
     * @param input the input string to compare
     * @return true if string could be an intersection, false if there is a
     * semantic error with the string.
     */
    public static boolean isIntersectionString(String input){
        int delimiterInstances = countDelimiterInstances(input,
                LINE_INFO_SEPARATOR);

        if (delimiterInstances == 0){
            // Just intersectionID by itself, e.g. 'X'
            //return isValidIntersectionID(input);

            if (isValidIntersectionID(input)){
//                System.out.println(input + " - Valid intersection ID for single case");
                return true;
            } else{
//                System.out.println(input + " - Invalid because of single id");
                return false;
            }

        }
        if (delimiterInstances == 2){

            // Check if delimiters are empty.
            int inputLength = input.length() - 1;

            if (input.substring(0,1).equals(LINE_INFO_SEPARATOR)
                || input.substring(inputLength, inputLength+1).equals(LINE_INFO_SEPARATOR)
                || input.contains(LINE_INFO_SEPARATOR + LINE_INFO_SEPARATOR)){
                // If the string's first or last character is the delimiter
                // Or if there are two consecutive delimiters, we know that
                // one parameter is empty
//                System.out.println("Compartment empty");
                return false;
            }

            String duration = input.split(LINE_INFO_SEPARATOR)[1];
            if (!isInteger(duration)) {
//                System.out.println("Duration of " + duration + " is invalid");
                return false;
            }
            String intersectionID = input.split(LINE_INFO_SEPARATOR)[0];
            if (!isValidIntersectionID(intersectionID)){
//                System.out.println("Invalid intersection id " + intersectionID);
                return false;
            }

            return true;
        }
//        System.out.println(input + " - Invalid because number of delimiter " +
//                "characters - " + delimiterInstances);
        return false; // If the number of delimiter instances does not match;
    }

    public static boolean isValidIntersectionID(String ID){
        return !ID.equals("PP") &&
                !ID.equals("SC") &&
                !ID.equals("VC") &&
                ID.length() != 0; // TODO @1232
    }

    public static boolean isInteger(String input){
        try{
            Integer.parseInt(input);
        } catch (NumberFormatException e){
            return false;
        }

        return true;
    }
}
