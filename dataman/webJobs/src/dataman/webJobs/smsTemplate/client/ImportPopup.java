package dataman.webJobs.smsTemplate.client;

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
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.webJobs.smsTemplate.shared.SmsTemplate_Mdl;
import dataman.webJobs.smsTemplate.shared.SmsTemplate_Srv;
import dataman.webJobs.smsTemplate.shared.SmsTemplate_SrvAsync;

public class ImportPopup extends ImportPopup_Dsg implements IsWidget, DmRootEvents, ClickHandler, BlurHandler, AttachEvent.Handler {

    SmsTemplate_Mdl configuration_Mdl = new SmsTemplate_Mdl();

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    interface CallBack {

        public void onYes(DmResult<SmsTemplate_Mdl> mdl);
    }

    CallBack callBack;
    ServiceInvoker<SmsTemplate_SrvAsync> SrvAsync;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void chooseFile(String ServerDirectory, String serverFileName) {
        SrvAsync.invokeRPC().importJson(ServerDirectory, serverFileName, new AsyncCallback<DmResult<SmsTemplate_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<SmsTemplate_Mdl> result) {
                configuration_Mdl = result.getData();
                lblFileName.setVisible(true);
                lblFileName.getElement().getStyle().setColor("blue");
                btnYes.setFocus(true);
//                if (!DmShared.xNull(configuration_Mdl.getTempName()).equalsIgnoreCase("")) {
//                    lblMessage.setText("Choosen File has " + "," + "\n Do you want to continue?");
//                    lblMessage.setWordWrap(true);
//                    lblMessage.getElement().getStyle().setColor("red");
//                } else {
                lblMessage.setText("File has been succesfully choosen" + "," + "\n Do you want to continue?");
                lblMessage.getElement().getStyle().setColor("blue");

//                    
//                }
                fillExportModel();
            }
        });
    }

    private void importFile() {
        SrvAsync.invokeRPC().checkImportedValues(configuration_Mdl, new AsyncCallback<DmResult<SmsTemplate_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<SmsTemplate_Mdl> result) {
                hide();
                callBack.onYes(result);

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

    public void show(String shortName, String overHead) {

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        lblFileName.setVisible(false);
        btnUpload.getButton().setFocus(true);
    }

    private void fillExportModel() {
        configuration_Mdl.setSharedKeyFieldCode("");
        configuration_Mdl.setSharedKeyFieldName("");
        configuration_Mdl.setSharedModifiedBy("");
        configuration_Mdl.setSharedPreparedBy("");
//        configuration_Mdl.getGrdSiteName().add(new KeyFieldExtended());
//        for (int i = 0; i < configuration_Mdl.getGrdManageLine().size(); i++) {
//            GrdOverheadLine_Mdl grdOverhead_Mdl = configuration_Mdl.getGrdManageLine().get(i);
//            grdOverhead_Mdl.setOverheadCode("");
//            grdOverhead_Mdl.setContraExpTag("");
//            grdOverhead_Mdl.setPostACCode("");
//        }
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
            importFile();
        }
    }

    public void clear() {
        lblFileName.setText("");
        lblMessage.setText("");
        configuration_Mdl = new SmsTemplate_Mdl();
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
        SrvAsync = new ServiceInvoker<>(GWT.create(SmsTemplate_Srv.class), SmsTemplate.class.getName());
        toolContainer.setTitle("Import");

        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        btnCancel.addClickHandler(this);
        btnYes.addClickHandler(this);
        btnNo.addClickHandler(this);
        popupPanel.addAttachHandler(this);

    }

}
