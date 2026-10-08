package dataman.webJobs.emailManager.client;

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
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.webJobs.emailManager.shared.EmailManager_Srv;
import dataman.webJobs.emailManager.shared.EmailManager_SrvAsync;
import dataman.webJobs.emailManager.shared.Template_Mdl;
import java.util.List;

public class DraftPopup extends DraftPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    interface CallBack {

        public void onOk(Template_Mdl template_Mdl);

        public void onClose();
    }

    private CallBack callBack;
    private final ServiceInvoker<EmailManager_SrvAsync> SrvAsync;
    private Template_Mdl template_Mdl;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(EmailManager emailManager, String templateCode, String templateName) {
        displayData(templateCode, templateName);
        popupPanel.show();
        popupPanel.showRelativeTo(emailManager.txtTemplateName);
        popupPanel.setGlassEnabled(true);
        txtHTemplateName.setFocus(true);
    }

    private void displayData(String templateCode, String templateName) {
        txtHTemplateName.setTag(templateCode);
        txtHTemplateName.setText(templateName);

    }

    private void txtHelpEvents() {
        txtHTemplateName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<Template_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTempName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Template_Mdl>>>() {
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
            if (DmShared.iNull(txtHTemplateName.getTag()).longValue() != 0) {
                callBack.onOk(template_Mdl);
                popupPanel.hide();
            } else {
                new DmAlertMessageBox("Template name is mandatory.", txtHTemplateName).show();
                return;
            }

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
        SrvAsync = new ServiceInvoker<EmailManager_SrvAsync>(GWT.create(EmailManager_Srv.class), EmailManager.class.getName());
        manageEvent();
        txtHelpEvents();
        popupPanel.addAttachHandler(this);
    }

}
