package dataman.dmBase.dmEventbus.server;

public abstract class AbstractEventHandler<E extends Event> implements EventHandler<E> {

    private String eventName;

    public AbstractEventHandler(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public final String getType() {
        return this.eventName;
    }

    @Override
    public final boolean canHandle(String eventName) {
        boolean isHandlable = false;
        if (eventName == null) {
            isHandlable = false;
        } else if (eventName.equalsIgnoreCase(this.eventName)) {
            isHandlable = true;
        }
        return isHandlable;
    }

}
