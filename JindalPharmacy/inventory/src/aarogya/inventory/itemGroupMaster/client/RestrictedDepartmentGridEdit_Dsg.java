package aarogya.inventory.itemGroupMaster.client;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class RestrictedDepartmentGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHRestrictedDepartment">
    interface GridRestrictedDepartment_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

    }
    final DmTextHelp<KeyFieldExtended, GridRestrictedDepartment_PrpAcs> txtHRestrictedDepartment = new DmTextHelp(GWT.create(GridRestrictedDepartment_PrpAcs.class), 6, true);

    private void iniTxtHRestrictedDepartment() {
        txtHRestrictedDepartment.getDmGrid().addTextColumn(txtHRestrictedDepartment.getDmGrid().getPropertyAccess().name(), "Department", 190, "name");
        txtHRestrictedDepartment.getDmGrid().initializeGrid(txtHRestrictedDepartment.getDmGrid().getPropertyAccess().fieldHashCode(), 200, 0, 1);
        txtHRestrictedDepartment.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    public RestrictedDepartmentGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHRestrictedDepartment();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "22px", null, null, DmPosition.CENTER, true);

        //////////////////////////// 1st Row ////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "43%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Restricted Department/Godown");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRestrictedDepartment));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
