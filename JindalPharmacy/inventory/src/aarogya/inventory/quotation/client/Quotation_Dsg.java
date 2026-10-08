package aarogya.inventory.quotation.client;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.overHead.client.OverHead;
import dataman.dmToolkit.overHead.shared.OverHeadDimension;
import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.quotation.shared.Find_Mdl;
import aarogya.inventory.quotation.shared.GrdItemDetail_Mdl;
import aarogya.inventory.quotation.shared.TxtHEnquiry_Mdl;
import aarogya.inventory.quotation.shared.TxtHSupplier_Mdl;
import aarogya.selfToolkit.serviceGrid.client.ServiceGrid;
import aarogya.selfToolkit.taxSummary.client.TaxSummary;
import com.google.gwt.user.client.ui.InlineLabel;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton;
import dataman.dmToolkit.dmFileUploadButton.shared.IFileUploadNotifier;
import dataman.dmToolkit.dmStatic.shared.DmShared;

abstract class Quotation_Dsg extends IsWidgetExtended implements IFileUploadNotifier {

    final pmInventory pmInventory = new pmInventory();
    final String widgetName = pmInventory.QUOTATION.caption;
    final String moduleName = pmInventory.moduleName;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmGUI gui = new DmGUI(topControl, 450);
//    final OverHead overHead = new OverHead(topControl, new OverHeadDimension(353, 120, 0, 0, 0, true));

    final DmTextBox txtQuotationDate = new DmTextBox(11, true);
    final DmTextBox txtQuotationNo = new DmTextBox(20, false);

    final DmTextBox txtVendorRefNo = new DmTextBox(30, true);
    final DmTextBox txtVendorQNDate = new DmTextBox(11, true);
    final DmTextBox txtValidUptoDate = new DmTextBox(11, false);

    final DmTextBox txtEnquiryDate = new DmTextBox(11, false);
    final DmTextBox txtEnquiryRefNo = new DmTextBox(30, false);

    final FlowPanel flwAddIndent = new FlowPanel();
    final Button btnAddIndents = new Button("Add Indent");

    final ServiceGrid serviceDetailGrid = new ServiceGrid(632, 120);
    final TaxSummary taxSummary = new TaxSummary(625, 140, 1);
    final TabLayoutPanel tabPanel2 = new TabLayoutPanel(1.6, Style.Unit.EM);

    //<editor-fold defaultstate="collapsed" desc="OverHead Containers">
    final DmTextBoxNumeric txtGoodsAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtServiceAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtDiscount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtTaxableAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSgstUgst = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtCGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtIGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSubTotal = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtFinalDiscountPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtFinalDicount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtTotalAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtRoundoff = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtNetAmount = new DmTextBoxNumeric(2, true, 12, false);
    //final DmTextBoxNumeric txtLandedValue = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtUGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmLabel lblSgstUgst = new DmLabel("SGST/UGST");

    //</editor-fold>
    //======================< Attachments >=================//
    public final static String attachFileBlog = "Attach Files";
    public final static String deleteFileBlog = "Delete Files";
    final MenuItem manageFileMenu = new MenuItem("Mange Files");
    final MenuItem mnuDownloadCsv = new MenuItem("Download Csv");
    final Menu menu = new Menu();

    DmFileUploadButton btnImport;
    final FlowPanel flwImport = new FlowPanel();
    private DmFileRuleType fileRuleType;
    final DmLabel lblFileName = new DmLabel("");

