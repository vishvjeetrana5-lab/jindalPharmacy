package aarogya.pharmacy.saleBill.client;

import aarogya.pharmacy.saleBill.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.taxSummary.client.TaxSummary;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import aarogya.selfToolkit.uhidPopup.shared.UhidPopup_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import java.util.HashMap;

abstract class SaleBill_Dsg extends IsWidgetExtended {

    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String rateEditableBlog = "Rate Editable";
    public final static String dateEditableBlog = "Date Editable";
    public final static String discountEditableBlog = "Discount Editable";
    public final static String schemeEditableBlog = "Scheme Editable";
    public final static String saleReturnBlog = "Sale Return";
    public final static String departmentEditableBlog = "Department Editable";
    public final static String ctpAllowBlog = "CTP Allow";
    public final static String ctoAllowBlog = "CTO Allow";
    public final static String ctrAllowBlog = "CTR Allow";
    public final static String receiptAllowBlog = "Receipt Allow";
    public final static String opdAllowBlog = "OPD Allow";
    public final static String ipdAllowBlog = "IPD Allow";
    public final static String dayCareAllowBlog = "Day Care Allow";
    public final static String emergencyAllowBlog = "Emergency Allow";
    public final static String walkInAllowBlog = "Walk-In Allow";
    public final static String ledgerAllowBlog = "Ledger Allow";
    public final static String uhidAllowBlog = "UHID Allow";
    public final static String addBatchBlog = "Add Batch";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtBillNo = new DmTextBox(30, false);
    final DmTextBox txtDate = new DmTextBox(11, true);
    final DmTextBox txtPatient = new DmTextBox(50, true);
    final DmTextBox txtRegNo = new DmTextBox(35, false);
    final DmTextBox txtUHIDNo = new DmTextBox(35, false);
//    final DmTextBox txtRemark = new DmTextBox(200, false);

    final DmRemarkTextBox txtAddress = new DmRemarkTextBox(250, false);
    final DmTextBox txtIdNo = new DmTextBox(50, false);
    final DmTextBoxMobile txtMobile = new DmTextBoxMobile(true);

    final DmTextBox txtReciptNo = new DmTextBox(50, false);
    final DmTextBox txtReciptType = new DmTextBox(50, false);
    final DmTextBox txtOrganisation = new DmTextBox();

    final DmTextBoxNumeric txtItemGross = new DmTextBoxNumeric(2, true, 12, false);
    final DmLabel labelBillDiscount = new DmLabel("Bill Discount");
    final DmLabel lblPharmacyBalance = new DmLabel("");
    final DmTextBoxNumeric txtDiscount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtDiscount2 = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtTaxableAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtCGSTAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSgstUgstAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtBillAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtRoundoff = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtNetAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtTaxAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtMRP_Amount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtDiffPercentOfAmountWithMRP = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtDiscountOnMRPValue = new DmTextBoxNumeric(2, true, 12, false);
    final DmLabel lblSgstUgst = new DmLabel("");
    final DmLabel lblPatientClass = new DmLabel("");
    final DmLabel lblPackegedInfo = new DmLabel("");

    final Button btnPayment = new Button("Payment Mode");
    final FlowPanel flwPayment = new FlowPanel();

    final DmLabel specialRemarks = new DmLabel("");
    final FlowPanel flwRemark = new FlowPanel();

    final FlowPanel flwPrescription = new FlowPanel();
    final Button btnPrescription = new Button("");
    final DmLabel lblDiscarded = new DmLabel("");
//    final DmLabel lblRegistrationBalance = new DmLabel("");

    final Button btnOPDSearch = new Button("...");
    final Button btnRequisitionSearch = new Button("");
    final FlowPanel flwRequisitionSearch = new FlowPanel();
    final FlowPanel flwSearchRoom = new FlowPanel();
    final FlowPanel flwRegID_UHID = new FlowPanel();
    final FlowPanel flwOldOpdNo = new FlowPanel();
    final Button btnSearchRoom = new Button("...");
    final Button btnLedgerSearch = new Button("...");
    final FlowPanel flwPatientSearch = new FlowPanel();
    final FlowPanel flwPatientSearchTd = new FlowPanel();
    final Button btnPatientSearch = new Button("Patient Search");
    final Button btnOrganisation = new Button("...");
    final Button btnremarks = new Button("...");

