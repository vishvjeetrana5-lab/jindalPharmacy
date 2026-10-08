package aarogya.pharmacy.employeeMaster.client;

import aarogya.pharmacy.employeeMaster.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class EmployeeMaster_Dsg extends IsWidgetExtended {

    public final static String widgetName = "Employee Master";
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmSuggestBox txtName = new DmSuggestBox(50, 10, true, "Name", DmDataType.STRING);
    final DmTextBox txtEmpID = new DmTextBox(50, true);
    final DmTextBox txtJoinDate = new DmTextBox(20, true);
    final DmTextBox txtAddress1 = new DmTextBox(100, false);
    final DmTextBox txtAddress2 = new DmTextBox(100, false);
    final DmTextBoxNumeric txtPinCode = new DmTextBoxNumeric(0, false, 6, false);
    final DmTextBox txtPAddress1 = new DmTextBox(100, false);
    final DmTextBox txtPAddress2 = new DmTextBox(100, false);
    final DmTextBoxNumeric txtPPinCode = new DmTextBoxNumeric(0, false, 6, false);
    final DmTextBox txtMobileNo = new DmTextBox(50, false);
    final DmTextBox txtPhoneNo = new DmTextBox(50, false);
    final DmTextBox txtEmailID = new DmTextBox(50, false);
    final DmTextBox txtDOB = new DmTextBox(20, false);
    final DmTextBox txtPanNo = new DmTextBox(15, false);
    final DmTextBox txtAnvDate = new DmTextBox(20, false);
    final Button btnCopyAddress = new Button("Copy");

    Style stl;

    //<editor-fold defaultstate="collapsed" desc="txtHAccountGroup">
    interface TxtHAccountGroup_PrpAcs extends KeyField_T_PrpAcs<TxtHAccountGroup_Mdl> {

        @Editor.Path("Nature")
        ValueProvider<TxtHAccountGroup_Mdl, String> nature();

    }
    final DmTextHelp<TxtHAccountGroup_Mdl, TxtHAccountGroup_PrpAcs> txtHAccountGroup = new DmTextHelp(GWT.create(TxtHAccountGroup_PrpAcs.class), 5, true);

    private void iniAccountGroup() {
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().name(), "Account Group Name", 0, "GroupName");
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().nature(), "Nature", 100, "Nature");
        txtHAccountGroup.getDmGrid().initializeGrid(txtHAccountGroup.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHAccountGroup.getHelper().addSort("GroupName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCountry">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCountry = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniTxtHCountry() {
        txtHCountry.getDmGrid().addTextColumn(txtHCountry.getDmGrid().getPropertyAccess().name(), "Country Name", 0, "NAME");
        txtHCountry.getDmGrid().initializeGrid(txtHCountry.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHCountry.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCity">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCity = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniTxtHCity() {
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().name(), "City Name", 0, "CityName");
        txtHCity.getDmGrid().initializeGrid(txtHCity.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHCity.getHelper().addSort("CityName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPCountry">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPCountry = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniTxtHPCountry() {
        txtHPCountry.getDmGrid().addTextColumn(txtHPCountry.getDmGrid().getPropertyAccess().name(), "Country Name", 0, "NAME");
        txtHPCountry.getDmGrid().initializeGrid(txtHPCountry.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHPCountry.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPCity">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPCity = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniTxtHPCity() {
        txtHPCity.getDmGrid().addTextColumn(txtHPCity.getDmGrid().getPropertyAccess().name(), "City Name", 0, "CityName");
        txtHPCity.getDmGrid().initializeGrid(txtHPCity.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHPCity.getHelper().addSort("CityName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHDepartment.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDesignation">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDesignation = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniTxtHDesignation() {
        txtHDesignation.getDmGrid().addTextColumn(txtHDesignation.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHDesignation.getDmGrid().initializeGrid(txtHDesignation.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHDesignation.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGender">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGender = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHGender() {
        txtHGender.getDmGrid().addTextColumn(txtHGender.getDmGrid().getPropertyAccess().name(), "Gender", 0, "Gender");
        txtHGender.getDmGrid().initializeGrid(txtHGender.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHReligion">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReligion = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniTxtHReligion() {
        txtHReligion.getDmGrid().addTextColumn(txtHReligion.getDmGrid().getPropertyAccess().name(), "Religion", 0, "Religion");
        txtHReligion.getDmGrid().initializeGrid(txtHReligion.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMaritalStatus">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMaritalStatus = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHMaritalStatus() {
        txtHMaritalStatus.getDmGrid().addTextColumn(txtHMaritalStatus.getDmGrid().getPropertyAccess().name(), "Marital Status", 0, "Marital Status");
        txtHMaritalStatus.getDmGrid().initializeGrid(txtHMaritalStatus.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniActive() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("EmpID")
        ValueProvider<Find_Mdl, String> empID();

        @Editor.Path("JoinDate")
        ValueProvider<Find_Mdl, String> joinDate();

        @Editor.Path("AccountGroup")
        ValueProvider<Find_Mdl, String> accountGroup();

        @Editor.Path("Department")
        ValueProvider<Find_Mdl, String> department();

        @Editor.Path("Designation")
        ValueProvider<Find_Mdl, String> designation();

        @Editor.Path("Gender")
        ValueProvider<Find_Mdl, String> gender();

        @Editor.Path("MobileNo")
        ValueProvider<Find_Mdl, String> mobileNo();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 12);
//</editor-fold>

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().empID(), "Employee ID", 100, "subCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().joinDate(), "Join Date", 100, "doj");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().accountGroup(), "Account Group", 100, "groupName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().department(), "Department", 200, "department");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().designation(), "Designation", 100, "designation");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().gender(), "Gender", 100, "sex");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().mobileNo(), "Mobile No.", 100, "mobile");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);//183

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }

    public EmployeeMaster_Dsg() {
        fIni();
    }

    private void fIni() {
        Anchor aBtn;
        Button btnApprovedBy = new Button();

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");

        vrtInnerForm.add(vl.add(topControl));

        iniFind();
        iniAccountGroup();
        iniTxtHDepartment();
        iniTxtHDesignation();
        iniTxtHMaritalStatus();
        iniTxtHGender();
        iniTxtHReligion();
        iniTxtHCity();
        iniTxtHCountry();
        iniTxtHPCountry();
        iniTxtHPCity();
        iniActive();
        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        vrtInnerForm.add(flwWorkingArea);
        flwWorkingArea.add(workingArea());
        //Test Start

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);
        //Test End

        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "210px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "210px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "210px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "210px", "left", null, DmPosition.NONE, true);
        flwTd.add(rightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftArea() {

        FlowPanel flwTable;
        FlowPanel flwTr;
        FlowPanel flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "90%", "210px", null, null, DmPosition.CENTER, true);
        /*====================================< Left Section >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Employee ID");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEmpID));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Join Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtJoinDate));

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Account Group");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAccountGroup));

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobileNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Phone No.");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPhoneNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("EMail ID");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEmailID));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Designation");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDesignation));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHActive));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("DOB");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDOB));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Anv. Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();

        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAnvDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 7th Row >====================================*/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gender");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGender));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Religion");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);

        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHReligion));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 87th Row >====================================*/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Marital Status");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMaritalStatus));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PAN No.");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);

        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPanNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();

        DmClient.setStyle(flwTable, "90%", "210px", null, null, DmPosition.CENTER, true);
        /*====================================< Right Section >====================================*/

 /*====================================< Display >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "15px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        lbl = new DmLabel("Temporary Address");
        stl = lbl.getElement().getStyle();
        stl.setFontWeight(Style.FontWeight.BOLD);
        stl.setHeight(15, Style.Unit.PX);
        flwTr.add(lbl);
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress1));

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress2));

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("City");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCity));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 7th Row >====================================*/
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Country");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCountry));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PIN");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPinCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< Display Permanent Address Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Permanent Address");
        stl = lbl.getElement().getStyle();
        stl.setHeight(25, Style.Unit.PX);
        stl.setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnCopyAddress);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 8th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPAddress1));

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 9th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPAddress2));

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 10th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("City");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPCity));

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 11th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Country");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPCountry));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PIN");
        lbl.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPPinCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< ************* >====================================*/
        return flwTable;
    }

}
