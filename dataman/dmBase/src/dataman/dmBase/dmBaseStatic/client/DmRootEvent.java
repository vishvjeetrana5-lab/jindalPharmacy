package dataman.dmBase.dmBaseStatic.client;

import com.google.gwt.user.client.Event;

public class DmRootEvent {

    private Event.NativePreviewEvent event;
    private boolean stopPropagation = false;

    public Event.NativePreviewEvent getEvent() {
        return event;
    }

    public void setEvent(Event.NativePreviewEvent event) {
        this.event = event;
    }

    public boolean isStopPropagation() {
        return stopPropagation;
    }

    public void stopPropagation() {
        this.stopPropagation = true;
    }
}
