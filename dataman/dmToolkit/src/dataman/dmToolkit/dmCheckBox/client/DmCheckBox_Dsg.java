package dataman.dmToolkit.dmCheckBox.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;

class DmCheckBox_Dsg extends FocusWidget {
    final FlowPanel checkBox = new FlowPanel();
            
    public DmCheckBox_Dsg() {
        super();
        super.setElement(checkBox.getElement());
        checkBox.setStyleName("dm-CheckBox");
    }
}
