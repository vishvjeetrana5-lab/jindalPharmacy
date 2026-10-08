package aarogya.master.additionalUserPermission.client;

import aarogya.master.additionalUserPermission.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class ManageUserRoleGridEdit<T1, T2> extends ManageUserRoleGridEdit_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    ServiceInvoker<AdditionalUserPermission_SrvAsync> srvAsync;

    private final UserDetailGridEdit gridUser = new UserDetailGridEdit();

    interface CallBack {

        public void onOk();

        public void onClose();
    }

    CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show() {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHRoleName.setFocus(true);
            }
        });

    }

    private void textHelpEvents() {
        txtHRoleName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRole(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRoleName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRoleName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRoleName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    fillData();
                } else {
                    grdUserDetail.getGrid().getStore().clear();
                }
            }
        });

    }

    private void gridEvents() {

        gridUser.addCallBack(new UserDetailGridEdit.CallBack() {
            @Override
            public void onOk() {
                fillData();

            }

            @Override
            public void onClose() {
                fillData();
            }
        });
        grdUserDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {

                showUser(grdUserDetail.getGrid().getStore().get(event.getRowIndex()));
            }
        });
        grdUserDetail.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE && !event.getNativeEvent().getCtrlKey()) {
                    showUser(grdUserDetail.getGrid().getSelectionModel().getSelectedItem());

                }
            }
        }, KeyDownEvent.getType());
    }

    private void showUser(GrdManageUserRole_Mdl grdManageUserRole_Mdl) {
        gridUser.show(txtHRoleName.getText(), grdManageUserRole_Mdl);
    }

    public void hide() {
        this.popupPanel.hide();
        callBack.onOk();
    }

    @Override
    public void onClick(ClickEvent event) {
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

    public ManageUserRoleGridEdit() {
        super();
        srvAsync = new ServiceInvoker<AdditionalUserPermission_SrvAsync>(GWT.create(AdditionalUserPermission_Srv.class), AdditionalUserPermission.class.getName());
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Manage User Role");
        toolContainer.addWidget(flwClose);
        toolContainer.addGap();
        gridEvents();
        textHelpEvents();
        manageEvents();

    }

    private void fillData() {
        srvAsync.invokeRPC().getFillUserDetail(DmShared.xNull(txtHRoleName.getText()), new AsyncCallback<DmResult<List<GrdManageUserRole_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdManageUserRole_Mdl>> result) {
                if (result.isStatus()) {
                    grdUserDetail.getGrid().getStore().clear();
                    grdUserDetail.getGrid().getStore().addAll(result.getData());
                    grdUserDetail.getGrid().getStore().add(new GrdManageUserRole_Mdl());
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });

    }

    private void manageEvents() {

        btnClose.addClickHandler(this);

    }

}
