package aarogya.inventory.gstServiceMaster.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class ItsRateSlabGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtRateFrom = new DmTextBoxNumeric(2, false, 8, true);
    final DmTextBoxNumeric txtRateUpto = new DmTextBoxNumeric(2, false, 8, true);

    //<editor-fold defaultstate="collapsed" desc="txtHGstType">pe
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGstType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, true);

    private void iniTxtHGstType() {
        txtHGstType.getDmGrid().addTextColumn(txtHGstType.getDmGrid().getPropertyAccess().name(), "Type", 120, "name");
        txtHGstType.getDmGrid().initializeGrid(txtHGstType.getDmGrid().getPropertyAccess().code(), 120, 0, 1);

        txtHGstType.getHelper().addSort("printSrNo", DmSortType.ASC);
        txtHGstType.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    public ItsRateSlabGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        iniTxtHGstType();
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "96%", "38px", null, null, DmPosition.CENTER, true);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "22px", "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate From");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "22%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtRateFrom));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "21%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate Upto");
        lbl.getElement().getStyle().setMarginLeft(13, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "22%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtRateUpto));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //============================================ 3rd Row ===========================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GST Type");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHGstType));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        //============================== END ================================================

        return flwTable;
    }

}
