package aarogya.pharmacy.saleBillReturn.client;

import aarogya.pharmacy.saleBillReturn.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.taxSummary.client.TaxSummary;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
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
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
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

abstract class SaleBillReturn_Dsg extends IsWidgetExtended {

    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String rateEditableBlog = "Rate Editable";
    public final static String dateEditableBlog = "Date Editable";
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
    public final static String paymentModeBlog = "Payment Mode";
    public final static String discountEditableBlog = "Discount Editable";
    public final static String medicineSearchBlog = "Medicine Search";

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
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(100, false);

    final DmTextBox txtReciptNo = new DmTextBox(50, false);
    final DmTextBox txtReciptType = new DmTextBox(50, false);
    final DmTextBox txtIdNo = new DmTextBox(50, false);
    final DmTextBox txtOrganisation = new DmTextBox();
    final DmTextBoxMobile txtMobile = new DmTextBoxMobile(false);
    final DmRemarkTextBox txtAddress = new DmRemarkTextBox(250, false);

    final DmTextBoxNumeric txtItemGross = new DmTextBoxNumeric(2, true, 12, false);
    final DmLabel labelDiscount = new DmLabel("Medicine Discount");
    final DmTextBoxNumeric txtDiscount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtDiscount2 = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtTaxableAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtCGSTAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSgstUgstAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtBillAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtRoundoff = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtNetAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmLabel lblSgstUgst = new DmLabel("");
    final DmTextBoxNumeric txtTaxAmount = new DmTextBoxNumeric(2, true, 12, false);

    final Button btnPayment = new Button("Payment Mode");
    final Button btnLedgerSearch = new Button("...");
    final FlowPanel flwPayment = new FlowPanel();
    final DmLabel specialRemarks = new DmLabel("");
    final FlowPanel flwRemark = new FlowPanel();
    final DmLabel lblDiscarded = new DmLabel("");

    final Button btnOPDSearch = new Button("...");
    final Button btnMedicineSearch = new Button("");
    final FlowPanel flwSearchRoom = new FlowPanel();
    final Button btnSearchRoom = new Button("...");
    final Button btnOrganisation = new Button("...");
    final FlowPanel flwPatientSearch = new FlowPanel();
    final FlowPanel flwPatientSearchTd = new FlowPanel();
    final Button btnPatientSearch = new Button("Patient Search");

    final DmTextBox txtRefDate = new DmTextBox(50, false);
    final DmTextBox txtRefNo = new DmTextBox(16, false);

    final FlowPanel flwSaleBillSearch = new FlowPanel();
    final Button btnSaleBillSearch = new Button("");

    final FlowPanel flwReturnRequestSearch = new FlowPanel();
    final Button btnReturnRequestSearch = new Button("");
    final FlowPanel flwMedicineSearch = new FlowPanel();

    final DmLabel lblPharmacyBalance = new DmLabel("");
    final TaxSummary taxSummary = new TaxSummary(595, 140, 2);

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

    //<editor-fold defaultstate="collapsed" desc="txtHCustomerType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, true);

