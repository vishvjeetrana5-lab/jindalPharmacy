package dataman.dmToolkitR.dmGUI.client;

import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmToolkitR.dmStatic.client.DmClient;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

abstract class DmGUI_Dsg {

    final FlowPanel mainContainer = new FlowPanel();

    public DmGUI_Dsg(int height) {
        mainContainer.addStyleName("dm-patient-name");
        DmClient.setStyle(mainContainer, "100%", "100%", "left", null, DmPosition.NONE, true);
    }

}
