package ca.grimoire.events;

import java.util.concurrent.Callable;

/**
 * A {@link Callable}-like interface for delivering events.
 * 
 * @param <L>
 *            the event listener type being delivered to.
 * @see Dispatcher
 */
public interface Firer<L> {

    /**
     * Delivers an event to a single listener object.
     * 
     * @param listener
     *            the listener to deliver to.
     */
    public void fire(L listener);

}
