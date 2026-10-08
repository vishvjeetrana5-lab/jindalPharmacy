package redirect.changePassword.client;

import redirect.changePassword.shared.ChangePassword_Mdl;
import redirect.changePassword.shared.ChangePassword_SrvAsync;
import redirect.changePassword.shared.ChangePassword_Srv;
import redirect.changePassword.shared.ChangePassword_Vld;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class ChangePassword extends ChangePassword_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents {

    ServiceInvoker<ChangePassword_SrvAsync> SrvAsync;
    String uid = "";

    public ChangePassword(String username, String uid) {
        super();
        SrvAsync = new ServiceInvoker<ChangePassword_SrvAsync>(GWT.create(ChangePassword_Srv.class), ChangePassword.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(vl.add(btnSave));
        toolContainer.addWidget(vl.add(btnClose));
        FlowPanel flw = new FlowPanel();
        flw.setWidth("710px");
        toolContainer.addWidget(flw);
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
        this.uid = uid;
        filluserDetails(username, uid);
    }

    private void filluserDetails(String username, String uid) {
        try {
            SrvAsync.invokeRPC(true).getUserDetail(username, uid, new AsyncCallback<DmResult<ChangePassword_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<ChangePassword_Mdl> result) {
                    if (result.isStatus()) {
                        txtUserName.setText(result.getData().getUserName());
                        txtUserName.setTag(result.getData().getUserName());
                        txtUserName.setTag2nd(DmShared.xNull(result.getData().getUserNatureCode()));

                        if (DmShared.xNull(result.getData().getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                            redirectToLogin("Your link has been expired");
                        }
                    } else {

                        FlowPanel flwTemp = new FlowPanel();
                        flwTemp.getElement().setAttribute("style", "margin:0px auto;height:100px;width:270px;margin-top:150px;");
                        InlineLabel lbl = new InlineLabel(result.getErrorMessage());
                        lbl.getElement().setAttribute("style", "font-size:15px;font-weight:bold;width:100%;float:left;color:red;");
//                        InlineLabel lblLogin = new InlineLabel("Click here to Login");
//                        lblLogin.getElement().setAttribute("style", " margin-left:25px; cursor:pointer; font-size:15px;font-weight:bold;color:blue;margin-top:10px; text-decoration:underline;");
                        flwForm.clear();
                        flwTemp.add(lbl);
//                        flwTemp.add(lblLogin);
                        flwForm.add(flwTemp);
                        flwForm.removeStyleName("dm-FlwForm");

//                        flwForm.clear();
//                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
////                        redirectToLogin(result.getErrorMessage());
                    }
                }
            });
        } catch (Exception ex) {
        }
    }

    private void redirectToLogin(String msg) {
        FlowPanel flwTemp = new FlowPanel();
        flwTemp.getElement().setAttribute("style", "margin:0px auto;height:100px;width:270px;margin-top:150px;");
        InlineLabel lbl = new InlineLabel(msg);
        lbl.getElement().setAttribute("style", "font-size:15px;font-weight:bold;width:100%;float:left;color:red;");
        InlineLabel lblLogin = new InlineLabel("Click here to Login");
        lblLogin.getElement().setAttribute("style", " margin-left:25px; cursor:pointer; font-size:15px;font-weight:bold;color:blue;margin-top:10px; text-decoration:underline;");
        flwForm.clear();
        flwTemp.add(lbl);
        flwTemp.add(lblLogin);
        flwForm.add(flwTemp);
        flwForm.removeStyleName("dm-FlwForm");
        lblLogin.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (DmShared.xNull(txtUserName.getTag2nd()).equalsIgnoreCase("CT")) {
//                    Window.Location.assign(GWT.getHostPageBaseURL() + "customer.html" + Window.Location.getQueryString());
                    Window.Location.assign(GWT.getHostPageBaseURL() + "welcomeGWT.html" + Window.Location.getQueryString());

                } else if (DmShared.xNull(txtUserName.getTag2nd()).equalsIgnoreCase("EM")) {
                    Window.Location.assign(GWT.getHostPageBaseURL() + "welcomeGWT.html" + Window.Location.getQueryString());

                } else {
                    Window.Location.assign(GWT.getHostPageBaseURL() + "welcomeGWT.html" + Window.Location.getQueryString());

                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private ChangePassword_Mdl topControlSaveInitialize() {
        ChangePassword_Mdl mrTemp = new ChangePassword_Mdl();

        mrTemp.setUserName(DmShared.xNull(txtUserName.getText()));
        mrTemp.setNewPassword(DmShared.xNull(txtNewPassword.getText()));
        mrTemp.setConfirmPasword(DmShared.xNull(txtConfirmPassword.getText()));
        mrTemp.setSharedKeyFieldCode(uid);
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtUserName.getTag()));
        return mrTemp;
    }

    private void topControlSave() {
        ChangePassword_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ChangePassword_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtNewPassword).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        txtConfirmPassword.setText("");
                        txtNewPassword.setText("");
                        txtUserName.setText("");
                        redirectToLogin("Password Changed Successfully.");
                    } else {
                        // topControlSaveError(result.getErrorNumber(), DmShared.xNull(result.getErrorMessage()));
                        redirectToLogin(result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());

        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 200:
                new DmAlertMessageBox(errorMessage, txtNewPassword).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtConfirmPassword).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtConfirmPassword).show();
                break;
            default:
                //new DmAlertMessageBox(errorMessage, txtNewPassword).show();
                redirectToLogin(errorMessage);
                break;
        }

    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            txtNewPassword.setFocus(true);
        } else {
        }
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource().equals(btnSave)) {
                topControlSave();
            }
            if (event.getSource().equals(btnClose)) {
                onClose();
            }
        } catch (DmException e) {
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    redirectToLogin("Visit to Login Page");
                }
            }
        }).show();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

}
