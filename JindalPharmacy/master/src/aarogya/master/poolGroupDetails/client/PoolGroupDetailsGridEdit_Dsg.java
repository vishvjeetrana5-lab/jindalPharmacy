package aarogya.master.poolGroupDetails.client;

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
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;

public abstract class PoolGroupDetailsGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmUserControl userControl = new DmUserControl();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
   

    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, false, 10, true);

    public PoolGroupDetailsGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        ini();
        iniTxtHHeaderRefType();
        iniTxtHCname();

    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

    }

    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHConsultantName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    public void iniTxtHCname() {
        txtHConsultantName.getDmGrid().addTextColumn(txtHConsultantName.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHConsultantName.getDmGrid().initializeGrid(txtHConsultantName.getDmGrid().getPropertyAccess().code(), 380, 0, 1);

        txtHConsultantName.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //<editor-fold defaultstate="collapsed" desc="txtHHeaderRefType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHeaderRefType = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHHeaderRefType() {
        txtHHeaderRefType.getDmGrid().addTextColumn(txtHHeaderRefType.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHHeaderRefType.getDmGrid().initializeGrid(txtHHeaderRefType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHHeaderRefType.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
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
        lbl = new DmLabel("Consultant Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "97%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtHConsultantName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //===============================< 2nd Row >===============================//
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("%/Fix");
        flwTD.add(lbl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "97%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtHHeaderRefType));
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

        return flwTable;
    }

}
