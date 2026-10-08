package dataman.accounts.finalPaymentEntry.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.finalPaymentEntry.shared.TxtHOtherAC_Mdl;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class OtherAcGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);
    final DmRemarkTextBox txtRemak = new DmRemarkTextBox(200, false);

    //<editor-fold defaultstate="collapsed" desc="TxtHOtherAC_Mdl">
    interface TxtHPostingAC_PrpAcs extends KeyField_T_PrpAcs<TxtHOtherAC_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHOtherAC_Mdl, String> manualCode();
    }

    final DmTextHelp<TxtHOtherAC_Mdl, TxtHPostingAC_PrpAcs> txtHOtherAC = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHPostingAC_PrpAcs.class), 7, true);

    public void iniOtherAC() {
        txtHOtherAC.getDmGrid().addTextColumn(txtHOtherAC.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHOtherAC.getDmGrid().addTextColumn(txtHOtherAC.getDmGrid().getPropertyAccess().manualCode(), "Code", 50, "ManualCode");
        txtHOtherAC.getDmGrid().initializeGrid(txtHOtherAC.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHOtherAC.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    public OtherAcGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniOtherAC();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "60px", null, null, DmPosition.CENTER, true);

        //Description
        flwTR = new FlowPanel();
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Other A/c");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "80%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHOtherAC));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtAmount));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "40%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtRemak));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }
}
