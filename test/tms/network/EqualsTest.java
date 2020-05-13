package tms.network;

import org.junit.Test;
import tms.sensors.DemoSpeedCamera;
import tms.util.DuplicateSensorException;
import tms.util.IntersectionNotFoundException;
import tms.util.RouteNotFoundException;

import static org.junit.Assert.*;

/**
 * 63) Test that equals() returns the same value if routes have the same:
 *      a) Same number of intersections
 *      b) Intersections must be permutations of each other.
 *  64) Test that equals() returns a different value for 'non-equal'
 *      networks.
 *  65) Test that hashCode() returns the same value for 'equal' networks.
 *  66) Test that two different intersections have different hashCodes().
 */
public class EqualsTest {
    /**
     * Test that equals() returns the same value if routes have the same:
     *   a) Same number of intersections
     *   b) Intersections must be permutations of each other.
     * @throws IntersectionNotFoundException never
     * @throws DuplicateSensorException never
     * @throws RouteNotFoundException never
     */
    @Test
    public void equals_sameForEqualNetworks()
            throws IntersectionNotFoundException, DuplicateSensorException,
            RouteNotFoundException {
        Network networkA = new Network();
        Network networkB = new Network();

        assertEquals(networkA, networkB);

        networkA.createIntersection("A");
        networkB.createIntersection("A");
        networkA.createIntersection("B");
        networkB.createIntersection("B");

        assertEquals(networkA, networkB);

        networkA.connectIntersections("A", "B", 40);
        networkB.connectIntersections("A", "B", 40);

        assertEquals(networkA, networkB);

        networkA.addSensor("A", "B", new DemoSpeedCamera(
                new int[] {1,2,3,4}, 40
        ));

        networkB.addSensor("A", "B", new DemoSpeedCamera(
                new int[] {1,2,3,4}, 40
        ));

        assertEquals(networkA, networkB);

        networkA.addSpeedSign("A", "B", 90);
        networkB.addSpeedSign("A", "B", 90);

        networkA.createIntersection("C");
        networkB.createIntersection("D");
        networkA.createIntersection("D");
        networkB.createIntersection("C");

        assertEquals(networkA, networkB);
    }

    /**
     * Test that equals() returns a different value for 'non-equal'
     * networks.
     */
    @Test
    public void equals_differentForNonEqualNetworks(){
        Network networkA = new Network();
        Network networkB = new Network();

        networkA.createIntersection("A");

        assertNotEquals(networkA, networkB);

        networkB.createIntersection("A");

        assertEquals(networkA, networkB);

        networkA.createIntersection("B");
        networkB.createIntersection("C");

        assertNotEquals(networkA, networkB);
    }

    /**
     * Test that hashCode() returns the same value for 'equal' networks.
     * @throws IntersectionNotFoundException never
     * @throws DuplicateSensorException never
     * @throws RouteNotFoundException never
     */
    @Test
    public void hashCode_sameForEqualNetworks() throws IntersectionNotFoundException, DuplicateSensorException, RouteNotFoundException {
        Network networkA = new Network();
        Network networkB = new Network();

        assertEquals(networkA.hashCode(), networkB.hashCode());

        networkA.createIntersection("A");
        networkB.createIntersection("A");
        networkA.createIntersection("B");
        networkB.createIntersection("B");

        assertEquals(networkA.hashCode(), networkB.hashCode());

        networkA.connectIntersections("A", "B", 40);
        networkB.connectIntersections("A", "B", 40);

        assertEquals(networkA.hashCode(), networkB.hashCode());

        networkA.addSensor("A", "B", new DemoSpeedCamera(
                new int[] {1,2,3,4}, 40
        ));

        networkB.addSensor("A", "B", new DemoSpeedCamera(
                new int[] {1,2,3,4}, 40
        ));

        assertEquals(networkA.hashCode(), networkB.hashCode());

        networkA.addSpeedSign("A", "B", 90);
        networkB.addSpeedSign("A", "B", 90);

        networkA.createIntersection("C");
        networkB.createIntersection("D");
        networkA.createIntersection("D");
        networkB.createIntersection("C");

        assertEquals(networkA.hashCode(), networkB.hashCode());
    }

    @Test
    public void hashCode_differentForNonEqualNetworks(){
        Network networkA = new Network();
        Network networkB = new Network();

        networkA.createIntersection("A");

        assertNotEquals(networkA.hashCode(), networkB.hashCode());

        networkB.createIntersection("A");

        assertEquals(networkA.hashCode(), networkB.hashCode());

        networkA.createIntersection("B");
        networkB.createIntersection("C");

        assertNotEquals(networkA.hashCode(), networkB.hashCode());
    }
}
