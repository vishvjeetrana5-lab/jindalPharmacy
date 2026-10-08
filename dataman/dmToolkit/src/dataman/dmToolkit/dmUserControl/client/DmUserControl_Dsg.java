package dataman.dmToolkit.dmUserControl.client;

import dataman.dmToolkit.self.client.DmToolkit_Res;
import com.google.gwt.user.client.ui.HorizontalPanel;

abstract class DmUserControl_Dsg {

    final DmToolkit_Res.dmCss dcStyle = DmToolkit_Res.rsObj.dcStyle();
    final HorizontalPanel hrtMainPnl = new HorizontalPanel();

    public DmUserControl_Dsg() {
        dcStyle.ensureInjected();
        Ini();
    }

    private void Ini() {
//        hrtMainPnl.setCellWidth(hrtBtnPnl, "100%");
        hrtMainPnl.addStyleName("dm-ToolContainer");
        hrtMainPnl.getElement().setAttribute("style", hrtMainPnl.getElement().getAttribute("style")
                + ";background:white;border-top:1px solid gray;");
    }
}
