package dataman.accounts.gstVoucherEntry.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.accounts.gstVoucherEntry.shared.*;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.itemAdjustment.client.ItemAdjustment;
import dataman.dmToolkit.itemAdjustment.shared.*;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.ArrayList;
import java.util.List;

public class AcDetailsGridEdit<T1, T2> extends AcDetailsGridEdit_Dsg<T1, T2> implements DmRootEvents, AttachEvent.Handler, KeyUpHandler, BlurHandler {

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSGSUGSTTPer)) {
            txtCGSTTPer.setText(txtSGSUGSTTPer.getText());
            calculate();
        } else if (event.getSource().equals(txtCGSTTPer)) {
            if (DmShared.vNull(txtSGSUGSTTPer.getText()).doubleValue() == 0) {
                txtSGSUGSTTPer.setText(txtCGSTTPer.getText());
            }
            calculate();
        } else if (event.getSource().equals(txtIGSTTPer)
                || event.getSource().equals(txtTaxableAmount)) {
            calculate();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="CallBack">
    interface CallBack {

        public void onOk(GridAcDetailsMdl grdAcDetails);

        public void onDelete();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    final ServiceInvoker<GstVoucherEntry_SrvAsync> srvAsync;
    final LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    final ItemAdjustment itemAdjustmentPopup = new ItemAdjustment();
    private List<GrdItemAdjustment_Mdl> lstItemAdjustments = new ArrayList<>();
    GstVoucherEntry gstVoucherEntry;
    private int hashCode = 0;
    String accPartyTypeCode = "", gstIn = "", gstInDate = "", stateCode = "", accNature = "", v_Sno = "", entryTransactionType = "", prvSelectedAc = "";
    String prvSelectedHsn = "", qty = "", rate = "", roundoffGst = "", isRCM = "";
    private boolean isUT = false;

    public AcDetailsGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Details", "400px", "");
        srvAsync = new ServiceInvoker<GstVoucherEntry_SrvAsync>(GWT.create(GstVoucherEntry_Srv.class), GstVoucherEntry.class.getName());
        txtItemAdjustment.addKeyUpHandler(this);
        textHelpEvents();
        handlerEvents();
    }

    public void show(int currentRowIndex, GstVoucherEntry gstVoucherEntry, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        this.gstVoucherEntry = gstVoucherEntry;
        entryTransactionType = "";
        initialize();
        manageDisplay();
        if (gstVoucherEntry.txtHVoucherType.getTag2nd().equalsIgnoreCase(lvc.CREDIT_INVOICE_GST)
                || gstVoucherEntry.txtHVoucherType.getTag2nd().equalsIgnoreCase(lvc.CREDIT_NOTE_GST)) {
            entryTransactionType = "Debit";
        } else if (gstVoucherEntry.txtHVoucherType.getTag2nd().equalsIgnoreCase(lvc.DEBIT_INVOICE_GST)
                || gstVoucherEntry.txtHVoucherType.getTag2nd().equalsIgnoreCase(lvc.DEBIT_NOTE_GST)) {
            entryTransactionType = "Credit";
        }

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHAccountName.setFocus(true);
            }
        });
    }

    private void initialize() {
        if (DmShared.xNull(gstVoucherEntry.defaultValue.getIsSiteStateUT()).equalsIgnoreCase("1")) {
            isUT = true;
        }
        roundoffGst = gstVoucherEntry.txtHGSTRounding.getTag();
        isRCM = gstVoucherEntry.txtHRcm.getTag();
        setDisplay();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHHSN.setFocus(true);
            }
        });
    }

    private void handlerEvents() {
        txtCGSTTPer.addBlurHandler(this);
        txtSGSUGSTTPer.addBlurHandler(this);
        txtIGSTTPer.addBlurHandler(this);
        txtTaxableAmount.addBlurHandler(this);
    }

    private void textHelpEvents() {
        itemAdjustmentPopup.addCallBack(new ItemAdjustment.CallBack() {
            @Override
            public void onOk(List<GrdItemAdjustment_Mdl> lstItemAdjustments) {
                AcDetailsGridEdit.this.lstItemAdjustments = lstItemAdjustments;
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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountNameGrid(helper, blnToEndOfFile, gstVoucherEntry.txtHAccountName.getTag(), gstVoucherEntry.txtVoucherDate.getText(), new AsyncCallback<DmResultPageing<List<TxtHAccountNameGrid_Mdl>>>() {

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
                if (blnStatus && tRow != null) {
                    if (DmShared.iNull(tRow.getSharedKeyFieldCode()).longValue() != DmShared.iNull(prvSelectedAc).longValue()) {
                        resetAccount();
                        prvSelectedAc = tRow.getSharedKeyFieldCode();
                    }
                    txtHAccountName.setTag2nd(DmShared.xNull(tRow.getSCode()));
                    accPartyTypeCode = tRow.getPartyTypeCode();
                    accNature = tRow.getNatureManualCode();
                    gstIn = tRow.getGstin();
                    gstInDate = tRow.getGstinDate();
                    stateCode = tRow.getStateCode();
                }
            }
        });

        txtHHSN.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHHSN_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHHSN(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHHSN_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHHSN_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHHSN.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, final TxtHHSN_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    if (DmShared.iNull(tRow.getSharedKeyFieldCode()).longValue() != DmShared.iNull(prvSelectedHsn).longValue()) {
                        resetDetails();
                        prvSelectedHsn = tRow.getSharedKeyFieldCode();
                    }
                    txtHHSN.setTag2nd(tRow.getItemGSTNatureTag());
                    txtHSNSAC.setText(tRow.getHsn());
                }
            }
        });

        txtHSgstAccount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTaxAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSgstAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHCgstAccount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTaxAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCgstAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHIgstAccount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTaxAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIgstAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
    }

    private void showItemAdjustmentPopup() {
        if (!DmShared.xNull(txtHAccountName.getTag()).trim().equals("")) {
            ItemAdjustment_Mdl iapMdl = new ItemAdjustment_Mdl();
            iapMdl.setIsEditable(isOkEnable());
            iapMdl.setAcCode(txtHAccountName.getTag());
            iapMdl.setAcName(txtHAccountName.getText());

            if (entryTransactionType.equalsIgnoreCase("Debit")) {
                iapMdl.setAmtDr(txtAmount.getText());
            } else if (entryTransactionType.equalsIgnoreCase("Credit")) {
                iapMdl.setAmtCr(txtAmount.getText());
            }
//            iapMdl.setAmtDr(txtDebit.getText());
//            iapMdl.setAmtCr(txtCredit.getText());
            iapMdl.setLstItemAdjustment(lstItemAdjustments);
            itemAdjustmentPopup.show(iapMdl);
        }
    }

    private void clear() {
        txtHAccountName.setTag("");
        txtHAccountName.setText("");
        txtHAccountName.setTag2nd("");

        v_Sno = "";

        resetAccount();
    }

    private void resetAccount() {
        txtAmount.setText("");
        txtNarration.setText("");

        txtDebit.setText("");
        txtDebit.setTag("");
        txtDebit.setTag2nd("");

        txtCredit.setText("");
        txtCredit.setTag("");
        txtCredit.setTag2nd("");

        txtTaxableAmount.setText("");
        txtTaxableAmount.setTag("");
        txtTaxableAmount.setTag2nd("");

        txtCGSTAmount.setText("");
        txtCGSTAmount.setTag("");
        txtCGSTAmount.setTag2nd("");

        txtIGSTAmount.setText("");
        txtIGSTAmount.setTag("");
        txtIGSTAmount.setTag2nd("");

        txtSGSTUGSTAmount.setText("");
        txtSGSTUGSTAmount.setTag("");
        txtSGSTUGSTAmount.setTag2nd("");

        txtNetAmount.setText("");
        txtNetAmount.setTag("");
        txtNetAmount.setTag2nd("");

        accPartyTypeCode = "";
        accNature = "";
        gstIn = "";
        gstInDate = "";
        stateCode = "";

        lstItemAdjustments = new ArrayList<>();
        txtItemAdjustment.setText("");
        txtItemAdjustment.setTag("");
        txtItemAdjustment.setTag2nd("");
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtItemAdjustment)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_SPACE) {
                showItemAdjustmentPopup();
            }
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            itemAdjustmentPopup.hide();
        }
    }

    @Override
    public void onOk() {
        if (DmShared.iNull(txtHAccountName.getTag()).longValue() == 0) {
            new DmAlertMessageBox("A/c Name is Mandatory", txtHAccountName).show();
            return;
        } else if (DmShared.vNull(txtAmount.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Amount is Mandatory", txtAmount).show();
            return;
        }

        if (DmShared.vNull(txtCGSTAmount.getText()).doubleValue() > 0
                && DmShared.xNull(txtHCgstAccount.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("CGST A/c is mandatory", txtHCgstAccount).show();
            return;
        }
        if (DmShared.vNull(txtIGSTAmount.getText()).doubleValue() > 0
                && DmShared.xNull(txtHIgstAccount.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("IGST A/c is mandatory", txtHIgstAccount).show();
            return;
        }
        if (DmShared.vNull(txtSGSTUGSTAmount.getText()).doubleValue() > 0
                && DmShared.xNull(txtHSgstAccount.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("SGST A/c is mandatory", txtHSgstAccount).show();
            return;
        }
        proceedOk();
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
    public void manageOk(T1 row) {
        GridAcDetailsMdl ourRow = (GridAcDetailsMdl) row;
        ourRow.setHashCode(DmShared.iNull(hashCode).toString());
        ourRow.setvSNo(v_Sno);
        ourRow.setAccMCode(txtHAccountName.getTag2nd());
        ourRow.setAccCode(txtHAccountName.getTag());
        prvSelectedAc = txtHAccountName.getTag();
        ourRow.setAccName(txtHAccountName.getText());
        ourRow.setAccNatureManualCode(accNature);
        ourRow.setAccStateCode(stateCode);
        ourRow.setAccPartyType(accPartyTypeCode);
        ourRow.setAccGstin(gstIn);
        ourRow.setAccGstinDate(gstInDate);
        ourRow.setNarration(txtNarration.getText());

        if (entryTransactionType.equalsIgnoreCase("Debit")) {
            ourRow.setDebit(txtAmount.getText());
        } else if (entryTransactionType.equalsIgnoreCase("Credit")) {
            ourRow.setCredit(txtAmount.getText());
        }

        if (lstItemAdjustments == null) {
            ourRow.getLstItemAdjustments().addAll(new ArrayList<GrdItemAdjustment_Mdl>());
        } else {
            ourRow.setLstItemAdjustments(new ArrayList<GrdItemAdjustment_Mdl>());
            ourRow.getLstItemAdjustments().addAll(lstItemAdjustments);
        }

        ourRow.setAmount(txtAmount.getText());

        calculate();
        ourRow.setSharedKeyFieldName(v_Sno);
        ourRow.setHsnCode(txtHHSN.getTag());
        ourRow.setHsnName(txtHHSN.getText());
        ourRow.setTaxableAmt(txtTaxableAmount.getText());
        ourRow.setGrossAmt(txtGrossAmount.getText());
        ourRow.setNetAmt(txtNetAmount.getText());
        ourRow.setItemGstNature(txtHHSN.getTag2nd());
        ourRow.setHsnSac(txtHSNSAC.getText());
        ourRow.setQty("1");
        ourRow.setRate(txtTaxableAmount.getText());
        ourRow.setCgstPer(txtCGSTTPer.getText());
        ourRow.setCgstAmt(txtCGSTAmount.getText());
        ourRow.setIgstAmt(txtIGSTAmount.getText());
        ourRow.setIgstPer(txtIGSTTPer.getText());
        if (isUT) {
            ourRow.setUgstPer(txtSGSUGSTTPer.getText());
            ourRow.setUgstAmt(txtSGSTUGSTAmount.getText());
        } else {
            ourRow.setSgstPer(txtSGSUGSTTPer.getText());
            ourRow.setSgstAmt(txtSGSTUGSTAmount.getText());
        }

        ourRow.setSgstUgstPer(txtSGSUGSTTPer.getText());
        ourRow.setSgstUgstAmt(txtSGSTUGSTAmount.getText());

        ourRow.setRoundOffGST(roundoffGst);
        ourRow.setIsRCM(isRCM);

        ourRow.setcGstAcNameTag(txtHCgstAccount.getTag());
        ourRow.setcGstAcNameText(txtHCgstAccount.getText());

        ourRow.setsGstAcNameTag(txtHSgstAccount.getTag());
        ourRow.setsGstAcNameText(txtHSgstAccount.getText());

        ourRow.setiGstAcNameTag(txtHIgstAccount.getTag());
        ourRow.setiGstAcNameText(txtHIgstAccount.getText());

        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GridAcDetailsMdl();
    }

    @Override
    public void displayRow(T1 row) {
        clear();
        GridAcDetailsMdl ourRow = (GridAcDetailsMdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHAccountName.setText(DmShared.xNull(ourRow.getAccName()));
        txtHAccountName.setTag(DmShared.xNull(ourRow.getAccCode()));
        txtHAccountName.setTag2nd(DmShared.xNull(ourRow.getAccMCode()));

        txtDebit.setText(DmClient.vNull(ourRow.getDebit(), "0.00"));
        txtCredit.setText(DmClient.vNull(ourRow.getCredit(), "0.00"));
        txtNarration.setText(DmShared.xNull(ourRow.getNarration()));

//        if (entryTransactionType.equalsIgnoreCase("Debit")) {
//            txtAmount.setText(DmClient.vNull(ourRow.getDebit(), "0.00"));
//        } else {
//            txtAmount.setText(DmClient.vNull(ourRow.getCredit(), "0.00"));
//        }
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));

        accPartyTypeCode = ourRow.getAccPartyType();
        accNature = ourRow.getAccNatureManualCode();
        gstIn = ourRow.getAccGstin();
        gstInDate = ourRow.getAccGstinDate();
        stateCode = ourRow.getAccStateCode();
        v_Sno = ourRow.getvSNo();

        lstItemAdjustments = new ArrayList<GrdItemAdjustment_Mdl>();
        lstItemAdjustments.addAll(ourRow.getLstItemAdjustments());
        double totalAmt = 0;
        for (int i = 0; i < lstItemAdjustments.size(); i++) {
            totalAmt += DmShared.vNull(lstItemAdjustments.get(i).getAdjustment());
        }
        txtItemAdjustment.setText(DmClient.vNull(totalAmt, "0.00"));

        v_Sno = ourRow.getSharedKeyFieldName();
        txtHHSN.setTag(ourRow.getHsnCode());
        txtHHSN.setText(ourRow.getHsnName());
        txtHHSN.setTag2nd(ourRow.getItemGstNature());
        txtTaxableAmount.setText(ourRow.getTaxableAmt());
        txtGrossAmount.setText(ourRow.getGrossAmt());
        txtNetAmount.setText(ourRow.getNetAmt());
        qty = ourRow.getQty();
        rate = ourRow.getRate();
        txtHSNSAC.setText(ourRow.getHsnSac());
        txtCGSTTPer.setText(ourRow.getCgstPer());
        txtCGSTAmount.setText(ourRow.getCgstAmt());
        if (isUT) {
            txtSGSTUGSTAmount.setText(ourRow.getUgstAmt());
            txtSGSUGSTTPer.setText(ourRow.getUgstPer());
        } else {
            txtSGSTUGSTAmount.setText(ourRow.getSgstAmt());
            txtSGSUGSTTPer.setText(ourRow.getSgstPer());
        }
        txtIGSTAmount.setText(ourRow.getIgstAmt());
        txtIGSTTPer.setText(ourRow.getIgstPer());
        roundoffGst = ourRow.getRoundOffGST();
        isRCM = ourRow.getIsRCM();

        txtHCgstAccount.setText(DmShared.xNull(ourRow.getcGstAcNameText()));
        txtHCgstAccount.setTag(DmShared.xNull(ourRow.getcGstAcNameTag()));

        txtHSgstAccount.setText(DmShared.xNull(ourRow.getsGstAcNameText()));
        txtHSgstAccount.setTag(DmShared.xNull(ourRow.getsGstAcNameTag()));

        txtHIgstAccount.setText(DmShared.xNull(ourRow.getiGstAcNameText()));
        txtHIgstAccount.setTag(DmShared.xNull(ourRow.getiGstAcNameTag()));
    }

    private void setDisplay() {
        if (isUT) {
            lblSGSTUGST.setText("UGST%");
        } else {
            lblSGSTUGST.setText("SGST%");
        }
    }

    private void calculate() {
        double cgstAmt = 0, sgstAmt = 0, igstAmt = 0, ugstAmt = 0, netAmt = 0, taxableAmt = 0, totalTax = 0;

        taxableAmt = DmShared.vNull(txtTaxableAmount.getText()).doubleValue();
        cgstAmt = GstVoucherEntry.roundOff((taxableAmt * DmShared.vNull(txtCGSTTPer.getText()).doubleValue() * 0.01), roundoffGst);
        igstAmt = GstVoucherEntry.roundOff((taxableAmt * DmShared.vNull(txtIGSTTPer.getText()).doubleValue() * 0.01), roundoffGst);
        if (isUT) {
            ugstAmt = GstVoucherEntry.roundOff((taxableAmt * DmShared.vNull(txtSGSUGSTTPer.getText()).doubleValue() * 0.01), roundoffGst);
        } else {
            sgstAmt = GstVoucherEntry.roundOff((taxableAmt * DmShared.vNull(txtSGSUGSTTPer.getText()).doubleValue() * 0.01), roundoffGst);
        }
        totalTax = cgstAmt + igstAmt + ugstAmt + sgstAmt;
        if (DmShared.iNull(isRCM).intValue() == 1) {
            netAmt = taxableAmt;
        } else {
            netAmt = GstVoucherEntry.roundOff((taxableAmt + totalTax), roundoffGst);
        }

        txtGrossAmount.setText(DmClient.vNull(taxableAmt, "0.00"));
        txtTaxableAmount.setText(DmClient.vNull(taxableAmt, "0.00"));
        txtCGSTAmount.setText(DmClient.vNull(cgstAmt, "0.00"));
        txtSGSTUGSTAmount.setText(DmClient.vNull(GstVoucherEntry.roundOff(sgstAmt + ugstAmt, roundoffGst), "0.00"));
        txtIGSTAmount.setText(DmClient.vNull(igstAmt, "0.00"));
        txtNetAmount.setText(DmClient.vNull(netAmt, "0.00"));
        txtAmount.setText(DmClient.vNull(netAmt, "0.00"));
        txtAmount.setTag(DmClient.vNull(netAmt, "0.00"));
    }

    private void resetDetails() {
        txtCGSTAmount.setText("");
        txtCGSTAmount.setTag("");
        txtCGSTAmount.setTag2nd("");

        txtSGSTUGSTAmount.setText("");
        txtSGSTUGSTAmount.setTag("");
        txtSGSTUGSTAmount.setTag2nd("");

        txtIGSTAmount.setText("");
        txtIGSTAmount.setTag("");
        txtIGSTAmount.setTag2nd("");

        txtCGSTTPer.setText("");
        txtCGSTTPer.setTag("");
        txtCGSTTPer.setTag2nd("");

        txtSGSUGSTTPer.setText("");
        txtSGSUGSTTPer.setTag("");
        txtSGSUGSTTPer.setTag2nd("");

        txtIGSTTPer.setText("");
        txtIGSTTPer.setTag("");
        txtIGSTTPer.setTag2nd("");

        txtTaxableAmount.setText("");
        txtTaxableAmount.setTag("");
        txtTaxableAmount.setTag2nd("");

        txtGrossAmount.setText("");
        txtGrossAmount.setTag("");
        txtGrossAmount.setTag2nd("");

        txtNetAmount.setText("");
        txtNetAmount.setTag("");
        txtNetAmount.setTag2nd("");
    }

    private void manageDisplay() {
        txtHIgstAccount.setEnabled(true);
        txtHCgstAccount.setEnabled(true);
        txtHSgstAccount.setEnabled(true);

        if (!txtIGSTTPer.isEnabled()) {
            txtIGSTTPer.setEnabled(true);
        }
        if (!txtCGSTTPer.isEnabled()) {
            txtCGSTTPer.setEnabled(true);
        }
        if (!txtSGSUGSTTPer.isEnabled()) {
            txtSGSUGSTTPer.setEnabled(true);
        }
        if (!gstVoucherEntry.isIGST) {
            txtIGSTTPer.setEnabled(false);
            txtHIgstAccount.setEnabled(false);
            txtIGSTTPer.setText("");
            txtIGSTTPer.setTag("");
            txtIGSTTPer.setTag2nd("");

            txtIGSTAmount.setText("");
            txtIGSTAmount.setTag("");
            txtIGSTAmount.setTag2nd("");
        } else {
            txtCGSTTPer.setEnabled(false);
            txtSGSUGSTTPer.setEnabled(false);
            txtHCgstAccount.setEnabled(false);
            txtHSgstAccount.setEnabled(false);

            txtCGSTTPer.setText("");
            txtCGSTTPer.setTag("");
            txtCGSTTPer.setTag2nd("");

            txtCGSTAmount.setText("");
            txtCGSTAmount.setTag("");
            txtCGSTAmount.setTag2nd("");

            txtSGSUGSTTPer.setText("");
            txtSGSUGSTTPer.setTag("");
            txtSGSUGSTTPer.setTag2nd("");

            txtSGSTUGSTAmount.setText("");
            txtSGSTUGSTAmount.setTag("");
            txtSGSTUGSTAmount.setTag2nd("");
        }
    }
}
