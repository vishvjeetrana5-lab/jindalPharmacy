package dataman.accounts.voucherEntryPaymentJV.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.accounts.voucherEntryPaymentJV.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.accountAdjustment.client.AccountAdjustment;
import dataman.dmToolkit.accountAdjustment.shared.AccountAdjustment_Mdl;
import dataman.dmToolkit.accountAdjustment.shared.GrdAdjustment_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.itemAdjustment.client.ItemAdjustment;
import dataman.dmToolkit.itemAdjustment.shared.GrdItemAdjustment_Mdl;
import dataman.dmToolkit.itemAdjustment.shared.ItemAdjustment_Mdl;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.ArrayList;
import java.util.List;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;

public class VoucherEntryPaymentJVGridEdit<T1, T2> extends VoucherEntryPaymentJVGridEdit_Dsg<T1, T2> implements DmRootEvents, AttachEvent.Handler, KeyUpHandler, BlurHandler {

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtDebit || event.getSource() == txtCredit) {
            if (event.getSource() == txtDebit) {
                txtDebit.setTag(txtDebit.getText());
            }
            txtDebit.setText(txtDebit.getTag());
            if (event.getSource() == txtCredit) {
                txtCredit.setTag(txtCredit.getText());
            }
            txtCredit.setText(txtCredit.getTag());
            lblTotalTds.setText("0.00");
            showTdsCategory(tdsMdl);
            calculateTotalTds();
            updateDebitCredit();
        }
        if (event.getSource() == txtTdsAmountProposed || event.getSource() == txtTdsAmountProposed) {
            updateDebitCredit();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="CallBack">
    interface CallBack {

        public void onOk(GrdVoucherEntryPaymentJV_Mdl grdVoucherEntry);

        public void onDelete();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    final ServiceInvoker<VoucherEntryPaymentJV_SrvAsync> srvAsync;
    final LibraryVoucherCategory lvc = new LibraryVoucherCategory();
//    final ChequeDetail chequeDetail = new ChequeDetail();
//    final TdsCategory tdsCategory = new TdsCategory();
    final CostCenter costCenter = new CostCenter();
    final AccountAdjustment adjustmentPopup = new AccountAdjustment();
    final ItemAdjustment itemAdjustmentPopup = new ItemAdjustment();

    protected VoucherEntryPaymentJV voucherEntry;
    private ChequeDetail_Mdl chequeDetailMdl;
    private List<CostCenterGrid_Mdl> costCenterGridData;
    private List<GrdAdjustment_Mdl> lstAdjustments = new ArrayList<>();
    private List<GrdItemAdjustment_Mdl> lstItemAdjustments = new ArrayList<>();
    private TDS_Mdl tdsMdl;

    private int hashCode = 0;
    protected String acAcountNo = "", acBank = "", acBranchAddress = "", acIfscCode = "", acSwiftCode = "", acBranch = "";
    boolean isDebit;

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
//            chequeDetail.hide();
//            tdsCategory.hide();
            costCenter.hide();
            adjustmentPopup.hide();
            itemAdjustmentPopup.hide();

        }
    }

    private void textHelpEvents() {
        costCenter.addCallBack(new CostCenter.CallBack() {
            @Override
            public void onOk(List<CostCenterGrid_Mdl> costCenterGridMdl) {
                costCenterGridData = new ArrayList<CostCenterGrid_Mdl>();
                costCenterGridData.addAll(costCenterGridMdl);

                txtCostCenterAmt.setFocus(true);
                calculateCostCenterAmt();
            }

            @Override
            public void onClose() {
                txtCostCenterAmt.setFocus(true);
            }
        });

//        tdsCategory.addTdsDetailCallBack(new TdsCategory.TDSDetailCallBack() {
//            @Override
//            public void onOk(TDS_Mdl tDS_Mdl) {
//                tdsMdl = new TDS_Mdl();
//                tdsMdl = tDS_Mdl;
//
//                txtTDS.setTag(DmShared.xNull(tDS_Mdl.getTdsCategoryCode()));
//                txtTDS.setText(DmShared.xNull(tDS_Mdl.getTdsCategoryName()));
//                txtDebit.setText(DmClient.vNull(tDS_Mdl.getDebit(), "0.00"));
//                txtCredit.setText(DmClient.vNull(tDS_Mdl.getCredit(), "0.00"));
//                double totalAmt = 0;
//                if (tdsMdl != null) {
//                    if (tdsMdl.getTdsGridList() != null) {
//                        for (int i = 0; i < tdsMdl.getTdsGridList().size(); i++) {
//                            totalAmt += DmShared.vNull(tdsMdl.getTdsGridList().get(i).getAmount());
//                        }
//                    }
//                }
//                txtTDSAmt.setText(DmClient.vNull(totalAmt, "0.00"));
//                if (DmShared.iNull(txtTDS.getTag()).longValue() != 0) {
//                    txtDebit.setEnabled(false);
//                    txtCredit.setEnabled(false);
//                }
//                txtTDSAmt.setFocus(true);
//            }
//
//            @Override
//            public void onClose() {
//                txtTDSAmt.setFocus(true);
//            }
//        });
//
//        chequeDetail.addChequeDetailCallBack(new ChequeDetail.ChequeDetailCallBack() {
//            @Override
//            public void onOk(ChequeDetail_Mdl chequeDetail_Mdl) {
//                chequeDetailMdl = new ChequeDetail_Mdl();
//                chequeDetailMdl = chequeDetail_Mdl;
//                txtChequeNo.setText(chequeDetail_Mdl.getChequeNo());
//                txtChequeNo.setFocus(true);
//            }
//
//            @Override
//            public void onClose() {
//                txtChequeNo.setFocus(true);
//            }
//        });
        adjustmentPopup.addCallBack(new AccountAdjustment.CallBack() {
            @Override
            public void onOk(List<GrdAdjustment_Mdl> lstAdjustments) {
                VoucherEntryPaymentJVGridEdit.this.lstAdjustments = lstAdjustments;
                double totalAdjustment = 0;
                if (lstAdjustments != null) {
                    for (GrdAdjustment_Mdl gaMdl : lstAdjustments) {
                        totalAdjustment += DmShared.vNull(gaMdl.getAdjustment());
                    }
                }
                txtAdjustment.setText(DmClient.vNull(totalAdjustment, "0.00"));
                txtAdjustment.setFocus(true);
            }

            @Override
            public void onClose() {
                txtAdjustment.setFocus(true);
            }
        });

        itemAdjustmentPopup.addCallBack(new ItemAdjustment.CallBack() {
            @Override
            public void onOk(List<GrdItemAdjustment_Mdl> lstItemAdjustments) {
                VoucherEntryPaymentJVGridEdit.this.lstItemAdjustments = lstItemAdjustments;
                double totalAdjustment = 0;
                if (lstItemAdjustments != null) {
                    for (GrdItemAdjustment_Mdl gaMdl : lstItemAdjustments) {
                        totalAdjustment += DmShared.vNull(gaMdl.getAdjustment());
                    }
                }
                txtItemAdjustment.setText(DmClient.vNull(totalAdjustment, "0.00"));
                txtItemAdjustment.setFocus(true);
            }

            @Override
            public void onClose() {
                txtItemAdjustment.setFocus(true);
            }
        });

        txtHAccountName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAccountNameGrid_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountNameGrid(helper, blnToEndOfFile, voucherEntry.currentWidgetName, new AsyncCallback<DmResultPageing<List<TxtHAccountNameGrid_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHAccountNameGrid_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHAccountName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, final TxtHAccountNameGrid_Mdl tRow) {
                resetAccount();
                if (blnStatus && tRow != null) {
                    txtTDS.setText("");
                    txtTDS.setTag("");
                    txtTDS.setTag2nd("");
                    getLedgerBalance(String.valueOf(txtHAccountName.getTag()));
                    txtHAccountName.setTag2nd(DmShared.xNull(tRow.getNatureManualCode()));
                    txtACManualCode.setText(tRow.getSCode());

                    if (tRow.getIsDefault()) {
                        acAcountNo = DmShared.xNull(tRow.getAccountNo());
                        acBank = DmShared.xNull(tRow.getBank());
                        acBranch = DmShared.xNull(tRow.getBranch());
                        acBranchAddress = DmShared.xNull(tRow.getBankAddress());
                        acSwiftCode = DmShared.xNull(tRow.getSwiftCode());
                        acIfscCode = DmShared.xNull(tRow.getIfscCode());
                    }
                    showChequeDetail();
                    if (!DmShared.xNull(tRow.getTdsCatCode()).equalsIgnoreCase("")) {
//                        new DmConfirmMessageBox("TDS Is Applicable For This Account. Do You Want To Apply It?", new DialogHideEvent.DialogHideHandler() {
//
//                            @Override
//                            public void onDialogHide(DialogHideEvent event) {
//                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
//                                    txtTDS.setTag(DmShared.xNull(tRow.getTdsCatCode()));
//                                    txtTDS.setText(DmShared.xNull(tRow.getTdsCatName()));
//                                    showTdsCategory(tdsMdl);
//                                } else {
//                                    txtNarration.setFocus(true);
//                                }
//                            }
//                        }).show();
                        txtTDS.setTag(DmShared.xNull(tRow.getTdsCatCode()));
                        txtTDS.setText(DmShared.xNull(tRow.getTdsCatName()));
                        showTdsCategory(tdsMdl);
                    }
                }
            }
        });

        txtDebit.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                textOnValueChange(event);
            }
        });

        txtCredit.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                textOnValueChange(event);
            }
        });

        txtHTdsCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTdsCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTdsCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus) {
                    if (tRow != null) {
                        bindTdsGrid(tRow.getSharedKeyFieldCode());
                    }
                }
            }

        });

        txtHTdsDeduct.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHTdsDeduct_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTdsDeduct(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTdsDeduct_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHTdsDeduct_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHTdsDeduct.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHTdsDeduct_Mdl tRow) {
                updateDebitCredit();
            }

        });

        txtHTdsFor.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTdsFor(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTdsFor.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    txtHTdsFor.setText(tRow.getSharedKeyFieldName());
                    txtHTdsFor.setTag(tRow.getSharedKeyFieldCode());
                }
                txtHTdsFor.setFocus(true);
                updateDebitCredit();
            }

        });

        txtTdsOnAmount.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                calculateTotalTds();
                updateDebitCredit();
            }
        });
        txtHIsCrossCheque.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsCrossCheque).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsCrossCheque.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsCrossCheque).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                updateDebitCredit();
            }
        });
        txtHAccountNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAccountNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountNo(txtHAccountName.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHAccountNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccountNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHAccountNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHAccountNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccountNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, final TxtHAccountNo_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    txtBank.setText(DmShared.xNull(tRow.getBank()));
                    txtBranch.setText(DmShared.xNull(tRow.getBranch()));
                    txtIFSCCode.setText(DmShared.xNull(tRow.getIfscCode()));
                    txtBranchAddress.setText(DmShared.xNull(tRow.getBankAddress()));

                } else {
                    txtBank.setText("");
                    txtBranch.setText("");
                    txtIFSCCode.setText("");
                    txtBranchAddress.setText("");

                }
            }
        });

    }

    @Override
    public void onClick(ClickEvent event) {
//        if (event.getSource() == lblChequeNo) {
//            showChequeDetail();
//        } else if (event.getSource() == lblTDSAmt) {
//            showTdsCategory(tdsMdl);
        if (event.getSource() == lblCostCenterAmt) {
            showCostCenterDetail();
        } else if (event.getSource() == lblAdjustment) {
            showAdjustmentPopup();
        } else if (event.getSource() == lblItemAdjustment) {
            showItemAdjustmentPopup();
        } else {
            super.onClick(event);
        }
    }

    public void show(int currentRowIndex, VoucherEntryPaymentJV voucherEntry, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, boolean reqChequeDet) {
        clear();
        this.voucherEntry = voucherEntry;
        lblLedgerBalance.setText("");

        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        if (DmShared.xNull(voucherEntry.txtHVoucherType.getTag2nd()).equalsIgnoreCase(voucherEntry.lvc.JOURNAL)
                || DmShared.xNull(voucherEntry.txtHVoucherType.getTag2nd()).equalsIgnoreCase(voucherEntry.lvc.CONTRA)) {
            if ((DmShared.vNull(txtDebit.getText()).doubleValue() + DmShared.vNull(txtCredit.getText()).doubleValue()) == 0) {
                if (DmShared.vNull(voucherEntry.lblDiff_DebCre.getTitle()).doubleValue() > 0) {
                    txtCredit.setText(DmClient.vNull(voucherEntry.lblDiff_DebCre.getTitle(), "0.00").replaceAll("-", ""));

                } else if (DmShared.vNull(voucherEntry.lblDiff_DebCre.getTitle()).doubleValue() < 0) {
                    txtDebit.setText(DmClient.vNull(voucherEntry.lblDiff_DebCre.getTitle(), "0.00").replaceAll("-", ""));

                }
            }
        }
        flwChequeDetParent.clear();

        if (reqChequeDet) {
            flwChequeDetParent.add(flwChequeDetChild);
        }
    }

    @Override
    public void displayRow(T1 row) {
        double totalAmt = 0;
        GrdVoucherEntryPaymentJV_Mdl ourRow = (GrdVoucherEntryPaymentJV_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        flwDebitText.clear();
        flwCreditText.clear();
        flwDebitLabel.clear();
        flwCreditLabel.clear();
        txtTDS.setTag("");
        txtTDS.setText("");

        if (!getDmGrid().getGrid().getColumnModel().isHidden(VoucherEntryPaymentJV_Dsg.GridIndex.debit)) {
            flwDebitText.add(txtDebit);
            flwDebitLabel.add(lblDebit);
        }
        if (!getDmGrid().getGrid().getColumnModel().isHidden(VoucherEntryPaymentJV_Dsg.GridIndex.credit)) {
            flwCreditText.add(txtCredit);
            flwCreditLabel.add(lblCredit);
        }

        tdsMdl = new TDS_Mdl();
        tdsMdl = ourRow.getTds_Mdl();
        showTDSDetail(tdsMdl, voucherEntry.txtHVoucherType.getTag2nd(), isDebit, DmShared.xNull(voucherEntry.topControl.getMode()));
//        showTdsCategory(tdsMdl);

        costCenterGridData = new ArrayList<CostCenterGrid_Mdl>();
        costCenterGridData.addAll(ourRow.getCostCenterGrdData());

        lstAdjustments = new ArrayList<GrdAdjustment_Mdl>();
        lstAdjustments.addAll(ourRow.getLstAdjustments());

        lstItemAdjustments = new ArrayList<GrdItemAdjustment_Mdl>();
        lstItemAdjustments.addAll(ourRow.getLstItemAdjustments());

        chequeDetailMdl = new ChequeDetail_Mdl();
        chequeDetailMdl = ourRow.getChequeDetail_Mdl();
        chequeDetailMdl.setChequeDetailBind(true);
        showChequeDetail();

        //////////////////////////////////////////////////////////////////////////////////////
        totalAmt = 0;
        for (int i = 0; i < costCenterGridData.size(); i++) {
            totalAmt += DmShared.vNull(costCenterGridData.get(i).getAmount());
        }
        txtCostCenterAmt.setText(DmClient.vNull(totalAmt, "0.00"));
        totalAmt = 0;
        for (int i = 0; i < lstAdjustments.size(); i++) {
            totalAmt += DmShared.vNull(lstAdjustments.get(i).getAdjustment());
        }
        txtAdjustment.setText(DmClient.vNull(totalAmt, "0.00"));
        totalAmt = 0;
        for (int i = 0; i < lstItemAdjustments.size(); i++) {
            totalAmt += DmShared.vNull(lstItemAdjustments.get(i).getAdjustment());
        }
        txtItemAdjustment.setText(DmClient.vNull(totalAmt, "0.00"));
        totalAmt = 0;
        if (tdsMdl != null) {
            if (tdsMdl.getTdsGridList() != null) {
                for (int i = 0; i < tdsMdl.getTdsGridList().size(); i++) {
                    totalAmt += DmShared.vNull(tdsMdl.getTdsGridList().get(i).getAmount());
                }
            }
        }
        txtTDSAmt.setText(DmClient.vNull(totalAmt, "0.00"));
        //-----------------------------------------------------------------------------------
        if (chequeDetailMdl != null) {
            txtChequeNo.setText(chequeDetailMdl.getChequeNo());
        }
        ////////////////////////////////////////////////////////////////////////////////////                
        txtACManualCode.setText(DmShared.xNull(ourRow.getAccMCode()));
        txtHAccountName.setText(DmShared.xNull(ourRow.getAccName()));
        txtHAccountName.setTag(DmShared.xNull(ourRow.getAccCode()));
        getLedgerBalance(DmShared.xNull(ourRow.getAccCode()));
        txtHAccountName.setTag2nd(DmShared.xNull(ourRow.getAccNature()));
        txtNarration.setText(DmShared.xNull(ourRow.getNarration()));
        txtDebit.setText(DmShared.xNull(ourRow.getDebit()));
        txtCredit.setText(DmShared.xNull(ourRow.getCredit()));

        txtTDS.setTag(DmShared.xNull(tdsMdl.getTdsCategoryCode()));
        txtTDS.setText(DmShared.xNull(tdsMdl.getTdsCategoryName()));
        if (DmShared.iNull(txtTDS.getTag()).longValue() != 0) {
            txtDebit.setEnabled(false);
            txtCredit.setEnabled(false);
        } else {
            txtDebit.setEnabled(true);
            txtCredit.setEnabled(true);
        }

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHAccountName.setFocus(true);
            }
        });

    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdVoucherEntryPaymentJV_Mdl();
    }

    @Override
    public void onOk() {
        if (!DmClient.isValidText(txtHAccountName.getText(), "A/c Name", txtHAccountName)) {
            return;
        }
        if (DmShared.vNull(txtDebit.getText()).doubleValue() <= 0 && DmShared.vNull(txtCredit.getText()).doubleValue() <= 0) {
            new DmAlertMessageBox("Amount is mandatory.", txtDebit).show();
            if (txtDebit.isAttached()) {
                txtDebit.setFocus(true);
            } else if (txtCredit.isAttached()) {
                txtCredit.setFocus(true);
            }
            return;
        }

//===================================<Cheque Validation>==================================//
        if (!txtChequeDate.getText().trim().equalsIgnoreCase("") && !txtClearDate.getText().trim().equalsIgnoreCase("")) {
            if (!(DmShared.convertDate(txtChequeDate.getText()).before(DmShared.convertDate(txtClearDate.getText()))
                    || DmShared.convertDate(txtChequeDate.getText()).equals(DmShared.convertDate(txtClearDate.getText())))) {
                new DmAlertMessageBox("Clear date should be greater than or equal to cheque date.", (DmTextBox) null).show();
                return;
            }
        }

        if (!DmShared.xNull(txtChequeNo.getText()).equalsIgnoreCase("") && DmShared.xNull(txtHIsCrossCheque.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Cross Cheque is mandatory", txtHIsCrossCheque).show();
            return;
        }
//====================================<TDS Validation>====================================//
        if (DmShared.vNull(lblTotalTds.getText()).doubleValue() > 0) {
            if (!DmClient.isValidText(txtHTdsCategory.getTag(), "TDS Category", txtHTdsCategory)) {
                return;
            }
            if (!DmClient.isValidText(txtHTdsDeduct.getTag(), "TDS Deduct From", txtHTdsDeduct)) {
                return;
            }
            if (!DmClient.isValidAmount(txtTdsOnAmount.getText(), "TDS On Amount", txtTdsOnAmount)) {
                return;
            }

            if ((DmShared.vNull(txtTdsAmountProposed.getText()).doubleValue() - DmShared.vNull(lblTotalTds.getText()).doubleValue()) <= 0) {
                new DmAlertMessageBox("Total TDS should not be greater than amount.", (DmTextBox) null).show();
                return;
            }
            if (!DmClient.isValidText(txtHTdsFor.getTag(), "TDS For", txtHTdsFor)) {
                return;
            }
        }
        //====================================<TDS Validation>====================================//

        if (costCenterGridData != null) {
            if (costCenterGridData.size() > 0) {
                double ccAmount = 0;
                for (int i = 0; i < costCenterGridData.size(); i++) {
                    if (!DmShared.xNull(costCenterGridData.get(i).getCode()).equalsIgnoreCase("")) {
                        ccAmount += DmShared.vNull(costCenterGridData.get(i).getAmount());
                    }
                }
                if (DmShared.vNull(txtCredit.getText()).doubleValue() > 0) {
                    if (ccAmount != DmShared.vNull(txtCredit.getText()).doubleValue()) {
                        new DmAlertMessageBox("Cost Center Amount and Credit Amount is not equal, check and try again.", txtCredit).show();
                        return;
                    }
                } else if (DmShared.vNull(txtDebit.getText()).doubleValue() > 0) {
                    if (ccAmount != DmShared.vNull(txtDebit.getText()).doubleValue()) {
                        new DmAlertMessageBox("Cost Center Amount and Debit Amount is not equal, check and try again.", txtDebit).show();
                        return;
                    }
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdVoucherEntryPaymentJV_Mdl ourRow = (GrdVoucherEntryPaymentJV_Mdl) row;

        ourRow.setAccMCode(txtACManualCode.getText());
        ourRow.setAccName(txtHAccountName.getText());
        ourRow.setAccCode(txtHAccountName.getTag());
        ourRow.setAccNature(txtHAccountName.getTag2nd());

        ourRow.setNarration(txtNarration.getText());
        ourRow.setDebit(null);
        if (DmShared.vNull(txtDebit.getText()).doubleValue() > 0) {
            ourRow.setDebit(DmClient.vNull(txtDebit.getText(), "0.00"));
            ourRow.setTotalAmount(DmClient.vNull(txtDebit.getText(), "0.00"));
        }
        ourRow.setCredit(null);
        if (DmShared.vNull(txtCredit.getText()).doubleValue() > 0) {
            ourRow.setCredit(DmClient.vNull(txtCredit.getText(), "0.00"));
            ourRow.setTotalAmount(DmClient.vNull(txtCredit.getText(), "0.00"));
        }

        ourRow.setFavourOf(txtFavourOF.getText());
        ourRow.setChqNo(txtChequeNo.getText());

        ourRow.setTds(DmClient.vNull(lblTotalTds.getText(), "0.00"));
        ourRow.setTotalAmount(DmClient.vNull(txtTdsAmountProposed.getText(), "0.00"));

        if (costCenterGridData == null) {
            ourRow.getCostCenterGrdData().addAll(new ArrayList<CostCenterGrid_Mdl>());
        } else {
            ourRow.setCostCenterGrdData(new ArrayList());
            ourRow.getCostCenterGrdData().addAll(costCenterGridData);
        }
        if (lstAdjustments == null) {
            ourRow.getLstAdjustments().addAll(new ArrayList<GrdAdjustment_Mdl>());
        } else {
            ourRow.setLstAdjustments(new ArrayList<GrdAdjustment_Mdl>());
            ourRow.getLstAdjustments().addAll(lstAdjustments);
        }
        if (lstItemAdjustments == null) {
            ourRow.getLstItemAdjustments().addAll(new ArrayList<GrdItemAdjustment_Mdl>());
        } else {
            ourRow.setLstItemAdjustments(new ArrayList<GrdItemAdjustment_Mdl>());
            ourRow.getLstItemAdjustments().addAll(lstItemAdjustments);
        }
        chequeDetailMdl = fillChequeDetail();
        ourRow.setChequeDetail_Mdl(chequeDetailMdl);
        if (DmShared.vNull(lblTotalTds.getText()).doubleValue() > 0) {
            tdsMdl = fillTDSDetail();
            ourRow.setTds_Mdl(tdsMdl);
        }

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        proceedDelete();
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    public VoucherEntryPaymentJVGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Voucher Entry Payment(JV)", "700px", "520px");
        srvAsync = new ServiceInvoker<VoucherEntryPaymentJV_SrvAsync>(GWT.create(VoucherEntryPaymentJV_Srv.class), VoucherEntryPaymentJV.class.getName());
        txtKeyEvents();
        textHelpEvents();
        manageEvents();
    }

    private void manageEvents() {
//        lblChequeNo.addClickHandler(this);
        lblCostCenterAmt.addClickHandler(this);
//        lblTDSAmt.addClickHandler(this);
        lblAdjustment.addClickHandler(this);
        lblItemAdjustment.addClickHandler(this);

//        txtChequeNo.addKeyUpHandler(this);
        txtCostCenterAmt.addKeyUpHandler(this);
//        txtTDSAmt.addKeyUpHandler(this);
        txtAdjustment.addKeyUpHandler(this);
        txtItemAdjustment.addKeyUpHandler(this);
        txtTdsAmountProposed.addBlurHandler(this);
        txtTdsOnAmount.addBlurHandler(this);
        txtDebit.addBlurHandler(this);
        txtCredit.addBlurHandler(this);
    }

    private void showChequeDetail() {
        if (voucherEntry.topControl.getMode().equalsIgnoreCase("New")) {
            if (!DmShared.xNull(txtHAccountName.getTag()).trim().equalsIgnoreCase("")) {
                chequeDetailMdl.setAccountCode(DmShared.xNull((txtHAccountName.getTag())));
            }

            if (!chequeDetailMdl.isChequeDetailBind()) {
                txtFavourOF.setText(DmShared.xNull(txtHAccountName.getText()));
                txtHAccountNo.setTag(DmShared.iNull(acAcountNo).toString());
                txtHAccountNo.setText(DmShared.xNull(acAcountNo));
                txtBranchAddress.setText(DmShared.xNull(acBranchAddress));
                txtBank.setText(DmShared.xNull(acBank));
                txtBranch.setText(DmShared.xNull(acBranch));
                txtIFSCCode.setText(DmShared.xNull(acIfscCode));
                txtHIsCrossCheque.setText("Yes");
                txtHIsCrossCheque.setTag("1");
            } else {
                txtChequeNo.setText(DmShared.xNull(chequeDetailMdl.getChequeNo()));
                txtChequeDate.setText(DmShared.xNull(chequeDetailMdl.getChequeDate()));
                txtHIsCrossCheque.setTag(DmShared.xNull(chequeDetailMdl.getIsCrossChequeCode()));
                txtHIsCrossCheque.setText(DmShared.xNull(chequeDetailMdl.getIsCrossChequeText()));
                txtFavourOF.setText(DmShared.xNull(chequeDetailMdl.getFavourOf()));
                txtClearDate.setText(DmShared.xNull(chequeDetailMdl.getClearDate()));
                txtHAccountNo.setTag(DmShared.iNull(chequeDetailMdl.getAccountCode()).toString());
                txtHAccountNo.setText(DmShared.xNull(chequeDetailMdl.getAccountNo()));
                txtBranchAddress.setText(DmShared.xNull(chequeDetailMdl.getBranchAddress()));
                txtBank.setText(DmShared.xNull(chequeDetailMdl.getBank()));
                txtBranch.setText(DmShared.xNull(chequeDetailMdl.getBranch()));
                txtIFSCCode.setText(DmShared.xNull(chequeDetailMdl.getIfscCode()));
            }

        } else {
            txtChequeNo.setText(DmShared.xNull(chequeDetailMdl.getChequeNo()));
            txtChequeDate.setText(DmShared.xNull(chequeDetailMdl.getChequeDate()));
            txtHIsCrossCheque.setTag(DmShared.xNull(chequeDetailMdl.getIsCrossChequeCode()));
            txtHIsCrossCheque.setText(DmShared.xNull(chequeDetailMdl.getIsCrossChequeText()));
            txtFavourOF.setText(DmShared.xNull(chequeDetailMdl.getFavourOf()));
            txtClearDate.setText(DmShared.xNull(chequeDetailMdl.getClearDate()));
            txtHAccountNo.setTag(DmShared.iNull(chequeDetailMdl.getAccountCode()).toString());
            txtHAccountNo.setText(DmShared.xNull(chequeDetailMdl.getAccountNo()));
            txtBranchAddress.setText(DmShared.xNull(chequeDetailMdl.getBranchAddress()));
            txtBank.setText(DmShared.xNull(chequeDetailMdl.getBank()));
            txtBranch.setText(DmShared.xNull(chequeDetailMdl.getBranch()));
            txtIFSCCode.setText(DmShared.xNull(chequeDetailMdl.getIfscCode()));
        }
//        if (!isOkEnable()) {
//            chequeDetail.show(chequeDetailMdl, false);
//        } else {
//            chequeDetail.show(chequeDetailMdl, true);
//        }
    }

    private void showCostCenterDetail() {
        if (!DmShared.xNull(voucherEntry.txtHCostCenter.getText()).trim().equalsIgnoreCase("")) {
            costCenterGridData = null;
            new DmAlertMessageBox("To bifurcate cost center line wise, you have to remove cost center from header.", txtCostCenterAmt).show();
        } else {
            if (!isOkEnable()) {
                costCenter.show(costCenterGridData, DmClient.vNull(DmShared.vNull(DmClient.vNull(txtCredit.getText(), "0.00")) + DmShared.vNull(DmClient.vNull(txtDebit.getText(), "0.00")), "0.00"), false);
            } else {

                costCenter.show(costCenterGridData, DmClient.vNull(DmShared.vNull(DmClient.vNull(txtCredit.getText(), "0.00")) + DmShared.vNull(DmClient.vNull(txtDebit.getText(), "0.00")), "0.00"), true);
            }
        }
    }

    private void showTdsCategory(TDS_Mdl tdsm) {
        boolean isDebit = false;
        double tempAmout = 0;
        String tempTdsFor = "";

        tdsm.setTdsCategoryCode(DmShared.xNull(txtTDS.getTag()));
        tdsm.setTdsCategoryName(DmShared.xNull(txtTDS.getText()));
        tdsm.setTdsDeductFormCode(DmShared.xNull(txtHAccountName.getTag()));
        tdsm.setTdsDeductFormName(DmShared.xNull(txtHAccountName.getText()));
        if (DmShared.vNull(txtDebit.getText()) > 0) {
            isDebit = true;
        } else if (DmShared.vNull(txtCredit.getText()) > 0) {
            isDebit = false;
        } else {
            if (!getDmGrid().getGrid().getColumnModel().isHidden(VoucherEntryPaymentJV_Dsg.GridIndex.debit)) {
                isDebit = true;
            } else if (!getDmGrid().getGrid().getColumnModel().isHidden(VoucherEntryPaymentJV_Dsg.GridIndex.credit)) {
                isDebit = false;
            }
        }

        if (DmShared.vNull(tdsm.getTdsOnAmount()) > 0) {
            tempAmout = DmShared.vNull(tdsm.getTdsOnAmount());
        } else if (DmShared.vNull(txtDebit.getText()) > 0) {
            tempAmout = DmShared.vNull(txtDebit.getText());
        } else if (DmShared.vNull(txtCredit.getText()) > 0) {
            tempAmout = DmShared.vNull(txtCredit.getText());
        } else {
            tempAmout = 0;
        }

        if (!DmShared.xNull(tdsm.getTdsFor()).trim().equalsIgnoreCase("")) {
            tempTdsFor = tdsm.getTdsFor();
        } else {
            if (DmShared.xNull(voucherEntry.txtHVoucherType.getTag2nd()).equalsIgnoreCase(voucherEntry.lvc.JOURNAL)
                    || DmShared.xNull(voucherEntry.txtHVoucherType.getTag2nd()).equalsIgnoreCase(voucherEntry.lvc.BANK_PAYMENT)) {

                tempTdsFor = "P";

            } else if (DmShared.xNull(voucherEntry.txtHVoucherType.getTag2nd()).equalsIgnoreCase(voucherEntry.lvc.BANK_RECEIPT)) {
                tempTdsFor = "R";
            }

        }

        tdsm.setTdsOnAmount(DmClient.vNull(tempAmout, "0.00"));
        tdsm.setTdsFor(tempTdsFor);

        showTDSDetail(tdsm, voucherEntry.txtHVoucherType.getTag2nd(), isDebit, DmShared.xNull(voucherEntry.topControl.getMode()));
    }

    private void showAdjustmentPopup() {
        if (!DmShared.xNull(txtHAccountName.getTag()).trim().equals("")) {
            AccountAdjustment_Mdl vapMdl = new AccountAdjustment_Mdl();
            vapMdl.setIsEditable(isOkEnable());
            vapMdl.setAcCode(txtHAccountName.getTag());
            vapMdl.setAcName(txtHAccountName.getText());
            vapMdl.setAmtDr(txtDebit.getText());
            vapMdl.setAmtCr(txtCredit.getText());
            vapMdl.setLstAdjustment(lstAdjustments);
            adjustmentPopup.show(vapMdl);
        }
    }

    private void showItemAdjustmentPopup() {
        if (!DmShared.xNull(txtHAccountName.getTag()).trim().equals("")) {
            ItemAdjustment_Mdl iapMdl = new ItemAdjustment_Mdl();
            iapMdl.setIsEditable(isOkEnable());
            iapMdl.setAcCode(txtHAccountName.getTag());
            iapMdl.setAcName(txtHAccountName.getText());
            iapMdl.setAmtDr(txtDebit.getText());
            iapMdl.setAmtCr(txtCredit.getText());
            iapMdl.setLstItemAdjustment(lstItemAdjustments);
            itemAdjustmentPopup.show(iapMdl);
        }
    }

    private void textOnValueChange(ValueChangeEvent<String> event) {
        if (event.getSource() == txtDebit) {
            if (DmShared.vNull(event.getValue()) > 0) {
                resetDebitCredit();
                txtCredit.setText("");
            }
        } else if (event.getSource() == txtCredit) {
            if (DmShared.vNull(event.getValue()) > 0) {
                resetDebitCredit();
                txtDebit.setText("");
            }
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
//        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_H) {
//            showChequeDetail();
//        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_T) {
//            showTdsCategory(tdsMdl);
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_K) {
            onOk();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_O) {
            showCostCenterDetail();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_J) {
            showAdjustmentPopup();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_I) {
            showItemAdjustmentPopup();
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
//            if (event.getSource().equals(txtChequeNo)) {
//                showChequeDetail();
//            } else if (event.getSource().equals(txtTDSAmt)) {
//                showTdsCategory(tdsMdl);
            if (event.getSource().equals(txtCostCenterAmt)) {
                showCostCenterDetail();
            } else if (event.getSource().equals(txtAdjustment)) {
                showAdjustmentPopup();
            } else if (event.getSource().equals(txtItemAdjustment)) {
                showItemAdjustmentPopup();
            }
        }
    }

    private void calculateCostCenterAmt() {
        double totalAmt = 0;

        if (costCenterGridData != null) {
            for (int i = 0; i < costCenterGridData.size(); i++) {
                totalAmt += DmShared.vNull(costCenterGridData.get(i).getAmount());
            }
        }

        txtCostCenterAmt.setText(DmClient.vNull(totalAmt, "0.00"));
    }

    private void getLedgerBalance(String StrSubCode) {
        srvAsync.invokeRPC(true).getGetLedgerBalance(StrSubCode, DmClient.getStartDate(), DmClient.getLoginDate(), new AsyncCallback<DmResult<LedgerBalance_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<LedgerBalance_Mdl> result) {
                if (result.isStatus()) {
                    if (DmShared.vNull(result.getData().getBalance()) > 0) {
                        lblLedgerBalance.setText("Dr. " + DmClient.vNull(result.getData().getBalance(), "0.00"));
                    } else if (DmShared.vNull(result.getData().getBalance()) < 0) {
                        lblLedgerBalance.setText("Cr. " + DmClient.vNull(0 - DmShared.vNull(result.getData().getBalance()).doubleValue(), "0.00"));
                    } else {
                        lblLedgerBalance.setText("");
                    }

                    if (DmShared.vNull(result.getData().getYtdDr()).doubleValue() != 0) {
                        lblYtdDr.setText("YTD Dr. " + DmClient.vNull(result.getData().getYtdDr(), "0.00"));
                    } else {
                        lblYtdDr.setText("");
                    }

                    if (DmShared.vNull(result.getData().getYtdCr()).doubleValue() != 0) {
                        lblYtdCr.setText("YTD Cr. " + DmClient.vNull(result.getData().getYtdCr(), "0.00"));
                    } else {
                        lblYtdCr.setText("");
                    }

                    if (DmShared.vNull(result.getData().getTdsDr()).doubleValue() != 0) {
                        lblYtdTdsDr.setText("TDS Dr. " + DmClient.vNull(result.getData().getTdsDr(), "0.00"));
                    } else {
                        lblYtdTdsDr.setText("");
                    }

                    if (DmShared.vNull(result.getData().getTdsCr()).doubleValue() != 0) {
                        lblYtdTdsCr.setText("TDS Cr. " + DmClient.vNull(result.getData().getTdsCr(), "0.00"));
                    } else {
                        lblYtdTdsCr.setText("");
                    }

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void clear() {
        txtBank.setEnabled(false);
        txtBranch.setEnabled(false);
        txtIFSCCode.setEnabled(false);
        txtBranchAddress.setEnabled(false);
        txtChequeNo.setText("");
        txtChequeDate.setText("");
        txtFavourOF.setText("");
        txtClearDate.setText("");
        txtHAccountNo.setText("");
        txtHAccountNo.setTag("");
        txtBank.setText("");
        txtBranch.setText("");
        txtIFSCCode.setText("");
        txtBranchAddress.setText("");

        voucherEntry = null;
        hashCode = 0;

        txtHAccountName.setText("");
        txtHAccountName.setTag("");
        txtHAccountName.setTag2nd("");
        resetAccount();

    }

    private void resetAccount() {
        acAcountNo = "";
        acBank = "";
        acBranchAddress = "";
        acIfscCode = "";
        acSwiftCode = "";
        acBranch = "";

        txtACManualCode.setText("");
        txtACManualCode.setTag("");
        txtACManualCode.setTag2nd("");

        lblLedgerBalance.setText("Bal 0.00");
        lblYtdDr.setText("YTD Dr. 0.00");
        lblYtdCr.setText("YTD Cr 0.00");
        lblYtdTdsDr.setText("TDS Dr 0.00");
        lblYtdTdsCr.setText("TDS Cr 0.00");

        txtNarration.setText("");
        txtNarration.setTag("");
        txtNarration.setTag2nd("");

        txtDebit.setText("");
        txtDebit.setTag("");
        txtDebit.setTag2nd("");

        txtCredit.setText("");
        txtCredit.setTag("");
        txtCredit.setTag2nd("");
        resetDebitCredit();

    }

    private void resetDebitCredit() {
        tdsMdl = new TDS_Mdl();
//        txtTDS.setText("");
//        txtTDS.setTag("");
//        txtTDS.setTag2nd("");

        txtTDSAmt.setText("");
        txtTDSAmt.setTag("");
        txtTDSAmt.setTag2nd("");

        chequeDetailMdl = new ChequeDetail_Mdl();
        txtChequeNo.setText("");
        txtChequeNo.setTag("");
        txtChequeNo.setTag2nd("");

        costCenterGridData = new ArrayList<>();
        txtCostCenterAmt.setText("");
        txtCostCenterAmt.setTag("");
        txtCostCenterAmt.setTag2nd("");

        lstAdjustments = new ArrayList<>();
        txtAdjustment.setText("");
        txtAdjustment.setTag("");
        txtAdjustment.setTag2nd("");

        lstItemAdjustments = new ArrayList<>();
        txtItemAdjustment.setText("");
        txtItemAdjustment.setTag("");
        txtItemAdjustment.setTag2nd("");
    }

    private void showTDSDetail(TDS_Mdl tdsm, String voucherCategory, boolean isDebit, String mode) {
        TdsCategoryGrid_Mdl data;
        double totalValue = 0;
        this.isDebit = isDebit;
        txtHTdsFor.setText("");
        txtHTdsFor.setTag("");
        txtHTdsCategory.setText(DmShared.xNull(tdsm.getTdsCategoryName()));
        txtHTdsCategory.setTag(DmShared.xNull(tdsm.getTdsCategoryCode()));
        txtHTdsDeduct.setText(DmShared.xNull(tdsm.getTdsDeductFormName()));
        txtHTdsDeduct.setTag(DmShared.xNull(tdsm.getTdsDeductFormCode()));

        txtHTdsFor.setTag(DmShared.xNull(tdsm.getTdsFor()));
        txtHTdsFor.setText(txtHTdsFor.getTag().trim().equalsIgnoreCase("R") ? "Receivable" : "Payable");
        txtTdsOnAmount.setText(DmClient.vNull(tdsm.getTdsOnAmount(), "0.00"));
        if (DmShared.vNull(tdsm.getOrignalAmount()).doubleValue() > 0) {
            txtTdsAmountProposed.setText(tdsm.getOrignalAmount());
        } else {
            txtTdsAmountProposed.setText(txtTdsOnAmount.getText());
        }
        lblTotalTds.setText("0.00");
        if (DmShared.xNull(mode).equalsIgnoreCase("New") && !tdsm.isTdsDetailBind()) {
            bindTdsGrid(DmShared.xNull(tdsm.getTdsCategoryCode()));
        } else {
            tdsCategoryGrid.getGrid().getStore().clear();
            if (tdsm.getTdsGridList() != null) {
                for (int i = 0; i < tdsm.getTdsGridList().size(); i++) {
                    data = new TdsCategoryGrid_Mdl();
                    data.setDescription(DmShared.xNull(tdsm.getTdsGridList().get(i).getDescription()));
                    data.setDescriptionCode(DmShared.xNull(tdsm.getTdsGridList().get(i).getDescriptionCode()));
                    data.setDescriptionMCode(DmShared.xNull(tdsm.getTdsGridList().get(i).getDescriptionMCode()));
                    data.setPostingAccount(DmShared.xNull(tdsm.getTdsGridList().get(i).getPostingAccount()));
                    data.setPostingAccountCode(DmShared.xNull(tdsm.getTdsGridList().get(i).getPostingAccountCode()));
                    data.setPercent(tdsm.getTdsGridList().get(i).getPercent());
                    data.setAmount(tdsm.getTdsGridList().get(i).getAmount());
                    data.setFormula(tdsm.getTdsGridList().get(i).getFormula());
                    data.setRoundOffTDS(tdsm.getTdsGridList().get(i).getRoundOffTDS());
                    tdsCategoryGrid.getGrid().getStore().add(data);
                    totalValue = totalValue + DmShared.vNull(tdsm.getTdsGridList().get(i).getAmount());
                }
                lblTotalTds.setText(DmClient.vNull(totalValue, "0.00"));
            }
        }

//        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
//            @Override
//            public void execute() {
//                txtHTdsCategory.setFocus(true);
//            }
//        });
    }

    private TDS_Mdl fillTDSDetail() {
        TDS_Mdl tDS_Mdl = new TDS_Mdl();
        TdsCategoryGrid_Mdl data;
        List<TdsCategoryGrid_Mdl> dataList = new ArrayList();
        double amount;

        try {
            if (DmShared.vNull(lblTotalTds.getText()).doubleValue() > 0) {

                calculateTotalTds();
                if (DmShared.vNull(txtTdsAmountProposed.getText()).doubleValue() <= 0) {
                    txtTdsAmountProposed.setText(txtTdsOnAmount.getText());
                }

                for (int i = 0; i < tdsCategoryGrid.getGrid().getStore().getAll().size(); i++) {
                    data = new TdsCategoryGrid_Mdl();
                    data.setDescription(DmShared.xNull(tdsCategoryGrid.getGrid().getStore().get(i).getDescription()));
                    data.setDescriptionCode(DmShared.xNull(tdsCategoryGrid.getGrid().getStore().get(i).getDescriptionCode()));
                    data.setDescriptionMCode(DmShared.xNull(tdsCategoryGrid.getGrid().getStore().get(i).getDescriptionMCode()));
                    data.setPostingAccount(DmShared.xNull(tdsCategoryGrid.getGrid().getStore().get(i).getPostingAccount()));
                    data.setPostingAccountCode(DmShared.xNull(tdsCategoryGrid.getGrid().getStore().get(i).getPostingAccountCode()));
                    data.setPercent(tdsCategoryGrid.getGrid().getStore().get(i).getPercent());
                    data.setAmount(tdsCategoryGrid.getGrid().getStore().get(i).getAmount());
                    data.setFormula(tdsCategoryGrid.getGrid().getStore().get(i).getFormula());
                    data.setRoundOffTDS(tdsCategoryGrid.getGrid().getStore().get(i).getRoundOffTDS());
                    dataList.add(data);
                }

                tDS_Mdl.setTdsCategoryName(DmShared.xNull(txtHTdsCategory.getText()));
                tDS_Mdl.setTdsCategoryCode(DmShared.xNull(txtHTdsCategory.getTag()));
                tDS_Mdl.setTdsDeductFormName(DmShared.xNull(txtHTdsDeduct.getText()));
                tDS_Mdl.setTdsDeductFormCode(DmShared.xNull(txtHTdsDeduct.getTag()));
                tDS_Mdl.setTdsOnAmount(DmClient.vNull(txtTdsOnAmount.getText(), "0.00"));
                tDS_Mdl.setOrignalAmount(DmClient.vNull(txtTdsAmountProposed.getText(), "0.00"));
//            parentRow.setTdsDeducted(DmClient.vNull(lblTotalTds.getText(),"0.00"));
                tDS_Mdl.setTdsFor(DmShared.xNull(txtHTdsFor.getTag()));
                tDS_Mdl.setTdsDetailBind(true);

                if (tDS_Mdl.getTdsFor().trim().equalsIgnoreCase("R")) {
                    if (DmShared.vNull(tDS_Mdl.getDebit()).doubleValue() > 0) {
                        amount = DmShared.vNull(txtTdsAmountProposed.getText());
                        tDS_Mdl.setDebit(DmClient.vNull(amount, "0.00"));
                    } else if (DmShared.vNull(tDS_Mdl.getCredit()).doubleValue() > 0) {
                        amount = DmShared.vNull(txtTdsAmountProposed.getText()) - DmShared.vNull(lblTotalTds.getText());
                        tDS_Mdl.setCredit(DmClient.vNull(amount, "0.00"));
                        //Debit
                    } else if (isDebit) {
                        amount = DmShared.vNull(txtTdsAmountProposed.getText());
                        tDS_Mdl.setDebit(DmClient.vNull(amount, "0.00"));
                        //Credit
                    } else {
                        amount = DmShared.vNull(txtTdsAmountProposed.getText()) - DmShared.vNull(lblTotalTds.getText());
                        tDS_Mdl.setCredit(DmClient.vNull(amount, "0.00"));
                    }
                } else {
                    if (DmShared.vNull(tDS_Mdl.getCredit()).doubleValue() > 0) {
                        amount = DmShared.vNull(txtTdsAmountProposed.getText());
                        tDS_Mdl.setCredit(DmClient.vNull(amount, "0.00"));
                    } else if (DmShared.vNull(tDS_Mdl.getDebit()).doubleValue() > 0) {
                        amount = DmShared.vNull(txtTdsAmountProposed.getText()) - DmShared.vNull(lblTotalTds.getText());
                        tDS_Mdl.setDebit(DmClient.vNull(amount, "0.00"));
                    } else if (isDebit) {
                        amount = DmShared.vNull(txtTdsAmountProposed.getText()) - DmShared.vNull(lblTotalTds.getText());
                        tDS_Mdl.setDebit(DmClient.vNull(amount, "0.00"));
                    } else {
                        amount = DmShared.vNull(txtTdsAmountProposed.getText());
                        tDS_Mdl.setCredit(DmClient.vNull(amount, "0.00"));
                    }
                }

                tDS_Mdl.getTdsGridList().addAll(dataList);
            }
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
        return tDS_Mdl;
    }

    private void calculateTotalTds() {
        List<TdsCategoryGrid_Mdl> listGrid = tdsCategoryGrid.getGrid().getStore().getAll();
        String orignalFormula = "", tempFormula = "", convertedFormula = "", expenseCode = "", roundOffTDS = "";
        OverHeadLine_Shared.GetWhat getWhat = OverHeadLine_Shared.GetWhat.AMOUNT;
        boolean flag = false;
        double tempValue, totalValue = 0, grossValue;

        grossValue = DmShared.vNull(txtTdsOnAmount.getText());
        lblTotalTds.setText("0.00");
        for (int i = 0; i < listGrid.size(); i++) {
            roundOffTDS = DmShared.xNull(listGrid.get(i).getRoundOffTDS());
            if (DmShared.xNull(listGrid.get(i).getFormula()).equalsIgnoreCase("")) {
                orignalFormula = "[G]";
            } else {
//                orignalFormula = listGrid.get(i).getFormula().trim().toUpperCase();
                orignalFormula = listGrid.get(i).getFormula().trim();
            }

            convertedFormula = "";
            for (int j = 0; (j < orignalFormula.length()); j++) {
                tempFormula = orignalFormula.substring(j, j + 1);
                if (flag) {
                    if (tempFormula.equalsIgnoreCase("]") || tempFormula.equalsIgnoreCase("}")) {
                        flag = false;
                        if (expenseCode.equalsIgnoreCase("G")) {
                            convertedFormula = convertedFormula + NumberFormat.getFormat("0.00").format(grossValue);
                        } else {
                            convertedFormula = convertedFormula + NumberFormat.getFormat("0.00").format(otherAddDedFetch(expenseCode, getWhat));
                        }

                    } else {
                        expenseCode = expenseCode + tempFormula;
                    }
                } else {
                    if (tempFormula.equalsIgnoreCase("[") || tempFormula.equalsIgnoreCase("{")) {
                        expenseCode = "";
                        flag = true;
                        if (tempFormula.equalsIgnoreCase("{")) {
                            getWhat = OverHeadLine_Shared.GetWhat.ATRATE;
                        } else {
                            getWhat = OverHeadLine_Shared.GetWhat.AMOUNT;
                        }
                    } else {
                        convertedFormula = convertedFormula + tempFormula;
                    }
                }
            }
            if (convertedFormula.trim().equalsIgnoreCase("")) {
                convertedFormula = "0";
            }

            tempValue = DmShared.compute(convertedFormula);

            if (Double.isInfinite(tempValue)
                    || Double.isNaN(tempValue)) {
                tempValue = 0d;
            }

            if (DmShared.vNull(listGrid.get(i).getPercent()).doubleValue() != 0) {
                tempValue = DmShared.vNull(DmClient.vNull((tempValue * DmShared.vNull(listGrid.get(i).getPercent()).doubleValue()) / 100.0, "0.0000000"));
            }

            if (DmShared.xNull(roundOffTDS).trim().equalsIgnoreCase("D")) {
                listGrid.get(i).setAmount(DmClient.vNull(tempValue, "0.0000000"));

            } else {
                if (DmShared.xNull(roundOffTDS).trim().equalsIgnoreCase("Y")) {
                    tempValue = DmShared.vNull(DmClient.vNull(tempValue, "0"));

                } else if (DmShared.xNull(roundOffTDS).trim().equalsIgnoreCase("H")) {
                    tempValue = Math.ceil(tempValue);

                } else if (DmShared.xNull(roundOffTDS).trim().equalsIgnoreCase("L")) {
                    tempValue = Math.floor(tempValue);
                }

                listGrid.get(i).setAmount(DmClient.vNull(tempValue, "0.00"));
            }

//            listGrid.get(i).setAmount(String.valueOf(tempValue));
            totalValue = totalValue + DmShared.vNull(listGrid.get(i).getAmount());
        }
        lblTotalTds.setText(DmClient.vNull(totalValue, "0.00"));
        tdsCategoryGrid.commitChanges();
    }

    public double otherAddDedFetch(String expCode, OverHeadLine_Shared.GetWhat getWhat) {
        List<TdsCategoryGrid_Mdl> listGrid = tdsCategoryGrid.getGrid().getStore().getAll();
        double rtnValue = 0;

        for (int i = 0; i < listGrid.size(); i++) {
            if (listGrid.get(i).getDescriptionMCode().equalsIgnoreCase(expCode)) {
                switch (getWhat) {
                    case ATRATE:
                        rtnValue = DmShared.vNull(listGrid.get(i).getPercent());
                        break;
                    case AMOUNT:
                        rtnValue = DmShared.vNull(listGrid.get(i).getAmount());
                        break;
                }
            }
        }
        return rtnValue;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 10:
                new DmAlertMessageBox(errorMessage, txtHTdsCategory).show();
                break;
            case 100:
                new DmAlertMessageBox(errorMessage, txtHTdsDeduct).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtTdsOnAmount).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHTdsFor).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
        }
    }

    public void bindTdsGrid(String code) {
        srvAsync.invokeRPC(true).getTDSData(code, new AsyncCallback<DmResult<List<TdsCategoryGrid_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<TdsCategoryGrid_Mdl>> result) {
                if (DmShared.xNull(txtHTdsDeduct.getText()).trim().equalsIgnoreCase("")) {
                    txtHTdsDeduct.setText(DmShared.xNull(txtHAccountName.getText()));
                    txtHTdsDeduct.setTag(DmShared.xNull(txtHAccountName.getTag()));
                }

                if (DmShared.vNull(txtDebit.getText()).doubleValue() > 0) {
                    txtTdsOnAmount.setText(DmClient.vNull(txtDebit.getTag(), "0.00"));
                    txtTdsAmountProposed.setText(DmClient.vNull(txtDebit.getTag(), "0.00"));
                } else if (DmShared.vNull(txtCredit.getText()).doubleValue() > 0) {
                    txtTdsOnAmount.setText(DmClient.vNull(txtCredit.getTag(), "0.00"));
                    txtTdsAmountProposed.setText(DmClient.vNull(txtCredit.getTag(), "0.00"));
                }

                lblTotalTds.setText("0.00");

                if (result.isStatus()) {
                    tdsCategoryGrid.getGrid().getStore().clear();
                    for (int i = 0; i < result.getData().size(); i++) {
                        TdsCategoryGrid_Mdl data = new TdsCategoryGrid_Mdl();
                        data.setDescription(result.getData().get(i).getDescription());
                        data.setDescriptionCode(result.getData().get(i).getDescriptionCode());
                        data.setDescriptionMCode(result.getData().get(i).getDescriptionMCode());
                        data.setPostingAccount(result.getData().get(i).getPostingAccount());
                        data.setPostingAccountCode(result.getData().get(i).getPostingAccountCode());
                        data.setPercent(result.getData().get(i).getPercent());
                        data.setAmount(result.getData().get(i).getAmount());
                        data.setFormula(result.getData().get(i).getFormula());
                        data.setRoundOffTDS(result.getData().get(i).getRoundOffTDS());
                        tdsCategoryGrid.getGrid().getStore().add(data);
                    }
                    calculateTotalTds();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private ChequeDetail_Mdl fillChequeDetail() {
        ChequeDetail_Mdl chequeDetail_Mdl = new ChequeDetail_Mdl();
        if (!DmShared.xNull(txtChequeNo.getText().trim()).equalsIgnoreCase("")) {
            chequeDetail_Mdl.setChequeNo(txtChequeNo.getText());
            chequeDetail_Mdl.setChequeDate(txtChequeDate.getText());
            chequeDetail_Mdl.setFavourOf(txtFavourOF.getText());
            chequeDetail_Mdl.setClearDate(txtClearDate.getText());
//            chequeDetail_Mdl.setClearRemark(txtRemark.getText());
            chequeDetail_Mdl.setAccountNo(txtHAccountNo.getText());
            chequeDetail_Mdl.setBank(DmShared.xNull(txtBank.getText()));
            chequeDetail_Mdl.setBranchAddress(DmShared.xNull(txtBranchAddress.getText()));
            chequeDetail_Mdl.setIfscCode(DmShared.xNull(txtIFSCCode.getText()));
            chequeDetail_Mdl.setBranch(DmShared.xNull(txtBranch.getText()));
            chequeDetail_Mdl.setIsCrossChequeCode(DmShared.xNull(txtHIsCrossCheque.getTag()));
            chequeDetail_Mdl.setIsCrossChequeText(DmShared.xNull(txtHIsCrossCheque.getText()));
            chequeDetail_Mdl.setChequeDetailBind(true);
        }
        return chequeDetail_Mdl;
    }

    private void txtKeyEvents() {
        txtChequeDate.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                txtChequeDate.setText(DmClient.returnDate(txtChequeDate.getText()));
            }
        });

        txtClearDate.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                txtClearDate.setText(DmClient.returnDate(txtClearDate.getText()));
            }
        });
    }

    private void updateDebitCredit() {
        double amount;
        if (DmShared.xNull(txtHTdsFor.getTag()).trim().equalsIgnoreCase("R")) {
            if (DmShared.vNull(txtDebit.getText()).doubleValue() > 0) {
                amount = DmShared.vNull(txtTdsAmountProposed.getText());
                if (amount != 0) {
                    txtDebit.setText(DmClient.vNull(amount, "0.00"));
                }
            } else if (DmShared.vNull(txtCredit.getText()).doubleValue() > 0) {
                amount = DmShared.vNull(txtTdsAmountProposed.getText()) - DmShared.vNull(lblTotalTds.getText());
                if (amount != 0) {
                    txtCredit.setText(DmClient.vNull(amount, "0.00"));
                }
                //Debit
            } else if (isDebit) {
                amount = DmShared.vNull(txtTdsAmountProposed.getText());
                if (amount != 0) {
                    txtDebit.setText(DmClient.vNull(amount, "0.00"));
                }
                //Credit
            } else {
                amount = DmShared.vNull(txtTdsAmountProposed.getText()) - DmShared.vNull(lblTotalTds.getText());
                if (amount != 0) {
                    txtCredit.setText(DmClient.vNull(amount, "0.00"));
                }
            }
        } else {
            if (DmShared.vNull(txtCredit.getText()).doubleValue() > 0) {
                amount = DmShared.vNull(txtTdsAmountProposed.getText());
                if (amount != 0) {
                    txtCredit.setText(DmClient.vNull(amount, "0.00"));
                }
            } else if (DmShared.vNull(txtDebit.getText()).doubleValue() > 0) {
                amount = DmShared.vNull(txtTdsAmountProposed.getText()) - DmShared.vNull(lblTotalTds.getText());
                if (amount != 0) {
                    txtDebit.setText(DmClient.vNull(amount, "0.00"));
                }
            } else if (isDebit) {
                amount = DmShared.vNull(txtTdsAmountProposed.getText()) - DmShared.vNull(lblTotalTds.getText());
                if (amount != 0) {
                    txtDebit.setText(DmClient.vNull(amount, "0.00"));
                }
            } else {
                amount = DmShared.vNull(txtTdsAmountProposed.getText());
                if (amount != 0) {
                    txtCredit.setText(DmClient.vNull(amount, "0.00"));
                }
            }
        }
    }
}
