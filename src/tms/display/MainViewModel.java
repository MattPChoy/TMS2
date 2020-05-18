package tms.display;

import javafx.beans.property.*;
import tms.intersection.Intersection;
import tms.network.Network;
import tms.route.Route;
import tms.sensors.*;
import tms.util.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The representation of the TMS model to be displayed in the GUI.
 * @ass2_part_given View-Model code for A2.
 */
public class MainViewModel {

    //Model(s)
    private Network network;

    private StringProperty title = new SimpleStringProperty();
    private BooleanProperty pausedProperty = new SimpleBooleanProperty(true);
    private StringProperty pausedText = new SimpleStringProperty(
            "System Paused: " + pausedProperty.getValue().toString());
    private StringProperty pausedButtonText = new SimpleStringProperty("Unpause");

    private StringProperty detailsText = new SimpleStringProperty("");

    private BooleanProperty changedStructureProperty = new SimpleBooleanProperty(false);

    private IntegerProperty seconds = new SimpleIntegerProperty(0);
    private StringProperty timeElapsed = new SimpleStringProperty(
            "Seconds Elapsed: " + seconds.getValue().toString());

    private Route selectedRoute;
    private Intersection selectedIntersection;

    private BooleanProperty routeSelected = new SimpleBooleanProperty(false);
    private BooleanProperty intersectionSelected = new SimpleBooleanProperty(false);
    private BooleanProperty noSelected = new SimpleBooleanProperty(true);

    private StringProperty error = new SimpleStringProperty("");
    /** A boolean variable to indicate whether the simulation is paused. */
    private boolean paused = false;

    /**
     * Creates a model of the network to be used in the GUI.
     *
     * @param network the network to be displayed.
     * @ass2_given View-Model code for A2.
     */
    public MainViewModel(Network network) {
        this.network = network;
        title.setValue("TMS Control Portal");
    }

    /**
     * Takes in a congestion value (roughly out of 100) and converts it to a hexadecimal colour code string of format
     * "#aabb00" where aa is the hexidecimal value of the red given by min(255, congestion * 255 / 100)) and bb
     * is the hexidecimal value of the green given by max(0, 255 - congestion * 255 / 100)).
     *
     * @param congestion the congestion level
     * @return the hex code in format #00ff00
     * @ass2_given View-Model code for A2.
     */
    public static String getColor(int congestion) {
        // of format"-fx-background-color: #00ff00"
        if (congestion < 0) {
            congestion = 0;
        }
        int scaledCongestion  = congestion * 255 / 100;
        String red = String.format("%02X", Math.min(255, scaledCongestion));
        String green = String.format("%02X", Math.max(0, 255 - scaledCongestion));
        return "#" + red +  green + "00";
    }

