package dataman.accounts.advanceRequestEntry.client;

import dataman.accounts.advanceRequestEntry.shared.Find_Mdl;
import dataman.accounts.advanceRequestEntry.shared.GrdAdvPaymentDetail_Mdl;
import dataman.accounts.advanceRequestEntry.shared.GrdBillDetail_Mdl;
import dataman.accounts.advanceRequestEntry.shared.GrdFinalPaymentDetail_Mdl;
import dataman.accounts.advanceRequestEntry.shared.GrdGRNDetail_Mdl;
import dataman.accounts.advanceRequestEntry.shared.GrdPODetail_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.libraryMenu.self.shared.lmAccounts;

public abstract class AdvanceRequestEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().ADVANCE_REQUEST_ENTRY.caption;
    final String moduleName = new lmAccounts().moduleName;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    public static final String tabPODetail = "P.Order Details";
    public static final String tabGrnDetail = "GRN Details";
    public static final String tabBillDetail = "Bill Details";
    public static final String tabAdPayDetail = "Adv. Payment Details";
    public static final String tabFinalPayDetail = "Final Payment Details";

    final FlowPanel flwSummary = new FlowPanel();
    final FlowPanel flwPoDetail = new FlowPanel();
    final FlowPanel flwGrnDetail = new FlowPanel();
    final FlowPanel flwBillDetail = new FlowPanel();
    final FlowPanel flwAdvPaymentDetail = new FlowPanel();
    final FlowPanel flwFinalPayDetail = new FlowPanel();

    final FlowPanel flwPoSearch = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtReqNo = new DmTextBox(20, true);
    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBox txtPurchaseOrder = new DmTextBox(20, true);
    final DmTextBox txtPurchaseOrderDate = new DmTextBox(11, false);
    final DmTextBox txtSupplierName = new DmTextBox(30, false);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(30, false);
    final DmTextBox txtVoucherType = new DmTextBox();
    final DmTextBoxNumeric txtAmt = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);
    final DmTextBoxNumeric txtTotalPoValue = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtTotalMrn = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtTotalBill = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtTotalAdvance = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtTotalAdvanceTds = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtTotalAdvanceNetPay = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtTotalBillPay = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtAdvanceReqAmt = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);
    final DmTextBox txtDueDate = new DmTextBox(11, true);

    final Button btnPOSearch = new Button("...");

    final DmLabel cancelBy = new DmLabel("");
    //<editor-fold defaultstate="collapsed" desc="txtHModuleType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHModuleType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    public void iniModuleType() {
        txtHModuleType.getDmGrid().addTextColumn(txtHModuleType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHModuleType.getDmGrid().initializeGrid(txtHModuleType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHModuleType.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ReqDate")
        ValueProvider<Find_Mdl, String> reqDate();

        @Editor.Path("ModuleText")
        ValueProvider<Find_Mdl, String> moduleText();

        @Editor.Path("PoNoText")
        ValueProvider<Find_Mdl, String> poNoText();

        @Editor.Path("AdvanceReqAmt")
        ValueProvider<Find_Mdl, String> advanceReqAmt();

        @Editor.Path("DueDate")
        ValueProvider<Find_Mdl, String> dueDate();

        @Editor.Path("PreparedBy")
        ValueProvider<Find_Mdl, String> preparedBy();

        @Editor.Path("ModifiedBy")
        ValueProvider<Find_Mdl, String> modifiedBy();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22, true);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Req. No.", 150, "adReqNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().reqDate(), "Req. Date", 80, "adReqDate", DmEnum.DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().moduleText(), "Module", 150, "module");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().poNoText(), "PO. No.", 150, "poNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().advanceReqAmt(), "Advance Req. Amt.", 150, "advanceReqAmt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().dueDate(), "Due Date", 80, "dueDate", DmEnum.DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().preparedBy(), "Prepared By", 200, "preparedBy");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().modifiedBy(), "Modified By", 200, "u_Name");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 500);
        dmFind.getHelper().addSort("adReqDate", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("V_No", DmEnum.DmSortType.ASC, "V_No");
        dmFind.getHelper().addSort("adReqNo", DmEnum.DmSortType.ASC, "adReqNo");
        dmFind.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<Find_Mdl>() {
            @Override
            public String getColStyle(Find_Mdl model, ValueProvider<? super Find_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.xNull(model.getIsStatus()).equalsIgnoreCase("1")) {
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

    public AdvanceRequestEntry_Dsg() {
        ini();
        iniFind();
        iniModuleType();

    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(50);
        userControl.addGeneralBlog(cancelBy);
        cancelBy.setHeight("15Px");
        cancelBy.getElement().getStyle().setColor("red");
        cancelBy.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        cancelBy.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        cancelBy.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        cancelBy.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "517px", null, null, DmEnum.DmPosition.CENTER, true);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "50px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(topArea());
        flwTable.add(flwTr);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "377px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(tabArea());
        flwTable.add(flwTr);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + ";border-top:1px solid gray; border-bottom:1px solid gray; margin: 1px 0px;");
        DmClient.setStyle(flwTr, "100%", "55px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(summaryArea());
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "35px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(bottomArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel PoDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "344px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdPoDetail());
        return flwTable;
    }

    private FlowPanel GrnDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "344px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdGRNDetail());
        return flwTable;
    }

    private FlowPanel BillDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "344px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdBillDetail());
        return flwTable;
    }

    private FlowPanel AdvPaymentDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "344px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdAdvPaymentDetail());
        return flwTable;
    }

    private FlowPanel FinalPayDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "344px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdFinalPaymentDetail());
        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        int tabIndex = 0;
        DmClient.setStyle(tabPanel, "100%", "380px", null, null, DmEnum.DmPosition.NONE, true);

        tabPanel.add(flwPoDetail, tabPODetail);
        tabIndex++;

        tabPanel.add(flwGrnDetail, tabGrnDetail);
        tabIndex++;

        tabPanel.add(flwBillDetail, tabBillDetail);
        tabIndex++;

        tabPanel.add(flwAdvPaymentDetail, tabAdPayDetail);
        tabIndex++;

        tabPanel.add(flwFinalPayDetail, tabFinalPayDetail);
        tabIndex++;

        flwPoDetail.add(PoDetailTabArea());
        flwGrnDetail.add(GrnDetailTabArea());
        flwBillDetail.add(BillDetailTabArea());
        flwAdvPaymentDetail.add(AdvPaymentDetailTabArea());
        flwFinalPayDetail.add(FinalPayDetailTabArea());

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        HorizontalPanel hrtPnl;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "50px", null, null, DmEnum.DmPosition.CENTER, true);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Req. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtReqNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Req. Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Module");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHModuleType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("P.O. No.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        hrtPnl.add(txtPurchaseOrder);
        hrtPnl.setCellWidth(txtPurchaseOrder, "100%");
        DmClient.setStyle(flwPoSearch, "100%", "18spx", "left", null, DmEnum.DmPosition.NONE, true);

        btnPOSearch.setWidth("18px");
        btnPOSearch.setHeight("20px");

        btnPOSearch.getElement().setAttribute("style", btnPOSearch.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnPOSearch.addStyleName("dm-button-grad");
        btnPOSearch.setTitle("Patient Search");

        flwPoSearch.add(btnPOSearch);
        hrtPnl.add(flwPoSearch);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("P.O. Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPurchaseOrderDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmt));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Supplier Name");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtSupplierName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel summaryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "50px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total P.O. Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalPoValue));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total GRN Against P.O");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalMrn));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Bill Against P.O.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalBill));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Advance");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalAdvance));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Advance Tds");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalAdvanceTds));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Advance Net Pay");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalAdvanceNetPay));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Bill Payment");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalBillPay));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Advance Req.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtAdvanceReqAmt));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel bottomArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "30px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Due Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDueDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdPODetail">
    interface GridPoDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPODetail_Mdl> {

        @Editor.Path("Amt")
        ValueProvider<GrdPODetail_Mdl, String> amt();

        @Editor.Path("Unit")
        ValueProvider<GrdPODetail_Mdl, String> unit();

        @Editor.Path("Qty")
        ValueProvider<GrdPODetail_Mdl, String> qty();

        @Editor.Path("Rate")
        ValueProvider<GrdPODetail_Mdl, String> rate();

    }
    final DmGrid<GrdPODetail_Mdl, GridPoDetail_PrpAcs> grdPODetail = new DmGrid(GWT.create(GridPoDetail_PrpAcs.class));

    private Grid iniGrdPoDetail() {
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().name(), "Item Name", 370, "itemName");
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().unit(), "Unit", 180, "unit");
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().qty(), "Qty", 190, "quantity").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().rate(), "Rate", 150, "rate").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().amt(), "Amount", 150, "amt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdPODetail.initializeGrid(grdPODetail.getPropertyAccess().fieldHashCode(), 1260, 344);
        return grdPODetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdMRNDetail">
    interface GridMRNDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdGRNDetail_Mdl> {

        @Editor.Path("GrnNo")
        ValueProvider<GrdGRNDetail_Mdl, String> grnNo();

        @Editor.Path("ChallanNo")
        ValueProvider<GrdGRNDetail_Mdl, String> challanNo();

        @Editor.Path("ChallanDate")
        ValueProvider<GrdGRNDetail_Mdl, String> challanDate();

        @Editor.Path("Value")
        ValueProvider<GrdGRNDetail_Mdl, String> value();

        @Editor.Path("Status")
        ValueProvider<GrdGRNDetail_Mdl, String> status();

        @Editor.Path("EntryBy")
        ValueProvider<GrdGRNDetail_Mdl, String> entryBy();

        @Editor.Path("ItemName")
        ValueProvider<GrdGRNDetail_Mdl, String> itemName();

    }
    final DmGrid<GrdGRNDetail_Mdl, GridMRNDetail_PrpAcs> grdMRNDetail = new DmGrid(GWT.create(GridMRNDetail_PrpAcs.class));

    private Grid iniGrdGRNDetail() {
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().name(), "GRN Date", 100, "grnDate");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().grnNo(), "GRN No.", 200, "grnNo");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().challanNo(), "Challan No.", 120, "pChallanNo");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().challanDate(), "Challan Date", 100, "pChallanDate");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().itemName(), "Item", 300, "itemName");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().value(), "Value", 120, "value").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().status(), "Status", 80, "");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().entryBy(), "Entered By", 150, "preparedBy");
        grdMRNDetail.initializeGrid(grdMRNDetail.getPropertyAccess().fieldHashCode(), 1260, 344);
        return grdMRNDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdBillDetail">
    interface GridBillDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdBillDetail_Mdl> {

        @Editor.Path("BillNo")
        ValueProvider<GrdBillDetail_Mdl, String> billNo();

        @Editor.Path("PartyNo")
        ValueProvider<GrdBillDetail_Mdl, String> partyNo();

        @Editor.Path("PartyDate")
        ValueProvider<GrdBillDetail_Mdl, String> partyDate();

        @Editor.Path("Value")
        ValueProvider<GrdBillDetail_Mdl, String> value();

        @Editor.Path("Tds")
        ValueProvider<GrdBillDetail_Mdl, String> tds();

        @Editor.Path("NetPay")
        ValueProvider<GrdBillDetail_Mdl, String> netPay();

        @Editor.Path("Status")
        ValueProvider<GrdBillDetail_Mdl, String> status();

        @Editor.Path("LastLocation")
        ValueProvider<GrdBillDetail_Mdl, String> lastLocation();

        @Editor.Path("PaymDueDate")
        ValueProvider<GrdBillDetail_Mdl, String> paymDueDate();

        @Editor.Path("EnterdBy")
        ValueProvider<GrdBillDetail_Mdl, String> enterdBy();

        @Editor.Path("ItemName")
        ValueProvider<GrdBillDetail_Mdl, String> itemName();
    }
    final DmGrid<GrdBillDetail_Mdl, GridBillDetail_PrpAcs> grdBillDetail = new DmGrid(GWT.create(GridBillDetail_PrpAcs.class));

    private Grid iniGrdBillDetail() {

        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().name(), "Bill Date", 80, "v_date");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().billNo(), "Bill No.", 100, "recId");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().partyNo(), "Party No.", 80, "partyBillNo");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().partyDate(), "Party Date", 80, "partyBillDate");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().itemName(), "Item", 280, "itemName");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().value(), "Value", 100, "value").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().tds(), "TDS", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().netPay(), "Net Pay", 90, "netPay").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().status(), "Status", 65, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().lastLocation(), "Last Location", 120, "");
