package dataman.webJobs.scheduler.client;

import dataman.libraryMenu.self.shared.lmWebJobs;
import dataman.dmBase.self.shared.*;
import dataman.webJobs.scheduler.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class Scheduler_Dsg extends IsWidgetExtended {

    final String widgetName = new lmWebJobs().SCHEDULER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final VerticalPanel vrtInnerFormOnetime = new VerticalPanel();
    final VerticalPanel vrtInnerFormDaily = new VerticalPanel();
    final VerticalPanel vrtInnerFormWeekly = new VerticalPanel();
    final VerticalPanel vrtInnerFormMonthly = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmSuggestBox txtSchedulerName = new DmSuggestBox(100, 7, true, "Name", DmDataType.STRING);
    final DmTextBox txtDBLookUp = new DmTextBox(50, false);

    final Menu menu = new Menu();
    MenuItem log;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    final RadioButton radioOnes = new RadioButton("a", "Ones");
    final RadioButton radioDaily = new RadioButton("a", "Daily");
    final RadioButton radioWeekly = new RadioButton("a", "Weekly");
    final RadioButton radioMonthly = new RadioButton("a", "Monthly");
    final RadioButton radioMonthsDay = new RadioButton("b", "Days");
    final RadioButton radioMonthsOn = new RadioButton("b", "On");

    final DmTextBox txtStartDate = new DmTextBox(30, true);
    final DmTextBoxTime txtStartTime = new DmTextBoxTime(true);
    final DmTextBoxNumeric txtRepeatDay = new DmTextBoxNumeric(0, false, 1, true);
    final DmTextBoxNumeric txtRepeatWeek = new DmTextBoxNumeric(0, false, 1, true);
    final CheckBox cbSunday = new CheckBox("Sunday");
    final CheckBox cbMonday = new CheckBox("Monday");
    final CheckBox cbTuesday = new CheckBox("Tuesday");
    final CheckBox cbWednesday = new CheckBox("Wednesday");
    final CheckBox cbThursday = new CheckBox("Thursday");
    final CheckBox cbFriday = new CheckBox("Friday");
    final CheckBox cbSaturday = new CheckBox("Saturday");

    final FlowPanel flwRBSchChanger = new FlowPanel();
    final FlowPanel flwExpiry = new FlowPanel();

    final DmTextBox txtExpiryDate = new DmTextBox(30, false);
    final DmTextBoxTime txtExpiryTime = new DmTextBoxTime(false);

    //<editor-fold defaultstate="collapsed" desc="txtHPLt">
    interface DmDatabase_PrpAcs extends KeyField_T_PrpAcs<DataBase_Mdl> {

        @Editor.Path("Databasename")
        ValueProvider<DataBase_Mdl, String> dbaName();
    }

    final DmTextHelp<DataBase_Mdl, DmDatabase_PrpAcs> txtHPLT = new DmTextHelp(GWT.create(DmDatabase_PrpAcs.class), 5, true);

    private void iniPLT() {
        txtHPLT.getDmGrid().addTextColumn(txtHPLT.getDmGrid().getPropertyAccess().name(), "P.L.T Name", 0, "name");
        txtHPLT.getDmGrid().initializeGrid(txtHPLT.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHPLT.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHPPLTSite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHPLTSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Site Name", 1, "name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 200, 0, 2);

        txtHSite.getHelper().addSort("name", DmSortType.ASC);

    }
    //</editor-fold>   

    //<editor-fold defaultstate="collapsed" desc="txtHTemplate">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTemplate = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTemplate() {
        txtHTemplate.getDmGrid().addTextColumn(txtHTemplate.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHTemplate.getDmGrid().initializeGrid(txtHTemplate.getDmGrid().getPropertyAccess().code(), 120, 0, 1);

        txtHTemplate.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniActive() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Type", 0, "name");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 80, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHJobType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHJobType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniSchedulerType() {
        txtHJobType.getDmGrid().addTextColumn(txtHJobType.getDmGrid().getPropertyAccess().name(), "Type", 0, "name");
        txtHJobType.getDmGrid().initializeGrid(txtHJobType.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHMonthsName">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMonthsName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 13, true);

    private void iniTxtHmonthsName() {
        txtHMonthsName.getDmGrid().addTextColumn(txtHMonthsName.getDmGrid().getPropertyAccess().name(), "Months", 1, "Name");
        txtHMonthsName.getDmGrid().initializeGrid(txtHMonthsName.getDmGrid().getPropertyAccess().code(), 150, 0, 2);

    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="txtHMonthDate">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMonthDate = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHMonthsnumber() {
        txtHMonthDate.getDmGrid().addTextColumn(txtHMonthDate.getDmGrid().getPropertyAccess().name(), "Date", 1, "Name");
        txtHMonthDate.getDmGrid().initializeGrid(txtHMonthDate.getDmGrid().getPropertyAccess().code(), 90, 0, 2);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHMonthsWeek">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMonthsOnWeek = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 8, true);

    private void iniTxtHMonthsWeek() {
        txtHMonthsOnWeek.getDmGrid().addTextColumn(txtHMonthsOnWeek.getDmGrid().getPropertyAccess().name(), "Week", 1, "Name");
        txtHMonthsOnWeek.getDmGrid().initializeGrid(txtHMonthsOnWeek.getDmGrid().getPropertyAccess().code(), 120, 0, 2);

    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="TxtHMonthsDays">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMonthsOnDays = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 8, true);

    private void iniTxtHMonthsdays() {
        txtHMonthsOnDays.getDmGrid().addTextColumn(txtHMonthsOnDays.getDmGrid().getPropertyAccess().name(), "Days", 1, "Name");
        txtHMonthsOnDays.getDmGrid().initializeGrid(txtHMonthsOnDays.getDmGrid().getPropertyAccess().code(), 120, 0, 2);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRespUser">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRespUser = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHRespUser() {
        txtHRespUser.getDmGrid().addTextColumn(txtHRespUser.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHRespUser.getDmGrid().initializeGrid(txtHRespUser.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHRespUser.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Find">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("PltName")
        ValueProvider<Find_Mdl, String> pltName();

        @Editor.Path("JobType")
        ValueProvider<Find_Mdl, String> jobType();

        @Editor.Path("TaskScheduler")
        ValueProvider<Find_Mdl, String> taskScheduler();

        @Editor.Path("Active")
        ValueProvider<Find_Mdl, String> active();

        @Editor.Path("StartDateTime")
        ValueProvider<Find_Mdl, String> startDateTime();

        @Editor.Path("ExpireDateTime")
        ValueProvider<Find_Mdl, String> expireDateTime();

        @Editor.Path("ResponsibleUser")
        ValueProvider<Find_Mdl, String> responsibleUser();

        @Editor.Path("IsAlive")
        ValueProvider<Find_Mdl, String> isAlive();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 11);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Scheduler Name", 1, "schedulerName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().pltName(), "Product License To", 185, "pltname");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().responsibleUser(), "Resp. User", 70, "responsibleUser");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().jobType(), "Job Type", 55, "jobType");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().taskScheduler(), "Task Scheduler", 85, "taskScheduler");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().startDateTime(), "Start Date / Time", 110, "startDateTime");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().expireDateTime(), "Expire Date / Time", 110, "expireDateTime");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().active(), "Active", 40, "active");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isAlive(), "Status", 40, "");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 180, 1);

        dmFind.getHelper().addSort("schedulerName", DmSortType.ASC);

    }
    //</editor-fold>

    public Scheduler_Dsg() {
        ini();
        iniPLT();
        iniTemplate();
        iniActive();
        iniSchedulerType();
        iniTxtHMonthsWeek();
        iniTxtHMonthsdays();
        iniTxtHMonthsnumber();
        iniTxtHmonthsName();
        iniTxtHPLTSite();
        iniTxtHRespUser();
        createMenu();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(tabArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(createBottom());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);

    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "235px", null, null, DmPosition.NONE, true);
        tabPanel.add(workingAreaGenral(), "General");
        tabPanel.add(vl.add(workingAreaTaskScheduler()), "Task Scheduler");
        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");
        return tabPanel;
    }

    private FlowPanel workingAreaGenral() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "130px", null, null, DmPosition.CENTER, true);

        //Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Scheduler Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSchedulerName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //PLT
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Product License To");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPLT));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //DataBase Look Up
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("DataBase Look Up");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDBLookUp));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //PLT Site Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSite));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Scheduler Type & Template
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Job Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHJobType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Template");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTemplate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //Responsible User & Active
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Responsible User");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRespUser));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtHActive);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel workingAreaTaskScheduler() {
        FlowPanel flwTable, flwTr, flwTd;
        CaptionPanel cPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "150px", null, null, DmPosition.CENTER, true);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "150px", "left", null, DmPosition.NONE, true);

        cPnl = new CaptionPanel("Scheduler");
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", "150px", "left", null, DmPosition.NONE, true);
        DmClient.setStyle(cPnl, "75%", "125px", "left", null, DmPosition.NONE, false);
        cPnl.add(tab2LeftSection());
        flwTd.add(cPnl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", "150px", "left", null, DmPosition.NONE, true);

        flwTd.add(tab2RightSection());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel tab2LeftSection() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "100px", null, null, DmPosition.CENTER, true);
        //One time
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd.add(vl.add(radioOnes));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Daily
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(radioDaily));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Weekly
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(radioWeekly));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Monthly
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(radioMonthly));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel tab2RightSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "140px", null, null, DmPosition.CENTER, true);
        //Top
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Start Date/Time");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtStartDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtStartTime));
        txtStartTime.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "100px", "left", null, DmPosition.NONE, true);
        DmClient.setStyle(flwRBSchChanger, "100%", "100px", "left", null, DmPosition.NONE, true);
        flwTr.add(flwRBSchChanger);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        DmClient.setStyle(flwExpiry, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwExpiry.add(expiryDiv());
        flwTr.add(flwExpiry);
        flwTable.add(flwTr);

        return flwTable;
    }

    protected FlowPanel expiryDiv() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmLabel lbl;
        DmClient.setStyle(flwTable, "100%", "20px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Expire Date/Time");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(txtExpiryDate);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(txtExpiryTime);
        txtExpiryTime.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    protected FlowPanel flwDaily() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "100px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Repeat Every");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRepeatDay));

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Day");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PCT);
        flwTd.add(lbl);

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    protected FlowPanel flwWeekly() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "100px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Repeat Every");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRepeatWeek));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel(" Week On");
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(4, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        // list of Check Box 1
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(cbSunday));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(cbMonday));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(cbTuesday));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        // list of Check Box 2
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(cbWednesday));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(cbThursday));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(cbFriday));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        // list of Check Box 3
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(cbSaturday));

        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    protected FlowPanel flwMonthly() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "100px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Months");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMonthsName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //2
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(radioMonthsDay));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMonthDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //3
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(radioMonthsOn));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMonthsOnWeek));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "34.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMonthsOnDays));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private DmUserControl createBottom() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;
    }

    private Menu createMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };
        log = new MenuItem("Log", selectionHandler);
        menu.add(log);

        return menu;
    }

}
