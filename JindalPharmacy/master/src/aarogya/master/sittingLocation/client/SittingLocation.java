package aarogya.master.sittingLocation.client;

import aarogya.master.sittingLocation.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public final class SittingLocation extends SittingLocation_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents, KeyPressHandler {

    private DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
    private final ServiceInvoker<SittingLocation_SrvAsync> srvAsync;

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource().equals(btnSave)) {
                new DmConfirmMessageBox("Are you sure? You want to save.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                            topControlSave();
                        }
                    }
                }).show();
            }
            if (event.getSource().equals(btnClose)) {
                onClose();
            }
        } catch (DmException e) {
        }
    }

    private void textHelpEvents() {
        txtHLocation.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHLocation(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLocation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLocation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLocation).show();
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
    public Widget asWidget() {
        return flwForm;
    }

    public SittingLocation(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(SittingLocation_Srv.class), SittingLocation.class.getName());
        this.asWidget().setTitle(widgetName);
        textHelpEvents();
        handleEvent();
        manageDisplay();
        swi.setWidget(this);
    }

    private void handleEvent() {
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtDeviceBlock.addKeyPressHandler(this);
        txtDeviceCode.addKeyPressHandler(this);
    }

    private SittingLocation_Mdl topControlSaveInitialize() {
        SittingLocation_Mdl mrTemp = new SittingLocation_Mdl();
        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtConsultname.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtConsultname.getText()));
        mrTemp.setLocationTag(DmShared.xNull(txtHLocation.getTag()));
        mrTemp.setLocationText(DmShared.xNull(txtHLocation.getText()));
        mrTemp.setDeviceBlock(DmShared.xNull(txtDeviceBlock.getText()));
        mrTemp.setDeviceCode(DmShared.xNull(txtDeviceCode.getText()));
        return mrTemp;
    }

    private void topControlSave() {
        SittingLocation_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            SittingLocation_Vld.chkSave(mrTemp);
            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(SittingLocation.this);
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
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
                new DmAlertMessageBox(errorMessage, txtHLocation).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHLocation).show();
        }
    }

    private void manageDisplay() {
        txtConsultname.setEnabled(false);
    }

    @Override
    public void searchRecord(String key) {
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(SittingLocation.this);
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

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "SITTING":
                    openWithCPOE((String) object, callback);
                    break;
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtHLocation.setFocus(true);
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    public void openWithCPOE(String consultantCode, final DmCallBack callback) {
        srvAsync.invokeRPC(true).getDefaultValue(consultantCode, new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    txtConsultname.setTag(defaultValue.getConsultantCode());
                    txtConsultname.setText(defaultValue.getConsultantName());
                    txtHLocation.setTag(defaultValue.getHkLocationCode());
                    txtHLocation.setText(defaultValue.getHkLocationName());
                    txtDeviceBlock.setText(defaultValue.getDeviceBlock());
                    txtDeviceCode.setText(defaultValue.getDeviceCode());
                    if (callback != null) {
                        DmResult<Object> dmrTemp = new DmResult<>();
                        dmrTemp.setStatus(true);
                        callback.result(dmrTemp);
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int charCode = event.getCharCode();
        if (event.getSource().equals(txtDeviceCode)) {
            if ((charCode > 31) && (charCode < 48)) {
                txtDeviceCode.cancelKey();
            } else if (charCode > 57 && charCode < 65) {
                txtDeviceCode.cancelKey();
            } else if (charCode > 90 && charCode < 97) {
                txtDeviceCode.cancelKey();
            } else if (charCode > 121) {
                txtDeviceCode.cancelKey();
            }
        } else if (event.getSource().equals(txtDeviceBlock)) {
            if ((charCode > 31) && (charCode < 48)) {
                txtDeviceBlock.cancelKey();
            } else if (charCode > 57 && charCode < 65) {
                txtDeviceBlock.cancelKey();
            } else if (charCode > 90 && charCode < 94) {
                txtDeviceBlock.cancelKey();
            } else if (charCode > 95 && charCode < 97) {
                txtDeviceBlock.cancelKey();
            } else if (charCode > 121) {
                txtDeviceBlock.cancelKey();
            }
        }
    }
}
