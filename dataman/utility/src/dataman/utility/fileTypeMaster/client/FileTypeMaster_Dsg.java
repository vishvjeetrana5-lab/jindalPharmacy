package dataman.utility.fileTypeMaster.client;

import dataman.libraryMenu.self.shared.lmUtility;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment; 
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.utility.fileTypeMaster.shared.Find_Mdl;

import dataman.utility.fileTypeMaster.shared.GrdFileTypeMaster_Mdl;

abstract class FileTypeMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().FILE_TYPE_MASTER.caption;
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final Menu menu = new Menu();
    MenuItem syncronizeFileDB;
    MenuItem exportData;
    SelectionHandler<MenuItem> selectionHandler;

    final DmTextBox txtRuleCode = new DmTextBox(5, false);
    final DmTextBox txtDescription = new DmTextBox(150, false);

    abstract void menuClick(SelectionEvent<MenuItem> event);

    public interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> 
    {
        @Editor.Path("Description")
        ValueProvider<Find_Mdl, String> description();
    }
   public  final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 17);

   
    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Rule Code", 300, "ruleCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().description(), "Description",800 , "description");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1258, 250, 1);
        dmFind.getHelper().addSort("ruleCode", DmEnum.DmSortType.ASC);
    }

    public FileTypeMaster_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(createBottom());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
        createMenu();
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable;
        FlowPanel flwTr;
        FlowPanel flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "363px", "left", null, DmPosition.CENTER, true);
        /*====================================< 1st Row (Main Form) >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTd.add(topArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row (First Grid)>====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "338px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "338px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(iniGrid()));

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;

    }

    private FlowPanel topArea() {

        FlowPanel flwTable;
        FlowPanel flwTr;
        FlowPanel flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "80%", "20px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Rule Code");
        lblTemp.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRuleCode));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Description");
        lblTemp.getElement().getStyle().setMarginLeft(8, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDescription));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grid">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdFileTypeMaster_Mdl> {

        @Editor.Path("Filter")
        ValueProvider<GrdFileTypeMaster_Mdl, String> filter();

        @Editor.Path("defaultName")
        ValueProvider<GrdFileTypeMaster_Mdl, String> defaultName();

    }

    final DmGrid<GrdFileTypeMaster_Mdl, Grd_PrpAcs> dmGrd = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid iniGrid() {
        dmGrd.addRowNumberColumn();
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().code(), "File Type", 100, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().name(), "Max. Size (KB)", 150, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().filter(), "Filters", 500, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().defaultName(), "Default", 100, "");
        dmGrd.initializeGrid(dmGrd.getPropertyAccess().fieldHashCode(), 1280, 339, 2);

        return dmGrd.getGrid();
    }
//</editor-fold>

    private DmUserControl createBottom() {
        userControl.addTextBlog(modifiedByBlog, 100, false, true);
        userControl.addGap(100);
        return userControl;

    }

    private Menu createMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {
            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        syncronizeFileDB = new MenuItem("Syncronize File DB", selectionHandler);
        menu.add(syncronizeFileDB);
        exportData = new MenuItem("Export Data", selectionHandler);
        menu.add(exportData);

        return menu;
    }
}
