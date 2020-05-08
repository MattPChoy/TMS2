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

    /**
     * Returns true if and only if this sensor is equal to the other given
     * sensor.
     *
     * For two sensors to be equal, the must have:
     *
     * 1) the same type (ie. the same subclass of DemoSensor)
     * 2) the same threshold value
     * 3) the same data values array (each data value should match)
     *
     * Overrides equals in class Object
     * @param obj other object to compare equality
     * @return true if equal, false otherwise
     */
    public boolean equals(Object obj){
        if (this instanceof DemoPressurePad && obj instanceof DemoPressurePad ||
            this instanceof DemoSpeedCamera && obj instanceof DemoSpeedCamera ||
            this instanceof DemoVehicleCount && obj instanceof DemoVehicleCount
        ){
            Sensor otherSensor = (Sensor) obj;

            if (otherSensor.getThreshold() == this.getThreshold()){
                if (otherSensor.toString().equals(this.toString())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the hashcode of this object
     *
     * For two sensors hashcodes will be equal if they have:
     * 1) The same type (ie. the same subclass of DemoSensor)
     * 2) The same type of threshold value
     * 3) The same data values array (each data value should match)
     *
     * Overrides hashCode in class Object
     * @return int hashcode of this object.
     */
    public int hashCode(){
        int result = 16;

        if (this instanceof DemoPressurePad){
            result *= 2;
        } else if (this instanceof DemoSpeedCamera){
            result *= 3;
        } else if (this instanceof DemoVehicleCount){
            result *= 5;
        }

        result *= threshold;

        for (int datum : data){
            result *= datum;
        }

        return result;
    }
}
