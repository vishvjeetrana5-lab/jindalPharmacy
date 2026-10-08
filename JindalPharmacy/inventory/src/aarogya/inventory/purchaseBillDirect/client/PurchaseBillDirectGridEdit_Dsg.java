package aarogya.inventory.purchaseBillDirect.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
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
import aarogya.inventory.purchaseBillDirect.shared.GrdItemDetail_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.TxtHItemName_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.TxtHOrderNo_Mdl;

public abstract class PurchaseBillDirectGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    //<editor-fold defaultstate="collapsed" desc="OverHeadLine Containers">
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtDiscountPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtDiscount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtTaxableAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSgstUgstPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtSgstUgst = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSGSTPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtSGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtUGSTPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtUGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtCGSTPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtCGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtIGSTPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtIGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSubTotal = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtFinalDiscountPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtFinalDiscount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtItemValue = new DmTextBoxNumeric(2, true, 12, false);
    final DmLabel lblSgstUgst = new DmLabel("SGST/UGST");
    //</editor-fold>

    final DmCheckBox cbIsManualGstType = new DmCheckBox();
    final DmCheckBox cbIsManualTax = new DmCheckBox();

    final DmTextBox txtHSN = new DmTextBox();

    final Anchor aProperties = new Anchor("Properties");
    final Anchor aLabRejectedQty = new Anchor("Lab Rejected Qty.");
    final Anchor aQCRejectedQty = new Anchor("QC Rejected Qty.");

    final DmTextBoxNumeric txtLabQty = new DmTextBoxNumeric(4, false, 12, false);
    final DmTextBox txtLabQtyUnit = new DmTextBox();
    final DmTextBoxNumeric txtQCQty = new DmTextBoxNumeric(4, false, 12, false);
    final DmTextBox txtQCQtyUnit = new DmTextBox();
    final DmTextBoxNumeric txtRejectedQty = new DmTextBoxNumeric(4, false, 12, false);
    final DmTextBox txtRejectedQtyUnit = new DmTextBox();
    final DmTextBoxNumeric txtDrCrNoteAmt = new DmTextBoxNumeric(4, true, 12, false);

    final DmTextBox txtPurchaseUnit = new DmTextBox();
    final DmTextBox txtChallanQtyUnit = new DmTextBox();
    final DmTextBox txtReceiveQtyUnit = new DmTextBox();

    final DmTextBox txtSkuQtyUnit = new DmTextBox();
    final DmTextBox txtSkuDifferenceQtyUnit = new DmTextBox();
    final DmTextBox txtAcceptQtyUnit = new DmTextBox();
    final DmTextBox txtRateUnit = new DmTextBox();

    final DmTextBox txtAcceptQty = new DmTextBox();
    final DmTextBox txtProperties = new DmTextBox();
    final DmTextBox txtPoRefNo = new DmTextBox();
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(150, false);

    final DmTextBoxNumeric txtSkuDifferenceQty = new DmTextBoxNumeric(4, true, 12, false);
    final DmTextBoxNumeric txtRate = new DmTextBoxNumeric(2, false, 12, true);
    // final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtReceiveQty = new DmTextBoxNumeric(4, false, 12, true);

    final DmTextBoxNumeric txtSKUQty = new DmTextBoxNumeric(4, false, 12, false);
    final DmTextBoxNumeric txtChallanQty = new DmTextBoxNumeric(4, false, 12, true);

    final DmTextBox txtShortageQty = new DmTextBox();
    final DmTextBox txtShortageQtyUnit = new DmTextBox();

    final DmCheckBox cbCurrentSupplier = new DmCheckBox();
    final DmCheckBox cbOtherSuppliers = new DmCheckBox();

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHDepartment.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHItemName">
    interface TxtHItemName_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHItemName_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHItemName_Mdl, String> manualCode();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<TxtHItemName_Mdl, String> purchaseUnitName();

    }

    final DmTextHelp<TxtHItemName_Mdl, TxtHItemName_PrpAcs> txtHItemName = new DmTextHelp(GWT.create(TxtHItemName_PrpAcs.class), 7, true);

    private void iniTxtHItemName() {
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().name(), "Item Name", 200, "name");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().purchaseUnitName(), "Unit", 50, "purchaseUnitName");
        txtHItemName.getDmGrid().initializeGrid(txtHItemName.getDmGrid().getPropertyAccess().fieldHashCode(), 350, 0, 1);
        txtHItemName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHOrderNo">
    interface TxtHOrderNo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHOrderNo_Mdl> {

        @Editor.Path("PoRefNo")
        ValueProvider<TxtHOrderNo_Mdl, String> poRefNo();

        @Editor.Path("OrderQty")
        ValueProvider<TxtHOrderNo_Mdl, String> orderQty();

        @Editor.Path("PendingQty")
        ValueProvider<TxtHOrderNo_Mdl, String> pendingQty();

        @Editor.Path("UnitName")
        ValueProvider<TxtHOrderNo_Mdl, String> unitName();

        @Editor.Path("Rate")
        ValueProvider<TxtHOrderNo_Mdl, String> rate();

        @Editor.Path("ItemName")
        ValueProvider<TxtHOrderNo_Mdl, String> itemName();

        @Editor.Path("Properties")
        ValueProvider<TxtHOrderNo_Mdl, String> properties();

    }

    final DmTextHelp<TxtHOrderNo_Mdl, TxtHOrderNo_PrpAcs> txtHOrderNo = new DmTextHelp(GWT.create(TxtHOrderNo_PrpAcs.class), 7, false);

    private void iniTxtHOrderNo() {
        txtHOrderNo.getDmGrid().addTextColumn(txtHOrderNo.getDmGrid().getPropertyAccess().name(), "Order No.", 80, "poRecId");
        txtHOrderNo.getDmGrid().addTextColumn(txtHOrderNo.getDmGrid().getPropertyAccess().poRefNo(), "Ref. No.", 70, "poRefNo");
        txtHOrderNo.getDmGrid().addTextColumn(txtHOrderNo.getDmGrid().getPropertyAccess().itemName(), "Item", 1, "itemName");
        txtHOrderNo.getDmGrid().addTextColumn(txtHOrderNo.getDmGrid().getPropertyAccess().unitName(), "Unit", 40, "unitName");
        txtHOrderNo.getDmGrid().addTextColumn(txtHOrderNo.getDmGrid().getPropertyAccess().properties(), "Properties", 100, "");
        txtHOrderNo.getDmGrid().addTextColumn(txtHOrderNo.getDmGrid().getPropertyAccess().orderQty(), "Order Qty.", 80, "orderQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHOrderNo.getDmGrid().addTextColumn(txtHOrderNo.getDmGrid().getPropertyAccess().pendingQty(), "Pending Qty.", 90, "pendingQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHOrderNo.getDmGrid().addTextColumn(txtHOrderNo.getDmGrid().getPropertyAccess().rate(), "Rate", 50, "rate").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHOrderNo.getDmGrid().initializeGrid(txtHOrderNo.getDmGrid().getPropertyAccess().fieldHashCode(), 740, 0, 3);
        txtHOrderNo.getHelper().addSort("itemName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdPrevGrn">
    interface GrdGrnDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemDetail_Mdl> {

        @Editor.Path("RefDate")
        ValueProvider<GrdItemDetail_Mdl, String> refDate();

        @Editor.Path("RefNo")
        ValueProvider<GrdItemDetail_Mdl, String> refNo();

        @Editor.Path("SupplierName")
        ValueProvider<GrdItemDetail_Mdl, String> supplierName();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<GrdItemDetail_Mdl, String> purchaseUnitName();

        @Editor.Path("AcceptQty")
        ValueProvider<GrdItemDetail_Mdl, String> acceptQty();

        @Editor.Path("Rate")
        ValueProvider<GrdItemDetail_Mdl, String> rate();

        @Editor.Path("SkuName")
        ValueProvider<GrdItemDetail_Mdl, String> skuName();

        @Editor.Path("SkuQty")
        ValueProvider<GrdItemDetail_Mdl, String> skuQty();

        @Editor.Path("SkuRate")
        ValueProvider<GrdItemDetail_Mdl, String> skuRate();

        @Editor.Path("LandedSkuValue")
        ValueProvider<GrdItemDetail_Mdl, String> landedSkuValue();

    }

    final DmGridPaging<GrdItemDetail_Mdl, GrdGrnDetail_PrpAcs, String> grdPrevGrn = new DmGridPaging(GWT.create(GrdGrnDetail_PrpAcs.class), 5, true);

    private void iniGrdPrevGrn() {
        grdPrevGrn.getDmGrid().addTextColumn(grdPrevGrn.getDmGrid().getPropertyAccess().refDate(), "Ref. Date", 75, "");
        grdPrevGrn.getDmGrid().addTextColumn(grdPrevGrn.getDmGrid().getPropertyAccess().refNo(), "Ref. No.", 100, "");
        grdPrevGrn.getDmGrid().addTextColumn(grdPrevGrn.getDmGrid().getPropertyAccess().supplierName(), "Supplier", 210, "");
        grdPrevGrn.getDmGrid().addTextColumn(grdPrevGrn.getDmGrid().getPropertyAccess().purchaseUnitName(), "Unit", 35, "");
        grdPrevGrn.getDmGrid().addTextColumn(grdPrevGrn.getDmGrid().getPropertyAccess().acceptQty(), "Qty", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrn.getDmGrid().addTextColumn(grdPrevGrn.getDmGrid().getPropertyAccess().rate(), "Rate", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrn.getDmGrid().addTextColumn(grdPrevGrn.getDmGrid().getPropertyAccess().skuName(), "SKU", 35, "");
        grdPrevGrn.getDmGrid().addTextColumn(grdPrevGrn.getDmGrid().getPropertyAccess().skuQty(), "SKU Qty.", 65, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrn.getDmGrid().addTextColumn(grdPrevGrn.getDmGrid().getPropertyAccess().skuRate(), "SKU Rate", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrn.getDmGrid().addTextColumn(grdPrevGrn.getDmGrid().getPropertyAccess().landedSkuValue(), "SKU Land. Val.", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPrevGrn.getDmGrid().initializeGrid(grdPrevGrn.getDmGrid().getPropertyAccess().fieldHashCode(), 825, 300);
        grdPrevGrn.getHelper().addSort("refDate", DmSortType.DESC);
        grdPrevGrn.getHelper().addSort("v_SNo", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGstType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGstType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, false);

    private void iniTxtHGstType() {
        txtHGstType.getDmGrid().addTextColumn(txtHGstType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHGstType.getDmGrid().initializeGrid(txtHGstType.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHGstType.getHelper().addSort("printSrNo", DmSortType.ASC);
        txtHGstType.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public PurchaseBillDirectGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.setStyleName("dm-FlwWorkingArea");
        container.add(workingArea());
        iniTxtHItemName();
        iniTxtHOrderNo();
        iniGrdPrevGrn();
        iniTxtHDepartment();
        iniTxtHGstType();
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width: 64.5%; float: left;");
        flwTd.add(leftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width: 34%; float: left; padding-left: 1%; border-left: 1px solid rgb(204, 204, 204);");
        flwTd.add(rightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2ns Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
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
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTable.add(grdPrevGrn);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", null, "left", null, DmPosition.NONE, true);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Order No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("Ref No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(vl.add(txtHOrderNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(vl.add(txtPoRefNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "66%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("Unit");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 4th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "66%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHItemName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        txtPurchaseUnit.setReadOnly(true);
        flwTd.add(txtPurchaseUnit);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 5th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        aProperties.setStyleName("gwt-Label");
        flwTd.add(aProperties);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 6th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        txtProperties.setReadOnly(true);
        flwTd.add(vl.add(txtProperties));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 5th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(4, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GST Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(7, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginBottom(3, Style.Unit.PX);
        cbIsManualGstType.setTitle("Check it for manual GST");
        flwTd.add(cbIsManualGstType);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(18, Style.Unit.PCT);
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
        //====================================< 7th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Doc. Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("Received Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("Shortage Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 8th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtChallanQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        txtChallanQtyUnit.setReadOnly(true);
        txtChallanQtyUnit.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTd.add(txtChallanQtyUnit);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("-");
        lbl.getElement().setAttribute("Style", "margin-left:10px;");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(vl.add(txtReceiveQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        txtReceiveQtyUnit.setReadOnly(true);
        txtReceiveQtyUnit.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTd.add(txtReceiveQtyUnit);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("=");
        lbl.getElement().setAttribute("Style", "margin-left:10px;");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        txtShortageQty.setReadOnly(true);
        flwTd.add(vl.add(txtShortageQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        txtShortageQtyUnit.setReadOnly(true);
        txtShortageQtyUnit.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTd.add(txtShortageQtyUnit);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 7th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(aLabRejectedQty);
        aLabRejectedQty.setStyleName("gwt-Label");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(aQCRejectedQty);
        aQCRejectedQty.setStyleName("gwt-Label");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Total Rejected Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 8th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        txtLabQty.setReadOnly(true);
        flwTd.add(vl.add(txtLabQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        txtLabQtyUnit.setReadOnly(true);
        txtLabQtyUnit.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTd.add(txtLabQtyUnit);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("+");
        lbl.getElement().setAttribute("Style", "margin-left:10px;");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        txtQCQty.setReadOnly(true);
        flwTd.add(vl.add(txtQCQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        txtQCQtyUnit.setReadOnly(true);
        txtQCQtyUnit.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTd.add(txtQCQtyUnit);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("=");
        lbl.getElement().setAttribute("Style", "margin-left:10px;");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        txtRejectedQty.setReadOnly(true);
        flwTd.add(vl.add(txtRejectedQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        txtRejectedQtyUnit.setReadOnly(true);
        txtRejectedQtyUnit.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTd.add(txtRejectedQtyUnit);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 9th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Dr./Cr. Note Amt.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("SKU Difference");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "26%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("SKU Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 10th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtDrCrNoteAmt.setReadOnly(true);
        flwTd.add(vl.add(txtDrCrNoteAmt));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSkuDifferenceQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        txtSkuDifferenceQtyUnit.setReadOnly(true);
        txtSkuDifferenceQtyUnit.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTd.add(txtSkuDifferenceQtyUnit);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(vl.add(txtSKUQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        txtSkuQtyUnit.setReadOnly(true);
        txtSkuQtyUnit.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTd.add(vl.add(txtSkuQtyUnit));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******** >====================================//
        //====================================< 11th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Accept Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("Rate");
        flwTd.add(lbl);
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
//        lbl = new DmLabel("Amount");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //====================================< ******** >====================================//
        //====================================< 12th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAcceptQty));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        txtAcceptQtyUnit.setReadOnly(true);
        txtAcceptQtyUnit.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTd.add(txtAcceptQtyUnit);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("X");
        lbl.getElement().setAttribute("Style", "margin-left:10px;");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        txtRateUnit.setReadOnly(true);
        txtRateUnit.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTd.add(txtRateUnit);
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("=");
//        lbl.getElement().setAttribute("Style", "margin-left:10px;");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
//        flwTd.add(vl.add(txtAmount));
//        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******** >====================================//
        //====================================< 13th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******** >====================================//

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
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
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
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(lblSgstUgst);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(38, Style.Unit.PCT);
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
        lbl = new DmLabel("Sub Total");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        txtSubTotal.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtSubTotal));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Final Discount ");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 2nd Row >====================================//
         flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        txtFinalDiscountPer.getElement().setPropertyString("placeholder", " % ");
        txtFinalDiscountPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtFinalDiscountPer));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "67%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        txtFinalDiscount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtFinalDiscount));
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

}
