package aarogya.selfToolkit.patientNameEditing.client;

import aarogya.selfToolkit.patientNameEditing.shared.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PatientName extends PatientName_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(PatientInfo_Mdl info_Mdl);

        public void onCancel();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(PatientInfo_Mdl mrTemp, Boolean aliasStatus, Boolean isEditable) {

        btnOk.setVisible(isEditable);
        txtAliasName.setEnabled(isEditable);
        txtLastName.setEnabled(isEditable);
        txtMiddleName.setEnabled(isEditable);
        txtFirstName.setEnabled(isEditable);

        txtAliasName.setEnabled(aliasStatus);

        if (mrTemp != null) {
            txtFirstName.setText(mrTemp.getFirstName());
            txtMiddleName.setText(mrTemp.getMiddleName());
            txtLastName.setText(mrTemp.getLastName());
            txtAliasName.setText(mrTemp.getAliasName());
        }
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtFirstName.setFocus(true);
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            this.callBack.onCancel();
        }
    }

    private void textHelpEvents() {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnOk) {
            if (DmShared.xNull(txtAliasName.getText()).equalsIgnoreCase("") && DmShared.xNull(txtFirstName.getText()).equalsIgnoreCase("") && DmShared.xNull(txtMiddleName.getText()).equalsIgnoreCase("") && DmShared.xNull(txtLastName.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Please give any First, Middle, Last Name ", txtFirstName).show();
                return;
            }
            PatientInfo_Mdl mrTemp = new PatientInfo_Mdl();

            mrTemp.setFirstName(DmShared.xNull(txtFirstName.getText()));
            mrTemp.setLastName(DmShared.xNull(txtLastName.getText()));
            mrTemp.setMiddleName(DmShared.xNull(txtMiddleName.getText()));
            mrTemp.setAliasName(DmShared.xNull(txtAliasName.getText()));

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
    public Widget asWidget() {
        return this.popupPanel;
    }

    public PatientName() {
        super();
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        textHelpEvents();
    }

}
