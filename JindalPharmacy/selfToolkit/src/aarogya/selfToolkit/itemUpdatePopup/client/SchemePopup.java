package aarogya.selfToolkit.itemUpdatePopup.client;

import aarogya.selfToolkit.itemUpdatePopup.shared.SchemePopup_Mdl;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class SchemePopup extends SchemePopup_Dsg implements IsWidget, ClickHandler, BlurHandler, DmRootEvents, AttachEvent.Handler {

    //<editor-fold defaultstate="collapsed" desc="CallBack">
    public interface CallBack {

        public void ok(SchemePopup_Mdl spMdl);

        public void onClose();

    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {

        this.callBack = callBack;
    }
    //</editor-fold>

    private String unitDecimalFormat;

    public SchemePopup() {
        super();
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(vl.add(btnOk));
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        manageEvents();
        manageDisplay();
    }

    public void show(SchemePopup_Mdl spMdl, String unitDecimalPlaces) {
        this.unitDecimalFormat = unitDecimalPlaces;
        txtCurrentQty.setText(DmShared.xNull(spMdl.getCurrentQty()));
        txtCurrentFreeQty.setText(DmShared.xNull(spMdl.getCurrentFreeQty()));
        txtCurrentSchemeOnQty.setText(DmShared.xNull(spMdl.getCurrentSchemeOnQty()));
        txtCurrentSchemeFreeQty.setText(DmShared.xNull(spMdl.getCurrentSchemeFreeQty()));
        txtMasterSchemeOnQty.setText(DmShared.xNull(spMdl.getMasterSchemeOnQty()));
        txtMasterSchemeFreeQty.setText(DmShared.xNull(spMdl.getMasterSchemeFreeQty()));
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtCurrentSchemeOnQty.setFocus(true);
    }

    public void hide(boolean isCallBackRequired) {
        this.popupPanel.hide();
        if (isCallBackRequired) {
            callBack.onClose();
        }
    }

    public void hide() {
        hide(true);

    }

    private void onOk() {

        if (DmShared.vNull(txtCurrentSchemeFreeQty.getText()).doubleValue() != 0
                && DmShared.vNull(txtCurrentSchemeOnQty.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Please fill Scheme On quantity.", txtCurrentSchemeOnQty).show();
            return;
        }

        if (DmShared.vNull(txtCurrentSchemeOnQty.getText()).doubleValue() != 0
                && DmShared.vNull(txtCurrentSchemeFreeQty.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Please fill Scheme Free quantity.", txtCurrentSchemeFreeQty).show();
            return;
        }

        SchemePopup_Mdl spMdl = new SchemePopup_Mdl();
        spMdl.setCurrentQty(DmShared.xNull(txtCurrentQty.getText()));
        spMdl.setCurrentFreeQty(DmShared.xNull(txtCurrentFreeQty.getText()));
        spMdl.setCurrentSchemeOnQty(DmShared.xNull(txtCurrentSchemeOnQty.getText()));
        spMdl.setCurrentSchemeFreeQty(DmShared.xNull(txtCurrentSchemeFreeQty.getText()));
        spMdl.setMasterSchemeOnQty(DmShared.xNull(txtMasterSchemeOnQty.getText()));
        spMdl.setMasterSchemeFreeQty(DmShared.xNull(txtMasterSchemeFreeQty.getText()));
        callBack.ok(spMdl);
        hide(false);
    }

    private void manageDisplay() {
        txtCurrentQty.setEnabled(false);
        txtCurrentFreeQty.setEnabled(false);
        txtMasterSchemeOnQty.setEnabled(false);
        txtMasterSchemeFreeQty.setEnabled(false);
    }

    private void manageEvents() {
        btnOk.addClickHandler(this);
        btnClose.addClickHandler(this);
        txtCurrentSchemeOnQty.addBlurHandler(this);
        txtCurrentSchemeFreeQty.addBlurHandler(this);
        popupPanel.addAttachHandler(this);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnOk)) {
            onOk();
        } else if (event.getSource().equals(btnClose)) {
            hide();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtCurrentSchemeOnQty)) {
            txtCurrentSchemeOnQty.setText(DmClient.vNull(txtCurrentSchemeOnQty.getText(), unitDecimalFormat));
        } else if (event.getSource().equals(txtCurrentSchemeFreeQty)) {
            txtCurrentSchemeFreeQty.setText(DmClient.vNull(txtCurrentSchemeFreeQty.getText(), unitDecimalFormat));
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
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }
}
