package aarogya.pharmacy.saleBillWholeSale.client;

import aarogya.pharmacy.saleBillWholeSale.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.taxSummary.client.TaxSummary;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
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
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
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

abstract class SaleBillWholeSale_Dsg extends IsWidgetExtended {

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
    public final static String vendorAllowBlog = "Emergency Allow";
    public final static String addBatchBlog = "Add Batch";
    public final static String approvedByBlog = "Approved By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    private pmPharmacy pmMenu = new pmPharmacy();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtBillNo = new DmTextBox(30, false);
    final DmTextBox txtDate = new DmTextBox(11, true);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(100, false);

    final DmTextBox txtReciptNo = new DmTextBox(50, false);
    final DmTextBox txtReciptType = new DmTextBox(50, false);

    final DmTextBoxNumeric txtItemGross = new DmTextBoxNumeric(2, true, 12, false);
    final DmLabel labelBillDiscount = new DmLabel("Bill Discount");
    final DmLabel lblPharmacyBalance = new DmLabel("");
    final DmTextBoxNumeric txtDiscount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtDiscount2 = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtTaxableAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtCGSTAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtIGSTAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSgstUgstAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtBillAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtRoundoff = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtNetAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtTaxAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmLabel lblSgstUgst = new DmLabel("");

    final Button btnPayment = new Button("Payment Mode");
    final FlowPanel flwPayment = new FlowPanel();
    final FlowPanel flwApproveButton = new FlowPanel();

    final DmLabel specialRemarks = new DmLabel("");
    final FlowPanel flwRemark = new FlowPanel();

    final DmLabel lblDiscarded = new DmLabel("");
    final DmLabel lblParty = new DmLabel("Party Name");
    final DmLabel lblTransporter = new DmLabel("Transporter");

    final FlowPanel flwContainer = new FlowPanel();
    final FlowPanel flwTxtHVendor = new FlowPanel();
    final FlowPanel flwTxtVendor = new FlowPanel();
    final DmTextBox txtVendor = new DmTextBox();

    final DmTextBox txtPlaceOfSupply = new DmTextBox(50, false);
    final DmTextBox txtVehicleNo = new DmTextBox(15, false);
    final DmTextBox txtLRNo = new DmTextBox(20, false);
    final DmTextBox txtLRDate = new DmTextBox(11, false);
    final DmTextBox txtEWayBillDate = new DmTextBox(50, false);
    final DmTextBox txtEWayBillNo = new DmTextBox(20, false);

    final TaxSummary taxSummary = new TaxSummary(595, 115, 2);

    final FlowPanel flwEstimate = new FlowPanel();
    final Menu menu = new Menu();
    final MenuItem mnuGstActions = new MenuItem("GST Actions");
    final Button btnApprovedBy = new Button();

    public boolean isEstimate = false;

    final DmTextBox txtDueDate = new DmTextBox(11, true);
    final DmTextBox txtPartyPODate = new DmTextBox(11, false);
    final DmTextBox txtGatePassNo = new DmTextBox(11, true);
    final DmTextBox txtPartyPONo = new DmTextBox(20, true);

    public String callFrom1 = "";

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

    //<editor-fold defaultstate="collapsed" desc="txtHVHVendor">
    interface TxtHVendor_PrpAcs extends KeyField_T_PrpAcs<TxtHVendor_Mdl> {

        @Editor.Path("DocumentNatureCode")
        ValueProvider<TxtHVType_Mdl, String> documentNatureCode();

        @Editor.Path("DocumentTypeCode")
        ValueProvider<TxtHVType_Mdl, String> documentTypeCode();

    }
    final DmTextHelp<TxtHVendor_Mdl, TxtHVendor_PrpAcs> txtHVendor = new DmTextHelp(GWT.create(TxtHVendor_PrpAcs.class), 7, true);

