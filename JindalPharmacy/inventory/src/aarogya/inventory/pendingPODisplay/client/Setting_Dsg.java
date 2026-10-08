package aarogya.inventory.pendingPODisplay.client;

import aarogya.inventory.pendingPODisplay.shared.TxtHItemName_Mdl;
import aarogya.inventory.pendingPODisplay.shared.TxtHSupplier_Mdl;
import aarogya.inventory.pendingPODisplay.shared.TxtHPurchaseOrder_Mdl;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
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
    final DmTextBox txtRequiredDate = new DmTextBox(15, false);

    //<editor-fold defaultstate="collapsed" desc="txtHPurchaseOrder">
    interface TxtHPurchaseOrder_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHPurchaseOrder_Mdl> {

        @Editor.Path("PoType")
        ValueProvider<TxtHPurchaseOrder_Mdl, String> reqType();

        @Editor.Path("PoDate")
        ValueProvider<TxtHPurchaseOrder_Mdl, String> reqDate();
    }

    final DmTextHelpMulti<TxtHPurchaseOrder_Mdl, TxtHPurchaseOrder_PrpAcs> txtHPurchaseOrder = new DmTextHelpMulti(GWT.create(TxtHPurchaseOrder_PrpAcs.class), 7);

    private void iniTxtHPurchaseOrder() {
        txtHPurchaseOrder.getDmGrid().addTextColumn(txtHPurchaseOrder.getDmGrid().getPropertyAccess().name(), "Order No.", 120, "pOrderNo");
        txtHPurchaseOrder.getDmGrid().addTextColumn(txtHPurchaseOrder.getDmGrid().getPropertyAccess().reqType(), "Order Type", 90, "category");
        txtHPurchaseOrder.getDmGrid().addTextColumn(txtHPurchaseOrder.getDmGrid().getPropertyAccess().reqDate(), "Order Date", 90, "pOrderDate", DmDataType.DATE);
        txtHPurchaseOrder.getDmGrid().initializeGrid(txtHPurchaseOrder.getDmGrid().getPropertyAccess().fieldHashCode(), 330, 0, 2);

        txtHPurchaseOrder.getHelper().addSort("pOrderDate", DmSortType.DESC);
        txtHPurchaseOrder.getHelper().addSort("pOrderNo", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSupplier">
    interface TxtHSupplierName_PrpAcs extends KeyField_T_PrpAcs<TxtHSupplier_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHSupplier_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHSupplier_Mdl, String> cityName();

    }
    final DmTextHelpMulti<TxtHSupplier_Mdl, TxtHSupplierName_PrpAcs> txtHSupplier = new DmTextHelpMulti(GWT.create(TxtHSupplierName_PrpAcs.class), 5);

    private void iniTxtHSupplier() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 250, "name");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 0, "manualCode");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().cityName(), "City Name", 100, "cityName");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 350, 0, 2);
        txtHSupplier.getHelper().addSort("name", DmSortType.ASC);
        txtHSupplier.getHelper().addSort("cityName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHItemName">
    interface TxtHItemName_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHItemName_Mdl> {

        @Editor.Path("PurchaseUnitName")
        ValueProvider<TxtHItemName_Mdl, String> purchaseUnitName();

    }

    final DmTextHelpMulti<TxtHItemName_Mdl, TxtHItemName_PrpAcs> txtHMedName = new DmTextHelpMulti(GWT.create(TxtHItemName_PrpAcs.class), 5);

    private void iniTxtHItemName() {
        txtHMedName.getDmGrid().addTextColumn(txtHMedName.getDmGrid().getPropertyAccess().name(), "Name", 250, "name");
        txtHMedName.getDmGrid().addTextColumn(txtHMedName.getDmGrid().getPropertyAccess().purchaseUnitName(), "Unit", 50, "purchaseUnitName");
        txtHMedName.getDmGrid().initializeGrid(txtHMedName.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 0, 2);
        txtHMedName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public Setting_Dsg() {
        ini();
        iniTxtHItemName();
        iniTxtHSupplier();
        iniTxtHPurchaseOrder();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("600px");
        popupPanel.setHeight("100px");
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
        DmClient.setStyle(flwTable, "100%", "65px", null, null, DmPosition.CENTER, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("From Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("To Date");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Medicine Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Order No.");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPurchaseOrder));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//        
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Supplier");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSupplier));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Required Date");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRequiredDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
        return flwTable;
    }

}
