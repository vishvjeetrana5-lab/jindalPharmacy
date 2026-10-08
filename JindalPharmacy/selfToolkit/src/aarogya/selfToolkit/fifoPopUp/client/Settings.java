package aarogya.selfToolkit.fifoPopUp.client;

import aarogya.selfToolkit.fifoPopUp.shared.ClsVar;
import aarogya.selfToolkit.fifoPopUp.shared.FifoSetting_Mdl;
import aarogya.selfToolkit.fifoPopUp.shared.FifoPopUp_Srv;
import aarogya.selfToolkit.fifoPopUp.shared.FifoPopUp_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;

public class Settings extends Settings_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler {

    interface CallBack {

        public void onOk(FifoSetting_Mdl values);
    }
    CallBack callBack;
    FifoSetting_Mdl setting_Mdl;
    private final ServiceInvoker<FifoPopUp_SrvAsync> SrvAsync;
    String siteCode = "", siteName = "", moduleType = "";

    public Settings(String moduleType) {
        super(moduleType);
        SrvAsync = new ServiceInvoker<FifoPopUp_SrvAsync>(GWT.create(FifoPopUp_Srv.class), FifoPopUp.class.getName());
        textHelpEvents();
        handlerEvents();
        toolContainer.setTitle("Settings");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        if (moduleType.equalsIgnoreCase(ClsVar.moduleTypePharmacy_Code)) {
            txtHModuleType.setTag(ClsVar.moduleTypePharmacy_Code);
            txtHModuleType.setText(ClsVar.moduleTypePharmacy_Name);
        } else if (moduleType.equalsIgnoreCase(ClsVar.moduleTypeInventory_Code)) {
            txtHModuleType.setTag(ClsVar.moduleTypeInventory_Code);
            txtHModuleType.setText(ClsVar.moduleTypeInventory_Name);
        }

    }

    private void setDetails(FifoSetting_Mdl mdlDetails) {
        clear();
        txtHCat.setTag(DmShared.xNull(mdlDetails.getCategoryCode()));
        txtHCat.setText(DmShared.xNull(mdlDetails.getCategoryName()));
        txtHDepartment.setTag(DmShared.xNull(mdlDetails.getDepartmentCode()));
        txtHDepartment.setText(DmShared.xNull(mdlDetails.getDepartmentName()));
        txtHGroup.setText(DmShared.xNull(mdlDetails.getGroupName()));
        txtHGroup.setTag(DmShared.xNull(mdlDetails.getGroupCode()));
        txtHItemName.setTag(DmShared.xNull(mdlDetails.getItemCode()));
        txtHItemName.setText(DmShared.xNull(mdlDetails.getItemName()));
        siteCode = DmShared.xNull(mdlDetails.getSiteCode());
        siteName = DmShared.xNull(mdlDetails.getSiteName());
    }

    private void clear() {

        txtHItemName.setText("");
        txtHItemName.setTag("");
        txtHItemName.setTag2nd("");

        txtHCat.setText("");
        txtHCat.setTag("");
        txtHCat.setTag2nd("");

        txtHDepartment.setText("");
        txtHDepartment.setTag("");
        txtHDepartment.setTag2nd("");

        txtHGroup.setText("");
        txtHGroup.setTag("");
        txtHGroup.setTag2nd("");

        siteCode = "";
        siteName = "";

    }

    private void handlerEvents() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onBlur(BlurEvent event) {
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
            this.hide();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide();
        } else if (event.getSource() == btnOk) {
            if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")
                    && DmShared.xNull(txtHGroup.getTag()).equalsIgnoreCase("")
                    && DmShared.xNull(txtHDepartment.getTag()).equalsIgnoreCase("")
                    && DmShared.xNull(txtHCat.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("At least one parameter selection is mandatory!", txtHItemName).show();
                return;
            }

            fillRecords();
        }
    }

    private void fillRecords() {
        setting_Mdl = new FifoSetting_Mdl();
        setting_Mdl.setItemCode(DmShared.xNull(txtHItemName.getTag()));
        setting_Mdl.setItemName(DmShared.xNull(txtHItemName.getText()));
        setting_Mdl.setGroupCode(DmShared.xNull(txtHGroup.getTag()));
        setting_Mdl.setGroupName(DmShared.xNull(txtHGroup.getText()));
        setting_Mdl.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        setting_Mdl.setDepartmentName(DmShared.xNull(txtHDepartment.getText()));
        setting_Mdl.setCategoryCode(DmShared.xNull(txtHCat.getTag()));
        setting_Mdl.setCategoryName(DmShared.xNull(txtHCat.getText()));
        setting_Mdl.setModuleType(DmShared.xNull(txtHModuleType.getTag()));
        setting_Mdl.setSiteCode(DmShared.xNull(siteCode));
        setting_Mdl.setSiteName(DmShared.xNull(siteName));
        callBack.onOk(setting_Mdl);
        hide();
    }

    public void show(FifoSetting_Mdl mdlDetails) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        setDetails(mdlDetails);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHItemName.setFocus(true);
            }
        });
    }

    public void hide() {
        if (callBack != null) {
            this.popupPanel.hide();
        }
    }

    private void textHelpEvents() {
        txtHDepartment.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(DmShared.xNull(txtHModuleType.getTag()).equalsIgnoreCase("SM") ? "PR" : "PH", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHCat.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCat(txtHModuleType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCat).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCat.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCat).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHGroup(txtHModuleType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGroup).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHItemName.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItemName(txtHModuleType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHItemName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHModuleType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHModuleType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHModuleType).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHModuleType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHModuleType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
    }

}
