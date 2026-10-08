package aarogya.master.cardRecharge.client;

import dataman.dmBase.self.shared.DmEnum.*;
import aarogya.master.cardRecharge.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.cardPaymentPopup.shared.GrdCardPayment_Mdl;
import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class CardRecharge_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().CARD_RECHARGE.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtCardType = new DmTextBox(50, false);
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, false, 10, true);
    final DmTextBox txtVoucherDate = new DmTextBox(12, true);
    final DmTextBox txtVoucherNo = new DmTextBox(20, false);
    final DmTextBox txtAllotmentDate = new DmTextBox(12, false);
    final DmTextBox txtContactPerson = new DmTextBox(200, false);
    final DmTextBox txtLinkedAC = new DmTextBox(100, false);
    final DmTextBox txtValidDate = new DmTextBox(12, false);
    final DmTextBox txtCardNo = new DmTextBox(20, false);
    final DmTextBox txtCardRegNo = new DmTextBox(20, false);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(250, false);

    final DmTextBoxNumeric txtReceipt = new DmTextBoxNumeric(2, true, 11, false);
    final DmTextBoxNumeric txtBalance = new DmTextBoxNumeric(2, true, 11, false);
    final DmTextBoxNumeric txtCollected = new DmTextBoxNumeric(2, true, 11, false);
    final DmTextBoxNumeric txtReturn = new DmTextBoxNumeric(2, true, 11, false);
    final DmLabel lblMessage = new DmLabel("");

    final Button btnCardHelp = new Button("...");

    //<editor-fold defaultstate="collapsed" desc="TxtHCardNo">
    interface TxtHCardNo_PrpAcs extends KeyField_T_PrpAcs<RegisteredWallet_Mdl> {

        @Editor.Path("CardNo")
        ValueProvider<RegisteredWallet_Mdl, String> cardNo();

        @Editor.Path("Mobile")
        ValueProvider<RegisteredWallet_Mdl, String> mobile();

        @Editor.Path("CardTypeName")
        ValueProvider<RegisteredWallet_Mdl, String> cardTypeName();

        @Editor.Path("contactPerson")
        ValueProvider<RegisteredWallet_Mdl, String> contactPerson();

    }

    final DmHelpGrid<RegisteredWallet_Mdl, TxtHCardNo_PrpAcs> txtHCardHelp = new DmHelpGrid(GWT.create(TxtHCardNo_PrpAcs.class), 10);

    private void iniTxtHCardNo() {
        txtHCardHelp.getDmGrid().addTextColumn(txtHCardHelp.getDmGrid().getPropertyAccess().name(), "Card Reg. No.", 120, "recid");
        txtHCardHelp.getDmGrid().addTextColumn(txtHCardHelp.getDmGrid().getPropertyAccess().contactPerson(), "Contact Person", 125, "personName");
        txtHCardHelp.getDmGrid().addTextColumn(txtHCardHelp.getDmGrid().getPropertyAccess().cardNo(), "Card No.", 100, "cardNo");
        txtHCardHelp.getDmGrid().addTextColumn(txtHCardHelp.getDmGrid().getPropertyAccess().mobile(), "Mobile", 75, "Mobile");
        txtHCardHelp.getDmGrid().addTextColumn(txtHCardHelp.getDmGrid().getPropertyAccess().cardTypeName(), "Card Type", 80, "cardTypeName");
        txtHCardHelp.getDmGrid().initializeGrid(txtHCardHelp.getDmGrid().getPropertyAccess().code(), 550, 0, 2);

        txtHCardHelp.getHelper().addSort("v_Date", DmSortType.DESC);
        txtHCardHelp.getHelper().addSort("short_Name", DmSortType.ASC);
        txtHCardHelp.getHelper().addSort("v_No", DmSortType.DESC);
        txtHCardHelp.getHelper().addSort("recid", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VoucherType")
        ValueProvider<Find_Mdl, String> voucherType();

        @Editor.Path("VoucherDate")
        ValueProvider<Find_Mdl, String> voucherDate();

        @Editor.Path("CardRegNo")
        ValueProvider<Find_Mdl, String> cardRegNo();

        @Editor.Path("CardNo")
        ValueProvider<Find_Mdl, String> cardNo();

        @Editor.Path("CardType")
        ValueProvider<Find_Mdl, String> cardType();

        @Editor.Path("Amount")
        ValueProvider<Find_Mdl, String> amount();

        @Editor.Path("PersonName")
        ValueProvider<Find_Mdl, String> person();

        @Editor.Path("Remark")
        ValueProvider<Find_Mdl, String> remark();

    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher No.", 100, "Description");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Entry Date", 100, "Entry_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cardType(), "Card Type", 100, "CardType");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cardRegNo(), "Card Reg. No.", 100, "cardRegNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cardNo(), "Card No.", 100, "cardNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().remark(), "Remark", 330, "remark");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().person(), "Contact Person", 250, "cardHolderName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().amount(), "Amount", 150, "Amount", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1280, 183, 1);

        dmFind.getHelper().addSort("Description", DmSortType.ASC);
    }
    //</editor-fold>

    public CardRecharge_Dsg() {
        ini();
    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniTxtHCardNo();
        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.add(iniUserControl());

        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);

    }

    private DmUserControl iniUserControl() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addGeneralBlog(lblMessage);
        lblMessage.setHeight("15Px");
        lblMessage.getElement().getStyle().setColor("red");
        lblMessage.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblMessage.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblMessage.getElement().getStyle().setMarginRight(3, Style.Unit.PX);
        lblMessage.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);

        return userControl;

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        CaptionPanel cPnl;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "519px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(topArea());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "60px", "left", null, DmPosition.NONE, true);

        cPnl = new CaptionPanel();
        cPnl.getElement().setAttribute("style", "border-right: none; border-left: none; width 98%; height: 40px");
        cPnl.add(cardDetailArea());
        flwTr.add(cPnl);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "54%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "390px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdCardRecharge()));
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "24px", "left", null, DmPosition.NONE, true);
        flwTr.add(amountStrip());
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private FlowPanel amountStrip() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "24px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Receipt");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(15, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        txtReceipt.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(vl.add(txtReceipt));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Balance");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(15, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        txtBalance.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(vl.add(txtBalance));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Collected");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(15, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        txtCollected.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(vl.add(txtCollected));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Return");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setHeight(15, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        txtReturn.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(vl.add(txtReturn));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "24px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10.8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24.6%", null, "left", null, DmPosition.NONE, false);

        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        btnCardHelp.addStyleName("dm-button-grad");
        btnCardHelp.setWidth("20px");
        btnCardHelp.setHeight("20px");
        hrtPnl.add(txtCardNo);
        hrtPnl.add(btnCardHelp);
        hrtPnl.setCellWidth(txtCardNo, "100%");
        hrtPnl.setCellWidth(btnCardHelp, "20px");
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10.6%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19.9%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Date");
        lbl.getElement().getStyle().setMarginLeft(52, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel cardDetailArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "42px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card Reg. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardRegNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Contact Person");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtContactPerson));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10.8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Allotment Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10.4%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAllotmentDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Linked AC");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLinkedAC));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10.8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Valid Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10.4%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtValidDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdRecharge">
    interface GrdPayment_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdCardPayment_Mdl> {

        @Editor.Path("CardNo")
        ValueProvider<GrdCardPayment_Mdl, String> cardNo();

        @Editor.Path("CardHolderName")
        ValueProvider<GrdCardPayment_Mdl, String> cardHolderName();

        @Editor.Path("CardAuthorization")
        ValueProvider<GrdCardPayment_Mdl, String> cardAuthorization();

        @Editor.Path("Amount")
        ValueProvider<GrdCardPayment_Mdl, String> amount();

    }
    final DmGrid<GrdCardPayment_Mdl, GrdPayment_PrpAcs> grdPayment = new DmGrid(GWT.create(GrdPayment_PrpAcs.class));

    public Grid iniGrdCardRecharge() {
        grdPayment.addRowNumberColumn();
        grdPayment.addTextColumn(grdPayment.getPropertyAccess().name(), "Payment Mode", 220, "");
        grdPayment.addTextColumn(grdPayment.getPropertyAccess().amount(), "Amount", 180, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPayment.addTextColumn(grdPayment.getPropertyAccess().cardNo(), "Card No.", 170, "");
        grdPayment.addTextColumn(grdPayment.getPropertyAccess().cardHolderName(), "Card Holder Name", 350, "");
        grdPayment.addTextColumn(grdPayment.getPropertyAccess().cardAuthorization(), "Authorization", 300, "");
        grdPayment.initializeGrid(grdPayment.getPropertyAccess().fieldHashCode(), 1250, 390, 5);
        return grdPayment.getGrid();
    }
    //</editor-fold>

}
