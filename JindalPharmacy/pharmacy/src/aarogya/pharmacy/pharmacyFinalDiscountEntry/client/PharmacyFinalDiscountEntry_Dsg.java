package aarogya.pharmacy.pharmacyFinalDiscountEntry.client;

import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.Find_Mdl;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.GrdDiscountDetails_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class PharmacyFinalDiscountEntry_Dsg extends IsWidgetExtended {

    final pmPharmacy pmPharmacy = new pmPharmacy();
    final String widgetName = pmPharmacy.SALE_FINAL_DISCOUNT_ENTRY.caption;
    final String moduleName = pmPharmacy.SALE_FINAL_DISCOUNT_ENTRY.moduleName;
    final String preparedByBlog = "Prepared By";
    final String modifiedByBlog = "Modified By";
    public final static String dateEditableBlog = "Date Editable";
    public final static String opdAllowBlog = "OPD Allow";
    public final static String ipdAllowBlog = "IPD Allow";
    public final static String dayCareAllowBlog = "Day Care Allow";
    public final static String emergencyAllowBlog = "Emergency Allow";
    public final static String walkInAllowBlog = "Walk-In Allow";
    public final static String ledgerAllowBlog = "Ledger Allow";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwSearchBtn = new FlowPanel();
    final FlowPanel flwAdjustParent = new FlowPanel();
    final FlowPanel flwAdjustChild = new FlowPanel();
    final Button btnOPDSearch = new Button("...");
    final Button btnWalkInSearch = new Button("...");
    final Button btnIPDSearch = new Button("...");
    final Button btnLedgerSearch = new Button("...");
    final Button btnAdjust = new Button("Adjust");

    final DmLabel labelDiscount = new DmLabel("Discount");
    final DmTextBoxNumeric txtDiscountPer = new DmTextBoxNumeric(2, false, 5, true);
    final DmTextBoxNumeric txtDiscount = new DmTextBoxNumeric(2, true, 8, false);

    final DmTextBox txtUniqueId = new DmTextBox(20, true);
    final DmTextBox txtReceiptType = new DmTextBox(100, false);
    final DmTextBox txtVoucherType = new DmTextBox(100, false);
    final DmTextBox txtVoucherNo = new DmTextBox(15, false);
    final DmTextBox txtVoucherDate = new DmTextBox(11, false);
    final DmTextBox txtDisplayName = new DmTextBox();
    final DmTextBox txtIdType = new DmTextBox();
    final DmTextBox txtIdNo = new DmTextBox();
    final DmTextBox txtAddress = new DmTextBox();
    final DmTextBox txtCity = new DmTextBox();
    final DmTextBox txtMobile = new DmTextBox();
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, true, 15, true);
//    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(250, true);

    final DmLabel cancelBy = new DmLabel("");
    final DmLabel lblPharmacyBalance = new DmLabel("");

    //<editor-fold defaultstate="collapsed" desc="txtHCustomerType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCustomerType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, true);

    private void iniTxtHCustomerType() {
        txtHCustomerType.getDmGrid().addTextColumn(txtHCustomerType.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHCustomerType.getDmGrid().initializeGrid(txtHCustomerType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VoucherDate")
        ValueProvider<Find_Mdl, String> voucherDate();

        @Editor.Path("VoucherType")
        ValueProvider<Find_Mdl, String> voucherType();

        @Editor.Path("PatientName")
        ValueProvider<Find_Mdl, String> patientName();

        @Editor.Path("ManualCode")
        ValueProvider<Find_Mdl, String> manualCode();

        @Editor.Path("Amount")
        ValueProvider<Find_Mdl, String> amount();

        @Editor.Path("Status")
        ValueProvider<Find_Mdl, String> status();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 15);

    private void iniFind() {

        ColumnConfig<Find_Mdl, String> discarded;
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher No.", 100, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 75, "v_Date", DmEnum.DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().manualCode(), "Pharmacy A/c", 100, "manualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().patientName(), "Name", 150, "patientName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().amount(), "Discount", 100, "oh_Amt_Net").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        discarded = dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().status(), "Status", 70, "status");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 500, 4);

        discarded.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='color : red;'>" + DmShared.xNull(value) + "</div>");
            }
        });

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("v_no", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDiscountReason">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDisReason = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHDiscountReason() {
        txtHDisReason.getDmGrid().addTextColumn(txtHDisReason.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDisReason.getDmGrid().initializeGrid(txtHDisReason.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDisReason.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public PharmacyFinalDiscountEntry_Dsg() {
        ini();
        iniTxtHCustomerType();
        iniTxtHDiscountReason();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", "left", null, DmEnum.DmPosition.NONE, true);
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
        Button button;
        userControl.addTextBlog(preparedByBlog, 100, false, true);

        userControl.addGeneralBlog(cancelBy);
        cancelBy.setHeight("15Px");
        cancelBy.getElement().getStyle().setColor("red");
        cancelBy.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        cancelBy.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        cancelBy.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        cancelBy.getElement().getStyle().setFontSize(12, Style.Unit.PX);

        userControl.addGap(100);

        userControl.addGeneralBlog(lblPharmacyBalance);
        lblPharmacyBalance.setHeight("15Px");
        lblPharmacyBalance.getElement().getStyle().setColor("red");
        lblPharmacyBalance.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblPharmacyBalance.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblPharmacyBalance.getElement().getStyle().setMarginRight(3, Style.Unit.PX);
        lblPharmacyBalance.getElement().getStyle().setFontSize(12, Style.Unit.PX);

        button = new Button("Payment Mode");
        button.setWidth("100Px");

        userControl.addTextBlog(modifiedByBlog, 100, false, false);
        return userControl;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        //================================< 1st Row >================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + "; padding: 7px 0px;");
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Type");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCustomerType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmEnum.DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        hrtPnl.add(txtUniqueId);
        hrtPnl.setCellWidth(txtUniqueId, "100%");
        DmClient.setStyle(flwSearchBtn, "100%", "20px", "left", null, DmEnum.DmPosition.NONE, true);

        btnIPDSearch.setWidth("20px");
        btnIPDSearch.setHeight("20px");
        btnIPDSearch.getElement().setAttribute("style", btnIPDSearch.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnIPDSearch.setTitle("Patient Search (IPD)");
        flwSearchBtn.add(btnIPDSearch);

        btnOPDSearch.setWidth("20px");
        btnOPDSearch.setHeight("20px");
        btnOPDSearch.getElement().setAttribute("style", btnOPDSearch.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnOPDSearch.setTitle("Patient Search (OPD)");
        flwSearchBtn.add(btnOPDSearch);

        btnWalkInSearch.setWidth("20px");
        btnWalkInSearch.setHeight("20px");
        btnWalkInSearch.getElement().setAttribute("style", btnWalkInSearch.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnWalkInSearch.setTitle("Walk-In Search");
        flwSearchBtn.add(btnWalkInSearch);

        btnLedgerSearch.setWidth("20px");
        btnLedgerSearch.setHeight("20px");
        btnLedgerSearch.getElement().setAttribute("style", btnLedgerSearch.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnLedgerSearch.setTitle("Ledger Search");
        flwSearchBtn.add(btnLedgerSearch);

        hrtPnl.add(flwSearchBtn);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Voucher No.");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////

        flwTable.add(flwTr);
        //================================< ******* >================================//
        //================================< 2nd Row >================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + "; border-top: 1px solid #777; padding-top: 7px;");

        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDisplayName));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("ID Type");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtIdType));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("ID No.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtIdNo));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////

        flwTable.add(flwTr);
        //================================< ******* >================================//
        //================================< 3rd Row >================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + "; padding-bottom: 7px;");
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("City");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtCity));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////

        flwTable.add(flwTr);
        //================================< 4th Row >================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + "; border-top: 1px solid #777; padding: 7px 0px;");

        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        labelDiscount.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(labelDiscount);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiscountPer));
        txtDiscountPer.getElement().setAttribute("placeholder", "%");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiscount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        btnAdjust.setWidth("50px");
        btnAdjust.setHeight("23px");
        btnAdjust.getElement().setAttribute("style", btnAdjust.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnAdjust.setTitle("Ledger Search");
        flwAdjustChild.add(vl.add(btnAdjust));
        flwAdjustParent.add(flwAdjustChild);
        flwTd.add(flwAdjustParent);

        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "59%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDisReason));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////
        //================================< 5th Row >================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + "; border-top: 1px solid #777; padding: 7px 0px;");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(createGrid());
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////

        flwTable.add(flwTr);
        //================================< ******* >================================//

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdDiscountDetail">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdDiscountDetails_Mdl> {

        @Editor.Path("TaxableValue")
        ValueProvider<GrdDiscountDetails_Mdl, String> taxableValue();

        @Editor.Path("HsnCode")
        ValueProvider<GrdDiscountDetails_Mdl, String> hsnCode();

        @Editor.Path("HsnName")
        ValueProvider<GrdDiscountDetails_Mdl, String> hsnName();

        @Editor.Path("GstPer")
        ValueProvider<GrdDiscountDetails_Mdl, String> gstPer();

        @Editor.Path("CgstPer")
        ValueProvider<GrdDiscountDetails_Mdl, String> cgstPer();

        @Editor.Path("SgstPer")
        ValueProvider<GrdDiscountDetails_Mdl, String> sgstPer();

        @Editor.Path("UgstPer")
        ValueProvider<GrdDiscountDetails_Mdl, String> ugstPer();

        @Editor.Path("IgstPer")
        ValueProvider<GrdDiscountDetails_Mdl, String> igstPer();

        @Editor.Path("CgstAmt")
        ValueProvider<GrdDiscountDetails_Mdl, String> cgstAmt();

        @Editor.Path("SgstAmt")
        ValueProvider<GrdDiscountDetails_Mdl, String> sgstAmt();

        @Editor.Path("UgstAmt")
        ValueProvider<GrdDiscountDetails_Mdl, String> ugstAmt();

        @Editor.Path("IgstAmt")
        ValueProvider<GrdDiscountDetails_Mdl, String> igstAmt();

        @Editor.Path("GstAmt")
        ValueProvider<GrdDiscountDetails_Mdl, String> gstAmt();

        @Editor.Path("NetValue")
        ValueProvider<GrdDiscountDetails_Mdl, String> netValue();

    }

    final DmGrid<GrdDiscountDetails_Mdl, Grd_PrpAcs> grdDiscountDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid createGrid() {

        int i = 0;
        grdDiscountDetail.addRowNumberColumn();
        ++i;
        grdDiscountDetail.addTextColumn(grdDiscountDetail.getPropertyAccess().hsnName(), "HSN", 160, "");
        ++i;
        grdDiscountDetail.addTextColumn(grdDiscountDetail.getPropertyAccess().taxableValue(), "Taxable Value", 120, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdDiscountDetail.addTextColumn(grdDiscountDetail.getPropertyAccess().gstPer(), "GST %", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdDiscountDetail.addTextColumn(grdDiscountDetail.getPropertyAccess().gstAmt(), "GST", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdDiscountDetail.addTextColumn(grdDiscountDetail.getPropertyAccess().cgstPer(), "CGST %", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdDiscountDetail.addTextColumn(grdDiscountDetail.getPropertyAccess().cgstAmt(), "CGST", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdDiscountDetail.addTextColumn(grdDiscountDetail.getPropertyAccess().sgstPer(), "SGST %", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdDiscountDetail.addTextColumn(grdDiscountDetail.getPropertyAccess().sgstAmt(), "SGST", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdDiscountDetail.addTextColumn(grdDiscountDetail.getPropertyAccess().ugstPer(), "UGST %", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdDiscountDetail.addTextColumn(grdDiscountDetail.getPropertyAccess().ugstAmt(), "UGST", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdDiscountDetail.addTextColumn(grdDiscountDetail.getPropertyAccess().netValue(), "Net Value", 120, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDiscountDetail.initializeGrid(grdDiscountDetail.getPropertyAccess().fieldHashCode(), 1270, 300);

        return grdDiscountDetail.getGrid();
    }
    //</editor-fold>

}
