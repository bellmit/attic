package ca.grimoire.events;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class DispatcherTest {
    private final Mockery mockery = new JUnit4Mockery();

    private static interface Listener {
        public void onEvent();
    }

    @Test
    public void oneListener() {
        Dispatcher<Listener> dispatcher = new Dispatcher<Listener>();

        final Listener listener = mockery.mock(Listener.class);

        dispatcher.addListener(listener);

        mockery.checking(new Expectations() {
            {
                one(listener).onEvent();
            }
        });

        dispatcher.fire(new Firer<Listener>() {
            public void fire(Listener listener) {
                listener.onEvent();
            }
        });
    }

    @Test
    public void twoListeners() {
        Dispatcher<Listener> dispatcher = new Dispatcher<Listener>();

        final Listener listenerOne = mockery.mock(Listener.class,
                "listener one");
        final Listener listenerTwo = mockery.mock(Listener.class,
                "listener two");

        dispatcher.addListener(listenerOne);
        dispatcher.addListener(listenerTwo);

        mockery.checking(new Expectations() {
            {
                Sequence eventSequence = mockery
                        .sequence("Event delivery sequence");

                // We actually expect events to be delivered in the *opposite*
                // order as the listeners were added. See the class docs for
                // details.
                one(listenerTwo).onEvent();
                inSequence(eventSequence);

                one(listenerOne).onEvent();
                inSequence(eventSequence);
            }
        });

        dispatcher.fire(new Firer<Listener>() {
            public void fire(Listener listener) {
                listener.onEvent();
            }
        });
    }

    @Test
    public void removeListener() {
        Dispatcher<Listener> dispatcher = new Dispatcher<Listener>();

        final Listener listenerOne = mockery.mock(Listener.class,
                "listener one");
        final Listener listenerTwo = mockery.mock(Listener.class,
                "listener two");

        dispatcher.addListener(listenerOne);
        dispatcher.addListener(listenerTwo);

        dispatcher.removeListener(listenerOne);

        mockery.checking(new Expectations() {
            {
                one(listenerTwo).onEvent();
            }
        });

        dispatcher.fire(new Firer<Listener>() {
            public void fire(Listener listener) {
                listener.onEvent();
            }
        });
    }

    @Test
    public void removalWhileIterating() {
        final Dispatcher<Listener> dispatcher = new Dispatcher<Listener>();

        final Listener listenerOne = mockery.mock(Listener.class,
                "listener one");
        final Listener listenerTwo = mockery.mock(Listener.class,
                "listener two");

        dispatcher.addListener(listenerOne);
        dispatcher.addListener(new Listener() {
            public void onEvent() {
                dispatcher.removeListener(listenerOne);
                dispatcher.removeListener(listenerTwo);
            }
        });
        dispatcher.addListener(listenerTwo);

        mockery.checking(new Expectations() {
            {
                Sequence eventSequence = mockery
                        .sequence("Event delivery sequence");

                // Removal won't stop delivery this time around.
                one(listenerTwo).onEvent();
                inSequence(eventSequence);

                one(listenerOne).onEvent();
                inSequence(eventSequence);

                // The second time around, nothing happens.
            }
        });

        dispatcher.fire(new Firer<Listener>() {
            public void fire(Listener listener) {
                listener.onEvent();
            }
        });

        dispatcher.fire(new Firer<Listener>() {
            public void fire(Listener listener) {
                listener.onEvent();
            }
        });
    }
}
