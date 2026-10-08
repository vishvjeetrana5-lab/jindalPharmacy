package dataman.payroll.departmentMaster.client;

import dataman.payroll.departmentMaster.shared.Find_Mdl;
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
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmPayroll;

abstract class DepartmentMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new lmPayroll().DEPARTMENT_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmSuggestBox txtDepartmentName = new DmSuggestBox(35, 10, true, "Name", DmDataType.STRING);
    final DmSuggestBox txtShortName = new DmSuggestBox(15, 10, true, "shortName", DmDataType.STRING);
    final DmTextBox txtDisplayName = new DmTextBox(35, true);

    final MenuItem mnuManageSite = new MenuItem("Manage Site");
    final MenuItem mnuExportCard = new MenuItem("Export Department");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="txtHCostCenter">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCostCenter = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    public void iniTxtCostCenter() {
        txtHCostCenter.getDmGrid().addTextColumn(txtHCostCenter.getDmGrid().getPropertyAccess().name(), "Cost Center", 0, "Name");
        txtHCostCenter.getDmGrid().initializeGrid(txtHCostCenter.getDmGrid().getPropertyAccess().code(), 125, 0, 1);
        txtHCostCenter.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

        @Editor.Path("CostCenter")
        ValueProvider<Find_Mdl, String> costCenter();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 14);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Department", 0, "NAME");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
//        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().costCenter(), "Cost center", 170, "costCenter");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("NAME", DmSortType.ASC);
        dmFind.getHelper().addSort("shortName", DmSortType.ASC);
//        dmFind.getHelper().addSort("costCenter", DmSortType.ASC);

    }
    //</editor-fold>

    public DepartmentMaster_Dsg() {
        ini();
        iniOtherMenu();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniTxtCostCenter();

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
        DmClient.setStyle(flwTable, "62%", "60px", null, null, DmPosition.CENTER, true);

        //Department Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDepartmentName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Short Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Display Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDisplayName));
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Short Name");
//        lbl.getElement().getStyle().setMarginLeft(22, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtShortName));
//        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
//        lbl.getElement().getStyle().setMarginLeft(22, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "29%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);

        //nature
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Cost Center");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "29%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHCostCenter));
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);

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
