package dataman.dmWebCamera.self.client;

import elemental.html.VideoElement;

public class ElementalUtils {

    public final native static com.google.gwt.dom.client.Element castElementToElement(elemental.dom.Element e) /*-{
     return e;
     }-*/;

    public static native String b64decode(String a) /*-{
     return window.atob(a);
     }-*/;

    public final native boolean isSupported() /*-{
     navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;
     return !!navigator.getUserMedia;
     }-*/;

    public native static boolean getUserVideo(VideoElement element, UserMediaCallback mediacb) /*-{
     var doError = function(message) {
     var e = @dataman.dmWebCamera.self.client.UserMediaException::new(Ljava/lang/String;)(message);
     mediacb.@dataman.dmWebCamera.self.client.UserMediaCallback::onError(Ldataman/dmWebCamera/self/client/UserMediaException;)(e);
     }
     navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mediaDevices.getUserMedia || navigator.msGetUserMedia;
     if (navigator.getUserMedia) {
     var onReady = function(stream) {
     element.srcObject = stream;
     element.volume = 0.0;
     element.play();
     mediacb.@dataman.dmWebCamera.self.client.UserMediaCallback::onReady(Lcom/google/gwt/dom/client/VideoElement;Ldataman/dmWebCamera/self/client/UserMediaStream;)(element, stream);
     };
     var onError = function(error) {
     var type = typeof error;
     if (type === 'string') {
     if (error == 'PERMISSION_DENIED') {
     doError('PERMISSION_DENIED');
     } else {
     doError('CONSTRAINT_NOT_SATISFIED');
     }
     } else {
     if (error.name) {
     doError(error.name);
     } else {
     if (error['PERMISSION_DENIED']) {
     doError('PERMISSION_DENIED');
     } else {
     doError('CONSTRAINT_NOT_SATISFIED');
     }
     }
     }
     };
     navigator.getUserMedia({video: true, toString: function() {return "video";}}, onReady, onError);
     return true;
     } else {
     doError('NOT_SUPPORTED');
     return false;
     }
     }-*/;
}
