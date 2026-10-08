package dataman.utility.walkThroughMaster.client;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class DefaultDetailGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtValue = new DmTextBoxNumeric(0, false, 8, false);

    //<editor-fold defaultstate="collapsed" desc="txtHUserName">
    interface GridUser_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

    }
    final DmTextHelp<KeyFieldExtended, GridUser_PrpAcs> txtHUserName = new DmTextHelp(GWT.create(GridUser_PrpAcs.class), 6, false);

    private void iniTxtHUser() {
        txtHUserName.getDmGrid().addTextColumn(txtHUserName.getDmGrid().getPropertyAccess().name(), "User", 100, "name");
        txtHUserName.getDmGrid().initializeGrid(txtHUserName.getDmGrid().getPropertyAccess().fieldHashCode(), 245, 0, 1);
        txtHUserName.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHVType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, false);

    public void iniTxtHVType() {
        txtHVType.getDmGrid().addTextColumn(txtHVType.getDmGrid().getPropertyAccess().name(), "Nature", 0, "Name");
        txtHVType.getDmGrid().initializeGrid(txtHVType.getDmGrid().getPropertyAccess().code(), 245, 0, 1);
        txtHVType.getHelper().addSort("name", DmSortType.ASC);

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHSiteName">
    interface GridSite_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

    }

    final DmTextHelp<KeyFieldExtended, GridSite_PrpAcs> txtHSiteName = new DmTextHelp(GWT.create(GridSite_PrpAcs.class), 5, false);

    public void iniTxtHSite() {
        txtHSiteName.getDmGrid().addTextColumn(txtHSiteName.getDmGrid().getPropertyAccess().name(), "Site Name", 0, "Name");
        txtHSiteName.getDmGrid().initializeGrid(txtHSiteName.getDmGrid().getPropertyAccess().fieldHashCode(), 245, 0, 1);
        txtHSiteName.getHelper().addSort("name", DmSortType.ASC);

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    interface GridDepartment_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

    }
    final DmTextHelp<KeyFieldExtended, GridDepartment_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(GridDepartment_PrpAcs.class), 6, false);

    public void iniTxtHServiceType() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Department", 0, "Name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().fieldHashCode(), 245, 0, 1);
        txtHDepartment.getHelper().addSort("name", DmSortType.ASC);

    }
    //</editor-fold>

    DefaultDetailGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHUser();
        iniTxtHVType();
        iniTxtHSite();
        iniTxtHServiceType();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "100px", null, null, DmPosition.CENTER, true);

        //============================== Upper Area ==================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "100px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "100px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(upperArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //============================== END ========================================

        return flwTable;
    }

    private FlowPanel upperArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "100px", null, null, DmPosition.CENTER, true);

        //================================ 1st Row ====================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("User Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHUserName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //============================== END =========================================
        //================================ 2nd Row ===================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //============================== END =========================================
        //================================ 3rd Row ===================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSiteName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //============================== END =========================================

        //================================ 4th Row ===================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //============================== END =========================================

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Value Upto");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtValue));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
