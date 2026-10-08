package dataman.accounts.wholesaleBillSettlement.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.accounts.wholesaleBillSettlement.shared.Find_Mdl;
import dataman.accounts.wholesaleBillSettlement.shared.GrdPendingFPReqDetail_Mdl;
import dataman.accounts.wholesaleBillSettlement.shared.GridDrAdjustBills_Mdl;
import dataman.accounts.wholesaleBillSettlement.shared.TxtHAccountName_Mdl;
import dataman.accounts.wholesaleBillSettlement.shared.TxtHSupplier_Mdl;
import dataman.accounts.wholesaleBillSettlement.shared.TxtHVoucherType_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
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
import dataman.libraryMenu.self.shared.lmAccounts;

public abstract class WholesaleBillSettlement_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().WHOLE_SALE_BILL_SETTLEMENT.caption;
    final String moduleName = new lmAccounts().moduleName;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Approved By";

    final DmUserControl userControl = new DmUserControl();
    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    public static final String tabFPDetail = "Pending F.P. Req. Details";
    final Button btnApprovedBy = new Button();
    final FlowPanel flwFPDetail = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtPayNo = new DmTextBox(20, true);
    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final InlineLabel chkCheckAll = new InlineLabel();
    final DmTextBoxNumeric txtPartyAcBalance = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, true, 10, false);
    final DmTextBoxNumeric txtBankCashBalance = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, true, 10, false);
    final DmTextBoxNumeric txtSelectedBillDr = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtLessOtherCr = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtNetDifference = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
