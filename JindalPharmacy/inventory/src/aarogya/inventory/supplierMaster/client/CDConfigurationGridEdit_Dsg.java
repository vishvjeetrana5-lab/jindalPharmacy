package aarogya.inventory.supplierMaster.client;

import com.google.gwt.core.shared.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class CDConfigurationGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final DmTextBoxNumeric txtDiscPer = new DmTextBoxNumeric(4, false, 12, true);
    final DmTextBox txtApplicableFrom = new DmTextBox(11, true);

    //<editor-fold defaultstate="collapsed" desc="txtHMedicineCompany">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMedicineCompany = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    private void iniTxtHMedicineCompany() {
        txtHMedicineCompany.getDmGrid().addTextColumn(txtHMedicineCompany.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHMedicineCompany.getDmGrid().initializeGrid(txtHMedicineCompany.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
        txtHMedicineCompany.getHelper().addSort("NAME", DmSortType.ASC);
    }

    //</editor-fold>
    public CDConfigurationGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHMedicineCompany();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "70px", null, null, DmPosition.CENTER, true);

        //==================================<<1st Row>>=============================================
        //<editor-fold defaultstate="collapsed" desc="Medicine Company">
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Company");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicineCompany));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //</editor-fold>
        //==================================<<*****>>=============================================

        //==================================<<2nd Row>>=============================================
        //<editor-fold defaultstate="collapsed" desc="Discount">
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Discount Per.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiscPer));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //</editor-fold>
        //==================================<<*****>>=============================================

        //==================================<<3rd Row>>=============================================
        //<editor-fold defaultstate="collapsed" desc="Applicable From">
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Applicable From");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtApplicableFrom));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //</editor-fold>
        //==================================<<*****>>=============================================
        return flwTable;
    }
}
