package dataman.utility.userPermission.client;

import dataman.utility.userPermission.shared.GrdCostCenter_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class CostCenterGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtShortName = new DmTextBox();

    //<editor-fold defaultstate="collapsed" desc="txtHCostCenterGrid">
    interface txtHCostCenterData_PrpAcs extends KeyField_T_PrpAcs<GrdCostCenter_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<GrdCostCenter_Mdl, String> ShortName();
    }

    final DmTextHelp<GrdCostCenter_Mdl, txtHCostCenterData_PrpAcs> txtHCostCenter = new DmTextHelp(GWT.create(txtHCostCenterData_PrpAcs.class), 10, true);

    private void iniTxtHCostCenterGrid() {
        txtHCostCenter.getDmGrid().addTextColumn(txtHCostCenter.getDmGrid().getPropertyAccess().name(), "Name", 215, "Name");
        txtHCostCenter.getDmGrid().addTextColumn(txtHCostCenter.getDmGrid().getPropertyAccess().ShortName(), "Code", 80, "ManualCode");
        txtHCostCenter.getDmGrid().initializeGrid(txtHCostCenter.getDmGrid().getPropertyAccess().code(), 350, 310, 1);
        txtHCostCenter.getHelper().addSort("Name", DmSortType.ASC);
    }
//</editor-fold>

    public CostCenterGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(section1());
        container.setStyleName("dm-FlwWorkingArea");
        iniTxtHCostCenterGrid();
    }

    private FlowPanel section1() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "40px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Cost Center");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCostCenter));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        txtShortName.setEnabled(false);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
