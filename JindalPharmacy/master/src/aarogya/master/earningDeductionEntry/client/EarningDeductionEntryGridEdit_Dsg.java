package aarogya.master.earningDeductionEntry.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;

public abstract class EarningDeductionEntryGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmUserControl userControl = new DmUserControl();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
//    final DmTextBox txtHHNature = new DmTextBox(4, false);

    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, false, 10, true);

    public EarningDeductionEntryGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        ini();
        iniTxtHHeaderRefType();
        iniTxtHNature();
        iniTxtHType();

    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

    }

    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    public void iniTxtHNature() {
        txtHNature.getDmGrid().addTextColumn(txtHNature.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHNature.getDmGrid().initializeGrid(txtHNature.getDmGrid().getPropertyAccess().code(), 380, 0, 1);

        txtHNature.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //<editor-fold defaultstate="collapsed" desc="txtHDescription">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDescription = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHHeaderRefType() {
        txtHDescription.getDmGrid().addTextColumn(txtHDescription.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHDescription.getDmGrid().initializeGrid(txtHDescription.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHDescription.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHType = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHType() {
        txtHType.getDmGrid().addTextColumn(txtHType.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHType.getDmGrid().initializeGrid(txtHType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHType.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        //===============================< 1st Row >===============================//
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Type");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "97%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtHNature));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //===============================< 2nd Row >===============================//
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Description");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "97%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtHDescription));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //===============================< 3rd Row >===============================//
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtAmount));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        //===============================< 3rd Row >===============================//
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Nature");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtHType));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

}
