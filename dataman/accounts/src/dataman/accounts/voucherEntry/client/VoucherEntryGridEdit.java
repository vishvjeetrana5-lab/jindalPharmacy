package dataman.accounts.voucherEntry.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.accounts.voucherEntry.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
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

public class VoucherEntryGridEdit<T1, T2> extends VoucherEntryGridEdit_Dsg<T1, T2> implements DmRootEvents, AttachEvent.Handler, KeyUpHandler {

    //<editor-fold defaultstate="collapsed" desc="CallBack">
    interface CallBack {

        public void onOk(GrdVoucherEntry_Mdl grdVoucherEntry);
//        public void onOk(GrdVoucherEntry_Mdl grdVoucherEntry, List<GrdVoucherEntry_Mdl> listSysRecord);

        //public void onDelete();
        public void onDelete(String parantAcCode);
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    final ServiceInvoker<VoucherEntry_SrvAsync> srvAsync;
    final LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    final ChequeDetail chequeDetail = new ChequeDetail();
    final TdsCategory tdsCategory = new TdsCategory();
    final CostCenter costCenter = new CostCenter();
    final AccountAdjustment adjustmentPopup = new AccountAdjustment();
    final ItemAdjustment itemAdjustmentPopup = new ItemAdjustment();
    protected VoucherEntry voucherEntry;
    private ChequeDetail_Mdl chequeDetailMdl;
    private List<CostCenterGrid_Mdl> costCenterGridData;
    private List<GrdAdjustment_Mdl> lstAdjustments = new ArrayList<>();
    private List<GrdItemAdjustment_Mdl> lstItemAdjustments = new ArrayList<>();
    private TDS_Mdl tdsMdl;
    // List<GrdVoucherEntry_Mdl> lstVoucherEntry;
    private int hashCode = 0;
    protected String acAcountNo = "", acBank = "", acBranchAddress = "", acIfscCode = "", acSwiftCode = "", acBranch = "";
    boolean isHederRecord = false, isTdsRecord = false;
    private String tdsForTag = "", tdsForText = "";

    @Override

    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            chequeDetail.hide();
            tdsCategory.hide();
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

        tdsCategory.addTdsDetailCallBack(new TdsCategory.TDSDetailCallBack() {
            @Override
            public void onOk(TDS_Mdl tDS_Mdl) {

                tdsMdl = new TDS_Mdl();
                tdsMdl = tDS_Mdl;

                txtTDS.setTag(DmShared.xNull(tDS_Mdl.getTdsCategoryCode()));
                txtTDS.setText(DmShared.xNull(tDS_Mdl.getTdsCategoryName()));
                txtDebit.setText(DmClient.vNull(tDS_Mdl.getDebit(), "0.00"));
                txtCredit.setText(DmClient.vNull(tDS_Mdl.getCredit(), "0.00"));
                double totalAmt = 0;
                if (tdsMdl != null) {
                    if (tdsMdl.getTdsGridList() != null) {
                        for (int i = 0; i < tdsMdl.getTdsGridList().size(); i++) {
                            totalAmt += DmShared.vNull(tdsMdl.getTdsGridList().get(i).getAmount());
                        }
                    }
                }
                txtTDSAmt.setText(DmClient.vNull(totalAmt, "0.00"));
                if (DmShared.iNull(txtTDS.getTag()).longValue() != 0) {
                    txtDebit.setEnabled(false);
                    txtCredit.setEnabled(false);
                }
                txtTDSAmt.setFocus(true);

            }

            @Override
            public void onClose() {
                txtTDSAmt.setFocus(true);
            }
        });

        chequeDetail.addChequeDetailCallBack(new ChequeDetail.ChequeDetailCallBack() {
            @Override
            public void onOk(ChequeDetail_Mdl chequeDetail_Mdl) {
                chequeDetailMdl = new ChequeDetail_Mdl();
                chequeDetailMdl = chequeDetail_Mdl;
                txtChequeNo.setText(chequeDetail_Mdl.getChequeNo());
                txtChequeNo.setFocus(true);
            }

            @Override
            public void onClose() {
                txtChequeNo.setFocus(true);
            }
        });

