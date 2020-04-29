package tms.sensors;

import tms.util.TimedItem;
import tms.util.TimedItemManager;

import java.util.Arrays;
import java.util.Objects;

/**
 * An abstract class to represent the shared functionality of the demo sensor
 * types.
 * @ass1_2
 */
public abstract class DemoSensor implements TimedItem {

    /** Array of observed data values */
    private int[] data;
    /** Threshold data value for determining congestion */
    private int threshold;
    /** Internal count of seconds passed for setting the current data value */
    private int secondsPassed;
    /** Current data value indicated by the sensor */
    private int currentValue;

    /**
     * Creates a new sensor, using the given list of data values and threshold.
     * <p>
     * The initial value returned by {@link DemoSensor#getCurrentValue()}
     * should be the first element of the given data array.
     * <p>
     * The sensor should be registered as a timed item, see
     * {@link TimedItemManager#registerTimedItem(TimedItem)}.
     *
     * @requires data.length &gt; 0
     * @param data a non-empty array of data values
     * @param threshold a threshold value that indicated what value is high
     *                  congestion
     * @ass1
     */
    protected DemoSensor(int[] data, int threshold) {
        this.addData(data);
        this.threshold = threshold;
        this.secondsPassed = 0;

        TimedItemManager.getTimedItemManager().registerTimedItem(this);
    }

    /**
     * Sets this sensor's data array to the given array.
     *
     * @param data the array of sensor values to have the sensor use
     * @ass1
     */
    private void addData(int[] data) {
        this.data = data;
        this.currentValue = data[0];
    }

    /**
     * Returns the current data value as measured by the sensor.
     *
     * @return the current data value
     * @ass1
     */
    protected int getCurrentValue() {
        return currentValue;
    }

    /**
     * Returns the threshold data value.
     *
     * @return the threshold
     * @ass1
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Sets the current data value returned by
     * {@link DemoSensor#getCurrentValue()} to be the next value in the data
     * array passed to the constructor.
     * <p>
     * If the end of the data array is reached, it should wrap around to the
     * start of the array and continue in the same order.
     * @ass1
     */
    @Override
    public void oneSecond() {
        secondsPassed++;
        int secs = secondsPassed % data.length;
        currentValue = data[secs];
    }

    /**
     * Returns the string representation of this sensor.
     *
     * @return "threshold:list,of,data,values" where 'threshold' is this
     * sensor's threshold and 'list,of,data,values' is this sensor's data array
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s%s%s",
                this.threshold,
                ":",
                String.join(",",
                        Arrays.stream(this.data).mapToObj(String::valueOf)
                                .toArray(String[]::new)));
    }
}
