package dataman.payroll.shiftMaster.client;

import dataman.payroll.shiftMaster.client.*;
import dataman.payroll.shiftMaster.shared.*;
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
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

import dataman.libraryMenu.self.shared.lmPayroll;

abstract class ShiftMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new lmPayroll().SHIFT_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final Button btnApprovedBy = new Button();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtShiftName = new DmTextBox(100, true);
    final DmTextBox txtShortName = new DmTextBox(100, true);
    final DmTextBox txtDuration = new DmTextBox(100, false);
    final DmTextBoxTime txtToTime = new DmTextBoxTime(true);
    final DmTextBoxTime txtFromTime = new DmTextBoxTime(true);
    final DmTextBoxTime txtInTimeGrace = new DmTextBoxTime(true);
    final DmTextBoxTime txtOutTimeGrace = new DmTextBoxTime(true);
    final DmTextBoxNumeric txtWorkingHrs = new DmTextBoxNumeric(2, false, 5, false);


    // <editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

        @Editor.Path("FromTime")
        ValueProvider<Find_Mdl, String> fromTime();

        @Editor.Path("ToTime")
        ValueProvider<Find_Mdl, String> toTime();

        @Editor.Path("InTimeGrace")
        ValueProvider<Find_Mdl, String> inTimeGrace();

        @Editor.Path("OutTimeGrace")
        ValueProvider<Find_Mdl, String> outTimeGrace();

    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 10);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Shift Name", 100, "shiftName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().fromTime(), "From Time", 100, "fromTime");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().toTime(), "To Time", 100, "toTime");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().inTimeGrace(), "In Time Grace", 100, "inTimeGrace");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().outTimeGrace(), "Out Time Grace", 100, "outTimeGrace");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("shiftName", DmSortType.ASC);
    }
    //</editor-fold>
    // Constructor

    //<editor-fold defaultstate="collapsed" desc="TxtHBloodGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIncludesNextDay = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 4,true);

    private void iniTxtHIncludesNextDay() {
        txtHIncludesNextDay.getDmGrid().addTextColumn(txtHIncludesNextDay.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHIncludesNextDay.getDmGrid().initializeGrid(txtHIncludesNextDay.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
        txtHIncludesNextDay.getHelper().addSort("Name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    public ShiftMaster_Dsg() {
        ini();
        iniTxtHIncludesNextDay();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true); // It sets the 
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        //    vrtInnerForm.add(createBottom());
        vrtInnerForm.add(addUserControl());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "200px", null, null, DmPosition.CENTER, true);

        //================1st Row============================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        //===========Left Section=====================================
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", "200px", "left", null, DmPosition.NONE, false);
        flwTD.add(top_leftSection());
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel top_leftSection() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "200px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTR.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Shift Name");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtShiftName));

        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtShortName));

        flwTR.add(flwTD);

        flwTable.add(flwTR);
        
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Includes Next day");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHIncludesNextDay));

        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 2nd Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        //flwTR.getElement().getStyle().setMarginTop(5, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("From Time");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtFromTime));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 3rd Row >====================================*/

        //   flwTR.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To Time");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtToTime));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*====================================< 4th Row >====================================*/

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        //     flwTR.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Duration (Hr.)");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtDuration));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 5th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        //   flwTR.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("In Time Grace");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtInTimeGrace));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Out Time Grace");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtOutTimeGrace));
        flwTR.add(flwTD);

        flwTable.add(flwTR);     

        return flwTable;
    }

    private DmUserControl addUserControl() {
        userControl.addTextBlog(preparedByBlog, 200, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 200, false, true);
//        userControl.addGap(100);

        return userControl;
    }
}
