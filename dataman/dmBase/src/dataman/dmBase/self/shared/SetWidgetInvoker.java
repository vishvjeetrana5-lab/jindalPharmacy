package dataman.dmBase.self.shared;

import com.google.gwt.user.client.ui.IsWidget;
import java.util.HashMap;

public interface SetWidgetInvoker {
 
    public void setWidget(IsWidget wgtObject);

    public void setWidget(String moduleName, String menuText, SetWidgetInvoker swi);

    public void setWidget(String moduleName, String menuText, HashMap paramHashMap, UserMenu userMenu, SetWidgetInvoker swi);

    public void closeWidget(IsWidget wgtObject);
}
