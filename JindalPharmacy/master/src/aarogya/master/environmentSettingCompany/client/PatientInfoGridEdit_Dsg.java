package aarogya.master.environmentSettingCompany.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;

public abstract class PatientInfoGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtSrNo = new DmTextBoxNumeric(0, false, 2, true);
    final DmTextBox txtName = new DmTextBox(50, false);

    //<editor-fold defaultstate="collapsed" desc="txtHIsVisible">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsVisible = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, false);

    private void iniTxtHIsVisible() {
        txtHIsVisible.getDmGrid().addTextColumn(txtHIsVisible.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHIsVisible.getDmGrid().initializeGrid(txtHIsVisible.getDmGrid().getPropertyAccess().code(), 70, 0, 1);

        txtHIsVisible.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIdTypemandatory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsMandatory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, false);

    private void iniTxtHIsMandatory() {
        txtHIsMandatory.getDmGrid().addTextColumn(txtHIsMandatory.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHIsMandatory.getDmGrid().initializeGrid(txtHIsMandatory.getDmGrid().getPropertyAccess().code(), 70, 0, 1);

        txtHIsMandatory.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public PatientInfoGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHIsVisible();
        iniTxtHIsMandatory();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "96%", "85px", null, null, DmPosition.CENTER, true);

        ////////////////////////////////////////////////////1st///////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "72%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sr. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSrNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Is Visible");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsVisible));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Is Mandatory");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsMandatory));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
