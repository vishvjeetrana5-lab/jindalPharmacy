package aarogya.pharmacy.wholeSalePaymentRecipt.client;

import aarogya.pharmacy.wholeSalePaymentRecipt.shared.Find_Mdl;
import aarogya.pharmacy.wholeSalePaymentRecipt.shared.TxtHDepositAc_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmDataType;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract public class WholeSalePaymentRecipt_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().WHOLE_SALE_PAYMENT_RECIPT.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBox txtVoucherNo = new DmTextBox(20, false);
    final DmTextBox txtInstrumentNo = new DmTextBox(20, false);
    final DmTextBox txtInstrumentDate = new DmTextBox(11, false);
    final DmTextBox txtRemark = new DmTextBox(250, false);
    final DmTextBoxNumeric txtReceivedAmt = new DmTextBoxNumeric(2, false, 12, true);
    final DmTextBoxNumeric txtTDSAmt = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtsettledAmt = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtNetAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtUnsettledAmt = new DmTextBoxNumeric(2, true, 12, false);

    final DmLabel lblUnsettled = new DmLabel("Unsettled Amt.");
    final Button btnRemark = new Button("...");

    //<editor-fold defaultstate="collapsed" desc="TxtHVoucher">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVoucher = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniVoucherType() {
        txtHVoucher.getDmGrid().addTextColumn(txtHVoucher.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHVoucher.getDmGrid().initializeGrid(txtHVoucher.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHVoucher.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDebtorsName">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDebtorsName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHDebtorsName() {
        txtHDebtorsName.getDmGrid().addTextColumn(txtHDebtorsName.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHDebtorsName.getDmGrid().initializeGrid(txtHDebtorsName.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHDebtorsName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepositAC">
    interface TxtHDepositAc_PrpAcs extends KeyField_T_PrpAcs<TxtHDepositAc_Mdl> {

        @Editor.Path("Nature")
        ValueProvider<TxtHDepositAc_Mdl, String> nature();

    }

    final DmTextHelp<TxtHDepositAc_Mdl, TxtHDepositAc_PrpAcs> txtHDepositAC = new DmTextHelp(GWT.create(TxtHDepositAc_PrpAcs.class), 7, true);

    private void iniTxtHDepositAC() {
        txtHDepositAC.getDmGrid().addTextColumn(txtHDepositAC.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHDepositAC.getDmGrid().addTextColumn(txtHDepositAC.getDmGrid().getPropertyAccess().nature(), "Nature", 50, "nature");
        txtHDepositAC.getDmGrid().initializeGrid(txtHDepositAC.getDmGrid().getPropertyAccess().code(), 350, 0, 1);

        txtHDepositAC.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTDSAccount">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTDSAccount = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHTDSAccount() {
        txtHTDSAccount.getDmGrid().addTextColumn(txtHTDSAccount.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHTDSAccount.getDmGrid().initializeGrid(txtHTDSAccount.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHTDSAccount.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHInstrument"> 
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHInstrument = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, true);

    private void iniTxtHInstrument() {
        txtHInstrument.getDmGrid().addTextColumn(txtHInstrument.getDmGrid().getPropertyAccess().name(), "Instrument", 150, "");
        txtHInstrument.getDmGrid().initializeGrid(txtHInstrument.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDisallowType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDisallowType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHDisallowType() {
        txtHDisallowType.getDmGrid().addTextColumn(txtHDisallowType.getDmGrid().getPropertyAccess().name(), "Type", 0, "name");
        txtHDisallowType.getDmGrid().initializeGrid(txtHDisallowType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VoucherDate")
        ValueProvider<Find_Mdl, String> voucherDate();

        @Editor.Path("ChequeNo")
        ValueProvider<Find_Mdl, String> chequeNo();

        @Editor.Path("ChequeDate")
        ValueProvider<Find_Mdl, String> chequeDate();

        @Editor.Path("PartyName")
        ValueProvider<Find_Mdl, String> partyName();

        @Editor.Path("DepositeAC")
        ValueProvider<Find_Mdl, String> depositeAC();

        @Editor.Path("Amount")
        ValueProvider<Find_Mdl, String> amount();

        @Editor.Path("UnSetteldAmt")
        ValueProvider<Find_Mdl, String> unSetteldAmt();

        @Editor.Path("ReceivedAmt")
        ValueProvider<Find_Mdl, String> receivedAmt();

        @Editor.Path("TdsAmt")
        ValueProvider<Find_Mdl, String> tdsAmt();

        @Editor.Path("SetteldAmt")
        ValueProvider<Find_Mdl, String> setteldAmt();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher No.", 110, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 75, "v_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().partyName(), "Debtor Name", 180, "partyName");
        // dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().tdsAc(), "TDS A/c", 120, "tdsAcName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().depositeAC(), "Deposit In", 150, "depositInName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().chequeNo(), "Cheque No.", 110, "chequeNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().chequeDate(), "Cheque Date", 75, "chequeDate", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().receivedAmt(), "Recd. Amt.", 110, "recdAmt", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().tdsAmt(), "TDS Amt.", 110, "tdsAmt", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().amount(), "Net Amt.", 110, "amount", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().unSetteldAmt(), "Unsettled Amt.", 110, "unsettledAmt", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().setteldAmt(), "Settled Amt.", 110, "SettledAmt", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0);

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC, "short_Name");
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public WholeSalePaymentRecipt_Dsg() {
        ini();
        iniFind();
        iniVoucherType();
        iniTxtHDebtorsName();
        iniTxtHDepositAC();
        iniTxtHTDSAccount();
        iniTxtHInstrument();
        iniTxtHDisallowType();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(addUserControl());

        vrtInnerForm.setCellHeight(flw, "100%");

        flwForm.add(vrtInnerForm);
    }

    private DmUserControl addUserControl() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        return userControl;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "523px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "90px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "90px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", "90px", "left", null, DmPosition.NONE, true);
        flwTd.add(middleArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", "110px", "left", null, DmPosition.NONE, true);
        flwTd.add(rightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "10.4%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Disallow Type");
//        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "16.2%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHDisallowType));
//        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10.4%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        DmClient.setStyle(btnRemark, "20px", "20px", null, null, DmPosition.NONE, true);
        btnRemark.addStyleName("dm-button-grad");
        hrtPnl.add(vl.add(txtRemark));
        hrtPnl.setCellWidth(txtRemark, "100%");
        hrtPnl.add(btnRemark);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "80px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucher));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher No.");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row>========================================= */
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Received From");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDebtorsName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row>========================================= */
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("TDS A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTDSAccount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row>========================================= */
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Deposit In");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepositAC));
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel middleArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "80px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Instrument");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHInstrument));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Instrument No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtInstrumentNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Instrument Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtInstrumentDate));
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        
       

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "97px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Received Amt.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReceivedAmt));
        txtReceivedAmt.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("TDS Amt.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTDSAmt));
        txtTDSAmt.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Net Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNetAmt));
        txtNetAmt.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
         /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Settled Amt.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtsettledAmt));
        txtsettledAmt.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 5st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "18px", "left", null, DmPosition.NONE, false);
        lblUnsettled.setStyleName("dm-HLabel");
        lblUnsettled.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lblUnsettled.getElement().getStyle().setColor("blue");
        lblUnsettled.setHeight("15px");
        lblUnsettled.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblUnsettled.setTitle("Party Bill Settlement");
        flwTd.add(lblUnsettled);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUnsettledAmt));
        txtUnsettledAmt.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
