package dataman.utility.walkThroughMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.walkThroughMaster.shared.GrdAuthority_Mdl;
import dataman.utility.walkThroughMaster.shared.Find_Mdl;
import dataman.utility.walkThroughMaster.shared.GrdDefaultDetail_Mdl;
import dataman.utility.walkThroughMaster.shared.WalkThroughMaster_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.ArrayList;
import java.util.List;
import dataman.utility.walkThroughMaster.shared.WalkThroughMaster_Srv;
import dataman.utility.walkThroughMaster.shared.WalkThroughMaster_SrvAsync;

public final class WalkThroughMaster extends WalkThroughMaster_Dsg implements BlurHandler, ClickHandler {

    ServiceInvoker<WalkThroughMaster_SrvAsync> SrvAsync;

    final AuthorityGridEdit<GrdAuthority_Mdl, GridAuthority_PrpAcs> authorityGridEdit = new AuthorityGridEdit(gridAuthority);
    final DefaultDetailGridEdit<GrdDefaultDetail_Mdl, GridDefault_PrpAcs> defaultGridEdit = new DefaultDetailGridEdit(gridDefaultDetail);

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setOtherRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                onAdd();
                //manageDisplay(true);
                txtName.setFocus(true);

            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {

                onAdd();
                // manageDisplay(false);

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
                prnRecord(false);

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

                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(WalkThroughMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtName.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestionSetupName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtName).show();
                        }
                    }
                });
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

        authorityGridEdit.addCallBack(new AuthorityGridEdit.CallBack() {

            @Override
            public void onOk(GrdAuthority_Mdl authority_Mdl) {
                gridAuthority.commitChanges();
            }

            @Override
            public void onDelete() {
                gridAuthority.commitChanges();
            }

        });

        gridAuthority.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showAuthorityGridEdit(event.getRowIndex());
            }
        });

        gridAuthority.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showAuthorityGridEdit(gridAuthority.getGrid().getStore().indexOf(gridAuthority.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        defaultGridEdit.addCallBack(new DefaultDetailGridEdit.CallBack() {

            @Override
            public void onOk(GrdDefaultDetail_Mdl gridDefault_Mdl) {
                gridDefaultDetail.commitChanges();

            }

            @Override
            public void onDelete() {
                gridDefaultDetail.commitChanges();

            }

        });

        gridDefaultDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showDefaultGridEdit(event.getRowIndex());
            }
        });

        gridDefaultDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showDefaultGridEdit(gridDefaultDetail.getGrid().getStore().indexOf(gridDefaultDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showDefaultGridEdit(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            defaultGridEdit.show(currentRowIndex, false, false, false);
        } else {
            defaultGridEdit.show(currentRowIndex, true, true, true);
        }
    }

    private void showAuthorityGridEdit(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            authorityGridEdit.show(currentRowIndex, false, false, false);
        } else {
            authorityGridEdit.show(currentRowIndex, true, true, true);
        }
    }

    public WalkThroughMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(WalkThroughMaster_Srv.class), WalkThroughMaster.class.getName());
        this.asWidget().setTitle(widgetName);

        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        manageDisplay(false);
        gridEvents();

        swi.setWidget(this);
    }

    private void topControlSave() {
        WalkThroughMaster_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            //ConsultantSharingMaster_Vld.chkSave(mrTemp);
            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
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

    private WalkThroughMaster_Mdl topControlSaveInitialize() {
        WalkThroughMaster_Mdl mrTemp = new WalkThroughMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));

        List<GrdAuthority_Mdl> OBJListAuth = new ArrayList<>();
        OBJListAuth.addAll(gridAuthority.getGrid().getStore().getAll());
        mrTemp.getGrdAuthority_Mdl().addAll(OBJListAuth);

        List<GrdDefaultDetail_Mdl> OBJListDefault = new ArrayList<>();
        OBJListDefault.addAll(gridDefaultDetail.getGrid().getStore().getAll());
        mrTemp.getGrdDefaultMdl().addAll(OBJListDefault);
        return mrTemp;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
        }
    }

    @Override
    public void onBlur(BlurEvent event) {

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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<WalkThroughMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<WalkThroughMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });

    }

    private void topControlMoveRecSuccess(DmResult<WalkThroughMaster_Mdl> result) {
        WalkThroughMaster_Mdl data;

        if (result.isStatus()) {
            data = result.getData();

            topControl.setDocId(data.getSharedKeyFieldCode());
            txtName.setText(data.getSharedKeyFieldName());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            gridAuthority.getGrid().getStore().clear();
            gridAuthority.getGrid().getStore().addAll(data.getGrdAuthority_Mdl());

            gridDefaultDetail.getGrid().getStore().clear();
            gridDefaultDetail.getGrid().getStore().addAll(data.getGrdDefaultMdl());

            manageDisplay(false);
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);

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
                    topControlCard();
                    clear();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onAdd() {

//        txtHConsultant.setTag(defaultValue.getEnquiryTypeCode());
//        txtHConsultant.setText(defaultValue.getEnquiryTypeName());
        gridAuthority.getGrid().getStore().add(new GrdAuthority_Mdl());
        gridDefaultDetail.getGrid().getStore().add(new GrdDefaultDetail_Mdl());
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void clear() {

        gridDefaultDetail.getGrid().getStore().clear();

        gridAuthority.getGrid().getStore().clear();

        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    private void manageDisplay(boolean flag) {

        txtName.setEnabled(flag);
    }

    @Override
    public void onClick(ClickEvent event) {

    }

    private void prnRecord(boolean flag) {

    }

}
