package aarogya.pharmacy.irpInfoPopup.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import aarogya.pharmacy.irpInfoPopup.shared.IrpInfoPopup_Mdl;
import aarogya.pharmacy.irpInfoPopup.shared.IrpInfoPopup_Srv;
import aarogya.pharmacy.irpInfoPopup.shared.IrpInfoPopup_SrvAsync;
import aarogya.pharmacy.irpInfoPopup.shared.IrpInfoPopup_Vld;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class IrpInfoPopup extends IrpInfoPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents {

    private CallBack callBack;
    private final ServiceInvoker<IrpInfoPopup_SrvAsync> srvAsync;
    private final IrpInfoPopup_Mdl prevRecord = new IrpInfoPopup_Mdl();
    private final boolean isIrpTypeEnable;
    private DmEnum.IRP_Type irp_Type;

    private void onOk() {
        try {
            final IrpInfoPopup_Mdl mrTemp = saveInitialize();
            IrpInfoPopup_Vld.chkSave(mrTemp);
            saveIrpInfo(mrTemp);
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHIrpName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHIrpType).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtUrl).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHIsActive).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHIrpName).show();
                break;
        }
    }

    public void show(UIObject showRelativeTo, DmEnum.IRP_Type irp_Type, String irpCode) {
        clear();
        this.irp_Type = irp_Type;
        if (!DmShared.xNull(irpCode).trim().equalsIgnoreCase("")) {
            getMoverec(irpCode);
        } else {
            onNew();
        }
        manageTopContainer();
        popupPanel.show();
        popupPanel.showRelativeTo(showRelativeTo);
        popupPanel.setGlassEnabled(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHIrpName.setFocus(true);
            }
        });
    }

    private void displayRow(IrpInfoPopup_Mdl mrTemp) {
        clear();
        if (mrTemp != null && !DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
            txtHIrpType.setTag(DmShared.xNull(mrTemp.getIrpTypeTag()));
            txtHIrpType.setText(DmShared.xNull(mrTemp.getIrpTypeText()));
            txtUrl.setText(DmShared.xNull(mrTemp.getUrl()));
            txtHIrpName.setTag(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            txtHIrpName.setTag2nd(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            txtHIrpName.setText(DmShared.xNull(mrTemp.getSharedKeyFieldName()));

            txtHIsActive.setTag(DmShared.xNull(mrTemp.getIsActiveCode()));
            txtHIsActive.setText(DmShared.xNull(mrTemp.getIsActiveText()));
        }
    }

    private void onNew() {
        clear();
        manageDisplay();
        setDefaultValue();
        manageTopContainer();
    }

    private void onDelete() {
        new DmConfirmMessageBox("Are you sure that you want to delete this IRP.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    srvAsync.invokeRPC(true).deleteIrpInfo(DmShared.xNull(txtHIrpName.getTag()), new AsyncCallback<DmResult<String>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                        }

                        @Override
                        public void onSuccess(DmResult<String> result) {
                            if (result.isStatus()) {
                                callBack.onDelete(DmShared.iNull(txtHIrpType.getTag()).intValue());
                                hide();
                            } else {
                                new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                            }
                        }
                    });
                }
            }
        }).show();
    }

    private void manageDisplay() {
        txtHIrpType.setEnabled(isIrpTypeEnable);
    }

    private void getMoverec(String irpCode) {
        srvAsync.invokeRPC(true).getMoveRec(irpCode, new AsyncCallback<DmResult<IrpInfoPopup_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<IrpInfoPopup_Mdl> result) {
                if (result.isStatus()) {
                    if (result.getData() != null) {
                        result.getData().copy(prevRecord);
                        displayRow(result.getData());
                        manageDisplay();
                        manageTopContainer();
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void setDefaultValue() {
        if (irp_Type != null) {
            txtHIrpType.setTag(DmShared.iNull(irp_Type.getCode()).toString());
            txtHIrpType.setTag2nd(DmShared.iNull(irp_Type.getCode()).toString());
            txtHIrpType.setText(DmShared.xNull(irp_Type.getValue()));
        }
        txtHIsActive.setTag("1");
        txtHIsActive.setText("Yes");
    }

    public interface CallBack {

        public void onOk(int irpTypeCode, IrpInfoPopup_Mdl iipm);

        public void onClose();

        public void onDelete(int irpTypeCode);

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public IrpInfoPopup(boolean isIrpTypeEnable) {
        super();
        this.isIrpTypeEnable = isIrpTypeEnable;
        srvAsync = new ServiceInvoker<>(GWT.create(IrpInfoPopup_Srv.class), IrpInfoPopup.class.getName());
        toolContainer.setTitle(widgetName);
        manageEvent();
        txtHelpEvents();
    }

    private void clear() {

        txtHIrpType.setTag("");
        txtHIrpType.setText("");
        txtHIrpType.setTag2nd("");

        txtHIrpName.setTag("");
        txtHIrpName.setText("");
        resetIrpName();

    }

    private void resetIrpName() {
        txtHIrpName.setTag2nd("");

        txtUrl.setText("");
        txtHIsActive.setTag("");
        txtHIsActive.setTag("");
        txtHIsActive.setText("");
    }

    private void manageEvent() {
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        btnDelete.addClickHandler(this);
    }

    private void txtHelpEvents() {
        txtHIrpType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHIrpType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIrpType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIrpType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIrpType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIrpName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<IrpInfoPopup_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHIrpName(txtHIrpType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<IrpInfoPopup_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIrpName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<IrpInfoPopup_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHIrpName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIrpName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, IrpInfoPopup_Mdl t1Row) {
                if (!DmShared.xNull(txtHIrpName.getTag()).equalsIgnoreCase(DmShared.xNull(txtHIrpName.getTag2nd()))) {
                    resetIrpName();
                    txtHIrpName.setTag2nd(DmShared.xNull(txtHIrpName.getTag()));
                }
                if (blnStatus && t1Row != null) {
                    txtUrl.setText(DmShared.xNull(t1Row.getUrl()));
                    txtHIsActive.setTag(DmShared.xNull(t1Row.getIsActiveCode()));
                    txtHIsActive.setText(DmShared.xNull(t1Row.getIsActiveText()));
                }
                if (DmShared.xNull(txtHIrpName.getTag()).trim().equalsIgnoreCase("")) {
                    setDefaultValue();
                }
                manageTopContainer();
            }
        });

        txtHIsActive.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHIsActive(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsActive).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsActive.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsActive).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHIsActive.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
            }
        });
    }

    private IrpInfoPopup_Mdl saveInitialize() {
        IrpInfoPopup_Mdl mrTemp = new IrpInfoPopup_Mdl();
        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHIrpName.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHIrpName.getText()));
        mrTemp.setUrl(DmShared.xNull(txtUrl.getText()));
        mrTemp.setIrpTypeTag(DmShared.xNull(txtHIrpType.getTag()));
        mrTemp.setIrpTypeText(DmShared.xNull(txtHIrpType.getText()));
        mrTemp.setIsActiveCode(DmShared.xNull(txtHIsActive.getTag()));
        mrTemp.setIsActiveText(DmShared.xNull(txtHIsActive.getText()));
        return mrTemp;
    }

    private IrpInfoPopup_Mdl saveIrpInfo(final IrpInfoPopup_Mdl mrTemp) {
        try {
            srvAsync.invokeRPC(true).saveIrpInfo(prevRecord, mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHIrpName).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        mrTemp.setSharedKeyFieldCode(result.getData());
                        hide(false, mrTemp);
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException ex) {
            new DmAlertMessageBox(ex.getMessage(), txtHIrpName).show();
        }
        return mrTemp;
    }

    private void hide(boolean isCallEvent, IrpInfoPopup_Mdl mrTemp) {
        this.popupPanel.hide();
        if (isCallEvent) {
            this.callBack.onClose();
        }
        if (mrTemp != null) {
            this.callBack.onOk(DmShared.iNull(txtHIrpType.getTag()).intValue(), mrTemp);
        }
    }

    public void hide() {
        hide(false, null);
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide(true, null);
        } else if (event.getSource() == btnOk) {
            onOk();
        } else if (event.getSource() == btnDelete) {
            onDelete();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true, null);
        }
    }

    private void manageTopContainer() {
        topContainer.clear();
        topContainer.add(flwOk);
        lblMode.setText("New");
        if (!DmShared.xNull(txtHIrpName.getTag()).trim().equalsIgnoreCase("")) {
            topContainer.add(flwDelete);
            lblMode.setText("Edit");
        }
        topContainer.add(flwClose);
        topContainer.add(flwMode);
    }

}
