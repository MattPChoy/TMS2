package tms.network;

import org.junit.Test;
import tms.util.InvalidNetworkException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class NetworkInitaliserTest {
    private final String FILE_SEPARATOR = File.separator;
    private final String RESOURCES_LOCATION = "test" + FILE_SEPARATOR
            + "tms" + FILE_SEPARATOR
            + "network" + FILE_SEPARATOR
            + "resources" + FILE_SEPARATOR;
    private final String NETWORKS_LOCATION = "networks" + FILE_SEPARATOR;
    private final String DEMO_TXT = NETWORKS_LOCATION + "demo.txt";
    @Test
    public void testLineEOF_Valid1() throws IOException,
            InvalidNetworkException {
        String filename = RESOURCES_LOCATION + "DEMO_EOF_VALID_1.txt";
        NetworkInitialiser.loadNetwork(filename);
    }

    @Test
    public void testLineEOF_Valid2() throws IOException,
            InvalidNetworkException {
        String filename = RESOURCES_LOCATION + "DEMO_EOF_VALID_2.txt";
        NetworkInitialiser.loadNetwork(filename);
    }

    @Test // 3 blank lines at EOF
    public void testLineEOF_Invalid1() throws IOException {
        boolean exceptionThrown = false;

        String filename = RESOURCES_LOCATION + "DEMO_EOF_INVALID_1.txt";
        try{
            NetworkInitialiser.loadNetwork(filename);
        } catch (InvalidNetworkException e){
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void networkInitialisation_doesNotCreateSensorsAsIntersection()
            throws IOException {
        boolean exceptionThrown = false;

        String filename = RESOURCES_LOCATION + "DEMO_SENSOR_INTERSECTION_TEST" +
                ".txt";
        try{
            NetworkInitialiser.loadNetwork(filename);
        } catch (InvalidNetworkException e){
            System.out.println(e.getMessage());
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void networkInitialisation_doesNotCreateSensorsAsIntersection1()
            throws IOException {
        boolean exceptionThrown = false;

        String filename = RESOURCES_LOCATION +
                "DEMO_SENSOR_INTERSECTION_TEST_2" +
                ".txt";
        try{
            NetworkInitialiser.loadNetwork(filename);
        } catch (InvalidNetworkException e){
            System.out.println(e.getMessage());
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void networkInitialisation_doesNotCreateSensorsAsIntersection2()
            throws IOException {
        boolean exceptionThrown = false;

        String filename = RESOURCES_LOCATION +
                "DEMO_SENSOR_INTERSECTION_TEST_3" +
                ".txt";
        try{
            NetworkInitialiser.loadNetwork(filename);
        } catch (InvalidNetworkException e){
            System.out.println(e.getMessage());
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void networkInitialisation_intersectionDeclarationMismatch()
            throws IOException{
        // Create a test that demonstrates that the network initializer
        // throws an error when we have a mismatch between the number of
        // declared intersections and the number of defined intersections

        boolean exceptionThrown = false;

        String filename = RESOURCES_LOCATION +
                "DEMO_INVALID_NUMBER_OF_INTERSECTIONS" +
                ".txt";
        try{
            NetworkInitialiser.loadNetwork(filename);
        } catch (InvalidNetworkException e){
            System.out.println(e.getMessage());
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void networkInitialisation_trafficLightDurationNotNumber()
            throws IOException{
        // Create a test that demonstrates that when the traffic light data is
        // malformed (i.e. duration that cannot be converted to an integer) it
        // throws an error.

        boolean exceptionThrown = false;

        String filename = RESOURCES_LOCATION +
                "DEMO_INVALID_LIGHT_DURATION" +
                ".txt";
        try{
            NetworkInitialiser.loadNetwork(filename);
        } catch (InvalidNetworkException e){
            System.out.println(e.getMessage());
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void networkInitialisation_trafficLightOrderInvalid()
            throws IOException{
        boolean exceptionThrown = false;

        String filename = RESOURCES_LOCATION +
                "DEMO_INVALID_ORDER" +
                ".txt";
        try{
            NetworkInitialiser.loadNetwork(filename);
        } catch (InvalidNetworkException e){
            System.out.println(e.getMessage());
            exceptionThrown = true;
        }

        if (!exceptionThrown) fail();
    }

    @Test
    public void isIntersectionString_validation(){
        // TODO check @1232
        Object[] values = new Object[] {
                "A", true,  // Testing single case Valid
                "B", true,   // Testing single case Valid

                "PP", false,  // Testing single case Invalid
                "SC", false, // Testing single case Invalid
                "VC", false, // Testing single case Invalid

                "A:B", false, // Testing double case Invalid (is invalid for
                // all X:Y)
                "PP:B", false, // Testing double case Invalid
                "B:VC", false, // Testing double case Invalid
                "A:", false,   // Testing double case Invalid
                ":B", false,   // Testing double case Invalid
                ":", false,    // Testing double case Invalid

                "X:3:Z,X", true, // Is valid
                "X:10:A", true, // Is valid
                "X:45:A,C,E,F,G,E,D,E,S", true, // Is valid

                "Y:A:Z,X", false, // Is not valid because A is not an integer
                "::", false, // Is invalid
                "::A", false, // Is invalid
                ":B:", false, // Is invalid
                ":A:B", false, // Is invalid
                "A::", false, // Is invalid
                "A:B:", false, // Is invalid
                "A::B", false, // Is invalid
                "A:B:", false, // Is invalid
                "X:3:", false, // Is invalid

                "X:A:Z,X", false, // Is invalid
                "PP:3:Z,X", false, // Is invalid
                "SC:3:Z,X", false, // Is invalid
                "VC:3:Z,X", false, // Is invalid
        };

        for (int i = 0; i < values.length; i += 2){

            boolean expected = (boolean) values[i+1];
            boolean actual = NetworkInitialiser.isIntersectionString(
                    (String) values[i]
            );
            assertEquals(expected, actual);
        }
    }

    @Test
    public void isRouteString_validation(){
        Object[] values = new Object[] {
                "A:B:40:0", true,
                ":::", false,
                ":::A", false,
                "::A:", false,
                "::A:B", false,

                ":A::", false,
                ":A::B", false,
                ":A:B:", false,
                ":A:B:C", false,

                "A:::", false,
                "A:::B", false,
                "A::B:", false,
                "A::B:C", false,

                "A:B::", false,
                "A:B::C", false,
                "A:B:C:", false,

                "A:B:A:C", false,
                "A:B:1:C", false,
                "A:B:C:2", false,
                "A:B:1:1", true,

                "A:B:40:0:A", false,
                "A:B:40:0:30", true,

                "PP:B:40:0:30", false,
                "A:PP:40:0:30", false,
                "PP:PP:40:0:30", false,

                "SC:B:40:0:30", false,
                "A:SC:40:0:30", false,
                "SC:SC:40:0:30", false,

                "VC:B:40:0:30", false,
                "A:VC:40:0:30", false,
                "VC:VC:40:0:30", false,
        };

        for (int i = 0; i < values.length; i += 2){

            boolean expected = (boolean) values[i+1];
            boolean actual = NetworkInitialiser.isRouteString(
                    (String) values[i]
            );
            assertEquals((String) values[i], expected, actual);
        }
    }

    @Test
    public void isSensorString_validate(){
        Object[] values = new Object[] {
                "::", false,
                "PP:40:", false,
                "PP::1,2,3,4", false,

                "PP:-1:1,2,3,4", false,
                "PP:40:1,2,-3,4", false,
                "PP:40:1,2,3,4", true,
                "SC:30:1,2,3,4", true,
                "VC:0:1,2,3,4", true,

                "AP:40:1,2,3,4", false,
                "VC:-3:-1,2,3", false
        };

        for (int i = 0; i < values.length; i += 2){

            boolean expected = (boolean) values[i+1];
            boolean actual = NetworkInitialiser.isSensorString(
                    (String) values[i]
            );
            assertEquals((String) values[i], expected, actual);
        }
    }

    @Test
    public void get(){
        System.out.println(
                NetworkInitialiser.isRouteString("A:B:40:0:30")
        );
    }

    @Test
    public void getSTDOUT() throws IOException, InvalidNetworkException {
        NetworkInitialiser.loadNetwork(DEMO_TXT);
//        System.out.println(NetworkInitialiser.getRange(
//                NetworkInitialiser.read(DEMO_TXT),
//                NetworkInitialiser.SectionName.ROUTES
//        ));
    }

}
