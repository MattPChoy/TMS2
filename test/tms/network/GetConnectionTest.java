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
 *  44) Test that getConnection() throws an IntersectionNotFoundException if
 *      the provided id 'from' does not correspond with any existing
 *      intersection.
 *  45) Test that getConnection() throws an IntersectionNotFoundException if
 *      the provided id 'to' does not correspond with any existing
 *      intersection.
 *  46) Test that getConnection() throws a RouteNotFoundException if no
 *      route spans between the two intersections in the direction from -> to
 *  47) Test that getConnection() returns the route that connects the two
 *      given intersections.
 */
public class GetConnectionTest {

    private Network n;

    @Before
    public void setup() {
        n = new Network();
    }

    /**
     * Test that getConnection() throws an IntersectionNotFoundException if
     * the provided id 'from' does not correspond with any existing
     * intersection.
     */
    @Test
    public void getConnection_throwsIntersectionNotFoundExceptionFrom() {
        boolean exceptionThrown = false;
        n.createIntersection("A");

        try {
            n.getConnection("P", "A");
        } catch (IntersectionNotFoundException e) {
            exceptionThrown = true;
        } catch (RouteNotFoundException ignored) {

        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that getConnection() throws an IntersectionNotFoundException if
     * the provided id 'to' does not correspond with any existing intersection.
     */
    @Test
    public void getConnection_throwsIntersectionNotFoundException() {
        boolean exceptionThrown = false;
        n.createIntersection("A");

        try {
            n.getConnection("A", "P");
        } catch (IntersectionNotFoundException e) {
            exceptionThrown = true;
        } catch (RouteNotFoundException ignored) {

        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that getConnection() throws a RouteNotFoundException if no route
     * spans between the two intersections in the direction from -> to
     *
     * @throws IntersectionNotFoundException never
     */
    @Test
    public void getConnection_throwsRouteNotFoundException()
            throws IntersectionNotFoundException {
        boolean exceptionThrown = false;
        n.createIntersection("A");
        n.createIntersection("B");

        try {
            n.getConnection("A", "B");
        } catch (RouteNotFoundException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that getConnection() returns the route that connects the two
     * given intersections.
     *
     * @throws IntersectionNotFoundException never
     * @throws RouteNotFoundException never
     * @throws DuplicateSensorException never
     */
    @Test
    public void getConnection_returnsCorrectRoute()
            throws IntersectionNotFoundException, RouteNotFoundException,
            DuplicateSensorException {
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 30);

        Route r = n.getConnection("A", "B");
        assertEquals(30, r.getSpeed());

        r.addSensor(
                new DemoSpeedCamera(
                        new int[] {1,2,3,4}, 40
                )
        );

        Route r2 = n.getConnection("A", "B");

        String expectedToString = "A:B:30:1" + System.lineSeparator()
                + "SC:40:1,2,3,4";
        String actual = r2.toString();

        assertEquals(expectedToString, actual);
    }
}
