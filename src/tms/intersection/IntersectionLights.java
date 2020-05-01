package tms.intersection;

import tms.route.Route;
import tms.route.TrafficLight;
import tms.route.TrafficSignal;
import tms.util.TimedItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a set of traffic lights at an intersection.
 *
 * For simplicity, traffic lights only allow one incoming route to be green at
 * any given time, with incoming traffic allowed to exit via any outbound route.
 */
public class IntersectionLights implements TimedItem {
    private int yellowTime, duration;
    private List<Route> connections;
    private int time = 0;
    // The index of the route in connections which currently has a green or
    // yellow signal.
    private int activeIndex;
    private int cycleTime;
    private boolean verbose = true; //TODO REMOVE THIS

    /**
     * Creates a new set of traffic lights at an intersection.
     *
     * The first route in the given list of incoming routes should have its
     * TrafficLight signal set to TrafficSignal.GREEN.
     *
     * @requires connections.size > 0, yellowTime >= 1, duration >
     * yellowTime, connections.route.getTrafficLight() != null;
     *
     */
    public IntersectionLights(List<Route> connections, int yellowTime,
                              int duration){
        this.yellowTime = yellowTime;
        this.duration = duration;
        this.connections  = connections;
        this.cycleTime = connections.size() * duration;

        // Now that we have ensured that all routes have traffic lights, then
        // we can safely set the traffic signal of the first element to
        // TrafficSignal.GREEN.

        activeIndex = 0; // The first element in connections has been set to
        // green.

        for (int i = 0; i < connections.size(); i++){
            TrafficLight toSet = connections.get(i).getTrafficLight();

            if (i == 0){
                toSet.setSignal(TrafficSignal.GREEN);
            }
            else{
                toSet.setSignal(TrafficSignal.RED);
            }
        }
    }

    /**
     * Returns the time in seconds for which a traffic light will appear yellow
     * when transitioning from green to red.
     *
     * @return yellow time in seconds for this set of traffic lights
     */
    public int getYellowTime(){
        return yellowTime;
    }

    /**
     * Sets a new duration of each green-yellow cycle.
     *
     * The current progress of the lights cycle should be reset, such that on
     * the next call to oneSecond(), only one second of the new duration has
     * been elapsed for the incoming route that currently has a green light.
     *
     * @param duration the new light signal duration
     * @requires duration > getYellowTime
     */
    public void setDuration(int duration){
        this.duration = duration;
    }

    /**
     * Simulates one second passing and updates the state of this set of traffic
     * lights.
     *
     * If enough time has passed such that a full green-yellow duration has
     * elapsed, or such that the current green light should now be yellow, the
     * appropriate light signals should be changed:
     *
     *  1) When a traffic light signal has been green for 'duration -
     *     yellowTime' seconds, it should be changed from green to yellow.
     *  2) When a traffic light signal has been yellow for 'yellowTime'
     *     seconds, it should be changed from yellow to red, and the next
     *     incoming route in the order passed to
     *     IntersectionLights(List,int,int) should be given a green light.
     *     If the end of the list of routes has been reached, simply wrap around
     *     to the start of the list and repeat.
     *
     * If no routes are connected to the intersection, the duration shall not
     * elapse and the call should simply return without changing anything
     */
    public void oneSecond(){
        //TODO: Implement the logic for this method.

        System.out.println("Prepping for "+ time);

        if (connections.size() == 0) return; // Exit out of the method without
                                             // doing anything.
        int mTime = time % duration;



        List<String> output = new ArrayList<>();
        output.add("R");
        output.add("R");
        output.add("R");
        output.add("R");

        if (0 <= mTime && mTime <= 7){
            System.out.println(time+ " GREEN");

            switch(activeIndex){
                case 0:
                    System.out.println("Setting " + activeIndex + " GREEN0");
                    connections.get(0).getTrafficLight().setSignal(TrafficSignal.GREEN);
                    connections.get(1).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(2).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(3).getTrafficLight().setSignal(TrafficSignal.RED);
                    break;
                case 1:
                    System.out.println("Setting " + activeIndex + " GREEN1");
                    connections.get(0).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(1).getTrafficLight().setSignal(TrafficSignal.GREEN);
                    connections.get(2).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(3).getTrafficLight().setSignal(TrafficSignal.RED);
                    break;
                case 2:
                    System.out.println("Setting " + activeIndex + " GREEN2");
                    connections.get(0).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(1).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(2).getTrafficLight().setSignal(TrafficSignal.GREEN);
                    connections.get(3).getTrafficLight().setSignal(TrafficSignal.RED);
                    break;
                case 3:
                    System.out.println("Setting " + activeIndex + " GREEN3");
                    connections.get(0).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(1).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(2).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(3).getTrafficLight().setSignal(TrafficSignal.GREEN);
                    break;
            }
        }
        if (8 <= mTime && mTime <= 14){
            System.out.println(time+ " YELLOW");
            switch(activeIndex){
                case 0:
                    System.out.println("Setting " + activeIndex + " YELLOW0");
                    connections.get(0).getTrafficLight().setSignal(TrafficSignal.YELLOW);
                    connections.get(1).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(2).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(3).getTrafficLight().setSignal(TrafficSignal.RED);
                    break;
                case 1:
                    System.out.println("Setting " + activeIndex + " YELLOW1");
                    connections.get(0).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(1).getTrafficLight().setSignal(TrafficSignal.YELLOW);
                    connections.get(2).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(3).getTrafficLight().setSignal(TrafficSignal.RED);
                    break;
                case 2:
                    System.out.println("Setting " + activeIndex + " YELLOW2");
                    connections.get(0).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(1).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(2).getTrafficLight().setSignal(TrafficSignal.YELLOW);
                    connections.get(3).getTrafficLight().setSignal(TrafficSignal.RED);
                    break;
                case 3:
                    System.out.println("Setting " + activeIndex + " YELLOW3");
                    connections.get(0).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(1).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(2).getTrafficLight().setSignal(TrafficSignal.RED);
                    connections.get(3).getTrafficLight().setSignal(TrafficSignal.YELLOW);
                    break;
            }

            if (mTime == duration - 1){ // last one before mTime = 0
                if (time == duration * connections.size() - 1){
                    activeIndex = 0;
                } else activeIndex++;
            }
        }

        time++;
        /*
        if (time == 0){
            // Initially, set all elements (besides the first, which is
            // green) to TrafficSignal.RED.

            for (int i = 0; i < connections.size(); i++){
                if (i != 0){
                    connections.get(i).setSignal(TrafficSignal.RED);
                }
            }
        }

        switchTrafficLights(); // Logic for switching light colours based
                               // on time elapsed.
        time++;
        */

    }

