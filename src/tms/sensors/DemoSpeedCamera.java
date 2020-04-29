package tms.sensors;

/**
 * An implementation of a speed camera sensor.
 * @ass1
 */
public class DemoSpeedCamera extends DemoSensor implements SpeedCamera {

    /**
     * Creates a new speed camera sensor with the given threshold and data.
     *
     * @see DemoSensor#DemoSensor(int[], int)
     * @param data a non-empty array of data values
     * @param threshold a threshold value that indicates which values represent
     *                  high congestion
     * @ass1
     */
    public DemoSpeedCamera(int[] data, int threshold) {
        super(data, threshold);
    }

    /**
     * {@inheritDoc}
     * @ass1
     */
    @Override
    public int averageSpeed() {
        return this.getCurrentValue();
    }

    /**
     * Calculates the congestion rate as the complement of the percentage given
     * by {@code averageSpeed()} divided by {@code getThreshold()}.
     * <p>
     * For example, a route with an average speed of 60 km/h, and a threshold
     * value of 100 km/h, would be 40 percent congested.
     * <p>
     * Floating point division should be used when performing the calculation,
     * however the resulting floating point number should be rounded to the
     * nearest integer before being returned.
     *
     * @return the calculated congestion rate as an integer between 0 and 100
     * inclusive
     * @ass1
     */
    @Override
    public int getCongestion() {
        float congestion = (float) this.averageSpeed() / this.getThreshold();
        int congestionPct = Math.round(100 - 100 * congestion);
        return Math.min(Math.max(congestionPct, 0), 100);
    }

    /**
     * Returns the string representation of this sensor.
     *
     * @return "SC:threshold:list,of,data,values" where 'threshold' is this
     * sensor's threshold and 'list,of,data,values' is this sensor's data array
     * @see DemoSensor#toString()
     * @ass1
     */
    @Override
    public String toString() {
        return "SC" + ":" + super.toString();
    }
}
