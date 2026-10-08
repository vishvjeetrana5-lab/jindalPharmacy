package aarogya.pharmacy.saleBillReturnProvisional.client;

import aarogya.pharmacy.medicineReceive.shared.TxtHMedicineName_Mdl;
import aarogya.pharmacy.saleBillReturnProvisional.shared.TxtHInvoicedItem_Mdl;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

public abstract class ReturnMedicineBatchDetail_Dsg {

    public final static String widgetName = "Invoiced Medicine Details";
    final DmTextBox txtBatchNo = new DmTextBox(50, false);
    final DmTextBox txtExpiryDate = new DmTextBox(11, false);
    final DmTextBoxNumeric txtInvoicedQty = new DmTextBoxNumeric(4, false, 12, false);
    final DmTextBox txtSKU = new DmTextBox();
    final DmTextBoxNumeric txtReturnQuantity = new DmTextBoxNumeric(4, false, 12, true);
//    final DmTextBox txtBarCode = new DmTextBox(50, false);
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button btnOK = new Button();
    final FlowPanel flwOK = new FlowPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final FlowPanel flwDelete = new FlowPanel();
    final Button btnCancel = new Button();
    final Button btnDelete = new Button();
    final DmClient.ViaList vl = new DmClient.ViaList();

    public ReturnMedicineBatchDetail_Dsg(String title, String width, String height) {
//        super(title, width, height);
        ini();
        iniTxtHReceivedFrom();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("500px");
        popupPanel.setHeight("150px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("OK");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOK.setTitle("Ok");
        btnOK.addStyleName("dm-top-control-btnStyle");
        btnOK.getElement().appendChild(aBtn.getElement());
        btnOK.getElement().appendChild(label.getElement());
        btnOK.setWidth("70px");
        flwOK.add(btnOK);
        
        label = new InlineLabel("Delete");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-trash-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDelete.setTitle("Delete");
        btnDelete.addStyleName("dm-top-control-btnStyle");
        btnDelete.getElement().appendChild(aBtn.getElement());
        btnDelete.getElement().appendChild(label.getElement());
        btnDelete.setWidth("70px");
        flwDelete.add(btnDelete);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOK);
        toolContainer.addWidget(flwDelete);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "", null, null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Barcode");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);
//
//        /*====================================< 2nd Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtBarCode));
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);

        /*====================================< 2.1 Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHInvoicedItem));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

//        /*====================================< 5th Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "19%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Batch No.");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Expiry Date");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Invoiced Qty");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTable.add(flwTr);
//
//        /*====================================< 6th Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtBatchNo));
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "36%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtExpiryDate));
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(txtInvoicedQty);
//        flwTr.add(flwTd);
//
//        flwTable.add(flwTr);
//
        /*====================================< 7th Row >====================================*/
 /*====================================< 5th Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Return Quantity");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReturnQuantity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSKU));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="txtHReceivedFrom">
    interface TxtHTxtHInvoicedItem_PrpAcs extends KeyField_T_PrpAcs<TxtHInvoicedItem_Mdl> {

        @Editor.Path("MedicineCode")
        ValueProvider<TxtHInvoicedItem_Mdl, String> medicineCode();

        @Editor.Path("MedicineName")
        ValueProvider<TxtHInvoicedItem_Mdl, String> medicineName();

        @Editor.Path("MedicineBatchCode")
        ValueProvider<TxtHInvoicedItem_Mdl, String> medicineBatchCode();

        @Editor.Path("MedicineBatchNo")
        ValueProvider<TxtHInvoicedItem_Mdl, String> medicineBatchNo();

        @Editor.Path("Qty")
        ValueProvider<TxtHInvoicedItem_Mdl, String> qty();
    }

    final DmTextHelp<TxtHInvoicedItem_Mdl, TxtHTxtHInvoicedItem_PrpAcs> txtHInvoicedItem = new DmTextHelp(GWT.create(TxtHTxtHInvoicedItem_PrpAcs.class), 6, false);

    private void iniTxtHReceivedFrom() {
        txtHInvoicedItem.getDmGrid().addTextColumn(txtHInvoicedItem.getDmGrid().getPropertyAccess().medicineName(), "Name", 300, "medicineName");
        txtHInvoicedItem.getDmGrid().addTextColumn(txtHInvoicedItem.getDmGrid().getPropertyAccess().medicineCode(), "Medicine Code", 0, "medicineCode");
        txtHInvoicedItem.getDmGrid().addTextColumn(txtHInvoicedItem.getDmGrid().getPropertyAccess().medicineBatchNo(), "Batch NO", 0, "medicineBatchNo");
        txtHInvoicedItem.getDmGrid().addTextColumn(txtHInvoicedItem.getDmGrid().getPropertyAccess().qty(), "Qty", 80, "qty");
        txtHInvoicedItem.getDmGrid().initializeGrid(txtHInvoicedItem.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHInvoicedItem.getHelper().addSort("medicineName", DmEnum.DmSortType.ASC);
        txtHInvoicedItem.getHelper().addSort("medicineCode", DmSortType.ASC);
    }
    //</editor-fold>

}
