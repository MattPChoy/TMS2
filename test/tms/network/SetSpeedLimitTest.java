package tms.network;

import org.junit.Before;
import org.junit.Test;
import tms.route.Route;
import tms.util.IntersectionNotFoundException;
import tms.util.RouteNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 *  34) Test that setSpeedLimit() throws an IntersectionNotFoundException if
 *      the provided ID 'from' does not correspond with an existing intersection
 *  35) Test that setSpeedLimit() throws an IntersectionNotFoundException if
 *      the provided ID 'to' does not correspond with an existing intersection
 *  36) Test that setSpeedLimit() throws a RouteNotFound exception if no
 *      route spans between the two intersections in the direction from -> to
 *  37) Test that setSpeedLimit() throws an IllegalStateException if the
 *      given route does not have an electronic speed sign
 *  38) Test that setSpeedLimit() throws an IllegalArgumentException if the
 *      given speed limit is negative.
 *  39) Test that setSpeedLimit() changes the speed of the speed sign.
 */
public class SetSpeedLimitTest {

    private Network n;

    @Before
    public void setup() {
        n = new Network();
    }

    /**
     * Test that setSpeedLimit() throws an IntersectionNotFoundException if
     * the provided id 'from' does not correspond with an existing intersection.
     *
     * @throws RouteNotFoundException never
     */
    @Test
    public void setSpeedLimit_throwsIntersectionNotFoundExceptionFrom()
            throws RouteNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("B");

        try {
            n.setSpeedLimit("A", "B", 40);
        } catch (IntersectionNotFoundException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that setSpeedLimit() throws an IntersectionNotFoundException if
     * the provided id 'to' does not correspond with an existing intersection.
     *
     * @throws RouteNotFoundException never
     */
    @Test
    public void setSpeedLimit_throwsIntersectionNotFoundExceptionTo()
            throws RouteNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");

        try {
            n.setSpeedLimit("A", "B", 40);
        } catch (IntersectionNotFoundException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that setSpeedLimit() throws an RouteNotFoundException if
     * no route spans between the two intersections in the direction from -> to
     *
     * @throws IntersectionNotFoundException never
     */
    @Test
    public void setSpeedLimit_throwsRouteNotFoundException()
            throws IntersectionNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        try {
            n.setSpeedLimit("A", "B", 40);
        } catch (RouteNotFoundException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that setSpeedLimit() throws an IllegalStateException if the route
     * does not have an electronic speed sign.
     *
     * @throws IntersectionNotFoundException never
     * @throws RouteNotFoundException        never
     */
    @Test
    public void setSpeedLimit_throwsIllegalStateExceptionWhenSpeedSignMissing()
            throws IntersectionNotFoundException, RouteNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");
        n.connectIntersections("A", "B", 40);

        try {
            n.setSpeedLimit("A", "B", 20);
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }


        if (!exceptionThrown) fail();
    }

    /**
     * Test that setSpeedLimit() throws an IllegalArgumentException if the
     * given speed limit is negative.
     *
     * @throws IntersectionNotFoundException never
     * @throws RouteNotFoundException        never
     */
    @Test
    public void setSpeedLimit_throwsIllegalArgumentExceptionNegativeSpeed()
            throws IntersectionNotFoundException, RouteNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 40);
        n.addSpeedSign("A", "B", 80);

        try {
            n.setSpeedLimit("A", "B", -20);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that setSpeedLimit() changes the speed of the speed sign to the
     * correct value.
     *
     * @throws IntersectionNotFoundException never
     * @throws RouteNotFoundException        never
     */
    @Test
    public void setSpeedLimit_changesToCorrectValue()
            throws IntersectionNotFoundException, RouteNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 40);

        Route r = n.getConnection("A", "B");
        assertEquals(40, r.getSpeed());

        n.addSpeedSign("A", "B", 80);
        assertEquals(80, r.getSpeed());
    }
}
