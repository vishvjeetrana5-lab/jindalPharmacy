package aarogya.pharmacy.organizationBillingOnRateMaster.client;

import aarogya.pharmacy.organizationBillingOnRateMaster.shared.*;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public final class OrganizationBillingOnRateMaster extends OrganizationBillingOnRateMaster_Dsg {

    private ServiceInvoker<OrganizationBillingOnRateMaster_SrvAsync> SrvAsync;
    private ModifyReason_Mdl modifyReasonMdl;
    private final ModifyReason modifyReason = new ModifyReason();

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                OrganizationBillingOnRateMaster.this.modifyReasonMdl = modifyReason;
                if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                    topControlDelete(topControl.getDocId());
                } else {
                    topControlSave();
                }
            }
        });
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clearBlogs();
                onAdd();
                //txtMedicineComposition.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHMedicineSaleOn.setFocus(true);
                manageDisplay(true);

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
//                topControlSave();
                showModifyReason();
            }

        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(OrganizationBillingOnRateMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtHOrganisation.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHOrganization(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOrganisation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOrganisation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOrganisation).show();
                        }
                    }
                });

            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });
        txtHMedicineSaleOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineSaleOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineSaleOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedicineSaleOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineSaleOn).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
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

    public OrganizationBillingOnRateMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("A", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("D", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(OrganizationBillingOnRateMaster_Srv.class), OrganizationBillingOnRateMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        manageDisplay(false);
        swi.setWidget(this);
    }

    public void onAdd() {
        manageDisplay(true);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private OrganizationBillingOnRateMaster_Mdl topControlSaveInitialize() {
        OrganizationBillingOnRateMaster_Mdl mrTemp = new OrganizationBillingOnRateMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHOrganisation.getText()).trim());
        mrTemp.setMedicineSaleOnTag(DmShared.xNull(txtHMedicineSaleOn.getTag()));

        if (modifyReasonMdl != null) {
            mrTemp.setModifyReason(modifyReasonMdl);
        }

        return mrTemp;
    }

    private void topControlSave() {
        OrganizationBillingOnRateMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            OrganizationBillingOnRateMaster_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
//                    new DmAlertMessageBox(caught.getMessage(), txtNursingSta).show();
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
                new DmAlertMessageBox(errorMessage, txtHOrganisation).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHMedicineSaleOn).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHMedicineSaleOn).show();
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<OrganizationBillingOnRateMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<OrganizationBillingOnRateMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<OrganizationBillingOnRateMaster_Mdl> result) {
        OrganizationBillingOnRateMaster_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtHOrganisation.setTag(data.getSharedKeyFieldCode());
            txtHOrganisation.setText(data.getSharedKeyFieldName());
            txtHMedicineSaleOn.setTag(DmShared.xNull(data.getMedicineSaleOnTag()));
            txtHMedicineSaleOn.setText(DmShared.xNull(data.getMedicineSaleOnText()));

            userControl.setTextBlog(preparedByBlog, DmShared.xNull(data.getSharedPreparedBy()));
            userControl.setTextBlog(modifiedByBlog, DmShared.xNull(data.getSharedModifiedBy()));
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        manageDisplay(false);
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
//        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {
//
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
//            }
//
//            @Override
//            public void onSuccess(DmResult<String> result) {
//                if (result.isStatus()) {
//                    clearBlogs();
//                    topControlCard();
//                } else {
//                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
//                }
//            }
//        });
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    public void clearBlogs() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    private void manageDisplay(boolean flag) {
        txtHOrganisation.setEnabled(false);
    }
}
