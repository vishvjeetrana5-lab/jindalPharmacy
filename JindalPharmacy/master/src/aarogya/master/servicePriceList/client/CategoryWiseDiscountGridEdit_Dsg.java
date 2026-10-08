package aarogya.master.servicePriceList.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;

public abstract class CategoryWiseDiscountGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtCategory = new DmTextBox(100, false);

    final DmTextBoxNumeric txtDiscountPer = new DmTextBoxNumeric(2, false, 12, false);

    public CategoryWiseDiscountGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "50px", null, null, DmEnum.DmPosition.CENTER, true);

        // Control Detail
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "50px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "50px", null, null, DmEnum.DmPosition.CENTER, true);

        //Category
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Service Category");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtCategory));
        txtCategory.setEnabled(false);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Discount
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Discount %");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        txtDiscountPer.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTd.add(vl.add(txtDiscountPer));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
