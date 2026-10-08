package dataman.accounts.fundInTransitEntry.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.accounts.fundInTransitEntry.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.HashMap;
import java.util.List;

public final class FundInTransitEntry extends FundInTransitEntry_Dsg {

    ServiceInvoker<FundInTransitEntry_SrvAsync> SrvAsync;
    String dateEnterByUser = DmClient.getLoginDate();
    String defaultVoucherType;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{"FNDIT"})) {

            @Override
            public String getVoucherType() {
                return defaultVoucherType;
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
                txtDtEntry.setTag2nd(defaultVoucherType);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtEntryNo.setEnabled(false);
                SrvAsync.invokeRPC().getEditCheck(topControl.getDocId(), new AsyncCallback<DmResult<String>>() {

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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(FundInTransitEntry.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtHCostCenter.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCostCenter_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCostCenter(helper, blnToEndOfFile, txtCurrentSite.getTag().toString(), new AsyncCallback<DmResultPageing<List<TxtHCostCenter_Mdl>>>() {

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
            public void getSelectedRow(boolean blnStatus, TxtHCostCenter_Mdl tRow) {
            }
        });
        txtHCrAccount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAccounts_Mdl>() {

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
            public void getSelectedRow(boolean blnStatus, TxtHAccounts_Mdl t1Row) {
            }
        });
        txtHDrAccount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAccounts_Mdl>() {

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
            public void getSelectedRow(boolean blnStatus, TxtHAccounts_Mdl t1Row) {
            }
        });
        txtHTransferNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHTransferNo_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTransferNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTransferNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTransferNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHTransferNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHTransferNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTransferNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHTransferNo_Mdl t1Row) {
                if (blnStatus) {
                    txtHTransferNo.setTag(t1Row.getSharedKeyFieldCode());
                    txtHTransferNo.setText(t1Row.getSharedKeyFieldName().trim() + "/" + t1Row.getVType().trim() + "/" + t1Row.getSubCode());

                    txtAmount.setText(DmShared.vNull(t1Row.getAmount()).toString());

                    txtHCrAccount.setTag(t1Row.getDefaultCode());
                    txtHCrAccount.setText(t1Row.getDefaultName());

                    txtTrnSite.setTag(t1Row.getSiteCode());
                    txtTrnSite.setText(t1Row.getSName());

                    txtTrnCrAccount.setTag(t1Row.getSubCode());
                    txtTrnCrAccount.setText(t1Row.getSubName());

                    txtTrnDrAccount.setTag(t1Row.getContraSub());
                    txtTrnDrAccount.setText(t1Row.getContraName());

                    txtHCostCenter.setTag(t1Row.getCostCenter());
                    txtHCostCenter.setText(t1Row.getCCName());

                    txtDtCheque.setText(DmShared.dNull(DmShared.convertDate(t1Row.getDtChq())));
                    txtChequeNo.setText(t1Row.getChqNo());
                    txtTrnRemark.setText(t1Row.getNarration());

                    txtHCostCenter.setFocus(blnStatus);
                } else {
                    txtHTransferNo.setFocus(true);
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

    public FundInTransitEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(FundInTransitEntry_Srv.class), FundInTransitEntry.class.getName());
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

    private FundInTransitEntry_Mdl topControlSaveInitialize() {
        FundInTransitEntry_Mdl mrTemp = new FundInTransitEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getObjRecId().setCounter(DmShared.iNull(txtEntryNo.getTag()).longValue());
            mrTemp.getObjRecId().setPrefix(DmShared.xNull(txtEntryNo.getTag2nd()));
            mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(txtEntryNo.getText()));
        }
        dateEnterByUser = txtDtEntry.getText();
        mrTemp.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        mrTemp.setChqNo(DmShared.xNull(txtChequeNo.getText()));
        mrTemp.setCostCenterTag(DmShared.xNull(txtHCostCenter.getTag()));
        mrTemp.setCostCenterText(DmShared.xNull(txtHCostCenter.getText()));
        mrTemp.setCrAccountDesc(DmShared.xNull(txtHCrAccount.getTag2nd()));
        mrTemp.setCrAccountTag(DmShared.xNull(txtHCrAccount.getTag()));
        mrTemp.setCrAccountText(DmShared.xNull(txtHCrAccount.getText()));
        mrTemp.setCurrentSiteTag(DmShared.xNull(txtCurrentSite.getTag()));
        mrTemp.setCurrentSiteText(DmShared.xNull(txtCurrentSite.getText()));
        mrTemp.setDrAccountDesc(DmShared.xNull(txtHDrAccount.getTag2nd()));
        mrTemp.setDrAccountTag(DmShared.xNull(txtHDrAccount.getTag()));
        mrTemp.setDrAccountText(DmShared.xNull(txtHDrAccount.getText()));
        mrTemp.setDtChq(DmShared.dNull(DmShared.convertDate(txtDtCheque.getText())));
        mrTemp.setDtClearing(DmShared.dNull(txtDtClearing.getText()));
        mrTemp.setDtEntry(DmShared.dNull(DmShared.convertDate(txtDtEntry.getText())));
        mrTemp.setVoucherPrefix(DmClient.getVoucherPrefix());
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtDtEntry.getTag2nd()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtEntryNo.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setTransCostCenterTag(DmShared.xNull(txtTrnCostCenter.getTag()));
        mrTemp.setTransCostCenterText(DmShared.xNull(txtTrnCostCenter.getText()));
        mrTemp.setTransCrAccountTag(DmShared.xNull(txtTrnCrAccount.getTag()));
        mrTemp.setTransCrAccountText(DmShared.xNull(txtTrnCrAccount.getText()));
        mrTemp.setTransDrAccountTag(DmShared.xNull(txtTrnDrAccount.getTag()));
        mrTemp.setTransDrAccountText(DmShared.xNull(txtTrnDrAccount.getText()));
        mrTemp.setTransRemark(DmShared.xNull(txtTrnRemark.getText()));
        mrTemp.setTransferNoTag(DmShared.xNull(txtHTransferNo.getTag()));
        mrTemp.setTransferNoText(DmShared.xNull(txtHTransferNo.getText()));
        mrTemp.setTransSiteTag(DmShared.xNull(txtTrnSite.getTag()));
        mrTemp.setTransSiteText(DmShared.xNull(txtTrnSite.getText()));
        return mrTemp;
    }

    private void topControlSave() {
        FundInTransitEntry_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            FundInTransitEntry_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(topControl.getPermissions(), mrTemp, new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtDtEntry).show();
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
            case 300:
                new DmAlertMessageBox(errorMessage, txtHTransferNo).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHDrAccount).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHCrAccount).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtAmount).show();
                break;
            case 700:
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
            SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<FundInTransitEntry_Mdl>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<FundInTransitEntry_Mdl> result) {
                    topControlMoveRecSuccess(result);
                }
            });
        } catch (Exception e) {
        }
    }

    private void topControlMoveRecSuccess(DmResult<FundInTransitEntry_Mdl> result) {
        FundInTransitEntry_Mdl data;
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtAmount.setText(DmClient.vNull(data.getAmount(), "0.00"));
            txtChequeNo.setText(data.getChqNo());
            txtCurrentSite.setTag(data.getCurrentSiteTag());
            txtCurrentSite.setText(data.getCurrentSiteText());
            txtDtCheque.setText(data.getDtChq());
            txtDtClearing.setText(data.getDtClearing());
            txtDtEntry.setText(data.getDtEntry());
            txtDtEntry.setTag(data.getVoucherPrefix());
            txtDtEntry.setTag2nd(DmShared.xNull(data.getVoucherTypeCode()));
            txtEntryNo.setText(data.getSharedKeyFieldName());
            txtEntryNo.setTag(DmShared.iNull(data.getObjRecId().getCounter()).toString());
            txtEntryNo.setTag2nd(DmShared.xNull(data.getObjRecId().getPrefix()));
            txtRemark.setText(data.getRemark());
            txtTrnCostCenter.setTag(data.getTransCostCenterTag());
            txtTrnCostCenter.setText(data.getTransCostCenterText());
            txtTrnCrAccount.setTag(data.getTransCrAccountTag());
            txtTrnDrAccount.setTag(data.getTransDrAccountTag());
            txtTrnDrAccount.setText(data.getTransDrAccountText());
            txtTrnRemark.setText(data.getTransRemark());
            txtTrnSite.setTag(data.getTransSiteTag());
            txtTrnSite.setText(data.getTransSiteText());

            txtHCostCenter.setTag(data.getCostCenterTag());
            txtHCostCenter.setText(data.getCostCenterText());

            txtHCrAccount.setTag(data.getCrAccountTag());
            txtHCrAccount.setText(data.getCrAccountText());

            txtHDrAccount.setTag(data.getDrAccountTag());
            txtHDrAccount.setText(data.getDrAccountText());

            txtHTransferNo.setTag(data.getTransferNoTag());
            txtHTransferNo.setText(data.getTransferNoText());

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
        txtTrnCostCenter.setEnabled(false);
        txtTrnCrAccount.setEnabled(false);
        txtTrnDrAccount.setEnabled(false);
        txtTrnRemark.setEnabled(false);
        txtTrnSite.setEnabled(false);
        txtAmount.setEnabled(false);
        txtChequeNo.setEnabled(false);
        txtDtCheque.setEnabled(false);
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
                        txtCurrentSite.setText(data.getSharedKeyFieldName()
                        );
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
                    defaultVoucherType = result.getData();
                    swi.setWidget(FundInTransitEntry.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void clear() {
        txtDtEntry.setText(dateEnterByUser);
        manageDisplay();
        txtEntryNo.setEnabled(false);
        siteCode();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
    }
    
        private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Entry No.", DmShared.xNull(txtEntryNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(txtDtEntry.getTag2nd()), "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }
}
