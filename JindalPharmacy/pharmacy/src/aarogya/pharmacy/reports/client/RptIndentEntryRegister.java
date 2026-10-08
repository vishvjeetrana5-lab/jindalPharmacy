package aarogya.pharmacy.reports.client;

import aarogya.pharmacy.reports.shared.RptIndentEntryRegister_Srv;
import aarogya.pharmacy.reports.shared.RptIndentEntryRegister_SrvAsync;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.pharmacy.reports.shared.*;
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
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.HashMap;
import java.util.List;

public class RptIndentEntryRegister extends DmReportLayout implements IsWidget, BlurHandler {

    final String widgetName = new pmPharmacy().INDENT_REGISTER.caption;
    ServiceInvoker<RptIndentEntryRegister_SrvAsync> SrvAsync;
    DmClient.ViaList vl = new DmClient.ViaList();
    DmTextBox txtFromDate = new DmTextBox(12, true);
    DmTextBox txtToDate = new DmTextBox(12, true);

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 7);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHDepartment.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIndentor">
    interface Ind_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHIndentor = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHIndentor() {
        txtHIndentor.getDmGrid().addTextColumn(txtHIndentor.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHIndentor.getDmGrid().addTextColumn(txtHIndentor.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHIndentor.getDmGrid().initializeGrid(txtHIndentor.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHIndentor.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIndentType">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHIndentType = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 3);

    private void iniTxtHIndentType() {
        txtHIndentType.getDmGrid().addTextColumn(txtHIndentType.getDmGrid().getPropertyAccess().name(), "Name", 0, "Description");
        txtHIndentType.getDmGrid().initializeGrid(txtHIndentType.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHIndentType.getHelper().addSort("Description", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHStatus">
    DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHStatus = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHStatus() {
        txtHStatus.getDmGrid().addTextColumn(txtHStatus.getDmGrid().getPropertyAccess().name(), "Name", 0, "Status");
        txtHStatus.getDmGrid().initializeGrid(txtHStatus.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedicine">
    interface KeyField_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();

    }
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedicine = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHMedicineName() {
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHMedicine.getDmGrid().addTextColumn(txtHMedicine.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code", 100, "manualCode");
        txtHMedicine.getDmGrid().initializeGrid(txtHMedicine.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHMedicine.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedCategory">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedCategory = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHMedCategory() {
        txtHMedCategory.getDmGrid().addTextColumn(txtHMedCategory.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHMedCategory.getDmGrid().initializeGrid(txtHMedCategory.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMedCategory.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMedGroup">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedGroup = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHMedGroup() {
        txtHMedGroup.getDmGrid().addTextColumn(txtHMedGroup.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHMedGroup.getDmGrid().initializeGrid(txtHMedGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMedGroup.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Site", 200, "name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHSite.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIndentNo">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHIndentNo = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHIndentNo() {
        txtHIndentNo.getDmGrid().addTextColumn(txtHIndentNo.getDmGrid().getPropertyAccess().name(), "Indent No.", 0, "name");
        txtHIndentNo.getDmGrid().initializeGrid(txtHIndentNo.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHIndentNo.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRequisitionNo">
    interface TxtHRequisitionNo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHRequisitionNo_Mdl> {

        @Editor.Path("requisitionDate")
        ValueProvider<TxtHRequisitionNo_Mdl, String> requisitionDate();

    }

    final DmTextHelpMulti<TxtHRequisitionNo_Mdl, TxtHRequisitionNo_PrpAcs> txtHRequisitionNo = new DmTextHelpMulti(GWT.create(TxtHRequisitionNo_PrpAcs.class), 7, false);

    private void iniTxtHRequisitionNo() {
        txtHRequisitionNo.getDmGrid().addTextColumn(txtHRequisitionNo.getDmGrid().getPropertyAccess().name(), "Requisition No.", 1, "requisitionNo");
        txtHRequisitionNo.getDmGrid().addTextColumn(txtHRequisitionNo.getDmGrid().getPropertyAccess().requisitionDate(), "Reqs. Date", 80, "requisitionDate", DmEnum.DmDataType.DATE);
        txtHRequisitionNo.getDmGrid().initializeGrid(txtHRequisitionNo.getDmGrid().getPropertyAccess().fieldHashCode(), 250, 0, 2);

        txtHRequisitionNo.getHelper().addSort("requisitionDate", DmEnum.DmSortType.DESC);
        txtHRequisitionNo.getHelper().addSort("requisitionNo", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHMedCompany">
    DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHMedCompany = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 5);

    private void iniTxtHMedCompany() {
        txtHMedCompany.getDmGrid().addTextColumn(txtHMedCompany.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHMedCompany.getDmGrid().initializeGrid(txtHMedCompany.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHMedCompany.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
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
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } else if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    private void txtHelpEvents() {

        txtHDepartment.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHIndentType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIndentType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIndentType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIndentType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIndentType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHIndentor.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIndetor(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIndentor).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIndentor.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIndentor).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }

        });

        txtHStatus.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHStatus(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHStatus).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHStatus.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHStatus).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }

        });

        txtHMedicine.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicine).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
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

        txtHMedCategory.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedCategory).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedCategory).show();
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
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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

        txtHIndentNo.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIndentNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIndentNo).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIndentNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIndentNo).show();
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
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRequisitionNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHRequisitionNo_Mdl>>>() {

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

        txtHMedCompany.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedCompany(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedCompany).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedCompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedCompany).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }

        });

    }

    public RptIndentEntryRegister(SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptIndentEntryRegister_Srv.class), RptIndentEntryRegister.class.getName());
        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtHelpEvents();
        setDefault();
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

        iniTxtHDepartment();
        addControl("department", "Department", vl.add(txtHDepartment));

        iniTxtHIndentType();
        addControl("indentType", "Indent Type", vl.add(txtHIndentType));

        iniTxtHIndentNo();
        addControl("indentNo", "Indent No.", vl.add(txtHIndentNo));

        iniTxtHIndentor();
        addControl("indentor", "Indentor", vl.add(txtHIndentor));

        iniTxtHRequisitionNo();
        addControl("requisitionNo", "Requisition No.", vl.add(txtHRequisitionNo));

        iniTxtHMedGroup();
        addControl("medGroup", "Medicine Group", vl.add(txtHMedGroup));

        iniTxtHMedCategory();
        addControl("medCategory", "Medicine Category", vl.add(txtHMedCategory));

        iniTxtHMedicineName();
        addControl("medName", "Medicine", vl.add(txtHMedicine));

        iniTxtHStatus();
        addControl("Status", "Status", vl.add(txtHStatus));

        iniTxtHMedCompany();
        addControl("medCompany", "Medicine Company", vl.add(txtHMedCompany));

        swi.setWidget(this);

    }

    private void setDefault() {

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
