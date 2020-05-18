package tms.network;

import org.junit.Before;
import org.junit.Test;
import tms.util.InvalidNetworkException;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class NetworkInitaliserTest {
    private Network n;
    private final String FILE_SEPARATOR = File.separator;
    private final String RESOURCES_LOCATION = "test" + FILE_SEPARATOR
            + "tms" + FILE_SEPARATOR
            + "network" + FILE_SEPARATOR
            + "resources" + FILE_SEPARATOR;

    private final String VALID_FILES =
            RESOURCES_LOCATION + "valid" + FILE_SEPARATOR;
    private final String INVALID_FILES =
            RESOURCES_LOCATION + "invalid" + FILE_SEPARATOR;

    private final String NETWORKS_LOCATION = "networks" + FILE_SEPARATOR;
    private final String DEMO_TXT = NETWORKS_LOCATION + "demo";
    private final String LINE_BREAK = System.lineSeparator();

    @Before
    public void setup() {
        n = null;
    }


    /**
     * Test that the output is correct for an empty network
     */
    @Test
    public void loadNetwork_emptyNetworkCorrectOutput() {
        String expected = "0" + LINE_BREAK + // Number of intersections
                "0" + LINE_BREAK + // Number of routes
                "1";               // Default yellow time @1159
        n = new Network();
        String actual = n.toString();

        assertEquals(expected, actual);
    }

    /**
     * Test that the number of intersections is correct for the demo network
     */
    @Test
    public void loadNetwork_demoNetworkNumberOfIntersections()
            throws IOException, InvalidNetworkException {
        n = NetworkInitialiser.loadNetwork(DEMO_TXT + ".txt");

        int expected = 4;
        int actual = n.getIntersections().size();

        assertEquals(expected, actual);
    }

    /**
     * Test that a network with an intersection ID of "PP" throws an
     * InvalidNetworkException
     */
    @Test
    public void loadNetwork_invalidIntersectionFormats() throws IOException {
        /*
         * Test for non-compliance in the constants section
         * > number of intersections cannot be parsed as an integer
         * > number of routes cannot be parsed as an integer
         * > yellowTime cannot be parsed as an integer
         *
         * > yellowTime is zero
         * > yellowTime is negative
         * > number of intersections is negative
         * > number of routes is negative
         */
        testInvalid("Const_NonIntIntersection");
        testInvalid("Const_NegIntersection");
        testInvalid("Const_NonIntYellow");
        testInvalid("Const_YellowIsZero");
        testInvalid("Const_NegYellow");
        testInvalid("Const_NonIntRoute");
        testInvalid("Const_NegRoute");

        testInvalid("Inter_IsPP");
        testInvalid("Inter_IsSC");
        testInvalid("Inter_IsVC");
        testInvalid("Inter_IsTab");
        testInvalid("Inter_IsSpace");
        testInvalid("Inter_isLinebreak");
        testInvalid("Inter_singleSemicolon");
        testInvalid("Inter_zeroDuration"); // Negative
        testInvalid("Inter_YellowDuration"); // Equal to YellowTime
        testInvalid("Inter_emptyOrder");      // A:3:
        testInvalid("Inter_addedOrder");      // Non-incoming added
        testInvalid("Inter_MissingOrder");
        testInvalid("Inter_SameID");

        testInvalid("Route_fromNotInstantiated");
        testInvalid("Route_toNotInstantiated");
        testInvalid("Route_nonIntDefaultSpeed");
        testInvalid("Route_negativeDefaultSpeed");
        testInvalid("Route_negativeNumSensors");
        testInvalid("Route_nonIntNumSensors");
        testInvalid("Route_duplicateRoute");
        testInvalid("Route_negativeSpeedSignSpeed");

        testInvalid("Const_NumIntersectionsLarger");
        testInvalid("Const_NumIntersectionsSmaller");
        testInvalid("Const_NumRoutesSmaller");
        testInvalid("Const_NumRoutesLarger");

        // Test negative values for sensor threshold, and data values
        testInvalid("Sensor_negativeThreshold");
        testInvalid("Sensor_negativeData");
        testInvalid("Sensor_moreDeclared");
        testInvalid("Sensor_lessDeclared");
        testInvalid("Sensor_nonDefinedType");
        testInvalid("Sensor_duplicateType");
        testInvalid("Sensor_zeroThreshold");

//        testInvalid("Format_threeBlankLines");
        testInvalid("Format_fourBlankLines");
        testInvalid("Format_fileTooShort");
        testInvalid("Format_mixingSections1");
        testInvalid("Format_mixingSections2");

    }

    @Test
    public void loadNetwork_validFileFormats() throws IOException,
            InvalidNetworkException {

        // Base cases
        testValid("EmptyNetwork");
        testValid(DEMO_TXT);
        testValid("Inter_validOrder");

        // Edge cases
        testValid("Inter_validDuration");

        testValid("Route_circular");
        testValid("Inter_PPAdded");
        testValid("Inter_SCAdded");
        testValid("Inter_VCAdded");

        testValid("Inter_TabAdded");
        testValid("Inter_SpaceAdded");

        testValid("Format_noBlankLines");
        testValid("Format_oneBlankLine");
        testValid("Format_twoBlankLines");

        testValid("Format_commentsIgnored");
        testValid("Format_commentsNotLinebreakAtEOF");
    }

    /**
     * A method to test that a given filepath is invalid
     *
     * @param filename file location
     */
    public void testInvalid(String filename) throws IOException {
        boolean exceptionThrown = false;

        String fp = INVALID_FILES + FILE_SEPARATOR + filename + ".txt";

        try {
            n = NetworkInitialiser.loadNetwork(
                    fp
            );
        } catch (InvalidNetworkException e) {
            exceptionThrown = true;
            System.out.println("==========" + filename + "===========");
            System.out.println("> [" + filename + "] \t" + e.getMessage());
            System.out.println("==================================");
        }

        if (!exceptionThrown) fail(filename);
    }

    public void testValid(String filename) throws InvalidNetworkException,
            IOException {
        String fp;
        if (!filename.contains(FILE_SEPARATOR)) {
            fp = VALID_FILES + FILE_SEPARATOR + filename + ".txt";
        } else fp = filename + ".txt";
        n = NetworkInitialiser.loadNetwork(fp);
    }
}
