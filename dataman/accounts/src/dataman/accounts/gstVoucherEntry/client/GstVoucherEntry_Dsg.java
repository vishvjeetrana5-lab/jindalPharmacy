package dataman.accounts.gstVoucherEntry.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.accounts.gstVoucherEntry.shared.Find_Mdl;
import dataman.accounts.gstVoucherEntry.shared.GridAcDetailsMdl;
//import dataman.accounts.gstVoucherEntry.shared.GridHSNDetailsMdl;
import dataman.accounts.gstVoucherEntry.shared.TxtHAccountName_Mdl;
import dataman.accounts.gstVoucherEntry.shared.TxtHRefVoucher_Mdl;
import dataman.accounts.gstVoucherEntry.shared.TxtHVoucherType_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmDataType;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmAccounts;

abstract class GstVoucherEntry_Dsg extends IsWidgetExtended {

    public String widgetName = new lmAccounts().GST_VOUCHER.caption;
    public String moduleName = new lmAccounts().GST_VOUCHER.moduleName;

    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Posted By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtVoucherNo = new DmTextBox(20, false);
    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBox txtRefNo = new DmTextBox(30, false);
    final DmTextBox txtRefDate = new DmTextBox(11, false);
    final DmTextBox txtWTACode = new DmTextBox(50, false);

    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(250, false);

    final DmLabel lblStripAcAmtTotal = new DmLabel("");
    final DmLabel lblStripHSNTaxableAmtTotal = new DmLabel("");
    final DmLabel lblStripHSNCGSTAmtTotal = new DmLabel("");
    final DmLabel lblStripHSNSGSTAmtTotal = new DmLabel("");
    final DmLabel lblStripHSNIGSTAmtTotal = new DmLabel("");
    final DmLabel lblStripHSNUGSTAmtTotal = new DmLabel("");
    final DmLabel lblStripHSN_SGST_UGSTAmtTotal = new DmLabel("");

    //<editor-fold defaultstate="collapsed" desc="BUTTON">
    final Button btnApprovedBy = new Button();
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHVoucherType">
    interface TxtHVoucherType_PrpAcs extends KeyField_T_PrpAcs<TxtHVoucherType_Mdl> {

    }

