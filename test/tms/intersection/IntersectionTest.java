package tms.intersection;

import java.util.List;
import java.util.ArrayList;
import org.junit.Test;
import tms.route.Route;
import tms.route.TrafficLight;
import tms.route.TrafficSignal;
import static org.junit.Assert.*;
import tms.util.InvalidOrderException;
import tms.util.RouteNotFoundException;
import tms.util.TimedItemManager;


/**
 * Class used for testing the validity of the implemented
 * Intersection. The following tests are implemented:
 *
 *
 *  1) Test that the hasTrafficLight() method returns the correct boolean if
 *     there is no traffic light.
 *  2) Test that the hasTrafficLight() method returns the correct boolean if
 *     there is a traffic light.
 *
 *  3) Test that the setLightDuration() method throws an
 *     IllegalStateException if the intersection does not have a set of traffic
 *     lights as determined by the hasTrafficLights() method
 *  4) Test that the setLightDuration() method throws an
 *     IllegalArgumentException if the duration is less than the traffic light's
 *     yellow time plus one.
 *  5) Test that the setLightDuration() method does not throw any error when
 *     valid values are passed.
 *
 *  6) Test that the addTrafficLight() method does not throw an error when
 *     duration = yellowTime + 1;
 *  7) Test that the addTrafficLights() method throws an
 *     IllegalArgumentException if duration == yellowTime
 *     (i.e. duration < yellowTime)
 *  8) Test that the addTrafficLights() method throws an
 *     IllegalArgumentException if yellowTime is 0
 *  9) Test that the addTrafficLights() method throws an
 *     IllegalArgumentException if yellowTime is negative
 * 10) Test that the addTrafficLight() method adds a traffic light object to
 *     each route which does not already have one.
 * 11) Test that an instance of IntersectionLights is instantiated for the
 *     network when addTrafficLights() is called.
 * 12) Test that an order parameter which is not a permutation of the
 *     incoming routes throws an InvalidOrderException.
 *
 * 13) Test that the addConnection() method throws an IllegalStateException
 *     if the route to be created already exists.
 * 14) Test that the addConnection() method throws an
 *     IllegalArgumentException if the defaultSpeed parameter is negative
 * 15) Test that the addConnection() method creates a new route between the
 *     appropriate intersections in the correct direction.
 * 16) Test that the addConnection() method instantiates a route with the
 *     correct ID.
 * 17) Test that the addConnection() method instantiates a route with correct
 *     default speed.
 * 18) Test that the equals() method is the same for two intersections that
 *     have the same ID
 * 19) Test that the equals() method is different for two intersections with
 *     differing IDs.
 *
 * 20) Test that the toString() method has the correct output for an
 *     intersection with no traffic light.
 * 21) Test that the toString() method has the correct output for an
 *     intersection with a traffic light.
 *
 * 22) Test that the traffic light of a route gets added to the cycle when
 *     the route is created after the instantiation of the traffic light.
 */
public class IntersectionTest {
    /**
     * Test that the hasTrafficLight() method returns the correct boolean if
     * there is no traffic light.
     */
    @Test
    public void testHasTrafficLights_false(){
        Intersection A = new Intersection("A");
        assertFalse(A.hasTrafficLights());
    }

