package aarogya.pharmacy.quotation.client;

import aarogya.pharmacy.quotation.shared.GrdItemDetail_Mdl;
import aarogya.pharmacy.quotation.shared.TxtHEnquiry_Mdl;
import aarogya.pharmacy.quotation.shared.Find_Mdl;
import aarogya.pharmacy.quotation.shared.TxtHSupplier_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton;
import dataman.dmToolkit.dmFileUploadButton.shared.IFileUploadNotifier;
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

abstract class Quotation_Dsg extends IsWidgetExtended implements IFileUploadNotifier {

    private pmPharmacy pmPharmacy = new pmPharmacy();
    final String widgetName = pmPharmacy.QUOTATION.caption;
    final String moduleName = pmPharmacy.QUOTATION.moduleName;
    public final static String attachFileBlog = "Attach Files";
    public final static String deleteFileBlog = "Delete Files";
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmGUI gui = new DmGUI(topControl, 450);
    final OverHead overHead = new OverHead(topControl, new OverHeadDimension(370, 140, 0, 0, 0, false));

    final DmTextBox txtQuotationDate = new DmTextBox(11, true);
    final DmTextBox txtQuotationNo = new DmTextBox(20, false);
    final DmTextBox txtVendorRefNo = new DmTextBox(30, true);
    final DmTextBox txtVendorQNDate = new DmTextBox(11, true);
    final DmTextBox txtEnquiryDate = new DmTextBox(11, false);
    final DmTextBox txtEnquiryRefNo = new DmTextBox(30, false);
    final FlowPanel flwAddIndent = new FlowPanel();
    final Button btnAddIndents = new Button("Add Indent");
    final MenuItem mnuManageFile = new MenuItem("Manage File");
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
        txtHQuotationType.getDmGrid().initializeGrid(txtHQuotationType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
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

    private void iniTxtHVendor() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHSupplier.getHelper().addSort("name", DmSortType.ASC);
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
        txtHEnquiryNo.getDmGrid().initializeGrid(txtHEnquiryNo.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 0, 1);

        txtHEnquiryNo.getHelper().addSort("enquiryDate", DmSortType.DESC);
        txtHEnquiryNo.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

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

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Quotation No.", 110, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().quotationDate(), "Date", 80, "v_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().quotationType(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().vendorName(), "Vendor Name", 1, "vendorName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().vendorQotNo(), "Vendor Ref. No.", 110, "vendorRefNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().vendorQotDate(), "Ref. Date", 80, "vendorRefDate", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().enquiryNo(), "Enquiry No.", 110, "enquiryNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().enquiryRefNo(), "Enq. Ref. No.", 110, "enquiryRefNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().enquiryDate(), "Enq. Date", 80, "enquiryRefDate", DmDataType.DATE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 4);

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmSortType.DESC, "v_No");
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public Quotation_Dsg() {
        ini();
        iniFind();
        iniMenu();
        iniTxtHQuotationType();
        iniTxtHVendor();
        iniTxtHEnquiryNo();
    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", "left", null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.add(addUserControl());

        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
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
        DmClient.setStyle(flwTable, "98.8%", null, "left", null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
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

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdItemDetail()));
        flwTable.add(flwTr);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, null, null, "right", null, DmPosition.NONE, true);
        flwTd.add(vl.add(overHead));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

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
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSupplier));
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
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVendorRefNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVendorQNDate));
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
        btnAddIndents.setWidth("100%");
        btnAddIndents.setHeight("20px");
        btnAddIndents.getElement().setAttribute("style", btnAddIndents.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnAddIndents.setTitle("Add Indent");
        flwAddIndent.add(btnAddIndents);
        flwTd.add(flwAddIndent);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", "20px", "right", null, DmPosition.NONE, false);
        DmClient.setStyle(flwImport, "49%", "20px", "left", null, DmPosition.CENTER, false);
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

    private Menu iniMenu() {
        menu.add(mnuManageFile);
        menu.add(mnuDownloadCsv);
        return menu;
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
        
        @Editor.Path("Mrp")
        ValueProvider<GrdItemDetail_Mdl, String> mrp();

        @Editor.Path("DeliveryDate")
        ValueProvider<GrdItemDetail_Mdl, String> deliveryDate();

        @Editor.Path("Remark")
        ValueProvider<GrdItemDetail_Mdl, String> remark();

    }
    final DmGrid<GrdItemDetail_Mdl, GridItemDetail_PrpAcs> gridItemDetail = new DmGrid(GWT.create(GridItemDetail_PrpAcs.class));

    public Grid iniGrdItemDetail() {
        gridItemDetail.addRowNumberColumn();
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().name(), "Medicine Name", 250, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().quantity(), "Qty.", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().displayUnit(), "Unit", 50, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().rate(), "Rate", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().amount(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().mrp(), "MRP", 100, "mrp").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().deliveryDate(), "Delivery Date", 80, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().remark(), "Remark", 270, "");
        gridItemDetail.initializeGrid(gridItemDetail.getPropertyAccess().fieldHashCode(), 1263, 240, 2);
        
        gridItemDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdItemDetail_Mdl>() {
            @Override
            public String getColStyle(GrdItemDetail_Mdl model, ValueProvider<? super GrdItemDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (!DmShared.xNull(model.getSharedKeyFieldName()).trim().equalsIgnoreCase("") && 
                        DmShared.iNull(model.getSharedKeyFieldCode()).intValue()==0) {
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

}
