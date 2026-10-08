package dataman.dmToolkitR.printerMapping.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkitR.dmAlertBox.client.DmAlertBox;
import dataman.dmToolkitR.dmStatic.client.DmClient;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmToolkitR.dmTextBox.client.DmTextBox;
import dataman.dmToolkitR.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkitR.printerMapping.shared.PrinterMapping_Srv;
import dataman.dmToolkitR.printerMapping.shared.PrinterMapping_SrvAsync;
import dataman.dmToolkitR.printerMapping.shared.PrinterMappingLocalSetting_Mdl;
import java.util.HashMap;
import java.util.List;

public class PrinterMapping extends PrinterMapping_Dsg implements ClickHandler {

    private final ServiceInvoker<PrinterMapping_SrvAsync> srvAsync;
    private DmReportProperty_Mdl reportProperty;
    private String moduleName, menuText, reportName;

    public void show() {

        model.show();

        txtMenu.setEnabled(false);
        txtModule.setEnabled(false);
        txtReportName.setEnabled(false);

        txtHPrinter.setFocus(true);

        btnSubmit.setTitle("Submit");
        btnSubmit.setText("Submit");

//        createResendOtpTimer();
    }

    public PrinterMapping(DmReportProperty_Mdl reportProperty, String moduleName, String menuText, String reportName) {
        super();
        srvAsync = new ServiceInvoker<PrinterMapping_SrvAsync>(GWT.create(PrinterMapping_Srv.class), PrinterMapping.class.getName());
        manageEvents();
        textHelpEvent();

        this.reportProperty = reportProperty;
        this.moduleName = moduleName.trim();
        this.menuText = menuText.trim();
        this.reportName = reportName.trim();

        txtMenu.setText(menuText);
        txtModule.setText(moduleName);
        txtReportName.setText(reportName);

    }

    private void textHelpEvent() {
        txtHPrinter.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC().getTxtHPrinter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPrinter.setAllData(result.getData(), result.getPaging());
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(KeyFieldExtended tRow) {
                txtHPrinter.setText(tRow.getSharedKeyFieldName());
                txtHPrinter.setTag(tRow.getSharedKeyFieldCode());

            }
        });

    }

    private void manageEvents() {
        btnSubmit.addClickHandler(this);
        btnBack.addClickHandler(this);

    }

    private void hide(boolean isCallbackRequired) {
        model.hide();

    }

    public void hide() {
        hide(false);
    }

    @Override
    public Widget asWidget() {
        return model;
    }

    @Override
    public void searchRecord(String key) {
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnSubmit) {

            if (flwPrinterArea.isAttached()) {
                onOkClick();
                //hide(true);
            }
        } else if (event.getSource() == btnBack) {

            hide(true);
        }
    }

    public void onOkClick() {

        if (DmShared.xNull(txtHPrinter.getText()).equalsIgnoreCase("")) {

            new DmAlertBox("Printer Name is mandatory ", txtHPrinter).show();
            return;
        }

        if (DmClient.isReportMapExists(moduleName, menuText, reportName)) {
            //if (true) {
            reportProperty.setPrinterCode(txtHPrinter.getTag());
            saveLocalSettings(reportProperty, true);
            hide();

//            new DmConfirmMessageBox("Report Map for Module : " + moduleName
//                    + ", Menu : " + menuText + " and Report Name : " + reportName
//                    + " is already exists. Do you want to overwrite it ?", new DialogHideEvent.DialogHideHandler() {
//
//                @Override
//                public void onDialogHide(DialogHideEvent event) {
//
//                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
//                        reportProperty.setPrinterCode(txtHPrinter.getTag());
//                        saveLocalSettings(reportProperty, true);
//                        hide();
//                    } else {
//                        hide();
//                    }
//                }
//            }).show();
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
        srvAsync.invokeRPC().getPrinterMappingLocalSettingFromJSon(jsonOfJVMLocalSettings, new AsyncCallback<DmResult<PrinterMappingLocalSetting_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {

                new DmAlertBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PrinterMappingLocalSetting_Mdl> result) {
                if (result.isStatus()) {
                    DmClient.refreshLocalSettingPrintReportMap(result.getData().getReportMappings());
                    DmClient.refreshLocalSettingPrinters(result.getData().getPrinterMappings());
                } else {

                    new DmAlertBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void getJsonOfReportMapModel(DmLocalSettingPrintMap_Mdl reportMapModel, final DmClient.IReportMapJSonInternalCallback reportMapCallback) {
        srvAsync.invokeRPC().getJsonOfLocalSettingReportMapModel(reportMapModel, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {

                new DmAlertBox(caught.getMessage(), (DmTextBox) null).show();
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

}
