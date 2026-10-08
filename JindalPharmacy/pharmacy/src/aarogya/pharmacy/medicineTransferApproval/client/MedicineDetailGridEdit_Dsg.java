package aarogya.pharmacy.medicineTransferApproval.client;

import aarogya.pharmacy.medicineTransferApproval.client.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;

public abstract class MedicineDetailGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtReqDate = new DmTextBox(11, false);
    final DmTextBox txtItemName = new DmTextBox(50, false);

    final DmTextBoxNumeric txtIndQty = new DmTextBoxNumeric(4, true, 10, false);
    final DmTextBoxNumeric txtApprovedQty = new DmTextBoxNumeric(4, true, 10, false);
    final DmTextBox txtAprUnit = new DmTextBox(20, false);
    final DmTextBox txtIndUnit = new DmTextBox(20, false);
    final FlowPanel flwPopup = new FlowPanel();
    final Button btnPopup = new Button("...");

    public MedicineDetailGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "85px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtItemName));
        hrtPnl.setCellWidth(txtItemName, "100%");
        DmClient.setStyle(flwPopup, "100%", "20px", "left", null, DmPosition.NONE, true);
        btnPopup.addStyleName("dm-button-grad");
        btnPopup.setWidth("20px");
        btnPopup.setHeight("20px");
        btnPopup.getElement().setAttribute("style", btnPopup.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnPopup.setTitle("Property Detail");
        flwPopup.add((btnPopup));
        hrtPnl.add(flwPopup);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Required Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReqDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Appr. Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtApprovedQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAprUnit));
        flwTd.getElement().getStyle().setMarginLeft(4, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Indent Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIndQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIndUnit));
        flwTd.getElement().getStyle().setMarginLeft(4, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 1st Row >====================================*/
        return flwTable;
    }

}
