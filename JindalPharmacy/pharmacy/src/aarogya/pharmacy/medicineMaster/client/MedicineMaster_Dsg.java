package aarogya.pharmacy.medicineMaster.client;

import aarogya.pharmacy.medicineMaster.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class MedicineMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().MEDICINE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String attachFileBlog = "Attach Files";
    public final static String deleteFileBlog = "Delete Files";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final FlowPanel flwForm = new FlowPanel();
    final PopupPanel ppForm = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtManualCode = new DmTextBox(20, false);
    final DmSuggestBox txtDisplayName = new DmSuggestBox(100, 10, true, "Name", DmDataType.STRING);
    final DmTextBox txtItemGSTNature = new DmTextBox(250, false);
    final DmTextBox txtName = new DmTextBox(250, false);
    final DmTextBox txtPackSize = new DmTextBox(15, true);
    final DmTextBoxNumeric txtMinLvl = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtMaxLvl = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtReOrder = new DmTextBoxNumeric(2, false, 10, false);
    final DmRemarkTextBox txtInstructions = new DmRemarkTextBox(250, false);
    final DmRemarkTextBox txtRegionalInstructions = new DmRemarkTextBox(250, false);
    final DmTextBox txtBin = new DmTextBox(35, false);
    final DmTextBox txtITSOnPurchaseReturn = new DmTextBox(35, false);
    final DmTextBox txtITSOnSaleReturn = new DmTextBox(35, false);
    SelectionHandler<MenuItem> selectionHandler;
    final DmLabel lbldisplay = new DmLabel("");
    int tabIndex = 0, tabIndexAisala;

    final DmTextBoxNumeric txtConversionRatePurchase = new DmTextBoxNumeric(4, false, 10, true);
    final DmTextBoxNumeric txtConversionRateSale = new DmTextBoxNumeric(4, false, 10, true);

    final DmTextBoxNumeric txtMRP = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtSaleRate = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtPurchaseRate = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtSchemeOnQty = new DmTextBoxNumeric(4, false, 10, false);
    final DmTextBoxNumeric txtSchemeFreeQty = new DmTextBoxNumeric(4, false, 10, false);
    final DmTextBoxNumeric txtPurchaseDisPer = new DmTextBoxNumeric(2, false, 4, false);
    final DmTextBoxNumeric txtSaleDisPer = new DmTextBoxNumeric(2, false, 4, false);
    final DmTextBoxNumeric txtTaxPerc = new DmTextBoxNumeric(2, false, 4, true);
    final DmTextBoxNumeric txtPriority = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextBox txtResGroup = new DmTextBox(250, false);

    final DmLabel lblPurchaseConvertionRate = new DmLabel("");
    final DmLabel lblSaleConvertionRate = new DmLabel("");
    final DmLabel lblPurchaseUnitName = new DmLabel("");
    final DmLabel lblMRPUnitName = new DmLabel("");
    final DmLabel lblPurchaseRateUnitName = new DmLabel("");
    final DmLabel lblSaleRateUnitName = new DmLabel("");

    final Menu menu = new Menu();
    final MenuItem mnuManageSite = new MenuItem("Manage Site");
    final MenuItem mnuExportCard = new MenuItem("Export Data");
    final MenuItem mnuManageFile = new MenuItem("Manage File");

    //<editor-fold defaultstate="collapsed" desc="txtHSubCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSubCategory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHSubCategory() {
        txtHSubCategory.getDmGrid().addTextColumn(txtHSubCategory.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHSubCategory.getDmGrid().initializeGrid(txtHSubCategory.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHSubCategory.getHelper().addSort("name", DmSortType.ASC);
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
    //<editor-fold defaultstate="collapsed" desc="TxtHMedGroup">
    interface TxtHMedGroup_PrpAcs extends KeyField_T_PrpAcs<TxtHMedGrp_Mdl> {

    }
    final DmTextHelp<TxtHMedGrp_Mdl, TxtHMedGroup_PrpAcs> txtHMedGroup = new DmTextHelp(GWT.create(TxtHMedGroup_PrpAcs.class), 4, true);

    private void iniTxtHMedGroup() {
        txtHMedGroup.getDmGrid().addTextColumn(txtHMedGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHMedGroup.getDmGrid().initializeGrid(txtHMedGroup.getDmGrid().getPropertyAccess().code(), 275, 0, 1);

        txtHMedGroup.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //    //<editor-fold defaultstate="collapsed" desc="TxtHResGroup">
//    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHResGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4);
//
//    private void iniTxtHResGroup() {
//        txtHResGroup.getDmGrid().addTextColumn(txtHResGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
//        txtHResGroup.getDmGrid().initializeGrid(txtHResGroup.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
//
//        txtHResGroup.getHelper().addSort("NAME", DmSortType.ASC);
//    }
//
//    //</editor-fold>
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
    //<editor-fold defaultstate="collapsed" desc="TxtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHStatus = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHStatus() {
        txtHStatus.getDmGrid().addTextColumn(txtHStatus.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHStatus.getDmGrid().initializeGrid(txtHStatus.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHMedicineNature">
//    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMedicineNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);
//
//    private void iniTxtHMedicineNature() {
//        txtHMedicineNature.getDmGrid().addTextColumn(txtHMedicineNature.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
//        txtHMedicineNature.getDmGrid().initializeGrid(txtHMedicineNature.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
//    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHCompany">
    interface TxtHCompany_PrpAcs extends KeyField_T_PrpAcs<TxtHCompany_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHCompany_Mdl, String> shortName();
    }

    final DmTextHelp<TxtHCompany_Mdl, TxtHCompany_PrpAcs> txtHCompany = new DmTextHelp(GWT.create(TxtHCompany_PrpAcs.class), 7, true);

    private void iniTxtHCompany() {
        txtHCompany.getDmGrid().addTextColumn(txtHCompany.getDmGrid().getPropertyAccess().name(), "Name", 0, "companyName");
        txtHCompany.getDmGrid().addTextColumn(txtHCompany.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
        txtHCompany.getDmGrid().initializeGrid(txtHCompany.getDmGrid().getPropertyAccess().code(), 275, 0, 1);

        txtHCompany.getHelper().addSort("companyName", DmSortType.ASC);
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
    //<editor-fold defaultstate="collapsed" desc="TxtHConsignmentItem">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHConsignmentItem = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHConsignmentItem() {
        txtHConsignmentItem.getDmGrid().addTextColumn(txtHConsignmentItem.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHConsignmentItem.getDmGrid().initializeGrid(txtHConsignmentItem.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHHighValue">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHighValue = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHHighValue() {
        txtHHighValue.getDmGrid().addTextColumn(txtHHighValue.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHHighValue.getDmGrid().initializeGrid(txtHHighValue.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHFlowThrough">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHFlowThrough = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHFlowThrough() {
        txtHFlowThrough.getDmGrid().addTextColumn(txtHFlowThrough.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHFlowThrough.getDmGrid().initializeGrid(txtHFlowThrough.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHNarcotics">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNarcotics = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHNarcotics() {
        txtHNarcotics.getDmGrid().addTextColumn(txtHNarcotics.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHNarcotics.getDmGrid().initializeGrid(txtHNarcotics.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHBestBefore">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBestBefore = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHBestBefore() {
        txtHBestBefore.getDmGrid().addTextColumn(txtHBestBefore.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHBestBefore.getDmGrid().initializeGrid(txtHBestBefore.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHReturnable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReturnable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHReturnable() {
        txtHReturnable.getDmGrid().addTextColumn(txtHReturnable.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHReturnable.getDmGrid().initializeGrid(txtHReturnable.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHHSN">
    interface TxtHHSN_PrpAcs extends KeyField_T_PrpAcs<TxtHHSN_Mdl> {

        @Editor.Path("Hsn")
        ValueProvider<TxtHHSN_Mdl, String> hsn();

        @Editor.Path("ItemGSTNatureText")
        ValueProvider<TxtHHSN_Mdl, String> gstNature();
    }
    final DmTextHelp<TxtHHSN_Mdl, TxtHHSN_PrpAcs> txtHHSN = new DmTextHelp(GWT.create(TxtHHSN_PrpAcs.class), 6, true);

    private void iniTxtHHSN() {
        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().hsn(), "HSN / SAC", 80, "hsn");
        txtHHSN.getDmGrid().addTextColumn(txtHHSN.getDmGrid().getPropertyAccess().gstNature(), "GST Nature", 80, "");
        txtHHSN.getDmGrid().initializeGrid(txtHHSN.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHHSN.getHelper().addSort("name", DmSortType.ASC);
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
    //<editor-fold defaultstate="collapsed" desc="txtHBilledItem">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBilledItem = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHBuildItem() {
        txtHBilledItem.getDmGrid().addTextColumn(txtHBilledItem.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHBilledItem.getDmGrid().initializeGrid(txtHBilledItem.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHLifeSaving">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHLifeSaving = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHLifeSaving() {
        txtHLifeSaving.getDmGrid().addTextColumn(txtHLifeSaving.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHLifeSaving.getDmGrid().initializeGrid(txtHLifeSaving.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHPurchaseUnitSKU">
    interface Purchase_PrpAcs extends KeyField_T_PrpAcs<Sku_Mdl> {
    }
    final DmTextHelp<Sku_Mdl, Purchase_PrpAcs> txtHPurchaseUnitSKU = new DmTextHelp(GWT.create(Purchase_PrpAcs.class), 4, true);

    private void initxtHPurchaseUnitSKU() {
        txtHPurchaseUnitSKU.getDmGrid().addTextColumn(txtHPurchaseUnitSKU.getDmGrid().getPropertyAccess().name(), "Name", 0, "unit");
        txtHPurchaseUnitSKU.getDmGrid().initializeGrid(txtHPurchaseUnitSKU.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHPurchaseUnitSKU.getHelper().addSort("unit", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHSaleUnitSKU">
    interface Sale_PrpAcs extends KeyField_T_PrpAcs<Sku_Mdl> {
    }
    final DmTextHelp<Sku_Mdl, Sale_PrpAcs> txtHSaleUnitSKU = new DmTextHelp(GWT.create(Sale_PrpAcs.class), 4, true);

    private void initxtHSaleUnitSKU() {
        txtHSaleUnitSKU.getDmGrid().addTextColumn(txtHSaleUnitSKU.getDmGrid().getPropertyAccess().name(), "Name", 0, "unit");
        txtHSaleUnitSKU.getDmGrid().initializeGrid(txtHSaleUnitSKU.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHSaleUnitSKU.getHelper().addSort("unit", DmSortType.ASC);
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
    //<editor-fold defaultstate="collapsed" desc="txtHIsDiscountAllow">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsDiscountAllow = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHIsDiscountAllow() {
        txtHIsDiscountAllow.getDmGrid().addTextColumn(txtHIsDiscountAllow.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHIsDiscountAllow.getDmGrid().initializeGrid(txtHIsDiscountAllow.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHIsDiscountAllow.getHelper().addSort("Type", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="isSaleRateEditable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsSaleRateEditable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtIsSaleRateEditable() {
        txtHIsSaleRateEditable.getDmGrid().addTextColumn(txtHIsSaleRateEditable.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHIsSaleRateEditable.getDmGrid().initializeGrid(txtHIsSaleRateEditable.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHIsSaleRateEditable.getHelper().addSort("Type", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHIsIncludeFreeQtyInSaleRate">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsIncludeFreeQtyInSaleRate = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHIsIncludeFreeQtyInSaleRate() {
        txtHIsIncludeFreeQtyInSaleRate.getDmGrid().addTextColumn(txtHIsIncludeFreeQtyInSaleRate.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHIsIncludeFreeQtyInSaleRate.getDmGrid().initializeGrid(txtHIsIncludeFreeQtyInSaleRate.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHIsIncludeFreeQtyInSaleRate.getHelper().addSort("Type", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHIsCommonItem">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsCommonItem = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHIsCommonItem() {
        txtHIsCommonItem.getDmGrid().addTextColumn(txtHIsCommonItem.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHIsCommonItem.getDmGrid().initializeGrid(txtHIsCommonItem.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHIsCommonItem.getHelper().addSort("Type", DmEnum.DmSortType.ASC);
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

    //<editor-fold defaultstate="collapsed" desc="txtHMarginRateFix">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs1> txtHMarginRateFix = new DmTextHelp(GWT.create(KeyField_PrpAcs1.class), 3, true);

    private void iniTxtHMarginRateFix() {
        txtHMarginRateFix.getDmGrid().addTextColumn(txtHMarginRateFix.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHMarginRateFix.getDmGrid().initializeGrid(txtHMarginRateFix.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHMarginRateFix.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHQuotationReq">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHQuotationReq = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHQuotationReq() {
        txtHQuotationReq.getDmGrid().addTextColumn(txtHQuotationReq.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHQuotationReq.getDmGrid().initializeGrid(txtHQuotationReq.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHIsBarcodeActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsBarcodeActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHIsBarcodeActive() {
        txtHIsBarcodeActive.getDmGrid().addTextColumn(txtHIsBarcodeActive.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHIsBarcodeActive.getDmGrid().initializeGrid(txtHIsBarcodeActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHIsBarcodeActive.getHelper().addSort("Type", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Category")
        ValueProvider<Find_Mdl, String> category();

        @Editor.Path("ManualCode")
        ValueProvider<Find_Mdl, String> manualCode();

        @Editor.Path("Company")
        ValueProvider<Find_Mdl, String> company();

        @Editor.Path("Composition")
        ValueProvider<Find_Mdl, String> composition();

        @Editor.Path("MedGroup")
        ValueProvider<Find_Mdl, String> medGroup();

        @Editor.Path("RestrictGroup")
        ValueProvider<Find_Mdl, String> restrictGroup();

        @Editor.Path("Sku")
        ValueProvider<Find_Mdl, String> sku();

        @Editor.Path("Active")
        ValueProvider<Find_Mdl, String> active();

        @Editor.Path("MedicinePacking")
        ValueProvider<Find_Mdl, String> medicinePacking();

        @Editor.Path("ModifiedOn")
        ValueProvider<Find_Mdl, String> medifiedOn();

        @Editor.Path("MedicineSubCategory")
        ValueProvider<Find_Mdl, String> medicineSubCategory();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 25, true);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Medicine", 200, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().category(), "Category", 80, "categoryName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().medicineSubCategory(), "Sub Category", 80, "subCategoryName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().company(), "Medicine Company", 100, "companyName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().composition(), "Composition", 150, "compositionName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().medGroup(), "Medicine Group", 100, "groupName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().restrictGroup(), "Res. Group", 80, "resName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().sku(), "SKU", 50, "sku");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().medicinePacking(), "Packing", 80, "itemPacking");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().active(), "Active", 50, "activeText");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().medifiedOn(), "Modified On", 80, "modifiedDate", DmDataType.DATE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);
        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHITSOnPurchase">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHITSOnPurchase = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHITSOnPurchase() {
        txtHITSOnPurchase.getDmGrid().addTextColumn(txtHITSOnPurchase.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHITSOnPurchase.getDmGrid().initializeGrid(txtHITSOnPurchase.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHITSOnPurchase.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHITSOnSale">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHITSOnSale = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHITSOnSale() {
        txtHITSOnSale.getDmGrid().addTextColumn(txtHITSOnSale.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHITSOnSale.getDmGrid().initializeGrid(txtHITSOnSale.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHITSOnSale.getHelper().addSort("NAME", DmSortType.ASC);
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
    public MedicineMaster_Dsg() {
        ini();
        iniOtherMenu();
        iniTxtHCategory();
        iniTxtHMaterialCategory();
        iniTxtHCompany();
        iniTxtHStatus();
        iniTxtHManufacturer();
        iniTxtHMedGroup();

        iniTxtHIsDiscountAllow();
        iniSKU();
        iniTxtHDivision();
        iniTxtHHSN();
        iniTxtHIsCommonItem();
        iniTxtHBestBefore();
        iniTxtHHighValue();
        iniTxtHFlowThrough();
        iniTxtHNarcotics();
        iniTxtHConsignmentItem();
        iniTxtHReturnable();
        iniTxtHLifeSaving();
        iniTxtHBuildItem();
        initxtHPurchaseUnitSKU();
        initxtHSaleUnitSKU();
        iniTxtHAllowNegetiveStock();
        iniTxtIsSaleRateEditable();
        iniTxtHIsIncludeFreeQtyInSaleRate();
        iniTxtHPoReq();
        iniTxtHIndentReq();
        iniTxtHQuotationReq();
        iniTxtHIsBarcodeActive();
        iniTxtHITSOnPurchase();
        iniTxtHITSOnSale();
        iniTxtHSaleAC();
        iniTxtHSaleReturnAC();
        iniTxtHPurchaseAC();
        iniTxtHPurchaseReturnAC();
        iniTxtHInventoryAc();
        iniTxtHItemModel();
        iniTxtHItemStrength();
        iniTxtHSubCategory();
        iniTxtHMarginRateFix();

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
        flw.add(workingArea2());
        flw.add(tabArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);
        vl.add(txtITSOnPurchaseReturn);
        vl.add(txtITSOnSaleReturn);
        flwForm.add(vrtInnerForm);
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "521px", null, null, DmPosition.NONE, true);
        tabIndex = 0;
        tabPanel.add(vl.add(workingArea()), "General");
        ++tabIndex;
//        tabPanel.add(vl.add(compositionArea()), "Composition");
//        ++tabIndex;
        tabPanel.add(vl.add(tabMedicineDepartmentInfo()), "Department Info");
        tabIndexAisala = ++tabIndex;
        tabPanel.add(vl.add(tabAISALA()), "<span class = 'gwt-Label' style='width: 50px ! important;'>AISALA</span>", true);
        ++tabIndex;
        tabPanel.add(vl.add(tabRestrictedDept()), "Restricted Department");
        ++tabIndex;
        tabPanel.add(vl.add(tabApprovedVendors()), "Approved Suppliers");
        ++tabIndex;
        tabPanel.add(vl.add(tabSchemeDetails()), "Sale Scheme Details");
        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");
        return tabPanel;
    }

    private FlowPanel workingArea2() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();

        DmClient.setStyle(flwTable, "100%", "35px", null, null, DmPosition.CENTER_HORIZONTAL, true);
        flwTable.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);


        /*====================================< 1st Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTR.getElement().getStyle().setMarginTop(10, Style.Unit.PX);

//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Display Name");
//        lbl.getElement().getStyle().setMarginLeft(22, Style.Unit.PCT);
//        flwTD.add(lbl);
//        flwTR.add(flwTD);
//
//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "37%", null, "left", null, DmPosition.NONE, false);
//        flwTD.add(vl.add(txtDisplayName));
//
//        flwTR.add(flwTD);
//        flwTable.add(flwTR);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "6%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTD.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(22, Style.Unit.PCT);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "40%", null, "left", null, DmPosition.NONE, false);
        lbl.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
        flwTD.add(vl.add(txtName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "490px", null, null, DmPosition.CENTER, true);

        //Medicine Name
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "490px", "left", null, DmPosition.CENTER, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "70%", "490px", "left", null, DmPosition.NONE, false);
        flwTD.add(leftArea());
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", "490px", "left", null, DmPosition.NONE, false);
        flwTD.add(rightArea());
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "490px", null, null, DmPosition.CENTER_HORIZONTAL, true);

        /*====================================< 1st Row >====================================*/
//        flwTR = new FlowPanel();
//        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Display Name");
//        flwTD.add(lbl);
//        flwTR.add(flwTD);
//
//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
//        flwTD.add(vl.add(txtDisplayName));
//        flwTR.add(flwTD);
//        flwTable.add(flwTR);

        /*====================================< 2nd Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Manual Code");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtManualCode));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Display Name");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtDisplayName));

        flwTR.add(flwTD);
        flwTable.add(flwTR);

//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Category");
//        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
//        flwTD.add(lbl);
//        flwTR.add(flwTD);
//
//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
//        flwTD.add(vl.add(txtHCategory));
//        flwTR.add(flwTD);
//        flwTable.add(flwTR);

        /*====================================< 3rd Row >====================================*/
//        flwTR = new FlowPanel();
//        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Name");
//        flwTD.add(lbl);
//        flwTR.add(flwTD);
//
//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
//        flwTD.add(vl.add(txtName));
//        flwTR.add(flwTD);
//        flwTable.add(flwTR);

        /*====================================< 4Ath Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Material Category");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHMaterialCategory));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Model");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHItemModel));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*====================================< 4Cth Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Strength");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHItemStrength));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Category");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHCategory));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        /*====================================< 4Bth Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Group");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHMedGroup));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("HSN");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHHSN));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        //////////////////////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GST Nature");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtItemGSTNature));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase ITS");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHITSOnPurchase));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*====================================< 4th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale ITS");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHITSOnSale));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase A/c");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHPurchaseAC));
        flwTR.add(flwTD);

//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("S. Ret. ITS");
//        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
//        flwTD.add(lbl);
//        flwTR.add(flwTD);
//
//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
//        flwTD.add(vl.add(txtHITSOnSaleReturn));
//        flwTR.add(flwTD);
        flwTable.add(flwTR);
        /*====================================< 4th Row >====================================*/
//        flwTR = new FlowPanel();
//        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
//
////        flwTD = new FlowPanel();
////        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
////        lbl = new DmLabel("P. Ret. ITS");
////        flwTD.add(lbl);
////        flwTR.add(flwTD);
////

////        flwTD = new FlowPanel();
////        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
////        flwTD.add(vl.add(txtHITSOnPurchaseReturn));
////        flwTR.add(flwTD);
//
//        
//
//        flwTable.add(flwTR);
        /*====================================< 4th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale A/c");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSaleAC));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("P.Ret. A/c");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHPurchaseReturnAC));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*====================================< 4th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("S.Ret. A/c");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSaleReturnAC));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Company");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHCompany));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        /*====================================< 5th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Manufacturer");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHManufacturer));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Instructions");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtInstructions));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Regional Instructions");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtRegionalInstructions));
        txtRegionalInstructions.addStyleName(DmEnum.RegionalLanguage.getValueFrom(DmClient.getRegionalLanguage()).getCssStyleName());
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        /*====================================< 6th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Division");

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "40%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHDivision));
        txtResGroup.setEnabled(false);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Res. Group");
        lbl.getElement().getStyle().setMarginLeft(22, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtResGroup));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        /*====================================< 8th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);

        lbl = new DmLabel("Narcotics & control Substances");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHNarcotics));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consignment Item");
        lbl.getElement().getStyle().setMarginLeft(23, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHConsignmentItem));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Returnable Item");
        lbl.getElement().getStyle().setMarginLeft(22, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHReturnable));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 9th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Include Free Qty. In Sale Rate");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHIsIncludeFreeQtyInSaleRate));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Best Before");
        lbl.getElement().getStyle().setMarginLeft(23, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHBestBefore));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Discount Allow");
        lbl.getElement().getStyle().setMarginLeft(22, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHIsDiscountAllow));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*==========================<10th row>*====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("High Value");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHHighValue));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Billed Item");
        lbl.getElement().getStyle().setMarginLeft(23, Style.Unit.PCT);

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHBilledItem));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Rate Editable");
        lbl.getElement().getStyle().setMarginLeft(22, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHIsSaleRateEditable));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*==========================<11th row>*====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Allow Negative Stock");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHAllowNegetiveStock));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Flow Through");
        lbl.getElement().getStyle().setMarginLeft(23, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHFlowThrough));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Life Saving");
        lbl.getElement().getStyle().setMarginLeft(22, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHLifeSaving));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*==========================<12th row>*====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PO Required");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHPoReq));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Indent Required");
        lbl.getElement().getStyle().setMarginLeft(23, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHIndentReq));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Quotation Required");
        lbl.getElement().getStyle().setMarginLeft(22, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHQuotationReq));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        //---------------------------------------------------------------------------
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.setStyleName("dm-StripLabel");
        lbl = new DmLabel("COMPOSITION");
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(12, Style.Unit.PX);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        //---------------------------------------------------------------------------

        ///////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "98%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(iniCompositionDetails());
        flwTR.add(flwTD);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTR, flwTD, flwTD1;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "214px", null, null, DmPosition.NONE, true);

        /*====================================< Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Barcode Active");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "54%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHIsBarcodeActive));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "16%", null, "left", null, DmPosition.NONE, false);
        flwTR.add(flwTD);

        flwTable.add(flwTR);

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
        lbl.getElement().getStyle().setMarginLeft(4, Style.Unit.PX);
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
        lbl.getElement().getStyle().setMarginLeft(4, Style.Unit.PX);
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

        /*====================================< 5th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Packing");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "54%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPackSize));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "16%", null, "left", null, DmPosition.NONE, false);
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 5th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "63%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("MRP");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "21%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtMRP));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "16%", null, "left", null, DmPosition.NONE, false);
        lblMRPUnitName.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lblMRPUnitName);
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 5th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "63%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Rate");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "21%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtSaleRate));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "16%", null, "left", null, DmPosition.NONE, false);
        lblSaleRateUnitName.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lblSaleRateUnitName);
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 5th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "63%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase Rate");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "21%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPurchaseRate));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "16%", null, "left", null, DmPosition.NONE, false);
        lblPurchaseRateUnitName.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lblPurchaseRateUnitName);
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 5th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "63%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase Discount");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "21%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPurchaseDisPer));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel(" %");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 5th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "63%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Discount");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "21%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtSaleDisPer));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel(" %");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*====================================< 5th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "63%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Total Tax %");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "21%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtTaxPerc));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel(" %");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*====================================< 5th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "63%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Priority");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "21%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPriority));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================<2nd Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Scheme");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD1 = new FlowPanel();
        DmClient.setStyle(flwTD1, "70%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "40%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtSchemeOnQty));
        flwTD1.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel(" + ");
        lbl.getElement().getStyle().setMarginLeft(4, Style.Unit.PX);
        flwTD.add(lbl);
        flwTD1.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtSchemeFreeQty));
        flwTD1.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lblPurchaseConvertionRate.getElement().getStyle().setFloat(Style.Float.LEFT);
        flwTD.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTD.add(lblPurchaseUnitName);
        flwTD1.add(flwTD);

        flwTR.add(flwTD1);

        flwTable.add(flwTR);

        /*====================================< 5th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Common");
        lbl.setTitle("Common medicine for Pharmacy and Inventory");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "54%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHIsCommonItem));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "16%", null, "left", null, DmPosition.NONE, false);
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================<6th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "54%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(txtHStatus);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "16%", null, "left", null, DmPosition.NONE, false);
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sub Category");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "54%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSubCategory));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate / Margin Fix");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "54%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHMarginRateFix));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel tabMedicineDepartmentInfo() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "60%", "250px", null, null, DmPosition.CENTER, true);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "250px", "left", null, DmPosition.NONE, true);
//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("");
//        flwTD.add(lbl);
//        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", "250px", "left", null, DmPosition.NONE, false);
        flwTD.add(iniMedDetails());
        flwTR.add(flwTD);
//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("");
//        flwTD.add(lbl);
//        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;

    }

    private FlowPanel tabAISALA() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "250px", null, null, DmPosition.CENTER, true);

        //======================= AlternativeItems Grid ===========================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "250px", "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "48%", "250px", "left", null, DmPosition.NONE, true);
        flwTD.add(vl.add(iniGridAlternativeItems()));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "4%", "250px", "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("");
        flwTD.add(vl.add(lbl));
        flwTR.add(flwTD);

        //======================= sound alike and look alike Grid ===========================================
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "48%", "250px", "left", null, DmPosition.NONE, true);
        flwTD.add(vl.add(iniGridSoundLookAlike()));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;

    }

    private FlowPanel tabRestrictedDept() {
        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "45%", "250px", null, null, DmPosition.CENTER, true);
        flwTable.add(vl.add(iniGridRestrictedDept()));
        return flwTable;
    }

    private FlowPanel tabApprovedVendors() {

        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "45%", "250px", null, null, DmPosition.CENTER, true);
        flwTable.add(vl.add(iniGridApprovedVendors()));
        return flwTable;

    }

    private FlowPanel tabSchemeDetails() {

        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "78%", "250px", null, null, DmPosition.CENTER, true);
        flwTable.add(vl.add(iniSchemeDetailsGrid()));
        return flwTable;

    }

    //<editor-fold defaultstate="collapsed" desc="grdSchemeDetail">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdSchemeDetails_Mdl> {

        @Editor.Path("SchemeOnQty")
        ValueProvider<GrdSchemeDetails_Mdl, String> schemeOnQty();

        @Editor.Path("SchemeFreeQty")
        ValueProvider<GrdSchemeDetails_Mdl, String> schemeFreeQty();

        @Editor.Path("FreeItemUnitName")
        ValueProvider<GrdSchemeDetails_Mdl, String> freeItemUnitName();

        @Editor.Path("Description")
        ValueProvider<GrdSchemeDetails_Mdl, String> description();

    }

    final DmGrid<GrdSchemeDetails_Mdl, Grd_PrpAcs> grdSchemeDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid iniSchemeDetailsGrid() {
        grdSchemeDetail.addRowNumberColumn();
        grdSchemeDetail.addTextColumn(grdSchemeDetail.getPropertyAccess().name(), "Medicine Name", 350, "");
        grdSchemeDetail.addTextColumn(grdSchemeDetail.getPropertyAccess().freeItemUnitName(), "Unit", 100, "");
        grdSchemeDetail.addTextColumn(grdSchemeDetail.getPropertyAccess().schemeOnQty(), "Scheme On", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdSchemeDetail.addTextColumn(grdSchemeDetail.getPropertyAccess().schemeFreeQty(), "Scheme Free", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdSchemeDetail.addTextColumn(grdSchemeDetail.getPropertyAccess().description(), "Description", 270, "");
        grdSchemeDetail.initializeGrid(grdSchemeDetail.getPropertyAccess().fieldHashCode(), 950, 250);

        return grdSchemeDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdItemDetails">
    interface GrdMedDetails_Mdl_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMedicineDepartmentInfo_Mdl> {

        @Editor.Path("MinStock")
        ValueProvider<GrdMedicineDepartmentInfo_Mdl, String> minStock();

        @Editor.Path("MaxStock")
        ValueProvider<GrdMedicineDepartmentInfo_Mdl, String> maxStock();

        @Editor.Path("ReOrder")
        ValueProvider<GrdMedicineDepartmentInfo_Mdl, String> reOrder();

        @Editor.Path("BinName")
        ValueProvider<GrdMedicineDepartmentInfo_Mdl, String> binName();

        @Editor.Path("SiteName")
        ValueProvider<GrdMedicineDepartmentInfo_Mdl, String> siteName();

    }

    final DmGrid<GrdMedicineDepartmentInfo_Mdl, GrdMedDetails_Mdl_PrpAcs> grdMedDetails = new DmGrid(GWT.create(GrdMedDetails_Mdl_PrpAcs.class));

    private Grid iniMedDetails() {

        grdMedDetails.addTextColumn(grdMedDetails.getPropertyAccess().name(), "Department", 220, "");
        grdMedDetails.addTextColumn(grdMedDetails.getPropertyAccess().siteName(), "Site Name", 180, "");
        grdMedDetails.addTextColumn(grdMedDetails.getPropertyAccess().minStock(), "Min. Level", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMedDetails.addTextColumn(grdMedDetails.getPropertyAccess().maxStock(), "Max. Level", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMedDetails.addTextColumn(grdMedDetails.getPropertyAccess().reOrder(), "Re-Order Level", 118, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMedDetails.addTextColumn(grdMedDetails.getPropertyAccess().binName(), "Bin", 90, "");
        grdMedDetails.initializeGrid(grdMedDetails.getPropertyAccess().fieldHashCode(), 764, 250);

        return grdMedDetails.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdCompositionDetails">
    interface GrdCompositionDetails_Mdl_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdComposition_Mdl> {

        @Editor.Path("Power")
        ValueProvider<GrdComposition_Mdl, String> power();

        @Editor.Path("BaseText")
        ValueProvider<GrdComposition_Mdl, String> baseText();

        @Editor.Path("UnitText")
        ValueProvider<GrdComposition_Mdl, String> unitText();

    }

    final DmGrid<GrdComposition_Mdl, GrdCompositionDetails_Mdl_PrpAcs> grdCompositionDetails = new DmGrid(GWT.create(GrdCompositionDetails_Mdl_PrpAcs.class));

    private Grid iniCompositionDetails() {
        grdCompositionDetails.addRowNumberColumn();
        grdCompositionDetails.addTextColumn(grdCompositionDetails.getPropertyAccess().name(), "Composition", 525, "");
        grdCompositionDetails.addTextColumn(grdCompositionDetails.getPropertyAccess().power(), "Power", 90, "");
        grdCompositionDetails.addTextColumn(grdCompositionDetails.getPropertyAccess().unitText(), "Unit", 90, "");
        grdCompositionDetails.addTextColumn(grdCompositionDetails.getPropertyAccess().baseText(), "Base Composition", 110, "");
        grdCompositionDetails.initializeGrid(grdCompositionDetails.getPropertyAccess().fieldHashCode(), 830, 85);

        return grdCompositionDetails.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GridAlternativeItems">
    interface GridAlternativeItems_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdAlternativeItems_Mdl> {

    }
    final DmGrid<GrdAlternativeItems_Mdl, GridAlternativeItems_PrpAcs> gridAlternativeItems = new DmGrid(GWT.create(GridAlternativeItems_PrpAcs.class));

    private Grid iniGridAlternativeItems() {
        gridAlternativeItems.addRowNumberColumn();
        gridAlternativeItems.addTextColumn(gridAlternativeItems.getPropertyAccess().name(), "Alternative Items", 480, "");
        gridAlternativeItems.initializeGrid(gridAlternativeItems.getPropertyAccess().fieldHashCode(), 500, 250, 1);
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
        gridSoundLookAlike.addTextColumn(gridSoundLookAlike.getPropertyAccess().name(), "Sound alike and Look alike", 480, "");
        gridSoundLookAlike.initializeGrid(gridSoundLookAlike.getPropertyAccess().fieldHashCode(), 500, 250, 1);
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
        gridRestrictedDept.addTextColumn(gridRestrictedDept.getPropertyAccess().name(), "Restricted Department", 530, "");
        gridRestrictedDept.initializeGrid(gridRestrictedDept.getPropertyAccess().fieldHashCode(), 550, 250, 1);
        gridRestrictedDept.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        gridRestrictedDept.getGrid().getView().setViewConfig(new GridViewConfig<GrdRestrictedDept_Mdl>() {
            @Override
            public String getColStyle(GrdRestrictedDept_Mdl model, ValueProvider<? super GrdRestrictedDept_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
//                if (DmShared.xNull(model.getIsStatus()).equals("1")) {
//                    styleClass += "dm-GridCellDairyCancel ";
//                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdRestrictedDept_Mdl model, int rowIndex) {
                return "";
            }
        });
        return gridRestrictedDept.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gridApprovedVendors">
    interface GridApprovedVendors_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdApprovedVendors_Mdl> {

    }
    final DmGrid<GrdApprovedVendors_Mdl, GridApprovedVendors_PrpAcs> gridApprovedVendors = new DmGrid(GWT.create(GridApprovedVendors_PrpAcs.class));

    private Grid iniGridApprovedVendors() {
        gridApprovedVendors.addRowNumberColumn();
        gridApprovedVendors.addTextColumn(gridApprovedVendors.getPropertyAccess().name(), "Approved Suppliers", 520, "");
        gridApprovedVendors.initializeGrid(gridApprovedVendors.getPropertyAccess().fieldHashCode(), 550, 250, 1);
        gridApprovedVendors.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        gridApprovedVendors.getGrid().getView().setViewConfig(new GridViewConfig<GrdApprovedVendors_Mdl>() {
            @Override
            public String getColStyle(GrdApprovedVendors_Mdl model, ValueProvider<? super GrdApprovedVendors_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
//                if (DmShared.xNull(model.getIsStatus()).equals("1")) {
//                    styleClass += "dm-GridCellDairyCancel ";
//                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdApprovedVendors_Mdl model, int rowIndex) {
                return "";
            }
        });
        return gridApprovedVendors.getGrid();
    }

    //</editor-fold>
    private Menu iniOtherMenu() {
        menu.add(mnuManageSite);
        menu.add(mnuExportCard);
        menu.add(mnuManageFile);
        return menu;
    }
}
