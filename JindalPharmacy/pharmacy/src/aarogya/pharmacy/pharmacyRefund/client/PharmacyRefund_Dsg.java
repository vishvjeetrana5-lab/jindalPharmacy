package aarogya.pharmacy.pharmacyRefund.client;

import aarogya.pharmacy.pharmacyRefund.shared.Find_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmControlBlog;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class PharmacyRefund_Dsg extends IsWidgetExtended {

    final pmPharmacy pmPharmacy = new pmPharmacy();
    final String widgetName = pmPharmacy.PHARMACY_REFUND.caption;
    final String moduleName = pmPharmacy.moduleName;
    final String preparedByBlog = "Prepared By";
    final String modifiedByBlog = "Modified By";
    public final static String opdAllowBlog = "OPD Allow";
    public final static String ipdAllowBlog = "IPD Allow";
    public final static String dayCareAllowBlog = "Day Care Allow";
    public final static String emergencyAllowBlog = "Emergency Allow";
    public final static String walkInAllowBlog = "Walk-In Allow";
    public final static String ledgerAllowBlog = "Ledger Allow";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwSearchBtn = new FlowPanel();
    final Button btnOPDSearch = new Button("...");
    final Button btnWalkInSearch = new Button("...");
    final Button btnIPDSearch = new Button("...");
    final Button btnLedgerSearch = new Button("...");

    final DmTextBox txtUniqueId = new DmTextBox(20, true);
    final DmTextBox txtReceiptType = new DmTextBox(100, false);
    final DmTextBox txtReceiptNo = new DmTextBox(15, false);
    final DmTextBox txtReceiptDate = new DmTextBox(11, true);
    final DmTextBox txtDisplayName = new DmTextBox();
    final DmTextBox txtIdType = new DmTextBox();
    final DmTextBox txtIdNo = new DmTextBox();
    final DmTextBox txtAddress = new DmTextBox();
    final DmTextBox txtCity = new DmTextBox();
    final DmTextBox txtMobile = new DmTextBox();
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, true, 15, true);
    final DmTextBox txtRemark = new DmTextBox(100, false);

    DmControlBlog paymentCBlog;
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

        @Editor.Path("DisplayName")
        ValueProvider<Find_Mdl, String> displayName();

        @Editor.Path("ManualCode")
        ValueProvider<Find_Mdl, String> manualCode();

        @Editor.Path("Mobile")
        ValueProvider<Find_Mdl, String> mobile();

        @Editor.Path("Amount")
        ValueProvider<Find_Mdl, String> amount();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 5);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Receipt No.", 0, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherType(), "Type", 60, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 75, "v_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().displayName(), "Name", 150, "displayName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "Manual Code");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().mobile(), "Mobile", 100, "mobile");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().amount(), "Amount", 100, "amount");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("v_no", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public PharmacyRefund_Dsg() {
        iniTxtHCustomerType();
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", "left", null, DmPosition.NONE, true);
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
        paymentCBlog = userControl.addGeneralBlog(button);

        userControl.addTextBlog(modifiedByBlog, 100, false, false);
        return userControl;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);
        //================================< 1st Row >================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + "; padding: 7px 0px;");

        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Type");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCustomerType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(txtUniqueId);
        hrtPnl.setCellWidth(txtUniqueId, "100%");
        DmClient.setStyle(flwSearchBtn, "100%", "20px", "left", null, DmPosition.NONE, true);

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
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Receipt No.");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReceiptNo));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReceiptDate));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////

        flwTable.add(flwTr);
        //================================< ******* >================================//
        //================================< 2nd Row >================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + "; border-top: 1px solid #777; padding-top: 7px;");

        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDisplayName));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ID Type");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIdType));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ID No.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIdNo));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////

        flwTable.add(flwTr);
        //================================< ******* >================================//
        //================================< 3rd Row >================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + "; padding-bottom: 7px;");
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("City");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCity));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////

        flwTable.add(flwTr);
        //================================< 4th Row >================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + "; border-top: 1px solid #777; padding: 7px 0px;");

        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "59%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);
        //////////////////////////////////////////////////////////////////////////////

        flwTable.add(flwTr);
        //================================< ******* >================================//

        return flwTable;
    }
}
