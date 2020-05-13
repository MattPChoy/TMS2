package tms.network;

import org.junit.Before;
import org.junit.Test;
import tms.util.IntersectionNotFoundException;
import tms.util.InvalidOrderException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *  26) Test that addLights() throws an IntersectionNotFoundException if the
 *      provided ID does not correspond with an existing intersection.
 *  27) Test that addLights() throws an InvalidOrderException if
 *      the order specified is not a permutation of the intersection's
 *      incoming routes.
 *  28) Test that addLights() throws an InvalidOrderException if
 *      the order specified is empty.
 *  29) Test that addLights() adds a traffic light object
 */
public class AddLightsTest {
    private Network n;

    @Before
    public void setup() {
        n = new Network();
    }

    /**
     * Test that addLights() throws an IntersectionNotFoundException if the
     * provided ID does not correspond with an existing intersection.
     */
    @Test
    public void addLights_throwsIntersectionNotFoundException() {
        boolean exceptionThrown = false;

        List<String> order = new ArrayList<>();

        try {
            n.addLights("A", 50, order);
        } catch (IntersectionNotFoundException | InvalidOrderException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that the addLights() method throws an InvalidOrderException if
     * the order specified is not a permutation of the intersection's
     * incoming routes.
     *
     * @throws IntersectionNotFoundException never
     */
    @Test
    public void addLights_throwsInvalidOrderExceptionNotPermutation()
            throws IntersectionNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        List<String> order = new ArrayList<>();

        order.add("P");

        try {
            n.addLights("A", 50, order);
        } catch (InvalidOrderException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that the addLights() method throws an InvalidOrderException if
     * the order specified is empty.
     *
     * @throws IntersectionNotFoundException never
     */
    @Test
    public void addLights_throwsInvalidOrderExceptionEmpty()
            throws IntersectionNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        List<String> order = new ArrayList<>();

        try {
            n.addLights("A", 50, order);
        } catch (InvalidOrderException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that the addLights() method throws an IllegalArgumentException if
     * the given duration is less than the network's yellow time plus one.
     *
     * @throws IntersectionNotFoundException never
     */
    @Test
    public void addLights_throwsIllegalArgumentExceptionInvalidYellowTime()
            throws IntersectionNotFoundException {

        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.setYellowTime(20);
        List<String> order = new ArrayList<>();

        try {
            n.addLights("A", 20, order);
        } catch (InvalidOrderException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that the addLights() method adds a traffic light object
     *
     * @throws IntersectionNotFoundException never
     * @throws InvalidOrderException         never
     */
    @Test
    public void addLights_addsTrafficLight()
            throws IntersectionNotFoundException, InvalidOrderException {
        n.createIntersection("A");
        n.createIntersection("B");

        n.connectIntersections("A", "B", 50);

        List<String> order = new ArrayList<>();
        order.add("A");
        assertFalse(n.findIntersection("A").hasTrafficLights());

        n.addLights("B", 30, order);

        assertTrue(n.findIntersection("B").hasTrafficLights());
    }
}
