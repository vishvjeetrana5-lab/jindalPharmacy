package dataman.accounts.enviroSettings.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.accounts.enviroSettings.shared.*;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public final class EnviroSettings extends EnviroSettings_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents, KeyDownHandler {

    private final ServiceInvoker<EnviroSettings_SrvAsync> srvAsync;
    private String destSiteName = "", destSiteCode = "", isCopy = "";

    private void textHelpEvents() {
        txtHSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus) {
                    if (t1Row != null) {
                        topControlMoveRec(DmShared.xNull(txtHSite.getTag()));
                        destSiteCode = txtHSite.getTag();
                        destSiteName = txtHSite.getText();
                    }
                }
            }
        });
        txtHSiteCopy.addHelpGridCallBack(new DmHelpGrid.HelpGridCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSiteCopy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
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
                        txtHSite.setText(DmShared.xNull(destSiteName));
                        txtHSite.setTag(DmShared.iNull(destSiteCode).toString());
                    }
                }
            }
        });
        txtHRoundOffTDS.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRoundOff(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRoundOffTDS).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRoundOffTDS.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRoundOffTDS).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                }
            }
        });
        txtHRoundOffGST.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRoundOff(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRoundOffGST).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRoundOffGST.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRoundOffGST).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                }
            }
        });
        txtHStockAdjustmentVType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHStockAdjustmentVType>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHStockAdjustmentVType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHStockAdjustmentVType>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHStockAdjustmentVType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHStockAdjustmentVType>> result) {
                        if (result.isStatus()) {
                            txtHStockAdjustmentVType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHStockAdjustmentVType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

        txtHVoucherExcludeInTurnover.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHVouchersExcludesInTurnover(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherExcludeInTurnover).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVoucherExcludeInTurnover.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherExcludeInTurnover).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {

            }
        });

        txtHTDSOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTDSOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTDSOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTDSOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTDSOn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                }
            }
        });

    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource().equals(btnSave)) {
                topControlSave();
            }
            if (event.getSource().equals(btnClose)) {
                onClose();
            } else if (event.getSource().equals(btnCopy)) {
                txtHSiteCopy.show(btnCopy, "");
                txtHSiteCopy.reQuery("");
            }

        } catch (DmException e) {
        }

    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (!txtHSite.getTag().equalsIgnoreCase("")) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_CTRL && event.getNativeKeyCode() == KeyCodes.KEY_S) {
                topControlSave();
            }
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
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
            topControlSave();
        }
    }

    public EnviroSettings(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(EnviroSettings_Srv.class), EnviroSettings.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
        btnCopy.addClickHandler(this);
        textHelpEvents();
        flwForm.addAttachHandler(this);
        iniDefaultValue(swi);
    }

    private EnviroSettings_Mdl topControlSaveInitialize() {
        EnviroSettings_Mdl mrTemp = new EnviroSettings_Mdl();
        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHSite.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHSite.getText()));
        mrTemp.setTdsRoundOffTag(DmShared.xNull(txtHRoundOffTDS.getTag()));
        mrTemp.setGstRoundOffTag(DmShared.xNull(txtHRoundOffGST.getTag()));
        mrTemp.setStockAdjustmentVTypeTag(DmShared.xNull(txtHStockAdjustmentVType.getTag()));
        mrTemp.setVoucherExcludesInTurnoverTag(DmShared.xNull(txtHVoucherExcludeInTurnover.getTag()));
        mrTemp.setTdsOnTag(DmShared.xNull(txtHTDSOn.getTag()));

        mrTemp.setCopyFromSite(destSiteCode);
        return mrTemp;
    }

    private void topControlSave() {
        EnviroSettings_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            EnviroSettings_Vld.chkSave(mrTemp);
            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControlMoveRec(result.getData());
                        new DmAlertMessageBox("Record saved successfully", txtHSite).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHSite).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHRoundOffTDS).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHRoundOffGST).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHSite).show();
        }

    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<EnviroSettings_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<EnviroSettings_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<EnviroSettings_Mdl> result) {
        EnviroSettings_Mdl data;
        if (result.isStatus()) {
            data = result.getData();
            txtHRoundOffTDS.setTag(DmShared.xNull(data.getTdsRoundOffTag()));
            txtHRoundOffTDS.setText(DmShared.xNull(data.getTdsRoundOffText()));
            txtHRoundOffGST.setTag(DmShared.xNull(data.getGstRoundOffTag()));
            txtHRoundOffGST.setText(DmShared.xNull(data.getGstRoundOffText()));
            txtHStockAdjustmentVType.setTag(DmShared.xNull(data.getStockAdjustmentVTypeTag()));
            txtHStockAdjustmentVType.setText(DmShared.xNull(data.getStockAdjustmentVTypeText()));
            txtHTDSOn.setTag(DmShared.xNull(data.getTdsOnTag()));
            txtHTDSOn.setText(DmShared.xNull(data.getTdsOnText()));
            txtHVoucherExcludeInTurnover.setTag(DmShared.xNull(data.getVoucherExcludesInTurnoverTag()));
            txtHVoucherExcludeInTurnover.setText(DmShared.xNull(data.getVoucherExcludesInTurnoverText()));
            userControl.setTextBlog(modifiedByBlog, data.getUserEntryDateTime());
            txtHSiteCopy.getDmGrid().getGrid().getData("");
            if (!DmShared.xNull(isCopy).equalsIgnoreCase("")) {
                isCopy = "";
            }
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EnviroSettings.this);
                }
            }
        }).show();
    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    txtHSite.setTag(result.getData().getSharedKeyFieldCode());
                    txtHSite.setText(result.getData().getSharedKeyFieldName());
                    topControlMoveRec(result.getData().getSharedKeyFieldCode());
                    swi.setWidget(EnviroSettings.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                }
            }
        });
    }

}
