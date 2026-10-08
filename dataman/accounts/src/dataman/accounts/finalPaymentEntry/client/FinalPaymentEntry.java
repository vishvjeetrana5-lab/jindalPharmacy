package dataman.accounts.finalPaymentEntry.client;

import dataman.accounts.finalPaymentEntry.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
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
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FinalPaymentEntry extends FinalPaymentEntry_Dsg implements BlurHandler, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyPressHandler {

    DefaultValue_Mdl defaultValueMdl = new DefaultValue_Mdl();
    ServiceInvoker<FinalPaymentEntry_SrvAsync> SrvAsync;
    boolean isApprove = true;
    final PaymentPopup paymentPopup = new PaymentPopup();
    PaymentPopUp_Mdl paymnetPopupMdl = new PaymentPopUp_Mdl();
    protected String tdsCategoryCode = "", tdsCategoryName = "";
    final OtherAcGridEdit<GrdOtherAc_Mdl, GridAcDetail_PrpAcs> grdEditOtherAc = new OtherAcGridEdit(grdAcDetail);
    SetWidgetInvoker swi;
    private PrintPopup printPopup = new PrintPopup();
    boolean blnCheckAll = false, btnBlnCheckAll = false;
    HashMap<String, List<HashMap<String, LedgerAdj_Mdl>>> hmMain = new HashMap<>();
//    HashMap<String, LedgerAdj_Mdl> hm;
//    List<HashMap<String, LedgerAdj_Mdl>> listOfMaps;
    String vr_Docid = "", vr_V_SNo = "";
    LedgerAdj_Mdl lgAdj;
    double drAdjustAmt = 0d;

    protected String bankLetterDocid = "";

    public FinalPaymentEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(FinalPaymentEntry_Srv.class), FinalPaymentEntry.class.getName());
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

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new LibraryVoucherCategory().FINAL_PAYMENT_ENTRY})) {

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
                    grdAcDetail.getGrid().getStore().add(new GrdOtherAc_Mdl());
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
                if (DmShared.vNull(txtOriginalAmount.getText()).doubleValue() > 0) {
                    showPaymentDetails();
                } else {
                    topControlSave();
                }

            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(FinalPaymentEntry.this);
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

        txtHModuleType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHModuleType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHModuleType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHModuleType.setData(result.getData(), result.getPaging());

                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHModuleType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (t1Row != null) {

                }
            }
        });
        txtHModuleType.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            if (!DmShared.xNull(txtHModuleType.getTag()).trim().equalsIgnoreCase("")) {
                                txtHSupplier.setFocus(true);
                            } else {
                                txtHModuleType.setFocus(true);

                            }
                        }
                    });
                }
            }
        });
        txtHSupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSupplier_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSupplier(txtVoucherType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>>() {

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
                    tdsCategoryCode = DmShared.xNull(tRow.getTdsCatCode());
                    txtSupplierDisplayName.setText(DmShared.xNull(tRow.getDisplayName()));
                    getPendingFinalPaymnetReqDetail(tRow.getSharedKeyFieldCode());
                    getPendingFinalDebitAdjmentRec(txtHSupplier.getTag());
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

        paymentPopup.addCallBack(new PaymentPopup.CallBack() {
            @Override
            public void onOk(PaymentPopUp_Mdl popUp_Mdl) {
                paymnetPopupMdl = popUp_Mdl;
                double orgAmt = 0d;
                for (GrdPayment_Mdl node : paymnetPopupMdl.getLstPaymentMdl()) {
                    orgAmt += DmShared.vNull(node.getOriginalAmount()).doubleValue();
                }
                if (DmShared.vNull(orgAmt).doubleValue() != DmShared.vNull(txtOriginalAmount.getText()).doubleValue()) {
                    new DmAlertMessageBox("Can't add Amount not match.", txtOriginalAmount).show();
                    return;
                }
                double totalPaidAmt = 0, totalOriginakAmt = 0d, totalTdsDeduction = 0;
                for (GrdPayment_Mdl payment_Mdl : paymnetPopupMdl.getLstPaymentMdl()) {
                    if (DmShared.iNull(payment_Mdl.getCardTypeCode()).longValue() != 0l) {
                        totalPaidAmt += DmShared.vNull(payment_Mdl.getNetPay()).doubleValue();
                        totalOriginakAmt += DmShared.vNull(payment_Mdl.getOriginalAmount()).doubleValue();
                        totalTdsDeduction += DmShared.vNull(payment_Mdl.getTdsDeduction()).doubleValue();
                    }
                    txtAmountPaid.setText(DmClient.vNull(totalPaidAmt, "0.00"));
                    txtOriginalAmount.setText(DmClient.vNull(totalOriginakAmt, "0.00"));
                    txtTdsDeduction.setText(DmClient.vNull(totalTdsDeduction, "0.00"));

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

    private void gridEvent() {
        grdPeningFPReqDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {

                if (event.getColumnIndex() == 1 && DmShared.xNull(topControl.getMode()).equalsIgnoreCase("New")) {
                    grdCheckedValue(event.getRowIndex());
                }
            }
        });

        grdDrdAdjust.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                grdDrdAdjust.lockScroll();
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
                        new DmAlertMessageBox("Atleast one Records select for adjustment.", txtOriginalAmount).show();
                    }

                }
                grdDrdAdjust.releaseScroll();
            }
        });