    //<editor-fold defaultstate="collapsed" desc="txtHQuotationType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHQuotationType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHQuotationType() {
        txtHQuotationType.getDmGrid().addTextColumn(txtHQuotationType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHQuotationType.getDmGrid().initializeGrid(txtHQuotationType.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHQuotationType.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSupplier">
    interface TxtHSupplier_PrpAcs extends KeyField_T_PrpAcs<TxtHSupplier_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHSupplier_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHSupplier_Mdl, String> cityName();
    }

    final DmTextHelp<TxtHSupplier_Mdl, TxtHSupplier_PrpAcs> txtHSupplier = new DmTextHelp(GWT.create(TxtHSupplier_PrpAcs.class), 6, true);

    private void iniTxtHSupplier() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 500, 0, 1);
        txtHSupplier.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHSupplier.getHelper().addSort("cityName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHEnquiryNo">
    interface TxtHIEnquiryNo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHEnquiry_Mdl> {

        @Editor.Path("EnquiryDate")
        ValueProvider<TxtHEnquiry_Mdl, String> enquiryDate();

        @Editor.Path("EnquiryRefNo")
        ValueProvider<TxtHEnquiry_Mdl, String> enquiryRefNo();

    }

    final DmTextHelp<TxtHEnquiry_Mdl, TxtHIEnquiryNo_PrpAcs> txtHEnquiryNo = new DmTextHelp(GWT.create(TxtHIEnquiryNo_PrpAcs.class), 7, false);

    private void iniTxtHEnquiryNo() {
        txtHEnquiryNo.getDmGrid().addTextColumn(txtHEnquiryNo.getDmGrid().getPropertyAccess().name(), "Enquiry No.", 1, "name");
        txtHEnquiryNo.getDmGrid().addTextColumn(txtHEnquiryNo.getDmGrid().getPropertyAccess().enquiryDate(), "Date", 80, "enquiryDate", DmDataType.DATE);
        txtHEnquiryNo.getDmGrid().addTextColumn(txtHEnquiryNo.getDmGrid().getPropertyAccess().enquiryRefNo(), "Ref. No.", 100, "refNo");
        txtHEnquiryNo.getDmGrid().initializeGrid(txtHEnquiryNo.getDmGrid().getPropertyAccess().fieldHashCode(), 350, 0, 1);

        txtHEnquiryNo.getHelper().addSort("enquiryDate", DmSortType.DESC);
        txtHEnquiryNo.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRCMApplicable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRCMApplicable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHRCMApplicable() {
        txtHRCMApplicable.getDmGrid().addTextColumn(txtHRCMApplicable.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHRCMApplicable.getDmGrid().initializeGrid(txtHRCMApplicable.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("QuotationDate")
        ValueProvider<Find_Mdl, String> quotationDate();

        @Editor.Path("VendorName")
        ValueProvider<Find_Mdl, String> vendorName();

        @Editor.Path("VendorQotNo")
        ValueProvider<Find_Mdl, String> vendorQotNo();

        @Editor.Path("VendorQotDate")
        ValueProvider<Find_Mdl, String> vendorQotDate();

        @Editor.Path("EnquiryNo")
        ValueProvider<Find_Mdl, String> enquiryNo();

        @Editor.Path("EnquiryDate")
        ValueProvider<Find_Mdl, String> enquiryDate();

        @Editor.Path("EnquiryRefNo")
        ValueProvider<Find_Mdl, String> enquiryRefNo();

        @Editor.Path("QuotationType")
        ValueProvider<Find_Mdl, String> quotationType();

        @Editor.Path("ValidUptoDate")
        ValueProvider<Find_Mdl, String> validUptoDate();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Quotation No.", 110, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().quotationDate(), "Date", 80, "replace(convert(varchar, v_Date, 106), ' ','-')", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().quotationType(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().validUptoDate(), "Valid Upto", 80, "validUpto", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().vendorName(), "Vendor Name", 1, "vendorName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().vendorQotNo(), "Vendor Ref. No.", 110, "vendorRefNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().vendorQotDate(), "Date", 80, "replace(convert(varchar, vendorRefDate, 106), ' ','-')", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().enquiryNo(), "Enquiry No.", 110, "enquiryNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().enquiryRefNo(), "Enq. Ref. No.", 110, "enquiryRefNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().enquiryDate(), "Date", 80, "replace(convert(varchar, enquiryRefDate, 106), ' ','-')", DmDataType.DATE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 5);

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmSortType.DESC, "v_No");
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public Quotation_Dsg() {
        ini();
        iniFind();
        iniTxtHQuotationType();
        iniTxtHSupplier();
        iniTxtHEnquiryNo();
        iniTxtHRCMApplicable();
        iniMenu();
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
        Anchor aBtn;
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("green");
        return userControl;
    }

    private TabLayoutPanel workingArea() {
        DmClient.setStyle(tabPanel, "100%", "100%", "left", null, DmPosition.CENTER, true);

        tabPanel.add(vl.add(tabGeneral()), "General");
        tabPanel.add(vl.add(tabTermsCondition()), "Terms & Conditions");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private TabLayoutPanel workingAreaBottomGrid() {
        DmClient.setStyle(tabPanel2, "100%", "147px", null, null, DmPosition.NONE, true);

        tabPanel2.add(vl.add(serviceDetailGrid), "Service");
        tabPanel2.add(vl.add(taxSummary), "Tax Summary");

        tabPanel2.selectTab(0);
        tabPanel2.ensureDebugId("cwTabPanel");

        return tabPanel2;
    }

    private FlowPanel tabTermsCondition() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98.8%", null, "left", null, DmPosition.NONE, true);
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
        DmClient.setStyle(flwTable, "99.8%", "451px", "left", null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98.7%", "68px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();

        DmClient.setStyle(flwTd, "49.5%", null, "left", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginLeft(8, Style.Unit.PX);
        flwTd.add(vl.add(topLeftArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "right", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginLeft(8, Style.Unit.PX);
        flwTd.add(vl.add(topRightArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.5%", "231px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(7, Style.Unit.PX);
        flwTr.add(vl.add(iniGrdItemDetail()));
        flwTable.add(flwTr);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTd.add(workingAreaBottomGrid());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "44%", null, "right", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginRight(10, Style.Unit.PX);
        flwTd.add(vl.add(overHeadArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel overHeadArea() {
        FlowPanel flwTable, flwTr, flwTd, flwTemp;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "75%", null, "right", null, DmPosition.NONE, true);

        //*************************<Left Side>****************************************
        flwTemp = new FlowPanel();
        DmClient.setStyle(flwTemp, "50%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("Goods Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        txtGoodsAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtGoodsAmount));
        flwTr.add(flwTd);
        flwTemp.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("Service Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        txtServiceAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtServiceAmount));
        flwTr.add(flwTd);
        flwTemp.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("Discount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        txtDiscount.getElement().setPropertyString("placeholder", " % ");
        txtDiscount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtDiscount));
        flwTr.add(flwTd);
        flwTemp.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("Taxable Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        txtTaxableAmt.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtTaxableAmt));
        flwTr.add(flwTd);
        flwTemp.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        flwTd.add(lblSgstUgst);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        txtSgstUgst.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtSgstUgst));
        flwTr.add(flwTd);
        flwTemp.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("CGST");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        txtCGST.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtCGST));
        flwTr.add(flwTd);
        flwTemp.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("IGST");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        txtIGST.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtIGST));
        flwTr.add(flwTd);
        flwTemp.add(flwTr);

        flwTable.add(flwTemp);
        //*************************<Right Side>****************************************
        flwTemp = new FlowPanel();
        DmClient.setStyle(flwTemp, "50%", null, "left", null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmPosition.NONE, false);
        txtTotalAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtTotalAmount));
        flwTr.add(flwTd);
        flwTemp.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Round off");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmPosition.NONE, false);
        txtRoundoff.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtRoundoff));
        flwTr.add(flwTd);
        flwTemp.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Net Amount");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmPosition.NONE, false);
        txtNetAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtNetAmount));
        flwTr.add(flwTd);
        flwTemp.add(flwTr);

        flwTable.add(flwTemp);
        return flwTable;

    }