    /**
     * Returns the string representation of this set of IntersectionLights.
     *
     * The format to return is "duration:list,of,intersection,ids" where
     * 'duration' is our current duration and 'list,of,intersection,ids' is a
     * comma-separated list of the IDs of all intersections that have an
     * incoming route to this set of traffic lights, in order given to
     * IntersectionLights' constructor.
     *
     * For example, for a set of traffic lights with inbound routes from three
     * intersections - A, C and B - in that order, and a duration of 8 seconds,
     * return the string "8:A,C,B".
     */
    @Override
    public String toString(){
        return duration + ":" + parseList(getIncomingIntersections());
        // method.
    }

    /***
     * A method to return a list of Intersections which have an incoming
     * route to this set of traffic lights, in the order given to the
     * IntersectionLights' construtor.
     *
     * @return A list of intersections which have incoming routes to this
     * intersection (in the order passed to the IntersectionLights'
     * constructor).
     */
    private List<Intersection> getIncomingIntersections(){
        List<Intersection> originIntersections = new ArrayList<>();

        for (Route r: connections){
            Intersection from = r.getFrom();

            originIntersections.add(from);
        }

        return originIntersections;
    }

    /**
    * A separate method used to partition the logic for switching the traffic
     * lights.
     */
    private void switchTrafficLights() {

        int greenTime = duration - yellowTime;
        int modTime   = time % duration;

        // if ((0 <= modTime) && (modTime <= greenTime - 1)){
        if ((0 <= modTime) && (modTime <= 6)){
            setLights(TrafficSignal.GREEN, activeIndex);

            // When transitioning from GREEN -> YELLOW we do not need to
            // switch the active index.
        }
        // else if ((yellowTime + 1) <= duration){
        else if ((7) <= modTime){
            if (modTime == 8) System.out.println("Setting to yellow");
            setLights(TrafficSignal.YELLOW, activeIndex);

            if (modTime == duration - 1){
                System.out.println("SAI @ t=" + time);
                setActiveIndex();
            }
        }
    }

    /**
     * A method which turns a list object into a string of comma delimited
     * values
     */
    private String parseList(List<Intersection> originIntersections) {
        List<String> intersectionIDs = new ArrayList<>();

        for (Intersection i: originIntersections){
            intersectionIDs.add(i.getId());
        }

        return String.join(",", intersectionIDs);
    }

    /**
     * A method to iterate the active index and wrap it to the start if
     * required.
     */
    private void setActiveIndex(){
        if (time % cycleTime == 0){
            activeIndex = 0; // Reset the active light back to 0 if
            // we have completed a cycle.
        }
        else{
            activeIndex++; // Else normally iterate the active light.
        }

        System.out.println("Setting the active index to: " + activeIndex);
    }

    /**
     * A method which sets the lights in the connections list.
     * It sets the light at activeIndex with the given signal, and the others
     * to red.
     */
    private void setLights(TrafficSignal signal, int activeIndex) {
        if (time == 15) System.out.println("t=15");

        for (int i = 0; i < connections.size() - 1; i++){
            TrafficLight toSet = connections.get(i).getTrafficLight();

            if (i == activeIndex){
                toSet.setSignal(signal);
            } else{
                toSet.setSignal(TrafficSignal.RED);
            }
        }
    }
}
