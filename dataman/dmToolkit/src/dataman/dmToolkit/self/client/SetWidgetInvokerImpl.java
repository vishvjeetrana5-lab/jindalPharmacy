package dataman.dmToolkit.self.client;

import com.google.gwt.user.client.ui.IsWidget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import java.util.HashMap;

public abstract class SetWidgetInvokerImpl implements SetWidgetInvoker {

    public abstract void setWidget(IsWidget wgtObject);

    @Override
    public void setWidget(String moduleName, String menuText, SetWidgetInvoker swi) {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuText, swi);
    }

    @Override
    public void setWidget(String moduleName, String menuText, HashMap paramHashMap, UserMenu userMenu, SetWidgetInvoker swi) {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuText, paramHashMap, userMenu, swi);
    }

    @Override
    public void closeWidget(IsWidget wgtObject) {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(wgtObject);
    }

}
