package tms.route;

import tms.intersection.Intersection;
import tms.sensors.DemoPressurePad;
import tms.sensors.Sensor;
import tms.util.DuplicateSensorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
}
