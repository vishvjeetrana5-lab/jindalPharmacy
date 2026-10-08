package aarogya.localSetting.client;

import aarogya.localSetting.shared.GrdLabTestCodification_Mdl;
import aarogya.localSetting.shared.LabMachineConfiguration_Mdl;
import aarogya.localSetting.shared.LocalSetting_Srv;
import aarogya.localSetting.shared.LocalSetting_SrvAsync;
import aarogya.localSetting.shared.TxtHMachineSpecification_Mdl;
import aarogya.projectMenu.mi.shared.MIConfigCommands;
import aarogya.projectMenu.mi.shared.MIConfigEnum;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class GrdEditMachineConfig<T1, T2> extends GrdEditMachineConfig_Dsg<T1, T2> implements DmRootEvents, AttachEvent.Handler {

    interface CallBack {

        public void onOk(LabMachineConfiguration_Mdl machineConfig, String mode, int currentRowIndex);

        public void onClose(String mode, int currentRowIndex);
    }

    private CallBack callBack;
    private final GrdEditTestCodification<GrdLabTestCodification_Mdl, GrdLabTestCodification_PrpAcs> gridEditPopup = new GrdEditTestCodification(grdLabTestCodification);
    private String machineConfigBaseUrl;
    private final ServiceInvoker<LocalSetting_SrvAsync> SrvAsync;
    private int hashCode = 0;
    String mode;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
            callBack.onClose(this.mode, this.getCurrentRowIndex());
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            if (!this.mode.equalsIgnoreCase("Delete")) {
                if ((!DmShared.removeSpace(txtMachineName.getText().trim()).equalsIgnoreCase("") || DmShared.removeSpace(txtHMachineName.getText().trim()).equalsIgnoreCase(""))) {
                    onOk();
                }
            }
        }

    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, final boolean isDeleteFocus, String mode) {
        setTitle(mode);
        this.mode = mode;

        flwDynamicMain.clear();
        flwDynamic.clear();

        if (this.mode.equalsIgnoreCase("Assign")) {
            flwDynamic.add(vl2.add(txtHMachineName));

        }
        if (this.mode.equalsIgnoreCase("Delete")) {
            flwDynamic.add(vl2.add(txtHMachineName));
            clearData();

        }

        if (this.mode.equalsIgnoreCase("Create") || this.mode.equalsIgnoreCase("GridEdit")) {
            flwDynamic.add(vl2.add(txtMachineName));

        }
        if (this.mode.equalsIgnoreCase("Create") || this.mode.equalsIgnoreCase("Assign")) {
            clearData();

        }

        if (this.mode.equalsIgnoreCase("Create")) {
            txtMachineName.setEnabled(true);

        } else {
            txtMachineName.setEnabled(false);

        }
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);

        if (this.mode.equalsIgnoreCase("GridEdit")) {
            super.setDeleteCaptionToDiscard();

        } else if (this.mode.equalsIgnoreCase("Delete")) {
            super.setDiscardCaptionToDelete();

        }

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtMachineName.setFocus(true);
                txtHMachineName.setFocus(true);

            }
        });
    }

    private void clearData() {
        txtComPortNo.setText("");
        txtBaudRate.setText("");
        txtStopBit.setText("");
        txtDataBit.setText("");
        txtParityStatus.setText("");
        txtHardwareControl.setText("");
        txtIPAddress.setText("");
        txtPortNo.setText("");
        txtHMachineName.setText("");
        txtHMachineName.setTag("");

        txtHModelNo.setText("");
        txtHModelNo.setTag("");

        txtMachineCode.setText("");
        txtCompanyName.setText("");
        txtCommunicationMode.setText("");
        txtTransmissionMode.setText("");
        txtProtocol.setText("");
        txtMachineName.setText("");

        grdLabTestCodification.getGrid().getStore().clear();
        grdLabTestCodification.getGrid().getStore().add(new GrdLabTestCodification_Mdl());
    }

    @Override
    public void displayRow(T1 row) {

        if (mode.equalsIgnoreCase("GridEdit")) {
            LabMachineConfiguration_Mdl ourRow = (LabMachineConfiguration_Mdl) row;
            if (!DmShared.xNull(ourRow.getMachineName()).trim().equalsIgnoreCase("")) {
                txtHMachineName.setText(DmShared.xNull(ourRow.getMachineName()).trim());
                grdLabTestCodification.getGrid().getStore().clear();
                getData();
            }

        }
    }

    private void textHelpEvents() {
        txtMachineName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestion(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtMachineName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtMachineName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtMachineName).show();
                        }
                    }
                });
            }
        });
        txtHModelNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMachineSpecification_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMachineSpecification(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMachineSpecification_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHModelNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMachineSpecification_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHModelNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHModelNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMachineSpecification_Mdl tRow) {
                if (blnStatus) {
                    if (tRow != null) {

                        txtCompanyName.setText(tRow.getCompanyName());
                        txtCommunicationMode.setText(tRow.getCommunicationMode());
                        txtTransmissionMode.setText(tRow.getTransmissionMode());
                        txtProtocol.setText(tRow.getProtocoll());
                        txtMachineCode.setText(tRow.getSharedKeyFieldCode());

                        flwDynamicMain.clear();
                        if (tRow.getCommunicationMode().equalsIgnoreCase("TCP")) {
                            flwDynamicMain.add(flwFrameTCP);

                            txtComPortNo.setText("");
                            txtBaudRate.setText("");
                            txtStopBit.setText("");
                            txtDataBit.setText("");
                            txtParityStatus.setText("");
                            txtHardwareControl.setText("");

                            txtIPAddress.setFocus(true);
                        } else if (tRow.getCommunicationMode().equalsIgnoreCase("Serial")) {

                            flwDynamicMain.add(flwFrameASTM);

                            txtIPAddress.setText("");
                            txtPortNo.setText("");

                            txtComPortNo.setFocus(true);
                        }

                        fetchMachineDetails(tRow.getSharedKeyFieldCode());
                    }
                }
            }
        });
        txtHMachineName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMachineName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMachineName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMachineName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMachineName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (!DmShared.xNull(txtHMachineName.getTag()).equalsIgnoreCase("")) {
                    getData();
                    grdLabTestCodification.getGrid().getStore().add(new GrdLabTestCodification_Mdl());
                }
            }
        });
    }

    private void fetchMachineDetails(String machineConfiguration) {
        try {
            String tempUrl = machineConfigBaseUrl + "cmd=" + MIConfigCommands.MI_Cmd_FetchMachineDetails.toString() + "&configName=" + machineConfiguration;
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, tempUrl);
            requestBuilder.setCallback(new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        parseMachineDetails(response.getText());
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

    private void parseMachineDetails(String responseJsonString) {
        try {
            SrvAsync.invokeRPC().parseMachineDetails(responseJsonString, new AsyncCallback<DmResult<LabMachineConfiguration_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
                }

                @Override
                public void onSuccess(DmResult<LabMachineConfiguration_Mdl> result) {
                    if (result.isStatus()) {
                        fillMachineDetails(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                    }
                }
            });
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), (Button) null).show();
        }
    }

    private void fillMachineDetails(LabMachineConfiguration_Mdl machineConfigModel) {
        try {
            txtMachineCode.setText(DmShared.xNull(machineConfigModel.getMachineCode()).trim());

            if (machineConfigModel.getCommunicationMode().equalsIgnoreCase("tcp")) {
                String machineIp = "", machinePort = "";
                if (!machineConfigModel.getPortNo().equalsIgnoreCase("")) {
                    String tempPortString = DmShared.xNull(machineConfigModel.getPortNo().toString()).trim();
                    int indexOfColon = tempPortString.indexOf(":");
                    if (indexOfColon >= 0) {
                        String[] portDetails = tempPortString.split("\\:");
                        machineIp = portDetails[0].trim();
                        machinePort = portDetails[1].trim();
                    } else {
                        machinePort = tempPortString;
                    }
                }
                if (!DmShared.xNull(machineIp).equalsIgnoreCase("")) {
                    txtIPAddress.setText(machineIp);
                }
                if (!DmShared.xNull(machinePort).equalsIgnoreCase("")) {
                    txtPortNo.setText(machinePort);
                }
            } else if (machineConfigModel.getCommunicationMode().equalsIgnoreCase("serial")) {
                txtComPortNo.setText("");
                if (!DmShared.xNull(machineConfigModel.getBaudRate()).trim().equalsIgnoreCase("")) {
                    txtBaudRate.setText(DmShared.xNull(machineConfigModel.getBaudRate()).trim());
                }
                if (!DmShared.xNull(machineConfigModel.getStopBit()).trim().equalsIgnoreCase("")) {
                    txtStopBit.setText(DmShared.xNull(machineConfigModel.getStopBit()).trim());
                }
                if (!DmShared.xNull(machineConfigModel.getDataBit()).trim().equals("")) {
                    txtDataBit.setText(DmShared.xNull(machineConfigModel.getDataBit()).trim());
                }
                if (!DmShared.xNull(machineConfigModel.getParity()).trim().equals("")) {
                    txtParityStatus.setText(DmShared.xNull(machineConfigModel.getParity()).trim());
                }
                if (!DmShared.xNull(machineConfigModel.getHardwareControl()).trim().equals("")) {
                    txtHardwareControl.setText(DmShared.xNull(machineConfigModel.getHardwareControl()).trim());
                }
            }
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), (Button) null).show();
        }
    }

    @Override
    public T1 getNewRow() {
        LabMachineConfiguration_Mdl labModel = new LabMachineConfiguration_Mdl();
        labModel.setUiHashCode(getDmGrid().getGrid().getStore().size());
        return (T1) labModel;
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtCommunicationMode.getText()).equalsIgnoreCase("tcp")) {
                if (DmShared.xNull(txtIPAddress.getText().trim()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("IP Address is mandatory ", txtIPAddress).show();
                    return;
                }
                if (DmShared.xNull(txtPortNo.getText().trim()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Port No. By is mandatory ", txtPortNo).show();
                    return;
                }
            } else if (DmShared.xNull(txtCommunicationMode.getText()).equalsIgnoreCase("serial")) {
                if (DmShared.xNull(txtComPortNo.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("COM Port No. is mandatory ", txtComPortNo).show();
                    return;
                }
                if (DmShared.xNull(txtBaudRate.getText().trim()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Baud Rate is mandatory ", txtBaudRate).show();
                    return;
                }
                if (DmShared.xNull(txtStopBit.getText().trim()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Stop Bit is mandatory ", txtStopBit).show();
                    return;
                }
                if (DmShared.xNull(txtDataBit.getText().trim()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Data Bit is mandatory ", txtDataBit).show();
                    return;
                }
                if (DmShared.xNull(txtParityStatus.getText().trim()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Parity is mandatory ", txtParityStatus).show();
                    return;
                }
                if (DmShared.xNull(txtHardwareControl.getText().trim()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Hardware Control is mandatory ", txtHardwareControl).show();
                    return;
                }
            }

            //====================== Conditional Checks ===================================
            if (this.mode.equalsIgnoreCase("Assign") || this.mode.equalsIgnoreCase("GridEdit")) {
                if (this.mode.equalsIgnoreCase("Assign")) {
                    if (DmShared.xNull(txtHMachineName.getText().trim()).equalsIgnoreCase("")) {
                        new DmAlertMessageBox("Machine Name is mandatory ", txtHMachineName).show();
                        return;
                    }
                }
                if (this.mode.equalsIgnoreCase("Assign")) {
                    for (LabMachineConfiguration_Mdl service : (List<LabMachineConfiguration_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                        if (service.getMachineName() != null) {
                            if (service.getMachineName().equalsIgnoreCase(DmShared.xNull(txtHMachineName.getText()))) {
                                new DmAlertMessageBox("Machine Name already exist.", txtHMachineName).show();
                                return;
                            }
                        }
                    }
                }
                setValue();
            }
            if (this.mode.equalsIgnoreCase("Create")) {
                if (DmShared.xNull(txtMachineName.getText().trim()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Machine Name is mandatory ", txtMachineName).show();
                    return;
                }
                if (DmShared.xNull(txtHModelNo.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Model No. is mandatory ", txtHModelNo).show();
                    return;
                }
                setValue();
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtMachineName).show();
        }
    }

    public void setValue() {
        LabMachineConfiguration_Mdl ourRow = new LabMachineConfiguration_Mdl();

        ourRow.setMachineName(DmShared.xNull(DmShared.xNull((mode.equalsIgnoreCase("GridEdit") || mode.equalsIgnoreCase("Create")) ? txtMachineName.getText() : txtHMachineName.getText())));
        ourRow.setMachineCode(DmShared.xNull(txtMachineCode.getText()));
        ourRow.setModelNo(DmShared.xNull(txtHModelNo.getText()));
        ourRow.setCompanyName(DmShared.xNull(txtCompanyName.getText()));
        ourRow.setCommunicationMode(DmShared.xNull(txtCommunicationMode.getText()));
        ourRow.setTransmissionMode(DmShared.xNull(txtTransmissionMode.getText()));
        ourRow.setProtocoll(DmShared.xNull(txtProtocol.getText()));
        ourRow.setIpAddress(DmShared.xNull(txtIPAddress.getText()));
        ourRow.setPortNo(DmShared.xNull(txtPortNo.getText()));
        ourRow.setComPortNo(DmShared.xNull(txtComPortNo.getText()));
        ourRow.setBaudRate(DmShared.xNull(txtBaudRate.getText()));
        ourRow.setStopBit(DmShared.xNull(txtStopBit.getText()));
        ourRow.setDataBit(DmShared.xNull(txtDataBit.getText()));
        ourRow.setParity(DmShared.xNull(txtParityStatus.getText()));
        ourRow.setHardwareControl(DmShared.xNull(txtHardwareControl.getText()));
        ourRow.setMode(DmShared.xNull(mode));

        ourRow.getGrdData().clear();
        ourRow.getGrdData().addAll(grdLabTestCodification.getGrid().getStore().getAll());

        saveRecord(ourRow);
    }

    public void getData() {
        SrvAsync.invokeRPC().getData(txtHMachineName.getText(), new AsyncCallback<DmResult<LabMachineConfiguration_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<LabMachineConfiguration_Mdl> result) {

                LabMachineConfiguration_Mdl ourRow;
                ourRow = result.getData();

                txtMachineName.setText(DmShared.xNull(ourRow.getMachineName()));
                txtMachineCode.setText(DmShared.xNull(ourRow.getMachineCode()));

                MIConfigEnum configEnum = MIConfigEnum.getConfiguration(DmShared.xNull(txtMachineCode.getText()));

                txtCommunicationMode.setText(DmShared.xNull(configEnum.getCommunicationMode()));
                txtTransmissionMode.setText(DmShared.xNull(configEnum.getTransmissionMode()));
                txtProtocol.setText(DmShared.xNull(configEnum.getProtocol()));
                txtCompanyName.setText(DmShared.xNull(configEnum.getMachineCompany()));
                txtHModelNo.setText(DmShared.xNull(configEnum.getMachineModel()));
                txtHModelNo.setTag(DmShared.xNull(configEnum.getConfigurationName()));

                if (DmShared.xNull(configEnum.getCommunicationMode()).equalsIgnoreCase("TCP")) {
                    flwDynamicMain.clear();
                    flwDynamicMain.add(flwFrameTCP);

                    txtComPortNo.setText("");
                    txtBaudRate.setText("");
                    txtStopBit.setText("");
                    txtDataBit.setText("");
                    txtParityStatus.setText("");
                    txtHardwareControl.setText("");

                    txtIPAddress.setText(DmShared.xNull(ourRow.getIpAddress()));
                    txtPortNo.setText(DmShared.xNull(ourRow.getPortNo()));

                    txtIPAddress.setFocus(true);

                }
                if (DmShared.xNull(configEnum.getCommunicationMode()).equalsIgnoreCase("Serial")) {
                    flwDynamicMain.clear();
                    flwDynamicMain.add(flwFrameASTM);

                    txtIPAddress.setText("");
                    txtPortNo.setText("");

                    txtComPortNo.setText(DmShared.xNull(ourRow.getComPortNo()));
                    txtBaudRate.setText(DmShared.xNull(ourRow.getBaudRate()));
                    txtStopBit.setText(DmShared.xNull(ourRow.getStopBit()));
                    txtDataBit.setText(DmShared.xNull(ourRow.getDataBit()));
                    txtParityStatus.setText(DmShared.xNull(ourRow.getParity()));
                    txtHardwareControl.setText(DmShared.xNull(ourRow.getHardwareControl()));

                    txtComPortNo.setFocus(true);
                }

                grdLabTestCodification.getGrid().getStore().clear();
                grdLabTestCodification.getGrid().getStore().addAll(ourRow.getGrdData());

                grdLabTestCodification.getGrid().getStore().add(new GrdLabTestCodification_Mdl());

            }
        });
    }

    public void saveRecord(final LabMachineConfiguration_Mdl mrTemp) {
        SrvAsync.invokeRPC().saveRecordToSQL(mrTemp, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtMachineName).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    proceedOk();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtMachineName).show();
                }
            }
        });
    }

    @Override
    public void manageOk(T1 row) {

        LabMachineConfiguration_Mdl ourRow = (LabMachineConfiguration_Mdl) row;

        ourRow.setMachineName(DmShared.removeSpace(DmShared.xNull((mode.equalsIgnoreCase("GridEdit") || mode.equalsIgnoreCase("Create")) ? txtMachineName.getText().trim() : txtHMachineName.getText().trim())));
        ourRow.setMachineCode(DmShared.removeSpace(DmShared.xNull(txtMachineCode.getText().trim())));
        ourRow.setSharedKeyFieldCode(ourRow.getMachineCode());
        ourRow.setSharedKeyFieldName(ourRow.getMachineName());
        ourRow.setModelNo(DmShared.removeSpace(DmShared.xNull(txtHModelNo.getText().trim())));
        ourRow.setCompanyName(DmShared.removeSpace(DmShared.xNull(txtCompanyName.getText())));
        ourRow.setCommunicationMode(DmShared.removeSpace(DmShared.xNull(txtCommunicationMode.getText().trim())));
        ourRow.setTransmissionMode(DmShared.removeSpace(DmShared.xNull(txtTransmissionMode.getText().trim())));
        ourRow.setProtocoll(DmShared.removeSpace(DmShared.xNull(txtProtocol.getText().trim())));
        ourRow.setIpAddress(DmShared.removeSpace(DmShared.xNull(txtIPAddress.getText().trim())));
        ourRow.setPortNo(DmShared.removeSpace(DmShared.xNull(txtPortNo.getText().trim())));
        ourRow.setComPortNo(DmShared.removeSpace(DmShared.xNull(txtComPortNo.getText().trim())));
        ourRow.setBaudRate(DmShared.removeSpace(DmShared.xNull(txtBaudRate.getText().trim())));
        ourRow.setStopBit(DmShared.removeSpace(DmShared.xNull(txtStopBit.getText().trim())));
        ourRow.setDataBit(DmShared.removeSpace(DmShared.xNull(txtDataBit.getText().trim())));
        ourRow.setParity(DmShared.removeSpace(DmShared.xNull(txtParityStatus.getText().trim())));
        ourRow.setHardwareControl(DmShared.removeSpace(DmShared.xNull(txtHardwareControl.getText().trim())));

        ourRow.getGrdData().clear();

        for (int i = 0; i < grdLabTestCodification.getGrid().getStore().getAll().size() - 1; i++) {
            if (!DmShared.xNull(grdLabTestCodification.getGrid().getStore().get(i).getMachineTestName().trim()).equalsIgnoreCase("")) {
                ourRow.getGrdData().add(grdLabTestCodification.getGrid().getStore().get(i));
            }
        }

        //ourRow.getGrdData().addAll(grdLabTestCodification.getGrid().getStore().getAll());
        callBack.onOk(ourRow, this.mode, this.getCurrentRowIndex());
        clearData();
        hide();
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        if (this.mode.equalsIgnoreCase("Delete")) {

            if (DmShared.xNull(txtHMachineName.getText().trim()).equalsIgnoreCase("")) {

                new DmAlertMessageBox("Select Machine Name", txtHMachineName).show();

                return;
            }

            if (!DmShared.xNull(txtHMachineName.getTag()).equalsIgnoreCase("")) {
                new DmConfirmMessageBox("Are you sure to delete < " + txtHMachineName.getTag() + " > Machine from Database?", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            hide();
                            LabMachineConfiguration_Mdl lmcm = new LabMachineConfiguration_Mdl();
                            lmcm.setMachineName(DmShared.xNull(txtHMachineName.getTag()));
                            callBack.onOk(lmcm, GrdEditMachineConfig.this.mode, GrdEditMachineConfig.this.getCurrentRowIndex());
                        }
                    }
                }).show();

            }
        } else if (!this.mode.equalsIgnoreCase("Delete")) {
            proceedDelete();
        }

    }

    @Override
    public void onClose() {
        hide();
        callBack.onClose(this.mode, this.getCurrentRowIndex());
    }

    @Override
    public void manageDelete() {
        callBack.onOk(new LabMachineConfiguration_Mdl(), GrdEditMachineConfig.this.mode, GrdEditMachineConfig.this.getCurrentRowIndex());
    }

    private void showPopUp(int currentRowIndex) {
        gridEditPopup.show(currentRowIndex, true, true, true, true);
    }

    private void gridEvents() {
        gridEditPopup.addCallBack(new GrdEditTestCodification.CallBack() {

            @Override
            public void onOk(GrdLabTestCodification_Mdl configDetail) {
                grdLabTestCodification.commitChanges();
            }

            @Override
            public void onDelete() {
                grdLabTestCodification.commitChanges();
            }
        });
        grdLabTestCodification.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showPopUp(event.getRowIndex());
            }
        });
        grdLabTestCodification.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showPopUp(grdLabTestCodification.getGrid().getStore().indexOf(grdLabTestCodification.getGrid().getSelectionModel().getSelectedItem()));
                }
            }

        }, KeyDownEvent.getType());
    }

    public GrdEditMachineConfig(DmGrid<T1, T2> grid) {
        super(grid, "Machine Interface", "560px", "365px");
        machineConfigBaseUrl = "http://localhost:" + DmShared.iNull(DmClient.getPACWebPort()).toString() + "/machineConfigQuery?";
        SrvAsync = new ServiceInvoker<LocalSetting_SrvAsync>(GWT.create(LocalSetting_Srv.class), LocalSetting.class.getName());
        textHelpEvents();
        gridEvents();
        setControls();
        flwForm.addAttachHandler(this);
    }

    public String getMachineConfigurationBaseUrl() {
        return DmShared.xNull(machineConfigBaseUrl);
    }

    private void setControls() {

        txtCompanyName.setEnabled(false);
        txtCommunicationMode.setEnabled(false);
        txtTransmissionMode.setEnabled(false);
        txtProtocol.setEnabled(false);
        txtMachineCode.setEnabled(false);

        grdLabTestCodification.getGrid().getStore().add(new GrdLabTestCodification_Mdl());

    }
}
