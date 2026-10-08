package dataman.dmToolkitR.dmLabel.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class DmLabel extends DmLabel_Dsg implements IsWidget {

    public DmLabel(String strCaption) {
        super(strCaption);
    }

    @Override
    public Widget asWidget() {
        return this;
    }

}
