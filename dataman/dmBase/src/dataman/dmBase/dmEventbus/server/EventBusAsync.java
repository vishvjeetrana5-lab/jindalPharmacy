package dataman.dmBase.dmEventbus.server;

import java.util.Collections;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventBusAsync<E extends Event> implements EventBus<E> {

    private final Thread eventQueueThread;

    private final Queue<E> eventsQueue = new ConcurrentLinkedQueue<>();

    private final Set<WeakHandler> handlers = Collections.newSetFromMap(new ConcurrentHashMap<WeakHandler, Boolean>());

    //private final ExecutorService handlersExecutor;
    public EventBusAsync() {
        //this(Executors.newCachedThreadPool());
//        this.handlersExecutor = handlersExecutor;
        eventQueueThread = new Thread(this::eventsQueue, "EventQueue handlers thread");
        eventQueueThread.setDaemon(true);
        eventQueueThread.start();
    }

//    public EventBusAsync(ExecutorService handlersExecutor) {
//        this.handlersExecutor = handlersExecutor;
//        eventQueueThread = new Thread(this::eventsQueue, "EventQueue handlers thread");
//        eventQueueThread.setDaemon(true);
//        eventQueueThread.start();
//    }
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
        event.lock();
        eventsQueue.add(event);
    }

    @Override
    public boolean hasPendingEvents() {
        return !eventsQueue.isEmpty();
    }

    private void eventsQueue() {
        while (true) {
            E event = eventsQueue.poll();
            if (event != null) {
                notifySubscribers(event);
            }
        }
    }

    private void notifySubscribers(E event) {
        ExecutorService handlersExecutor = null;
        try {
            handlersExecutor = Executors.newCachedThreadPool();

            for (WeakHandler wh : handlers) {
                EventHandler eh = wh.getEventHandler();
                if (eh == null) {
                    continue;
                }

                try {
                    if (eh.getType() == null) {
                        if (eh.canHandle(event.getType())) {
                            handlersExecutor.submit(() -> {
                                runHandler(eh, event);
                            });
                        }
                    } else if (eh.getType().equals(event.getType())) {
                        handlersExecutor.submit(() -> {
                            runHandler(eh, event);
                        });
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (handlersExecutor != null) {
                handlersExecutor.shutdown();
                handlersExecutor = null;
            }
        }
    }

    private void runHandler(EventHandler eh, E event) {
        try {
            eh.handle(event);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
