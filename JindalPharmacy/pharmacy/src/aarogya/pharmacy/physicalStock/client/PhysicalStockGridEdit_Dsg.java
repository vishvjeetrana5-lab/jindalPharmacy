package aarogya.pharmacy.physicalStock.client;

import aarogya.pharmacy.physicalStock.shared.TxtHBatchNo_Mdl;
import aarogya.pharmacy.physicalStock.shared.TxtHMedicineName_Mdl;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class PhysicalStockGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtSKU = new DmTextBox(50, false);

    final DmTextBox txtRateDisplay = new DmTextBox();
    final DmTextBox txtMRPDisplay = new DmTextBox();
    final DmTextBox txtSaleRateDisplay = new DmTextBox();

    final DmTextBox txtExpiryDate = new DmTextBox(11, false);
    final DmTextBoxNumeric txtMRP = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtSaleRate = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtRate = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtQuantity = new DmTextBoxNumeric(4, false, 12, false);
    final DmTextBox txtBarcode = new DmTextBox(50, false);

    //<editor-fold defaultstate="collapsed" desc="txtHMedicineName">
    interface TxtHMedicineName_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHMedicineName_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHMedicineName_Mdl, String> manualCode();

        @Editor.Path("SkuName")
        ValueProvider<TxtHMedicineName_Mdl, String> skuName();

    }

    final DmTextHelp<TxtHMedicineName_Mdl, TxtHMedicineName_PrpAcs> txtHMedicineName = new DmTextHelp(GWT.create(TxtHMedicineName_PrpAcs.class), 10, true);

    private void iniTxtHMedicineName() {
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().name(), "Medicine Name", 200, "name");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().skuName(), "SKU", 50, "skuName");
        txtHMedicineName.getDmGrid().initializeGrid(txtHMedicineName.getDmGrid().getPropertyAccess().fieldHashCode(), 600, 0, 1);
        txtHMedicineName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHBatchNo">
    interface TxtHBatchNo_PrpAcs extends KeyField_T_PrpAcs<TxtHBatchNo_Mdl> {

        @Editor.Path("ExpireDate")
        ValueProvider<TxtHBatchNo_Mdl, String> expireDate();

        @Editor.Path("Mrp")
        ValueProvider<TxtHBatchNo_Mdl, String> mrp();

        @Editor.Path("SaleRate")
        ValueProvider<TxtHBatchNo_Mdl, String> saleRate();

        @Editor.Path("Barcode")
        ValueProvider<TxtHBatchNo_Mdl, String> barcode();

    }

    final DmTextHelp<TxtHBatchNo_Mdl, TxtHBatchNo_PrpAcs> txtHBatchNo = new DmTextHelp(GWT.create(TxtHBatchNo_PrpAcs.class), 10, true);

    private void iniTxtHBatchNo() {
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().name(), "Batch No.", 1, "batchNo");
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().expireDate(), "Expiry Date", 80, "expiryDate", DmDataType.DATE);
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().mrp(), "MRP", 100, "mrp", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().saleRate(), "Sale Rate", 80, "saleRate", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().barcode(), "Barcode", 150, "barcode");

        txtHBatchNo.getDmGrid().initializeGrid(txtHBatchNo.getDmGrid().getPropertyAccess().code(), 600, 0, 1);

        txtHBatchNo.getHelper().addSort("batchNo", DmSortType.ASC);
        txtHBatchNo.getHelper().addSort("expiryDate", DmSortType.ASC);
    }
    //</editor-fold>

    public PhysicalStockGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        iniTxtHMedicineName();
        iniTxtHBatchNo();
        container.setStyleName("dm-FlwWorkingArea");
        container.getElement().setAttribute("Style", "float:left;");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        flwTable.getElement().setAttribute("Style", "width: 98%; float: left; padding: 4px;");
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicineName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Batch No.");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBatchNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Barcode");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBarcode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Quantity");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQuantity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("SKU");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSKU));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate/Pack");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtRateDisplay);
        txtRateDisplay.setReadOnly(true);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Expiry Date");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtExpiryDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 4th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("MRP/Pack");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMRP));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtMRPDisplay);
        txtMRPDisplay.setReadOnly(true);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Rate/Pack");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSaleRate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtSaleRateDisplay);
        txtSaleRateDisplay.setReadOnly(true);

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

}
