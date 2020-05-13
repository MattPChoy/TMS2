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
 *  54) Test that findIntersection() throws an IntersectionNotFoundException
 *      if the provided id 'id' does not correspond with any existing
 *      intersection.
 *  55) Test that findIntersection() returns the correct object with correct ID.
 */
public class FindIntersection {

    private Network n;

    @Before
    public void setup() {
        n = new Network();
    }

    /**
     * Test that findIntersection() throws an IntersectionNotFoundException
     * if the provided id 'id' does not correspond with any existing
     * intersection.
     */
    @Test
    public void findIntersection_throwsIntersectionNotFoundExceptionID(){
        boolean exceptionThrown = false;

        try{
            n.findIntersection("A");
        } catch (IntersectionNotFoundException e){
            exceptionThrown = true;
        }
        if (!exceptionThrown) fail();
    }

    /**
     Test that findIntersection() returns the correct object with correct ID.
     @throws IntersectionNotFoundException never
     @throws InvalidOrderException never
     */
    @Test
    public void findIntersection_correctOutput()
            throws IntersectionNotFoundException, InvalidOrderException {
        n.createIntersection("A");
        n.createIntersection("B");
        n.createIntersection("C");

        n.connectIntersections("B", "A", 30);
        n.connectIntersections("C", "A", 30);
        List<String> order = new ArrayList<>(); order.add("B"); order.add("C");


        assertEquals("A", n.findIntersection("A").toString());
        n.addLights("A", 30, order);

        assertEquals("A:30:B,C", n.findIntersection("A").toString());

    }
}
