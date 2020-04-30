package tms.intersection;

import org.junit.Before;
import org.junit.Test;
import tms.route.Route;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class IntersectionLightsTest {
    IntersectionLights i = null;


    @Before
    public void setup(){
        i = null;
    }

    @Test
    public void getYellowTime() {
        List<Route> connections = new ArrayList<>();
        Intersection from = new Intersection("A");

        int yellowTime = 20;
        int duration = 30;
        connections.add(new Route("ID", from, 50));

        i = new IntersectionLights(
                connections,
                yellowTime,
                duration
        );

        assertEquals(yellowTime, i.getYellowTime());
    }

    @Test
    public void setDuration() {
        int duration = 34;
        int yellowTime = 13;

        List<Route> connections = new ArrayList<>();
        Intersection from = new Intersection("A");
        connections.add(new Route("ID", from, 50));

        i = new IntersectionLights(connections, yellowTime, duration);

        String methodDuration = i.toString().substring(0, integerLength(duration));

        assertEquals(Integer.toString(duration), methodDuration);
    }

    @Test
    public void oneSecond() {
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


        i = new IntersectionLights(
                connections,
                yellowTime,
                duration
        );

        String expected = duration + ":A,B,C,D";

        assertEquals(expected, i.toString());
    }

    /**
     * A method to return the length of an integer in characters;
     */
    private int integerLength(int integerToCompare){
        return Integer.toString(integerToCompare).length();
    }

}