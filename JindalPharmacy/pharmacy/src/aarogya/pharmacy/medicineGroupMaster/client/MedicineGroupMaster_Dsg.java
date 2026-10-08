package aarogya.pharmacy.medicineGroupMaster.client;

import aarogya.pharmacy.medicineGroupMaster.shared.Find_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class MedicineGroupMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().MEDICINE_GROUP_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmSuggestBox txtMediGrp = new DmSuggestBox(200, 10, true, "Name", DmDataType.STRING);
    final DmTextBoxNumeric txtSaleDis = new DmTextBoxNumeric(2, false, 5, false);

    final MenuItem mnuExportCard = new MenuItem("Export Data");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("SaleDiscount")
        ValueProvider<Find_Mdl, String> saleDiscount();

        @Editor.Path("IsCommonText")
        ValueProvider<Find_Mdl, String> isCommon();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 8);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Group Name", 100, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().saleDiscount(), "Sale Discount", 100, "saleDiscount");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isCommon(), "Common", 100, "commonText");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);
        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHIsCommon">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsCommon = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHIsCommon() {
        txtHIsCommon.getDmGrid().addTextColumn(txtHIsCommon.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHIsCommon.getDmGrid().initializeGrid(txtHIsCommon.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    
     //<editor-fold defaultstate="collapsed" desc="txtHInventoryAc">
    interface KeyField_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHInventoryAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, false);

    private void iniTxtHInventoryAc() {
        txtHInventoryAc.getDmGrid().addTextColumn(txtHInventoryAc.getDmGrid().getPropertyAccess().name(), "Inventory A/c", 100, "Name");
        txtHInventoryAc.getDmGrid().addTextColumn(txtHInventoryAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHInventoryAc.getDmGrid().initializeGrid(txtHInventoryAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHInventoryAc.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //=======================================================other Accounts=================================================
    //<editor-fold defaultstate="collapsed" desc="txtHConsumptionAc">
    interface KeyField_PrpAcs5 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs5> txtHConsumptionAc = new DmTextHelp(GWT.create(KeyField_PrpAcs5.class), 6, false);

    private void iniTxtHConsumptionAc() {
        txtHConsumptionAc.getDmGrid().addTextColumn(txtHConsumptionAc.getDmGrid().getPropertyAccess().name(), "Inventory A/c", 100, "Name");
        txtHConsumptionAc.getDmGrid().addTextColumn(txtHConsumptionAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHConsumptionAc.getDmGrid().initializeGrid(txtHConsumptionAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHConsumptionAc.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHGrIrAc">
    interface KeyField_PrpAcs6 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs6> txtHGrIrAc = new DmTextHelp(GWT.create(KeyField_PrpAcs6.class), 6, false);

    private void iniTxtHGrIrAc() {
        txtHGrIrAc.getDmGrid().addTextColumn(txtHGrIrAc.getDmGrid().getPropertyAccess().name(), "Inventory A/c", 100, "Name");
        txtHGrIrAc.getDmGrid().addTextColumn(txtHGrIrAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHGrIrAc.getDmGrid().initializeGrid(txtHGrIrAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHGrIrAc.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHCOGSAc">
    interface KeyField_PrpAcs7 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs7> txtHCOGSAc = new DmTextHelp(GWT.create(KeyField_PrpAcs7.class), 6, false);

    private void iniTxtHCOGSAc() {
        txtHCOGSAc.getDmGrid().addTextColumn(txtHCOGSAc.getDmGrid().getPropertyAccess().name(), "Inventory A/c", 100, "Name");
        txtHCOGSAc.getDmGrid().addTextColumn(txtHCOGSAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHCOGSAc.getDmGrid().initializeGrid(txtHCOGSAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHCOGSAc.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHInTransitAc">
    interface KeyField_PrpAcs8 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs8> txtHInTransitAc = new DmTextHelp(GWT.create(KeyField_PrpAcs8.class), 6, false);

    private void iniTxtHInTransitAc() {
        txtHInTransitAc.getDmGrid().addTextColumn(txtHInTransitAc.getDmGrid().getPropertyAccess().name(), "Inventory A/c", 100, "Name");
        txtHInTransitAc.getDmGrid().addTextColumn(txtHInTransitAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHInTransitAc.getDmGrid().initializeGrid(txtHInTransitAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHInTransitAc.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHPIDAc">
    interface KeyField_PrpAcs9 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs9> txtHPIDAc = new DmTextHelp(GWT.create(KeyField_PrpAcs9.class), 6, false);

    private void iniTxtHPIDAc() {
        txtHPIDAc.getDmGrid().addTextColumn(txtHPIDAc.getDmGrid().getPropertyAccess().name(), "Inventory A/c", 100, "Name");
        txtHPIDAc.getDmGrid().addTextColumn(txtHPIDAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHPIDAc.getDmGrid().initializeGrid(txtHPIDAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHPIDAc.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>
    //=======================================================other Accounts=================================================

    //<editor-fold defaultstate="collapsed" desc="TxtHPurchaseAc">
    interface KeyField_PrpAcs1 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs1> txtHPurchaseAc = new DmTextHelp(GWT.create(KeyField_PrpAcs1.class), 6, false);

    private void iniTxtHPurchaseAc() {
        txtHPurchaseAc.getDmGrid().addTextColumn(txtHPurchaseAc.getDmGrid().getPropertyAccess().name(), "Purchase A/c", 100, "Name");
        txtHPurchaseAc.getDmGrid().addTextColumn(txtHPurchaseAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHPurchaseAc.getDmGrid().initializeGrid(txtHPurchaseAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHPurchaseAc.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHPurchaseReturnAc">
    interface KeyField_PrpAcs2 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs2> txtHPurchaseReturnAc = new DmTextHelp(GWT.create(KeyField_PrpAcs2.class), 6, false);

    private void iniTxtHPurchaseReturnAc() {
        txtHPurchaseReturnAc.getDmGrid().addTextColumn(txtHPurchaseReturnAc.getDmGrid().getPropertyAccess().name(), "Purchase Return A/c", 100, "Name");
        txtHPurchaseReturnAc.getDmGrid().addTextColumn(txtHPurchaseReturnAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHPurchaseReturnAc.getDmGrid().initializeGrid(txtHPurchaseReturnAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHPurchaseReturnAc.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHSalesAc">
    interface KeyField_PrpAcs3 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs3> txtHSalesAc = new DmTextHelp(GWT.create(KeyField_PrpAcs3.class), 6, false);

    private void iniTxtHSalesAc() {
        txtHSalesAc.getDmGrid().addTextColumn(txtHSalesAc.getDmGrid().getPropertyAccess().name(), "Sales Return A/c", 100, "Name");
        txtHSalesAc.getDmGrid().addTextColumn(txtHSalesAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHSalesAc.getDmGrid().initializeGrid(txtHSalesAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHSalesAc.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHSalesReturnAc">
    interface KeyField_PrpAcs4 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs4> txtHSalesReturnAc = new DmTextHelp(GWT.create(KeyField_PrpAcs4.class), 6, false);

    private void iniTxtHSalesReturnAc() {
        txtHSalesReturnAc.getDmGrid().addTextColumn(txtHSalesReturnAc.getDmGrid().getPropertyAccess().name(), "Sales Return A/c", 100, "Name");
        txtHSalesReturnAc.getDmGrid().addTextColumn(txtHSalesReturnAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHSalesReturnAc.getDmGrid().initializeGrid(txtHSalesReturnAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHSalesReturnAc.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>
    public MedicineGroupMaster_Dsg() {
        ini();
        iniTxtHIsCommon();
        iniMenu();
        iniTxtHPurchaseAc();
        iniTxtHPurchaseReturnAc();
        iniTxtHSalesAc();
        iniTxtHSalesReturnAc();
        iniTxtHInventoryAc();
        iniTxtHConsumptionAc();
        iniTxtHGrIrAc();
        iniTxtHCOGSAc();
        iniTxtHInTransitAc();
        iniTxtHPIDAc();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 30) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "40%", "285px", null, null, DmPosition.CENTER, true);

        //======================= 1st Row (MEDICINE GROUP) ===========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Group");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMediGrp));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //============================== END =========================================

        //======================= 2nd Row (SALE DISCOUNT) ===========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Discount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSaleDis));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //======================= 3rd Row (IS COMMON) ===========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Common");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsCommon));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        //======================= 3rd Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPurchaseAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //======================= 4th Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase Return A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPurchaseReturnAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //======================= 5th Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sales A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSalesAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //======================= 6th Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sales Return A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSalesReturnAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //======================= 7th Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Inventory A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHInventoryAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //======================= 8th Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gr/Ir - A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGrIrAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //======================= 9th Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consumption A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHConsumptionAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //======================= 10th Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("COGS A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCOGSAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //======================= 11th Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PID A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPIDAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //======================= 12th Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Intransit A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHInTransitAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //============================== END =========================================

        
        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuExportCard);
        return menu;
    }
}
