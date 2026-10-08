package aarogya.pharmacy.purchaseOrder.client;

import aarogya.pharmacy.purchaseOrder.shared.TxtHQuotationNo_Mdl;
import aarogya.pharmacy.purchaseOrder.shared.Find_Mdl;
import aarogya.pharmacy.purchaseOrder.shared.GrdMedicineDetail_Mdl;
import aarogya.pharmacy.purchaseOrder.shared.TxtHSupplier_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.taxSummary.client.TaxSummary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.overHead.client.OverHead;
import dataman.dmToolkit.overHead.shared.OverHeadDimension;

abstract class PurchaseOrder_Dsg extends IsWidgetExtended {

    final pmPharmacy pmPharmacy = new pmPharmacy();
    final String widgetName = pmPharmacy.PURCHASE_ORDER.caption;
    final String moduleName = pmPharmacy.moduleName;
    final String preparedByBlog = "Prepared By";
    final String modifiedByBlog = "Modified By";
    public static final String approvedByBlog = "Approved By";
    public static final String unApprovedByBlog = "Un-Approved";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final Button btnApprovedBy = new Button();
    final DmTextBox txtOrderNo = new DmTextBox(20, false);
    final DmTextBox txtVoucherDate = new DmTextBox(20, true);
    final DmTextBox txtRefNo = new DmTextBox(20, false);
    final DmTextBox txtIndentNo = new DmTextBox(20, false);
    final DmTextBox txtRefDate = new DmTextBox();
    final DmTextBox txtWTACode = new DmTextBox();
    final DmTextBox txtEnquiryNo = new DmTextBox(15, false);
    final FlowPanel flwAddIndent = new FlowPanel();
    final Button btnAddIndent = new Button("Add Indent");
    final CheckBox cbShowUsedQuotationsAlso = new CheckBox();

    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmGUI gui = new DmGUI(topControl, 450);
    final OverHead overHead = new OverHead(topControl, new OverHeadDimension(400, 130, 0, 0, 0, false));
    final TaxSummary taxSummary = new TaxSummary(595, 140, 2);

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Type", 0, "name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHVoucherType.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHQuotationNo">
    interface TxtHQuotationNo_PrpAcs extends KeyField_T_PrpAcs<TxtHQuotationNo_Mdl> {

        @Editor.Path("SupplierRefDate")
        ValueProvider<TxtHQuotationNo_Mdl, String> supplierRefDate();

        @Editor.Path("SupplierRefNo")
        ValueProvider<TxtHQuotationNo_Mdl, String> supplierRefNo();

        @Editor.Path("EnquiryRefNo")
        ValueProvider<TxtHQuotationNo_Mdl, String> enquiryRefNo();

    }
    final DmTextHelp<TxtHQuotationNo_Mdl, TxtHQuotationNo_PrpAcs> txtHQuotationNo = new DmTextHelp(GWT.create(TxtHQuotationNo_PrpAcs.class), 7);

