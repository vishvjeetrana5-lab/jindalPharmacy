package aarogya.inventory.indent.client;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import aarogya.inventory.indent.shared.TxtHItemName_Mdl;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class IndentGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {
    
    final FlowPanel flwForm = new FlowPanel();
    
    final DmTextBox txtReqDate = new DmTextBox(11, false);
    final DmTextBox txtRemarks = new DmTextBox(100, false);
    final DmTextBox txtProperties = new DmTextBox(100, false);
    final DmTextBoxNumeric txtQty = new DmTextBoxNumeric(4, true, 12, true);
    final DmTextBox txtUnit = new DmTextBox(20, false);
    final DmTextBox txtRequisitionNo = new DmTextBox(50, false);
    final DmTextBox txtPrperty = new DmTextBox(50, false);
    final FlowPanel flwPopup = new FlowPanel();
    FlowPanel flwLblReqisitionNo = new FlowPanel();
    FlowPanel flwLblReqisitionNoOuter = new FlowPanel();
    final DmLabel lblReqisitionNo = new DmLabel("");
    DmLabel lblProperties = new DmLabel("");
    final Anchor aStock = new Anchor("Show Stock");

    //<editor-fold defaultstate="collapsed" desc="txtHItemName">
    interface TxtHItemName_PrpAcs extends KeyField_T_PrpAcs<TxtHItemName_Mdl> {
        
        @Editor.Path("ManualCode")
        ValueProvider<TxtHItemName_Mdl, String> manualCode();
        
        @Editor.Path("SkuName")
        ValueProvider<TxtHItemName_Mdl, String> skuName();
        
    }
    
    final DmTextHelp<TxtHItemName_Mdl, TxtHItemName_PrpAcs> txtHItemName = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHItemName_PrpAcs.class), 7, true);
    
    public void iniTxtHItemName() {
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().name(), "Item Name", 200, "name");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().skuName(), "SKU", 50, "skuName");
        txtHItemName.getDmGrid().initializeGrid(txtHItemName.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHItemName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public IndentGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHItemName();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }
    
    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;
        
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "120px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Requisition Item");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(lblReqisitionItem);
//        flwLblReqisitionItem.add(flwTd);
//        flwTr.add(flwLblReqisitionItem);
//        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwLblReqisitionNo = new FlowPanel();
        DmClient.setStyle(flwLblReqisitionNo, "70%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "42%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Requisition No.");
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwLblReqisitionNo.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "58%", null, "left", null, DmPosition.NONE, false);
        lblReqisitionNo.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lblReqisitionNo);
        flwLblReqisitionNo.add(flwTd);
        flwLblReqisitionNoOuter.add(flwLblReqisitionNo);
        flwTr.add(flwLblReqisitionNoOuter);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "right", null, DmPosition.NONE, false);
        aStock.setStyleName("gwt-Label");
        aStock.getElement().getStyle().setFloat(Style.Float.RIGHT);
        flwTd.add(aStock);
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtHItemName));
        hrtPnl.setCellWidth(txtHItemName, "100%");
        
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lblProperties = new DmLabel("Properties");
        lblProperties.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblProperties.getElement().getStyle().setColor("Blue");
        flwTd.add(lblProperties);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtProperties));
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Quantity");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQty));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUnit));
        flwTd.getElement().getStyle().setMarginLeft(4, Style.Unit.PX);
        flwTr.add(flwTd);
        
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

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemarks));
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        
        return flwTable;
    }
    
}
