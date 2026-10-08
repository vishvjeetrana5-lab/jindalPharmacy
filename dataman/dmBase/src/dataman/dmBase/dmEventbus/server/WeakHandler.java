package dataman.dmBase.dmEventbus.server;

class WeakHandler {

    private final int hash;

    private EventHandler eventHandler;

    WeakHandler(EventHandler handler) {
        eventHandler = handler;
        hash = handler.hashCode();
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WeakHandler)) {
            return false;
        }

        Object t = this;
        Object u = ((WeakHandler) obj);
        if (t == u) {
            return true;
        }
        if (t == null || u == null) {
            return false;
        }
        return t.equals(u);
    }
}
