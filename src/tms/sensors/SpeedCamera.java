package tms.sensors;

/**
 * A type of sensor that measures the average speed of vehicles travelling
 * along a route.
 * @ass1
 */
public interface SpeedCamera extends Sensor {

    /**
     * Returns the observed average speed of vehicles travelling past
     * this sensor in km/h.
     * <p>
     * If there are no vehicles whose speed to measure, returns 0.
     *
     * @return the current average speed in km/h reported by the speed camera
     * @ass1
     */
    int averageSpeed();
}
