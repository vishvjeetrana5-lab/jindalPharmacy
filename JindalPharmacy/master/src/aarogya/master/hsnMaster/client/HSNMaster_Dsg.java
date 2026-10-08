package aarogya.master.hsnMaster.client;

import dataman.dmBase.self.shared.*;
import aarogya.master.hsnMaster.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.HeaderGroupConfig;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class HSNMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().HSN_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwMain = new FlowPanel();
    final FlowPanel flwRateGrid = new FlowPanel();
    final FlowPanel flwITS = new FlowPanel();

    final DmSuggestBox txtHSN = new DmSuggestBox(8, 10, true, "hsn", DmDataType.STRING);
    final DmSuggestBox txtName = new DmSuggestBox(50, 10, true, "name", DmDataType.STRING);
    final MenuItem mnuExport = new MenuItem("Export HSN");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="txtHITSOnPurchase">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHITSOnPurchase = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHITSOnPurchase() {
        txtHITSOnPurchase.getDmGrid().addTextColumn(txtHITSOnPurchase.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHITSOnPurchase.getDmGrid().initializeGrid(txtHITSOnPurchase.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHITSOnPurchase.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHITSOnPurchaseReturn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHITSOnPurchaseReturn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHITSOnPurchaseReturn() {
        txtHITSOnPurchaseReturn.getDmGrid().addTextColumn(txtHITSOnPurchaseReturn.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHITSOnPurchaseReturn.getDmGrid().initializeGrid(txtHITSOnPurchaseReturn.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHITSOnPurchaseReturn.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHITSOnSale">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHITSOnSale = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHITSOnSale() {
        txtHITSOnSale.getDmGrid().addTextColumn(txtHITSOnSale.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHITSOnSale.getDmGrid().initializeGrid(txtHITSOnSale.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHITSOnSale.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHITSOnSaleReturn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHITSOnSaleReturn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHITSOnSaleReturn() {
        txtHITSOnSaleReturn.getDmGrid().addTextColumn(txtHITSOnSaleReturn.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHITSOnSaleReturn.getDmGrid().initializeGrid(txtHITSOnSaleReturn.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHITSOnSaleReturn.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSaleAC">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSaleAC = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHSaleAC() {
        txtHSaleAC.getDmGrid().addTextColumn(txtHSaleAC.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHSaleAC.getDmGrid().initializeGrid(txtHSaleAC.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHSaleAC.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSaleReturnAC">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSaleReturnAC = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHSaleReturnAC() {
        txtHSaleReturnAC.getDmGrid().addTextColumn(txtHSaleReturnAC.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHSaleReturnAC.getDmGrid().initializeGrid(txtHSaleReturnAC.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHSaleReturnAC.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPurchaseAC">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPurchaseAC = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHPurchaseAC() {
        txtHPurchaseAC.getDmGrid().addTextColumn(txtHPurchaseAC.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHPurchaseAC.getDmGrid().initializeGrid(txtHPurchaseAC.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHPurchaseAC.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPurchaseReturnAC">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPurchaseReturnAC = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHPurchaseReturnAC() {
        txtHPurchaseReturnAC.getDmGrid().addTextColumn(txtHPurchaseReturnAC.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHPurchaseReturnAC.getDmGrid().initializeGrid(txtHPurchaseReturnAC.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHPurchaseReturnAC.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHIsHSN() {
        txtHNature.getDmGrid().addTextColumn(txtHNature.getDmGrid().getPropertyAccess().name(), "Nature", 0, "name");
        txtHNature.getDmGrid().initializeGrid(txtHNature.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTaxBasedOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTaxBasedOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHTaxBasedOn() {
        txtHTaxBasedOn.getDmGrid().addTextColumn(txtHTaxBasedOn.getDmGrid().getPropertyAccess().name(), "Nature", 0, "name");
        txtHTaxBasedOn.getDmGrid().initializeGrid(txtHTaxBasedOn.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHItemGSTNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHItemGSTNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHItemGSTNature() {
        txtHItemGSTNature.getDmGrid().addTextColumn(txtHItemGSTNature.getDmGrid().getPropertyAccess().name(), "Nature", 0, "");
        txtHItemGSTNature.getDmGrid().initializeGrid(txtHItemGSTNature.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("HSNCode")
        ValueProvider<Find_Mdl, String> hsnCode();

        @Editor.Path("Nature")
        ValueProvider<Find_Mdl, String> nature();

        @Editor.Path("TaxBasedOn")
        ValueProvider<Find_Mdl, String> taxBasedOn();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().hsnCode(), "HSN / SAC", 250, "hsn");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().nature(), "Goods / Service Nature", 250, "nature");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().taxBasedOn(), "Tax Based On", 250, "taxBasedOn");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1280, 183, 1);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public HSNMaster_Dsg() {
        ini();
        iniTxtHSaleAC();
        iniTxtHSaleReturnAC();
        iniTxtHPurchaseAC();
        iniTxtHPurchaseReturnAC();
        iniTxtHItemGSTNature();
        iniTxtHITSOnSale();
        iniTxtHITSOnSaleReturn();
        iniTxtHITSOnPurchase();
        iniTxtHITSOnPurchaseReturn();
        iniTxtHIsHSN();
        iniTxtHTaxBasedOn();
        iniMenu();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(createBottom());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmUserControl createBottom() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "476px", null, null, DmPosition.CENTER, true);
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "60%", "88px", "left", null, DmPosition.NONE, false);
        flwTR.add(topPart());
        flwTR.getElement().getStyle().setMarginLeft(21, Style.Unit.PCT);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "96%", "273px", "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwMain, "100%", "273px", null, null, DmPosition.NONE, true);
        DmClient.setStyle(flwRateGrid, "100%", "273px", null, null, DmPosition.NONE, true);
        DmClient.setStyle(flwITS, "100%", "273px", null, null, DmPosition.NONE, true);
        flwRateGrid.add(rateGrid());
        flwITS.add(ITSPart());
        flwTR.add(flwMain);
        flwTR.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        flwTable.add(flwTR);
        //============================== END =======================================

        return flwTable;
    }

    private FlowPanel rateGrid() {
        FlowPanel flwTable, flwTR;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "273px", null, null, DmPosition.CENTER, true);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTR.add((iniGrdRate()));
        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel topPart() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "86px", null, null, DmPosition.CENTER, true);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //============================================== 2nd Row ===========================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("HSN / SAC");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSN));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Goods / Service Nature");

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHNature));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GST Nature");
        lbl.getElement().getStyle().setMarginLeft(13, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHItemGSTNature));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ITS Based On");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHTaxBasedOn));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel ITSPart() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "62.4%", "273px", null, null, DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setMarginLeft(19.9, Style.Unit.PCT);

        //============================================ 3rd Row ===========================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ITS On Purchase");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add((txtHITSOnPurchase));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        //============================== END ================================================

        //======================================== 4th Row ==================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ITS On Sale");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add((txtHITSOnSale));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ITS On Purchase Return");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add((txtHITSOnPurchaseReturn));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        //============================== END ================================================

        //======================================== 4th Row ==================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ITS On Sale Return");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add((txtHITSOnSaleReturn));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        //============================== END =======================================

        //======================= 5th Row (ITS ON SALE) ===========================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase A/c");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add((txtHPurchaseAC));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sales A/c");

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add((txtHSaleAC));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        //======================= 5th Row (ITS ON SALE) ===========================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase Return A/c");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add((txtHPurchaseReturnAC));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sales Return A/c");

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add((txtHSaleReturnAC));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuExport);
        return menu;
    }

    //<editor-fold defaultstate="collapsed" desc="grdRate">
    interface GridRate_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdRateITS_Mdl> {

        @Editor.Path("RateFrom")
        ValueProvider<GrdRateITS_Mdl, String> rateFrom();

        @Editor.Path("RateUpto")
        ValueProvider<GrdRateITS_Mdl, String> rateUpto();

        @Editor.Path("ItsOnPurchaseText")
        ValueProvider<GrdRateITS_Mdl, String> itsOnPurchaseText();

        @Editor.Path("ItsOnSaleText")
        ValueProvider<GrdRateITS_Mdl, String> itsOnSaleText();

        @Editor.Path("ItsOnPurchaseReturnText")
        ValueProvider<GrdRateITS_Mdl, String> itsOnPurchaseReturnText();

        @Editor.Path("ItsOnSaleReturnText")
        ValueProvider<GrdRateITS_Mdl, String> itsOnSaleReturnText();

        @Editor.Path("AcPurchaseText")
        ValueProvider<GrdRateITS_Mdl, String> acPurchaseText();

        @Editor.Path("AcSaleText")
        ValueProvider<GrdRateITS_Mdl, String> acSaleText();

        @Editor.Path("AcPurchaseReturnText")
        ValueProvider<GrdRateITS_Mdl, String> acPurchaseReturnText();

        @Editor.Path("AcSaleReturnText")
        ValueProvider<GrdRateITS_Mdl, String> acSaleReturnText();

    }
    final DmGrid<GrdRateITS_Mdl, GridRate_PrpAcs> grdRate = new DmGrid(GWT.create(GridRate_PrpAcs.class));

    private Grid iniGrdRate() {
        grdRate.addRowNumberColumn();
        grdRate.addTextColumn(grdRate.getPropertyAccess().rateFrom(), "From", 80, "");
        grdRate.addTextColumn(grdRate.getPropertyAccess().rateUpto(), "Upto", 80, "");
        grdRate.addTextColumn(grdRate.getPropertyAccess().itsOnPurchaseText(), "On Purchase", 90, "");
        grdRate.addTextColumn(grdRate.getPropertyAccess().itsOnSaleText(), "On Sale", 90, "");
        grdRate.addTextColumn(grdRate.getPropertyAccess().itsOnPurchaseReturnText(), "On Purch. Rtrn.", 100, "");
        grdRate.addTextColumn(grdRate.getPropertyAccess().itsOnSaleReturnText(), "On Sale Rtrn.", 100, "");
        grdRate.addTextColumn(grdRate.getPropertyAccess().acPurchaseText(), "Purchase A/c.", 90, "");
        grdRate.addTextColumn(grdRate.getPropertyAccess().acSaleText(), "Sale A/c.", 90, "");
        grdRate.addTextColumn(grdRate.getPropertyAccess().acPurchaseReturnText(), "Purch. Rtrn. A/c.", 100, "");
        grdRate.addTextColumn(grdRate.getPropertyAccess().acSaleReturnText(), "Sales Rtrn. A/c.", 90, "");

        grdRate.addBeforeInitializeHandler(new DmGrid.BeforeInitialize() {
            @Override
            public void beforeInitialize(ColumnModel columnModel) {
                columnModel.addHeaderGroup(0, 1, new HeaderGroupConfig("Rate", 1, 2));
                columnModel.addHeaderGroup(0, 3, new HeaderGroupConfig("ITS", 1, 4));
                columnModel.addHeaderGroup(0, 7, new HeaderGroupConfig("Account", 1, 4));
            }
        });

        grdRate.initializeGrid(grdRate.getPropertyAccess().fieldHashCode(), 940, 222);
        return grdRate.getGrid();
    }
    //</editor-fold>

}
