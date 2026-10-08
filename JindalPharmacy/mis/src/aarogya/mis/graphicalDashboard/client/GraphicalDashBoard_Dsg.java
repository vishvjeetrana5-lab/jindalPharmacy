package aarogya.mis.graphicalDashboard.client;

import aarogya.projectMenu.self.shared.pmMIS;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmChart.client.DmChart;
import dataman.dmToolkit.dmChart.client.DmChartMultiValue;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class GraphicalDashBoard_Dsg {

    final String widgetName = new pmMIS().GRAPHICAL_DASHBOARD.caption;
    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwSetting = new FlowPanel();
    final Button btnFilter = new Button();
    final Button btnRefresh = new Button();
    final FlowPanel flwRefresh = new FlowPanel();
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    final DmTextBox txtFromDate = new DmTextBox(12, true);
    final DmTextBox txtToDate = new DmTextBox(12, true);
    final Button btnAnalyse = new Button("Analyse");

    protected DmChart OPDchart1;
    protected DmChart OPDchart2;
    protected DmChart OPDchart3;
    protected DmChart OPDchart4;
    protected DmChart OPDchart5;
    protected DmChartMultiValue OPDchart6;

    protected DmLabel lblOPD1;
    protected DmLabel lblOPD2;
    protected DmLabel lblOPD3;

    protected DmChart IPDchart1;
    protected DmChart IPDchart2;
    protected DmChart IPDchart3;
    protected DmChart IPDchart4;
    protected DmChart IPDchart5;
    protected DmChartMultiValue IPDchart6;

    protected DmChart DIAGchart1;
    protected DmChart DIAGchart2;
    protected DmChart DIAGchart3;
    protected DmChart DIAGchart6;

    protected DmChart PHARMchart1;
    protected DmChart PHARMchart2;
    protected DmChartMultiValue PHARMchart3;
    protected DmChart PHARMchart4;
    protected DmChart PHARMchart6;
    protected DmLabel lblCompanyWiseSale;
    protected DmLabel lblItemWiseSale;

    protected DmChart NETchart1;

    FlowPanel flwTableOPD;
    final Anchor btnExpandOPDChart1 = new Anchor();
    final Anchor btnExpandOPDChart2 = new Anchor();
    final Anchor btnExpandOPDChart3 = new Anchor();
    final Anchor btnExpandOPDChart4 = new Anchor();
    final Anchor btnExpandOPDChart5 = new Anchor();
    final Anchor btnExpandOPDChart6 = new Anchor();

    FlowPanel flwTableIPD;
    final Anchor btnExpandIPDChart1 = new Anchor();
    final Anchor btnExpandIPDChart2 = new Anchor();
    final Anchor btnExpandIPDChart3 = new Anchor();
    final Anchor btnExpandIPDChart4 = new Anchor();
    final Anchor btnExpandIPDChart5 = new Anchor();
    final Anchor btnExpandIPDChart6 = new Anchor();

    FlowPanel flwTableDIAG;
    final Anchor btnExpandDIAGChart1 = new Anchor();
    final Anchor btnExpandDIAGChart2 = new Anchor();
    final Anchor btnExpandDIAGChart3 = new Anchor();
    final Anchor btnExpandDIAGChart4 = new Anchor();
    final Anchor btnExpandDIAGChart5 = new Anchor();
    final Anchor btnExpandDIAGChart6 = new Anchor();

    FlowPanel flwTablePHARM;
    final Anchor btnExpandPHARMChart1 = new Anchor();
    final Anchor btnExpandPHARMChart2 = new Anchor();
    final Anchor btnExpandPHARMChart3 = new Anchor();
    final Anchor btnExpandPHARMChart4 = new Anchor();
    final Anchor btnExpandPHARMChart5 = new Anchor();
    final Anchor btnExpandPHARMChart6 = new Anchor();
    final DmTextBoxNumeric txtTopCompany = new DmTextBoxNumeric(0, false, 2, false);
    final DmTextBoxNumeric txtTopItem = new DmTextBoxNumeric(0, false, 2, false);

    FlowPanel flwTableNET;
    final Anchor btnExpandNETChart1 = new Anchor();

    protected FlowPanel flwOPDExpand;
    final Anchor btnOPDCollapse = new Anchor();
    protected int chartLocationOPD;
    protected DmLabel lblOPDChartName = new DmLabel("-");

    protected FlowPanel flwIPDExpand;
    final Anchor btnIPDCollapse = new Anchor();
    protected int chartLocationIPD;
    protected DmLabel lblIPDChartName = new DmLabel("-");

    protected FlowPanel flwDIAGExpand;
    final Anchor btnDIAGCollapse = new Anchor();
    protected int chartLocationDIAG;
    protected DmLabel lblDIAGChartName = new DmLabel("-");

    protected FlowPanel flwPHARMExpand;
    final Anchor btnPHARMCollapse = new Anchor();
    protected int chartLocationPHARM;
    protected DmLabel lblPHARMChartName = new DmLabel("-");

    protected DmLabel lblNEThartName = new DmLabel("-");

    protected FlowPanel flwupdateChartDimensionDIAG4;
    protected FlowPanel flwupdateChartDimensionPHARM6;
    public boolean isExpand;

    //<editor-fold defaultstate="collapsed" desc="txtHInvestigation">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHInvestigation = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 9, false);

    private void iniInvestigation() {
        txtHInvestigation.getDmGrid().addTextColumn(txtHInvestigation.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHInvestigation.getDmGrid().initializeGrid(txtHInvestigation.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHInvestigation.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

//<editor-fold defaultstate="collapsed" desc="txtHPaymentMode">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPaymentMode = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniPaymentMode() {
        txtHPaymentMode.getDmGrid().addTextColumn(txtHPaymentMode.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHPaymentMode.getDmGrid().initializeGrid(txtHPaymentMode.getDmGrid().getPropertyAccess().code(), 140, 0, 1);
        txtHPaymentMode.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public GraphicalDashBoard_Dsg() {
        ini();
        iniInvestigation();
        iniPaymentMode();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("621px");
        vrtInnerForm.add(toolContainer);
        vrtInnerForm.add(iniToolContorls());

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Filter");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnFilter.addStyleName("dm-top-control-btnStyle");
        btnFilter.setTitle("Filter");
        btnFilter.getElement().appendChild(aBtn.getElement());
        btnFilter.getElement().appendChild(label.getElement());
        btnFilter.setWidth("70px");
        flwSetting.add(btnFilter);

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.setTitle("Refresh");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("75px");
        flwRefresh.add(btnRefresh);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        //==============================OPD_TAB_AREA_CONTROLS=======================================
        btnExpandOPDChart1.addStyleName("fa fa-expand");
        btnExpandOPDChart1.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandOPDChart1.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandOPDChart2.addStyleName("fa fa-expand");
        btnExpandOPDChart2.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandOPDChart2.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandOPDChart3.addStyleName("fa fa-expand");
        btnExpandOPDChart3.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandOPDChart3.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandOPDChart4.addStyleName("fa fa-expand");
        btnExpandOPDChart4.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandOPDChart4.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandOPDChart5.addStyleName("fa fa-expand");
        btnExpandOPDChart5.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandOPDChart5.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandOPDChart6.addStyleName("fa fa-expand");
        btnExpandOPDChart6.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandOPDChart6.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        //==============================IPD_TAB_AREA_CONTROLS=======================================
        btnExpandIPDChart1.addStyleName("fa fa-expand");
        btnExpandIPDChart1.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandIPDChart1.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandIPDChart2.addStyleName("fa fa-expand");
        btnExpandIPDChart2.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandIPDChart2.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandIPDChart3.addStyleName("fa fa-expand");
        btnExpandIPDChart3.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandIPDChart3.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandIPDChart4.addStyleName("fa fa-expand");
        btnExpandIPDChart4.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandIPDChart4.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandIPDChart5.addStyleName("fa fa-expand");
        btnExpandIPDChart5.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandIPDChart5.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandIPDChart6.addStyleName("fa fa-expand");
        btnExpandIPDChart6.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandIPDChart6.getElement().getStyle().setFontSize(22, Style.Unit.PX);
        //==============================DIAGNOSTICS_TAB_AREA_CONTROLS=======================================
        btnExpandDIAGChart1.addStyleName("fa fa-expand");
        btnExpandDIAGChart1.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandDIAGChart1.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandDIAGChart2.addStyleName("fa fa-expand");
        btnExpandDIAGChart2.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandDIAGChart2.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandDIAGChart3.addStyleName("fa fa-expand");
        btnExpandDIAGChart3.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandDIAGChart3.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandDIAGChart4.addStyleName("fa fa-expand");
        btnExpandDIAGChart4.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandDIAGChart4.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandDIAGChart5.addStyleName("fa fa-expand");
        btnExpandDIAGChart5.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandDIAGChart5.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandDIAGChart6.addStyleName("fa fa-expand");
        btnExpandDIAGChart6.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandDIAGChart6.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        //==============================PHARM_TAB_AREA_CONTROLS=======================================
        btnExpandPHARMChart1.addStyleName("fa fa-expand");
        btnExpandPHARMChart1.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandPHARMChart1.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandPHARMChart2.addStyleName("fa fa-expand");
        btnExpandPHARMChart2.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandPHARMChart2.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandPHARMChart3.addStyleName("fa fa-expand");
        btnExpandPHARMChart3.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandPHARMChart3.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandPHARMChart4.addStyleName("fa fa-expand");
        btnExpandPHARMChart4.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandPHARMChart4.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandPHARMChart5.addStyleName("fa fa-expand");
        btnExpandPHARMChart5.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandPHARMChart5.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnExpandPHARMChart6.addStyleName("fa fa-expand");
        btnExpandPHARMChart6.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandPHARMChart6.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        //==============================NET_TAB_AREA_CONTROLS=======================================
        btnExpandNETChart1.addStyleName("fa fa-expand");
        btnExpandNETChart1.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnExpandNETChart1.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        //==============================COLLAPS_BUTTON=======================================
        btnOPDCollapse.addStyleName("fa fa-compress");
        btnOPDCollapse.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnOPDCollapse.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnIPDCollapse.addStyleName("fa fa-compress");
        btnIPDCollapse.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnIPDCollapse.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnDIAGCollapse.addStyleName("fa fa-compress");
        btnDIAGCollapse.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnDIAGCollapse.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        btnPHARMCollapse.addStyleName("fa fa-compress");
        btnPHARMCollapse.getElement().setAttribute("style", "font-weight: bold;padding-right: 5px;color: white;cursor: pointer;");
        btnPHARMCollapse.getElement().getStyle().setFontSize(22, Style.Unit.PX);

        //==============================Add Controls=====================================
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwSetting);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        return toolContainer;
    }

    private TabLayoutPanel tabArea() {

        DmClient.setStyle(tabPanel, "100%", "568px", null, null, DmPosition.NONE, true);
        //==============================Set OPDTab Tab===================================================
//        flwTableOPD = new FlowPanel();
//        DmClient.setStyle(flwTableOPD, "100%", "592px", null, null, DmPosition.CENTER, true);
//        flwTableOPD.add(OPDTab());
//        tabPanel.add(flwTableOPD, "OPD");
        //==============================Set IPD Tab===================================================
//        flwTableIPD = new FlowPanel();
//        DmClient.setStyle(flwTableIPD, "100%", "592px", null, null, DmPosition.CENTER, true);
//        flwTableIPD.add(IPDTab());
//        tabPanel.add(flwTableIPD, "IPD");

        //==============================Set DIAG Tab===================================================
//        flwTableDIAG = new FlowPanel();
//        DmClient.setStyle(flwTableDIAG, "100%", "592px", null, null, DmPosition.CENTER, true);
//        flwTableDIAG.add(DIAGTab());
//        tabPanel.add(flwTableDIAG, "Diagnostics");

        //==============================Set PHARM Tab===================================================
        flwTablePHARM = new FlowPanel();
        DmClient.setStyle(flwTablePHARM, "100%", "592px", null, null, DmPosition.CENTER, true);
        flwTablePHARM.add(PHARMTab());
        tabPanel.add(flwTablePHARM, "Pharmacy");

        //==============================Set NET Tab===================================================
        flwTableNET = new FlowPanel();
        DmClient.setStyle(flwTableNET, "100%", "592px", null, null, DmPosition.CENTER, true);
        flwTableNET.add(NETTab());
        tabPanel.add(flwTableNET, "Net Revenue");

        tabPanel.selectTab(0);

        tabPanel.ensureDebugId("cwTabPanel");
        return tabPanel;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "596px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "95%", "30px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("From Date");
        lbl.getElement().getStyle().setFontSize(14, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        txtFromDate.getElement().getStyle().setFontSize(14, Style.Unit.PX);
        txtFromDate.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontSize(14, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTd.add(lbl);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        txtToDate.getElement().getStyle().setFontSize(14, Style.Unit.PX);
        txtToDate.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        btnAnalyse.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        btnAnalyse.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTd.add(vl.add(btnAnalyse));
        flwTr.add(flwTd);
        flwTr.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTable.add(flwTr);
        //=================================2nd Row======================================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "568px", "left", null, DmPosition.NONE, true);
        flwTr.add(tabArea());
        flwTable.add(flwTr);

        return flwTable;

    }

    public FlowPanel OPDTab() {

        FlowPanel flwTable, flwTr, flwTd;

        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "546px", null, null, DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");
        // flwTable.getElement().getStyle().setBackgroundColor("lightgray");

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98%", "23px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", "20px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consultant wise OPD");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", "25px", "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandOPDChart1);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("OPD collection");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandOPDChart2);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department wise OPD");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandOPDChart3);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "233px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        OPDchart1 = new DmChart(236, 415, DmChartType.Pie);
        flwTd.add(OPDchart1.asWidget());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        OPDchart2 = new DmChart(236, 415, DmChartType.Pie);
        flwTd.add(OPDchart2.asWidget());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        OPDchart3 = new DmChart(236, 418, DmChartType.Pie);
        flwTd.add(OPDchart3.asWidget());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98%", "23px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTr.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Area wise OPD");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandOPDChart4);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Case wise OPD");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandOPDChart5);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("OPD/Emergency Date wise");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandOPDChart6);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "233px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        OPDchart4 = new DmChart(236, 415, DmChartType.Pie);
        flwTd.add(OPDchart4.asWidget());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        OPDchart5 = new DmChart(236, 415, DmChartType.Pie);
        flwTd.add(OPDchart5.asWidget());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        OPDchart6 = new DmChartMultiValue(235, 423, DmChartType.Line);
        flwTd.add(OPDchart6.asWidget());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "32px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lblOPD1 = new DmLabel("-");
        lblOPD1.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lblOPD1.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lblOPD1);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        // flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lblOPD2 = new DmLabel("-");
        lblOPD2.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lblOPD2.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lblOPD2);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        //  flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lblOPD3 = new DmLabel("-");
        lblOPD3.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lblOPD3.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lblOPD3);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        //flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    public FlowPanel IPDTab() {

        FlowPanel flwTable, flwTr, flwTd;

        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "546px", null, null, DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");
        // flwTable.getElement().getStyle().setBackgroundColor("lightgray");

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98%", "26px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", "20px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consultant wise IPD");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", "25px", "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandIPDChart1);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("IPD collection");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandIPDChart2);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department wise IPD");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandIPDChart3);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "233px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        IPDchart1 = new DmChart(236, 415, DmChartType.Pie);
        flwTd.add(IPDchart1.asWidget());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        IPDchart2 = new DmChart(236, 415, DmChartType.Pie);
        flwTd.add(IPDchart2.asWidget());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        IPDchart3 = new DmChart(236, 418, DmChartType.Pie);
        flwTd.add(IPDchart3.asWidget());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98%", "26px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTr.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Room Category wise IPD");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandIPDChart4);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Case wise IPD");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandIPDChart5);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Day wise Bed Occupancy");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandIPDChart6);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "233px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        IPDchart4 = new DmChart(236, 415, DmChartType.Pie);
        flwTd.add(IPDchart4.asWidget());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        IPDchart5 = new DmChart(236, 415, DmChartType.Pie);
        flwTd.add(IPDchart5.asWidget());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        IPDchart6 = new DmChartMultiValue(235, 423, DmChartType.Line);
        flwTd.add(IPDchart6.asWidget());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    public FlowPanel DIAGTab() {

        FlowPanel flwTable, flwTr, flwTd;

        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "546px", null, null, DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");
        // flwTable.getElement().getStyle().setBackgroundColor("lightgray");

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98%", "26px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", "20px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Category wise LAB");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", "25px", "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandDIAGChart1);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("LAB collection");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandDIAGChart2);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Area wise LAB");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandDIAGChart3);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "233px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        DIAGchart1 = new DmChart(236, 415, DmChartType.Pie);
        flwTd.add(DIAGchart1.asWidget());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        DIAGchart2 = new DmChart(236, 415, DmChartType.Pie);
        flwTd.add(DIAGchart2.asWidget());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        DIAGchart3 = new DmChart(236, 418, DmChartType.Pie);
        flwTd.add(DIAGchart3.asWidget());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98%", "26px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTr.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date wise summary for Investigation");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        // flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        flwTd.add(vl.add(txtHInvestigation));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(43, Style.Unit.PCT);
        flwTd.add(btnExpandDIAGChart6);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "233px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwupdateChartDimensionDIAG4 = new FlowPanel();
        DmClient.setStyle(flwupdateChartDimensionDIAG4, "99%", null, "left", null, DmPosition.NONE, false);
