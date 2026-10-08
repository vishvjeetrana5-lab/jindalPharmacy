package aarogya.selfToolkit.pharmacyDiscountLineWise.client;

import aarogya.selfToolkit.pharmacyDiscountLineWise.shared.*;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;

public class PharmacyDiscountLineWise extends PharmacyDiscountLineWise_Dsg implements IsWidget, ClickHandler, BlurHandler, DmRootEvents, AttachEvent.Handler {

    double totalGrossAmount = 0d;

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtDiscountPer && (!txtDiscountPer.getText().equalsIgnoreCase(""))) {
            txtDiscount.setText("");
        } else if (event.getSource() == txtDiscount && (!txtDiscount.getText().equalsIgnoreCase(""))) {
            txtDiscountPer.setText("");
        }

    }

    public interface CallBack {

        public void onOk(PharmacyDiscountLineWise_Mdl pharmacyDiscountLineWise_Mdl);

        public void onClear();

        public void onCancel();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(PharmacyDiscountLineWise_Mdl mrTemp, String voucherDate, String vType) {
        if (mrTemp != null) {

            txtDiscount.setText(DmShared.xNull(mrTemp.getDiscount()));
            txtDiscountPer.setText(DmShared.xNull(mrTemp.getDiscountPer()));

            this.totalGrossAmount = DmShared.vNull(mrTemp.getTotalGrossAmount());
        }

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtDiscount.setFocus(true);
            }
        });

    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            callBack.onCancel();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
            hide(true);
        } else if (event.getSource().equals(btnOk)) {
            if ((DmShared.vNull(txtDiscount.getText()).doubleValue() == 0) && (DmShared.vNull(txtDiscountPer.getText()).doubleValue() == 0)) {
                new DmAlertMessageBox("Discount is mandatory.", txtDiscountPer).show();
                return;
            }

            if (DmShared.vNull(txtDiscount.getText()) > totalGrossAmount) {
                new DmAlertMessageBox("Discount Amount should be less than or equal to " + DmClient.vNull(totalGrossAmount, "0.00"), txtDiscount).show();
            }
            if (DmShared.vNull(txtDiscountPer.getText()) > 100) {
                new DmAlertMessageBox("Discount Percent should be less than or equal to " + DmClient.vNull("100", "0.00"), txtDiscount).show();
            }

            PharmacyDiscountLineWise_Mdl mrTemp = new PharmacyDiscountLineWise_Mdl();

            mrTemp.setDiscount(DmClient.vNull(txtDiscount.getText(), "0.00"));
            mrTemp.setDiscountPer(DmClient.vNull(txtDiscountPer.getText(), "0.00"));
            mrTemp.setTotalGrossAmount(DmClient.vNull(totalGrossAmount, "0.00"));

            if ((DmShared.vNull(txtDiscountPer.getText()) > 0)) {
                mrTemp.setDiscount("");
            }
            callBack.onOk(mrTemp);
            hide(false);
        } else if (event.getSource().equals(btnClear)) {
            new DmConfirmMessageBox("Are you sure to clear all discount?", new DialogHideEvent.DialogHideHandler() {
                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        hide(false);
                        callBack.onClear();
                    }
                }
            }).show();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        }
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
    public Widget asWidget() {
        return this.popupPanel;
    }

    public PharmacyDiscountLineWise(boolean isOpenedFromPurchase) {
        super(isOpenedFromPurchase);

        btnClear.addClickHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);

        txtDiscountPer.addBlurHandler(this);
        txtDiscount.addBlurHandler(this);
    }

}
