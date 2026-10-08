package dataman.accounts.reports.client;

import dataman.accounts.reports.shared.RptBillHandleRegister_Srv;
import dataman.accounts.reports.shared.RptBillHandleRegister_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.util.HashMap;
import java.util.List;

public class RptBillHandleRegister extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new lmAccounts().BILL_STATUS_REPORT.caption;
    ServiceInvoker<RptBillHandleRegister_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(12, true);
    DmTextBox txtToDate = new DmTextBox(12, true);

//    //<editor-fold defaultstate="collapsed" desc="txtHSite">
//    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5, true);
//
//    private void iniTxtHSite() {
//        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Site", 200, "name");
//        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
//        txtHSite.getHelper().addSort("name", DmEnum.DmSortType.ASC);
//    }
//    //</editor-fold>
//
//    //<editor-fold defaultstate="collapsed" desc="txtHPbill">
//    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHPbill = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
//
//    private void iniTxtHPBill() {
//        txtHPbill.getDmGrid().addTextColumn(txtHPbill.getDmGrid().getPropertyAccess().name(), "Voucher No.", 130, "name");
//        txtHPbill.getDmGrid().addTextColumn(txtHPbill.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Ref. No.", 100, "partyBillNo");
//        txtHPbill.getDmGrid().initializeGrid(txtHPbill.getDmGrid().getPropertyAccess().code(), 250, 0, 2);
//        txtHPbill.getHelper().addSort("v_Date", DmEnum.DmSortType.ASC);
//        txtHPbill.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
//        txtHPbill.getHelper().addSort("v_No", DmEnum.DmSortType.ASC);
//        txtHPbill.getHelper().addSort("name", DmEnum.DmSortType.ASC);
//
//    }
//    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHBillType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBillType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7);

    private void iniTxtHPurchaseType() {
        txtHBillType.getDmGrid().addTextColumn(txtHBillType.getDmGrid().getPropertyAccess().name(), "Type", 1, "name");
        txtHBillType.getDmGrid().initializeGrid(txtHBillType.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHBillType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    
     //<editor-fold defaultstate="collapsed" desc="txtHBillType">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHLastLoc = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 7);

    private void iniTxtHLastLoc() {
        txtHLastLoc.getDmGrid().addTextColumn(txtHLastLoc.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHLastLoc.getDmGrid().initializeGrid(txtHLastLoc.getDmGrid().getPropertyAccess().code(), 250, 0, 2);
        txtHLastLoc.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMappingMod">
    final DmTextHelp<KeyFieldExtended, dataman.dmBase.self.shared.KeyField_PrpAcs> txtHMappingMod = new DmTextHelp(GWT.create(dataman.dmBase.self.shared.KeyField_PrpAcs.class), 5, false);

    public void iniTxtHMappingMod() {
        txtHMappingMod.getDmGrid().addTextColumn(txtHMappingMod.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHMappingMod.getDmGrid().initializeGrid(txtHMappingMod.getDmGrid().getPropertyAccess().code(), 125, 0, 1);
        txtHMappingMod.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSupplier"> 
    interface KeyField_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> sharedModifiedBy();

    }
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSupplier = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHSupplier() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().sharedModifiedBy(), "City", 100, "city");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 400, 0, 2);
        txtHSupplier.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

//    //<editor-fold defaultstate="collapsed" desc="txtHMedGroup">
//    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
//
//    private void iniTxtHMedGroup() {
//        txtHMedGroup.getDmGrid().addTextColumn(txtHMedGroup.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
//        txtHMedGroup.getDmGrid().initializeGrid(txtHMedGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
//        txtHMedGroup.getHelper().addSort("name", DmEnum.DmSortType.ASC);
//    }
//    //</editor-fold>
//
//    //<editor-fold defaultstate="collapsed" desc="TxtHCompany">
//    interface TxtHCompany_PrpAcs extends KeyField_T_PrpAcs<TxtHCompany_Mdl> {
//
//        @Editor.Path("ShortName")
//        ValueProvider<TxtHCompany_Mdl, String> shortName();
//    }
//
//    final DmTextHelpMulti<TxtHCompany_Mdl, TxtHCompany_PrpAcs> txtHCompany = new DmTextHelpMulti(GWT.create(TxtHCompany_PrpAcs.class), 7);
//
//    private void iniTxtHCompany() {
//        txtHCompany.getDmGrid().addTextColumn(txtHCompany.getDmGrid().getPropertyAccess().name(), "Name", 0, "companyName");
//        txtHCompany.getDmGrid().addTextColumn(txtHCompany.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
//        txtHCompany.getDmGrid().initializeGrid(txtHCompany.getDmGrid().getPropertyAccess().code(), 275, 0, 2);
//
//        txtHCompany.getHelper().addSort("companyName", DmSortType.ASC);
//    }
//
//    //</editor-fold>
//    //<editor-fold defaultstate="collapsed" desc="txtHMedCat">
//    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedCat = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);
//
//    private void iniTxtHMedCat() {
//        txtHMedCat.getDmGrid().addTextColumn(txtHMedCat.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
//        txtHMedCat.getDmGrid().initializeGrid(txtHMedCat.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
//        txtHMedCat.getHelper().addSort("name", DmEnum.DmSortType.ASC);
//    }
//    //</editor-fold>
//
//    //<editor-fold defaultstate="collapsed" desc="txtHMedicine">
//    interface GridMed_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHMedicine_Mdl> {
//
//        @Editor.Path("Unit")
//        ValueProvider<TxtHMedicine_Mdl, String> unit();
//
//        @Editor.Path("ManualCode")
//        ValueProvider<TxtHMedicine_Mdl, String> manualCode();
//    }
//    DmTextHelpMulti<TxtHMedicine_Mdl, GridMed_PrpAcs> txtHMedicine = new DmTextHelpMulti(GWT.create(GridMed_PrpAcs.class), 10);
//
//    private void iniTxtHMedicine() {
//        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
//        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().name(), "Manual Code", 100, "manualCode");
//        txtHMedicine.getDmGrid().initializeGrid(txtHMedicine.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 0, 2);
//        txtHMedicine.getHelper().addSort("name", DmEnum.DmSortType.ASC);
//    }
//
//    //</editor-fold>
//    // /<editor-fold defaultstate="collapsed" desc="txtHBatchNo">
//    interface TxtHBatchNo_PrpAcs extends KeyField_T_PrpAcs<BatchNo_Mdl> {
//
//        @Editor.Path("ExpiryDate")
//        ValueProvider<BatchNo_Mdl, String> expiryDate();
//
//        @Editor.Path("Mrp")
//        ValueProvider<BatchNo_Mdl, String> mrp();
//
//        @Editor.Path("SaleRate")
//        ValueProvider<BatchNo_Mdl, String> saleRate();
//
//        @Editor.Path("MedicineName")
//        ValueProvider<BatchNo_Mdl, String> medicineName();
//
//    }
//
//    final DmTextHelpMulti<BatchNo_Mdl, TxtHBatchNo_PrpAcs> txtHBatchNo = new DmTextHelpMulti<>(GWT.create(TxtHBatchNo_PrpAcs.class), 5);
//
//    private void iniTxtHBatchNo() {
//        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().name(), "Batch No.", 60, "batchNo");
//        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().medicineName(), "Medicine", 120, "medicineName");
//        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().expiryDate(), "Expiry Date", 80, "expiryDate", DmEnum.DmDataType.DATE);
//        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().mrp(), "MRP", 60, "");
//        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().saleRate(), "Sale Rate", 80, "");
//        txtHBatchNo.getDmGrid().initializeGrid(txtHBatchNo.getDmGrid().getPropertyAccess().code(), 550, 0, 2);
//
//        txtHBatchNo.getHelper().addSort("medicineName", DmEnum.DmSortType.ASC);
//        txtHBatchNo.getHelper().addSort("expiryDate", DmEnum.DmSortType.ASC);
//        txtHBatchNo.getHelper().addSort("batchNo", DmEnum.DmSortType.ASC);
//    }
//
//    //</editor-fold>  
    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }
    
             private void startDownloading(String fileName, String filePath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(DmRequestBuilder.getSessionId(),getFilter(), reportProperty,widgetName, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                     if (result.isStatus()) {
                    KeyFieldExtended data = result.getData();
                    if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                        startDownloading(data.getSharedKeyFieldName(), data.getSharedKeyFieldCode());
                    } else {
                        getReport(data.getSharedKeyFieldCode());
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    private void txtHelpEvents() {

        txtHBillType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBillType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBillType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBillType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBillType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
               
            }

          
        });

        txtHSupplier.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVendor(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSupplier.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSupplier).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        
          txtHLastLoc.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHLastLoc(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLastLoc).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLastLoc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLastLoc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHMappingMod.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHModule(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMappingMod).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMappingMod.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMappingMod).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

    }

    public RptBillHandleRegister(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptBillHandleRegister_Srv.class), RptBillHandleRegister.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtHelpEvents();
//        setDefaultValue();
        initialize(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {

        addControl("fromDate", "From Date", vl.add(txtFromDate));
        txtFromDate.setText(DmClient.getStartDate());

        addControl("toDate", "To Date", vl.add(txtToDate));
        txtToDate.setText(DmClient.getLoginDate());

        iniTxtHPurchaseType();
        addControl("billType", "Bill Type", vl.add(txtHBillType));

        iniTxtHMappingMod();
        addControl("module", "Module", vl.add(txtHMappingMod));

        iniTxtHSupplier();
        addControl("supplier", "Supplier", vl.add(txtHSupplier));
        
        iniTxtHLastLoc();
        addControl("lastLoc", "Last Location", vl.add(txtHLastLoc));

        swi.setWidget(this);
    }

//    private void setDefaultValue() {
//        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<KeyFieldExtended>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
//            }
//
//            @Override
//            public void onSuccess(DmResult<KeyFieldExtended> result) {
//                if (result.isStatus()) {
//                    KeyFieldExtended keyField = result.getData();
////                    txtHSite.setTag(keyField.getSharedKeyFieldCode());
////                    txtHSite.setText(keyField.getSharedKeyFieldName());
//                } else {
//                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
//                }
//            }
//        });
//
//    }

}
