package aarogya.pharmacy.medicineInTransit.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;

public abstract class MedicineInTransitGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(200, false);
    final DmTextBox txtBatchNo = new DmTextBox(50, true);
    final DmTextBox txtExpiryDate = new DmTextBox(11, false);
    final DmTextBox txtMAnufacturingDate = new DmTextBox(11, false);
    final DmTextBox txtMRPPackSize = new DmTextBox();
    final DmTextBox txtSaleRatePackSize = new DmTextBox();
    final DmTextBox txtSKU = new DmTextBox();
    final DmTextBoxNumeric txtMRP = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtSaleRate = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtBestBeforeDays = new DmTextBoxNumeric(0, false, 10, false);
    final DmTextBoxNumeric txtQuantity = new DmTextBoxNumeric(4, false, 12, true);
    final DmTextBoxNumeric txtSKURate = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBox txtSKURatePackSize = new DmTextBox();

    final Button btnBatchNo = new Button("...");
    final DmLabel lblReqisitionNoValue = new DmLabel("");
    final FlowPanel flwLblReqisitionNo = new FlowPanel();
    final FlowPanel flwLblReqisitionNoOuter = new FlowPanel();
    final Anchor aStock = new Anchor("Show Stock");
    final DmTextBox txtMedicineName = new DmTextBox();
    final DmTextBox txtBarCode = new DmTextBox(50, false);
    final DmLabel lblBatchNo = new DmLabel("Batch No.");

    public MedicineInTransitGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "281px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", "width: 100%; float: left; border-bottom: 1px solid #333; margin-bottom: 6px;");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, true);
        //<editor-fold defaultstate="collapsed" desc="flwLblReqisitionNoOuter Dsg">
        flwLblReqisitionNo.getElement().setAttribute("Style", "width: 100%; float: left;");

        FlowPanel flwLbl = new FlowPanel();
        DmClient.setStyle(flwLbl, "32%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Requisition No.");
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwLbl.add(lbl);
        flwLblReqisitionNo.add(flwLbl);

        FlowPanel flwCtrl = new FlowPanel();
        DmClient.setStyle(flwCtrl, "68%", null, "left", null, DmPosition.NONE, false);
        lblReqisitionNoValue.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwCtrl.add(lblReqisitionNoValue);
        flwLblReqisitionNo.add(flwCtrl);
        flwLblReqisitionNoOuter.add(flwLblReqisitionNo);
        //</editor-fold>
        flwTd.add(flwLblReqisitionNoOuter);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "right", null, DmPosition.NONE, true);
        aStock.setStyleName("gwt-Label");
        flwTd.add(aStock);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Barcode");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBarCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMedicineName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(lblBatchNo);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Expiry Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "43%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBatchNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(btnBatchNo);
        btnBatchNo.getElement().getStyle().setWidth(20, Style.Unit.PX);
        btnBatchNo.getElement().getStyle().setHeight(21, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtExpiryDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 7th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Manufacturing Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("MRP");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 8th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMAnufacturingDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMRP));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 9th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Rate");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Quantity");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 10th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSaleRate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSaleRatePackSize));
        txtSaleRatePackSize.getElement().getStyle().setWidth(85, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQuantity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSKU));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 11th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("SKU Rate");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 12th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "47%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSKURate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSKURatePackSize));
        txtSKURatePackSize.getElement().getStyle().setWidth(85, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 13th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 14th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
