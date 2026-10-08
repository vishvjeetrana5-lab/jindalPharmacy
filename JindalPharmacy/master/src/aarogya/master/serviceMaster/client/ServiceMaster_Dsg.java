package aarogya.master.serviceMaster.client;

import aarogya.master.serviceMaster.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ServiceMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().SERVICE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String priceListBlog = "Price List";
    public final static String packageDetailBlog = "Package Detail";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmToolContainer toolContainer = new DmToolContainer();
    final Button btnApplicable = new Button("Price List");
    final Button btnPackagedetail = new Button("Package Detail");
    final DmSuggestBox txtName = new DmSuggestBox(160, 10, true, "Name", DmDataType.STRING);
    final DmTextBox txtShortName = new DmTextBox(10, true);
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtPrntSerial = new DmTextBoxNumeric(0, true, 4, false);
    final Menu menu = new Menu();
    final MenuItem manageSiteMenu = new MenuItem("Manage Site");
    final MenuItem mnuExportCard = new MenuItem("Export Service");

    //<editor-fold defaultstate="collapsed" desc="txtHCategory">
    interface TxtHCategory_PrpAcs extends KeyField_T_PrpAcs<TxtHCategory_Mdl> {

        @Editor.Path("Its_Name")
        ValueProvider<TxtHCategory_Mdl, String> its_Name();

        @Editor.Path("ServiceAcName")
        ValueProvider<TxtHCategory_Mdl, String> serviceAcName();
    }
    final DmTextHelp<TxtHCategory_Mdl, TxtHCategory_PrpAcs> txtHCategory = new DmTextHelp(GWT.create(TxtHCategory_PrpAcs.class), 10, true);

    private void iniTxtHCategory() {
        txtHCategory.getDmGrid().addTextColumn(txtHCategory.getDmGrid().getPropertyAccess().name(), "Name", 1, "categoryName");
        txtHCategory.getDmGrid().addTextColumn(txtHCategory.getDmGrid().getPropertyAccess().its_Name(), "ITS On Sale", 100, "itsSaleName");
        txtHCategory.getDmGrid().addTextColumn(txtHCategory.getDmGrid().getPropertyAccess().serviceAcName(), "Purchase / Service A/c", 180, "serviceAcName");
        txtHCategory.getDmGrid().initializeGrid(txtHCategory.getDmGrid().getPropertyAccess().code(), 450, 0, 1);

        txtHCategory.getHelper().addSort("categoryName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHGroup() {
        txtHGroup.getDmGrid().addTextColumn(txtHGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHGroup.getDmGrid().initializeGrid(txtHGroup.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHGroup.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHActive() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtNightShift">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNightShift = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHNightShift() {
        txtHNightShift.getDmGrid().addTextColumn(txtHNightShift.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHNightShift.getDmGrid().initializeGrid(txtHNightShift.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHType() {
        txtHType.getDmGrid().addTextColumn(txtHType.getDmGrid().getPropertyAccess().name(), "Type", 0, "isRateEditable");
        txtHType.getDmGrid().initializeGrid(txtHType.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRateEditable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRateEditable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHRateEditable() {
        txtHRateEditable.getDmGrid().addTextColumn(txtHRateEditable.getDmGrid().getPropertyAccess().name(), "Type", 0, "isRateEditable");
        txtHRateEditable.getDmGrid().initializeGrid(txtHRateEditable.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDiscount">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDiscount = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHDiscount() {
        txtHDiscount.getDmGrid().addTextColumn(txtHDiscount.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHDiscount.getDmGrid().initializeGrid(txtHDiscount.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHServiceRoutine">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceRoutine = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHDayWise() {
        txtHServiceRoutine.getDmGrid().addTextColumn(txtHServiceRoutine.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHServiceRoutine.getDmGrid().initializeGrid(txtHServiceRoutine.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Find">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

        @Editor.Path("CategoryName")
        ValueProvider<Find_Mdl, String> categoryName();

        @Editor.Path("PrintSNo")
        ValueProvider<Find_Mdl, String> printSNo();

        @Editor.Path("IsDiscountAllow")
        ValueProvider<Find_Mdl, String> isDiscountAllow();

        @Editor.Path("GroupName")
        ValueProvider<Find_Mdl, String> groupName();

        @Editor.Path("ServiceRoutine")
        ValueProvider<Find_Mdl, String> serviceRoutine();

        @Editor.Path("IsNightShift")
        ValueProvider<Find_Mdl, String> isNightShift();

        @Editor.Path("NightShiftAmount")
        ValueProvider<Find_Mdl, String> nightShiftAmount();

        @Editor.Path("IsRateEditable")
        ValueProvider<Find_Mdl, String> isRateEditable();

        @Editor.Path("IsActive")
        ValueProvider<Find_Mdl, String> isActive();

        @Editor.Path("IsType")
        ValueProvider<Find_Mdl, String> isType();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 1, "NAME");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 75, "shortName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().categoryName(), "Category", 160, "categoryName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().groupName(), "Group Name", 100, "groupName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().printSNo(), "Print S.No.", 70, "printSNo", DmDataType.INTEGER);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isDiscountAllow(), "Discount", 60, "isDiscountAllow");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().serviceRoutine(), "Service Routine", 50, "serviceRoutine");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isNightShift(), "Night Shift", 62, "isNightShift");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().nightShiftAmount(), "Night Shift Amt.", 90, "nightShiftAmount");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isType(), "Type", 70, "nightShiftType");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isRateEditable(), "Rate Editable", 75, "isRateEditable");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isActive(), "Active", 50, "isActive");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 1);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHLinkDept">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHLinkDept = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHLinkDept() {
        txtHLinkDept.getDmGrid().addTextColumn(txtHLinkDept.getDmGrid().getPropertyAccess().name(), "Department", 1, "Name");
        txtHLinkDept.getDmGrid().initializeGrid(txtHLinkDept.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHLinkDept.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtServiceType">
    interface KeyFieldService_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {
    }

    final DmTextHelp<KeyFieldExtended, KeyFieldService_PrpAcs> txtHServiceType = new DmTextHelp(GWT.create(KeyFieldService_PrpAcs.class), 5, true, true);

    private void iniTxtHServiceType() {
        txtHServiceType.getDmGrid().addTextColumn(txtHServiceType.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHServiceType.getDmGrid().initializeGrid(txtHServiceType.getDmGrid().getPropertyAccess().fieldHashCode(), 200, 0, 1);
        txtHServiceType.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public ServiceMaster_Dsg() {
        ini();
        iniFind();
        iniTxtHRateEditable();
        iniTxtHCategory();
        iniTxtHActive();
        iniTxtHDayWise();
        iniTxtHDiscount();
        iniTxtHNightShift();
        iniTxtHGroup();
        iniTxtHLinkDept();
        createMenu();
        iniTxtHType();
        iniTxtHServiceType();
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

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(50);
        btnApplicable.addStyleName("dm-button-grad");
        btnApplicable.setWidth("100px");
        userControl.addControlBlog(priceListBlog, btnApplicable);
        userControl.addGap(1);
        btnPackagedetail.setWidth("110px");
        btnPackagedetail.addStyleName("dm-button-grad");
        btnPackagedetail.setTitle("Package Rate List");
        userControl.addControlBlog(packageDetailBlog, btnPackagedetail);
        userControl.addGap(50);
        btnPackagedetail.setVisible(false);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "70%", "476px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Type ");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceType));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Print S.No.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPrntSerial));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Category");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCategory));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Routine");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceRoutine));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Group Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGroup));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Night Shift Rate");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHNightShift));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate Editable");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRateEditable));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtHActive);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Type");
        lbl.getElement().getStyle().setMarginLeft(50, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(vl.add(txtHType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, true);
        lbl = new DmLabel("Discount");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtHDiscount);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Link Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLinkDept));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private Menu createMenu() {
        menu.add(manageSiteMenu);
        menu.add(mnuExportCard);
        return menu;
    }

}
