package aarogya.master.prescriptionGuiMapping.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;

public abstract class PrescriptionGuiMappingGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHGUITemplateName">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGUITempName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private Grid iniTxtHGUITemplateName() {
        txtHGUITempName.getDmGrid().addTextColumn(txtHGUITempName.getDmGrid().getPropertyAccess().name(), "GUI Template", 0, "name");
        txtHGUITempName.getDmGrid().initializeGrid(txtHGUITempName.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHGUITempName.getHelper().addSort("name", DmSortType.ASC);
        return txtHGUITempName.getDmGrid().getGrid();
    }
    //</editor-fold>

    public PrescriptionGuiMappingGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "96%", "50px", null, null, DmPosition.CENTER, true);
        iniTxtHGUITemplateName();

        // GUI Template Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GUI Template");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGUITempName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
