package dataman.dmToolkitR.dmGridHelp.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

abstract class DmGrid_Dsg implements IsWidget {

    FlowPanel flowPanel = new FlowPanel();

    public DmGrid_Dsg() {
        ini();
    }

    private void ini() {
        flowPanel.setStyleName("dmGrid");
//        flowPanel.setHeight((Window.getClientHeight() - 50) + "px");
    }
    public void setDmGridSize (int size){
        flowPanel.getElement().setAttribute("style", "min-width:" + size + "px");
    }
}
