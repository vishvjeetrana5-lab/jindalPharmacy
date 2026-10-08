package dataman.utility.overheadConfiguration.client;

import dataman.utility.overheadConfiguration.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OverheadConfiguration extends OverheadConfiguration_Dsg implements AttachEvent.Handler, ClickHandler {

    ServiceInvoker<OverheadConfiguration_SrvAsync> SrvAsync;
    final OverheadConfigurationGridEdit<OverheadConfiguration, Grd_PrpAcs> manageHeaderGridEdit = new OverheadConfigurationGridEdit(dmGrd);
    final SiteNameGridEdit<KeyFieldExtended, KeyField_PrpAcs> grdSiteEntry = new SiteNameGridEdit(dmSiteGrd);
    final ApplicableFrom applicableFrom = new ApplicableFrom();
    final ImportPopup importPopup = new ImportPopup();
    private HandlerRegistration clickRegistration;
    private OverheadConfiguration_Mdl configuration_Mdl = new OverheadConfiguration_Mdl();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtHVoucherType.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHVoucherType.setEnabled(false);
                txtOverheadName.setFocus(true);
                txtApplicableDate.setEnabled(false);
                dmGrd.getGrid().getStore().add(new GrdOverhead_Mdl());
                dmSiteGrd.getGrid().getStore().add(new KeyFieldExtended());
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

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(OverheadConfiguration.this);
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
        importPopup.addCallBack(new ImportPopup.CallBack() {

            @Override
            public void onYes(DmResult<OverheadConfiguration_Mdl> mdl) {
                topControlMoveRecSuccess(mdl, true);
            }
        });

        applicableFrom.addCallBack(new ApplicableFrom.CallBack() {

            @Override
            public void onOk(String newDtApplicable, String oldDtApplicable) {
                try {
                    txtApplicableDate.setTag(DmShared.dNull(oldDtApplicable));
                    txtApplicableDate.setText(DmShared.dNull(newDtApplicable));
                    if (!DmShared.dNull(oldDtApplicable).equalsIgnoreCase("")) {
                        fillGrid();
                    }
                } catch (Exception ex) {
                    new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
                }
            }

            @Override
            public void onDelete(String applicableFrom) {
                topControlMoveRec(DmShared.xNull(topControl.getDocId()));
            }
        });

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucherType_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVoucherType_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(DmShared.xNull(result.getErrorMessage())), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVoucherType_Mdl tRow) {
                if (blnStatus) {
                    if (tRow != null) {
                        txtOverheadName.setText(DmShared.xNull(tRow.getDescription()));
                        txtHVoucherType.setTag2nd(DmShared.xNull(tRow.getShortName()));
                        txtOverheadName.setFocus(true);
                    }
                }
            }
        });

        txtHTable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTableAffected(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTable).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTable.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(DmShared.xNull(result.getErrorMessage())), txtHTable).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(DmShared.xNull(result.getErrorMessage())), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Find tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);

                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void gridEvents() {

        manageHeaderGridEdit.addCallBack(new OverheadConfigurationGridEdit.CallBack() {
            @Override
            public void onOk(GrdOverhead_Mdl grdManageHeader) {
                dmGrd.commitChanges();
            }

            @Override
            public void onDelete() {
                dmGrd.commitChanges();
            }
        });

        dmGrd.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showManageHeaderGridEdit(event.getRowIndex());
            }
        });

        dmGrd.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    showManageHeaderGridEdit(dmGrd.getGrid().getStore().indexOf(dmGrd.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        grdSiteEntry.addCallBack(new SiteNameGridEdit.CallBack() {

            @Override
            public void onOk(KeyFieldExtended applicableSite) {
                dmSiteGrd.commitChanges();
            }

            @Override
            public void onDelete() {
                dmSiteGrd.commitChanges();
            }
        });

        dmSiteGrd.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showApplicableSiteGridEdit(event.getRowIndex());
            }
        });

        dmSiteGrd.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showApplicableSiteGridEdit(dmSiteGrd.getGrid().getStore().indexOf(dmSiteGrd.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showApplicableSiteGridEdit(int currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            grdSiteEntry.show(currentRowIndex, false, false, false);
        } else {
            grdSiteEntry.show(currentRowIndex, true, true, true);
        }
    }

    private void textEvent() {
        txtApplicableDate.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                txtApplicableDate.setText(DmClient.returnDate(txtApplicableDate.getText()));
            }
        });

    }

    private void showManageHeaderGridEdit(int currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            manageHeaderGridEdit.show(currentRowIndex, false, false, false, txtHTable.getText());
        } else {
            manageHeaderGridEdit.show(currentRowIndex, true, true, true, txtHTable.getText());
        }
    }

    public OverheadConfiguration(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<OverheadConfiguration_SrvAsync>(GWT.create(OverheadConfiguration_Srv.class), OverheadConfiguration.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        gridEvents();
        textEvent();
        swi.setWidget(this);
    }

    private OverheadConfiguration_Mdl topControlSaveInitialize() {
        OverheadConfiguration_Mdl mrTemp = new OverheadConfiguration_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHVoucherType.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setTableAffectedTag(DmShared.xNull(txtHTable.getTag()));
        mrTemp.setTableAffectedText(DmShared.xNull(txtHTable.getText()));

        mrTemp.setApplicableFrom(DmShared.xNull(txtApplicableDate.getText()));
        mrTemp.setOverheadName(DmShared.xNull(txtOverheadName.getText()));

        dmGrd.getGrid().getStore().commitChanges();
        mrTemp.getGrdManageHeader().addAll(dmGrd.getGrid().getStore().getAll());

        dmSiteGrd.getGrid().getStore().commitChanges();
        List<KeyFieldExtended> kfList = new ArrayList<KeyFieldExtended>();
        for (KeyFieldExtended field : dmSiteGrd.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(field.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                kfList.add(field);
            }
        }
        mrTemp.getGrdSiteName().addAll(kfList);

        return mrTemp;
    }

    private void topControlSave() {
        OverheadConfiguration_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            OverheadConfiguration_Vld.chkSave(mrTemp);
            List<GrdOverhead_Mdl> grdList = dmGrd.getGrid().getStore().getAll();
            OverheadConfiguration_Vld.checkGrid(grdList);
            checkGrid(grdList);
            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
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
                new DmAlertMessageBox(errorMessage, txtHVoucherType).show();
                break;
            case 150:
                new DmAlertMessageBox(errorMessage, txtOverheadName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHTable).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtApplicableDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHVoucherType).show();
                break;
        }
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
                        new DmAlertMessageBox(DmShared.xNull(DmShared.xNull(result.getErrorMessage())), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<OverheadConfiguration_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<OverheadConfiguration_Mdl> result) {
                topControlMoveRecSuccess(result, false);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<OverheadConfiguration_Mdl> result, Boolean isImport) {
        OverheadConfiguration_Mdl data = new OverheadConfiguration_Mdl();

        if (result.isStatus()) {
            data = result.getData();
            configuration_Mdl = result.getData();

            txtHTable.setTag(data.getTableAffectedTag());
            txtHTable.setText(data.getTableAffectedText());

            dmGrd.getGrid().getStore().clear();
            dmGrd.getGrid().getStore().addAll(data.getGrdManageHeader());
            dmSiteGrd.getGrid().getStore().clear();
            dmSiteGrd.getGrid().getStore().addAll(data.getGrdSiteName());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        if (isImport) {
            txtHVoucherType.setEnabled(false);
            txtApplicableDate.setEnabled(false);
            txtOverheadName.setFocus(true);
            dmSiteGrd.getGrid().getStore().add(new KeyFieldExtended());
            dmGrd.getGrid().getStore().add(new GrdOverhead_Mdl());
        } else {
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtApplicableDate.setText(data.getApplicableFrom());
            txtHVoucherType.setText(data.getSharedKeyFieldName());
            txtHVoucherType.setTag(data.getVoucherTypeCode());
            txtHVoucherType.setTag2nd(data.getVoucherTypeShortName());
            txtOverheadName.setText(data.getOverheadName());
            topControl.setDisplay(true);
        }
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

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
                    new DmAlertMessageBox(DmShared.xNull(DmShared.xNull(result.getErrorMessage())), (DmTextBox) null).show();
                }
            }
        });
    }

    private void exportJson() {
        SrvAsync.invokeRPC().exportJson(DmRequestBuilder.getSessionId(), configuration_Mdl, new AsyncCallback<DmResult<KeyFieldExtended>>() {
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

    protected void fillGrid() {
        SrvAsync.invokeRPC().fillGrid(DmShared.xNull(topControl.getDocId()), DmShared.dNull(txtApplicableDate.getTag()), new AsyncCallback<DmResult<List<GrdOverhead_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdOverhead_Mdl>> result) {
                if (result.isStatus()) {
                    dmGrd.getGrid().getStore().clear();
                    dmGrd.getGrid().getStore().addAll(result.getData());
                    if (result != null) {
                        if (dmGrd.getGrid().getStore().size() <= 0) {
                            dmGrd.getGrid().getStore().clear();
                            txtHTable.setText("");
                            txtHTable.setTag("");
                        } else {
                            txtHTable.setText(result.getData().get(0).getSharedPreparedBy());
                            txtHTable.setTag(result.getData().get(0).getSharedPreparedBy());
                        }
                    }

                }
            }
        });
    }

    private void checkGrid(List<GrdOverhead_Mdl> grdOverhead) {
        HashMap<Integer, String[]> formulaList = new HashMap<>();

        try {
            for (int i = 0; i < grdOverhead.size(); i++) {
                if (!DmShared.xNull(grdOverhead.get(i).getOverhead()).equalsIgnoreCase("")) {
                    formulaList.put(DmShared.iNull(grdOverhead.get(i).getSrlNo()).intValue(), new String[]{DmShared.xNull(grdOverhead.get(i).getManualCode()), DmShared.xNull(grdOverhead.get(i).getFormula())});
                }
            }
            OverHead_Shared.isValidFormula(formulaList, true);
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onClick(ClickEvent event) {

        if (event.getSource().equals(btnShowAppPupup)) {
            try {
                if (!DmShared.xNull(txtHVoucherType.getTag()).equalsIgnoreCase("")) {
                    applicableFrom.show(btnShowAppPupup, true, true, topControl.getDocId());
                } else {
                    new DmAlertMessageBox("Select Record To View/Add ApplicableFrom", (DmTextBox) null).show();
                    return;
                }
            } catch (DmException e) {
                new DmAlertMessageBox(DmShared.xNull(DmShared.xNull(e.toString())), btnShowAppPupup).show();
                return;
            }
        }

    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(mnuExportData.getText())) {
            if (topControl.getMode().equalsIgnoreCase("Browse")) {
                new DmConfirmMessageBox("Are you sure? You want to export record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            exportJson();
                        }
                    }
                }).show();
            }
        }
        if (event.getSelectedItem().getText().equalsIgnoreCase(mnuImportData.getText())) {
            if (topControl.getMode().equalsIgnoreCase("New") || topControl.getMode().equalsIgnoreCase("Edit")) {
                if (DmShared.iNull(txtHVoucherType.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Please select Voucher Type.", txtHVoucherType).show();
                    return;
                } else if (DmShared.xNull(txtOverheadName.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Please select OverHead Name.", txtOverheadName).show();
                    return;
                } else {
                    importPopup.show(DmShared.xNull(txtHVoucherType.getTag2nd()), DmShared.xNull(txtOverheadName.getText()));
                }
            }
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            this.clickRegistration = btnShowAppPupup.addClickHandler(this);
        } else {
            manageHeaderGridEdit.hide();
            this.clickRegistration.removeHandler();
        }
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

    private void clear() {
        txtApplicableDate.setText(DmClient.getStartDate());
        dmGrd.getGrid().getStore().clear();
        dmGrd.getGrid().getStore().add(new GrdOverhead_Mdl());
        dmSiteGrd.getGrid().getStore().clear();
        dmSiteGrd.getGrid().getStore().add(new KeyFieldExtended());
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        txtApplicableDate.setEnabled(false);
    }
}
