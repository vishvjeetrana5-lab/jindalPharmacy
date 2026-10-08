package dataman.utility.branchMaster.client;

import dataman.libraryMenu.self.shared.lmUtility;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.utility.branchMaster.shared.*;

abstract public class BranchMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().BRANCH_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwForm = new FlowPanel();
    final DmSuggestBox txtBranchName = new DmSuggestBox(75, 10, true, "Name", DmDataType.STRING);
    final DmTextBox txtBranchCode = new DmTextBox(6, true);
    final DmTextBox txtCityName = new DmTextBox(35, true);

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("BranchCode")
        ValueProvider<Find_Mdl, String> branchCode();

        @Editor.Path("CityName")
        ValueProvider<Find_Mdl, String> cityName();

        @Editor.Path("UserName")
        ValueProvider<Find_Mdl, String> userName();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 7);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Branch Name", 250, "Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().branchCode(), "Branch Code", 100, "ManualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cityName(), "City Name", 300, "City");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().userName(), "User Name", 200, "U_Name");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1280, 200, 1);
        dmFind.getHelper().addSort("Name", DmSortType.ASC);
        dmFind.getHelper().addSort("ManualCode", DmSortType.ASC);
        dmFind.getHelper().addSort("City", DmSortType.ASC);
        dmFind.getHelper().addSort("U_Name", DmSortType.ASC);
    }
//</editor-fold>

    public BranchMaster_Dsg() {
        ini();
        iniFind();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "400px", "65px", null, null, DmPosition.CENTER, true);
        FlowPanel flwTr;
        FlowPanel flwTd;
        DmLabel lblTemp;

        ///////////////////////// TR1 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        lblTemp = new DmLabel("Branch Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBranchName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        ///////////////////////// TR2 //////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        lblTemp = new DmLabel("Branch Code");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBranchCode));
        flwTr.add(flwTd);
        /////////////////////////////////////TR3///////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        lblTemp = new DmLabel("City Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCityName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
