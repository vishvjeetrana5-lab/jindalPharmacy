package dataman.webJobs.staffInfo.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel; 
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public abstract class StaffInfo_Dsg {

    public final static String widgetName = "Staff Info";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final FlowPanel flwOk = new FlowPanel();
    final FlowPanel flwOkChild = new FlowPanel();
    final Button btnOk = new Button();
    final Button btnClose = new Button();

    public StaffInfo_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;

        VerticalPanel vrtInnerForm = new VerticalPanel();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("300px");
        popupPanel.setHeight("100px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        DmClient.setStyle(flw, "100%", "120px", null, null, DmPosition.CENTER, true);
        flw.add(vl.add(iniGrd()));
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    //<editor-fold defaultstate="collapsed" desc="Staff Info">
    interface GrdStaffInfo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {
    }

    final DmGrid<KeyFieldExtended, GrdStaffInfo_PrpAcs> grdStaffInfo = new DmGrid(GWT.create(GrdStaffInfo_PrpAcs.class));

    private Grid iniGrd() {
        grdStaffInfo.addRowNumberColumn();
        grdStaffInfo.addTextColumn(grdStaffInfo.getPropertyAccess().name(), "Staff", 265, "");
        grdStaffInfo.initializeGrid(grdStaffInfo.getPropertyAccess().fieldHashCode(), 295, 118, 1);
        return grdStaffInfo.getGrid();
    }
    //</editor-fold>

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOkChild.add(vl.add(btnOk));
        flwOk.add(flwOkChild);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(vl.add(btnClose));
        toolContainer.addGap();
        toolContainer.setTitle(widgetName);

        return toolContainer;
    }

}
