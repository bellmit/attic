package ca.grimoire.events;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Dispatches events for a given listener type. When events are fired, the known
 * listeners are fired from the most-recently-added backwards (to allow for
 * listeners that intercept events).
 * 
 * @param <L>
 *            the event listener interface.
 */
public class Dispatcher<L> {

    private final LinkedList<L> listeners = new LinkedList<L>();

    /**
     * Adds a listener to the head of the listener queue. Subsequent event
     * firings will include the passed listener.
     * 
     * @param listener
     *            the listener to add.
     */
    public void addListener(L listener) {
        if (listener == null)
            throw new IllegalArgumentException("listener");

        listeners.addFirst(listener);
    }

    /**
     * Dispatches an event. The firer's {@link Firer#fire(Object)} method will
     * be applied to every listener in the dispatcher, from the most recently
     * added to the oldest.
     * 
     * @param firer
     *            the event firer implementation.
     */
    public void fire(Firer<L> firer) {
        if (firer == null)
            throw new IllegalArgumentException("firer");

        List<L> listeners = copyListeners();

        for (L listener : listeners)
            firer.fire(listener);
    }

    /**
     * Removes a listener from the dispatcher. If the same listener has been
     * added more than once, this removes only the most recently added
     * occurrence.
     * 
     * @param listener
     *            the listener to remove.
     */
    public void removeListener(L listener) {
        listeners.remove(listener);
    }

    private List<L> copyListeners() {
        return new ArrayList<L>(this.listeners);
    }
}
