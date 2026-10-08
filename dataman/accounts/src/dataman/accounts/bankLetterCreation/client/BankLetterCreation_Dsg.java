package dataman.accounts.bankLetterCreation.client;

import dataman.accounts.bankLetterCreation.shared.*;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmAccounts;

abstract class BankLetterCreation_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().BANK_LETTER_CREATION.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Approved By";
    public static final String isCheckPermissionBlog = "Check/uncheck Permission";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtBankLetterNo = new DmTextBox(20, true);
    final DmTextBox txtWTACode = new DmTextBox();

    final DmTextBox txtSearchText = new DmTextBox(50, false);
    final Button btnApprovedBy = new Button();
    final DmTextBox txtVDate = new DmTextBox(20, true);
    final DmTextBox txtFromFinalPayment = new DmTextBox(20, false);
    final DmTextBox txtToFinalPayment = new DmTextBox(20, false);
    final DmLabel cancelBy = new DmLabel("");
    protected byte ColCheckPBill = 0;

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface Find_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Date")
        ValueProvider<Find_Mdl, String> date();

        @Editor.Path("FromFinalPaymentDate")
        ValueProvider<Find_Mdl, String> fromFinalPaymentDate();

        @Editor.Path("toFinalPaymentDate")
        ValueProvider<Find_Mdl, String> toFinalPaymentDate();

        @Editor.Path("ApprovedBy")
        ValueProvider<Find_Mdl, String> approvedBy();
    }

    final DmFind<Find_Mdl, Find_PrpAcs> dmFind = new DmFind(GWT.create(Find_PrpAcs.class), 22);

    public void iniFind() {

        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Bulk Pay. Letter No.", 120, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().date(), "Bulk Pay. Letter Date", 120, "v_date");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().fromFinalPaymentDate(), "Final Payment Entry From Date", 220, "fromBilldate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().toFinalPaymentDate(), "Final Payment Entry Approval To Datee", 220, "toBilldate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approvedBy(), "Approved By", 100, "approvedBy");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 958);

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
        dmFind.getHelper().addSort("v_No", DmSortType.DESC, "v_No");

        dmFind.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<Find_Mdl>() {
            @Override
            public String getColStyle(Find_Mdl model, ValueProvider<? super Find_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (!DmShared.xNull(model.getApprovedBy()).equalsIgnoreCase("")) {
                    styleClass += "dm-GridCellDairyConfirm ";
                }

                if (DmShared.iNull(model.getIsDiscarded()).intValue() == 1) {
                    styleClass += "dm-GridCellDairyCancel ";
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

    public BankLetterCreation_Dsg() {
        ini();

        iniFind();
    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addTextBlog(modifiedByBlog, 100, false, true);
        userControl.addGeneralBlog(cancelBy);
        cancelBy.setHeight("15Px");
        cancelBy.getElement().getStyle().setColor("red");
        cancelBy.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        cancelBy.getElement().getStyle().setFontSize(13, Style.Unit.PX);
        userControl.addGap(100);
        userControl.addTextBlog(approvedByBlog, 100, true, false);
        Anchor aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("green");
        btnApprovedBy.getElement().appendChild(aBtn.getElement());
        btnApprovedBy.setWidth("30px");
        userControl.addControlBlog(approvedByBlog, btnApprovedBy);
        vrtInnerForm.add(userControl);

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable;
        FlowPanel flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "450px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "47px", "left", null, DmPosition.NONE, false);
        flwTr.add(topArea());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "415px", "left", null, DmPosition.NONE, false);
        flwTr.getElement().getStyle().setMarginLeft(4, Style.Unit.PX);
        flwTr.add(vl.add(iniGrdMedicineDetail()));
        flwTable.add(flwTr);


        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "68px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "55px", "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(leftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(rightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;
        Anchor aSearch = new Anchor();

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Bank Letter No.");
        lblTemp.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBankLetterNo));
        txtBankLetterNo.setEnabled(false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Date");
        lblTemp.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVDate));
        txtVDate.setEnabled(false);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Search");
        lblTemp.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSearchText));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", "21px", "left", null, DmPosition.NONE, true);
        aSearch.setStyleName("fa fa-search ");

        flwTd.add(aSearch);
        flwTd.getElement().getStyle().setMarginTop(0.4, Style.Unit.PCT);
        flwTd.getElement().getStyle().setMarginLeft(0.5, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "42%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Final Payment Entry From Date");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromFinalPayment));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "42%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Final Payment Entry To Date");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtToFinalPayment));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="grdFinalPaymentDetail">
    interface GrdMedicineDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdBankLetterCreation_Mdl> {

        //
        @Editor.Path("PayDate")
        ValueProvider<GrdBankLetterCreation_Mdl, String> payDate();

        @Editor.Path("Module")
        ValueProvider<GrdBankLetterCreation_Mdl, String> module();

        @Editor.Path("Supplier")
        ValueProvider<GrdBankLetterCreation_Mdl, String> supplier();

        @Editor.Path("AmtPaid")
        ValueProvider<GrdBankLetterCreation_Mdl, String> amtPaid();

        @Editor.Path("PayMode")
        ValueProvider<GrdBankLetterCreation_Mdl, String> payMode();

        @Editor.Path("ChqNo")
        ValueProvider<GrdBankLetterCreation_Mdl, String> chqNo();

        @Editor.Path("Bank")
        ValueProvider<GrdBankLetterCreation_Mdl, String> bank();

        @Editor.Path("Remark")
        ValueProvider<GrdBankLetterCreation_Mdl, String> remark();
//
//        @Editor.Path("OrgAmt")
//        ValueProvider<GrdBankLetterCreation_Mdl, String> orgAmt();
//
//        @Editor.Path("Tds")
//        ValueProvider<GrdBankLetterCreation_Mdl, String> tds();

        @Editor.Path("Type")
        ValueProvider<GrdBankLetterCreation_Mdl, String> type();

    }
    final DmGrid<GrdBankLetterCreation_Mdl, GrdMedicineDetail_PrpAcs> grdBillDetail = new DmGrid(GWT.create(GrdMedicineDetail_PrpAcs.class));

    private Grid iniGrdMedicineDetail() {
        DmColumnConfig<GrdBankLetterCreation_Mdl, String> locationSet;
        byte colIndex = 0;
        grdBillDetail.addRowNumberColumn();
        colIndex++;
        locationSet = grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().type(), "Check", 50, "");
        ColCheckPBill = colIndex;
        colIndex++;
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().chqNo(), "Inst. No.", 100, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().name(), "Pay. No.", 100, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().payDate(), "Date", 80, "", DmEnum.DmDataType.DATE);
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().module(), "Module", 120, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().supplier(), "Supplier", 200, "");
//        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().orgAmt(), "Final Amt.", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().tds(), "TDS", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().amtPaid(), "Paid Amt.", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().payMode(), "Pay Mode", 100, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().bank(), "A/c Name", 200, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().remark(), "Remark", 183, "");
        grdBillDetail.initializeGrid(grdBillDetail.getPropertyAccess().fieldHashCode(), 1260, 415);

        locationSet.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        locationSet.setCellPadding(false);

        locationSet.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {

                boolean checkValue = grdBillDetail.getGrid().getStore().get(context.getIndex()).isCheck();

                if (checkValue) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em; margin-top:3%;'></span>");

                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em; margin-top:3%;'></span>");
                }

            }
        });

        return grdBillDetail.getGrid();
    }
//</editor-fold>

}
