package tms.network;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 1) Test that the constructor initialises a network with no intersections.
 */
public class ConstructorTest {
    private Network n;
    final String LINE_BREAK = System.lineSeparator();

    @Before
    public void setup() {
        n = new Network();
    }

    /**
     * Test that the constructor initialises a network with no intersections.
     */
    @Test
    public void constructor_newEmptyNetwork() {
        assertEquals(0, n.getIntersections().size());

        String toString = "0" + LINE_BREAK + "0" + LINE_BREAK + "1";

        assertEquals(toString, n.toString());
    }
}
