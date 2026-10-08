package dataman.dmWebCamera.self.client;

import com.google.gwt.dom.client.VideoElement;


public interface UserMediaCallback
{
    public void onReady(VideoElement element, UserMediaStream stream);

    public void onError(UserMediaException exception);
}