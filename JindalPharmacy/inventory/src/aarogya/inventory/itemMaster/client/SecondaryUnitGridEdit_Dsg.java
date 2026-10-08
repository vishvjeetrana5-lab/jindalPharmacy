package aarogya.inventory.itemMaster.client;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class SecondaryUnitGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtConversionRate = new DmTextBoxNumeric(0, false, 4, true);
    //<editor-fold defaultstate="collapsed" desc="TxtHSecondryUnitSKU">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSecondryUnitSKU = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 4, true);

    private void initxtHSecondryUnitSKU() {
        txtHSecondryUnitSKU.getDmGrid().addTextColumn(txtHSecondryUnitSKU.getDmGrid().getPropertyAccess().name(), "Name", 0, "unit");
        txtHSecondryUnitSKU.getDmGrid().initializeGrid(txtHSecondryUnitSKU.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHSecondryUnitSKU.getHelper().addSort("unit", DmSortType.ASC);
    }

    //</editor-fold>
    public SecondaryUnitGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        initxtHSecondryUnitSKU();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "40px", null, null, DmPosition.CENTER, true);

        //////////////////////////// 1st Row ////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Secondry Unit");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSecondryUnitSKU));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //////////////////////////// 2nd Row ////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Conversion Rate");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtConversionRate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
