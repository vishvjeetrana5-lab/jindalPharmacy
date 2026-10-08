package aarogya.inventory.itemGroupMaster.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class DepartmentConcumptionAcGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    public DepartmentConcumptionAcGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHDepartment();
        iniTxtHConsumptionAc();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    //<editor-fold defaultstate="collapsed" desc="txtHRestrictedDepartment">
    interface GridRestrictedDepartment_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

    }
    final DmTextHelp<KeyFieldExtended, GridRestrictedDepartment_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(GridRestrictedDepartment_PrpAcs.class), 6, true);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Department", 190, "name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().fieldHashCode(), 200, 0, 1);
        txtHDepartment.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>    
    
    //<editor-fold defaultstate="collapsed" desc="txtHRestrictedDepartment">
    interface GridConsumptionAc_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

    }
    final DmTextHelp<KeyFieldExtended, GridConsumptionAc_PrpAcs> txtHConsumptionAc = new DmTextHelp(GWT.create(GridConsumptionAc_PrpAcs.class), 6, true);

    private void iniTxtHConsumptionAc() {
        txtHConsumptionAc.getDmGrid().addTextColumn(txtHConsumptionAc.getDmGrid().getPropertyAccess().name(), "Consumption A/c", 190, "name");
        txtHConsumptionAc.getDmGrid().initializeGrid(txtHConsumptionAc.getDmGrid().getPropertyAccess().fieldHashCode(), 200, 0, 1);
        txtHConsumptionAc.getHelper().addSort("name", DmEnum.DmSortType.ASC);
}
    //</editor-fold>    
    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "44px", null, null, DmEnum.DmPosition.CENTER, true);

        //////////////////////////// 1st Row ////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "43%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "43%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Consumption A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHConsumptionAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        return flwTable;
    }
}