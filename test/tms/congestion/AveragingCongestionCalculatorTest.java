package tms.congestion;

import static org.junit.Assert.*;
import org.junit.Test;
import tms.sensors.DemoPressurePad;
import tms.sensors.DemoSpeedCamera;
import tms.sensors.DemoVehicleCount;
import tms.sensors.Sensor;
import tms.util.TimedItemManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used for testing the validity of the implemented
 * AveragingCongestionCalculator. The following tests are implemented:
 *
 *  1) Data values from a single sensors changes every time the
 *     TimedItemManager's oneSecond() is called.
 *  2) Data values from multiple sensors change every time the
 *     TimedItemManager's oneSecond() method is called.
 *  3) AveragingCongestionCalculator() returns 0 if there are no sensors.
 *  4) AveragingCongestionCalculator() returns expected value for 1 sensor.
 *  5) AveragingCongestionCalculator() returns expected value for 3 sensors.
 *  6) Congestion values ending in .5 get rounded up
 *  7) Congestion values ending in decimals greater than .5 get rounded up
 *  8) Congestion values ending in decimals smaller than .5 get rounded down
 *  9) Negative (non-zero) congestion values get bounded to 0
 * 10) Positive (non-zero) congestion values get bounded to 100
 */
public class AveragingCongestionCalculatorTest {
    TimedItemManager t = TimedItemManager.getTimedItemManager();

    /**
     * Test that the data values change as TimedItemManager
     * .getTimedItemManager.oneSecond() is called.
     */
    @Test
    public void testCongestion_oneSecondIteration(){
        List<Sensor> sensors = new ArrayList<>();

        int[] data = {30,40,50,60};
        int threshold = 100;

        sensors.add(
                new DemoPressurePad(data, threshold)
        );

        AveragingCongestionCalculator a = new AveragingCongestionCalculator(
                sensors
        );


        for (int datum : data) {
            int expected =
                    (int) (Math.round(
                            (((double) datum / (double) threshold)) *
                                    (double) 100)
                    );

            assertEquals(
                    expected, a.calculateCongestion()
            );

            t.oneSecond();
        }
    }

    @Test
    public void testCongestion_oneSecondIterationWithMoreSensors(){
        List<Sensor> sensors = new ArrayList<>();

        int[] data = {30,40,50,60};
        int[] data2 = {30,40,50,60};
        int threshold = 100;

        sensors.add(
                new DemoPressurePad(data, threshold)
        );

        sensors.add(
                new DemoPressurePad(data, threshold)
        );

        AveragingCongestionCalculator a = new AveragingCongestionCalculator(
                sensors
        );

        for (int i = 0; i < data.length; i++){
            int expected =
                    (int) (Math.round(
                            (((double) data[i] / (double) threshold)) *
                                    (double) 100)
                    );

            int expected1 =
                    (int) (Math.round(
                            (((double) data2[i] / (double) threshold)) *
                                    (double) 100)
                    );

            int average = averageOf(expected, expected1);
            assertEquals(
                    average, a.calculateCongestion()
            );

            t.oneSecond();
        }
    }

    @Test
    public void testCongestion_noNodesReturnsZero(){
        List<Sensor> sensors = new ArrayList<>();

        AveragingCongestionCalculator a = new AveragingCongestionCalculator(
                sensors
        );

        assertEquals(0, a.calculateCongestion());
    }

    @Test
    public void testCongestion_singleNode(){
        List<Sensor> sensors = new ArrayList<>();

        sensors.add(
                new DemoPressurePad(new int[] {45,2,3,4}, 60)
        );

        AveragingCongestionCalculator a = new AveragingCongestionCalculator(
                sensors
        );

        assertEquals(75, a.calculateCongestion());
    }

    @Test
    public void testCongestion_multipleNodes(){
        List<Sensor> sensors = new ArrayList<>();

        sensors.add(
                new DemoPressurePad(new int[] {43,2,3}, 40)
        ); // Congestion should be 100

        sensors.add(
                new DemoSpeedCamera(new int[] {60,2,3,4,5}, 100)
        ); // Congestion should be 40

        sensors.add(
                new DemoVehicleCount(new int[] {60,29, 30, 21, 39}, 100)
        ); // Congestion should be 40

        AveragingCongestionCalculator a = new AveragingCongestionCalculator(
                sensors
        );

        assertEquals(60, a.calculateCongestion());


    }

    @Test
    public void testCongestion_roundingUp(){
        List<Sensor> sensors = new ArrayList<>();

        sensors.add(
                new DemoPressurePad(new int[] {255,2,3,4}, 1000)
        );

        AveragingCongestionCalculator a = new AveragingCongestionCalculator(
                sensors
        );

        assertEquals(26, a.calculateCongestion());
    }

    @Test
    public void testCongestion_roundingDown(){
        List<Sensor> sensors = new ArrayList<>();

        sensors.add(
                new DemoPressurePad(new int[] {2549,2,3,4}, 10000)
        );

        AveragingCongestionCalculator a = new AveragingCongestionCalculator(
                sensors
        );

        assertEquals(25, a.calculateCongestion());
    }

    @Test
    public void testCongestion_roundingUp2(){
        List<Sensor> sensors = new ArrayList<>();

        sensors.add(
                new DemoPressurePad(new int[] {260,2,3,4}, 1000)
        );

        AveragingCongestionCalculator a = new AveragingCongestionCalculator(
                sensors
        );

        assertEquals(26, a.calculateCongestion());
    }

    @Test
    public void testCongestion_lowerBoundRoundsTo0(){
        List<Sensor> sensors = new ArrayList<>();

        sensors.add(
                new DemoPressurePad(new int[] {-260,2,3,4}, 1000)
        );

        AveragingCongestionCalculator a = new AveragingCongestionCalculator(
                sensors
        );

        assertEquals(0, a.calculateCongestion());
    }

    @Test
    public void testCongestion_upperBoundRoundsTo100(){
        List<Sensor> sensors = new ArrayList<>();

        sensors.add(
                new DemoPressurePad(new int[] {260,2,3,4}, 10)
        );

        AveragingCongestionCalculator a = new AveragingCongestionCalculator(
                sensors
        );

        assertEquals(100, a.calculateCongestion());
    }

    private int averageOf(int a, int b){
        int sum = a+b;

        return (int) ((double) sum / (double) 2);
    }

}
