package dataman.utility.voucherType.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.voucherType.shared.*;

public abstract class FormAppliedGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtLC = new DmTextBox();
    final DmTextBox txtTRN = new DmTextBox();

    //<editor-fold defaultstate="collapsed" desc="txtHFormApplied">
    interface Grid_FormApplied extends KeyField_T_PrpAcs<TxtHFormApplied_Mdl> {

        @Editor.Path("Location")
        ValueProvider<TxtHFormApplied_Mdl, String> location();

        @Editor.Path("AppliedFor")
        ValueProvider<TxtHFormApplied_Mdl, String> appliedFor();

    }

    final DmTextHelp<TxtHFormApplied_Mdl, Grid_FormApplied> txtHFormApplied = new DmTextHelp(GWT.create(Grid_FormApplied.class), 10);

    public Grid fGrid_FormApplied() {
        txtHFormApplied.getDmGrid().addTextColumn(txtHFormApplied.getDmGrid().getPropertyAccess().name(), "Name", 120, "Name");
        txtHFormApplied.getDmGrid().addTextColumn(txtHFormApplied.getDmGrid().getPropertyAccess().location(), "Location", 100, "L_C");
        txtHFormApplied.getDmGrid().addTextColumn(txtHFormApplied.getDmGrid().getPropertyAccess().appliedFor(), "Applied For", 140, "TrnType");
        txtHFormApplied.getDmGrid().initializeGrid(txtHFormApplied.getDmGrid().getPropertyAccess().code(), 385, 200, 1);
        txtHFormApplied.getHelper().addSort("Name", DmSortType.ASC);
        txtHFormApplied.getHelper().addSort("L_C", DmSortType.ASC);
        txtHFormApplied.getHelper().addSort("TrnType", DmSortType.ASC);
        return txtHFormApplied.getDmGrid().getGrid();

    }

//</editor-fold>
    public FormAppliedGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        fGrid_FormApplied();
        container.add(section1());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel section1() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "20px", null, null, DmPosition.CENTER, true);

        //Name
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHFormApplied));
        txtHFormApplied.setMandatory(true);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

}
