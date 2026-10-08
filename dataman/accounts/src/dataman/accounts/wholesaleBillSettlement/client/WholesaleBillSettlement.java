package dataman.accounts.wholesaleBillSettlement.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.accounts.wholesaleBillSettlement.shared.DefaultValue_Mdl;
import dataman.accounts.wholesaleBillSettlement.shared.Find_Mdl;
import dataman.accounts.wholesaleBillSettlement.shared.GrdPendingFPReqDetail_Mdl;
import dataman.accounts.wholesaleBillSettlement.shared.GridDrAdjustBills_Mdl;
import dataman.accounts.wholesaleBillSettlement.shared.LedgerAdj_Mdl;
import dataman.accounts.wholesaleBillSettlement.shared.TxtHAccountName_Mdl;
import dataman.accounts.wholesaleBillSettlement.shared.TxtHSupplier_Mdl;
import dataman.accounts.wholesaleBillSettlement.shared.TxtHVoucherType_Mdl;
import dataman.accounts.wholesaleBillSettlement.shared.WholesaleBillSettlement_Mdl;
import dataman.accounts.wholesaleBillSettlement.shared.WholesaleBillSettlement_Srv;
import dataman.accounts.wholesaleBillSettlement.shared.WholesaleBillSettlement_SrvAsync;
import dataman.accounts.wholesaleBillSettlement.shared.WholesaleBillSettlement_Vld;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
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
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WholesaleBillSettlement extends WholesaleBillSettlement_Dsg implements BlurHandler, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyPressHandler {

    DefaultValue_Mdl defaultValueMdl = new DefaultValue_Mdl();
    ServiceInvoker<WholesaleBillSettlement_SrvAsync> SrvAsync;
    SetWidgetInvoker swi;
    LedgerAdj_Mdl lgAdj;
    boolean isApprove = true,printRecord = false;
    boolean blnCheckAll = false, btnBlnCheckAll = false;
    double drAdjustAmt = 0d;
    String vr_Docid = "", vr_V_SNo = "";
    HashMap<String, List<HashMap<String, LedgerAdj_Mdl>>> hmMain = new HashMap<>();
    HashMap<String, FieldUDT_Mdl> hashMapFilterPrint = new HashMap<>();

    public WholesaleBillSettlement(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(WholesaleBillSettlement_Srv.class), WholesaleBillSettlement.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        findEvents();
        manageEvent();
        textHelpEvents();
        gridEvent();
        this.swi = swi;
        iniDefaultValues(swi);
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new LibraryVoucherCategory().WHOLE_SALE_BILL_SETTLEMENT})) {

            @Override
            public String getVoucherType() {
                return defaultValueMdl.getVoucherTypeCode();
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
                    manageDisplay();
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
                if (DmShared.iNull(txtNetDifference.getText()).intValue() != 0) {
                    new DmAlertMessageBox("Difference is not Zero. so Records Can't save.", (DmTextBox) null).show();
                } else {
                    topControlSave();
                }

            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(WholesaleBillSettlement.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void showPrintPopup() {

        prnRecord(false);
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
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
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

    private void textHelpEvents() {

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
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVoucherType_Mdl tRow) {
                if (blnStatus) {
                    if (tRow != null) {
                        clear();
                        txtHVoucherType.setText(tRow.getSharedKeyFieldName());
                        txtHVoucherType.setTag(tRow.getSharedKeyFieldCode());
                        txtHAccountName.setText("");
                        txtHAccountName.setTag("");
                        txtHAccountName.setTag2nd("");
                    }
                }
                txtHVoucherType.setFocus(true);
            }

        });

        txtHAccountName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAccountName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, DmShared.xNull(txtHVoucherType.getTag()), new AsyncCallback<DmResultPageing<List<TxtHAccountName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHAccountName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHAccountName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHAccountName_Mdl tRow) {
                if (blnStatus) {
                    if (tRow != null) {
                        txtHAccountName.setTag2nd(tRow.getNatureManualCode());
                    }
                }
                txtHAccountName.setFocus(true);
            }
        });

        txtHSupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSupplier_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHSupplier_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSupplier.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSupplier).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHSupplier_Mdl tRow) {
                if (blnStatus && !DmShared.xNull(tRow.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    txtSelectedBillDr.setText("0.00");
                    txtCrAdjustAmt.setText("0.00");
                    txtNetDifference.setText("0.00");
                    txtSupplierDisplayName.setText(DmShared.xNull(tRow.getDisplayName()));
                    getPendingFinalPaymnetReqDetail(tRow.getSharedKeyFieldCode());
                    getPendingFinalDebitAdjmentRec(tRow.getSharedKeyFieldCode());
                    drChkCheckAll.setStyleName("fa fa-square-o");
                    chkCheckAll.setStyleName("fa fa-square-o");
                }

                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        if (!DmShared.xNull(txtHSupplier.getTag()).trim().equalsIgnoreCase("")) {
                            //txt.setFocus(true);
                        }
                    }
                });
            }
        });

