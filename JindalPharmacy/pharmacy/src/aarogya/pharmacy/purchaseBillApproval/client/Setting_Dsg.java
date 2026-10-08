package aarogya.pharmacy.purchaseBillApproval.client;

import aarogya.pharmacy.purchaseBillApproval.shared.TxtHCategory_Mdl;
import aarogya.pharmacy.purchaseBillApproval.shared.TxtHPurchaseBillNo_Mdl;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.*;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;

abstract class Setting_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final DmTextBox txtFromDate = new DmTextBox(15, false);
    final DmTextBox txtToDate = new DmTextBox(15, false);

    Style style;

    //<editor-fold defaultstate="collapsed" desc="txtHBillNo">
    interface TxtHIndentNo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHPurchaseBillNo_Mdl> {

        @Editor.Path("BillType")
        ValueProvider<TxtHPurchaseBillNo_Mdl, String> reqType();

        @Editor.Path("BillDate")
        ValueProvider<TxtHPurchaseBillNo_Mdl, String> reqDate();
    }

    final DmTextHelpMulti<TxtHPurchaseBillNo_Mdl, TxtHIndentNo_PrpAcs> txtHBillNo = new DmTextHelpMulti(GWT.create(TxtHIndentNo_PrpAcs.class), 7);

    private void iniTxtHIndentNo() {
        txtHBillNo.getDmGrid().addTextColumn(txtHBillNo.getDmGrid().getPropertyAccess().name(), "Bill No.", 90, "pbRecId");
        txtHBillNo.getDmGrid().addTextColumn(txtHBillNo.getDmGrid().getPropertyAccess().reqType(), "Bill Type", 80, "category");
        txtHBillNo.getDmGrid().addTextColumn(txtHBillNo.getDmGrid().getPropertyAccess().reqDate(), "Bill Date", 80, "pbDate", DmDataType.DATE);
        txtHBillNo.getDmGrid().initializeGrid(txtHBillNo.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 0, 2);

        txtHBillNo.getHelper().addSort("pbDate", DmSortType.DESC);
        txtHBillNo.getHelper().addSort("pbRecId", DmSortType.ASC);
    }
    //</editor-fold>

    interface TxtHCategoy_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHCategory_Mdl> {

        @Editor.Path("Description")
        ValueProvider<TxtHCategory_Mdl, String> description();

    }
    final DmTextHelpMulti<TxtHCategory_Mdl, TxtHCategoy_PrpAcs> txtHCategory = new DmTextHelpMulti(GWT.create(TxtHCategoy_PrpAcs.class), 2);

    private void iniTxtHCategory() {
        txtHCategory.getDmGrid().addTextColumn(txtHCategory.getDmGrid().getPropertyAccess().description(), "Description", 150, "Name");
        txtHCategory.getDmGrid().addTextColumn(txtHCategory.getDmGrid().getPropertyAccess().name(), "Category", 70, "Category");
        txtHCategory.getDmGrid().initializeGrid(txtHCategory.getDmGrid().getPropertyAccess().fieldHashCode(), 250, 0, 1);

        txtHCategory.getHelper().addSort("Name", DmSortType.ASC);
    }
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSupplier = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 4);

    private void iniTxtHSupplier() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSupplier.getHelper().addSort("Name", DmSortType.ASC);
    }

    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHGrnNo = new DmTextHelpMulti<>(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHGRNNo() {
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHGrnNo.getDmGrid().initializeGrid(txtHGrnNo.getDmGrid().getPropertyAccess().code(), 200, 0, 2);

        txtHGrnNo.getHelper().addSort("Name", DmSortType.ASC);
    }

    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHItemName() {
        txtHMedName.getDmGrid().addTextColumn(txtHMedName.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHMedName.getDmGrid().initializeGrid(txtHMedName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);

        txtHMedName.getHelper().addSort("Name", DmSortType.ASC);
    }

    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHStatus = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniTxtHStatus() {
        txtHStatus.getDmGrid().addTextColumn(txtHStatus.getDmGrid().getPropertyAccess().name(), "Status", 150, "Name");
        txtHStatus.getDmGrid().initializeGrid(txtHStatus.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHStatus.getHelper().addSort("Name", DmSortType.ASC);
    }

    public Setting_Dsg() {
        ini();
        iniTxtHItemName();
        iniTxtHGRNNo();
        iniTxtHSupplier();
        iniTxtHIndentNo();
        iniTxtHStatus();
        iniTxtHCategory();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("400px");
        popupPanel.setHeight("130px");
        iniToolControls();
        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolControls() {
        InlineLabel label;
        Anchor aBtn;
        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

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
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "130px", null, null, DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("From Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("To Date");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Status");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHStatus));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Type");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCategory));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Supplier");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSupplier));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Medicine Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("GRN No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGrnNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Bill No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBillNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
//        lblTemp = new DmLabel("Status");
//        flwTd.add(lblTemp);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHStatus));
//        flwTr.add(flwTd);
//
//        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
        return flwTable;
    }

}
