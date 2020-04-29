package tms.sensors;

/**
 * A type of sensor that measures the number of vehicles waiting at a fixed
 * point on the route.
 * @ass1
 */
public interface PressurePad extends Sensor {

    /**
     * Returns the number of vehicles currently waiting on the pressure pad.
     *
     * @return the current traffic count reported by the pressure pad
     * @ass1
     */
    int countTraffic();
}
