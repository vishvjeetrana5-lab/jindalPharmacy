package dataman.dmWebCamera.self.client;

import elemental.client.Browser;
import elemental.html.CanvasElement;
import elemental.html.CanvasRenderingContext2D;
import elemental.html.VideoElement;

//keeps the url of the usermedia to avoid unecessary 'getUserMedia' requests
public class UserMediaHolder implements UserMediaCallback {

    private boolean userMediaAvailable = false;
    private String userMediaUrl;
    private VideoWidget videoElement;
    private UserMediaStream mediaStream;
    private IWebcamAvailableCallback callback;
    private elemental.html.VideoElement eVidElem;

    public UserMediaHolder(IWebcamAvailableCallback callBack) {
        this.callback = callBack;
    }

    protected interface IWebcamAvailableCallback {

        public void onWebCamAvailability();
    }

    public VideoWidget getVideoWidget() {
        return this.videoElement;
    }

    public String getInstantaneousImage(){
        return makePicture(this.videoElement.getElemetalVideoElement(), Browser.getDocument().createCanvasElement());
    }
    
    private String makePicture(VideoElement videoElement, CanvasElement canvasElement) {
        canvasElement.setWidth(videoElement.getVideoWidth());
        canvasElement.setHeight(videoElement.getVideoHeight());
        elemental.html.CanvasRenderingContext2D ctx = (CanvasRenderingContext2D) canvasElement.getContext("2d");
        ctx.drawImage(videoElement, 0, 0, videoElement.getVideoWidth(),videoElement.getVideoHeight(),0,0,videoElement.getVideoWidth(),videoElement.getVideoHeight());
        return canvasElement.toDataURL("image/png");
    }

    public boolean initialize() {
        boolean bool = false;
        try {
            this.eVidElem= Browser.getDocument().createVideoElement();
            ElementalUtils.getUserVideo(this.eVidElem, this);
        } catch (Exception ex) {
            bool = false;
        }
        return bool;
    }

    @Override
    public void onReady(com.google.gwt.dom.client.VideoElement element, UserMediaStream stream) {
        userMediaAvailable = true;
        this.videoElement = new VideoWidget(element,this.eVidElem);
        mediaStream = stream;
        if (this.callback != null) {
            this.callback.onWebCamAvailability();
        }
    }

    @Override
    public void onError(UserMediaException exception) {
        destroyResources();
    }

    private static native String getBrowserName() /*-{
     return navigator.userAgent.toLowerCase();
     }-*/;

    private static boolean isChromeBrowser() {
        return getBrowserName().toLowerCase().contains("chrome");
    }

    private static boolean isFirefoxBrowser() {
        return getBrowserName().toLowerCase().contains("firefox");
    }

    private static boolean isIEBrowser() {
        return getBrowserName().toLowerCase().contains("msie");
    }

    public boolean destroyResources() {
        boolean bool = false;
        try {
            userMediaAvailable = false;
            if (mediaStream != null) {
                if (isChromeBrowser()) {
                    mediaStream.stopAllVideoTracks();
                } else if (isFirefoxBrowser()) {
                    mediaStream.stopStream();
                }
                mediaStream = null;
            }
            if (this.videoElement != null) {
                if (this.videoElement.getParent() != null) {
                    this.videoElement.removeFromParent();
                    this.videoElement.getVideoElement().setSrc("");
                }
                this.videoElement = null;
            }
            userMediaUrl = "";
        } catch (Exception ex) {
            bool = false;
        }
        return bool;
    }
}
