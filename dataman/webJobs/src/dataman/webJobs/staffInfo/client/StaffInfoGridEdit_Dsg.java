package dataman.webJobs.staffInfo.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
 
public abstract class StaffInfoGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    //<editor-fold defaultstate="collapsed" desc="txtHSubCode">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSubCode = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHSubCode() {
        txtHSubCode.getDmGrid().addTextColumn(txtHSubCode.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHSubCode.getDmGrid().initializeGrid(txtHSubCode.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHSubCode.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public StaffInfoGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHSubCode();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "40px", null, null, DmPosition.CENTER, true);

        //Description
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Staff");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSubCode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
