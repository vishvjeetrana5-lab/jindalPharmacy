package aarogya.pharmacy.purchaseOrder.client;

import aarogya.pharmacy.purchaseOrder.shared.GrdGrnPurchaseHistory_Mdl;
import aarogya.pharmacy.purchaseOrder.shared.GrdIndentDetail_Mdl;
import aarogya.pharmacy.purchaseOrder.shared.TxtHMedicineName_Mdl;
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

public abstract class PurchaseOrderGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final FlowPanel flwOHL = new FlowPanel();
    final FlowPanel flwAddIndent = new FlowPanel();
    final DmTextBox txtPurchaseUnit = new DmTextBox();
    final DmTextBox txtRequiredDate = new DmTextBox(11, false);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(150, false);
    final DmTextBox txtTolerance = new DmTextBox();
    final DmTextBoxNumeric txtRate = new DmTextBoxNumeric(2, true, 10, true);
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtMRP = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtQuantity = new DmTextBoxNumeric(4, false, 10, true);
    final Button btnAddIndent = new Button("Add Indent");
    final Anchor aTolerance = new Anchor("Tolerance");

    final DmCheckBox cbCurrentSupplier = new DmCheckBox();
    final DmCheckBox cbOtherSuppliers = new DmCheckBox();
    final DmTextBox txtMedicineName = new DmTextBox();
    final DmTextBox txtMedCompany = new DmTextBox(25, false);

    public PurchaseOrderGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        iniGrdPrevGrn();
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "464px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "130px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", "130px", "left", null, DmPosition.NONE, true);
        flwTd.add(entryArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "130px", "left", null, DmPosition.NONE, true);
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
        DmClient.setStyle(flwTr, "100%", "130px", "left", null, DmPosition.NONE, true);
        flwTr.add(iniGrdIndentDetail());
        flwTable.add(flwTr);
        //====================================< 4th Row >====================================//

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
        DmClient.setStyle(flwTable, "98%", "128px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMedicineName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Company");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMedCompany));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Order Qty.");
        lbl.getElement().getStyle().setMarginLeft(14, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQuantity));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 3rd Row >====================================*/

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        aTolerance.setStyleName("gwt-Label");
        aTolerance.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(aTolerance);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtTolerance.setReadOnly(true);
        flwTd.add(vl.add(txtTolerance));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Unit");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtPurchaseUnit.setReadOnly(true);
        flwTd.add(txtPurchaseUnit);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

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
        lbl = new DmLabel("MRP");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMRP));
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

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmount));
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

        @Editor.Path("LastPurchaseRate")
        ValueProvider<GrdIndentDetail_Mdl, String> lastPurchaseRate();

        @Editor.Path("ApprovedPurchaseRate")
        ValueProvider<GrdIndentDetail_Mdl, String> approvedPurchaseRate();

    }
    final DmGrid<GrdIndentDetail_Mdl, GridIndentDetail_PrpAcs> gridIndentDetail = new DmGrid(GWT.create(GridIndentDetail_PrpAcs.class));

    public Grid iniGrdIndentDetail() {
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().orderQty(), "Order SKU Qty", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().adjustedQty(), "Adjusted Qty", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().indentQty(), "Indent Qty", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().indentSKU(), "SKU", 90, "");
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().name(), "Indent No.", 80, "");
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().lastPurchaseRate(), "Rate", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().approvedPurchaseRate(), "App.Rate", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().indentDate(), "Indent Date", 80, "");
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().requiredDate(), "Required Date", 90, "");
        gridIndentDetail.addTextColumn(gridIndentDetail.getPropertyAccess().remark(), "Remark", 100, "");
        gridIndentDetail.initializeGrid(gridIndentDetail.getPropertyAccess().fieldHashCode(), 910, 127, 8);
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

        @Editor.Path("FreeQty")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> freeQty();

        @Editor.Path("Rate")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> rate();

        @Editor.Path("DiscountPer")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> discountPer();

        @Editor.Path("SaleRate")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> saleRate();

        @Editor.Path("Mrp")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> mrp();

        @Editor.Path("ProfitPer")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> profitPer();

        @Editor.Path("BatchNo")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> batchNo();

        @Editor.Path("ExpiryDate")
        ValueProvider<GrdGrnPurchaseHistory_Mdl, String> expiryDate();

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
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().refDate(), "Ref. Date", 80, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().refNo(), "Ref. No.", 110, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().supplierName(), "Supplier", 185, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().purchaseUnitName(), "Unit", 50, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().acceptQty(), "Qty.", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().freeQty(), "Free Qty.", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().rate(), "Rate", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().discountPer(), "Dis. %", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().saleRate(), "Sale Rate", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().mrp(), "MRP", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().profitPer(), "%", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().batchNo(), "Batch No.", 80, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().expiryDate(), "Exp. Dt.", 80, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().skuName(), "SKU", 40, "");
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().skuQty(), "SKU Qty.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().addTextColumn(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().landedSkuValue(), "SKU Land. Val.", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrnPurchase.getDmGrid().initializeGrid(grdPrevGrnPurchase.getDmGrid().getPropertyAccess().fieldHashCode(), 750, 0);
        grdPrevGrnPurchase.getHelper().addSort("refDate", DmSortType.DESC);
        grdPrevGrnPurchase.getHelper().addSort("v_SNo", DmSortType.ASC);
    }
    //</editor-fold>

}
