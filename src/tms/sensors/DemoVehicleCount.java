package tms.sensors;

public class DemoVehicleCount extends DemoSensor implements VehicleCount {
    /**
     * Creates a new vehicle count sensor with the given threshold and data
     * @param data a non empty array of data values
     * @param threshold a threshold value that indicates which values
     *                  represent high congestion
     * @requires data.size() > 0
     */
    public DemoVehicleCount(int[] data, int threshold){
        super(data, threshold);
    }

    /**
     * Returns the object rate of vehicles travelling past this sensor in
     * vehicles per minute
     *
     * @return the current rate of traffic flow in vehicles/minute reported
     * by the vehicle count
     */
    public int countTraffic(){
        return this.getCurrentValue();
    }

    /**
     * Calculates the congestion rate as the complement of the percentage
     * given by countTraffic() divided by getThreshold().
     *
     * For example, a route with a traffic of 60 cars per minute, and a
     * threshold value of 100 cars per minute, would be 40 percent congested.
     *
     * Floating point division should be used when performing the
     * calculation, however the resulting floating point number should be
     * rounded to the nearest integer before being returned.
     *
     * @return the calculated congestion rate as an integer between 0 and 100
     * included.
     */
    public int getCongestion(){
        float congestion = (float) this.countTraffic() / this.getThreshold();
        int congestionPct = Math.round(100 - 100 * congestion);
        return Math.min(Math.max(congestionPct, 0), 100);
    }

    /**
     * Returns the string representation of this sensor.
     *
     * Overrides toString in class DemoSensor
     *
     * @return "VC:threshold:list,of,data,values" where 'threshold' is this
     * sensor's threshold and 'list,of,data,values' is this sensor's data array
     */
    public String toString(){
        return "VC:" + super.toString();
    }
}
