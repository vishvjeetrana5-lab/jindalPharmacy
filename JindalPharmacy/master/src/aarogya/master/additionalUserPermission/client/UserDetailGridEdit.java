package aarogya.master.additionalUserPermission.client;

import aarogya.master.additionalUserPermission.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class UserDetailGridEdit<T1, T2> extends UserDetailGridEdit_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    ServiceInvoker<AdditionalUserPermission_SrvAsync> srvAsync;
    private String roleName = "";

    interface CallBack {

        public void onOk();

        public void onClose();
    }

    CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String role, GrdManageUserRole_Mdl manageUser_Mdl) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        this.roleName = role;
        txtRoleName.setEnabled(false);
        fillData(manageUser_Mdl);

    }

    private void textHelpEvents() {
        txtHUserName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHUser(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUserName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHUserName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHUserName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

    }

    private GrdManageUserRole_Mdl saveInitialize() {
        GrdManageUserRole_Mdl ourRow = new GrdManageUserRole_Mdl();
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHUserName.getTag()));
        ourRow.setRoleName(DmShared.xNull(roleName));

        return ourRow;
    }

    public void updateUserRole(boolean isDelete) {
        GrdManageUserRole_Mdl mrTemp;
        try {
            mrTemp = saveInitialize();
            srvAsync.invokeRPC().updateUser(isDelete, mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHUserName).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        hide();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtHUserName).show();
                    }
                }
            });
        } catch (DmException e) {

        }
    }

    public void hide() {
        this.popupPanel.hide();
        callBack.onOk();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnSave) {
            if (DmShared.xNull(txtHUserName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("User Name is mandatory.", txtHUserName).show();
                return;
            }
            updateUserRole(false);
        } else if (event.getSource() == btnDelete) {
            if (DmShared.xNull(txtHUserName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("User Name is mandatory.", txtHUserName).show();
                return;
            }
            updateUserRole(true);
        }
        if (event.getSource() == btnClose) {
            hide();
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
            callBack.onOk();
            this.hide();
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

    public UserDetailGridEdit() {
        super();
        srvAsync = new ServiceInvoker<AdditionalUserPermission_SrvAsync>(GWT.create(AdditionalUserPermission_Srv.class), AdditionalUserPermission.class.getName());
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Manage User");
        toolContainer.addWidget(flwSave);
        toolContainer.addWidget(flwDelete);
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        textHelpEvents();
        manageEvents();

    }

    private void fillData(GrdManageUserRole_Mdl manageUser_Mdl) {
        txtRoleName.setText(DmShared.xNull(roleName));
        txtRoleName.setTag(DmShared.xNull(roleName));

        txtHUserName.setTag(DmShared.xNull(manageUser_Mdl.getSharedKeyFieldCode()));
        txtHUserName.setText(DmShared.xNull(manageUser_Mdl.getSharedKeyFieldName()));

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHUserName.setFocus(true);
            }
        });
    }

    private void manageEvents() {
        btnSave.addClickHandler(this);
        btnDelete.addClickHandler(this);
        btnClose.addClickHandler(this);

    }

}
