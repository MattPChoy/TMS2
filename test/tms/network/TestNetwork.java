package tms.network;

import org.junit.Before;
import org.junit.Test;
import tms.intersection.Intersection;
import tms.route.Route;
import tms.sensors.DemoPressurePad;
import tms.sensors.DemoSpeedCamera;
import tms.sensors.DemoVehicleCount;
import tms.sensors.SpeedCamera;
import tms.util.DuplicateSensorException;
import tms.util.IntersectionNotFoundException;
import tms.util.InvalidOrderException;
import tms.util.RouteNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestNetwork {
    private Network n;
    final String LINE_BREAK = System.lineSeparator();

    @Before
    public void setup(){
        n = new Network();
    }
/*----------------------------------------------------------------------------*/
    @Test
    public void testNetworkConstructor_emptyNetwork(){
        assertEquals(0, n.getIntersections().size());
    }
/*----------------------------------------------------------------------------*/
    @Test
    public void testYellowTime_IllegalArgumentException1(){
        boolean exceptionThrown = false;
        boolean exceptionThrown2 = false;

        try{
            n.setYellowTime(0);
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }

        try{
            n.setYellowTime(-1);
        } catch (IllegalArgumentException e){
            exceptionThrown2 = true;
        }

        if (!exceptionThrown && !exceptionThrown2){
            fail();
        }
    }

    @Test
    public void setYellowTime(){
        n.setYellowTime(10);

        assertEquals(10, n.getYellowTime());
    }

    @Test
    public void setYellowTime_existingUnedited() throws IntersectionNotFoundException, InvalidOrderException, RouteNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");
        n.connectIntersections("A", "B", 30);

        List<String> order = new ArrayList<>();
        order.add("A");

        n.addLights("B", 20, order);


        assertEquals(1, n.getYellowTime());

    }

    @Test
    public void testGetYellowTime_notInstantiated(){
        assertEquals(1, n.getYellowTime());
    }
