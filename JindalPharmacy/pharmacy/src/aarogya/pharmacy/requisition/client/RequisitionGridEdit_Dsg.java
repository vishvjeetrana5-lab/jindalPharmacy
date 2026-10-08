package aarogya.pharmacy.requisition.client;

import aarogya.pharmacy.requisition.shared.TxtHMedicine_Mdl;
import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.google.gwt.user.client.ui.HorizontalPanel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class RequisitionGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtReqDate = new DmTextBox(11, false);

    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(100, false);
    final DmRemarkTextBox txtCurrPosition = new DmRemarkTextBox(100, false);
    ;
    final DmTextBox txtApprovedBy = new DmTextBox(50, false);
    final DmTextBox txtApprovedDateTime = new DmTextBox(50, false);
    final DmTextBoxNumeric txtQty = new DmTextBoxNumeric(4, false, 12, true);
    final DmTextBox txtUnit = new DmTextBox(50, false);
    final DmTextBox txtPurchaseUnit = new DmTextBox(50, false);

    //<editor-fold defaultstate="collapsed" desc="txtHMedicineName">
    interface TxtHMedicine_PrpAcs extends KeyField_T_PrpAcs<TxtHMedicine_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHMedicine_Mdl, String> manualCode();

        @Editor.Path("SkuName")
        ValueProvider<TxtHMedicine_Mdl, String> skuName();

        @Editor.Path("MedicineGroup")
        ValueProvider<TxtHMedicine_Mdl, String> medicineGroup();

        @Editor.Path("MedicineCategory")
        ValueProvider<TxtHMedicine_Mdl, String> medicineCategory();

        @Editor.Path("CurStkOfToDepartment")
        ValueProvider<TxtHMedicine_Mdl, String> curStkOfToDepartment();
    }

    final DmTextHelp<TxtHMedicine_Mdl, TxtHMedicine_PrpAcs> txtHMedicineName = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHMedicine_PrpAcs.class), 10, true);

    public void iniTxtHMedicineName() {
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().name(), "Medicine Name", 360, "medicineName");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().skuName(), "SKU", 70, "skuName");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().medicineGroup(), "Medicine Group", 110, "medicineGroup");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().medicineCategory(), "Medicine Category", 100, "medicinecategory");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().curStkOfToDepartment(), "Stk To Dept.", 90, "curStkOfToDepartment");
        txtHMedicineName.getDmGrid().initializeGrid(txtHMedicineName.getDmGrid().getPropertyAccess().code(), 850, 0, 1);
        txtHMedicineName.getHelper().addSort("medicineName", DmSortType.ASC);
    }
    //</editor-fold>

    public RequisitionGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHMedicineName();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "113px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("SKU");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicineName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUnit));
        txtUnit.setEnabled(false);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Purchase Unit");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Req. Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Req. Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Remark");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPurchaseUnit));
        txtPurchaseUnit.setEnabled(false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtReqDate));
        hrtPnl.setCellWidth(txtReqDate, "55%");
//        hrtPnl.add(vl.add(txtReqTime));
//        hrtPnl.setCellWidth(txtReqTime, "30%");
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtRemark));
//        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Curr. Position");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCurrPosition));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        return flwTable;
    }

}
