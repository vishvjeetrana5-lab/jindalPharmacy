package aarogya.inventory.itemMaster.client;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.grid.HeaderGroupConfig;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import aarogya.inventory.itemMaster.shared.Find_Mdl;
import aarogya.inventory.itemMaster.shared.GrdAlternativeItems_Mdl;
import aarogya.inventory.itemMaster.shared.GrdApprovedVendors_Mdl;
import aarogya.inventory.itemMaster.shared.GrdItemDepartmentInfo_Mdl;
import aarogya.inventory.itemMaster.shared.GrdQcParameter_Mdl;
import aarogya.inventory.itemMaster.shared.GrdItsRateSlab_Mdl;
import aarogya.inventory.itemMaster.shared.GrdRestrictedDept_Mdl;
import aarogya.inventory.itemMaster.shared.GrdSoundLookAlike_Mdl;
import aarogya.inventory.itemMaster.shared.Sku_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHCategory_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHMaterialCategory_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHHSN_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHItemPrperty_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHItemType_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHMarketingCo_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHManufacturer_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHItemMake_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHDivision_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHGroup_Mdl;
import aarogya.projectMenu.self.shared.pmInventory;

abstract class ItemMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmInventory().ITEM_MASTER.caption;
    public final static String preparedByBlog = "Prepared By & Date";
    public final static String modifiedByBlog = "Modified By & Date";
    public final static String statusChangedByBlog = "Status By & Date";
    final Button btnActiveBy = new Button();
    final DmTextBox txtWTACode = new DmTextBox(15, true);
    final DmTextBoxNumeric txtTolerancePlus = new DmTextBoxNumeric(2, false, 5, false);
    final DmTextBoxNumeric txtToleranceMinus = new DmTextBoxNumeric(2, false, 5, false);

    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtManualCode = new DmTextBox(20, false);

    final DmTextBox txtPackSize = new DmTextBox(15, true);
    int tabIndex = 0, tabIndexAisala;

    final DmSuggestBox txtName = new DmSuggestBox(100, 10, true, "Name", DmDataType.STRING);

    final DmTextBoxNumeric txtMinLvl = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtMaxLvl = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtReOrder = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtConversionRatePurchase = new DmTextBoxNumeric(4, false, 10, true);
    final DmTextBoxNumeric txtConversionRateSale = new DmTextBoxNumeric(4, false, 10, true);

    final DmLabel lblPurchaseConvertionRate = new DmLabel("");
    final DmLabel lblSaleConvertionRate = new DmLabel("");

    final DmTextBox txtBin = new DmTextBox(20, false);
    SelectionHandler<MenuItem> selectionHandler;

    final Menu menu = new Menu();
    final MenuItem mnuManageSite = new MenuItem("Manage Site");
    final MenuItem mnuExportCard = new MenuItem("Export Data");

    final DmLabel lblPackSize = new DmLabel("");
    final FlowPanel flwITSContainer = new FlowPanel();
    final FlowPanel flwGstType = new FlowPanel();
    final FlowPanel flwMain = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="Helps & find">
    //<editor-fold defaultstate="collapsed" desc="txtHItemType">
    interface TxtHItemType_PrpAcs extends KeyField_T_PrpAcs<TxtHItemType_Mdl> {

        @Editor.Path("Shortname")
        ValueProvider<TxtHItemType_Mdl, String> shortname();
    }

    final DmTextHelp<TxtHItemType_Mdl, TxtHItemType_PrpAcs> txtHItemType = new DmTextHelp(GWT.create(TxtHItemType_PrpAcs.class), 6, true);

    private void iniTxtHItemType() {
        txtHItemType.getDmGrid().addTextColumn(txtHItemType.getDmGrid().getPropertyAccess().name(), "Item Type", 0, "name");
        txtHItemType.getDmGrid().addTextColumn(txtHItemType.getDmGrid().getPropertyAccess().shortname(), "Short Name", 70, "manualcode");
        txtHItemType.getDmGrid().initializeGrid(txtHItemType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHItemType.getHelper().addSort("name", DmSortType.ASC);
        txtHItemType.getHelper().addSort("manualcode", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHHSN">
    interface TxtHHSN_PrpAcs extends KeyField_T_PrpAcs<TxtHHSN_Mdl> {

        @Editor.Path("Hsn")
        ValueProvider<TxtHHSN_Mdl, String> hsn();
    }
    final DmTextHelp<TxtHHSN_Mdl, TxtHHSN_PrpAcs> txtHHSN = new DmTextHelp(GWT.create(TxtHHSN_PrpAcs.class), 6, false);

    private void initxtHHSN() {
        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().hsn(), "HSN / SAC", 80, "hsn");
        txtHHSN.getDmGrid().initializeGrid(txtHHSN.getDmGrid().getPropertyAccess().code(), 280, 0, 1);
        txtHHSN.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHCategory">
    interface TxtHCategory_PrpAcs extends KeyField_T_PrpAcs<TxtHCategory_Mdl> {

    }
    final DmTextHelp<TxtHCategory_Mdl, TxtHCategory_PrpAcs> txtHCategory = new DmTextHelp(GWT.create(TxtHCategory_PrpAcs.class), 6, true);

    private void iniTxtHCategory() {
        txtHCategory.getDmGrid().addTextColumn(txtHCategory.getDmGrid().getPropertyAccess().name(), "Item Category", 1, "categoryName");
        txtHCategory.getDmGrid().initializeGrid(txtHCategory.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
        txtHCategory.getHelper().addSort("categoryName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMaterialCategory">
    interface TxtHMaterialCategory_PrpAcs extends KeyField_T_PrpAcs<TxtHMaterialCategory_Mdl> {

    }
    final DmTextHelp<TxtHMaterialCategory_Mdl, TxtHMaterialCategory_PrpAcs> txtHMaterialCategory = new DmTextHelp(GWT.create(TxtHMaterialCategory_PrpAcs.class), 6, true);

    private void iniTxtHMaterialCategory() {
        txtHMaterialCategory.getDmGrid().addTextColumn(txtHMaterialCategory.getDmGrid().getPropertyAccess().name(), "Name", 1, "categoryName");
        txtHMaterialCategory.getDmGrid().initializeGrid(txtHMaterialCategory.getDmGrid().getPropertyAccess().code(), 185, 0, 1);

        txtHMaterialCategory.getHelper().addSort("categoryName", DmSortType.ASC);
    }
    //</editor-fold>
    
    

    //<editor-fold defaultstate="collapsed" desc="TxtHItemGroup">
    interface TxtHGroup_PrpAcs extends KeyField_T_PrpAcs<TxtHGroup_Mdl> {

    }
    final DmTextHelp<TxtHGroup_Mdl, TxtHGroup_PrpAcs> txtHItemGroup = new DmTextHelp(GWT.create(TxtHGroup_PrpAcs.class), 10, true);

    private void iniTxtHItemGroup() {
        txtHItemGroup.getDmGrid().addTextColumn(txtHItemGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHItemGroup.getDmGrid().initializeGrid(txtHItemGroup.getDmGrid().getPropertyAccess().code(), 275, 0, 1);

        txtHItemGroup.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHSKU">
    interface SKU_PrpAcs extends KeyField_T_PrpAcs<Sku_Mdl> {

        @Editor.Path("AllowedDecimalPlaces")
        ValueProvider<Sku_Mdl, String> allowedDecimalPlaces();

    }

    final DmTextHelp<Sku_Mdl, SKU_PrpAcs> txtHSKU = new DmTextHelp(GWT.create(SKU_PrpAcs.class), 4, true);

    private void iniSKU() {
        txtHSKU.getDmGrid().addTextColumn(txtHSKU.getDmGrid().getPropertyAccess().name(), "Name", 0, "unit");
        txtHSKU.getDmGrid().initializeGrid(txtHSKU.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHSKU.getHelper().addSort("unit", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHBestBefore">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBestBefore = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHBestBefore() {
        txtHBestBefore.getDmGrid().addTextColumn(txtHBestBefore.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHBestBefore.getDmGrid().initializeGrid(txtHBestBefore.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHHighValue">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHighValue = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHHighValue() {
        txtHHighValue.getDmGrid().addTextColumn(txtHHighValue.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHHighValue.getDmGrid().initializeGrid(txtHHighValue.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHMaterialType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMaterialType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHMaterialType() {
        txtHMaterialType.getDmGrid().addTextColumn(txtHMaterialType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHMaterialType.getDmGrid().initializeGrid(txtHMaterialType.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHMaterialType.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="TxtHItemModel">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHItemModel = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHItemModel() {
        txtHItemModel.getDmGrid().addTextColumn(txtHItemModel.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHItemModel.getDmGrid().initializeGrid(txtHItemModel.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHItemModel.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="TxtHItemModel">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHItemStrength = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHItemStrength() {
        txtHItemStrength.getDmGrid().addTextColumn(txtHItemStrength.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHItemStrength.getDmGrid().initializeGrid(txtHItemStrength.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHItemStrength.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHFlowThrough">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHFlowThrough = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHFlowThrough() {
        txtHFlowThrough.getDmGrid().addTextColumn(txtHFlowThrough.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHFlowThrough.getDmGrid().initializeGrid(txtHFlowThrough.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHConsignmentItem">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHConsignmentItem = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHConsignmentItem() {
        txtHConsignmentItem.getDmGrid().addTextColumn(txtHConsignmentItem.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHConsignmentItem.getDmGrid().initializeGrid(txtHConsignmentItem.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHReturnable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReturnable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHReturnable() {
        txtHReturnable.getDmGrid().addTextColumn(txtHReturnable.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHReturnable.getDmGrid().initializeGrid(txtHReturnable.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHBilledItem">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBilledItem = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHBuildItem() {
        txtHBilledItem.getDmGrid().addTextColumn(txtHBilledItem.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHBilledItem.getDmGrid().initializeGrid(txtHBilledItem.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //    //<editor-fold defaultstate="collapsed" desc="TxtHHSN">
//    interface TxtHHSN_PrpAcs extends KeyField_T_PrpAcs<TxtHHSN_Mdl> {
//
//        @Editor.Path("Hsn")
//        ValueProvider<TxtHHSN_Mdl, String> hsn();
//    }
//    final DmTextHelp<TxtHHSN_Mdl, TxtHHSN_PrpAcs> txtHSN = new DmTextHelp(GWT.create(TxtHHSN_PrpAcs.class), 6, false);
//
//    private void initxtHHSN() {
//        txtHSN.getDmGrid().addTextColumn(txtHSN.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
//        txtHSN.getDmGrid().addTextColumn(txtHSN.getDmGrid().getPropertyAccess().hsn(), "HSN", 80, "hsn");
//        txtHSN.getDmGrid().initializeGrid(txtHSN.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
//
//        txtHSN.getHelper().addSort("name", DmSortType.ASC);
//    }
//    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHPurchaseUnitSKU">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPurchaseUnitSKU = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 4, true);

    private void initxtHPurchaseUnitSKU() {
        txtHPurchaseUnitSKU.getDmGrid().addTextColumn(txtHPurchaseUnitSKU.getDmGrid().getPropertyAccess().name(), "Name", 0, "unit");
        txtHPurchaseUnitSKU.getDmGrid().initializeGrid(txtHPurchaseUnitSKU.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHPurchaseUnitSKU.getHelper().addSort("unit", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHSaleUnitSKU">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSaleUnitSKU = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 4, true);

    private void initxtHSaleUnitSKU() {
        txtHSaleUnitSKU.getDmGrid().addTextColumn(txtHSaleUnitSKU.getDmGrid().getPropertyAccess().name(), "Name", 0, "unit");
        txtHSaleUnitSKU.getDmGrid().initializeGrid(txtHSaleUnitSKU.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHSaleUnitSKU.getHelper().addSort("unit", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHItemMake">
    interface TxtHItemMake_PrpAcs extends KeyField_T_PrpAcs<TxtHItemMake_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHItemMake_Mdl, String> shortName();
    }
    final DmTextHelp<TxtHItemMake_Mdl, TxtHItemMake_PrpAcs> txtHItemMake = new DmTextHelp(GWT.create(TxtHItemMake_PrpAcs.class), 6, true);

    private void iniTxtHItemMake() {
        txtHItemMake.getDmGrid().addTextColumn(txtHItemMake.getDmGrid().getPropertyAccess().name(), "Company Name", 100, "companyName");
        txtHItemMake.getDmGrid().addTextColumn(txtHItemMake.getDmGrid().getPropertyAccess().shortName(), "Short Name", 80, "shortName");
        txtHItemMake.getDmGrid().initializeGrid(txtHItemMake.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHItemMake.getHelper().addSort("companyName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMarketingCo">
    interface TxtHMarketingCo_PrpAcs extends KeyField_T_PrpAcs<TxtHMarketingCo_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHMarketingCo_Mdl, String> shortName();
    }
    final DmTextHelp<TxtHMarketingCo_Mdl, TxtHMarketingCo_PrpAcs> txtHMarketingCo = new DmTextHelp(GWT.create(TxtHMarketingCo_PrpAcs.class), 6, true);

    private void iniMarketingCo() {
        txtHMarketingCo.getDmGrid().addTextColumn(txtHMarketingCo.getDmGrid().getPropertyAccess().name(), "Company Name", 100, "companyName");
        txtHMarketingCo.getDmGrid().addTextColumn(txtHMarketingCo.getDmGrid().getPropertyAccess().shortName(), "Short Name", 80, "shortName");
        txtHMarketingCo.getDmGrid().initializeGrid(txtHMarketingCo.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHMarketingCo.getHelper().addSort("companyName", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHProperties">
    interface TxtHItemPrperty_PrpAcs extends KeyField_T_PrpAcs<TxtHItemPrperty_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHItemPrperty_Mdl, String> manualCode();
    }
    DmTextHelpMulti<TxtHItemPrperty_Mdl, TxtHItemPrperty_PrpAcs> txtHProperties = new DmTextHelpMulti(GWT.create(TxtHItemPrperty_PrpAcs.class), 6);

    private void iniTxtHProperties() {
        txtHProperties.getDmGrid().addTextColumn(txtHProperties.getDmGrid().getPropertyAccess().name(), "Property", 1, "Name");
        txtHProperties.getDmGrid().addTextColumn(txtHProperties.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHProperties.getDmGrid().initializeGrid(txtHProperties.getDmGrid().getPropertyAccess().code(), 350, 0, 2);
        txtHProperties.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAllowNegetiveStock">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAllowNegetiveStock = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHAllowNegetiveStock() {
        txtHAllowNegetiveStock.getDmGrid().addTextColumn(txtHAllowNegetiveStock.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHAllowNegetiveStock.getDmGrid().initializeGrid(txtHAllowNegetiveStock.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHAllowNegetiveStock.getHelper().addSort("Type", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHPoReq">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPoReq = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHPoReq() {
        txtHPoReq.getDmGrid().addTextColumn(txtHPoReq.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHPoReq.getDmGrid().initializeGrid(txtHPoReq.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIndentReq">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIndentReq = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHIndentReq() {
        txtHIndentReq.getDmGrid().addTextColumn(txtHIndentReq.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHIndentReq.getDmGrid().initializeGrid(txtHIndentReq.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHQuotationReq">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHQuotationReq = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHQuotationReq() {
        txtHQuotationReq.getDmGrid().addTextColumn(txtHQuotationReq.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHQuotationReq.getDmGrid().initializeGrid(txtHQuotationReq.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHLabRequired">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHLabReq = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 4, true);

    public void iniTxtHLabReq() {
        txtHLabReq.getDmGrid().addTextColumn(txtHLabReq.getDmGrid().getPropertyAccess().name(), "Lab Required", 0, "NAME");
        txtHLabReq.getDmGrid().initializeGrid(txtHLabReq.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHItemGSTNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHItemGSTNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHItemGSTNature() {
        txtHItemGSTNature.getDmGrid().addTextColumn(txtHItemGSTNature.getDmGrid().getPropertyAccess().name(), "Nature", 0, "");
        txtHItemGSTNature.getDmGrid().initializeGrid(txtHItemGSTNature.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHITSBasedOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHITSBasedOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHTaxBasedOn() {
        txtHITSBasedOn.getDmGrid().addTextColumn(txtHITSBasedOn.getDmGrid().getPropertyAccess().name(), "Nature", 0, "name");
        txtHITSBasedOn.getDmGrid().initializeGrid(txtHITSBasedOn.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHGstType">pe
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGstType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, false);

    private void iniTxtHGstType() {
        txtHGstType.getDmGrid().addTextColumn(txtHGstType.getDmGrid().getPropertyAccess().name(), "Type", 120, "name");
        txtHGstType.getDmGrid().initializeGrid(txtHGstType.getDmGrid().getPropertyAccess().code(), 120, 0, 1);

        txtHGstType.getHelper().addSort("printSrNo", DmSortType.ASC);
        txtHGstType.getHelper().addSort("name", DmSortType.ASC);

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
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs1> txtHPurchaseReturnAc = new DmTextHelp(GWT.create(KeyField_PrpAcs1.class), 6, false);

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
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs2> txtHSalesAc = new DmTextHelp(GWT.create(KeyField_PrpAcs2.class), 6, false);

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

    //<editor-fold defaultstate="collapsed" desc="txtHGRIRAc">
    interface KeyField_PrpAcs10 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs10> txtHGRIRAc = new DmTextHelp(GWT.create(KeyField_PrpAcs10.class), 6, false);

    private void iniTxtHGRIRAc() {
        txtHGRIRAc.getDmGrid().addTextColumn(txtHGRIRAc.getDmGrid().getPropertyAccess().name(), "Inventory A/c", 100, "Name");
        txtHGRIRAc.getDmGrid().addTextColumn(txtHGRIRAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHGRIRAc.getDmGrid().initializeGrid(txtHGRIRAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHGRIRAc.getHelper().addSort("Name", DmSortType.ASC);
    }

    //<editor-fold defaultstate="collapsed" desc="txtHConsumptionAc">
    interface KeyField_PrpAcs11 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs11> txtHConsumptionAc = new DmTextHelp(GWT.create(KeyField_PrpAcs11.class), 6, false);

    private void iniTxtHConsumptionAc() {
        txtHConsumptionAc.getDmGrid().addTextColumn(txtHConsumptionAc.getDmGrid().getPropertyAccess().name(), "Inventory A/c", 100, "Name");
        txtHConsumptionAc.getDmGrid().addTextColumn(txtHConsumptionAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHConsumptionAc.getDmGrid().initializeGrid(txtHConsumptionAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHConsumptionAc.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHCOGSAc">
    interface KeyField_PrpAcs12 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs12> txtHCOGSAc = new DmTextHelp(GWT.create(KeyField_PrpAcs12.class), 6, false);

    private void iniTxtHCOGSAc() {
        txtHCOGSAc.getDmGrid().addTextColumn(txtHCOGSAc.getDmGrid().getPropertyAccess().name(), "Inventory A/c", 100, "Name");
        txtHCOGSAc.getDmGrid().addTextColumn(txtHCOGSAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHCOGSAc.getDmGrid().initializeGrid(txtHCOGSAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHCOGSAc.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHPIDAc">
    interface KeyField_PrpAcs13 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs13> txtHPIDAc = new DmTextHelp(GWT.create(KeyField_PrpAcs13.class), 6, false);

    private void iniTxtHPIDAc() {
        txtHPIDAc.getDmGrid().addTextColumn(txtHPIDAc.getDmGrid().getPropertyAccess().name(), "Inventory A/c", 100, "Name");
        txtHPIDAc.getDmGrid().addTextColumn(txtHPIDAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHPIDAc.getDmGrid().initializeGrid(txtHPIDAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHPIDAc.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHInTransitAc">
    interface KeyField_PrpAcs14 extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs14> txtHInTransitAc = new DmTextHelp(GWT.create(KeyField_PrpAcs14.class), 6, false);

    private void iniTxtHInTransitAc() {
        txtHInTransitAc.getDmGrid().addTextColumn(txtHInTransitAc.getDmGrid().getPropertyAccess().name(), "Inventory A/c", 100, "Name");
        txtHInTransitAc.getDmGrid().addTextColumn(txtHInTransitAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHInTransitAc.getDmGrid().initializeGrid(txtHInTransitAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHInTransitAc.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHNarcotics">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNarcotics = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHNarcotics() {
        txtHNarcotics.getDmGrid().addTextColumn(txtHNarcotics.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHNarcotics.getDmGrid().initializeGrid(txtHNarcotics.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHItemSpeciality">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHItemSpeciality = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniTxtHItemSpeciality() {
        txtHItemSpeciality.getDmGrid().addTextColumn(txtHItemSpeciality.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHItemSpeciality.getDmGrid().initializeGrid(txtHItemSpeciality.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHItemSpeciality.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHManufacturer">
    interface TxtHManufacturer_PrpAcs extends KeyField_T_PrpAcs<TxtHManufacturer_Mdl> {

        @Editor.Path("CityName")
        ValueProvider<TxtHManufacturer_Mdl, String> city();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHManufacturer_Mdl, String> manualCode();

        @Editor.Path("Address")
        ValueProvider<TxtHManufacturer_Mdl, String> address();
    }
    final DmTextHelp<TxtHManufacturer_Mdl, TxtHManufacturer_PrpAcs> txtHManufacturer = new DmTextHelp(GWT.create(TxtHManufacturer_PrpAcs.class), 6, false);

    private void iniTxtHManufacturer() {
        txtHManufacturer.getDmGrid().addTextColumn(txtHManufacturer.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHManufacturer.getDmGrid().addTextColumn(txtHManufacturer.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "");
        txtHManufacturer.getDmGrid().addTextColumn(txtHManufacturer.getDmGrid().getPropertyAccess().city(), "City", 100, "");
        txtHManufacturer.getDmGrid().addTextColumn(txtHManufacturer.getDmGrid().getPropertyAccess().address(), "Address", 100, "");
        txtHManufacturer.getDmGrid().initializeGrid(txtHManufacturer.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHManufacturer.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHDivision">
    interface TxtHDivision_PrpAcs extends KeyField_T_PrpAcs<TxtHDivision_Mdl> {

        @Editor.Path("CityName")
        ValueProvider<TxtHDivision_Mdl, String> cityName();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHDivision_Mdl, String> manualCode();

        @Editor.Path("Address")
        ValueProvider<TxtHDivision_Mdl, String> address();
    }
    final DmTextHelp<TxtHDivision_Mdl, TxtHDivision_PrpAcs> txtHDivision = new DmTextHelp(GWT.create(TxtHDivision_PrpAcs.class), 6, false);

    private void iniTxtHDivision() {
        txtHDivision.getDmGrid().addTextColumn(txtHDivision.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHDivision.getDmGrid().addTextColumn(txtHDivision.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "");
        txtHDivision.getDmGrid().addTextColumn(txtHDivision.getDmGrid().getPropertyAccess().cityName(), "City", 100, "");
        txtHDivision.getDmGrid().addTextColumn(txtHDivision.getDmGrid().getPropertyAccess().address(), "Address", 100, "");
        txtHDivision.getDmGrid().initializeGrid(txtHDivision.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHDivision.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Category")
        ValueProvider<Find_Mdl, String> category();

        @Editor.Path("MedGroup")
        ValueProvider<Find_Mdl, String> medGroup();

        @Editor.Path("Sku")
        ValueProvider<Find_Mdl, String> sku();

        @Editor.Path("ItemType")
        ValueProvider<Find_Mdl, String> itemType();

        @Editor.Path("Hsn")
        ValueProvider<Find_Mdl, String> hsn();

        @Editor.Path("PurchaseUnit")
        ValueProvider<Find_Mdl, String> purchaseUnit();

        @Editor.Path("SaleUnit")
        ValueProvider<Find_Mdl, String> saleUnit();

        @Editor.Path("Active")
        ValueProvider<Find_Mdl, String> active();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Item", 400, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().category(), "Category", 150, "categoryName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().medGroup(), "Item Group", 150, "groupName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().itemType(), "Item Type", 120, "itemTypeName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().hsn(), "HSN", 110, "hsName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().sku(), "SKU", 50, "sku");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().purchaseUnit(), "Purchase Unit", 80, "purchaseUnitName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().saleUnit(), "Sale Unit", 80, "saleUnitName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().active(), "Active", 100, "");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 1);
        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="TxtHActive">
    final DmTextHelp<KeyFieldExtended, dataman.dmBase.self.shared.KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(dataman.dmBase.self.shared.KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHActive() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>
//<editor-fold defaultstate="collapsed" desc="txtHCostCenter">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCostCenter = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, false);
    private void iniTxtHCostCenter() {
        txtHCostCenter.getDmGrid().addTextColumn(txtHCostCenter.getDmGrid().getPropertyAccess().name(), "Costcenter", 100, "Name");
        txtHCostCenter.getDmGrid().initializeGrid(txtHCostCenter.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHCostCenter.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="txtHBudgetHead">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBudgetHead = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, false);
    private void iniTxtHBudgetHead() {
        txtHBudgetHead.getDmGrid().addTextColumn(txtHBudgetHead.getDmGrid().getPropertyAccess().name(), "Costcenter", 100, "Name");
        txtHBudgetHead.getDmGrid().initializeGrid(txtHBudgetHead.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHBudgetHead.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>
    public ItemMaster_Dsg() {
        ini();
        iniTxtHActive();
        iniFind();
        iniOtherMenu();
        initxtHHSN();
        iniTxtHInventoryAc();
        iniTxtHPurchaseAc();
        iniTxtHPurchaseReturnAc();
        iniTxtHSalesAc();
        iniTxtHSalesReturnAc();
        iniTxtHGRIRAc();
        iniTxtHConsumptionAc();
        iniTxtHCOGSAc();
        iniTxtHPIDAc();
        iniTxtHInTransitAc();
        iniTxtHCategory();
        iniTxtHMaterialCategory();
        iniTxtHMaterialType();
        iniTxtHItemType();
        iniTxtHItemGroup();
        iniSKU();
        iniTxtHBestBefore();
        iniTxtHHighValue();
        iniTxtHFlowThrough();
        iniTxtHConsignmentItem();
        iniTxtHReturnable();
        iniTxtHBuildItem();
        initxtHPurchaseUnitSKU();
        initxtHSaleUnitSKU();
        iniTxtHProperties();
        iniTxtHAllowNegetiveStock();
        iniTxtHPoReq();
        iniTxtHIndentReq();
        iniTxtHQuotationReq();
        iniTxtHLabReq();
        iniTxtHItemGSTNature();
        iniTxtHTaxBasedOn();
        iniTxtHGstType();
        iniTxtHItemMake();
        iniTxtHNarcotics();
        iniTxtHItemSpeciality();
        iniMarketingCo();
        iniTxtHDivision();
        iniTxtHManufacturer();
        iniTxtHItemModel();
        iniTxtHItemStrength();
        iniTxtHCostCenter();
        iniTxtHBudgetHead();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, false);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 29) + "px");
        vrtInnerForm.add(vl.add(topControl));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea2());
        flw.add(tabArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");

        userControl.addTextBlog(preparedByBlog, 200, false, true);
        userControl.addTextBlog(modifiedByBlog, 200, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(statusChangedByBlog, 200, true, false);
        Anchor aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnActiveBy.addStyleName("dm-button-grad");
        btnActiveBy.getElement().appendChild(aBtn.getElement());
        btnActiveBy.setWidth("30px");
        userControl.addControlBlog(statusChangedByBlog, btnActiveBy);
        vrtInnerForm.add(userControl);

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea2() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();

        DmClient.setStyle(flwTable, "100%", "35px", null, null, DmPosition.CENTER_HORIZONTAL, true);
        flwTable.getElement().getStyle().setMarginLeft(40, Style.Unit.PX);


        /*====================================< 1st Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTR.getElement().getStyle().setMarginTop(10, Style.Unit.PX);

        flwTD = new FlowPanel();
        flwTD.getElement().getStyle().setMarginLeft(22, Style.Unit.PCT);
        DmClient.setStyle(flwTD, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "36%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        return flwTable;
    }

    private TabLayoutPanel tabArea() {

        DmClient.setStyle(tabPanel, "100%", "440px", null, null, DmPosition.NONE, true);
        tabIndex = 0;
        tabPanel.add(vl.add(workingArea()), "General");
        ++tabIndex;
        tabPanel.add(vl.add(tabItemDepartmentInfo()), "Stock Level");
        tabIndexAisala = ++tabIndex;
        tabPanel.add(vl.add(tabAISALA()), "<span class = 'gwt-Label' style='width: 50px ! important;'>AISALA</span>", true);
        ++tabIndex;
        tabPanel.add(vl.add(tabRestrictedDept()), "Restricted Department/Godown");
        ++tabIndex;
        tabPanel.add(vl.add(tabApprovedVendors()), "Approved Suppliers");
        ++tabIndex;
        tabPanel.add(vl.add(qcParameter()), "QC Parameter");
        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");
        return tabPanel;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "420px", null, null, DmPosition.NONE, true);

        //1st row
        flwTR = new FlowPanel();
        flwTR.getElement().setAttribute("style", "width: 100%; height: 215px; float: left; margin-top: 7px;");

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "70%", "250px", "left", null, DmPosition.NONE, false);
        flwTD.add(leftArea());
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", "250px", "left", null, DmPosition.NONE, false);
        flwTD.add(rightArea());
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;
        HorizontalPanel hrtPnl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "250px", null, null, DmPosition.CENTER, true);

        //2nd row
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Manual Code");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtManualCode));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Material Category");
        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "24%", null, "right", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHMaterialCategory));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*++++++++++++++++++++++++++++2And row++++++++++++++++++++++++++++*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Material Type");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHMaterialType));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Brand/Make");
        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "24%", null, "right", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHItemMake));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*==========================<5th row>*====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Model");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHItemModel));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Specification");
        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "24%", null, "right", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHItemStrength));
        flwTR.add(flwTD);

        //3rd row
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Type");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHItemType));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Category");
        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "24%", null, "right", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHCategory));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        //4th row
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("HSN/SAC");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHHSN));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GST Nature");
        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "24%", null, "right", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHItemGSTNature));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*==========================<6th row>*====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Group");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHItemGroup));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Marketing Co.");
        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "24%", null, "right", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHMarketingCo));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*==========================<7th row>*====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Manufacturer");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHManufacturer));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Division");
        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "24%", null, "right", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHDivision));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*==========================<8th row>*====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Speciality");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHItemSpeciality));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Narcotics Substances");
        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "24%", null, "right", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHNarcotics));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*==========================<9th row>*====================================*/
////////////////////////////////////////////////////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Applicable Property");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "70%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHProperties));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        //////////////////////////////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase A/c");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHPurchaseAc));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase Return A/c");
        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "23%", null, "right", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHPurchaseReturnAc));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        ///////////////////////////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sales A/c");
        //lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSalesAc));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sales Return A/c");
        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "23%", null, "right", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSalesReturnAc));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        ///////////////////////////

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Inventory A/c");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHInventoryAc));
        flwTR.add(flwTD);

//        ============================================================================================================================
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gr/Ir A/c");
        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "23%", null, "right", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHGRIRAc));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        ///////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consumption A/c");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHConsumptionAc));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("COGS A/c");
        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "23%", null, "right", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHCOGSAc));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        ///////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PID A/c");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHPIDAc));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Intransit A/c");
        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "23%", null, "right", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHInTransitAc));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        
        ///////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Costcenter");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHCostCenter));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Budget Head");
        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "23%", null, "right", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHBudgetHead));
        flwTR.add(flwTD);

        flwTable.add(flwTR);


        /*==========================<9th row>*====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consignment Item");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHConsignmentItem));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Best Before");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHBestBefore));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Flow Through");
        lbl.getElement().getStyle().setMarginLeft(24, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHFlowThrough));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*==========================<10th row>*====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("High Value");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHHighValue));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Returnable Item");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHReturnable));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Billed Item");
        lbl.getElement().getStyle().setMarginLeft(24, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHBilledItem));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*==========================<11th row>*====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Indent Required");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHIndentReq));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Quotation Required");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHQuotationReq));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PO Required");
        lbl.getElement().getStyle().setMarginLeft(24, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHPoReq));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*==========================<12th row>*====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Lab Required");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHLabReq));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Allow Negative Stock");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHAllowNegetiveStock));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Status");
        lbl.getElement().getStyle().setMarginLeft(24, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHActive));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;

    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTR, flwTD, flwTD1;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "250px", null, null, DmPosition.NONE, true);


        /*====================================< 1st Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("SKU");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "54%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSKU));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "16%", null, "left", null, DmPosition.NONE, false);
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================<2nd Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase Unit");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD1 = new FlowPanel();
        DmClient.setStyle(flwTD1, "70%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "40%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHPurchaseUnitSKU));
        flwTD1.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel(" = ");
        lbl.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        flwTD.add(lbl);
        flwTD1.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtConversionRatePurchase));
        flwTD1.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lblPurchaseConvertionRate.getElement().getStyle().setFloat(Style.Float.LEFT);
        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTD.add(lblPurchaseConvertionRate);
        flwTD1.add(flwTD);

        flwTR.add(flwTD1);

        flwTable.add(flwTR);


        /*====================================<4th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Unit");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD1 = new FlowPanel();
        DmClient.setStyle(flwTD1, "70%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "40%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSaleUnitSKU));
        flwTD1.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel(" = ");
        lbl.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        flwTD.add(lbl);
        flwTD1.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtConversionRateSale));
        flwTD1.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lblSaleConvertionRate.getElement().getStyle().setFloat(Style.Float.LEFT);
        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTD.add(lblSaleConvertionRate);
        flwTD1.add(flwTD);

        flwTR.add(flwTD1);

        flwTable.add(flwTR);

        /*====================================<4th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GST Based On");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "54%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHITSBasedOn));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================<grid Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        DmClient.setStyle(flwITSContainer, "100%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwGstType, "100%", null, "left", null, DmPosition.NONE, false);
        flwITSContainer.add(rateGrid());
        flwGstType.add(gstTypeText());
        flwTR.add(flwMain);

        flwTable.add(flwTR);


        /*====================================<5th Row>====================================*/
        return flwTable;
    }

    private FlowPanel rateGrid() {
        FlowPanel flwTable, flwTR;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "50px", null, null, DmPosition.NONE, true);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, null, null, DmPosition.NONE, false);
        flwTR.add((iniGrdRate()));
        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel gstTypeText() {
        FlowPanel flwTable, flwTR, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmPosition.NONE, true);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GST Type");
        flwTd.add(lbl);
        flwTR.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "54%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGstType));
        flwTR.add(flwTd);

        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel tabItemDepartmentInfo() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "200px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "200px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", "200px", "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(127, Style.Unit.PX);
        flwTd.add(iniItemDetails());
        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel tabAISALA() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "200px", null, null, DmPosition.CENTER, true);

        //======================= AlternativeItems Grid ===========================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "150px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", "200px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(iniGridAlternativeItems()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", "150px", "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("");
        flwTd.add(vl.add(lbl));
        flwTr.add(flwTd);

        //======================= sound alike and look alike Grid ===========================================
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", "200px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(iniGridSoundLookAlike()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel tabRestrictedDept() {
        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "40%", "200px", null, null, DmPosition.CENTER, true);
        flwTable.add(vl.add(iniGridRestrictedDept()));
        return flwTable;
    }

    private FlowPanel tabApprovedVendors() {

        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "45%", "200px", null, null, DmPosition.CENTER, true);
        flwTable.add(vl.add(iniGridApprovedVendors()));
        return flwTable;

    }

    private FlowPanel qcParameter() {

        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "45%", "200px", null, null, DmPosition.CENTER, true);
        flwTable.add(vl.add(iniGridQcParameter()));
        return flwTable;

    }

    private Menu iniOtherMenu() {
        menu.add(mnuManageSite);
        menu.add(mnuExportCard);
        return menu;
    }

    //<editor-fold defaultstate="collapsed" desc="grdItemDetails">
    interface GrdItemDetails_Mdl_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemDepartmentInfo_Mdl> {

        @Editor.Path("MinStock")
        ValueProvider<GrdItemDepartmentInfo_Mdl, String> minStock();

        @Editor.Path("MaxStock")
        ValueProvider<GrdItemDepartmentInfo_Mdl, String> maxStock();

        @Editor.Path("ReOrder")
        ValueProvider<GrdItemDepartmentInfo_Mdl, String> reOrder();

        @Editor.Path("BinName")
        ValueProvider<GrdItemDepartmentInfo_Mdl, String> binName();

        @Editor.Path("SiteName")
        ValueProvider<GrdItemDepartmentInfo_Mdl, String> siteName();

        @Editor.Path("AMConsumptionValue")
        ValueProvider<GrdItemDepartmentInfo_Mdl, String> aMConsumptionValue();

    }

    final DmGrid<GrdItemDepartmentInfo_Mdl, GrdItemDetails_Mdl_PrpAcs> grdItemDetails = new DmGrid(GWT.create(GrdItemDetails_Mdl_PrpAcs.class));

    private Grid iniItemDetails() {

        grdItemDetails.addTextColumn(grdItemDetails.getPropertyAccess().siteName(), "Site Name", 150, "");
        grdItemDetails.addTextColumn(grdItemDetails.getPropertyAccess().name(), "Godown", 220, "");
        grdItemDetails.addTextColumn(grdItemDetails.getPropertyAccess().minStock(), "Min. Level", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetails.addTextColumn(grdItemDetails.getPropertyAccess().maxStock(), "Max. Level", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetails.addTextColumn(grdItemDetails.getPropertyAccess().reOrder(), "Re-Order Level", 115, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetails.addTextColumn(grdItemDetails.getPropertyAccess().binName(), "Location", 80, "");
        grdItemDetails.addTextColumn(grdItemDetails.getPropertyAccess().aMConsumptionValue(), "Consumption", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetails.initializeGrid(grdItemDetails.getPropertyAccess().fieldHashCode(), 780, 200);

        return grdItemDetails.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GridAlternativeItems">
    interface GridAlternativeItems_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdAlternativeItems_Mdl> {

    }
    final DmGrid<GrdAlternativeItems_Mdl, GridAlternativeItems_PrpAcs> gridAlternativeItems = new DmGrid(GWT.create(GridAlternativeItems_PrpAcs.class));

    private Grid iniGridAlternativeItems() {
        gridAlternativeItems.addRowNumberColumn();
        gridAlternativeItems.addTextColumn(gridAlternativeItems.getPropertyAccess().name(), "Alternative Items", 420, "");
        gridAlternativeItems.initializeGrid(gridAlternativeItems.getPropertyAccess().fieldHashCode(), 600, 195, 1);
        gridAlternativeItems.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        gridAlternativeItems.getGrid().getView().setViewConfig(new GridViewConfig<GrdAlternativeItems_Mdl>() {
            @Override
            public String getColStyle(GrdAlternativeItems_Mdl model, ValueProvider<? super GrdAlternativeItems_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
//                if (DmShared.xNull(model.getIsStatus()).equals("1")) {
//                    styleClass += "dm-GridCellDairyCancel ";
//                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdAlternativeItems_Mdl model, int rowIndex) {
                return "";
            }
        });
        return gridAlternativeItems.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gridSoundLookAlike">
    interface GridSoundLookAlike_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdSoundLookAlike_Mdl> {

    }
    final DmGrid<GrdSoundLookAlike_Mdl, GridSoundLookAlike_PrpAcs> gridSoundLookAlike = new DmGrid(GWT.create(GridSoundLookAlike_PrpAcs.class));

    private Grid iniGridSoundLookAlike() {
        gridSoundLookAlike.addRowNumberColumn();
        gridSoundLookAlike.addTextColumn(gridSoundLookAlike.getPropertyAccess().name(), "Sound alike and Look alike", 420, "");
        gridSoundLookAlike.initializeGrid(gridSoundLookAlike.getPropertyAccess().fieldHashCode(), 600, 195, 1);
        gridSoundLookAlike.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        gridSoundLookAlike.getGrid().getView().setViewConfig(new GridViewConfig<GrdSoundLookAlike_Mdl>() {
            @Override
            public String getColStyle(GrdSoundLookAlike_Mdl model, ValueProvider<? super GrdSoundLookAlike_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";

                return styleClass;
            }

            @Override
            public String getRowStyle(GrdSoundLookAlike_Mdl model, int rowIndex) {
                return "";
            }
        });
        return gridSoundLookAlike.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GridRestrictedDept">
    interface GridRestrictedDept_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdRestrictedDept_Mdl> {

    }
    final DmGrid<GrdRestrictedDept_Mdl, GridRestrictedDept_PrpAcs> gridRestrictedDept = new DmGrid(GWT.create(GridRestrictedDept_PrpAcs.class));

    private Grid iniGridRestrictedDept() {
        gridRestrictedDept.addRowNumberColumn();
        gridRestrictedDept.addTextColumn(gridRestrictedDept.getPropertyAccess().name(), "Restricted Department/Godown", 420, "");
        gridRestrictedDept.initializeGrid(gridRestrictedDept.getPropertyAccess().fieldHashCode(), 500, 200);
        gridRestrictedDept.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
        return gridRestrictedDept.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gridApprovedVendors">
    interface GridApprovedVendors_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdApprovedVendors_Mdl> {

        @Editor.Path("Rate")
        ValueProvider<GrdApprovedVendors_Mdl, String> rate();

        @Editor.Path("UnitName")
        ValueProvider<GrdApprovedVendors_Mdl, String> unitName();

    }
    final DmGrid<GrdApprovedVendors_Mdl, GridApprovedVendors_PrpAcs> gridApprovedVendors = new DmGrid(GWT.create(GridApprovedVendors_PrpAcs.class));

    private Grid iniGridApprovedVendors() {
        gridApprovedVendors.addRowNumberColumn();
        gridApprovedVendors.addTextColumn(gridApprovedVendors.getPropertyAccess().name(), "Approved Suppliers", 300, "");
        gridApprovedVendors.addTextColumn(gridApprovedVendors.getPropertyAccess().rate(), "Rate", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridApprovedVendors.addTextColumn(gridApprovedVendors.getPropertyAccess().unitName(), "Unit", 100, "");
        gridApprovedVendors.initializeGrid(gridApprovedVendors.getPropertyAccess().fieldHashCode(), 500, 200, 1);
        gridApprovedVendors.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
        return gridApprovedVendors.getGrid();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="gridQcParameter">
    interface GridQcParameter_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdQcParameter_Mdl> {

    }
    final DmGrid<GrdQcParameter_Mdl, GridQcParameter_PrpAcs> gridQcParameter = new DmGrid(GWT.create(GridQcParameter_PrpAcs.class));

    private Grid iniGridQcParameter() {
        gridQcParameter.addRowNumberColumn();
        gridQcParameter.addTextColumn(gridQcParameter.getPropertyAccess().name(), "QC Parameter", 420, "");
        gridQcParameter.initializeGrid(gridQcParameter.getPropertyAccess().fieldHashCode(), 500, 200);
        gridQcParameter.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
        return gridQcParameter.getGrid();
    }

    //</editor-fold>
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
        grdItsRateSlab.addTextColumn(grdItsRateSlab.getPropertyAccess().rateFrom(), "From", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItsRateSlab.addTextColumn(grdItsRateSlab.getPropertyAccess().rateUpto(), "Upto", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItsRateSlab.addTextColumn(grdItsRateSlab.getPropertyAccess().gstTypeName(), "GST Type", 90, "");

        grdItsRateSlab.addBeforeInitializeHandler(new DmGrid.BeforeInitialize() {
            @Override
            public void beforeInitialize(ColumnModel columnModel) {
                columnModel.addHeaderGroup(0, 1, new HeaderGroupConfig("Rate", 1, 2));
            }
        });

        grdItsRateSlab.initializeGrid(grdItsRateSlab.getPropertyAccess().fieldHashCode(), 313, 150);
        return grdItsRateSlab.getGrid();
    }
    //</editor-fold>
}
