package dataman.dmWebCamera.self.client;

import com.google.gwt.core.client.JavaScriptObject;

public class UserMediaStream extends JavaScriptObject {

    protected UserMediaStream() {
    }

    public final native void stopStream() /*-{
     return this.stop();
     }-*/;

    public final native void stopAllVideoTracks() /*-{
     this.getVideoTracks().forEach(function (track) {
     track.stop();
     });
     }-*/;

    public final native boolean isActive() /*-{
     return this.active;
     }-*/;

    public final native String getID() /*-{
     return this.id;
     }-*/;
}
