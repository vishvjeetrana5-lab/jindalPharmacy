package aarogya.localSetting.client;

import aarogya.localSetting.shared.GrdLabTestCodification_Mdl;
import aarogya.localSetting.shared.LabMachineConfiguration_Mdl;
import aarogya.localSetting.shared.LocalSetting_Srv;
import aarogya.localSetting.shared.LocalSetting_SrvAsync;
import aarogya.localSetting.shared.LocalSetting_Vld;
import aarogya.projectMenu.mi.shared.LocalSettingsExtendedSaveCommands;
import aarogya.projectMenu.mi.shared.MIConfigCommands;
import aarogya.selfToolkit.stStatic.client.STClient;
import aarogya.selfToolkit.stStatic.shared.STLocalSetting_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
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
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.List;

public class MachineInterfacePopup extends MachineInterfacePopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    private ServiceInvoker<LocalSetting_SrvAsync> SrvAsync;
    private String localSettingsUrl = "";
    private String machineInterfaceSettingsSaveUrl = "";
//    private int newRomIndex;
//    private boolean isAssign;
//    private boolean isCreate;
//    private boolean isDelete;

    public void show() {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
    }

    private void gridEvents() {

        grdLabMachineConfiguration.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                openGridEditPopup(event.getRowIndex(), true, false, true, true, "GridEdit");

            }
        });

        grdLabMachineConfiguration.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    openGridEditPopup(grdLabMachineConfiguration.getGrid().getStore().indexOf(grdLabMachineConfiguration.getGrid().getSelectionModel().getSelectedItem()), true, false, true, true, "GridEdit");
                }
            }

        }, KeyDownEvent.getType());

    }

    public MachineInterfacePopup() {

        super();
        SrvAsync = new ServiceInvoker<LocalSetting_SrvAsync>(GWT.create(LocalSetting_Srv.class), LocalSetting.class.getName());
        toolContainer.setTitle("Machine Interface");

        btnCancel.addClickHandler(this);
        btnSave.addClickHandler(this);
        btnCreate.addClickHandler(this);
        btnAssign.addClickHandler(this);
        btnDelete.addClickHandler(this);
        popupPanel.addAttachHandler(this);
//        pacBaseURL = "http://localhost:" + DmShared.iNull(DmClient.getPACWebPort()).toString() + "/localSettings?";
        localSettingsUrl = "http://localhost:" + DmShared.iNull(DmClient.getPACWebPort()).toString() + "/localSettings?";
        machineInterfaceSettingsSaveUrl = "http://localhost:" + DmShared.iNull(DmClient.getPACWebPort()).toString() + "/machineConfigQuery?";
        moveRec(false);
        gridEvents();

    }

    private void isLocalSettingExists(final boolean showMessageOfSave) {
        try {
//            String tempUrl = pacBaseURL + "cmd=isFileExists";
            String tempUrl = localSettingsUrl + "cmd=isFileExists";
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
        SrvAsync.invokeRPC().getLabMachineModelsFromJsonOfSavedConfigs(settings.getExtendedLocalSettingJsonString(), new AsyncCallback<DmResult<List<LabMachineConfiguration_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<LabMachineConfiguration_Mdl>> result) {
                if (result.isStatus()) {
                    grdLabMachineConfiguration.getGrid().getStore().clear();
                    if (result.getData() != null) {
                        grdLabMachineConfiguration.getGrid().getStore().addAll(result.getData());
                    }
                    grdLabMachineConfiguration.commitChanges();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                }
            }
        });
        // requestToStartMachineInterfaces(settings.getExtendedLocalSettingJsonString());
        if (showMessageOfSave) {
            new DmAlertMessageBox("Machine Interfaces saved successfully ", (DmTextBox) null).show();
        }
    }

    private STLocalSetting_Mdl topControlSaveInitialize() {

        STLocalSetting_Mdl mrTemp = new STLocalSetting_Mdl();

        return mrTemp;
    }

    private void topControlSave() {

        try {
            final STLocalSetting_Mdl localSetting_Mdl = topControlSaveInitialize();
            LocalSetting_Vld.chkSave(localSetting_Mdl);

            SrvAsync.invokeRPC().getJsonOfLabMachineConfigurations(new ArrayList<>(grdLabMachineConfiguration.getGrid().getStore().getAll()), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        localSetting_Mdl.setExtendedLocalSettingJsonString(result.getData());
                        initiateSavingOfLocalSettings(localSetting_Mdl);
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

    private void initiateSavingOfLocalSettings(STLocalSetting_Mdl localSetting_Mdl) {
        SrvAsync.invokeRPC().getJsonOfSettings(localSetting_Mdl, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox("An error occured wlile mapping the printers !", (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    setLocalSettings(result.getData(), true);
                }
            }
        });
    }

    private void setLocalSettings(String jsonString, final boolean showMessageForSave) {
        try {
//            String tempUrl = pacBaseURL + "cmd=saveMISettings&data=" + jsonString + "&" + LocalSettingsExtendedSaveCommands.LSE_StartMachines.toString() + "=" + Boolean.TRUE.toString();
            String tempUrl = machineInterfaceSettingsSaveUrl + "cmd=" + MIConfigCommands.MI_Cmd_SaveMISettings.toString() + "&data=" + jsonString + "&" + LocalSettingsExtendedSaveCommands.LSE_StartMachines.toString() + "=" + Boolean.TRUE.toString();
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

            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
        }

    }

    private void getLocalSettings(final boolean showMessageOfSave) {
        try {
//            String tempUrl = pacBaseURL + "cmd=getLocalSettings";
            String tempUrl = localSettingsUrl + "cmd=getLocalSettings";
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

        if (event.getSource().equals(btnSave)) {
            topControlSave();
        } else if (event.getSource().equals(btnAssign)) {
            LabMachineConfiguration_Mdl labModel = new LabMachineConfiguration_Mdl();
            labModel.setUiHashCode(grdLabMachineConfiguration.getGrid().getStore().size());
            grdLabMachineConfiguration.getGrid().getStore().add(labModel);
            openGridEditPopup(grdLabMachineConfiguration.getGrid().getStore().size() - 1, true, false, false, true, "Assign");
        } else if (event.getSource().equals(btnCreate)) {
            //grdLabMachineConfiguration.getGrid().getStore().add(new LabMachineConfiguration_Mdl());
            LabMachineConfiguration_Mdl labModel = new LabMachineConfiguration_Mdl();
            labModel.setUiHashCode(grdLabMachineConfiguration.getGrid().getStore().size());
            grdLabMachineConfiguration.getGrid().getStore().add(labModel);
            openGridEditPopup(grdLabMachineConfiguration.getGrid().getStore().size() - 1, true, false, false, true, "Create");
        } else if (event.getSource().equals(btnDelete)) {
            openGridEditPopup(grdLabMachineConfiguration.getGrid().getStore().size() - 1, false, false, true, true, "Delete");
        } else if (event.getSource().equals(btnCancel)) {
            hide(true);
        }

    }

    private void openGridEditPopup(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, final boolean isDeleteFocus, String mode) {
        GrdEditMachineConfig<LabMachineConfiguration_Mdl, GrdLabMachineConfiguration_PrpAcs> gridEditPopup = new GrdEditMachineConfig(grdLabMachineConfiguration);

        gridEditPopup.addCallBack(new GrdEditMachineConfig.CallBack() {

            @Override
            public void onOk(LabMachineConfiguration_Mdl machineConfig, String mode, int currentRowIndex) {
                String machineName = "";

                if (mode.equalsIgnoreCase("Delete")) {
                    machineName = machineConfig.getMachineName();
                    for (int i = 0; i < grdLabMachineConfiguration.getGrid().getStore().getAll().size() - 1; i++) {
                        if (!DmShared.xNull(grdLabMachineConfiguration.getGrid().getStore().get(i).getMachineName().trim()).equalsIgnoreCase(machineName)) {
                            grdLabMachineConfiguration.getGrid().getStore().remove(grdLabMachineConfiguration.getGrid().getStore().get(i));
                            break;
                        }
                    }

                    deleteRecordFromSQL(machineName);
                }

                grdLabMachineConfiguration.commitChanges();
            }

            @Override
            public void onClose(String mode, int currentRowIndex) {
                if (mode.equalsIgnoreCase("Assign") || mode.equalsIgnoreCase("Create")) {
                    grdLabMachineConfiguration.getGrid().getStore().remove(currentRowIndex);

                }
            }
        });
        gridEditPopup.show(currentRowIndex, okNeeded, newNeeded, deleteNeeded, isDeleteFocus, mode);

    }

    private void deleteRecordFromSQL(String strKey) {

        SrvAsync.invokeRPC().deleteRecordFromSQL(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {

//                topControlSave();
            }
        });

    }

    @Override
    public void onBlur(BlurEvent event) {

    }

}
