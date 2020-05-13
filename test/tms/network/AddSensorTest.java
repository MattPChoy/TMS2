package tms.network;


import org.junit.Before;
import org.junit.Test;
import tms.route.Route;
import tms.sensors.DemoSpeedCamera;
import tms.util.DuplicateSensorException;
import tms.util.IntersectionNotFoundException;
import tms.util.RouteNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 *  49) Test that addSensor() throws a DuplicateSensorException if a sensor
 *      of the same type already exists on the given route.
 *  50) Test that addSensor() throws an IntersectionNotFoundException if the
 *      provided id 'from' does not correspond with any existing intersection.
 *  51) Test that addSensor() throws an IntersectionNotFoundException if the
 *      provided id 'to' does not correspond with any existing intersection.
 *  52) Test that addSensor() throws a RouteNotFoundException if no route
 *      spans between the two intersections in the direction from -> to
 *  53) Test that addSensor() adds the appropriate sensor to the route.
 */
public class AddSensorTest {
    private Network n;

    @Before
    public void setup() {
        n = new Network();
    }

    /**
     * Test that addSensor() throws a DuplicateSensorException if a sensor
     * of the same type already exists on the given route.
     * @throws IntersectionNotFoundException never
     * @throws DuplicateSensorException never
     * @throws RouteNotFoundException never
     */
    @Test
    public void addSensor_throwsDuplicateSensorException() throws IntersectionNotFoundException, DuplicateSensorException, RouteNotFoundException {
        boolean exceptionThrown = false;
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 30);

        n.addSensor("A", "B", new DemoSpeedCamera(
                new int[] {1,2,3,4}, 40
        ));

        try{
            n.addSensor("A", "B", new DemoSpeedCamera(
                    new int[] {1,2,3,4}, 40
            ));
        } catch (DuplicateSensorException | RouteNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that addSensor() throws an IntersectionNotFoundException if the
     * provided id 'from' does not correspond with any existing intersection.
     * @throws RouteNotFoundException never
     * @throws DuplicateSensorException never
     */
    @Test
    public void addSensor_throwsIntersectionNotFoundExceptionFrom()
        throws RouteNotFoundException, DuplicateSensorException {

        boolean exceptionThrown = false;
        n.createIntersection("B");

        try{
            n.addSensor("A", "B", new DemoSpeedCamera(
                    new int[] {1,2,3,4}, 40
            ));
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that addSensor() throws an IntersectionNotFoundException if the
     * provided id 'to' does not correspond with any existing intersection.
     * @throws RouteNotFoundException never
     * @throws DuplicateSensorException never
     */
    @Test
    public void addSensor_throwsIntersectionNotFoundExceptionTo()
            throws RouteNotFoundException, DuplicateSensorException {

        boolean exceptionThrown = false;
        n.createIntersection("A");

        try{
            n.addSensor("A", "B", new DemoSpeedCamera(
                    new int[] {1,2,3,4}, 40
            ));
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that addSensor() throws a RouteNotFoundException if no route
     * spans between the two intersections in the direction from -> to
     */
    @Test
    public void addSensor_throwsRouteNotFoundException(){
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        try{
            n.addSensor("A", "B", new DemoSpeedCamera(
                    new int[] {1,2,3,4},
                    40
            ));
        } catch (Exception e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that addSensor() adds the appropriate sensor to the route.
     * @throws IntersectionNotFoundException never
     * @throws DuplicateSensorException never
     * @throws RouteNotFoundException never
     */
    @Test
    public void addSensor_addsCorrectSensor()
            throws IntersectionNotFoundException, DuplicateSensorException,
            RouteNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");
        n.connectIntersections("A", "B", 40);

        n.addSensor("A", "B", new DemoSpeedCamera(
                new int[] {1,2,3,4},
                40
        ));

        Route r = n.getConnection("A", "B");

        assertEquals(
                "A:B:40:1" + System.lineSeparator() + "SC:40:1,2,3,4",
                r.toString()
        );
    }
}
