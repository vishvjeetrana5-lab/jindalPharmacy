package aarogya.selfToolkit.passportInfo.client;

import aarogya.selfToolkit.passportInfo.shared.PassportInfo_Mdl;
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
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;

public class PassportInfo extends PassportInfo_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    public interface CallBack {

        public void onOk(PassportInfo_Mdl info_Mdl);

        public void onCancel();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(PassportInfo_Mdl mrTemp, Boolean isEditable) {
        if (isEditable) {
            btnOk.setVisible(false);
            txtVisaNo.setEnabled(false);
            txtVisaNoExpiry.setEnabled(false);
            txtPassportNo.setEnabled(false);
            txtPassportNoExpiry.setEnabled(false);
        } else {
            btnOk.setVisible(true);
            txtVisaNo.setEnabled(true);
            txtVisaNoExpiry.setEnabled(true);
            txtPassportNo.setEnabled(true);
            txtPassportNoExpiry.setEnabled(true);
        }

        if (mrTemp != null) {
            txtPassportNo.setText(DmShared.xNull(mrTemp.getPassportNo()));
            txtPassportNoExpiry.setText(DmShared.dNull(mrTemp.getPassportExpiryDate()));
            txtVisaNo.setText(DmShared.xNull(mrTemp.getVisaNo()));
            txtVisaNoExpiry.setText(DmShared.dNull(mrTemp.getVisaExpiryDate()));
        }
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtPassportNo.setFocus(true);
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

    private void textHelpEvents() {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnOk) {
            PassportInfo_Mdl mrTemp = new PassportInfo_Mdl();

            mrTemp.setPassportNo(DmShared.xNull(txtPassportNo.getText()).trim());
            mrTemp.setPassportExpiryDate(DmShared.dNull(txtPassportNoExpiry.getText()).trim());
            mrTemp.setVisaNo(DmShared.xNull(txtVisaNo.getText()).trim());
            mrTemp.setVisaExpiryDate(DmShared.dNull(txtVisaNoExpiry.getText()));

            callBack.onOk(mrTemp);
            hide(false);
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
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtPassportNoExpiry)) {
            txtPassportNoExpiry.setText(DmClient.returnDate(txtPassportNoExpiry.getText()));
        }
        if (event.getSource().equals(txtVisaNoExpiry)) {
            txtVisaNoExpiry.setText(DmClient.returnDate(txtVisaNoExpiry.getText()));
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public PassportInfo() {
        super();
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        textHelpEvents();
        txtPassportNoExpiry.addBlurHandler(this);
        txtVisaNoExpiry.addBlurHandler(this);
    }

}