    /**
     * Test that the hasTrafficLight() method returns the correct boolean if
     * there is a traffic light.
     * @throws RouteNotFoundException never
     */
    @Test
    public void testHasTrafficLight_true() throws RouteNotFoundException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);
        List<Route> order = new ArrayList<>();
        order.add(A.getConnection(B));

        try{
            A.addTrafficLights(order, 3, 10);
        } catch (InvalidOrderException o){
            fail();
        }
        assertTrue(A.hasTrafficLights());
    }

    /**
     * Test that the setLightDuration() method throws an
     * IllegalStateException if the intersection does not have a set of
     * traffic lights as determined by the hasTrafficLights() method
     */
    @Test
    public void testSetLightDuration_throwsIllegalStateException(){
        // Try to make the setLightDuration throw an IllegalStateException
        // when there is no traffic light.
        boolean thrownError = false;

        Intersection A = new Intersection("A");

        try{
            A.setLightDuration(10);
        } catch (IllegalStateException e){
            thrownError = true;
        }

        if (!thrownError) fail();
    }

    /**
     * Test that the setLightDuration() method throws an
     * IllegalArgumentException if the duration is less than the traffic
     * light's yellow time plus one.
     * @throws RouteNotFoundException never
     * @throws InvalidOrderException never
     */
    @Test
    public void testSetLightDuration_throwsIllegalArgumentException()
            throws RouteNotFoundException, InvalidOrderException {
        boolean thrownError = false;

        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);

        List<Route> order = new ArrayList<>();
        order.add(A.getConnection(B));

        A.addTrafficLights(order, 20, 50);

        try{
            A.setLightDuration(20);
        } catch (IllegalArgumentException e){
            thrownError = true;
        }

        if (!thrownError){
            fail();
        }
    }

    /**
     * Test that the setLightDuration() method does not throw any error when
     * valid values are passed.
     * @throws RouteNotFoundException never
     * @throws InvalidOrderException never
     */
    @Test
    public void testSetLightDuration_appropriateValues()
            throws RouteNotFoundException, InvalidOrderException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);

        List<Route> order = new ArrayList<>();
        order.add(A.getConnection(B));

        A.addTrafficLights(order, 20, 50);

        A.setLightDuration(21);
    }
