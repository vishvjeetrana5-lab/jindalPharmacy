package dataman.webJobs.environmentSetting.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.webJobs.environmentSetting.shared.EnvironmentSetting_Mdl;
import dataman.webJobs.environmentSetting.shared.EnvironmentSetting_Srv;
import dataman.webJobs.environmentSetting.shared.EnvironmentSetting_SrvAsync;
import dataman.webJobs.environmentSetting.shared.EnvironmentSetting_Vld;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
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
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public final class EnvironmentSetting extends EnvironmentSetting_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents {

    ServiceInvoker<EnvironmentSetting_SrvAsync> SrvAsync;
    String siteCode = "", siteName = "";

    private void textHelpEvents() {
        txtHSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus) {
                    if (t1Row != null) {
                        topControlMoveRec(DmShared.xNull(txtHSite.getTag()));
                        siteCode = txtHSite.getTag();
                        siteName = txtHSite.getText();
                    }
                }
            }
        });
        txtHSmtpSsl.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSmtpSsl(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSmtpSsl).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSmtpSsl.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSmtpSsl).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHSiteCopy.addHelpGridCallBack(new DmHelpGrid.HelpGridCallBack<KeyFieldExtended>() {
//            final String siteCode = txtHSite.getTag();
//            final String siteName = txtHSite.getText();

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSiteCopy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus) {
                    if (t1Row != null) {
                        topControlMoveRec(t1Row.getSharedKeyFieldCode());
                        txtHSite.setText(DmShared.iNull(siteName).toString());
                        txtHSite.setTag(DmShared.iNull(siteCode).toString());
                    }
                }
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource().equals(btnSave)) {
                topControlSave();
            } else if (event.getSource().equals(btnClose)) {
                onClose();
            } else if (event.getSource().equals(btnCopy)) {
                txtHSiteCopy.show(btnCopy, "");
                txtHSiteCopy.reQuery("");
            }
        } catch (DmException e) {
        }

    }

    public EnvironmentSetting(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(EnvironmentSetting_Srv.class), EnvironmentSetting.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(btnSave);
        toolContainer.addWidget(btnCopy);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
        btnCopy.addClickHandler(this);
        textHelpEvents();
        defaultValues();
        flwForm.addAttachHandler(this);
        swi.setWidget(this);

    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private EnvironmentSetting_Mdl topControlSaveInitialize() {
        EnvironmentSetting_Mdl mrTemp = new EnvironmentSetting_Mdl();

        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHSite.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHSite.getText()));
        mrTemp.setSmtpSslTag(DmShared.xNull(txtHSmtpSsl.getTag()));
        mrTemp.setSmtpServer(DmShared.xNull(txtSmtpServer.getText()));
        mrTemp.setSmtpPort(DmShared.xNull(txtSmtpPort.getText()));
        mrTemp.setUrl(DmShared.xNull(txtUrl.getText()));
        mrTemp.setAccName(DmShared.xNull(txtAccName.getText()));
        mrTemp.setPassword(DmShared.xNull(txtPassword.getText()));
        mrTemp.setFormat(DmShared.xNull(txtFormat.getText()));
        mrTemp.setMsgLength(DmShared.xNull(txtMsgLength.getText()));
        mrTemp.setPayRefOtpMobile(DmShared.xNull(txtPayRefOtpMobile.getText()));

        return mrTemp;
    }

    private void topControlSave() {
        EnvironmentSetting_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            EnvironmentSetting_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControlMoveRec(result.getData());
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
                txtHSite.setFocus(true);
                new DmAlertMessageBox(errorMessage, txtHSite).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtUrl).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtAccName).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtPassword).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtFormat).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtMsgLength).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtSmtpServer).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtSmtpPort).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtHSmtpSsl).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, txtPayRefOtpMobile).show();
                break;

            default:
                new DmAlertMessageBox(errorMessage, txtHSite).show();
        }

    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<EnvironmentSetting_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<EnvironmentSetting_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<EnvironmentSetting_Mdl> result) {
        EnvironmentSetting_Mdl data;

        if (result.isStatus()) {
            data = result.getData();

            txtHSmtpSsl.setTag(data.getSmtpSslTag());
            txtHSmtpSsl.setText(data.getSmtpSslText());
            txtSmtpServer.setText(data.getSmtpServer());
            txtSmtpPort.setText(data.getSmtpPort());
            txtUrl.setText(data.getUrl());
            txtAccName.setText(data.getAccName());
            txtFormat.setText(data.getFormat());
            txtPassword.setText(data.getPassword());
            txtMsgLength.setText(data.getMsgLength());
            txtPayRefOtpMobile.setText(data.getPayRefOtpMobile());

            String dateUser = DmShared.xNull(data.getuName()) + "  -  " + (data.getDate()) + "  -  " + DmShared.xNull(data.getTime());
            userControl.setTextBlog(modifiedByBlog, dateUser);
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

    public void defaultValues() {
        SrvAsync.invokeRPC().getSiteCode(new AsyncCallback<DmResult<EnvironmentSetting_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<EnvironmentSetting_Mdl> result) {
                if (result.isStatus()) {
                    txtHSite.setTag(DmShared.xNull(result.getData().getSharedKeyFieldCode()));
                    txtHSite.setText(DmShared.xNull(result.getData().getSharedKeyFieldName()));
                    siteCode = DmShared.xNull(result.getData().getSharedKeyFieldCode());
                    siteName = DmShared.xNull(result.getData().getSharedKeyFieldName());
                    topControlMoveRec(txtHSite.getTag());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EnvironmentSetting.this);
                }
            }
        }).show();
    }

    private void onAdd() {

        txtHSmtpSsl.setTag("1");
        txtHSmtpSsl.setText("Yes");

        tabPanel.selectTab(0);

        userControl.setTextBlog(modifiedByBlog, "");

    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }
}
