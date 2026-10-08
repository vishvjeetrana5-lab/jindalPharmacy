package dataman.utility.guiConfig.client;

import dataman.utility.guiConfig.shared.*;
import dataman.dmBase.self.shared.*;
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
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFileManager.client.DmFileManager;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.HashMap;
import java.util.List;

public final class GUIConfig extends GUIConfig_Dsg implements AttachEvent.Handler, ClickHandler {

    private final ServiceInvoker<GUIConfig_SrvAsync> srvAsync;
    private final Setting<GUIConfig> setting = new Setting();
    private final SiteGridEdit<KeyFieldExtended, GrdApplicableSite_PrpAcs> siteGridEdit = new SiteGridEdit(grdApplicableSite);
    private final RowGridEdit<GUIRow_Mdl, GrdDescription_PrpAcs> rowGridEdit = new RowGridEdit(grdDescription);
    private final DmFileManager fileManager = new DmFileManager("GUI");
    private boolean isAttachFiles = true, isDeleteFiles = true;
    private GUIConfig_Mdl guiConfiguration_Mdl = new GUIConfig_Mdl();
    private final ImportPopup importPopup = new ImportPopup();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(attachFileBlog)) {
                isAttachFiles = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(deleteFileBlog)) {
                isDeleteFiles = true;
            }
        }

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
                txtName.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay();
                grdApplicableSite.getGrid().getStore().add(new KeyFieldExtended());
                grdDescription.getGrid().getStore().add(new GUIRow_Mdl());
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
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

        topControl.addOtherHandeller(new DmTopControl.eOther() {

            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(GUIConfig.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        importPopup.addCallBack(new ImportPopup.CallBack() {

            @Override
            public void onYes(DmResult<GUIConfig_Mdl> mdl) {
                topControlMoveRecSuccess(mdl, true);
            }
        });

        txtHTable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTable(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTable).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTable.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTable).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHTemplate.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTemplate(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTemplate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTemplate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTemplate).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        setting.addCallBack(new Setting.CallBack() {

            @Override
            public void onOk(String sqlQuery) {
                txtQuery.setText(sqlQuery);
            }
        });
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
                            Window.alert(result.getErrorMessage());
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Find_Mdl tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);
                if (blnStatus && tRow != null) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void gridEvents() {
        siteGridEdit.addCallBack(new SiteGridEdit.CallBack() {

            @Override
            public void onOk(KeyFieldExtended keyField) {
                grdApplicableSite.commitChanges();
            }

            @Override
            public void onDelete() {
                grdApplicableSite.commitChanges();
            }
        });
        grdApplicableSite.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showSiteGrid(event.getRowIndex());
            }
        });
        grdApplicableSite.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showSiteGrid(grdApplicableSite.getGrid().getStore().indexOf(grdApplicableSite.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        rowGridEdit.addCallBack(new RowGridEdit.CallBack() {

            @Override
            public void onOk(GUIRow_Mdl row_Mdl) {
                grdDescription.commitChanges();
            }

            @Override
            public void onDelete() {
                grdDescription.commitChanges();
            }
        });
        grdDescription.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showDescGrid(event.getRowIndex());
            }
        });
        grdDescription.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showDescGrid(grdDescription.getGrid().getStore().indexOf(grdDescription.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void handlerEvents() {
        btnQuery.addClickHandler(this);
    }

    public GUIConfig(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(GUIConfig_Srv.class), GUIConfig.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        handlerEvents();
        flwForm.addAttachHandler(this);
        findEvents();
        gridEvents();
        swi.setWidget(this);
    }

    private void showSiteGrid(int currentRowIndex) {

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            siteGridEdit.show(currentRowIndex, false, false, false);
        } else {
            siteGridEdit.show(currentRowIndex, true, true, true);
        }
    }

    private void showDescGrid(int currentRowIndex) {
        String prvRow = "";
        if (currentRowIndex > 0) {
            prvRow = DmShared.xNull(grdDescription.getGrid().getStore().get(currentRowIndex - 1).getSrNo());
        } else {
            prvRow = DmShared.xNull(grdDescription.getGrid().getStore().get(currentRowIndex).getSrNo());
        }
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            rowGridEdit.show(currentRowIndex, false, false, false, this, prvRow);
        } else {
            rowGridEdit.show(currentRowIndex, true, true, true, this, prvRow);
        }
    }

    private GUIConfig_Mdl topControlSaveInitialize() {
        GUIConfig_Mdl mrTemp = new GUIConfig_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setTempKeyCode(fileManager.getTempKeyCode());
        mrTemp.setSqlQuery(DmShared.xNull(txtQuery.getText()));
        mrTemp.setTableName(DmShared.xNull(txtHTable.getTag()));
        mrTemp.setTemplateCode(DmShared.xNull(txtHTemplate.getTag()));
        mrTemp.setTemplateName(DmShared.xNull(txtHTemplate.getText()));
        mrTemp.setReportPrefix(DmShared.xNull(txtReportPrefix.getText()));

        mrTemp.getGrdData().addAll(grdDescription.getGrid().getStore().getAll());
        mrTemp.getGrdSite().addAll(grdApplicableSite.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void topControlSave() {
        GUIConfig_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            GUIConfig_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtName).show();
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
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHTemplate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHTable).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, btnQuery).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtName).show();
        }
    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<GUIConfig_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<GUIConfig_Mdl> result) {
                topControlMoveRecSuccess(result, false);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<GUIConfig_Mdl> result, Boolean isImport) {
        if (result.isStatus()) {
            GUIConfig_Mdl data = result.getData();
            guiConfiguration_Mdl = result.getData();
            txtName.setText(data.getSharedKeyFieldName());
            txtHTable.setTag(data.getTableName());
            txtHTable.setText(data.getTableName());
            txtQuery.setText(data.getSqlQuery());
            txtReportPrefix.setText(data.getReportPrefix());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());

            grdApplicableSite.getGrid().getStore().clear();
            grdApplicableSite.getGrid().getStore().addAll(data.getGrdSite());
            grdDescription.getGrid().getStore().clear();
            grdDescription.getGrid().getStore().addAll(data.getGrdData());

            if (isImport) {
                txtName.setFocus(true);
                grdApplicableSite.getGrid().getStore().add(new KeyFieldExtended());
                grdDescription.getGrid().getStore().add(new GUIRow_Mdl());
            } else {
                topControl.setDocId(data.getSharedKeyFieldCode());
                txtHTemplate.setTag(data.getTemplateCode());
                txtHTemplate.setText(data.getTemplateName());
                topControl.setDisplay(true);
            }
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
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
                    clear();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            siteGridEdit.hide();
            rowGridEdit.hide();
            rowGridEdit.columnGridEdit.hide();
            rowGridEdit.columnGridEdit.setting.hide();
            setting.hide();
        }
    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(manageFileMenu.getText())) {
            fileManager.show(topControl.getMode().trim().equalsIgnoreCase("New") ? "" : topControl.getDocId(), "1", isAttachFiles, isDeleteFiles);
        } else if (event.getSelectedItem().getText().equalsIgnoreCase(mnuExportData.getText())) {
            if (topControl.getMode().equalsIgnoreCase("Browse")) {
//                if (DmShared.iNull(topControl.getDocId()).longValue() == 0) {
//                    new DmAlertMessageBox("Please select a record.", (DmTextBox) null).show();
//                    return;
//                } else {
                new DmConfirmMessageBox("Are you sure? You want to export record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            exportJson();
                        }
                    }
                }).show();
