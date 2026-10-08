package aarogya.pharmacy.expiryModification.client;

import aarogya.pharmacy.expiryModification.shared.TxtHBatchNo_Mdl;
import aarogya.pharmacy.expiryModification.shared.TxtHMedicineName_Mdl;
import aarogya.pharmacy.expiryModification.shared.dmFind_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ExpiryModification_Dsg extends IsWidgetExtended {

    final pmPharmacy pmPharmacy = new pmPharmacy();
    final String widgetName = pmPharmacy.EXPIRY_MODIFICATION.caption;
    final String moduleName = pmPharmacy.moduleName;

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtSKU = new DmTextBox(50, false);

    final DmTextBox txtRateDisplay = new DmTextBox();
    final DmTextBox txtMRPDisplay = new DmTextBox();
    final DmTextBox txtSaleRateDisplay = new DmTextBox();

    final DmTextBox txtExpiryDate = new DmTextBox(11, true);
    final DmTextBoxNumeric txtMRP = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtSaleRate = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtRate = new DmTextBoxNumeric(2, false, 10, false);

    //<editor-fold defaultstate="collapsed" desc="txtHFilterType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHFilterType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, false);

    private void iniTxtHFilterType() {
        txtHFilterType.getDmGrid().addTextColumn(txtHFilterType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHFilterType.getDmGrid().initializeGrid(txtHFilterType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHFilterType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHFilterValue">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHFilterValue = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, false);

    private void iniTxtHFilterValue() {
        txtHFilterValue.getDmGrid().addTextColumn(txtHFilterValue.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHFilterValue.getDmGrid().initializeGrid(txtHFilterValue.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHFilterValue.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedicineName">
    interface TxtHMedicineName_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHMedicineName_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHMedicineName_Mdl, String> manualCode();

        @Editor.Path("SkuName")
        ValueProvider<TxtHMedicineName_Mdl, String> skuName();

    }

    final DmTextHelp<TxtHMedicineName_Mdl, TxtHMedicineName_PrpAcs> txtHMedicineName = new DmTextHelp(GWT.create(TxtHMedicineName_PrpAcs.class), 10, true);

    private void iniTxtHMedicineName() {
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().name(), "Medicine Name", 200, "name");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHMedicineName.getDmGrid().addTextColumn(txtHMedicineName.getDmGrid().getPropertyAccess().skuName(), "SKU", 50, "skuName");
        txtHMedicineName.getDmGrid().initializeGrid(txtHMedicineName.getDmGrid().getPropertyAccess().fieldHashCode(), 600, 0, 1);
        txtHMedicineName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHBatchNo">
    interface TxtHBatchNo_PrpAcs extends KeyField_T_PrpAcs<TxtHBatchNo_Mdl> {

        @Editor.Path("ExpireDate")
        ValueProvider<TxtHBatchNo_Mdl, String> expireDate();

        @Editor.Path("Mrp")
        ValueProvider<TxtHBatchNo_Mdl, String> mrp();

        @Editor.Path("SaleRate")
        ValueProvider<TxtHBatchNo_Mdl, String> saleRate();

        @Editor.Path("Barcode")
        ValueProvider<TxtHBatchNo_Mdl, String> barcode();

    }

    final DmTextHelp<TxtHBatchNo_Mdl, TxtHBatchNo_PrpAcs> txtHBatchNo = new DmTextHelp(GWT.create(TxtHBatchNo_PrpAcs.class), 10, true);

    private void iniTxtHBatchNo() {
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().name(), "Batch No.", 1, "batchNo");
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().expireDate(), "Expiry Date", 80, "expiryDate", DmDataType.DATE);
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().mrp(), "MRP", 100, "mrp", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().saleRate(), "Sale Rate", 80, "saleRate", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().barcode(), "Barcode", 150, "barcode");

        txtHBatchNo.getDmGrid().initializeGrid(txtHBatchNo.getDmGrid().getPropertyAccess().code(), 600, 0, 1);

        txtHBatchNo.getHelper().addSort("batchNo", DmSortType.ASC);
        txtHBatchNo.getHelper().addSort("expiryDate", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDepartment.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<dmFind_Mdl> {

        @Editor.Path("BatchNo")
        ValueProvider<dmFind_Mdl, String> batchNo();

        @Editor.Path("VoucherDate")
        ValueProvider<dmFind_Mdl, String> voucherDate();

        @Editor.Path("OldExpiryDate")
        ValueProvider<dmFind_Mdl, String> oldExpiryDate();

        @Editor.Path("NewExpiryDate")
        ValueProvider<dmFind_Mdl, String> newExpiryDate();

    }

    final DmFind<dmFind_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 12);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Medicine Name", 1, "itemName");

        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().batchNo(), "Batch No.", 300, "batchNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 150, "v_Date");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().oldExpiryDate(), "Old Expiry", 150, "oldexpiryDate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().newExpiryDate(), "New Expiry", 150, "newExpiryDate");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 1);

        dmFind.getHelper().addSort("itemName", DmSortType.ASC);
    }
    //</editor-fold>

    public ExpiryModification_Dsg() {
        ini();
        iniFind();
        iniTxtHDepartment();
        iniTxtHMedicineName();
        iniTxtHBatchNo();
        iniTxtHFilterType();
        iniTxtHFilterValue();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.setCellHeight(flw, "100%");

        flwForm.add(vrtInnerForm);

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "250px", null, null, DmPosition.CENTER, true);

//        /*====================================< 1st Row >====================================*/
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Department");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHDepartment));
//        flwTr.add(flwTd);
//
//        flwTable.add(flwTr);
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Filter Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHFilterType));
        flwTr.add(flwTd);

//        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Filter Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHFilterValue));
        flwTr.add(flwTd);

//        flwTable.add(flwTr);
        //====================================< 2st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMedicineName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //================================< 3rd Row >===================================================//

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Batch No.");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBatchNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 4nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("SKU");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSKU));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 5rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Rate/Pack");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtRate));
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(txtRateDisplay);
//        txtRateDisplay.setReadOnly(true);
//        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Expiry Date");
//        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
//        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtExpiryDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 6th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("MRP/Pack");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMRP));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtMRPDisplay);
        txtMRPDisplay.setReadOnly(true);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sale Rate/Pack");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSaleRate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtSaleRateDisplay);
        txtSaleRateDisplay.setReadOnly(true);

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
