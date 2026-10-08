package aarogya.master.organizationServiceMapping.client;

import aarogya.master.organizationServiceMapping.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
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
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.*;
import java.util.HashMap;
import java.util.List;

public final class OrganizationServiceMapping extends OrganizationServiceMapping_Dsg implements BlurHandler, AttachEvent.Handler {

    private final ServiceInvoker<OrganizationServiceMapping_SrvAsync> SrvAsync;
    private final ServiceGridEdit<GrdService_Mdl, GridService_PrpAcs> gridServiceEdit = new ServiceGridEdit(grdService);
    private final RoomCategoryGridEdit<GrdRoomCategory_Mdl, GrdRoomCategory_PrpAcs> gridRoomCategoryGridEdit = new RoomCategoryGridEdit(grdRoomCategory);
    private final SearchServicePopup searchServicePopup = new SearchServicePopup();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
                manageDisplay(true);
                txtHEmpanelment.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay(false);
                txtApplicableDate.setEnabled(false);
                grdRoomCategory.getGrid().getStore().add(new GrdRoomCategory_Mdl());
                ((CellSelectionModel) grdService.getGrid().getSelectionModel()).selectCell(0, 1);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(OrganizationServiceMapping.this);
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
        searchServicePopup.addCallBack(new SearchServicePopup.CallBack() {
            @Override
            public void onOk(String serviceCode) {
                setFocusToGrid(serviceCode);
            }

            @Override
            public void onClose() {
                ((CellSelectionModel) grdService.getGrid().getSelectionModel()).selectCell(0, 1);
            }
        });