//        DIAGchart6 = new DmChart(236, 1250, DmChartType.ColumnRenderer);
//        flwupdateChartDimensionDIAG4.add(DIAGchart6.asWidget());
        flwTr.add(flwupdateChartDimensionDIAG4);

        flwTable.add(flwTr);

        return flwTable;
    }

    public FlowPanel PHARMTab() {

        FlowPanel flwTable, flwTr, flwTd;

        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "546px", null, null, DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");
        // flwTable.getElement().getStyle().setBackgroundColor("lightgray");

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98%", "26px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", "20px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Top");
        lbl.getElement().getStyle().setFontSize(100, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", "20px", "left", null, DmPosition.NONE, false);
        flwTd.add(txtTopCompany);
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        lblCompanyWiseSale = new DmLabel("Company wise % Sale");
        lblCompanyWiseSale.getElement().getStyle().setFontSize(100, Style.Unit.PCT);
        lblCompanyWiseSale.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lblCompanyWiseSale);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", "25px", "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandPHARMChart1);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Revenue");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandPHARMChart2);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase/Sale Date wise");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandPHARMChart3);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "233px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        PHARMchart1 = new DmChart(236, 415, DmChartType.Pie);
        flwTd.add(PHARMchart1.asWidget());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        PHARMchart2 = new DmChart(236, 415, DmChartType.Pie);
        flwTd.add(PHARMchart2.asWidget());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        PHARMchart3 = new DmChartMultiValue(236, 418, DmChartType.Line);
        flwTd.add(PHARMchart3.asWidget());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98%", "26px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTr.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", "20px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Top");
        lbl.getElement().getStyle().setFontSize(100, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", "20px", "left", null, DmPosition.NONE, false);
        flwTd.add(txtTopItem);
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblItemWiseSale = new DmLabel("Item wise % Sale");
        lblItemWiseSale.getElement().getStyle().setFontSize(100, Style.Unit.PCT);
        lblItemWiseSale.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lblItemWiseSale);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandPHARMChart4);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Day wise sale for Payment Mode");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        // flwTd.getElement().getStyle().setMarginLeft(0, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPaymentMode));
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnExpandPHARMChart6);
        flwTd.getElement().getStyle().setMarginLeft(29, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

//        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "233px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        PHARMchart4 = new DmChart(236, 415, DmChartType.Pie);
        flwTd.add(PHARMchart4.asWidget());
        flwTr.add(flwTd);

        flwupdateChartDimensionPHARM6 = new FlowPanel();
        DmClient.setStyle(flwupdateChartDimensionPHARM6, "66%", null, "left", null, DmPosition.NONE, false);
//        PHARMchart6 = new DmChart(235, 800, DmChartType.ColumnRenderer);
//        flwupdateChartDimensionPHARM6.add(PHARMchart6.asWidget());
        flwTr.add(flwupdateChartDimensionPHARM6);

        flwTable.add(flwTr);

        return flwTable;
    }

    public FlowPanel NETTab() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "546px", null, null, DmPosition.CENTER, true);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "26px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        lbl = new DmLabel("Net Revenue");
        lbl.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lbl.getElement().getStyle().setMarginLeft(42, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "80%", "485px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        NETchart1 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
        flwTr.add(NETchart1);

        flwTable.add(flwTr);

        return flwTable;
    }

    public FlowPanel ExpandOPDChart() {

        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "546px", null, null, DmPosition.CENTER, true);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "26px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmPosition.NONE, false);
        lblOPDChartName.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lblOPDChartName.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblOPDChartName.getElement().getStyle().setMarginLeft(42, Style.Unit.PCT);
        flwTd.add(lblOPDChartName);

        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnOPDCollapse);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwOPDExpand = new FlowPanel();
        DmClient.setStyle(flwOPDExpand, "80%", "485px", "left", null, DmPosition.NONE, true);

        switch (chartLocationOPD) {
            case 1:
                OPDchart1 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwOPDExpand.add(OPDchart1);
                break;
            case 2:
                OPDchart2 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwOPDExpand.add(OPDchart2);
                break;
            case 3:
                OPDchart3 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwOPDExpand.add(OPDchart3);
                break;
            case 4:
                OPDchart4 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwOPDExpand.add(OPDchart4);
                break;
            case 5:
                OPDchart5 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwOPDExpand.add(OPDchart5);
                break;
            case 6:
                OPDchart6 = new DmChartMultiValue(470, 850, DmEnum.DmChartType.Line);
                flwOPDExpand.add(OPDchart6);
                break;
            default:
                break;
        }
        flwOPDExpand.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTable.add(flwOPDExpand);

        return flwTable;
    }

    public FlowPanel ExpandIPDChart() {

        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "546px", null, null, DmPosition.CENTER, true);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "26px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmPosition.NONE, false);
        lblIPDChartName.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lblIPDChartName.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblIPDChartName.getElement().getStyle().setMarginLeft(42, Style.Unit.PCT);
        flwTd.add(lblIPDChartName);

        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnIPDCollapse);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwIPDExpand = new FlowPanel();
        DmClient.setStyle(flwIPDExpand, "80%", "485px", "left", null, DmPosition.NONE, true);

        switch (chartLocationIPD) {
            case 1:
                IPDchart1 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwIPDExpand.add(IPDchart1);
                break;
            case 2:
                IPDchart2 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwIPDExpand.add(IPDchart2);
                break;
            case 3:
                IPDchart3 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwIPDExpand.add(IPDchart3);
                break;
            case 4:
                IPDchart4 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwIPDExpand.add(IPDchart4);
                break;
            case 5:
                IPDchart5 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwIPDExpand.add(IPDchart5);
                break;
            case 6:
                IPDchart6 = new DmChartMultiValue(470, 850, DmEnum.DmChartType.Line);
                flwIPDExpand.add(IPDchart6);
                break;
            default:
                break;
        }
        flwIPDExpand.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTable.add(flwIPDExpand);

        return flwTable;
    }

    public FlowPanel ExpandDIAGChart() {

        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "546px", null, null, DmPosition.CENTER, true);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "26px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmPosition.NONE, false);
        lblDIAGChartName.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lblDIAGChartName.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblDIAGChartName.getElement().getStyle().setMarginLeft(42, Style.Unit.PCT);
        flwTd.add(lblDIAGChartName);

        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnDIAGCollapse);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwDIAGExpand = new FlowPanel();
        DmClient.setStyle(flwDIAGExpand, "80%", "485px", "left", null, DmPosition.NONE, true);

        switch (chartLocationDIAG) {
            case 1:
                DIAGchart1 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwDIAGExpand.add(DIAGchart1);
                break;
            case 2:
                DIAGchart2 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwDIAGExpand.add(DIAGchart2);
                break;
            case 3:
                DIAGchart3 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwDIAGExpand.add(DIAGchart3);
                break;
//            case 4:
//                DIAGchart4 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
//                flwDIAGExpand.add(DIAGchart4);
//                break;
//            case 5:
//                DIAGchart5 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
//                flwDIAGExpand.add(DIAGchart5);
//                break;
            case 6:
                DIAGchart6 = new DmChart(470, 850, DmEnum.DmChartType.ColumnRenderer);
                flwDIAGExpand.add(DIAGchart6);
                break;
            default:
                break;
        }
        flwDIAGExpand.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTable.add(flwDIAGExpand);

        return flwTable;
    }

    public FlowPanel ExpandPHARMChart() {

        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "546px", null, null, DmPosition.CENTER, true);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "26px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setBackgroundColor("rgb(0, 172, 193)");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmPosition.NONE, false);
        lblPHARMChartName.getElement().getStyle().setFontSize(111, Style.Unit.PCT);
        lblPHARMChartName.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblPHARMChartName.getElement().getStyle().setMarginLeft(42, Style.Unit.PCT);
        flwTd.add(lblPHARMChartName);

        flwTd.getElement().getStyle().setColor("white");
        flwTd.getElement().getStyle().setMarginTop(4, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnPHARMCollapse);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwPHARMExpand = new FlowPanel();
        DmClient.setStyle(flwPHARMExpand, "80%", "485px", "left", null, DmPosition.NONE, true);

        switch (chartLocationPHARM) {
            case 1:
                PHARMchart1 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwPHARMExpand.add(PHARMchart1);
                break;
            case 2:
                PHARMchart2 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwPHARMExpand.add(PHARMchart2);
                break;
            case 3:
                PHARMchart3 = new DmChartMultiValue(470, 850, DmEnum.DmChartType.Line);
                flwPHARMExpand.add(PHARMchart3);
                break;
            case 4:
                PHARMchart4 = new DmChart(470, 850, DmEnum.DmChartType.Pie);
                flwPHARMExpand.add(PHARMchart4);
                break;

            case 6:
                PHARMchart6 = new DmChart(470, 900, DmEnum.DmChartType.ColumnRenderer);
                flwPHARMExpand.add(PHARMchart6);
                break;
            default:
                break;
        }
        flwPHARMExpand.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTable.add(flwPHARMExpand);

        return flwTable;
    }

}
