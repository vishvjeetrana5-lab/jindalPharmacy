package aarogya.master.customEnviro.client;

import aarogya.master.customEnviro.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public final class CustomEnviro extends CustomEnviro_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents {

    private final ServiceInvoker<CustomEnviro_SrvAsync> SrvAsync;

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource().equals(btnSave)) {
                topControlSave();
            } else if (event.getSource().equals(btnClose)) {
                onClose();
            }
        } catch (DmException e) {
        }

    }

    private void textHelpEvents() {

        txtHIsPbillMrpLessthenPo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIsPbillMrpLessthenPo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsPbillMrpLessthenPo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsPbillMrpLessthenPo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsPbillMrpLessthenPo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

    }

    public CustomEnviro(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(CustomEnviro_Srv.class), CustomEnviro.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(btnSave);

        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);

        onLoad();
        textHelpEvents();

        flwForm.addAttachHandler(this);
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private CustomEnviro_Mdl topControlSaveInitialize() {
        CustomEnviro_Mdl mrTemp = new CustomEnviro_Mdl();

        mrTemp.setUnApproveUptoDays(DmShared.xNull(txtUnApproveUptoDays.getText()));
        mrTemp.setEditUptoDays(DmShared.xNull(txtEditUptoDays.getText()));

        mrTemp.setIsPbillMrpLessthenPoTag(DmShared.xNull(txtHIsPbillMrpLessthenPo.getTag()));
        mrTemp.setIsPbillMrpLessthenPoText(DmShared.xNull(txtHIsPbillMrpLessthenPo.getText()));

        return mrTemp;
    }

    private void topControlSave() {
        CustomEnviro_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            CustomEnviro_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        onLoad();
                        new DmAlertMessageBox("Record saved successfully", txtEditUptoDays).show();
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

    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<CustomEnviro_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<CustomEnviro_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<CustomEnviro_Mdl> result) {
        CustomEnviro_Mdl data;

        if (result.isStatus()) {
            CustomEnviro_Mdl mrTemp;
            mrTemp = result.getData();

            txtUnApproveUptoDays.setText(DmShared.xNull(mrTemp.getUnApproveUptoDays()));
            txtEditUptoDays.setText(DmShared.xNull(mrTemp.getEditUptoDays()));

            if (DmShared.xNull(mrTemp.getIsPbillMrpLessthenPoTag()).equalsIgnoreCase("0")) {
                txtHIsPbillMrpLessthenPo.setTag("0");
                txtHIsPbillMrpLessthenPo.setText("No");
            } else if (DmShared.xNull(mrTemp.getIsPbillMrpLessthenPoTag()).equalsIgnoreCase("1")) {
                txtHIsPbillMrpLessthenPo.setTag("1");
                txtHIsPbillMrpLessthenPo.setText("Yes");
            }

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
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
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(CustomEnviro.this);
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
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            topControlSave();
        }
    }

    private void onLoad() {
        topControlMoveRec(null);
    }

}
