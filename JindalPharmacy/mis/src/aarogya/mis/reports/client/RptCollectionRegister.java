package aarogya.mis.reports.client;

import aarogya.mis.reports.shared.RptCollectionRegister_Srv;
import aarogya.mis.reports.shared.RptCollectionRegister_SrvAsync;
import aarogya.mis.reports.shared.TxtHVType_Mdl;
import aarogya.projectMenu.self.shared.pmMIS;
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
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmReportLayout.client.DmReportLayout;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.HashMap;
import java.util.List;

public class RptCollectionRegister extends DmReportLayout implements IsWidget, BlurHandler {

    public final static String userNameEditableBlog = "User Name Editable";
    private final String widgetName;
    private final ServiceInvoker<RptCollectionRegister_SrvAsync> SrvAsync;
    private final DmClient.ViaList vl = new DmClient.ViaList();
    private final DmTextBox txtFromDate = new DmTextBox(12, true);
    private final DmTextBox txtToDate = new DmTextBox(12, true);
    private final DmTextBoxTime txtToTime = new DmTextBoxTime(false);
    private final DmTextBoxTime txtFromTime = new DmTextBoxTime(false);
    private boolean isUserNameEditable = false;

    //<editor-fold defaultstate="collapsed" desc="txtHDateApplicable">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDateApplicable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHDateApplicable() {
        txtHDateApplicable.getDmGrid().addTextColumn(txtHDateApplicable.getDmGrid().getPropertyAccess().name(), "Date Applicable", 1, "Type");
        txtHDateApplicable.getDmGrid().initializeGrid(txtHDateApplicable.getDmGrid().getPropertyAccess().code(), 125, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHUserName">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHUserName = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHUserName() {
        txtHUserName.getDmGrid().addTextColumn(txtHUserName.getDmGrid().getPropertyAccess().name(), "Type", 1, "Name");
        txtHUserName.getDmGrid().initializeGrid(txtHUserName.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHUserName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHStatus">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHStatus() {
        txtHType.getDmGrid().addTextColumn(txtHType.getDmGrid().getPropertyAccess().name(), "Type", 1, "Type");
        txtHType.getDmGrid().initializeGrid(txtHType.getDmGrid().getPropertyAccess().code(), 125, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHReportFor">
    interface TxtHReportFor_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

    }
    private final DmTextHelpMulti<KeyFieldExtended, TxtHReportFor_PrpAcs> txtHReportFor = new DmTextHelpMulti(GWT.create(TxtHReportFor_PrpAcs.class), 8, false);

    private void iniTxtHReportFor() {
        txtHReportFor.getDmGrid().addTextColumn(txtHReportFor.getDmGrid().getPropertyAccess().name(), "Report For", 1, "Type");
        txtHReportFor.getDmGrid().initializeGrid(txtHReportFor.getDmGrid().getPropertyAccess().fieldHashCode(), 200, 0, 2);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPayMode">
    private final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHPayMode = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHPayMode() {
        txtHPayMode.getDmGrid().addTextColumn(txtHPayMode.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHPayMode.getDmGrid().initializeGrid(txtHPayMode.getDmGrid().getPropertyAccess().code(), 200, 0, 2);
        txtHPayMode.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHModuleType">
    interface TxtHVType_PrpAcs extends KeyField_T_PrpAcs<TxtHVType_Mdl> {

        @Editor.Path("Description")
        ValueProvider<TxtHVType_Mdl, String> description();

        @Editor.Path("Category")
        ValueProvider<TxtHVType_Mdl, String> category();
    }
    private final DmTextHelpMulti<TxtHVType_Mdl, TxtHVType_PrpAcs> txtHModuleType = new DmTextHelpMulti(GWT.create(TxtHVType_PrpAcs.class), 10);

    private void iniTxtHModuleType() {
        txtHModuleType.getDmGrid().addTextColumn(txtHModuleType.getDmGrid().getPropertyAccess().name(), "Short Name", 100, "shortName");
        txtHModuleType.getDmGrid().addTextColumn(txtHModuleType.getDmGrid().getPropertyAccess().description(), "Description", 200, "description");
        txtHModuleType.getDmGrid().addTextColumn(txtHModuleType.getDmGrid().getPropertyAccess().category(), "Category", 100, "category");
        txtHModuleType.getDmGrid().initializeGrid(txtHModuleType.getDmGrid().getPropertyAccess().code(), 400, 200, 2);
        txtHModuleType.getHelper().addSort("description", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHReportView">
    private final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReportView = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, true);

    private void iniTxtHReportView() {
        txtHReportView.getDmGrid().addTextColumn(txtHReportView.getDmGrid().getPropertyAccess().name(), "Type", 1, "Type");
        txtHReportView.getDmGrid().initializeGrid(txtHReportView.getDmGrid().getPropertyAccess().code(), 175, 0, 1);
    }
    //</editor-fold>

    @Override
    public void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(this);
    }

    @Override
    public void onView() {
        SrvAsync.invokeRPC(true).execute(DmRequestBuilder.getSessionId(),getFilter(), reportProperty, widgetName, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended data = result.getData();
                    if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
//                    if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL && !data.getSharedKeyFieldName().equalsIgnoreCase("Summary")) {
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

        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHStatus(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHReportView.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReportView(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReportView).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReportView.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReportView).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHReportFor.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCatType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReportFor).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReportFor.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReportFor).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

        txtHPayMode.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPayMode(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPayMode).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPayMode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPayMode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHModuleType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHModuleType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVType_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHModuleType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVType_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHModuleType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHModuleType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHDateApplicable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDateApplicable(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDateApplicable).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDateApplicable.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDateApplicable).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

                if (DmShared.xNull(txtHDateApplicable.getText()).equalsIgnoreCase("Voucher Date")) {
//                    txtFromTime.setEnabled(false);
//                    txtToTime.setEnabled(false);
                    txtFromTime.setText("");
                    txtToTime.setText("");
                } else if (!DmShared.xNull(txtHDateApplicable.getText()).equalsIgnoreCase("Voucher Date")) {
//                    txtFromTime.setEnabled(true);
//                    txtToTime.setEnabled(true);
                }
            }
        });

        txtHUserName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHUserName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUserName).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHUserName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHUserName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

    }

    public RptCollectionRegister(UserMenu menuPermission, SetWidgetInvoker swi, HashMap paramHashMap) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(RptCollectionRegister_Srv.class), RptCollectionRegister.class.getName());

        if (paramHashMap != null) {
            widgetName = (String) paramHashMap.get("widgetName");
        } else {
            widgetName = new pmMIS().COLLECTION_REGISTER.caption;
        }
        for (String controlPermission : menuPermission.getControlPermission()) {
            isUserNameEditable = controlPermission.equalsIgnoreCase(userNameEditableBlog);
        }

        setTitle(this.asWidget(), widgetName);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtHelpEvents();
        setDefaultValue(swi);
    }

    @Override
    public Widget asWidget() {
        return this.form;
    }

    private void initialize(SetWidgetInvoker swi) {
        iniTxtHDateApplicable();
        addControl("dateApplicable", "Date Applicable", vl.add(txtHDateApplicable));

        addControl("fromDate", "From Date", vl.add(txtFromDate));

        addControl("toDate", "To Date", vl.add(txtToDate));

        iniTxtHStatus();
        addControl("type", "Report Type", vl.add(txtHType));

        iniTxtHUserName();
        addControl("userName", "User Name", vl.add(txtHUserName));

        iniTxtHModuleType();
        addControl("moduleType", "Voucher Type", vl.add(txtHModuleType));

        iniTxtHPayMode();
        addControl("payMode", "Payment Mode", vl.add(txtHPayMode));

        addControl("fromTime", "From Time", vl.add(txtFromTime));

        addControl("toTime", "To Time", vl.add(txtToTime));

        iniTxtHReportFor();
        addControl("catType", "Report For", vl.add(txtHReportFor));

//        iniTxtHReportView();
//        addControl("reportView", "Report View", vl.add(txtHReportView));
        swi.setWidget(this);
    }

    private void setDefaultValue(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    initialize(swi);
                    boolean isAdministratorLogin = DmShared.xNull(result.getData().getSharedKeyFieldName()).equalsIgnoreCase("0");
//                    if (DmShared.xNull(result.getData().getSharedKeyFieldName()).equalsIgnoreCase("0")) {
//                        txtHType.setEnabled(false);
//                        txtHPayMode.setEnabled(false);
//                    } else {
//                        txtHType.setEnabled(true);
//                        txtHPayMode.setEnabled(true);
//                    }
                    txtHUserName.setEnabled(isAdministratorLogin);
                    if (!isAdministratorLogin) {
                        txtHUserName.setEnabled(isUserNameEditable);
                    }
                    txtHReportView.setTag("1");
                    txtHReportView.setText("Receipt Type Wise");
                }

            }
        });

//        txtHType.setTag("Summary");
//        txtHType.setText("Summary");
        txtHType.setTag("Detail");
        txtHType.setText("Detail");
        txtHDateApplicable.setText("Voucher Date");
//        txtFromTime.setEnabled(false);
//        txtToTime.setEnabled(false);
        txtFromDate.setText(DmClient.getLoginDate());
        txtToDate.setText(DmClient.getLoginDate());

        if (widgetName.equalsIgnoreCase(new pmMIS().COLLECTION_REGISTER_USERWISE.caption)) {
            txtHUserName.setText(DmClient.getLoginUserName());
            txtHUserName.setTag(DmClient.getLoginUserName());
        }
    }

}
