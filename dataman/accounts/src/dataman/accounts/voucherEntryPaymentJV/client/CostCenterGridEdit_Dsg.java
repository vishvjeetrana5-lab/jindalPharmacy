package dataman.accounts.voucherEntryPaymentJV.client;

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
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.voucherEntryPaymentJV.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.DmEnum.*;

import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class CostCenterGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHCostCenterGrid">
    interface TxtHCostCenterGrid extends KeyField_T_PrpAcs<TxtHCostCenterGrid_Mdl> {

        @Editor.Path("SCode")
        ValueProvider<TxtHCostCenterGrid_Mdl, String> sCode();

        @Editor.Path("SiteCode")
        ValueProvider<TxtHCostCenterGrid_Mdl, String> siteCode();
    }
    final DmTextHelp<TxtHCostCenterGrid_Mdl, TxtHCostCenterGrid> txtHCostCenterGrid = new DmTextHelp(GWT.create(TxtHCostCenterGrid.class), 7);

    public void iniTxtHCostCenterGrid() {
        txtHCostCenterGrid.getDmGrid().addTextColumn(txtHCostCenterGrid.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHCostCenterGrid.getDmGrid().addTextColumn(txtHCostCenterGrid.getDmGrid().getPropertyAccess().sCode(), "Code", 100, "ManualCode");
        txtHCostCenterGrid.getDmGrid().addTextColumn(txtHCostCenterGrid.getDmGrid().getPropertyAccess().siteCode(), "Site", 100, "Site_Code");
        txtHCostCenterGrid.getDmGrid().initializeGrid(txtHCostCenterGrid.getDmGrid().getPropertyAccess().code(), 400, 200, 1);
        txtHCostCenterGrid.getDmGrid().getGrid().setHeight(200);
        txtHCostCenterGrid.getHelper().addSort("Name", DmSortType.ASC);
        txtHCostCenterGrid.getHelper().addSort("ManualCode", DmSortType.ASC);
    }
//</editor-fold>

    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 15, true);

    public CostCenterGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHCostCenterGrid();
        container.add(section1());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel section1() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "40px", null, null, DmPosition.CENTER, true);

        //Cost Center
        flwTR = new FlowPanel();
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Cost Center");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "66%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHCostCenterGrid));
        txtHCostCenterGrid.setMandatory(true);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //Amount
        flwTR = new FlowPanel();
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "66%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtAmount));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

}