    final DmTextHelp<TxtHVoucherType_Mdl, TxtHVoucherType_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(TxtHVoucherType_PrpAcs.class), 7, true);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Name", 1, "Description");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHVoucherType.getHelper().addSort("Description", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHReason">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReason = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7);

    private void iniTxtHReason() {
        txtHReason.getDmGrid().addTextColumn(txtHReason.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHReason.getDmGrid().initializeGrid(txtHReason.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHReason.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHState">
    interface DmState_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> sharedModifiedBy();
    }
    final DmTextHelp<KeyFieldExtended, DmState_PrpAcs> txtHState = new DmTextHelp(GWT.create(DmState_PrpAcs.class), 5, true);

    private void initxtHState() {
        txtHState.getDmGrid().addTextColumn(txtHState.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHState.getDmGrid().addTextColumn(txtHState.getDmGrid().getPropertyAccess().sharedModifiedBy(), "State Code", 100, "numericCode");
        txtHState.getDmGrid().initializeGrid(txtHState.getDmGrid().getPropertyAccess().code(), 300, 0, 1); //244

        txtHState.getHelper().addSort("Name", DmSortType.ASC);
        txtHState.getHelper().addSort("numericCode", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHRefVoucher">
    interface TxtHRefVoucher extends KeyFieldHashCode_T_PrpAcs<TxtHRefVoucher_Mdl> {

        @Editor.Path("RefV_Date")
        ValueProvider<TxtHRefVoucher_Mdl, String> refV_Date();

        @Editor.Path("RefDocType")
        ValueProvider<TxtHRefVoucher_Mdl, String> refDocType();

        @Editor.Path("RefDocValue")
        ValueProvider<TxtHRefVoucher_Mdl, String> refDocValue();
    }
    final DmTextHelp<TxtHRefVoucher_Mdl, TxtHRefVoucher> txtHRefVoucher = new DmTextHelp(GWT.create(TxtHRefVoucher.class), 7, false);

    private void iniTxtHRefVoucher() {
        txtHRefVoucher.getDmGrid().addTextColumn(txtHRefVoucher.getDmGrid().getPropertyAccess().name(), "Voucher No.", 1, "recId");
        txtHRefVoucher.getDmGrid().addTextColumn(txtHRefVoucher.getDmGrid().getPropertyAccess().refV_Date(), "Date", 100, "v_Date", DmDataType.DATE);
        txtHRefVoucher.getDmGrid().addTextColumn(txtHRefVoucher.getDmGrid().getPropertyAccess().refDocType(), "Type", 100, "type");
        txtHRefVoucher.getDmGrid().addTextColumn(txtHRefVoucher.getDmGrid().getPropertyAccess().refDocValue(), "Amount", 100, "docValue", DmDataType.DOUBLE);
        txtHRefVoucher.getDmGrid().initializeGrid(txtHRefVoucher.getDmGrid().getPropertyAccess().fieldHashCode(), 400, 0, 1);
        txtHRefVoucher.getHelper().addSort("v_Type", DmSortType.ASC);
        txtHRefVoucher.getHelper().addSort("v_Date", DmSortType.DESC);
        txtHRefVoucher.getHelper().addSort("recId", DmSortType.ASC);
        txtHRefVoucher.getHelper().addSort("v_No", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHAccountName">
    interface TxtHAccountName extends KeyField_T_PrpAcs<TxtHAccountName_Mdl> {

        @Editor.Path("CityName")
        ValueProvider<TxtHAccountName_Mdl, String> cityName();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHAccountName_Mdl, String> manualCode();

        @Editor.Path("Nature")
        ValueProvider<TxtHAccountName_Mdl, String> nature();
    }
    final DmTextHelp<TxtHAccountName_Mdl, TxtHAccountName> txtHAccountName = new DmTextHelp(GWT.create(TxtHAccountName.class), 7, true);

    private void iniTxtHAccountName() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().cityName(), "City", 100, "cityName");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().nature(), "Nature", 100, "nature");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().manualCode(), "Code", 50, "manualCode");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHAccountName.getHelper().addSort("name", DmSortType.ASC);
        txtHAccountName.getHelper().addSort("cityName", DmSortType.ASC);
        txtHAccountName.getHelper().addSort("nature", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRcm">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRcm = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 3, false, false);

    private void iniTxtHRcm() {
        txtHRcm.getDmGrid().addTextColumn(txtHRcm.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHRcm.getDmGrid().initializeGrid(txtHRcm.getDmGrid().getPropertyAccess().code(), 70, 70, 1);
        txtHRcm.getHelper().addSort("name", DmEnum.DmSortType.DESC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGSTRounding">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGSTRounding = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false, true);

    private void iniTxtHGSTRounding() {
        txtHGSTRounding.getDmGrid().addTextColumn(txtHGSTRounding.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHGSTRounding.getDmGrid().initializeGrid(txtHGSTRounding.getDmGrid().getPropertyAccess().code(), 70, 70, 1);
        txtHGSTRounding.getHelper().addSort("name", DmEnum.DmSortType.DESC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGSTType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGSTType = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 4, false, true);

    private void iniTxtHGSTType() {
        txtHGSTType.getDmGrid().addTextColumn(txtHGSTType.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHGSTType.getDmGrid().initializeGrid(txtHGSTType.getDmGrid().getPropertyAccess().code(), 70, 70, 1);
        txtHGSTType.getHelper().addSort("name", DmEnum.DmSortType.DESC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("V_Date")
        ValueProvider<Find_Mdl, String> V_Date();

        @Editor.Path("VoucherType")
        ValueProvider<Find_Mdl, String> VoucherType();

        @Editor.Path("Account1")
        ValueProvider<Find_Mdl, String> Account1();

        @Editor.Path("PreparedBy")
        ValueProvider<Find_Mdl, String> PreparedBy();

        @Editor.Path("ModifiyedBy")
        ValueProvider<Find_Mdl, String> ModifiyedBy();

        @Editor.Path("PostedBy")
        ValueProvider<Find_Mdl, String> PostedBy();

        @Editor.Path("EntryDtTime")
        ValueProvider<Find_Mdl, String> entryDtTime();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 14, true);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher No.", 120, "Voucher_No");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().VoucherType(), "Voucher Type", 100, "Voucher_Type");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().V_Date(), "Voucher Date", 80, "Voucher_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().Account1(), "Party", 150, "Account1");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().PreparedBy(), "Prepared By", 100, "Prepared_By");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().ModifiyedBy(), "Modifiyed By", 100, "Modified_By");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().PostedBy(), "Posted By", 100, "Posted_By");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().entryDtTime(), "Entry Date", 125, "entryDateTime", DmDataType.DATE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0);
        dmFind.getHelper().addSort("V_Date", DmSortType.DESC);
        dmFind.getHelper().addSort("Voucher_Type", DmSortType.ASC);
        dmFind.getHelper().addSort("V_NO", DmSortType.DESC);
        dmFind.getHelper().addSort("Voucher_No", DmSortType.ASC);

    }
    //</editor-fold>

    public GstVoucherEntry_Dsg() {
        ini();
        iniTxtHVoucherType();
        initxtHState();
        iniTxtHAccountName();
        iniTxtHReason();
        iniTxtHRcm();
        iniTxtHGSTType();
        iniTxtHGSTRounding();
        iniTxtHRefVoucher();
        iniFind();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

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
        Anchor aBtn;
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addTextBlog(modifiedByBlog, 100, false, true);
        userControl.addGap(100);

        userControl.addTextBlog(approvedByBlog, 100, true, false);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnApprovedBy.addStyleName("dm-button-grad");
        btnApprovedBy.getElement().appendChild(aBtn.getElement());
        btnApprovedBy.setWidth("30px");
        userControl.addControlBlog(approvedByBlog, btnApprovedBy);

        return userControl;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", null, null, DmEnum.DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTr.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(leftArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(rightArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(vl.add(createAcGrid()));

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(lblStripAcAmtTotal);
        flwTr.add(lblStripHSNTaxableAmtTotal);
        flwTr.add(lblStripHSN_SGST_UGSTAmtTotal);
        flwTr.add(lblStripHSNCGSTAmtTotal);
        flwTr.add(lblStripHSNIGSTAmtTotal);
//        lblStripAcAmtTotal.getElement().getStyle().setFloat(Style.Float.RIGHT);
        lblStripAcAmtTotal.getElement().getStyle().setMarginLeft(35, Style.Unit.PCT);
        lblStripHSNTaxableAmtTotal.getElement().getStyle().setMarginLeft(18, Style.Unit.PCT);
        lblStripHSN_SGST_UGSTAmtTotal.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lblStripHSNCGSTAmtTotal.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lblStripHSNIGSTAmtTotal.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        flwTr.addStyleName("dm-StripLabel");

        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);
//        flwTr.add(vl.add(createHSNGrid()));

//        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "", null, null, DmEnum.DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Party");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAccountName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Agst. Invoice");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRefVoucher));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Reason");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHReason));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Ref. No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtRefNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Ref. Date.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtRefDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "", null, null, DmEnum.DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtVoucherDate);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Voucher No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtVoucherNo);
        txtVoucherNo.setEnabled(false);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Place Of Supply");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtHState);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("RCM");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtHRcm);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("GST Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGSTType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("GST Rounding");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtHGSTRounding);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdAccountDetail">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GridAcDetailsMdl> {

        @Editor.Path("AccMCode")
        ValueProvider<GridAcDetailsMdl, String> accMCode();

        @Editor.Path("AccName")
        ValueProvider<GridAcDetailsMdl, String> accName();

        @Editor.Path("Narration")
        ValueProvider<GridAcDetailsMdl, String> narration();

        @Editor.Path("Amount")
        ValueProvider<GridAcDetailsMdl, String> amount();
        
//==============================================HSN Detail=====================================//
        @Editor.Path("HsnName")
        ValueProvider<GridAcDetailsMdl, String> hsnName();

        @Editor.Path("HsnSac")
        ValueProvider<GridAcDetailsMdl, String> hsnSac();

        @Editor.Path("SgstPer")
        ValueProvider<GridAcDetailsMdl, String> sgstPer();

        @Editor.Path("SgstUgstPer")
        ValueProvider<GridAcDetailsMdl, String> sgstUgstPer();

        @Editor.Path("CgstPer")
        ValueProvider<GridAcDetailsMdl, String> cgstPer();

        @Editor.Path("IgstPer")
        ValueProvider<GridAcDetailsMdl, String> igstPer();

        @Editor.Path("UgstPer")
        ValueProvider<GridAcDetailsMdl, String> ugstPer();

        @Editor.Path("SgstAmt")
        ValueProvider<GridAcDetailsMdl, String> sgstAmt();

        @Editor.Path("CgstAmt")
        ValueProvider<GridAcDetailsMdl, String> cgstAmt();

        @Editor.Path("IgstAmt")
        ValueProvider<GridAcDetailsMdl, String> igstAmt();

        @Editor.Path("UgstAmt")
        ValueProvider<GridAcDetailsMdl, String> ugstAmt();

        @Editor.Path("SgstUgstAmt")
        ValueProvider<GridAcDetailsMdl, String> sgstUgstAmt();

        @Editor.Path("TaxableAmt")
        ValueProvider<GridAcDetailsMdl, String> taxableAmt();
    }

    final DmGrid<GridAcDetailsMdl, Grd_PrpAcs> grdAccountDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid createAcGrid() {
        grdAccountDetail.addRowNumberColumn();
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().accMCode(), "A/c Code", 90, null);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().accName(), "A/c Name", 200, null);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().narration(), "Narration", 100, null);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().amount(), "Amount", 90, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        //==============================================HSN Detail=====================================//
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().hsnName(), "HSN Name", 100, null);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().hsnSac(), "HSN/SAC", 80, null);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().taxableAmt(), "Taxable Amt", 90, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().sgstUgstPer(), "SGST/UGST %", 80, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().sgstUgstAmt(), "SGST/UGST", 90, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().cgstPer(), "CGST %", 80, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().cgstAmt(), "CGST", 90, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().igstPer(), "IGST %", 80, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().igstAmt(), "IGST", 90, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAccountDetail.initializeGrid(grdAccountDetail.getPropertyAccess().fieldHashCode(), 1280, 170, 4);
        return grdAccountDetail.getGrid();
    }
    //</editor-fold>
    
}
