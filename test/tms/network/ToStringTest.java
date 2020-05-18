package tms.network;

import org.junit.Test;
import tms.sensors.DemoPressurePad;
import tms.sensors.DemoSpeedCamera;
import tms.sensors.DemoVehicleCount;
import tms.util.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test that the toString() method omits any comments (no line starting with
 * ';')
 * Test that the toString() method orders intersections alphabetically.
 * Test that the toString() method adds sensors to the routes section
 * Test that the toString() method correctly outputs traffic lights
 * Test that the toString() method correctly outputs the yellow lights and
 * number of intersections.
 */
public class ToStringTest {
    private final String FILE_SEPARATOR = File.separator;
    private final String RESOURCES_LOCATION = "test" + FILE_SEPARATOR
            + "tms" + FILE_SEPARATOR
            + "network" + FILE_SEPARATOR
            + "resources" + FILE_SEPARATOR;
    private final String NETWORKS_LOCATION = "networks" + FILE_SEPARATOR;
    private final String DEMO_TXT = NETWORKS_LOCATION + "demo.txt";

    @Test
    public void testToStringSTDOUT() throws IntersectionNotFoundException,
            DuplicateSensorException, RouteNotFoundException,
            InvalidOrderException {

        Network n = new Network();

        n.setYellowTime(1);

        n.createIntersection("W");
        n.createIntersection("X");
        n.createIntersection("Y");
        n.createIntersection("Z");
        List<String> order = new ArrayList<>();
        order.add("Z"); order.add("X");


        n.connectIntersections("X", "Y", 60);
        n.makeTwoWay("X", "Y");

        n.addSensor(
                "Y", "X", new DemoPressurePad(
                        new int[] {5,2,4,4,1,5,2,7,3,5,6,5,8,5,4,2,3,3,2,5},
                        5
                )
        );

        n.connectIntersections("Y", "Z", 100);

        n.addSensor("Y", "Z", new DemoPressurePad(
                new int[] {1,3,2,1,1,3,4,7,4,7,9,7,8,4,8,8,5,3,2,2},
                8
        ));

        n.addSensor("Y", "Z", new DemoVehicleCount(
                new int[] {42,40,37,34,35,31,36,41,41,47,48,50,53,48,54,58,52
                        ,52,61,55},
                50
                )
        );

        n.connectIntersections("Z", "X", 40);

        n.addSensor("Z", "X",
                new DemoSpeedCamera(
                        new int[] {39,40,40,40,36,32,25,28,31,39,40,40,40,40,40,
                                40,36,35,39,40},
                        40
                )
        );

        n.connectIntersections("Z", "Y", 100);

        n.addSpeedSign("Z", "Y", 80);
        n.addLights("Y", 3, order);
        System.out.println(
                n.toString()
        );
    }
}