//                }
            }
        } else if (event.getSelectedItem().getText().equalsIgnoreCase(mnuImportData.getText())) {
            if (topControl.getMode().equalsIgnoreCase("New") || topControl.getMode().equalsIgnoreCase("Edit")) {
                if (DmShared.iNull(txtHTemplate.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Please select Template Name.", txtHTemplate).show();
                    return;
                } else {
                    importPopup.show(DmShared.xNull(txtHTemplate.getText()));
                }
            }
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnQuery)) {
            if (topControl.getMode().equalsIgnoreCase("Browse")) {
                setting.show(false, btnQuery, GUIConfig.this, DmShared.xNull(txtQuery.getText()));
            } else {
                setting.show(true, btnQuery, GUIConfig.this, DmShared.xNull(txtQuery.getText()));
            }
        }
    }

    private void manageDisplay() {
        txtQuery.setEnabled(false);
    }

    private void getTempKeyCode() {

        srvAsync.invokeRPC().getTempKeyCode(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtName).show();
            }

            @Override
            public void onSuccess(String result) {
                fileManager.setTempKeyCode(result);
            }
        });
    }

    private void exportJson() {
        srvAsync.invokeRPC().exportJson(DmRequestBuilder.getSessionId(), guiConfiguration_Mdl, new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended field = result.getData();
                    startDownloading(DmShared.xNull(field.getSharedKeyFieldName()), DmShared.xNull(field.getSharedKeyFieldCode()));
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

    private void onAdd() {
        clear();
        grdApplicableSite.getGrid().getStore().add(new KeyFieldExtended());
        grdDescription.getGrid().getStore().add(new GUIRow_Mdl());
        manageDisplay();
        getTempKeyCode();
    }

    private void clear() {
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(preparedByBlog, "");
        grdApplicableSite.getGrid().getStore().clear();
        grdDescription.getGrid().getStore().clear();
    }
}
