package aarogya.inventory.itemMaster.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class QcParameterGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHQCParameter">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHQCParameter = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 7, true, true);

    private void iniTxtHApprovedVendors() {
        txtHQCParameter.getDmGrid().addTextColumn(txtHQCParameter.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHQCParameter.getDmGrid().initializeGrid(txtHQCParameter.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHQCParameter.getHelper().addSort("name", DmSortType.ASC);
    }

//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHQCParameter">
//    interface GridRestrictedDepartment_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {
//
//    }
//    final DmTextHelp<KeyFieldExtended, GridRestrictedDepartment_PrpAcs> txtHQCParameter = new DmTextHelp(GWT.create(GridRestrictedDepartment_PrpAcs.class), 6, true);
//
//    private void iniTxtHApprovedVendors() {
//        txtHQCParameter.getDmGrid().addTextColumn(txtHQCParameter.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
//        txtHQCParameter.getDmGrid().initializeGrid(txtHQCParameter.getDmGrid().getPropertyAccess().fieldHashCode(), 150, 0, 1);
//        txtHQCParameter.getHelper().addSort("name", DmEnum.DmSortType.ASC);
//
//    }
    //</editor-fold>
    
    
    
    
    
    
    
    public QcParameterGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHApprovedVendors();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "22px", null, null, DmPosition.CENTER, true);

        //////////////////////////// 1st Row ////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("QC Parameter");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHQCParameter));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
