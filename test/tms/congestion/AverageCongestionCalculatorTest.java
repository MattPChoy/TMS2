package tms.congestion;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import tms.sensors.DemoPressurePad;
import tms.sensors.DemoSpeedCamera;
import tms.sensors.DemoVehicleCount;
import tms.sensors.Sensor;
import tms.util.TimedItemManager;

import java.util.ArrayList;
import java.util.List;

public class AverageCongestionCalculatorTest {
    TimedItemManager t = TimedItemManager.getTimedItemManager();;
    /*
    * Test the rounding of numbers from say .3, .5, .7 for various numbers of
    *  sensors.
    *
    * */

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

    private int averageOf(int a, int b){
        int sum = a+b;

        return (int) ((double) sum / (double) 2);
    }

}
