package dataman.dmBase.dmEventbus.server;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EventBusSimple<E extends Event> implements EventBus<E> {

    private final AtomicInteger processing = new AtomicInteger();

    private final Set<WeakHandler> handlers = Collections.newSetFromMap(new ConcurrentHashMap<WeakHandler, Boolean>());

    @Override
    public void subscribe(EventHandler<E> subscriber) {
        handlers.add(new WeakHandler(subscriber));
    }

    @Override
    public void unsubscribe(EventHandler<E> subscriber) {
        handlers.remove(new WeakHandler(subscriber));
    }

    @Override
    public void publish(E event) {
        if (event == null) {
            return;
        }
        processing.incrementAndGet();
        try {
            event.lock();
            processEvent(event);
        } finally {
            processing.decrementAndGet();
        }
    }

    @Override
    public boolean hasPendingEvents() {
        return processing.get() > 0;
    }

    private void processEvent(E event) {
        if (event != null) {
            notifySubscribers(event);
        }
    }

    private void notifySubscribers(E event) {
        for (WeakHandler wh : handlers) {
            EventHandler eh = wh.getEventHandler();
            if (eh == null) {
                continue;
            }

            try {
                if (eh.getType() == null) {
                    if (eh.canHandle(event.getType())) {
                        eh.handle(event);
                    }
                } else if (eh.getType().equals(event.getType())) {
                    eh.handle(event);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
