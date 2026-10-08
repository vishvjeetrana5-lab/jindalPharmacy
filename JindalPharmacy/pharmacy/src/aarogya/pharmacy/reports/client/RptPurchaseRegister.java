package aarogya.pharmacy.reports.client;

import aarogya.pharmacy.reports.shared.BatchNo_Mdl;
import aarogya.pharmacy.reports.shared.RptPurchaseRegister_Srv;
import aarogya.pharmacy.reports.shared.RptPurchaseRegister_SrvAsync;
import aarogya.pharmacy.reports.shared.TxtHCompany_Mdl;
import aarogya.pharmacy.reports.shared.TxtHMedicine_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
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
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.HashMap;
import java.util.List;

public class RptPurchaseRegister extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new pmPharmacy().PURCHASE_REGISTER.caption;
    ServiceInvoker<RptPurchaseRegister_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(12, true);
    DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Site", 200, "name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSite.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPbill">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHPbill = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHPBill() {
        txtHPbill.getDmGrid().addTextColumn(txtHPbill.getDmGrid().getPropertyAccess().name(), "Voucher No.", 130, "name");
        txtHPbill.getDmGrid().addTextColumn(txtHPbill.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Ref. No.", 100, "partyBillNo");
        txtHPbill.getDmGrid().initializeGrid(txtHPbill.getDmGrid().getPropertyAccess().code(), 250, 0, 2);
        txtHPbill.getHelper().addSort("v_Date", DmEnum.DmSortType.ASC);
        txtHPbill.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        txtHPbill.getHelper().addSort("v_No", DmEnum.DmSortType.ASC);
        txtHPbill.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHBillType">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHBillType = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 3);

    private void iniTxtHPurchaseType() {
        txtHBillType.getDmGrid().addTextColumn(txtHBillType.getDmGrid().getPropertyAccess().name(), "Type", 1, "name");
        txtHBillType.getDmGrid().initializeGrid(txtHBillType.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHBillType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIsApproved">
    DmTextHelp<KeyFieldExtended, dataman.dmBase.self.shared.KeyField_PrpAcs> txtHIsApproved = new DmTextHelp(GWT.create(dataman.dmBase.self.shared.KeyField_PrpAcs.class), 5, true, false);

    private void initxtHIsApproved() {
        txtHIsApproved.getDmGrid().addTextColumn(txtHIsApproved.getDmGrid().getPropertyAccess().name(), "Type", 1, "Type");
        txtHIsApproved.getDmGrid().initializeGrid(txtHIsApproved.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRate/MarginFix">
    DmTextHelp<KeyFieldExtended, dataman.dmBase.self.shared.KeyField_PrpAcs> txtHRateMarginFix = new DmTextHelp(GWT.create(dataman.dmBase.self.shared.KeyField_PrpAcs.class), 5, true, false);

    private void initxtHRateMarginFix() {
        txtHRateMarginFix.getDmGrid().addTextColumn(txtHRateMarginFix.getDmGrid().getPropertyAccess().name(), "Type", 1, "Type");
        txtHRateMarginFix.getDmGrid().initializeGrid(txtHRateMarginFix.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
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

    //<editor-fold defaultstate="collapsed" desc="txtHPorder">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHPorder = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHPorder() {
        txtHPorder.getDmGrid().addTextColumn(txtHPorder.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHPorder.getDmGrid().initializeGrid(txtHPorder.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHPorder.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHMedGroup">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHMedGroup() {
        txtHMedGroup.getDmGrid().addTextColumn(txtHMedGroup.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHMedGroup.getDmGrid().initializeGrid(txtHMedGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMedGroup.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHCompany">
    interface TxtHCompany_PrpAcs extends KeyField_T_PrpAcs<TxtHCompany_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHCompany_Mdl, String> shortName();
    }

    final DmTextHelpMulti<TxtHCompany_Mdl, TxtHCompany_PrpAcs> txtHCompany = new DmTextHelpMulti(GWT.create(TxtHCompany_PrpAcs.class), 7);

    private void iniTxtHCompany() {
        txtHCompany.getDmGrid().addTextColumn(txtHCompany.getDmGrid().getPropertyAccess().name(), "Name", 0, "companyName");
        txtHCompany.getDmGrid().addTextColumn(txtHCompany.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
        txtHCompany.getDmGrid().initializeGrid(txtHCompany.getDmGrid().getPropertyAccess().code(), 275, 0, 2);

        txtHCompany.getHelper().addSort("companyName", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHMedCat">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedCat = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHMedCat() {
        txtHMedCat.getDmGrid().addTextColumn(txtHMedCat.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHMedCat.getDmGrid().initializeGrid(txtHMedCat.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMedCat.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSubCat">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSubCat = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHSubCat() {
        txtHSubCat.getDmGrid().addTextColumn(txtHSubCat.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHSubCat.getDmGrid().initializeGrid(txtHSubCat.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSubCat.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedicine">
    interface GridMed_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHMedicine_Mdl> {

        @Editor.Path("Unit")
        ValueProvider<TxtHMedicine_Mdl, String> unit();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHMedicine_Mdl, String> manualCode();
    }
    DmTextHelpMulti<TxtHMedicine_Mdl, GridMed_PrpAcs> txtHMedicine = new DmTextHelpMulti(GWT.create(GridMed_PrpAcs.class), 10);

    private void iniTxtHMedicine() {
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().name(), "Manual Code", 100, "manualCode");
        txtHMedicine.getDmGrid().initializeGrid(txtHMedicine.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 0, 2);
        txtHMedicine.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    // /<editor-fold defaultstate="collapsed" desc="txtHBatchNo">
    interface TxtHBatchNo_PrpAcs extends KeyField_T_PrpAcs<BatchNo_Mdl> {

        @Editor.Path("ExpiryDate")
        ValueProvider<BatchNo_Mdl, String> expiryDate();

        @Editor.Path("Mrp")
        ValueProvider<BatchNo_Mdl, String> mrp();

        @Editor.Path("SaleRate")
        ValueProvider<BatchNo_Mdl, String> saleRate();

        @Editor.Path("MedicineName")
        ValueProvider<BatchNo_Mdl, String> medicineName();

    }

    final DmTextHelpMulti<BatchNo_Mdl, TxtHBatchNo_PrpAcs> txtHBatchNo = new DmTextHelpMulti<>(GWT.create(TxtHBatchNo_PrpAcs.class), 5);

    private void iniTxtHBatchNo() {
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().name(), "Batch No.", 60, "batchNo");
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().medicineName(), "Medicine", 120, "medicineName");
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().expiryDate(), "Expiry Date", 80, "expiryDate", DmEnum.DmDataType.DATE);
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().mrp(), "MRP", 60, "");
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().saleRate(), "Sale Rate", 80, "");
        txtHBatchNo.getDmGrid().initializeGrid(txtHBatchNo.getDmGrid().getPropertyAccess().code(), 550, 0, 2);

        txtHBatchNo.getHelper().addSort("medicineName", DmEnum.DmSortType.ASC);
        txtHBatchNo.getHelper().addSort("expiryDate", DmEnum.DmSortType.ASC);
        txtHBatchNo.getHelper().addSort("batchNo", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>  
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
        SrvAsync.invokeRPC(true).execute(DmRequestBuilder.getSessionId(), getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                        KeyFieldExtended kf = result.getData();
                        startDownloading(DmShared.xNull(kf.getSharedKeyFieldName()), DmShared.xNull(kf.getSharedKeyFieldCode()));
                    } else {
                        getReport(result.getData().getSharedKeyFieldName());
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
        txtHBatchNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<BatchNo_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBatchNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<BatchNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBatchNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<BatchNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBatchNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBatchNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHBillType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
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
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
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

        txtHMedGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedGroup).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHMedCat.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineCat(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedCat).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedCat.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedCat).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHSubCat.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSubCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSubCat).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSubCat.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSubCat).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHMedicine.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHMedicine_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicine(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicine).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMedicine_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMedicine.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicine).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHPorder.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPorder(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPorder).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPorder.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPorder).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });

            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });
        txtHPbill.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPbill(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPbill).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPbill.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPbill).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHCompany.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHCompany_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCompany(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCompany_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCompany).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCompany_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCompany).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHIsApproved.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIsActive(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsApproved).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsApproved.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsApproved).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHRateMarginFix.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRateMarginFix(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRateMarginFix).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRateMarginFix.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRateMarginFix).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

    }

    public RptPurchaseRegister(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptPurchaseRegister_Srv.class), RptPurchaseRegister.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtHelpEvents();
        setDefaultValue();
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

        iniTxtHSite();
        addControl("site", "Site", vl.add(txtHSite));

        iniTxtHPurchaseType();
        addControl("billType", "Bill Type", vl.add(txtHBillType));

        iniTxtHPorder();
        addControl("pOrder", "PO No.", vl.add(txtHPorder));

        iniTxtHPBill();
        addControl("pBill", "Voucher No.", vl.add(txtHPbill));

        iniTxtHSupplier();
        addControl("supplier", "Supplier", vl.add(txtHSupplier));

        iniTxtHMedGroup();
        addControl("medGroup", "Medicine Group", vl.add(txtHMedGroup));

        iniTxtHMedCat();
        addControl("medCat", "Medicine Category", vl.add(txtHMedCat));

        iniTxtHSubCat();
        addControl("subCat", "Sub Category", vl.add(txtHSubCat));

        iniTxtHCompany();
        addControl("medCom", "Medicine Company", vl.add(txtHCompany));

        iniTxtHMedicine();
        addControl("medicine", "Medicine", vl.add(txtHMedicine));

        iniTxtHBatchNo();
        addControl("batchNo", "Batch No.", vl.add(txtHBatchNo));

        initxtHIsApproved();
        addControl("isApproved", "IsApprove", vl.add(txtHIsApproved));

        initxtHRateMarginFix();
        addControl("rateMarginFix", "Rate/MarginFix", vl.add(txtHRateMarginFix));

        swi.setWidget(this);
    }

    private void setDefaultValue() {
        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended keyField = result.getData();
                    txtHSite.setTag(keyField.getSharedKeyFieldCode());
                    txtHSite.setText(keyField.getSharedKeyFieldName());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

}
