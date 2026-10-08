package aarogya.selfToolkit.fifoPopUp.client;

import aarogya.selfToolkit.fifoPopUp.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class FifoPopUp extends FifoPopUp_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(FifoSetting_Mdl mdl);

        public void close();
    }

    private final ServiceInvoker<FifoPopUp_SrvAsync> SrvAsync;
    private CallBack callBack;
    private boolean isOpenedAsForm = false;
    private FifoSetting_Mdl fifoSettingMdl = new FifoSetting_Mdl();
    private String moduleType = "";
    private Settings settings;
    private SetWidgetInvoker swi;

    public FifoPopUp(UserMenu menuPermission, final SetWidgetInvoker swi, String moduleType) {
        super(swi != null ? true : false, moduleType);
        this.swi = swi;
        SrvAsync = new ServiceInvoker<FifoPopUp_SrvAsync>(GWT.create(FifoPopUp_Srv.class), FifoPopUp.class.getName());
        toolContainer.setTitle(widgetName);
        settings = new Settings(moduleType);
        this.moduleType = moduleType;
        manageEvent();
        initialize(null);
        if (swi != null) {
            flwForm.asWidget().setTitle(widgetName);
            isOpenedAsForm = true;
            swi.setWidget(this);
        }

    }

    public void show(FifoSetting_Mdl mdlDetails) {
        isOpenedAsForm = false;
        popupPanel.clear();
        popupPanel.add(flwForm);
        initialize(mdlDetails);
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                btnSettings.setFocus(true);
            }
        });
    }

    @Override
    public Widget asWidget() {
        if (isOpenedAsForm) {
            return flwForm;
        } else {
            return this.popupPanel;
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide();
        } else if (event.getSource() == btnOk) {
            try {
                startFIFO();
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), btnOk).show();
            }
        } else if (event.getSource().equals(btnSettings)) {
            settings.show(fifoSettingMdl);
        }
    }

    private void startFIFO() {
        try {
            FifoPopUp_Vld.chkSave(fifoSettingMdl);
            SrvAsync.invokeRPC().startFifo(fifoSettingMdl, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        if (callBack != null) {
                            callBack.onOk(fifoSettingMdl);
                        }
                        readLogFile(result.getValue1());
                    } else {
                        labelStatus.setText("Error");
                    }

                }
            });
        } catch (DmException e) {
            showError(e.getErrorNo(), e.getMessage());
        }
    }

    private void showError(int errNo, String errMsg) {

        switch (errNo) {
            case 100:
            case 200:
                new DmAlertMessageBox(errMsg, btnSettings).show();
                break;
            default:
                new DmAlertMessageBox(errMsg, btnSettings).show();
                break;
        }

    }

    private void readLogFile(String filePath) {
        SrvAsync.invokeRPC().readLogFile(filePath, new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    txtLog.setText(result.getData().getSharedKeyFieldName());
                    labelStatus.setText("Completed");
                    new DmAlertMessageBox("FIFO Process Completed", (DmTextBox) null).show();
                } else {
                    labelStatus.setText("Error");
                }
            }
        });
    }

    private void initialize(FifoSetting_Mdl settingValues) {
        if (settingValues != null) {
            fifoSettingMdl.setItemCode(DmShared.xNull(settingValues.getItemCode()));
            fifoSettingMdl.setItemName(DmShared.xNull(settingValues.getItemName()));
            fifoSettingMdl.setGroupCode(DmShared.xNull(settingValues.getGroupCode()));
            fifoSettingMdl.setGroupName(DmShared.xNull(settingValues.getGroupName()));
            fifoSettingMdl.setDepartmentCode(DmShared.xNull(settingValues.getDepartmentCode()));
            fifoSettingMdl.setDepartmentName(DmShared.xNull(settingValues.getDepartmentName()));
            fifoSettingMdl.setCategoryCode(DmShared.xNull(settingValues.getCategoryCode()));
            fifoSettingMdl.setCategoryName(DmShared.xNull(settingValues.getCategoryName()));
            fifoSettingMdl.setModuleType(DmShared.xNull(settingValues.getModuleType()));
            fifoSettingMdl.setSiteCode(DmShared.xNull(settingValues.getSiteCode()));
            fifoSettingMdl.setSiteName(DmShared.xNull(settingValues.getSiteName()));
        } else {
            fifoSettingMdl.setModuleType(DmShared.xNull(moduleType));
        }
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCallBackRequired) {
        if (!isOpenedAsForm) {
            this.popupPanel.hide();
            if (isCallBackRequired) {
                callBack.close();
            }
        } else {
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(FifoPopUp.this);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            hide();
        }
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void manageEvent() {
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        flwForm.addAttachHandler(this);
        btnSettings.addClickHandler(this);

        settings.addCallBack(new Settings.CallBack() {
            @Override
            public void onOk(FifoSetting_Mdl values) {
                fifoSettingMdl = values;
            }
        });
    }

}
