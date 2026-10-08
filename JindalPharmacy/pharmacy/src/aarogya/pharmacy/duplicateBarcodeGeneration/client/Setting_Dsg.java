package aarogya.pharmacy.duplicateBarcodeGeneration.client;

import aarogya.pharmacy.duplicateBarcodeGeneration.client.*;
import aarogya.pharmacy.duplicateBarcodeGeneration.shared.PBillNoH_Mdl;
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
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class Setting_Dsg extends IsWidgetExtended {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    final DmTextBox txtFromDate = new DmTextBox(15, true);
    final DmTextBox txtToDate = new DmTextBox(15, true);

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 350, "name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 350, 0, 2);
        txtHDepartment.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHBarCode">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHBarCode = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6);

    private void iniTxtHBarCode() {
        txtHBarCode.getDmGrid().addTextColumn(txtHBarCode.getDmGrid().getPropertyAccess().name(), "Name", 350, "barCode");
        txtHBarCode.getDmGrid().initializeGrid(txtHBarCode.getDmGrid().getPropertyAccess().code(), 350, 0, 2);
        txtHBarCode.getHelper().addSort("v_date", DmSortType.DESC);
        txtHBarCode.getHelper().addSort("v_time", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHItem">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHItem = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6);

    private void iniTxtHItem() {
        txtHItem.getDmGrid().addTextColumn(txtHItem.getDmGrid().getPropertyAccess().name(), "Name", 350, "name");
        txtHItem.getDmGrid().initializeGrid(txtHItem.getDmGrid().getPropertyAccess().code(), 350, 0, 2);
        txtHItem.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHBatchNo">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHBatchNo = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6);

    private void iniTxtHBatchNo() {
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().name(), "Name", 200, "batchNo");
        txtHBatchNo.getDmGrid().initializeGrid(txtHBatchNo.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHBatchNo.getHelper().addSort("batchNo", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHColor">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHColor = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6);

    private void iniTxtHColor() {
        txtHColor.getDmGrid().addTextColumn(txtHColor.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHColor.getDmGrid().initializeGrid(txtHColor.getDmGrid().getPropertyAccess().code(), 150, 0, 2);
        txtHColor.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHSize">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSize = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHSize() {
        txtHSize.getDmGrid().addTextColumn(txtHSize.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHSize.getDmGrid().initializeGrid(txtHSize.getDmGrid().getPropertyAccess().code(), 150, 0, 2);
        txtHSize.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHManufacturingDate">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHManufacturingDate = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6);

    private void iniTxtHManufacturingDate() {
        txtHManufacturingDate.getDmGrid().addTextColumn(txtHManufacturingDate.getDmGrid().getPropertyAccess().name(), "Name", 100, "manufacturingDate");
        txtHManufacturingDate.getDmGrid().initializeGrid(txtHManufacturingDate.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHManufacturingDate.getHelper().addSort("manufacturingDate", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHExpiryDate">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHExpiryDate = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6);

    private void iniTxtHExpiryDate() {
        txtHExpiryDate.getDmGrid().addTextColumn(txtHExpiryDate.getDmGrid().getPropertyAccess().name(), "Name", 100, "expiryDate");
        txtHExpiryDate.getDmGrid().initializeGrid(txtHExpiryDate.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHExpiryDate.getHelper().addSort("expiryDate", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHSaleRate">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSaleRate = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHSaleRate() {
        txtHSaleRate.getDmGrid().addTextColumn(txtHSaleRate.getDmGrid().getPropertyAccess().name(), "Name", 150, "skuSaleRate");
        txtHSaleRate.getDmGrid().initializeGrid(txtHSaleRate.getDmGrid().getPropertyAccess().code(), 150, 0, 2);
        txtHSaleRate.getHelper().addSort("skuSaleRate", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHMRP">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMRP = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6);

    private void iniTxtHMRP() {
        txtHMRP.getDmGrid().addTextColumn(txtHMRP.getDmGrid().getPropertyAccess().name(), "Name", 150, "mrp");
        txtHMRP.getDmGrid().initializeGrid(txtHMRP.getDmGrid().getPropertyAccess().code(), 150, 0, 2);
        txtHMRP.getHelper().addSort("mrp", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPBillNo">
    interface TxtHPBillNo_PrpAcs extends KeyField_T_PrpAcs<PBillNoH_Mdl> {

        @Editor.Path("Description")
        ValueProvider<PBillNoH_Mdl, String> description();

        @Editor.Path("PartyBillDate")
        ValueProvider<PBillNoH_Mdl, String> partyBillDate();

        @Editor.Path("PartyBillNO")
        ValueProvider<PBillNoH_Mdl, String> partyBillNo();

        @Editor.Path("SupplierName")
        ValueProvider<PBillNoH_Mdl, String> supplierName();

        @Editor.Path("Pdate")
        ValueProvider<PBillNoH_Mdl, String> pdate();
    }
    final DmTextHelpMulti<PBillNoH_Mdl, TxtHPBillNo_PrpAcs> txtHPBillNo = new DmTextHelpMulti(GWT.create(TxtHPBillNo_PrpAcs.class), 6);

    private void iniTxtHPBillNo() {
        txtHPBillNo.getDmGrid().addTextColumn(txtHPBillNo.getDmGrid().getPropertyAccess().name(), "RecId", 140, "recid");
        txtHPBillNo.getDmGrid().addTextColumn(txtHPBillNo.getDmGrid().getPropertyAccess().partyBillNo(), "Party Bill No", 115, "partyBillNo");
        txtHPBillNo.getDmGrid().addTextColumn(txtHPBillNo.getDmGrid().getPropertyAccess().pdate(), "P.Date", 80, "v_date");
        txtHPBillNo.getDmGrid().addTextColumn(txtHPBillNo.getDmGrid().getPropertyAccess().description(), "Description", 145, "Description");
        txtHPBillNo.getDmGrid().addTextColumn(txtHPBillNo.getDmGrid().getPropertyAccess().partyBillDate(), "P.Bill Date", 80, "partyBillDate");
        txtHPBillNo.getDmGrid().addTextColumn(txtHPBillNo.getDmGrid().getPropertyAccess().supplierName(), "Supplier", 200, "supplierName");
        txtHPBillNo.getDmGrid().initializeGrid(txtHPBillNo.getDmGrid().getPropertyAccess().code(), 800, 0, 1);
        txtHPBillNo.getHelper().addSort("v_date", DmSortType.DESC);
        txtHPBillNo.getHelper().addSort("recid", DmSortType.DESC);
    }

    //</editor-fold>
    public Setting_Dsg() {
        ini();
        iniTxtHPBillNo();
        iniToolContorls();
        iniTxtHDepartment();
        iniTxtHBarCode();
        iniTxtHItem();
        iniTxtHColor();
        iniTxtHBatchNo();
        iniTxtHMRP();
        iniTxtHSaleRate();
        iniTxtHExpiryDate();
        iniTxtHManufacturingDate();
        iniTxtHSize();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("500px");
        popupPanel.setHeight("75px");

        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("OK");
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
        DmClient.setStyle(flwTable, "98%", "146px", null, null, DmPosition.CENTER, true);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("From Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("To Date");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Department");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Bar Code");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBarCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //=====================================================================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Item");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHItem));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("BatchNo.");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBatchNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //=====================================================================================

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Color");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHColor));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Size");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSize));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //=====================================================================================

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Manufa. date");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHManufacturingDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Sale rate");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSaleRate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //=====================================================================================

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Expiry Date");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHExpiryDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("MRP");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMRP));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //=====================================================================================

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("PBill No.");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPBillNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        return flwTable;
    }

}
