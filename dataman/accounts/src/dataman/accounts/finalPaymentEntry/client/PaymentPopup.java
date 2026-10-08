package dataman.accounts.finalPaymentEntry.client;

import dataman.accounts.finalPaymentEntry.shared.GrdPayment_Mdl;
import dataman.accounts.finalPaymentEntry.shared.PaymentPopUp_Mdl;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;

public class PaymentPopup extends PaymentPopup_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, KeyUpHandler, ClickHandler {

    public interface CallBack {

        public void onOk(PaymentPopUp_Mdl popUp_Md);

        public void onClose();
    }

    private final PaymentPopupGridEdit gridEditPayment = new PaymentPopupGridEdit(gridPayment);
    private CallBack callBack;
    protected FinalPaymentEntry finPayEntry;
    private PaymentPopUp_Mdl paymnetPopupMdl;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(FinalPaymentEntry advanceEntry, PaymentPopUp_Mdl paymnetPopupMdl) {
        this.finPayEntry = advanceEntry;
        this.paymnetPopupMdl = paymnetPopupMdl;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        clear();
        flwOk.clear();
        flwCancel.clear();
        if (!advanceEntry.topControl.getMode().equalsIgnoreCase("Browse")) {
            flwOk.add(btnOk);

        }
        flwCancel.add(btnCancel);
        getPaymentGridPopUpDetail();
    }

    private void getPaymentGridPopUpDetail() {
        gridPayment.getGrid().getStore().clear();
        gridPayment.getGrid().getStore().addAll(paymnetPopupMdl.getLstPaymentMdl());
        if (!finPayEntry.topControl.getMode().equalsIgnoreCase("Browse")) {
            gridPayment.getGrid().getStore().add(new GrdPayment_Mdl());
        }

    }

    private void clear() {
        gridPayment.getGrid().getStore().clear();
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            gridEditPayment.hide();
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
    public void onKeyUp(KeyUpEvent event) {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnOk) {
            PaymentPopUp_Mdl paymnetPopupMdl1 = new PaymentPopUp_Mdl();
            paymnetPopupMdl1.getLstPaymentMdl().clear();
            paymnetPopupMdl1.getLstPaymentMdl().addAll(gridPayment.getGrid().getStore().getAll());
            paymnetPopupMdl = paymnetPopupMdl1;
            hide(false);
            callBack.onOk(paymnetPopupMdl);
        }
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            callBack.onClose();
        }
    }

    private void gridEvents() {
        gridEditPayment.addCallBack(new PaymentPopupGridEdit.CallBack() {
            @Override
            public void onOk(GrdPayment_Mdl grdPayment) {

                gridPayment.commitChanges();
            }

            @Override
            public void onDelete() {
                gridPayment.commitChanges();
            }
        });
        gridPayment.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showGridEdit(event.getRowIndex());
            }
        });
        gridPayment.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showGridEdit(gridPayment.getGrid().getStore().indexOf(gridPayment.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showGridEdit(int rowIndex) {

        if (finPayEntry.topControl.getMode().equalsIgnoreCase("Browse")) {
            gridEditPayment.show(rowIndex, false, false, false, this);
        } else {
            gridEditPayment.show(rowIndex, true, true, true, this);

        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public PaymentPopup() {
        super();
        gridEvents();

        popupPanel.addAttachHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);

    }

}
