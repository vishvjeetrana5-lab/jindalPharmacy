package dataman.webJobs.smsManager.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.webJobs.emailManager.shared.Template_Mdl;
import dataman.webJobs.smsManager.shared.SmsManager_Srv;
import dataman.webJobs.smsManager.shared.SmsManager_SrvAsync;
import dataman.webJobs.smsTemplate.shared.GrdFieldName_Mdl;

import java.util.List;

public class DraftPopup extends DraftPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    interface CallBack {

        public void onOk(Template_Mdl template_Mdl);

        public void onClose();
    }

    private CallBack callBack;
    private final ServiceInvoker<SmsManager_SrvAsync> srvAsync;
    private Template_Mdl template_Mdl;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(SmsManager smsManager, Template_Mdl templateMdl) {
        this.template_Mdl = template_Mdl;
        displayData();
        popupPanel.show();
        popupPanel.showRelativeTo(smsManager.txtTemplateName);
        popupPanel.setGlassEnabled(true);
        txtHTemplateName.setFocus(true);
    }

    private void displayData() {
        txtHTemplateName.setTag("");
        txtHTemplateName.setText("");
        txtMsg.setText("");

        if (template_Mdl != null) {
            txtHTemplateName.setTag(DmShared.xNull(template_Mdl.getSharedKeyFieldCode()));
            txtHTemplateName.setText(DmShared.xNull(template_Mdl.getSharedKeyFieldName()));
            txtMsg.setText(DmShared.xNull(template_Mdl.getMessageTemplate()));
        }
    }

    private void txtHelpEvents() {
        txtHTemplateName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<Template_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTempName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Template_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTemplateName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Template_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHTemplateName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTemplateName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Template_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    template_Mdl = t1Row;
                    txtMsg.setText(template_Mdl.getMessageTemplate());
                    setMetaData();
                }
            }
        });
    }

    private void manageEvent() {
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);

    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            callBack.onClose();
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
            if (DmShared.iNull(txtHTemplateName.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Template name is mandatory.", txtHTemplateName).show();
                return;
            }

            if (DmShared.xNull(txtMsg.getText()).trim().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Message is mandatory.", txtMsg).show();
                return;
            }

            template_Mdl.setMessageTemplate(txtMsg.getText());
            callBack.onOk(template_Mdl);
            popupPanel.hide();

        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            popupPanel.hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            popupPanel.hide();
        }
    }

    public DraftPopup() {
        super();
        toolContainer.setTitle(widgetName);
        srvAsync = new ServiceInvoker<SmsManager_SrvAsync>(GWT.create(SmsManager_Srv.class), SmsManager.class.getName());
        manageEvent();
        txtHelpEvents();
        popupPanel.addAttachHandler(this);
    }

    private void setMetaData() {

        srvAsync.invokeRPC().getMetaDataOnly(template_Mdl, new AsyncCallback<DmResult<List<GrdFieldName_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdFieldName_Mdl>> result) {
                if (result.isStatus()) {
                    grdFieldDetail.getGrid().getStore().clear();
                    grdFieldDetail.getGrid().getStore().addAll(result.getData());

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

}
