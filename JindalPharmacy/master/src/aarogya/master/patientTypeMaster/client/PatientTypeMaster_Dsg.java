package aarogya.master.patientTypeMaster.client;

import aarogya.master.patientTypeMaster.shared.Find_Mdl;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class PatientTypeMaster_Dsg extends IsWidgetExtended {

    public final static String widgetName = "Patient Type Master";
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmSuggestBox txtPatientType = new DmSuggestBox(35, 10, true, "Name", DmDataType.STRING);
    final PopupPanel ppForm = new PopupPanel();
    final Menu menu = new Menu();
    final MenuItem mnuExportCard = new MenuItem("Export Service");
//    final DmSuggestBox txtShortName = new DmSuggestBox(10, 7, true, "shortName", DmDataType.STRING);

    //<editor-fold defaultstate="collapsed" desc="txtHClass">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHClass = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHClass() {
        txtHClass.getDmGrid().addTextColumn(txtHClass.getDmGrid().getPropertyAccess().name(), "Class", 0, "name");
        txtHClass.getDmGrid().initializeGrid(txtHClass.getDmGrid().getPropertyAccess().code(), 125, 0, 1);
        txtHClass.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHStatus = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHStatus() {
        txtHStatus.getDmGrid().addTextColumn(txtHStatus.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHStatus.getDmGrid().initializeGrid(txtHStatus.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold> 

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ClassName")
        ValueProvider<Find_Mdl, String> className();

        @Editor.Path("Active")
        ValueProvider<Find_Mdl, String> active();

//        @Editor.Path("ShortName")
//        ValueProvider<Find_Mdl, String> shortName();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 7);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Patient Type", 0, "NAME");
//        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 250, "shortName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().className(), "Class ", 380, "className");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().active(), "Active ", 360, "isActive");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("NAME", DmSortType.ASC);
        dmFind.getHelper().addSort("className", DmSortType.ASC);
    }
    //</editor-fold>

    public PatientTypeMaster_Dsg() {
        ini();
        iniMenu();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniTxtHClass();
        iniTxtHStatus();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workinArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(iniUserControl());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "62%", "60px", null, null, DmPosition.CENTER, true);

        //Department Name
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Patient Type");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPatientType));
        flwTR.add(flwTD);

//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Short Name");
//        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
//        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
//        flwTD.add(lbl);
//        flwTR.add(flwTD);
//
//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "18%", null, "left", null, DmPosition.NONE, false);
//        flwTD.add(vl.add(txtShortName));
//        flwTR.add(flwTD);

        flwTable.add(flwTR);

        //Short Name
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Class");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "37%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHClass));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        lbl.getElement().getStyle().setMarginLeft(40, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "18%", null, "left", null, DmPosition.NONE, false);
        flwTD.add((txtHStatus));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

    private DmUserControl iniUserControl() {

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }

    private Menu iniMenu() {
        menu.add(mnuExportCard);
        return menu;
    }
}
