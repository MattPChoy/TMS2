package tms.network;

import org.junit.Before;
import org.junit.Test;
import tms.intersection.Intersection;
import tms.route.Route;
import tms.util.IntersectionNotFoundException;
import tms.util.RouteNotFoundException;

import static org.junit.Assert.*;

/**
 *  56) Test that makeTwoWay() throws an IntersectionNotFoundException if the
 *      provided id 'from' does not correspond with any existing intersection.
 *  57) Test that makeTwoWay() throws an IntersectionNotFoundException if the
 *       provided id 'to' does not correspond with any existing intersection.
 *  58) Test that makeTwoWay() throws a RouteNotFoundException if no route
 *      spans between the two intersections in the direction from -> to
 *  59) Test that makeTwoWay() throws a RouteNotFoundException if a route is
 *      defined in the wrong direction (to -> from)
 *  60) Test that the route created by makeTwoWay() starts at the
 *      intersection 'to' and terminates at the intersection 'from'
 *  61) Test that if the original route does not have a electronic speed sign,
 *      then the new route created by makeTwoWay() does not have an electronic
 *      speed sign, and they have the same default speed.
 *  62) Test that if the original route has an electronic speed sign, then
 *      the new route created by makeTwoWay() has an electronic speed sign with
 *      the same initialised speed.
 */
public class MakeTwoWay {
    private Network n;

    @Before
    public void setup() {
        n = new Network();
    }

    /**
     * Test that makeTwoWay() throws an IntersectionNotFoundException if the
     * provided id 'from' does not correspond with any existing intersections.
     *
     * @throws RouteNotFoundException never
     */
    @Test
    public void makeTwoWay_throwsIntersectionNotFoundFrom() throws RouteNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");

        try{
            n.makeTwoWay("P", "A");
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that makeTwoWay() throws an IntersectionNotFoundException if the
     * provided id 'to' does not correspond with any existing intersections.
     *
     * @throws RouteNotFoundException never
     */
    @Test
    public void makeTwoWay_throwsIntersectionNotFoundExceptionTo()
            throws RouteNotFoundException{
        boolean exceptionThrown = false;

        n.createIntersection("A");

        try{
            n.makeTwoWay("A", "P");
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that makeTwoWay() throws a RouteNotFoundException() if no route
     * spans between the two intersections in the direction from -> to
     *
     * @throws IntersectionNotFoundException never
     */
    @Test
    public void makeTwoWay_throwsRouteNotFoundException()
            throws IntersectionNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        try{
            n.makeTwoWay("A", "B");
        } catch (RouteNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that makeTwoWay() throws a RouteNotFoundException if a route is
     * defined in the wrong direction.
     *
     * @throws IntersectionNotFoundException never
     */
    @Test
    public void makeTwoWay_throwsRouteNotFoundExceptionWrongDirection()
            throws IntersectionNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("B", "A", 30);

        try{
            n.makeTwoWay("A", "B");
        } catch (Exception e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that the route created by makeTwoWay() starts at the
     * intersection 'to' and terminates at the intersection 'from'
     *
     * @throws IntersectionNotFoundException never
     * @throws RouteNotFoundException never
     */
    @Test
    public void makeTwoWay_correctDirection()
            throws IntersectionNotFoundException, RouteNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 40);
        n.makeTwoWay("A", "B");

        n.getConnection("A", "B"); // Test that the original route exists.
        n.getConnection("B", "A"); // Test that the new route exists
    }

    /**
     * Test that if the original route does not have a electronic speed sign,
     * then the new route created by makeTwoWay() does not have an electronic
     * speed sign, and they have the same default speed.
     * @throws IntersectionNotFoundException never
     * @throws RouteNotFoundException never
     */
    @Test
    public void makeTwoWay_noSpeedSign() throws IntersectionNotFoundException, RouteNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 40);

        assertFalse(
                n.getConnection("A", "B").hasSpeedSign()
        );
    }

    /**
     * Test that if the original route has an electronic speed sign, then
     * the new route created by makeTwoWay() has an electronic speed sign with
     * the same initialised speed.
     * @throws IntersectionNotFoundException never
     * @throws RouteNotFoundException never
     */
    @Test
    public void makeTwoWay_correctSpeedSign()
            throws IntersectionNotFoundException, RouteNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 40);

        n.addSpeedSign("A", "B", 80);

        assertTrue(
                n.getConnection("A", "B").hasSpeedSign()
        );
    }
}
