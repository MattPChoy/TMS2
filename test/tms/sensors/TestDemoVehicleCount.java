package tms.sensors;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import tms.util.TimedItemManager;

public class TestDemoVehicleCount {
    TimedItemManager t;

    @Before
    public void setup(){
        t = TimedItemManager.getTimedItemManager();
    }

    @Test
    public void testInstantiation(){
        DemoVehicleCount d = new DemoVehicleCount(
                new int[] {1,2,3,4},
                40
        );

        String expected = "VC:40:1,2,3,4";
        assertEquals(expected, d.toString());
    }

    @Test
    public void testDataIteration(){
        // Check that the oneSecond method operates as expected, iterating
        // through the values in the data array (and looping back to the start)

        int[] data = new int[] {1,2,3,4, 90, 83};

        DemoVehicleCount d = new DemoVehicleCount(
                data,
                40
        );

        for (int entry: data) {
            assertEquals(entry, d.countTraffic());
            t.oneSecond();
        }
    }

}
