package tms.congestion;

import tms.sensors.Sensor;

import java.util.List;
/**
 * An implementation of a congestion calculator that calculates the average
 * congestion value from all of its sensors.
 */
public class AveragingCongestionCalculator implements CongestionCalculator{
    private final int CONGESTION_LOWER_BOUND = 0;
    private final int CONGESTION_UPPER_BOUND = 100;
    private List<Sensor> sensors;

    /**
     * Creates a new averaging congestion calculator for a given list of sensors
     * on a route.
     *
     * @param sensors a list of sensors for which to calculate the congestion.
     */
    public AveragingCongestionCalculator(List<Sensor> sensors){
        this.sensors = sensors;
    }

    /**
     * Calculates the average congestion level, as returned by
     * Sensor.getCongestion(), of all the sensors stored by this calculator.
     *
     * If there are no sensors stored, return 0.
     *
     * If the computed average is not an integer, it should be rounded to the
     * nearest integer before being returned.
     *
     * @return the average congestion, (0 to 100) inclusive.
     */
    @Override
    public int calculateCongestion() {
        /// First validation step - if there are no sensors in the list, return 0
        int numberOfSensors = sensors.size();
        if (numberOfSensors == 0) return numberOfSensors;

        // If the code execution gets to this point, then we have established
        // that there is at least one sensor in the list. Assume that all
        // elements of the list are sensors.

        int sum = 0;

        for (Sensor s: sensors) {
            // TODO: Confirm that we assume that the congestion is validated
            //  and that we don't have to verify it ourselves.
            sum += s.getCongestion();
        }

        double result = (double) sum / (double) numberOfSensors;
        return (int) Math.round(result); // Round to the nearest integer.
    }
}
