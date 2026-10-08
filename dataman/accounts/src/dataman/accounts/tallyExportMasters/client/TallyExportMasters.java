package dataman.accounts.tallyExportMasters.client;

import dataman.accounts.tallyExportMasters.shared.Filter_Mdl;
import dataman.accounts.tallyExportMasters.shared.TallyExportMasters_Srv;
import dataman.accounts.tallyExportMasters.shared.TallyExportMasters_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.HashMap;

public class TallyExportMasters extends TallyExportMasters_Dsg implements IsWidget, ClickHandler, BlurHandler, AttachEvent.Handler, DmRootEvents {

    private ServiceInvoker<TallyExportMasters_SrvAsync> srvAsync;
    private Filter_Mdl filter;

    public TallyExportMasters(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(TallyExportMasters_Srv.class), TallyExportMasters.class.getName());
        this.asWidget().setTitle(widgetName);
        manageEvents();

        swi.setWidget(this);

    }

    private void exportAcMastersToTallyXML() {

        filter = new Filter_Mdl();
        filter.setSessionId(DmRequestBuilder.getSessionId());

        srvAsync.invokeRPC(true).exportAcMastersToTallyXML(filter, new AsyncCallback<DmResult<String[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String[]> result) {
                if (result.isStatus()) {
                    startDownloading(DmShared.xNull(result.getData()[0]), DmShared.xNull(result.getData()[1]));

                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void startDownloading(String filePath, String fileName) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

    private void manageEvents() {
        flwForm.addAttachHandler(this);
        btnClose.addClickHandler(this);
        btnExportXML.addClickHandler(this);
    }

    private void onDownload() {

        exportAcMastersToTallyXML();
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(TallyExportMasters.this);
                }
            }
        }).show();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnExportXML)) {
            onDownload();
        } else if (event.getSource().equals(btnClose)) {
            onClose();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            onDownload();
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

}
