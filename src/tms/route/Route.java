package tms.route;

import tms.intersection.Intersection;
import tms.network.NetworkInitialiser;
import tms.sensors.DemoPressurePad;
import tms.sensors.Sensor;
import tms.util.DuplicateSensorException;

import java.util.*;

/**
 * Represents a one-way connection between two intersections.
 * <p>
 * All routes have a string identifier (ID), an origin intersection, a default
 * speed, a congestion calculator, up to one of each type of sensor,
 * and up to one of each type of signal (TrafficLight and SpeedSign).
 * @ass1_2
 */
public class Route {
    /** Identifier string. */
    private String id;
    /** Intersection at which this route begins. */
    private Intersection from;
    /** List of sensors on this route, limited to up to one of each type. */
    private List<Sensor> sensors;
    /** Electronic speed sign on this route, null if none exists. */
    private SpeedSign speedSign;
    /** Traffic light signal on this route, null if none exists. */
    private TrafficLight trafficLight;
    /** Speed limit of this route if no electronic speed sign exists. */
    private int defaultSpeed;

    /**
     * Creates a new route with the given ID, origin intersection and default
     * speed.
     * <p>Creates a new averaging congestion calculator if required. @ass2
     *
     * @param id the identifier string to represent the route
     * @param from the intersection from which this route originates
     * @param defaultSpeed the default speed limit for vehicles on this route
     * @ass1_2
     */
    public Route(String id, Intersection from, int defaultSpeed) {
        this.id = id;
        this.from = from;
        this.defaultSpeed = defaultSpeed;
        sensors = new ArrayList<>();

        // TODO: implement the additional logic for this method
    }

    /**
     * Returns the intersection at which this route begins.
     *
     * @return the intersection this route goes from
     * @ass1
     */
    public Intersection getFrom() {
        return this.from;
    }

    /**
     * Returns the traffic light signal on the route, or null if none exists.
     *
     * @return the TrafficLight instance deployed on the route
     * @ass1
     */
    public TrafficLight getTrafficLight() {
        return this.trafficLight;
    }

    /**
     * Returns a new list containing all the sensors on this route.
     * <p>
     * Adding/removing sensors from this list should not affect the route's
     * internal list of sensors.
     *
     * @return list of all sensors on this route
     * @ass1
     */
    public List<Sensor> getSensors() {
        return new ArrayList<>(this.sensors);
    }

    /**
     * Returns true if this route has an electronic speed sign; false otherwise.
     *
     * @return whether an electronic speed sign is present on this route
     * @ass1
     */
    public boolean hasSpeedSign() {
        return this.speedSign != null;
    }

    /**
     * Returns the currently active speed limit for vehicles on this route.
     * <p>
     * If an electronic speed sign is present, return its displayed speed.
     * Otherwise, return the default speed limit of the route.
     *
     * @return the current speed limit of the route
     * @ass1
     */
    public int getSpeed() {
        if (this.speedSign == null) {
            return defaultSpeed;
        }
        return this.speedSign.getCurrentSpeed();
    }

    /**
     * Sets the traffic signal if there is a traffic light controlling traffic
     * flow on this route.
     * <p>
     * If there is no traffic light for this route, no action should be taken.
     *
     * @param signal the traffic light signal to set
     * @ass1
     */
    public void setSignal(TrafficSignal signal) {
        if (trafficLight != null) {
            trafficLight.setSignal(signal);
        }
    }

    /**
     * Adds a TrafficLight signal to the route. It should default to RED.
     * @ass1
     */
    public void addTrafficLight() {
        trafficLight = new TrafficLight();
    }

    /**
     * Creates and adds a new electronic speed sign to this route.
     * <p>
     * If an electronic speed sign already exists on this route, it should
     * be overwritten.
     *
     * @param initialSpeed initial speed limit to be displayed on speed sign
     * @throws IllegalArgumentException if the given speed is negative
     * @ass1
     */
    public void addSpeedSign(int initialSpeed) {
        if (initialSpeed < 0) {
            throw new IllegalArgumentException("Speed sign speed must be >= 0");
        }
        this.speedSign = new SpeedSign(initialSpeed);
    }

    /**
     * Sets the speed limit of this route to the given value.
     * <p>
     * This method will only change the speed displayed on electronic speed
     * signs. If this route doesn't have a SpeedSign, throw an exception and
     * take no action.
     *
     * @param newSpeed new speed limit to be displayed on the speed sign
     * @throws IllegalStateException if the route has no electronic speed sign
     * @throws IllegalArgumentException if the given speed is negative
     * @ass1
     */
    public void setSpeedLimit(int newSpeed) {
        if (this.speedSign == null) {
            throw new IllegalStateException(
                    "Route must have electronic speed sign");
        }
        if (newSpeed < 0) {
            throw new IllegalArgumentException("Speed sign speed must be >= 0");
        }
        this.speedSign.setCurrentSpeed(newSpeed);
    }

    /**
     * Adds a sensor to the route if a sensor of the same type is not already
     * on the route.
     *
     * @param sensor the sensor to add to the route
     * @throws DuplicateSensorException if the sensor to add is of the same
     * type as a sensor deployed on this route
     * @ass1
     */
    public void addSensor(Sensor sensor) throws DuplicateSensorException {
        for (Sensor s : sensors) {
            if (s.getClass().equals(sensor.getClass()) ) {
                throw new DuplicateSensorException(
                        "Duplicate sensor of type: \""
                                + s.getClass().getSimpleName() + "\"");
            }
        }
        sensors.add(sensor);
    }

