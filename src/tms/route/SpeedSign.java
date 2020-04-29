package tms.route;

/**
 * Represents an electronic speed sign, indicating a dynamic speed limit on a
 * route.
 * @ass1
 */
public class SpeedSign {
    /** Currently displayed speed limit on the speed sign. */
    private int currentSpeed;

    /**
     * Creates a new electronic speed sign with the given initial displayed
     * speed.
     *
     * @param initialSpeed the initial speed limit to be shown on the sign
     * @ass1
     */
    public SpeedSign(int initialSpeed) {
        this.currentSpeed = initialSpeed;
    }

    /**
     * Get the speed displayed by the sign (not the speed of cars on the route).
     *
     * @return the current speed limit displayed by this sign
     * @ass1
     */
    public int getCurrentSpeed() {
        return this.currentSpeed;
    }

    /**
     * Sets the speed limit displayed.
     *
     * @param speed the new speed limit to display
     * @ass1
     */
    public void setCurrentSpeed(int speed) {
        this.currentSpeed = speed;
    }
}