//        paymentPopup.addCallBack(new PaymentPopup.CallBack() {
//            @Override
//            public void onOk(PaymentPopUp_Mdl popUp_Mdl) {
//                paymnetPopupMdl = popUp_Mdl;
//                double orgAmt = 0d;
//                for (GrdPayment_Mdl node : paymnetPopupMdl.getLstPaymentMdl()) {
//                    orgAmt += DmShared.vNull(node.getOriginalAmount()).doubleValue();
//                }
//                if (DmShared.vNull(orgAmt).doubleValue() != DmShared.vNull(txtOriginalAmount.getText()).doubleValue()) {
//                    new DmAlertMessageBox("Can't add Amount not match.", txtOriginalAmount).show();
//                    return;
//                }
//                double totalPaidAmt = 0, totalOriginakAmt = 0d, totalTdsDeduction = 0;
//                for (GrdPayment_Mdl payment_Mdl : paymnetPopupMdl.getLstPaymentMdl()) {
//                    if (DmShared.iNull(payment_Mdl.getCardTypeCode()).longValue() != 0l) {
//                        totalPaidAmt += DmShared.vNull(payment_Mdl.getNetPay()).doubleValue();
//                        totalOriginakAmt += DmShared.vNull(payment_Mdl.getOriginalAmount()).doubleValue();
//                        totalTdsDeduction += DmShared.vNull(payment_Mdl.getTdsDeduction()).doubleValue();
//                    }
//                    txtAmountPaid.setText(DmClient.vNull(totalPaidAmt, "0.00"));
//                    txtOriginalAmount.setText(DmClient.vNull(totalOriginakAmt, "0.00"));
//                    txtTdsDeduction.setText(DmClient.vNull(totalTdsDeduction, "0.00"));
//
//                }
//
//                if (topControl.getMode().equalsIgnoreCase("New")) {
//                    topControlSave();
//                } else {
//                    topControlSave();
//                }
//            }
//
//            @Override
//            public void onClose() {
//
//            }
//        });
    }

    private void gridEvent() {
        grdPeningFPReqDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                grdPeningFPReqDetail.lockScroll();
                if (event.getColumnIndex() == 1 && DmShared.xNull(topControl.getMode()).equalsIgnoreCase("New")) {
                    grdCheckedValue(event.getRowIndex());
                }
                grdPeningFPReqDetail.releaseScroll();
            }
        });

        grdDrdAdjustBills.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                grdDrdAdjustBills.lockScroll();
                boolean ischeck = false;

                if (event.getColumnIndex() == 1 && DmShared.xNull(topControl.getMode()).equalsIgnoreCase("New")) {
                    for (GrdPendingFPReqDetail_Mdl node : grdPeningFPReqDetail.getGrid().getStore().getAll()) {
                        if (DmShared.xNull(node.getChecked()).equalsIgnoreCase("1")) {
                            ischeck = true;
                            break;
                        }
                    }
                    if (ischeck) {
                        grdDrdCheckedValue(event.getRowIndex(), false);
                    } else {
                        new DmAlertMessageBox("Atleast one Records select for adjustment.", txtHSupplier).show();
                    }

                }
                grdDrdAdjustBills.releaseScroll();
            }
        });
    }

    private void calculate() {
        double netValue = 0d, selectAmt = 0d, drAdjustAmt = 0d;

        for (GrdPendingFPReqDetail_Mdl node : grdPeningFPReqDetail.getGrid().getStore().getAll()) {
            if (DmShared.xNull(node.getChecked()).equalsIgnoreCase("1")) {
                selectAmt += DmShared.vNull(node.getBillAmountAmt()).doubleValue();
                drAdjustAmt += DmShared.vNull(node.getAdjustedAmt()).doubleValue();
            }

        }

        txtSelectedBillDr.setText(DmClient.vNull(selectAmt, "0.00"));
        txtCrAdjustAmt.setText(DmClient.vNull(drAdjustAmt, "0.00"));
        netValue = DmShared.vNull(txtSelectedBillDr.getText()).doubleValue()
                - DmShared.vNull(txtCrAdjustAmt.getText()).doubleValue();
        txtNetDifference.setText(DmClient.vNull(netValue, "0.00"));

    }

    private void grdCheckedValue(Integer currentRowIndex) {
        if (!topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            blnCheckAll = false;
            chkCheckAll.setStyleName("fa fa-square-o");
            boolean checkCrAmt = false;

            if (DmShared.iNull(grdPeningFPReqDetail.getGrid().getStore().get(currentRowIndex).getChecked()).intValue() == 1) {
                for (int i = 0; i < grdPeningFPReqDetail.getGrid().getStore().getAll().size(); i++) {
                    GrdPendingFPReqDetail_Mdl gaMdl = grdPeningFPReqDetail.getGrid().getStore().getAll().get(i);
                    if (DmShared.vNull(gaMdl.getAdjustedAmt()).doubleValue() > 0 && i == currentRowIndex) {
                        checkCrAmt = true;
                    }
                }
                if (checkCrAmt) {
                    new DmAlertMessageBox("Adjust Amount found can't be Diselect this recoads.", (DmTextBox) null).show();
                } else {
                    grdPeningFPReqDetail.getGrid().getStore().get(currentRowIndex).setChecked("0");
                }

                calculate();

            } else {
                grdPeningFPReqDetail.getGrid().getStore().get(currentRowIndex).setChecked("1");
                calculate();
            }
            grdPeningFPReqDetail.commitChanges();
        }
    }

    private void getPendingFinalPaymnetReqDetail(String supplierCode) {
        SrvAsync.invokeRPC(true).getPendingFinalPaymnetReqDetail(supplierCode, new AsyncCallback<DmResult<List<GrdPendingFPReqDetail_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdPendingFPReqDetail_Mdl>> result) {
                if (result.isStatus()) {
//                    txtOriginalAmount.setText("0.00");
//                    txtLessOtherCr.setText("0.00");
//                    txtNetValueCr.setText("0.00");
//                    txtSelectedBillDr.setText("0.00");
//                    txtPartyAcBalance.setText("0.00");
//                    txtBankCashBalance.setText("0.00");
//                    txtPartyAcBalance.setText(result.getValue1());
                    grdPeningFPReqDetail.getGrid().getStore().clear();
                    grdPeningFPReqDetail.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlSave() {
        WholesaleBillSettlement_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            WholesaleBillSettlement_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, isApprove, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());
                        resetDrAdjust();
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

    private WholesaleBillSettlement_Mdl topControlSaveInitialize() {
        WholesaleBillSettlement_Mdl mrTemp = new WholesaleBillSettlement_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecId().setCounter(DmShared.iNull(txtPayNo.getTag2nd()).longValue());
            mrTemp.getRecId().setRecIdValue(DmShared.xNull(txtPayNo.getText()));
            mrTemp.getRecId().setPrefix(DmShared.xNull(txtPayNo.getTag()));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtPayNo.getText()));
        mrTemp.setSupplierTag(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setSupplierText(DmShared.xNull(txtHSupplier.getText()));
        mrTemp.setAccountNameCode(DmShared.xNull(txtHAccountName.getTag()));
        mrTemp.setAccountName(DmShared.xNull(txtHAccountName.getText()));

        mrTemp.setV_Date(DmShared.dNull(txtVoucherDate.getText()));

        mrTemp.setVoucherTypeTag(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setVoucherTypeText(DmShared.xNull(txtHVoucherType.getText()));
//        mrTemp.setCreditAmt(DmShared.xNull(txtCreditAmt.getText()));
        mrTemp.setTotalBillAmt(DmShared.xNull(txtSelectedBillDr.getText()));
        mrTemp.setAdjustmentAmt(DmShared.xNull(txtCrAdjustAmt.getText()));
        mrTemp.setNetvalueAmt(DmShared.xNull(txtNetDifference.getText()));
//        mrTemp.setNetAmt(DmShared.xNull(txtOriginalAmount.getText()));
        mrTemp.getLstPendingFPayReq().addAll(grdPeningFPReqDetail.getGrid().getStore().getAll());
        mrTemp.getLstdrAdjustAmt().addAll(grdDrdAdjustBills.getGrid().getStore().getAll());
        mrTemp.setListHmMain(hmMain);
        return mrTemp;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHVoucherType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHSupplier).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHAccountName).show();
                break;
//            case 400:
//                new DmAlertMessageBox(errorMessage, txtCreditAmt).show();
//                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHSupplier).show();
        }
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
                    swi.setWidget(WholesaleBillSettlement.this);
                    manageDisplay();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<WholesaleBillSettlement_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<WholesaleBillSettlement_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<WholesaleBillSettlement_Mdl> result) {
        WholesaleBillSettlement_Mdl data;
        if (result.isStatus()) {
            clear();
            resetDrAdjust();
            data = result.getData();
            txtVoucherDate.setText(DmShared.xNull(data.getV_Date()));
            topControl.setDocId(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtPayNo.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtPayNo.setTag(DmShared.xNull(data.getRecId().getPrefix()));
            txtPayNo.setTag2nd(DmShared.iNull(data.getRecId().getCounter()).toString());
            txtHVoucherType.setTag(DmShared.xNull(data.getVoucherTypeTag()));
            txtHVoucherType.setText(DmShared.xNull(data.getVoucherTypeText()));

            txtHSupplier.setTag(DmShared.xNull(data.getSupplierTag()));
            txtHSupplier.setText(DmShared.xNull(data.getSupplierText()));
            txtRemak.setText(DmShared.xNull(data.getRemark()));

//            txtSelectedBillDr.setText(DmClient.vNull(data.getTotalBillAmt(), "0.00"));
//            txtLessOtherCr.setText(DmClient.vNull(data.getLessOtherCr(), "0.00"));
//            txtNetValueCr.setText(DmClient.vNull(data.getNetValueCr(), "0.00"));
//            txtTdsDeduction.setText(DmClient.vNull(data.getTdsDeduction(), "0.00"));
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, data.getApprovedBy());
            fillPendingFPReqGrid(data);
            fillDebitAdjusmentRec(data);
            calculate();
            for (GridDrAdjustBills_Mdl node : grdDrdAdjustBills.getGrid().getStore().getAll()) {
                if (DmShared.xNull(node.getChecked()).equalsIgnoreCase("1")) {
                    drAdjustAmt += DmShared.vNull(node.getPendingAdjustment()).doubleValue();
                }

            }
            txtCrAdjustAmt.setText(DmClient.vNull(drAdjustAmt, "0.00"));
            txtNetDifference.setText("0");

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void fillPendingFPReqGrid(WholesaleBillSettlement_Mdl data) {
        grdPeningFPReqDetail.getGrid().getStore().clear();
        grdPeningFPReqDetail.getGrid().getStore().addAll(data.getLstPendingFPayReq());
    }

    private void onAdd() {
        clear();
        manageDisplay();
        txtHVoucherType.setTag(DmShared.xNull(defaultValueMdl.getVoucherTypeCode()));
        txtHVoucherType.setText(DmShared.xNull(defaultValueMdl.getVoucherTypeName()));
        getServerDateTime();
    }

    private void getServerDateTime() {
        SrvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();
            }

            @Override
            public void onSuccess(String[] result) {
                txtVoucherDate.setText(DmShared.dNull(result[0]));

            }
        });
    }

    private void manageDisplay() {
        txtPayNo.setEnabled(false);
        txtHVoucherType.setEnabled(false);
        txtVoucherDate.setEnabled(false);
        txtPartyAcBalance.setEnabled(false);
        txtBankCashBalance.setEnabled(false);
        txtLessOtherCr.setEnabled(false);
        txtSelectedBillDr.setEnabled(false);
        txtNetDifference.setEnabled(false);
//        txtOriginalAmount.setEnabled(false);
        txtAmountPaid.setEnabled(false);
        txtTdsDeduction.setEnabled(false);
        txtHSupplier.setEnabled(false);
        txtCrAdjustAmt.setEnabled(false);

        if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("New")) {
            txtHSupplier.setEnabled(true);
            txtRemak.setEnabled(true);
        }
    }

    private void manageEvent() {
        txtVoucherDate.addBlurHandler(this);
        btnApprovedBy.addClickHandler(this);
        flwForm.addAttachHandler(this);
        chkCheckAll.addClickHandler(this);
        drChkCheckAll.addClickHandler(this);

    }

    private void clear() {
        txtHSupplier.setText("");
        txtHSupplier.setTag("");
//        txtOriginalAmount.setText("0.00");
        txtLessOtherCr.setText("0.00");
        txtNetDifference.setText("0.00");
        txtSelectedBillDr.setText("0.00");
        txtPartyAcBalance.setText("0.00");
        txtBankCashBalance.setText("0.00");
        grdPeningFPReqDetail.getGrid().getStore().clear();
        grdDrdAdjustBills.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
        txtCrAdjustAmt.setText("0.00");

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
    public void onBlur(BlurEvent event) {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(chkCheckAll)) {
            if (topControl.getMode().trim().equalsIgnoreCase("New")) {
                manageAllCheck();
            }

        }
        if (event.getSource().equals(drChkCheckAll)) {
            if (topControl.getMode().trim().equalsIgnoreCase("New")) {
                drManageAllCheck();
            }
        }
        if (event.getSource().equals(btnApprovedBy)) {
            btnClick((Button) event.getSource());
        }

    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {

    }

    @Override
    public void onKeyPress(KeyPressEvent event) {

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

    private void manageAllCheck() {
        if (!blnCheckAll) {
            blnCheckAll = true;
            chkCheckAll.setStyleName("fa fa-check-square-o");
        } else {
            blnCheckAll = false;
            chkCheckAll.setStyleName("fa fa-square-o");
        }
        setAllChkBox(0);
    }

    private void drManageAllCheck() {
        if (!btnBlnCheckAll) {
            btnBlnCheckAll = true;
            drChkCheckAll.setStyleName("fa fa-check-square-o");
        } else {
            btnBlnCheckAll = false;
            drChkCheckAll.setStyleName("fa fa-square-o");
        }
        setAllDrChkBox(0);
    }

    private void setAllDrChkBox(int columnIndex) {

        if (columnIndex == 0) {
            List<GridDrAdjustBills_Mdl> lst = grdDrdAdjustBills.getGrid().getStore().getAll();
            if (btnBlnCheckAll) {
                boolean ischeck = false;

                if (columnIndex == 0 && DmShared.xNull(topControl.getMode()).equalsIgnoreCase("New")) {
                    for (GrdPendingFPReqDetail_Mdl node : grdPeningFPReqDetail.getGrid().getStore().getAll()) {
                        if (DmShared.xNull(node.getChecked()).equalsIgnoreCase("1")) {
                            ischeck = true;
                            break;
                        }
                    }
                    if (ischeck) {
                        for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                            grdDrdCheckedValue(rowIndex, true);
                        }
                    } else {
                        btnBlnCheckAll = false;
                        drChkCheckAll.setStyleName("fa fa-square-o");
                        new DmAlertMessageBox("Atleast one Records select for adjustment.", txtHSupplier).show();
                    }

                }

            } else {
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                    grdDrdCheckedValue(rowIndex, false);
                }
            }
            grdDrdAdjustBills.commitChanges();
        }
    }

    private void setAllChkBox(int columnIndex) {
        if (columnIndex == 0) {
            List<GrdPendingFPReqDetail_Mdl> lst = grdPeningFPReqDetail.getGrid().getStore().getAll();
            if (blnCheckAll) {
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                    grdPeningFPReqDetail.getGrid().getStore().get(rowIndex).setChecked("1");
                    calculate();
                }
            } else {
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                    grdPeningFPReqDetail.getGrid().getStore().get(rowIndex).setChecked("0");
                    calculate();
                }
            }
            grdPeningFPReqDetail.commitChanges();
        }
    }

    private void getPendingFinalDebitAdjmentRec(String supplierCode) {
        SrvAsync.invokeRPC(true).getPendingFinalCreditAdjmentRec(supplierCode, new AsyncCallback<DmResult<List<GridDrAdjustBills_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GridDrAdjustBills_Mdl>> result) {
                if (result.isStatus()) {
                    hmMain.clear();
                    txtCrAdjustAmt.setText("");
                    grdDrdAdjustBills.getGrid().getStore().clear();
                    grdDrdAdjustBills.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void fillDebitAdjusmentRec(WholesaleBillSettlement_Mdl data) {
        grdDrdAdjustBills.getGrid().getStore().clear();
        grdDrdAdjustBills.getGrid().getStore().addAll(data.getLstdrAdjustAmt());
    }

    private void resetDrAdjust() {
        drAdjustAmt = 0d;
        hmMain.clear();
    }

    private void autoAdjust(Integer currentRowIndex) {

        drAdjustAmt = 0.00;

        for (int i = 0; i < grdDrdAdjustBills.getGrid().getStore().getAll().size(); i++) {
            GridDrAdjustBills_Mdl drdMdl = grdDrdAdjustBills.getGrid().getStore().getAll().get(i);
            if (DmShared.xNull(drdMdl.getChecked()).trim().equals("1")) {
                drAdjustAmt = DmShared.vNull(drdMdl.getPendingAdjustment());
                vr_Docid = drdMdl.getSharedKeyFieldCode();
                vr_V_SNo = drdMdl.getVr_V_SNo();
                lgAdj = new LedgerAdj_Mdl();

                hmMain.put(vr_Docid, calculateAdj(drAdjustAmt));

            }
        }
        grdDrdAdjustBills.commitChanges();
        calculate();
    }

    private void grdDrdCheckedValue(Integer currentRowIndex, boolean callFromCheckAll) {

        if (!topControl.getMode().trim().equalsIgnoreCase("Browse")) {

            if (DmShared.iNull(grdDrdAdjustBills.getGrid().getStore().get(currentRowIndex).getChecked()).intValue() == 1 && !callFromCheckAll) {

                btnBlnCheckAll = false;
                drChkCheckAll.setStyleName("fa fa-square-o");

                grdDrdAdjustBills.getGrid().getStore().get(currentRowIndex).setChecked("0");

                calculate();
                for (GrdPendingFPReqDetail_Mdl node : grdPeningFPReqDetail.getGrid().getStore().getAll()) {
                    if (DmShared.xNull(node.getChecked()).trim().equals("1")) {
                        node.setPendingToAdjustAmt(DmShared.xNull(node.getBillAmountAmt()));
                        node.setAdjustedAmt("0");
                    }

                }
                hmMain.clear();
                autoAdjust(currentRowIndex);
                grdPeningFPReqDetail.commitChanges();

            } else {

                for (GrdPendingFPReqDetail_Mdl node : grdPeningFPReqDetail.getGrid().getStore().getAll()) {
                    if (DmShared.xNull(node.getChecked()).trim().equals("1")) {
                        node.setPendingToAdjustAmt(DmShared.xNull(node.getBillAmountAmt()));
                        node.setAdjustedAmt("0");
                    }

                }
                hmMain.clear();
                grdDrdAdjustBills.getGrid().getStore().get(currentRowIndex).setChecked("1");
                autoAdjust(currentRowIndex);
            }
            grdDrdAdjustBills.commitChanges();

        }
    }

    private List<HashMap<String, LedgerAdj_Mdl>> calculateAdj(double workingAmount) {

        List<HashMap<String, LedgerAdj_Mdl>> listOfMaps = new ArrayList<>();

        double amount = 0;
        String adj_DocId = "";
        HashMap<String, LedgerAdj_Mdl> hm = new HashMap<>();

        for (int i = 0; i < grdPeningFPReqDetail.getGrid().getStore().getAll().size(); i++) {
            GrdPendingFPReqDetail_Mdl gaMdl = grdPeningFPReqDetail.getGrid().getStore().getAll().get(i);
            if (gaMdl != null) {
                if (DmShared.xNull(gaMdl.getChecked()).trim().equals("1") && DmShared.vNull(gaMdl.getPendingToAdjustAmt()).doubleValue() != 0) {
                    lgAdj = new LedgerAdj_Mdl();
                    if (hm.containsKey(lgAdj.getAdj_DocId())) {
                        hm.remove(lgAdj.getAdj_DocId());
                    }

                    amount = DmShared.vNull(gaMdl.getPendingToAdjustAmt());

                    if ((workingAmount - amount) >= 0) {
                        adj_DocId = DmShared.xNull(gaMdl.getSharedKeyFieldCode());
                        lgAdj.setAdj_V_SNo(DmShared.xNull(gaMdl.getLgV_SNo()));
                        lgAdj.setAdjustment(DmClient.vNull(gaMdl.getPendingToAdjustAmt(), "0.00"));
                        gaMdl.setPendingToAdjustAmt("0");
                        gaMdl.setAdjustedAmt(DmClient.vNull(amount + DmShared.vNull(gaMdl.getAdjustedAmt()), "0.00"));
                        lgAdj.setVr_V_SNo(DmShared.xNull(vr_V_SNo));
                        hm.put(adj_DocId, lgAdj);
                        workingAmount -= amount;
                    } else if (workingAmount != 0) {
                        adj_DocId = DmShared.xNull(gaMdl.getSharedKeyFieldCode());
                        lgAdj.setAdj_V_SNo(DmShared.xNull(gaMdl.getLgV_SNo()));
                        lgAdj.setAdjustment(DmClient.vNull(workingAmount, "0.00"));
                        gaMdl.setAdjustedAmt(DmClient.vNull(workingAmount + DmShared.vNull(gaMdl.getAdjustedAmt()), "0.00"));
                        gaMdl.setPendingToAdjustAmt(DmClient.vNull((DmShared.vNull(gaMdl.getPendingToAdjustAmt()) - workingAmount), "0.00"));
                        lgAdj.setVr_V_SNo(DmShared.xNull(vr_V_SNo));
                        hm.put(adj_DocId, lgAdj);
                        workingAmount = 0.00;
                    }

                }
            }

        }
        grdPeningFPReqDetail.commitChanges();

        if (!listOfMaps.isEmpty()) {
            listOfMaps.clear();
        }
        listOfMaps.add(hm);

        return listOfMaps;

    }
    
    private void prnRecord(boolean printToPrinter) {
        hashMapFilterPrint.put("docId", new FieldUDT_Mdl("Return No.", DmShared.xNull(txtPayNo.getText()), DmShared.xNull(topControl.getDocId())));

        new DmConfirmMessageBox("Are you sure? You want to print this record.", new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    printRecord = true;
                    finalPrint(printRecord, hashMapFilterPrint);
                } else {
                    printRecord = false;
                    finalPrint(printRecord, hashMapFilterPrint);
                }
            }
        }).show();
    }
    
    private void finalPrint(boolean printToPrinter, HashMap hashMapFilter) {
        
        new OpenSourceReport(this.txtHVoucherType.getTag(), "", "Whole Sale Bill Settlement", widgetName, hashMapFilterPrint, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

    }
}
