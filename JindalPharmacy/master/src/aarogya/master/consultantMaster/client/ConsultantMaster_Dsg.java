package aarogya.master.consultantMaster.client;

import aarogya.master.consultantMaster.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.HeaderGroupConfig;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ConsultantMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().CONSULTANT_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String priceListBlog = "Price List";
    public final static String attachFileBlog = "Attach Files";
    public final static String deleteFileBlog = "Delete Files";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(20, Style.Unit.PX);
    final DmSuggestBox txtConsultant = new DmSuggestBox(50, 7, true, "name", DmDataType.STRING);
    final DmTextBox txtEmail = new DmTextBox(50, false);
    final DmSuggestBox txtShortName = new DmSuggestBox(25, 7, true, "shortName", DmDataType.STRING);
    final DmTextBox txtDisplayName = new DmTextBox(100, true);

    final DmTextBox txtDOA = new DmTextBox(11, false);
    final DmTextBox txtDOB = new DmTextBox(11, false);
    final DmTextBox txtDeviceCode = new DmTextBox(25, false);
    final DmTextBox txtDeviceBlock = new DmTextBox(25, false);
    final DmTextBox txtMCIReg = new DmTextBox(25, false);
    final DmTextBoxMobile txtMobile = new DmTextBoxMobile(false);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(250, false);
    final DmTextBox txtPAN = new DmTextBox(10, false);

    final DmTextBoxNumeric txtValidUpTo = new DmTextBoxNumeric(0, false, 4, false);
    final DmTextBoxNumeric txtNoOfVisit = new DmTextBoxNumeric(0, false, 43, false);

    final DmTextBox txtApplicableFrom = new DmTextBox(11, false);
    final DmRemarkTextBox txtRegionalDescription = new DmRemarkTextBox(250, false);
    final Button btnApplicable = new Button("Price List");
    final Button btnQueue = new Button("...");
    final Menu menu = new Menu();
    MenuItem manageSiteMenu, manageFileMenu;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    //<editor-fold defaultstate="collapsed" desc="TxtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 280, 0, 1);

        txtHDepartment.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHAccountName">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAccountName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHAccountName() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 280, 0, 1);

        txtHAccountName.getHelper().addSort("NAME", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHSpecialization">
    interface TxtHSpecialization_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {
    }
    final DmTextHelp<KeyFieldExtended, TxtHSpecialization_PrpAcs> txtHSpecialization = new DmTextHelp(GWT.create(TxtHSpecialization_PrpAcs.class), 10, true);

    private void iniTxtHSpecialization() {
        txtHSpecialization.getDmGrid().addTextColumn(txtHSpecialization.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSpecialization.getDmGrid().initializeGrid(txtHSpecialization.getDmGrid().getPropertyAccess().code(), 280, 0, 1);
        txtHSpecialization.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHNature() {
        txtHNature.getDmGrid().addTextColumn(txtHNature.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHNature.getDmGrid().initializeGrid(txtHNature.getDmGrid().getPropertyAccess().code(), 280, 0, 1);
        txtHNature.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHActive() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHIntial">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHInitial = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHInitial() {
        txtHInitial.getDmGrid().addTextColumn(txtHInitial.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHInitial.getDmGrid().initializeGrid(txtHInitial.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHInitial.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHQueueAlgo">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHQueueAlgo = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHQueueAlgo() {
        txtHQueueAlgo.getDmGrid().addTextColumn(txtHQueueAlgo.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHQueueAlgo.getDmGrid().initializeGrid(txtHQueueAlgo.getDmGrid().getPropertyAccess().code(), 175, 0, 1);
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

        @Editor.Path("ChamberNo")
        ValueProvider<Find_Mdl, String> chamberNo();

        @Editor.Path("AnnivDate")
        ValueProvider<Find_Mdl, String> annivDate();

        @Editor.Path("Dob")
        ValueProvider<Find_Mdl, String> dob();

        @Editor.Path("OpdValidUpto")
        ValueProvider<Find_Mdl, String> opdValidUpto();

        @Editor.Path("Email")
        ValueProvider<Find_Mdl, String> email();

        @Editor.Path("Remark")
        ValueProvider<Find_Mdl, String> remark();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 23, true);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Consultant", 150, "ConsName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "manualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().department(), "Department", 100, "department");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().specialisation(), "Specialization", 100, "specility");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().mciRegNo(), "MCI Reg. No.", 100, "mciReg");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().chamberNo(), "Chamber No.", 100, "chamberNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().opdValidUpto(), "OPD Valid Upto", 100, "OPDValidity").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().annivDate(), "Anniv. Date", 80, "doa", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().dob(), "DOB", 100, "dob", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().email(), "Email", 120, "eMail");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().mobile(), "Mobile", 80, "mobile");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().remark(), "Remark", 130, "remark");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().active(), "Active", 80, "isActive");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 0);

        dmFind.getHelper().addSort("ConsName", DmSortType.ASC);
        dmFind.getHelper().addSort("department", DmSortType.ASC);
        dmFind.getHelper().addSort("specility", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHLocation">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHLocation = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHLocation() {
        txtHLocation.getDmGrid().addTextColumn(txtHLocation.getDmGrid().getPropertyAccess().name(), "Name", 1, "roomNo");
        txtHLocation.getDmGrid().initializeGrid(txtHLocation.getDmGrid().getPropertyAccess().code(), 190, 0, 1);
        txtHLocation.getHelper().addSort("roomNo", DmSortType.ASC);
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

    //<editor-fold defaultstate="collapsed" desc="txtHConsultantNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHConsultantNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHConsultantNature() {
        txtHConsultantNature.getDmGrid().addTextColumn(txtHConsultantNature.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHConsultantNature.getDmGrid().initializeGrid(txtHConsultantNature.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
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

    public ConsultantMaster_Dsg() {
        ini();

    }

    private void ini() {
        FlowPanel flw;
        iniMenu();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        //<editor-fold defaultstate="collapsed" desc="ini's Declartation">
        iniFind();
        iniTxtHSpecialization();
        iniTxtHDepartment();
        iniTxtHAccountName();
        iniTxtHNature();
        iniTxtHActive();
        iniTxtHLocation();
        iniTxtHConsultantNature();
        iniTxtHInitial();
        iniTxtHQueueAlgo();
        iniTxtHTDSCategory();
        iniTxtHLinkDept();
        //</editor-fold>

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.add(iniUserControl());

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "516px", null, null, DmPosition.CENTER, true);

        // Control Detail
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "250px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTr.add(gridArea());
        flwTable.add(flwTr);

        // Grid Tab
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "272px", "left", null, DmPosition.NONE, true);
        flwTr.add(gridTab());

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "70%", "235px", "left", null, DmPosition.NONE, true);

        //Consultant Name & Short Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consultant");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHInitial));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsultant));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Department & Specialization
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
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Nature & Mobile
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
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Anniv. Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDOA));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Email
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
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("DOB");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDOB));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Date Of Anniversary
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

        //OPD Valid UpTo
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("OPD Valid UpTo");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtValidUpTo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("No. Of Visit");
        lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNoOfVisit));
        flwTr.add(flwTd);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        //MCI Reg
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("MCI Reg No.");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMCIReg));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consultation Room");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLocation));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nature");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHConsultantNature));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Queue Algo");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);

        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        btnQueue.addStyleName("dm-button-grad");
        DmClient.setStyle(btnQueue, "20px", "20px", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtHQueueAlgo));
        hrtPnl.add(btnQueue);
        hrtPnl.setCellWidth(btnQueue, "25px");
        hrtPnl.setCellWidth(txtHQueueAlgo, "100%");
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtHActive);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Device Code");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDeviceCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Device Area");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDeviceBlock));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PAN No.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPAN));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

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

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Applicable From");
        lbl.getElement().getStyle().setMarginLeft(16, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtApplicableFrom));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Regional Description");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtRegionalDescription.setHeight("20px");
        txtRegionalDescription.addStyleName(DmEnum.RegionalLanguage.getValueFrom(DmClient.getRegionalLanguage()).getCssStyleName());
        flwTd.add(vl.add(txtRegionalDescription));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Link Department");
        lbl.getElement().getStyle().setMarginLeft(16, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLinkDept));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel gridArea() {
        FlowPanel flwTable;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "30%", "235px", "left", null, DmPosition.NONE, true);

        flwTable.add(vl.add(iniGrdOPDConsultant()));
        return flwTable;

    }

    private TabLayoutPanel gridTab() {
        DmClient.setStyle(tabPanel, "100%", "272px", null, null, DmPosition.NONE, true);
        tabPanel.add(iniTimeDetail(), "Time Wise Detail");
        tabPanel.add(iniClinicDetail(), "Clinic Detail");
        tabPanel.add(iniServices(), "Operation Detail");
        tabPanel.add(iniBankAccountDetail(), "Bank Account Detail");
        tabPanel.ensureDebugId("cwTabPanel");
        return tabPanel;
    }

    private DmUserControl iniUserControl() {
        btnApplicable.addStyleName("dm-button-grad");
        btnApplicable.setWidth("100px");
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(50);
        userControl.addControlBlog(priceListBlog, btnApplicable);
        userControl.addGap(50);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        return userControl;

    }

    private Menu iniMenu() {
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
        grdClinicDetail.addTextColumn(grdClinicDetail.getPropertyAccess().address(), "Address1", 220, null);
        grdClinicDetail.addTextColumn(grdClinicDetail.getPropertyAccess().address1(), "Address2", 220, null);
        grdClinicDetail.initializeGrid(grdClinicDetail.getPropertyAccess().fieldHashCode(), 1260, 243);
        return grdClinicDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Timing Detail Grid">
    interface GrdTiming_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdTiming_Mdl> {

        @Editor.Path("FromTime")
        ValueProvider<GrdTiming_Mdl, String> from();

        @Editor.Path("ToTime")
        ValueProvider<GrdTiming_Mdl, String> toTime();

        @Editor.Path("Opd")
        ValueProvider<GrdTiming_Mdl, String> opd();

        @Editor.Path("Emergency")
        ValueProvider<GrdTiming_Mdl, String> emergency();

        @Editor.Path("Appointment")
        ValueProvider<GrdTiming_Mdl, String> appointment();

        @Editor.Path("AppointmentAdvance")
        ValueProvider<GrdTiming_Mdl, String> appointmentAdvance();

        @Editor.Path("Patient")
        ValueProvider<GrdTiming_Mdl, String> patient();

        @Editor.Path("GraceMin")
        ValueProvider<GrdTiming_Mdl, String> graceMin();

        @Editor.Path("ReVisit")
        ValueProvider<GrdTiming_Mdl, String> reVisit();

        @Editor.Path("OnlineCharge")
        ValueProvider<GrdTiming_Mdl, String> onlineCharge();

        @Editor.Path("OnlinePatient")
        ValueProvider<GrdTiming_Mdl, String> onllinePatient();

        @Editor.Path("PayClinic")
        ValueProvider<GrdTiming_Mdl, String> payClinic();
    }
    final DmGrid<GrdTiming_Mdl, GrdTiming_PrpAcs> grdTimingDetail = new DmGrid(GWT.create(GrdTiming_PrpAcs.class));

    private Grid iniTimeDetail() {
        grdTimingDetail.addRowNumberColumn();
        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().name(), "Day", 55, null);
        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().from(), "From Time", 80, null);
        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().toTime(), "To Time", 80, null);
        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().graceMin(), "Grace Mins", 100, null);
        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().opd(), "OPD", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().emergency(), "Emergency", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().appointment(), "Appointment", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().appointmentAdvance(), "Appointment Adv.", 150, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().reVisit(), "Re-Visit", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().onlineCharge(), "Online", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().payClinic(), "Pay Clinic", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().patient(), "No. of Pat.", 80, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdTimingDetail.addTextColumn(grdTimingDetail.getPropertyAccess().onllinePatient(), "Online Pat.", 80, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdTimingDetail.addBeforeInitializeHandler(new DmGrid.BeforeInitialize() {
            @Override
            public void beforeInitialize(ColumnModel columnModel) {
                columnModel.addHeaderGroup(0, 5, new HeaderGroupConfig("Fees", 1, 7));
            }
        });

        grdTimingDetail.initializeGrid(grdTimingDetail.getPropertyAccess().fieldHashCode(), 1260, 243, 2);

        return grdTimingDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdBankDetail">
    interface GrdBank_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdBankDetail_Mdl> {

        @Editor.Path("AccountNo")
        ValueProvider<GrdBankDetail_Mdl, String> accountNo();

        @Editor.Path("BankName")
        ValueProvider<GrdBankDetail_Mdl, String> bankName();

        @Editor.Path("IfscCode")
        ValueProvider<GrdBankDetail_Mdl, String> ifscCode();

        @Editor.Path("BranchAddress")
        ValueProvider<GrdBankDetail_Mdl, String> branchAddress();

        @Editor.Path("Branch")
        ValueProvider<GrdBankDetail_Mdl, String> branch();

        @Editor.Path("SwiftNo")
        ValueProvider<GrdBankDetail_Mdl, String> swiftNo();

        @Editor.Path("IsDefault")
        ValueProvider<GrdBankDetail_Mdl, String> isDefault();

    }
    final DmGrid<GrdBankDetail_Mdl, GrdBank_PrpAcs> grdBankDetail = new DmGrid(GWT.create(GrdBank_PrpAcs.class));

    private Grid iniGridBank() {
        ColumnConfig<GrdBankDetail_Mdl, String> selectDefaultYes;
        grdBankDetail.addRowNumberColumn();
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().accountNo(), "Account No.", 150, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().bankName(), "Bank", 180, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().branch(), "Branch", 160, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().branchAddress(), "Branch Address", 180, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().ifscCode(), "IFSC Code", 100, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().swiftNo(), "Swift No.", 100, "");
        selectDefaultYes = grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().isDefault(), "Default", 50, "");
        grdBankDetail.initializeGrid(grdBankDetail.getPropertyAccess().fieldHashCode(), 950, 200);
        selectDefaultYes.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectDefaultYes.setCellPadding(false);
        selectDefaultYes.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdBankDetail.getGrid().getStore().get(context.getIndex()).getIsDefault()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        return grdBankDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Operation Detail Grid">
    interface GrdServiceDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdService_Mdl> {

        @Editor.Path("Amount")
        ValueProvider<GrdService_Mdl, String> rate();
    }

    final DmGrid<GrdService_Mdl, GrdServiceDetail_PrpAcs> grdServiceDetail = new DmGrid(GWT.create(GrdServiceDetail_PrpAcs.class));

    private FlowPanel iniServices() {
        FlowPanel flwTable, flwTr;
        grdServiceDetail.addRowNumberColumn();
        grdServiceDetail.addTextColumn(grdServiceDetail.getPropertyAccess().name(), "Operation Name", 540, null);

        grdServiceDetail.addTextColumn(grdServiceDetail.getPropertyAccess().rate(), "Rate", 200, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdServiceDetail.initializeGrid(grdServiceDetail.getPropertyAccess().fieldHashCode(), 767, 238);

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "60%", "250px", null, null, DmPosition.CENTER, true);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "260px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(grdServiceDetail.getGrid()));
        flwTable.add(flwTr);
        return flwTable;
    }
    //</editor-fold>

    interface GrdSiteDetail_PrpAcs extends KeyField_T_PrpAcs<GrdSiteDetail_Mdl> {

        @Editor.Path("SharedKeyFieldCode")
        ValueProvider<GrdSiteDetail_Mdl, String> siteCode();

        @Editor.Path("SiteName")
        ValueProvider<GrdSiteDetail_Mdl, String> siteName();

    }

    final DmGrid<GrdSiteDetail_Mdl, GrdSiteDetail_PrpAcs> grdSiteDetail = new DmGrid(GWT.create(GrdSiteDetail_PrpAcs.class));

    private Grid iniGrdOPDConsultant() {
        grdSiteDetail.addRowNumberColumn();
        ColumnConfig<GrdSiteDetail_Mdl, String> colAllowSites;
        ColumnConfig<GrdSiteDetail_Mdl, String> colOPDConsultant;

        grdSiteDetail.addTextColumn(grdSiteDetail.getPropertyAccess().siteName(), "Site Name", 120, "");

        colAllowSites = grdSiteDetail.addTextColumn(grdSiteDetail.getPropertyAccess().siteCode(), "Allow For Site", 80, "");
        colOPDConsultant = grdSiteDetail.addTextColumn(grdSiteDetail.getPropertyAccess().siteCode(), "IS OPD Consultant", 80, "");
        grdSiteDetail.initializeGrid(grdSiteDetail.getPropertyAccess().code(), 350, 210, 2);

        colAllowSites.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        colOPDConsultant.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        colAllowSites.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {

                if (grdSiteDetail.getGrid().getStore().get(context.getIndex()).isAllowForSite()) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        colOPDConsultant.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                if (grdSiteDetail.getGrid().getStore().get(context.getIndex()).isOPDConsultant()) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        return grdSiteDetail.getGrid();
    }

    private FlowPanel iniBankAccountDetail() {
        FlowPanel flwTable, flwTr;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "75%", "251px", null, null, DmPosition.CENTER, true);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "260px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGridBank()));
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
        return flwTable;
    }

}
