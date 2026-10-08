package aarogya.pharmacy.egstParam.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import aarogya.pharmacy.egstParam.shared.EgstParam_SrvAsync;
import aarogya.pharmacy.egstParam.shared.EgstParam_Srv;
import aarogya.pharmacy.egstParam.shared.EgstParam_Vld;
import aarogya.pharmacy.irpInfoPopup.client.IrpInfoPopup;
import aarogya.pharmacy.irpInfoPopup.shared.IrpInfoPopup_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.gst.shared.EGstParam_Mdl;

public class EgstParam extends EgstParam_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents, KeyDownHandler {

    private final ServiceInvoker<EgstParam_SrvAsync> srvAsync;
    private final IrpInfoPopup irpInfo = new IrpInfoPopup(false);
    private String destGstinName = "", destGstinCode = "", isCopy = "";
    private final String strSampleFormat = "{url}/{folder}/dec/{version}/{api}?aspid={aspid}&password={asppassword}&Gstin={gstin}&AuthToken={authtoken}&user_name={username}&QrCodeSize={qrcodeSize}&[ParseIrnResp={parseIrnResp}]";
    private final String strStpSampleFormat = "{PROTOCOL}://{BASE_URL}?aspid={ASPID}&password={ASP_PASSWORD}&action={ACTION}&gstin={GSTIN}&searchGstin=[GSTIN]";

