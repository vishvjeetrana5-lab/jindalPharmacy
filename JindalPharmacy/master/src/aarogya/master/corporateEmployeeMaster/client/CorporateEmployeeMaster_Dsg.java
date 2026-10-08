package aarogya.master.corporateEmployeeMaster.client;

import aarogya.master.corporateEmployeeMaster.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract public class CorporateEmployeeMaster_Dsg extends IsWidgetExtended {

    public final static String widgetName = new pmMaster().CORPORATE_EMPLOYEE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmSuggestBox txtEmpName = new DmSuggestBox(50, 10, true, "Name", DmDataType.STRING);
    final DmTextBox txtEmpCode = new DmTextBox(20, true);
    final DmTextBox txtPolicyNo = new DmTextBox(20, false);
    final DmTextBox txtCardHolderName = new DmTextBox(45, true);
    final DmTextBox txtRank = new DmTextBox(20, false);
    final DmTextBox txtAddress2 = new DmTextBox(50, false);
    final DmTextBox txtAddress1 = new DmTextBox(50, false);
    final DmTextBox txtState = new DmTextBox(50, false);
    final DmTextBoxNumeric txtPinCode = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, false, 6, false);
    final DmTextBoxMobile txtMobile = new DmTextBoxMobile(false);
    final DmTextBox txtDob = new DmTextBox(50, false);
    final DmTextBoxNumeric txtAgeYear = new DmTextBoxNumeric(0, true, 3, true);
    final DmTextBoxNumeric txtAgeMonth = new DmTextBoxNumeric(0, true, 2, false);
    final DmTextBoxNumeric txtAgeDay = new DmTextBoxNumeric(0, true, 2, false);

    //<editor-fold defaultstate="collapsed" desc="txtHCorporate">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCorporate = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHCorporate() {

        txtHCorporate.getDmGrid().addTextColumn(txtHCorporate.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHCorporate.getDmGrid().initializeGrid(txtHCorporate.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHCorporate.getHelper().addSort("NAME", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRelationship">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRelationship = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, true);

    private void iniTxtHRelationship() {
        txtHRelationship.getDmGrid().addTextColumn(txtHRelationship.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHRelationship.getDmGrid().initializeGrid(txtHRelationship.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHRelationship.getHelper().addSort("Name", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHCity">
    interface txtHCity_PrpAcs extends KeyField_T_PrpAcs<TxtHCity_Mdl> {

        @Editor.Path("StateName")
        ValueProvider<TxtHCity_Mdl, String> stateName();

        @Editor.Path("CountryName")
        ValueProvider<TxtHCity_Mdl, String> countryName();
    }
    final DmTextHelp<TxtHCity_Mdl, txtHCity_PrpAcs> txtHCity = new DmTextHelp(GWT.create(txtHCity_PrpAcs.class), 10);

    private void iniCity() {
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().name(), "City", 0, "CityName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().stateName(), "State", 125, "stateName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "countryName");
        txtHCity.getDmGrid().initializeGrid(txtHCity.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHCity.getHelper().addSort("CityName", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHZone">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHZone = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniZone() {
        txtHZone.getDmGrid().addTextColumn(txtHZone.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHZone.getDmGrid().initializeGrid(txtHZone.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHZone.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<Find_Mdl, String> manualCode();

        @Editor.Path("CorporateName")
        ValueProvider<Find_Mdl, String> corporateName();

        @Editor.Path("PolicyNo")
        ValueProvider<Find_Mdl, String> policyNo();

        @Editor.Path("Relationship")
        ValueProvider<Find_Mdl, String> relationship();

        @Editor.Path("Rank")
        ValueProvider<Find_Mdl, String> rank();

        @Editor.Path("CardHolderName")
        ValueProvider<Find_Mdl, String> cardHolderName();

    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Employee Name", 0, "empName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().manualCode(), "Code", 150, "manualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().corporateName(), "Corporate", 250, "corporateName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().policyNo(), "Policy No.", 150, "policyName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().relationship(), "Relationship", 150, "relationship");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().rank(), "Rank", 100, "rank");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cardHolderName(), "Card Holder Name", 200, "cardHolderName");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 600, 1);

        dmFind.getHelper().addSort("empName", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGender">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGender = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHGender() {
        txtHGender.getDmGrid().addTextColumn(txtHGender.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHGender.getDmGrid().initializeGrid(txtHGender.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHGender.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHBloodGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBloodGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 8);

    private void iniTxtHBloodGroup() {
        txtHBloodGroup.getDmGrid().addTextColumn(txtHBloodGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHBloodGroup.getDmGrid().initializeGrid(txtHBloodGroup.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
        txtHBloodGroup.getHelper().addSort("Name", DmSortType.ASC);

    }
    //</editor-fold>

    public CorporateEmployeeMaster_Dsg() {
        ini();

    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        iniFind();
        iniTxtHCorporate();
        iniTxtHRelationship();
        iniTxtHBloodGroup();
        iniTxtHGender();
        iniZone();
        iniCity();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workinArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(createBottom());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "476px", null, null, DmPosition.CENTER, true);

        ///////////////////////////////////////////1st Row//////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Corporate");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCorporate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        ///////////////////////////////////////////////2nd Row////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEmpCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //
        ///////////////////////////////////////////// 3nd row//////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEmpName));
        flwTr.add(flwTd);

        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //////////////////////////////////////////////4th Row///////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Emp./Policy/Service No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPolicyNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /////////////////////////////////////////////5th Row///////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card Holder Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardHolderName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //////////////////////////////////////////// 6th Row//////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Relationship");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRelationship));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rank");
        lbl.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRank));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //////////////////////////////////////////7th row////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("DOB");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDob, "DOB"));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Age");
        lbl.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);

        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);

        hrtPnl.add(vl.add(txtAgeYear, "Age Year"));
        txtAgeYear.getElement().setPropertyString("placeholder", "YY");
        hrtPnl.setCellWidth(txtAgeYear, "40%");

        hrtPnl.add(vl.add(txtAgeMonth, "Age Month"));
        txtAgeMonth.getElement().setPropertyString("placeholder", "MM");
        hrtPnl.setCellWidth(txtAgeMonth, "30%");

        hrtPnl.add(vl.add(txtAgeDay, "Age Day"));
        txtAgeDay.getElement().setPropertyString("placeholder", "DD");
        hrtPnl.setCellWidth(txtAgeDay, "30%");

        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////////////////////8th row/////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Blood Group");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBloodGroup, "Blood Group"));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gender");
        lbl.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGender));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        ///////////////////////////////////////9th row////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //6th
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);

        flwTd.add(vl.add(txtAddress1));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 10th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress2));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================<11th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("City");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCity));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 12th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("State");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtState));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/
 /*====================================< 13th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Zone");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHZone));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PIN");
        lbl.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPinCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/

        return flwTable;

    }

    private DmUserControl createBottom() {

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }

}
