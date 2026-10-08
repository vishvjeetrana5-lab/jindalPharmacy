package dataman.dmToolkit.tdsCategoryPopup.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import dataman.dmToolkit.tdsCategoryPopup.shared.*;
import java.util.ArrayList;
import java.util.List;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;

public class TdsCategoryPopup extends TdsCategoryPopup_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler {

    ServiceInvoker<TdsCategoryPopup_SrvAsync> SrvAsync;
    TDSDetailCallBack TDSDetailCallBack;
    boolean editable, isDebit;
    private String voucherCategory = "";
    private String pBillVDate = "";
    private String trnDocId = "";
    private String poDocId = "";

    private LibraryVoucherCategory lvc = new LibraryVoucherCategory();

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtTdsAmountProposed) {
            if (DmShared.vNull(txtTdsOnAmount.getText()).doubleValue() == 0) {
                txtTdsOnAmount.setText(DmClient.vNull(txtTdsAmountProposed.getText(), "0.00"));
            }
        }
    }

    public interface TDSDetailCallBack {

        public void onOk(TdsCategoryPopup_Mdl tDS_Mdl);

        public void setTDS(String tdsAmount);

        public void onClose();
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
            TDSDetailCallBack.onClose();
        }
    }

    public void hide() {
        popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnOk)) {
            onOK();
        } else if (event.getSource().equals(btnCancel)) {
            hide();
            TDSDetailCallBack.onClose();
        }
    }

    public void onOK() {
        TdsCategoryPopup_Mdl tDS_Mdl = new TdsCategoryPopup_Mdl();
        TdsCategoryGrid_Mdl data;
        List<TdsCategoryGrid_Mdl> dataList = new ArrayList();
        double amount;

        try {
            if (!DmClient.isValidText(txtHTdsCategory.getTag(), "TDS Category", txtHTdsCategory)) {
                return;
            }
            if (!DmClient.isValidText(txtHTdsDeduct.getTag(), "TDS Deduct From", txtHTdsDeduct)) {
                return;
            }
            if (!DmClient.isValidAmount(txtTdsOnAmount.getText(), "TDS On Amount", txtTdsOnAmount)) {
                return;
            }
            calculateTotalTds();
            if (DmShared.vNull(txtTdsAmountProposed.getText()).doubleValue() <= 0) {
                txtTdsAmountProposed.setText(txtTdsOnAmount.getText());
            }

            if ((DmShared.vNull(txtTdsAmountProposed.getText()).doubleValue() - DmShared.vNull(lblTotalTds.getText()).doubleValue()) <= 0) {
                new DmAlertMessageBox("Total TDS should not be greater than amount.", (DmTextBox) null).show();
                return;
            }

            if (!DmClient.isValidText(txtHTdsFor.getTag(), "TDS For", txtHTdsFor)) {
                return;
            }
            if (DmShared.xNull(this.voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().BANK_PAYMENT)) {
                if (!DmShared.xNull(txtHTdsFor.getTag()).equalsIgnoreCase("P")) {
                    new DmAlertMessageBox("For this Voucher Entry Tds For should be Payable, First change the tdsFor in master than save Entry.", txtHTdsCategory).show();
                    return;
                }
            } else if (DmShared.xNull(this.voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().BANK_RECEIPT)) {
                if (!DmShared.xNull(txtHTdsFor.getTag()).equalsIgnoreCase("R")) {
                    new DmAlertMessageBox("For this Voucher Entry Tds For should be Receivable, First change the tdsFor in master than save Entry.", txtHTdsCategory).show();
                    return;
                }
            }
            for (int i = 0; i < tdsCategoryGrid.getGrid().getStore().getAll().size(); i++) {
                data = new TdsCategoryGrid_Mdl();
                data.setDescription(DmShared.xNull(tdsCategoryGrid.getGrid().getStore().get(i).getDescription()));
                data.setDescriptionCode(DmShared.xNull(tdsCategoryGrid.getGrid().getStore().get(i).getDescriptionCode()));
                data.setDescriptionMCode(DmShared.xNull(tdsCategoryGrid.getGrid().getStore().get(i).getDescriptionMCode()));
                data.setPostingAccount(DmShared.xNull(tdsCategoryGrid.getGrid().getStore().get(i).getPostingAccount()));
                data.setPostingAccountCode(DmShared.xNull(tdsCategoryGrid.getGrid().getStore().get(i).getPostingAccountCode()));
                data.setPostingManualCode(DmShared.xNull(tdsCategoryGrid.getGrid().getStore().get(i).getPostingManualCode()));
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
            tDS_Mdl.setTdsDeductManualCode(DmShared.xNull(txtHTdsDeduct.getTag2nd()));
            tDS_Mdl.setTdsOnAmount(DmClient.vNull(txtTdsOnAmount.getText(), "0.00"));
            tDS_Mdl.setOrignalAmount(DmClient.vNull(txtTdsAmountProposed.getText(), "0.00"));
            tDS_Mdl.setTdsForTag(DmShared.xNull(txtHTdsFor.getTag()));
            tDS_Mdl.setvCategory(voucherCategory);
            tDS_Mdl.setTdsDetailBind(true);

            if (tDS_Mdl.getTdsForTag().trim().equalsIgnoreCase("R")) {
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
            hide();
            TDSDetailCallBack.onOk(tDS_Mdl);

        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void textHelpEvents() {

        txtHTdsCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTdsCategory(voucherCategory, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
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
                    if (!DmShared.xNull(tRow.getSharedKeyFieldCode()).equalsIgnoreCase("")) {

                        txtHTdsFor.setTag(tRow.getSharedModifiedBy().equalsIgnoreCase("Payable") ? "P" : DmShared.xNull(tRow.getSharedModifiedBy()).equalsIgnoreCase("Receivable") ? "R" : "");
                        txtHTdsFor.setText(tRow.getSharedModifiedBy());
                        bindTdsGrid();
                    }

                }
            }

        });

        txtHTdsDeduct.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHTdsDeduct_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTdsDeduct(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTdsDeduct_Mdl>>>() {
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

            }

        });

        txtHTdsFor.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTdsFor(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
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
                if (blnStatus) {
                    txtHTdsFor.setText(tRow.getSharedKeyFieldName());
                    txtHTdsFor.setTag(tRow.getSharedKeyFieldCode());
                }
                txtHTdsFor.setFocus(true);
            }

        });

        txtTdsOnAmount.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                txtTdsOnAmount.setTag("");
                calculateTotalTds();
            }
        });
    }

    private GetTDSData_Mdl setGetTDSMDL() {
        GetTDSData_Mdl tdsData_Mdl = new GetTDSData_Mdl();
        tdsData_Mdl.setCurrentTDSOnAmt(DmShared.vNull(txtTdsOnAmount.getText()).toString());
        tdsData_Mdl.setOriginalAmt(DmShared.vNull(txtTdsAmountProposed.getText()).toString());
        tdsData_Mdl.setPartyCode(DmShared.xNull(txtHTdsDeduct.getTag()));
        tdsData_Mdl.setTdsCatCode(DmShared.xNull(txtHTdsCategory.getTag()));
        tdsData_Mdl.setTrnDocID(DmShared.xNull(trnDocId));
        tdsData_Mdl.setpBillvDate(DmShared.xNull(pBillVDate));
        tdsData_Mdl.setvCat(DmShared.xNull(voucherCategory));
        tdsData_Mdl.setPoDocID(DmShared.xNull(poDocId));
        return tdsData_Mdl;
    }

    public void bindTdsGrid() {
        SrvAsync.invokeRPC(true).getTDSData(setGetTDSMDL(), new AsyncCallback<DmResult<TdsCategoryPopup_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<TdsCategoryPopup_Mdl> result) {
                if (result.isStatus()) {
                    fillData(result);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        }
        );
    }

    private void fillData(DmResult<TdsCategoryPopup_Mdl> result) {
        txtHTdsCategory.setTag(DmShared.xNull(result.getData().getTdsCategoryCode()));
        txtHTdsCategory.setText(DmShared.xNull(result.getData().getTdsCategoryName()));
        txtHTdsFor.setTag(result.getData().getTdsForTag());
        txtHTdsFor.setText(result.getData().getTdsForText());
        lblClosingBalance.setText(DmClient.vNull(result.getData().getClosingBalance(), "0.00"));
        lblYTDCR.setText(DmClient.vNull(result.getData().getYtdCR(), "0.00"));
        lblYTDDR.setText(DmClient.vNull(result.getData().getYtdDR(), "0.00"));
        lblYTDTDSonAmt.setText(DmClient.vNull(result.getData().getYtdTDSonAmt(), "0.00"));
        lblYTDTurnover.setText(DmClient.vNull(result.getData().getTurnover(), "0.00"));
        txtTdsOnAmount.setText(DmClient.vNull(result.getData().getTdsOnAmount(), "0.00"));
        lblInitialTurnover.setText(DmClient.vNull(result.getData().getInitialTurnoverAmount(), "0.00"));

        tdsCategoryGrid.getGrid().getStore().clear();
        for (int i = 0; i < result.getData().getTdsGridList().size(); i++) {
            TdsCategoryGrid_Mdl data = new TdsCategoryGrid_Mdl();
            data.setDescription(result.getData().getTdsGridList().get(i).getDescription());
            data.setDescriptionCode(result.getData().getTdsGridList().get(i).getDescriptionCode());
            data.setDescriptionMCode(result.getData().getTdsGridList().get(i).getDescriptionMCode());
            data.setPostingAccount(result.getData().getTdsGridList().get(i).getPostingAccount());
            data.setPostingAccountCode(result.getData().getTdsGridList().get(i).getPostingAccountCode());
            data.setPostingManualCode(result.getData().getTdsGridList().get(i).getPostingManualCode());
            data.setPercent(result.getData().getTdsGridList().get(i).getPercent());
            data.setAmount(result.getData().getTdsGridList().get(i).getAmount());
            data.setFormula(result.getData().getTdsGridList().get(i).getFormula());
            data.setRoundOffTDS(result.getData().getTdsGridList().get(i).getRoundOffTDS());
            tdsCategoryGrid.getGrid().getStore().add(data);
        }
        calculateTotalTds();
    }

    public TdsCategoryPopup() {
        super();
        SrvAsync = new ServiceInvoker<TdsCategoryPopup_SrvAsync>(GWT.create(TdsCategoryPopup_Srv.class), TdsCategoryPopup.class.getName());
        textHelpEvents();
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        btnOk.addClickHandler(this);
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtTdsAmountProposed.addBlurHandler(this);
    }

    private void calculateTotalTds() {
        List<TdsCategoryGrid_Mdl> listGrid = tdsCategoryGrid.getGrid().getStore().getAll();
        String orignalFormula = "", tempFormula = "", convertedFormula = "", expenseCode = "", roundOffTDS = "";
        OverHeadLine_Shared.GetWhat getWhat = OverHeadLine_Shared.GetWhat.AMOUNT;
        boolean flag = false, lessTurnover = false;
        double tempValue, totalValue = 0, grossValue;

        grossValue = DmShared.vNull(txtTdsOnAmount.getText());
        lblTotalTds.setText("0.00");
        for (int i = 0; i < listGrid.size(); i++) {
            lessTurnover = true;
            roundOffTDS = DmShared.xNull(listGrid.get(i).getRoundOffTDS());
            if (DmShared.xNull(listGrid.get(i).getFormula()).equalsIgnoreCase("")) {
                orignalFormula = "[G]";
            } else {

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
        TDSDetailCallBack.setTDS(lblTotalTds.getText());
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

    public void addTdsDetailCallBack(TDSDetailCallBack TDSDetailCallBack) {
        this.TDSDetailCallBack = TDSDetailCallBack;

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

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public void autoCalulate() {

    }

    public void show(TdsCategoryPopup_Mdl tdsm, String voucherCategory, boolean isDebit, boolean editable, String mode) {
        if (this.voucherCategory.equalsIgnoreCase(lvc.FINAL_PAYMENT_ENTRY)) {
            txtTdsOnAmount.setReadOnly(false);
            txtTdsOnAmount.setEnabled(true);
        } else {
            txtTdsOnAmount.setReadOnly(true);
            txtTdsOnAmount.setEnabled(false);
        }

        txtHTdsCategory.setEnabled(false);
        if (!DmShared.xNull(txtHTdsDeduct.getTag()).equalsIgnoreCase("") && DmShared.iNull(txtTdsAmountProposed.getText()).longValue() != 0) {
            popupPanel.show();
            popupPanel.center();
            popupPanel.setGlassEnabled(true);
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {

                    txtTdsOnAmount.setFocus(true);

                }
            });

            if (this.voucherCategory.equalsIgnoreCase(lvc.FINAL_PAYMENT_ENTRY)) {
                txtTdsOnAmount.setReadOnly(false);
                txtTdsOnAmount.setEnabled(true);
            } else {
                txtTdsOnAmount.setReadOnly(true);
                txtTdsOnAmount.setEnabled(false);
            }
            return;
        }
        this.voucherCategory = voucherCategory;
        this.editable = editable;
        this.isDebit = isDebit;
        txtHTdsFor.setText("");
        txtHTdsFor.setTag("");
        txtHTdsCategory.setText(DmShared.xNull(tdsm.getTdsCategoryName()));
        txtHTdsCategory.setTag(DmShared.xNull(tdsm.getTdsCategoryCode()));
        txtHTdsDeduct.setText(DmShared.xNull(tdsm.getTdsDeductFormName()));
        txtHTdsDeduct.setTag(DmShared.xNull(tdsm.getTdsDeductFormCode()));
        txtHTdsDeduct.setTag2nd(DmShared.xNull(tdsm.getTdsDeductManualCode()));
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        if (DmShared.xNull(tdsm.getTdsForTag()).equalsIgnoreCase("p") || DmShared.xNull(tdsm.getTdsForTag()).equalsIgnoreCase("R")) {
            txtHTdsFor.setEnabled(false);
        } else {
            txtHTdsFor.setEnabled(true);
        }

        txtHTdsFor.setTag(DmShared.xNull(tdsm.getTdsForTag()));
        txtHTdsFor.setText(DmShared.xNull(tdsm.getTdsForTag()).equalsIgnoreCase("P") ? "Payable" : DmShared.xNull(tdsm.getTdsForTag()).equalsIgnoreCase("R") ? "Receivable" : "");
        txtTdsOnAmount.setText(DmClient.vNull(tdsm.getTdsOnAmount(), "0.00"));
        txtTdsOnAmount.setTag(DmClient.vNull(tdsm.getTdsOnAmount(), "0.00"));
        if (DmShared.vNull(tdsm.getOrignalAmount()).doubleValue() > 0) {
            txtTdsAmountProposed.setText(tdsm.getOrignalAmount());
        } else {
            txtTdsAmountProposed.setText(txtTdsOnAmount.getText());
        }
        lblTotalTds.setText("0.00");

        if (DmShared.xNull(mode).equalsIgnoreCase("New")) {
            bindTdsGrid();
        } else {

            TdsCategoryGrid_Mdl data;
            double totalValue = 0;
            tdsCategoryGrid.getGrid().getStore().clear();
            if (tdsm.getTdsGridList() != null) {
                for (int i = 0; i < tdsm.getTdsGridList().size(); i++) {
                    data = new TdsCategoryGrid_Mdl();
                    data.setDescription(DmShared.xNull(tdsm.getTdsGridList().get(i).getDescription()));
                    data.setDescriptionCode(DmShared.xNull(tdsm.getTdsGridList().get(i).getDescriptionCode()));
                    data.setDescriptionMCode(DmShared.xNull(tdsm.getTdsGridList().get(i).getDescriptionMCode()));
                    data.setPostingAccount(DmShared.xNull(tdsm.getTdsGridList().get(i).getPostingAccount()));
                    data.setPostingAccountCode(DmShared.xNull(tdsm.getTdsGridList().get(i).getPostingAccountCode()));
                    data.setPostingManualCode(DmShared.xNull(tdsm.getTdsGridList().get(i).getPostingManualCode()));
                    data.setPercent(tdsm.getTdsGridList().get(i).getPercent());
                    data.setAmount(tdsm.getTdsGridList().get(i).getAmount());
                    data.setFormula(tdsm.getTdsGridList().get(i).getFormula());
                    data.setRoundOffTDS(tdsm.getTdsGridList().get(i).getRoundOffTDS());
                    tdsCategoryGrid.getGrid().getStore().add(data);
                    totalValue += DmShared.vNull(tdsm.getTdsGridList().get(i).getAmount()).doubleValue();
                }
                lblTotalTds.setText(DmClient.vNull(totalValue, "0.00"));
            }
        }
        setVisibleOk(editable);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {

                txtHTdsCategory.setFocus(true);
            }
        });

    }

    public void setVisibleOk(boolean visible) {
        flwOk.clear();
        flwOk.setVisible(false);

        if (visible) {
            flwOk.add(btnOk);
            flwOk.setVisible(true);
        }
    }

    public void show() {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtHTdsCategory.setEnabled(false);

        if (this.voucherCategory.equalsIgnoreCase(lvc.FINAL_PAYMENT_ENTRY)) {
            txtTdsOnAmount.setReadOnly(false);
            txtTdsOnAmount.setEnabled(true);
        } else {
            txtTdsOnAmount.setReadOnly(true);
            txtTdsOnAmount.setEnabled(false);
        }

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {

                txtTdsOnAmount.setFocus(true);
            }
        });
    }

    public void setPpFields(TdsCategoryPopup_Mdl tdsm) {
        if (tdsm != null) {
            pBillVDate = DmShared.dNull(tdsm.getVoucherDate());
            trnDocId = DmShared.xNull(tdsm.getTrnDocID());
            poDocId = DmShared.xNull(tdsm.getPoDocID());
            txtHTdsDeduct.setTag(DmShared.xNull(tdsm.getTdsDeductFormCode()));
            txtHTdsDeduct.setText(DmShared.xNull(tdsm.getTdsDeductFormName()));
            txtTdsOnAmount.setText(DmClient.vNull(tdsm.getTdsOnAmount(), "0.00"));
            if (DmShared.vNull(tdsm.getTdsOnAmount()).doubleValue() == 0) {
                txtHTdsCategory.setText("");
                txtHTdsCategory.setTag("");
            }
            voucherCategory = DmShared.xNull(tdsm.getvCategory());

            if (DmShared.vNull(tdsm.getOrignalAmount()).doubleValue() > 0) {
                txtTdsAmountProposed.setText(tdsm.getOrignalAmount());
            } else {
                txtTdsAmountProposed.setText(txtTdsOnAmount.getText());
            }
            bindTdsGrid();
        }

    }

    public void clear() {
        txtTdsOnAmount.setText("");
        txtTdsAmountProposed.setText("");
        lblTotalTds.setText("");
        txtHTdsCategory.setTag("");
        txtHTdsCategory.setTag2nd("");
        txtHTdsCategory.setText("");
        txtHTdsDeduct.setTag("");
        txtHTdsDeduct.setTag2nd("");
        txtHTdsDeduct.setText("");
        txtHTdsFor.setTag("");
        txtHTdsFor.setTag2nd("");
        txtHTdsFor.setText("");
        tdsCategoryGrid.getGrid().getStore().clear();
        trnDocId = "";
    }

    public void moveRec(TdsCategoryPopup_Mdl tdsm) {

        double totalValue = 0;
        txtHTdsCategory.setText(DmShared.xNull(tdsm.getTdsCategoryName()));
        txtHTdsCategory.setTag(DmShared.xNull(tdsm.getTdsCategoryCode()));

        txtHTdsDeduct.setText(DmShared.xNull(tdsm.getTdsDeductFormName()));
        txtHTdsDeduct.setTag(DmShared.xNull(tdsm.getTdsDeductFormCode()));
        txtHTdsDeduct.setTag2nd(DmShared.xNull(tdsm.getTdsDeductManualCode()));
        txtHTdsFor.setTag(DmShared.xNull(tdsm.getTdsForTag()));
        txtHTdsFor.setText(DmShared.xNull(tdsm.getTdsForTag()).equalsIgnoreCase("P") ? "Payable" : DmShared.xNull(tdsm.getTdsForTag()).equalsIgnoreCase("R") ? "Receivable" : "");
        txtTdsOnAmount.setText(DmClient.vNull(tdsm.getTdsOnAmount(), "0.00"));
        txtTdsAmountProposed.setText(tdsm.getOrignalAmount());
        tdsCategoryGrid.getGrid().getStore().addAll(tdsm.getTdsGridList());
        for (TdsCategoryGrid_Mdl mdl : tdsm.getTdsGridList()) {
            totalValue += DmShared.vNull(mdl.getAmount()).doubleValue();
        }
        lblTotalTds.setText(DmClient.vNull(totalValue, "0.00"));
        pBillVDate = DmShared.dNull(tdsm.getVoucherDate());
        TDSDetailCallBack.setTDS(lblTotalTds.getText());

        lblClosingBalance.setText(DmClient.vNull(tdsm.getClosingBalance(), "0.00"));
        lblYTDCR.setText(DmClient.vNull(tdsm.getYtdCR(), "0.00"));
        lblYTDDR.setText(DmClient.vNull(tdsm.getYtdDR(), "0.00"));
        lblYTDTDSonAmt.setText(DmClient.vNull(tdsm.getYtdTDSonAmt(), "0.00"));
        lblYTDTurnover.setText(DmClient.vNull(tdsm.getTurnover(), "0.00"));

    }
}
