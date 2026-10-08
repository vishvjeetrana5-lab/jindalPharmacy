package aarogya.inventory.purchaseOrder.client;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import aarogya.inventory.purchaseOrder.shared.GrdGrnPurchaseHistory_Mdl;
import aarogya.inventory.purchaseOrder.shared.GrdIndentDetail_Mdl;
import aarogya.inventory.purchaseOrder.shared.TxtHItemName_Mdl;

public abstract class PurchaseOrderGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final FlowPanel flwAddIndent = new FlowPanel();
    //<editor-fold defaultstate="collapsed" desc="OverHeadLine Containers">
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtDiscountPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtDiscount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtTaxableAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSgstUgstPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtSgstUgst = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtCGSTPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtCGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtIGSTPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtIGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtItemValue = new DmTextBoxNumeric(2, true, 12, false);
    final DmLabel lblSgstUgst = new DmLabel("SGST/UGST");
    //</editor-fold>
    final DmTextBox txtRequiredDate = new DmTextBox(11, false);
    final DmTextBox txtPurchaseUnit = new DmTextBox();
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(150, false);
    final DmTextBox txtProperty = new DmTextBox(50, false);
    final DmTextBox txtProperties = new DmTextBox(50, false);
    final DmTextBox txtTolerance = new DmTextBox();
    final DmTextBoxNumeric txtRate = new DmTextBoxNumeric(2, false, 12, true);
    final DmTextBoxNumeric txtQuantity = new DmTextBoxNumeric(4, false, 12, true);
    final Button btnAddIndent = new Button("Add Indent");
    final Anchor aProperties = new Anchor("Properties");
    final Anchor aTolerance = new Anchor("Tolerance");
    final DmCheckBox cbCurrentSupplier = new DmCheckBox();
    final DmCheckBox cbOtherSuppliers = new DmCheckBox();

    final DmTextBoxNumeric txtSGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtUGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtUGSTPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtSGSTPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBox txtHSN = new DmTextBox();
    final DmCheckBox cbIsManualGstType = new DmCheckBox();
    final DmCheckBox cbIsManualTax = new DmCheckBox();
    TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

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

    //<editor-fold defaultstate="collapsed" desc="txtHGstType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGstType = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 6, false);

    private void iniTxtHGstType() {
        txtHGstType.getDmGrid().addTextColumn(txtHGstType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHGstType.getDmGrid().initializeGrid(txtHGstType.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHGstType.getHelper().addSort("printSrNo", DmSortType.ASC);
        txtHGstType.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public PurchaseOrderGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        iniTxtHItemName();
        iniTxtHGstType();
        iniGrdPrevGrn();
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "480px", null, null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width: 64.5%; height: 264px; float: left; margin-left: 2px;");
        flwTd.add(entryArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width: 34%; float: left; padding-left: 1%; border-left: 1px solid rgb(204, 204, 204);");
        flwTd.add(rightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width: 100%; height: 200px; float: left;");
        flwTd.add(GridArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel GridArea() {
        DmClient.setStyle(tabPanel, "100%", "200px", null, null, DmPosition.CENTER_HORIZONTAL, true);
        tabPanel.add(indentDetailTab(), "Indent Detail");
        tabPanel.add(purchaseHistoryTab(), "Purchase History");
        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel indentDetailTab() {
        FlowPanel flwTable, flwTd, flwTr;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "200px", null, null, DmPosition.NONE, true);

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

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "165px", "left", null, DmPosition.NONE, true);
        flwTr.add(iniGrdIndentDetail());
        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel purchaseHistoryTab() {

        FlowPanel flwTable, flwTd, flwTr;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "200px", null, null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "30px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("Style", "float: left; margin-top: 6px;");
        lbl = new DmLabel("Purchase History :");
        lbl.setStyleName("dm-HLabel");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("Style", "margin-top: 5px; margin-bottom: 1px; float: right;");
        cbCurrentSupplier.getElement().setAttribute("Style", "margin-right: 5px;");
        flwTd.add(cbCurrentSupplier);
        lbl = new DmLabel("Current Supplier");
        lbl.getElement().setAttribute("Style", "margin-right: 40px; float: left; width: 100%; white-space: nowrap; height: 15px; padding-top: 1px;");
        flwTd.add(lbl);
        cbOtherSuppliers.getElement().setAttribute("Style", "margin-right: 5px;");
        flwTd.add(cbOtherSuppliers);
        lbl = new DmLabel("Other Suppliers");
        lbl.getElement().setAttribute("Style", "white-space: nowrap; width: 100%; margin-right: 37px; height: 15px; padding-top: 1px;");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //====================================< 5th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "154px", "left", null, DmPosition.NONE, true);
        flwTr.add(grdPrevGrnPurchase);
        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "264px", null, null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        //**********************************************************************************//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "69%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //**********************************************************************************//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHItemName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 3rd Row >====================================*/
        //**********************************************************************************//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "52%", null, "left", null, DmPosition.NONE, false);
        aProperties.setStyleName("gwt-Label");
        flwTd.add(aProperties);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //**********************************************************************************//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        txtProperties.setReadOnly(true);
        flwTd.add(vl.add(txtProperties));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 4thRow >====================================*/

        //====================================< 5th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("ITS Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(7, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginBottom(3, Style.Unit.PX);
        cbIsManualGstType.setTitle("Check it for manual ITS");
        flwTd.add(cbIsManualGstType);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(17, Style.Unit.PCT);
        lbl = new DmLabel("HSN");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 6th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGstType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        txtHSN.setReadOnly(true);
        flwTd.add(txtHSN);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //**********************************************************************************//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Order Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Unit");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        aTolerance.setStyleName("gwt-Label");
        flwTd.add(aTolerance);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //**********************************************************************************//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQuantity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4.5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtPurchaseUnit.setReadOnly(true);
        flwTd.add(txtPurchaseUnit);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4.5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtTolerance.setReadOnly(true);
        flwTd.add(txtTolerance);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 5th Row >====================================*/

        //**********************************************************************************//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Required Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //**********************************************************************************//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4.5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRequiredDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 6th Row >====================================*/
        //**********************************************************************************//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "52%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //**********************************************************************************//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmPosition.NONE, true);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gross Amount ");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        txtAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Discount");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtDiscountPer.getElement().setPropertyString("placeholder", " % ");
        txtDiscountPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtDiscountPer));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "67%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        txtDiscount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtDiscount));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Taxable Value ");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        txtTaxableAmt.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtTaxableAmt));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(lblSgstUgst);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(24, Style.Unit.PCT);
        lbl = new DmLabel("Manual Tax");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginBottom(1, Style.Unit.PX);
        cbIsManualTax.setTitle("Check it for manual TAX");
        flwTd.add(cbIsManualTax);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtSgstUgstPer.getElement().setPropertyString("placeholder", " % ");
        txtSgstUgstPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtSgstUgstPer));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "67%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        txtSgstUgst.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtSgstUgst));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("CGST ");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtCGSTPer.getElement().setPropertyString("placeholder", " % ");
        txtCGSTPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtCGSTPer));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "67%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        txtCGST.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtCGST));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("IGST ");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtIGSTPer.getElement().setPropertyString("placeholder", " % ");
        txtIGSTPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtIGSTPer));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "67%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        txtIGST.addStyleName("dm-textInput-IGST");
        txtIGST.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtIGST));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Value ");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        txtItemValue.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtItemValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

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

    //<editor-fold defaultstate="collapsed" desc="grdPrevGrnPurchase">
    interface GrdPrevGrnPurchase_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdGrnPurchaseHistory_Mdl> {

        @Editor.Path("RefDate")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> refDate();

        @Editor.Path("RefNo")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> refNo();

        @Editor.Path("SupplierName")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> supplierName();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> purchaseUnitName();

        @Editor.Path("AcceptQty")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> acceptQty();

        @Editor.Path("Rate")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> rate();

        @Editor.Path("SkuName")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> skuName();

        @Editor.Path("SkuQty")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> skuQty();

        @Editor.Path("SkuRate")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> skuRate();

        @Editor.Path("LandedSkuValue")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> landedSkuValue();

    }

    final DmGridPaging<GrdGrnPurchaseHistory_Mdl, GrdPrevGrnPurchase_PrpAcs, String> grdPrevGrnPurchase = new DmGridPaging(GWT.create(GrdPrevGrnPurchase_PrpAcs.class), 5, true);

    private void iniGrdPrevGrn() {
        grdPrevGrnPurchase.getDmGrid().addRowNumberColumn();
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().refDate(), "Ref. Date", 75, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().refNo(), "Ref. No.", 100, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().supplierName(), "Supplier", 180, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().purchaseUnitName(), "Unit", 35, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().acceptQty(), "Qty.", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().rate(), "Rate", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().skuName(), "SKU", 35, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().skuQty(), "SKU Qty.", 65, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().landedSkuValue(), "SKU Land. Val.", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().initializeGrid(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().fieldHashCode(), 750, 0);
        grdPrevGrnPurchase.getHelper().addSort("refDate", DmSortType.DESC);
        grdPrevGrnPurchase.getHelper().addSort("v_SNo", DmSortType.ASC);
    }
    //</editor-fold>
}
