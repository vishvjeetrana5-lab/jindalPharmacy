package dataman.webJobs.smsTemplate.client;

import dataman.webJobs.smsTemplate.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.webJobs.self.shared.WebJobs_Shared;
import dataman.webJobs.staffInfo.client.StaffInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class SmsTemplate extends SmsTemplate_Dsg implements AttachEvent.Handler, ClickHandler, BlurHandler {

    private final ServiceInvoker<SmsTemplate_SrvAsync> srvAsync;
    private String ServerFileName, ServerDirectory;
    private SmsTemplate_Mdl smsTemplateMdl;
    private SmsTemplate_Mdl smsTemplateExport_Mdl = new SmsTemplate_Mdl();
    private final ImportPopup importPopup = new ImportPopup();
    private final StaffInfo staffInfo = new StaffInfo();
    private final WebJobs_Shared wjs = new WebJobs_Shared();
    private List<KeyFieldExtended> lstStaff = new ArrayList<>();
    private boolean isStaffInfo = false;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {

            @Override
            public void editClicked() {
                txtTempName.setFocus(true);
                setFunctionDiv();
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                            topControlDelete(topControl.getDocId());
                        }
                    }
                }).show();
            }
        });

        topControl.addCardHandeller(new DmTopControl.eCard() {
            @Override
            public void cardClicked() {
                topControlCard();
            }
        });

        topControl.addFindHandeller(new DmTopControl.eFind() {
            @Override
            public void findClicked() {
                flwForm.remove(vrtInnerForm);
                flwForm.add(dmFind);
            }
        });

        topControl.addMoveRecHandeller(new DmTopControl.iMoveRec() {
            @Override
            public void moveRec(String strFetchCode) {

                topControlMoveRec(strFetchCode);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {

            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(SmsTemplate.this);
            }
        });

        topControl.addOtherHandeller(new DmTopControl.eOther() {

            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        staffInfo.addCallBack(new StaffInfo.CallBack() {
            @Override
            public void onOk(List<KeyFieldExtended> lstQueue) {
                lstStaff.clear();
                lstStaff.addAll(lstQueue);
            }

            @Override
            public void onClose() {
                btnStaffInfo.setFocus(true);
            }
        });
        txtTempName.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggetionTemplate(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtTempName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtTempName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtTempName).show();
                        }
                    }
                });
            }
        });
        importPopup.addCallBack(new ImportPopup.CallBack() {

            @Override
            public void onYes(DmResult<SmsTemplate_Mdl> mdl) {
                topControlMoveRecSuccess(mdl, true);
            }
        });
        txtHTemplateGroupName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHUndTempName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTemplateGroupName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTemplateGroupName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTemplateGroupName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });
        txtHFunctionName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHFunctionName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHFunctionName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHFunctionName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHFunctionName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    showDataSource();
                }
            }
        });
        txtHDataSource.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDataSource(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDataSource).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDataSource.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDataSource).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    setFunctionDiv();
                    grdFieldDetail.getGrid().getStore().clear();
                }
            }
        });
    }

    private void showDataSource() {
        try {
            srvAsync.invokeRPC().getSqlDataSource(DmShared.xNull(txtHFunctionName.getTag()), new AsyncCallback<DmResult<List<GrdFieldName_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHFunctionName).show();
                }

                @Override
                public void onSuccess(DmResult<List<GrdFieldName_Mdl>> result) {
                    if (result.isStatus()) {
                        grdFieldDetail.getGrid().getStore().clear();
                        grdFieldDetail.getGrid().getStore().addAll(result.getData());
                        showStaffInfo(result.getValue1());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void showReadFile() {
        try {
            srvAsync.invokeRPC().getReadFile(smsTemplateMdl, new AsyncCallback<DmResult<List<GrdFieldName_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtCSVFunction).show();
                }

                @Override
                public void onSuccess(DmResult<List<GrdFieldName_Mdl>> result) {
                    if (result.isStatus()) {
                        grdFieldDetail.getGrid().getStore().clear();
                        grdFieldDetail.getGrid().getStore().addAll(result.getData());
                        showStaffInfo(result.getValue1());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Find_Mdl tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);

                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void grdEvent() {
        grdFieldDetail.getGrid().addRowDoubleClickHandler(new RowDoubleClickEvent.RowDoubleClickHandler() {

            @Override
            public void onRowDoubleClick(RowDoubleClickEvent event) {
                if (!topControl.getMode().equalsIgnoreCase("Browse")) {
                    String formatValue = "";
                    formatValue = grdFieldDetail.getGrid().getStore().get(event.getRowIndex()).getFormattedFieldName();
                    String str1 = DmShared.xNull(formatValue);
                    String str2 = txtMsg.getText();
                    txtMsg.setText(str2 + str1);
                }

            }
        });

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {

        } else {
            pnlGrd1.hide();

        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    public SmsTemplate(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(SmsTemplate_Srv.class), SmsTemplate.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        handlerEvents();
        grdEvent();
        flwStaffInfo.clear();
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnHelp)) {
            pnlGrd1.clear();
            pnlGrd1.showRelativeTo(btnHelp);
            pnlGrd1.add(txtHelp);
            txtHelp.setText("Note : Every data source must have following mandatory fields :- \n"
                    + "   [UniqueId,SubCode,Send_To_Name,Send_To_Address,Send_To_Mobile] \n"
                    + "\n"
                    + "================== Data Source : SQL ==================\n"
                    + "Create table return function, Name started with prefix 'SMS_*' With One Varchar Parameter.\n"
                    + "   Example :- Create Function [dbo].[SMS_Test] (@TempCode bigint, @SiteCode int) \n"
                    + "              Returns Table \n"
                    + "              As \n"
                    + "              Return \n"
                    + "              ( \n"
                    + "              Select SG.Subcode As UniqueId,SG.Subcode,SG.Name As Send_To_Name,  \n"
                    + "              SG.Add1 + SG.Add2 As Send_To_Address, \n"
                    + "              SG.Mobile As Send_To_Mobile,SG.Nature,\n"
                    + "              CT.CityName As City_Name,CO.Name As Country_Name, '' as staffInfo \n"
                    + "              From Subgroup SG \n"
                    + "              Left Join City CT On CT.CityCode = SG.CityCode \n"
                    + "              Left Join Country CO On CO.Code = SG.CountryCode \n"
                    + "              ) \n"
                    + "\n"
                    + "===================================================");
            txtHelp.setReadOnly(true);

        } else if (event.getSource().equals(btnStaffInfo)) {
            staffInfo.show(btnStaffInfo, topControl.getMode(), lstStaff);
        }

    }

    private SmsTemplate_Mdl topControlSaveInitialize() {
        SmsTemplate_Mdl mrTemp = new SmsTemplate_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setTempName(DmShared.xNull(txtTempName.getText()));
        mrTemp.setApiTemplateId(DmShared.xNull(txtTemplateId.getText()));
        mrTemp.setTemplateGroupCode(DmShared.xNull(txtHTemplateGroupName.getTag()));
        mrTemp.setTemplateGroupName(DmShared.xNull(txtHTemplateGroupName.getText()));
        mrTemp.setDataSource(DmWebJobDataSources.getValueFrom(DmShared.iNull(txtHDataSource.getTag()).intValue()));
        if (DmShared.iNull(txtHDataSource.getTag()).intValue() == DmWebJobDataSources.CSV.getSourceType()) {
            mrTemp.setFunctionTag(DmShared.xNull(txtCSVFunction.getTag()));
            mrTemp.setFunctionText(DmShared.xNull(txtCSVFunction.getText()));
        } else if (DmShared.iNull(txtHDataSource.getTag()).intValue() == DmWebJobDataSources.FUNCTION.getSourceType()) {
            mrTemp.setFunctionTag(DmShared.xNull(txtHFunctionName.getTag()));
            mrTemp.setFunctionText(DmShared.xNull(txtHFunctionName.getText()));
        }
        mrTemp.setFileName(DmShared.xNull(ServerFileName));
        mrTemp.setFilePath(DmShared.xNull(ServerDirectory));
        mrTemp.setMsg(DmShared.xNull(txtMsg.getText()));
        mrTemp.getLstStaffInfo().addAll(lstStaff);
        grdFieldDetail.getGrid().getStore().clear();
        mrTemp.setStaffInfo(isStaffInfo);
        return mrTemp;
    }

    private void setFunctionDiv() {
        flwFunction.clear();
        if (DmShared.iNull(txtHDataSource.getTag()).intValue() == DmWebJobDataSources.FUNCTION.getSourceType()) {
            lblFn.setText("Function Name");
            flwFunction.add(vl.add(txtHFunctionName));
        } else {
            lblFn.setText("File Name");
            flwFunction.add(hrtPnlCSV);
        }
    }

    private void handlerEvents() {
        btnHelp.addClickHandler(this);
        btnStaffInfo.addClickHandler(this);
    }

    private void topControlSave() {
        SmsTemplate_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            SmsTemplate_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtTempName).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");
                        } else {
                            topControlMoveRec(topControl.getDocId());
                        }
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtTempName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHTemplateGroupName).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHDataSource).show();
                break;

            case 400:
                new DmAlertMessageBox(errorMessage, txtHFunctionName).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtCSVFunction).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtTempName).show();
                break;
        }
    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtTempName).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtTempName).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<SmsTemplate_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtTempName).show();
            }

            @Override
            public void onSuccess(DmResult<SmsTemplate_Mdl> result) {
                topControlMoveRecSuccess(result, false);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<SmsTemplate_Mdl> result, Boolean isImport) {
        clear();
        SmsTemplate_Mdl data = new SmsTemplate_Mdl();
        if (result.isStatus()) {
            data = result.getData();
            smsTemplateExport_Mdl = result.getData();
            this.smsTemplateMdl = data;
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtTempName.setText(data.getTempName());
            txtTemplateId.setText(data.getApiTemplateId());
            txtHTemplateGroupName.setTag(data.getTemplateGroupCode());
            txtHTemplateGroupName.setText(data.getTemplateGroupName());
            txtHDataSource.setTag(DmShared.iNull(data.getDataSource().getSourceType()).toString());
            txtHDataSource.setText(data.getDataSource().getSourceName());
            lstStaff.addAll(data.getLstStaffInfo());
            setFunctionDiv();
            if (DmShared.iNull(txtHDataSource.getTag()).intValue() == DmWebJobDataSources.CSV.getSourceType()) {
                txtCSVFunction.setTag(data.getFunctionTag());
                txtCSVFunction.setText(data.getFunctionText());
                txtHFunctionName.setTag("");
                txtHFunctionName.setText("");
                showReadFile();
            } else if (DmShared.iNull(txtHDataSource.getTag()).intValue() == DmWebJobDataSources.FUNCTION.getSourceType()) {
                txtHFunctionName.setTag(data.getFunctionTag());
                txtHFunctionName.setText(data.getFunctionText());
                txtCSVFunction.setTag("");
                txtCSVFunction.setText("");
                if (isImport) {
                    showDataSource();
                } else {
                    if (!DmShared.xNull(topControl.getDocId()).equalsIgnoreCase("")) {
                        showDataSource();
                    }
                }

            }
            txtMsg.setText(data.getMsg());
            grdFieldDetail.getGrid().getStore().clear();
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        if (!isImport) {
            topControl.setDocId(data.getSharedKeyFieldCode());
            topControl.setDisplay(true);
        }
    }

    private void topControlDelete(String strKey) {
        srvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    userControl.setTextBlog(preparedByBlog, "");
                    userControl.setTextBlog(modifiedByBlog, "");
                    topControlCard();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onAdd() {
        clear();
        txtHDataSource.setTag(String.valueOf(DmWebJobDataSources.FUNCTION.getSourceType()));
        txtHDataSource.setText(DmWebJobDataSources.FUNCTION.getSourceName());
        setFunctionDiv();
        txtTempName.setFocus(true);

        txtTempName.setFocus(true);
        txtRefV_Type.setEnabled(false);

    }

    private void clear() {
        isStaffInfo = false;
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        txtHFunctionName.setTag("");
        txtHFunctionName.setText("");
        txtCSVFunction.setTag("");
        txtCSVFunction.setText("");
        lstStaff.clear();
        grdFieldDetail.getGrid().getStore().clear();
    }

    @Override
    public void notifyForUpload(String notifier) {

    }

    @Override
    public void processAfterUploaded(DmIFile_Mdl file) {
        SmsTemplate_Mdl stm = new SmsTemplate_Mdl();
        txtCSVFunction.setTag(file.getFileName());
        txtCSVFunction.setText(file.getFileName());
        ServerFileName = (file.getServerFileName());
        ServerDirectory = (file.getServerDirectory());
        stm.setUploadFileName(DmShared.xNull(file.getServerFileName()));
        stm.setUploadFilePath(DmShared.xNull(file.getServerDirectory()));
        this.smsTemplateMdl = stm;
        showReadFile();
    }

    private void exportJson() {
        srvAsync.invokeRPC().exportJson(DmRequestBuilder.getSessionId(), smsTemplateExport_Mdl, new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended field = result.getData();
                    startDownloading(DmShared.xNull(field.getSharedKeyFieldName()), DmShared.xNull(field.getSharedKeyFieldCode()));
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
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(mnuExportData.getText())) {
            if (topControl.getMode().equalsIgnoreCase("Browse")) {
                if (DmShared.iNull(topControl.getDocId()).longValue() == 0) {
                    new DmAlertMessageBox("Please select a record.", (DmTextBox) null).show();
                } else {
                    new DmConfirmMessageBox("Are you sure? You want to export this record.", new DialogHideEvent.DialogHideHandler() {

                        @Override
                        public void onDialogHide(DialogHideEvent event) {
                            if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                exportJson();
                            }
                        }
                    }).show();
                }
            }
        } else if (event.getSelectedItem().getText().equalsIgnoreCase(mnuImportData.getText())) {
            if (topControl.getMode().equalsIgnoreCase("New") || topControl.getMode().equalsIgnoreCase("Edit")) {
                importPopup.show("", DmShared.xNull(txtTempName.getText()));
            }
        }
    }

    private void showStaffInfo(String staffInfo) {
        flwStaffInfo.clear();
        isStaffInfo = false;
        if (staffInfo.equalsIgnoreCase(wjs.staffInfo)) {
            isStaffInfo = true;
            flwStaffInfo.add(btnStaffInfo);
        }
    }
}
