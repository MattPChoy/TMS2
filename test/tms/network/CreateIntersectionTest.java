package tms.network;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 *
 *   7) Test that createIntersection() throws an IllegalArgumentException if
 *      a duplicate ID is passed as a parameter.
 *   8) Test that createIntersection() throws an IllegalArgumentException if
 *      the ID contains the delimiter character ':'.
 *
 *   9) Test that createIntersection() throws an IllegalArgumentException if
 *      the ID is comprised of a tab "\t" character
 *  10) Test that createIntersection() throws an IllegalArgumentException if
 *      the ID is comprised of a linebreak type 1 "\n" character
 *  11) Test that createIntersection() throws an IllegalArgumentException if
 *      the ID is comprised of a linebreak type 2 "\n\r" character
 *  12) Test that createIntersection() throws an IllegalArgumentException if
 *      the ID is comprised of a space " ".
 *  13) Test that createIntersection() throws an IllegalArgumentException if
 *      the ID is comprised of multiple whitespace characters.
 *
 *  14) Test that createIntersection() does not throw any exceptions if the
 *      ID contains a tab "\t" character but another non-whitespace character.
 *  15) Test that createIntersection() does not throw any exceptions if the
 *      ID contains a linebreak type 1 "\n" character but another
 *      non-whitespace character.
 *  16) Test that createIntersection() does not throw any exceptions if the
 *      ID contains a linebreak type 2 "\n\r" character but another
 *      non-whitespace character.
 *  17) Test that createIntersection() does not throw any exceptions if the
 *      ID contains a space character " " but also another non-whitespace
 *      character.
 *  18) Test that createIntersections() does not throw any exceptions if the
 *      ID contains multiple whitespace characters but also another
 *      non-whitespace character
 */
public class CreateIntersectionTest {
    private Network n;

    @Before
    public void setup() {
        n = new Network();
    }

    /**
     * Test that createIntersection() throws an IllegalArgumentException if
     * a duplicate ID is passed as a parameter.
     */
    @Test
    public void createIntersection_intersectionExistThrowsIllegalArgumentException() {
        n.createIntersection("A");
        boolean exceptionThrown = false;

        try {
            n.createIntersection("A");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) {
            fail();
        }

    }

    /**
     * Test that createIntersection() throws an IllegalArgumentException if
     * the ID contains the delimiter character ':'.
     */
    @Test
    public void createIntersection_idContainsDelimiterThrowsIllegalArgumentException() {

        boolean exceptionThrown = false;

        try {
            n.createIntersection("A:B");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) {
            fail();
        }
    }

    /**
     * Test that createIntersection() throws an IllegalArgumentException if
     * the ID is comprised of a tab "\t" character
     */
    @Test
    public void createIntersection_throwsExceptionIfTab() {
        boolean exceptionThrown = false;

        try {
            n.createIntersection("\t");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that createIntersection() throws an IllegalArgumentException if
     * the ID is comprised of a linebreak type 1 "\n" character
     */
    @Test
    public void createIntersection_throwsExceptionIfLinebreak1() {
        boolean exceptionThrown = false;

        try {
            n.createIntersection("\n");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that createIntersection() throws an IllegalArgumentException if
     * the ID is comprised of a linebreak type 2 "\n\r" character
     */
    @Test
    public void createIntersection_throwsExceptionIfLinebreak2() {
        boolean exceptionThrown = false;

        try {
            n.createIntersection("\n\r");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that createIntersection() throws an IllegalArgumentException if
     * the ID is comprised of a space " " character
     */
    @Test
    public void createIntersection_throwsExceptionIfSpace() {
        boolean exceptionThrown = false;

        try {
            n.createIntersection(" ");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    /**
     * Test that createIntersection() throws an IllegalArgumentException if
     * the ID is comprised of a space " " character
     */
    @Test
    public void createIntersection_throwsExceptionIfMultipleWhitespaces() {
        boolean exceptionThrown = false;

        List<String> whitespaces = new ArrayList<>();
        whitespaces.add(" \t\n\r");
        whitespaces.add("     \t");
        whitespaces.add("\t\t\t");
        whitespaces.add("\t\r");
        whitespaces.add("\t\n");
        whitespaces.add("   \t");
        whitespaces.add("   \r  \t");
        whitespaces.add("\r  \t");

        for (String stringContainingWhitespace : whitespaces) {
            try {
                n.createIntersection(stringContainingWhitespace);
            } catch (IllegalArgumentException e) {
                exceptionThrown = true;
            }

            if (!exceptionThrown) fail();

            exceptionThrown = false;
        }
    }
    /*----------------------------------------------------------------------------*/
    /**
     * Test that createIntersection() does not throw any exceptions if the ID
     * contains a tab "\t" character but another non-whitespace character.
     */
    @Test
    public void createIntersection_doesNotFailIfTab(){
        n.createIntersection("\ta");
    }

    /**
     * Test that createIntersection() does not throw any exceptions if the
     * ID contains a linebreak type 1 "\n" character but another non-whitespace
     * character.
     */
    @Test
    public void createIntersection_doesNotFailIfLinebreak1() {
        n.createIntersection("\na");
    }

    /**
     * Test that createIntersection() does not throw any exceptions if the
     * ID contains a linebreak type 2 "\n\r" character but another
     * non-whitespace character.
     */
    @Test
    public void createIntersection_doesNotFailIfLinebreak2() {
        n.createIntersection("\n\ra");
    }

    /**
     * Test that createIntersection() does not throw any exceptions if the
     * ID contains a tab "\t" character but another non-whitespace character.
     */
    @Test
    public void createIntersection_doesNotFailIfSpace() {
        n.createIntersection(" a");
    }

    /**
     * Test that createIntersections() does not throw any exceptions if the
     * ID contains multiple whitespace characters but also another
     * non-whitespace character.
     */
    @Test
    public void createIntersection_doesNotFailMultipleCharacters() {

        List<String> whitespaces = new ArrayList<>();
        whitespaces.add(" \t\n\ra");
        whitespaces.add("     \ta");
        whitespaces.add("\t\t\ta");
        whitespaces.add("\t\ra");
        whitespaces.add("\t\na");
        whitespaces.add(" a  \t");
        whitespaces.add("   \r aa \t");
        whitespaces.add("\ra  \t");

        for (String stringContainingWhitespace : whitespaces) {
            n.createIntersection(stringContainingWhitespace);
        }
    }
}
