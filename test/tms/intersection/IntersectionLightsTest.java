package tms.intersection;

import org.junit.Before;
import org.junit.Test;
import tms.route.Route;
import tms.route.TrafficSignal;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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

        IntersectionLights lights = new IntersectionLights(
                connections,
                7,
                15
        );
    }

    @Test
    public void testConstructor(){
        // test that the constructor properly sets the traffic light signals.

        assertEquals(TrafficSignal.GREEN,
                connections.get(0).getTrafficLight().getSignal());
        for (int i = 1; i < connections.size(); i++){
            assertEquals(TrafficSignal.RED,
                    connections.get(i).getTrafficLight().getSignal());
        }
    }

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

    @Test
    public void setDuration() {
        int duration = 34;
        int yellowTime = 13;

        List<Route> connections = new ArrayList<>();
        Intersection from = new Intersection("A");
        connections.add(new Route("ID", from, 50));

        lights = new IntersectionLights(connections, yellowTime, duration);

        String methodDuration = lights.toString().substring(0, integerLength(duration));

        assertEquals(Integer.toString(duration), methodDuration);
    }

    @Test
    public void oneSecond(){
        lights = new IntersectionLights(connections, 7, 15);

        for (int i = 0; i < 60; i++){
            lights.oneSecond();
        }
    }

    @Test
    public void oneSecond1() {
        int yellowTime = 7;
        int duration = 15;
        int greenTime = 8;

        assertEquals(4, connections.size());

        lights = new IntersectionLights(
              connections,
              yellowTime, duration
        );

        for (int time = 0; time < 120; time++){
            lights.oneSecond();
            if (0 <= time && time <= 7){
                // We should expect GRRR
                assertLights(0, TrafficSignal.GREEN, time);
            }

            else if (8 <= time && time <=14){
                // We should expect YRRR
                assertLights(0, TrafficSignal.YELLOW, time);
            }

            else if (15 <= time && time <= 22){
                // We should expect RGRR
                assertLights(1, TrafficSignal.GREEN, time);
            }

            else if (23 <= time && time <= 29){
                // We should expect RYRR
                assertLights(1, TrafficSignal.YELLOW, time);
            }

            else if (30 <= time && time <= 37){
                // We should expect RRGR
                assertLights(2, TrafficSignal.GREEN, time);
            }

            else if (38 <= time && time <= 44){
                // We should expect RRYR
                assertLights(2, TrafficSignal.YELLOW, time);
            }

            else if (45 <= time && time < 52){
                // We should expect RRRG
                assertLights(3, TrafficSignal.GREEN, time);
            }

            else if (53 <= time && time < 59){
                assertLights(3, TrafficSignal.YELLOW, time);
            }

        }
    }

    @Test
    public void testToString() {
        List<Route> connections = new ArrayList<>();
        List<Intersection> intersections = new ArrayList<>();
        Intersection from = new Intersection("A");
        Intersection from2 = new Intersection("B");
        Intersection from3 = new Intersection("C");
        Intersection from4 = new Intersection("D");

        int yellowTime = 20;
        int duration = 30;
        connections.add(new Route("ID", from, 50));
        connections.add(new Route("ID2", from2, 60));
        connections.add(new Route("ID3", from3, 70));
        connections.add(new Route("ID4", from4, 80));

        intersections.add(from);
        intersections.add(from2);
        intersections.add(from3);
        intersections.add(from4);


        lights = new IntersectionLights(
                connections,
                yellowTime,
                duration
        );

        String expected = duration + ":A,B,C,D";

        assertEquals(expected, lights.toString());
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
    private void assertLights(int activeIndex, TrafficSignal signal, int time){
        for (int i = 0; i < connections.size(); i++){
            if (i == activeIndex){
                assertEquals("ActiveIndex has incorrect value at time " + time,
                        signal,
                        connections.get(i).getTrafficLight().getSignal());
            }

            else{
                assertEquals("Index " + i + " has incorrect value at time " + time + " using AI of " + activeIndex,
                        TrafficSignal.RED,
                        connections.get(i).getTrafficLight().getSignal());
            }
        }
    }

}