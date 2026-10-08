package aarogya.inventory.gstServiceMaster.client;

import aarogya.inventory.gstServiceMaster.shared.Sku_Mdl;
import aarogya.inventory.gstServiceMaster.shared.TxtHCategory_Mdl;
import aarogya.inventory.gstServiceMaster.shared.Find_Mdl;
import aarogya.inventory.gstServiceMaster.shared.TxtHHSN_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.HeaderGroupConfig;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import aarogya.inventory.gstServiceMaster.shared.GrdItsRateSlab_Mdl;
import aarogya.projectMenu.self.shared.pmInventory;

public class GstServiceMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmInventory().SERVICE_OVERHEAD_MASTER.caption;
    public final static String preparedByBlog = "Prepared By & Date";
    public final static String modifiedByBlog = "Modified By & Date";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtUniqueId = new DmTextBox(2, false);
    final DmSuggestBox txtItemName = new DmSuggestBox(50, 10, true, "Name", DmEnum.DmDataType.STRING);
    final DmTextBox txtManualCode = new DmTextBox(20, false);
    final DmTextBoxNumeric txtValue = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtPrintSrNo = new DmTextBoxNumeric(0, false, 5, false);

    final Menu menu = new Menu();
    final MenuItem mnuManageSite = new MenuItem("Manage Site");
    final FlowPanel flwGstsContainer = new FlowPanel();
    final FlowPanel flwGstType = new FlowPanel();
    final FlowPanel flwMain = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="Panels">
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Text Helps">
    //<editor-fold defaultstate="collapsed" desc="TxtHHSN">

    @Override
    public Widget asWidget() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void searchRecord(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    interface TxtHHSN_PrpAcs extends KeyField_T_PrpAcs<TxtHHSN_Mdl> {

        @Editor.Path("Hsn")
        ValueProvider<TxtHHSN_Mdl, String> hsn();

        @Editor.Path("ItemGSTNatureText")
        ValueProvider<TxtHHSN_Mdl, String> gstNature();
    }
    final DmTextHelp<TxtHHSN_Mdl, TxtHHSN_PrpAcs> txtHHSN = new DmTextHelp(GWT.create(TxtHHSN_PrpAcs.class), 6, false);

    private void iniTxtHHSN() {
        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().hsn(), "HSN / SAC", 80, "hsn");
        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().gstNature(), "GST Nature", 80, "");
        txtHHSN.getDmGrid().initializeGrid(txtHHSN.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHHSN.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHSKU">
    interface SKU_PrpAcs extends KeyField_T_PrpAcs<Sku_Mdl> {
    }
    final DmTextHelp<Sku_Mdl, SKU_PrpAcs> txtHSKU = new DmTextHelp(GWT.create(SKU_PrpAcs.class), 4, true);

    private void iniSKU() {
        txtHSKU.getDmGrid().addTextColumn(txtHSKU.getDmGrid().getPropertyAccess().name(), "Name", 0, "unit");
        txtHSKU.getDmGrid().initializeGrid(txtHSKU.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHSKU.getHelper().addSort("unit", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHCategory">
    interface TxtHCategory_PrpAcs extends KeyField_T_PrpAcs<TxtHCategory_Mdl> {

    }
    final DmTextHelp<TxtHCategory_Mdl, TxtHCategory_PrpAcs> txtHCategory = new DmTextHelp(GWT.create(TxtHCategory_PrpAcs.class), 6, true);

    private void iniTxtHCategory() {
        txtHCategory.getDmGrid().addTextColumn(txtHCategory.getDmGrid().getPropertyAccess().name(), "Name", 1, "categoryName");
        txtHCategory.getDmGrid().initializeGrid(txtHCategory.getDmGrid().getPropertyAccess().code(), 185, 0, 1);

        txtHCategory.getHelper().addSort("categoryName", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHMedGroup() {
        txtHGroup.getDmGrid().addTextColumn(txtHGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHGroup.getDmGrid().initializeGrid(txtHGroup.getDmGrid().getPropertyAccess().code(), 275, 0, 1);

        txtHGroup.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHActive() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHType() {
        txtHType.getDmGrid().addTextColumn(txtHType.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHType.getDmGrid().initializeGrid(txtHType.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHType.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHMapWithField">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMapWithField = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniTxtHMapWithField() {
        txtHMapWithField.getDmGrid().addTextColumn(txtHMapWithField.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHMapWithField.getDmGrid().initializeGrid(txtHMapWithField.getDmGrid().getPropertyAccess().code(), 275, 0, 1);

        txtHMapWithField.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHTaxBasedOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTaxBasedOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniTxtHTaxBasedOn() {
        txtHTaxBasedOn.getDmGrid().addTextColumn(txtHTaxBasedOn.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHTaxBasedOn.getDmGrid().initializeGrid(txtHTaxBasedOn.getDmGrid().getPropertyAccess().code(), 275, 0, 1);

        txtHTaxBasedOn.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHItemGSTNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGSTNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHItemGSTNature() {
        txtHGSTNature.getDmGrid().addTextColumn(txtHGSTNature.getDmGrid().getPropertyAccess().name(), "Nature", 0, "");
        txtHGSTNature.getDmGrid().initializeGrid(txtHGSTNature.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHValuationType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHValuationType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHValuationType() {
        txtHValuationType.getDmGrid().addTextColumn(txtHValuationType.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHValuationType.getDmGrid().initializeGrid(txtHValuationType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHGstType">
    final DmTextHelp<KeyFieldExtended, dataman.dmBase.self.shared.KeyField_PrpAcs> txtHGstType = new DmTextHelp(GWT.create(dataman.dmBase.self.shared.KeyField_PrpAcs.class), 5, true, false);

    private void iniTxtHGstType() {
        txtHGstType.getDmGrid().addTextColumn(txtHGstType.getDmGrid().getPropertyAccess().name(), "Type", 120, "name");
        txtHGstType.getDmGrid().initializeGrid(txtHGstType.getDmGrid().getPropertyAccess().code(), 120, 0, 1);

        txtHGstType.getHelper().addSort("printSrNo", DmSortType.ASC);
        txtHGstType.getHelper().addSort("name", DmSortType.ASC);

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHSubCode">
    interface KeyField_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSubCode = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, false);

    private void iniTxtHSubCode() {
        txtHSubCode.getDmGrid().addTextColumn(txtHSubCode.getDmGrid().getPropertyAccess().name(), "Inventory A/c", 100, "Name");
        txtHSubCode.getDmGrid().addTextColumn(txtHSubCode.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHSubCode.getDmGrid().initializeGrid(txtHSubCode.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHSubCode.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>  
    //<editor-fold defaultstate="collapsed" desc="TxtHPurchase">
    interface KeyField_PrpAcs1 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs1> txtHPurchase = new DmTextHelp(GWT.create(KeyField_PrpAcs1.class), 6, false);

    private void iniTxtHPurchase() {
        txtHPurchase.getDmGrid().addTextColumn(txtHPurchase.getDmGrid().getPropertyAccess().name(), "Purchase A/c", 100, "Name");
        txtHPurchase.getDmGrid().addTextColumn(txtHPurchase.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHPurchase.getDmGrid().initializeGrid(txtHPurchase.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHPurchase.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="TxtHPurchaseReturn">
    interface KeyField_PrpAcs2 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs1> txtHPurchaseReturn = new DmTextHelp(GWT.create(KeyField_PrpAcs1.class), 6, false);

    private void iniTxtHPurchaseReturn() {
        txtHPurchaseReturn.getDmGrid().addTextColumn(txtHPurchaseReturn.getDmGrid().getPropertyAccess().name(), "Purchase Return A/c", 100, "Name");
        txtHPurchaseReturn.getDmGrid().addTextColumn(txtHPurchaseReturn.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHPurchaseReturn.getDmGrid().initializeGrid(txtHPurchaseReturn.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHPurchaseReturn.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold> 
    //<editor-fold defaultstate="collapsed" desc="TxtHSales">
    interface KeyField_PrpAcs3 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs2> txtHSales = new DmTextHelp(GWT.create(KeyField_PrpAcs2.class), 6, false);

    private void iniTxtHSales() {
        txtHSales.getDmGrid().addTextColumn(txtHSales.getDmGrid().getPropertyAccess().name(), "Sales A/c", 100, "Name");
        txtHSales.getDmGrid().addTextColumn(txtHSales.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHSales.getDmGrid().initializeGrid(txtHSales.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHSales.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHSalesReturn">
    interface KeyField_PrpAcs4 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs2> txtHSalesReturn = new DmTextHelp(GWT.create(KeyField_PrpAcs2.class), 6, false);

    private void iniTxtHSalesReturn() {
        txtHSalesReturn.getDmGrid().addTextColumn(txtHSalesReturn.getDmGrid().getPropertyAccess().name(), "Sales Return A/c", 100, "Name");
        txtHSalesReturn.getDmGrid().addTextColumn(txtHSalesReturn.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHSalesReturn.getDmGrid().initializeGrid(txtHSalesReturn.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHSalesReturn.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPayableAc">
    interface KeyField_PrpAcs5 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs5> txtHPayableAc = new DmTextHelp(GWT.create(KeyField_PrpAcs5.class), 6, false);

    private void iniTxtHPayableAc() {
        txtHPayableAc.getDmGrid().addTextColumn(txtHPayableAc.getDmGrid().getPropertyAccess().name(), "Inventory A/c", 100, "Name");
        txtHPayableAc.getDmGrid().addTextColumn(txtHPayableAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHPayableAc.getDmGrid().initializeGrid(txtHPayableAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHPayableAc.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Category")
        ValueProvider<Find_Mdl, String> category();

        @Editor.Path("ManualCode")
        ValueProvider<Find_Mdl, String> manualCode();

        @Editor.Path("Group")
        ValueProvider<Find_Mdl, String> group();

        @Editor.Path("SkuName")
        ValueProvider<Find_Mdl, String> sku();

        @Editor.Path("Active")
        ValueProvider<Find_Mdl, String> active();

        @Editor.Path("ItemTypeText")
        ValueProvider<Find_Mdl, String> itemTypeText();

        @Editor.Path("HsnName")
        ValueProvider<Find_Mdl, String> hsnName();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 18);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Item Name", 250, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().hsnName(), "HSN", 120, "hsnName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().category(), "Category", 100, "categoryName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().group(), "Group", 120, "groupName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().itemTypeText(), "Type", 130, "typename");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().sku(), "SKU", 70, "sku");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().active(), "Active", 80, "activeText");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    public GstServiceMaster_Dsg() {
        ini();
        iniTxtHHSN();
        iniSKU();
        iniTxtHCategory();
        iniTxtHMedGroup();
        iniTxtHActive();
        iniOtherMenu();
        iniTxtHType();
        iniTxtHMapWithField();
        iniTxtHTaxBasedOn();
        iniTxtHItemGSTNature();
        iniTxtHValuationType();
        iniTxtHGstType();
        iniTxtHSubCode();
        iniTxtHPurchase();
        iniTxtHSales();
        iniTxtHPurchaseReturn();
        iniTxtHSalesReturn();
        iniTxtHPayableAc();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");

        userControl.addTextBlog(preparedByBlog, 200, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 200, true, false);
        vrtInnerForm.add(userControl);

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "60%", "410px", null, null, DmEnum.DmPosition.CENTER_HORIZONTAL, true);
        //-------------------------------------------------1st Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtItemName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

//-------------------------------------------------2nd Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Manual Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtManualCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Unique ID");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtUniqueId));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //-------------------------------------------------3rd Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("SKU");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSKU));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

//-------------------------------------------------#####---------------------------------------//
//-------------------------------------------------4th Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Category");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCategory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

//-------------------------------------------------5th Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Group");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGroup));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //-------------------------------------------------6th Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(new DmLabel("Service A/c"));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSubCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //-------------------------------------------------7th Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Purchase A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPurchase));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(22, Style.Unit.PX);
        lbl = new DmLabel("Purchase Return A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPurchaseReturn));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //-------------------------------------------------8th Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Sales A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSales));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(22, Style.Unit.PX);
        lbl = new DmLabel("Sales Return A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSalesReturn));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //-------------------------------------------------9th Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Payable Ac");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPayableAc));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //-------------------------------------------------9th Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Map With Field");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMapWithField));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

//-------------------------------------------------10th Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("SAC");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHSN));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(22, Style.Unit.PX);
        lbl = new DmLabel("GST Nature");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGSTNature));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //-------------------------------------------------8th Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Valuation Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHValuationType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Value");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

//-------------------------------------------------#####---------------------------------------//
//-------------------------------------------------10th Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Print Sr.No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPrintSrNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("GST Based On");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTaxBasedOn));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //-------------------------------------------------11th Row---------------------------------------//
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
//
//        /*====================================<grid Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmEnum.DmPosition.NONE, false);

        DmClient.setStyle(flwGstsContainer, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        DmClient.setStyle(flwGstType, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwGstsContainer.add(rateGrid());
        flwGstType.add(gstTypeText());
        flwTd.add(flwMain);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        lbl.getElement().getStyle().setMarginLeft(24, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(24, Style.Unit.PX);
        flwTd.add(vl.add(txtHActive));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rateGrid() {
        FlowPanel flwTable, flwTR;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "50px", null, null, DmEnum.DmPosition.NONE, true);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, null, null, DmEnum.DmPosition.NONE, false);
        flwTR.add((iniGrdRate()));
        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel gstTypeText() {
        FlowPanel flwTable, flwTR, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmEnum.DmPosition.NONE, true);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmEnum.DmPosition.NONE, false);

        lbl = new DmLabel("GST Type");
        flwTd.add(lbl);
        flwTR.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGstType));
        flwTR.add(flwTd);

        flwTable.add(flwTR);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdItsRateSlab">
    interface GridItsRateSlab_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItsRateSlab_Mdl> {

        @Editor.Path("RateFrom")
        ValueProvider<GrdItsRateSlab_Mdl, String> rateFrom();

        @Editor.Path("RateUpto")
        ValueProvider<GrdItsRateSlab_Mdl, String> rateUpto();

        @Editor.Path("GstTypeName")
        ValueProvider<GrdItsRateSlab_Mdl, String> gstTypeName();

    }
    final DmGrid<GrdItsRateSlab_Mdl, GridItsRateSlab_PrpAcs> grdItsRateSlab = new DmGrid(GWT.create(GridItsRateSlab_PrpAcs.class));

    protected Grid iniGrdRate() {
        grdItsRateSlab.addRowNumberColumn();
        grdItsRateSlab.addTextColumn(grdItsRateSlab.getPropertyAccess().rateFrom(), "From", 92, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItsRateSlab.addTextColumn(grdItsRateSlab.getPropertyAccess().rateUpto(), "Upto", 92, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItsRateSlab.addTextColumn(grdItsRateSlab.getPropertyAccess().gstTypeName(), "GST Type", 124, "");

        grdItsRateSlab.addBeforeInitializeHandler(new DmGrid.BeforeInitialize() {
            @Override
            public void beforeInitialize(ColumnModel columnModel) {
                columnModel.addHeaderGroup(0, 1, new HeaderGroupConfig("Rate", 1, 2));
            }
        });

        grdItsRateSlab.initializeGrid(grdItsRateSlab.getPropertyAccess().fieldHashCode(), 385, 150);
        return grdItsRateSlab.getGrid();
    }
    //</editor-fold>

    private Menu iniOtherMenu() {
        menu.add(mnuManageSite);
        return menu;
    }

}
