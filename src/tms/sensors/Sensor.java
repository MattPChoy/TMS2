package tms.sensors;

/**
 * A device used to detect congestion by comparing observed traffic flow
 * measures to a predefined threshold value. This interface will need to be
 * extendable.
 * @ass1
 */
public interface Sensor {

    /**
     * Returns the level of congestion as detected by this sensor.
     * <p>
     * A value of 0 indicates no congestion and 100 indicates maximum
     * congestion.
     *
     * @return congestion levels present at the sensor, 0 to 100
     * @ass1
     */
    int getCongestion();

    /**
     * Returns the level below/above which observed data indicates congestion
     * is occurring on a route.
     * <p>
     * The exact meaning of the threshold differs per sensor implementation.
     *
     * @return the threshold value
     * @ass1
     */
    int getThreshold();
}
