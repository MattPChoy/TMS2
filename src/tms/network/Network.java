package tms.network;

import tms.intersection.Intersection;
import tms.route.Route;
import tms.sensors.Sensor;
import tms.util.DuplicateSensorException;
import tms.util.IntersectionNotFoundException;
import tms.util.InvalidOrderException;
import tms.util.RouteNotFoundException;

import java.util.List;

public class Network {
    /**
     * Creates a new empty network with no intersections.
     */
    public Network(){

    }

    /**
     * Returns the yellow time for all traffic lights in this network.
     * @returns traffic light yellow time in seconds.
     */
    public int getYellowTime(){
        return 0;
    }

    /**
     * Sets the time that lights appear yellow between turning from green to red
     * (in seconds) for all new traffic lights added to this network.
     *
     * Existing traffic lights should not have their yellow time changed after
     * this method is called.
     *
     * The yellow time must be at least one (1) second. If the argument provided
     * is below 1, throw an exception and do not set the yellow time.
     */
    public void setYellowTime(int yellowTime){
        if (yellowTime < 1){
            throw new IllegalArgumentException();
        }

        // Set yellow time
    }

    /**
     * Creates a new intersection with the given ID and adds it to this network.
     * @param id identifier of the intersection to be created;
     * @throws IllegalArgumentException if an intersection exists with the
     * given ID, or if the given id contains the colon character, or if the
     * ID contains only whitespace (space, newline, tab, etc.) characters.
     */
    public void createIntersection(String id) throws IllegalArgumentException{

    }

    /***
     * Creates a connecting route between the two intersections with the given
     * IDs.
     *
     * The new route should start at 'from' and end at 'to', and have a default
     * speed of 'defaultSpeed'.
     * @param from ID of the origin intersection
     * @param to   ID of the destination intersection
     * @param defaultSpeed speed limit of the route to create
     * @throws IntersectionNotFoundException if no intersection exists with
     * the ID 'from' or 'to'
     * @throws IllegalStateException if a route already exists between the given
     * two intersections
     * @throws IllegalArgumentException if defaultSpeed is negative
     */
    public void connectIntersections(String from, String to, int defaultSpeed)
            throws IntersectionNotFoundException, IllegalStateException,
            IllegalArgumentException{

    }
    /**
     * Adds traffic lights to the intersection with the given ID.
     *
     * The traffic lights will change every duration seconds and will cycle in
     * the order given by intersectionOrder, whereby each element in the list
     * represents the intersection from which each incoming route originates.
     * The yellow time will be the network's yellow time value.
     *
     * If the intersection already has traffic lights, the existing lights
     * should be completely overwritten and reset, and the new duration and
     * order should be set.
     *
     * @param intersectionId id of the intersection to add traffic lights to
     * @param duration number of seconds between traffic light cycles
     * @param intersectionOrder list of origin intersection IDs, traffic
     *                          lights will go green in this order.
     * @throws  IntersectionNotFoundException if no intersection with the
     * given ID exists.
     * @throws InvalidOrderException if the order specified is not a
     * permutation of the intersection's incoming routes; or if order is empty.
     * @throws IllegalArgumentException if the given duration is less than
     * the network's yellow time + 1
     *
     */
    public void addLights(String intersectionId, int duration,
                          List<String> intersectionOrder) throws
            IntersectionNotFoundException, InvalidOrderException,
            IllegalArgumentException{

    }

    /**
     * Adds an electronic speed sign on the route between the two given
     * intersections.
     *
     * The new speed sign should have an initial displayed speed of
     * 'initialSpeed'.
     *
     * @param from ID of origin intersection
     * @param to   ID of destination intersection
     *
     * @throws IntersectionNotFoundException if no intersection exists with
     * an ID given by 'from' or 'to'
     * @throws RouteNotFoundException if no route exists between the two
     * given intersections
     * @throws IllegalArgumentException if the given speed is negative
     */
    public void addSpeedSign(String from, String to, int initialSpeed)
            throws IntersectionNotFoundException, RouteNotFoundException{

    }

    /**
     * Sets the speed limit on the route between the two given intersections.
     *
     * Speed limits can only be changed on routes with an electronic speed sign.
     * Calling this method on a route without an electronic speed sign should
     * result in an exception.
     *
     *
     * @param from ID of origin intersection
     * @param to ID of destination intersection
     * @param newLimit new speed limit
     *
     * @throws IntersectionNotFoundException - if no intersection exists with
     * an ID given by 'from' or 'to'
     * @throws RouteNotFoundException - if no route exists between the two given
     * intersections
     *
     * @throws IllegalStateException - if the specified route does not have an
     * electronic speed sign
     * @throws IllegalArgumentException - if the given speed limit is negative
     */
    public void setSpeedLimit(String from, String to, int newLimit)
            throws IntersectionNotFoundException, RouteNotFoundException{

    }

