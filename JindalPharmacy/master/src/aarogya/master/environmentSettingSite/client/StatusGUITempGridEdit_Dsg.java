package aarogya.master.environmentSettingSite.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class StatusGUITempGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final DmTextBox txtStatus = new DmTextBox(35, false);

    //<editor-fold defaultstate="collapsed" desc="txtHGuiTemplet">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGuiTemplet = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHGuiTemplet() {
        txtHGuiTemplet.getDmGrid().addTextColumn(txtHGuiTemplet.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHGuiTemplet.getDmGrid().initializeGrid(txtHGuiTemplet.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHGuiTemplet.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public StatusGUITempGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHGuiTemplet();
        container.setStyleName("dm-FlwWorkingArea");
        container.add(workingArea());
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "60px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Status");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtStatus));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GUI Template");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGuiTemplet));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
}
