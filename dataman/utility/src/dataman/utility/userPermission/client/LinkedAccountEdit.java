package dataman.utility.userPermission.client;

import dataman.utility.userPermission.shared.TxtHLinkedAccount_Mdl;
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
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import java.util.List;
import dataman.utility.userPermission.shared.UserPermission_Srv;
import dataman.utility.userPermission.shared.UserPermission_SrvAsync;

public class LinkedAccountEdit extends LinkedAccountEdit_Dsg implements IsWidget, ClickHandler, AttachEvent.Handler, DmRootEvents {

    interface CallBack {

        public void onOk(TxtHLinkedAccount_Mdl txtHLinkedAccount);
    }
    private final ServiceInvoker<UserPermission_SrvAsync> SrvAsync;
    private CallBack callBack;

    String selectedUserName, role;

    public void addCallBack(CallBack CallBack) {
        this.callBack = CallBack;
    }

    public void hide() {
        popupPanel.hide();
    }

    public void show(TxtHLinkedAccount_Mdl parentRow, boolean editable, Button btn, String selectedUserName, String role) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        popupPanel.showRelativeTo(btn);

        txtHLinkedAccount.setTag(parentRow.getSharedKeyFieldCode());
        txtHLinkedAccount.setText(parentRow.getSharedKeyFieldName());

        flwOk.clear();
        flwOk.setVisible(false);

        if (editable) {
            flwOk.add(btnOk);
            flwOk.setVisible(true);
        }
        txtHLinkedAccount.setFocus(true);
        this.selectedUserName = selectedUserName;
        this.role = role;

    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.popupPanel.hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            txtHLinkedAccount.setFocus(true);
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void txtKeyEvents() {
        txtHLinkedAccount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHLinkedAccount_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHLinkedAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHLinkedAccount_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLinkedAccount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHLinkedAccount_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHLinkedAccount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHLinkedAccount).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHLinkedAccount_Mdl tRow) {
                if (blnStatus) {

                }
            }
        });
    }

    protected void saveLinkedAccount() {
        SrvAsync.invokeRPC().saveLinkedAccount(DmShared.xNull(this.selectedUserName), DmShared.xNull(txtHLinkedAccount.getTag()), new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHLinkedAccount).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    TxtHLinkedAccount_Mdl mdl = new TxtHLinkedAccount_Mdl();
                    mdl.setSharedKeyFieldCode(DmShared.xNull(txtHLinkedAccount.getTag()));
                    mdl.setSharedKeyFieldName(DmShared.xNull(txtHLinkedAccount.getText()));
                    popupPanel.hide();
                    callBack.onOk(mdl);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
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
            saveLinkedAccount();
        }
    }

    public LinkedAccountEdit() {
        super();
        SrvAsync = new ServiceInvoker<UserPermission_SrvAsync>(GWT.create(UserPermission_Srv.class), UserPermission.class.getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtKeyEvents();
    }
}
