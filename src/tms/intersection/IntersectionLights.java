package tms.intersection;

import tms.route.Route;
import tms.route.TrafficLight;
import tms.route.TrafficSignal;
import tms.util.TimedItem;
import tms.util.TimedItemManager;

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

        for (Route r : connections){
            if (r.getTrafficLight() == null){
                r.addTrafficLight();
            }
        }

        resetLights();

        // Register as timed item
        TimedItemManager.getTimedItemManager().registerTimedItem(this);
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
        this.time = 0;

        System.out.println("Time reset in setDuration method");

        resetLights();
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
        System.out.println("OneSecond called! ");

        if (connections.size() == 0) return; // Exit out of the method without
                                             // doing anything.
        int mTime = time % duration;

        if (0 <= mTime && mTime <= 7) {
            setTrafficLights(TrafficSignal.GREEN);
        }
        if (8 <= mTime && mTime <= 14) {
            setTrafficLights(TrafficSignal.YELLOW);

            if (mTime == duration - 1) { // last one before mTime = 0
                if (time == duration * connections.size() - 1) {
                    activeIndex = 0;
                } else activeIndex++;
            }
        }

        time++;
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
        return duration + ":" + parseList(
                getIncomingIntersections(connections));
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
    private static List<Intersection> getIncomingIntersections(
            List<Route> connections){

        List<Intersection> originIntersections = new ArrayList<>();

        for (Route r: connections) {
            Intersection from = r.getFrom();

            originIntersections.add(from);
        }

        return originIntersections;
    }

    /**
     * A method which turns a list object into a string of comma delimited
     * values. Used in the toString method.
     */
    private static String parseList(List<Intersection> originIntersections) {
        List<String> intersectionIDs = new ArrayList<>();

        for (Intersection i: originIntersections) {
            intersectionIDs.add(i.getId());
        }

        return String.join(",", intersectionIDs);
    }

    /**
     * A method used to switch the signals of the traffic light objects.
     */
    private void setTrafficLights(TrafficSignal signal){
        for (int i = 0; i < connections.size(); i++){
            TrafficLight lightToChange = connections.get(i).getTrafficLight();

            if (i == activeIndex){
                lightToChange.setSignal(signal);
            } else{
                lightToChange.setSignal(TrafficSignal.RED);
            }
        }
    }

    private void resetLights(){
        for (int i = 0; i < connections.size(); i++){
            TrafficLight toSet = connections.get(i).getTrafficLight();

            if (i == 0){
//                toSet.setSignal(TrafficSignal.GREEN);
                connections.get(0).setSignal(TrafficSignal.GREEN);
            }
            else{
//                toSet.setSignal(TrafficSignal.RED);
                connections.get(i).setSignal(TrafficSignal.RED);
            }
        }
        System.out.println();
    }

}