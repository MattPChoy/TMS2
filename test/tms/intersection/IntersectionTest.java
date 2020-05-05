package tms.intersection;

import org.junit.Test;
import tms.route.Route;
import tms.route.TrafficLight;
import tms.util.InvalidOrderException;
import tms.util.RouteNotFoundException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class IntersectionTest {
    @Test
    public void testRoute1(){
        Intersection from = new Intersection("A");
        Route r1 = new Route("A:B", from, 50);
        Route r2 = r1;

        assertTrue(r1 == r2);
    }

    @Test
    public void testHasTrafficLights_false(){
        Intersection A = new Intersection("A");
        assertFalse(A.hasTrafficLights());
    }

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

    @Test
    public void testSetLightDuration_throwsIllegalArgumentException() throws RouteNotFoundException, InvalidOrderException {
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

    @Test
    public void testSetLightDuration_doesNotThrowError() throws RouteNotFoundException, InvalidOrderException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);

        List<Route> order = new ArrayList<>();
        order.add(A.getConnection(B));

        A.addTrafficLights(order, 20, 50);

        A.setLightDuration(30);
    }

    @Test
    public void testAddTrafficLights_InvalidYellowTime() throws RouteNotFoundException, InvalidOrderException {
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

    @Test
    public void testAddTrafficLights_InvalidDuration() throws RouteNotFoundException, InvalidOrderException {
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

    @Test
    public void testAddTrafficLights_trafficSignalAdded() throws RouteNotFoundException, InvalidOrderException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);

        List<Route> order = new ArrayList<>();
        order.add(A.getConnection(B));
        A.addTrafficLights(order, 10, 20);

        assertNotNull(A.getConnection(B).getTrafficLight());
    }

    @Test
    public void testAddTrafficLights_intersectionLightsInstantiated() throws RouteNotFoundException, InvalidOrderException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);

        List<Route> order = new ArrayList<>();
        order.add(A.getConnection(B));
        A.addTrafficLights(order, 10, 20);

        assertTrue(A.hasTrafficLights());
    }

    @Test
    public void testAddTrafficLights_nonPermutationThrowsInvalidOrderException(){
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        boolean errorThrown = false;

        A.addConnection(B, 40);

        List<Route> order = new ArrayList<>();
        order.add(new Route(
                "routeID", new Intersection("P"), 40
        ));
        try{
            A.addTrafficLights(order, 10, 20);
        } catch (InvalidOrderException e){
            errorThrown = true;
        }

        if (!errorThrown) fail();
    }

    @Test
    public void testAddTrafficLights_emptyOrderListThrowsInvalidOrderException(){
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        boolean errorThrown = false;

        List<Route> order = new ArrayList<>();
        try{
            A.addTrafficLights(order, 10, 20);
        } catch (InvalidOrderException e){
            errorThrown = true;
        }

        if (!errorThrown) fail();
    }

    @Test
    public void testAddConnection_createsNewRoute() throws RouteNotFoundException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);
        assertEquals(40, A.getConnection(B).getSpeed());
    }

    @Test
    public void testAddConnection_addsToIncoming() throws RouteNotFoundException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);

        assertTrue(A.getConnections().contains(A.getConnection(B)));
    }

    @Test
    public void testAddConnection_correctIDFormat() throws RouteNotFoundException {
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);

        assertEquals("B:A:40:0", A.getConnection(B).toString());
    }

    @Test
    public void testAddConnection_correctDefaultSpeed() throws RouteNotFoundException{
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");

        A.addConnection(B, 40);

        assertEquals(40, A.getConnection(B).getSpeed());
    }

    @Test
    public void testAddConnection_addsTrafficLightIfExists() throws RouteNotFoundException, InvalidOrderException {
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

    @Test
    public void testEquals_sameIdentifier(){
        Intersection A = new Intersection("A");
        Intersection A2 = new Intersection("A");

        assertTrue(A.equals(A2));
    }

    @Test
    public void testEquals_differentIdentifier(){
        Intersection A = new Intersection("A1");
        Intersection A2 = new Intersection("A2");

        assertFalse(A.equals(A2));
    }

    @Test
    public void testHashCode_sameIdentifier(){
        Intersection A = new Intersection("A");
        Intersection A2 = new Intersection("A");

        assertEquals(A.hashCode(), A2.hashCode());
    }

    @Test
    public void testToString_noTrafficLight(){
        Intersection A = new Intersection("A");
        Intersection B = new Intersection("B");



        assertEquals("A", A.toString());
    }

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
}