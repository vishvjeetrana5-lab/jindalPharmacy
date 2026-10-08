package dataman.utility.changePassword.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import dataman.utility.changePassword.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserMenu;

public class ChangePassword extends ChangePassword_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents {

    ServiceInvoker<ChangePassword_SrvAsync> SrvAsync;

    public ChangePassword(UserMenu menuPermission, SetWidgetInvoker swi) {
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
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private ChangePassword_Mdl topControlSaveInitialize() {
        ChangePassword_Mdl mrTemp = new ChangePassword_Mdl();

        mrTemp.setOldPassword(DmShared.xNull(txtOldPassword.getText()));
        mrTemp.setNewPassword(DmShared.xNull(txtNewPassword.getText()));
        mrTemp.setConfirmPasword(DmShared.xNull(txtConfirmPassword.getText()));
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
                    new DmAlertMessageBox(caught.getMessage(), txtOldPassword).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        txtConfirmPassword.setText("");
                        txtNewPassword.setText("");
                        txtOldPassword.setText("");
                        new DmAlertMessageBox("Password Changed Successfully.", txtOldPassword).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), DmShared.xNull(result.getErrorMessage()));
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtOldPassword).show();
                break;
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
                new DmAlertMessageBox(errorMessage, txtOldPassword).show();
                break;
        }

    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    txtOldPassword.setFocus(true);
                }
            });
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
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ChangePassword.this);
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
