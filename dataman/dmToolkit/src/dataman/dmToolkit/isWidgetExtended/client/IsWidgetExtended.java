package dataman.dmToolkit.isWidgetExtended.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmCallBack;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmToolkit.dmStatic.client.DmClient;
import java.util.ArrayList;
import java.util.List;

public abstract class IsWidgetExtended implements IsWidget {

    @Override
    public abstract Widget asWidget();

    public abstract void searchRecord(String key);

    public void action(String nature, Object object) {

    }

    public void action(String nature, Object object, DmCallBack callBack) {

    }

    public DmClient.ViaList getViaList() {
        return null;
    }
}
