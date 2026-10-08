package aarogya.inventory.stockLedger.client;

import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.stockLedger.shared.GrdStockDetail_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;

abstract class StockLedger_Dsg extends IsWidgetExtended {

    final String widgetName = new pmInventory().STOCK_LEDGER_DISPLAY.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    final Button btnSetting = new Button();
    final Button btnBack = new Button();
    final Button btnRefresh = new Button();
    final Button btnPrint = new Button();
    final DmTextBox txtSearchValue = new DmTextBox(100, false);

    final DmLabel lblIssueQty = new DmLabel("");
    final DmLabel lblReceiveQty = new DmLabel("");
    final DmLabel lblBalance = new DmLabel("");
    final DmLabel lblBalanceValue = new DmLabel("");
    final DmLabel lblOpening = new DmLabel("");

    final DmLabel lblHeading = new DmLabel("");

    public StockLedger_Dsg() {
        super();
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("550px");

        vrtInnerForm.add(vl.add(iniToolContainer()));

        iniGrdDisplay();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);

    }

    private DmToolContainer iniToolContainer() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Setting");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSetting.setTitle("Setting");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.getElement().appendChild(aBtn.getElement());
        btnSetting.getElement().appendChild(label.getElement());
        btnSetting.setWidth("70px");

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.setTitle("Refresh");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("70px");

        label = new InlineLabel("Print");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnPrint.setTitle("Print");
        btnPrint.addStyleName("dm-top-control-btnStyle");
        btnPrint.getElement().appendChild(aBtn.getElement());
        btnPrint.getElement().appendChild(label.getElement());
        btnPrint.setWidth("55px");

        label = new InlineLabel("Back");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-caret-left");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnBack.setTitle("Back");
        btnBack.addStyleName("dm-top-control-btnStyle");
        btnBack.getElement().appendChild(aBtn.getElement());
        btnBack.getElement().appendChild(label.getElement());
        btnBack.setWidth("55px");

        FlowPanel flwHeading = new FlowPanel();
        DmClient.setStyle(flwHeading, "100%", null, "left", null, DmPosition.NONE, true);
        flwHeading.setStyleName("dm-StripLabel");
        lblHeading.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblHeading.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        lblHeading.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblHeading.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblHeading.setHeight("20px");
        flwHeading.add(lblHeading);

        toolContainer.addWidget(btnBack);
        toolContainer.addWidget(btnSetting);
        toolContainer.addWidget(btnRefresh);
        toolContainer.addWidget(btnPrint);

        toolContainer.addWidget(flwHeading);
        toolContainer.addGap();
        toolContainer.setTitle(widgetName);
        toolContainer.setModeVisible(false);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "520px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.7%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtSearchValue));
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "465px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(grdDisplay));
        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.setStyleName("dm-StripLabel");
        flwTr.getElement().setAttribute("style", "border-top: 1px solid lightgray; height:35px !important;");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "190px", "35px", "left", null, DmPosition.NONE, true);
        lblOpening.getElement().setAttribute("style", "width: 100% !important; float:left; height: 20px; color: red; margin-top: 3%; margin-left: 1%; font-weight: bold;");
        flwTd.add(lblOpening);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "190px", "35px", "left", null, DmPosition.NONE, true);
        lblReceiveQty.getElement().setAttribute("style", "width: 100% !important; float:left; height: 20px; margin-top: 4%; font-weight: bold;");
        flwTd.add(lblReceiveQty);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "190px", "35px", "left", null, DmPosition.NONE, true);
        lblIssueQty.getElement().setAttribute("style", "width: 100% !important; float:left; height: 20px; margin-top: 4%; font-weight: bold;");
        flwTd.add(lblIssueQty);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "190px", "35px", "left", null, DmPosition.NONE, true);
        lblBalance.getElement().setAttribute("style", "width: 100% !important; float:left; height: 20px; margin-top: 4%; font-weight: bold;");
        flwTd.add(lblBalance);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "198px", "35px", "left", null, DmPosition.NONE, true);
        lblBalanceValue.getElement().setAttribute("style", "width: 100% !important; float:left; height: 20px; margin-top: 4%; font-weight: bold;");
        flwTd.add(lblBalanceValue);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdDisplay">
    interface GrdDisplay_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdStockDetail_Mdl> {

        @Editor.Path("VoucherDate")
        ValueProvider<GrdStockDetail_Mdl, String> voucherDate();

        @Editor.Path("VoucherType")
        ValueProvider<GrdStockDetail_Mdl, String> voucherType();

        @Editor.Path("Particulars")
        ValueProvider<GrdStockDetail_Mdl, String> particulars();

        @Editor.Path("BatchNo")
        ValueProvider<GrdStockDetail_Mdl, String> batchNo();

        @Editor.Path("RecQtyDisplay")
        ValueProvider<GrdStockDetail_Mdl, String> recQtyDisplay();

        @Editor.Path("IsuQtyDisplay")
        ValueProvider<GrdStockDetail_Mdl, String> isuQtyDisplay();

        @Editor.Path("BalanceDisplay")
        ValueProvider<GrdStockDetail_Mdl, String> balanceDisplay();

        @Editor.Path("PropertiesDisplay")
        ValueProvider<GrdStockDetail_Mdl, String> propertiesDisplay();

        @Editor.Path("ValueDisplay")
        ValueProvider<GrdStockDetail_Mdl, String> valueDisplay();

        @Editor.Path("BalanceValueDisplay")
        ValueProvider<GrdStockDetail_Mdl, String> balanceValueDisplay();

    }

    final DmGridPaging<GrdStockDetail_Mdl, GrdDisplay_PrpAcs, KeyFieldExtended> grdDisplay = new DmGridPaging(GWT.create(GrdDisplay_PrpAcs.class), 20);

    private void iniGrdDisplay() {
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().voucherDate(), "Date", 75, "isNull(v_date,'')", DmDataType.DATE);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().name(), "Voucher No.", 110, "isNull(recId,'')");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().voucherType(), "Type", 45, "isNull(v_Desc,'')");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().particulars(), "Particulars", 1, "isNull(particulars,'')");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().propertiesDisplay(), "Properties", 100, "isNull(propertyList,'')");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().recQtyDisplay(), "Receive", 80, "isNull(receiveQty,'')", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().isuQtyDisplay(), "Issue", 80, "isNull(issueQty,'')", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().valueDisplay(), "Value", 80, "isNull(issueReceiveValue,'')").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().balanceDisplay(), "Balance Qty.", 100, "isNull(balanceQty,'')", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().balanceValueDisplay(), "Balance Value", 100, "isNull(balanceValue,'')", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().initializeGrid(grdDisplay.getDmGrid().getPropertyAccess().fieldHashCode(), 1280, 470, 4);

        grdDisplay.getHelper().addSort("orderIndex", DmSortType.ASC);
    }
    //</editor-fold>

}