    private void iniQuotationNo() {
        txtHQuotationNo.getDmGrid().addTextColumn(txtHQuotationNo.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHQuotationNo.getDmGrid().addTextColumn(txtHQuotationNo.getDmGrid().getPropertyAccess().supplierRefNo(), "Supplier Ref. No.", 120, "supplierRefNo");
        txtHQuotationNo.getDmGrid().addTextColumn(txtHQuotationNo.getDmGrid().getPropertyAccess().supplierRefDate(), "Supplier Ref. Date", 120, "supplierRefDate", DmDataType.DATE);
        txtHQuotationNo.getDmGrid().initializeGrid(txtHQuotationNo.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHQuotationNo.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="txtHSupplier">
    interface TxtHSupplier_PrpAcs extends KeyField_T_PrpAcs<TxtHSupplier_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHSupplier_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHSupplier_Mdl, String> cityName();

    }
    final DmTextHelp<TxtHSupplier_Mdl, TxtHSupplier_PrpAcs> txtHSupplier = new DmTextHelp(GWT.create(TxtHSupplier_PrpAcs.class), 7, true);

    private void iniSupplierPT() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().cityName(), "City Name", 150, "cityName");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHSupplier.getHelper().addSort("name", DmSortType.ASC);
        txtHSupplier.getHelper().addSort("cityName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("OrderNoDate")
        ValueProvider<Find_Mdl, String> orderNoDate();

        @Editor.Path("SupplierPT")
        ValueProvider<Find_Mdl, String> supplierPT();

        @Editor.Path("RefNo")
        ValueProvider<Find_Mdl, String> refNo();

        @Editor.Path("RefNoDate")
        ValueProvider<Find_Mdl, String> refNoDate();

        @Editor.Path("QuotationNo")
        ValueProvider<Find_Mdl, String> quotationNo();

        @Editor.Path("EnquiryNo")
        ValueProvider<Find_Mdl, String> enquiryNo();

        @Editor.Path("Currency")
        ValueProvider<Find_Mdl, String> currency();

        @Editor.Path("PurchaseType")
        ValueProvider<Find_Mdl, String> purchaseType();

        @Editor.Path("Approval")
        ValueProvider<Find_Mdl, String> approval();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "PO No.", 100, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().orderNoDate(), "Date", 80, "", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().purchaseType(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierPT(), "Supplier", 1, "vendorName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().refNo(), "Ref. No.", 75, "RefNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().refNoDate(), "Ref. No. Date", 100, "RefDate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().quotationNo(), "Quotation No.", 100, "qRecId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().enquiryNo(), "Enquiry No.", 100, "enquiryNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approval(), "Approval", 150, "approvedBy");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 4);//183

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmSortType.ASC);
    }
    //</editor-fold>

    public PurchaseOrder_Dsg() {
        ini();
        iniFind();
        iniSupplierPT();
        iniTxtHVoucherType();
        iniQuotationNo();
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

        tabPanel.add(tabGeneral(), "General");
        tabPanel.add(tabTermsCondition(), "Terms & Conditions");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");
        return tabPanel;
    }

    private FlowPanel tabTermsCondition() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);
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
        DmClient.setStyle(flwTable, "98.8%", "455px", null, null, DmPosition.CENTER, true);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.7%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(topLeftArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.9%", null, "right", null, DmPosition.NONE, true);
        flwTd.add(vl.add(topRightArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(createGrid()));
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
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PO Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Supplier");
        lbl.setTitle("Supplier - PT;");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSupplier));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Ref. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRefNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRefDate));

        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel topRightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PO No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtOrderNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
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
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Quotation No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        cbShowUsedQuotationsAlso.getElement().setAttribute("Style", "float: left; padding-top: 1px;");
        cbShowUsedQuotationsAlso.setTitle("Check to view used quotations also");
        cbShowUsedQuotationsAlso.setEnabled(false);
        flwTd.add(cbShowUsedQuotationsAlso);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHQuotationNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Enq. No.");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEnquiryNo));

        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnAddIndent.setWidth("159px");
        btnAddIndent.setHeight("20px");
        btnAddIndent.getElement().setAttribute("style", btnAddIndent.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnAddIndent.setTitle("Add Indent");
        flwAddIndent.getElement().setAttribute("Style", "width:100%; float:left;");
//        flwAddIndent.add(btnAddIndent);
        flwTd.add(flwAddIndent);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMedicineDetail_Mdl> {
        
        @Editor.Path("MedicineCompanyName")
        ValueProvider<GrdMedicineDetail_Mdl, String> medicineCompanyName();

        @Editor.Path("Remark")
        ValueProvider<GrdMedicineDetail_Mdl, String> Remark();

        @Editor.Path("OrderQuantity")
        ValueProvider<GrdMedicineDetail_Mdl, String> OrderQty();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<GrdMedicineDetail_Mdl, String> purchaseUnitName();

        @Editor.Path("Rate")
        ValueProvider<GrdMedicineDetail_Mdl, String> Rate();

        @Editor.Path("Mrp")
        ValueProvider<GrdMedicineDetail_Mdl, String> mrp();

        @Editor.Path("PurchaseRate")
        ValueProvider<GrdMedicineDetail_Mdl, String> purchaseRate();

        @Editor.Path("Amount")
        ValueProvider<GrdMedicineDetail_Mdl, String> Amount();

        @Editor.Path("ReqDate")
        ValueProvider<GrdMedicineDetail_Mdl, String> ReqDate();

        @Editor.Path("LastRateComparison")
        ValueProvider<GrdMedicineDetail_Mdl, String> lastRateComparison();

        @Editor.Path("LastRateComparisonName")
        ValueProvider<GrdMedicineDetail_Mdl, String> lastRateComparisonName();

    }
    final DmGrid<GrdMedicineDetail_Mdl, Grd_PrpAcs> grdItemDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid createGrid() {
        grdItemDetail.addRowNumberColumn();
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().name(), "Medicine", 125, "itemName");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().medicineCompanyName(), "Medicine CompanyName", 110, "itemName");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().OrderQty(), "Order Qty.", 75, "quantity").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().purchaseUnitName(), "Unit", 65, "purchaseUnitName");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().Rate(), "Rate", 75, "rate").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().mrp(), "MRP", 75, "mrp").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().purchaseRate(), "Prv. Rate", 75, "purchaseRate").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().Amount(), "Amount", 100, "oh_Amt_Gross").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().ReqDate(), "Required Date", 90, "reqDate", DmDataType.DATE);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().lastRateComparisonName(), "R.S.", 30, "lastRateComparisonName");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().Remark(), "Remark", 250, "remark");
        grdItemDetail.initializeGrid(grdItemDetail.getPropertyAccess().fieldHashCode(), 1260, 240, 2);
        grdItemDetail.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        grdItemDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdMedicineDetail_Mdl>() {
            @Override
            public String getColStyle(GrdMedicineDetail_Mdl model, ValueProvider<? super GrdMedicineDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.iNull(model.getIsStatus()).intValue() == 1) {
                    styleClass += "dm-GridCellDairyCancel ";
                } else if (DmShared.iNull(model.getIsStatus()).intValue() == 2) {
                    styleClass += "dm-GridCellDairyConfirm ";
                } else if (DmShared.vNull(model.getChallanQyt()) != 0) {
                    styleClass += "dm-GridCellDisable ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdMedicineDetail_Mdl model, int rowIndex) {
                return "";
            }
        });

        return grdItemDetail.getGrid();
    }
    //</editor-fold>

}
