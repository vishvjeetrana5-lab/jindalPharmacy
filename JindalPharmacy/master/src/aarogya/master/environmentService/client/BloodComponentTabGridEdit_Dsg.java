package aarogya.master.environmentService.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class BloodComponentTabGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final DmTextBox txtBloodComponent = new DmTextBox(100, false);

    //<editor-fold defaultstate="collapsed" desc="txtHLinkedSeriveAC">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHLinkedSeriveAC = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHLinkedSeriveAC() {
        txtHLinkedSeriveAC.getDmGrid().addTextColumn(txtHLinkedSeriveAC.getDmGrid().getPropertyAccess().name(), "Account", 100, "name");

        txtHLinkedSeriveAC.getDmGrid().initializeGrid(txtHLinkedSeriveAC.getDmGrid().getPropertyAccess().code(), 210, 0, 1);
        txtHLinkedSeriveAC.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public BloodComponentTabGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");

        iniTxtHLinkedSeriveAC();

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "42px", null, null, DmEnum.DmPosition.CENTER, true);

        //Description
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Blood Component");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBloodComponent));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Link Service");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLinkedSeriveAC));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
