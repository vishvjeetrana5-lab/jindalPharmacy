package dataman.payroll.attendenceCodeDefinition.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmPayroll;
import dataman.payroll.attendenceCodeDefinition.shared.*;

abstract class AttendenceCodeDefinition_Dsg extends IsWidgetExtended {

    final String widgetName = new lmPayroll().ATTENDENCE_CODE_DEFINITION.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmSuggestBox txtSgName = new DmSuggestBox(35, 10, true, "Name", DmDataType.STRING);
    final DmSuggestBox txtSgShortName = new DmSuggestBox(15, 10, true, "shortName", DmDataType.STRING);
    final DmTextBoxNumeric txtWorkingHrs = new DmTextBoxNumeric(2, false, 5, false);
    final DmTextBoxNumeric txtDays = new DmTextBoxNumeric(2, false, 5, false);

    final MenuItem mnuManageSite = new MenuItem("Manage Site");
    final MenuItem mnuExportCard = new MenuItem("Export Department");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="txtHDefault">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDefault = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, true, true);

    public void iniTxtHDefault() {
        txtHDefault.getDmGrid().addTextColumn(txtHDefault.getDmGrid().getPropertyAccess().name(), "By Default", 0, "");
        txtHDefault.getDmGrid().initializeGrid(txtHDefault.getDmGrid().getPropertyAccess().code(), 125, 0, 1);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDefault">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPlusMinus = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, true, true);

    public void iniTxtHPlusMinus() {
        txtHPlusMinus.getDmGrid().addTextColumn(txtHPlusMinus.getDmGrid().getPropertyAccess().name(), "+/-", 0, "");
        txtHPlusMinus.getDmGrid().initializeGrid(txtHPlusMinus.getDmGrid().getPropertyAccess().code(), 125, 0, 1);

    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

        @Editor.Path("WorkingHours")
        ValueProvider<Find_Mdl, String> workingHours();

        @Editor.Path("Days")
        ValueProvider<Find_Mdl, String> days();

        @Editor.Path("ByDefault")
        ValueProvider<Find_Mdl, String> byDefault();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 14);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "manualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().workingHours(), "Working Hours", 170, "workingHours");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().days(), "Days", 170, "days");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().byDefault(), "By Default", 170, "byDefault");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("NAME", DmSortType.ASC);
        dmFind.getHelper().addSort("manualCode", DmSortType.ASC);
        dmFind.getHelper().addSort("workingHours", DmSortType.ASC);

    }
    //</editor-fold>

    public AttendenceCodeDefinition_Dsg() {
        ini();
        iniOtherMenu();
        iniFind();
        iniTxtHDefault();
        iniTxtHPlusMinus();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workinArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(iniUserControl());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "40%", "120px", null, null, DmPosition.CENTER, true);
//        DmClient.setStyle(flwTable, "62%", "60px", null, null, DmPosition.CENTER, true);

        // Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSgName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
        // Short Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
//        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSgShortName));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Working Hours");
        lbl.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtWorkingHrs));
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        
        // Working Hours
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
//        
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Working Hours");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtWorkingHrs));
//        flwTr.add(flwTd);
//        
//        flwTable.add(flwTr);
        
         // Days
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Days");
//        lbl.getElement().getStyle().setMarginLeft(35, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDays));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("+/-");
        lbl.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPlusMinus));
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);

         // By Default
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("By Default");
//        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDefault));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private DmUserControl iniUserControl() {

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }

    private Menu iniOtherMenu() {
        menu.add(mnuManageSite);
        menu.add(mnuExportCard);
        return menu;
    }
}
