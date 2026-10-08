package dataman.dmToolkitR.dmNotify.client;

import com.google.gwt.user.client.ui.RootPanel;

public class DmNotify extends DmNotify_Dsg {

    public DmNotify() {
        super();
    }

    public void getNotify(String msg) {
        span.setHTML(msg);
        RootPanel.get().add(flwNotify);
        flwNotify.addStyleName("dm-notify-animate");
    }
}