//    final DmTextBoxNumeric txtOriginalAmount = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);
//    final DmTextBoxNumeric txtCreditAmt = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);
    final DmTextBoxNumeric txtAmountPaid = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);
    final DmTextBoxNumeric txtTdsDeduction = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);
    final DmRemarkTextBox txtRemak = new DmRemarkTextBox(200, false);
    final DmTextBox txtSupplierDisplayName = new DmTextBox(100, true);
    final DmTextBoxNumeric txtCrAdjustAmt = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, true, 10, false);
    final InlineLabel drChkCheckAll = new InlineLabel();

    //<editor-fold defaultstate="collapsed" desc="txtHSupplier">
    interface TxtHSupplierName_PrpAcs extends KeyField_T_PrpAcs<TxtHSupplier_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHSupplier_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHSupplier_Mdl, String> cityName();

    }
    final DmTextHelp<TxtHSupplier_Mdl, TxtHSupplierName_PrpAcs> txtHSupplier = new DmTextHelp(GWT.create(TxtHSupplierName_PrpAcs.class), 7, true);

    private void iniSupplier() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHSupplier.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHSupplier.getHelper().addSort("cityName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHVoucherType">
    interface TxtHVoucherType extends KeyField_T_PrpAcs<TxtHVoucherType_Mdl> {

    }

    final DmTextHelp<TxtHVoucherType_Mdl, TxtHVoucherType> txtHVoucherType = new DmTextHelp(GWT.create(TxtHVoucherType.class), 7, true);

    public void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Name", 1, "Description");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHVoucherType.getHelper().addSort("Description", DmSortType.ASC);
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

    public void iniTxtHAccountName() {
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

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("PayDate")
        ValueProvider<Find_Mdl, String> payDate();

        @Editor.Path("Supplier")
        ValueProvider<Find_Mdl, String> supplier();

        @Editor.Path("Remark")
        ValueProvider<Find_Mdl, String> remark();

        @Editor.Path("OrgAmt")
        ValueProvider<Find_Mdl, String> orgAmt();

        @Editor.Path("ApprovedBy")
        ValueProvider<Find_Mdl, String> approvedBy();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22, true);

    public void iniFind() {

        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Pay. Req. No.", 100, "recid");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().payDate(), "Date", 80, "date", DmEnum.DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplier(), "Supplier", 200, "supplierName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().orgAmt(), "Final Amt.", 100, "finalAmt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().remark(), "Remark", 300, "remark");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approvedBy(), "Approved By", 150, "approvedBy");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 500,3);
        dmFind.getHelper().addSort("date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("V_No", DmEnum.DmSortType.ASC, "V_No");
        dmFind.getHelper().addSort("recid", DmEnum.DmSortType.ASC, "recid");

        dmFind.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<Find_Mdl>() {
            @Override
            public String getColStyle(Find_Mdl model, ValueProvider<? super Find_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (!DmShared.xNull(model.getApprovedBy()).equalsIgnoreCase("")) {
                    styleClass += "dm-GridCellDairyConfirm ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(Find_Mdl model, int rowIndex) {
                return "";
            }
        });
    }
//</editor-fold>

    public WholesaleBillSettlement_Dsg() {
        ini();
        iniFind();
        iniSupplier();
        iniTxtHVoucherType();
        iniTxtHAccountName();
    }

    private void ini() {
        Button button;
        Anchor aBtn;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addTextBlog(modifiedByBlog, 100, false, true);
        userControl.addGap(80);

        userControl.addTextBlog(approvedByBlog, 100, true, false);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnApprovedBy.setTitle("Approved By");
        btnApprovedBy.addStyleName("dm-top-control-btnStyle");
        btnApprovedBy.getElement().appendChild(aBtn.getElement());
        btnApprovedBy.setWidth("30px");
        userControl.addControlBlog(approvedByBlog, btnApprovedBy);

        vrtInnerForm.add(userControl);
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "497px", null, null, DmEnum.DmPosition.CENTER, true);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "42px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "42px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(leftArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "42px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(rightArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================<Grid Area >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "403px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "400px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(pendingFPReqDetailTabArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "400px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(PendingToBillAdjustDetails()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Summary Area >====================================*/
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + ";border-top:1px solid gray; border-bottom:1px solid gray; margin:6px 1px 0px;");
        DmClient.setStyle(flwTr, "99%", "45px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(summaryArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel pendingFPReqDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "384px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdFpReqDetail());
        flwTable.add(chkCheckAll);
        chkCheckAll.addStyleName("fa fa-square-o");
        chkCheckAll.getElement().setAttribute("style", "position:absolute; top:5px; left:33px; z-index:10;cursor:pointer;");
        chkCheckAll.setTitle("Select all");

        return flwTable;
    }

    private FlowPanel PendingToBillAdjustDetails() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "384px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdDrdAdjust());
        flwTable.add(drChkCheckAll);
        drChkCheckAll.addStyleName("fa fa-square-o");
        drChkCheckAll.getElement().setAttribute("style", "position:absolute; top:5px; left:33px; z-index:10;cursor:pointer;");
        drChkCheckAll.setTitle("Select all");
        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "96%", "42px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setMarginTop(4, Style.Unit.PX);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Party Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSupplier));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "42px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setMarginTop(4, Style.Unit.PX);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Voucher No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPayNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);


        /*====================================< 2nd Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
//        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        lbl = new DmLabel("A/c Name");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHAccountName));
//        flwTr.add(flwTd);
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        lbl = new DmLabel("Credit Amt");
//        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
//        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd.add(vl.add(txtCreditAmt));
//        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel summaryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "42px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Debit");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtSelectedBillDr));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Credit");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtCrAdjustAmt));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Difference");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtNetDifference));
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        lbl = new DmLabel("Total Amount");
//        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
//        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
//        flwTd.add(vl.add(txtOriginalAmount));
//        flwTr.add(flwTd);
        flwTable.add(flwTr);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "58%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemak));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Pening FP Req. Detail">
    interface GridFPReqDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPendingFPReqDetail_Mdl> {

        @Editor.Path("VoucherType")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> voucherType();

        @Editor.Path("VoucherDate")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> voucherDate();

        @Editor.Path("PartyBillNo")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> partyBillNo();
//
//        @Editor.Path("PartyBillDate")
//        ValueProvider<GrdPendingFPReqDetail_Mdl, String> partyBillDate();

//        @Editor.Path("DebtorName")
//        ValueProvider<GrdPendingFPReqDetail_Mdl, String> debtorName();

        @Editor.Path("BillAmountAmt")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> billAmountAmt();

//        @Editor.Path("AdjustedAmt")
//        ValueProvider<GrdPendingFPReqDetail_Mdl, String> adjustedAmt();

//        @Editor.Path("AdjustmentAmt")
//        ValueProvider<GrdPendingFPReqDetail_Mdl, String> adjustmentAmt();

        @Editor.Path("Checked")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> checked();

    }
    final DmGrid<GrdPendingFPReqDetail_Mdl, GridFPReqDetail_PrpAcs> grdPeningFPReqDetail = new DmGrid(GWT.create(GridFPReqDetail_PrpAcs.class));

    private Grid iniGrdFpReqDetail() {

        ColumnConfig<GrdPendingFPReqDetail_Mdl, String> selectCheckBox;
        grdPeningFPReqDetail.addRowNumberColumn();
        selectCheckBox = grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().checked(), "", 25, "");
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().name(), "Vr. No.", 120, "sbRecId");
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().voucherDate(), "Vr. Date", 100, "voucherDate");
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().voucherType(), "Vr. Type", 80, "voucherType");
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().partyBillNo(), "Ref. No.", 130, "partyBillNo");
//        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().partyBillDate(), "PO Date", 110, "partyBillDate");
//        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().debtorName(), "Party Ledger", 300, "debtorName");
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().billAmountAmt(), "Debit", 140, "billAmount").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().adjustedAmt(), "Adjusted", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().adjustmentAmt(), "Adjustment", 120, "advanceSettled").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPeningFPReqDetail.initializeGrid(grdPeningFPReqDetail.getPropertyAccess().fieldHashCode(), 635, 400, 5);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                if (DmShared.iNull(grdPeningFPReqDetail.getGrid().getStore().get(context.getIndex()).getChecked()).intValue() == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                } else if (DmShared.iNull(grdPeningFPReqDetail.getGrid().getStore().get(context.getIndex()).getChecked()).intValue() == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                }

            }
        });
        return grdPeningFPReqDetail.getGrid();

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DRD Adjust Detail">
    interface GridDrdAdjustBills_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GridDrAdjustBills_Mdl> {

        @Editor.Path("Checked")
        ValueProvider<GridDrAdjustBills_Mdl, String> checked();

//        @Editor.Path("SupplierText")
//        ValueProvider<GridDrAdjustBills_Mdl, String> supplierText();

        @Editor.Path("VoucherDate")
        ValueProvider<GridDrAdjustBills_Mdl, String> voucherDate();

        @Editor.Path("VoucherType")
        ValueProvider<GridDrAdjustBills_Mdl, String> voucherType();

        @Editor.Path("RefNo")
        ValueProvider<GridDrAdjustBills_Mdl, String> refNo();

        @Editor.Path("PendingAdjustment")
        ValueProvider<GridDrAdjustBills_Mdl, String> pendingAdjustment();
    }
    final DmGrid<GridDrAdjustBills_Mdl, GridDrdAdjustBills_PrpAcs> grdDrdAdjustBills = new DmGrid(GWT.create(GridDrdAdjustBills_PrpAcs.class));

    private Grid iniGrdDrdAdjust() {
        ColumnConfig<GridDrAdjustBills_Mdl, String> selectCheckBox;
        grdDrdAdjustBills.addRowNumberColumn();
        selectCheckBox = grdDrdAdjustBills.addTextColumn(grdDrdAdjustBills.getPropertyAccess().checked(), "", 25, "");
        grdDrdAdjustBills.addTextColumn(grdDrdAdjustBills.getPropertyAccess().name(), "Vr. No.", 120, "billRecId");
        grdDrdAdjustBills.addTextColumn(grdDrdAdjustBills.getPropertyAccess().voucherDate(), "Vr. Date", 100, "voucherDate");
        grdDrdAdjustBills.addTextColumn(grdDrdAdjustBills.getPropertyAccess().voucherType(), "Vr. Type", 80, "voucherType");
        grdDrdAdjustBills.addTextColumn(grdDrdAdjustBills.getPropertyAccess().refNo(), "Ref. No.", 110, "refNo");
//        grdDrdAdjustBills.addTextColumn(grdDrdAdjustBills.getPropertyAccess().supplierText(), "Ledger", 200, "supplierName");
        grdDrdAdjustBills.addTextColumn(grdDrdAdjustBills.getPropertyAccess().pendingAdjustment(), "Credit", 140, "pendingAdjustment").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDrdAdjustBills.initializeGrid(grdDrdAdjustBills.getPropertyAccess().fieldHashCode(), 624, 400, 5);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                if (DmShared.iNull(grdDrdAdjustBills.getGrid().getStore().get(context.getIndex()).getChecked()).intValue() == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                } else if (DmShared.iNull(grdDrdAdjustBills.getGrid().getStore().get(context.getIndex()).getChecked()).intValue() == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                }

            }
        });
        return grdDrdAdjustBills.getGrid();
    }
    //</editor-fold>
}