//        grdDrdAdjust.getGrid().addDomHandler(new KeyDownHandler() {
//            @Override
//            public void onKeyDown(KeyDownEvent event) {
//                if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
//
//                    boolean ischeck = false;
//
//                    if (grdDrdAdjust.getSelectedCell().getCell() == 1 && DmShared.xNull(topControl.getMode()).equalsIgnoreCase("New")) {
//                        for (GrdPendingFPReqDetail_Mdl node : grdPeningFPReqDetail.getGrid().getStore().getAll()) {
//                            if (DmShared.xNull(node.getChecked()).equalsIgnoreCase("1")) {
//                                ischeck = true;
//                                break;
//                            }
//                        }
//                        if (ischeck) {
//                            grdDrdCheckedValue(grdDrdAdjust.getSelectedCell().getRow());
//                        } else {
//                            new DmAlertMessageBox("Atleast one Records select for adjustment.", txtOriginalAmount).show();
//                        }
//                    } else if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
//                        if (grdDrdAdjust.getGrid().getStore().indexOf(grdDrdAdjust.getGrid().getSelectionModel().getSelectedItem()) == 0) {
////                        txtSearchValue.setFocus(true);
//                        }
//                    }
//                }
//            }
//        }, KeyDownEvent.getType());
        grdEditOtherAc.addCallBack(new OtherAcGridEdit.CallBack() {

            @Override
            public void onOk(GrdOtherAc_Mdl grdOtherMdl) {
                if (DmShared.vNull(grdOtherMdl.getAmount()).doubleValue() > DmShared.vNull(txtSelectedBillDr.getText()).doubleValue()) {
                    new DmAlertMessageBox("Can't add going Negative.", txtOriginalAmount).show();
                    return;
                }
                grdAcDetail.commitChanges();
                txtLessOtherCr.setText(DmClient.vNull(grdOtherMdl.getAmount(), "0.00"));
                calculate();
            }

            @Override
            public void onDelete() {
                grdAcDetail.commitChanges();
                txtLessOtherCr.setText("0.00");
                calculate();
            }
        });

        grdAcDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showOtherAcGridEdit(event.getRowIndex());
            }
        });

        grdAcDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    showOtherAcGridEdit(grdAcDetail.getGrid().getStore().indexOf(grdAcDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showOtherAcGridEdit(int currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            grdEditOtherAc.show(currentRowIndex, false, false, false);
        } else {
            grdEditOtherAc.show(currentRowIndex, true, true, true);
        }
    }

    private void calculate() {
        double netValue = 0d, selectAmt = 0d, lessOtherCr = 0d, drAdjustAmt = 0d;

        for (GrdPendingFPReqDetail_Mdl node : grdPeningFPReqDetail.getGrid().getStore().getAll()) {
            if (DmShared.xNull(node.getChecked()).equalsIgnoreCase("1")) {
                selectAmt += DmShared.vNull(node.getFinalPayReq()).doubleValue();
                drAdjustAmt += DmShared.vNull(node.getDrAdjustAmt()).doubleValue();
            }

        }
        for (GrdOtherAc_Mdl node : grdAcDetail.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(node.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                lessOtherCr += DmShared.vNull(node.getAmount()).doubleValue();
            }

        }
//        for (Map.Entry<String, List<HashMap<String, LedgerAdj_Mdl>>> entry : hmMain.entrySet()) {
//            List<HashMap<String, LedgerAdj_Mdl>> listOfMaps = entry.getValue();
//            for (HashMap<String, LedgerAdj_Mdl> innerMap : listOfMaps) {
//                for (Map.Entry<String, LedgerAdj_Mdl> innerEntry : innerMap.entrySet()) {
//                    LedgerAdj_Mdl ledgerMdlValue = innerEntry.getValue();
//                    
//                }
//            }
//        }

        txtSelectedBillDr.setText(DmClient.vNull(selectAmt, "0.00"));
        txtLessOtherCr.setText(DmClient.vNull(lessOtherCr, "0.00"));
        txtDrAdjustAmt.setText(DmClient.vNull(drAdjustAmt, "0.00"));
        netValue = DmShared.vNull(txtSelectedBillDr.getText()).doubleValue()
                - DmShared.vNull(txtLessOtherCr.getText()).doubleValue()
                - DmShared.vNull(txtDrAdjustAmt.getText()).doubleValue();
        txtNetValueCr.setText(DmClient.vNull(netValue, "0.00"));
        txtOriginalAmount.setText(DmClient.vNull(txtNetValueCr.getText(), "0.00"));

    }

    private void grdCheckedValue(Integer currentRowIndex) {
        if (!topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            blnCheckAll = false;
            chkCheckAll.setStyleName("fa fa-square-o");
            boolean checkDrAmt = false;

            if (DmShared.iNull(grdPeningFPReqDetail.getGrid().getStore().get(currentRowIndex).getChecked()).intValue() == 1) {
                for (int i = 0; i < grdPeningFPReqDetail.getGrid().getStore().getAll().size(); i++) {
                    GrdPendingFPReqDetail_Mdl gaMdl = grdPeningFPReqDetail.getGrid().getStore().getAll().get(i);
                    if (DmShared.vNull(gaMdl.getDrAdjustAmt()).doubleValue() > 0 && i == currentRowIndex) {
                        checkDrAmt = true;
                    }
                }
                if (checkDrAmt) {
                    new DmAlertMessageBox("Dr. Adjust Amount found can't be Diselect.", (DmTextBox) null).show();
                } else {
                    grdPeningFPReqDetail.getGrid().getStore().get(currentRowIndex).setChecked("0");
                }

                calculate();

            } else {
                grdPeningFPReqDetail.getGrid().getStore().get(currentRowIndex).setChecked("1");
                calculate();
            }
            grdPeningFPReqDetail.commitChanges();
            grdAcDetail.getGrid().getStore().clear();
            grdAcDetail.getGrid().getStore().add(new GrdOtherAc_Mdl());
        }
    }

    private void getPendingFinalPaymnetReqDetail(String supplierCode) {
        SrvAsync.invokeRPC(true).getPendingFinalPaymnetReqDetail(supplierCode, txtHModuleType.getTag(), new AsyncCallback<DmResult<List<GrdPendingFPReqDetail_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdPendingFPReqDetail_Mdl>> result) {
                if (result.isStatus()) {
                    txtOriginalAmount.setText("0.00");
                    txtLessOtherCr.setText("0.00");
                    txtNetValueCr.setText("0.00");
                    txtSelectedBillDr.setText("0.00");
                    txtPartyAcBalance.setText("0.00");
                    txtBankCashBalance.setText("0.00");
                    txtPartyAcBalance.setText(result.getValue1());
                    grdPeningFPReqDetail.getGrid().getStore().clear();
                    grdPeningFPReqDetail.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void topControlSave() {
        FinalPaymentEntry_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            FinalPaymentEntry_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, isApprove, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtOriginalAmount).show();
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

    private FinalPaymentEntry_Mdl topControlSaveInitialize() {
        FinalPaymentEntry_Mdl mrTemp = new FinalPaymentEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecId().setCounter(DmShared.iNull(txtPayNo.getTag2nd()).longValue());
            mrTemp.getRecId().setRecIdValue(DmShared.xNull(txtPayNo.getText()));
            mrTemp.getRecId().setPrefix(DmShared.xNull(txtPayNo.getTag()));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtPayNo.getText()));
        mrTemp.setModuleTag(DmShared.xNull(txtHModuleType.getTag()));
        mrTemp.setModuleText(DmShared.xNull(txtHModuleType.getText()));

        mrTemp.setSupplierTag(DmShared.xNull(txtHSupplier.getTag()));
        mrTemp.setSupplierText(DmShared.xNull(txtHSupplier.getText()));

        mrTemp.setV_Date(DmShared.dNull(txtVoucherDate.getText()));

        mrTemp.setVoucherTypeTag(DmShared.xNull(txtVoucherType.getTag()));
        mrTemp.setVoucherTypeText(DmShared.xNull(txtVoucherType.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemak.getText()));
        mrTemp.setDrAdjustAmt(DmShared.xNull(txtDrAdjustAmt.getText()));

        mrTemp.setPaidAmt(DmShared.xNull(txtAmountPaid.getText()));
        mrTemp.setOriginalAmt(DmShared.xNull(txtOriginalAmount.getText()));
        mrTemp.setTdsDeduction(DmShared.xNull(txtTdsDeduction.getText()));
        mrTemp.setPaymentPopUp_Mdl(paymnetPopupMdl);
        mrTemp.getLstOtherAc().addAll(grdAcDetail.getGrid().getStore().getAll());
        mrTemp.getLstPendingFPayReq().addAll(grdPeningFPReqDetail.getGrid().getStore().getAll());
        mrTemp.getLstdrAdjustAmt().addAll(grdDrdAdjust.getGrid().getStore().getAll());
        mrTemp.setListHmMain(hmMain);
        return mrTemp;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHModuleType).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHSupplier).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtOriginalAmount).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHModuleType).show();
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
                    swi.setWidget(FinalPaymentEntry.this);
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<FinalPaymentEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<FinalPaymentEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<FinalPaymentEntry_Mdl> result) {
        FinalPaymentEntry_Mdl data;
        if (result.isStatus()) {
            clear();
            resetDrAdjust();
            data = result.getData();
            txtVoucherDate.setText(DmShared.xNull(data.getV_Date()));

            topControl.setDocId(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtPayNo.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtPayNo.setTag(DmShared.xNull(data.getRecId().getPrefix()));
            txtPayNo.setTag2nd(DmShared.iNull(data.getRecId().getCounter()).toString());

            txtVoucherType.setTag(DmShared.xNull(data.getVoucherTypeTag()));
            txtVoucherType.setText(DmShared.xNull(data.getVoucherTypeText()));

            txtHModuleType.setTag(DmShared.xNull(data.getModuleTag()));
            txtHModuleType.setText(DmShared.xNull(data.getModuleText()));

            txtHSupplier.setTag(DmShared.xNull(data.getSupplierTag()));
            txtHSupplier.setText(DmShared.xNull(data.getSupplierText()));
            txtSupplierDisplayName.setText(DmShared.xNull(data.getSupplierDisplayName()));
            txtRemak.setText(DmShared.xNull(data.getRemark()));
            txtPartyAcBalance.setText(DmClient.vNull(data.getPartyAcBalance(), "0.00"));
            txtBankCashBalance.setText(DmClient.vNull(data.getBankCashLedgerBalance(), "0.00"));
            txtAmountPaid.setText(DmClient.vNull(data.getPaidAmt(), "0.00"));

            txtSelectedBillDr.setText(DmClient.vNull(data.getSelectedBillDr(), "0.00"));
            txtLessOtherCr.setText(DmClient.vNull(data.getLessOtherCr(), "0.00"));
            txtNetValueCr.setText(DmClient.vNull(data.getNetValueCr(), "0.00"));
            txtTdsDeduction.setText(DmClient.vNull(data.getTdsDeduction(), "0.00"));

            grdAcDetail.getGrid().getStore().clear();
            grdAcDetail.getGrid().getStore().addAll(data.getLstOtherAc());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            userControl.setTextBlog(approvedByBlog, data.getApprovedBy());
            paymnetPopupMdl = data.getPaymentPopUp_Mdl();
            tdsCategoryCode = DmShared.xNull(data.getTdsCategoryCode());
            tdsCategoryName = DmShared.xNull(data.getTdsCategoryName());
            bankLetterDocid = DmShared.xNull(data.getBankLetterDocid());

            fillPendingFPReqGrid(data);
            fillDebitAdjusmentRec(data);
            calculate();
            txtDrAdjustAmt.setText(DmClient.vNull(data.getDrAdjustAmt(), "0.00"));

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void fillPendingFPReqGrid(FinalPaymentEntry_Mdl data) {
        grdPeningFPReqDetail.getGrid().getStore().clear();
        grdPeningFPReqDetail.getGrid().getStore().addAll(data.getLstPendingFPayReq());
    }

    private void onAdd() {
        clear();
        manageDisplay();
        grdAcDetail.getGrid().getStore().add(new GrdOtherAc_Mdl());
        txtVoucherType.setTag(DmShared.xNull(defaultValueMdl.getVoucherTypeCode()));
        txtVoucherType.setText(DmShared.xNull(defaultValueMdl.getVoucherTypeName()));
        txtHModuleType.setTag("PO");
        txtHModuleType.setText("Purchase");
        getServerDateTime();
    }

    private void getServerDateTime() {
        SrvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtOriginalAmount).show();
            }

            @Override
            public void onSuccess(String[] result) {
                txtVoucherDate.setText(DmShared.dNull(result[0]));

            }
        });
    }

    private void manageDisplay() {
        txtPayNo.setEnabled(false);
        txtVoucherDate.setEnabled(false);
        txtPartyAcBalance.setEnabled(false);
        txtBankCashBalance.setEnabled(false);
        txtLessOtherCr.setEnabled(false);
        txtSelectedBillDr.setEnabled(false);
        txtNetValueCr.setEnabled(false);
        txtOriginalAmount.setEnabled(false);
        txtAmountPaid.setEnabled(false);
        txtTdsDeduction.setEnabled(false);
        txtHModuleType.setEnabled(false);
        txtHSupplier.setEnabled(false);
        txtDrAdjustAmt.setEnabled(false);

        if (DmShared.xNull(topControl.getMode()).equalsIgnoreCase("New")) {
            txtHModuleType.setEnabled(true);
            txtHSupplier.setEnabled(true);
            txtRemak.setEnabled(true);
        } else {

        }
    }

    private void manageEvent() {
        ((Button) paymentCBlog.getControlBlog()).addClickHandler(this);
        txtVoucherDate.addBlurHandler(this);
        btnApprovedBy.addClickHandler(this);
        flwForm.addAttachHandler(this);
        chkCheckAll.addClickHandler(this);
        drChkCheckAll.addClickHandler(this);

    }

    private void clear() {
        tdsCategoryCode = "";
        tdsCategoryName = "";
        bankLetterDocid = "";
        txtHModuleType.setText("");
        txtHModuleType.setTag("");
        txtHSupplier.setText("");
        txtHSupplier.setTag("");
        txtOriginalAmount.setText("0.00");
        txtLessOtherCr.setText("0.00");
        txtNetValueCr.setText("0.00");
        txtSelectedBillDr.setText("0.00");
        txtPartyAcBalance.setText("0.00");
        txtBankCashBalance.setText("0.00");
        paymnetPopupMdl = new PaymentPopUp_Mdl();
        grdAcDetail.getGrid().getStore().clear();
        grdPeningFPReqDetail.getGrid().getStore().clear();
        grdDrdAdjust.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
        txtDrAdjustAmt.setText("0.00");

    }

    private void showPaymentDetails() {
        paymentPopup.show(this, paymnetPopupMdl);

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
        if (event.getSource().equals(paymentCBlog.getControlBlog()) || event.getSource().equals(btnApprovedBy)) {
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
        if (button == paymentCBlog.getControlBlog()) {
            showPaymentDetails();

        }
        if (button.equals(btnApprovedBy)) {
            if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    setApproved(DmShared.iNull(topControl.getDocId()).toString(), true);
                } else {

                    if (!DmShared.xNull(bankLetterDocid).equalsIgnoreCase("")) {
                        new DmAlertMessageBox("This record can't be Disapprove because Bank Letter has been created.", (DmTextBox) null).show();
                        return;
                    } else {
                        setApproved(DmShared.iNull(topControl.getDocId()).toString(), false);
                    }

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
            List<GridDrdAdjust_Mdl> lst = grdDrdAdjust.getGrid().getStore().getAll();
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
                        new DmAlertMessageBox("Atleast one Records select for adjustment.", txtOriginalAmount).show();
                    }

                }

            } else {
                for (int rowIndex = 0; rowIndex < lst.size(); rowIndex++) {
                    grdDrdCheckedValue(rowIndex, false);
                }
            }
            grdDrdAdjust.commitChanges();
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
        SrvAsync.invokeRPC(true).getPendingFinalDebitAdjmentRec(supplierCode, new AsyncCallback<DmResult<List<GridDrdAdjust_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GridDrdAdjust_Mdl>> result) {
                if (result.isStatus()) {
                    hmMain.clear();
                    txtDrAdjustAmt.setText("");
                    grdDrdAdjust.getGrid().getStore().clear();
                    grdDrdAdjust.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillDebitAdjusmentRec(FinalPaymentEntry_Mdl data) {
        grdDrdAdjust.getGrid().getStore().clear();
        grdDrdAdjust.getGrid().getStore().addAll(data.getLstdrAdjustAmt());
    }

    private void resetDrAdjust() {
        drAdjustAmt = 0d;
        hmMain.clear();
    }

    private void autoAdjust(Integer currentRowIndex) {

        drAdjustAmt = 0.00;

        for (int i = 0; i < grdDrdAdjust.getGrid().getStore().getAll().size(); i++) {
            GridDrdAdjust_Mdl drdMdl = grdDrdAdjust.getGrid().getStore().getAll().get(i);
            if (DmShared.xNull(drdMdl.getChecked()).trim().equals("1")) {
                drAdjustAmt = DmShared.vNull(drdMdl.getPendingAdjustment());
                vr_Docid = drdMdl.getSharedKeyFieldCode();
                vr_V_SNo = drdMdl.getVr_V_SNo();
                lgAdj = new LedgerAdj_Mdl();

                hmMain.put(vr_Docid, calculateAdj(drAdjustAmt));

            }
        }
        grdDrdAdjust.commitChanges();
        calculate();
    }

    private void grdDrdCheckedValue(Integer currentRowIndex, boolean callFromCheckAll) {

        if (!topControl.getMode().trim().equalsIgnoreCase("Browse")) {

            if (DmShared.iNull(grdDrdAdjust.getGrid().getStore().get(currentRowIndex).getChecked()).intValue() == 1 && !callFromCheckAll) {

                btnBlnCheckAll = false;
                drChkCheckAll.setStyleName("fa fa-square-o");

                grdDrdAdjust.getGrid().getStore().get(currentRowIndex).setChecked("0");

                calculate();
                for (GrdPendingFPReqDetail_Mdl node : grdPeningFPReqDetail.getGrid().getStore().getAll()) {
                    if (DmShared.xNull(node.getChecked()).trim().equals("1")) {
                        node.setPendingToAdjustAmt(DmShared.xNull(node.getFinalPayReq()));
                        node.setDrAdjustAmt("0");
                    }

                }
                hmMain.clear();
                autoAdjust(currentRowIndex);
                grdPeningFPReqDetail.commitChanges();

            } else {

                for (GrdPendingFPReqDetail_Mdl node : grdPeningFPReqDetail.getGrid().getStore().getAll()) {
                    if (DmShared.xNull(node.getChecked()).trim().equals("1")) {
                        node.setPendingToAdjustAmt(DmShared.xNull(node.getFinalPayReq()));
                        node.setDrAdjustAmt("0");
                    }

                }
                hmMain.clear();
                grdDrdAdjust.getGrid().getStore().get(currentRowIndex).setChecked("1");
                autoAdjust(currentRowIndex);
            }
            grdDrdAdjust.commitChanges();

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
                        adj_DocId = DmShared.xNull(gaMdl.getPbillDocId());
                        lgAdj.setAdj_V_SNo(DmShared.xNull(gaMdl.getLgV_SNo()));
                        lgAdj.setAdjustment(DmClient.vNull(gaMdl.getPendingToAdjustAmt(), "0.00"));
                        gaMdl.setPendingToAdjustAmt("0");
                        gaMdl.setDrAdjustAmt(DmClient.vNull(amount + DmShared.vNull(gaMdl.getDrAdjustAmt()), "0.00"));
                        lgAdj.setVr_V_SNo(DmShared.xNull(vr_V_SNo));
                        hm.put(adj_DocId, lgAdj);
                        workingAmount -= amount;
                    } else if (workingAmount != 0) {
                        adj_DocId = DmShared.xNull(gaMdl.getPbillDocId());
                        lgAdj.setAdj_V_SNo(DmShared.xNull(gaMdl.getLgV_SNo()));
                        lgAdj.setAdjustment(DmClient.vNull(workingAmount, "0.00"));
                        gaMdl.setDrAdjustAmt(DmClient.vNull(workingAmount + DmShared.vNull(gaMdl.getDrAdjustAmt()), "0.00"));
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

}