/*----------------------------------------------------------------------------*/
    @Test
    public void testAddLights_intersectionNotFoundException()
            throws InvalidOrderException, RouteNotFoundException {
        List<String> intersectionOrder = new ArrayList<>();
        intersectionOrder.add("A");

        boolean exceptionThrown = false;

        try{
            n.addLights("A", 20, intersectionOrder);
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();

    }

    @Test
    public void testAddLights_invalidOrderException_emptyList() throws IntersectionNotFoundException, RouteNotFoundException {
        n.createIntersection("A");
        boolean exceptionThrown = false;
        List<String> intersectionOrder = new ArrayList<>();

        try{
            n.addLights("A", 20, intersectionOrder);
        } catch (InvalidOrderException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void testAddLights_invalidOrderException_notPermutation()
            throws IntersectionNotFoundException, RouteNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("to");
        n.createIntersection("from");
        n.createIntersection("a");

        n.connectIntersections("from", "to", 50);

        List<String> intersectionOrder = new ArrayList<>();
        intersectionOrder.add("from"); intersectionOrder.add("a");

        try{
            n.addLights("to", 20, intersectionOrder);
        } catch (InvalidOrderException e){
            exceptionThrown = true;
        }

//        if (!exceptionThrown) fail();
    }

    @Test
    public void testAddLights_illegalDurationValue() throws IntersectionNotFoundException, RouteNotFoundException, InvalidOrderException        {
        boolean exceptionThrown = false;

        n.setYellowTime(10);
        n.createIntersection("from");
        n.createIntersection("to");
        n.connectIntersections("from", "to", 50);

        List<String> intersectionOrder = new ArrayList<>();
        intersectionOrder.add("from");
        try {
            n.addLights("to", 10, intersectionOrder);
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }
/*----------------------------------------------------------------------------*/
    @Test
    public void createIntersection_testAppropriateValues() throws IntersectionNotFoundException {
        n.createIntersection("A");

        assertEquals("A", n.getIntersection("A").toString());
    }

    @Test
    public void createIntersection_testIntersectionExists(){
        n.createIntersection("A");
        boolean exceptionThrown = false;

        try{
            n.createIntersection("A");
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) {
            fail();
        }
    }

    @Test
    public void createIntersection_containsDelimiter(){
        boolean exceptionThrown = false;
        try{
            n.createIntersection("A:B");
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) {
            fail();
        }
    }

    @Test
    public void createIntersection_onlyWhitespace_spaceCharacter(){
        boolean exceptionThrown = false;
        boolean exceptionThrown1 = false;

        try{
            n.createIntersection(" ");
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }

        try{
            n.createIntersection("  ");
        } catch (IllegalArgumentException e){
            exceptionThrown1 = true;
        }

        if (!exceptionThrown && !exceptionThrown1) {
            fail();
        }
    }

    @Test
    public void createIntersection_containsWhitespace_doesNotFail(){
        boolean exceptionThrown = false;

        try{
            n.createIntersection("a ");
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }

        if (exceptionThrown){
            fail();
        }
    }

    @Test
    public void createIntersection_onlyWhitespace_tabCharacter(){
        boolean exceptionThrown = false;
        boolean exceptionThrown1 = false;

        try{
            n.createIntersection("\t");
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }

        try{
            n.createIntersection("\t\t");
        } catch (IllegalArgumentException e){
            exceptionThrown1 = true;
        }

        if (!exceptionThrown && !exceptionThrown1) {
            fail();
        }
    }

    @Test
    public void createIntersection_containsTabCharacter_doesNotFail(){
        boolean exceptionThrown = false;

        try{
            n.createIntersection("a\t");
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }

        if (exceptionThrown){
            fail();
        }
    }

    @Test
    public void createIntersection_onlyWhitespace_CRLF(){
        boolean exceptionThrown = false;

        try{
            n.createIntersection("\n\r");
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown){
            fail();
        }
    }

    @Test
    public void createIntersection_containsCRLF_doesNotFail(){
        boolean exceptionThrown = false;

        try{
            n.createIntersection("p\n\r");
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }

        if (exceptionThrown){
            fail();
        }
    }
/*----------------------------------------------------------------------------*/
    @Test
    public void connectIntersection_throwsNoErrors() throws IntersectionNotFoundException, RouteNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 50);

        assertEquals(50, n.getRoute("A", "B").getSpeed());
    }

    @Test
    public void connectIntersection_throwsIntersectionNotFoundException(){
        n.createIntersection("A");
        boolean exceptionThrown = false;

        try{
            n.connectIntersections("A", "B", 50);
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown){
            fail();
        }
    }

    @Test
    public void connectIntersection_throwsIntersectionNotFoundException1(){
        n.createIntersection("B");
        boolean exceptionThrown = false;

        try{
            n.connectIntersections("A", "B", 50);
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown){
            fail();
        }
    }

    @Test // If route already exists
    public void connectIntersection_throwsIllegalStateException() throws IntersectionNotFoundException {
        // When route already exists
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 50);

        boolean exceptionThrown = false;

        try{
            n.connectIntersections("A", "B", 20);
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown){
            fail();
        }
    }

    @Test // negative speed
    public void connectIntersection_throwsIllegalArgumentException(){
        n.createIntersection("A");
        n.createIntersection("B");

        boolean exceptionThrown = false;

        try{
            n.connectIntersections("A", "B", -50);
        } catch (IllegalArgumentException | IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown){
            fail();
        }
    }
