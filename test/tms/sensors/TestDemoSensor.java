package tms.sensors;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestDemoSensor{
    @Test
    public void testEquals_isEqual1(){
        Sensor s1 = new DemoPressurePad(new int[] {1,2,3,4}, 40);
        Sensor s2 = new DemoPressurePad(new int[] {1,2,3,4}, 40);

        assertTrue(s1.equals(s2));
    }

    @Test
    public void testEquals_isEqual2(){
        Sensor s1 = new DemoSpeedCamera(new int[] {1,2,3,4}, 40);
        Sensor s2 = new DemoSpeedCamera(new int[] {1,2,3,4}, 40);

        assertTrue(s1.equals(s2));
    }

    @Test
    public void testEquals_isEqual3(){
        Sensor s1 = new DemoVehicleCount(new int[] {1,2,3,4}, 40);
        Sensor s2 = new DemoVehicleCount(new int[] {1,2,3,4}, 40);

        assertTrue(s1.equals(s2));
    }

    @Test
    public void testEquals_isNotEqual1(){
        Sensor s1 = new DemoSpeedCamera(new int[] {1,2,3,4}, 40);
        Sensor s2 = new DemoVehicleCount(new int[] {1,2,3,4}, 40);

        assertFalse(s1.equals(s2));
    }

    @Test
    public void testEquals_isNotEqual2(){
        Sensor s1 = new DemoSpeedCamera(new int[] {1,2,3,4}, 40);
        Sensor s2 = new DemoSpeedCamera(new int[] {1,2,3,3}, 40);

        assertFalse(s1.equals(s2));
    }

    @Test
    public void testEquals_isNotEqual3(){
        Sensor s1 = new DemoSpeedCamera(new int[] {1,2,3,4}, 30);
        Sensor s2 = new DemoSpeedCamera(new int[] {1,2,3,3}, 40);

        assertFalse(s1.equals(s2));
    }

    @Test
    public void testHashCode_isEquals(){
        Sensor s1 = new DemoSpeedCamera(new int[] {1,2,3,4}, 40);
        Sensor s2 = new DemoSpeedCamera(new int[] {1,2,3,4}, 40);

        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void testHashCode_notEquals_dataArray(){
        Sensor s1 = new DemoSpeedCamera(new int[] {1,2,3,4}, 40);
        Sensor s2 = new DemoSpeedCamera(new int[] {1,2,3,3}, 40);

        assertNotEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void testHashCode_notEquals_threshold(){
        Sensor s1 = new DemoSpeedCamera(new int[] {1,2,3,4}, 40);
        Sensor s2 = new DemoSpeedCamera(new int[] {1,2,3,4}, 39);

        assertNotEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void testHashCode_notEquals_subclass(){
        Sensor s1 = new DemoVehicleCount(new int[] {1,2,3,4}, 40);
        Sensor s2 = new DemoSpeedCamera(new int[] {1,2,3,4}, 39);

        assertNotEquals(s1.hashCode(), s2.hashCode());
    }
}
