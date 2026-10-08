package aarogya.inventory.workOrder.client;

import aarogya.inventory.workOrder.shared.GrdIndentDetail_Mdl;
import aarogya.inventory.workOrder.shared.TxtHItemName_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class WorkOrderGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final FlowPanel flwOHL = new FlowPanel();
    final FlowPanel flwAddIndent = new FlowPanel();
    final DmTextBox txtRequiredDate = new DmTextBox(11, false);
    final DmTextBox txtPurchaseUnit = new DmTextBox();
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(150, false);
    final DmTextBox txtProperty = new DmTextBox(50, false);
    final DmTextBox txtProperties = new DmTextBox(50, false);
    final DmTextBox txtTolerance = new DmTextBox();
    final DmTextBoxNumeric txtRate = new DmTextBoxNumeric(2, false, 12, true);
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtQuantity = new DmTextBoxNumeric(4, false, 12, true);
    final Button btnAddIndent = new Button("Add Indent");
    final Anchor aProperties = new Anchor("Perperties");
    final Anchor aTolerance = new Anchor("Tolerance");

    //<editor-fold defaultstate="collapsed" desc="txtHItemName">
    interface TxtHItemName_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHItemName_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHItemName_Mdl, String> manualCode();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<TxtHItemName_Mdl, String> purchaseUnitName();

    }

    final DmTextHelp<TxtHItemName_Mdl, TxtHItemName_PrpAcs> txtHItemName = new DmTextHelp(com.google.gwt.core.client.GWT.create(TxtHItemName_PrpAcs.class), 7, true);

    private void iniTxtHItemName() {
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().name(), "Item Name", 200, "name");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().purchaseUnitName(), "Unit", 50, "purchaseUnitName");
        txtHItemName.getDmGrid().initializeGrid(txtHItemName.getDmGrid().getPropertyAccess().fieldHashCode(), 350, 0, 1);
        txtHItemName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public WorkOrderGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        iniTxtHItemName();
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "338px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "155px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", "155px", "left", null, DmPosition.NONE, true);
        flwTd.add(entryArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "155px", "left", null, DmPosition.NONE, true);
        flwTd.add(flwOHL);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "90%", "18px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Indent Detail :");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        lbl.setWidth("70%");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", "20px", "right", null, DmPosition.NONE, false);
        btnAddIndent.addStyleName("dm-button-grad");
        btnAddIndent.setWidth("75px");
        btnAddIndent.setHeight("20px");
        btnAddIndent.getElement().setAttribute("style", btnAddIndent.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnAddIndent.setTitle("Add Indent");
        flwAddIndent.getElement().setAttribute("Style", "width:100%; float:left;");
        flwTd.add(flwAddIndent);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "165px", "left", null, DmPosition.NONE, true);
        flwTr.add(iniGrdIndentDetail());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "150px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHItemName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Unit");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtPurchaseUnit.setReadOnly(true);
        flwTd.add(txtPurchaseUnit);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 1st Row >====================================*/
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
        lbl = new DmLabel("Order Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQuantity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        aTolerance.setStyleName("gwt-Label");
        aTolerance.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        aTolerance.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(aTolerance);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtTolerance.setReadOnly(true);
        flwTd.add(vl.add(txtTolerance));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Required Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRequiredDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridIndentDetail">
    interface GridIndentDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdIndentDetail_Mdl> {

        @Editor.Path("IndentQty")
        ValueProvider<GrdIndentDetail_Mdl, String> indentQty();

        @Editor.Path("OrderQty")
        ValueProvider<GrdIndentDetail_Mdl, String> orderQty();

        @Editor.Path("AdjustedQty")
        ValueProvider<GrdIndentDetail_Mdl, String> adjustedQty();

        @Editor.Path("IndentSKU")
        ValueProvider<GrdIndentDetail_Mdl, String> indentSKU();

        @Editor.Path("IndentDate")
        ValueProvider<GrdIndentDetail_Mdl, String> indentDate();

        @Editor.Path("RequiredDate")
        ValueProvider<GrdIndentDetail_Mdl, String> requiredDate();

        @Editor.Path("Remark")
        ValueProvider<GrdIndentDetail_Mdl, String> remark();

        @Editor.Path("Properties")
        ValueProvider<GrdIndentDetail_Mdl, String> properties();

    }
    final DmGrid<GrdIndentDetail_Mdl, GridIndentDetail_PrpAcs> gridIndentDetail = new DmGrid(GWT.create(GridIndentDetail_PrpAcs.class));

    public Grid iniGrdIndentDetail() {
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().orderQty(), "Order SKU Qty.", 110, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().adjustedQty(), "Adjusted Qty.", 110, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().indentQty(), "Indent Qty.", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().indentSKU(), "SKU", 90, "");
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().name(), "Indent No.", 80, "");
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().indentDate(), "Indent Date", 80, "");
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().requiredDate(), "Required Date", 90, "");
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().remark(), "Remark", 1, "");
        gridIndentDetail.initializeGrid(gridIndentDetail.getPropertyAccess().fieldHashCode(), 750, 160, 7);
        return gridIndentDetail.getGrid();
    }
    //</editor-fold>

}
