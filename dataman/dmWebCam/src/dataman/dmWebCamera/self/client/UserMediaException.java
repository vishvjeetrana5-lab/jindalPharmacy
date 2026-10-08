package dataman.dmWebCamera.self.client;

public class UserMediaException extends Exception {

    public UserMediaException(final String message) {
        super(message);
        com.google.gwt.user.client.Window.alert(message);
    }
}
