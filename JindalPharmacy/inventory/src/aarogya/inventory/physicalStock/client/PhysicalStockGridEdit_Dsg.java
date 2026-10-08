package aarogya.inventory.physicalStock.client;

import aarogya.inventory.physicalStock.shared.TxtHItemName_Mdl;
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
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class PhysicalStockGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {
 
    final DmTextBox txtSKU = new DmTextBox(50, false);
    final DmTextBoxNumeric txtQuantity = new DmTextBoxNumeric(4, false, 12, true);
    final DmTextBox txtProperties = new DmTextBox();
    final Anchor aProperties = new Anchor("Properties");

    final DmTextBoxNumeric txtRate = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBox txtRateDisplay = new DmTextBox();

    //<editor-fold defaultstate="collapsed" desc="txtHItemName">
    interface TxtHItemName_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHItemName_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHItemName_Mdl, String> manualCode();

        @Editor.Path("SkuName")
        ValueProvider<TxtHItemName_Mdl, String> skuName();

    }

    final DmTextHelp<TxtHItemName_Mdl, TxtHItemName_PrpAcs> txtHItemName = new DmTextHelp(GWT.create(TxtHItemName_PrpAcs.class), 7, true);

    private void iniTxtHItemName() {
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().name(), "Item Name", 150, "name");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().skuName(), "SKU", 50, "skuName");
        txtHItemName.getDmGrid().initializeGrid(txtHItemName.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 0, 1);
        txtHItemName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public PhysicalStockGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        iniTxtHItemName();
        container.setStyleName("dm-FlwWorkingArea");
        container.getElement().setAttribute("Style", "float:left;");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        flwTable.getElement().setAttribute("Style", "width: 98%; float: left; padding: 4px;");
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHItemName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        aProperties.setStyleName("gwt-Label");
        flwTd.add(aProperties);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        txtProperties.setReadOnly(true);
        flwTd.add(vl.add(txtProperties));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Quantity");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQuantity));
        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSKU));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate/Pack");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRate));
        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtRateDisplay);
        txtRateDisplay.setReadOnly(true);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