        adjustmentPopup.addCallBack(new AccountAdjustment.CallBack() {
            @Override
            public void onOk(List<GrdAdjustment_Mdl> lstAdjustments) {
                VoucherEntryGridEdit.this.lstAdjustments = lstAdjustments;
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
                VoucherEntryGridEdit.this.lstItemAdjustments = lstItemAdjustments;
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
                    getLedgerBalance(String.valueOf(txtHAccountName.getTag()));
                    txtHAccountName.setTag2nd(DmShared.xNull(tRow.getNatureManualCode()));
                    txtACManualCode.setText(tRow.getSCode());
                    txtACManualCode.setTag(tRow.getDisplayName());
                    txtPan.setText(DmShared.xNull(tRow.getPan()));
                    txtTan.setText(DmShared.xNull(tRow.getTan()));

                    if (tRow.getIsDefault()) {
                        acAcountNo = DmShared.xNull(tRow.getAccountNo());
                        acBank = DmShared.xNull(tRow.getBank());
                        acBranch = DmShared.xNull(tRow.getBranch());
                        acBranchAddress = DmShared.xNull(tRow.getBankAddress());
                        acSwiftCode = DmShared.xNull(tRow.getSwiftCode());
                        acIfscCode = DmShared.xNull(tRow.getIfscCode());
                    }

                    if (!DmShared.xNull(tRow.getTdsCatCode()).equalsIgnoreCase("")) {
                        new DmConfirmMessageBox("TDS Is Applicable For This Account. Do You Want To Apply It?", new DialogHideEvent.DialogHideHandler() {

                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                    txtTDS.setTag(DmShared.xNull(tRow.getTdsCatCode()));
                                    txtTDS.setText(DmShared.xNull(tRow.getTdsCatName()));
                                    tdsForTag = (DmShared.xNull(tRow.getTdsForCode()));
                                    tdsForText = (DmShared.xNull(tRow.getTdsForName()));
                                    
                                    showTdsCategory(tdsMdl);
                                } else {
                                    txtNarration.setFocus(true);
                                }
                            }
                        }).show();
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
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == lblChequeNo) {
            showChequeDetail();
        } else if (event.getSource() == lblTDSAmt) {
            showTdsCategory(tdsMdl);
        } else if (event.getSource() == lblCostCenterAmt) {
            showCostCenterDetail();
        } else if (event.getSource() == lblAdjustment) {
            showAdjustmentPopup();
        } else if (event.getSource() == lblItemAdjustment) {
            showItemAdjustmentPopup();
        } else {
            super.onClick(event);
        }
    }

    public void show(int currentRowIndex, VoucherEntry voucherEntry, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        clear();
        this.voucherEntry = voucherEntry;
        lblLedgerBalance.setText("");
        lblLedgerFyCr.setText("");
        lblLedgerFyDr.setText("");

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

    }