    public EgstParam(UserMenu menuPermission, SetWidgetInvoker swiMain) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(EgstParam_Srv.class), EgstParam.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        textHelpEvents();
        manageEvents();
        flwForm.addAttachHandler(this);
        txtEwayExampleFormat.setText(DmShared.xNull(strSampleFormat));
        txtEinvExampleFormat.setText(DmShared.xNull(strSampleFormat));
        iniDefaultValue(swiMain);
    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHUserGstin).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    topControlMoveRec(result.getData().getSharedKeyFieldCode());
                    txtHUserGstin.setTag(result.getData().getSharedKeyFieldCode());
                    txtHUserGstin.setText(result.getData().getSharedKeyFieldName());
                    swi.setWidget(EgstParam.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHUserGstin).show();
                }
            }
        });
    }

    private void textHelpEvents() {
        txtHUserGstin.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGstIn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUserGstin).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHUserGstin.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHUserGstin).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus) {
                    if (t1Row != null) {
                        topControlMoveRec(DmShared.xNull(txtHUserGstin.getTag()));
                        destGstinCode = txtHUserGstin.getTag();
                        destGstinName = txtHUserGstin.getText();
                    }
                }
            }
        });

        txtHUserGstinCopy.addHelpGridCallBack(new DmHelpGrid.HelpGridCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGstIn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHUserGstinCopy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus) {
                    if (t1Row != null) {
                        isCopy = t1Row.getSharedKeyFieldCode();
                        topControlMoveRec(t1Row.getSharedKeyFieldCode());
                        txtHUserGstin.setText(DmShared.xNull(destGstinName));
                        txtHUserGstin.setTag(DmShared.iNull(destGstinCode).toString());
                    }
                }
            }
        });

        txtHEwayIRP.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<IrpInfoPopup_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBaseUrl(DmEnum.IRP_Type.E_WAY.getCode(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<IrpInfoPopup_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEwayIRP).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<IrpInfoPopup_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHEwayIRP.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEwayIRP).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, IrpInfoPopup_Mdl tRow) {
                if (!DmShared.xNull(txtHEwayIRP.getTag2nd()).equalsIgnoreCase(txtHEwayIRP.getTag())) {
                    txtEwayBaseUrl.setText("");
                    txtHEwayIRP.setTag2nd(DmShared.xNull(txtHEwayIRP.getTag()));
                }
                if (tRow != null && blnStatus) {
                    txtEwayBaseUrl.setText(DmShared.xNull(tRow.getUrl()));
                }
            }

        });

        txtHEinvIRP.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<IrpInfoPopup_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBaseUrl(DmEnum.IRP_Type.E_INVOICE.getCode(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<IrpInfoPopup_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEinvIRP).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<IrpInfoPopup_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHEinvIRP.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEinvIRP).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, IrpInfoPopup_Mdl tRow) {
                if (!DmShared.xNull(txtHEinvIRP.getTag2nd()).equalsIgnoreCase(txtHEinvIRP.getTag())) {
                    txtEinvBaseUrl.setText("");
                    txtHEinvIRP.setTag2nd(DmShared.xNull(txtHEinvIRP.getTag()));
                }
                if (tRow != null && blnStatus) {
                    txtEinvBaseUrl.setText(DmShared.xNull(tRow.getUrl()));
                }
            }

        });

        irpInfo.addCallBack(new IrpInfoPopup.CallBack() {

            @Override
            public void onOk(int irpTypeCode, IrpInfoPopup_Mdl mrTemp) {
                if (mrTemp != null) {
                    if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() > 0) {
                        if (DmEnum.IRP_Type.getViaCode(irpTypeCode) != null) {
                            switch (DmEnum.IRP_Type.getViaCode(irpTypeCode)) {
                                case E_WAY:
                                    if (DmShared.iNull(mrTemp.getIsActiveCode()).intValue() == 1) {
                                        txtHEwayIRP.setTag(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                                        txtHEwayIRP.setTag2nd(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                                        txtHEwayIRP.setText(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                                        txtEwayBaseUrl.setText(DmShared.xNull(mrTemp.getUrl()));
                                    } else {
                                        txtHEwayIRP.setTag("");
                                        txtHEwayIRP.setTag2nd("");
                                        txtHEwayIRP.setText("");
                                        txtEwayBaseUrl.setText("");
                                    }

                                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                                        @Override
                                        public void execute() {
                                            txtHEwayIRP.setFocus(true);
                                        }
                                    });
                                    break;
                                case E_INVOICE:
                                    if (DmShared.iNull(mrTemp.getIsActiveCode()).intValue() == 1) {
                                        txtHEinvIRP.setTag(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                                        txtHEinvIRP.setTag2nd(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                                        txtHEinvIRP.setText(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                                        txtEinvBaseUrl.setText(DmShared.xNull(mrTemp.getUrl()));
                                    } else {
                                        txtHEinvIRP.setTag("");
                                        txtHEinvIRP.setTag2nd("");
                                        txtHEinvIRP.setText("");
                                        txtEinvBaseUrl.setText("");
                                    }
                                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                                        @Override
                                        public void execute() {
                                            txtHEinvIRP.setFocus(true);
                                        }
                                    });
                            }
                        }
                    }
                }
            }

            @Override
            public void onClose() {
                txtHEwayIRP.setFocus(true);
            }

            @Override
            public void onDelete(int irpTypeCode) {
                if (DmEnum.IRP_Type.getViaCode(irpTypeCode) != null) {
                    switch (DmEnum.IRP_Type.getViaCode(irpTypeCode)) {
                        case E_WAY:
                            txtHEwayIRP.setTag("");
                            txtHEwayIRP.setTag2nd("");
                            txtHEwayIRP.setText("");
                            txtEwayBaseUrl.setText("");
                            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                                @Override
                                public void execute() {
                                    txtHEwayIRP.setFocus(true);
                                }
                            });
                            break;
                        case E_INVOICE:
                            txtHEinvIRP.setTag("");
                            txtHEinvIRP.setTag2nd("");
                            txtHEinvIRP.setText("");
                            txtEinvBaseUrl.setText("");
                            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                                @Override
                                public void execute() {
                                    txtHEinvIRP.setFocus(true);
                                }
                            });
                    }
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
        txtHUserGstin.setText(DmShared.xNull(key));
        txtHUserGstin.setTag(DmShared.iNull(key).toString());
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            irpInfo.hide();
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void manageEvents() {
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
        btnCopy.addClickHandler(this);
        lblEwayBaseUrl.addClickHandler(this);
        lblEinvBaseUrl.addClickHandler(this);
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EgstParam.this);
                }
            }
        }).show();
    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource().equals(btnSave)) {
                topControlSave();
            } else if (event.getSource().equals(btnClose)) {
                onClose();
            } else if (event.getSource().equals(btnCopy)) {
                txtHUserGstinCopy.show(btnCopy, "");
                txtHUserGstinCopy.reQuery("");
            } else if (event.getSource().equals(lblEwayBaseUrl)) {
                showCreateIrp(lblEwayBaseUrl, DmEnum.IRP_Type.E_WAY, DmShared.xNull(txtHEwayIRP.getTag()));
            } else if (event.getSource().equals(lblEinvBaseUrl)) {
                showCreateIrp(lblEinvBaseUrl, DmEnum.IRP_Type.E_INVOICE, DmShared.xNull(txtHEinvIRP.getTag()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            new DmAlertMessageBox(e.getMessage(), txtHUserGstin).show();
        }
    }

    private void showCreateIrp(UIObject showRelativeTo, DmEnum.IRP_Type irpType, String irpCode) {
        irpInfo.show(showRelativeTo, irpType, irpCode);
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            topControlSave();
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (!txtHUserGstin.getTag().equalsIgnoreCase("")) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_CTRL && event.getNativeKeyCode() == KeyCodes.KEY_S) {
                topControlSave();
            }
        }
    }

    private void topControlSave() {
        EGstParam_Mdl mrTemp;
        try {
            checkPassword();
            mrTemp = topControlSaveInitialize();
            EgstParam_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHUserGstin).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControlMoveRec(result.getData());
                        txtHUserGstin.setText(DmShared.xNull(result.getData()));
                        txtHUserGstin.setTag(DmShared.xNull(result.getData()));
                        new DmAlertMessageBox("Record saved successfully", txtHUserGstin).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private EGstParam_Mdl topControlSaveInitialize() {

        EGstParam_Mdl mrTemp = new EGstParam_Mdl();
        mrTemp.setUserGstin(DmShared.xNull(txtHUserGstin.getText()));
        //<editor-fold defaultstate="collapsed" desc="E-Way">
        mrTemp.setEway_UserName(DmShared.xNull(txtEwayUserName.getText()));
        mrTemp.setEway_UserPassword(DmShared.xNull(txtEwayUserPass.getText()));
        mrTemp.setEway_AspUserId(DmShared.xNull(txtEwayAspId.getText()));
        mrTemp.setEway_AspPassword(DmShared.xNull(txtEwayAspPassword.getText()));
        mrTemp.setEway_BillApiVersion(DmShared.xNull(txtEwayBaseVersion.getText()));
        mrTemp.setEway_GenerateAction(DmShared.xNull(txtEwayGenerateAction.getText()));
        mrTemp.setEway_CancelAction(DmShared.xNull(txtEwayCancelAction.getText()));
        mrTemp.setEway_BaseUrl(DmShared.xNull(txtEwayBaseUrl.getText()));
        mrTemp.setEway_IrpCode(DmShared.xNull(txtHEwayIRP.getTag()));
        mrTemp.setEway_IrpName(DmShared.xNull(txtHEwayIRP.getText()));
        mrTemp.setEway_AuthApiVersion(DmShared.xNull(txtEwayAuthVersion.getText()));
        mrTemp.setEway_GSPName(DmShared.xNull(txtEwayGSPName.getText()));
        mrTemp.setEway_UrlFormat(DmShared.xNull(txtEwayUrlFormat.getText()));
        mrTemp.setEway_BillApiName(DmShared.xNull(txtEwayBaseApiName.getText()));
        mrTemp.setEway_AuthApiName(DmShared.xNull(txtEwayAuthApiName.getText()));
        mrTemp.setEway_AuthAction(DmShared.xNull(txtEwayAuthAction.getText()));
        mrTemp.setEway_AuthFormat(DmShared.xNull(txtEwayAuthFormat.getText()));
        mrTemp.setEway_PrintApiName(DmShared.xNull(txtEwayPrintlApiName.getText()));
        mrTemp.setEway_PrintAction(DmShared.xNull(txtEwayPrintAction.getText()));
        mrTemp.setEway_DetailePrintAction(DmShared.xNull(txtEwayDetailedPrintAction.getText()));
        mrTemp.setEway_PrintFormat(DmShared.xNull(txtEwayPrintFormat.getText()));
        mrTemp.setEway_AuthFolderName(DmShared.xNull(txtEwayAuthFolderName.getText()));
        mrTemp.setEway_BillFolderName(DmShared.xNull(txtEwayBaseFolderName.getText()));
        mrTemp.setEway_PrintApiVersion(DmShared.xNull(txtEwayPrintlVersion.getText()));
        mrTemp.setEway_getDetailAction(DmShared.xNull(txtEwaygetDetailAction.getText()));
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="E-invoice">
        mrTemp.setEinv_UserName(DmShared.xNull(txtEinvUserName.getText()));
        mrTemp.setEinv_UserPassword(DmShared.xNull(txtEinvUserPass.getText()));
        mrTemp.setEinv_ASPID(DmShared.xNull(txtEinvAspId.getText()));
        mrTemp.setEinv_ASPID_Password(DmShared.xNull(txtEinvAspPassword.getText()));
        mrTemp.setEinv_BillApiVersion(DmShared.xNull(txtEinvBaseVersion.getText()));
        mrTemp.setEinv_GenerateAction(DmShared.xNull(txtEinvGenerateAction.getText()));
        mrTemp.setEinv_CancelAction(DmShared.xNull(txtEinvCancelAction.getText()));
        mrTemp.setEinv_IrpCode(DmShared.xNull(txtHEinvIRP.getTag()));
        mrTemp.setEinv_IrpName(DmShared.xNull(txtHEinvIRP.getText()));
        mrTemp.setEinv_BaseUrl(DmShared.xNull(txtEinvBaseUrl.getText()));
        mrTemp.setEinv_AuthApiVersion(DmShared.xNull(txtEinvAuthVersion.getText()));
        mrTemp.setEinv_GSPName(DmShared.xNull(txtEinvGSPName.getText()));
        mrTemp.setEinv_UrlFormat(DmShared.xNull(txtEinvUrlFormat.getText()));
        mrTemp.setEinv_BillApiName(DmShared.xNull(txtEinvBaseApiName.getText()));
        mrTemp.setEinv_AuthApiName(DmShared.xNull(txtEinvAuthApiName.getText()));
        mrTemp.setEinv_AuthAction(DmShared.xNull(txtEinvAuthAction.getText()));
        mrTemp.setEinv_AuthFormat(DmShared.xNull(txtEinvAuthFormat.getText()));
        mrTemp.setEinv_PrintApiName(DmShared.xNull(txtEinvPrintlApiName.getText()));
        mrTemp.setEinv_PrintAction(DmShared.xNull(txtEinvPrintAction.getText()));
        mrTemp.setEinv_DetailedPrintAction(DmShared.xNull(txtEinvDetailedPrintAction.getText()));
        mrTemp.setEinv_PrintFormat(DmShared.xNull(txtEinvPrintFormat.getText()));
        mrTemp.setEinv_AuthFolderName(DmShared.xNull(txtEinvAuthFolderName.getText()));
        mrTemp.setEinv_ApiFolderName(DmShared.xNull(txtEinvBaseFolderName.getText()));
        mrTemp.setEinv_PrintApiVersion(DmShared.xNull(txtEinvPrintlVersion.getText()));
        mrTemp.setEinv_getDetailAction(DmShared.xNull(txtEinvgetDetailAction.getText()));
        mrTemp.setEinv_qrCodeSize(DmShared.xNull(txtQrCodeSize.getText()));
        //new field add
        mrTemp.setEinv_CancelFormat(DmShared.xNull(txtEinvCancelFormat.getText()));
        mrTemp.setEinv_eWayViaIrnApiName(DmShared.xNull(txtEinv_Eway_ViaIRNApiName.getText()));
        mrTemp.setEinv_eWayViaIrnApiFolderName(DmShared.xNull(txtEinv_Eway_ViaIRNApiFolder.getText()));
        mrTemp.setEinv_eWayViaIrnApiVersion(DmShared.xNull(txtEinv_Eway_ViaIRNApiVersion.getText()));
        mrTemp.setEinv_eWayViaIrnUrlFormat(DmShared.xNull(txtEinv_Eway_ViaIRNUrlFormat.getText()));

        //</editor-fold>
        return mrTemp;
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<EGstParam_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<EGstParam_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<EGstParam_Mdl> result) {
        EGstParam_Mdl data;
        if (result.isStatus()) {
            data = result.getData();
            //<editor-fold defaultstate="collapsed" desc="E-Way">
            txtEwayUserName.setText(DmShared.xNull(data.getEway_UserName()));
            txtEwayUserPass.setText(DmShared.xNull(data.getEway_UserPassword()));
            txtEwayAspId.setText(DmShared.xNull(data.getEway_AspUserId()));
            txtEwayAspPassword.setText(DmShared.xNull(data.getEway_AspPassword()));
            txtEwayBaseVersion.setText(DmShared.xNull(data.getEway_BillApiVersion()));
            txtEwayGenerateAction.setText(DmShared.xNull(data.getEway_GenerateAction()));
            txtEwayCancelAction.setText(DmShared.xNull(data.getEway_CancelAction()));
            txtHEwayIRP.setTag(DmShared.xNull(data.getEway_IrpCode()));
            txtHEwayIRP.setText(DmShared.xNull(data.getEway_IrpName()));
            txtEwayBaseUrl.setText(DmShared.xNull(data.getEway_BaseUrl()));
            txtEwayAuthVersion.setText(DmShared.xNull(data.getEway_AuthApiVersion()));
            txtEwayGSPName.setText(DmShared.xNull(data.getEway_GSPName()));
            txtEwayUrlFormat.setText(DmShared.xNull(data.getEway_UrlFormat()));
            txtEwayBaseApiName.setText(DmShared.xNull(data.getEway_BillApiName()));
            txtEwayAuthApiName.setText(DmShared.xNull(data.getEway_AuthApiName()));
            txtEwayAuthAction.setText(DmShared.xNull(data.getEway_AuthAction()));
            txtEwayAuthFormat.setText(DmShared.xNull(data.getEway_AuthFormat()));
            txtEwayPrintlApiName.setText(DmShared.xNull(data.getEway_PrintApiName()));
            txtEwayPrintAction.setText(DmShared.xNull(data.getEway_PrintAction()));
            txtEwayDetailedPrintAction.setText(DmShared.xNull(data.getEway_DetailePrintAction()));
            txtEwayPrintFormat.setText(DmShared.xNull(data.getEway_PrintFormat()));
            txtEwayAuthFolderName.setText(DmShared.xNull(data.getEway_AuthFolderName()));
            txtEwayBaseFolderName.setText(DmShared.xNull(data.getEway_BillFolderName()));
            txtEwayPrintlVersion.setText(DmShared.xNull(data.getEway_PrintApiVersion()));
            txtEwaygetDetailAction.setText(DmShared.xNull(data.getEway_getDetailAction()));
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="E-invoice">
            txtEinvUserName.setText(DmShared.xNull(data.getEinv_UserName()));
            txtEinvUserPass.setText(DmShared.xNull(data.getEinv_UserPassword()));
            txtEinvAspId.setText(DmShared.xNull(data.getEinv_ASPID()));
            txtEinvAspPassword.setText(DmShared.xNull(data.getEinv_ASPID_Password()));
            txtEinvBaseVersion.setText(DmShared.xNull(data.getEinv_BillApiVersion()));
            txtEinvGenerateAction.setText(DmShared.xNull(data.getEinv_GenerateAction()));
            txtEinvCancelAction.setText(DmShared.xNull(data.getEinv_CancelAction()));
            txtHEinvIRP.setTag(DmShared.xNull(data.getEinv_IrpCode()));
            txtHEinvIRP.setText(DmShared.xNull(data.getEinv_IrpName()));
            txtEinvBaseUrl.setText(DmShared.xNull(data.getEinv_BaseUrl()));
            txtEinvAuthVersion.setText(DmShared.xNull(data.getEinv_AuthApiVersion()));
            txtEinvGSPName.setText(DmShared.xNull(data.getEinv_GSPName()));
            txtEinvUrlFormat.setText(DmShared.xNull(data.getEinv_UrlFormat()));
            txtEinvBaseApiName.setText(DmShared.xNull(data.getEinv_BillApiName()));
            txtEinvAuthApiName.setText(DmShared.xNull(data.getEinv_AuthApiName()));
            txtEinvAuthAction.setText(DmShared.xNull(data.getEinv_AuthAction()));
            txtEinvAuthFormat.setText(DmShared.xNull(data.getEinv_AuthFormat()));
            txtEinvPrintlApiName.setText(DmShared.xNull(data.getEinv_PrintApiName()));
            txtEinvPrintAction.setText(DmShared.xNull(data.getEinv_PrintAction()));
            txtEinvDetailedPrintAction.setText(DmShared.xNull(data.getEinv_DetailedPrintAction()));
            txtEinvPrintFormat.setText(DmShared.xNull(data.getEinv_PrintFormat()));
            txtEinvAuthFolderName.setText(DmShared.xNull(data.getEinv_AuthFolderName()));
            txtEinvBaseFolderName.setText(DmShared.xNull(data.getEinv_ApiFolderName()));
            txtEinvPrintlVersion.setText(DmShared.xNull(data.getEinv_PrintApiVersion()));
            txtEinvgetDetailAction.setText(DmShared.xNull(data.getEinv_getDetailAction()));
            txtQrCodeSize.setText(DmShared.xNull(data.getEinv_qrCodeSize()));
            //</editor-fold>
            //new field add 
            txtEinvCancelFormat.setText(DmShared.xNull(data.getEinv_CancelFormat()));
            txtEinv_Eway_ViaIRNApiName.setText(DmShared.xNull(data.getEinv_eWayViaIrnApiName()));
            txtEinv_Eway_ViaIRNApiFolder.setText(DmShared.xNull(data.getEinv_eWayViaIrnApiFolderName()));
            txtEinv_Eway_ViaIRNApiVersion.setText(DmShared.xNull(data.getEinv_eWayViaIrnApiVersion()));
            txtEinv_Eway_ViaIRNUrlFormat.setText(DmShared.xNull(data.getEinv_eWayViaIrnUrlFormat()));

            userControl.setTextBlog(modifiedByBlog, data.getUserDateTime());
            txtHUserGstin.getDmGrid().getGrid().getData("");
            if (!DmShared.xNull(isCopy).equalsIgnoreCase("")) {
                isCopy = "";
            }

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHUserGstin).show();
                break;
            case 200:
                tabPanel.selectTab(0);
                new DmAlertMessageBox(errorMessage, txtEwayUserName).show();
                break;
            case 300:
                tabPanel.selectTab(0);
                new DmAlertMessageBox(errorMessage, txtEwayUserPass).show();
                break;
            case 400:
                tabPanel.selectTab(0);
                new DmAlertMessageBox(errorMessage, txtEwayAspId).show();
                break;
            case 500:
                tabPanel.selectTab(0);
                new DmAlertMessageBox(errorMessage, txtEwayAspPassword).show();
                break;
            case 600:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtEinvUserName).show();
                break;
            case 700:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtEinvUserPass).show();
                break;
            case 800:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtEinvAspPassword).show();
                break;
            case 900:
                tabPanel.selectTab(0);
                new DmAlertMessageBox(errorMessage, txtEwayUserCPass).show();
                break;
            case 1000:
                tabPanel.selectTab(0);
                new DmAlertMessageBox(errorMessage, txtEwayAspCPassword).show();
                break;
            case 1100:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtEinvUserCPass).show();
                break;
            case 1200:
                tabPanel.selectTab(1);
                new DmAlertMessageBox(errorMessage, txtEinvAspCPassword).show();
                break;
            default:
                tabPanel.selectTab(0);
                new DmAlertMessageBox(errorMessage, txtHUserGstin).show();
        }

    }

    private void checkPassword() throws DmException {
        DmShared.isValidText(txtEwayUserPass.getText(), "User Password", 300);
        DmShared.isValidText(txtEwayUserCPass.getText(), "User Confirm Password", 900);
        if (!DmShared.xNull(txtEwayUserPass.getText()).equals(DmShared.xNull(txtEwayUserCPass.getText()))) {
            throw new DmException(900, "E-way User Password not match with Confirm Password!");
        }
        DmShared.isValidText(txtEwayAspPassword.getText(), "ASP Password", 500);
        DmShared.isValidText(txtEwayAspCPassword.getText(), "ASP Confirm Password", 1000);
        if (!DmShared.xNull(txtEwayAspPassword.getText()).equals(DmShared.xNull(txtEwayAspCPassword.getText()))) {
            throw new DmException(1000, "E-way ASP Password not match with Confirm Password!");
        }
        DmShared.isValidText(txtEinvUserPass.getText(), "User Password", 700);
        DmShared.isValidText(txtEinvUserCPass.getText(), "User Confirm Password", 1100);
        if (!DmShared.xNull(txtEinvUserPass.getText()).equals(DmShared.xNull(txtEinvUserCPass.getText()))) {
            throw new DmException(1100, "E-invoice User Password not match with Confirm Password!");
        }
        DmShared.isValidText(txtEinvAspPassword.getText(), "ASP Password", 900);
        DmShared.isValidText(txtEinvAspCPassword.getText(), "ASP Confirm Password", 1200);
        if (!DmShared.xNull(txtEinvAspPassword.getText()).equals(DmShared.xNull(txtEinvAspCPassword.getText()))) {
            throw new DmException(1200, "E-invoice ASP Password not match with Confirm Password!");
        }
    }
}
