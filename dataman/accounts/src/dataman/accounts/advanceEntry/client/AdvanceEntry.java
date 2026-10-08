package dataman.accounts.advanceEntry.client;

import dataman.accounts.advanceEntry.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmCallBack;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.List;

public class AdvanceEntry extends AdvanceEntry_Dsg implements BlurHandler, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyPressHandler {

    DefaultValue_Mdl defaultValueMdl = new DefaultValue_Mdl();
    private AdvanceReqEntrySearch advReqSearch = new AdvanceReqEntrySearch();
    //  private ModifyReason_Mdl modifyReasonMdl;
    //  private final ModifyReason modifyReason = new ModifyReason();
    ServiceInvoker<AdvanceEntry_SrvAsync> SrvAsync;
    boolean isApprove = true, isDeleteFiles = true;
    final PaymentPopup paymentPopup = new PaymentPopup();
    PaymentPopUp_Mdl paymnetPopupMdl = new PaymentPopUp_Mdl();
    protected String tdsCategoryCode = "", tdsCategoryName = "";
    SetWidgetInvoker swi;
    private PrintPopup printPopup = new PrintPopup();

    public AdvanceEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(AdvanceEntry_Srv.class), AdvanceEntry.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        findEvents();
        manageEvent();
        textHelpEvents();
        this.swi = swi;
        iniDefaultValues(swi);
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new LibraryVoucherCategory().ADVANCE_ENTRY})) {

            @Override
            public String getVoucherType() {
                return defaultValueMdl.getSharedKeyFieldCode();
            }

            @Override
            public String getVoucherDate() {
                return txtVoucherDate.getText();
            }

            @Override
            public String getVoucherValue() {
                return "";
            }
        });
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(approvedByBlog)) {
                isApprove = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(deleteFileBlog)) {
                isDeleteFiles = true;
            }
        }

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();

            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    manageDisplay(true);
                    txtPaidAmt.setFocus(true);
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
            public void moveRec(String strKey) {
                topControlMoveRec(strKey);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {
                showPrintPopup();
            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {

                if (topControl.getMode().equalsIgnoreCase("Edit")) {
                    showPaymentDetails();
                } else {
                    showPaymentDetails();

                }
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AdvanceEntry.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void showPrintPopup() {
        printPopup.show(this);

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

    private void textHelpEvents() {

        advReqSearch.addCallBack(new AdvanceReqEntrySearch.CallBack() {
            @Override
            public void onOk(String advReqNo, String advReqDocid) {
                getAdvReqEntryDetail(advReqNo, advReqDocid, null);
            }

            @Override
            public void onClose() {
                txtAdvReqNo.setFocus(true);
            }
        });

        paymentPopup.addCallBack(new PaymentPopup.CallBack() {
            @Override
            public void onOk(PaymentPopUp_Mdl popUp_Mdl) {
                paymnetPopupMdl = popUp_Mdl;

                double totalPaidAmt = 0, totalOriginakAmt = 0d, totalTdsDeduction = 0;
                for (GrdPayment_Mdl payment_Mdl : paymnetPopupMdl.getLstPaymentMdl()) {
                    if (DmShared.iNull(payment_Mdl.getCardTypeCode()).longValue() != 0l) {
                        totalPaidAmt += DmShared.vNull(payment_Mdl.getNetPay()).doubleValue();
                        totalOriginakAmt += DmShared.vNull(payment_Mdl.getOriginalAmount()).doubleValue();
                        totalTdsDeduction += DmShared.vNull(payment_Mdl.getTdsDeduction()).doubleValue();
                    }
                    txtPaidAmt.setText(DmClient.vNull(totalPaidAmt, "0.00"));
                    txtOriginalAmt.setText(DmClient.vNull(totalOriginakAmt, "0.00"));
                    txtTotalTds.setText(DmClient.vNull(totalTdsDeduction, "0.00"));

                }

                if (topControl.getMode().equalsIgnoreCase("New")) {
                    topControlSave();
                } else {
                    topControlSave();
                }
            }

            @Override
            public void onClose() {

            }
        });

    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(topControl.getPermissions(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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

    private void topControlSave() {
        AdvanceEntry_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            AdvanceEntry_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, isApprove, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtAdvanceReqAmt).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());
                        topControlMoveRec(topControl.getDocId());

                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private AdvanceEntry_Mdl topControlSaveInitialize() {
        AdvanceEntry_Mdl mrTemp = new AdvanceEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecId().setCounter(DmShared.iNull(txtEntryNo.getTag2nd()).longValue());
            mrTemp.getRecId().setRecIdValue(DmShared.xNull(txtEntryNo.getText()));
            mrTemp.getRecId().setPrefix(DmShared.xNull(txtEntryNo.getTag()));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtEntryNo.getText()));
        mrTemp.setAdvReqDocId(DmShared.xNull(txtAdvReqNo.getTag()));
        mrTemp.setAdvReqRecId(DmShared.xNull(txtAdvReqNo.getText()));

        mrTemp.setPoDocId(DmShared.xNull(txtPurchaseOrder.getTag()));
        mrTemp.setPoRecId(DmShared.xNull(txtPurchaseOrder.getText()));

        mrTemp.setModuleTag(DmShared.xNull(txtModuleType.getTag()));
        mrTemp.setModuleText(DmShared.xNull(txtModuleType.getText()));

        mrTemp.setSupplierTag(DmShared.xNull(txtSupplierName.getTag()));
        mrTemp.setSupplierText(DmShared.xNull(txtSupplierName.getText()));

        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));

        mrTemp.setVoucherTypeTag(DmShared.xNull(txtVoucherType.getTag()));
        mrTemp.setVoucherTypeText(DmShared.xNull(txtVoucherType.getText()));

        mrTemp.setDueDate(DmShared.xNull(txtDueDate.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));

        mrTemp.setAdvanceReqAmt(DmShared.xNull(txtAdvanceReqAmt.getText()));
        mrTemp.setPartyLedgerBal(DmShared.xNull(txtPartyLedBal.getText()));
        mrTemp.setBankCashLedgerBal(DmShared.xNull(txtBankCashLedBal.getText()));
        mrTemp.setPaymentPopUp_Mdl(paymnetPopupMdl);
        mrTemp.setPaidAmount(DmShared.xNull(txtPaidAmt.getText()));
        mrTemp.setOriginalAmount(DmShared.xNull(txtOriginalAmt.getText()));
        mrTemp.setTdsDeduction(DmShared.xNull(txtTotalTds.getText()));
//
//        if (modifyReasonMdl != null) {
//            mrTemp.setReasonInfo_Mdl(modifyReasonMdl);
//        }
        return mrTemp;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtAdvReqNo).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtModuleType).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtAdvanceReqAmt).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtDueDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtPartyLedBal).show();
        }
    }

    private void setApproved(String docId, boolean isApprove) {
        try {
            SrvAsync.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControlMoveRec(result.getData());
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });

        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
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
    public void onClick(ClickEvent event) {

        btnClick((Button) event.getSource());
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
        }
        if (event.getSource() == txtDueDate) {
            txtDueDate.setText(DmClient.returnDate(txtDueDate.getText()));
        }

    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            btnClick(btnAdReqSearch);
            rootEvent.stopPropagation();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            paymentPopup.hide();
            advReqSearch.hide();
        }

    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getSource().equals(txtAdvReqNo)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!DmShared.xNull(txtAdvReqNo.getText()).trim().equalsIgnoreCase("")) {
                    getAdvReqEntryDetail(DmShared.xNull(txtAdvReqNo.getText()), "", null);
                } else {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            btnAdReqSearch.setFocus(true);
                        }
                    });
                }
            }
        }
    }

    private void getAdvReqEntryDetail(String advReNo, String advReqDocId, final DmCallBack callback) {
        try {
            SrvAsync.invokeRPC().getAdvReqEntryDetail(advReNo, advReqDocId, DmShared.xNull(txtModuleType.getTag()),
                    new AsyncCallback<DmResult<AdvanceEntry_Mdl>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtAdvReqNo).show();
                }

                @Override
                public void onSuccess(DmResult<AdvanceEntry_Mdl> result) {
                    if (result.isStatus() && result.getData() != null) {
                        clear();

                        fillRecords(result.getData());

                        txtVoucherDate.setEnabled(false);

                        if (callback != null) {
                            DmResult<Object> dmrTemp = new DmResult<>();
                            dmrTemp.setStatus(true);
                            callback.result(dmrTemp);
                        }

                        txtPaidAmt.setFocus(true);

                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtAdvReqNo).show();
                    }
                }

            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtAdvReqNo).show();
        }
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(topControl.getPermissions(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<AdvanceEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<AdvanceEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<AdvanceEntry_Mdl> result) {
        AdvanceEntry_Mdl data;

        if (result.isStatus()) {
            clear();
            data = result.getData();
            txtVoucherDate.setText(DmShared.xNull(data.getVoucherDate()));

            topControl.setDocId(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtEntryNo.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtEntryNo.setTag(DmShared.xNull(data.getRecId().getPrefix()));
            txtEntryNo.setTag2nd(DmShared.xNull(data.getRecId().getCounter().toString()));

            txtVoucherType.setTag(DmShared.xNull(data.getVoucherTypeTag()));
            txtVoucherType.setText(DmShared.xNull(data.getVoucherTypeText()));

            txtModuleType.setTag(DmShared.xNull(data.getModuleTag()));
            txtModuleType.setText(DmShared.xNull(data.getModuleText()));
            txtPartyLedBal.setText(DmClient.vNull(data.getPartyLedgerBal(), "0.00"));
            txtBankCashLedBal.setText(DmClient.vNull(data.getBankCashLedgerBal(), "0.00"));
            txtPaidAmt.setText(DmClient.vNull(data.getPaidAmount(), "0.00"));
            txtOriginalAmt.setText(DmClient.vNull(data.getOriginalAmount(), "0.00"));
            txtTotalTds.setText(DmClient.vNull(data.getTdsDeduction(), "0.00"));

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, data.getApprovedBy());
            paymnetPopupMdl = data.getPaymentPopUp_Mdl();
            fillRecords(data);

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        flwAdvReqSearch.clear();
        txtAdvReqNo.setEnabled(false);
    }

    private void fillRecords(AdvanceEntry_Mdl data) {
        if (data != null) {
            if (!DmShared.xNull(data.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                txtEntryNo.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            }

            txtAdvReqNo.setTag(DmShared.xNull(data.getAdvReqDocId()));
            txtAdvReqNo.setText(DmShared.xNull(data.getAdvReqRecId()));

            txtPurchaseOrder.setText(DmShared.xNull(data.getPoRecId()));
            txtPurchaseOrder.setTag(DmShared.xNull(data.getPoDocId()));

            txtPurchaseOrderDate.setText(DmShared.dNull(data.getPoDate()));

            txtSupplierName.setTag(DmShared.xNull(data.getSupplierTag()));
            txtSupplierName.setText(DmShared.xNull(data.getSupplierText()));
            txtSupplierDisplayName.setText(DmShared.xNull(data.getSupplierDisplayName()));

            txtModuleType.setTag(DmShared.xNull(data.getModuleTag()));
            txtModuleType.setText(DmShared.xNull(data.getModuleText()));

            txtAmt.setText(DmShared.xNull(data.getPoAmt()));

            txtAdvanceReqAmt.setText(DmShared.xNull(data.getAdvanceReqAmt()));
            txtDueDate.setText(DmShared.dNull(data.getDueDate()));
            txtRemark.setText(DmShared.xNull(data.getRemark()));

            txtPartyLedBal.setText(DmClient.vNull(data.getPartyLedgerBal(), "0.00"));
            txtBankCashLedBal.setText(DmClient.vNull(data.getBankCashLedgerBal(), "0.00"));

            grdPODetail.getGrid().getStore().clear();
            grdPODetail.getGrid().getStore().addAll(data.getLstGrdPoDetail());

            grdMRNDetail.getGrid().getStore().clear();
            grdMRNDetail.getGrid().getStore().addAll(data.getLstGrdMrnDetail());

            grdBillDetail.getGrid().getStore().clear();
            grdBillDetail.getGrid().getStore().addAll(data.getLstGrdBillDetail());

            grdAdvPaymnetDetail.getGrid().getStore().clear();
            grdAdvPaymnetDetail.getGrid().getStore().addAll(data.getLstGrdAdvPaymentDetails());

            grdFinalPaymnetDetail.getGrid().getStore().clear();
            grdFinalPaymnetDetail.getGrid().getStore().addAll(data.getLstGrdFinalPaymentDetails());

            tdsCategoryCode = DmShared.xNull(data.getTdsCategoryCode());
            calculate();

        }

    }

    private void calculate() {
        double totalPoAmt = 0, totalGrnAmt = 0, totalBillAmt = 0, totalAdvBillPay = 0, totalOrgAmt = 0d, totalTdsAmt = 0d, totalFinalPay = 0d, totalTaxableAmt = 0d;
        for (int i = 0; i < grdPODetail.getGrid().getStore().size(); i++) {
            if (DmShared.iNull(grdPODetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0l) {
                totalPoAmt += DmShared.vNull(grdPODetail.getGrid().getStore().get(i).getAmt()).doubleValue();
                totalTaxableAmt+=DmShared.vNull(grdPODetail.getGrid().getStore().get(i).getTaxableAmt()).doubleValue();
            }
        }
        txtTotalPoValue.setText(DmClient.vNull(totalPoAmt, "0.00"));
        txtTotalTaxableAmt.setText(DmClient.vNull(totalTaxableAmt, "0.00"));
        for (int i = 0; i < grdMRNDetail.getGrid().getStore().size(); i++) {
            if (DmShared.iNull(grdMRNDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0l) {
                totalGrnAmt += DmShared.vNull(grdMRNDetail.getGrid().getStore().get(i).getValue()).doubleValue();
            }
        }
        txtTotalMrn.setText(DmClient.vNull(totalGrnAmt, "0.00"));

        for (int i = 0; i < grdBillDetail.getGrid().getStore().size(); i++) {
            if (DmShared.iNull(grdBillDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0l) {
                totalBillAmt += DmShared.vNull(grdBillDetail.getGrid().getStore().get(i).getValue()).doubleValue();
            }
        }
        txtTotalBill.setText(DmClient.vNull(totalBillAmt, "0.00"));

        for (int i = 0; i < grdAdvPaymnetDetail.getGrid().getStore().size(); i++) {
            if (DmShared.iNull(grdAdvPaymnetDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0l) {
                totalAdvBillPay += DmShared.vNull(grdAdvPaymnetDetail.getGrid().getStore().get(i).getNetPay()).doubleValue();
                totalOrgAmt += DmShared.vNull(grdAdvPaymnetDetail.getGrid().getStore().get(i).getAmt()).doubleValue();
                totalTdsAmt += DmShared.vNull(grdAdvPaymnetDetail.getGrid().getStore().get(i).getTdsDeduction()).doubleValue();
            }

        }
        txtTotalAdvance.setText(DmClient.vNull(totalOrgAmt, "0.00"));

        for (int i = 0; i < grdFinalPaymnetDetail.getGrid().getStore().size(); i++) {
            if (DmShared.iNull(grdFinalPaymnetDetail.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0l) {
                totalFinalPay += DmShared.vNull(grdFinalPaymnetDetail.getGrid().getStore().get(i).getAmt()).doubleValue();

            }
        }
        txtTotalBillPay.setText(DmClient.vNull(totalFinalPay, "0.00"));
    }

    private void manageEvent() {
        ((Button) paymentCBlog.getControlBlog()).addClickHandler(this);
        txtVoucherDate.addBlurHandler(this);
        btnAdReqSearch.addClickHandler(this);
        btnApprovedBy.addClickHandler(this);
        flwForm.addAttachHandler(this);
        flwAdvReqSearch.addAttachHandler(this);
        txtAdvReqNo.addKeyPressHandler(this);

    }

    private void btnClick(Button button) {
        if (!button.isAttached()) {
            return;
        }
        if (!button.isVisible()) {
            return;
        }
        if (!button.isEnabled()) {
            return;
        }
        if (button == btnAdReqSearch) {

            advReqSearch.show();

        }
        if (button == paymentCBlog.getControlBlog()) {
            showPaymentDetails();

        }
        if (button.equals(btnApprovedBy)) {
            if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    setApproved(DmShared.iNull(topControl.getDocId()).toString(), true);
                } else {
                    setApproved(DmShared.iNull(topControl.getDocId()).toString(), false);
                }
            }
        }

    }

    private void showPaymentDetails() {

        paymentPopup.show(this, paymnetPopupMdl,DmShared.xNull(txtPurchaseOrder.getTag()).trim());

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
                    defaultValueMdl = result.getData();
                    swi.setWidget(AdvanceEntry.this);
                    manageDisplay(false);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        txtVoucherType.setTag(DmShared.xNull(defaultValueMdl.getSharedKeyFieldCode()));
        txtVoucherType.setText(DmShared.xNull(defaultValueMdl.getSharedKeyFieldName()));
        getServerDateTime();
    }

    private void getServerDateTime() {
        SrvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtPurchaseOrder).show();
            }

            @Override
            public void onSuccess(String[] result) {
                txtVoucherDate.setText(DmShared.dNull(result[0]));
                txtAdvReqNo.setFocus(true);

            }
        });
    }

    private void manageDisplay(boolean flag) {
        txtEntryNo.setEnabled(false);
        txtVoucherDate.setEnabled(false);
        txtAdvReqNo.setEnabled(true);
        txtModuleType.setEnabled(false);
        txtPurchaseOrderDate.setEnabled(false);
        txtPurchaseOrder.setEnabled(false);
        txtAmt.setEnabled(false);
        txtSupplierName.setEnabled(false);
        txtPartyLedBal.setEnabled(false);
        txtBankCashLedBal.setEnabled(false);

        txtTotalPoValue.setEnabled(false);
        txtTotalMrn.setEnabled(false);
        txtTotalBill.setEnabled(false);
        txtTotalAdvance.setEnabled(false);
        txtOriginalAmt.setEnabled(false);
        txtTotalTds.setEnabled(false);
        txtTotalBillPay.setEnabled(false);
        txtAdvanceReqAmt.setEnabled(false);
        txtDueDate.setEnabled(false);
        txtRemark.setEnabled(false);
        txtPaidAmt.setEnabled(false);
        txtOriginalAmt.setEnabled(false);
        txtTotalTds.setEnabled(false);
        txtTotalTaxableAmt.setEnabled(false);

        if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("New")) {
            flwAdvReqSearch.add(btnAdReqSearch);
            txtAdvReqNo.setEnabled(true);

        } else {
            flwAdvReqSearch.clear();
            txtAdvReqNo.setEnabled(false);

        }

    }

    private void clear() {
        tdsCategoryCode = "";
        tdsCategoryName = "";
        txtAdvReqNo.setText("");
        txtAdvReqNo.setTag("");

        txtPurchaseOrder.setText("");
        txtPurchaseOrder.setTag("");

        txtModuleType.setText("");
        txtModuleType.setTag("");

        txtTotalPoValue.setText("");
        txtTotalMrn.setText("");
        txtTotalBill.setText("");
        txtTotalBillPay.setText("");
        txtTotalAdvance.setText("");
        txtAdvanceReqAmt.setText("");
        txtDueDate.setText("");
        txtRemark.setText("");

        txtHAccount.setText("");
        txtHAccount.setTag("");

        txtPaidAmt.setText("0.00");
        txtOriginalAmt.setText("0.00");
        txtTotalTds.setText("0.00");
        txtBankCashLedBal.setText("0.00");
        txtPartyLedBal.setText("0.00");

        paymnetPopupMdl = new PaymentPopUp_Mdl();

        grdPODetail.getGrid().getStore().clear();
        grdAdvPaymnetDetail.getGrid().getStore().clear();
        grdBillDetail.getGrid().getStore().clear();
        grdFinalPaymnetDetail.getGrid().getStore().clear();
        grdMRNDetail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");

    }

//    private void showModifyReason() {
//        modifyReason.show(topControl.getMode(), topControl.getDocId());
//        modifyReason.addCallBack(new ModifyReason.CallBack() {
//
//            @Override
//            public void onOk(ModifyReason_Mdl modifyReason) {
//                try {
//                    modifyReasonMdl = modifyReason;
//                    topControlSave();
//                } catch (Exception e) {
//                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
//                }
//            }
//        });
//    }
 


}
