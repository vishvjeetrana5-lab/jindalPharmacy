package aarogya.inventory.purchaseBill.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import aarogya.inventory.purchaseBill.shared.TxtHGrnNo_Mdl;

public abstract class PurchaseBillGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {
    
    final FlowPanel flwForm = new FlowPanel();
    
    final DmTextBoxNumeric txtRate = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtDocQty = new DmTextBoxNumeric(4, false, 12, true);
    final DmTextBoxNumeric txtAcptQty = new DmTextBoxNumeric(4, false, 12, true);
    final DmTextBoxNumeric txtSKUQty = new DmTextBoxNumeric(4, false, 12, false);
    
    final DmTextBox txtChallanNo = new DmTextBox(100, false);
    final DmTextBox txtChallanDate = new DmTextBox(100, false);
    final DmTextBox txtOrderNo = new DmTextBox(100, false);
    final DmTextBox txtItemName = new DmTextBox(100, false);
    final DmTextBox txtPurchaseUnit = new DmTextBox(100, false);
    final DmTextBox txtProperties = new DmTextBox();
    final DmTextBox txtHSN = new DmTextBox();

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

    final DmCheckBox cbIsManualGstType = new DmCheckBox();  //<Ag> Arrange in Dsg
    final DmCheckBox cbIsManualTax = new DmCheckBox();      //<Ag> Arrange in Dsg

    final Anchor aProperties = new Anchor("Properties");

    //<editor-fold defaultstate="collapsed" desc="txtHGrnNo">
    interface TxtHGrnNo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHGrnNo_Mdl> {
        
        @Editor.Path("PcRefNo")
        ValueProvider<TxtHGrnNo_Mdl, String> pcRefNo();
        
        @Editor.Path("ItemName")
        ValueProvider<TxtHGrnNo_Mdl, String> itemName();
        
        @Editor.Path("PurchaseUnitName")
        ValueProvider<TxtHGrnNo_Mdl, String> purchaseUnitName();
        
        @Editor.Path("GrnQty")
        ValueProvider<TxtHGrnNo_Mdl, String> grnQty();
        
        @Editor.Path("PendingQty")
        ValueProvider<TxtHGrnNo_Mdl, String> pendingQty();
        
        @Editor.Path("Rate")
        ValueProvider<TxtHGrnNo_Mdl, String> rate();
    }
    
    final DmTextHelp<TxtHGrnNo_Mdl, TxtHGrnNo_PrpAcs> txtHGrnNo = new DmTextHelp(GWT.create(TxtHGrnNo_PrpAcs.class), 7, false);
    
    private void iniTxtHGrnNo() {
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().name(), "GRN No.", 120, "pcRecId");
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().pcRefNo(), "Challan No.", 120, "pcRefNo");
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().itemName(), "Item", 1, "itemName");
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().purchaseUnitName(), "Unit", 60, "purchaseUnitName");
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().grnQty(), "GRN Qty.", 80, "grnQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().pendingQty(), "Pending Qty.", 90, "pendingQty").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().rate(), "Rate", 80, "rate").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHGrnNo.getDmGrid().initializeGrid(txtHGrnNo.getDmGrid().getPropertyAccess().fieldHashCode(), 750, 0, 3);
        txtHGrnNo.getHelper().addSort("pcV_Date", DmSortType.DESC);
        txtHGrnNo.getHelper().addSort("pcRecId", DmSortType.ASC);
        txtHGrnNo.getHelper().addSort("itemName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGstType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGstType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, false);      //<Ag> Arrange in Dsg

    private void iniTxtHGstType() {
        txtHGstType.getDmGrid().addTextColumn(txtHGstType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHGstType.getDmGrid().initializeGrid(txtHGstType.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        
        txtHGstType.getHelper().addSort("printSrNo", DmSortType.ASC);
        txtHGstType.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>    

    public PurchaseBillGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHGrnNo();
        iniTxtHGstType();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }
    
    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "337px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "337px", "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width: 64.5%; float: left;");
        flwTd.add(entryArea());
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width: 34%; float: left; padding-left: 1%; border-left: 1px solid rgb(204, 204, 204);");
        flwTd.add(rightArea());
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        
        return flwTable;
    }
    
    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", null, null, null, DmPosition.CENTER, true);

        //====================================< 1st Row >====================================//
        //************************************************************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "52%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GRN No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        //************************************************************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGrnNo));
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //====================================< 2nd Row >====================================//
        //************************************************************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Challan No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Challan Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Order No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        //************************************************************************************

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtChallanNo));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtChallanDate));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtOrderNo));
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 4th Row >====================================//

        //************************************************************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "67%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Unit");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        //************************************************************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtItemName));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPurchaseUnit));
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 4th Row >====================================//

        //************************************************************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        aProperties.setStyleName("gwt-Label");
        flwTd.add(aProperties);
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        //************************************************************************************

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        txtProperties.setReadOnly(true);
        flwTd.add(txtProperties);
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 5th Row >====================================//
        //************************************************************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GST Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        cbIsManualGstType.setTitle("Check it for manual GST");
        flwTd.add(cbIsManualGstType);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(72, Style.Unit.PX);
        lbl = new DmLabel("HSN");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        //************************************************************************************
        //====================================< 5th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGstType));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30.5%", null, "left", null, DmPosition.NONE, false);
        txtHSN.setReadOnly(true);
        flwTd.add(vl.add(txtHSN));
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        //************************************************************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Doc. Qty.");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Accept. Qty.");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        //************************************************************************************

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDocQty));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAcptQty));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRate));
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        //====================================< ******* >====================================//

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
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(lblSgstUgst);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(35, Style.Unit.PCT);
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
