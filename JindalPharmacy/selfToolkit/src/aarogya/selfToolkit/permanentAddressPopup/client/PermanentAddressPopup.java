package aarogya.selfToolkit.permanentAddressPopup.client;

import aarogya.selfToolkit.permanentAddressPopup.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class PermanentAddressPopup extends PermanentAddressPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(PermanentAddressPopup_Mdl addressPopup_Mdl);
    }
    CallBack callBack;
    ServiceInvoker<PermanentAddressPopup_SrvAsync> srvAsync;
    PermanentAddressPopup_Mdl permanentAddress_Mdl = null, copyAddress = null;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(PermanentAddressPopup_Mdl pam, PermanentAddressPopup_Mdl ca) {

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        copyAddress = ca;
        fillData(pam);
        txtAddress1.setFocus(true);
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public PermanentAddressPopup() {
        super();
        srvAsync = new ServiceInvoker<PermanentAddressPopup_SrvAsync>(GWT.create(PermanentAddressPopup_Srv.class), PermanentAddressPopup.class.getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        btnCopy.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        textHelpEvents();
    }

    private void textHelpEvents() {

        txtHCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHCity.setTag2nd(DmShared.xNull(t1Row.getCountryCode()));
                } else {
                    txtHCity.setTag2nd("");
                }
            }
        });

        txtHArea.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHArea(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHArea).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHArea.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHArea).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
            if (DmShared.xNull(txtHCity.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("City is mandatory.", txtHCity).show();
                return;
            }
            copyData();
            callBack.onOk(permanentAddress_Mdl);
            popupPanel.hide();
        } else if (event.getSource() == btnCopy) {
            fillData(copyAddress);
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

    private void fillData(PermanentAddressPopup_Mdl permntAddress) {
        if (permntAddress != null) {
            txtAddress1.setText(DmShared.xNull(permntAddress.getPermanentAddress1()));
            txtAddress2.setText(DmShared.xNull(permntAddress.getPermanentAddress2()));
            txtHCity.setText(DmShared.xNull(permntAddress.getPermanentCity()));
            txtHCity.setTag(DmShared.iNull(permntAddress.getPermanentCityCode()).toString());
            txtHArea.setText(DmShared.xNull(permntAddress.getPermanentZone()));
            txtHArea.setTag(DmShared.iNull(permntAddress.getPermanentZoneCode()).toString());
            txtPINCode.setText(DmShared.xNull(permntAddress.getPermanentPinCode()));
        }

    }

    private void copyData() {
        permanentAddress_Mdl = new PermanentAddressPopup_Mdl();
        permanentAddress_Mdl.setPermanentAddress1(DmShared.xNull(txtAddress1.getText()));
        permanentAddress_Mdl.setPermanentAddress2(DmShared.xNull(txtAddress2.getText()));
        permanentAddress_Mdl.setPermanentCity(DmShared.xNull(txtHCity.getText()));
        permanentAddress_Mdl.setPermanentCityCode(DmShared.iNull(txtHCity.getTag()).toString());
        permanentAddress_Mdl.setPermanentZone(DmShared.xNull(txtHArea.getText()));
        permanentAddress_Mdl.setPermanentZoneCode(DmShared.iNull(txtHArea.getTag()).toString());
        permanentAddress_Mdl.setPermanentPinCode(DmShared.xNull(txtPINCode.getText()));
    }
}
