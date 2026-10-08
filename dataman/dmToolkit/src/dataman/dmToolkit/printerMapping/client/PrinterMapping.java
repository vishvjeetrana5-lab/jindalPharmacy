package dataman.dmToolkit.printerMapping.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmLocalSettingPrintMap_Mdl;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.printerMapping.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.util.List;

public class PrinterMapping extends PrinterMapping_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    private final ServiceInvoker<PrinterMapping_SrvAsync> SrvAsync;
    private DmReportProperty_Mdl reportProperty;
    private String moduleName, menuText, reportName;

    public void show() {

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        txtMenu.setEnabled(false);
        txtModule.setEnabled(false);
        txtReportName.setEnabled(false);

        txtHPrinter.setFocus(true);

    }

    private void textHelpEvents() {

        txtHPrinter.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {

                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPrinter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPrinter).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPrinter.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPrinter).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    public PrinterMapping(DmReportProperty_Mdl reportProperty, String moduleName, String menuText, String reportName) {

        super();

        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        SrvAsync = new ServiceInvoker<PrinterMapping_SrvAsync>(GWT.create(PrinterMapping_Srv.class), PrinterMapping.class.getName());

        popupPanel.addAttachHandler(this);

        textHelpEvents();
        this.reportProperty = reportProperty;
        this.moduleName = moduleName.trim();
        this.menuText = menuText.trim();
        this.reportName = reportName.trim();

        txtMenu.setText(menuText);
        txtModule.setText(moduleName);
        txtReportName.setText(reportName);
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        }
    }

    @Override
    public void onClick(ClickEvent event) {

        if (event.getSource().equals(btnOk)) {

            onOkClick();

        }

        if (event.getSource().equals(btnCancel)) {

            hide();
        }

    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {

        }
    }

    public void onOkClick() {

        if (DmShared.xNull(txtHPrinter.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Printer Name is mandatory ", txtHPrinter).show();
            return;
        }

        if (DmClient.isReportMapExists(moduleName, menuText, reportName)) {

            new DmConfirmMessageBox("Report Map for Module : " + moduleName
                    + ", Menu : " + menuText + " and Report Name : " + reportName
                    + " is already exists. Do you want to overwrite it ?", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {

                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        reportProperty.setPrinterCode(txtHPrinter.getTag());
                        saveLocalSettings(reportProperty, true);
                        hide();
                    } else {
                        hide();
                    }
                }
            }).show();

        } else {
            reportProperty.setPrinterCode(txtHPrinter.getTag());
            saveLocalSettings(reportProperty, false);
            hide();
        }

    }

    private void saveLocalSettings(final DmReportProperty_Mdl reportProperty, boolean replaceOldMapping) {
        DmClient.assignPrinterCodeToReportMap(reportProperty, moduleName, menuText, reportName, reportProperty.getPrinterCode(), replaceOldMapping, new DmClient.IReportMapAssignmentPrinterCodeUICallback() {
            @Override
            public void getJSonOfLocalSettingReportMap(DmLocalSettingPrintMap_Mdl reportMapModel, DmClient.IReportMapJSonInternalCallback reportMapInternalCallback) {
                getJsonOfReportMapModel(reportMapModel, reportMapInternalCallback);
            }

            @Override
            public void setLocalSettings(String jsonOfJVMLocalSettings) {
                getSettingsFromJson(jsonOfJVMLocalSettings);
            }
        });
    }

    private void getSettingsFromJson(String jsonOfJVMLocalSettings) {
        SrvAsync.invokeRPC().getPrinterMappingLocalSettingFromJSon(jsonOfJVMLocalSettings, new AsyncCallback<DmResult<PrinterMappingLocalSetting_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<PrinterMappingLocalSetting_Mdl> result) {
                if (result.isStatus()) {
                    DmClient.refreshLocalSettingPrintReportMap(result.getData().getReportMappings());
                    DmClient.refreshLocalSettingPrinters(result.getData().getPrinterMappings());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                }
            }
        });
    }

    private void getJsonOfReportMapModel(DmLocalSettingPrintMap_Mdl reportMapModel, final DmClient.IReportMapJSonInternalCallback reportMapCallback) {
        SrvAsync.invokeRPC().getJsonOfLocalSettingReportMapModel(reportMapModel, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    String jsonString = result.getData();
                    reportMapCallback.onJSonOfLocalSettingReportMap(jsonString);
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

}
