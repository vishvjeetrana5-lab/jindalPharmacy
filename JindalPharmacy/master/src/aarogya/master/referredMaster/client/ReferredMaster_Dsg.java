package aarogya.master.referredMaster.client;

import aarogya.master.referredMaster.shared.Find_Mdl;
import aarogya.master.referredMaster.shared.GrdClinicDetail_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ReferredMaster_Dsg extends IsWidgetExtended {

    public final static String widgetName = new pmMaster().REFERRED_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String attachFileBlog = "Attach Files";
    public final static String deleteFileBlog = "Delete Files";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(20, Style.Unit.PX);
    final DmSuggestBox txtReferred = new DmSuggestBox(50, 7, true, "name", DmDataType.STRING);
    final DmTextBox txtEmail = new DmTextBox(50, false);
    final DmSuggestBox txtShortName = new DmSuggestBox(25, 7, true, "shortName", DmDataType.STRING);
    final DmTextBox txtDisplayName = new DmTextBox(100, true);
    final DmTextBox txtDOA = new DmTextBox(11, false);
    final DmTextBox txtDOB = new DmTextBox(11, false);
    final DmTextBox txtMCIReg = new DmTextBox(25, false);
    final DmTextBoxMobile txtMobile = new DmTextBoxMobile(false);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(250, false);

    final Menu menu = new Menu();
    MenuItem manageSiteMenu, manageFileMenu;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    //<editor-fold defaultstate="collapsed" desc="TxtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 280, 0, 1);

        txtHDepartment.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHSpecialization">
    interface TxtHSpecialization_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {
    }
    final DmTextHelp<KeyFieldExtended, TxtHSpecialization_PrpAcs> txtHSpecialization = new DmTextHelp(GWT.create(TxtHSpecialization_PrpAcs.class), 10, false);

    private void iniTxtHSpecialization() {
        txtHSpecialization.getDmGrid().addTextColumn(txtHSpecialization.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSpecialization.getDmGrid().initializeGrid(txtHSpecialization.getDmGrid().getPropertyAccess().code(), 280, 0, 1);
        txtHSpecialization.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, true, true);

    private void iniTxtHActive() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHPrefixReferred">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPrefixReferred = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHPrefixReferred() {
        txtHPrefixReferred.getDmGrid().addTextColumn(txtHPrefixReferred.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPrefixReferred.getDmGrid().initializeGrid(txtHPrefixReferred.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHPrefixReferred.getHelper().addSort("Name", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTDSCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTDSCategory = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHTDSCategory() {
        txtHTDSCategory.getDmGrid().addTextColumn(txtHTDSCategory.getDmGrid().getPropertyAccess().name(), "TDS Category", 0, "NAME");
        txtHTDSCategory.getDmGrid().initializeGrid(txtHTDSCategory.getDmGrid().getPropertyAccess().code(), 280, 0, 1);
        txtHTDSCategory.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

        @Editor.Path("Department")
        ValueProvider<Find_Mdl, String> department();

        @Editor.Path("Specialization")
        ValueProvider<Find_Mdl, String> specialisation();

        @Editor.Path("Mobile")
        ValueProvider<Find_Mdl, String> mobile();

        @Editor.Path("IsActive")
        ValueProvider<Find_Mdl, String> active();

        @Editor.Path("MciRegNo")
        ValueProvider<Find_Mdl, String> mciRegNo();

        @Editor.Path("AnnivDate")
        ValueProvider<Find_Mdl, String> annivDate();

        @Editor.Path("Dob")
        ValueProvider<Find_Mdl, String> dob();

        @Editor.Path("Email")
        ValueProvider<Find_Mdl, String> email();

        @Editor.Path("Remark")
        ValueProvider<Find_Mdl, String> remark();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 23, true);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Consultant", 150, "ConsName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "manualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().department(), "Department", 170, "department");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().specialisation(), "Specialization", 170, "specility");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().mciRegNo(), "MCI Reg. No.", 100, "mciReg");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().annivDate(), "Anniv. Date", 80, "doa", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().dob(), "DOB", 80, "dob", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().email(), "Email", 120, "eMail");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().mobile(), "Mobile", 80, "mobile");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().remark(), "Remark", 130, "remark");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().active(), "Active", 50, "isActive");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);

        dmFind.getHelper().addSort("ConsName", DmSortType.ASC);
        dmFind.getHelper().addSort("department", DmSortType.ASC);
        dmFind.getHelper().addSort("specility", DmSortType.ASC);
    }
    //</editor-fold>

    public ReferredMaster_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        createMenu();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        //<editor-fold defaultstate="collapsed" desc="ini's Declartation">
        iniFind();
        iniTxtHSpecialization();
        iniTxtHDepartment();
        iniTxtHPrefixReferred();
        iniTxtHTDSCategory();

        iniTxtHActive();
        //</editor-fold>

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.add(createBottom());

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "489px", null, null, DmPosition.CENTER, true);

        // ============================= Header Detail ==============================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "154px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);
        //=============================== END ===========================================

        // ============================= Grid Tab =====================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "335px", "left", null, DmPosition.NONE, true);
        flwTr.add(gridTab());
        flwTable.add(flwTr);
        //=============================== END ===========================================

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "80%", "155px", null, null, DmPosition.CENTER, true);

        //===================== 1st Row (Referred Consultant & Short Name) ====================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Referred");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPrefixReferred));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReferred));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //=============================== END ===========================================

        //===================== 2nd Row (Display Name & Mobile No.) =====================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Display Name");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDisplayName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //=============================== END ===========================================

        //===================== 3rd Row (Department & Anniv. Date) ======================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Anniv. Date");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDOA));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //=============================== END ===========================================

        //===================== 4th Row (Specialization & DOB) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Specialization");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSpecialization));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("DOB");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDOB));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //=============================== END ===========================================

        //===================== 5th Row (Email & Chamber No) ============================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Email");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEmail));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("MCI Reg No.");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        txtMCIReg.setAlignment(ValueBoxBase.TextAlignment.LEFT);
        flwTd.add(vl.add(txtMCIReg));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //=============================== END ===========================================
        //===================== 5th Row (Email & Chamber No) ============================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("TDS Category");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTDSCategory));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //=============================== END ===========================================
        //===================== 6th Row (MCI Reg No. & Active) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        //MCI Reg
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");

        lbl.getElement().getStyle().setMarginLeft(21, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtHActive);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //=============================== END ===========================================

        return flwTable;
    }

    private TabLayoutPanel gridTab() {
        DmClient.setStyle(tabPanel, "100%", "350px", null, null, DmPosition.NONE, true);
        tabPanel.add(vl.add(iniClinicDetail()), "Clinic Detail");
        tabPanel.ensureDebugId("cwTabPanel");
        return tabPanel;
    }

    private DmUserControl createBottom() {

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        return userControl;

    }

    private Menu createMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        manageSiteMenu = new MenuItem("Manage Site", selectionHandler);
        menu.add(manageSiteMenu);

        manageFileMenu = new MenuItem("Manage File", selectionHandler);
        menu.add(manageFileMenu);

        return menu;
    }

    //<editor-fold defaultstate="collapsed" desc="Clinic Detail Grid">
    interface GrdClinicDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdClinicDetail_Mdl> {

        @Editor.Path("Timing")
        ValueProvider<GrdClinicDetail_Mdl, String> timing();

        @Editor.Path("Phone")
        ValueProvider<GrdClinicDetail_Mdl, String> phone();

        @Editor.Path("CityText")
        ValueProvider<GrdClinicDetail_Mdl, String> city();

        @Editor.Path("Address")
        ValueProvider<GrdClinicDetail_Mdl, String> address();

        @Editor.Path("Address1")
        ValueProvider<GrdClinicDetail_Mdl, String> address1();

    }

    final DmGrid<GrdClinicDetail_Mdl, GrdClinicDetail_PrpAcs> grdClinicDetail = new DmGrid(GWT.create(GrdClinicDetail_PrpAcs.class));

    private Grid iniClinicDetail() {
        grdClinicDetail.addRowNumberColumn();
        grdClinicDetail.addTextColumn(grdClinicDetail.getPropertyAccess().name(), "Site", 170, null);
        grdClinicDetail.addTextColumn(grdClinicDetail.getPropertyAccess().timing(), "Timing", 100, null);
        grdClinicDetail.addTextColumn(grdClinicDetail.getPropertyAccess().phone(), "Phone", 100, null);
        grdClinicDetail.addTextColumn(grdClinicDetail.getPropertyAccess().city(), "City", 90, null);
        grdClinicDetail.addTextColumn(grdClinicDetail.getPropertyAccess().address(), "Address1", 300, null);
        grdClinicDetail.addTextColumn(grdClinicDetail.getPropertyAccess().address1(), "Address2", 300, null);
        grdClinicDetail.initializeGrid(grdClinicDetail.getPropertyAccess().fieldHashCode(), 1260, 350, 5);
        return grdClinicDetail.getGrid();
    }
    //</editor-fold>

}
