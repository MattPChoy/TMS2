package tms.congestion;

import tms.sensors.Sensor;
import java.util.List;


/***
 * A device used to combine congestion levels of multiple sensors and return a
 * single calculated congestion level.
 */
public interface CongestionCalculator {
    /**
     * Returns a congestion level, between 0 and 100, indicating the calculated
     * congestion of a route based on the reported congestion levels of the
     * sensors on that route.
     * @return calculated congestion level, 0 to 100 inclusive.
     */
    public int calculateCongestion();
}
