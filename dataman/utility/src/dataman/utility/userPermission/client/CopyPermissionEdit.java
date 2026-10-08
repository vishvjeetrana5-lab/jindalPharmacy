package dataman.utility.userPermission.client;

import dataman.utility.userPermission.shared.TxtHUsers_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import java.util.List;
import dataman.utility.userPermission.shared.UserPermission_Srv;
import dataman.utility.userPermission.shared.UserPermission_SrvAsync;

public class CopyPermissionEdit extends CopyPermissionEdit_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    interface CallBack {

        public void onOk(TxtHUsers_Mdl users_Mdl);
    }

    private final ServiceInvoker<UserPermission_SrvAsync> SrvAsync;
    private CallBack callBack;
    private String selectedUserName;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void txtKeyEvents() {
        txtHUserCP.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHUsers_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHUser(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHUsers_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUserCP).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHUsers_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHUserCP.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHUserCP).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHUsers_Mdl tRow) {
            }
        });
    }

    public void hide() {
        popupPanel.hide();
    }

    public void show(Button btn, String selectedUserName) {
        this.selectedUserName = selectedUserName;
        popupPanel.show();
        popupPanel.showRelativeTo(btn);
        popupPanel.setGlassEnabled(true);

        txtHUserCP.setFocus(true);
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

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
            if (DmShared.xNull(txtHUserCP.getText()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Copy Permission is mandatory.", txtHUserCP).show();
            } else {
                new DmConfirmMessageBox("Are you sure? You want to copy permission.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            saveCopyPermission();
                        }
                    }
                }).show();
            }
        }
    }

    protected void saveCopyPermission() {
        SrvAsync.invokeRPC().copyPermission(DmShared.xNull(txtHUserCP.getText()), DmShared.xNull(selectedUserName), new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHUserCP).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    TxtHUsers_Mdl mdl = new TxtHUsers_Mdl();
                    mdl.setSharedKeyFieldCode(DmShared.xNull(txtHUserCP.getTag()));
                    mdl.setSharedKeyFieldName(DmShared.xNull(txtHUserCP.getText()));
                    popupPanel.hide();
                    callBack.onOk(mdl);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHUserCP).show();
                }
            }
        });
    }

    public CopyPermissionEdit() {
        super();
        SrvAsync = new ServiceInvoker<UserPermission_SrvAsync>(GWT.create(UserPermission_Srv.class), UserPermission.class.getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtKeyEvents();
    }
}
