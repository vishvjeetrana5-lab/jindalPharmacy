package dataman.dmWebCamera.self.client;

import com.google.gwt.dom.client.VideoElement;
import com.google.gwt.user.client.ui.Widget;

public class VideoWidget extends Widget {

    private VideoElement videoElem;
    private elemental.html.VideoElement eVideoElem;

    public VideoWidget(com.google.gwt.dom.client.VideoElement videoElement, elemental.html.VideoElement eVideoElement) {
        this.eVideoElem = eVideoElement;
        setElement(videoElement);
    }

    public elemental.html.VideoElement getElemetalVideoElement(){
        return this.eVideoElem;
    }
    
    public com.google.gwt.dom.client.VideoElement getVideoElement() {
        return videoElem;
    }
}