    private void iniTxtHCustomerType() {
        txtHType.getDmGrid().addTextColumn(txtHType.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHType.getDmGrid().initializeGrid(txtHType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
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

    //<editor-fold defaultstate="collapsed" desc="txtHSaleBillNo">
    interface TxtHSaleBillNo_PrpAcs extends KeyField_T_PrpAcs<TxtHSaleBillNo_Mdl> {

        @Editor.Path("DepartmentName")
        ValueProvider<TxtHSaleBillNo_Mdl, String> department();

        @Editor.Path("Patient")
        ValueProvider<TxtHSaleBillNo_Mdl, String> patient();

        @Editor.Path("SaleBillDate")
        ValueProvider<TxtHSaleBillNo_Mdl, String> saleBillDate();

        @Editor.Path("PaymentSourceName")
        ValueProvider<TxtHSaleBillNo_Mdl, String> paymentSourceName();
    }
    final DmTextHelp<TxtHSaleBillNo_Mdl, TxtHSaleBillNo_PrpAcs> txtHSaleBillNo = new DmTextHelp(GWT.create(TxtHSaleBillNo_PrpAcs.class), 7, false);

    private void iniSaleBillNo() {
        txtHSaleBillNo.getDmGrid().addTextColumn(txtHSaleBillNo.getDmGrid().getPropertyAccess().name(), "Bill No.", 0, "recId");
        txtHSaleBillNo.getDmGrid().addTextColumn(txtHSaleBillNo.getDmGrid().getPropertyAccess().saleBillDate(), "Date", 75, "v_Date", DmDataType.DATE);
        txtHSaleBillNo.getDmGrid().addTextColumn(txtHSaleBillNo.getDmGrid().getPropertyAccess().department(), "Department By", 150, "department");
        txtHSaleBillNo.getDmGrid().addTextColumn(txtHSaleBillNo.getDmGrid().getPropertyAccess().patient(), "Patient Name", 225, "patientName");
        txtHSaleBillNo.getDmGrid().addTextColumn(txtHSaleBillNo.getDmGrid().getPropertyAccess().paymentSourceName(), "Payment Source", 120, "");
        txtHSaleBillNo.getDmGrid().initializeGrid(txtHSaleBillNo.getDmGrid().getPropertyAccess().code(), 745, 0, 1);

        txtHSaleBillNo.getHelper().addSort("v_Date", DmSortType.DESC);
        txtHSaleBillNo.getHelper().addSort("short_Name", DmSortType.ASC);
        txtHSaleBillNo.getHelper().addSort("v_No", DmSortType.DESC);
        txtHSaleBillNo.getHelper().addSort("recId", DmSortType.ASC);
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

    //<editor-fold defaultstate="collapsed" desc="txtHGstReason">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGstReason = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHReason() {
        txtHGstReason.getDmGrid().addTextColumn(txtHGstReason.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHGstReason.getDmGrid().initializeGrid(txtHGstReason.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHGstReason.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHCardSerial">
    interface MedicineReturnRequest_Mdl_PrpAcs extends KeyField_T_PrpAcs<MedicineReturnRequest_Mdl> {

        @Editor.Path("ReturnDate")
        ValueProvider<MedicineReturnRequest_Mdl, String> returnDate();

        @Editor.Path("IsChecked")
        ValueProvider<MedicineReturnRequest_Mdl, String> isChecked();

        @Editor.Path("PatientName")
        ValueProvider<MedicineReturnRequest_Mdl, String> patientName();

        @Editor.Path("PatientUHID")
        ValueProvider<MedicineReturnRequest_Mdl, String> patientUHID();

        @Editor.Path("PatientRegID")
        ValueProvider<MedicineReturnRequest_Mdl, String> patientRegID();

        @Editor.Path("PatientRegDocID")
        ValueProvider<MedicineReturnRequest_Mdl, String> patientRegDocID();

        @Editor.Path("Amount")
        ValueProvider<MedicineReturnRequest_Mdl, String> amount();

    }

    final DmHelpGrid<MedicineReturnRequest_Mdl, MedicineReturnRequest_Mdl_PrpAcs> txtHMedicineReturnRequest = new DmHelpGrid(GWT.create(MedicineReturnRequest_Mdl_PrpAcs.class), 10);

    private void iniTxtHMedicineReturnRequest() {
        txtHMedicineReturnRequest.getDmGrid().addTextColumn(txtHMedicineReturnRequest.getDmGrid().getPropertyAccess().name(), "Return No", 100, "recId");
        txtHMedicineReturnRequest.getDmGrid().addTextColumn(txtHMedicineReturnRequest.getDmGrid().getPropertyAccess().returnDate(), "Date", 80, "v_Date");
        txtHMedicineReturnRequest.getDmGrid().addTextColumn(txtHMedicineReturnRequest.getDmGrid().getPropertyAccess().patientName(), "Patient Name", 200, "patientName");
        txtHMedicineReturnRequest.getDmGrid().addTextColumn(txtHMedicineReturnRequest.getDmGrid().getPropertyAccess().patientRegID(), "Reg. No", 100, "regDocId");
        txtHMedicineReturnRequest.getDmGrid().addTextColumn(txtHMedicineReturnRequest.getDmGrid().getPropertyAccess().patientUHID(), "UHID No.", 100, "uhidDocID");
        txtHMedicineReturnRequest.getDmGrid().addTextColumn(txtHMedicineReturnRequest.getDmGrid().getPropertyAccess().amount(), "Amount", 80, "amount");
        txtHMedicineReturnRequest.getDmGrid().initializeGrid(txtHMedicineReturnRequest.getDmGrid().getPropertyAccess().code(), 660, 0, 3);
        txtHMedicineReturnRequest.getHelper().addSort("v_Date", DmSortType.ASC);
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

        @Editor.Path("PreparedBy")
        ValueProvider<Find_Mdl, String> preparedBy();

        @Editor.Path("preparedDate")
        ValueProvider<Find_Mdl, String> preparedDate();

        @Editor.Path("preparedTime")
        ValueProvider<Find_Mdl, String> preparedTime();

        @Editor.Path("Status")
        ValueProvider<Find_Mdl, String> status();

        @Editor.Path("SaleBillReturnType")
        ValueProvider<Find_Mdl, String> saleBillReturnType();

        @Editor.Path("PaymentMode")
        ValueProvider<Find_Mdl, String> paymentMode();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 25);

    private void iniFind() {
        ColumnConfig<Find_Mdl, String> discarded;
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Bill No.", 130, "BillNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().date(), "Date", 80, "v_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().uhidNo(), "UHID No.", 0, "UHIDNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().opdNO(), "OPD/IPD No.", 0, "regNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().consDocName(), "Consultent Doctor", 130, "consultantName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().patientName(), "Patient Name", 200, "patientName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().preparedBy(), "Prepared By", 100, "preparedBy");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().preparedDate(), "Entry Date", 80, "preparedDate", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().preparedTime(), "Entry Time", 40, "preparedTime", DmDataType.DOUBLE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().saleBillReturnType(), "Type", 100, "saleBillReturnType");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().paymentMode(), "Mode", 70, "paymentMode");
        discarded = dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().status(), "Status", 80, "status");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 6);

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
    //</editor-fol

    private boolean isSaleBill = false;

    public SaleBillReturn_Dsg(String widgetName) {
        isSaleBill = widgetName.equalsIgnoreCase(new pmPharmacy().SALE_BILL_RETURN.caption);
        ini();
        iniTxtHVType();
        iniTxtHConsultant();
        iniTxtHDepartment();
        iniTxtHCustomerType();
        iniSaleBillNo();
        iniTxtHIdType();
        iniTxtHCity();
        iniTxtHReason();
        iniTxtHMedicineReturnRequest();
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

//        userControl.addGap(10);
//        userControl.addGeneralBlog(lblRegistrationBalance);
//        lblRegistrationBalance.setHeight("15Px");
//        lblRegistrationBalance.getElement().getStyle().setColor("red");
//        lblRegistrationBalance.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//        lblRegistrationBalance.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
//        lblRegistrationBalance.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
//        lblRegistrationBalance.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        userControl.addGap(100);
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
        DmClient.setStyle(flwTable, "100%", "534px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "110px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "110px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftEntryArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "110px", "left", null, DmPosition.NONE, true);
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
        InlineLabel label;
        InlineLabel labelMed;

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
        hrtPnl.add(txtRegNo);
        hrtPnl.setCellWidth(txtRegNo, "100%");
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

        DmClient.setStyle(flwPatientSearchTd, "50%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwPatientSearch, "60%", null, "right", null, DmPosition.NONE, false);

        btnPatientSearch.setWidth("140px");
        btnPatientSearch.setHeight("20px");
        btnPatientSearch.getElement().setAttribute("style", btnPatientSearch.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        flwPatientSearch.add(btnPatientSearch);
        flwPatientSearchTd.add(flwPatientSearch);
        flwTr.add(flwPatientSearchTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reason");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGstReason));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Bill No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSaleBillNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
//////////////////////////////////////////////////////////////////////////////////////
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

        //--------------------------------------------------------------------------------------------------------------//
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
        //--------------------------------------------------------------------------------------------------------------//

        return flwTable;
    }

    private FlowPanel rightEntryArea() {
        FlowPanel flwTable, flwTr, flwTd;

        DmLabel lbl;
        InlineLabel label, labelMed;

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
        lbl = new DmLabel("Return No.");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(47, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtBillNo);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("UHID No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(txtUHIDNo);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Consultant");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(txtHConsultant);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Ref. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(txtRefNo);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Ref. Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(txtRefDate);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Organisation");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        btnOrganisation.setWidth("20px");
        btnOrganisation.setHeight("20px");
        txtOrganisation.getElement().getStyle().setWidth(87.4, Style.Unit.PCT);
        flwTd.add(vl.add(txtOrganisation));
        flwTd.add(btnOrganisation);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        label = new InlineLabel("Add Return Request");
        label.setWidth("185px");
        flwReturnRequestSearch.add(btnReturnRequestSearch);
        btnReturnRequestSearch.getElement().appendChild(label.getElement());
        btnReturnRequestSearch.getElement().setAttribute("Style", "width:100%;height:20px;font-family:Arial Unicode MS,Arial,sans-serif;padding-top:0;padding-bottom:0;font-size:12px;");
        flwTd.add(flwReturnRequestSearch);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginLeft(122, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        labelMed = new InlineLabel("Sold Medicines");
        labelMed.setWidth("100px");
        flwMedicineSearch.add(btnMedicineSearch);
        btnMedicineSearch.getElement().appendChild(labelMed.getElement());
        btnMedicineSearch.getElement().setAttribute("Style", "width:100%;height:20px;font-family:Arial Unicode MS,Arial,sans-serif;padding-top:0;padding-bottom:0;font-size:12px;");
        flwTd.add(flwMedicineSearch);
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
        flwTd.add(labelDiscount);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiscount));
        txtDiscount.getElement().setAttribute("style", txtDiscount.getElement().getAttribute("style")
                + ";text-alignt:right; ");
        txtDiscount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        ///////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Bill Discount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiscount2));
        txtDiscount2.getElement().setAttribute("style", txtDiscount.getElement().getAttribute("style")
                + ";text-alignt:right; ");
        txtDiscount2.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Taxable Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTaxableAmt));
        txtTaxableAmt.getElement().setAttribute("style", txtTaxableAmt.getElement().getAttribute("style")
                + ";text-alignt:right; ");
        txtTaxableAmt.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

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

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(33, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Bill Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBillAmt));
        txtBillAmt.getElement().setAttribute("style", txtBillAmt.getElement().getAttribute("style")
                + ";text-alignt:right; ");
        txtBillAmt.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

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
                + ";text-alignt:right; ");
        txtNetAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel idTypeSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "170px", null, null, DmPosition.CENTER, true);
//----------------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

//----------------------------------------------------------------------------------------------------------------//
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

        @Editor.Path("FreeQty")
        ValueProvider<GrdMedicine_Mdl, String> freeQty();

        @Editor.Path("Unit")
        ValueProvider<GrdMedicine_Mdl, String> saleUnitName();

        @Editor.Path("Rate")
        ValueProvider<GrdMedicine_Mdl, String> rate();

        @Editor.Path("NetAmt")
        ValueProvider<GrdMedicine_Mdl, String> amount();

        @Editor.Path("Amount")
        ValueProvider<GrdMedicine_Mdl, String> grossAmount();

        @Editor.Path("TotalDiscount")
        ValueProvider<GrdMedicine_Mdl, String> totalDiscount();

        @Editor.Path("GstPer")
        ValueProvider<GrdMedicine_Mdl, String> gstPer();

        @Editor.Path("GstAmount")
        ValueProvider<GrdMedicine_Mdl, String> gstAmount();

        @Editor.Path("SbRecId")
        ValueProvider<GrdMedicine_Mdl, String> sbRecId();

        @Editor.Path("BatchNo")
        ValueProvider<GrdMedicine_Mdl, String> batchNo();

        @Editor.Path("Mrp")
        ValueProvider<GrdMedicine_Mdl, String> mrp();

        @Editor.Path("ExpDate")
        ValueProvider<GrdMedicine_Mdl, String> expDate();

    }
    final DmGrid<GrdMedicine_Mdl, GrdMediDetails_PrpAcs> grdMediDetails = new DmGrid(GWT.create(GrdMediDetails_PrpAcs.class));

    private Grid iniGridMedicineDetails() {
        DmColumnConfig<GrdMedicine_Mdl, String> colSaleRate, colAmount;
        grdMediDetails.addRowNumberColumn();
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().name(), "Medicine Name", 250, "");
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().batchNo(), "Batch No.", 70, "");
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().expDate(), "Exp. Date", 80, "");
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().saleUnitName(), "Unit", 60, "");
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().quantity(), "Qty.", 60, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().freeQty(), "F.Qty.", 60, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().mrp(), "MRP/Pack", 70, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colSaleRate = grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().rate(), "S.Rate/Pack", 80, "", DmDataType.DOUBLE);
        colSaleRate.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().grossAmount(), "Gross Amt.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().totalDiscount(), "Disount", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().gstPer(), "GST%", 120, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().gstAmount(), "Tax Amt.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colAmount = grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().amount(), "Net. Amt.", 100, "");
        colAmount.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().sbRecId(), "Sale Bill No.", 150, "");
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
        return grdMediDetails.getGrid();
    }
    //</editor-fold>
}
