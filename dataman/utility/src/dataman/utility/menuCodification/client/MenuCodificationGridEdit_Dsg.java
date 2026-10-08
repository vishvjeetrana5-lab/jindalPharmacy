package dataman.utility.menuCodification.client;

import dataman.utility.menuCodification.shared.TxtHMenuName_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;

public abstract class MenuCodificationGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtMenuCode = new DmTextBox(6, true);
    final DmTextBox txtMenuText = new DmTextBox(250, true);
    final DmTextBox txtModuleName = new DmTextBox(100, true);

    //<editor-fold defaultstate="collapsed" desc="txtHMenuName">
    interface TxtHMenuName_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHMenuName_Mdl> {

        @Editor.Path("ModuleName")
        ValueProvider<TxtHMenuName_Mdl, String> moduleName();

        @Editor.Path("MenuName")
        ValueProvider<TxtHMenuName_Mdl, String> menuName();

    }
    final DmTextHelp<TxtHMenuName_Mdl, TxtHMenuName_PrpAcs> txtHMenuName = new DmTextHelp(GWT.create(TxtHMenuName_PrpAcs.class), 7, true);

    private Grid iniTxtHMenuName() {
        txtHMenuName.getDmGrid().addTextColumn(txtHMenuName.getDmGrid().getPropertyAccess().name(), "Menu Text", 120, "mnuName");
        txtHMenuName.getDmGrid().addTextColumn(txtHMenuName.getDmGrid().getPropertyAccess().menuName(), "Menu Name", 150, "mnuText");
        txtHMenuName.getDmGrid().addTextColumn(txtHMenuName.getDmGrid().getPropertyAccess().moduleName(), "Module Name", 100, "mnuModule");
        txtHMenuName.getDmGrid().initializeGrid(txtHMenuName.getDmGrid().getPropertyAccess().fieldHashCode(), 400, 0, 1);

        txtHMenuName.getHelper().addSort("mnuModule", DmSortType.ASC);
        txtHMenuName.getHelper().addSort("mnuName", DmSortType.ASC);
        return txtHMenuName.getDmGrid().getGrid();
    }
    //</editor-fold>

    public MenuCodificationGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        iniTxtHMenuName();
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "96%", "40px", null, null, DmPosition.CENTER, true);

        // Menu Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Menu");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMenuName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Menu Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMenuCode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
