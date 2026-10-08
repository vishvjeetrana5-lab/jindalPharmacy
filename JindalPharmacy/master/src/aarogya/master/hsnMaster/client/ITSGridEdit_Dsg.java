package aarogya.master.hsnMaster.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class ITSGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
//    final DmTextBoxNumeric txtRateFrom = new DmTextBoxNumeric(0, false, 6, true);
    final DmTextBox txtCodeSNO = new DmTextBox(10, true);
    //<editor-fold defaultstate="collapsed" desc="txtHITS">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHITS = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHITSOnSale() {
        txtHITS.getDmGrid().addTextColumn(txtHITS.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHITS.getDmGrid().initializeGrid(txtHITS.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHITS.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAccName">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAccName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHPurchaseAC() {
        txtHAccName.getDmGrid().addTextColumn(txtHAccName.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHAccName.getDmGrid().initializeGrid(txtHAccName.getDmGrid().getPropertyAccess().code(), 180, 0, 1);

        txtHAccName.getHelper().addSort("NAME", DmSortType.ASC);
    }

    //</editor-fold>
    public ITSGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        iniTxtHITSOnSale();
        iniTxtHPurchaseAC();
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "96%", "40px", null, null, DmPosition.CENTER, true);

        ////////////////////////////////////////////////////1st///////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ITS");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "72%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHITS));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Account Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "72%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAccName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