    private FlowPanel topLeftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Quotation Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHQuotationType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Supplier Name");
        lbl.setTitle("Round Off - RO;");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSupplier));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        lbl = new DmLabel("RCM");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(28, Style.Unit.PX);
        flwTd.add(vl.add(txtHRCMApplicable));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Supplier Ref. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVendorRefNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(13, Style.Unit.PX);
        lbl = new DmLabel("Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVendorQNDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(12, Style.Unit.PX);
        lbl = new DmLabel("Valid Upto");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtValidUptoDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topRightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Quotation No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQuotationNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQuotationDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Enquiry No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEnquiryNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", "20px", "left", null, DmPosition.NONE, false);
        btnAddIndents.addStyleName("dm-button-grad");
        btnAddIndents.setWidth("100%");
        btnAddIndents.setHeight("20px");
        btnAddIndents.getElement().setAttribute("style", btnAddIndents.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnAddIndents.setTitle("Add Indent");
        flwTd.add(flwAddIndent);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", "20px", "right", null, DmPosition.NONE, false);
        DmClient.setStyle(flwImport, "100%", "20px", "left", null, DmPosition.CENTER, false);
//        DmClient.setStyle(flwSite, "49%", "20px", "right", null, DmPosition.CENTER, false);
        Anchor aBtn;
        InlineLabel label;
        label = new InlineLabel("Import");

        fileRuleType = new DmFileRuleType();
        fileRuleType.setFilters("csv");
        fileRuleType.setFileSizeLimit("102400");
        btnImport = new DmFileUploadButton(this, fileRuleType);

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-upload");
        aBtn.getElement().getStyle().setColor("#152de6");//#7d7d7d
        btnImport.setTitle("Import");
        btnImport.addStyleName("dm-top-control-btnStyle");
        btnImport.getElement().appendChild(aBtn.getElement());
        btnImport.getElement().appendChild(label.getElement());
        btnImport.setHeight("20px");
        btnImport.setWidth("100%");
        flwImport.add(btnImport);
        flwTd.add(flwImport);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Enquiry Ref. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEnquiryRefNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEnquiryDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridItemDetail">
    interface GridItemDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemDetail_Mdl> {

        @Editor.Path("Quantity")
        ValueProvider<GrdItemDetail_Mdl, String> quantity();

        @Editor.Path("DisplayUnit")
        ValueProvider<GrdItemDetail_Mdl, String> displayUnit();

        @Editor.Path("Rate")
        ValueProvider<GrdItemDetail_Mdl, String> rate();

        @Editor.Path("Amount")
        ValueProvider<GrdItemDetail_Mdl, String> amount();

        @Editor.Path("ColourName")
        ValueProvider<GrdItemDetail_Mdl, String> colorName();

        @Editor.Path("SizeName")
        ValueProvider<GrdItemDetail_Mdl, String> sizeName();

        @Editor.Path("Properties")
        ValueProvider<GrdItemDetail_Mdl, String> properties();

        @Editor.Path("DeliveryDate")
        ValueProvider<GrdItemDetail_Mdl, String> deliveryDate();

        @Editor.Path("Remark")
        ValueProvider<GrdItemDetail_Mdl, String> remark();

    }
    final DmGrid<GrdItemDetail_Mdl, GridItemDetail_PrpAcs> gridItemDetail = new DmGrid(GWT.create(GridItemDetail_PrpAcs.class));

    public Grid iniGrdItemDetail() {
        gridItemDetail.addRowNumberColumn();
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().name(), "Item Name", 250, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().properties(), "Properties", 250, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().quantity(), "Qty.", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().displayUnit(), "Unit", 55, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().rate(), "Rate", 85, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().amount(), "Amount", 105, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().deliveryDate(), "Delivery Date", 85, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().remark(), "Remark", 300, "");
        gridItemDetail.initializeGrid(gridItemDetail.getPropertyAccess().fieldHashCode(), 1260, 229);

        gridItemDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdItemDetail_Mdl>() {
            @Override
            public String getColStyle(GrdItemDetail_Mdl model, ValueProvider<? super GrdItemDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (model.isServiceRecord()) {
                    styleClass = "dm-GridRemoveRow";
                }

                if (!DmShared.xNull(model.getSharedKeyFieldName()).trim().equalsIgnoreCase("")
                        && DmShared.iNull(model.getSharedKeyFieldCode()).intValue() == 0) {
                    styleClass += "dm-GridCellDairyCancel";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdItemDetail_Mdl model, int rowIndex) {
                return "";
            }
        });
        return gridItemDetail.getGrid();
    }

    //</editor-fold>
    private Menu iniMenu() {
        menu.add(manageFileMenu);
        menu.add(mnuDownloadCsv);
        return menu;
    }
}
