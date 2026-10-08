package dataman.accounts.accountOpeningEntry.client;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.accounts.accountOpeningEntry.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.List;

public final class AccountOpeningEntry extends AccountOpeningEntry_Dsg implements KeyUpHandler, ValueChangeHandler<String> {

    ServiceInvoker<AccountOpeningEntry_SrvAsync> SrvAsync;
    protected DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{"OPBAL"})) {

            @Override
            public String getVoucherType() {
                return txtHVoucherType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtVoucherDate.getText();
            }

            @Override
            public String getVoucherValue() {
                String rtnVal = "";
                if (DmShared.vNull(txtDebit.getText()) > 0.0) {
                    rtnVal = txtDebit.getText();
                } else if (DmShared.vNull(txtCredit.getText()) > 0.0) {
                    rtnVal = txtCredit.getText();
                }
                return rtnVal;
            }
        });

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    txtHVoucherType.setFocus(true);
                    manageDisplay(false, txtHAccountName.getTag2nd());
                } else {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is approved. You can't modify this record.", (DmTextBox) null).show();
                }

            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                        @Override
                        public void onDialogHide(DialogHideEvent event) {
                            if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                topControlDelete(topControl.getDocId());
                            }
                        }
                    }).show();
                } else {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is approved. You can't delete this record.", (DmTextBox) null).show();
                }
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AccountOpeningEntry.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {

        txtHAccountName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAccountName_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHAccountName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHAccountName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHAccountName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccountName).show();
                        }

                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHAccountName_Mdl tRow) {
                if (blnStatus) {
                    txtHAccountName.setTag2nd(tRow.getAccountNameAccDesc());
                    manageDisplay(false, DmShared.xNull(tRow.getAccountNameAccDesc()));
                    getGrdData(tRow.getSharedKeyFieldCode(), DmClient.getVoucherPrefix());
                }
            }
        });

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHCostCenter.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCostCenter_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCostCenter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCostCenter_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCostCenter).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCostCenter_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCostCenter.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCostCenter).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCostCenter_Mdl t1Row) {

            }
        });

    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(topControl.getPermissions(), helper, blnToEndOfFile, DmClient.getVoucherPrefix(), new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    private void handlerEvents() {
        txtDebit.addKeyUpHandler(this);
        txtCredit.addKeyUpHandler(this);
        txtVoucherDate.addValueChangeHandler(this);
        txtRefDate.addValueChangeHandler(this);
        txtChequeDate.addValueChangeHandler(this);
        txtClearDate.addValueChangeHandler(this);
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtDebit)) {
            if (!DmShared.xNull(txtDebit.getText()).equalsIgnoreCase("")) {
                txtCredit.setEnabled(false);
            } else {
                txtCredit.setEnabled(true);
            }
        } else if (event.getSource().equals(txtCredit)) {
            if (!DmShared.xNull(txtCredit.getText()).equalsIgnoreCase("")) {
                txtDebit.setEnabled(false);
            } else {
                txtDebit.setEnabled(true);
            }
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        if (event.getSource().equals(txtVoucherDate)) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
        } else if (event.getSource().equals(txtRefDate)) {
            txtRefDate.setText(DmClient.returnDate(txtRefDate.getText()));
        } else if (event.getSource().equals(txtChequeDate)) {
            txtChequeDate.setText(DmClient.returnDate(txtChequeDate.getText()));
        } else if (event.getSource().equals(txtClearDate)) {
            txtClearDate.setText(DmClient.returnDate(txtClearDate.getText()));
        }
    }

    private void btnClickEvents() {
        btnApprovedBy.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                SrvAsync.invokeRPC().setApproval(userControl.getTextBlog(approvedByBlog), topControl.getDocId(), new AsyncCallback<DmResult<String>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), btnApprovedBy).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            topControlMoveRec(result.getData());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), btnApprovedBy).show();
                        }
                    }
                });
            }
        });
    }

    public AccountOpeningEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<AccountOpeningEntry_SrvAsync>(GWT.create(AccountOpeningEntry_Srv.class), AccountOpeningEntry.class.getName());
        this.asWidget().setTitle(widgetName);
        handlerEvents();
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        btnClickEvents();
        iniDefaultValues(swi);
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    swi.setWidget(AccountOpeningEntry.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
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
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(topControl.getPermissions(), DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<AccountOpeningEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<AccountOpeningEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<AccountOpeningEntry_Mdl> result) {
        AccountOpeningEntry_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtChequeDate.setText(DmShared.xNull(data.getChqDate()));
            txtChequeNo.setText(DmShared.xNull(data.getChqNo()));
            txtCredit.setText(DmClient.vNull(data.getCredit(), "0.00"));
            txtDebit.setText(DmClient.vNull(data.getDebit(), "0.00"));
            txtFavourOf.setText(DmShared.xNull(data.getFavourOf()));
            txtHAccountName.setText(DmShared.xNull(data.getAccountName()));
            txtHAccountName.setTag(DmShared.xNull(data.getAccountNameCode()));
            txtHAccountName.setTag2nd(DmShared.xNull(data.getAccountNameAccDesc()));
            txtHCostCenter.setText(DmShared.xNull(data.getCostCenterName()));
            txtHCostCenter.setTag(DmShared.xNull(data.getCostCenterCode()));
            txtHVoucherType.setText(DmShared.xNull(data.getVoucherTypeName()));
            txtHVoucherType.setTag(DmShared.xNull(data.getVoucherTypeCode()));
            txtNarration.setText(DmShared.xNull(data.getNarration()));
            txtRefDate.setText(DmShared.xNull(data.getRefDate()));
            txtRefNo.setText(DmShared.xNull(data.getRefNo()));
            txtVoucherDate.setText(DmShared.xNull(data.getDate()));
            txtVoucherNo.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtVoucherNo.setTag(DmShared.iNull(data.getObjRecId().getCounter()).toString());
            txtVoucherNo.setTag2nd(DmShared.xNull(data.getObjRecId().getPrefix()));
            txtClearDate.setText(DmShared.xNull(data.getChqClearDate()));
            txtReconcilationRemark.setText(DmShared.xNull(data.getReconcilationRemark()));

            dmGrd.getGrid().getStore().clear();
            dmGrd.getGrid().getStore().addAll(data.getStrGrdData());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, data.getApprovedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlSave() {
        AccountOpeningEntry_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            AccountOpeningEntry_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(topControl.getPermissions(), mrTemp, new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");
                            clear();

                        } else {
                            topControlMoveRec(topControl.getDocId());
                        }
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });

            dmGrd.getGrid().getStore().clear();
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHVoucherType).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHAccountName).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtCredit).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtCredit).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtChequeDate).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtClearDate).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtClearDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHVoucherType).show();
                break;
        }

    }

    private AccountOpeningEntry_Mdl topControlSaveInitialize() {
        AccountOpeningEntry_Mdl mrTemp = new AccountOpeningEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getObjRecId().setCounter(DmShared.iNull(txtVoucherNo.getTag()).longValue());
            mrTemp.getObjRecId().setPrefix(DmShared.xNull(txtVoucherNo.getTag2nd()));
            mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(txtVoucherNo.getText()));
        }

        mrTemp.setAccountName(DmShared.xNull(txtHAccountName.getText()));
        mrTemp.setAccountNameCode(DmShared.xNull(txtHAccountName.getTag()));
        mrTemp.setAccountNameAccDesc(DmShared.xNull(txtHAccountName.getTag2nd()));
        mrTemp.setChqClearDate(DmShared.dNull(txtClearDate.getText()));
        mrTemp.setChqDate(DmShared.dNull(txtChequeDate.getText()));
        mrTemp.setChqNo(DmShared.xNull(txtChequeNo.getText()));
        mrTemp.setCostCenterCode(DmShared.xNull(txtHCostCenter.getTag()));
        mrTemp.setCostCenterName(DmShared.xNull(txtHCostCenter.getText()));
        mrTemp.setCredit(DmClient.vNull(txtCredit.getText(), "0.00"));
        mrTemp.setDate(DmShared.dNull(txtVoucherDate.getText()));
        mrTemp.setDebit(DmClient.vNull(txtDebit.getText(), "0.00"));
        mrTemp.setFavourOf(DmShared.xNull(txtFavourOf.getText()));
        mrTemp.setNarration(DmShared.xNull(txtNarration.getText()));
        mrTemp.setReconcilationRemark(DmShared.xNull(txtReconcilationRemark.getText()));
        mrTemp.setRefDate(DmShared.xNull(txtRefDate.getText()));
        mrTemp.setRefNo(DmShared.xNull(txtRefNo.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setVoucherTypeName(DmShared.xNull(txtHVoucherType.getText()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()));
        mrTemp.setvPrefix(DmClient.getVoucherPrefix());

        return mrTemp;
    }

    private void getGrdData(String accountNameCode, String VPrefix) {
        SrvAsync.invokeRPC().getFrmGrdData(accountNameCode, VPrefix, new AsyncCallback<DmResultPageing<List<GrdAccountOpeningEntry_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResultPageing<List<GrdAccountOpeningEntry_Mdl>> result) {
                dmGrd.getGrid().getStore().clear();
                dmGrd.getGrid().getStore().addAll(result.getData());
            }

        });

    }

    private void manageDisplay(Boolean blnEnb, String accountNameAccDesc) {
        if (accountNameAccDesc.equalsIgnoreCase(DmShared.xNull(defaultValue.getBankNatureCode()))) {
            txtChequeNo.setEnabled(true);
            txtChequeDate.setEnabled(true);
            txtFavourOf.setEnabled(true);
        } else {
            txtChequeNo.setEnabled(false);
            txtChequeDate.setEnabled(false);
            txtFavourOf.setEnabled(false);
        }
        txtClearDate.setEnabled(false);
        txtVoucherNo.setEnabled(false);
        txtVoucherDate.setEnabled(false);
        txtHVoucherType.setEnabled(blnEnb);
    }

    private void clear() {
        dmGrd.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
    }

    private void onAdd() {
        txtHVoucherType.setTag(DmShared.xNull(defaultValue.getSharedKeyFieldCode()));
        txtHVoucherType.setText(DmShared.xNull(defaultValue.getSharedKeyFieldName()));
//        txtVoucherDate.setText(DmClient.getLoginDate());
        txtVoucherDate.setText(DmClient.getStartDate());
        manageDisplay(false, "");
        txtHAccountName.setFocus(true);
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

}
