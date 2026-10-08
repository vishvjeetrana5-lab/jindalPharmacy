package aarogya.master.managePatientSchedule.client;

import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.master.managePatientSchedule.shared.Find_Mdl;
import aarogya.master.managePatientSchedule.shared.GrdSchedules_Mdl;
import aarogya.master.managePatientSchedule.shared.TxtHSchedule_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmDataType;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ManagePatientSchedule_Dsg extends IsWidgetExtended {

    public final static String widgetName = new pmMaster().MANAGE_PATIENT_SCHEDULE.caption;

    public static final String saveScheduleBlog = "Save Schedule";
    public static final String deleteSchduleBlog = "Delete Schedule";
    public static final String manageSchduleBlog = "Change Schedule";
    public static final String patientFollowUpBlog = "Patient Follow-up";
    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwWorkignArea = new FlowPanel();
    final DmUserControl userControl = new DmUserControl();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final InlineLabel lblUHID = new InlineLabel();
    final DmTextBox txtRegNo = new DmTextBox(50, false);

    //--------------------------- 1st row Txt---------------------------------------------
    final FlowPanel flwSearchPatient = new FlowPanel();
    final Button btnSearchPatient = new Button("...");
    final DmTextBox txtUHID = new DmTextBox(20, true);
    final DmTextBox txtScheduleName = new DmTextBox(20, false);
    final DmTextBox txtEntryDate = new DmTextBox(12, true);
    final DmTextBox txtActive = new DmTextBox(12, true);

    //------------------------ 2nd row Txt---------------------------------------------------
    final DmTextBox txtPatientName = new DmTextBox(50, false);
    final DmTextBox txtGuardianName = new DmTextBox(50, false);
    final DmTextBox txtAddress = new DmTextBox(100, false);
    final DmTextBox txtMobile = new DmTextBox(35, false);
    final DmTextBox txtAge = new DmTextBox(11, false);
    final DmTextBox txtGender = new DmTextBox(15, false);

    //----------------------- Buttons for Tool Container--------------------------------------
    final Button btnNew = new Button();
    final FlowPanel flwNewParent = new FlowPanel();
    final FlowPanel flwNewChild = new FlowPanel();
    final Button btnClose = new Button();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnFind = new Button();
    final FlowPanel flwFind = new FlowPanel();

    final Button btnDiscard = new Button();
    final FlowPanel flwDiscardParent = new FlowPanel();
    final FlowPanel flwDiscardChild = new FlowPanel();

    final FlowPanel flwManageFile = new FlowPanel();
    final Button btnManageFile = new Button("");

    final FlowPanel flwRefresh = new FlowPanel();
    final Button btnRefresh = new Button();

    final FlowPanel flwSchedule = new FlowPanel();

    final Button btnSchedule = new Button("Patient Follow-up");

    //<editor-fold defaultstate="collapsed" desc="txtHSchedule">
    interface DmSchedule_PrpAcs extends KeyField_T_PrpAcs<TxtHSchedule_Mdl> {

        @Editor.Path("ScheduleManual")
        ValueProvider<TxtHSchedule_Mdl, String> scheduleManual();

        @Editor.Path("VDate")
        ValueProvider<TxtHSchedule_Mdl, String> vDate();

        @Editor.Path("ScheduleName")
        ValueProvider<TxtHSchedule_Mdl, String> scheduleName();

        @Editor.Path("Group")
        ValueProvider<TxtHSchedule_Mdl, String> group();
    }
    final DmTextHelp<TxtHSchedule_Mdl, DmSchedule_PrpAcs> txtHSchedule = new DmTextHelp(GWT.create(DmSchedule_PrpAcs.class), 10, true);

    private void iniTxtHSchedule() {
        txtHSchedule.getDmGrid().addTextColumn(txtHSchedule.getDmGrid().getPropertyAccess().name(), "Schedule No.", 100, "recId");
        txtHSchedule.getDmGrid().addTextColumn(txtHSchedule.getDmGrid().getPropertyAccess().scheduleName(), "Schedule Name", 90, "name");
        txtHSchedule.getDmGrid().addTextColumn(txtHSchedule.getDmGrid().getPropertyAccess().scheduleManual(), "Short Name", 90, "sName");
        txtHSchedule.getDmGrid().addTextColumn(txtHSchedule.getDmGrid().getPropertyAccess().group(), "Group", 90, "group");
        txtHSchedule.getDmGrid().addTextColumn(txtHSchedule.getDmGrid().getPropertyAccess().vDate(), "Date", 80, "v_Date", DmDataType.DATE);
        txtHSchedule.getDmGrid().initializeGrid(txtHSchedule.getDmGrid().getPropertyAccess().code(), 600, 0, 2);
        txtHSchedule.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        txtHSchedule.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("PatientRecId")
        ValueProvider<Find_Mdl, String> patientRecId();

        @Editor.Path("ScheduleName")
        ValueProvider<Find_Mdl, String> scheduleName();

        @Editor.Path("PatientName")
        ValueProvider<Find_Mdl, String> patientName();

        @Editor.Path("SchdlDate")
        ValueProvider<Find_Mdl, String> schdlDate();

        @Editor.Path("Mobile")
        ValueProvider<Find_Mdl, String> mobile();

        @Editor.Path("Active")
        ValueProvider<Find_Mdl, String> active();

    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Schedule No.", 100, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().schdlDate(), "Entry Date", 75, "v_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().scheduleName(), "Schedule Name", 200, "scheduleName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().patientRecId(), "UHID No.", 100, "patientRecId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().patientName(), "Patient Name", 220, "patientName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().mobile(), "Mobile", 75, "mobile");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().active(), "Active", 75, "active");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1280, 183);

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("patientName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public ManagePatientSchedule_Dsg() {
        ini();

    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(toolControls());
        iniTxtHSchedule();
        iniFind();
        flwWorkignArea.setStyleName("dm-FlwWorkingArea");
        flwWorkignArea.add(workingArea());
        vrtInnerForm.add(flwWorkignArea);
        vrtInnerForm.setCellHeight(flwWorkignArea, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        CaptionPanel cPnl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "544px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "30px", "left", null, DmPosition.NONE, true);
        flwTr.add(Uhid());
        flwTable.add(flwTr);

        /*====================================< 2st PtientDetails  >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "65px", "left", null, DmEnum.DmPosition.NONE, true);
        cPnl = new CaptionPanel();
        cPnl.getElement().setAttribute("style", "border-left: none; border-right: none;  width 100%; height: 40px;");
        cPnl.add(patientDetails());
        flwTr.add(cPnl);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "420px", "left", null, DmPosition.NONE, true);
        //----------------------------------- Procedure Grid------------------------------------------
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1280px", "420px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(iniGrdSchedule()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel patientDetails() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "60px", null, null, DmPosition.CENTER, true);
        //=================================Row-2=============================================   
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", "margin-top:7px; ");

        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Patient Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPatientName));
        flwTr.add(flwTd);

        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Guardian's Name");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGuardianName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gender");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        //----------------------------------------------------------------------------------------------------------------------
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTd.add(vl.add(txtGender));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //=================================Row-5=============================================
        flwTr = new FlowPanel();

        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "53%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Age");
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAge));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        btnSchedule.setWidth("100%");
        btnSchedule.addStyleName("dm-button-grad");
        btnSchedule.setTitle("Patient Follow-up");
        btnSchedule.getElement().getStyle().setMarginLeft(102, Style.Unit.PX);
        flwSchedule.add(btnSchedule);
        flwTd.add(flwSchedule);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel Uhid() {
        FlowPanel flwTable, flwTr, flwTd;
        HorizontalPanel hrtPnl;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "30px", null, null, DmPosition.CENTER, true);

        //=================================Row-1=============================================
        flwTr = new FlowPanel();

        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr = new FlowPanel();

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        lblUHID.setText("UHID No.");

        flwTd.add(lblUHID);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(txtUHID);
        hrtPnl.setCellWidth(txtUHID, "100%");
        DmClient.setStyle(flwSearchPatient, "100%", "20px", "left", null, DmPosition.NONE, true);
        btnSearchPatient.setWidth("20px");
        btnSearchPatient.setHeight("20px");
        btnSearchPatient.addStyleName("dm-button-grad");
        btnSearchPatient.getElement().setAttribute("style", btnSearchPatient.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnSearchPatient.setTitle("Patient Search");
        flwSearchPatient.add(vl.add(btnSearchPatient));
        hrtPnl.add(flwSearchPatient);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Schedule No.");
        lbl.getElement().getStyle().setMarginLeft(17, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSchedule));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Schedule");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtScheduleName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtActive));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Entry Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEntryDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private DmToolContainer toolControls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("New");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-plus");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnNew.addStyleName("dm-top-control-btnStyle");
        btnNew.setTitle("New");
        btnNew.getElement().appendChild(aBtn.getElement());
        btnNew.getElement().appendChild(label.getElement());
        btnNew.setWidth("40px");
        btnNew.setTitle("New: (F2)");
        flwNewChild.add(vl.add(btnNew));
        flwNewParent.add(flwNewChild);

        label = new InlineLabel("Find");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-search");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnFind.addStyleName("dm-top-control-btnStyle");
        btnFind.setTitle("Find");
        btnFind.getElement().appendChild(aBtn.getElement());
        btnFind.getElement().appendChild(label.getElement());
        btnFind.setWidth("40px");
        btnFind.setTitle("Find: (Ctrl + F)");
        flwFind.add(vl.add(btnFind));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.setTitle("Close");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("40px");
        btnClose.setTitle("Close: (Escape)");
        flwClose.add(vl.add(btnClose));

        label = new InlineLabel("Discard");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-ban");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDiscard.addStyleName("dm-top-control-btnStyle");
        btnDiscard.setTitle("Discard");
        btnDiscard.getElement().appendChild(aBtn.getElement());
        btnDiscard.getElement().appendChild(label.getElement());
        btnDiscard.setWidth("40px");
        btnDiscard.setTitle("Discard: (Ctrl + D)");
        flwDiscardChild.add(vl.add(btnDiscard));
        flwDiscardParent.add((flwDiscardChild));

        label = new InlineLabel("Manage File");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-paperclip");
        btnManageFile.setTitle("Manage File");
        btnManageFile.addStyleName("dm-top-control-btnStyle");
        btnManageFile.getElement().appendChild(aBtn.getElement());
        btnManageFile.getElement().appendChild(label.getElement());
        btnManageFile.setWidth("92px");

        label = new InlineLabel("Refresh");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("75px");
        btnRefresh.setTitle("New: (Ctrl+R)");
        flwRefresh.add(vl.add(btnRefresh));

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwNewParent);
        toolContainer.addWidget(flwFind);
        toolContainer.addWidget(flwRefresh);
        toolContainer.addWidget(vl.add(btnManageFile));
        toolContainer.addWidget(flwDiscardParent);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

        return toolContainer;

    }

    protected int colIndexManage;

    interface GridService_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdSchedules_Mdl> {

        @Editor.Path("CycleTime")
        ValueProvider<GrdSchedules_Mdl, String> cycleTime();

        @Editor.Path("PrintSno")
        ValueProvider<GrdSchedules_Mdl, String> printSNo();

        @Editor.Path("ScheduleDate")
        ValueProvider<GrdSchedules_Mdl, String> scheduleDate();

        @Editor.Path("BookDate")
        ValueProvider<GrdSchedules_Mdl, String> bookDate();

        @Editor.Path("DoneDate")
        ValueProvider<GrdSchedules_Mdl, String> doneDate();

        @Editor.Path("Remark")
        ValueProvider<GrdSchedules_Mdl, String> remark();

        @Editor.Path("ConsultantName")
        ValueProvider<GrdSchedules_Mdl, String> consultantName();

        @Editor.Path("EntryDateTime")
        ValueProvider<GrdSchedules_Mdl, String> userEntry();

        @Editor.Path("Machine")
        ValueProvider<GrdSchedules_Mdl, String> machine();

        @Editor.Path("GuiTemplate")
        ValueProvider<GrdSchedules_Mdl, String> guiTemplate();

    }
    final DmGrid<GrdSchedules_Mdl, GridService_PrpAcs> grdSchedule = new DmGrid(com.google.gwt.core.shared.GWT.create(GridService_PrpAcs.class));

    private Grid iniGrdSchedule() {
        DmColumnConfig<GrdSchedules_Mdl, String> colManage;
        int colIndex = 0;
        colIndex++;
        grdSchedule.addRowNumberColumn();
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().name(), "Procedure", 580, "");
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().cycleTime(), "Cycle Time", 65, "");
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().scheduleDate(), "Schedule Date", 80, "", DmDataType.DATE);
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().printSNo(), "Print S.No.", 60, "");
        //colIndexBook = colIndex;
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().bookDate(), "Booking", 75, "");
        //colIndexDoneDate = colIndex;
        colIndex++;
        //colBook = grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().doneDate(), "Done Date", 75, "");
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().doneDate(), "Done Date", 75, "");
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().remark(), "Remark", 120, "");
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().guiTemplate(), "Gui Template", 110, "");
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().machine(), "Machine & Places", 110, "");
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().consultantName(), "Consultant", 110, "");
        colIndex++;
        grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().userEntry(), "User name", 150, "");
        colIndexManage = colIndex;
        colIndex++;
        colManage = grdSchedule.addTextColumn(grdSchedule.getPropertyAccess().code(), "", 40, "");
        grdSchedule.initializeGrid(grdSchedule.getPropertyAccess().fieldHashCode(), 1280, 420, 2);
//        colBook.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//        colBook.setCellPadding(false);

        grdSchedule.getGrid().getView().setViewConfig(new GridViewConfig<GrdSchedules_Mdl>() {

            @Override
            public String getColStyle(GrdSchedules_Mdl model, ValueProvider<? super GrdSchedules_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (model.isDoneStatus()) {
                    styleClass += "dm-GridCellDisable ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdSchedules_Mdl model, int rowIndex) {
                return "";
            }
        });

        colManage.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                GrdSchedules_Mdl mdl = grdSchedule.getGrid().getStore().get(context.getIndex());
                if (DmShared.dNull(mdl.getDoneDate()).equalsIgnoreCase("")) {
                    sb.appendHtmlConstant("<span class='fa fa-pencil' style='float: left; width: 100%; cursor: pointer; "
                            + "font-weight: bold; text-align: center; font-size: 16px; color: tomato;'></span>");
                }
            }
        });

        return grdSchedule.getGrid();
    }
    //</editor-fold>

}
