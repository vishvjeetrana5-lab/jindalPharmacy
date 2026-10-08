package aarogya.pharmacy.medicineDepartmentInfo.client;

import aarogya.pharmacy.medicineDepartmentInfo.shared.TxtHMedicine_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class DepartmentInfoDetailsGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtMinLvl = new DmTextBoxNumeric(4, false, 10, false);
    final DmTextBoxNumeric txtMaxLvl = new DmTextBoxNumeric(4, false, 10, false);
    final DmTextBoxNumeric txtReOrder = new DmTextBoxNumeric(4, false, 10, false);
    final DmTextBox txtBin = new DmTextBox(20, false);
    final DmLabel lblDepartmentName = new DmLabel("");
    final DmTextBox txtUnit = new DmTextBox(50, false);

    interface TxtHMedicine_PrpAcs extends KeyField_T_PrpAcs<TxtHMedicine_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHMedicine_Mdl, String> manualCode();

        @Editor.Path("SkuName")
        ValueProvider<TxtHMedicine_Mdl, String> skuName();

        @Editor.Path("MedicineGroup")
        ValueProvider<TxtHMedicine_Mdl, String> medicineGroup();

        @Editor.Path("MedicineCategory")
        ValueProvider<TxtHMedicine_Mdl, String> medicineCategory();
    }

    final DmTextHelp<TxtHMedicine_Mdl, TxtHMedicine_PrpAcs> txtHMedicine = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHMedicine_PrpAcs.class), 10, true);

    public void iniTxtHMedicine() {
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().name(), "Medicine Name", 360, "medicineName");
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().skuName(), "SKU", 70, "skuName");
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().medicineGroup(), "Medicine Group", 200, "medicineGroup");
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().medicineCategory(), "Medicine Category", 100, "medicinecategory");
        txtHMedicine.getDmGrid().initializeGrid(txtHMedicine.getDmGrid().getPropertyAccess().code(), 850, 0, 1);
        txtHMedicine.getHelper().addSort("medicineName", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHSiteName">
    interface GridSite_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

    }

    public DepartmentInfoDetailsGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHMedicine();

        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "84px", null, null, DmPosition.CENTER, true);

        //============================== 1st Row ========================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicine));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("SKU");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);

        flwTd.add(vl.add(txtUnit));
        txtUnit.setEnabled(false);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //============================== END =========================================
        //================================ 2nd Row ===================================
        //============================== END =========================================
        //================================ 3rd Row ===================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Min. Level");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMinLvl));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Max. Level");
        lbl.getElement().getStyle().setMarginLeft(122, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMaxLvl));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //============================== END =========================================

        //================================ 4th Row ===================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Re-Order Level");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReOrder));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Bin");
        lbl.getElement().getStyle().setMarginLeft(122, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBin));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //============================== END =========================================

        return flwTable;
    }

}