    final TaxSummary taxSummary = new TaxSummary(595, 140, 2);

    final FlowPanel flwEstimate = new FlowPanel();

    public boolean isEstimate = false;
    final FlowPanel flwSearchUHID = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHOldOpd">
    interface TxtHOldOpd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHOldOpd_Mdl> {

        @Editor.Path("Date")
        ValueProvider<TxtHOldOpd_Mdl, String> date();

        @Editor.Path("Consultant")
        ValueProvider<TxtHOldOpd_Mdl, String> consultant();
    }
    public final DmTextHelp<TxtHOldOpd_Mdl, TxtHOldOpd_PrpAcs> txtHOldOpd = new DmTextHelp(GWT.create(TxtHOldOpd_PrpAcs.class), 5, true);

    private void iniTxtHOldOpd() {
        txtHOldOpd.getDmGrid().addTextColumn(txtHOldOpd.getDmGrid().getPropertyAccess().name(), "RegNo.", 0, "OPDNo");
        txtHOldOpd.getDmGrid().addTextColumn(txtHOldOpd.getDmGrid().getPropertyAccess().consultant(), "Consultant", 150, "consultant");
        txtHOldOpd.getDmGrid().addTextColumn(txtHOldOpd.getDmGrid().getPropertyAccess().date(), "Date", 150, "v_Date");
        txtHOldOpd.getDmGrid().initializeGrid(txtHOldOpd.getDmGrid().getPropertyAccess().fieldHashCode(), 400, 0, 1);
        txtHOldOpd.getHelper().addSort("v_Date", DmSortType.DESC);
        txtHOldOpd.getHelper().addSort("OPDNo", DmSortType.DESC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHUHID">
    interface TxtHUHID_PrpAcs extends KeyField_T_PrpAcs<UhidPopup_Mdl> {

        @Editor.Path("DocIdUHID")
        ValueProvider<UhidPopup_Mdl, String> docIdUHID();

        @Editor.Path("RecIdUHID")
        ValueProvider<UhidPopup_Mdl, String> recIdUHID();

        @Editor.Path("FirstName")
        ValueProvider<UhidPopup_Mdl, String> firstName();

        @Editor.Path("MiddleName")
        ValueProvider<UhidPopup_Mdl, String> middleName();

        @Editor.Path("LastName")
        ValueProvider<UhidPopup_Mdl, String> lastName();

        @Editor.Path("MobileNo")
        ValueProvider<UhidPopup_Mdl, String> mobileNo();

    }
    final DmTextHelp<UhidPopup_Mdl, TxtHUHID_PrpAcs> txtHUHID = new DmTextHelp(GWT.create(TxtHUHID_PrpAcs.class), 6, true, true);

    private void iniTxtHUHID() {
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().recIdUHID(), "UHID", 100, "uhid");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().firstName(), "First Name", 100, "patientFirstName");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().middleName(), "Middle Name", 100, "patientMiddleName");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().lastName(), "Last Name", 100, "patientLastName");
        txtHUHID.getDmGrid().addTextColumn(txtHUHID.getDmGrid().getPropertyAccess().mobileNo(), "Mobile", 100, "mobile");
        txtHUHID.getDmGrid().initializeGrid(txtHUHID.getDmGrid().getPropertyAccess().code(), 520, 0, 1);

        txtHUHID.getHelper().addSort("uhid", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHVType">
    interface TxtHVType_PrpAcs extends KeyField_T_PrpAcs<TxtHVType_Mdl> {

        @Editor.Path("DocumentNatureCode")
        ValueProvider<TxtHVType_Mdl, String> documentNatureCode();

        @Editor.Path("DocumentTypeCode")
        ValueProvider<TxtHVType_Mdl, String> documentTypeCode();

    }
    final DmTextHelp<TxtHVType_Mdl, TxtHVType_PrpAcs> txtHVType = new DmTextHelp(GWT.create(TxtHVType_PrpAcs.class), 7, true);

    private void iniTxtHVType() {
        txtHVType.getDmGrid().addTextColumn(txtHVType.getDmGrid().getPropertyAccess().name(), "Name", 0, "Description");
        txtHVType.getDmGrid().initializeGrid(txtHVType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHVType.getHelper().addSort("Description", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, true);

    private void iniTxtHCustomerType() {
        txtHType.getDmGrid().addTextColumn(txtHType.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHType.getDmGrid().initializeGrid(txtHType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtRemark = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHtxtRemark() {
        txtRemark.getDmGrid().addTextColumn(txtRemark.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtRemark.getDmGrid().initializeGrid(txtRemark.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHDepartment.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHConsutant">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHConsultant = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHConsultant() {
        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHConsultant.getDmGrid().initializeGrid(txtHConsultant.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHConsultant.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRequisition">
//    interface TxtHRequisition_PrpAcs extends KeyField_T_PrpAcs<TxtHRequisition_Mdl> {
//
//        @Editor.Path("DepartmentName")
//        ValueProvider<TxtHRequisition_Mdl, String> department();
//
//        @Editor.Path("RequisitionBy")
//        ValueProvider<TxtHRequisition_Mdl, String> requisitionBy();
//
//        @Editor.Path("PatientName")
//        ValueProvider<TxtHRequisition_Mdl, String> patient();
//
//        @Editor.Path("roomNo")
//        ValueProvider<TxtHRequisition_Mdl, String> roomNo();
//
//        @Editor.Path("RequisitionDate")
//        ValueProvider<TxtHRequisition_Mdl, String> dateReq();
//
//        @Editor.Path("RecIdRegistration")
//        ValueProvider<TxtHRequisition_Mdl, String> regNo();
//
//    }
//    final DmTextHelp<TxtHRequisition_Mdl, TxtHRequisition_PrpAcs> txtHRequisition = new DmTextHelp(GWT.create(TxtHRequisition_PrpAcs.class), 7, false);
//
//    private void iniTxtHRequisition() {
//        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().name(), "Requisition", 0, "recId");
//        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().dateReq(), "Date", 75, "v_Date", DmDataType.DATE);
//        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().requisitionBy(), "Requisition By", 100, "requestedByName");
//        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().department(), "Department By", 100, "department");
//        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().patient(), "Patient Name", 125, "patientName");
//        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().regNo(), "IPD No.", 80, "regNo");
//        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().roomNo(), "Room No.", 60, "roomNo");
//        txtHRequisition.getDmGrid().initializeGrid(txtHRequisition.getDmGrid().getPropertyAccess().code(), 675, 0, 1);
//
//        txtHRequisition.getHelper().addSort("v_Date", DmSortType.DESC);
//        txtHRequisition.getHelper().addSort("short_Name", DmSortType.ASC);
//        txtHRequisition.getHelper().addSort("v_No", DmSortType.DESC);
//        txtHRequisition.getHelper().addSort("recId", DmSortType.ASC);
//    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHEstimate">
    interface TxtHEstimate_PrpAcs extends KeyField_T_PrpAcs<TxtHEstimate_Mdl> {

        @Editor.Path("DepartmentName")
        ValueProvider<TxtHEstimate_Mdl, String> department();

        @Editor.Path("Patient")
        ValueProvider<TxtHEstimate_Mdl, String> patient();

        @Editor.Path("EstimateDate")
        ValueProvider<TxtHEstimate_Mdl, String> dateEstimate();
    }
    final DmTextHelp<TxtHEstimate_Mdl, TxtHEstimate_PrpAcs> txtHEstimate = new DmTextHelp(GWT.create(TxtHEstimate_PrpAcs.class), 7, false);

    private void iniEstimate() {
        txtHEstimate.getDmGrid().addTextColumn(txtHEstimate.getDmGrid().getPropertyAccess().name(), "Estimate", 0, "recId");
        txtHEstimate.getDmGrid().addTextColumn(txtHEstimate.getDmGrid().getPropertyAccess().dateEstimate(), "Date", 75, "v_Date", DmDataType.DATE);
        txtHEstimate.getDmGrid().addTextColumn(txtHEstimate.getDmGrid().getPropertyAccess().department(), "Department By", 200, "department");
        txtHEstimate.getDmGrid().addTextColumn(txtHEstimate.getDmGrid().getPropertyAccess().patient(), "Patient Name", 225, "patientName");
        txtHEstimate.getDmGrid().initializeGrid(txtHEstimate.getDmGrid().getPropertyAccess().code(), 675, 0, 1);

        txtHEstimate.getHelper().addSort("v_Date", DmSortType.DESC);
        txtHEstimate.getHelper().addSort("short_Name", DmSortType.ASC);
        txtHEstimate.getHelper().addSort("v_No", DmSortType.DESC);
        txtHEstimate.getHelper().addSort("recId", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOperation">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOperation = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHOperation() {
        txtHOperation.getDmGrid().addTextColumn(txtHOperation.getDmGrid().getPropertyAccess().name(), "Operation", 0, "name");
        txtHOperation.getDmGrid().initializeGrid(txtHOperation.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHOperation.getHelper().addSort("name", DmSortType.DESC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedicine">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMedicine = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHMedicine() {
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().name(), "Medicine", 0, "name");
        txtHMedicine.getDmGrid().initializeGrid(txtHMedicine.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHMedicine.getHelper().addSort("name", DmSortType.DESC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCity">
    interface TxtHCity_Mdl_PrpAcs extends KeyField_T_PrpAcs<TxtHCity_Mdl> {

        @Editor.Path("StateName")
        ValueProvider<TxtHCity_Mdl, String> stateName();

        @Editor.Path("CountryName")
        ValueProvider<TxtHCity_Mdl, String> countryName();

    }

    final DmTextHelp<TxtHCity_Mdl, TxtHCity_Mdl_PrpAcs> txtHCity = new DmTextHelp(GWT.create(TxtHCity_Mdl_PrpAcs.class), 4, false);

    private void iniTxtHCity() {
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().name(), "City", 0, "cityName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().stateName(), "State", 100, "stateName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "countryName");
        txtHCity.getDmGrid().initializeGrid(txtHCity.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHCity.getHelper().addSort("CityName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Date")
        ValueProvider<Find_Mdl, String> date();

        @Editor.Path("UhidNo")
        ValueProvider<Find_Mdl, String> uhidNo();

        @Editor.Path("OpdIPDNO")
        ValueProvider<Find_Mdl, String> opdNO();

        @Editor.Path("ConsDocName")
        ValueProvider<Find_Mdl, String> consDocName();

        @Editor.Path("PatientName")
        ValueProvider<Find_Mdl, String> patientName();

        @Editor.Path("DeptName")
        ValueProvider<Find_Mdl, String> deptName();

        @Editor.Path("Status")
        ValueProvider<Find_Mdl, String> status();

        @Editor.Path("PreparedBy")
        ValueProvider<Find_Mdl, String> preparedBy();

        @Editor.Path("preparedDate")
        ValueProvider<Find_Mdl, String> preparedDate();

        @Editor.Path("preparedTime")
        ValueProvider<Find_Mdl, String> preparedTime();

        @Editor.Path("estimateNo")
        ValueProvider<Find_Mdl, String> EstimateNo();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        ColumnConfig<Find_Mdl, String> discarded;
        String caption = "";
        if (isEstimate) {
            caption = "Estimate No.";
        } else {
            caption = "Bill No.";
        }
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), caption, 150, "BillNo");
        if (!isEstimate) {
            dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().EstimateNo(), "Estimate No.", 100, "estimateNo");
        } else {
            dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().EstimateNo(), "Estimate No.", 0, "estimateNo");
        }

        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().date(), "Date", 80, "v_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().uhidNo(), "UHID No.", 0, "UHIDNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().opdNO(), "OPD/IPD No.", 0, "regNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().consDocName(), "Consultant", 200, "consultantName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().patientName(), "Patient Name", 150, "patientName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().deptName(), "Department", 150, "departmentName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().preparedBy(), "Preapared By", 150, "preparedBy");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().preparedDate(), "Entry Date", 80, "preparedDate", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().preparedTime(), "Entry Time", 40, "preparedTime", DmDataType.DOUBLE);
        discarded = dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().status(), "Status", 80, "status");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 7);

        discarded.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='color : red;'>" + DmShared.xNull(value) + "</div>");
            }
        });

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
        dmFind.getHelper().addSort("v_no", DmSortType.DESC, "Serial");
        dmFind.getHelper().addSort("BillNo", DmSortType.DESC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHIdType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIdType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 8, false);

    private void iniTxtHIdType() {
        txtHIdType.getDmGrid().addTextColumn(txtHIdType.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHIdType.getDmGrid().initializeGrid(txtHIdType.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHIdType.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    private boolean isSaleBill = false;
    public String widgetName = "", voucher_Cat = "";

    public SaleBill_Dsg(HashMap paramHashMap) {
        if (paramHashMap != null) {
            widgetName = (String) paramHashMap.get("widgetName");
            voucher_Cat = (String) paramHashMap.get("vCategory");
        } else {
            widgetName = new pmPharmacy().SALE_BILL.caption;
        }
        if (DmShared.xNull(voucher_Cat).equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_ESTIMATE)) {
            isEstimate = true;
        } else {
            isEstimate = false;
        }

        isSaleBill = widgetName.equalsIgnoreCase(new pmPharmacy().SALE_BILL.caption);
        ini();
        iniTxtHVType();
        iniTxtHConsultant();
        iniTxtHDepartment();
        iniTxtHCustomerType();
//        iniTxtHRequisition();
        iniTxtHOperation();
        iniEstimate();
        iniTxtHIdType();
        iniTxtHCity();
        iniTxtHMedicine();
        iniTxtHUHID();
        iniTxtHOldOpd();
        iniTxtHtxtRemark();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        flw = new FlowPanel();
        flw.getElement().getStyle().setFloat(Style.Float.LEFT);
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(iniUserControl());

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmUserControl iniUserControl() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGeneralBlog(lblDiscarded);
        lblDiscarded.setHeight("15Px");
        lblDiscarded.getElement().getStyle().setColor("red");
        lblDiscarded.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblDiscarded.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblDiscarded.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        lblDiscarded.getElement().getStyle().setFontSize(12, Style.Unit.PX);

        userControl.addGap(10);
        lblPackegedInfo.setHeight("15px");
        userControl.addGeneralBlog(lblPackegedInfo);
        lblPackegedInfo.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblPackegedInfo.addStyleName("blink");

        userControl.addGap(15);
        userControl.addGeneralBlog(lblPharmacyBalance);
        lblPharmacyBalance.setHeight("15Px");
        lblPharmacyBalance.getElement().getStyle().setColor("red");
        lblPharmacyBalance.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblPharmacyBalance.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblPharmacyBalance.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        lblPharmacyBalance.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        userControl.addGap(100);
        DmClient.setStyle(flwPayment, "100%", null, "left", null, DmPosition.NONE, true);
        flwPayment.add(btnPayment);
        btnPayment.setWidth("100px");
        userControl.addGeneralBlog(flwPayment);

        userControl.addTextBlog(modifiedByBlog, 100, !isSaleBill, false);
        return userControl;

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "520px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "115px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "115px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftEntryArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "115px", "left", null, DmPosition.NONE, true);
        flwTd.add(rightEntryArea());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "222px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGridMedicineDetails()));
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "170px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "170px", "left", null, DmPosition.NONE, true);
        flwTd.add(idTypeSection());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "170px", "left", null, DmPosition.NONE, true);
        flwTd.add(amountSection());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftEntryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        HorizontalPanel hrtPnl;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "100px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Pharmacy Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
//-------------------------------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);

        DmClient.setStyle(flwRegID_UHID, "100%", null, "left", null, DmPosition.NONE, false);
//        flwRegID_UHID.add(vl.add(txtRegNo));
        flwRegID_UHID.add(vl.add(txtHUHID));
        hrtPnl.add(flwRegID_UHID);
//        hrtPnl.add(txtRegNo);
//        hrtPnl.setCellWidth(txtRegNo, "100%");

//        hrtPnl.add(txtHUHID);
//        hrtPnl.setCellWidth(txtHUHID, "100%");
        DmClient.setStyle(flwSearchRoom, "100%", "20px", "left", null, DmPosition.NONE, true);

        btnSearchRoom.setWidth("20px");
        btnSearchRoom.setHeight("20px");
        btnSearchRoom.getElement().setAttribute("style", btnSearchRoom.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnSearchRoom.setTitle("Patient Search");
        flwSearchRoom.add(btnSearchRoom);

        btnOPDSearch.setWidth("20px");
        btnOPDSearch.setHeight("20px");
        btnOPDSearch.getElement().setAttribute("style", btnOPDSearch.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnOPDSearch.setTitle("Patient Search (OPD)");
        flwSearchRoom.add(btnOPDSearch);

        btnLedgerSearch.setWidth("20px");
        btnLedgerSearch.setHeight("20px");
        btnLedgerSearch.getElement().setAttribute("style", btnLedgerSearch.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnLedgerSearch.setTitle("Ledger Search");
        flwSearchRoom.add(btnLedgerSearch);

        hrtPnl.add(flwSearchRoom);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblPatientClass.setText("");
        lblPatientClass.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblPatientClass.addStyleName("blink");
        flwTd.add(lblPatientClass);
        flwTr.add(flwTd);

        DmClient.setStyle(flwPatientSearchTd, "30%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwPatientSearch, "60%", null, "right", null, DmPosition.NONE, false);

        btnPatientSearch.setWidth("140px");
        btnPatientSearch.setHeight("20px");
        btnPatientSearch.getElement().setAttribute("style", btnPatientSearch.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        flwPatientSearch.add(btnPatientSearch);
        flwPatientSearchTd.add(flwPatientSearch);
        flwTr.add(flwPatientSearchTd);

        flwTable.add(flwTr);

        flwTable.add(flwTr);

        //----------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Patient Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPatient));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //----------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("City");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCity));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

//-------------------------------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Consultant");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtHConsultant));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("UHID No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtUHIDNo);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightEntryArea() {
        FlowPanel flwTable, flwTr, flwTd;

        DmLabel lbl;
        InlineLabel label;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "100px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtDate);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        if (isEstimate) {
            lbl = new DmLabel("Estimate No.");
        } else {
            lbl = new DmLabel("Bill No.");
        }
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(47, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtBillNo);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
//------------------------------------------------------------------------------------------------------------------//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        if (!isEstimate) {
            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
            lbl = new DmLabel("Estimate No.");
            flwTd.add(lbl);
            flwTr.add(flwTd);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
            flwTd.add(txtHEstimate);
            flwTr.add(flwTd);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
            lbl = new DmLabel("Organisation");
            lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
            flwTd.add(lbl);
            flwTr.add(flwTd);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
            btnOrganisation.setWidth("20px");
            btnOrganisation.setHeight("20px");
            txtOrganisation.getElement().getStyle().setWidth(84, Style.Unit.PCT);
            flwTd.add(txtOrganisation);
            flwTd.add(btnOrganisation);
            flwTr.add(flwTd);
        } else {
            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
            lbl = new DmLabel("Organisation");
            lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
            flwTd.add(lbl);
            flwTr.add(flwTd);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, true);
            btnOrganisation.setWidth("20px");
            btnOrganisation.setHeight("20px");
            txtOrganisation.getElement().getStyle().setWidth(87.5, Style.Unit.PCT);
            flwTd.add(txtOrganisation);
            flwTd.add(btnOrganisation);
            flwTr.add(flwTd);
        }

        flwTable.add(flwTr);
//--------------------------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Operation");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtHOperation);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        btnremarks.setWidth("20px");
        btnremarks.setHeight("20px");
//        txtRemark.getElement().getStyle().setWidth(84, Style.Unit.PCT);
        flwTd.add(vl.add(txtRemark));
//        flwTd.add(btnremarks);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
//-------------------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtHMedicine);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Old Opd No.");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(flwOldOpdNo);
        flwOldOpdNo.add(txtHOldOpd);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        label = new InlineLabel("Prescription");
        label.setWidth("185px");
        flwPrescription.add(btnPrescription);
        btnPrescription.getElement().appendChild(label.getElement());
        btnPrescription.getElement().setAttribute("Style", "width:98%;height:20px;font-family:Arial Unicode MS,Arial,sans-serif;padding-top:0;padding-bottom:0;font-size:12px;");
        flwTd.add(flwPrescription);
//        flwTr.add(flwTd);
        //--------------< Remove Prescription button >-------------------------
        flwTable.add(flwTr);
//---------------------------------------------------------------------------------------------------------------------
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        label = new InlineLabel("Add Requisition");
        label.setWidth("185px");
        flwRequisitionSearch.add(btnRequisitionSearch);
        btnRequisitionSearch.getElement().appendChild(label.getElement());
        btnRequisitionSearch.getElement().setAttribute("Style", "width:100%;height:20px;font-family:Arial Unicode MS,Arial,sans-serif;padding-top:0;padding-bottom:0;font-size:12px;");
        flwTd.add(flwRequisitionSearch);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel amountSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "170px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gross Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtItemGross));
        txtItemGross.getElement().setAttribute("style", txtItemGross.getElement().getAttribute("style")
                + ";text-alignt:right; ");
        txtItemGross.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Discount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiscount));
        txtDiscount.getElement().setAttribute("style", txtDiscount.getElement().getAttribute("style")
                + ";text-alignt:right; ");
        txtDiscount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(labelBillDiscount);
        labelBillDiscount.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        labelBillDiscount.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        labelBillDiscount.getElement().getStyle().setColor("blue");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtDiscount2);
        txtDiscount2.getElement().setAttribute("style", txtDiscount.getElement().getAttribute("style")
                + ";text-alignt:right; ");
        txtDiscount2.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        txtDiscount2.setEnabled(false);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        flwTd.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Taxable Amount");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtTaxableAmt));
//        txtTaxableAmt.getElement().setAttribute("style", txtTaxableAmt.getElement().getAttribute("style")
//                + ";text-alignt:right; ");
//        txtTaxableAmt.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
//        flwTr.add(flwTd);
//
//        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Tax Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTaxAmount));
        txtTaxAmount.getElement().setAttribute("style", txtTaxAmount.getElement().getAttribute("style")
                + ";text-alignt:right; ");
        txtTaxAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        flwTd.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Bill Amount");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtBillAmt));
//        txtBillAmt.getElement().setAttribute("style", txtBillAmt.getElement().getAttribute("style")
//                + ";text-alignt:right; ");
//        txtBillAmt.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
//        flwTr.add(flwTd);
//
//        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Round off");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRoundoff));
        txtRoundoff.getElement().setAttribute("style", txtRoundoff.getElement().getAttribute("style")
                + ";text-alignt:right; ");
        txtRoundoff.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Net Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNetAmount));
        txtNetAmount.getElement().setAttribute("style", txtNetAmount.getElement().getAttribute("style")
                + ";text-alignt:right; font-weight: bold; ");
        txtNetAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /**
         * *********************MRP Wise Valuation and Difference % with Sale
         * Rate**********************
         */
        if (isEstimate) {
            flwTr = new FlowPanel();
            DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

            flwTd = new FlowPanel();
            flwTd.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
            DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
            lbl = new DmLabel("MRP Value");
            flwTd.add(lbl);
            flwTr.add(flwTd);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
            flwTd.add(vl.add(txtMRP_Amount));
            txtMRP_Amount.getElement().setAttribute("style", txtMRP_Amount.getElement().getAttribute("style")
                    + ";text-alignt:right; font-weight: bold; ");
            txtMRP_Amount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            flwTr.add(flwTd);

            flwTd = new FlowPanel();
            flwTd.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
            DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
            lbl = new DmLabel("Dicount Value & %");
            flwTd.add(lbl);
            flwTr.add(flwTd);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
            flwTd.add(vl.add(txtDiffPercentOfAmountWithMRP));
            txtDiffPercentOfAmountWithMRP.getElement().setAttribute("style", txtDiffPercentOfAmountWithMRP.getElement().getAttribute("style")
                    + ";text-alignt:right; font-weight: bold; ");
            txtDiffPercentOfAmountWithMRP.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            flwTr.add(flwTd);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
            flwTd.add(vl.add(txtDiscountOnMRPValue));
            txtDiscountOnMRPValue.getElement().setAttribute("style", txtDiscountOnMRPValue.getElement().getAttribute("style")
                    + ";text-alignt:right; font-weight: bold; ");
            txtDiscountOnMRPValue.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

            flwTr.add(flwTd);

            flwTable.add(flwTr);
        }

        return flwTable;
    }

    private FlowPanel idTypeSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "170px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ID Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIdType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "54%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTd.add(vl.add(txtIdNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
//------------------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

//--------------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, null, null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(taxSummary));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Medicine Details">
    interface GrdMediDetails_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMedicine_Mdl> {

        @Editor.Path("Quantity")
        ValueProvider<GrdMedicine_Mdl, String> quantity();

        @Editor.Path("FreeQuantityInSaleUnit")
        ValueProvider<GrdMedicine_Mdl, String> freeQuantityInSaleUnit();

        @Editor.Path("Unit")
        ValueProvider<GrdMedicine_Mdl, String> saleUnitName();

        @Editor.Path("Rate")
        ValueProvider<GrdMedicine_Mdl, String> rate();

        @Editor.Path("NetAmt")
        ValueProvider<GrdMedicine_Mdl, String> amount();

        @Editor.Path("Amount")
        ValueProvider<GrdMedicine_Mdl, String> grossAmount();

        @Editor.Path("SgstAmt")
        ValueProvider<GrdMedicine_Mdl, String> sgstAmt();

        @Editor.Path("SgstPer")
        ValueProvider<GrdMedicine_Mdl, String> sgstPer();

        @Editor.Path("TotalDiscount")
        ValueProvider<GrdMedicine_Mdl, String> totalDiscount();

        @Editor.Path("CgstPer")
        ValueProvider<GrdMedicine_Mdl, String> cgstPer();

        @Editor.Path("CgstAmt")
        ValueProvider<GrdMedicine_Mdl, String> cgstAmt();

        @Editor.Path("RequisitionNo")
        ValueProvider<GrdMedicine_Mdl, String> requisitionNo();

        @Editor.Path("BatchNo")
        ValueProvider<GrdMedicine_Mdl, String> batchNo();

        @Editor.Path("Mrp")
        ValueProvider<GrdMedicine_Mdl, String> mrp();

        @Editor.Path("ExpDate")
        ValueProvider<GrdMedicine_Mdl, String> expDate();

        @Editor.Path("GstPer")
        ValueProvider<GrdMedicine_Mdl, String> gstPer();

        @Editor.Path("GstAmount")
        ValueProvider<GrdMedicine_Mdl, String> gstAmount();

    }
    final DmGrid<GrdMedicine_Mdl, GrdMediDetails_PrpAcs> grdMediDetails = new DmGrid(GWT.create(GrdMediDetails_PrpAcs.class));

    private Grid iniGridMedicineDetails() {
        DmColumnConfig<GrdMedicine_Mdl, String> colSaleRate, colAmount;
        grdMediDetails.addRowNumberColumn();
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().name(), "Medicine Name", 150, "");
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().batchNo(), "Batch No.", 70, "");
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().expDate(), "Exp. Date", 80, "");
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().saleUnitName(), "Unit", 60, "");
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().quantity(), "Qty.", 60, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().freeQuantityInSaleUnit(), "F.Qty.", 60, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().mrp(), "MRP/Pack", 80, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colSaleRate = grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().rate(), "S.Rate/Pack", 90, "", DmDataType.DOUBLE);
        colSaleRate.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().grossAmount(), "Gross Amt.", 80, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().totalDiscount(), "Discount", 70, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().gstPer(), "GST %", 60, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().gstAmount(), "Tax Amt.", 80, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colAmount = grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().amount(), "Net Amt.", 100, "");
        colAmount.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().requisitionNo(), "Requisition No.", 100, "");
        grdMediDetails.initializeGrid(grdMediDetails.getPropertyAccess().fieldHashCode(), 1280, 217, 2);
        colAmount.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant(DmClient.vNull(value, "0.00"));
            }
        });
        colSaleRate.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant(DmClient.vNull(value, "0.00"));
            }
        });

        grdMediDetails.getGrid().getView().setViewConfig(new GridViewConfig<GrdMedicine_Mdl>() {
            @Override
            public String getColStyle(GrdMedicine_Mdl model, ValueProvider<? super GrdMedicine_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.iNull(model.getStatus()).intValue() == 2) {
                    styleClass += "dm-GridCellDairyConfirm ";
                } else if (DmShared.vNull(model.getStatus()) == 1) {
                    styleClass += "dm-GridCellDisable ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdMedicine_Mdl model, int rowIndex) {
                return "";
            }
        });
        return grdMediDetails.getGrid();
    }
    //</editor-fold>
}
