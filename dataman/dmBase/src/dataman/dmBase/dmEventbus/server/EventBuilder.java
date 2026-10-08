package dataman.dmBase.dmEventbus.server;

import java.lang.reflect.Constructor;

public class EventBuilder<E extends Event> {

    private final E event;

    public EventBuilder(E event) {
        this.event = event;
    }

    public static EventBuilder create(String eventType) {
        return new EventBuilder(new Event(eventType));
    }

    public static <T extends Event> EventBuilder<T> create(Class<T> cls, String eventType) {
        try {
            Constructor<T> constructor = cls.getConstructor(String.class);
            return new EventBuilder<>(constructor.newInstance(eventType));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Can not create event builder for " + cls + " and type " + eventType, ex);
        }
    }

    public EventBuilder<E> set(String key, Object value) {
        event.set(key, value);
        return this;
    }

    public E build() {
        return this.event;
    }
}
