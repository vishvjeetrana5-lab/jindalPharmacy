package aarogya.master.serviceGroupMaster.client;

import dataman.dmBase.self.shared.*;
import aarogya.master.serviceGroupMaster.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ServiceGroupMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().SERVICE_GROUP_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmSuggestBox txtGroupName = new DmSuggestBox(50, 10, true, "name", DmDataType.STRING);
    final DmTextBoxNumeric txtPrintSerialNo = new DmTextBoxNumeric(2, false, 4, false);
    final MenuItem mnuExportCard = new MenuItem("Export Group");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="txtHIsPerPage">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsPerPage = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHIsPerPage() {
        txtHIsPerPage.getDmGrid().addTextColumn(txtHIsPerPage.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHIsPerPage.getDmGrid().initializeGrid(txtHIsPerPage.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
    }
    //</editor-fold>

    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("PrintSerialNo")
        ValueProvider<Find_Mdl, String> printSerialNo();

        @Editor.Path("IsPerPage")
        ValueProvider<Find_Mdl, String> isPerPage();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Group Name", 0, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().printSerialNo(), "Print S.No.", 100, "printSNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().isPerPage(), "Is Per Page", 100, "isPerPage");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1280, 183, 1);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }

    public ServiceGroupMaster_Dsg() {
        ini();
        iniTxtHIsPerPage();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniMenu();
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
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "476px", null, null, DmPosition.CENTER, true);

        //1st row////////////////////////////////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Group Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtGroupName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        /////////////////////////////////////////////////////////////////////////////

        //2nd row.////////////////////////////////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Print Separate Page");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHIsPerPage));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        /////3rd row
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Print Serial No.");
        flwTD.add(lbl);
//        lbl.getElement().getStyle().setMarginLeft(18, Style.Unit.PCT);
//        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPrintSerialNo));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        ///////////////////////////////////////////////////////////////////////////
        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuExportCard);
        return menu;
    }
}
