package dataman.dmToolkit.dmToolContainer.client;

import dataman.dmToolkit.self.client.DmToolkit_Res;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;

abstract class DmToolContainer_Dsg {
//<editor-fold defaultstate="collapsed" desc="variable decalrations">

    final DmToolkit_Res.dmCss dcStyle = DmToolkit_Res.rsObj.dcStyle();
    final HorizontalPanel hrtMainPnl = new HorizontalPanel();
    final HorizontalPanel hrtBtnPnl = new HorizontalPanel();

    final InlineLabel lblMode = new InlineLabel("");
    final InlineLabel lblTitle = new InlineLabel();
//</editor-fold>

    public DmToolContainer_Dsg() {
        dcStyle.ensureInjected();
        Ini();
    }

    private void Ini() {
        hrtBtnPnl.setWidth("100%");
        hrtMainPnl.add(hrtBtnPnl);
        hrtMainPnl.setCellWidth(hrtBtnPnl, "100%");

        lblMode.getElement().setAttribute("style", lblMode.getElement().getAttribute("style")
                + "; max-width:60px; font-weight: 600; color: #fff; float: right; padding: 3px 9px;margin-top: 5px;"
                + ";font-family: arial;font-weight: bold;font-size: 12px;margin-right: 5px;background-color: rgb(251, 140, 0);"
        );

        lblTitle.getElement().setAttribute("style", lblTitle.getElement().getAttribute("style")
                + ";max-width:165px; color: white;float: right;padding-right: 9px;padding-top: 8px;height: 17px;"
                + "background-color: rgb(81, 99, 149);font-family: arial;font-weight: bold;font-size: 12px;padding-left: 5px;"
        );
        lblTitle.setStyleName("ellipsis");

        hrtMainPnl.add(lblMode);
        hrtMainPnl.add(lblTitle);

        hrtMainPnl.addStyleName("dm-ToolContainer");
    }
}
