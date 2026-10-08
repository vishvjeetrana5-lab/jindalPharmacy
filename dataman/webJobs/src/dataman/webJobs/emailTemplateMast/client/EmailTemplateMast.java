package dataman.webJobs.emailTemplateMast.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.webJobs.emailTemplateMast.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmFileManager.client.DmFileManager;
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

public final class EmailTemplateMast extends EmailTemplateMast_Dsg implements AttachEvent.Handler, ClickHandler {

    private ServiceInvoker<EmailTemplateMast_SrvAsync> srvAsync;
    private String ServerFileName, ServerDirectory;
    private EmailTemplateMast_Mdl emailTemplateMdl;
    private final DmFileManager fileManager = new DmFileManager("EMT");
    private EmailTemplateMast_Mdl emailTemplateExport_Mdl = new EmailTemplateMast_Mdl();
    private final EmailTemplateGridEdit<GrdEmail_Mdl, EmailGrid_PrpAcs> emailGridEdit = new EmailTemplateGridEdit(grdEmailDetail);
    private final ImageDetailGridEdit<GrdImage_Mdl, ImageGrid_PrpAcs> imageDetailGridEdit = new ImageDetailGridEdit(grdImageDetail);
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
                grdEmailDetail.getGrid().getStore().add(new GrdEmail_Mdl());
                grdImageDetail.getGrid().getStore().add(new GrdImage_Mdl());
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EmailTemplateMast.this);
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
        fileManager.addCallBack(new DmFileManager.CallBack() {
            @Override
            public void close() {
                srvAsync.invokeRPC().updateImageContentId(topControl.getDocId(), new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtTempName).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {

                        } else {
                            topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                        }
                    }
                });
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
            public void onYes(DmResult<EmailTemplateMast_Mdl> mdl) {
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
            srvAsync.invokeRPC().getReadFile(emailTemplateMdl, new AsyncCallback<DmResult<List<GrdFieldName_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
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
        grdFieldDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                if (!topControl.getMode().equalsIgnoreCase("Browse")) {
                    String formatValue = "";
                    formatValue = grdFieldDetail.getGrid().getStore().get(event.getRowIndex()).getFormattedFieldName();
                    String str1 = DmShared.xNull(formatValue);
                    String str2 = txtMsg.getText();
                    txtMsg.setText(str2 + str1);
                    pnlGrd.hide();
                }

            }
        });

        emailGridEdit.addCallBack(new EmailTemplateGridEdit.CallBack() {

            @Override
            public void onOk(GrdEmail_Mdl grdTime) {
                grdEmailDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                grdEmailDetail.commitChanges();
            }
        });
        imageDetailGridEdit.addCallBack(new ImageDetailGridEdit.CallBack() {
            @Override
            public void onOk(GrdImage_Mdl opdService) {
                grdImageDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                grdImageDetail.commitChanges();
            }
        });
        grdEmailDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                grdEmailEdit(event.getRowIndex());
            }
        });
        grdEmailDetail.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    grdEmailEdit(grdEmailDetail.getGrid().getStore().indexOf(grdEmailDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
        grdImageDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                grdImageEdit(event.getRowIndex());
            }
        });
        grdImageDetail.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    grdImageEdit(grdImageDetail.getGrid().getStore().indexOf(grdImageDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {

        } else {
            emailGridEdit.hide();
            emailGridEdit.pnlGrd3.hide();
            imageDetailGridEdit.hide();
            pnlGrd.hide();
            pnlGrd1.hide();
            if (fileManager != null) {
                fileManager.hide();
            }
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void grdEmailEdit(Integer currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            emailGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            emailGridEdit.show(currentRowIndex, true, true, true, this);
        }
    }

    private void grdImageEdit(Integer currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            imageDetailGridEdit.show(currentRowIndex, false, false, false);
        } else {
            imageDetailGridEdit.show(currentRowIndex, true, true, true);
        }
    }

    public EmailTemplateMast(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(EmailTemplateMast_Srv.class), EmailTemplateMast.class.getName());
        this.asWidget().setTitle(widgetName);
        handlerEvents();
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
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
        if (event.getSource().equals(btnGrd)) {
            try {
                if (!DmShared.xNull(txtCSVFunction.getTag()).equalsIgnoreCase("")
                        && DmShared.iNull(txtHDataSource.getTag()).intValue() == DmShared.iNull(DmWebJobDataSources.CSV.getSourceType()).intValue()) {
                    grdFieldDetail.getGrid().getStore().clear();
                    showReadFile();
                } else if (!DmShared.xNull(txtHFunctionName.getTag()).equalsIgnoreCase("")
                        && DmShared.iNull(txtHDataSource.getTag()).intValue() == DmShared.iNull(DmWebJobDataSources.FUNCTION.getSourceType()).intValue()) {
                    grdFieldDetail.getGrid().getStore().clear();
                    showDataSource();
                }

                pnlGrd.showRelativeTo(btnGrd);
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), btnGrd).show();
            }
        } else if (event.getSource().equals(btnHelp)) {
            pnlGrd1.clear();
            pnlGrd1.showRelativeTo(btnHelp);
            pnlGrd1.add(txtHelp);
            txtHelp.setText("Note : Every data source must have following mandatory fields :- \n"
                    + "   [UniqueId,SubCode,Send_To_Name,Send_To_Address,Send_To_EMail] \n"
                    + "\n"
                    + "================== Data Source : SQL ==================\n"
                    + " Create table return function, Name started with prefix 'EMail_*' With One Varchar Parameter.\n"
                    + "\n"
                    + "   Example :- Create Function [dbo].[EMail_Test] (@TempCode bigint, @SiteCode int) \n"
                    + "              Returns Table \n"
                    + "              As \n"
                    + "              Return \n"
                    + "              ( \n"
                    + "              Select SG.Subcode As UniqueId,SG.Subcode,SG.Name As Send_To_Name,  \n"
                    + "              SG.Add1 + SG.Add2 As Send_To_Address,\n"
                    + "              SG.EMail As Send_To_EMail,SG.Nature,\n"
                    + "              '' As Send_BCc_EMail,'' As Send_Cc_EMail,\n"
                    + "              CT.CityName As City_Name,CO.Name As Country_Name \n"
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

    private EmailTemplateMast_Mdl topControlSaveInitialize() {
        EmailTemplateMast_Mdl mrTemp = new EmailTemplateMast_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setTempName(DmShared.xNull(txtTempName.getText()));
        mrTemp.setTempGroupCode(DmShared.xNull(txtHTemplateGroupName.getTag()));
        mrTemp.setTempGroupName(DmShared.xNull(txtHTemplateGroupName.getText()));
        mrTemp.setDataSource(DmWebJobDataSources.getValueFrom(DmShared.iNull(txtHDataSource.getTag()).intValue()));

        if (DmShared.iNull(txtHDataSource.getTag()).intValue() == DmWebJobDataSources.CSV.getSourceType()) {
            mrTemp.setFunctionTag(DmShared.xNull(txtCSVFunction.getTag()));
            mrTemp.setFunctionText(DmShared.xNull(txtCSVFunction.getText()));
        } else if (DmShared.iNull(txtHDataSource.getTag()).intValue() == DmWebJobDataSources.FUNCTION.getSourceType()) {
            mrTemp.setFunctionTag(DmShared.xNull(txtHFunctionName.getTag()));
            mrTemp.setFunctionText(DmShared.xNull(txtHFunctionName.getText()));
        }
        mrTemp.setStaffInfo(isStaffInfo);
        mrTemp.setFileName(DmShared.xNull(ServerFileName));
        mrTemp.setFilePath(DmShared.xNull(ServerDirectory));
        mrTemp.setSubject(DmShared.xNull(txtSubject.getText()));
        mrTemp.setMsg(DmShared.xNull(txtMsg.getText()));
        mrTemp.getGrdEmail().addAll(grdEmailDetail.getGrid().getStore().getAll());
        mrTemp.getGrdImageDetail().addAll(grdImageDetail.getGrid().getStore().getAll());
        mrTemp.getGrdFieldMdl().addAll(grdFieldDetail.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void handlerEvents() {
        btnGrd.addClickHandler(this);
        btnStaffInfo.addClickHandler(this);
        btnHelp.addClickHandler(this);
        flwForm.addAttachHandler(this);
    }

    private void topControlSave() {
        EmailTemplateMast_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            EmailTemplateMast_Vld.chkSave(mrTemp);

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
                new DmAlertMessageBox(errorMessage, txtSubject).show();
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
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<EmailTemplateMast_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtTempName).show();
            }

            @Override
            public void onSuccess(DmResult<EmailTemplateMast_Mdl> result) {
                topControlMoveRecSuccess(result, false);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<EmailTemplateMast_Mdl> result, Boolean isImport) {
        EmailTemplateMast_Mdl data = new EmailTemplateMast_Mdl();
        clear();
        if (result.isStatus()) {
            data = result.getData();
            emailTemplateExport_Mdl = result.getData();
            this.emailTemplateMdl = data;
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtTempName.setText(data.getTempName());
            txtHTemplateGroupName.setTag(data.getTempGroupCode());
            txtHTemplateGroupName.setText(data.getTempGroupName());
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
            txtSubject.setText(data.getSubject());
            txtMsg.setText(data.getMsg());
            grdFieldDetail.getGrid().getStore().clear();

            grdEmailDetail.getGrid().getStore().clear();
            grdEmailDetail.getGrid().getStore().addAll(data.getGrdEmail());

            grdImageDetail.getGrid().getStore().clear();
            grdImageDetail.getGrid().getStore().addAll(data.getGrdImageDetail());

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

    private void setFunctionDiv() {
        Anchor aBtn;
        flwFunction.clear();
        if (DmShared.iNull(txtHDataSource.getTag()).intValue() == DmWebJobDataSources.FUNCTION.getSourceType()) {
            lblFn.setText("Function Name");
            flwFunction.add(vl.add(txtHFunctionName));
        } else {
            lblFn.setText("File Name");
            flwFunction.add(hrtPnlCSV);

        }
    }

    @Override
    public void notifyForUpload(String notifier) {

    }

    @Override
    public void processAfterUploaded(DmIFile_Mdl file) {
        EmailTemplateMast_Mdl stm = new EmailTemplateMast_Mdl();
        txtCSVFunction.setTag(file.getFileName());
        txtCSVFunction.setText(file.getFileName());
        ServerFileName = (file.getServerFileName());
        ServerDirectory = (file.getServerDirectory());
        stm.setUploadFileName(DmShared.xNull(file.getServerFileName()));
        stm.setUploadFilePath(DmShared.xNull(file.getServerDirectory()));
        this.emailTemplateMdl = stm;
        showReadFile();
    }

    private void exportJson() {
        srvAsync.invokeRPC().exportJson(DmRequestBuilder.getSessionId(), emailTemplateExport_Mdl, new AsyncCallback<DmResult<KeyFieldExtended>>() {
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
                    return;
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
        }
        if (event.getSelectedItem().getText().equalsIgnoreCase(mnuImportData.getText())) {
            if (topControl.getMode().equalsIgnoreCase("New") || topControl.getMode().equalsIgnoreCase("Edit")) {
                importPopup.show("", DmShared.xNull(txtTempName.getText()));
            }
        }
        if (event.getSelectedItem().getText().equalsIgnoreCase(mnuManageFile.getText())) {
            if (topControl.getMode().equalsIgnoreCase("New") || topControl.getMode().equalsIgnoreCase("Edit")) {
                fileManager.show(topControl.getMode().trim().equalsIgnoreCase("New") ? "" : topControl.getDocId(), "1", true, true);
            }
        }
    }

    private void onAdd() {
        clear();
        txtHDataSource.setTag(String.valueOf(DmWebJobDataSources.FUNCTION.getSourceType()));
        txtHDataSource.setText(DmWebJobDataSources.FUNCTION.getSourceName());
        setFunctionDiv();
        txtTempName.setFocus(true);
        grdEmailDetail.getGrid().getStore().add(new GrdEmail_Mdl());
        grdImageDetail.getGrid().getStore().add(new GrdImage_Mdl());
        fileManager.setTempKeyCode(DmRequestBuilder.getSessionId());
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        grdImageDetail.getGrid().getStore().clear();
        grdFieldDetail.getGrid().getStore().clear();
        grdEmailDetail.getGrid().getStore().clear();
        txtHFunctionName.setTag("");
        txtHFunctionName.setText("");
        txtCSVFunction.setTag("");
        txtCSVFunction.setText("");
        lstStaff.clear();
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
