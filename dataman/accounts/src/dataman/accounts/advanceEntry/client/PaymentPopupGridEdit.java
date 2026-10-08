package dataman.accounts.advanceEntry.client;

import dataman.accounts.advanceEntry.shared.AdvanceEntry_Srv;
import dataman.accounts.advanceEntry.shared.AdvanceEntry_SrvAsync;
import dataman.accounts.advanceEntry.shared.GrdPayment_Mdl;
import dataman.accounts.advanceEntry.shared.TdsCategoryGrid_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import dataman.dmToolkit.tdsCategoryPopup.client.TdsCategoryPopup;
import dataman.dmToolkit.tdsCategoryPopup.shared.TdsCategoryPopup_Mdl;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.ArrayList;
import java.util.List;

public class PaymentPopupGridEdit<T1, T2> extends PaymentPopupGridEdit_Dsg<T1, T2> implements BlurHandler {

    public interface CallBack {

        public void onOk(GrdPayment_Mdl grdPayment);

        public void onDelete();
    }

    private final ServiceInvoker<AdvanceEntry_SrvAsync> SrvAsync;
    private CallBack callBack;
    private int hashCode;
    private PaymentPopup paymnetPopup;
    private List<TdsCategoryGrid_Mdl> lstTdsCategory = new ArrayList<>();
    private String supplierCode = "", supplierName = "", vDate = "";
    private final TdsCategoryPopup tdsCategoryPp = new TdsCategoryPopup();
    private TdsCategoryPopup_Mdl tdsCategoryPopup_Mdl;
    LibraryVoucherCategory lvct = new LibraryVoucherCategory();
    String ledgerDocId="",poDocId="";
    int indexOfIFSC=0;
    private void textHelpEvents() {
        chk.addCallBack(new DmCheckBox.CallBack() {
            @Override
            public void valueChanged() {
                if (!DmShared.xNull(paymnetPopup.advanceEntry.topControl.getMode()).equalsIgnoreCase("Browse")) {
                    if (chk.getValue() == true) {
                        setTDSCatPpFields();
                    } else {
                        lstTdsCategory.clear();
                        tdsCategoryPopup_Mdl = null;
                    }

                }

            }
        });
        txtHPaymentMode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<GrdPayment_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCardType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdPayment_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPaymentMode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdPayment_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPaymentMode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPaymentMode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, GrdPayment_Mdl tRow) {
                txtAmount.setEnabled(true);
                if (blnStatus && tRow != null) {
                    clear();
                    txtCardType.setTag(tRow.getCardTypeNatureCode());
                    txtCardType.setText(tRow.getCardTypeNatureName());
                    txtCardTypeShortName.setText(tRow.getCardTypeShortName());
                    txtAcName.setText(tRow.getAcNameName());
                    txtAcName.setTag(tRow.getAcNameCode());
                    txtAcName.setTag2nd(tRow.getAcNature());
                    txtHAccount.setText(tRow.getAcNameName());
                    txtHAccount.setTag(tRow.getAcNameCode());
                    txtHAccount.setTag2nd(tRow.getAcNature());

                    if (DmShared.vNull(txtAmount.getText()).doubleValue() == 0) {
                        txtAmount.setText(DmClient.vNull(txtAmount.getTag(), "0.00"));
                    }
                    if (chk.getValue()) {
                        setTDSCatPpFields();
                    }

                    DmEnum.CardType cardType = DmEnum.CardType.getValueOf(DmShared.iNull(tRow.getCardTypeNatureCode()).intValue());
                    mainFlwPanel.clear();
                    if (null == cardType) {
                    } else {
                        switch (cardType) {

                            case BANK:
                                mainFlwPanel.add(flwBank);
                                txtInsDate.setText(DmClient.getLoginDate());
                                if (DmShared.xNull(txtCardTypeShortName.getText()).trim().equalsIgnoreCase("cheque")){
                                    txtFavourOf.setText(DmShared.xNull(paymnetPopup.advanceEntry.txtSupplierName.getText()));
                                }else if (DmShared.xNull(txtCardTypeShortName.getText()).trim().equalsIgnoreCase("FNDTRF")) {
                                    indexOfIFSC=DmShared.xNull(paymnetPopup.advanceEntry.txtSupplierDisplayName.getText()).indexOf("IFSC");
                                    if (indexOfIFSC>0){
                                        txtFavourOf.setText(DmShared.xNull(paymnetPopup.advanceEntry.txtSupplierDisplayName.getText()).substring(0,indexOfIFSC-1));
                                    }else {
                                        txtFavourOf.setText(DmShared.xNull(paymnetPopup.advanceEntry.txtSupplierDisplayName.getText()));
                                    }
                                }else {
                                    txtFavourOf.setText(DmShared.xNull(txtHPaymentMode.getText()).trim()+" "+ DmShared.xNull(paymnetPopup.
                                            advanceEntry.txtSupplierDisplayName.getText()));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    txtAmount.setFocus(true);
                }
            }
        });
        txtHAccount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccounts(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAccount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAccount).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, PaymentPopup paymentPopup) {
        this.paymnetPopup = paymentPopup;
        supplierCode = DmShared.xNull(paymnetPopup.advanceEntry.txtSupplierName.getTag());
        supplierName = DmShared.xNull(paymnetPopup.advanceEntry.txtSupplierName.getText());
        vDate = DmShared.xNull(paymnetPopup.advanceEntry.txtVoucherDate.getText());

        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        manageDisplay();

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (txtHPaymentMode.isEnabled()) {
                    txtHPaymentMode.setFocus(true);
                } else {
                    txtAmount.setFocus(true);
                }
                txtAmount.setEnabled(true);
            }
        });
    }

    private void manageDisplay() {
        txtBalance.setEnabled(false);
        txtCardType.setEnabled(false);
        txtCardTypeShortName.setEnabled(false);
        txtAcName.setEnabled(false);
        txtTotalTds.setEnabled(false);
        txtTdsPer.setEnabled(false);
    }

    private void handlerEvents() {
        txtInsDate.addBlurHandler(this);
        txtAmount.addBlurHandler(this);
        lblTDS.addClickHandler(this);
        onClick();
    }

    public void onClick() {

        lblTDS.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (tdsCategoryPopup_Mdl != null) {
                    tdsCategoryPp.show(tdsCategoryPopup_Mdl, new LibraryVoucherCategory().FINAL_PAYMENT_ENTRY, true, true, paymnetPopup.advanceEntry.topControl.getMode());
                } else {
                    tdsCategoryPp.show();
                }
            }
        });
    }

    private void tdsCategoryPpEvents() {
        tdsCategoryPp.addTdsDetailCallBack(new TdsCategoryPopup.TDSDetailCallBack() {
            @Override
            public void onOk(TdsCategoryPopup_Mdl tDS_Mdl) {
                tdsCategoryPopup_Mdl = tDS_Mdl;
            }

            @Override
            public void setTDS(String tdsAmount) {
                txtTotalTds.setText(DmClient.vNull(tdsAmount, "0.00"));
                txtNetAmt.setText(DmClient.vNull(DmShared.vNull(txtAmount.getText()) - DmShared.vNull(txtTotalTds.getText()), "0.00"));
                if (DmShared.vNull(txtTotalTds.getText()).doubleValue() > 0) {
                    chk.setValue(true);
                } else {
                    chk.setValue(false);
                }
            }

            @Override
            public void onClose() {

            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdPayment_Mdl ourRow = (GrdPayment_Mdl) row;
        
        txtHPaymentMode.setTag(DmShared.xNull(ourRow.getCardTypeCode()));
        txtHPaymentMode.setText(DmShared.xNull(ourRow.getCardTypeName()));
        txtCardType.setTag(DmShared.xNull(ourRow.getCardTypeNatureCode()));
        txtCardType.setText(DmShared.xNull(ourRow.getCardTypeNatureName()));
        txtCardTypeShortName.setText(DmShared.xNull(ourRow.getCardTypeShortName()));
        txtNetAmt.setText(DmClient.vNull(ourRow.getNetPay(), "0.00"));
        txtAmount.setText(DmClient.vNull(ourRow.getOriginalAmount(), "0.00"));
        txtAcName.setTag(DmShared.xNull(ourRow.getAcNameCode()));
        txtAcName.setText(DmShared.xNull(ourRow.getAcNameName()));
        txtAcName.setTag2nd(DmShared.xNull(ourRow.getAcNature()));
        txtHAccount.setTag(DmShared.xNull(ourRow.getAcNameCode()));
        txtHAccount.setText(DmShared.xNull(ourRow.getAcNameName()));
        txtHAccount.setTag2nd(DmShared.xNull(ourRow.getAcNature()));
        DmEnum.CardType cardType = DmEnum.CardType.getValueOf(DmShared.iNull(ourRow.getCardTypeNatureCode()).intValue());
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        txtInstrumentNoBK.setText(DmShared.xNull(ourRow.getInsNo()));
        txtInsDate.setText(DmShared.xNull(ourRow.getInsDate()));
        txtFavourOf.setText(DmShared.xNull(ourRow.getFavourOf()));
        ledgerDocId=DmShared.xNull(ourRow.getLedgerDocId());
        poDocId=DmShared.xNull(ourRow.getPoDocID());
        tdsCategoryPopup_Mdl = ourRow.getTdsCatMdl();
        tdsCategoryPp.moveRec(tdsCategoryPopup_Mdl);
        mainFlwPanel.clear();

        switch (cardType) {
            case BANK:
                mainFlwPanel.add(flwBank);
                break;

        }
        txtHPaymentMode.setFocus(true);

        hashCode = ourRow.getSharedKeyFieldHashCode();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdPayment_Mdl();
    }

    @Override
    public void onOk() {
        DmEnum.CardType cardType = DmEnum.CardType.getValueOf(DmShared.iNull(txtCardType.getTag()).intValue());

        if (!DmClient.isValidText(txtHPaymentMode.getTag(), "Payment Mode", txtHPaymentMode)) {
            return;
        }

        if (!DmClient.isValidText(txtHAccount.getTag(), "A/c Name", txtHPaymentMode)) {
            return;
        }

        if (DmShared.vNull(txtAmount.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Amount is mandatory.", txtAmount).show();
            return;
        }

        if ((DmShared.vNull(txtAmount.getText()).doubleValue() - DmShared.vNull(txtTotalTds.getText()).doubleValue()) <= 0) {
            new DmAlertMessageBox("Total TDS should not be greater than amount.", txtAmount).show();
            return;
        }
        List<GrdPayment_Mdl> lstTemp = (List<GrdPayment_Mdl>) getDmGrid().getGrid().getStore().getAll();

        for (int i = 0; i < lstTemp.size(); i++) {
            GrdPayment_Mdl mdl = lstTemp.get(i);
            if (DmShared.iNull(txtHPaymentMode.getTag()).longValue() == DmShared.iNull(mdl.getCardTypeCode()).longValue()) {
                if (hashCode != mdl.getSharedKeyFieldHashCode()) {
                    new DmAlertMessageBox(txtHPaymentMode.getText() + " already exists.", txtHPaymentMode).show();
                    return;
                }
            }
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdPayment_Mdl ourRow = (GrdPayment_Mdl) row;
        TdsCategoryGrid_Mdl data = new TdsCategoryGrid_Mdl();
        List<TdsCategoryGrid_Mdl> dataList = new ArrayList();
        DmEnum.CardType cardType = DmEnum.CardType.getValueOf(DmShared.iNull(txtCardType.getTag()).intValue());

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHPaymentMode.getTag()).trim());
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHPaymentMode.getText()).trim());
        ourRow.setCardTypeCode(DmShared.xNull(txtHPaymentMode.getTag()).trim());
        ourRow.setCardTypeName(DmShared.xNull(txtHPaymentMode.getText()).trim());
        ourRow.setCardTypeShortName(DmShared.xNull(txtCardTypeShortName.getText()).trim());
        ourRow.setCardTypeNatureCode(DmShared.xNull(txtCardType.getTag()).trim());
        ourRow.setCardTypeNatureName(DmShared.xNull(txtCardType.getText()).trim());

        ourRow.setNetPay(DmClient.vNull(txtNetAmt.getText(), "0.00"));
        ourRow.setOriginalAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setAcNameCode(txtAcName.isAttached() ? DmShared.xNull(txtAcName.getTag()).trim() : DmShared.xNull(txtHAccount.getTag()).trim());
        ourRow.setAcNameName(txtAcName.isAttached() ? DmShared.xNull(txtAcName.getText()).trim() : DmShared.xNull(txtHAccount.getText()).trim());
        ourRow.setAcNature(txtAcName.isAttached() ? DmShared.xNull(txtAcName.getTag2nd()).trim() : DmShared.xNull(txtHAccount.getTag2nd()).trim());

        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setInsNo(DmShared.xNull(txtInstrumentNoBK.getText()));
        ourRow.setInsDate(DmShared.dNull(txtInsDate.getText()).trim());
        
        if (DmShared.xNull(txtFavourOf.getText()).equalsIgnoreCase("")) {
            ourRow.setFavourOf(DmShared.xNull(paymnetPopup.advanceEntry.txtSupplierName.getText()));
        } else {
            ourRow.setFavourOf(DmShared.xNull(txtFavourOf.getText()));
        }
        
        if (DmShared.vNull(txtTotalTds.getText()).doubleValue() != 0.0) {
            tdsCategoryPp.onOK();
            ourRow.setTdsCatMdl(tdsCategoryPopup_Mdl);
        } else {
            ourRow.setTdsCatMdl(null);
        }
        ourRow.setNetPay(DmClient.vNull(DmShared.vNull(txtAmount.getText()) - DmShared.vNull(txtTotalTds.getText()), "0.00"));
        ourRow.setTdsDeduction(DmClient.vNull(DmShared.vNull(txtTotalTds.getText()), "0.00"));

        ourRow.getTdsGridList().clear();
        ourRow.getTdsGridList().addAll(dataList);

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

    public PaymentPopupGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Payment", "500px", "200px");
        SrvAsync
                = new ServiceInvoker<>(GWT.create(AdvanceEntry_Srv.class
                ), AdvanceEntry.class
                        .getName());

        textHelpEvents();
        handlerEvents();
        tdsCategoryPpEvents();
    }

    private void clear() {
        lstTdsCategory = new ArrayList<>();
        txtAcName.setTag2nd("");
        txtAcName.setTag("");
        txtAcName.setText("");
        txtHAccount.setTag2nd("");
        txtHAccount.setTag("");
        txtHAccount.setText("");

        txtBalance.setText("");

        txtCardType.setTag("");
        txtCardType.setText("");
        txtCardTypeShortName.setText("");
        txtRemark.setText("");
        txtRemark.setText("");

        txtInsDate.setText("");

    }

    public double otherAddDedFetch(String expCode, OverHeadLine_Shared.GetWhat getWhat) {
        List<TdsCategoryGrid_Mdl> listGrid = lstTdsCategory;
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

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtInsDate) {
            txtInsDate.setText(DmClient.returnDate(txtInsDate.getText()));
        } else if (event.getSource() == txtAmount) {

            if (chk.getValue()) {
                setTDSCatPpFields();
            }
        }

    }

    private void setTDSCatPpFields() {

        if (DmShared.iNull(supplierCode).longValue() > 0) {
            if (tdsCategoryPopup_Mdl == null) {
                tdsCategoryPopup_Mdl = new TdsCategoryPopup_Mdl();
            }
            tdsCategoryPopup_Mdl.setTdsDeductFormCode(supplierCode);
            tdsCategoryPopup_Mdl.setTdsDeductFormName(supplierName);
            tdsCategoryPopup_Mdl.setTdsOnAmount(txtAmount.getText());
            tdsCategoryPopup_Mdl.setOrignalAmount(txtAmount.getText());
            tdsCategoryPopup_Mdl.setVoucherDate(DmShared.dNull(vDate));
            tdsCategoryPopup_Mdl.setvCategory(lvct.FINAL_PAYMENT_ENTRY);
            tdsCategoryPopup_Mdl.setTrnDocID(ledgerDocId);
            tdsCategoryPopup_Mdl.setPoDocID(poDocId);
        }
        tdsCategoryPp.setPpFields(tdsCategoryPopup_Mdl);
    }
}
