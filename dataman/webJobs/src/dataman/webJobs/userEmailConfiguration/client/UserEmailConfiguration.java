package dataman.webJobs.userEmailConfiguration.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.webJobs.userEmailConfiguration.shared.TxtHUsers_Mdl;
import dataman.webJobs.userEmailConfiguration.shared.UserEmailConfiguration_SrvAsync;
import dataman.webJobs.userEmailConfiguration.shared.UserEmailConfiguration_Srv;
import dataman.webJobs.userEmailConfiguration.shared.GrdUserEmaiEdit_Mdl;
import dataman.webJobs.userEmailConfiguration.shared.DefaultValue_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class UserEmailConfiguration extends UserEmailConfiguration_Dsg implements DmRootEvents, AttachEvent.Handler {

    ServiceInvoker<UserEmailConfiguration_SrvAsync> SrvAsync;
    UserEmailGridEdit<GrdUserEmaiEdit_Mdl, Grd_PrpAcs> grdEmailEdit = new UserEmailGridEdit(grdEmail);
    DefaultValue_Mdl defaultValue;
    private boolean isEditable = false;

    private void textHelpEvents() {
        txtHUser.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHUsers_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHUser(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHUsers_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUser).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHUsers_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHUser.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHUser).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHUsers_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    fillRecords(DmShared.xNull(t1Row.getSharedKeyFieldCode()));
                }
            }

        });
    }

    private void fillRecords(String userName) {
        SrvAsync.invokeRPC().fillGrid(userName, new AsyncCallback<DmResult<List<GrdUserEmaiEdit_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdUserEmaiEdit_Mdl>> result) {
                if (result.isStatus()) {
                    grdEmail.getGrid().getStore().clear();
                    grdEmail.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHUser).show();
                }
            }
        });

    }

    private void gridEdit() {
        grdEmail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showUserEmailGridEdit(event.getRowIndex());
            }
        });
        grdEmailEdit.addCallBack(new UserEmailGridEdit.CallBack() {

            @Override
            public void onOk(GrdUserEmaiEdit_Mdl grdEmailEdit) {
                grdEmail.commitChanges();
            }

            @Override
            public void onDelete() {

            }
        });
        grdEmail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showUserEmailGridEdit(event.getRowIndex());
            }
        });
        grdEmail.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showUserEmailGridEdit(grdEmail.getGrid().getStore().indexOf(grdEmail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showUserEmailGridEdit(int currentRowIndex) {
        grdEmailEdit.show(currentRowIndex, true, true, true, this);
    }

    public UserEmailConfiguration(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<UserEmailConfiguration_SrvAsync>(GWT.create(UserEmailConfiguration_Srv.class), UserEmailConfiguration.class.getName());
        this.asWidget().setTitle(widgetName);
        buttonClick();
        textHelpEvents();
        gridEdit();
        getPermission(menuPermission);
        iniDefaultValues(swi);
        flwForm.addAttachHandler(this);
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    txtHUser.setTag(defaultValue.getUserName());
                    txtHUser.setText(defaultValue.getUserName());
                    fillRecords(defaultValue.getUserName());
                    txtHUser.setEnabled(isEditable);
                    swi.setWidget(UserEmailConfiguration.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void getPermission(UserMenu menuPermission) {
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(userEditableBlog)) {
                isEditable = true;
            }
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    public void clear() {
        grdEmail.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    private void buttonClick() {
        btnClose.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(UserEmailConfiguration.this);
                        }
                    }
                }).show();

            }
        });
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(UserEmailConfiguration.this);
                    }
                }
            }).show();

        }
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }
}