/*----------------------------------------------------------------------------*/
    /**
     * Test that the addTrafficLights() method does not throw any error when
     * the duration is the traffic light's yellow time + 1
     * @throws RouteNotFoundException never
     * @throws InvalidOrderException never
     */
    @Test
    public void testAddTrafficLights_LowerValidDuration()
            throws RouteNotFoundException, InvalidOrderException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);

        List<Route> order = new ArrayList<>();
        order.add(A.getConnection(B));
       A.addTrafficLights(order, 10, 11);
    }

    /**
     * Test that the addTrafficLights() method throws an
     * IllegalArgumentException if duration == yellowTime
     * @throws RouteNotFoundException never
     * @throws InvalidOrderException never
     */
    @Test
    public void testAddTrafficLights_InvalidDurationYellowTimeEqual()
            throws RouteNotFoundException, InvalidOrderException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        boolean errorThrown = false;

        A.addConnection(B, 40);

        List<Route> order = new ArrayList<>();
        order.add(A.getConnection(B));
        try{
            A.addTrafficLights(order, 10, 10);
        } catch (IllegalArgumentException e){
            errorThrown = true;
        }

        if (!errorThrown) fail();
    }

    /**
     * Test that the addTrafficLights() method throws an
     * IllegalArgumentException if yellowTime is 0
     * @throws RouteNotFoundException never
     * @throws InvalidOrderException never
     */
    @Test
    public void testAddTrafficLight_InvalidYellowTimeIsZero()
            throws RouteNotFoundException, InvalidOrderException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        boolean errorThrown = false;

        A.addConnection(B, 40);

        List<Route> order = new ArrayList<>();
        order.add(A.getConnection(B));
        try{
            A.addTrafficLights(order, 0, 10);
        } catch (IllegalArgumentException e){
            errorThrown = true;
        }

        if (!errorThrown) fail();
    }

    /**
     * Test that the addTrafficLights() method throws an
     *  *     IllegalArgumentException if yellowTime is negative
     */
    @Test
    public void testAddTrafficLights_InvalidYellowTimeIsNegative()
            throws RouteNotFoundException, InvalidOrderException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        boolean errorThrown = false;

        A.addConnection(B, 40);

        List<Route> order = new ArrayList<>();
        order.add(A.getConnection(B));
        try{
            A.addTrafficLights(order, -10, 10);
        } catch (IllegalArgumentException e){
            errorThrown = true;
        }

        if (!errorThrown) fail();
    }

    /**
     * Test that the addTrafficLight() method adds a traffic light object to
     * each route which does not already have one.
     * @throws RouteNotFoundException never
     * @throws InvalidOrderException never
     */
    @Test
    public void testAddTrafficLight_TrafficSignalAddedForAllRoutes()
            throws RouteNotFoundException, InvalidOrderException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");
        Intersection C = new Intersection("C");
        Intersection D = new Intersection("D");

        A.addConnection(B, 40);
        A.addConnection(C, 40);
        A.addConnection(D, 40);

        A.getConnection(C).addTrafficLight();

        List<Route> order = new ArrayList<>();
        order.add(A.getConnection(B));
        order.add(A.getConnection(C));
        order.add(A.getConnection(D));
        A.addTrafficLights(order, 10, 20);

        assertNotNull(A.getConnection(B).getTrafficLight());
        assertNotNull(A.getConnection(C).getTrafficLight());
        assertNotNull(A.getConnection(D).getTrafficLight());
    }

    /**
     * Test that an instance of IntersectionLights is instantiated for the
     * network when addTrafficLights() is called.
     * @throws RouteNotFoundException never
     * @throws InvalidOrderException never
     */
    @Test
    public void testAddTrafficLights_intersectionLightsInstantiated()
            throws RouteNotFoundException, InvalidOrderException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);

        assertFalse(A.hasTrafficLights());

        List<Route> order = new ArrayList<>();
        order.add(A.getConnection(B));
        A.addTrafficLights(order, 10, 20);

        assertTrue(A.hasTrafficLights());
    }

    /**
     * Test that an order parameter which is not a permutation of the
     * incoming routes throws an InvalidOrderException.
     */
    @Test
    public void testAddTrafficLights_nonPermutationThrowsInvalidOrderException(){
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        boolean errorThrown = false;

        A.addConnection(B, 40);

        List<Route> order = new ArrayList<>();
        List<Route> newOrder = new ArrayList<>(order);
        newOrder.add(new Route(
                "routeID", new Intersection("P"), 40
        ));
        try{
            A.addTrafficLights(order, 10, 20);
        } catch (InvalidOrderException e){
            errorThrown = true;
        }

        if (!errorThrown) fail();
    }

    /**
     * Test that an empty order parameter throws an InvalidOrderException
     */
    @Test
    public void testAddTrafficLights_emptyOrderListThrowsInvalidOrderException() {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        boolean errorThrown = false;

        List<Route> order = new ArrayList<>();
        try {
            A.addTrafficLights(order, 10, 20);
        } catch (InvalidOrderException e) {
            errorThrown = true;
        }

        if (!errorThrown) fail();
    }
