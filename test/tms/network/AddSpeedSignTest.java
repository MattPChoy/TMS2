package tms.network;

import org.junit.Before;
import org.junit.Test;
import tms.route.Route;
import tms.util.IntersectionNotFoundException;
import tms.util.RouteNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 *  30) Test that addSpeedSign() throws an IntersectionNotFound exception if
 *      the provided ID does not correspond with an existing intersection.
 *  31) Test that addSpeedSign() throws an IllegalArgumentException if the
 *      default speed is negative
 *  32) Test that addSpeedSign() throws a RouteNotFoundException if the given
 *      route has not been instantiated.
 *  33) Test that addSpeedSign() has a matching initial speed value to that
 *      passed to the method.
 */
public class AddSpeedSignTest {

    private Network n;

    @Before
    public void setup() {
        n = new Network();
    }

    /**
     * Test that addSpeedSign() throws an IntersectionNotFound exception if the
     * provided ID does not correspond with an existing intersection.
     *
     * @throws RouteNotFoundException never
     */
    @Test
    public void addSpeedSign_throwsIntersectionNotFoundException()
            throws RouteNotFoundException {
        boolean exceptionThrown = false;

        try {
            n.addSpeedSign("A", "B", 40);
        } catch (IntersectionNotFoundException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that addSpeedSign() throws an IllegalArgumentException if the
     * default speed is negative
     *
     * @throws RouteNotFoundException        never
     * @throws IntersectionNotFoundException never
     */
    @Test
    public void addSpeedSign_throwsIllegalArgumentExceptionIfSpeedNegative()
            throws RouteNotFoundException, IntersectionNotFoundException {

        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 40);

        try {
            n.addSpeedSign("A", "B", -40);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that addSpeedSign() throws a RouteNotFoundException if the given
     * route has not been instantiated.
     *
     * @throws IntersectionNotFoundException never
     */
    @Test
    public void addSpeedSign_throwsRouteNotFoundExceptionIfRouteDoesNotExist()
            throws IntersectionNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        try {
            n.addSpeedSign("A", "B", 40);
        } catch (RouteNotFoundException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that addSpeedSign() throws has a matching initial speed value to
     * that passed to the method.
     *
     * @throws IntersectionNotFoundException never
     * @throws RouteNotFoundException        never
     */
    @Test
    public void addSpeedSign_correctInitialSpeed()
            throws IntersectionNotFoundException, RouteNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 40);

        Route r = n.getConnection("A", "B");

        assertEquals(40, r.getSpeed());

        n.addSpeedSign("A", "B", 80);

        assertEquals(80, r.getSpeed());
    }
    /*----------------------------------------------------------------------------*/
}
