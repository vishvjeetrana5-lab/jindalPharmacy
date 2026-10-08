package aarogya.master.environmentSettingSite.client;

import aarogya.master.environmentSettingSite.shared.*;
import aarogya.projectMenu.self.shared.ProjectRuleCode;
import aarogya.projectMenu.self.shared.pmMaster;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
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
    final DmTextBoxNumeric txtDefaultRegFee = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, true, 10, false);
    final DmTextBox txtUploadStatus = new DmTextBox();
    final DmTextBoxNumeric txtIncludeDDInMonth = new DmTextBoxNumeric(0, false, 2, false);
    final DmTextBoxNumeric txtIncludeMMInMonth = new DmTextBoxNumeric(0, false, 2, false);
    final DmTextBoxNumeric txtCancelDaysOPD = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextBoxNumeric txtServiceDaysOPD = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextBoxNumeric txtCancelDaysDiagnostic = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextBoxNumeric txtDiagnosticDays = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextBoxNumeric txtPancardLimit = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtCashpaymentLimit = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtAppointmentHistoryViewDays = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextBoxNumeric txtHistoryViewDays = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextBoxNumeric txtLabHistoryViewDays = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextArea txtDocumentFooter = new DmTextArea(250, false);
    final DmTextArea txtDocumentHeader = new DmTextArea(250, false);
    final DmTextArea txtDocumentFooterRegional = new DmTextArea(250, false);
    final DmTextBoxTime txtFrom = new DmTextBoxTime(false);
    final DmTextBoxTime txtTo = new DmTextBoxTime(false);
    final Image imgView = new Image();
    final DmLabel progressLbl = new DmLabel("");
    final FlowPanel flwImage = new FlowPanel();

    final DmTextBoxTime txtTimeCheckout = new DmTextBoxTime(false);
    final DmTextBoxNumeric txtGraceMinuteIn = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextBoxNumeric txtGraceMinuteOut = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextBoxNumeric txtIPDHalfDayUpTo = new DmTextBoxNumeric(0, false, 4, false);
    final DmTextBoxNumeric txtPercentageRoom = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextBoxNumeric txtOPDNoOfSticker = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextBoxNumeric txtIPDNoOfSticker = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextBoxNumeric txtDiagnosticNoOfSticker = new DmTextBoxNumeric(0, false, 3, false);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    final DmTextBoxNumeric txtNewBornDay = new DmTextBoxNumeric(0, false, 2, false);
    final DmTextBoxNumeric txtNewBornMonth = new DmTextBoxNumeric(0, false, 2, false);
    final DmTextBoxNumeric txtNewBornYear = new DmTextBoxNumeric(0, false, 2, false);

    final DmTextBoxNumeric txtInfantMonth = new DmTextBoxNumeric(0, false, 2, false);
    final DmTextBoxNumeric txtInfantYear = new DmTextBoxNumeric(0, false, 2, false);

    final DmTextBoxNumeric txtChildMonth = new DmTextBoxNumeric(0, false, 2, false);
    final DmTextBoxNumeric txtChildYear = new DmTextBoxNumeric(0, false, 2, false);
    final DmTextBoxNumeric txtDietScheduleDays = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextBox txtDietaryProperty1 = new DmTextBox(25, false);
    final DmTextBox txtDietaryProperty2 = new DmTextBox(25, false);
    final DmTextBox txtDietaryProperty3 = new DmTextBox(25, false);
    final DmTextBox txtDietaryProperty4 = new DmTextBox(25, false);
    final DmTextBox txtDietaryProperty5 = new DmTextBox(25, false);
    final DmTextBox txtDietaryProperty6 = new DmTextBox(25, false);

    final DmTextBoxNumeric txtValidUpTo = new DmTextBoxNumeric(0, false, 4, false);
    final DmTextBoxNumeric txtNoOfVisit = new DmTextBoxNumeric(0, false, 43, false);
    final DmTextBoxNumeric txtAppointmentCountMonthWise = new DmTextBoxNumeric(0, false, 2, false);

    final DmTextBoxNumeric txtAppointmentBookUptoDays = new DmTextBoxNumeric(0, false, 4, false);
    final DmTextBox txtPaymentGatewayURL = new DmTextBox(250, false);

    final Button btnQueue = new Button("...");

    DmFileRuleType fileRuleTypeDocument;

    final Button btnExport = new Button();

    final DmTextBox txtTransactionLockDate = new DmTextBox(11, false);

    //<editor-fold defaultstate="collapsed" desc="txtHCardType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCardType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHCardType() {
        txtHCardType.getDmGrid().addTextColumn(txtHCardType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHCardType.getDmGrid().initializeGrid(txtHCardType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHCardType.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPackage">
    interface TxtHPackage_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> shortName();
    }

    final DmTextHelp<KeyFieldExtended, TxtHPackage_PrpAcs> txtHPackage = new DmTextHelp(GWT.create(TxtHPackage_PrpAcs.class), 10, false);

    private void iniTxtHPackage() {
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().name(), "Package", 0, "name");
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().shortName(), "Short Name", 80, "shortName");
        txtHPackage.getDmGrid().initializeGrid(txtHPackage.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHPackage.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHConsultationRegistration">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHConsultationRegistration = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHConsultationRegistration() {
        txtHConsultationRegistration.getDmGrid().addTextColumn(txtHConsultationRegistration.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHConsultationRegistration.getDmGrid().initializeGrid(txtHConsultationRegistration.getDmGrid().getPropertyAccess().code(), 175, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHQueueAlgo">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHQueueAlgo = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHQueueAlgo() {
        txtHQueueAlgo.getDmGrid().addTextColumn(txtHQueueAlgo.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHQueueAlgo.getDmGrid().initializeGrid(txtHQueueAlgo.getDmGrid().getPropertyAccess().code(), 175, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSiteCopy">
    final DmHelpGrid<KeyFieldExtended, KeyField_PrpAcs> txtHSiteCopy = new DmHelpGrid(GWT.create(KeyField_PrpAcs.class), 6);

    private void iniSiteCopy() {
        txtHSiteCopy.getDmGrid().addTextColumn(txtHSiteCopy.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSiteCopy.getDmGrid().initializeGrid(txtHSiteCopy.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

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

    //<editor-fold defaultstate="collapsed" desc="txtHIsExistingPatient">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsExistingPatient = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniIsExistingPatient() {
        txtHIsExistingPatient.getDmGrid().addTextColumn(txtHIsExistingPatient.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHIsExistingPatient.getDmGrid().initializeGrid(txtHIsExistingPatient.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHIsExistingPatientDiagnostic">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsExistingPatientDiagnostic = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void txtHIsExistingPatientDiagnostic() {
        txtHIsExistingPatientDiagnostic.getDmGrid().addTextColumn(txtHIsExistingPatientDiagnostic.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHIsExistingPatientDiagnostic.getDmGrid().initializeGrid(txtHIsExistingPatientDiagnostic.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHIsBillH_Approval">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsBillH_Approval = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void txtHIsBillH_Approval() {
        txtHIsBillH_Approval.getDmGrid().addTextColumn(txtHIsBillH_Approval.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHIsBillH_Approval.getDmGrid().initializeGrid(txtHIsBillH_Approval.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCommonPricelist">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsCommonPricelist = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void txtHIsCommonPricelist() {
        txtHIsCommonPricelist.getDmGrid().addTextColumn(txtHIsCommonPricelist.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHIsCommonPricelist.getDmGrid().initializeGrid(txtHIsCommonPricelist.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHroomMandatory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHroomMandatory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniIsroomMandatory() {
        txtHroomMandatory.getDmGrid().addTextColumn(txtHroomMandatory.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHroomMandatory.getDmGrid().initializeGrid(txtHroomMandatory.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAddAlphaCase">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAddAlphaCase = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniPAddAlphaCase() {
        txtHAddAlphaCase.getDmGrid().addTextColumn(txtHAddAlphaCase.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHAddAlphaCase.getDmGrid().initializeGrid(txtHAddAlphaCase.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPatAlphaCase">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPatAlphaCase = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniPatAlphaCase() {
        txtHPatAlphaCase.getDmGrid().addTextColumn(txtHPatAlphaCase.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPatAlphaCase.getDmGrid().initializeGrid(txtHPatAlphaCase.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHEodPosting">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEodPosting = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void initxtHEodPosting() {
        txtHEodPosting.getDmGrid().addTextColumn(txtHEodPosting.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHEodPosting.getDmGrid().initializeGrid(txtHEodPosting.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHEodPosting">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceUnitEditable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void initxtHServiceUnitEditable() {
        txtHServiceUnitEditable.getDmGrid().addTextColumn(txtHServiceUnitEditable.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHServiceUnitEditable.getDmGrid().initializeGrid(txtHServiceUnitEditable.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOPDCase">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOPDCase = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHOPDCase() {
        txtHOPDCase.getDmGrid().addTextColumn(txtHOPDCase.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHOPDCase.getDmGrid().initializeGrid(txtHOPDCase.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHOPDCase.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOPDRefConsultant">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOPDRefConsultant = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHOPDRefConsultant() {
        txtHOPDRefConsultant.getDmGrid().addTextColumn(txtHOPDRefConsultant.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHOPDRefConsultant.getDmGrid().initializeGrid(txtHOPDRefConsultant.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHOPDRefConsultant.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOPDConsultant">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOPDConsultant = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHOPDConsultant() {
        txtHOPDConsultant.getDmGrid().addTextColumn(txtHOPDConsultant.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHOPDConsultant.getDmGrid().initializeGrid(txtHOPDConsultant.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHOPDConsultant.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHCity">
    interface TxtHCity_PrpAcs extends KeyField_T_PrpAcs<TxtHCity_Mdl> {

        @Editor.Path("StateName")
        ValueProvider<TxtHCity_Mdl, String> stateName();

        @Editor.Path("CityName")
        ValueProvider<TxtHCity_Mdl, String> cityName();

        @Editor.Path("CountryCode")
        ValueProvider<TxtHCity_Mdl, String> CountryCode();

        @Editor.Path("CountryName")
        ValueProvider<TxtHCity_Mdl, String> CountryName();
    }
    final DmTextHelp<TxtHCity_Mdl, TxtHCity_PrpAcs> txtHCity = new DmTextHelp(GWT.create(TxtHCity_PrpAcs.class), 10, false);

    private void iniTxtHCity() {
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().cityName(), "City", 0, "cityName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().stateName(), "State", 100, "stateName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().CountryName(), "Country", 100, "CountryName");
        txtHCity.getDmGrid().initializeGrid(txtHCity.getDmGrid().getPropertyAccess().code(), 420, 0, 1);

        txtHCity.getHelper().addSort("cityName", DmSortType.ASC);
        txtHCity.getHelper().addSort("stateName", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCountry">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCountry = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniCountry() {
        txtHCountry.getDmGrid().addTextColumn(txtHCountry.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHCountry.getDmGrid().initializeGrid(txtHCountry.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHCountry.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIdTypemandatory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOPDIdTypeMandatory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHIdTypemandatory() {
        txtHOPDIdTypeMandatory.getDmGrid().addTextColumn(txtHOPDIdTypeMandatory.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHOPDIdTypeMandatory.getDmGrid().initializeGrid(txtHOPDIdTypeMandatory.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHOPDIdTypeMandatory.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHHourlyServiceRoundOff">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHourlyServiceRoundOff = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHHourlyServiceRoundOff() {
        txtHHourlyServiceRoundOff.getDmGrid().addTextColumn(txtHHourlyServiceRoundOff.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHHourlyServiceRoundOff.getDmGrid().initializeGrid(txtHHourlyServiceRoundOff.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHHourlyServiceRoundOff.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDirectPrescription">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDirectPrescription = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHDirectPrescription() {
        txtHDirectPrescription.getDmGrid().addTextColumn(txtHDirectPrescription.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHDirectPrescription.getDmGrid().initializeGrid(txtHDirectPrescription.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOPDRelegionMandatory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOPDRelegionMandatory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHOPDRelegionMandatory() {
        txtHOPDRelegionMandatory.getDmGrid().addTextColumn(txtHOPDRelegionMandatory.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHOPDRelegionMandatory.getDmGrid().initializeGrid(txtHOPDRelegionMandatory.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIPDInfantUnit">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIPDInfantUnit = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHIPDInfantUnit() {
        txtHIPDInfantUnit.getDmGrid().addTextColumn(txtHIPDInfantUnit.getDmGrid().getPropertyAccess().name(), "Unit", 0, "unit");
        txtHIPDInfantUnit.getDmGrid().initializeGrid(txtHIPDInfantUnit.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHIPDInfantUnit.getHelper().addSort("unit", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIPDRoomClean">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIPDRoomClean = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHIPDRoomClean() {
        txtHIPDRoomClean.getDmGrid().addTextColumn(txtHIPDRoomClean.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHIPDRoomClean.getDmGrid().initializeGrid(txtHIPDRoomClean.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIPDIsAutoRoom"> 
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIPDIsAutoRoom = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHIPDIsAutoRoom() {
        txtHIPDIsAutoRoom.getDmGrid().addTextColumn(txtHIPDIsAutoRoom.getDmGrid().getPropertyAccess().name(), "Type", 1, "Name");
        txtHIPDIsAutoRoom.getDmGrid().initializeGrid(txtHIPDIsAutoRoom.getDmGrid().getPropertyAccess().code(), 125, 0, 1);
    }
    //</editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="TxtHTimeFormatRoom"> 
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTimeFormatRoom = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHTimeFormatRoom() {
        txtHTimeFormatRoom.getDmGrid().addTextColumn(txtHTimeFormatRoom.getDmGrid().getPropertyAccess().name(), "Type", 1, "Name");
        txtHTimeFormatRoom.getDmGrid().initializeGrid(txtHTimeFormatRoom.getDmGrid().getPropertyAccess().code(), 125, 0, 1);
    }
    //</editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="TxtHIPDControlAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIPDControlAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHIPDControlAc() {
        txtHIPDControlAc.getDmGrid().addTextColumn(txtHIPDControlAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHIPDControlAc.getDmGrid().initializeGrid(txtHIPDControlAc.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHIPDControlAc.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHOPDControlAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOPDControlAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHOPDControlAc() {
        txtHOPDControlAc.getDmGrid().addTextColumn(txtHOPDControlAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHOPDControlAc.getDmGrid().initializeGrid(txtHOPDControlAc.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHOPDControlAc.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHDiagnosticControlAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDiagnosticControlAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHDiagnosticControlAc() {
        txtHDiagnosticControlAc.getDmGrid().addTextColumn(txtHDiagnosticControlAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDiagnosticControlAc.getDmGrid().initializeGrid(txtHDiagnosticControlAc.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHDiagnosticControlAc.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDiagnosticHospitalAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDiagnosticHospitalAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHDiagnosticHospitalAc() {
        txtHDiagnosticHospitalAc.getDmGrid().addTextColumn(txtHDiagnosticHospitalAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDiagnosticHospitalAc.getDmGrid().initializeGrid(txtHDiagnosticHospitalAc.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHDiagnosticHospitalAc.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDiagnosticSelfAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDiagnosticSelfAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHDiagnosticSelfAc() {
        txtHDiagnosticSelfAc.getDmGrid().addTextColumn(txtHDiagnosticSelfAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDiagnosticSelfAc.getDmGrid().initializeGrid(txtHDiagnosticSelfAc.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHDiagnosticSelfAc.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHEmergencyControlAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEmergencyControlAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHEmergencyControlAc() {
        txtHEmergencyControlAc.getDmGrid().addTextColumn(txtHEmergencyControlAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHEmergencyControlAc.getDmGrid().initializeGrid(txtHEmergencyControlAc.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHEmergencyControlAc.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHDayCareControlAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDayCareControlAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHDayCareControlAc() {
        txtHDayCareControlAc.getDmGrid().addTextColumn(txtHDayCareControlAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDayCareControlAc.getDmGrid().initializeGrid(txtHDayCareControlAc.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHDayCareControlAc.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHInvDefaultPurchaseDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHInvDefaultPurchaseDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHInvDefaultPurchaseDepartment() {
        txtHInvDefaultPurchaseDepartment.getDmGrid().addTextColumn(txtHInvDefaultPurchaseDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHInvDefaultPurchaseDepartment.getDmGrid().initializeGrid(txtHInvDefaultPurchaseDepartment.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHInvDefaultPurchaseDepartment.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPrntYsNo">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPrntYsNo = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true, false);

    private void iniTxtHPrntYsNo() {
        txtHPrntYsNo.getDmGrid().addTextColumn(txtHPrntYsNo.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPrntYsNo.getDmGrid().initializeGrid(txtHPrntYsNo.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHPrntYsNo.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIsDiagAuto">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsDiagAuto = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void initxtHIsDiagAuto() {
        txtHIsDiagAuto.getDmGrid().addTextColumn(txtHIsDiagAuto.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHIsDiagAuto.getDmGrid().initializeGrid(txtHIsDiagAuto.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDirectSampleLabColl">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDirectSampleLabColl = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void initxtHSampleLabCollection() {
        txtHDirectSampleLabColl.getDmGrid().addTextColumn(txtHDirectSampleLabColl.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHDirectSampleLabColl.getDmGrid().initializeGrid(txtHDirectSampleLabColl.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPatientInitial">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPatientInitial = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHPatientInitial() {
        txtHPatientInitial.getDmGrid().addTextColumn(txtHPatientInitial.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHPatientInitial.getDmGrid().initializeGrid(txtHPatientInitial.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHPatientInitial.getHelper().addSort("Name", DmSortType.ASC);
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

    //<editor-fold defaultstate="collapsed" desc="txtHGuardianInitial">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGuardianInitial = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHGuardianInitial() {
        txtHGuardianInitial.getDmGrid().addTextColumn(txtHGuardianInitial.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHGuardianInitial.getDmGrid().initializeGrid(txtHGuardianInitial.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHGuardianInitial.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCityMandatory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCityMandatory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHCityMandatory() {
        txtHCityMandatory.getDmGrid().addTextColumn(txtHCityMandatory.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHCityMandatory.getDmGrid().initializeGrid(txtHCityMandatory.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHCityMandatory.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMaritalStatus">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMaritalStatus = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHMaritalStatusMandatory() {
        txtHMaritalStatus.getDmGrid().addTextColumn(txtHMaritalStatus.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHMaritalStatus.getDmGrid().initializeGrid(txtHMaritalStatus.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHMaritalStatus.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCasteMandatory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCasteMandatory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHCasteMandatory() {
        txtHCasteMandatory.getDmGrid().addTextColumn(txtHCasteMandatory.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHCasteMandatory.getDmGrid().initializeGrid(txtHCasteMandatory.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHCasteMandatory.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHBloodGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBloodGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHBloodGroup() {
        txtHBloodGroup.getDmGrid().addTextColumn(txtHBloodGroup.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHBloodGroup.getDmGrid().initializeGrid(txtHBloodGroup.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHBloodGroup.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHEmail">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEmail = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHEmail() {
        txtHEmail.getDmGrid().addTextColumn(txtHEmail.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHEmail.getDmGrid().initializeGrid(txtHEmail.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHEmail.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHDiagnosticRegAddition">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> TxtHDiagnosticRegAddition = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHTxtHDiagnoRegAddition() {
        TxtHDiagnosticRegAddition.getDmGrid().addTextColumn(TxtHDiagnosticRegAddition.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        TxtHDiagnosticRegAddition.getDmGrid().initializeGrid(TxtHDiagnosticRegAddition.getDmGrid().getPropertyAccess().code(), 310, 0, 1);

        TxtHDiagnosticRegAddition.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHBloodBankCipAddition">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> TxtHBloodBankCipAddition = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHBloodBankCipAddition() {
        TxtHBloodBankCipAddition.getDmGrid().addTextColumn(TxtHBloodBankCipAddition.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        TxtHBloodBankCipAddition.getDmGrid().initializeGrid(TxtHBloodBankCipAddition.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        TxtHBloodBankCipAddition.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOrgReceiptCard">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOrgReceiptCard = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHOrgReceiptCard() {
        txtHOrgReceiptCard.getDmGrid().addTextColumn(txtHOrgReceiptCard.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHOrgReceiptCard.getDmGrid().initializeGrid(txtHOrgReceiptCard.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHOrgReceiptCard.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHHospitalHoldCard">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHospitalHoldCard = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHHospitalHoldCard() {
        txtHHospitalHoldCard.getDmGrid().addTextColumn(txtHHospitalHoldCard.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHHospitalHoldCard.getDmGrid().initializeGrid(txtHHospitalHoldCard.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHHospitalHoldCard.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSecondaryCurrency">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSecondaryCurrency = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHSecondaryCurrency() {
        txtHSecondaryCurrency.getDmGrid().addTextColumn(txtHSecondaryCurrency.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHSecondaryCurrency.getDmGrid().initializeGrid(txtHSecondaryCurrency.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHSecondaryCurrency.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPatientImgMandatory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPatientImgMandatory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void initxtHPatientImgMandatory() {
        txtHPatientImgMandatory.getDmGrid().addTextColumn(txtHPatientImgMandatory.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHPatientImgMandatory.getDmGrid().initializeGrid(txtHPatientImgMandatory.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAreaMandatory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAreaMandatory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void initxtHAreaMandatory() {
        txtHAreaMandatory.getDmGrid().addTextColumn(txtHAreaMandatory.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHAreaMandatory.getDmGrid().initializeGrid(txtHAreaMandatory.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGuardianNameMandatory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGuardianNameMandatory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void initxtHGuardianNameMandatory() {
        txtHGuardianNameMandatory.getDmGrid().addTextColumn(txtHGuardianNameMandatory.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHGuardianNameMandatory.getDmGrid().initializeGrid(txtHGuardianNameMandatory.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPrintedOnLetterhead">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPrintedOnLetterhead = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void initxtHPrintedOnLetterhead() {
        txtHPrintedOnLetterhead.getDmGrid().addTextColumn(txtHPrintedOnLetterhead.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHPrintedOnLetterhead.getDmGrid().initializeGrid(txtHPrintedOnLetterhead.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTokenBlock">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTokenBlock = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void initxtHTokenBlock() {
        txtHTokenBlock.getDmGrid().addTextColumn(txtHTokenBlock.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHTokenBlock.getDmGrid().initializeGrid(txtHTokenBlock.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDisallowType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDisallowType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHDisallowType() {
        txtHDisallowType.getDmGrid().addTextColumn(txtHDisallowType.getDmGrid().getPropertyAccess().name(), "Type", 0, "name");
        txtHDisallowType.getDmGrid().initializeGrid(txtHDisallowType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDefPharmacySite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDefPharmacySite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHDefaultPharmacySite() {
        txtHDefPharmacySite.getDmGrid().addTextColumn(txtHDefPharmacySite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDefPharmacySite.getDmGrid().initializeGrid(txtHDefPharmacySite.getDmGrid().getPropertyAccess().code(), 300, 0, 2);

        txtHDefPharmacySite.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>
    
    
    
     //<editor-fold defaultstate="collapsed" desc="txtHLockVoucherPrefix">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHLockVoucherPrefix = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHVoucherPrefix() {
        txtHLockVoucherPrefix.getDmGrid().addTextColumn(txtHLockVoucherPrefix.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHLockVoucherPrefix.getDmGrid().initializeGrid(txtHLockVoucherPrefix.getDmGrid().getPropertyAccess().code(), 300, 0, 2);

        txtHLockVoucherPrefix.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>
    

    //<editor-fold defaultstate="collapsed" desc="txtHOPDAllowPatientForSite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHOPDAllowPatientForSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHOPDAllowPatientForSite() {
        txtHOPDAllowPatientForSite.getDmGrid().addTextColumn(txtHOPDAllowPatientForSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHOPDAllowPatientForSite.getDmGrid().initializeGrid(txtHOPDAllowPatientForSite.getDmGrid().getPropertyAccess().code(), 300, 0, 2);

        txtHOPDAllowPatientForSite.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIPDAllowPatientForSite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHIPDAllowPatientForSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6);

    private void initxtHIPDAllowPatientForSite() {
        txtHIPDAllowPatientForSite.getDmGrid().addTextColumn(txtHIPDAllowPatientForSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHIPDAllowPatientForSite.getDmGrid().initializeGrid(txtHIPDAllowPatientForSite.getDmGrid().getPropertyAccess().code(), 300, 0, 2);

        txtHIPDAllowPatientForSite.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDiagAllowPatientForSite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDiagAllowPatientForSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6);

    private void initxtHDiagAllowPatientForSite() {
        txtHDiagAllowPatientForSite.getDmGrid().addTextColumn(txtHDiagAllowPatientForSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDiagAllowPatientForSite.getDmGrid().initializeGrid(txtHDiagAllowPatientForSite.getDmGrid().getPropertyAccess().code(), 300, 0, 2);

        txtHDiagAllowPatientForSite.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDAYCAllowPatientForSite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDAYCAllowPatientForSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6);

    private void initxtHDAYCAllowPatientForSite() {
        txtHDAYCAllowPatientForSite.getDmGrid().addTextColumn(txtHDAYCAllowPatientForSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDAYCAllowPatientForSite.getDmGrid().initializeGrid(txtHDAYCAllowPatientForSite.getDmGrid().getPropertyAccess().code(), 300, 0, 2);

        txtHDAYCAllowPatientForSite.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDefPharmacyDepartment">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDefPharmacyDepartment = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void initxtHDefPharmacyDepartment() {
        txtHDefPharmacyDepartment.getDmGrid().addTextColumn(txtHDefPharmacyDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDefPharmacyDepartment.getDmGrid().initializeGrid(txtHDefPharmacyDepartment.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHDefPharmacyDepartment.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //    //<editor-fold defaultstate="collapsed" desc="txtHBloodBagDepartment">
//    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBooldBagDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);
//
//    private void iniTxtHBloodBagDepartment() {
//        txtHBooldBagDepartment.getDmGrid().addTextColumn(txtHBooldBagDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
//        txtHBooldBagDepartment.getDmGrid().initializeGrid(txtHBooldBagDepartment.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
//
//        txtHBooldBagDepartment.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
//    }
//    //</editor-fold>
    //    //<editor-fold defaultstate="collapsed" desc="txtHBloodComponenetDepartment">
//    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHComponentDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);
//
//    private void iniTxtHComponetDepartment() {
//        txtHComponentDepartment.getDmGrid().addTextColumn(txtHComponentDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
//        txtHComponentDepartment.getDmGrid().initializeGrid(txtHComponentDepartment.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
//
//        txtHComponentDepartment.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
//    }
//    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHCampCode">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDonorCampCode = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHCampCode() {
        txtHDonorCampCode.getDmGrid().addTextColumn(txtHDonorCampCode.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDonorCampCode.getDmGrid().initializeGrid(txtHDonorCampCode.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHDonorCampCode.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHReceiptGenerateOnFullDiscount">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReceiptGenerateOnFullDiscount = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHReceiptGenerateOnFullDiscount() {
        txtHReceiptGenerateOnFullDiscount.getDmGrid().addTextColumn(txtHReceiptGenerateOnFullDiscount.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHReceiptGenerateOnFullDiscount.getDmGrid().initializeGrid(txtHReceiptGenerateOnFullDiscount.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHReceiptGenerateOnFullDiscount.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHIsOtpRequiredForRefund">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsOtpRequiredForRefund = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void initxtHIsOtpRequiredForRefund() {
        txtHIsOtpRequiredForRefund.getDmGrid().addTextColumn(txtHIsOtpRequiredForRefund.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHIsOtpRequiredForRefund.getDmGrid().initializeGrid(txtHIsOtpRequiredForRefund.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHSendEmailOnBillEdit">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIcdxMandatory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void initxtHIcdxMandatory() {
        txtHIcdxMandatory.getDmGrid().addTextColumn(txtHIcdxMandatory.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHIcdxMandatory.getDmGrid().initializeGrid(txtHIcdxMandatory.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHPaymentOption">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHPaymentOption = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void initxtHPaymentOption() {
        txtHPaymentOption.getDmGrid().addTextColumn(txtHPaymentOption.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPaymentOption.getDmGrid().initializeGrid(txtHPaymentOption.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHPaymentOption.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHAppointmentPaymentGatewayAllowed">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAppointmentPaymentGatewayAllowed = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHAppointmentPaymentGatewayAllowed() {
        txtHAppointmentPaymentGatewayAllowed.getDmGrid().addTextColumn(txtHAppointmentPaymentGatewayAllowed.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHAppointmentPaymentGatewayAllowed.getDmGrid().initializeGrid(txtHAppointmentPaymentGatewayAllowed.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHAppointmentPaymentGatewayAllowed.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHAuotOPDRegAfterAppointment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAutoOPDRegAfterAppointment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHAuotOPDRegAfterAppointment() {
        txtHAutoOPDRegAfterAppointment.getDmGrid().addTextColumn(txtHAutoOPDRegAfterAppointment.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHAutoOPDRegAfterAppointment.getDmGrid().initializeGrid(txtHAutoOPDRegAfterAppointment.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHAutoOPDRegAfterAppointment.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHRunFromWebsite">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRunFromWebsite = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHRunFromWebsite() {
        txtHRunFromWebsite.getDmGrid().addTextColumn(txtHRunFromWebsite.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHRunFromWebsite.getDmGrid().initializeGrid(txtHRunFromWebsite.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHRunFromWebsite.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHIncludeTaxInCost">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIncludeTaxInCost = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHincludeTaxInCost() {
        txtHIncludeTaxInCost.getDmGrid().addTextColumn(txtHIncludeTaxInCost.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHIncludeTaxInCost.getDmGrid().initializeGrid(txtHIncludeTaxInCost.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPurchaseSalesAcPostingBasedOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPurchaseSalesAcPostingBasedOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void inTxtHPurchaseSalesAcPostingBasedOn() {
        txtHPurchaseSalesAcPostingBasedOn.getDmGrid().addTextColumn(txtHPurchaseSalesAcPostingBasedOn.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHPurchaseSalesAcPostingBasedOn.getDmGrid().initializeGrid(txtHPurchaseSalesAcPostingBasedOn.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHIndentRequired">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIndentRequired = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHIndentRequired() {
        txtHIndentRequired.getDmGrid().addTextColumn(txtHIndentRequired.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHIndentRequired.getDmGrid().initializeGrid(txtHIndentRequired.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRequisitionMandatoryInTransfer">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRequisitionMandatoryInTransfer = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHRequisitionMandatoryInTransfer() {
        txtHRequisitionMandatoryInTransfer.getDmGrid().addTextColumn(txtHRequisitionMandatoryInTransfer.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHRequisitionMandatoryInTransfer.getDmGrid().initializeGrid(txtHRequisitionMandatoryInTransfer.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSinglePOFlowInPurchase">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSinglePOFlowInPurchase = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHSinglePOFlow() {
        txtHSinglePOFlowInPurchase.getDmGrid().addTextColumn(txtHSinglePOFlowInPurchase.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHSinglePOFlowInPurchase.getDmGrid().initializeGrid(txtHSinglePOFlowInPurchase.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHEodPosting">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAutoBedConfirmation = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void initxtHAutoBedConfirmation() {
        txtHAutoBedConfirmation.getDmGrid().addTextColumn(txtHAutoBedConfirmation.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHAutoBedConfirmation.getDmGrid().initializeGrid(txtHAutoBedConfirmation.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAssetDepreciationAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAssetDepreciationAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHAssetDepreciationAc() {
        txtHAssetDepreciationAc.getDmGrid().addTextColumn(txtHAssetDepreciationAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHAssetDepreciationAc.getDmGrid().initializeGrid(txtHAssetDepreciationAc.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHAssetDepreciationAc.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepreciationPostOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepreciationPostOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHDepreciationPostOn() {
        txtHDepreciationPostOn.getDmGrid().addTextColumn(txtHDepreciationPostOn.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHDepreciationPostOn.getDmGrid().initializeGrid(txtHDepreciationPostOn.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHOPDConsultant.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public EnvironmentSettingSite_Dsg() {
        ini();
        iniSite();
        iniSiteCopy();
        iniPAddAlphaCase();
        iniPatAlphaCase();
        initxtHEodPosting();
        initxtHServiceUnitEditable();
        iniTxtHOPDCase();
        iniTxtHOPDRefConsultant();
        iniTxtHOPDConsultant();
        iniTxtHCity();
        iniCountry();
        iniTxtHIdTypemandatory();
        iniTxtHDirectPrescription();
        iniTxtHOPDRelegionMandatory();
        iniTxtHIPDInfantUnit();
        iniTxtHIPDRoomClean();
        iniTxtHIPDIsAutoRoom();
        iniTxtHTimeFormatRoom();
        iniTxtHDiagnosticControlAc();
        iniTxtHDiagnosticHospitalAc();
        iniTxtHIPDControlAc();
        iniTxtHOPDControlAc();
        iniTxtHDisallowType();
        initxtHSampleLabCollection();
        iniIsExistingPatient();
        txtHIsExistingPatientDiagnostic();
        txtHIsBillH_Approval();
        iniTxtHPatientInitial();
        iniTxtHAddress();
        initxtHIsDiagAuto();
        iniTxtHGuardianInitial();
        iniTxtHCityMandatory();
        iniTxtHMaritalStatusMandatory();
        iniTxtHCasteMandatory();
        iniTxtHBloodGroup();
        iniTxtHEmail();
        iniTxtHDayCareControlAc();
        iniTxtHEmergencyControlAc();
        iniTxtHPrntYsNo();
        iniIsroomMandatory();
        iniTxtHTxtHDiagnoRegAddition();
        iniTxtHBloodBankCipAddition();
        iniTxtHOrgReceiptCard();
        iniTxtHHospitalHoldCard();
        iniTxtHSecondaryCurrency();
        initxtHPatientImgMandatory();
        initxtHAreaMandatory();
        initxtHGuardianNameMandatory();
        initxtHPrintedOnLetterhead();
        iniTxtHDiagnosticSelfAc();
        iniTxtHHourlyServiceRoundOff();
        initxtHTokenBlock();
        iniTxtHOPDAllowPatientForSite();
        initxtHIPDAllowPatientForSite();
        initxtHDAYCAllowPatientForSite();
        initxtHDiagAllowPatientForSite();
        initxtHDefPharmacyDepartment();
        iniTxtHPackage();
        iniTxtHDefaultPharmacySite();
//        iniTxtHBloodBagDepartment();
//        iniTxtHComponetDepartment();
        iniTxtHCampCode();
        iniTxtHQueueAlgo();
        iniTxtHConsultationRegistration();
        iniTxtHReceiptGenerateOnFullDiscount();
        initxtHIsOtpRequiredForRefund();
        initxtHIcdxMandatory();
        iniTxtHAppointmentPaymentGatewayAllowed();
        initxtHPaymentOption();
        iniTxtHAuotOPDRegAfterAppointment();
        iniTxtHRunFromWebsite();
        iniTxtHCardType();
        iniTxtHincludeTaxInCost();
        inTxtHPurchaseSalesAcPostingBasedOn();
        iniTxtHIndentRequired();
        iniTxtHRequisitionMandatoryInTransfer();
        iniTxtHSinglePOFlow();
        initxtHAutoBedConfirmation();
        iniTxtHInvDefaultPurchaseDepartment();
        txtHIsCommonPricelist();
        iniTxtHAssetDepreciationAc();
        iniTxtHDepreciationPostOn();
        iniTxtHVoucherPrefix();
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
        btnSave.setWidth("40px");

        label = new InlineLabel("Copy");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-copy");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCopy.setTitle("Copy");
        btnCopy.addStyleName("dm-top-control-btnStyle");
        btnCopy.getElement().appendChild(aBtn.getElement());
        btnCopy.getElement().appendChild(label.getElement());
        btnCopy.setWidth("40px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("40px");

        label = new InlineLabel("Export");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-upload");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnExport.addStyleName("dm-top-control-btnStyle");
        btnExport.setTitle("Export");
        btnExport.getElement().appendChild(aBtn.getElement());
        btnExport.getElement().appendChild(label.getElement());
        btnExport.setWidth("40px");

        toolContainer.addWidget(vl.add(btnSave));
        toolContainer.addWidget(vl.add(btnCopy));
        toolContainer.addWidget(btnExport);
        toolContainer.addWidget(vl.add(btnClose));
        toolContainer.addGap();
        return toolContainer;

    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("615px");
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
        DmClient.setStyle(flwTable, "100%", "555px", null, null, DmPosition.CENTER, true);

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
        DmClient.setStyle(tabPanel, "100%", "540px", null, null, DmPosition.NONE, true);

        tabPanel.add(tabGeneral(), "General");
        tabPanel.add((tabOPD()), "OPD");
        tabPanel.add((tabIPD()), "IPD");
        tabPanel.add((tabDiagnostic()), "Diagnostic");
        tabPanel.add((tabDayCare()), "Day Care");
        tabPanel.add((tabInventory()), "Inventory");
        tabPanel.add((tabOT()), "OT");
        tabPanel.add((tabDietary()), "Dietary");
        tabPanel.add((tabBloodbank()), "Blood Bank");
        tabPanel.add((tabOnlinePortal()), "Online Portal");
        tabPanel.add((tabAsset()), "Asset");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel tabGeneral() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "512px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        //================================<<<< 1st Row >>>=========================================//
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", "169px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftGeneral());
        flwTable.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", "130px", "right", null, DmPosition.NONE, true);
        flwTd.add(rightGeneral());
        flwTable.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "85%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "42.5%", "360px", "left", null, DmPosition.NONE, true);
        flwTd.add(bottomLeftGeneral());
        flwTable.add(flwTd);

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
        DmClient.setStyle(flwTable, "100%", "169px", null, null, DmPosition.CENTER, true);

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

        //================================<<<< 2nd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Include \"dd\" in age");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIncludeDDInMonth));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Months");
        flwTd.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Include \"mm\" in age");
        lbl.getElement().getStyle().setMarginLeft(35, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIncludeMMInMonth));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Years");
        flwTd.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Receipt Generate On Full Discount");
        lbl.getElement().getStyle().setMarginLeft(27, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHReceiptGenerateOnFullDiscount));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 3rd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Document Header");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDocumentHeader));
        txtDocumentHeader.setHeight("35px");
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 4th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Document Footer");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDocumentFooter));
        txtDocumentFooter.setHeight("35px");
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 5th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Document Footer Regional");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtDocumentFooterRegional.setHeight("35px");
        txtDocumentFooterRegional.addStyleName(DmEnum.RegionalLanguage.getValueFrom(DmClient.getRegionalLanguage()).getCssStyleName());
        flwTd.add(vl.add(txtDocumentFooterRegional));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Transaction Lock Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl.getElement().getStyle().setMarginLeft(27, Style.Unit.PCT);
        flwTd.add(vl.add(txtTransactionLockDate));
        flwTr.add(flwTd);
        
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Lock Year");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl.getElement().getStyle().setMarginLeft(27, Style.Unit.PCT);
        flwTd.add(vl.add(txtHLockVoucherPrefix));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel bottomLeftGeneral() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "360px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default City");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCity));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 2th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default Nationality");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCountry));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 3rd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Disallow Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDisallowType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 5th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PAN Card Limit");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPancardLimit));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 6th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Cash Limit");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCashpaymentLimit));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 7th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Org. Receipt Card Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOrgReceiptCard));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Hospital Hold Card Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHospitalHoldCard));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Secondary Currency");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSecondaryCurrency));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 8th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Patient Initial Mandatory");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPatientInitial));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("City Mandatory");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCityMandatory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 9th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Guardian Initial Mandatory");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGuardianInitial));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Caste Mandatory");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCasteMandatory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 10th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Guardian Name Mandatory");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGuardianNameMandatory));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Email Mandatory");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEmail));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 11th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Marital Status Mandatory");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMaritalStatus));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Religion Mandatory");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOPDRelegionMandatory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 12th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Id Type  Mandatory");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOPDIdTypeMandatory));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address Mandatory");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAddress));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 13th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Blood Group Mandatory");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBloodGroup));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Area Mandatory");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAreaMandatory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Patient Image Mandatory");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPatientImgMandatory));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ICD-X Mandatory");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIcdxMandatory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Is OTP Required for Refund");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsOtpRequiredForRefund));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("EOD Posting Type");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEodPosting));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

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
        DmClient.setStyle(flwTd, "98%", "18px", "left", null, DmPosition.NONE, false);
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

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "60px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "60px", "left", null, DmPosition.NONE, false);
        flwTd.add(bottomRightTopGeneral());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel bottomRightTopGeneral() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "60px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        //================================<<<< 14th Row >>>=========================================//
        //================================<<<< 17th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("New Born");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("DD");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNewBornDay));
        txtNewBornDay.getElement().setPropertyString("placeholder", "DD");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("MM");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNewBornMonth));
        txtNewBornMonth.getElement().setPropertyString("placeholder", "MM");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("YY");
        lbl.getElement().getStyle().setMarginLeft(12, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNewBornYear));
        txtNewBornYear.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        txtNewBornYear.getElement().setPropertyString("placeholder", "YY");
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 18th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Infant");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("MM");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtInfantMonth));
        txtInfantMonth.getElement().setPropertyString("placeholder", "MM");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("YY");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtInfantYear));
        txtInfantYear.getElement().setPropertyString("placeholder", "YY");
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 19th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Child");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("MM");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtChildMonth));
        txtChildMonth.getElement().setPropertyString("placeholder", "MM");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("YY");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtChildYear));
        txtChildYear.getElement().setPropertyString("placeholder", "YY");
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 15th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Night Shift");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("From");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFrom));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTo));
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

    private FlowPanel tabOPD() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "367px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("OPD Control A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOPDControlAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 2nd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Day Care Control A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDayCareControlAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 3rd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Case");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOPDCase));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 4th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Referred By");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOPDRefConsultant));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 5th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consultant");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOPDConsultant));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 6th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default UHID Fee");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDefaultRegFee));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("No. Of Sticker");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtOPDNoOfSticker));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Cancel Days");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCancelDaysOPD));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Days");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtServiceDaysOPD));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Appointment History View Days");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAppointmentHistoryViewDays));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("History View Days");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHistoryViewDays));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 8th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Direct Prescription");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDirectPrescription));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Token Block Nature");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTokenBlock));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default Pharmacy Dept.");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDefPharmacyDepartment));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 9th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default Pharmacy Site");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDefPharmacySite));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default Estimate Package");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPackage));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default Wallet Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCardType));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("OPD Valid UpTo");
//        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtValidUpTo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("No. Of Visit");
        lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNoOfVisit));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Queue Algo");
//        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);

        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        btnQueue.addStyleName("dm-button-grad");
        DmClient.setStyle(btnQueue, "20px", "20px", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtHQueueAlgo));
        hrtPnl.add(btnQueue);
        hrtPnl.setCellWidth(btnQueue, "25px");
        hrtPnl.setCellWidth(txtHQueueAlgo, "100%");
        flwTd.add(hrtPnl);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consultation Registration");
        lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHConsultationRegistration));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

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
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOPDAllowPatientForSite));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel rightIPDBottom() {
        FlowPanel flwTable, flwTr;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "230px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "230px", "left", null, DmPosition.NONE, true);
        flwTr.add(iniStatusGUITemp());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel tabIPD() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "380px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "380px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "230px", "left", null, DmPosition.NONE, true);
        flwTd.add((leftIPD()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "100px", "left", null, DmPosition.NONE, true);
        flwTd.add((rightIPD()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "235px", "left", null, DmPosition.NONE, true);
        flwTd.add(rightIPDBottom());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftIPD() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "230px", null, null, DmPosition.CENTER, true);
//        DmClient.setStyle(flwTable, "98%", "260px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("IPD Control A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIPDControlAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Emergency Control A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEmergencyControlAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Infant Unit");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIPDInfantUnit));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 2nd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default Status (Room Vacant)");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIPDRoomClean));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("No. Of Sticker");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIPDNoOfSticker));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Existing Patient");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsExistingPatient));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Room Mandatory");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHroomMandatory));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Hourly Service Round Off");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHourlyServiceRoundOff));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Unit Editable");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceUnitEditable));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Auto Bed Confirmation");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAutoBedConfirmation));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
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
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sites");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIPDAllowPatientForSite));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightIPD() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "100px", null, null, DmPosition.CENTER, true);

        ////////////////////////////////1 Row//////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "20px", "left", null, DmPosition.NONE, false);
        flwTd.setStyleName("dm-StripLabel");
        lbl = new DmLabel("Room Posting At The Time Of Discharge");
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(12, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        ////////////////////////////////2 Row//////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mode");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIPDIsAutoRoom));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Time Format");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTimeFormatRoom));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        ////////////////////////////////3 Row//////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Checkout Time");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTimeCheckout));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Grace Minute In");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGraceMinuteIn));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Out");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGraceMinuteOut));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        ////////////////////////////////4 Row//////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("HD Upto Minute");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIPDHalfDayUpTo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Half Day Percentage");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPercentageRoom));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel tabDiagnostic() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "380px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "380px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "230px", "left", null, DmPosition.NONE, true);
        flwTd.add((leftDiagnostic()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "110px", "left", null, DmPosition.NONE, true);
        flwTd.add((rightDiagnostic()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftDiagnostic() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        Anchor aBtn;
        InlineLabel label;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "230px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("No. Of Sticker");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiagnosticNoOfSticker));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
//========================================================================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Existing Patient");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsExistingPatientDiagnostic));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
//================================<<<< 2nd Row >>>=========================================//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Direct Sample Lab Collection");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDirectSampleLabColl));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 3rd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Result Auto Verification");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsDiagAuto));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 4th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Print if Balance");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPrntYsNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 5th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Print On Letterhead");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPrintedOnLetterhead));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 6th Row >>>=========================================// 
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Addition");
        lbl.setWordWrap(true);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(TxtHDiagnosticRegAddition));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 7th Row >>>=========================================//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Cancel Days");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCancelDaysDiagnostic));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Diagnostic Days");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiagnosticDays));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Lab History View Days");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLabHistoryViewDays));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //-------------------------------strip--------------------------------------------
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
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sites");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "62%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDiagAllowPatientForSite));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "42%", null, "left", null, DmPosition.NONE, false);
//        DmClient.setStyle(flwUploadDocument, "50%", "25px", "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "43%", "25px", "left", "relative", DmPosition.NONE, true);
//        label = new InlineLabel("Upload Radiology Header Document");
//        label.getElement().setAttribute("style", label.getElement().getAttribute("style")
//                + ";font-size:11px;padding:0px;width:78px;");
        fileRuleTypeDocument = new DmFileRuleType();
//        fileRuleTypeDocument.setRuleCode(ProjectRuleCode.CAMERA.getRuleCode());
        fileRuleTypeDocument.setRuleCode(ProjectRuleCode.RADIOLOGY_TEST.getRuleCode());
        fileRuleTypeDocument.setFileSizeLimit(DmShared.iNull(ProjectRuleCode.RADIOLOGY_TEST.getSize()).toString());
        fileRuleTypeDocument.setFilters(ProjectRuleCode.RADIOLOGY_TEST.getFilters());
        fileRuleTypeDocument.setFileTypeName("Report");
//        btnUploadDocument = new DmFileUploadButton(null, fileRuleTypeDocument);
//        aBtn = new Anchor();
//
//        aBtn.getElement().getStyle().setFloat(Style.Float.LEFT);
//        btnUploadDocument.getButton().addStyleName("dm-button-grad");
//        btnUploadDocument.getButton().getElement().appendChild(aBtn.getElement());
//        btnUploadDocument.getButton().getElement().appendChild(label.getElement());
//        btnUploadDocument.getButton().setSize("215px", "24px");
//        btnUploadDocument.setTitle("File Type: DGRAD");
//        btnUploadDocument.getButton().getElement().getStyle().setPadding(0, Style.Unit.PX);
//        flwUploadDocument.add(vl.add(btnUploadDocument));
//        flwTd.add(flwUploadDocument);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
//        btnDownloadDocument.addStyleName("dm-button-grad");
//        btnDownloadDocument.setTitle("File Type: DGRAD");
//        flwDownloadDocument.add(btnDownloadDocument);
//        flwTd.add(flwDownloadDocument);
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "80px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add((iniDiagnosticFile())));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightDiagnostic() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "110px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================// 
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "20px", "left", null, DmPosition.NONE, false);
        flwTd.setStyleName("dm-StripLabel");
        lbl = new DmLabel("Diagnostic Accounting");
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(12, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 2nd Row >>>=========================================// 
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Diagnostic Control A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDiagnosticControlAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 3rd Row >>>=========================================// 
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Hospital A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDiagnosticHospitalAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Diagnostic A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDiagnosticSelfAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel tabDayCare() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "348px", null, null, DmPosition.CENTER, true);

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
        flwTd.add(vl.add(txtHDAYCAllowPatientForSite));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel tabInventory() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "380px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "380px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "20px", "left", null, DmPosition.NONE, true);
        flwTd.add((leftInventory()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "100px", "left", null, DmPosition.NONE, true);
//        flwTd.add((rightIPD()));      //Add right area
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftInventory() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "20px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Include Tax in cost");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIncludeTaxInCost));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 2nd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Posting Based On");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPurchaseSalesAcPostingBasedOn));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 3rd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Indent Required");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIndentRequired));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 4th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Requisition In Transfer");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRequisitionMandatoryInTransfer));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 5th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Single PO Flow");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSinglePOFlowInPurchase));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< 6th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Main Store");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHInvDefaultPurchaseDepartment));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //========================================================================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Bill Handle Approval");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsBillH_Approval));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //========================================================================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Pricelist Common For Site");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsCommonPricelist));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< ******* >>>=========================================//
        return flwTable;
    }

    private FlowPanel tabAsset() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "380px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "380px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "20px", "left", null, DmPosition.NONE, true);
        flwTd.add((leftAsset()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "100px", "left", null, DmPosition.NONE, true);
//        flwTd.add((rightIPD()));      //Add right area
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftAsset() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "20px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Depreciation Ac");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAssetDepreciationAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 2nd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Deprecition Post On");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepreciationPostOn));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //================================<<<< ******* >>>=========================================//
        return flwTable;
    }

    private FlowPanel tabOT() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "380px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "380px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "20px", "left", null, DmPosition.NONE, true);
//        flwTd.add((leftOT()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel tabDietary() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "380px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "380px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "130px", "left", null, DmPosition.NONE, true);
        flwTd.add((leftDietary()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftDietary() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "130px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Schdule Days");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDietScheduleDays));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Property-1");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDietaryProperty1));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 2nd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Property-2");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDietaryProperty2));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 3rd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Property-3");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDietaryProperty3));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<<<< 4th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Property-4");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDietaryProperty4));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 5th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Property-5");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDietaryProperty5));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //================================<<<< 6th Row >>>=========================================// 
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Property-6");
        lbl.setWordWrap(true);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDietaryProperty6));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel tabBloodbank() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "480px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Blood Bag Issue Department");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHBooldBagDepartment));
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);
//        //================================<<<< 2nd Row >>>=========================================//
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Create Component Department");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHComponentDepartment));
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Donor Default Camp");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDonorCampCode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);

        //================================<<<< 3nd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Addition (Component Issue)");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(TxtHBloodBankCipAddition));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel tabOnlinePortal() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "380px", null, null, DmPosition.CENTER, true);

        //================================<<<< 1st Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Appointment Book Upto");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAppointmentBookUptoDays));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Days");
        flwTd.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 2nd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Appointment Payment Gateway Allowed");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAppointmentPaymentGatewayAllowed));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 3nd Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Payment Gateway URL");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPaymentGatewayURL));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 4th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Payment Option");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPaymentOption));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 5th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Auto OPD Registration After Appointment");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAutoOPDRegAfterAppointment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 6th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Run From Website");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRunFromWebsite));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================<<<< 7th Row >>>=========================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Appointment Count MonthWise");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAppointmentCountMonthWise));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdDiagnosticFile">
    interface GrdDiagnosticFile_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdDiagnosticFile_Mdl> {

        @Editor.Path("FileName")
        ValueProvider<GrdDiagnosticFile_Mdl, String> fileName();
    }
    final DmGrid<GrdDiagnosticFile_Mdl, GrdDiagnosticFile_PrpAcs> grdDiagnosticFile = new DmGrid(GWT.create(GrdDiagnosticFile_PrpAcs.class));

    private Grid iniDiagnosticFile() {
        grdDiagnosticFile.addRowNumberColumn();
        grdDiagnosticFile.addTextColumn(grdDiagnosticFile.getPropertyAccess().name(), "File Type", 150, "");
        grdDiagnosticFile.addTextColumn(grdDiagnosticFile.getPropertyAccess().fileName(), "File Name", 400, "");
        grdDiagnosticFile.initializeGrid(grdDiagnosticFile.getPropertyAccess().fieldHashCode(), 600, 80);
        return grdDiagnosticFile.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdLinkDepartment">
    interface GrdLinkDepartment_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLinkDepartment_Mdl> {

        @Editor.Path("LinkSiteName")
        ValueProvider<GrdLinkDepartment_Mdl, String> linkSiteName();

    }
    final DmGrid<GrdLinkDepartment_Mdl, GrdLinkDepartment_PrpAcs> grdLinkDepartment = new DmGrid(GWT.create(GrdLinkDepartment_PrpAcs.class));

    private Grid iniLinkDepartment() {
        grdLinkDepartment.addRowNumberColumn();
        grdLinkDepartment.addTextColumn(grdLinkDepartment.getPropertyAccess().name(), "Department", 210, "");
        grdLinkDepartment.addTextColumn(grdLinkDepartment.getPropertyAccess().linkSiteName(), "Link Site", 280, "");
        grdLinkDepartment.initializeGrid(grdLinkDepartment.getPropertyAccess().fieldHashCode(), 510, 170, 2);
        return grdLinkDepartment.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdDischargeStatusGUI">
    interface GrdStatusGUITemp_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdStatusGUITemp_Mdl> {

        @Editor.Path("TemplateName")
        ValueProvider<GrdStatusGUITemp_Mdl, String> template();
    }
    final DmGrid<GrdStatusGUITemp_Mdl, GrdStatusGUITemp_PrpAcs> grdDischargeStatusGUI = new DmGrid(GWT.create(GrdStatusGUITemp_PrpAcs.class));

    private Grid iniStatusGUITemp() {
        grdDischargeStatusGUI.addTextColumn(grdDischargeStatusGUI.getPropertyAccess().name(), "Discharge Status", 150, "");
        grdDischargeStatusGUI.addTextColumn(grdDischargeStatusGUI.getPropertyAccess().template(), "GUI Template", 230, "");
        grdDischargeStatusGUI.initializeGrid(grdDischargeStatusGUI.getPropertyAccess().fieldHashCode(), 590, 227);
        return grdDischargeStatusGUI.getGrid();
    }
    //</editor-fold>

//    //<editor-fold defaultstate="collapsed" desc="Timing Detail Grid">
//    interface GrdTiming_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdTiming_Mdl> {
//
//        @Editor.Path("FromTime")
//        ValueProvider<GrdTiming_Mdl, String> from();
//
//        @Editor.Path("ToTime")
//        ValueProvider<GrdTiming_Mdl, String> toTime();
//
//        @Editor.Path("Opd")
//        ValueProvider<GrdTiming_Mdl, String> opd();
//
//        @Editor.Path("Emergency")
//        ValueProvider<GrdTiming_Mdl, String> emergency();
//
//        @Editor.Path("Appointment")
//        ValueProvider<GrdTiming_Mdl, String> appointment();
//
//        @Editor.Path("AppointmentAdvance")
//        ValueProvider<GrdTiming_Mdl, String> appointmentAdvance();
//
//        @Editor.Path("Patient")
//        ValueProvider<GrdTiming_Mdl, String> patient();
//
//        @Editor.Path("GraceMin")
//        ValueProvider<GrdTiming_Mdl, String> graceMin();
//
//        @Editor.Path("ReVisit")
//        ValueProvider<GrdTiming_Mdl, String> reVisit();
//
//        @Editor.Path("OnlineCharge")
//        ValueProvider<GrdTiming_Mdl, String> onlineCharge();
//
//        @Editor.Path("OnlinePatient")
//        ValueProvider<GrdTiming_Mdl, String> onllinePatient();
//    }
//    final DmGrid<GrdTiming_Mdl, GrdTiming_PrpAcs> grdTimingDetail = new DmGrid(GWT.create(GrdTiming_PrpAcs.class));
//
//    private Grid iniTabOPDTimeDetail() {
//        grdTimingDetail.addRowNumberColumn();
//        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().name(), "Day", 130, null);
//        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().from(), "From Time", 80, null);
//        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().toTime(), "To Time", 80, null);
//        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().graceMin(), "Grace Mins", 100, null);
//        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().opd(), "OPD", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().emergency(), "Emergency", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().appointment(), "Appointment", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().appointmentAdvance(), "Appointment Adv.", 150, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().reVisit(), "Re-Visit", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().onlineCharge(), "Online", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().patient(), "No. of Pat.", 80, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().onllinePatient(), "Online Pat.", 80, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdTimingDetail.addBeforeInitializeHandler(new DmGrid.BeforeInitialize() {
//            @Override
//            public void beforeInitialize(ColumnModel columnModel) {
//                columnModel.addHeaderGroup(0, 5, new HeaderGroupConfig("Fees", 1, 6));
//            }
//        });
//
//        grdTimingDetail.initializeGrid(grdTimingDetail.getPropertyAccess().fieldHashCode(), 1260, 146);
//
//        return grdTimingDetail.getGrid();
//    }
//    //</editor-fold>
}