    /**
     * Saves the current state of the network to the given file location.
     * <p>
     * If an IOException occurs when opening or writing to the file, an error
     * message should be displayed to the user.
     *
     * @param filename path of file to which the network is saved
     * @see Network#toString()
     * @ass2 View-Model code for A2.
     */
    public void save(String filename) {
        try{
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(
                            new File(filename)
                    )
            );

            out.write(
                    network.toString()
            );
            out.close();
            System.out.println("Saved file " +  filename);
        } catch (IOException e){
            error.setValue("Error accessing file.");
        }
    }

    /**
     * Gets the title property of the App Window. ("TMS Control Portal" by default)
     *
     * @return the title property of the application
     * @ass2_given View-Model code for A2.
     */
    public StringProperty getTitle() {
        return title;
    }

    /**
     * Toggles the simulation's state so that it is either paused or not.
     *
     * @ensures <code>(paused
     *          &amp;&amp; {@link MainViewModel#getPausedText()}.equals("System paused: true")
     *          &amp;&amp; {@link MainViewModel#getPausedButtonText()}.equals("Unpause"))</code><br>
     *          <code>|| (!paused
     *          &amp;&amp; {@link MainViewModel#getPausedText()}.equals("System paused: false")
     *          &amp;&amp; {@link MainViewModel#getPausedButtonText()}.equals("Pause"))</code>
     * @ass2 View-Model code for A2.
     */
    public void togglePaused() {
        if (pausedProperty.getValue()){
            // Is currently paused, so unpause the system
            pausedProperty.set(false);
            pausedButtonText.set("Unpause");
            pausedText.set("System paused: false");
        } else {
            // Is not currently paused so pause the system
            pausedProperty.set(true);
            pausedButtonText.set("Pause");
            pausedText.set("System paused: true");
        }
    }

    /**
     *
     * Tick is called by the view, approximately once per second while not paused.
     * This method invokes the {@link TimedItemManager#oneSecond()} method so
     * that it can notify all {@link tms.util.TimedItem}s.
     * <p>
     * After calling this method, {@link MainViewModel#getTimeElapsed()}
     * should return the updated time elapsed.
     *
     * @ensures <code>{@link MainViewModel#isChanged()} == true </code>
     *          <code>&amp;&amp; seconds</code> <em>property</em> {@code== \old(seconds) + 1}
     * @ass2 View-Model code for A2.
     */
    public void tick() {
        if (!pausedProperty.getValue()){
            TimedItemManager.getTimedItemManager().oneSecond();
            seconds.set(
                    seconds.getValue() + 1
            );
            timeElapsed.set("Seconds Elapsed: " + seconds.getValue().toString());
        }

    }

    /**
     * Accepts key input from the view and acts according to the key.
     * <table>
     *   <tr><td>P, p</td><td>-</td><td>Pauses or unpauses the simulation.</td></tr>
     *   <tr><td>Q, q</td><td>-</td><td>Quits the portal.</td></tr>
     *   <tr><td>S, s</td><td>-</td><td>Saves the simulation's network data to
     *           the project's root directory as "DefaultSave.txt".</td></tr>
     * </table>
     *
     * @requires input != null
     * @param input incoming input from the view.
     * @ass2 View-Model code for A2.
     */
    void accept(String input) {
        switch (input.toLowerCase()){
            case "p":
                togglePaused();
                break;
            case "q":
                System.exit(1);
                break;
            case "s":
                save("DefaultSave.txt");
                break;
        }
    }

    /**
     * Sets the selected route to be the given route.
     * <p>
     * Also sets:
     * <ul>
     * <li>intersectionSelected.value to false</li>
     * <li>routeSelected.value to true</li>
     * <li>noSelected.value to false</li>
     * <li>selectedIntersection to to</li>
     * </ul>
     * and recreates the details text.
     *
     * @param route the route to be selected
     * @param to the intersection to be selected
     * @ass2_given View-Model code for A2.
     */
    public void setSelected(Route route, Intersection to) {
        routeSelected.setValue(true);
        intersectionSelected.setValue(false);
        noSelected.setValue(false);
        selectedRoute = route;
        selectedIntersection = to;
        createDetailsText();
    }

    /**
     * Sets the selected intersection to be the given intersection.
     * <p>
     * Also sets:
     * <ul>
     * <li>intersectionSelected.value to true</li>
     * <li>routeSelected.value to false</li>
     * <li>noSelected.value to false</li>
     * <li>selectedRoute to null</li>
     * </ul>
     * and recreates the details text.
     *
     * @param intersection the intersection to select
     * @ass2_given View-Model code for A2.
     */
    public void setSelected(Intersection intersection) {
        intersectionSelected.setValue(true);
        routeSelected.setValue(false);
        noSelected.setValue(false);
        selectedRoute = null;
        selectedIntersection = intersection;
        createDetailsText();
    }

    /**
     * Gets the selected intersection.
     *
     * @return selectedIntersection
     * @ass2_given View-Model code for A2.
     */
    public Intersection getSelectedIntersection () {
        return selectedIntersection;
    }

    /**
     * Gets the selected route.
     *
     * @return selectedRoute
     * @ass2_given View-Model code for A2.
     */
    public Route getSelectedRoute () {
        return selectedRoute;
    }

    /**
     * Gets a list of intersections in the network.
     *
     * @return a list of the network's intersections
     * @ass2_given View-Model code for A2.
     */
    public List<Intersection> getIntersections() {
        return network.getIntersections();
    }

    /**
     * takes a ButtonOption and args and calls the appropriate function.
     *
     * @see MainViewModel#addConnection(String, String)
     * @see MainViewModel#addLight(String, String)
     * @see MainViewModel#addSensor(String, String)
     * @see MainViewModel#addSign(String)
     * @see MainViewModel#addTwoWayConnection(String, String)
     * @see MainViewModel#addIntersection(String)
     * @see MainViewModel#changeDuration(String)
     * @see MainViewModel#reduceIncomingSpeeds()
     * @see MainViewModel#setSpeed(String)
     * @see MainViewModel#save(String)
     * @see MainViewModel#togglePaused()
     * @param option the selected function option
     * @param args the supplied args
     * @ass2_given View-Model code for A2.
     */
    public void takeInstruction(ButtonOptions option, List<Optional<String>> args) {
        registerChange();
        for (Optional<String> arg : args) {
            if (arg.isEmpty()) {  //user cancelled
                return;
            }
        }

        try {
            switch (option) {
                case ADD_CONN:
                    addConnection(args.get(0).orElse(""), args.get(1).orElse(""));
                    break;
                case ADD_SIGN:
                    addSign(args.get(0).orElse(""));
                    break;
                case ADD_LIGHT:
                    addLight(args.get(0).orElse(""), args.get(1).orElse(""));
                    break;
                case SET_SPEED:
                    setSpeed(args.get(0).orElse(""));
                    break;
                case ADD_SENSOR:
                    addSensor(args.get(0).orElse(""), args.get(1).orElse(""));
                    break;
                case REDUCE_SPEED:
                    reduceIncomingSpeeds();
                    break;
                case ADD_INTERSECTION:
                    addIntersection(args.get(0).orElse(""));
                    break;
                case ADD_TWO_WAY_CONN:
                    addTwoWayConnection(args.get(0).orElse(""), args.get(1).orElse(""));
                    break;
                case CHANGE_LIGHT_DURATION:
                    changeDuration(args.get(0).orElse(""));
                    break;
                case SAVE:
                    save(args.get(0).orElse(""));
                case PAUSE:
                    togglePaused();
                default:
                    //error
            }
        } catch (IndexOutOfBoundsException e) {
            //wrong number of args given
            System.exit(1);
        }
        createDetailsText();
    }

    /**
     * Creates a new DemoSensor and adds it to the selected route.
     * <p>
     * Sets the value of error to:
     * <ul>
     * <li>"Error adding sensor: Invalid sensor type: [sensorType]" if
     * sensorType is not "VC", "PP", or "SC"</li>
     * <li>"Error adding sensor: Threshold must be &gt; 0" if the specified
     * threshold is less than or equal to zero.</li>
     * <li>"Error adding sensor: Data values must be &gt;= 0" if one or more of
     * the specified data values are less than zero.</li>
     * <li>"Error adding sensor: Invalid data input" if dataString is not of
     * the format: "int:int,int,...int"</li>
     * <li>"Error adding sensor: " + error message thrown by
     * {@link Network#addSensor(String, String, Sensor)} if any other
     * exception is thrown</li>
     * </ul>
     *
     * @param sensorType the type of sensor. Valid choices are "VC", "PP",
     *                   or "SC"
     * @param dataString the sensor data in format "THRESHOLD:int,int,...int"
     * @ass2_given View-Model code for A2.
     */
    public void addSensor(String sensorType, String dataString) {
        try {
            int thresh = Integer.parseInt(dataString.split(":")[0]);
            if (thresh <= 0) {
                error.setValue("Error adding sensor: Threshold must be > 0");
                return;
            }
            String[] splitData = dataString.split(":")[1].split(",");
            int[] data = new int[splitData.length];
            for (int i = 0; i < dataString.split(",").length; i++) {
                data[i] = Integer.parseInt(splitData[i].strip());
                if (data[i] < 0) {
                    error.setValue(
                            "Error adding sensor: Data values must be >= 0");
                    return;
                }
            }

            Sensor sensor;
            switch (sensorType) {
                case "PP":
                    sensor = new DemoPressurePad(data, thresh);
                    break;
                case "SC":
                    sensor = new DemoSpeedCamera(data, thresh);
                    break;
                case "VC":
                    sensor = new DemoVehicleCount(data, thresh);
                    break;
                default:
                    error.setValue(String.format(
                            "Error adding sensor: Invalid sensor type: \"%s\"",
                            sensorType));
                    return;
            }

            network.addSensor(selectedRoute.getFrom().getId(),
                    selectedIntersection.getId(), sensor);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            error.setValue("Error adding sensor: Invalid data input");
        } catch (DuplicateSensorException dse) {
            error.setValue("Error adding sensor: " + dse.getMessage());
        } catch (IntersectionNotFoundException
                | RouteNotFoundException ignored) {
            // selected route/intersection must exist
        }
    }

    /**
     * Sets the speed of the speed sign on route to a user given speed
     * <p>
     * Sets the value of error to:
     * <ul>
     * <li>"Error changing speed sign limit: Speed must be a number" if the
     * speed cannot be read as an int</li>
     * <li>"Error changing speed sign limit: " + [error message returned by
     * {@link Network#setSpeedLimit(String, String, int)}] for any other
     * exception type</li>
     * </ul>
     *
     * @param speed the new speed to set the speed sign to.
     * @ass2_given View-Model code for A2.
     */
    public void setSpeed(String speed) {
        try {
            network.setSpeedLimit(selectedRoute.getFrom().getId(),
                    selectedIntersection.getId(),
                    Integer.parseInt(speed));
        } catch (NumberFormatException e) {
            error.setValue(
                    "Error changing speed sign limit: Speed must be a number");
        } catch (IllegalStateException | IllegalArgumentException e) {
            error.setValue("Error changing speed sign limit: "
                    + e.getMessage());
        } catch (IntersectionNotFoundException
                | RouteNotFoundException ignored) {
            // selected route/intersection must exist
        }
    }

    /**
     * Reduces the incoming speed of all routes with a speed sign to either
     * the selected intersection, or the Intersection that feeds the selected
     * route as per {@link Intersection#reduceIncomingSpeedSigns()}.
     *
     * @ass2_given View-Model code for A2.
     */
    public void reduceIncomingSpeeds() {
        if (isRouteSelected().not().getValue()) {
            selectedIntersection.reduceIncomingSpeedSigns();
        } else {
            (selectedRoute.getFrom()).reduceIncomingSpeedSigns();
        }
    }

    /**
     * Creates a new speed sign on the selected Route with the given speed
     * <p>
     * Sets the value of error to:
     * <ul>
     * <li>"Error adding speed sign: Speed must be a number" if the speed
     * cannot be read as an int</li>
     * <li>"Error adding speed sign: " + [error returned by
     * {@link Network#addSpeedSign(String, String, int)}] if any other
     * exception is thrown by the Network method</li>
     * </ul>
     *
     * @param speed the speed of the new speed sign.
     * @ass2_given View-Model code for A2.
     */
    public void addSign(String speed) {
        try {
            network.addSpeedSign(selectedRoute.getFrom().getId(),
                    selectedIntersection.getId(),
                    Integer.parseInt(speed));
        } catch (NumberFormatException e) {
            error.setValue("Error adding speed sign: Speed must be a number");
        } catch (IntersectionNotFoundException
                | RouteNotFoundException ignored) {
            // selected route/intersection must exist
        } catch (IllegalArgumentException e) {
            error.setValue("Error adding speed sign: " + e.getMessage());
        }
    }

    /**
     * Adds a light to the selected intersection with the specified order
     * (default if blank).
     * <p>
     * Sets the value of error to:
     * <ul>
     * <li>"Error adding traffic lights: Duration must be a number" if the
     * duration cannot be read as an int</li>
     * <li>"Error adding traffic lights: " + [error returned by
     * {@link Network#addLights(String, int, List)} if any other exception
     * is thrown by the Network method</li>
     * </ul>
     *
     * @param duration the duration of the new light
     * @param order the order of the lights (default if blank)
     * @ass2_given View-Model code for A2.
     */
    public void addLight(String duration, String order) {
        List<String> intersectionOrder;
        if (order.isBlank()) {
            intersectionOrder = selectedIntersection.getConnectedIntersections()
                    .stream().map(Intersection::getId).collect(Collectors.toList());
        } else {
            intersectionOrder = List.of(order.split(","));
        }

        try {
            network.addLights(selectedIntersection.getId(),
                    Integer.parseInt(duration.strip()),
                    intersectionOrder);
        } catch (NumberFormatException e) {
            error.setValue(
                    "Error adding traffic lights: Duration must be a number");
        } catch (InvalidOrderException | IntersectionNotFoundException
                | IllegalArgumentException e) {
            error.setValue("Error adding traffic lights: " + e.getMessage());
        }
    }

    /**
     * Changes the duration of a traffic light at the selected intersection
     * <p>
     * Sets the value of error to:
     * <ul>
     * <li>"Error changing traffic light duration: Duration must be a number"
     * if the duration cannot be read as an int</li>
     * <li>"Error changing traffic light duration: " + [Error message thrown by
     * {@link Network#changeLightDuration(String, int)}] if any other exception
     * type is thrown</li>
     * </ul>
     *
     * @param duration the new duration to set the traffic light to
     * @ass2_given View-Model code for A2.
     */
    public void changeDuration(String duration) {
        try {
            network.changeLightDuration(selectedIntersection.getId(),
                    Integer.parseInt(duration));
        } catch (NumberFormatException e) {
            error.setValue("Error changing traffic light duration: " +
                    "Duration must be a number");
        } catch (IllegalStateException | IllegalArgumentException e) {
            error.setValue("Error changing traffic light duration: " + e.getMessage());
        } catch (IntersectionNotFoundException ignored) {
            // selected intersection must exist
        }
    }

    /**
     * Adds a connection from a user given intersection with id "id" to selected Intersection.
     * <p>
     * Sets the value of error to:
     * <ul>
     * <li>"Error adding connection: Speed must be a number" if the speed
     * cannot be read as an int</li>
     * <li>"Error adding connection: " + [Error message thrown by
     * {@link Network#connectIntersections(String, String, int)}] if the
     * networks cannot be found</li>
     * </ul>
     *
     * @param id the id of the Intersection to connect from
     * @param speed the default speed of the new route
     * @ass2_given View-Model code for A2.
     */
    public void addConnection(String id, String speed) {
        addConnectionFromAndTo(id, selectedIntersection.getId(), speed);
    }

    /**
     * Creates a new route between two intersections.
     * <p>
     * Sets the value of error to:
     * <ul>
     * <li>"Error adding connection: Speed must be a number" if the speed
     * cannot be read as an int</li>
     * <li>"Error adding connection: " + [Error message thrown by
     * {@link Network#connectIntersections(String, String, int)}] for any other
     * exception type</li>
     * </ul>
     *
     * @param from the unchecked from id
     * @param to the unchecked to id
     * @param speed the default speed of the new route
     * @ass2_given View-Model code for A2.
     */
    public void addConnectionFromAndTo(String from, String to, String speed) {
        try {
            int val = Integer.parseInt(speed.strip());
            network.connectIntersections(from.strip(),
                    to.strip(), val);
        } catch (NumberFormatException e) {
            error.setValue("Error adding connection: Speed must be a number");
        } catch (IntersectionNotFoundException | IllegalStateException
                | IllegalArgumentException e) {
            error.setValue("Error adding connection: " + e.getMessage());
        }
    }

    /**
     * Adds a connection from a selected intersection to an intersection that has the specified id
     * <p>
     * Sets the value of error to:
     * <ul>
     * <li>"Error adding connection: Speed must be a number" ifd the speed
     * cannot be read as an int</li>
     * <li>"Error adding connection: " + [Error message thrown by
     * {@link Network#connectIntersections(String, String, int)}] if the
     * networkId cannot be found</li>
     * </ul>
     *
     * @param fromId the id of the intersection to connect to
     * @param speed the default speed to be set on both new created routes
     * @ass2_given View-Model code for A2.
     */
    public void addTwoWayConnection(String fromId, String speed) {
        // Create the connection from the intersection entered by the user to
        // the selected intersection
        addConnectionFromAndTo(fromId, selectedIntersection.getId(), speed);
        addConnectionFromAndTo(selectedIntersection.getId(), fromId, speed);
    }


    /**
     * Sets changedStructureProperty's value to true to reflect that a change has occurred.
     * This typically is called after change is observed.
     *
     * @ass2_given View-Model code for A2.
     */
    public void registerChange() {
        changedStructureProperty.set(true);
        resetError();
    }

    /**
     * gets whether a change has occurred. false by default. and creates the details text.
     *
     * @return true or false depending on whether or not a change has occurred
     * @ass2_given View-Model code for A2.
     */
    public boolean isChanged() {
        createDetailsText();
        return changedStructureProperty.get();
    }

    /**
     * Sets changedStructureProperty's value to false, to reflect that no change has occurred.
     * This typically is called after change is observed and handled.
     *
     * @ass2_given View-Model code for A2.
     */
    public void notChanged() {
        changedStructureProperty.set(false);
    }

    /**
     * Resets the error message to be empty
     *
     * @ass2_given View-Model code for A2.
     */
    private void resetError() {
        error.setValue("");
    }

    /**
     * Returns the StringProperty that is either empty or an error. (empty by default)
     * <p>
     * An empty value indicated no error otherwise the value is the error message
     *
     * @return the an error message or "".
     * @ass2_given View-Model code for A2.
     */
    public StringProperty errorProperty() {
        return error;
    }

    /**
     * Returns the StringProperty that contains the text to be displayed by the time elapsed display.
     * ("Seconds Elapsed: 0" by default)
     * <p>
     * Expected value is "Seconds Elapsed: " + seconds.getValue().toString()
     *
     * @return the StringProperty that contains the text to be displayed by the time elapsed display
     * @ass2_given View-Model code for A2.
     */
    public StringProperty getTimeElapsed() {
        return timeElapsed;
    }

    /**
     * Returns the StringProperty that contains the text to be displayed by the paused/un-paused display
     * . ("System Paused: true" by default)
     * <p>
     * Valid and expected values are "System paused: false", or "System paused: true"
     *
     * @return the StringProperty that contains the text to be displayed by the paused/un-paused display
     * @ass2_given View-Model code for A2.
     */
    public StringProperty getPausedText() {
        return pausedText;
    }

    /**
     * Returns the StringProperty that contains the text to be displayed by the pause button. (Unpause by default)
     * <p>
     * Valid and expected values are "Pause", or "Unpause"
     *
     * @return the StringProperty that contains the text to be displayed by the pause button
     * @ass2_given View-Model code for A2.
     */
    public StringProperty getPausedButtonText() {
        return pausedButtonText;
    }

    /**
     * Returns a BooleanProperty that is true IF a Route button is selected. (false by default)
     *
     * @return BooleanProperty that is true IF a Route button is selected
     * @ass2_given View-Model code for A2.
     */
    public BooleanProperty isRouteSelected() {
        return routeSelected;
    }

    /**
     * Returns a BooleanProperty that is true IF an Intersection button is selected. (false by default)
     *
     * @return BooleanProperty that is true IF an Intersection button is selected
     * @ass2_given View-Model code for A2.
     */
    public BooleanProperty isIntersectionSelected() {
        return intersectionSelected;
    }

    /**
     * Returns a BooleanProperty that is true IF nothing has been selected. (true by default)
     *
     * @return the BooleanProperty that is true IF nothing has been selected
     * @ass2_given View-Model code for A2.
     */
    public BooleanProperty isNothingSelected() {
        return noSelected;
    }

    /**
     * Get the StringProperty used to show the details of the selected item.(see {@link MainViewModel#setSelected})
     * . (empty by default)
     *
     * @return the string property for the details bar text.
     * @ass2_given View-Model code for A2.
     */
    public StringProperty detailsTextProperty() {
        return detailsText;
    }

    /**
     * Creates the text to display the selected components (see
     * {@link MainViewModel#setSelected}) details and sets this text as
     * this.detailsText's value
     * <p>
     * The text should be created in the following format (where '\n'
     * represents the system line separator):
     * <p>
     * If no items is currently selected (typically because the simulation just
     * started set this.detailsText's value to "".
     * <p>
     * If an Intersection is selected:
     * <p>
     * "Intersection [INTERSECTION_ID]"<br>
     * [OPTIONAL:'Contains TrafficLight']<br>
     * "Intersection fed by: [SPACE SEPARATED LIST OF FEEDING INTERSECTIONS EG. 'A B D']"
     * <p>
     * If a Route is selected:
     * <p>
     * "Route goes from [FROM_INTERSECTION_ID]"<br>
     * "Route goes to Intersection [TO_INTERSECTION_ID]"<br>
     * [OPTIONAL:'Contains TrafficLight']<br>
     * [THREE EMPTY LINES]<br>
     * "Route from [FROM_INTERSECTION_ID] to [TO_INTERSECTION_ID]"<br>
     * "Speed: [ROUTE SPEED (as per {@link Route#getSpeed()})]"<br>
     * [OPTIONAL:'Electronic speed sign used']<br>
     * [ELSE-OPTIONAL:'Electronic speed sign NOT used']<br>
     * [OPTIONAL:'TrafficLight used. Signal:
     * [{@link tms.route.TrafficSignal#RED},
     * {@link tms.route.TrafficSignal#GREEN},
     * {@link tms.route.TrafficSignal#YELLOW}, or
     * {@link tms.route.TrafficSignal#ERROR}]']<br>
     * "Congestion: [CONGESTION (as per {@link Route#getCongestion()})"<br>
     * [OPTIONAL-IF-SENSOR:'Sensors present:']<br>
     * [FOR EACH SENSOR: 'sensor.toString()' (see {@link DemoSensor#toString()})]
     *
     * @ass2_given View-Model code for A2.
     */
    public void createDetailsText() {
        if (isNothingSelected().getValue()) {
            detailsText.setValue("");
            return;
        }
        Intersection intersection = getSelectedIntersection();
        StringBuilder intersectionDetails = new StringBuilder();
        intersectionDetails.append("Intersection ").append(
                intersection.getId()).append(System.lineSeparator());
        if (intersection.hasTrafficLights()) {
            intersectionDetails.append("Contains TrafficLight").append(System.lineSeparator());
        }

        StringBuilder routeDetails = new StringBuilder();
        if (isRouteSelected().getValue()) {
            //Display Route
            Route route = getSelectedRoute();
            routeDetails.append("Route from ").append(route.getFrom().getId()).append(" to ")
                    .append(intersection.getId()).append(System.lineSeparator());
            routeDetails.append("Speed: ").append(route.getSpeed()).append(System.lineSeparator());
            if (route.hasSpeedSign()) {
                routeDetails.append("Electronic speed sign used").append(System.lineSeparator());
            } else {
                routeDetails.append("Electronic speed sign NOT used").append(System.lineSeparator());
            } if (route.getTrafficLight() != null) {
                routeDetails.append("TrafficLight used. Signal: ").append(
                        route.getTrafficLight().getSignal()).append(System.lineSeparator());
            }
            routeDetails.append("Congestion: ").append(route.getCongestion()).append(System.lineSeparator());
            if (!route.getSensors().isEmpty()) {
                routeDetails.append("Sensors present:").append(System.lineSeparator());
                for (Sensor sensor : route.getSensors()) {
                    routeDetails.append(sensor.toString()).append(System.lineSeparator());
                }
            }
            intersectionDetails = new StringBuilder("Route goes from ").append(route.getFrom().getId())
                    .append(System.lineSeparator()).append("Route goes to ").append(intersectionDetails);
        } else { // give more intersection details
            intersectionDetails.append("Intersection fed by: ");
            for (Intersection i: intersection.getConnectedIntersections()) {
                intersectionDetails.append(i.getId()).append(" ");
            }
        }
        detailsText.setValue(intersectionDetails.toString() + System.lineSeparator()
                + System.lineSeparator() + routeDetails.toString());
    }

    /**
     * Adds a new intersection with given unique ID to the network.
     * <p>
     * If the intersection id is duplciate or otherwise illegal the textProperty this.error's value should be
     * set to 'Error adding new intersection: ' followed by the error message raised by network.
     *
     * @param newIntersectionId the user provided (non checked) string ID of the new intersection
     * @ass2_given View-Model code for A2.
     */
    public void addIntersection(String newIntersectionId) {
        registerChange();
        try {
            network.createIntersection(newIntersectionId);
        } catch (IllegalArgumentException e) {
            error.setValue("Error adding new intersection: " + e.getMessage());
        }
    }
}
