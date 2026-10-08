package aarogya.pharmacy.reports.client;

import aarogya.pharmacy.reports.shared.BatchNo_Mdl;
import aarogya.pharmacy.reports.shared.RptMedicineInTransitRegister_Srv;
import aarogya.pharmacy.reports.shared.RptMedicineInTransitRegister_SrvAsync;
import aarogya.pharmacy.reports.shared.TxtHMedicine_Mdl;
import aarogya.pharmacy.reports.shared.TxtHRequisitionNo_Mdl;
import aarogya.pharmacy.reports.shared.TxtHTransferFromDept_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.DmDataType;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class RptMedicineInTransitRegister extends DmReportLayout implements IsWidget, BlurHandler {

    private final String widgetName = new pmPharmacy().MEDICINE_IN_TRANSIT_REGISTER.caption;

    private final ServiceInvoker<RptMedicineInTransitRegister_SrvAsync> srvAsync;
DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(12, true);
    private final DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHVoucherType = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Type", 1, "name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 2);

        txtHVoucherType.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartmentTo">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDepartmentTo = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHDepartmentTo() {
        txtHDepartmentTo.getDmGrid().addTextColumn(txtHDepartmentTo.getDmGrid().getPropertyAccess().name(), "Department", 1, "Name");
        txtHDepartmentTo.getDmGrid().initializeGrid(txtHDepartmentTo.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHDepartmentTo.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartmentFrom">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDepartmentFrom = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHDepartmentFrom() {
        txtHDepartmentFrom.getDmGrid().addTextColumn(txtHDepartmentFrom.getDmGrid().getPropertyAccess().name(), "Department", 1, "Name");
        txtHDepartmentFrom.getDmGrid().initializeGrid(txtHDepartmentFrom.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHDepartmentFrom.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTransferTo">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHTransferTo = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHTransferTo() {
        txtHTransferTo.getDmGrid().addTextColumn(txtHTransferTo.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHTransferTo.getDmGrid().initializeGrid(txtHTransferTo.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHTransferTo.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherNo">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHTransferNo = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHTransferNo() {
        txtHTransferNo.getDmGrid().addTextColumn(txtHTransferNo.getDmGrid().getPropertyAccess().name(), "Transfer No.", 1, "name");
        txtHTransferNo.getDmGrid().initializeGrid(txtHTransferNo.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHTransferNo.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedGroup">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHMedGroup() {
        txtHMedGroup.getDmGrid().addTextColumn(txtHMedGroup.getDmGrid().getPropertyAccess().name(), "Name", 1, "NAME");
        txtHMedGroup.getDmGrid().initializeGrid(txtHMedGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMedGroup.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedCat">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedCat = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHMedCat() {
        txtHMedCat.getDmGrid().addTextColumn(txtHMedCat.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHMedCat.getDmGrid().initializeGrid(txtHMedCat.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMedCat.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedicine">
    interface TxtHMed_PrpAcs extends KeyField_T_PrpAcs<TxtHMedicine_Mdl> {

        @Editor.Path("MedicineGroup")
        ValueProvider<TxtHMedicine_Mdl, String> medicineGroup();

        @Editor.Path("MedicineCategory")
        ValueProvider<TxtHMedicine_Mdl, String> medicineCategory();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHMedicine_Mdl, String> manualCode();
    }

    private final DmTextHelpMulti<TxtHMedicine_Mdl, TxtHMed_PrpAcs> txtHMedicine = new DmTextHelpMulti(GWT.create(TxtHMed_PrpAcs.class), 10);

    private void iniTxtHMedicine() {
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().name(), "Medicine Name", 200, "name");
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().medicineGroup(), "Medicine Group", 150, "medGroup");
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().medicineCategory(), "Medicine Category", 150, "medcategory");
        txtHMedicine.getDmGrid().initializeGrid(txtHMedicine.getDmGrid().getPropertyAccess().code(), 600, 0, 2);
        txtHMedicine.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Site", 200, "name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSite.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHBatchNo">
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

    private final DmTextHelpMulti<BatchNo_Mdl, TxtHBatchNo_PrpAcs> txtHBatchNo = new DmTextHelpMulti<>(GWT.create(TxtHBatchNo_PrpAcs.class), 5);

    private void iniTxtHBatchNo() {
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().name(), "Batch No.", 60, "batchNo");
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().medicineName(), "Medicine", 120, "medicineName");
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().expiryDate(), "Expiry Date", 80, "expiryDate", DmDataType.DATE);
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().mrp(), "MRP", 60, "");
        txtHBatchNo.getDmGrid().addTextColumn(txtHBatchNo.getDmGrid().getPropertyAccess().saleRate(), "Sale Rate", 80, "");
        txtHBatchNo.getDmGrid().initializeGrid(txtHBatchNo.getDmGrid().getPropertyAccess().code(), 550, 0, 2);

        txtHBatchNo.getHelper().addSort("medicineName", DmSortType.ASC);
        txtHBatchNo.getHelper().addSort("expiryDate", DmSortType.ASC);
        txtHBatchNo.getHelper().addSort("batchNo", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRequisitionNo">
    interface TxtHRequisitionNo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHRequisitionNo_Mdl> {

        @Editor.Path("requisitionDate")
        ValueProvider<TxtHRequisitionNo_Mdl, String> requisitionDate();

    }

    private final DmTextHelpMulti<TxtHRequisitionNo_Mdl, TxtHRequisitionNo_PrpAcs> txtHRequisitionNo = new DmTextHelpMulti(GWT.create(TxtHRequisitionNo_PrpAcs.class), 7, false);

    private void iniTxtHRequisitionNo() {
        txtHRequisitionNo.getDmGrid().addTextColumn(txtHRequisitionNo.getDmGrid().getPropertyAccess().name(), "Requisition No.", 1, "requisitionNo");
        txtHRequisitionNo.getDmGrid().addTextColumn(txtHRequisitionNo.getDmGrid().getPropertyAccess().requisitionDate(), "Reqs. Date", 80, "requisitionDate", DmDataType.DATE);
        txtHRequisitionNo.getDmGrid().initializeGrid(txtHRequisitionNo.getDmGrid().getPropertyAccess().fieldHashCode(), 250, 0, 2);

        txtHRequisitionNo.getHelper().addSort("requisitionDate", DmSortType.DESC);
        txtHRequisitionNo.getHelper().addSort("requisitionNo", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIssueNo">
    interface TxtHDepartmentFrom_PrpAcs extends KeyField_T_PrpAcs<TxtHTransferFromDept_Mdl> {

        @Editor.Path("DeptName")
        ValueProvider<TxtHTransferFromDept_Mdl, String> deptName();

        @Editor.Path("DeptCode")
        ValueProvider<TxtHTransferFromDept_Mdl, String> deptCode();

        @Editor.Path("EntryDate")
        ValueProvider<TxtHTransferFromDept_Mdl, String> entryDate();

        @Editor.Path("TransferToName")
        ValueProvider<TxtHTransferFromDept_Mdl, String> transferToName();

        @Editor.Path("TransferToCode")
        ValueProvider<TxtHTransferFromDept_Mdl, String> transferToCode();

        @Editor.Path("FromSiteCode")
        ValueProvider<TxtHTransferFromDept_Mdl, String> fromSiteCode();
    }
    private final DmTextHelpMulti<TxtHTransferFromDept_Mdl, TxtHDepartmentFrom_PrpAcs> txtHIssueNo = new DmTextHelpMulti(GWT.create(TxtHDepartmentFrom_PrpAcs.class), 6, false);

    private void iniTxtHTransfer() {
        txtHIssueNo.getDmGrid().addTextColumn(txtHIssueNo.getDmGrid().getPropertyAccess().name(), "Entry No", 100, "name");
        txtHIssueNo.getDmGrid().addTextColumn(txtHIssueNo.getDmGrid().getPropertyAccess().deptName(), "Dept Name", 100, "DeptName");
        txtHIssueNo.getDmGrid().addTextColumn(txtHIssueNo.getDmGrid().getPropertyAccess().deptCode(), "Dept Code", 0, "deptCode");
        txtHIssueNo.getDmGrid().addTextColumn(txtHIssueNo.getDmGrid().getPropertyAccess().entryDate(), "Date", 100, "entryDate");
        txtHIssueNo.getDmGrid().addTextColumn(txtHIssueNo.getDmGrid().getPropertyAccess().transferToName(), "Recieved By", 100, "transferToName");
        txtHIssueNo.getDmGrid().addTextColumn(txtHIssueNo.getDmGrid().getPropertyAccess().transferToCode(), "Recieved By Code", 0, "");
        txtHIssueNo.getDmGrid().initializeGrid(txtHIssueNo.getDmGrid().getPropertyAccess().code(), 500, 0, 2);
        txtHIssueNo.getHelper().addSort("DeptName", DmSortType.ASC);
        txtHIssueNo.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        srvAsync.invokeRPC(true).execute(getFilter(), reportProperty, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    getReport(result.getData());
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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBatchNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<BatchNo_Mdl>>>() {

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

        txtHVoucherType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHDepartmentTo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartmentTo).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartmentTo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartmentTo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHDepartmentFrom.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartmentFrom).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartmentFrom.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartmentFrom).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHTransferTo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTransferTo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTransferTo).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTransferTo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTransferTo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHTransferNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTransferNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTransferNo).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTransferNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTransferNo).show();
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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedCat(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
        txtHMedicine.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHMedicine_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicine(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>>() {

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

        txtHSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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

        txtHRequisitionNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHRequisitionNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRequisitionNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHRequisitionNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRequisitionNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHRequisitionNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHRequisitionNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRequisitionNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHIssueNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHRequisitionNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
//                if (DmShared.iNull(txtHDepartmentTo.getTag()).longValue() == 0) {
//                    new DmAlertMessageBox("Please Select Department To.", txtHIssueNo).show();
//                    return;
//                }

                srvAsync.invokeRPC(blnCallBackWait).getTxtHTransferFromDept(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTransferFromDept_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTransferTo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHTransferFromDept_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHIssueNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIssueNo).show();
                        }
                    }

                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

    }

    public RptMedicineInTransitRegister(SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(RptMedicineInTransitRegister_Srv.class), RptMedicineInTransitRegister.class.getName());
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

        iniTxtHVoucherType();
        addControl("voucherType", "InTransit Type", vl.add(txtHVoucherType));

        iniTxtHSite();
        addControl("site", "Site", vl.add(txtHSite));

        iniTxtHTransferNo();
        addControl("transferNo", "Voucher No.", vl.add(txtHTransferNo));

        iniTxtHDepartmentFrom();
        addControl("departmentFrom", "Department From", vl.add(txtHDepartmentFrom));

        iniTxtHDepartmentTo();
        addControl("departmentTo", "Department To", vl.add(txtHDepartmentTo));

        iniTxtHTransferTo();
        addControl("transferTo", "Transfer To", vl.add(txtHTransferTo));

        iniTxtHTransfer();
        addControl("intransitNo", "Transfer No", vl.add(txtHIssueNo));

        iniTxtHMedGroup();
        addControl("medGroup", "Medicine Group", vl.add(txtHMedGroup));

        iniTxtHMedCat();
        addControl("medCat", "Medicine Category", vl.add(txtHMedCat));

        iniTxtHMedicine();
        addControl("medicine", "Medicine", vl.add(txtHMedicine));

        iniTxtHBatchNo();
        addControl("batchNo", "Batch No.", vl.add(txtHBatchNo));

        iniTxtHRequisitionNo();
        addControl("requisitionNo", "Requisition No.", vl.add(txtHRequisitionNo));

        swi.setWidget(this);
    }

    private void setDefaultValue() {
        srvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<KeyFieldExtended>>() {
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
