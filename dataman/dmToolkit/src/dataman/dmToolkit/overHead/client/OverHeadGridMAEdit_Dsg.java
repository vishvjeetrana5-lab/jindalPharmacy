package dataman.dmToolkit.overHead.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.overHead.shared.AccountHelp_Mdl;

public abstract class OverHeadGridMAEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.NONE, true, 15, true);
    DmLabel postingAcLbl = new DmLabel("Posting A/c");

    //<editor-fold defaultstate="collapsed" desc="txtHPostingAC">
    interface TxtHPostingAC_PrpAcs extends KeyField_T_PrpAcs<AccountHelp_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<AccountHelp_Mdl, String> manualCode();
    }

    final DmTextHelp<AccountHelp_Mdl, TxtHPostingAC_PrpAcs> txtHPostingAC = new DmTextHelp(GWT.create(TxtHPostingAC_PrpAcs.class), 7, true);

    public void iniPostingAC() {
        txtHPostingAC.getDmGrid().addTextColumn(txtHPostingAC.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHPostingAC.getDmGrid().addTextColumn(txtHPostingAC.getDmGrid().getPropertyAccess().manualCode(), "Code", 50, "ManualCode");
        txtHPostingAC.getDmGrid().initializeGrid(txtHPostingAC.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHPostingAC.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public OverHeadGridMAEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniPostingAC();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "40px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(postingAcLbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPostingAC));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
