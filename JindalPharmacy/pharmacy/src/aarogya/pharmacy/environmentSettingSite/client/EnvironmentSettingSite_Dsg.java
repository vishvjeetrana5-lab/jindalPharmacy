package aarogya.pharmacy.environmentSettingSite.client;

import aarogya.pharmacy.environmentSettingSite.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class EnvironmentSettingSite_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().ENVIRONMENT_SETTING.caption;
    public final static String modifiedByBlog = "Modified By & Date";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final Button btnSave = new Button();
    final Button btnCopy = new Button();
    final Button btnClose = new Button();
    final Button btnDelete = new Button();
    DmFileUploadButton btnUpload;
    DmFileRuleType fileRuleType;
    final DmToolContainer toolContainer = new DmToolContainer();
    final DmTextBox txtUploadStatus = new DmTextBox();
    final DmTextBoxNumeric txtExpiryAlertDays = new DmTextBoxNumeric(0, false, 2, false);
    final Image imgView = new Image();
    final DmLabel progressLbl = new DmLabel("");
    final FlowPanel flwImage = new FlowPanel();
    final DmTextBoxNumeric txtReturnDays = new DmTextBoxNumeric(0, false, 3, false);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    DmFileRuleType fileRuleTypeDocument;

    //<editor-fold defaultstate="collapsed" desc="txtHSiteCopy">
    final DmHelpGrid<KeyFieldExtended, KeyField_PrpAcs> txtHSiteCopy = new DmHelpGrid(GWT.create(KeyField_PrpAcs.class), 3);

    private void iniSiteCopy() {
        txtHSiteCopy.getDmGrid().addTextColumn(txtHSiteCopy.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSiteCopy.getDmGrid().initializeGrid(txtHSiteCopy.getDmGrid().getPropertyAccess().code(), 120, 0, 1);

        txtHSiteCopy.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 500, 0, 1);

        txtHSite.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPatAlphaCase">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPatAlphaCase = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniPatAlphaCase() {
        txtHPatAlphaCase.getDmGrid().addTextColumn(txtHPatAlphaCase.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPatAlphaCase.getDmGrid().initializeGrid(txtHPatAlphaCase.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAddAlphaCase">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAddAlphaCase = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniPAddAlphaCase() {
        txtHAddAlphaCase.getDmGrid().addTextColumn(txtHAddAlphaCase.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHAddAlphaCase.getDmGrid().initializeGrid(txtHAddAlphaCase.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHPharmacyControlAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPharmacyControlAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHPharmacyControlAc() {
        txtHPharmacyControlAc.getDmGrid().addTextColumn(txtHPharmacyControlAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPharmacyControlAc.getDmGrid().initializeGrid(txtHPharmacyControlAc.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHPharmacyControlAc.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPharmacyHospitalAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPharmacyHospitalAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHPharmacyHospitalAc() {
        txtHPharmacyHospitalAc.getDmGrid().addTextColumn(txtHPharmacyHospitalAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPharmacyHospitalAc.getDmGrid().initializeGrid(txtHPharmacyHospitalAc.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHPharmacyHospitalAc.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPharmacySelfAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPharmacySelfAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHPharmacySelfAc() {
        txtHPharmacySelfAc.getDmGrid().addTextColumn(txtHPharmacySelfAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPharmacySelfAc.getDmGrid().initializeGrid(txtHPharmacySelfAc.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHPharmacySelfAc.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPharmacyFinalDiscountAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPharmacyFinalDiscountAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHPharmacyFinalDiscountAc() {
        txtHPharmacyFinalDiscountAc.getDmGrid().addTextColumn(txtHPharmacyFinalDiscountAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPharmacyFinalDiscountAc.getDmGrid().initializeGrid(txtHPharmacyFinalDiscountAc.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHPharmacyFinalDiscountAc.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPharmacyFinalDiscountOH">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPharmacyFinalDiscountOH = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHPharmacyFinalDiscountOH() {
        txtHPharmacyFinalDiscountOH.getDmGrid().addTextColumn(txtHPharmacyFinalDiscountOH.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPharmacyFinalDiscountOH.getDmGrid().initializeGrid(txtHPharmacyFinalDiscountOH.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHPharmacyFinalDiscountOH.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAddress">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAddress = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHAddress() {
        txtHAddress.getDmGrid().addTextColumn(txtHAddress.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHAddress.getDmGrid().initializeGrid(txtHAddress.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHAddress.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedicineAlphaCase">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMedicineAlphaCase = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniMedicineAlphaCase() {
        txtHMedicineAlphaCase.getDmGrid().addTextColumn(txtHMedicineAlphaCase.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHMedicineAlphaCase.getDmGrid().initializeGrid(txtHMedicineAlphaCase.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedicineSaleOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMedicineSaleOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHMedicineSaleOn() {
        txtHMedicineSaleOn.getDmGrid().addTextColumn(txtHMedicineSaleOn.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHMedicineSaleOn.getDmGrid().initializeGrid(txtHMedicineSaleOn.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPHAllowPatientForSite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHPHAllowPatientForSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6);

    private void initxtHPHAllowPatientForSite() {
        txtHPHAllowPatientForSite.getDmGrid().addTextColumn(txtHPHAllowPatientForSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPHAllowPatientForSite.getDmGrid().initializeGrid(txtHPHAllowPatientForSite.getDmGrid().getPropertyAccess().code(), 300, 0, 2);

        txtHPHAllowPatientForSite.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPHAllowSaleBillForSite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHPHAllowSaleBillForSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6);

    private void iniTxtHPHAllowSaleBillForSite() {
        txtHPHAllowSaleBillForSite.getDmGrid().addTextColumn(txtHPHAllowSaleBillForSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPHAllowSaleBillForSite.getDmGrid().initializeGrid(txtHPHAllowSaleBillForSite.getDmGrid().getPropertyAccess().code(), 300, 0, 2);

        txtHPHAllowSaleBillForSite.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDefaultService">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDefaultService = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 8, false);

    private void initxtHDefaultService() {
        txtHDefaultService.getDmGrid().addTextColumn(txtHDefaultService.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDefaultService.getDmGrid().initializeGrid(txtHDefaultService.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDefaultService.getHelper().addSort("name", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAllowReturnWithooutSale">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAllowReturnWithooutSale = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 8, false);

    private void iniTxtHAllowReturnWithooutSale() {
        txtHAllowReturnWithooutSale.getDmGrid().addTextColumn(txtHAllowReturnWithooutSale.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHAllowReturnWithooutSale.getDmGrid().initializeGrid(txtHAllowReturnWithooutSale.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHAllowReturnWithooutSale.getHelper().addSort("name", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIsIncludeTaxInCost">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsIncludeTaxInCost = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, false);

    private void iniTxtHIsIncludeTaxInCost() {
        txtHIsIncludeTaxInCost.getDmGrid().addTextColumn(txtHIsIncludeTaxInCost.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHIsIncludeTaxInCost.getDmGrid().initializeGrid(txtHIsIncludeTaxInCost.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHIsIncludeTaxInCost.getHelper().addSort("name", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPharmacyBarcodeActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPharmacyBarcodeActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 8, false);

    private void iniTxtHBarcodeActive() {
        txtHPharmacyBarcodeActive.getDmGrid().addTextColumn(txtHPharmacyBarcodeActive.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPharmacyBarcodeActive.getDmGrid().initializeGrid(txtHPharmacyBarcodeActive.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHPharmacyBarcodeActive.getHelper().addSort("name", DmSortType.ASC);

    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHPharmacyBarcodeActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSRateEditInPurchase = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 8, false);

    private void iniTxtHSRateEditInPurchase() {
        txtHSRateEditInPurchase.getDmGrid().addTextColumn(txtHSRateEditInPurchase.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSRateEditInPurchase.getDmGrid().initializeGrid(txtHSRateEditInPurchase.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHSRateEditInPurchase.getHelper().addSort("name", DmSortType.ASC);

    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHAutoInTransitByMedicineTransfer">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAutoInTransitByMedicineTransfer = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 8, false);

    private void iniTxtHAutoInTransitByMedicineTransfer() {
        txtHAutoInTransitByMedicineTransfer.getDmGrid().addTextColumn(txtHAutoInTransitByMedicineTransfer.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHAutoInTransitByMedicineTransfer.getDmGrid().initializeGrid(txtHAutoInTransitByMedicineTransfer.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHAutoInTransitByMedicineTransfer.getHelper().addSort("name", DmSortType.ASC);

    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHCentralStore">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCentralStore = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 5, true);

    public void iniCentralStore() {
        txtHCentralStore.getDmGrid().addTextColumn(txtHCentralStore.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHCentralStore.getDmGrid().initializeGrid(txtHCentralStore.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHCentralStore.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    public String voucher_Cat = "";

    public EnvironmentSettingSite_Dsg() {
        ini();
        iniSite();
        iniSiteCopy();
        iniPAddAlphaCase();
        iniPatAlphaCase();
        iniTxtHAllowReturnWithooutSale();
        iniTxtHIsIncludeTaxInCost();
        iniTxtHPharmacyHospitalAc();
        iniTxtHPharmacyControlAc();
        iniTxtHAddress();
        iniMedicineAlphaCase();
        iniTxtHPharmacySelfAc();
        iniTxtHMedicineSaleOn();
        initxtHPHAllowPatientForSite();
        iniTxtHPHAllowSaleBillForSite();
        iniTxtHPharmacyFinalDiscountOH();
        iniTxtHPharmacyFinalDiscountAc();
        initxtHDefaultService();
        iniTxtHBarcodeActive();
        iniTxtHAutoInTransitByMedicineTransfer();
        iniTxtHSRateEditInPurchase();
        iniCentralStore();
    }

    private DmToolContainer iniToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.setTitle("Save: (Ctrl + S)");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");

        label = new InlineLabel("Copy");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-copy");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCopy.setTitle("Copy");
        btnCopy.addStyleName("dm-top-control-btnStyle");
        btnCopy.getElement().appendChild(aBtn.getElement());
        btnCopy.getElement().appendChild(label.getElement());
        btnCopy.setWidth("70px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        toolContainer.addWidget(vl.add(btnSave));
        toolContainer.addWidget(vl.add(btnCopy));
        toolContainer.addWidget(vl.add(btnClose));
        toolContainer.addGap();
        return toolContainer;

    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("600px");
        vrtInnerForm.add(vl.add(iniToolControl()));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        userControl.addGap(40);
        userControl.addTextBlog(modifiedByBlog, 200, true, true);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "545px", null, null, DmPosition.CENTER, true);

        // Control Detail
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        // Grid Tab
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "518px", "left", null, DmPosition.NONE, true);
        flwTr.add(tabArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "75%", "20px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "93%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSite));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "518px", null, null, DmPosition.NONE, true);

        tabPanel.add(tabGeneral(), "General");
        tabPanel.add(vl.add(tabPharmacy()), "Pharmacy");
        tabPanel.add(vl.add(tabStore()), "Whole Sale");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel tabGeneral() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "485px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        //================================<<<< 1st Row >>>=========================================//
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", "50px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftGeneral());
        flwTable.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", "130px", "right", null, DmPosition.NONE, true);
        flwTd.add(rightGeneral());
        flwTable.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "42.5%", "360px", "left", null, DmPosition.NONE, true);
        flwTd.add(bottomRightGeneral());
        flwTable.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftGeneral() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "50px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Patient Alphabet Case");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPatAlphaCase));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address Alphabet Case");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAddAlphaCase));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< ******* >>>=========================================//
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Include Tax In Cost");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsIncludeTaxInCost));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);
        //================================<<<< ******* >>>=========================================//

        return flwTable;
    }

    private FlowPanel bottomRightGeneral() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "360px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "198%", "18px", "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PX);
        flwTd.setStyleName("dm-StripLabel");
        lbl = new DmLabel("Department Site Link");
        lbl.setStyleName("dm-HLabel");

        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.getElement().getStyle().setLineHeight(15, Style.Unit.PX);
        lbl.setHeight("20px");
        lbl.setWidth("70%");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "173px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "170px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniLinkDepartment()));
        flwTd.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightGeneral() {
        FlowPanel flwTable, flwTr, flwTd;
        Anchor aBtn;
        InlineLabel label;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "130px", null, null, DmPosition.CENTER, true);

        ////////////////////////////////1 Row//////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "115px", "100px", "right", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "100%", "left", "relative", DmPosition.NONE, true);
        imgView.setWidth("100%");
        imgView.setHeight("100%");
        imgView.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        progressLbl.setWidth("100%");
        DmClient.setStyle(flwImage, "100%", "100%", "left", null, DmPosition.NONE, true);
        flwTd.add(flwImage);
        flwTd.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
        flwTd.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        flwTd.getElement().getStyle().setBorderColor("lightgray");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "84%", "25px", "left", "relative", DmPosition.NONE, true);
        label = new InlineLabel("Company Logo");
        label.getElement().setAttribute("style", label.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;width:78px;");
        fileRuleType = new DmFileRuleType();
        fileRuleType.setFilters("jpg,png,jpeg");
        fileRuleType.setFileSizeLimit("1024");
        btnUpload = new DmFileUploadButton(null, fileRuleType);

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-paperclip");
        aBtn.getElement().getStyle().setColor("black");
        aBtn.getElement().getStyle().setFloat(Style.Float.LEFT);
        btnUpload.getButton().getElement().appendChild(aBtn.getElement());
        btnUpload.getButton().getElement().appendChild(label.getElement());
        btnUpload.getButton().setSize("97px", "24px");
        btnUpload.getButton().getElement().getStyle().setPadding(0, Style.Unit.PX);
        flwTd.add(btnUpload);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", "22px", "left", "relative", DmPosition.NONE, true);
        flwTd.add(btnDelete);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("red");
        btnDelete.getElement().appendChild(aBtn.getElement());
        btnDelete.setWidth("20px");
        btnDelete.setHeight("24px");
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel tabPharmacy() {

        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "380px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "380px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "110px", "left", null, DmPosition.NONE, true);
        flwTd.add((leftPharmacy()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "110px", "left", null, DmPosition.NONE, true);
        flwTd.add((rightPharmacy()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftPharmacy() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "110px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Alphabet Case");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicineAlphaCase));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 2nd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Expiry Alert Before Days");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtExpiryAlertDays));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 3rd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Sale On");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicineSaleOn));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default Service");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDefaultService));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Allow Return Without Sale");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAllowReturnWithooutSale));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Edit S.Rate In Purchase");
        lbl.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSRateEditInPurchase));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Return Days");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReturnDays));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Barcode Active");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPharmacyBarcodeActive));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Auto In Transit By Medicine Transfer");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAutoInTransitByMedicineTransfer));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<< END >>=============================================//
        //---------------------------------------------------------------------------
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "20px", "left", null, DmPosition.NONE, false);
        flwTd.setStyleName("dm-StripLabel");
        lbl = new DmLabel("Allow Patient For Site");
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(12, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //---------------------------------------------------------------------------
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sites");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPHAllowPatientForSite));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //---------------------------------------------------------------------------
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "20px", "left", null, DmPosition.NONE, false);
        flwTd.setStyleName("dm-StripLabel");
        lbl = new DmLabel("Allow Sale Bill For Site");
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(12, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //---------------------------------------------------------------------------
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sites");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPHAllowSaleBillForSite));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightPharmacy() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "110px", null, null, DmPosition.CENTER, true);

        ////////////////////////////////1 Row//////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "20px", "left", null, DmPosition.NONE, false);
        flwTd.setStyleName("dm-StripLabel");
        lbl = new DmLabel("Pharmacy Accounting");
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(12, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //2nd row
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Pharmacy Control A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPharmacyControlAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //4th row
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Hospital A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPharmacyHospitalAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Pharmacy A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPharmacySelfAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Pharmacy Final Discount A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPharmacyFinalDiscountAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Pharmacy Final Discount OH");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPharmacyFinalDiscountOH));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
    
    private FlowPanel tabStore() {

        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "380px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "380px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "110px", "left", null, DmPosition.NONE, true);
        flwTd.add((leftStore()));
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "50%", "110px", "left", null, DmPosition.NONE, true);
//        flwTd.add((rightPharmacy()));
//        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftStore() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "110px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Central Store For Sale");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCentralStore));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;

    }

    //<editor-fold defaultstate="collapsed" desc="grdLinkDepartment">
    interface GrdLinkDepartment_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLinkDepartment_Mdl> {

        @Editor.Path("LinkSiteName")
        ValueProvider<GrdLinkDepartment_Mdl, String> linkSiteName();

    }
    final DmGrid<GrdLinkDepartment_Mdl, GrdLinkDepartment_PrpAcs> grdLinkDepartment = new DmGrid(GWT.create(GrdLinkDepartment_PrpAcs.class));

    private Grid iniLinkDepartment() {
        grdLinkDepartment.addRowNumberColumn();
        grdLinkDepartment.addTextColumn(grdLinkDepartment.getPropertyAccess().name(), "Department", 495, "");
        grdLinkDepartment.addTextColumn(grdLinkDepartment.getPropertyAccess().linkSiteName(), "Link Site", 495, "");
        grdLinkDepartment.initializeGrid(grdLinkDepartment.getPropertyAccess().fieldHashCode(), 1024, 170);
        return grdLinkDepartment.getGrid();
    }
    //</editor-fold>

}
