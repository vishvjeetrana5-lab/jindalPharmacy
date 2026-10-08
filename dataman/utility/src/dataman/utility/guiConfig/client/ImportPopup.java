package dataman.utility.guiConfig.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.utility.guiConfig.shared.*;

public class ImportPopup extends ImportPopup_Dsg implements IsWidget, DmRootEvents, ClickHandler, BlurHandler, AttachEvent.Handler {

    interface CallBack {

        public void onYes(DmResult<GUIConfig_Mdl> mdl);
    }

    private String templateName = "";
    private DmResult<GUIConfig_Mdl> configuration_Mdl = new DmResult<>();
    private CallBack callBack;
    private ServiceInvoker<GUIConfig_SrvAsync> srvAsync;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void chooseFile(String ServerDirectory, String serverFileName) {
        srvAsync.invokeRPC().importJson(ServerDirectory, serverFileName, new AsyncCallback<DmResult<GUIConfig_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<GUIConfig_Mdl> result) {
                configuration_Mdl = result;
                lblFileName.setVisible(true);
                lblFileName.getElement().getStyle().setColor("blue");
                btnYes.setFocus(true);
                GUIConfig_Mdl config_Mdl = result.getData();
                if (!DmShared.xNull(templateName).equalsIgnoreCase(config_Mdl.getTemplateName())) {
                    lblMessage.setText("Choosen File has different Template Name" + "," + "\n Do you want to continue?");
                    lblMessage.setWordWrap(true);
                    lblMessage.getElement().getStyle().setColor("red");
                } else {
                    lblMessage.setText("File has been succesfully choosen" + "," + "\n Do you want to continue?");
                    lblMessage.getElement().getStyle().setColor("blue");
                }
            }
        });
    }

    @Override
    public void notifyForUpload(String notifier) {
    }

    @Override
    public void processAfterUploaded(DmIFile_Mdl file) {
        lblFileName.setText(file.getFileName());

        chooseFile(file.getServerDirectory(), file.getServerFileName());
    }

    public void show(String template) {
        templateName = template;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        lblFileName.setVisible(false);
        btnUpload.getButton().setFocus(true);
    }

    public void hide() {
        clear();
        this.popupPanel.hide();
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel || event.getSource() == btnNo) {
            hide();
        }
        if (event.getSource() == btnYes) {
            callBack.onYes(configuration_Mdl);
            hide();
        }
    }

    public void clear() {
        lblFileName.setText("");
        lblMessage.setText("");
        configuration_Mdl = new DmResult<>();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
//            callBack.onYes(docId);
            this.hide();
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public ImportPopup() {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(GUIConfig_Srv.class), GUIConfig.class.getName());
        toolContainer.setTitle("Import");

        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        btnCancel.addClickHandler(this);
        btnYes.addClickHandler(this);
        btnNo.addClickHandler(this);
        popupPanel.addAttachHandler(this);

    }

}
