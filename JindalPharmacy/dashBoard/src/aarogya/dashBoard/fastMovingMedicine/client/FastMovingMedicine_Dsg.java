package aarogya.dashBoard.fastMovingMedicine.client;

import aarogya.dashBoard.fastMovingMedicine.shared.*;
import aarogya.projectMenu.self.shared.pmDashBoard;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.MenuLink;
import java.util.ArrayList;
import java.util.List;

abstract class FastMovingMedicine_Dsg extends IsWidgetExtended implements ClickHandler {
//

    final String widgetName = new pmDashBoard().FAST_MOVING_MEDICINE.caption;
    final FlowPanel flwForm = new FlowPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmClient.ViaList vl = new DmClient.ViaList();
    private final List<MenuLink> menuLinkList = new ArrayList<>();

    private int width = Window.getClientWidth() < 1000 ? 1000 : Window.getClientWidth();
    private int height = ((Window.getClientHeight() - 35 - 27)) < 500 ? 500 : ((Window.getClientHeight() - 35 - 27));
//    private int gridWidth = (width - 250) - 30;
    private int gridWidth = width - 30;

    final Button btnRefresh = new Button();

    final Button btnNextThrityDays = new Button();
    final Button btnNext = new Button();
    final Button btnCancel = new Button();
    final Button btnExportData = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    final DmTextBox txtDaysUpto = new DmTextBox(11, true);
    final DmTextBox txtLastThrityDays = new DmTextBox(11, true);

    private int getGridRows() {
        return (int) (Math.floor(height / 23.6));
    }

    public FastMovingMedicine_Dsg() {
        iniMenuList();
        ini();
        iniGrdConsultant();
    }

    private void iniMenuList() {
        menuLinkList.clear();
    }
    DmLabel lbl2 = new DmLabel("");

