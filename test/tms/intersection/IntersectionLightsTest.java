package tms.intersection;

import org.junit.Before;
import org.junit.Test;
import tms.route.Route;
import tms.route.TrafficLight;
import tms.route.TrafficSignal;
import tms.util.InvalidOrderException;
import tms.util.RouteNotFoundException;
import tms.util.TimedItemManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
/**
 * Class used for testing the validity of the implemented
 * Intersection. The following tests are implemented:
 *
 *
 *  1)Test that the constructor:
 *      i) Sets the first trafficLight's signal to TrafficSignal.GREEN
 *     ii) Sets the rest of the trafficLights' signal to TrafficSignal.REDa
 *  2) Test that the getYellowTime() method returns the correct value for
 *     yellow time.
 *  3) Test that the setDuration() method correctly sets the duration
 *  4) Test that the setDuration() method resets the cycle of the traffic lights
 *  5) Test that the oneSecond() method correctly cycles the traffic lights
 *     as per the rules provided in the specification.
 *  6) Test that the traffic light of a route gets added to the cycle when
 *     the route is created after the instantiation of the traffic light.
 *  7) A method to return the length of an integer in characters;
 *  8) A method used for the oneSecond test method. Used to assert that a
 *     given light has a certain signal and the other lights are red.
 */
public class IntersectionLightsTest {
    IntersectionLights lights;
    List<Route> connections = new ArrayList<>();

    @Before
    public void setup(){
        lights = null;

        connections.add(
                new Route("ID1", new Intersection("A"), 30)
        );
        connections.add(
                new Route("ID2", new Intersection("B"), 30)
        );
        connections.add(
                new Route("ID3", new Intersection("C"), 30)
        );
        connections.add(
                new Route("ID4", new Intersection("D"), 30)
        );

        for (Route r: connections){
            r.addTrafficLight();
        }
    }

    /**
     * Test that the constructor:
     *   i) Sets the first trafficLight's signal to TrafficSignal.GREEN
     *  ii) Sets the rest of the trafficLights' signal to TrafficSignal.REDa
     */
    @Test
    public void testConstructor(){
        // test that the constructor properly sets the traffic light signals.

        lights = new IntersectionLights(connections, 10, 20);
        assertEquals(TrafficSignal.GREEN,
                connections.get(0).getTrafficLight().getSignal());

        for (int i = 1; i < connections.size(); i++){
            assertEquals(TrafficSignal.RED,
                    connections.get(i).getTrafficLight().getSignal());
        }
    }

    /**
     * Test that the getYellowTime() method returns the correct value for
     * yellow time.
     */
    @Test
    public void getYellowTime() {
        List<Route> connections = new ArrayList<>();
        Intersection from = new Intersection("A");

        int yellowTime = 20;
        int duration = 30;
        connections.add(new Route("ID", from, 50));

        lights = new IntersectionLights(
                connections,
                yellowTime,
                duration
        );

        assertEquals(yellowTime, lights.getYellowTime());
    }

    /**
     * Test that the setDuration() method correctly sets the duration
     */
    @Test
    public void testSetDuration_setsCorrectValue() {
        int yellowTime = 7;
        int duration = 10;

        assertEquals(4, connections.size());

        lights = new IntersectionLights(
                connections,
                yellowTime, duration
        );

        String methodDuration = lights.toString().substring(0, integerLength(duration));
        assertEquals(Integer.toString(duration), methodDuration);

        lights.setDuration(15);

        methodDuration = lights.toString().substring(0, integerLength(duration));
        assertEquals(Integer.toString(15), methodDuration);
    }

    /**
     * Test that the setDuration() method resets the cycle of the traffic lights
     */
    @Test
    public void testSetDuration_resetsCycle() {
        int yellowTime = 7;
        int duration = 10;

        assertEquals(4, connections.size());

        lights = new IntersectionLights(
                connections,
                yellowTime, duration
        );

        for (int i = 0; i < duration*1.5; i++){
            TimedItemManager.getTimedItemManager().oneSecond();
        }

        assertNotEquals(TrafficSignal.GREEN,
                connections.get(0).getTrafficLight().getSignal());

        lights.setDuration(15);

        assertEquals(TrafficSignal.GREEN,
                connections.get(0).getTrafficLight().getSignal());
    }

    /**
     * Test that the oneSecond() method correctly cycles the traffic lights
     * as per the rules provided in the specification.
     */
    @Test
    public void testOneSecond_fourConnections(){
        int yellowTime = 7;
        int duration = 15;

        assertEquals(4, connections.size());

        lights = new IntersectionLights(
                connections,
                yellowTime, duration
        );

        for (int time = 0; time < 120; time++) {
            lights.oneSecond();
            if (0 <= time && time <= 7) {
                // We should expect GRRR
                assertLights(0, TrafficSignal.GREEN);
            } else if (8 <= time && time <= 14) {
                // We should expect YRRR
                assertLights(0, TrafficSignal.YELLOW);
            } else if (15 <= time && time <= 22) {
                // We should expect RGRR
                assertLights(1, TrafficSignal.GREEN);
            } else if (23 <= time && time <= 29) {
                // We should expect RYRR
                assertLights(1, TrafficSignal.YELLOW);
            } else if (30 <= time && time <= 37) {
                // We should expect RRGR
                assertLights(2, TrafficSignal.GREEN);
            } else if (38 <= time && time <= 44) {
                // We should expect RRYR
                assertLights(2, TrafficSignal.YELLOW);
            } else if (45 <= time && time < 52) {
                // We should expect RRRG
                assertLights(3, TrafficSignal.GREEN);
            } else if (53 <= time && time < 59) {
                assertLights(3, TrafficSignal.YELLOW);
            }
        }
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

    /**
     * A method to return the length of an integer in characters;
     */
    private int integerLength(int integerToCompare){
        return Integer.toString(integerToCompare).length();
    }

    /**
     * A method used for the oneSecond test method. Used to assert that a
     * given light has a certain signal and the other lights are red.
     */
    private void assertLights(int activeIndex, TrafficSignal signal){
        for (int i = 0; i < connections.size(); i++) {
            if (i == activeIndex) {
                assertEquals(signal,
                        connections.get(i).getTrafficLight().getSignal());
            } else {
                assertEquals(TrafficSignal.RED,
                        connections.get(i).getTrafficLight().getSignal());
            }
        }
    }

}