package tms.network;

import org.junit.Before;
import org.junit.Test;
import tms.util.IntersectionNotFoundException;
import tms.util.InvalidOrderException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 *   2) Test that getYellowTime() method returns 1 when instantiated.
 *   3) Test that setYellowTime() method throws an
 *      IllegalArgumentException if yellowTime is zero
 *   4) Test that the setYellowTime() method throws an
 *      IllegalArgumentException if yellowTime is negative.
 *   5) Test that when setYellowTime() is given a positive value, it does not
 *      throw an exception, and sets the value as an instance variable
 *      correctly.
 *   6) Test that after running setYellowTime(), the value obtained from
 *      getYellowTime is correct.
 */
public class YellowTimeTest {
    private Network n;

    @Before
    public void setup() {
        n = new Network();
    }

    /**
     * Test that the setYellowTime() method throws an
     * IllegalArgumentException if yellowTime is zero
     */
    @Test
    public void setYellowTime_yellowTimeZeroThrowsIllegalArgumentException() {
        boolean exceptionThrown = false;

        try {
            n.setYellowTime(0);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that the getYellowTime method returns 1 when instantiated.
     */
    @Test
    public void getYellowTime_returnsCorrectValue() {
        assertEquals(1, n.getYellowTime());
    }

    /**
     * Test that the setYellowTime() method throws an
     * IllegalArgumentException if yellowTime is negative
     */
    @Test
    public void setYellowTime_yellowTimeNegativeThrowsIllegalArgumentException() {
        boolean exceptionThrown = false;

        try {
            n.setYellowTime(-10);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that when setYellowTime() is given a positive value, it does not
     * throw an exception, and sets the value as an instance variable
     * correctly.
     */
    @Test
    public void setYellowTime_throwsNoException() {
        n.setYellowTime(10);

        assertEquals(10, n.getYellowTime());
    }

    /**
     * Test that after running setYellowTime(), previous traffic lights
     * remain unedited.
     *
     * @throws IntersectionNotFoundException never
     */
    @Test
    public void setYellowTime_canGetValue() throws IntersectionNotFoundException,
            InvalidOrderException {
        n.createIntersection("A");
        n.createIntersection("B");
        n.connectIntersections("A", "B", 30);

        List<String> order = new ArrayList<>();
        order.add("A");

        n.addLights("B", 20, order);

        n.setYellowTime(10);

        assertEquals(10, n.getYellowTime());
    }
}
