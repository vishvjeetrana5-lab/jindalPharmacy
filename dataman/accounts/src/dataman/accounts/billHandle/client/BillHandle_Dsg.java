package dataman.accounts.billHandle.client;

import dataman.accounts.billHandle.shared.GrdBillDetail_Mdl;
import dataman.accounts.billHandle.shared.Find_Mdl;
import dataman.libraryMenu.self.shared.lmAccounts;
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
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmAccounts;

abstract class BillHandle_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().BILL_HANDLE.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Approved By";
    public static final String isCheckPermissionBlog = "Check/uncheck Permission";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtBillNo = new DmTextBox(20, true);
    final DmTextBox txtWTACode = new DmTextBox();

    final DmTextBox txtSearchText = new DmTextBox(50, false);
    final Button btnApprovedBy = new Button();
    final DmTextBox txtVDate = new DmTextBox(20, true);
    final DmTextBox txtFromBilldt = new DmTextBox(20, false);
    final DmTextBox txtToBilldt = new DmTextBox(20, false);
    final DmTextBox txtModule = new DmTextBox(20, true);
    protected byte ColCheckPBill = 0;
    //<editor-fold defaultstate="collapsed" desc="txtHBillHandleType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBillHandleType = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHRequisitionType() {
        txtHBillHandleType.getDmGrid().addTextColumn(txtHBillHandleType.getDmGrid().getPropertyAccess().name(), "Name", 0, "Description");
        txtHBillHandleType.getDmGrid().initializeGrid(txtHBillHandleType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHBillHandleType.getHelper().addSort("Description", DmSortType.ASC);
    }

//    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHFromLocation">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHFromLocation = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    public void iniTxtHDepartmentBy() {
        txtHFromLocation.getDmGrid().addTextColumn(txtHFromLocation.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHFromLocation.getDmGrid().initializeGrid(txtHFromLocation.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHFromLocation.getHelper().addSort("NAME", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHToLocation">
    interface DmDepartment_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> sharedModifiedBy();

    }
    final DmTextHelp<KeyFieldExtended, DmDepartment_PrpAcs> txtHToLocation = new DmTextHelp(GWT.create(DmDepartment_PrpAcs.class), 10, true);

    public void iniTxtHDepartmentTo() {
        txtHToLocation.getDmGrid().addTextColumn(txtHToLocation.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHToLocation.getDmGrid().initializeGrid(txtHToLocation.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
        txtHToLocation.getHelper().addSort("NAME", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface Find_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("FromLocationName")
        ValueProvider<Find_Mdl, String> fromLocationName();

        @Editor.Path("Date")
        ValueProvider<Find_Mdl, String> date();

        @Editor.Path("ToLocationName")
        ValueProvider<Find_Mdl, String> toLocationName();

        @Editor.Path("FromBilldate")
        ValueProvider<Find_Mdl, String> fromBilldate();

        @Editor.Path("ToBilldate")
        ValueProvider<Find_Mdl, String> toBilldate();

        @Editor.Path("Module")
        ValueProvider<Find_Mdl, String> module();

        @Editor.Path("ApprovedBy")
        ValueProvider<Find_Mdl, String> approvedBy();
    }

    final DmFind<Find_Mdl, Find_PrpAcs> dmFind = new DmFind(GWT.create(Find_PrpAcs.class), 22);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher No.", 120, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().date(), "V.Date", 80, "v_date");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().fromLocationName(), "From Location Name", 200, "fromLocationName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().toLocationName(), "To Location Name", 200, "toLocationName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().fromBilldate(), "From Bill date", 110, "fromBilldate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().toBilldate(), "To Bill date", 110, "toBilldate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().module(), "Mdoule", 110, "module");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approvedBy(), "Approved By", 100, "approvedBy");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 958, 3);

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmSortType.DESC, "v_No");
        dmFind.getHelper().addSort("recid", DmSortType.ASC);

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

    public BillHandle_Dsg() {
        ini();
        iniTxtHDepartmentBy();
        iniTxtHDepartmentTo();
//        iniTxtHRequisitionType();
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
        DmClient.setStyle(flwTr, "100%", "70px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "46px", "left", null, DmPosition.NONE, false);
        flwTd.add(leftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", "70px", "left", null, DmPosition.NONE, false);
        flwTd.add(middleArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", "70px", "left", null, DmPosition.NONE, false);
        flwTd.add(rightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "39px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Bill Handle No.");
        lblTemp.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBillNo));
        txtBillNo.setEnabled(false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Date");
        lblTemp.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVDate));
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
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSearchText));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("For Module");
        lblTemp.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtModule));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel middleArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "65px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("From Location");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHFromLocation));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("To Location");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHToLocation));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "65px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "42%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("From Bill Date");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromBilldt));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "42%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To Bill Date");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtToBilldt));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="grdBillDetail">
    interface GrdMedicineDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdBillDetail_Mdl> {

        @Editor.Path("Type")
        ValueProvider<GrdBillDetail_Mdl, String> type();

        @Editor.Path("VoucherNo")
        ValueProvider<GrdBillDetail_Mdl, String> voucherNo();

        @Editor.Path("Date")
        ValueProvider<GrdBillDetail_Mdl, String> date();

        @Editor.Path("PartyBillNo")
        ValueProvider<GrdBillDetail_Mdl, String> partyBillNo();

        @Editor.Path("PartyDate")
        ValueProvider<GrdBillDetail_Mdl, String> partyDate();

        @Editor.Path("PartyName")
        ValueProvider<GrdBillDetail_Mdl, String> partyName();

        @Editor.Path("NetAmt")
        ValueProvider<GrdBillDetail_Mdl, String> netAmt();

        @Editor.Path("PaymentDueDt")
        ValueProvider<GrdBillDetail_Mdl, String> paymentDueDt();

        @Editor.Path("LastStatusDt")
        ValueProvider<GrdBillDetail_Mdl, String> lastDueDt();

        @Editor.Path("Remark")
        ValueProvider<GrdBillDetail_Mdl, String> remark();

    }
    final DmGrid<GrdBillDetail_Mdl, GrdMedicineDetail_PrpAcs> grdBillDetail = new DmGrid(GWT.create(GrdMedicineDetail_PrpAcs.class));

    private Grid iniGrdMedicineDetail() {
        DmColumnConfig<GrdBillDetail_Mdl, String> locationSet;
        byte colIndex = 0;
        grdBillDetail.addRowNumberColumn();
        colIndex++;
        locationSet = grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().type(), "Check", 50, "");
        ColCheckPBill = colIndex;
        colIndex++;
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().type(), "Type", 180, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().voucherNo(), "V_No.", 60, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().date(), "Date", 100, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().partyBillNo(), "P.Bill No.", 100, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().partyDate(), "P.Bill Date", 80, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().partyName(), "Party Name", 200, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().netAmt(), "Net Amt.", 100, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().paymentDueDt(), "Payment Due Date", 100, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().lastDueDt(), "Last Status Date", 140, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().remark(), "Remark", 170, "");
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
        grdBillDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdBillDetail_Mdl>() {
            @Override
            public String getColStyle(GrdBillDetail_Mdl model, ValueProvider<? super GrdBillDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (!DmShared.xNull(model.getLastLoc()).equalsIgnoreCase("")) {
                    styleClass += "dm-GridCellDairyCancel ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdBillDetail_Mdl model, int rowIndex) {
                return "";
            }
        });

        return grdBillDetail.getGrid();
    }
//</editor-fold>

}
