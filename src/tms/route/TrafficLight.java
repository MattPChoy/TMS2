package tms.route;

/**
 * Represents a traffic light signal on a route, indicating whether vehicles
 * can proceed past the route's destination intersection.
 * @ass1
 */
public class TrafficLight {
    /** Current status colour of the traffic light */
    private TrafficSignal signal;

    /**
     * Creates a traffic light with an initial colour of
     * {@link TrafficSignal#RED}.
     * @ass1
     */
    public TrafficLight() {
        signal = TrafficSignal.RED;
    }

    /**
     * Get the traffic light signal currently displayed by the light.
     *
     * @return the displayed traffic light signal
     * @ass1
     */
    public TrafficSignal getSignal() {
        return this.signal;
    }

    /**
     * Sets the displayed traffic light signal to a new value.
     * <p>
     * Note: this method has no logic and as such will allow changing a red
     * light to green immediately.
     *
     * @param signal the new traffic light signal
     * @ass1
     */
    public void setSignal(TrafficSignal signal) {
        this.signal = signal;
    }
}