    private DmToolContainer iniToolControls() {
        Anchor aBtn;
        InlineLabel label;

        txtDaysUpto.getElement().getStyle().setFontSize(15, Style.Unit.PX);
        txtDaysUpto.setAlignment(ValueBoxBase.TextAlignment.CENTER);

        txtLastThrityDays.getElement().getStyle().setFontSize(15, Style.Unit.PX);
        txtLastThrityDays.setAlignment(ValueBoxBase.TextAlignment.CENTER);

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-right ");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnNextThrityDays.addStyleName("dm-top-control-btnStyle");
        btnNextThrityDays.setTitle("Next");
        btnNextThrityDays.getElement().appendChild(aBtn.getElement());
        btnNextThrityDays.setWidth("20px");
        btnNextThrityDays.setWidth("100%");
        
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-angle-right ");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnNext.addStyleName("dm-top-control-btnStyle");
        btnNext.setTitle("Next");
        btnNext.getElement().appendChild(aBtn.getElement());
        btnNext.setWidth("20px");
        btnNext.setWidth("100%");

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.setStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.setTitle("Refresh");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("80px");

        label = new InlineLabel("Export");
        aBtn = new Anchor();
        aBtn.setStyleName("fa fa-file-excel-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnExportData.addStyleName("dm-top-control-btnStyle");
        btnExportData.setTitle("Refresh");
        btnExportData.getElement().appendChild(aBtn.getElement());
        btnExportData.getElement().appendChild(label.getElement());
        btnExportData.setWidth("50px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("50px");
        btnCancel.setTitle("Close");
        flwCancel.add(btnCancel);

        DmLabel lbl = new DmLabel("Fast Moving medicine in");
        lbl.setHeight("15px");
        lbl.getElement().getStyle().setMarginTop(4, Style.Unit.PCT);
       
        DmLabel lbl1 = new DmLabel("days Over Rank ");
        lbl1.setHeight("15px");
        lbl1.getElement().getStyle().setMarginTop(4, Style.Unit.PCT);

        toolContainer.addWidget(txtSearchValue);
        txtSearchValue.setWidth("350px");
        toolContainer.addWidget(lbl);

        toolContainer.addWidget(vl.add(txtLastThrityDays));
        txtLastThrityDays.setWidth("32px");
        toolContainer.addWidget(btnNextThrityDays);

        toolContainer.addWidget(lbl1);
        toolContainer.addWidget(vl.add(txtDaysUpto));
        txtDaysUpto.setWidth("32px");
        toolContainer.addWidget(btnNext);

        toolContainer.addWidget(vl.add(btnRefresh));
        toolContainer.addWidget(btnExportData);
        btnNextThrityDays.setWidth("20px");
        btnNext.setWidth("20px");
        toolContainer.addWidget(flwCancel);
        btnCancel.setWidth("50px");

        toolContainer.addGap();
        toolContainer.setTitle(widgetName);

        return toolContainer;
    }

    private void ini() {
        FlowPanel flw;
        VerticalPanel vrtInnerForm = new VerticalPanel();

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        vrtInnerForm.add(toolContainer);
        vrtInnerForm.add(iniToolControls());

        flw = new FlowPanel();
        flw.setWidth("100%");
        flw.getElement().getStyle().setBackgroundColor("#eff");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private HorizontalPanel workingArea() {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        FlowPanel flowPanel;

        DmClient.setStyle(horizontalPanel, "100%", "100%", null, null, DmPosition.NONE, true);

        /*====================================< New Colunm >====================================*/
        flowPanel = new FlowPanel();
        flowPanel.add(leftArea());
        horizontalPanel.add(flowPanel);
        horizontalPanel.setCellWidth(flowPanel, "100%");
        horizontalPanel.setCellHeight(flowPanel, "100%");

//        /*====================================< New Colunm >====================================*/
//        flowPanel = new FlowPanel();
//        flowPanel.add(rightArea());
//        horizontalPanel.add(flowPanel);
//        horizontalPanel.setCellHeight(flowPanel, "100%");
        return horizontalPanel;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", height + "px", "left", null, DmPosition.NONE, true);
        flwTable.getElement().setAttribute("style", flwTable.getElement().getAttribute("style")
                + ";overflow-x:auto;background-color:white;");
        flwTable.add(grdDataDisplay);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd grdDataDisplay">
    interface GrdNearExpiryMedicine_PrpAcs extends KeyFieldHashCode_T_PrpAcs<FastMovingMedicine_Mdl> {

        @Editor.Path("MedicineName")
        ValueProvider<FastMovingMedicine_Mdl, String> medicineName();

        @Editor.Path("MedicineCategory")
        ValueProvider<FastMovingMedicine_Mdl, String> medicineCategory();

        @Editor.Path("TotalSoldQty")
        ValueProvider<FastMovingMedicine_Mdl, String> totalSoldQty();

        @Editor.Path("SalesRank")
        ValueProvider<FastMovingMedicine_Mdl, String> salesRank();

    }
    final DmGridPaging<FastMovingMedicine_Mdl, GrdNearExpiryMedicine_PrpAcs, SearchValue_Mdl> grdDataDisplay = new DmGridPaging(GWT.create(GrdNearExpiryMedicine_PrpAcs.class), getGridRows(), true);

    private void iniGrdConsultant() {

        grdDataDisplay.getDmGrid().addTextColumn(grdDataDisplay.getDmGrid().getPropertyAccess().medicineName(), "Medicine Name", 300, "medicineName");
        grdDataDisplay.getDmGrid().addTextColumn(grdDataDisplay.getDmGrid().getPropertyAccess().medicineCategory(), "Medicine Category", 150, "MedicineCategory");
        grdDataDisplay.getDmGrid().addTextColumn(grdDataDisplay.getDmGrid().getPropertyAccess().totalSoldQty(), "Total Sold Quantity", 200, "totalSoldQty");
        grdDataDisplay.getDmGrid().addTextColumn(grdDataDisplay.getDmGrid().getPropertyAccess().salesRank(), "Sale Rank", 120, "salesRank");

        grdDataDisplay.getDmGrid().initializeGrid(grdDataDisplay.getDmGrid().getPropertyAccess().fieldHashCode(), gridWidth, 0, 1);

        grdDataDisplay.getHelper().addSort("salesRank", DmSortType.ASC);

    }
    //</editor-fold>
}