    @Override
    public void displayRow(T1 row) {
        double totalAmt = 0;
        GrdVoucherEntry_Mdl ourRow = (GrdVoucherEntry_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        flwDebitText.clear();
        flwCreditText.clear();
        flwDebitLabel.clear();
        flwCreditLabel.clear();

        if (ourRow.isHeaderAcRecord()) {
            isHederRecord = true;

        } else {
            isHederRecord = false;
        }
        if (ourRow.isTdsRecord()) {
            isTdsRecord = true;
        } else {
            isTdsRecord = false;
        }
        if (isHederRecord || isTdsRecord) {
            flwDebitText.add(txtDebit);
            flwDebitLabel.add(lblDebit);
            flwCreditText.add(txtCredit);
            flwCreditLabel.add(lblCredit);
        } else {
            if (DmShared.xNull(voucherEntry.txtHVoucherType.getTag2nd()).trim().equalsIgnoreCase(lvc.BANK_PAYMENT)) {
                flwDebitText.add(txtDebit);
                flwDebitLabel.add(lblDebit);
            } else if (DmShared.xNull(voucherEntry.txtHVoucherType.getTag2nd()).trim().equalsIgnoreCase(lvc.BANK_RECEIPT)) {
                flwCreditText.add(txtCredit);
                flwCreditLabel.add(lblCredit);
            } else if (DmShared.xNull(voucherEntry.txtHVoucherType.getTag2nd()).trim().equalsIgnoreCase(lvc.JOURNAL)
                    || DmShared.xNull(voucherEntry.txtHVoucherType.getTag2nd()).trim().equalsIgnoreCase(lvc.CONTRA)) {

                flwDebitText.add(txtDebit);
                flwDebitLabel.add(lblDebit);
                flwCreditText.add(txtCredit);
                flwCreditLabel.add(lblCredit);
            }
        }

        tdsMdl = new TDS_Mdl();
        tdsMdl = ourRow.getTds_Mdl();

        costCenterGridData = new ArrayList<CostCenterGrid_Mdl>();
        costCenterGridData.addAll(ourRow.getCostCenterGrdData());

        lstAdjustments = new ArrayList<GrdAdjustment_Mdl>();
        lstAdjustments.addAll(ourRow.getLstAdjustments());

        lstItemAdjustments = new ArrayList<GrdItemAdjustment_Mdl>();
        lstItemAdjustments.addAll(ourRow.getLstItemAdjustments());

        chequeDetailMdl = new ChequeDetail_Mdl();
        chequeDetailMdl = ourRow.getChequeDetail_Mdl();
        txtPan.setText(ourRow.getPan());
        txtTan.setText(ourRow.getTan());

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
        txtHAccountName.setTag2nd(DmShared.xNull(ourRow.getAccNature()));
        txtNarration.setText(DmShared.xNull(ourRow.getNarration()));
        txtDebit.setText(DmShared.xNull(ourRow.getDebit()));
        txtCredit.setText(DmShared.xNull(ourRow.getCredit()));

        txtTDS.setTag(DmShared.xNull(tdsMdl.getTdsCategoryCode()));
        txtTDS.setText(DmShared.xNull(tdsMdl.getTdsCategoryName()));
        tdsForTag = DmShared.xNull(tdsMdl.getTdsForTag());
        tdsForText = DmShared.xNull(tdsMdl.getTdsForText());

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
        return (T1) new GrdVoucherEntry_Mdl();
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
        List<GrdVoucherEntry_Mdl> lstTemp = (List<GrdVoucherEntry_Mdl>) getDmGrid().getGrid().getStore().getAll();
        for (int i = 0; i < lstTemp.size(); i++) {
            GrdVoucherEntry_Mdl mdl = lstTemp.get(i);
            if (DmShared.xNull(txtHAccountName.getTag()).equalsIgnoreCase(DmShared.xNull(mdl.getAccCode()))
                    && !mdl.isTdsRecord()) {
                if (DmShared.iNull(hashCode).intValue() != DmShared.iNull(mdl.getSharedKeyFieldHashCode()).intValue()) {
                    new DmAlertMessageBox(txtHAccountName.getText() + " already exists.", txtHAccountName).show();
                    return;
                }

            }

        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdVoucherEntry_Mdl ourRow = (GrdVoucherEntry_Mdl) row;

        // lstVoucherEntry = new ArrayList<>();
        ourRow.setAccMCode(txtACManualCode.getText());
        ourRow.setAccName(txtHAccountName.getText());
        ourRow.setAccCode(txtHAccountName.getTag());
        ourRow.setParentAccountCode(txtHAccountName.getTag());
        ourRow.setAccNature(txtHAccountName.getTag2nd());

        ourRow.setNarration(txtNarration.getText());
        ourRow.setDebit(null);
        if (DmShared.vNull(txtDebit.getText()).doubleValue() > 0) {
            ourRow.setDebit(DmClient.vNull(txtDebit.getText(), "0.00"));
        }
        ourRow.setCredit(null);
        if (DmShared.vNull(txtCredit.getText()).doubleValue() > 0) {
            ourRow.setCredit(DmClient.vNull(txtCredit.getText(), "0.00"));
        }
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
        ourRow.setChequeDetail_Mdl(chequeDetailMdl);
        ourRow.setTds_Mdl(tdsMdl);

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        new DmConfirmMessageBox("Are you sure? You want to delet this record.", new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    proceedDelete();
                }
            }
        }).show();
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        if (callBack != null) {
            callBack.onDelete(DmShared.xNull(txtHAccountName.getTag()));
        }
    }

