package tms.sensors;

import org.junit.Before;
import org.junit.Test;
import tms.util.TimedItemManager;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void testDataRoundingLower(){
        // Check that numbers ending in decimals between *.0 and *.49* are
        // rounded down

        int[] data = new int[] {40};

        DemoVehicleCount d = new DemoVehicleCount(
                data,
                111
        );

        assertEquals(64, d.getCongestion());
    }

    @Test
    public void testDataRoundingUpper(){
        // Check that numbers ending in decimals between .5* to .99* are
        // rounded up the the integer above

        int[] data = new int[] {53};

        DemoVehicleCount d = new DemoVehicleCount(
                data,
                111
        );

        assertEquals(52, d.getCongestion());

    }
}
