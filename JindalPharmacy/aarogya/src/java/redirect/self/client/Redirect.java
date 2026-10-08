package redirect.self.client;

import redirect.changePassword.client.ChangePassword;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import dataman.dmToolkit.dmStatic.client.DmClient;

public class Redirect implements EntryPoint {

    @Override
    public void onModuleLoad() {
        DmClient.initializeVariables(true);
        ini();
    }

    private void ini() {
        final String action = Window.Location.getParameter("a");

        switch (action) {
            case "changePassword":
                applyWidget(new ChangePassword(Window.Location.getParameter("u"), Window.Location.getParameter("id")));
                break;
//            case "publicDisplaySystem":
//                applyWidget(new PublicDisplaySystem(Window.Location.getParameter("did")));
//                break;
            default:
                break;
        }
    }

    private void applyWidget(IsWidget wgtObject) {
        DmClient.getRoot().add(wgtObject);
    }

}
