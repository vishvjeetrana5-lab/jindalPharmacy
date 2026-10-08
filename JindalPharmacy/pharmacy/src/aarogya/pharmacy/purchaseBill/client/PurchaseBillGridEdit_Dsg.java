package aarogya.pharmacy.purchaseBill.client;

import aarogya.pharmacy.purchaseBill.shared.TxtHGrnNo_Mdl;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class PurchaseBillGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final FlowPanel flwOHL = new FlowPanel();

    final DmTextBoxNumeric txtRate = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtDocQty = new DmTextBoxNumeric(4, false, 12, false);
    final DmTextBoxNumeric txtAcptQty = new DmTextBoxNumeric(4, false, 12, false);
    final DmTextBoxNumeric txtFreeQty = new DmTextBoxNumeric(4, false, 12, false);
    final DmTextBoxNumeric txtSKUQty = new DmTextBoxNumeric(4, false, 12, false);

    final DmTextBox txtChallanNo = new DmTextBox(100, false);
    final DmTextBox txtChallanDate = new DmTextBox(100, false);
    final DmTextBox txtOrderNo = new DmTextBox(100, false);
    final DmTextBox txtMedName = new DmTextBox(100, false);
    final DmTextBox txtPurchaseUnit = new DmTextBox(100, false);

    final DmTextBox txtBatchNo = new DmTextBox(50, false);
    final DmTextBox txtExpDate = new DmTextBox(11, false);
    final DmTextBoxNumeric txtMRP = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtSaleRate = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBox txtBarcode = new DmTextBox(50, false);

    //<editor-fold defaultstate="collapsed" desc="txtHGrnNo">
    interface TxtHGrnNo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHGrnNo_Mdl> {

        @Editor.Path("PcRefNo")
        ValueProvider<TxtHGrnNo_Mdl, String> pcRefNo();

        @Editor.Path("ItemName")
        ValueProvider<TxtHGrnNo_Mdl, String> itemName();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<TxtHGrnNo_Mdl, String> purchaseUnitName();

        @Editor.Path("GrnQty")
        ValueProvider<TxtHGrnNo_Mdl, String> grnQty();

        @Editor.Path("PendingQty")
        ValueProvider<TxtHGrnNo_Mdl, String> pendingQty();

        @Editor.Path("Rate")
        ValueProvider<TxtHGrnNo_Mdl, String> rate();
    }

    final DmTextHelp<TxtHGrnNo_Mdl, TxtHGrnNo_PrpAcs> txtHGrnNo = new DmTextHelp(GWT.create(TxtHGrnNo_PrpAcs.class), 7, false);

    private void iniTxtHGrnNo() {
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().name(), "GRN No.", 80, "pcRecId");
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().pcRefNo(), "Challan No.", 70, "pcRefNo");
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().itemName(), "Medicine", 1, "itemName");
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().purchaseUnitName(), "Unit", 60, "purchaseUnitName");
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().grnQty(), "GRN Qty.", 80, "grnQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().pendingQty(), "Pending Qty.", 90, "pendingQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().rate(), "Rate", 50, "rate").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHGrnNo.getDmGrid().initializeGrid(txtHGrnNo.getDmGrid().getPropertyAccess().fieldHashCode(), 640, 0, 3);
        txtHGrnNo.getHelper().addSort("pcV_Date", DmSortType.DESC);
        txtHGrnNo.getHelper().addSort("pcRecId", DmSortType.ASC);
        txtHGrnNo.getHelper().addSort("itemName", DmSortType.ASC);
    }
    //</editor-fold>

    public PurchaseBillGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHGrnNo();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "190px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "188px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", "188px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "188px", "left", null, DmPosition.NONE, true);
        flwTd.add(rightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "188px", null, null, DmPosition.CENTER, true);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GRN No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGrnNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Challan No.");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtChallanNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Challan Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtChallanDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Order No.");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtOrderNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMedName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Unit");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtPurchaseUnit.setReadOnly(true);
        flwTd.add(vl.add(txtPurchaseUnit));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Doc. Qty.");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDocQty));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Acp. Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAcptQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Free Qty.");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFreeQty));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRate));
        flwTr.add(flwTd);

        /*====================================< 5th Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Batch No.");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBatchNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Barcode");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBarcode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("MRP");
        lbl.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMRP));
        flwTr.add(flwTd);

        /*====================================< 5th Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Rate");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSaleRate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Expiry Date");
//        lbl.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtExpDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "188px", null, null, DmPosition.CENTER, true);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "120px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(flwOHL));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //====================================< ******* >====================================//
        return flwTable;
    }

}
