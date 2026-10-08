package aarogya.localSetting.client;

import aarogya.localSetting.shared.LabMachineConfiguration_Mdl;
import aarogya.localSetting.shared.LocalSetting_Srv;
import aarogya.localSetting.shared.LocalSetting_SrvAsync;
import aarogya.localSetting.shared.LocalSetting_Vld;
import aarogya.selfToolkit.stStatic.client.STClient;
import aarogya.selfToolkit.stStatic.shared.STLocalSetting_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmLocalSettingPrintMap_Mdl;
import dataman.dmBase.self.shared.DmLocalSettingPrinter_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.ArrayList;
import java.util.List;

public class PrinterMappingPopup extends PrinterMappingPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    private ServiceInvoker<LocalSetting_SrvAsync> SrvAsync;
    private String pacBaseURL = "";
    private List<DmLocalSettingPrintMap_Mdl> listSettingPrintMap1 = new ArrayList<>();
    private List<DmLocalSettingPrintMap_Mdl> listSettingPrintMap2 = new ArrayList<>();
    private List<DmLocalSettingPrintMap_Mdl> listSettingPrintMap3 = new ArrayList<>();
    private List<DmLocalSettingPrintMap_Mdl> listSettingPrintMap4 = new ArrayList<>();
    private List<DmLocalSettingPrintMap_Mdl> listSettingPrintMap5 = new ArrayList<>();

    PrinterSettingsPopup prntrstgs = new PrinterSettingsPopup();

    public void show() {

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

    }

    public PrinterMappingPopup() {

        super();
        SrvAsync = new ServiceInvoker<LocalSetting_SrvAsync>(GWT.create(LocalSetting_Srv.class), LocalSetting.class.getName());
        toolContainer.setTitle("Printer Mapping");

        btnCancel.addClickHandler(this);
        btnSave.addClickHandler(this);
        popupPanel.addAttachHandler(this);

        btnPrnt1.addClickHandler(this);
        btnPrnt2.addClickHandler(this);
        btnPrnt3.addClickHandler(this);
        btnPrnt4.addClickHandler(this);
        btnPrnt5.addClickHandler(this);
        pacBaseURL = "http://localhost:" + DmShared.iNull(DmClient.getPACWebPort()).toString() + "/localSettings?";
        textHelpEvents();
        moveRec(false);

    }

    private void textHelpEvents() {
        txtHPrinter1.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                getPrinterInfo(txtHPrinter1, helper);
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                txtHPrinter1.setText(t1Row.getSharedKeyFieldName());
                txtHPrinter1.setTag(t1Row.getSharedKeyFieldCode());
            }
        });

        txtHPrinter2.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                getPrinterInfo(txtHPrinter2, helper);
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                txtHPrinter2.setText(t1Row.getSharedKeyFieldName());
                txtHPrinter2.setTag(t1Row.getSharedKeyFieldCode());
            }
        });

        txtHPrinter3.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                getPrinterInfo(txtHPrinter3, helper);
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                txtHPrinter3.setText(t1Row.getSharedKeyFieldName());
                txtHPrinter3.setTag(t1Row.getSharedKeyFieldCode());
            }
        });

        txtHPrinter4.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                getPrinterInfo(txtHPrinter4, helper);
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                txtHPrinter4.setText(t1Row.getSharedKeyFieldName());
                txtHPrinter4.setTag(t1Row.getSharedKeyFieldCode());
            }
        });

        txtHPrinter5.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                getPrinterInfo(txtHPrinter5, helper);
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                txtHPrinter5.setText(t1Row.getSharedKeyFieldName());
                txtHPrinter5.setTag(t1Row.getSharedKeyFieldCode());
            }
        });

        prntrstgs.addCallBack(new PrinterSettingsPopup.CallBack() {
            @Override
            public void ok(String title, List<DmLocalSettingPrintMap_Mdl> listSettingPrintMap) {
                if (listSettingPrintMap != null) {
                    if (DmShared.xNull(title).trim().equalsIgnoreCase("Printer-1")) {
                        listSettingPrintMap1.clear();
                        listSettingPrintMap1.addAll(listSettingPrintMap);
                    } else if (DmShared.xNull(title).trim().equalsIgnoreCase("Printer-2")) {
                        listSettingPrintMap2.clear();
                        listSettingPrintMap2.addAll(listSettingPrintMap);
                    } else if (DmShared.xNull(title).trim().equalsIgnoreCase("Printer-3")) {
                        listSettingPrintMap3.clear();
                        listSettingPrintMap3.addAll(listSettingPrintMap);
                    } else if (DmShared.xNull(title).trim().equalsIgnoreCase("Printer-4")) {
                        listSettingPrintMap4.clear();
                        listSettingPrintMap4.addAll(listSettingPrintMap);
                    } else if (DmShared.xNull(title).trim().equalsIgnoreCase("Printer-5")) {
                        listSettingPrintMap5.clear();
                        listSettingPrintMap5.addAll(listSettingPrintMap);
                    }
                }
            }

            @Override
            public void close() {
                //
            }
        });
    }

    private void getPrinterInfo(final DmTextHelp txtHelp, final DmHelper helper) {
        try {
            String tempUrl = pacBaseURL + "cmd=getPrinterInfo";
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, tempUrl);
            requestBuilder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        SrvAsync.invokeRPC().convertPrintServicesModel(helper, response.getText(), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
                            }

                            @Override
                            public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                                if (result.isStatus()) {
                                    txtHelp.setData(result.getData(), result.getPaging());
                                }
                            }
                        });
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    new DmAlertMessageBox(exception.getMessage(), (Button) null).show();
                }
            });
            requestBuilder.send();
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), (Button) null).show();
        }
    }

    private void isLocalSettingExists(final boolean showMessageOfSave) {
        try {
            String tempUrl = pacBaseURL + "cmd=isFileExists";
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, tempUrl);
            requestBuilder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        if (response.getText().equalsIgnoreCase("true")) {
                            getLocalSettings(showMessageOfSave);
                        }
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    new DmAlertMessageBox(exception.getMessage(), (Button) null).show();
                }
            });
            requestBuilder.send();
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), (Button) null).show();
        }
    }

    private void loadLocalSettings(String jsonString, final boolean showMessageOfSave) {
        SrvAsync.invokeRPC().getSettingsFromJson(jsonString, new AsyncCallback<DmResult<STLocalSetting_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<STLocalSetting_Mdl> result) {
                if (result.isStatus()) {
                    onSettingsLoad(result.getData(), showMessageOfSave);
                }
            }
        });
    }

    private void onSettingsLoad(final STLocalSetting_Mdl settings, boolean showMessageOfSave) {
        DmClient.refreshLocalSettingPrintReportMap(settings.getDmLocalSettingPrintMap_Mdls());
        DmClient.refreshLocalSettingPrinters(settings.getDmLocalSettingPrinter_Mdls());

        setPrinterSettings(settings.getDmLocalSettingPrinter_Mdls());
        setReportMapSettings(settings.getDmLocalSettingPrintMap_Mdls());
//
//        SrvAsync.invokeRPC().getLabMachineModelsFromJsonOfSavedConfigs(settings.getExtendedLocalSettingJsonString(), new AsyncCallback<DmResult<List<LabMachineConfiguration_Mdl>>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
//            }
//
//            @Override
//            public void onSuccess(DmResult<List<LabMachineConfiguration_Mdl>> result) {
//                if (result.isStatus()) {
//                
//                } else {
//                    new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
//                }
//            }
//        });

        if (showMessageOfSave) {
            new DmAlertMessageBox("Printers are mapped successfully ", txtHPrinter1).show();
        }
    }

    private STLocalSetting_Mdl topControlSaveInitialize() {
        STLocalSetting_Mdl mrTemp = new STLocalSetting_Mdl();

        for (int i = 0; i < listSettingPrintMap1.size(); i++) {
            listSettingPrintMap1.get(i).setPrinterCode("Printer-1");
            if (!mrTemp.getDmLocalSettingPrintMap_Mdls().contains(listSettingPrintMap1.get(i))) {
                mrTemp.getDmLocalSettingPrintMap_Mdls().add(listSettingPrintMap1.get(i));
            }
        }

        for (int i = 0; i < listSettingPrintMap2.size(); i++) {
            listSettingPrintMap2.get(i).setPrinterCode("Printer-2");
            if (!mrTemp.getDmLocalSettingPrintMap_Mdls().contains(listSettingPrintMap2.get(i))) {
                mrTemp.getDmLocalSettingPrintMap_Mdls().add(listSettingPrintMap2.get(i));
            }
        }

        for (int i = 0; i < listSettingPrintMap3.size(); i++) {
            listSettingPrintMap3.get(i).setPrinterCode("Printer-3");
            if (!mrTemp.getDmLocalSettingPrintMap_Mdls().contains(listSettingPrintMap3.get(i))) {
                mrTemp.getDmLocalSettingPrintMap_Mdls().add(listSettingPrintMap3.get(i));
            }
        }

        for (int i = 0; i < listSettingPrintMap4.size(); i++) {
            listSettingPrintMap4.get(i).setPrinterCode("Printer-4");
            if (!mrTemp.getDmLocalSettingPrintMap_Mdls().contains(listSettingPrintMap4.get(i))) {
                mrTemp.getDmLocalSettingPrintMap_Mdls().add(listSettingPrintMap4.get(i));
            }
        }

        for (int i = 0; i < listSettingPrintMap5.size(); i++) {
            listSettingPrintMap5.get(i).setPrinterCode("Printer-5");
            if (!mrTemp.getDmLocalSettingPrintMap_Mdls().contains(listSettingPrintMap5.get(i))) {
                mrTemp.getDmLocalSettingPrintMap_Mdls().add(listSettingPrintMap5.get(i));
            }
        }

        DmLocalSettingPrinter_Mdl printerModel = new DmLocalSettingPrinter_Mdl();
        printerModel.setPrinterCode("Printer-1");
        printerModel.setPrinterName(txtHPrinter1.getText());
        printerModel.setPrinterImageType((chkBoxPrntr1.getValue()) ? DmEnum.DmPrinterImageType.BYTE : DmEnum.DmPrinterImageType.RGB);

        mrTemp.getDmLocalSettingPrinter_Mdls().add(printerModel);

        printerModel = new DmLocalSettingPrinter_Mdl();
        printerModel.setPrinterCode("Printer-2");
        printerModel.setPrinterName(txtHPrinter2.getText());
        printerModel.setPrinterImageType((chkBoxPrntr2.getValue()) ? DmEnum.DmPrinterImageType.BYTE : DmEnum.DmPrinterImageType.RGB);

        mrTemp.getDmLocalSettingPrinter_Mdls().add(printerModel);

        printerModel = new DmLocalSettingPrinter_Mdl();
        printerModel.setPrinterCode("Printer-3");
        printerModel.setPrinterName(txtHPrinter3.getText());
        printerModel.setPrinterImageType((chkBoxPrntr3.getValue()) ? DmEnum.DmPrinterImageType.BYTE : DmEnum.DmPrinterImageType.RGB);

        mrTemp.getDmLocalSettingPrinter_Mdls().add(printerModel);

        printerModel = new DmLocalSettingPrinter_Mdl();
        printerModel.setPrinterCode("Printer-4");
        printerModel.setPrinterName(txtHPrinter4.getText());
        printerModel.setPrinterImageType((chkBoxPrntr4.getValue()) ? DmEnum.DmPrinterImageType.BYTE : DmEnum.DmPrinterImageType.RGB);

        mrTemp.getDmLocalSettingPrinter_Mdls().add(printerModel);

        printerModel = new DmLocalSettingPrinter_Mdl();
        printerModel.setPrinterCode("Printer-5");
        printerModel.setPrinterName(txtHPrinter5.getText());
        printerModel.setPrinterImageType((chkBoxPrntr5.getValue()) ? DmEnum.DmPrinterImageType.BYTE : DmEnum.DmPrinterImageType.RGB);

        mrTemp.getDmLocalSettingPrinter_Mdls().add(printerModel);

        return mrTemp;
    }

    private void topControlSave() {

        try {

            final STLocalSetting_Mdl localSetting_Mdl = topControlSaveInitialize();
            LocalSetting_Vld.chkSave(localSetting_Mdl);

            SrvAsync.invokeRPC().getJsonOfSettings(localSetting_Mdl, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        setLocalSettings(result.getData(), true);
                        hide();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void setLocalSettings(String jsonString, final boolean showMessageForSave) {
        try {
            String tempUrl = pacBaseURL + "cmd=savePrinterSettings&data=" + jsonString;
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, tempUrl);
            requestBuilder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        moveRec(showMessageForSave);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    new DmAlertMessageBox(exception.getMessage(), (Button) null).show();
                }
            });
            requestBuilder.send();
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), (Button) null).show();
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {

            case 100:
                new DmAlertMessageBox(errorMessage, txtHPrinter1).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHPrinter2).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHPrinter3).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
        }

    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean isCallEvent) {
        this.popupPanel.hide();

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

//        setPrinterSettings(DmClient.getLocalSettingPrinters());
//
//        setReportMapSettings(DmClient.getLocalSettingPrintMap());
        if (event.getSource().equals(btnSave)) {
            topControlSave();
        } else if (event.getSource().equals(btnCancel)) {
            hide(true);
        }

        if (event.getSource().equals(btnPrnt1)) {

            prntrstgs.show("Printer-1", listSettingPrintMap1);

        } else if (event.getSource().equals(btnPrnt2)) {

            prntrstgs.show("Printer-2", listSettingPrintMap2);

        } else if (event.getSource().equals(btnPrnt3)) {

            prntrstgs.show("Printer-3", listSettingPrintMap3);

        } else if (event.getSource().equals(btnPrnt4)) {

            prntrstgs.show("Printer-4", listSettingPrintMap4);

        } else if (event.getSource().equals(btnPrnt5)) {

            prntrstgs.show("Printer-5", listSettingPrintMap5);

        }
    }

    private void setReportMapSettings(List<DmLocalSettingPrintMap_Mdl> reportMapModels) {
        for (int i = 0; i < reportMapModels.size(); i++) {
            DmLocalSettingPrintMap_Mdl reportModel = reportMapModels.get(i);
            reportModel.setFieldUniqueCode(String.valueOf(reportModel.hashCode()));
            if (reportModel.getPrinterCode().equalsIgnoreCase("printer-1")) {
                if (!listSettingPrintMap1.contains(reportModel)) {
                    listSettingPrintMap1.add(reportModel);
                }
            } else if (reportModel.getPrinterCode().equalsIgnoreCase("printer-2")) {
                if (!listSettingPrintMap2.contains(reportModel)) {
                    listSettingPrintMap2.add(reportModel);
                }
            } else if (reportModel.getPrinterCode().equalsIgnoreCase("printer-3")) {
                if (!listSettingPrintMap3.contains(reportModel)) {
                    listSettingPrintMap3.add(reportModel);
                }
            } else if (reportModel.getPrinterCode().equalsIgnoreCase("printer-4")) {
                if (!listSettingPrintMap4.contains(reportModel)) {
                    listSettingPrintMap4.add(reportModel);
                }
            } else if (reportModel.getPrinterCode().equalsIgnoreCase("printer-5")) {
                if (!listSettingPrintMap5.contains(reportModel)) {
                    listSettingPrintMap5.add(reportModel);
                }
            }
        }
    }

    private void getLocalSettings(final boolean showMessageOfSave) {
        try {
            String tempUrl = pacBaseURL + "cmd=getLocalSettings";
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, tempUrl);
            requestBuilder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        String result = response.getText();
                        loadLocalSettings(result, showMessageOfSave);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    new DmAlertMessageBox(exception.getMessage(), (Button) null).show();
                }
            });
            requestBuilder.send();
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), (Button) null).show();
        }
    }

    private void moveRec(final boolean showMessageOfSave) {

        STClient.pacHandShake(new STClient.IPACHandShake() {
            @Override
            public void onSuccess() {
                isLocalSettingExists(showMessageOfSave);
            }
        });
    }

    private void setPrinterSettings(List<DmLocalSettingPrinter_Mdl> printerModels) {
        for (int i = 0; i < printerModels.size(); i++) {
            DmLocalSettingPrinter_Mdl printerModel = printerModels.get(i);
            if (printerModel.getPrinterCode().equalsIgnoreCase("printer-1")) {
                txtHPrinter1.setText(printerModel.getPrinterName());
                txtHPrinter1.setTag(printerModel.getPrinterCode());
                chkBoxPrntr1.setValue((printerModel.getPrinterImageType() == DmEnum.DmPrinterImageType.BYTE) ? Boolean.TRUE : Boolean.FALSE);
            } else if (printerModel.getPrinterCode().equalsIgnoreCase("printer-2")) {
                txtHPrinter2.setText(printerModel.getPrinterName());
                txtHPrinter2.setTag(printerModel.getPrinterCode());
                chkBoxPrntr2.setValue((printerModel.getPrinterImageType() == DmEnum.DmPrinterImageType.BYTE) ? Boolean.TRUE : Boolean.FALSE);
            } else if (printerModel.getPrinterCode().equalsIgnoreCase("printer-3")) {
                txtHPrinter3.setText(printerModel.getPrinterName());
                txtHPrinter3.setTag(printerModel.getPrinterCode());
                chkBoxPrntr3.setValue((printerModel.getPrinterImageType() == DmEnum.DmPrinterImageType.BYTE) ? Boolean.TRUE : Boolean.FALSE);
            } else if (printerModel.getPrinterCode().equalsIgnoreCase("printer-4")) {
                txtHPrinter4.setText(printerModel.getPrinterName());
                txtHPrinter4.setTag(printerModel.getPrinterCode());
                chkBoxPrntr4.setValue((printerModel.getPrinterImageType() == DmEnum.DmPrinterImageType.BYTE) ? Boolean.TRUE : Boolean.FALSE);
            } else if (printerModel.getPrinterCode().equalsIgnoreCase("printer-5")) {
                txtHPrinter5.setText(printerModel.getPrinterName());
                txtHPrinter5.setTag(printerModel.getPrinterCode());
                chkBoxPrntr5.setValue((printerModel.getPrinterImageType() == DmEnum.DmPrinterImageType.BYTE) ? Boolean.TRUE : Boolean.FALSE);
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

}
