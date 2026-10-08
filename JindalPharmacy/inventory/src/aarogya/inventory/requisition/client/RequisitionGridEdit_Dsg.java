package aarogya.inventory.requisition.client;

import aarogya.inventory.requisition.shared.TxtHItem_Mdl;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class RequisitionGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtQty = new DmTextBoxNumeric(4, false, 12, true);
    final DmTextBox txtProperties = new DmTextBox(100, false);
    final DmTextBox txtReqDate = new DmTextBox(20, false);
    final DmTextBox txtUnit = new DmTextBox(50, false);
    final DmTextBox txtRemark = new DmTextBox(100, false);
    final DmTextBox txtCurrPosition = new DmTextBox(100, false);
    final DmTextBox txtApprovedBy = new DmTextBox(50, false);
    final DmTextBox txtApprovedDateTime = new DmTextBox(50, false);
    final FlowPanel flwPopup = new FlowPanel();
    DmLabel lblProperties = new DmLabel("");

    //<editor-fold defaultstate="collapsed" desc="txtHItem">
    interface TxtHItem_PrpAcs extends KeyField_T_PrpAcs<TxtHItem_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHItem_Mdl, String> manualCode();

        @Editor.Path("SkuName")
        ValueProvider<TxtHItem_Mdl, String> sku();

        @Editor.Path("ItemGroup")
        ValueProvider<TxtHItem_Mdl, String> itemGroup();

        @Editor.Path("ItemCategory")
        ValueProvider<TxtHItem_Mdl, String> itemCategory();

        @Editor.Path("CurStkOfToDepartment")
        ValueProvider<TxtHItem_Mdl, String> curStkOfToDepartment();
    }

    final DmTextHelp<TxtHItem_Mdl, TxtHItem_PrpAcs> txtHItem = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHItem_PrpAcs.class), 7, true);

    public void iniItem() {
        txtHItem.getDmGrid().addTextColumn(txtHItem.getDmGrid().getPropertyAccess().name(), "Item Name", 290, "itemName");
        txtHItem.getDmGrid().addTextColumn(txtHItem.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHItem.getDmGrid().addTextColumn(txtHItem.getDmGrid().getPropertyAccess().sku(), "SKU", 100, "skuName");
        txtHItem.getDmGrid().addTextColumn(txtHItem.getDmGrid().getPropertyAccess().itemGroup(), "Item Group", 150, "itemGroup");
        txtHItem.getDmGrid().addTextColumn(txtHItem.getDmGrid().getPropertyAccess().itemCategory(), "Item Category", 150, "itemcategory");
//        txtHItem.getDmGrid().addTextColumn(txtHItem.getDmGrid().getPropertyAccess().curStkOfToDepartment(), "Stk To Dept.", 90, "curStkOfToDepartment");
        txtHItem.getDmGrid().initializeGrid(txtHItem.getDmGrid().getPropertyAccess().code(), 800, 0, 1);

        txtHItem.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<TxtHItem_Mdl>() {
            @Override
            public String getColStyle(TxtHItem_Mdl model, ValueProvider<? super TxtHItem_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (DmShared.vNull(model.getCurStkOfToDepartment()) == 0) {
                    styleClass += "dm-GridCellDairyCancel";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(TxtHItem_Mdl model, int rowIndex) {
                return "";
            }
        });

        txtHItem.getHelper().addSort("itemName", DmSortType.ASC);
    }

    //</editor-fold>
    public RequisitionGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniItem();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "110px", null, null, DmPosition.CENTER, true);

        //=========================================================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtUnit));
        txtUnit.setEnabled(false);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "78%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtHItem));
        hrtPnl.setCellWidth(txtHItem, "80%");
        hrtPnl.add((txtUnit));
        txtUnit.setEnabled(false);
        hrtPnl.setCellWidth(txtUnit, "20%");
        hrtPnl.add(flwPopup);
        flwTD.add(hrtPnl);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        /*====================================< 1st Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "22%", null, "left", null, DmPosition.NONE, false);
        lblProperties = new DmLabel("Properties");
        lblProperties.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblProperties.getElement().getStyle().setColor("Blue");
        flwTD.add(lblProperties);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "78%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtProperties));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 2nd Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Req. Qty.");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        txtQty.setAlignment(ValueBoxBase.TextAlignment.LEFT);
        flwTD.add(vl.add(txtQty));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Req. Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "26%", null, "left", null, DmPosition.NONE, false);

        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);

        hrtPnl.add(vl.add(txtReqDate));
        hrtPnl.setCellWidth(txtReqDate, "100%");

        flwTD.add(hrtPnl);
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*====================================< 3rd Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "78%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtRemark));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        /*====================================< 4th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Curr. Position");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "78%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtCurrPosition));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

}
