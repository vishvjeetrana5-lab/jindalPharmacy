package dataman.payroll.employeeMaster.client;

import dataman.payroll.employeeMaster.shared.GrdSoftId_Mdl;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public abstract class SoftIdPopup_Dsg {

    final PopupPanel pnlForm = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    DmLabel lblGeneralPatientInfo = new DmLabel("");
    DmLabel lblServiceAmount = new DmLabel("-");

    DmLabel lblServiceTotalAmount = new DmLabel("-");
    DmLabel lblReceivedAmount = new DmLabel("-");
    DmLabel lblBalanceAmount = new DmLabel("-");

    Button btnRefreshServiceDetail = new Button("");
    Button btnExportServiceDetail = new Button("");
    final DmTextBox txtName = new DmTextBox();

    //<editor-fold defaultstate="collapsed" desc="GrdSoftId Detail">
    interface GrdSoftId_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdSoftId_Mdl> {

    }
    final DmGrid<GrdSoftId_Mdl, GrdSoftId_PrpAcs> grdSoftIdPopup = new DmGrid(com.google.gwt.core.client.GWT.create(GrdSoftId_PrpAcs.class));

    private Grid iniGrdSoftIdDetail() {
        grdSoftIdPopup.addRowNumberColumn();
        grdSoftIdPopup.addTextColumn(grdSoftIdPopup.getPropertyAccess().name(), "Soft Id", 150, "");
        grdSoftIdPopup.initializeGrid(grdSoftIdPopup.getPropertyAccess().fieldHashCode(), 190, 100);

        return grdSoftIdPopup.getGrid();
    }
    //</editor-fold>

    public SoftIdPopup_Dsg() {

        FlowPanel flw;
        VerticalPanel vrtPnl = new VerticalPanel();

        DmClient.setStyle(vrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        pnlForm.setGlassEnabled(true);
        pnlForm.setWidth("250px");
        pnlForm.setHeight("160px");
        vrtPnl.add(iniToolControls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtPnl.add(flw);
        vrtPnl.setCellHeight(flw, "100%");
        pnlForm.add(vrtPnl);

    }

    private DmToolContainer iniToolControls() {
        InlineLabel label;
        Anchor aBtn;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        toolContainer.setTitle("Soft Id Detail");
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "135px", null, null, DmPosition.CENTER, true);


        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "28px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "26%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Employee");
        flwTd.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "71%", null, "left", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "115px", "left", null, DmPosition.CENTER, true);
        flwTr.add(vl.add(iniGrdSoftIdDetail()));
        flwTable.add(flwTr);
        /*====================================< New Row >====================================*/

        return flwTable;
    }

}
