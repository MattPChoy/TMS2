package tms.sensors;

public interface VehicleCount extends Sensor{
    /**
     * Returns the observed rate of vehicles travelling past this sensor in
     * vehicles per minute
     * @return the current traffic rate of traffic flow in vehicles/minute
     * reported by the vehicle count.
     */
    int countTraffic();
}
