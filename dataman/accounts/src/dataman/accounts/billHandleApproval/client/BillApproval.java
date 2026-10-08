package dataman.accounts.billHandleApproval.client;

import dataman.accounts.billHandle.client.BillHandle;
import dataman.accounts.billHandle.shared.BillHandle_Srv;
import dataman.accounts.billHandle.shared.BillHandle_SrvAsync;
import dataman.accounts.billHandleApproval.shared.GrdBillApproval_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.HashMap;
import dataman.accounts.billHandleApproval.shared.BillApproval_Srv;
import dataman.accounts.billHandleApproval.shared.BillApproval_SrvAsync;
import dataman.accounts.billHandleApproval.shared.DefaultValue_Mdl;
import dataman.libraryMenu.self.shared.lmAccounts;

public class BillApproval extends BillHandleApproval_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents {

    ServiceInvoker<BillApproval_SrvAsync> SrvAsync;
    ServiceInvoker<BillHandle_SrvAsync> srvAsyncReq;
    WalkThrough walkThrough;
    final Setting settingPopup = new Setting();
    DmCallBack callBack;
    SetWidgetInvoker swi;
    protected DefaultValue_Mdl defaultMdl;

    public BillApproval(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        SrvAsync = new ServiceInvoker<BillApproval_SrvAsync>(GWT.create(BillApproval_Srv.class), BillApproval.class.getName());
        srvAsyncReq = new ServiceInvoker<BillHandle_SrvAsync>(GWT.create(BillHandle_Srv.class), BillHandle.class.getName());
        walkThrough = new WalkThrough(srvAsyncReq);
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(btnSetting);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        btnSetting.addClickHandler(this);
        btnClose.addClickHandler(this);
        flwForm.addAttachHandler(this);
        textHelpEvents();
        gridEvents();
        getDefaultValue(swi);
    }

    private void getDefaultValue(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC().getDefalutValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultMdl = result.getData();
                    swi.setWidget(BillApproval.this);
                    dmGrd.reQuery();

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void textHelpEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                dmGrd.reQuery();
            }
        });
        settingPopup.addRequisitionApprovalEntryCallBackCallBack(new Setting.RequisitionApprovalEntryCallBack() {

            @Override
            public void onOk(DefaultValue_Mdl approvalEntry_Mdl) {
                defaultMdl = approvalEntry_Mdl;
                dmGrd.reQuery();
            }
        });

    }

    private void gridEvents() {
        dmGrd.addCallBack(new DmGridPaging.CallBack<DefaultValue_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, DefaultValue_Mdl param) {
                SrvAsync.invokeRPC(true).getFillRecords(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdBillApproval_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdBillApproval_Mdl>> result) {
                        if (result.isStatus()) {
                            dmGrd.setData(result.getData(), result.getPaging());
                            if (BillApproval.this.callBack != null) {
                                DmResult<Object> dmrTemp = new DmResult<>();
                                dmrTemp.setStatus(true);
                                BillApproval.this.callBack.result(dmrTemp);
                                BillApproval.this.callBack = null;
                            }
                        }
                    }
                });
            }

            @Override
            public DefaultValue_Mdl getParam() {
                return defaultMdl;
            }
        });

        dmGrd.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        if (DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getIsStatus()).equalsIgnoreCase("0")) {

                            int val = DmShared.vNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                            GrdBillApproval_Mdl grdBillApproval_Mdl = dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex());
                            if (val == 0) {
                                setApproved(DmShared.iNull(grdBillApproval_Mdl.getSharedKeyFieldCode()).toString(), grdBillApproval_Mdl.getWtaCode(), true);

                            } else if (val == 1) {
                            new DmAlertMessageBox("Record is approved. You can't modify this record.", (DmTextBox) null).show();

                           //     setApproved(DmShared.iNull(grdBillApproval_Mdl.getSharedKeyFieldCode()).toString(), grdBillApproval_Mdl.getWtaCode(), false);
                            }
                        }
                    } else if (event.getCellIndex() == 1) {
                        showBillHandle(DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
                    }

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

        dmGrd.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                if (DmShared.iNull(event.getColumnIndex()).intValue() > 1) {
//                    gridEdit(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
                }

            }
        });
        dmGrd.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
//                    gridEdit(dmGrd.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showBillHandle(final String strKey) {
        swi.setWidget(new lmAccounts().moduleName, new lmAccounts().BILL_HANDLE.caption, new SetWidgetInvoker() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                final String docId = strKey;
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                ((BillHandle) wgtObject).searchRecord(docId);
            }

            @Override
            public void setWidget(String moduleName, String menuText, SetWidgetInvoker swi) {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuText, swi);
            }

            @Override
            public void setWidget(String moduleName, String menuText, HashMap paramHashMap, UserMenu userMenu, SetWidgetInvoker swi) {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuText, paramHashMap, userMenu, swi);
            }

            @Override
            public void closeWidget(IsWidget wgtObject) {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(wgtObject);
            }
        });
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(BillApproval.this);
                }
            }
        }).show();
    }

    @Override
    public void onClick(ClickEvent event) {

        if (event.getSource() == btnClose) {

            onClose();
        } else if (event.getSource() == btnSetting) {
            showSetting();
        }
    }

    private void showSetting() {
        settingPopup.show(defaultMdl, true);

    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
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
    public Widget asWidget() {
        return flwForm;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;

        }
    }

    private void setApproved(String docId, String wtaCode, boolean isApprove) {
        try {
            if (DmShared.iNull(wtaCode).longValue() != 0) {
                walkThrough.show(wtaCode);
            } else {
                srvAsyncReq.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            dmGrd.getDmGrid().commitChanges();
                            dmGrd.reQuery();
                        } else {
                            dmGrd.reQuery();
                            topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                        }
                    }
                });
            }
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    @Override
    public void searchRecord(String key) {

    }

}