/*----------------------------------------------------------------------------*/
    /**
     * Test that the addConnection() method throws an IllegalStateException
     * if the route to be created already exists.
     */
    @Test
    public void testAddConnection_connectionAlreadyExistsThrowsException(){
        boolean exceptionThrown = false;

        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);

        try{
            A.addConnection(B, 42);
        } catch (IllegalStateException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that the addConnection() method throws an IllegalArgumentException
     * if the defaultSpeed parameter is negative
     */
    @Test
    public void testAddConnection_negativeDefaultSpeedThrowsException() {
        boolean errorThrown = false;

        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        try{
            A.addConnection(B, -10);
        } catch (IllegalArgumentException e){
            errorThrown = true;
        }

        if (!errorThrown) fail();
    }

    /**
     * Test that the addConnection() method creates a new route between the
     * appropriate intersections in the correct direction.
     * @throws RouteNotFoundException never
     */
    @Test
    public void testAddConnection_createsNewRoute() throws RouteNotFoundException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);
        assertEquals(40, A.getConnection(B).getSpeed());
    }

    /**
     * Test that the addConnection() method adds the route to the incoming list.
     * @throws RouteNotFoundException never
     */
    @Test
    public void testAddConnection_addsToIncoming() throws RouteNotFoundException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);

        assertTrue(A.getConnections().contains(A.getConnection(B)));
    }

    /**
     * Test that the addConnection() method instantiates a route with the
     * correct ID.
     * @throws RouteNotFoundException never
     */
    @Test
    public void testAddConnection_correctIDFormat() throws RouteNotFoundException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);

        assertEquals("B:A:40:0", A.getConnection(B).toString());
    }

    /**
     * Test that the addConnection() method instantiates a route with correct
     * default speed.
     * @throws RouteNotFoundException never
     */
    @Test
    public void testAddConnection_correctDefaultSpeed()
            throws RouteNotFoundException{
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);

        assertEquals(40, A.getConnection(B).getSpeed());
    }

    /**
     * Test that the addConnection() method adds a traffic light to the route
     * when this intersection has one
     * @throws RouteNotFoundException never
     * @throws InvalidOrderException never
     */
    @Test
    public void testAddConnection_addsTrafficLightIfExists()
            throws RouteNotFoundException, InvalidOrderException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");
        Intersection C = new Intersection("C");
        Intersection D = new Intersection("D");

        A.addConnection(B, 40);
        A.addConnection(C, 40);

        List<Route> order = new ArrayList<>();
        order.add(A.getConnection(B));
        order.add(A.getConnection(C));

        A.addTrafficLights(order, 10,20);

        A.addConnection(D, 50);

        assertNotNull(A.getConnection(B).getTrafficLight());
    }
/*----------------------------------------------------------------------------*/
    /**
     * Test that the equals() method is the same for two intersections that
     * have the same ID
     */
    @Test
    public void testEquals_sameIdentifier(){
        Intersection A = new Intersection("A");
        Intersection A2 = new Intersection("A");

        assertTrue(A.equals(A2));
    }

    /**
     * Test that the equals() method is different for two intersections with
     * differing IDs.
     */
    @Test
    public void testEquals_differentIdentifier(){
        Intersection A = new Intersection("A1");
        Intersection A2 = new Intersection("A2");

        assertFalse(A.equals(A2));
    }
/*----------------------------------------------------------------------------*/

    /**
     * Test that the hashCode() method is the same for two intersections that
     * have the same ID
     */
    @Test
    public void testHashCode_sameIdentifier(){
        Intersection A = new Intersection("A");
        Intersection A2 = new Intersection("A");

        assertEquals(A.hashCode(), A2.hashCode());
    }

    /**
     * Test that the hashCode() method is different for two intersections that
     * have differing IDs
     */
    @Test
    public void testHashCode_differentIdentifier(){
        Intersection A = new Intersection("A");
        Intersection A2 = new Intersection("A2");

        assertNotEquals(A.hashCode(), A2.hashCode());
    }

    /**
     * Test that the toString() method has the correct output for an
     * intersection with no traffic lights.
     */
    @Test
    public void testToString_noTrafficLight(){
        Intersection A = new Intersection("A");
        assertEquals("A", A.toString());
    }

    /**
     * Test that the toString() method has the correct output for an
     * intersection with a traffic light;
     * @throws RouteNotFoundException never
     * @throws InvalidOrderException never
     */
    @Test
    public void testToString_trafficLight() throws RouteNotFoundException, InvalidOrderException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");
        Intersection C = new Intersection("C");
        A.addConnection(B, 30);
        A.addConnection(C, 50);

        Route route1 = A.getConnection(B);
        Route route2 = A.getConnection(C);

        List<Route> order = new ArrayList<>();
        order.add(route1); order.add(route2);

        A.addTrafficLights(order, 10, 20);
        String expected = "A:20:B,C";
        assertEquals(expected, A.toString());
    }

    /**
     * Test that the traffic light of a route gets added to the cycle when
     * the route is created after the instantiation of the traffic light.
     * @throws RouteNotFoundException never
     * @throws InvalidOrderException never
     */
    @Test
    public void testAddingIntersectionAfterTrafficLightInstantiated() throws RouteNotFoundException, InvalidOrderException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");
        Intersection C = new Intersection("C");
        Intersection D = new Intersection("D");
        Intersection E = new Intersection("E");
        Intersection F = new Intersection("F");

        A.addConnection(B, 50);
        A.addConnection(C, 50);
        A.addConnection(D, 50);
        A.addConnection(E, 50);

        List<Route> order = new ArrayList<>();
        order.add(A.getConnection(B));
        order.add(A.getConnection(C));
        order.add(A.getConnection(D));
        order.add(A.getConnection(E));

        final int yellowTime = 12, duration = 29, greenTime =
                duration - yellowTime;
