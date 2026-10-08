package dataman.utility.userMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import dataman.utility.userMaster.shared.*;
import java.util.List;

public class ResetLink extends ResetLink_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    interface CallBack {

        public void resetLink(String userName, String eMail);

        public void close();
    }

    private CallBack callBack;
    private String eMail = "";
    private final ServiceInvoker<UserMaster_SrvAsync> srvAsync;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(Widget wdg, String userName) {
        eMail = "";
        popupPanel.show();
        popupPanel.showRelativeTo(wdg);
        popupPanel.setGlassEnabled(true);
        txtHUser.setText(userName);
        txtHUser.setTag(userName);
        txtHUser.setFocus(true);
        txtHUser.setEnabled(DmClient.getLoginUserName().equalsIgnoreCase("SA"));
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            callBack.close();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            txtHUser.setFocus(true);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnOk) {

            if (DmShared.xNull(txtHUser.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("User Name is mandatory.", txtHUser).show();
                return;
            }
            callBack.resetLink(DmShared.xNull(txtHUser.getText()), eMail);
            hide(false);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide(true);
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public ResetLink() {
        super();
        srvAsync = new ServiceInvoker<UserMaster_SrvAsync>(GWT.create(UserMaster_Srv.class), UserMaster.class.getName());
        handlerEvents();
    }

    private void handlerEvents() {
        popupPanel.addAttachHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        txtHUser.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRole(helper, blnToEndOfFile, true, "", new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUser).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHUser.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHUser).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    eMail = t1Row.getSharedModifiedBy();
                }
            }
        });
    }
}