    /**
     * Returns the string representation of this route.
     * <p>
     * The format of the string to return is "id:defaultSpeed:numberOfSensors",
     * where 'id' is our identifier string, 'defaultSpeed' is the default speed
     * of this route, and 'numberOfSensors' is the number of sensors of all
     * types currently on this route.
     * <p>
     * If this route has a SpeedSign, then the format to be returned should
     * instead be "id:defaultSpeed:numberOfSensors:speedSignSpeed" where
     * 'speedSignSpeed' is the current speed limit indicated on the speed sign.
     * <p>
     * If this route has any sensors, the format to be returned should be the
     * same as above, with an additional line for information pertaining to
     * each sensor on the route. The order in which these lines appear
     * should be alphabetical, meaning a line for a pressure plate (PP) should
     * come before a line for a speed camera (SC).
     * <p>
     * Each sensor line should contain that sensor's string representation as
     * returned by its specific toString method, e.g.
     * {@link DemoPressurePad#toString()}.
     * <p>
     * Note: {@link java.lang.System#lineSeparator()} should be used to
     *       separate lines.
     *
     * @return the formatted string representation
     * @ass1
     */
    @Override
    public String toString() {
        String str = String.format("%s%s%d%s%d",
                this.id, NetworkInitialiser.LINE_INFO_SEPARATOR,
                this.defaultSpeed, NetworkInitialiser.LINE_INFO_SEPARATOR,
                this.sensors.size());

        if (this.speedSign != null) {
            str += NetworkInitialiser.LINE_INFO_SEPARATOR
                    + this.speedSign.getCurrentSpeed();
        }

        String[] sensorLines = this.sensors.stream().map(Object::toString)
                .sorted().toArray(String[]::new);
        for (String sensorLine : sensorLines) {
            str += System.lineSeparator() + sensorLine;
        }
        return str;
    }

    /**
     * Get the congestion level reported by sensors on this route.
     *
     * By default, an AveragingCongestionCalculator should be used to find the
     * route's congestion level. See CongestionCalculator.calculateCongestion().
     *
     * @return the congestion level on this route as returned by the calculator
     */
    public int getCongestion(){
        return 0;
        // TODO implement the logic for this method
    }

    /**
     * Returns true if and only if this route is equal to the other given route.
     *
     * For two routes to be equal they must:
     * 1) have the same identifier string // done
     * 2) have the same default speed // done
     * 3) either both have a traffic light or both have no traffic signal
     * 4) have the same traffic light signal status (GREEN, RED etc)
     * 5) either both have an electronic speed sign or both have no electronic
     *    speed sign
     * 6) have the same electronic speed sign
     * 7) have the same sensors (comparison should make use of each sensor's
     *    equals() method, directly or indirectly).
     *
     * Overrides equals in class Object
     *
     * @param obj other object to compare equality
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Route)){
            return false;
        }

        Route other = (Route) obj; // Can convert now we know that it is
        // Route

        return (compareParameters(this, other)
                && compareParameters(this, other)
                && compareTrafficLights(this, other)
                && compareSignAndSensors(this, other));
    }

    /**
     * Returns the hash code of this route.
     *
     * Two routes that are equal must have the same hash code.
     *
     * Overrides hashCode in class Object
     *
     * @return has code of the route
     */
    @Override
    public int hashCode(){
        return 0;
    }

    /**
     * First section of testing: comparing toString, speed sign, sensors
     * @param a first route to compare for equality
     * @param b second route to compare for equality
     * @return whether routes are equal or not using these suite of tests.
     */
    public static boolean compareParameters(Route a, Route b){
        if (a.toString().equals(b.toString())){
            if (a.hasSpeedSign() == b.hasSpeedSign()){
                List<Sensor> aSensors = a.getSensors();
                List<Sensor> bSensors = b.getSensors();

                if (compare(aSensors, bSensors)){
                    // If the sensor lists are permutations
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Second section of testing: comparing they both have or don't have
     * traffic lights.
     * If they both have traffic lights then the signal must be the same.
     * @param a first route to compare for equality
     * @param b second route to compare for equality
     * @return whether routes are equal or not using these suite of tests.
     */
    public static boolean compareTrafficLights(Route a, Route b){
        boolean bothHave = a.getTrafficLight() != null
                && b.getTrafficLight() != null;
        boolean bothDontHave = a.getTrafficLight() == null &&
                b.getTrafficLight() == null;

        if (bothHave){
            TrafficLight aLight = a.getTrafficLight();
            TrafficLight bLight = b.getTrafficLight();
            return aLight.getSignal() == bLight.getSignal();

        } else return bothDontHave;
    }

    /**
     * Third section of testing: comparing speed signs and sensors.
     * If they have sensors, then the list of sensors must be permutations of
     * each other.
     * @param a first route to compare for equality
     * @param b second route to compare for equality
     * @return whether routes are equal or not using these suite of tests.
     */
    public static boolean compareSignAndSensors(Route a, Route b){
        if (compare(a.getSensors(), b.getSensors())){
            if ((a.hasSpeedSign() && b.hasSpeedSign())){
                return a.getSpeed() == b.getSpeed();
                // @1160 on Piazza
            } else return !a.hasSpeedSign() && !b.hasSpeedSign();
        }
        return false;
    }


    public static boolean compare(List<Sensor> a, List<Sensor> b){
        List<Sensor> firstList = a;
        List<Sensor> secondList = b;

        for(int i = 0; i < 2; i++){

            if (i != 0){
                firstList = b;
                secondList = a;
            }

            for (Sensor firstSensor : firstList){
                boolean found = false;

                for (Sensor secondSensor : secondList){
                    if (firstSensor.equals(secondSensor)){
                        found = true;
                    }
                }

                if (!found){
                    return false;
                }
            }
        }
        return true;
    }

}