//        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().paymDueDate(), "Pay. due Dt.", 80, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().enterdBy(), "Entered By", 100, "preparedBy");
        grdBillDetail.initializeGrid(grdBillDetail.getPropertyAccess().fieldHashCode(), 1260, 344);
        return grdBillDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdAdvPaymnetDetail">
    interface GridAdvPaymnetDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdAdvPaymentDetail_Mdl> {

        @Editor.Path("No")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> no();

        @Editor.Path("Mode")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> mode();

        @Editor.Path("Bank")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> bank();

        @Editor.Path("ChqDate")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> chqDate();

        @Editor.Path("ChqNo")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> chqNo();

        @Editor.Path("Amt")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> amt();

        @Editor.Path("TdsDeduction")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> tdsDeduction();

        @Editor.Path("NetPay")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> netPay();
    }
    final DmGrid<GrdAdvPaymentDetail_Mdl, GridAdvPaymnetDetail_PrpAcs> grdAdvPaymnetDetail = new DmGrid(GWT.create(GridAdvPaymnetDetail_PrpAcs.class));

    private Grid iniGrdAdvPaymentDetail() {
        grdAdvPaymnetDetail.addRowNumberColumn();
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().name(), "Date", 80, "");
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().no(), "No.", 100, "");
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().mode(), "Mode", 100, "");
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().bank(), "Bank", 300, "");
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().chqDate(), "Ins. Date", 80, "");
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().chqNo(), "Ins. No.", 200, "");
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().amt(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().tdsDeduction(), "Tds", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().netPay(), "Net Pay", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAdvPaymnetDetail.initializeGrid(grdAdvPaymnetDetail.getPropertyAccess().fieldHashCode(), 1260, 344);
        return grdAdvPaymnetDetail.getGrid();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="grdFinalPaymnetDetail">
    interface GridFinalPaymnetDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdFinalPaymentDetail_Mdl> {

        @Editor.Path("No")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> no();

        @Editor.Path("Mode")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> mode();

        @Editor.Path("Bank")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> bank();

        @Editor.Path("ChqDate")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> chqDate();

        @Editor.Path("ChqNo")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> chqNo();

        @Editor.Path("Amt")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> amt();

        @Editor.Path("TdsDeduction")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> tdsDeduction();

        @Editor.Path("NetPay")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> netPay();

    }
    final DmGrid<GrdFinalPaymentDetail_Mdl, GridFinalPaymnetDetail_PrpAcs> grdFinalPaymnetDetail = new DmGrid(GWT.create(GridFinalPaymnetDetail_PrpAcs.class));

    private Grid iniGrdFinalPaymentDetail() {
        grdFinalPaymnetDetail.addRowNumberColumn();
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().name(), "Date", 80, "");
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().no(), "No.", 100, "");
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().mode(), "Mode", 100, "");
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().bank(), "Bank", 300, "");
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().chqDate(), "Ins. Date", 80, "");
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().chqNo(), "Ins. No.", 200, "");
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().amt(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().tdsDeduction(), "Tds", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().netPay(), "Net Pay", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdFinalPaymnetDetail.initializeGrid(grdFinalPaymnetDetail.getPropertyAccess().fieldHashCode(), 1260, 347);
        return grdFinalPaymnetDetail.getGrid();
    }
    //</editor-fold>
}
