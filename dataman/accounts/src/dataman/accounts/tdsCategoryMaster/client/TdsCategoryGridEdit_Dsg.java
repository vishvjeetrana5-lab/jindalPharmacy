package dataman.accounts.tdsCategoryMaster.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.tdsCategoryMaster.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class TdsCategoryGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtPercent = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.NONE, false, 6, true);
    final DmTextBox txtCalculateOn = new DmTextBox(100, false);
    final DmTextBox txtManualCode = new DmTextBox(10, false);
    final DmTextBoxNumeric txtFromAmt = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.NONE, false, 12, false);
    final DmTextBoxNumeric txtToAmt = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.NONE, false, 12, false);
    final DmTextBoxNumeric txtPenaltyPercentage = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.NONE, false, 6, true);

    //<editor-fold defaultstate="collapsed" desc="txtHDescription">
    interface TxtHDescription_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> sharedModifiedBy();
    }
    final DmTextHelp<KeyFieldExtended, TxtHDescription_PrpAcs> txtHDescription = new DmTextHelp(GWT.create(TxtHDescription_PrpAcs.class), 7, true);

    public void iniDescription() {

        txtHDescription.getDmGrid().addTextColumn(txtHDescription.getDmGrid().getPropertyAccess().name(), "Description", 0, "Name");
        txtHDescription.getDmGrid().addTextColumn(txtHDescription.getDmGrid().getPropertyAccess().sharedModifiedBy(), "Manual Code", 100, "manualCode");
        txtHDescription.getDmGrid().initializeGrid(txtHDescription.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDescription.getHelper().addSort("Name", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPostingAC">
    interface TxtHPostingAC_PrpAcs extends KeyField_T_PrpAcs<TxtHPostingAC_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHPostingAC_Mdl, String> manualCode();
    }

    final DmTextHelp<TxtHPostingAC_Mdl, TxtHPostingAC_PrpAcs> txtHPostingAC = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHPostingAC_PrpAcs.class), 7, true);

    public void iniPostingAC() {
        txtHPostingAC.getDmGrid().addTextColumn(txtHPostingAC.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPostingAC.getDmGrid().addTextColumn(txtHPostingAC.getDmGrid().getPropertyAccess().manualCode(), "Code", 50, "ManualCode");
        txtHPostingAC.getDmGrid().initializeGrid(txtHPostingAC.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
        txtHPostingAC.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    public TdsCategoryGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniDescription();
        iniPostingAC();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "130px", null, null, DmPosition.CENTER, true);

        //Description
        flwTR = new FlowPanel();
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Description");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "66%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHDescription));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Manual Code");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtManualCode));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //Posting Account
        flwTR = new FlowPanel();
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Posting A/c");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "66%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHPostingAC));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //txtPercent
        flwTR = new FlowPanel();

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("From Amt.");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtFromAmt));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To Amt.");
        lbl.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "26%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtToAmt));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //===================================<Percent>===============================
        flwTR = new FlowPanel();
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Percentage %");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPercent));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Penality %");
        lbl.getElement().getStyle().setMarginLeft(30, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "26%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtPenaltyPercentage));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //Description      
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Calculate On");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "66%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtCalculateOn));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }
}
