package aarogya.master.managePatientSchedule.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class ManagePatientScheduleGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtCycleTime = new DmTextBoxNumeric(0, false, 4, true);
    final DmTextBox txtDescription = new DmTextBox(250, true);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(50, false);
    final DmTextBox txtScheduleDate = new DmTextBox(11, true);

    final DmTextBoxNumeric txtPrintSNo = new DmTextBoxNumeric(0, false, 2, false);
    final Button btnShuffleDate = new Button("Re-Schedule");
    final FlowPanel flwShuffleDate = new FlowPanel();

    public ManagePatientScheduleGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        iniGuiTemplate();
        iniMachinePlaces();
        iniTxtHConsultant();
    }

    //<editor-fold defaultstate="collapsed" desc="txtHGuiTemplate">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGuiTemplate = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniGuiTemplate() {
        txtHGuiTemplate.getDmGrid().addTextColumn(txtHGuiTemplate.getDmGrid().getPropertyAccess().name(), "Gui Template", 0, "name");
        txtHGuiTemplate.getDmGrid().initializeGrid(txtHGuiTemplate.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHGuiTemplate.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMachinePlaces">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMachinePlaces = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    public void iniMachinePlaces() {
        txtHMachinePlaces.getDmGrid().addTextColumn(txtHMachinePlaces.getDmGrid().getPropertyAccess().name(), "Machine & Places", 1, "Name");
        txtHMachinePlaces.getDmGrid().initializeGrid(txtHMachinePlaces.getDmGrid().getPropertyAccess().code(), 350, 0, 1);

        txtHMachinePlaces.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHConsultant">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHConsultant = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHConsultant() {
        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().name(), "Consultant", 0, "Name");
        txtHConsultant.getDmGrid().initializeGrid(txtHConsultant.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHConsultant.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "200px", null, null, DmPosition.CENTER, true);


        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Procedure");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDescription));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Schedule Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtScheduleDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();

        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        lbl = new DmLabel("Cycle Time (Minutes)");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCycleTime));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Print S.No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();

        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPrintSNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gui Template");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGuiTemplate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Machine & Places");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();

        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMachinePlaces));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consultant");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHConsultant));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "right", null, DmPosition.NONE, false);
        btnShuffleDate.addStyleName("dm-button-grad");
        btnShuffleDate.setTitle("Re-Schedule");
        btnShuffleDate.getElement().getStyle().setMarginLeft(9, Style.Unit.PX);
        flwShuffleDate.add(btnShuffleDate);
        flwTd.add(flwShuffleDate);

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
}
