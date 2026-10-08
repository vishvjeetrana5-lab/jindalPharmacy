package dataman.utility.voucherType.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class ReportConfigGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtReportName = new DmTextBox(100, true);

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private Grid iniTxtHSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 250, 200, 1);
        txtHSite.getHelper().addSort("name", DmSortType.ASC);
        return txtHSite.getDmGrid().getGrid();

    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPageSize = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true, false);

    private Grid iniTxtHPageSize() {
        txtHPageSize.getDmGrid().addTextColumn(txtHPageSize.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHPageSize.getDmGrid().initializeGrid(txtHPageSize.getDmGrid().getPropertyAccess().code(), 250, 200, 1);
        txtHPageSize.getHelper().addSort("name", DmSortType.ASC);
        return txtHPageSize.getDmGrid().getGrid();

    }

    public ReportConfigGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHSite();
        iniTxtHPageSize();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "70px", null, null, DmPosition.CENTER, true);

        //Name
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "70%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSite));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //Report name
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Report Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "70%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtReportName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        // paper size
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Page Size");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "70%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHPageSize));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTable.add(flwTR);

        return flwTable;
    }

}
