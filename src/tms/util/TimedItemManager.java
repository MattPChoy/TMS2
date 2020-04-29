package tms.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages timed items for the simulation. All timed items in the simulation
 * need to be registered with this manager.
 * <p>
 * All registered timed items will have their {@code oneSecond()} method
 * called when {@code TimedItemManager.oneSecond()} is called.
 * <p>
 * This is a singleton class. See the assignment specification and the provided
 * links for more information about the purpose of a singleton and how to
 * implement it.
 * <p>
 * In order to ensure correct singleton behaviour, there should <b>not</b>
 * be a public constructor.
 *
 * @see <a href = "https://refactoring.guru/design-patterns/singleton">
 *                 https://refactoring.guru/design-patterns/singleton</a> and
 *      <a href = "https://www.geeksforgeeks.org/singleton-class-java/">
 *                 https://www.geeksforgeeks.org/singleton-class-java/</a>
 * @ass1
 */
public class TimedItemManager implements TimedItem {
    private static TimedItemManager manager;
    private List<TimedItem> timedItems;

    /**
     * Creates the timed item manager that stores all the {@link TimedItem}s
     * in this simulation.
     * @ass1
     */
    private TimedItemManager() {
        timedItems = new ArrayList<TimedItem>();
    }

    /**
     * Register a TimedItem such that it is called on
     * {@link TimedItemManager#oneSecond()}.
     *
     * @param timedItem a TimedItem to register with the manager
     * @ass1
     */
    public void registerTimedItem(TimedItem timedItem) {
        timedItems.add(timedItem);
    }

    /**
     * Gets a singleton instance of the TimedItemManager and makes one if
     * required.
     * @return the singleton instance of the TimedItemManager
     * @ass1
     */
    public static TimedItemManager getTimedItemManager () {
        if (manager == null) {
            manager = new TimedItemManager();
        }
        return manager;
    }

    /**
     * Calls {@code oneSecond()} on each registered {@link TimedItem}.
     * @ass1
     */
    public void oneSecond() {
        for (TimedItem timedItem : timedItems) {
            timedItem.oneSecond();
        }
    }
}
