package dataman.accounts.defineCostCenter.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.defineCostCenter.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class DefineCostCenter_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().DEFINE_COSTCENTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmSuggestBox txtCostCenter = new DmSuggestBox(50, 10, false, "Name", DmDataType.STRING);
    final DmTextBox txtManualCode = new DmTextBox(20, true);
    final DmTextBox txtDescription = new DmTextBox(249, false);

    //<editor-fold defaultstate="collapsed" desc="txtHUnderCostCenter">
    interface txtHUnderCostCenter_PrpAcs extends KeyField_T_PrpAcs<TxtHUnderCostCenter_Mdl> {

        @Editor.Path("UnderCostCenter")
        ValueProvider<TxtHUnderCostCenter_Mdl, String> underCostCenter();
    }
    final DmTextHelp<TxtHUnderCostCenter_Mdl, txtHUnderCostCenter_PrpAcs> txtHUnderCostCenter = new DmTextHelp(GWT.create(txtHUnderCostCenter_PrpAcs.class), 7);

    private void initxtHUnderCostCenter() {
        txtHUnderCostCenter.getDmGrid().addTextColumn(txtHUnderCostCenter.getDmGrid().getPropertyAccess().name(), "Cost Center", 0, "Name");
        txtHUnderCostCenter.getDmGrid().addTextColumn(txtHUnderCostCenter.getDmGrid().getPropertyAccess().underCostCenter(), "Under Cost Center", 200, "UName");
        txtHUnderCostCenter.getDmGrid().initializeGrid(txtHUnderCostCenter.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHUnderCostCenter.getHelper().addSort("NAME", DmSortType.ASC);
        txtHUnderCostCenter.getHelper().addSort("UName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2);

    private void initxtHActive() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmGrd">
    final DmGrid<KeyFieldExtended, KeyField_PrpAcs> dmGrd = new DmGrid(GWT.create(KeyField_PrpAcs.class));

    public Grid iniGrid() {
        dmGrd.addRowNumberColumn();
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().name(), "Under Cost Center Hierarchy", 0, "");
        dmGrd.initializeGrid(dmGrd.getPropertyAccess().code(), 640, 200, 2);
        return dmGrd.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("UCostCenter")
        ValueProvider<Find_Mdl, String> UCostCenter();

        @Editor.Path("ActiveName")
        ValueProvider<Find_Mdl, String> activeName();

        @Editor.Path("Description")
        ValueProvider<Find_Mdl, String> description();

        @Editor.Path("ManualCode")
        ValueProvider<Find_Mdl, String> manualCode();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 18);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Cost Center Name", 0, "Cost_Center_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().UCostCenter(), "Under Cost Center", 150, "Under_Cost_Center");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "ManualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().activeName(), "Active", 150, "isActive");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().description(), "Description", 200, "Description");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);
        dmFind.getHelper().addSort("Cost_Center_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("Under_Cost_Center", DmSortType.ASC);
        dmFind.getHelper().addSort("ManualCode", DmSortType.ASC);
    }
    //</editor-fold>

    public DefineCostCenter_Dsg() {
        ini();
    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.addStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        /*====================================< Find Grid & Help >====================================*/
        iniFind();
        initxtHUnderCostCenter();
        initxtHActive();
        /*====================================< **************** >====================================*/
        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable;
        FlowPanel flwTr;
        FlowPanel flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "310px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Cost Center");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        txtCostCenter.setMandatory(true);
        flwTd.add(vl.add(txtCostCenter));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Under Cost Center");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHUnderCostCenter));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Description");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDescription));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/


 /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Manual Code");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtManualCode.setMandatory(true);
        flwTd.add(vl.add(txtManualCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();

        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Active");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtHActive.setMandatory(true);
        flwTd.add(vl.add(txtHActive));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.CENTER_HORIZONTAL, true);
        flwTr.getElement().getStyle().setTop(5, Style.Unit.PCT);
        flwTr.add(iniGrid());

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }
}
