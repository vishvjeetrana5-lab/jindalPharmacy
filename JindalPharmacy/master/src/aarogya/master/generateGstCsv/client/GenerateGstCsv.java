package aarogya.master.generateGstCsv.client;

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
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.generateGstCsv.shared.Filter_Mdl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import aarogya.master.generateGstCsv.shared.GenerateGstCsv_Srv;
import aarogya.master.generateGstCsv.shared.GenerateGstCsv_SrvAsync;

public class GenerateGstCsv extends GenerateGstCsv_Dsg implements IsWidget, ClickHandler, BlurHandler, AttachEvent.Handler, DmRootEvents {

    private ServiceInvoker<GenerateGstCsv_SrvAsync> srvAsync;
    private Filter_Mdl filter;

    public GenerateGstCsv(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(GenerateGstCsv_Srv.class), GenerateGstCsv.class.getName());
        this.asWidget().setTitle(widgetName);
        manageEvents();
        textHelpEvents();
        swi.setWidget(this);
        txtFromDate.setFocus(true);
    }

    private void textHelpEvents() {
        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
    }

    private void exportGstDataToCsv() {

        filter = new Filter_Mdl();
        filter.setSessionId(DmRequestBuilder.getSessionId());
        filter.setFromDate(txtFromDate.getText());
        filter.setToDate(txtToDate.getText());
        filter.setType(txtHType.getTag());

        srvAsync.invokeRPC(true).exportGstDataToCsv(filter, new AsyncCallback<DmResult<String[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String[]> result) {
                if (result.isStatus()) {
                    startDownloading(DmShared.xNull(result.getData()[0]), DmShared.xNull(result.getData()[1]));
                    clear();
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
        btnDownload.addClickHandler(this);
        btnClose.addClickHandler(this);
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
        if (DmShared.xNull(txtHType.getTag()).trim().equals("")) {
            new DmAlertMessageBox("Please select CSV Type", txtHType).show();
            return;
        }
        exportGstDataToCsv();
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(GenerateGstCsv.this);
                }
            }
        }).show();
    }

    private void clear() {
        txtHType.setText("");
        txtHType.setTag("");
        txtHType.setTag2nd("");
        txtHType.setFocus(true);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnDownload)) {
            onDownload();
        } else if (event.getSource().equals(btnClose)) {
            onClose();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtFromDate)) {
            txtFromDate.setText(DmClient.returnDate(getDateFromMonthYear(txtFromDate.getText(), false)));
            txtToDate.setText(DmClient.returnDate(getDateFromMonthYear(txtFromDate.getText(), true)));
        }
    }

    public static String getDateFromMonthYear(String tmpDate, boolean isLastDateRequired) {
        String date = "";
        if (!DmShared.xNull(tmpDate).trim().equalsIgnoreCase("")) {
            if (DmShared.xNull(tmpDate).trim().charAt(0) == '.' || DmShared.xNull(tmpDate).trim().charAt(0) == '/'
                    || DmShared.xNull(tmpDate).trim().charAt(0) == '-') {
                tmpDate = "01" + DmShared.xNull(tmpDate).trim();
            }
        }
        date = DmClient.returnDate(tmpDate);

        try {
            if (isLastDateRequired) {
                Date lastDay = (Date) DmShared.convertDate(date).clone();
                CalendarUtil.addMonthsToDate(lastDay, 1);
                CalendarUtil.setToFirstDayOfMonth(lastDay);
                CalendarUtil.addDaysToDate(lastDay, -1);
                date = DmShared.dNull(lastDay);
            } else {
                Date lastDay = (Date) DmShared.convertDate(date).clone();
                CalendarUtil.setToFirstDayOfMonth(lastDay);
                date = DmShared.dNull(lastDay);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
