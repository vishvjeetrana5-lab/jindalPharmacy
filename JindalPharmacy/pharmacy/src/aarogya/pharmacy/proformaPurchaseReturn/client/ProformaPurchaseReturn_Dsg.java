package aarogya.pharmacy.proformaPurchaseReturn.client;

import aarogya.pharmacy.proformaPurchaseReturn.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.taxSummary.client.TaxSummary;
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
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.overHead.client.OverHead;
import dataman.dmToolkit.overHead.shared.OverHeadDimension;

abstract class ProformaPurchaseReturn_Dsg extends IsWidgetExtended {

    final pmPharmacy pmPharmacy = new pmPharmacy();
    final String widgetName = pmPharmacy.PROFORMA_PURCHASE_RETURN.caption;
    final String moduleName = pmPharmacy.PROFORMA_PURCHASE_RETURN.moduleName;
    final String preparedByBlog = "Prepared By";
    final String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Approved By";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwAddPBill = new FlowPanel();
    final Button btnAddPBill = new Button("Purchase Bill");

    final Button btnApprovedBy = new Button();
    final DmTextBox txtVoucherNo = new DmTextBox(20, false);
    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(100, false);

    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmGUI gui = new DmGUI(topControl, 450);
    final TaxSummary taxSummary = new TaxSummary(595, 140, 2);
    final OverHead overHead = new OverHead(topControl, new OverHeadDimension(370, 120, 0, 0, 0, false));
    final DmLabel lblDiscarded = new DmLabel("");

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Type", 0, "name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHVoucherType.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSupplier">
    interface TxtHVendor_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHSupplier_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHSupplier_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHSupplier_Mdl, String> cityName();

    }
    final DmTextHelp<TxtHSupplier_Mdl, TxtHVendor_PrpAcs> txtHSupplier = new DmTextHelp(GWT.create(TxtHVendor_PrpAcs.class), 6, true);

    private void iniTxtHSupplier() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().fieldHashCode(), 450, 0, 1);
        txtHSupplier.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHSupplier.getHelper().addSort("cityName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGstReason">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGstReason = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHReason() {
        txtHGstReason.getDmGrid().addTextColumn(txtHGstReason.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHGstReason.getDmGrid().initializeGrid(txtHGstReason.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHGstReason.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VoucherType")
        ValueProvider<Find_Mdl, String> voucherType();

        @Editor.Path("VoucherDate")
        ValueProvider<Find_Mdl, String> voucherDate();

        @Editor.Path("SupplierName")
        ValueProvider<Find_Mdl, String> supplierName();

        @Editor.Path("Approval")
        ValueProvider<Find_Mdl, String> approval();

        @Editor.Path("Status")
        ValueProvider<Find_Mdl, String> status();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    public void iniFind() {
        ColumnConfig<Find_Mdl, String> discarded;
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Return No.", 120, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 80, "v_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherType(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierName(), "Supplier", 150, "supplierName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approval(), "Approved By", 130, "approvedBy");
        discarded = dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().status(), "Status", 80, "status");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 4);
        
        discarded.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='color : red;'>" + DmShared.xNull(value) + "</div>");
            }
        });

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmSortType.ASC);
    }
    //</editor-fold>

    public ProformaPurchaseReturn_Dsg() {
        ini();
        iniFind();
        iniTxtHSupplier();
        iniTxtHVoucherType();
        iniTxtHReason();
    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", "left", null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");

        vrtInnerForm.add(vl.add(topControl));

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        vrtInnerForm.add(flwWorkingArea);
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(addUserControl());

        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmUserControl addUserControl() {
        Anchor aBtn;
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addTextBlog(modifiedByBlog, 100, false, true);
        userControl.addGeneralBlog(lblDiscarded);
        lblDiscarded.setHeight("15Px");
        lblDiscarded.getElement().getStyle().setColor("red");
        lblDiscarded.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblDiscarded.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblDiscarded.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        lblDiscarded.getElement().getStyle().setFontSize(12, Style.Unit.PX);

        userControl.addGap(100);

        userControl.addTextBlog(approvedByBlog, 100, true, false);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("green");
        btnApprovedBy.getElement().appendChild(aBtn.getElement());
        btnApprovedBy.setWidth("30px");
        userControl.addControlBlog(approvedByBlog, btnApprovedBy);

        return userControl;
    }

    private TabLayoutPanel workingArea() {
        DmClient.setStyle(tabPanel, "100%", "100%", "left", null, DmPosition.NONE, true);

        tabPanel.add(vl.add(tabGeneral()), "General");
        tabPanel.add(vl.add(tabTermsCondition()), "Terms & Conditions");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel tabTermsCondition() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.CENTER, true);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(gui));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel tabGeneral() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98.8%", null, "left", null, DmPosition.NONE, true);
        flwTable.getElement().getStyle().setMarginLeft(7, Style.Unit.PX);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.7%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(topLeftArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", null, "right", null, DmPosition.NONE, true);
        flwTd.add(vl.add(topRightArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdProformaDetail()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, null, null, "left", null, DmPosition.NONE, false);
        flwTd.add(taxSummary);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, null, null, "right", null, DmPosition.NONE, false);
        flwTd.add(vl.add(overHead));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        return flwTable;
    }

    private FlowPanel topLeftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Return Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Supplier");
        lbl.setTitle("Supplier - PT ");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "82%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSupplier));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reason");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "82%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGstReason));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel topRightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", null, "left", null, DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Proforma No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnAddPBill.setWidth("155px");
        btnAddPBill.setHeight("20px");
        btnAddPBill.getElement().setAttribute("style", btnAddPBill.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnAddPBill.setTitle("Add Purchase Bill");
        flwAddPBill.add(btnAddPBill);
        flwTd.add(flwAddPBill);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="GrdProformaPurchaseReturn">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemDetail_Mdl> {

        @Editor.Path("ItemCompany")
        ValueProvider<GrdItemDetail_Mdl, String> itemCompany();
        
        @Editor.Path("BatchNo")
        ValueProvider<GrdItemDetail_Mdl, String> batchNo();

        @Editor.Path("ExpiryDate")
        ValueProvider<GrdItemDetail_Mdl, String> expiryDate();

        @Editor.Path("Mrp")
        ValueProvider<GrdItemDetail_Mdl, String> mrp();

        @Editor.Path("SaleRate")
        ValueProvider<GrdItemDetail_Mdl, String> saleRate();

        @Editor.Path("Quantity")
        ValueProvider<GrdItemDetail_Mdl, String> quantity();

        @Editor.Path("FreeQty")
        ValueProvider<GrdItemDetail_Mdl, String> freeQty();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<GrdItemDetail_Mdl, String> purchaseUnitName();

        @Editor.Path("SkuQty")
        ValueProvider<GrdItemDetail_Mdl, String> skuQty();

        @Editor.Path("SkuName")
        ValueProvider<GrdItemDetail_Mdl, String> skuName();

        @Editor.Path("Rate")
        ValueProvider<GrdItemDetail_Mdl, String> rate();

        @Editor.Path("Amount")
        ValueProvider<GrdItemDetail_Mdl, String> amount();

        @Editor.Path("Remark")
        ValueProvider<GrdItemDetail_Mdl, String> remark();

    }
    final DmGrid<GrdItemDetail_Mdl, Grd_PrpAcs> grdItemDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid iniGrdProformaDetail() {
        grdItemDetail.addRowNumberColumn();
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().name(), "Medicine Name", 180, "");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().itemCompany(), "Medicine Company", 150, "");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().batchNo(), "Batch No.", 72, "");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().expiryDate(), "Expiry Date", 80, "");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().mrp(), "MRP", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().saleRate(), "Sale Rate", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().quantity(), "Quantity", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().freeQty(), "Free Qty.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().purchaseUnitName(), "Unit", 40, "");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().skuQty(), "SKU Qty.", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().skuName(), "SKU", 40, "");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().rate(), "Rate", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().amount(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().remark(), "Remark", 200, "");
        grdItemDetail.initializeGrid(grdItemDetail.getPropertyAccess().fieldHashCode(), 1260, 219);
        grdItemDetail.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
        grdItemDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdItemDetail_Mdl>() {
            @Override
            public String getColStyle(GrdItemDetail_Mdl model, ValueProvider<? super GrdItemDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.iNull(model.getIsProformaUsed()).intValue() == 1) {
                    styleClass += "dm-GridCellDairyConfirm ";
                } 
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdItemDetail_Mdl model, int rowIndex) {
                return "";
            }
        });
        return grdItemDetail.getGrid();
    }
    //</editor-fold>

}
