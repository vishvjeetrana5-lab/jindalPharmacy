package dataman.accounts.voucherEntryApproval.client;

import dataman.accounts.voucherEntryApproval.shared.*;
import dataman.accounts.voucherEntry.shared.VoucherEntry_SrvAsync;
import dataman.accounts.voucherEntryApproval.shared.Setting_Mdl;
import dataman.accounts.voucherEntry.client.VoucherEntry;
import dataman.accounts.voucherEntry.shared.VoucherEntry_Srv;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
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
import dataman.libraryMenu.self.shared.lmAccounts;
import java.util.HashMap;

public class VoucherEntryApproval extends VoucherEntryApproval_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents {

    ServiceInvoker<VoucherEntryApproval_SrvAsync> srvAsync;
    ServiceInvoker<VoucherEntry_SrvAsync> srvAsyncVoucherEntry;

    Setting_Mdl pBillApproval_Mdl;
    final Setting settingPopup = new Setting();
    WalkThrough walkThrough;
    SetWidgetInvoker swi;

    private void textHelpEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                dmGrd.reQuery();
            }
        });

        settingPopup.addPBillApprovalCallBack(new Setting.PBillApprovalCallBack() {

            @Override
            public void onOk(Setting_Mdl approvalEntry_Mdl) {
                pBillApproval_Mdl = new Setting_Mdl();
                pBillApproval_Mdl = approvalEntry_Mdl;
                dmGrd.reQuery();
            }
        });

    }

    private void gridEvents() {

        dmGrd.addCallBack(new DmGridPaging.CallBack<Setting_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, Setting_Mdl param) {
                srvAsync.invokeRPC(true).getFillRecords(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdVoucherEntryApproval_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdVoucherEntryApproval_Mdl>> result) {
                        if (result.isStatus()) {
                            dmGrd.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public Setting_Mdl getParam() {
                return pBillApproval_Mdl;
            }
        });

        dmGrd.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        GrdVoucherEntryApproval_Mdl grdaccountsBillApproval_Mdl = dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex());
                        int val = DmShared.iNull(grdaccountsBillApproval_Mdl.getIsChecked()).intValue();
                        if (val == 0) {
                            setApproved(grdaccountsBillApproval_Mdl.getSharedKeyFieldCode(), grdaccountsBillApproval_Mdl.getWtaCode(), true);
                        } else if (val == 1) {
                            setApproved(grdaccountsBillApproval_Mdl.getSharedKeyFieldCode(), grdaccountsBillApproval_Mdl.getWtaCode(), false);
                        }
                    } else if (event.getCellIndex() == 1) {
                        String menuText = "";
                        lmAccounts pmMenu = new lmAccounts();
                        switch (DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getVoucherCategory()).toUpperCase()) {
                            case "PMT":
                                menuText = pmMenu.PAYMENT_VOUCHER.caption;
                                break;
                            case "RCT":
                                menuText = pmMenu.RECEIPT_VOUCHER.caption;
                                break;
                            case "JV":
                                menuText = pmMenu.JOURNAL_VOUCHER.caption;
                                break;
                            case "CNTRA":    
                                menuText = pmMenu.CONTRA_VOUCHER.caption;
                                break;
                        }
                        pmMenu = null;
                        
                        if (!menuText.trim().equalsIgnoreCase("")) {
                            showVoucher(DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()), menuText);
                        }

                    }

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

    }

    private void showVoucher(final String strKey, String menuText) {

//        swi.setWidget(new lmAccounts().moduleName, new lmAccounts().VOUCHER.caption, new SetWidgetInvoker() {
        swi.setWidget(new lmAccounts().moduleName, menuText, new SetWidgetInvoker() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                final String docId = strKey;
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                ((VoucherEntry) wgtObject).searchRecord(docId);
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

    private void onLoad() {
        pBillApproval_Mdl = new Setting_Mdl();
        pBillApproval_Mdl.setToDate(DmClient.getLoginDate());
        pBillApproval_Mdl.setStatusCode("P");
        pBillApproval_Mdl.setStatusName("Pending");        
        dmGrd.reQuery();
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(VoucherEntryApproval.this);
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

    public VoucherEntryApproval(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        srvAsync = new ServiceInvoker<VoucherEntryApproval_SrvAsync>(GWT.create(VoucherEntryApproval_Srv.class), VoucherEntryApproval.class.getName());
        srvAsyncVoucherEntry = new ServiceInvoker<VoucherEntry_SrvAsync>(GWT.create(VoucherEntry_Srv.class), VoucherEntry.class.getName());
        walkThrough = new WalkThrough(srvAsyncVoucherEntry);
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
        onLoad();
        swi.setWidget(this);
    }

    private void showSetting() {
        settingPopup.show(true);

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
                srvAsyncVoucherEntry.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
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
}
