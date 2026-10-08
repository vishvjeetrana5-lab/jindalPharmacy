package aarogya.lock.client;

import aarogya.login.shared.*;
import aarogya.login.client.Login;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.PCSData;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvokerExtended;

public class Lock extends Lock_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    ServiceInvoker<Login_SrvAsync> SrvAsync;

    public void show() {
        SrvAsync.invokeRPC(true).lockUser(new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    popupPanel.show();
                    popupPanel.center();
                    popupPanel.setGlassEnabled(true);
                } else {
                    Window.alert(result.getErrorMessage());
                }
            }
        });
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void validateLogin() {
        Login_Model.Body bTemp;

        try {
            bTemp = initializeValidateLogin();
            Login_Validation.chkLogin(bTemp);

            SrvAsync.invokeRPC(true).validateUser(bTemp, new AsyncCallback<DmResult<PCSData>>() {

                @Override
                public void onFailure(Throwable caught) {
                    txtpass.setFocus(true);
                    Window.alert(caught.getMessage());
                }

                @Override
                public void onSuccess(DmResult<PCSData> result) {
                    if (result.isStatus()) {
                        popupPanel.hide();
                    } else {
                        txtpass.setFocus(true);
                        Window.alert(result.getErrorMessage());
                    }
                }
            });
        } catch (Exception e) {
            Window.alert(e.getMessage());
        }
    }

    private Login_Model.Body initializeValidateLogin() {
        Login_Model.Body mrTemp = new Login_Model.Body();

        mrTemp.setUserName(DmClient.getLoginUserName());
        mrTemp.setPassword(txtpass.getText());

        return mrTemp;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnSwitchUser) {

            if (Window.confirm("Are you sure? You want to switch user.")) {
                DmClient.<SetWidgetInvokerExtended>getStoreItem("swiEvent").reInitialize(DmEnum.PCSEnum.Login);
            };
        } else if (event.getSource() == btnUnlock) {
            validateLogin();

        }

    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            lblUserName.setText(DmClient.getLoginUserName());
            txtpass.setFocus(true);
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    public Lock() {
        super();
        SrvAsync = new ServiceInvoker<Login_SrvAsync>(GWT.create(Login_Srv.class), Login.class.getName());
        popupPanel.addAttachHandler(this);
        btnSwitchUser.addClickHandler(this);
        btnUnlock.addClickHandler(this);
    }

}
