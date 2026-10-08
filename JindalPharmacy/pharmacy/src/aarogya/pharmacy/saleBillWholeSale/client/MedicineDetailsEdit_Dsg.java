package aarogya.pharmacy.saleBillWholeSale.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;

public abstract class MedicineDetailsEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, true, 10, false);
    final DmTextBoxNumeric txtTaxableAmt = new DmTextBoxNumeric(2, true, 10, false);
    final DmTextBoxNumeric txtRate = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtWholeRate = new DmTextBoxNumeric(7, false, 10, false);
    final DmTextBoxNumeric txtDiscountPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtDiscount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtDiscountPerUnit = new DmTextBoxNumeric(7, true, 15, false);

    final DmTextBoxNumeric txtDiscountPer2 = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtDiscount2 = new DmTextBoxNumeric(2, true, 12, false);

    final DmTextBoxNumeric txtCGSTPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtCGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSgstUgstPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtSgstUgst = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtNetAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBox txtBatchNo = new DmTextBox(100, false);
    final DmTextBox txtConversionRate = new DmTextBox();
    final DmTextBox txtPurchaseUnitName = new DmTextBox();
    final DmLabel lblBatchNo = new DmLabel("Batch No.");
    final DmLabel lblSgstUgst = new DmLabel("");
    final DmTextBoxNumeric txtTotalQty = new DmTextBoxNumeric(4, true, 12, false);
    final DmTextBoxNumeric txtFreeQty = new DmTextBoxNumeric(4, true, 12, false);
    final DmTextBox txtFreeQtyConversionRate = new DmTextBox();
    final DmTextBox txtConversionRateMRP = new DmTextBox();

    final FlowPanel flwAllowReturnValue = new FlowPanel();
    final CheckBox cbReturnSale = new CheckBox("");

    final FlowPanel flwBatch = new FlowPanel();
    final Button btnBatchNo = new Button("...");

    final DmTextBox txtMRP = new DmTextBox(20, false);
    final DmTextBox txtMedicineName = new DmTextBox();
    final DmTextBox txtManufacturingDate = new DmTextBox(100, false);
    final DmTextBox txtExpiryDate = new DmTextBox(100, false);
    final DmTextBoxNumeric txtGstAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtGstRate = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBox txtBarcode = new DmTextBox(50, false);

    final DmTextBoxNumeric txtIGSTPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtIGST = new DmTextBoxNumeric(2, true, 12, false);

    public MedicineDetailsEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        container.getElement().setAttribute("Style", "width:100%;float:left;");

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd, flwTd1;
        HorizontalPanel hrtPnl;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", "left", null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        if (1 == 2) { //Barcode is not required in wholesale bill
            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
            lbl = new DmLabel("Barcode");
            lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
            flwTd.add(lbl);
            flwTr.add(flwTd);
        }
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        DmClient.setStyle(flwAllowReturnValue, "100%", null, "left", null, DmPosition.NONE, true);
        cbReturnSale.setWidth("100px");
        cbReturnSale.setHeight("20px");
        cbReturnSale.getElement().setAttribute("style", cbReturnSale.getElement().getAttribute("style")
                + ";font-size:11.5px;padding:0px;");
        cbReturnSale.setTitle("Sale Return");
        cbReturnSale.setText("Sale Return");
        cbReturnSale.setTitle("Sale Return (Shortcut : ctrl+R)");
        flwAllowReturnValue.add(vl.add(cbReturnSale));
        hrtPnl.add(flwAllowReturnValue);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        if (1 == 2) { //Barcode is not required in wholesale bill
            flwTr = new FlowPanel();
            DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

            flwTd = new FlowPanel();
            DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
            flwTd.add(vl.add(txtBarcode));
            flwTr.add(flwTd);
            flwTable.add(flwTr);
        }

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMedicineName));
        txtMedicineName.setReadOnly(true);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(lblBatchNo);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("MRP/Pack");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd1 = new FlowPanel();
        DmClient.setStyle(flwTd1, "60%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBatchNo));
        flwTd1.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnBatchNo);
        btnBatchNo.getElement().getStyle().setWidth(20, Style.Unit.PX);
        btnBatchNo.getElement().getStyle().setHeight(21, Style.Unit.PX);
        flwTd1.add(flwTd);

        flwTr.add(flwTd1);

        flwTd1 = new FlowPanel();
        DmClient.setStyle(flwTd1, "40%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMRP));
        flwTd1.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "66%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtConversionRateMRP);
        txtConversionRateMRP.setReadOnly(true);
        flwTd1.add(flwTd);

        flwTr.add(flwTd1);

        flwTable.add(flwTr);
/////////////////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", "", "left", null, DmPosition.NONE, true);
        flwTd.add(lowerLeftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "39%", "", "left", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginRight(1, Style.Unit.PCT);
        flwTd.add(lowerRightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel lowerLeftArea() {
        FlowPanel flwTable, flwTr, flwTd, flwTd1, flwTd2;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "", null, null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mfg. Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtManufacturingDate);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Expiry Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtExpiryDate);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================<7th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 8th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd1 = new FlowPanel();
        DmClient.setStyle(flwTd1, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd2 = new FlowPanel();
        DmClient.setStyle(flwTd2, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd1.add(vl.add(txtTotalQty));
        flwTd2.add(txtConversionRate);
        flwTd.add(flwTd1);
        flwTd.add(flwTd2);

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Free Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd1 = new FlowPanel();
        DmClient.setStyle(flwTd1, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd2 = new FlowPanel();
        DmClient.setStyle(flwTd2, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd1.add(vl.add(txtFreeQty));
        flwTd2.add(txtFreeQtyConversionRate);
        flwTd.add(flwTd1);
        flwTd.add(flwTd2);

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 9th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Rate/Pack");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 10th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd1 = new FlowPanel();
        DmClient.setStyle(flwTd1, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd2 = new FlowPanel();
        DmClient.setStyle(flwTd2, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd1.add(vl.add(txtRate));
        flwTd2.add(txtPurchaseUnitName);
        flwTd.add(flwTd1);
        flwTd.add(flwTd2);

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel lowerRightArea() {
        FlowPanel flwTable, flwTr, flwTd, flwTd1, flwTd2;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "", null, null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTd.add(lbl);

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        txtAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(txtAmount);

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Discount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd1 = new FlowPanel();
        DmClient.setStyle(flwTd1, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd2 = new FlowPanel();
        DmClient.setStyle(flwTd2, "55%", null, "left", null, DmPosition.NONE, false);
        txtDiscountPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        txtDiscount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd1.add(vl.add(txtDiscountPer));
        flwTd2.add(vl.add(txtDiscount));
        flwTd.add(flwTd1);
        flwTd.add(flwTd2);

        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Bill Discount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd1 = new FlowPanel();
        DmClient.setStyle(flwTd1, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd2 = new FlowPanel();
        DmClient.setStyle(flwTd2, "55%", null, "left", null, DmPosition.NONE, false);
        txtDiscountPer2.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        txtDiscount2.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd1.add(txtDiscountPer2);
        flwTd2.add(txtDiscount2);
        txtDiscount2.setEnabled(false);
        txtDiscountPer2.setEnabled(false);
        flwTd.add(flwTd1);
        flwTd.add(flwTd2);

        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Taxable Amount");
        flwTd.add(lbl);

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        txtTaxableAmt.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(txtTaxableAmt);

        flwTr.add(flwTd);

        flwTable.add(flwTr);
//        /*====================================< 7th Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(lblSgstUgst);
//
//        flwTr.add(flwTd);
//
//        flwTable.add(flwTr);
//
//        /*====================================< 8th Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
//
//        flwTd1 = new FlowPanel();
//        DmClient.setStyle(flwTd1, "30%", null, "left", null, DmPosition.NONE, false);
//        flwTd2 = new FlowPanel();
//        DmClient.setStyle(flwTd2, "70%", null, "left", null, DmPosition.NONE, false);
//        txtSgstUgstPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
//        txtSgstUgst.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
//        flwTd1.add(vl.add(txtSgstUgstPer));
//        flwTd2.add(vl.add(txtSgstUgst));
//        flwTd.add(flwTd1);
//        flwTd.add(flwTd2);
//
//        flwTr.add(flwTd);
//
//        flwTable.add(flwTr);

        /*====================================< 9th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Tax Amount");
        flwTd.add(lbl);

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 10th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd1 = new FlowPanel();
        DmClient.setStyle(flwTd1, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd2 = new FlowPanel();
        DmClient.setStyle(flwTd2, "55%", null, "left", null, DmPosition.NONE, false);
        txtGstRate.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        txtGstAmt.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd1.add(vl.add(txtGstRate));
        flwTd2.add(vl.add(txtGstAmt));
        flwTd.add(flwTd1);
        flwTd.add(flwTd2);

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 11th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Net Amount");
        flwTd.add(lbl);

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 12th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        txtNetAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(txtNetAmount);

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }
}