    private void iniTxtHVendor() {
        txtHVendor.getDmGrid().addTextColumn(txtHVendor.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHVendor.getDmGrid().initializeGrid(txtHVendor.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHVendor.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHType">
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

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Date")
        ValueProvider<Find_Mdl, String> date();

        @Editor.Path("PartyName")
        ValueProvider<Find_Mdl, String> partyName();

        @Editor.Path("Status")
        ValueProvider<Find_Mdl, String> status();

        @Editor.Path("preparedDate")
        ValueProvider<Find_Mdl, String> preparedDate();

        @Editor.Path("preparedTime")
        ValueProvider<Find_Mdl, String> preparedTime();

        @Editor.Path("GatePassNo")
        ValueProvider<Find_Mdl, String> gatePassNo();
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
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().date(), "Date", 80, "v_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().partyName(), "Party", 150, "patientName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().preparedDate(), "Entry Date", 80, "preparedDate", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().preparedTime(), "Entry Time", 80, "preparedTime", DmDataType.DOUBLE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().gatePassNo(), "Gate Pass No", 150, "gatePassRecId");
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

    //<editor-fold defaultstate="collapsed" desc="txtHLocation">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHLocation = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHLocation() {
        txtHLocation.getDmGrid().addTextColumn(txtHLocation.getDmGrid().getPropertyAccess().name(), "Location", 0, "name");
        txtHLocation.getDmGrid().initializeGrid(txtHLocation.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHLocation.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHTransporter">
    interface TxtHtransporter_PrpAcs extends KeyField_T_PrpAcs<TxtHVendor_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHVendor_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHVendor_Mdl, String> cityName();
    }

    final DmTextHelp<TxtHVendor_Mdl, TxtHtransporter_PrpAcs> txtHTransporter = new DmTextHelp(GWT.create(TxtHtransporter_PrpAcs.class), 6, false);

    private void iniTxtHTransporter() {
        txtHTransporter.getDmGrid().addTextColumn(txtHTransporter.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHTransporter.getDmGrid().addTextColumn(txtHTransporter.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHTransporter.getDmGrid().addTextColumn(txtHTransporter.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHTransporter.getDmGrid().initializeGrid(txtHTransporter.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHTransporter.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHTransporter.getHelper().addSort("cityName", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHVehicleType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVehicleType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHVehicleType() {
        txtHVehicleType.getDmGrid().addTextColumn(txtHVehicleType.getDmGrid().getPropertyAccess().name(), "Vehicle Type", 0, "name");
        txtHVehicleType.getDmGrid().initializeGrid(txtHVehicleType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHVehicleType.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHTransportMode">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTransportMode = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniTxtHTrasportMode() {
        txtHTransportMode.getDmGrid().addTextColumn(txtHTransportMode.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHTransportMode.getDmGrid().initializeGrid(txtHTransportMode.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHTransportMode.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHEWayBill">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEWayBill = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHEWayBill() {
        txtHEWayBill.getDmGrid().addTextColumn(txtHEWayBill.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHEWayBill.getDmGrid().initializeGrid(txtHEWayBill.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHEWayBill.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }//</editor-fold>

    private boolean isSaleBill = false;
    public String widgetName = "", voucher_Cat = "", voucher_Cat1 = "";

    public SaleBillWholeSale_Dsg(HashMap paramHashMap) {
        if (paramHashMap != null) {
            widgetName = (String) paramHashMap.get("widgetName");
            voucher_Cat = (String) paramHashMap.get("vCategory");
            voucher_Cat1 = (String) paramHashMap.get("vCategory1");
        } else {
            widgetName = new pmPharmacy().SALE_BILL_WHOLE_SALE.caption;
        }
        if (DmShared.xNull(voucher_Cat).equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_ESTIMATE)) {
            isEstimate = true;
        } else {
            isEstimate = false;
        }

        isSaleBill = widgetName.equalsIgnoreCase(new pmPharmacy().SALE_BILL_WHOLE_SALE.caption);

        if (paramHashMap != null) {
            if (paramHashMap.containsKey("callFrom")) {
                callFrom1 = DmShared.xNull(paramHashMap.get("callFrom").toString());
            }
        }

        ini();
        iniTxtHVType();
        iniTxtHConsultant();
        iniTxtHDepartment();
        iniTxtHCustomerType();
        iniTxtHVendor();
        iniOtherMenu();
        iniTxtHEWayBill();
        iniTxtHTransporter();
        iniTxtHTrasportMode();
        iniTxtHVehicleType();
        iniTxtHLocation();

    }

    private Menu iniOtherMenu() {
        menu.add(mnuGstActions);
        return menu;
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
        Anchor aBtn;
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGeneralBlog(lblDiscarded);
        lblDiscarded.setHeight("15Px");
        lblDiscarded.getElement().getStyle().setColor("red");
        lblDiscarded.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblDiscarded.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblDiscarded.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        lblDiscarded.getElement().getStyle().setFontSize(12, Style.Unit.PX);

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

        if (DmShared.xNull(callFrom1).equalsIgnoreCase(pmMenu.WHOLE_SALE_GATE_PASS_OUT.caption)) {
            userControl.addTextBlog(approvedByBlog, 100, true, false);
        }

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("green");
        btnApprovedBy.getElement().appendChild(aBtn.getElement());
        btnApprovedBy.setWidth("30px");
        userControl.addGeneralBlog(flwApproveButton);

        return userControl;

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "520px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "130px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "130px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftEntryArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "130px", "left", null, DmPosition.NONE, true);
        flwTd.add(rightEntryArea());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "212px", "left", null, DmPosition.NONE, true);
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

    private FlowPanel idTypeSection() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "170px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, null, null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(taxSummary));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftEntryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        HorizontalPanel hrtPnl;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "130px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Customer Type");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHType));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblParty.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblParty.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblParty.getElement().getStyle().setColor("blue");
        flwTd.add(lblParty);
        flwTr.add(flwTd);

        DmClient.setStyle(flwContainer, "80%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwTxtHVendor, "100%", null, "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwTxtVendor, "100%", null, "left", null, DmPosition.NONE, false);

        flwTxtHVendor.add(vl.add(txtHVendor));
        flwTxtVendor.add(vl.add(txtVendor));
        flwContainer.add(flwTxtHVendor);
        flwTr.add(flwContainer);

        flwTable.add(flwTr);
        //----------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("State");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPlaceOfSupply));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GST Transaction");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLocation));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

//-------------------------------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

//--------------------------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Due Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtDueDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gate Pass No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGatePassNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //---------------------------------------Party PO No. And PO Date------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Party P.O. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtPartyPONo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Party P.O. Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPartyPODate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightEntryArea() {
        FlowPanel flwTable, flwTr, flwTd;

        DmLabel lbl;
        InlineLabel label;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "130px", null, null, DmPosition.CENTER, true);

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
//--------------------------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(lblTransporter);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTransporter));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Mode");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtHTransportMode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
//--------------------------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Vehicle Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVehicleType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Vehicle No.");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtVehicleNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
//--------------------------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("LR No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLRNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtLRDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //--------------------------------------------------------------------------------------------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("EWayBill(Y/N)");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEWayBill));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("EWay Bill No.");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtEWayBillNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel amountSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "135px", null, null, DmPosition.CENTER, true);

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

    //<editor-fold defaultstate="collapsed" desc="Medicine Details">
    interface GrdMediDetails_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMedicine_Mdl> {

        @Editor.Path("Quantity")
        ValueProvider<GrdMedicine_Mdl, String> quantity();

        @Editor.Path("FreeQuantityInPurchaseUnit")
        ValueProvider<GrdMedicine_Mdl, String> freeQuantityInPurchaseUnit();

        @Editor.Path("Unit")
        ValueProvider<GrdMedicine_Mdl, String> purchaseUnitName();

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
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().purchaseUnitName(), "Unit", 60, "");
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().quantity(), "Qty.", 60, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().freeQuantityInPurchaseUnit(), "F.Qty.", 60, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().mrp(), "MRP/Pack", 80, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colSaleRate = grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().rate(), "S.Rate/Pack", 90, "", DmDataType.DOUBLE);
        colSaleRate.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().grossAmount(), "Gross Amt.", 80, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().totalDiscount(), "Discount", 70, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().gstPer(), "GST %", 60, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().gstAmount(), "Tax Amt.", 80, "", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        colAmount = grdMediDetails.addTextColumn(grdMediDetails.getPropertyAccess().amount(), "Net Amt.", 100, "");
        colAmount.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMediDetails.initializeGrid(grdMediDetails.getPropertyAccess().fieldHashCode(), 1280, 210, 2);
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