//        A.addTrafficLights(order, 9, 23);

        A.addTrafficLights(order, yellowTime, duration);

        A.addConnection(F, 30);

        assertEquals(
                order.size(), A.getConnections().size()
        );

        int ctr = 0;

        TrafficSignal state = TrafficSignal.GREEN;

        TrafficLight active = new TrafficLight();
        int timeGreen = 0, timeYellow = 0;

        do {
            TimedItemManager.getTimedItemManager().oneSecond();

            for (Route route : order) {
                if (route.getTrafficLight().getSignal() != TrafficSignal.RED) {
                    active = route.getTrafficLight();
                }
            }

            TrafficSignal activeState = active.getSignal();

            if (activeState != state){
                if (activeState == TrafficSignal.YELLOW){
                    // Changed from green
                    state = TrafficSignal.YELLOW;
                    timeYellow++;
                    assertEquals(greenTime, timeGreen);
                    timeGreen = 0;

                } else if (activeState == TrafficSignal.GREEN){
                    // Changed from yellow
                    state = TrafficSignal.GREEN;
                    timeGreen++;
                    assertEquals(yellowTime, timeYellow);
                    timeYellow = 0;
                }
            } else{
                if (activeState == TrafficSignal.GREEN){
                    timeGreen++;
                }
                else{
                    timeYellow++;
                }
            }

            ctr++;
        } while (ctr != 1000);
    }


    /*
    // Used to test permutation in development. Method has now been made
    // private to conform to assignment specification.
    @Test
    public void test_isPermutation1() throws RouteNotFoundException {
        Intersection intersection = new Intersection("intersection");
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");
        Intersection C = new Intersection("C");
        Intersection D = new Intersection("D");
        Intersection E = new Intersection("E");

        intersection.addConnection(A, 5);
        intersection.addConnection(B, 10);
        intersection.addConnection(C, 20);
        intersection.addConnection(D, 30);
        intersection.addConnection(E, 30);

        List<Route> a = new ArrayList<>();
        List<Route> b = new ArrayList<>();

        a.add(intersection.getConnection(A));
        a.add(intersection.getConnection(B));
        a.add(intersection.getConnection(C));
        a.add(intersection.getConnection(D));
        a.add(intersection.getConnection(E));

        b.add(intersection.getConnection(A));
        b.add(intersection.getConnection(B));
        b.add(intersection.getConnection(D));
        b.add(intersection.getConnection(C));
        b.add(intersection.getConnection(E));

        assertTrue(Intersection.isPermutation(a,b));
    }

    @Test
    public void test_isPermutation2() throws RouteNotFoundException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");
        Intersection C = new Intersection("C");
        Intersection D = new Intersection("D");

        A.addConnection(B, 10);
        A.addConnection(C, 20);
        A.addConnection(D, 30);

        List<Route> a = new ArrayList<>();
        List<Route> b = new ArrayList<>();

        a.add(A.getConnection(B));
        a.add(A.getConnection(C));
        a.add(A.getConnection(D));

        b.add(A.getConnection(C));
        b.add(A.getConnection(B));

        assertFalse(Intersection.isPermutation(a,b));
    }
    */



}































































