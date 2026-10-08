package dataman.dmBase.dmEventbus.server;

public interface EventBus<E extends Event> {

    void subscribe(EventHandler<E> subscriber);

    void unsubscribe(EventHandler<E> subscriber);

    void publish(E event);

    boolean hasPendingEvents();

}
