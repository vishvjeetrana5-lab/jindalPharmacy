package aarogya.pharmacy.medicineTemplateMaster.client;

import aarogya.pharmacy.medicineTemplateMaster.shared.TxtHItem_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class MedicineTemplateMasterDetailsGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtQty = new DmTextBoxNumeric(4, false, 12, true);
    final DmTextBox txtSku = new DmTextBox(10, false);

    //<editor-fold defaultstate="collapsed" desc="txtHMedicine">
    interface TxtHItem_PrpAcs extends KeyField_T_PrpAcs<TxtHItem_Mdl> {

        @Editor.Path("Sku")
        ValueProvider<TxtHItem_Mdl, String> sku();
    }

    final DmTextHelp<TxtHItem_Mdl, TxtHItem_PrpAcs> txtHItem = new DmTextHelp(GWT.create(TxtHItem_PrpAcs.class), 7, true);

    public void iniItem() {
        txtHItem.getDmGrid().addTextColumn(txtHItem.getDmGrid().getPropertyAccess().name(), "Medicine", 200, "Name");
        txtHItem.getDmGrid().addTextColumn(txtHItem.getDmGrid().getPropertyAccess().sku(), "SKU", 100, "SKU");
        txtHItem.getDmGrid().initializeGrid(txtHItem.getDmGrid().getPropertyAccess().code(), 350, 0, 1);

        txtHItem.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    public MedicineTemplateMasterDetailsGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniItem();
        container.setStyleName("dm-FlwWorkingArea");
        container.add(workingArea());
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "40px", null, null, DmEnum.DmPosition.CENTER, true);

        //Item
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Medicine");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "79%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtHItem));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //txtqty
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Qty.");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        txtQty.setAlignment(ValueBoxBase.TextAlignment.LEFT);
        flwTD.add(vl.add(txtQty));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("SKU");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(txtSku);
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }

}
