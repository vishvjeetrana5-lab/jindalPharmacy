package dataman.accounts.voucherEntry.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.accounts.voucherEntry.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class ChequeDetail extends ChequeDetail_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    ServiceInvoker<VoucherEntry_SrvAsync> SrvAsync;

    interface ChequeDetailCallBack {

        public void onOk(ChequeDetail_Mdl chequeDetail_Mdl);

        public void onClose();
    }

    ChequeDetailCallBack chequeDetailCallBack;

    public void addChequeDetailCallBack(ChequeDetailCallBack chequeDetailCallBack) {
        this.chequeDetailCallBack = chequeDetailCallBack;
    }

    public void show(ChequeDetail_Mdl chequeDetail_Mdl, boolean editable) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        clear();

        txtChequeNo.setText(DmShared.xNull(chequeDetail_Mdl.getChequeNo()));
        txtChequeDate.setText(DmShared.xNull(chequeDetail_Mdl.getChequeDate()));
        txtHIsCrossCheque.setTag(DmShared.xNull(chequeDetail_Mdl.getIsCrossChequeCode()));
        txtHIsCrossCheque.setText(DmShared.xNull(chequeDetail_Mdl.getIsCrossChequeText()));
        txtFavourOF.setText(DmShared.xNull(chequeDetail_Mdl.getFavourOf()));
        txtClearDate.setText(DmShared.xNull(chequeDetail_Mdl.getClearDate()));
        txtRemark.setText(DmShared.xNull(chequeDetail_Mdl.getClearRemark()));
        txtHAccountNo.setTag(DmShared.iNull(chequeDetail_Mdl.getAccountCode()).toString());
        txtHAccountNo.setText(DmShared.xNull(chequeDetail_Mdl.getAccountNo()));
        txtBranchAddress.setText(DmShared.xNull(chequeDetail_Mdl.getBranchAddress()));
        txtBank.setText(DmShared.xNull(chequeDetail_Mdl.getBank()));
        txtBranch.setText(DmShared.xNull(chequeDetail_Mdl.getBranch()));
        txtSwiftNo.setText(DmShared.xNull(chequeDetail_Mdl.getSwiftCode()));
        txtIFSCCode.setText(DmShared.xNull(chequeDetail_Mdl.getIfscCode()));
        flwOk.clear();
        flwOk.setVisible(false);

        if (editable) {

            flwOk.add(btnOk);
            flwOk.setVisible(true);

        }

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtChequeNo.setFocus(true);
            }
        });

    }

    private void textHelpEvents() {
        txtHIsCrossCheque.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            }
        });
        txtHAccountNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAccountNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountNo(txtHAccountNo.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHAccountNo_Mdl>>>() {

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
                    txtSwiftNo.setText(DmShared.xNull(tRow.getSwiftNo()));
                    txtIFSCCode.setText(DmShared.xNull(tRow.getIfscCode()));
                    txtBranchAddress.setText(DmShared.xNull(tRow.getBankAddress()));

                } else {
                    txtBank.setText("");
                    txtBranch.setText("");
                    txtSwiftNo.setText("");
                    txtIFSCCode.setText("");
                    txtBranchAddress.setText("");

                }
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide();
            chequeDetailCallBack.onClose();
        } else if (event.getSource() == btnOk) {
            ChequeDetail_Mdl chequeDetail_Mdl = new ChequeDetail_Mdl();
            if (!txtChequeDate.getText().trim().equalsIgnoreCase("") && !txtClearDate.getText().trim().equalsIgnoreCase("")) {
                if (!(DmShared.convertDate(txtChequeDate.getText()).before(DmShared.convertDate(txtClearDate.getText()))
                        || DmShared.convertDate(txtChequeDate.getText()).equals(DmShared.convertDate(txtClearDate.getText())))) {
                    new DmAlertMessageBox("Clear date should be greater than or equal to cheque date.", (DmTextBox) null).show();
                    return;
                }
            }

            if (DmShared.xNull(txtHIsCrossCheque.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Cross Cheque is mandatory", txtHIsCrossCheque).show();
                return;
            }

            chequeDetail_Mdl.setChequeNo(txtChequeNo.getText());
            chequeDetail_Mdl.setChequeDate(txtChequeDate.getText());
            chequeDetail_Mdl.setFavourOf(txtFavourOF.getText());
            chequeDetail_Mdl.setClearDate(txtClearDate.getText());
            chequeDetail_Mdl.setClearRemark(txtRemark.getText());
            chequeDetail_Mdl.setAccountNo(txtHAccountNo.getText());
            chequeDetail_Mdl.setBank(DmShared.xNull(txtBank.getText()));
            chequeDetail_Mdl.setBranchAddress(DmShared.xNull(txtBranchAddress.getText()));
            chequeDetail_Mdl.setIfscCode(DmShared.xNull(txtIFSCCode.getText()));
            chequeDetail_Mdl.setSwiftCode(DmShared.xNull(txtSwiftNo.getText()));
            chequeDetail_Mdl.setBranch(DmShared.xNull(txtBranch.getText()));
            chequeDetail_Mdl.setIsCrossChequeCode(DmShared.xNull(txtHIsCrossCheque.getTag()));
            chequeDetail_Mdl.setIsCrossChequeText(DmShared.xNull(txtHIsCrossCheque.getText()));
            chequeDetail_Mdl.setChequeDetailBind(true);

            hide();
            chequeDetailCallBack.onOk(chequeDetail_Mdl);
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            txtChequeNo.setFocus(true);
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
            chequeDetailCallBack.onClose();
        }
    }

    public void hide() {
        this.popupPanel.hide();
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

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public ChequeDetail() {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(VoucherEntry_Srv.class), VoucherEntry.class.getName());
        txtKeyEvents();
        textHelpEvents();
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        popupPanel.addAttachHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    private void clear() {
        txtBank.setEnabled(false);
        txtBranch.setEnabled(false);
        txtIFSCCode.setEnabled(false);
        txtSwiftNo.setEnabled(false);
        txtBranchAddress.setEnabled(false);
        txtChequeNo.setText("");
        txtChequeDate.setText("");
        txtFavourOF.setText("");
        txtClearDate.setText("");
        txtRemark.setText("");
        txtHAccountNo.setText("");
        txtHAccountNo.setTag("");
        txtBank.setText("");
        txtBranch.setText("");
        txtIFSCCode.setText("");
        txtSwiftNo.setText("");
        txtBranchAddress.setText("");

    }
}
