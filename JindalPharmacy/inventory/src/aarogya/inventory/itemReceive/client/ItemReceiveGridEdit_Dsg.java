package aarogya.inventory.itemReceive.client;

import aarogya.inventory.itemReceive.shared.TxtHItemName_Mdl;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class ItemReceiveGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final DmTextBox txtSKU = new DmTextBox(50, false);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(200, false);
    final DmTextBoxNumeric txtQuantity = new DmTextBoxNumeric(4, false, 12, true);
    final DmTextBox txtProperties = new DmTextBox();
    final Anchor aProperties = new Anchor("Properties");
    final DmTextBox txtRatePackSize = new DmTextBox();
    final DmTextBox txtTotalPackSizeDisplay = new DmTextBox();

    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtTotalPacks = new DmTextBoxNumeric(4, true, 12, true);
    final DmTextBoxNumeric txtRate = new DmTextBoxNumeric(2, false, 10, false);

    //<editor-fold defaultstate="collapsed" desc="txtHItemName">
    interface TxtHItemName_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHItemName_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHItemName_Mdl, String> manualCode();

        @Editor.Path("Stock")
        ValueProvider<TxtHItemName_Mdl, String> stock();

        @Editor.Path("SkuName")
        ValueProvider<TxtHItemName_Mdl, String> skuName();

    }

    final DmTextHelp<TxtHItemName_Mdl, TxtHItemName_PrpAcs> txtHItemName = new DmTextHelp(GWT.create(TxtHItemName_PrpAcs.class), 7, true);

    private void iniTxtHItemName() {
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().name(), "Item Name", 150, "name");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().skuName(), "SKU", 50, "skuName");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().stock(), "Stock", 100, "currentStock", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHItemName.getDmGrid().initializeGrid(txtHItemName.getDmGrid().getPropertyAccess().fieldHashCode(), 400, 0, 1);
        txtHItemName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public ItemReceiveGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        iniTxtHItemName();
        container.setStyleName("dm-FlwWorkingArea");
        container.getElement().getStyle().setFloat(Style.Float.LEFT);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;


        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "", null, null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHItemName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        aProperties.setStyleName("gwt-Label");
        flwTd.add(aProperties);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", null, "left", null, DmPosition.NONE, false);
        txtProperties.setReadOnly(true);
        flwTd.add(vl.add(txtProperties));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Quantity");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQuantity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSKU));
        txtSKU.setReadOnly(true);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("No. Of Packs");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalPacks));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtTotalPackSizeDisplay);
        txtTotalPackSizeDisplay.setReadOnly(true);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate/Pack");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtRatePackSize);
        txtRatePackSize.setReadOnly(true);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "85%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