    /**
     * Returns the route that connects the two given intersections.
     *
     * @param intersectionId id of target intersection
     * @param duration new duration of traffic lights
     *
     * @throws IntersectionNotFoundException if no intersection exists with
     * an ID given by 'intersectionID'
     * @throws IllegalStateException if the given intersection has no traffic
     * lights
     * @throws IllegalArgumentException if the duration is less than the
     * network's yellow time + 1
     */
    public void changeLightDuration(String intersectionId, int duration)
            throws IntersectionNotFoundException{

    }

    /**
     * Returns the route that connects the two given intersections.
     *
     * @param from ID of origin intersection
     * @param to ID of destination intersection
     *
     * @throws IntersectionNotFoundException if no intersection exists with an
     * ID given by 'to'
     * @throws RouteNotFoundException if no route exists between the two
     * given intersections
     */
    public Route getConnection(String from, String to)
            throws IntersectionNotFoundException, RouteNotFoundException{

        return null;
    }

    /**
     * Adds a sensor to the route between the two intersections with the given
     * IDs.
     *
     * @param from ID of intersection at which the route originates
     * @param to   Id of intersection at which the route ends
     * @param sensor sensor instance to add to the route
     *
     * @throws DuplicateSensorException if a sensor already exists on the
     * route with the same type
     * @throws IntersectionNotFoundException if no intersection exists with
     * an ID given by 'from' or 'to
     * @throws RouteNotFoundException if no route exists between the given
     * to/from intersections
     */
    public void addSensor(String from, String to, Sensor sensor)
            throws DuplicateSensorException, IntersectionNotFoundException,
            RouteNotFoundException{

    }

    /**
     * Returns the congestion levle on the route between the two given
     * intersections.
     *
     * @param from ID of origin intersection
     * @param to   ID of destination intersection
     *
     * @return congestion level (integer between 0 and 100 of connecting
     * route
     *
     * @throws IntersectionNotFoundException if no intersection exists with
     * an ID givn by 'from' or 'to'
     * @throws RouteNotFoundException        if no connecting route exists
     * between the given two intersections.
     */
    public int getCongestion(String from, String to)
            throws IntersectionNotFoundException, RouteNotFoundException{
        return 0;
    }

    /**
     * Attempts to find an Intersection instance in this network witht he
     * same identifier as the given 'id' string.
     *
     * @param id intersection identifer to search for
     *
     * @return intersection that was found (if one was found)
     *
     * @throws IntersectionNotFoundException if no intersection could be
     * found with the given identifier
     */
    public Intersection findIntersection(String id)
            throws IntersectionNotFoundException{
        return null;
    }

    /**
     * Creates a new connecting route in the opposite direction to an existing
     * route.
     *
     * The newly created route should start at the intersection with the ID
     * given by 'from' and end at the intersection with the ID given by 'to'.
     * It should have the same default speed limit as the current speed limit of
     * the existing route, as returned by Route.getSpeed().
     *
     * If the existing route has an electronic speed sign, then a new electronic
     * speed sign should be added to the new route with the same displayed speed
     * as the existing speed sign.
     *
     * @param from ID of intersection that the existing route starts at
     * @param to   ID of intersection that the existing route ends at
     *
     * @throws IntersectionNotFoundException if no intersection exists with
     * the ID given by 'from' or 'to'
     * @throws RouteNotFoundException if no route currently exists between
     * the two given intersections
     * @throws IllegalStateException if a route already exists in the
     * opposite direction to the existing route
     */
    public void makeTwoWay(String from, String to)
            throws IntersectionNotFoundException, RouteNotFoundException{

    }

    /**
     * Returns true if and only if this network is equal to the other given
     * network.
     *
     * For two networks to be equal, they must have the same number of
     * intersections, and all intersections in the first network must be
     * contained in the second network, and vice versa.
     *
     * Overrides equals in class object.
     *
     * @param obj other object to compare equality
     *
     * @return true if equal, false otherwise.
     */
    public boolean equals(Object obj){
        return false;
    }

    /**
     * Returns the string representation of this network.
     *
     * The format of the string to return is identical to that described in
     * NetworkInitialiser.loadNetwork(String). All intersections in the network,
     * including all connecting routes with their respective sensors, should be
     * included in the returned string.
     *
     * Intersections and routes should be listed in alphabetical order, similar
     * to the way in which sensors are sorted alphabetically in Route.toString()
     *
     * Comments (lines beginning with a semicolon character ";") are not added
     * to the string representation of a network.
     *
     * See the example network save file (demo.txt) for an example of the string
     * representation of a network.
     */
    public String toString(){
        return null;
    }

    /**
     * Returns a new list containing all the intersections in this network.
     * Adding/removing intersections from this list should not affect the
     * network's internal list of intersections.
     *
     * @return list of all intersections in this network.
     */
    public List<Intersection> getIntersections(){
        return null;
    }


}
