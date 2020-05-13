package tms.network;

import org.junit.Before;
import org.junit.Test;
import tms.util.IntersectionNotFoundException;
import tms.util.InvalidOrderException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 *  40) Test that changeLightDuration() throws an
 *      IntersectionNotFoundException if the provided ID 'intersectionID' does
 *      not correspond with any existing intersection.
 *  41) Test that changeLightDuration() throws an IllegalStateException if
 *      the given intersection does not have a speed sign.
 *  42) Test that changeLightDuration() throws an IllegalArgumentException if
 *      duration < (this.getYellowTime + 1)
 *  43) Test that changeLightDuration() changes the duration of the light for
 *      valid parameters.
 */

public class ChangeLightDurationTest {
    private Network n;

    @Before
    public void setup() {
        n = new Network();
    }

    /**
     * Test that changeLightDuration() throws an
     * IntersectionNotFoundException if the provided ID 'intersectionID' does
     * not correspond with any existing intersection.
     */
    @Test
    public void changeLightDuration_throwsIntersectionNotFoundException() {
        boolean exceptionThrown = false;

        try {
            n.changeLightDuration("A", 20);
        } catch (IntersectionNotFoundException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that changeLightDuration() throws an IllegalStateException if the
     * given intersection does not have a set of lights sign.
     *
     * @throws IntersectionNotFoundException never
     */
    @Test
    public void changeLightDuration_throwsIllegalStateException() throws IntersectionNotFoundException {
        boolean exceptionThrown = false;

        n.createIntersection("A");

        try {
            n.changeLightDuration("A", 40);
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that changeLightDuration() throws an IllegalArgumentException if
     * duration < (this.getYellowTime + 1)
     *
     * @throws IntersectionNotFoundException never
     * @throws InvalidOrderException         never
     */
    @Test
    public void changeLightDuration_throwsIllegalArgumentExceptionForDuration()
            throws IntersectionNotFoundException, InvalidOrderException {
        boolean exceptionThrown = false;

        n.createIntersection("A");
        n.createIntersection("B");
        n.connectIntersections("A", "B", 40);
        List<String> order = new ArrayList<>();
        order.add("A");
        n.addLights("B", 20, order);
        n.setYellowTime(40);

        try {
            n.changeLightDuration("B", 40);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that changeLightDuration() changes the duration of the light for
     * valid parameters.
     *
     * @throws IntersectionNotFoundException never
     * @throws InvalidOrderException         never
     */
    @Test
    public void changeLightDuration_validParameters()
            throws IntersectionNotFoundException, InvalidOrderException {
        n.createIntersection("A");
        n.createIntersection("B");
        n.connectIntersections("A", "B", 40);
        List<String> order = new ArrayList<>();
        order.add("A");
        n.addLights("B", 20, order);
        n.setYellowTime(20);

        n.changeLightDuration("B", 40);
    }
}
