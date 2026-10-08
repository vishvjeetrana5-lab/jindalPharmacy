package dataman.payroll.employeeMaster.client;

import dataman.payroll.employeeMaster.shared.GrdLeave_Mdl;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
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
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public abstract class LeavePopup_Dsg {

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

    //<editor-fold defaultstate="collapsed" desc="Grd Leave Detail">
    interface GrdLeave_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLeave_Mdl> {

        @Editor.Path("Leaves")
        ValueProvider<GrdLeave_Mdl, String> leaves();

        @Editor.Path("FromDate")
        ValueProvider<GrdLeave_Mdl, String> fromDate();

        @Editor.Path("ToDate")
        ValueProvider<GrdLeave_Mdl, String> toDate();

        @Editor.Path("Leave")
        ValueProvider<GrdLeave_Mdl, String> leave();
        
    }
    final DmGrid<GrdLeave_Mdl, GrdLeave_PrpAcs> grdLeavePopup = new DmGrid(com.google.gwt.core.client.GWT.create(GrdLeave_PrpAcs.class));

    private Grid iniGrdLeaveDetail() {
        grdLeavePopup.addRowNumberColumn();
        grdLeavePopup.addTextColumn(grdLeavePopup.getPropertyAccess().leaves(), "Leaves", 50, "");
        grdLeavePopup.addTextColumn(grdLeavePopup.getPropertyAccess().fromDate(), "From Date", 100, "");
        grdLeavePopup.addTextColumn(grdLeavePopup.getPropertyAccess().toDate(), "To Date", 100, "");
        grdLeavePopup.addTextColumn(grdLeavePopup.getPropertyAccess().leave(), "Leave", 80, "");
        grdLeavePopup.initializeGrid(grdLeavePopup.getPropertyAccess().fieldHashCode(), 400, 250);

        return grdLeavePopup.getGrid();
    }
    //</editor-fold>

    public LeavePopup_Dsg() {

        FlowPanel flw;
        VerticalPanel vrtPnl = new VerticalPanel();

        DmClient.setStyle(vrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        pnlForm.setGlassEnabled(true);
        pnlForm.setWidth("410px");
        pnlForm.setHeight("308px");
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

        toolContainer.setTitle("Leave Detail");
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
//        DmClient.setStyle(flwTable, "99%", "511px", null, null, DmPosition.CENTER, true);
        DmClient.setStyle(flwTable, "99%", "280px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
//        flwTr.getElement().getStyle().setBackgroundColor("rgb(224, 255, 255)");
//        lblGeneralPatientInfo.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//        lblGeneralPatientInfo.getElement().getStyle().setColor("Gray");
//        lblGeneralPatientInfo.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
//        flwTr.add(lblGeneralPatientInfo);
//        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "28px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Employee Name");
        flwTd.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "71%", null, "left", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, true);
//        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
//        flwTd.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
//        lbl = new DmLabel("Employee Name ");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, true);
//        flwTd.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
//        flwTd.add(vl.add(txtHServiceCategory));
//        flwTr.add(flwTd);
//
////        flwTable.add(flwTr);
//        //
//        btnRefreshServiceDetail.addStyleName("fa fa-refresh");
//        btnRefreshServiceDetail.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: blue;cursor: pointer;");
//        btnRefreshServiceDetail.getElement().getStyle().setFontSize(12, Style.Unit.PX);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "2%", "25px", "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
//        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
//        btnRefreshServiceDetail.setTitle("Reset Filter");
//        flwTd.add(btnRefreshServiceDetail);
//        flwTr.add(flwTd);
//        //
//        //
//        btnExportServiceDetail.addStyleName("fa fa-file-excel-o");
//        btnExportServiceDetail.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: rgb(57, 191, 98);cursor: pointer;");
//        btnExportServiceDetail.getElement().getStyle().setFontSize(12, Style.Unit.PX);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "2%", "25px", "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
//        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
//        btnExportServiceDetail.setTitle("Export to CSV");
//        flwTd.add(btnExportServiceDetail);
//        flwTr.add(flwTd);
        //

        flwTable.add(flwTr);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "270px", "left", null, DmPosition.CENTER, true);
        flwTr.add(vl.add(iniGrdLeaveDetail()));
        flwTable.add(flwTr);
        /*====================================< New Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", "28px", "left", null, DmPosition.NONE, true);
//        flwTr.getElement().getStyle().setBackgroundColor("rgb(224, 255, 255)");
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, true);
//        lblServiceAmount.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//        lblServiceAmount.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
//        lblServiceAmount.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
//        flwTd.add(lblServiceAmount);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, true);
//        lblServiceTotalAmount.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//        lblServiceTotalAmount.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
//        lblServiceTotalAmount.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
//        lblServiceTotalAmount.getElement().getStyle().setColor("rgb(7, 107, 45)");
//        flwTd.add(lblServiceTotalAmount);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, true);
//        lblReceivedAmount.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//        lblReceivedAmount.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
//        lblReceivedAmount.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
//        lblReceivedAmount.getElement().getStyle().setColor("rgb(7, 107, 45)");
//        flwTd.add(lblReceivedAmount);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, true);
//        lblBalanceAmount.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//        lblBalanceAmount.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
//        lblBalanceAmount.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
//        lblBalanceAmount.getElement().getStyle().setColor("rgb(7, 107, 45)");
//        flwTd.add(lblBalanceAmount);
//        flwTr.add(flwTd);
//
//        flwTable.add(flwTr);

        return flwTable;
    }

}
