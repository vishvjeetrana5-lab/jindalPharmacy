package aarogya.master.overHeadMaster.client;

import aarogya.master.overHeadMaster.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class OverHeadMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().OVERHEAD_SERVICE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmSuggestBox txtName = new DmSuggestBox(160, 10, true, "Name", DmDataType.STRING);
    final DmTextBox txtShortName = new DmTextBox(10, true);
    final Menu menu = new Menu();
    MenuItem manageSiteMenu, manageFileMenu;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    //<editor-fold defaultstate="collapsed" desc="txtHCategory">
    interface TxtHCategory_PrpAcs extends KeyField_T_PrpAcs<TxtHCategory_Mdl> {

        @Editor.Path("Its_Name")
        ValueProvider<TxtHCategory_Mdl, String> its_Name();

        @Editor.Path("ServiceAcName")
        ValueProvider<TxtHCategory_Mdl, String> serviceAcName();
    }
    final DmTextHelp<TxtHCategory_Mdl, TxtHCategory_PrpAcs> txtHCategory = new DmTextHelp(GWT.create(TxtHCategory_PrpAcs.class), 10, true);

    private void iniTxtHCategory() {
        txtHCategory.getDmGrid().addTextColumn(txtHCategory.getDmGrid().getPropertyAccess().name(), "Name", 1, "categoryName");
        txtHCategory.getDmGrid().addTextColumn(txtHCategory.getDmGrid().getPropertyAccess().its_Name(), "ITS On Sale", 100, "itsSaleName");
        txtHCategory.getDmGrid().addTextColumn(txtHCategory.getDmGrid().getPropertyAccess().serviceAcName(), "Purchase / Service A/c", 180, "serviceAcName");
        txtHCategory.getDmGrid().initializeGrid(txtHCategory.getDmGrid().getPropertyAccess().code(), 450, 0, 1);

        txtHCategory.getHelper().addSort("categoryName", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Find">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

        @Editor.Path("Category")
        ValueProvider<Find_Mdl, String> category();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().category(), "Category", 370, "categoryName");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 1);

        dmFind.getHelper().addSort("NAME", DmSortType.ASC);
        dmFind.getHelper().addSort("shortName", DmSortType.ASC);
    }
    //</editor-fold>

    public OverHeadMaster_Dsg() {
        ini();
        iniFind();
        iniTxtHCategory();
        createMenu();
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
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "55%", "110px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Category");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCategory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private Menu createMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        manageSiteMenu = new MenuItem("Manage Site", selectionHandler);
        menu.add(manageSiteMenu);

        return menu;
    }

}