        txtHEmpanelment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHOrganization(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEmpanelment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEmpanelment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEmpanelment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHEmpanelment.setTag2nd(DmShared.xNull(t1Row.getSharedModifiedBy()));
                    fillOrganizationDetail();
                }
            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    private void gridEvents() {
        gridServiceEdit.addCallBack(new ServiceGridEdit.CallBack() {

            @Override
            public void onOk(GrdService_Mdl master_Mdl) {
                grdService.lockScroll();
                grdService.commitChanges();
                grdService.releaseScroll();
            }

            @Override
            public void onDelete() {
                grdService.lockScroll();
                grdService.commitChanges();
                grdService.releaseScroll();
            }
        });
        grdService.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showService(event.getRowIndex());
            }
        });
        grdService.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {

                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showService(grdService.getGrid().getStore().indexOf(grdService.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        gridRoomCategoryGridEdit.addCallBack(new RoomCategoryGridEdit.CallBack() {
            @Override
            public void onOk(GrdRoomCategory_Mdl master_Mdl) {
                grdRoomCategory.commitChanges();
            }

            @Override
            public void onDelete() {
                grdRoomCategory.commitChanges();
            }
        });
        grdRoomCategory.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showRoomCategory(event.getRowIndex());
            }
        });
        grdRoomCategory.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {

                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showRoomCategory(grdRoomCategory.getGrid().getStore().indexOf(grdRoomCategory.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        grdOrganizationDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                topControlMoveRec(DmShared.xNull(grdOrganizationDetail.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
            }
        });
    }

    private void showService(int currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            gridServiceEdit.show(currentRowIndex, false, false, false, this);
        } else {
            gridServiceEdit.show(currentRowIndex, true, false, true, this);
        }
    }

    private void showRoomCategory(int currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            gridRoomCategoryGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            gridRoomCategoryGridEdit.show(currentRowIndex, true, true, true, this);
        }
    }

    public OrganizationServiceMapping(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<OrganizationServiceMapping_SrvAsync>(GWT.create(OrganizationServiceMapping_Srv.class), OrganizationServiceMapping.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        swi.setWidget(this);
        manageDisplay(false);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtApplicableDate)) {
            txtApplicableDate.setText(DmClient.returnDate(txtApplicableDate.getText()));
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void notifyForUpload(String notifier) {

    }

    @Override
    public void processAfterUploaded(DmIFile_Mdl file) {
        if (file != null) {
            txtImportFrom.setTag(file.getServerDirectory() + "/" + file.getServerFileName());
            txtImportFrom.setText(file.getFileName());
            getImportDate();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            gridServiceEdit.hide();
            gridRoomCategoryGridEdit.hide();
            searchServicePopup.hide();
        }
    }

    private OrganizationServiceMapping_Mdl topControlSaveInitialize() {
        OrganizationServiceMapping_Mdl mrTemp = new OrganizationServiceMapping_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        mrTemp.setOrganizationTag(DmShared.xNull(txtHEmpanelment.getTag()));
        mrTemp.setRateListCode(DmShared.xNull(txtHEmpanelment.getTag2nd()));
        mrTemp.setApplicableDate(DmShared.dNull(txtApplicableDate.getText()));
        mrTemp.setApplicableTo(DmShared.dNull(txtApplicableTo.getText()));

        mrTemp.getListRoomCategory().clear();
        mrTemp.getListRoomCategory().addAll(grdRoomCategory.getGrid().getStore().getAll());
        mrTemp.getListServiceDetail().clear();
        mrTemp.getListServiceDetail().addAll(grdService.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void topControlSave() {
        OrganizationServiceMapping_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            OrganizationServiceMapping_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHEmpanelment).show();
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
                new DmAlertMessageBox(errorMessage, txtHEmpanelment).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtApplicableDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtImportFrom).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHEmpanelment).show();
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
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        topControlMoveRec(strKey, "", null);
    }

    private void topControlMoveRec(String strKey, String orgCode, final DmCallBack callBack) {
        SrvAsync.invokeRPC().getMoveRec(strKey, orgCode, new AsyncCallback<DmResult<OrganizationServiceMapping_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<OrganizationServiceMapping_Mdl> result) {
                if (result.isStatus()) {
                    topControlMoveRecSuccess(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
                if (callBack != null && result.isStatus()) {
                    topControl.btnClick("Edit");
                    DmResult<Object> dmrTemp = new DmResult<>();
                    dmrTemp.setStatus(true);
                    callBack.result(dmrTemp);
                } else {
                    topControl.setDisplay(true);
                }
            }
        });
    }

    private void topControlMoveRecSuccess(OrganizationServiceMapping_Mdl data) {
        manageDisplay(false);
        topControl.setDocId(data.getSharedKeyFieldCode());
        txtApplicableDate.setText(data.getApplicableDate());
        txtApplicableTo.setText(data.getApplicableTo());
        txtHEmpanelment.setTag(data.getOrganizationTag());
        txtHEmpanelment.setText(data.getOrganizationText());
        grdRoomCategory.getGrid().getStore().clear();
        grdRoomCategory.getGrid().getStore().addAll(data.getListRoomCategory());
        grdService.getGrid().getStore().clear();
        grdService.getGrid().getStore().addAll(data.getListServiceDetail());
        grdOrganizationDetail.getGrid().getStore().clear();
        grdOrganizationDetail.getGrid().getStore().addAll(data.getListOrganizationDetail());

        userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
        userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(DmShared.dNull(txtApplicableDate.getText()), DmShared.xNull(txtHEmpanelment.getTag()), strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    clearBlogs();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillOrganizationDetail() {
        SrvAsync.invokeRPC().getOrganizationDetail(DmShared.xNull(txtHEmpanelment.getTag()), new AsyncCallback<DmResult<List<GrdOrganizationDetail_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHEmpanelment).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdOrganizationDetail_Mdl>> result) {
                if (result.isStatus()) {

                    grdOrganizationDetail.getGrid().getStore().clear();
                    grdOrganizationDetail.getGrid().getStore().addAll(result.getData());
                    grdOrganizationDetail.commitChanges();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHEmpanelment).show();
                }
            }
        });
    }

    private void manageDisplay(boolean isVisible) {
        txtHEmpanelment.setEnabled(isVisible);
        txtApplicableTo.setEnabled(false);
        txtApplicableDate.addBlurHandler(this);
        txtImportFrom.setEnabled(false);

        if (topControl.getMode().equalsIgnoreCase("New")) {
            lblImportData.setVisible(true);
            txtImportFrom.setVisible(true);
            flwBtnUpload.setVisible(true);
        } else {
            lblImportData.setVisible(false);
            txtImportFrom.setVisible(false);
            flwBtnUpload.setVisible(false);
        }
    }

    private void getImportDate() {
        SrvAsync.invokeRPC().getImportData(DmShared.xNull(txtImportFrom.getTag()), new AsyncCallback<DmResult<List<GrdService_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdService_Mdl>> result) {
                if (result.isStatus()) {
                    grdService.getGrid().getStore().clear();
                    grdService.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void exportToCSV() {
        SrvAsync.invokeRPC().downloadCSV(DmRequestBuilder.getSessionId(), new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtApplicableDate).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended kf = result.getData();
                    startDownloading(DmShared.xNull(kf.getSharedKeyFieldName()), DmShared.xNull(kf.getSharedKeyFieldCode()));
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtApplicableDate).show();
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
        if (event.getSelectedItem().getText().equalsIgnoreCase(mnuDwnldCsv.getText())) {
            if (topControl.getMode().equalsIgnoreCase("Browse")) {
                new DmConfirmMessageBox("Are you sure? You want to download csv.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            exportToCSV();
                        }
                    }
                }).show();
            }
        } else if (event.getSelectedItem().getText().equalsIgnoreCase(mnuSearch.getText())) {
            if (DmShared.iNull(txtHEmpanelment.getTag()).longValue() != 0) {
                searchServicePopup.show(this);
            }
        }
    }

    @Override
    public void action(String nature, Object object, DmCallBack callBack) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        HashMap<String, String> hmOrganization = ((HashMap<String, String>) object);
        try {
            if (hmOrganization != null) {
                switch (nature) {
                    case "OPEN_ORGANIZATION":
                        topControlMoveRec("", ((String) hmOrganization.get("organizationCode")), callBack);
                        break;
                    case "ADD_SERVICE":
                        setFocusToGrid((String) hmOrganization.get("serviceCode"));
                        break;
                }
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void setFocusToGrid(String serviceCode) {
        for (int i = 0; i < grdService.getGrid().getStore().size(); i++) {
            if (DmShared.iNull(grdService.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() == DmShared.iNull(serviceCode).longValue()) {
                tabPanel.selectTab(0);
                final int row = i;
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        ((CellSelectionModel) grdService.getGrid().getSelectionModel()).selectCell(row, 1);
                    }
                });
                break;
            }
        }
    }

    private void onAdd() {
        clearBlogs();
        grdRoomCategory.getGrid().getStore().add(new GrdRoomCategory_Mdl());
    }

    private void clearBlogs() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        grdRoomCategory.getGrid().getStore().clear();
        grdService.getGrid().getStore().clear();
        grdOrganizationDetail.getGrid().getStore().clear();
    }
}