/*----------------------------------------------------------------------------*/
    @Test
    public void addSpeedSign_setsInitialSpeed() throws IntersectionNotFoundException, RouteNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 40);

        n.addSpeedSign("A", "B", 30);

        assertEquals(30, n.getConnection("A", "B").getSpeed());
    }

    @Test
    public void addSpeedSign_throwsIntersectionNotFoundException(){
        boolean exceptionThrown = false;

        try{
            n.connectIntersections("A", "B", 40);
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void addSpeedSign_throwsRouteNotFoundException() throws IntersectionNotFoundException, RouteNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        try{
            n.addSpeedSign("A", "B", 30);
        } catch (RouteNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void addSpeedSign_throwsIllegalArgumentException() throws IntersectionNotFoundException, RouteNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 40);

        try{
            n.addSpeedSign("A", "B", -20);
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }
/*----------------------------------------------------------------------------*/
    @Test
    public void setSpeedLimit_throwsRouteNotFoundException() throws IntersectionNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        try{
            n.setSpeedLimit("A", "B", 20);
        } catch (RouteNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void setSpeedLimit_throwsIntersectionNotFoundException1() throws RouteNotFoundException{
        boolean exceptionThrown = false;

        n.createIntersection("A");

        try{
            n.setSpeedLimit("A", "B", 20);
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void setSpeedLimit_throwsIntersectionNotFoundException2() throws RouteNotFoundException{
        boolean exceptionThrown = false;

        n.createIntersection("B");

        try{
            n.setSpeedLimit("A", "B", 20);
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void setSpeedLimit_throwsIllegalStateException() throws IntersectionNotFoundException, RouteNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections( "A", "B", 50);

        try{
            n.setSpeedLimit("A", "B", 20);
        } catch (IllegalStateException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void setSpeedLimit_throwsIllegalArgumentException() throws IntersectionNotFoundException, RouteNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections( "A", "B", 50);
        n.addSpeedSign("A", "B", 30);

        try{
            n.setSpeedLimit("A", "B", -20);
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }
/*----------------------------------------------------------------------------*/
    @Test
    public void changeLightDuration_throwsIntersectionNotFoundException() {

        boolean exceptionThrown = false;

        try{
            n.changeLightDuration("A", 10);
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void changeLightDuration_throwsIllegalStateException() throws IntersectionNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 40);

        try{
            n.changeLightDuration("A", 10);
        } catch (IllegalStateException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void changeLightDuration_throwsIllegalArgumentException() throws IntersectionNotFoundException, InvalidOrderException, RouteNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("from");
        n.createIntersection("to");

        n.connectIntersections("from", "to", 40);

        List<String> order = new ArrayList<>();
        order.add("from");
        n.addLights("to", 10, order);
        n.setYellowTime(10);

        try{
            n.changeLightDuration("to", 10);
        } catch (IllegalArgumentException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }
/*----------------------------------------------------------------------------*/
    @Test
    public void getConnection_throwsIntersectionNotFoundException(){
        boolean exceptionThrown = false;

        try {
            n.getConnection("A", "B");
        } catch (IntersectionNotFoundException | RouteNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void getConnection_throwsRouteNotFoundException() throws IntersectionNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        try{
            n.getConnection("A", "B");
        } catch (RouteNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void getConnection_appropriateValue() throws IntersectionNotFoundException, RouteNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 50);

        assertEquals("A:B:50:0", n.getConnection("A", "B").toString());
    }
/*----------------------------------------------------------------------------*/
    @Test
    public void addSensor_throwsDuplicateSensorException() throws IntersectionNotFoundException, DuplicateSensorException, RouteNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 50);

        n.addSensor("A", "B", new DemoSpeedCamera(new int[] {1}, 40));

        try{
            n.addSensor("A", "B", new DemoSpeedCamera(new int[] {1}, 40));
        } catch (DuplicateSensorException d){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void addSensor_throwsRouteNotFoundException() throws IntersectionNotFoundException, DuplicateSensorException {
        boolean exceptionThrown = false;
        n.createIntersection("A");
        n.createIntersection("B");
        try {
            n.addSensor("A", "B", new DemoSpeedCamera(new int[]{1}, 40));
        } catch (RouteNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void addSensor_throwsIntersectionNotFoundException() throws IntersectionNotFoundException, DuplicateSensorException, RouteNotFoundException {
        boolean exceptionThrown = false;
        try {
            n.addSensor("A", "B", new DemoSpeedCamera(new int[]{1}, 40));
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void addSensor_appropriateValues() throws IntersectionNotFoundException, DuplicateSensorException, RouteNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 50);

        n.addSensor("A", "B", new DemoSpeedCamera(new int[] {1,2,3}, 40));

        assertEquals("SC:40:1,2,3",
                n.getRoute("A", "B").getSensors().get(0).toString());
    }
/*----------------------------------------------------------------------------*/
    @Test
    public void getCongestion_appropriateBounds1()
            throws IntersectionNotFoundException, DuplicateSensorException,
            RouteNotFoundException {

        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 50);
        n.addSensor("A", "B", new DemoSpeedCamera(new int[] {1,2,3}, 40));

        assertTrue(0 <= n.getCongestion("A", "B") && n.getCongestion("A","B") <= 100);
    }

    @Test
    public void getCongestion_appropriateBounds2()
            throws IntersectionNotFoundException, DuplicateSensorException,
            RouteNotFoundException {

        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 50);
        n.addSensor("A", "B", new DemoSpeedCamera(new int[] {-100,2,3}, 40));

        assertTrue(0 <= n.getCongestion("A", "B") && n.getCongestion("A","B") <= 100);
    }

    @Test
    public void getCongestion_appropriateBounds3()
            throws IntersectionNotFoundException, DuplicateSensorException,
            RouteNotFoundException {

        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 50);
        n.addSensor("A", "B", new DemoSpeedCamera(new int[] {4000,2,3}, 40));

        assertTrue(0 <= n.getCongestion("A", "B") && n.getCongestion("A","B") <= 100);
    }
/*----------------------------------------------------------------------------*/
    @Test
    public void findIntersection_throwsIntersectionNotFoundException(){
        boolean exceptionThrown = false;

        try{
            n.findIntersection("A");
        } catch(IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void findIntersection_appropriateValues() throws IntersectionNotFoundException {
        n.createIntersection("A");
        assertTrue(n.findIntersection("A").getId().equals("A"));
    }
/*----------------------------------------------------------------------------*/
    @Test
    public void makeTwoWay_throwsIntersectionNotFoundException() throws RouteNotFoundException {
        boolean exceptionThrown = false;

        try{
            n.makeTwoWay("A", "B");
        } catch(IntersectionNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void makeTwoWay_throwsRouteNotFoundException() throws IntersectionNotFoundException{
        boolean exceptionThrown = false;
        n.createIntersection("A");
        n.createIntersection("B");

        try{
            n.makeTwoWay("A", "B");
        } catch(RouteNotFoundException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test // Opposing route already exists.
    public void makeTwoWay_throwsIllegalStateException() throws IntersectionNotFoundException, RouteNotFoundException {
        boolean exceptionThrown = false;
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 50);
        n.connectIntersections( "B", "A",50);

        try{
            n.makeTwoWay("A", "B");
        } catch(IllegalStateException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void makeTwoWay_appropriateValuesCreatesRoute() throws IntersectionNotFoundException, RouteNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");
        n.connectIntersections("A", "B", 40);

        n.makeTwoWay("A", "B");

        Route originalRoute = n.getRoute("A", "B");
        Route opposingRoute = n.getRoute("B", "A");

        assertEquals("B:A:40:0", opposingRoute.toString());
        assertFalse(originalRoute.hasSpeedSign());
        assertFalse(opposingRoute.hasSpeedSign());
        assertEquals(originalRoute.getSpeed(), opposingRoute.getSpeed());
    }

    @Test
    public void makeTwoWay_instantiatesSpeedSign() throws IntersectionNotFoundException, RouteNotFoundException {
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 40);
        assertEquals(40, n.getRoute("A","B").getSpeed());
        n.addSpeedSign("A", "B", 50);
        assertEquals(50, n.getRoute("A","B").getSpeed());
        assertTrue(n.getRoute("A", "B").hasSpeedSign());

        n.makeTwoWay("A", "B");
        assertTrue(n.getRoute("B", "A").hasSpeedSign());
        assertEquals(50, n.getRoute("B", "A").getSpeed());
    }

    @Test
    public void equals_trueForIdenticalNetwork(){
        Network one = new Network();
        Network two = new Network();

        assertTrue(one.equals(two));

        one.createIntersection("A");
        two.createIntersection("A");

        assertTrue(one.equals(two));

        one.createIntersection("B");
        two.createIntersection("B");

        assertTrue(one.equals(two));

        one.createIntersection("C");

        assertFalse(one.equals(two));
    }

    @Test
    public void hashCode_trueForIdenticalNetwork(){
        Network one = new Network();
        Network two = new Network();

        assertEquals(one.hashCode(), two.hashCode());

        one.createIntersection("A");
        two.createIntersection("A");

        assertEquals(one.hashCode(), two.hashCode());

        one.createIntersection("B");
        two.createIntersection("B");

        assertEquals(one.hashCode(), two.hashCode());

        one.createIntersection("C");

        assertNotEquals(one.hashCode(), two.hashCode());
    }

    @Test
    public void toString_emptyNetwork(){
        System.out.println(n.toString());
    }

    @Test
    public void toString_singleIntersection(){

        n.createIntersection("A");
        String expected =
                "1" + LINE_BREAK + "0" + LINE_BREAK + "1" + LINE_BREAK + "A"
                        + LINE_BREAK;

        assertEquals(expected, n.toString());
    }

    @Test
    public void toString_singleIntersectionSetYellowTime(){
        n.createIntersection("intersection");
        n.setYellowTime(10);
        String expected =
                "1" + LINE_BREAK + "0" + LINE_BREAK + "10" + LINE_BREAK +
                        "intersection"
                        + LINE_BREAK;
        assertEquals(expected, n.toString());
    }

    @Test
    public void toString_singleRoute() throws IntersectionNotFoundException {
        n.createIntersection("intersection1");
        n.createIntersection("intersection2");
        n.connectIntersections("intersection1", "intersection2", 50);
        n.setYellowTime(10);
        String expected =
                "2" + LINE_BREAK +    // Number of intersections
                "1" + LINE_BREAK +    // Number of routes
                "10" + LINE_BREAK +   // YellowTime
                "intersection1" + LINE_BREAK +
                "intersection2" + LINE_BREAK +
                "intersection1:intersection2:50:0" + LINE_BREAK;
        assertEquals(expected, n.toString());
    }

    @Test
    public void toString_complexRoute() throws IntersectionNotFoundException,
            DuplicateSensorException, RouteNotFoundException {
        n.createIntersection("intersectionA");
        n.createIntersection("intersectionB");
        n.createIntersection("intersectionC");
        n.createIntersection("intersectionD");
        n.createIntersection("intersectionE");
        n.createIntersection("intersectionF");
        n.createIntersection("Origin");

        n.setYellowTime(12);

        n.connectIntersections("intersectionA" , "Origin", 50);
        n.connectIntersections("intersectionB" , "Origin", 50);
        n.connectIntersections("intersectionC" , "Origin", 50);
        n.connectIntersections("intersectionD" , "Origin", 50);
        n.connectIntersections("intersectionE" , "Origin", 50);
        n.connectIntersections("intersectionF" , "Origin", 50);

        n.addSensor("intersectionC", "Origin", new DemoVehicleCount(
                new int[] {1,2,3,4,5,6,7,8}, 40
        ));
        n.addSensor("intersectionF", "Origin", new DemoPressurePad(
                new int[] {1,2,3,4,5,6,7,8}, 40
        ));

        String expected =
                "7" + LINE_BREAK +
                        "6" + LINE_BREAK +
                        "12" + LINE_BREAK +
                        "intersectionA" + LINE_BREAK +
                        "intersectionB" + LINE_BREAK +
                        "intersectionC" + LINE_BREAK +
                        "intersectionD" + LINE_BREAK +
                        "intersectionE" + LINE_BREAK +
                        "intersectionF" + LINE_BREAK +
                        "Origin" + LINE_BREAK +

                        "intersectionA:Origin:50:0" + LINE_BREAK +
                        "intersectionB:Origin:50:0" + LINE_BREAK +
                        "intersectionC:Origin:50:1" + LINE_BREAK +
                        "VC:40:1,2,3,4,5,6,7,8"     + LINE_BREAK +
                        "intersectionD:Origin:50:0" + LINE_BREAK +
                        "intersectionE:Origin:50:0" + LINE_BREAK +
                        "intersectionF:Origin:50:1" + LINE_BREAK +
                        "PP:40:1,2,3,4,5,6,7,8"     + LINE_BREAK;

        assertEquals(expected, n.toString());


    }

    @Test
    public void toString_givenNetwork() throws IntersectionNotFoundException,
            DuplicateSensorException, RouteNotFoundException, InvalidOrderException {
        n.createIntersection("W");
        n.createIntersection("X");
        n.createIntersection("Y");
        n.createIntersection("Z");

        n.connectIntersections("X", "Y", 60);
        n.connectIntersections("Y", "X", 60);
        n.addSensor("Y", "X", new DemoPressurePad(
                new int[] {5,2,4,4,1,5,2,7,3,5,6,5,8,5,4,2,3,3,2,5},
                5
        ));
        n.connectIntersections("Y", "Z", 100);
        n.addSensor("Y", "Z", new DemoPressurePad(
                new int[] {1,3,2,1,1,3,4,7,4,7,9,7,8,4,8,8,5,3,2,2},
                8
        ));
        n.addSensor("Y", "Z", new DemoVehicleCount(
                new int[] {42,40,37,34,35,31,36,41,41,47,48,50,53,48,54,58,52
                        ,52,61,55},
                50
                )
        );
        n.connectIntersections("Z", "X", 40);
        n.addSensor("Z", "X", new DemoSpeedCamera(
                new int[] {39,40,40,40,36,32,25,28,31,39,40,40,40,40,40,40,36,35
                        ,39,40},
                40
        ));
        n.connectIntersections("Z", "Y", 100);
        n.addSpeedSign("Z","Y", 80);

        List<String> order = new ArrayList<>();
        order.add("Z"); order.add("X");
        n.addLights("Y", 3, order);

        System.out.println(n.toString());

        String expected = "4" + LINE_BREAK +
                "5" + LINE_BREAK +
                "1" + LINE_BREAK +
                "W" + LINE_BREAK +
                "X" + LINE_BREAK +
                "Y:3:Z,X" + LINE_BREAK +
                "Z" + LINE_BREAK +
                "X:Y:60:0" + LINE_BREAK +
                "Y:X:60:1" + LINE_BREAK +
                "PP:5:5,2,4,4,1,5,2,7,3,5,6,5,8,5,4,2,3,3,2,5" + LINE_BREAK +
                "Y:Z:100:2" + LINE_BREAK +
                "PP:8:1,3,2,1,1,3,4,7,4,7,9,7,8,4,8,8,5,3,2,2" + LINE_BREAK +
                "VC:50:42,40,37,34,35,31,36,41,41,47,48,50,53,48,54,58,52,52," +
                "61,55" + LINE_BREAK +
                "Z:X:40:1" + LINE_BREAK +
                "SC:40:39,40,40,40,36,32,25,28,31,39,40,40,40,40,40,40,36,35,39,40" + LINE_BREAK +
                "Z:Y:100:0:80" + LINE_BREAK;

        assertEquals(expected, n.toString());
    }
}
