package aarogya.pharmacy.requisitionItemDetails.client;

import aarogya.pharmacy.saleBill.client.*;
import aarogya.pharmacy.saleBill.shared.TxtHMedicine_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.CheckBox;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;

public abstract class ItemBatchDetailEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtBatchNo = new DmTextBox(50, false);
    final DmTextBox txtMfgDate = new DmTextBox(11, false);
    final DmTextBox txtExpDate = new DmTextBox(11, false);
    final DmTextBox txtBestBefore = new DmTextBox(3, false);
    final DmTextBoxNumeric txtMRP = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtSaleRate = new DmTextBoxNumeric(2, false, 12, false);
    final FlowPanel flwWithBstBefr = new FlowPanel();
    final FlowPanel flwWithOutBstBefr = new FlowPanel();
    final FlowPanel flwBstBefr = new FlowPanel();
    final HorizontalPanel hrtPnl = new HorizontalPanel();
//    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, true, 10, false);
    final DmTextBoxNumeric txtReqQty = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtSaleQty = new DmTextBoxNumeric(2, false, 10, false);

    final DmTextBox txtSKUReqQty = new DmTextBox(20, false);
    final DmTextBox txtMedicineName = new DmTextBox(20, false);

    public ItemBatchDetailEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "147px", null, null, DmPosition.CENTER, true);

        // Control Detail
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "147px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "147px", "left", null, DmPosition.NONE, true);
        flwTd.add(entryArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "147px", null, null, DmPosition.CENTER, true);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("SKU");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMedicineName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSKUReqQty));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //////////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Batch No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("MRP");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Rate");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 3nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBatchNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMRP));
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSaleRate));
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwWithBstBefr.add(flwBestBefore());
        flwWithOutBstBefr.add(flwWithOutBestBefore());
        hrtPnl.add(flwWithOutBstBefr);
        hrtPnl.setCellWidth(flwWithOutBstBefr, "34%");
        flwBstBefr.add(hrtPnl);
        flwTr.add(flwBstBefr);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Req. Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //////////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReqQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSaleQty));
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    public FlowPanel flwBestBefore() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmPosition.CENTER, true);

        /*====================================< ******* >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mfg. Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Best Before");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 3nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48.6%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMfgDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48.6%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBestBefore));
        flwTd.getElement().getStyle().setMarginLeft(1.4, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    public FlowPanel flwWithOutBestBefore() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmPosition.CENTER, true);

        /*====================================< ******* >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Expiry Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 3nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtExpDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
}
