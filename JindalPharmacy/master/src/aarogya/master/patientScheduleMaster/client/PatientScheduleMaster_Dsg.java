package aarogya.master.patientScheduleMaster.client;

import aarogya.master.patientScheduleMaster.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmDataType;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmHelpGridMulti.client.DmHelpGridMulti;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class PatientScheduleMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().PATIENT_SCHEDULE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmSuggestBox txtName = new DmSuggestBox(35, 10, true, "Name", DmDataType.STRING);
    final DmTextBox txtReportPrefix = new DmTextBox(35, false);
    final DmSuggestBox txtShortName = new DmSuggestBox(10, 10, true, "shortName", DmDataType.STRING);

    //<editor-fold defaultstate="collapsed" desc="txtHSchdlGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSchdlGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniScheduleGroup() {
        txtHSchdlGroup.getDmGrid().addTextColumn(txtHSchdlGroup.getDmGrid().getPropertyAccess().name(), "Schedule Group", 0, "name");
        txtHSchdlGroup.getDmGrid().initializeGrid(txtHSchdlGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHSchdlGroup.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ScheduleGroup")
        ValueProvider<Find_Mdl, String> scheduleGroup();

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 200, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "manualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().scheduleGroup(), "Schedule Group", 100, "scheduleGroup");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1280, 0, 1);

        dmFind.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("manualCode", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("scheduleGroup", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    public PatientScheduleMaster_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniScheduleGroup();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(iniUserControl());

        vrtInnerForm.setCellHeight(flw, "100%");

        flwForm.add(vrtInnerForm);
    }

    private DmUserControl iniUserControl() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "519px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< 1st Row FROM AND TO  >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "60px", "left", null, DmEnum.DmPosition.NONE, true);

        //============================< To column( border-left: none )>=======================
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "60px", "left", null, DmEnum.DmPosition.NONE, true);

        flwTd.add(mainSection());
        flwTr.add(flwTd);
        flwTable.add(flwTr);


        /*====================================<2 Row Grid  >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "450px", "left", null, DmEnum.DmPosition.NONE, true);
        //flwTr.add(vl.add(iniGrdService()));
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1280px", "450px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(iniGrdService()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel mainSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "55%", "60px", null, null, DmEnum.DmPosition.CENTER, true);

        //======================================1 row======================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //======================================2 Row======================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Schedule Group");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSchdlGroup));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;

    }

    //<editor-fold defaultstate="collapsed" desc="Grd Service">
    interface GridService_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPatntSchdulMster_Mdl> {

        @Editor.Path("Procedures")
        ValueProvider<GrdPatntSchdulMster_Mdl, String> procedures();

        @Editor.Path("CycleTime")
        ValueProvider<GrdPatntSchdulMster_Mdl, String> cycleTime();

        @Editor.Path("PrintSNo")
        ValueProvider<GrdPatntSchdulMster_Mdl, String> printSNo();

        @Editor.Path("Days")
        ValueProvider<GrdPatntSchdulMster_Mdl, String> days();

        @Editor.Path("Consultant")
        ValueProvider<GrdPatntSchdulMster_Mdl, String> consultant();

        @Editor.Path("Machine")
        ValueProvider<GrdPatntSchdulMster_Mdl, String> machine();

        @Editor.Path("GuiTemplate")
        ValueProvider<GrdPatntSchdulMster_Mdl, String> guiTemplate();

    }
    final DmGrid<GrdPatntSchdulMster_Mdl, GridService_PrpAcs> grdService = new DmGrid(com.google.gwt.core.shared.GWT.create(GridService_PrpAcs.class));

    private Grid iniGrdService() {
        grdService.addRowNumberColumn();
        grdService.addTextColumn(grdService.getPropertyAccess().procedures(), "Procedure", 400, "");
        grdService.addTextColumn(grdService.getPropertyAccess().cycleTime(), "Cycle Time", 70, "");
        grdService.addTextColumn(grdService.getPropertyAccess().days(), "Day", 40, "");
        grdService.addTextColumn(grdService.getPropertyAccess().printSNo(), "Print S.No.", 65, "");
        grdService.addTextColumn(grdService.getPropertyAccess().guiTemplate(), "Gui Template", 215, "");
        grdService.addTextColumn(grdService.getPropertyAccess().machine(), "Machine & Places", 215, "");
        grdService.addTextColumn(grdService.getPropertyAccess().consultant(), "Consultant", 215, "");
        grdService.initializeGrid(grdService.getPropertyAccess().fieldHashCode(), 1280, 450, 2);
        return grdService.getGrid();
    }
    //</editor-fold>

}
