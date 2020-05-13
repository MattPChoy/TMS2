package tms.network;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import tms.route.Route;
import tms.util.IntersectionNotFoundException;
import tms.util.RouteNotFoundException;

/**
 *  19) Test that connectIntersection() creates a route between the
 *      two specified intersections
 *  20) Test that connectIntersection() creates a route with appropriate ID.
 *  21) Test that connectIntersection() creates a route with the correct
 *      default speed
 *  22) Test that connectIntersection() throws an IntersectionNotFoundException
 *      if there is no intersection with id 'from'
 *  23) Test that connectIntersection() throws an IntersectionNotFoundException
 *      if there is no intersection with id 'to'
 *  24) Test that connectIntersection() throws an IllegalStateException if
 *      such a route already exists.
 *  25) Test that connectIntersection() throws an IllegalArgumentException if
 *      the given speed is negative.
 */
public class ConnectIntersectionsTest {
    private Network n;

    @Before
    public void setup() {
        n = new Network();
    }

    /**
     * Test that connectIntersection() creates a route between the
     * two specified intersections
     *
     * @throws IntersectionNotFoundException never
     */
    @Test
    public void connectIntersections_connectsIntersections()
            throws IntersectionNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 40);
    }

    /**
     * Test that connectIntersection() creates a route with appropriate ID.
     *
     * @throws IntersectionNotFoundException never
     * @throws RouteNotFoundException never
     */
    @Test
    public void connectIntersections_correctID()
            throws IntersectionNotFoundException, RouteNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 40);

        Route r = n.getConnection("A", "B");
        assertEquals("A:B:40:0", r.toString());
    }

    /**
     * Test that connectIntersection() creates a route with the correct
     * default speed.
     *
     * @throws IntersectionNotFoundException never
     * @throws RouteNotFoundException never
     */
    @Test
    public void connectIntersections_correctDefaultSpeed()
            throws IntersectionNotFoundException, RouteNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 40);

        Route r = n.getConnection("A", "B");
        assertEquals("A:B:40:0", r.toString());
    }

    /**
     * Test that connectIntersection() throws an IntersectionNotFoundException
     * if there is no intersection with id 'from'
     */
    @Test
    public void connectIntersections_throwsIntersectionNotFoundExceptionFrom(){
        boolean exceptionThrown = false;

        n.createIntersection("A");

        try{
            n.connectIntersections("P", "A", 40);
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if(!exceptionThrown) fail();
    }

    /**
     * Test that connectIntersection() throws an IntersectionNotFoundException
     * if there is no intersection with id 'to'
     */
    @Test
    public void connectIntersections_throwsIntersectionNotFoundExceptionTo(){
        boolean exceptionThrown = false;

        n.createIntersection("A");

        try{
            n.connectIntersections("A", "P", 40);
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if(!exceptionThrown) fail();
    }

    /**
     * Test that connectIntersection() throws an IllegalStateException if
     * such a route already exists.
     *
     * @throws IntersectionNotFoundException never
     */
    @Test
    public void connectIntersections_throwsIllegalStateExceptionRouteExists()
            throws IntersectionNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");
        n.connectIntersections("A", "B", 40);

        try{
            n.connectIntersections("A", "B", 40);
        } catch (IllegalStateException e){
            exceptionThrown = true;
        }

        if(!exceptionThrown) fail();
    }

    /**
     * Test that connectIntersection() throws an IllegalArgumentException if
     * the given speed is negative.
     *
     * @throws IntersectionNotFoundException never
     */
    @Test
    public void connectIntersections_throwsIllegalArgumentExceptionNegativeSpd()
            throws IntersectionNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        try{
            n.connectIntersections("A", "P", -40);
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }

        if(!exceptionThrown) fail();
    }
}
