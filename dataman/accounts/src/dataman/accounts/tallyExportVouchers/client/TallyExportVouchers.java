package dataman.accounts.tallyExportVouchers.client;

import dataman.accounts.tallyExportVouchers.shared.Filter_Mdl;
import dataman.accounts.tallyExportVouchers.shared.TallyExportVouchers_SrvAsync;
import dataman.accounts.tallyExportVouchers.shared.TallyExportVouchers_Srv;
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

public class TallyExportVouchers extends TallyExportVouchers_Dsg implements IsWidget, ClickHandler, BlurHandler, AttachEvent.Handler, DmRootEvents {

    private ServiceInvoker<TallyExportVouchers_SrvAsync> srvAsync;
    private Filter_Mdl filter;

    public TallyExportVouchers(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(TallyExportVouchers_Srv.class), TallyExportVouchers.class.getName());
        this.asWidget().setTitle(widgetName);
        manageEvents();

        swi.setWidget(this);
        txtFromDate.setText(DmClient.getLoginDate());
        txtToDate.setText(DmClient.getLoginDate());
        txtFromDate.setFocus(true);
    }

    private void exportAcVouchersToTallyXML() {

        filter = new Filter_Mdl();
        filter.setSessionId(DmRequestBuilder.getSessionId());
        filter.setFromDate(txtFromDate.getText());
        filter.setToDate(txtToDate.getText());

        srvAsync.invokeRPC(true).exportAcVouchersToTallyXML(filter, new AsyncCallback<DmResult<String[]>>() {
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
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        btnClose.addClickHandler(this);
        btnExportXML.addClickHandler(this);
    }

    private void onDownload() {
        if (DmShared.dNull(txtFromDate.getText()).trim().equals("")) {
            new DmAlertMessageBox("Please select From Date", txtFromDate).show();
            return;
        }
        if (DmShared.dNull(txtToDate.getText()).trim().equals("")) {
            new DmAlertMessageBox("Please select To Date", txtToDate).show();
            return;
        }
          new DmConfirmMessageBox("Are you sure to export vouchers XML ?", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            exportAcVouchersToTallyXML();
                        }
                    }
                }).show();

       
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(TallyExportVouchers.this);
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
        if (event.getSource().equals(txtFromDate)) {
            txtFromDate.setText(DmClient.returnDate(getDateFromMonthYear(txtFromDate.getText())));
          
        }
        if (event.getSource().equals(txtToDate)) {
            txtToDate.setText(DmClient.returnDate(getDateFromMonthYear(txtToDate.getText())));
          
        }
    }

    public static String getDateFromMonthYear(String tmpDate) {
        String date = "";
        if (!DmShared.xNull(tmpDate).trim().equalsIgnoreCase("")) {
            if (DmShared.xNull(tmpDate).trim().charAt(0) == '.' || DmShared.xNull(tmpDate).trim().charAt(0) == '/'
                    || DmShared.xNull(tmpDate).trim().charAt(0) == '-') {
                tmpDate = "01" + DmShared.xNull(tmpDate).trim();
            }
        }
        date = DmClient.returnDate(tmpDate);

      

        return date;
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
