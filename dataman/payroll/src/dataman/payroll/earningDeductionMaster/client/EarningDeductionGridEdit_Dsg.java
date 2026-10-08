package dataman.payroll.earningDeductionMaster.client;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.payroll.earningDeductionMaster.shared.GrdEarnDed_Mdl;

public abstract class EarningDeductionGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHRestrictedDepartment">
    interface GridRestrictedDepartment_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdEarnDed_Mdl> {
        @Editor.Path("ShortName")
        ValueProvider<GrdEarnDed_Mdl, String> shortName();

    }
    final DmTextHelp<GrdEarnDed_Mdl, GridRestrictedDepartment_PrpAcs> txtHEarnDed = new DmTextHelp(GWT.create(GridRestrictedDepartment_PrpAcs.class), 6, true);

    private void iniTxtHRestrictedDepartment() {
        txtHEarnDed.getDmGrid().addTextColumn(txtHEarnDed.getDmGrid().getPropertyAccess().name(), "Earning/Deduction", 170, "name");
        txtHEarnDed.getDmGrid().addTextColumn(txtHEarnDed.getDmGrid().getPropertyAccess().shortName(), "Short Name",30, "shortName");
        txtHEarnDed.getDmGrid().initializeGrid(txtHEarnDed.getDmGrid().getPropertyAccess().fieldHashCode(), 200, 0, 1);
        txtHEarnDed.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    public EarningDeductionGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
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
        lbl = new DmLabel("Earning/Deduction");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEarnDed));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
