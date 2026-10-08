package dataman.dmBase.dmEventbus.server;

public interface EventHandler<E extends Event> {

    String getType();

    boolean canHandle(String eventType);

    void handle(E event);
}
