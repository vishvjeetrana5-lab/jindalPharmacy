package aarogya.master.environmentService.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class GeneralTabGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final DmTextBox txtServiceCategory = new DmTextBox(100, false);

    //<editor-fold defaultstate="collapsed" desc="txtHDefConsultant">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDefConsultant = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHDefConsultant() {
        txtHDefConsultant.getDmGrid().addTextColumn(txtHDefConsultant.getDmGrid().getPropertyAccess().name(), "Consultant", 100, "name");

        txtHDefConsultant.getDmGrid().initializeGrid(txtHDefConsultant.getDmGrid().getPropertyAccess().code(), 210, 0, 1);
        txtHDefConsultant.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDefConsultant">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDefDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHDefDepartment() {
        txtHDefDepartment.getDmGrid().addTextColumn(txtHDefDepartment.getDmGrid().getPropertyAccess().name(), "Department", 100, "name");

        txtHDefDepartment.getDmGrid().initializeGrid(txtHDefDepartment.getDmGrid().getPropertyAccess().code(), 210, 0, 1);
        txtHDefDepartment.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHLinkedSeriveAC">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHLinkedSeriveAC = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHLinkedSeriveAC() {
        txtHLinkedSeriveAC.getDmGrid().addTextColumn(txtHLinkedSeriveAC.getDmGrid().getPropertyAccess().name(), "Account", 100, "name");

        txtHLinkedSeriveAC.getDmGrid().initializeGrid(txtHLinkedSeriveAC.getDmGrid().getPropertyAccess().code(), 210, 0, 1);
        txtHLinkedSeriveAC.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public GeneralTabGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");

        iniTxtHDefConsultant();
        iniTxtHDefDepartment();

        iniTxtHLinkedSeriveAC();

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "83px", null, null, DmEnum.DmPosition.CENTER, true);

        //Description
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Service Category");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtServiceCategory));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);

        //Address
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Default Consultant");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDefConsultant));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Default Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDefDepartment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Link Service");
        //lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLinkedSeriveAC));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
