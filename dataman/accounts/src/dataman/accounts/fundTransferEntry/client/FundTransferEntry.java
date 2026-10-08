package dataman.accounts.fundTransferEntry.client;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.accounts.fundTransferEntry.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp.TextHelpCallBack;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import java.util.List;

public final class FundTransferEntry extends FundTransferEntry_Dsg {

    ServiceInvoker<FundTransferEntry_SrvAsync> SrvAsync;
    String dateEnterByUser = DmClient.getLoginDate();
    String voucherType;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{"FNDTR"})) {

            @Override
            public String getVoucherType() {
                return voucherType;
            }

            @Override
            public String getVoucherDate() {
                return txtDtEntry.getText();
            }

            @Override
            public String getVoucherValue() {
                return txtAmount.getText();
            }
        });

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtDtEntry.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtEntryNo.setEnabled(false);
                SrvAsync.invokeRPC().getEditCheck(DmShared.xNull(topControl.getDocId()), new AsyncCallback<DmResult<String>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtDtEntry).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            manageDisplay();
                            txtDtEntry.setFocus(true);
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtDtEntry).show();
                            topControl.btnClick("");
                        }
                    }
                });
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(FundTransferEntry.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtHCrAccount.addTextHelpCallBack(new TextHelpCallBack<TxtHAccounts_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHAccounts_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCrAccount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHAccounts_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCrAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCrAccount).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHAccounts_Mdl tRow) {
                if (blnStatus) {
                    txtHCrAccount.setTag2nd(tRow.getNature());
                }
            }
        });
        txtHDrAccount.addTextHelpCallBack(new TextHelpCallBack<TxtHAccounts_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHAccounts_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDrAccount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHAccounts_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHDrAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDrAccount).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHAccounts_Mdl tRow) {

                if (blnStatus) {
                    txtHDrAccount.setTag2nd(tRow.getNature());
                }
            }
        });
        txtHFromCostCenter.addTextHelpCallBack(new TextHelpCallBack<TxtHFromCostCenter_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHFromCostCenter(helper, blnToEndOfFile, (String) txtHTransferSite.getTag(), new AsyncCallback<DmResultPageing<List<TxtHFromCostCenter_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHFromCostCenter).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHFromCostCenter_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHFromCostCenter.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHFromCostCenter).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHFromCostCenter_Mdl tRow) {

            }
        });
        txtHTransferSite.addTextHelpCallBack(new TextHelpCallBack<TxtHTransferSite_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTransferSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTransferSite_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTransferSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHTransferSite_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHTransferSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTransferSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHTransferSite_Mdl tRow) {
                if (blnStatus) {
                    txtHDrAccount.setTag(tRow.getSubCode());
                    txtHDrAccount.setText(tRow.getAccountName());

                    txtHCrAccount.setTag("");
                    txtHCrAccount.setText("");
                }
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
                            if (result.isStatus()) {
                                dmFind.setData(result.getData(), result.getPaging());
                            } else {
                                new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                            }
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

    private void txtblurEvents() {
        txtDtEntry.addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                txtDtEntry.setText(DmClient.returnDate(txtDtEntry.getText()));
            }
        });
        txtDtCheque.addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                txtDtCheque.setText(DmClient.returnDate(txtDtCheque.getText()));
            }
        });
        txtDtClearing.addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                txtDtClearing.setText(DmClient.returnDate(txtDtClearing.getText()));
            }
        });
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

    public FundTransferEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(FundTransferEntry_Srv.class), FundTransferEntry.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        txtblurEvents();
        btnClickEvents();
        onLoadGetVType(swi);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private FundTransferEntry_Mdl topControlSaveInitialize() {
        FundTransferEntry_Mdl mrTemp = new FundTransferEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getObjRecId().setCounter(DmShared.iNull(txtEntryNo.getTag()).longValue());
            mrTemp.getObjRecId().setPrefix(DmShared.xNull(txtEntryNo.getTag2nd()));
            mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(txtEntryNo.getText()));
        }
        dateEnterByUser = txtDtEntry.getText();
        mrTemp.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        mrTemp.setChaqNo(DmShared.xNull(txtChequeNo.getText()));
        mrTemp.setCostCenterTag(DmShared.xNull(txtHFromCostCenter.getTag()));
        mrTemp.setCostCenterText(DmShared.xNull(txtHFromCostCenter.getText()));
        mrTemp.setCrAccountDesc(DmShared.xNull(txtHCrAccount.getTag2nd()));
        mrTemp.setCrAccountTag(DmShared.xNull(txtHCrAccount.getTag()));
        mrTemp.setCrAccountText(DmShared.xNull(txtHCrAccount.getText()));
        mrTemp.setCurrentSiteTag(DmShared.xNull(txtCurrentSite.getTag()));
        mrTemp.setCurrentSiteText(DmShared.xNull(txtCurrentSite.getText()));
        mrTemp.setDrAccountDesc(DmShared.xNull(txtHDrAccount.getTag2nd()));
        mrTemp.setDrAccountTag(DmShared.xNull(txtHDrAccount.getTag()));
        mrTemp.setDrAccountText(DmShared.xNull(txtHDrAccount.getText()));
        mrTemp.setDtCheque(DmShared.dNull(DmShared.convertDate(txtDtCheque.getText())));
        mrTemp.setDtClearing(DmShared.dNull(DmShared.convertDate(txtDtClearing.getText())));
        mrTemp.setDtEntry(DmShared.dNull(DmShared.convertDate(txtDtEntry.getText())));
        mrTemp.setVoucherPrefix(DmClient.getVoucherPrefix());
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtEntryNo.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setTrnCostCenterTag(DmShared.xNull(txtToCostCenter.getTag()));
        mrTemp.setTrnCostCenterText(DmShared.xNull(txtToCostCenter.getText()));
        mrTemp.setTrnCrAccountDesc(DmShared.xNull(txtTrnCrAccount.getTag2nd()));
        mrTemp.setTrnCrAccountTag(DmShared.xNull(txtTrnCrAccount.getTag()));
        mrTemp.setTrnCrAccountText(DmShared.xNull(txtTrnCrAccount.getText()));
        mrTemp.setTrnDrAccountDesc(DmShared.xNull(txtTrnDrAccount.getTag2nd()));
        mrTemp.setTrnDrAccountTag(DmShared.xNull(txtTrnDrAccount.getTag()));
        mrTemp.setTrnDrAccountText(DmShared.xNull(txtTrnDrAccount.getText()));
        mrTemp.setTrnRemark(DmShared.xNull(txtTrnRemark.getText()));
        mrTemp.setTrnToSiteTag(DmShared.xNull(txtHTransferSite.getTag()));
        mrTemp.setTrnToSiteText(DmShared.xNull(txtHTransferSite.getText()));
        return mrTemp;
    }

    private void topControlSave() {
        FundTransferEntry_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            FundTransferEntry_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(topControl.getPermissions(), mrTemp, new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtEntryNo).show();
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
                new DmAlertMessageBox(errorMessage, txtDtEntry).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtCurrentSite).show();
                break;
            case 250:
                new DmAlertMessageBox(errorMessage, txtHTransferSite).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHDrAccount).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHCrAccount).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtAmount).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtDtClearing).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtDtEntry).show();
                break;
        }

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
        try {
            SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<FundTransferEntry_Mdl>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<FundTransferEntry_Mdl> result) {
                    topControlMoveRecSuccess(result);
                }
            });
        } catch (Exception e) {
        }
    }

    private void topControlMoveRecSuccess(DmResult<FundTransferEntry_Mdl> result) {
        FundTransferEntry_Mdl data;
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtAmount.setText(DmClient.vNull(data.getAmount(), "0.00"));
            txtChequeNo.setText(data.getChaqNo());
            txtTrnCrAccount.setText(data.getTrnCrAccountText());
            txtTrnCrAccount.setTag(data.getTrnCrAccountTag());
            txtCurrentSite.setTag(data.getCurrentSiteTag());
            txtCurrentSite.setText(data.getCurrentSiteText());
            txtTrnDrAccount.setTag(data.getTrnDrAccountTag());
            txtTrnDrAccount.setText(data.getTrnDrAccountText());
            txtDtCheque.setText(data.getDtCheque());
            txtDtClearing.setText(data.getDtClearing());
            txtDtEntry.setText(data.getDtEntry());
            txtDtEntry.setTag(data.getVoucherPrefix());
            txtEntryNo.setText(data.getSharedKeyFieldName());
            txtEntryNo.setTag(DmShared.iNull(data.getObjRecId().getCounter()).toString());
            txtEntryNo.setTag2nd(DmShared.xNull(data.getObjRecId().getPrefix()));

            txtHCrAccount.setTag(data.getCrAccountTag());
            txtHCrAccount.setTag2nd(data.getCrAccountDesc());
            txtHCrAccount.setText(data.getCrAccountText());

            txtHDrAccount.setTag(data.getDrAccountTag());
            txtHDrAccount.setTag2nd(data.getDrAccountDesc());
            txtHDrAccount.setText(data.getDrAccountText());

            txtHFromCostCenter.setTag(data.getCostCenterTag());
            txtHFromCostCenter.setText(data.getCostCenterText());

            txtHTransferSite.setTag(data.getTrnToSiteTag());
            txtHTransferSite.setText(data.getTrnToSiteText());

            txtRemark.setText(data.getRemark());
            txtToCostCenter.setTag(data.getTrnCostCenterTag());
            txtToCostCenter.setText(data.getTrnCostCenterText());
            txtTrnRemark.setText(data.getTrnRemark());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, data.getApprovedBy());
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
                    clear();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void manageDisplay() {
        txtCurrentSite.setEnabled(false);
        txtToCostCenter.setEnabled(false);
        txtTrnCrAccount.setEnabled(false);
        txtTrnDrAccount.setEnabled(false);
        txtTrnRemark.setEnabled(false);
    }

    private void siteCode() {
        try {
            SrvAsync.invokeRPC().getCurrentSite(new AsyncCallback<DmResult<KeyFieldExtended>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<KeyFieldExtended> result) {
                    if (result.isStatus()) {
                        KeyFieldExtended data;
                        data = result.getData();

                        txtCurrentSite.setTag(data.getSharedKeyFieldCode());
                        txtCurrentSite.setText(data.getSharedKeyFieldName());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void onLoadGetVType(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC().getVoucherType(new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    voucherType = result.getData();
                    swi.setWidget(FundTransferEntry.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void clear() {
        txtDtEntry.setText(dateEnterByUser);
        txtEntryNo.setEnabled(false);
        manageDisplay();
        siteCode();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
    }
}
