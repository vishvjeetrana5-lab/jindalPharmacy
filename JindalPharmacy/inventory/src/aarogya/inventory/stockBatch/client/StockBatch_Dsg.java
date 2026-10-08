package aarogya.inventory.stockBatch.client;

import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.stockBatch.shared.GrdStockDetail_Mdl;
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
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.grid.HeaderGroupConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;

abstract class StockBatch_Dsg extends IsWidgetExtended {

    final String widgetName = new pmInventory().STOCK_BATCH_DISPLAY.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    final Button btnBack = new Button();
    final Button btnSetting = new Button();
    final Button btnRefresh = new Button();
    final Button btnPrint = new Button();
    final DmTextBox txtSearchValue = new DmTextBox(100, false);

    final DmLabel lblClosingValue = new DmLabel("");
    final DmLabel lblOpeningValue = new DmLabel("");

    final DmLabel lblHeading = new DmLabel("");

    public StockBatch_Dsg() {
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
        btnSetting.setTitle("Ok");
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
        label.setWidth("30px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
        aBtn.getElement().getStyle().setColor("tomato");
        btnPrint.getElement().appendChild(aBtn.getElement());
        btnPrint.getElement().appendChild(label.getElement());
        btnPrint.setWidth("55px");

        label = new InlineLabel("Back");
        label.setWidth("30px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-caret-left");
        aBtn.getElement().getStyle().setColor("tomato");
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
        DmLabel lblTemp;

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
        DmClient.setStyle(flwTd, "137px", "35px", "left", null, DmPosition.NONE, true);
        lblTemp = new DmLabel("Total :");
        lblTemp.setHeight("25px");
        lblTemp.getElement().setAttribute("style", "width: 500px !important; height: 20px; color: red; margin-top: 9%; margin-left: 1%; font-weight: bold;");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "260px", "35px", "left", null, DmPosition.NONE, true);
        lblOpeningValue.getElement().setAttribute("style", " text-align: right; width: 260px !important; height: 15px !important;color: red; margin-top: 5%; font-weight: bold;");
        flwTd.add(lblOpeningValue);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "190px", "35px", "left", null, DmPosition.NONE, true);
        lblClosingValue.getElement().setAttribute("style", " text-align: right; width: 190px !important; height: 15px !important; color: red; margin-top: 7%; margin-left: 182%;font-weight: bold;");
        flwTd.add(lblClosingValue);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdDisplay">
    interface GrdDisplay_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdStockDetail_Mdl> {

        @Editor.Path("PropertiesDisplay")
        ValueProvider<GrdStockDetail_Mdl, String> propertiesDisplay();

        @Editor.Path("DisplayOpeningQty")
        ValueProvider<GrdStockDetail_Mdl, String> displayOpeningQty();

        @Editor.Path("DisplayOpeningValue")
        ValueProvider<GrdStockDetail_Mdl, String> displayOpeningValue();

        @Editor.Path("DisplayPurchaseQty")
        ValueProvider<GrdStockDetail_Mdl, String> displayPurchaseQty();

        @Editor.Path("DisplayOtherReceiveQty")
        ValueProvider<GrdStockDetail_Mdl, String> displayOtherReceiveQty();

        @Editor.Path("DisplaySaleQty")
        ValueProvider<GrdStockDetail_Mdl, String> displaySaleQty();

        @Editor.Path("DisplayOtherIssueQty")
        ValueProvider<GrdStockDetail_Mdl, String> displayOtherIssueQty();

        @Editor.Path("DisplayClosingQty")
        ValueProvider<GrdStockDetail_Mdl, String> displayClosingQty();

        @Editor.Path("DisplayClosingValue")
        ValueProvider<GrdStockDetail_Mdl, String> displayClosingValue();

    }

    final DmGridPaging<GrdStockDetail_Mdl, GrdDisplay_PrpAcs, KeyFieldExtended> grdDisplay = new DmGridPaging(GWT.create(GrdDisplay_PrpAcs.class), 20);

    private void iniGrdDisplay() {
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().propertiesDisplay(), "Properties", 1, "ISNULL(propertyList,'')");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().displayOpeningQty(), "Quantity", 90, "isNull(openingStock,0)").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().displayOpeningValue(), "Value", 90, "isNull(openingValue,0)").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().displayPurchaseQty(), "Purchase", 90, "isNull(purchaseQty,0)").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().displayOtherReceiveQty(), "Other", 90, "isNull(otherReceiveQty,0)").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().displaySaleQty(), "Sale", 90, "isNull(saleQty,0)").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().displayOtherIssueQty(), "Other", 90, "isNull(otherIssueQty,0)").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().displayClosingQty(), "Quantity", 90, "isNull(closingStock,0)").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().displayClosingValue(), "Value", 90, "isNull(closingValue,0)").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDisplay.getDmGrid().addBeforeInitializeHandler(new DmGrid.BeforeInitialize() {
            @Override
            public void beforeInitialize(ColumnModel columnModel) {
                columnModel.addHeaderGroup(0, 1, new HeaderGroupConfig("Opening", 1, 2));
                columnModel.addHeaderGroup(0, 3, new HeaderGroupConfig("Receive Quantity", 1, 2));
                columnModel.addHeaderGroup(0, 5, new HeaderGroupConfig("Issue Quantity", 1, 2));
                columnModel.addHeaderGroup(0, 7, new HeaderGroupConfig("Closing", 1, 2));
            }

        });
        grdDisplay.getDmGrid().initializeGrid(grdDisplay.getDmGrid().getPropertyAccess().fieldHashCode(), 1280, 470, 1);

        grdDisplay.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<GrdStockDetail_Mdl>() {

            @Override
            public String getColStyle(GrdStockDetail_Mdl model, ValueProvider<? super GrdStockDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = ("dm-FontBold");
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdStockDetail_Mdl model, int rowIndex) {
                return "";
            }
        });

        grdDisplay.getHelper().addSort("orderIndex", DmSortType.ASC);
    }
    //</editor-fold>

}
