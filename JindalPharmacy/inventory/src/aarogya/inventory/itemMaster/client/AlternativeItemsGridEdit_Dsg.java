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
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import aarogya.inventory.itemMaster.shared.TxtHItemsName_Mdl;

public abstract class AlternativeItemsGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHAlternativeItems">
    interface TxtHItemsName_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHItemsName_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHItemsName_Mdl, String> manualCode();

    }
    final DmTextHelp<TxtHItemsName_Mdl, TxtHItemsName_PrpAcs> txtHAlternativeItems = new DmTextHelp(GWT.create(TxtHItemsName_PrpAcs.class), 6, true);

    private void iniTxtHAlternativeItems() {
        txtHAlternativeItems.getDmGrid().addTextColumn(txtHAlternativeItems.getDmGrid().getPropertyAccess().name(), "Item", 125, "name");
        txtHAlternativeItems.getDmGrid().addTextColumn(txtHAlternativeItems.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 75, "");
        txtHAlternativeItems.getDmGrid().initializeGrid(txtHAlternativeItems.getDmGrid().getPropertyAccess().fieldHashCode(), 250, 0, 1);
        txtHAlternativeItems.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    public AlternativeItemsGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHAlternativeItems();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "22px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "25px", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAlternativeItems));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
