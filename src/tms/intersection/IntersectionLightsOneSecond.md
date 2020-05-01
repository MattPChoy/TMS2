# IntersectionLights' OneSecond Method
Finding this method a little confusing, so creating a file to document the
 development process.
 
 From the JavaDoc, we have the following parameters:
 ```
 List<Route> connections // A non-empty list of incoming routes
 int yellowTime          // The time in seconds for which lights appear yellow
 int duration            // Time in seconds for which lights will appear
 yellow or green.
```

From this we can conclude that the amount of time for which a traffic light
 is green is given by duration-yellowTime.
 
##Time by Time:
 * From the note above, greenTime = duration - yellowTime
 * From time = 0 to time = (duration - yellowTime),
 >> TrafficSignal.GREEN | TrafficSignal.RED | TrafficSignal.RED
> | TrafficSignal.RED
 * From time = ((duration - yellowTime) + 1) to (duration)
 >> TrafficSignal.YELLOW | TrafficSignal.RED | TrafficSignal.RED
> | TrafficSignal.RED
 * From time = (duration + 1) to time = 2*duration - yellowTime
 >> TrafficSignal.RED | TrafficSignal.GREEN | TrafficSignal.RED
> | TrafficSignal.RED
 * From time = (2*duration - yellowTime + 1) to time = 2*duration (first case)
 >> TrafficSignal.RED | TrafficSignal.YELLOW | TrafficSignal.RED
> | TrafficSignal.RED 

## Implementation Flow
 1. Create a <code>package-private</code> variable called duration (so that
  the value can be edited by the setDuration method).
 2. If <code>connections.size() == 0</code> return nothing to exit out of the
    method without breaking the running code.
    of incomingRoutes, or if order is empty.
 3. For each intersection in <b>connection</b>, if there isn't a
    TrafficLight, create one? <b>@1103 on Piazza</b>

   
 ##References
 
 ###JavaDoc
 */tms/intersection/IntersectionLights > Constructor*
 * The first route in the given list of incoming routes should have its
  TrafficLight signal set to <b>TrafficSignal.GREEN</b> (Enum)
  
  ###@1032 on Piazza
  * When <code>intersectionLights.setDuration(int duration)</code> is called
 , duration = 0
  * After calling the <code>intersectionLights.oneSecond()</code> method the
   duration should be 1 second.
   
 ### @998 on Piazza
 * Add a functionality check that if <code>connections.size() == 0</code>
  then return and do nothing as it it is part of the spec (but as result of
   the structure of the assignment, the feature to remove routes is not
    currently implemented).
    
    
 
   