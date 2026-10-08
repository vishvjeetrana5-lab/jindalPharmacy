package aarogya.pharmacy.customerMaster.client;

import aarogya.pharmacy.customerMaster.shared.TxtHManufacturer_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class ManufacturerGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHManufacturer">
    interface TxtHManfacturer extends KeyField_T_PrpAcs<TxtHManufacturer_Mdl> {

        @Editor.Path("CityName")
        ValueProvider<TxtHManufacturer_Mdl, String> cityName();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHManufacturer_Mdl, String> manualCode();
    }
    final DmTextHelp<TxtHManufacturer_Mdl, TxtHManfacturer> txtHManufacturer = new DmTextHelp(GWT.create(TxtHManfacturer.class), 4, true);

    private void iniTxtHManufacturer() {
        txtHManufacturer.getDmGrid().addTextColumn(txtHManufacturer.getDmGrid().getPropertyAccess().name(), "Manufacturer", 0, "Name");
        txtHManufacturer.getDmGrid().addTextColumn(txtHManufacturer.getDmGrid().getPropertyAccess().cityName(), "City", 150, "CityName");
        txtHManufacturer.getDmGrid().addTextColumn(txtHManufacturer.getDmGrid().getPropertyAccess().manualCode(), "Code", 150, "ManualCode");
        txtHManufacturer.getDmGrid().initializeGrid(txtHManufacturer.getDmGrid().getPropertyAccess().code(), 500, 0, 1);
        txtHManufacturer.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    public ManufacturerGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHManufacturer();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "20px", null, null, DmPosition.CENTER, true);

        //Manufacturer
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Manufacturer");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHManufacturer));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);

        return flwTable;
    }
}
