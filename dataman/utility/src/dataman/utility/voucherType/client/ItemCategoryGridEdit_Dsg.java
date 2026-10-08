package dataman.utility.voucherType.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;

import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class ItemCategoryGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHItemCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHItemCategory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    public void initxtHItemCategory() {
        txtHItemCategory.getDmGrid().addTextColumn(txtHItemCategory.getDmGrid().getPropertyAccess().name(), "Category", 0, "name");
        txtHItemCategory.getDmGrid().initializeGrid(txtHItemCategory.getDmGrid().getPropertyAccess().code(), 200, 0, 1); //244
        txtHItemCategory.getHelper().addSort("name", DmSortType.ASC);
    }
//</editor-fold>

    public ItemCategoryGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        initxtHItemCategory();
        container.add(section1());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel section1() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "20px", null, null, DmPosition.CENTER, true);

        //Nature
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Category");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHItemCategory));
        txtHItemCategory.setMandatory(true);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

}