    public VoucherEntryGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Voucher Entry", "500px", "275px");
        srvAsync = new ServiceInvoker<VoucherEntry_SrvAsync>(GWT.create(VoucherEntry_Srv.class), VoucherEntry.class.getName());
        textHelpEvents();
        manageEvents();
    }

    private void manageEvents() {
        lblChequeNo.addClickHandler(this);
        lblCostCenterAmt.addClickHandler(this);
        lblTDSAmt.addClickHandler(this);
        lblAdjustment.addClickHandler(this);
        lblItemAdjustment.addClickHandler(this);

        txtChequeNo.addKeyUpHandler(this);
        txtCostCenterAmt.addKeyUpHandler(this);
        txtTDSAmt.addKeyUpHandler(this);
        txtAdjustment.addKeyUpHandler(this);
        txtItemAdjustment.addKeyUpHandler(this);
    }

    private void showChequeDetail() {
        if (voucherEntry.topControl.getMode().equalsIgnoreCase("New")) {
            chequeDetailMdl.setAccountCode(DmShared.xNull((txtHAccountName.getTag())));
            if (!chequeDetailMdl.isChequeDetailBind()) {
                chequeDetailMdl.setFavourOf(DmShared.xNull((txtACManualCode.getTag())));
                chequeDetailMdl.setChequeDate(DmShared.dNull(DmClient.getLoginDate()));
                chequeDetailMdl.setAccountNo(DmShared.xNull(acAcountNo));
                chequeDetailMdl.setBank(DmShared.xNull(acBank));
                chequeDetailMdl.setBranchAddress(DmShared.xNull(acBranchAddress));
                chequeDetailMdl.setBranch(DmShared.xNull(acBranch));
                chequeDetailMdl.setSwiftCode(DmShared.xNull(acSwiftCode));
                chequeDetailMdl.setIfscCode(DmShared.xNull(acIfscCode));
                chequeDetailMdl.setIsCrossChequeCode("1");
                chequeDetailMdl.setIsCrossChequeText("Yes");
            }

        }
        if (!isOkEnable()) {
            chequeDetail.show(chequeDetailMdl, false);
        } else {
            chequeDetail.show(chequeDetailMdl, true);
        }
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
        //String tempTdsFor = "";

        if (!DmShared.xNull(txtHAccountName.getTag()).equalsIgnoreCase("")) {
            tdsm.setTdsCategoryCode(DmShared.xNull(txtTDS.getTag()));
            tdsm.setTdsCategoryName(DmShared.xNull(txtTDS.getText()));
            tdsm.setTdsDeductFormCode(DmShared.xNull(txtHAccountName.getTag()));
            tdsm.setTdsDeductFormName(DmShared.xNull(txtHAccountName.getText()));
            tdsm.setTdsDeductManualCode(DmShared.xNull(txtHAccountName.getTag2nd()));
            tdsm.setVr_Date(voucherEntry.txtVoucherDate.getText());

            if (DmShared.vNull(txtDebit.getText()) > 0) {
                isDebit = true;
            } else if (DmShared.vNull(txtCredit.getText()) > 0) {
                isDebit = false;
            } else {

                if (DmShared.xNull(voucherEntry.txtHVoucherType.getTag2nd()).trim().equalsIgnoreCase(lvc.BANK_PAYMENT)) {

                    isDebit = true;

                } else if (DmShared.xNull(voucherEntry.txtHVoucherType.getTag2nd()).trim().equalsIgnoreCase(lvc.BANK_RECEIPT)) {
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

            if (!DmShared.xNull(tdsm.getTdsForTag()).trim().equalsIgnoreCase("")) {
                tdsForTag = tdsm.getTdsForTag();
            }
//            else {
//                if (DmShared.xNull(voucherEntry.txtHVoucherType.getTag2nd()).equalsIgnoreCase(voucherEntry.lvc.JOURNAL)
//                        || DmShared.xNull(voucherEntry.txtHVoucherType.getTag2nd()).equalsIgnoreCase(voucherEntry.lvc.BANK_PAYMENT)) {
//
//                    tempTdsFor = "P";
//
//                } else if (DmShared.xNull(voucherEntry.txtHVoucherType.getTag2nd()).equalsIgnoreCase(voucherEntry.lvc.BANK_RECEIPT)) {
//                    tempTdsFor = "R";
//                }

//            }
            tdsm.setTdsOnAmount(DmClient.vNull(tempAmout, "0.00"));
            // tdsm.setTdsFor(tempTdsFor);
            tdsm.setTdsForTag(tdsForTag);

            if (!isOkEnable()) {
                tdsCategory.show(tdsm, voucherEntry.txtHVoucherType.getTag2nd(), isDebit, false, DmShared.xNull(voucherEntry.topControl.getMode()));
            } else {
                tdsCategory.show(tdsm, voucherEntry.txtHVoucherType.getTag2nd(), isDebit, true, DmShared.xNull(voucherEntry.topControl.getMode()));
            }
        } else {
            new DmAlertMessageBox("Please Mention A/c Name.", txtHAccountName).show();
        }
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
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_H) {
            showChequeDetail();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_T) {
            showTdsCategory(tdsMdl);
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
            if (event.getSource().equals(txtChequeNo)) {
                showChequeDetail();
            } else if (event.getSource().equals(txtTDSAmt)) {
                showTdsCategory(tdsMdl);
            } else if (event.getSource().equals(txtCostCenterAmt)) {
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
        srvAsync.invokeRPC(true).getGetLedgerBalance(StrSubCode, new AsyncCallback<DmResult<Double[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Double[]> result) {
                if (result.isStatus()) {
                    if (DmShared.vNull(result.getData()[0]).doubleValue() > 0) {
                        lblLedgerBalance.setText("Dr. " + DmClient.vNull(result.getData()[0], "0.00"));
                    } else if (DmShared.vNull(result.getData()[0]).doubleValue() < 0) {
                        lblLedgerBalance.setText("Cr. " + DmClient.vNull((0 - result.getData()[0]), "0.00"));
                    } else {
                        lblLedgerBalance.setText("");
                    }
                    if (DmShared.vNull(result.getData()[1]).doubleValue() > 0 || DmShared.vNull(result.getData()[1]).doubleValue() < 0) {
                        lblLedgerFyDr.setText("F.Y Dr. " + DmClient.vNull(result.getData()[1], "0.00"));
                    } else {
                        lblLedgerFyDr.setText("");
                    }
                    if (DmShared.vNull(result.getData()[2]).doubleValue() > 0 || DmShared.vNull(result.getData()[2]).doubleValue() < 0) {
                        lblLedgerFyCr.setText("F.Y Cr. " + DmClient.vNull(result.getData()[2], "0.00"));
                    } else {
                        lblLedgerFyCr.setText("");
                    }

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void clear() {
        voucherEntry = null;
        hashCode = 0;

        txtHAccountName.setText("");
        txtHAccountName.setTag("");
        txtHAccountName.setTag2nd("");
        resetAccount();

    }

    private void resetAccount() {
        tdsForTag = "";
        tdsForText = "";
        acAcountNo = "";
        acBank = "";
        acBranchAddress = "";
        acIfscCode = "";
        acSwiftCode = "";
        acBranch = "";

        txtACManualCode.setText("");
        txtACManualCode.setTag("");
        txtACManualCode.setTag2nd("");

        lblLedgerBalance.setText("");
        lblLedgerFyDr.setText("");
        lblLedgerFyCr.setText("");

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
        txtTDS.setText("");
        txtTDS.setTag("");
        txtTDS.setTag2nd("");

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

}
