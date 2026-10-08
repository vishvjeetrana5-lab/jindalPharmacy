package aarogya.inventory.itemTransferApproval.client;

import aarogya.inventory.itemTransferApproval.client.*;
import aarogya.inventory.itemTransfer.client.ItemTransfer;
import aarogya.inventory.itemTransfer.shared.ItemTransfer_Srv;
import aarogya.inventory.itemTransfer.shared.ItemTransfer_SrvAsync;
import aarogya.inventory.itemTransferApproval.shared.*;
import aarogya.projectMenu.self.shared.pmInventory;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
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

public class ItemTransferApproval extends ItemTransferApproval_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents {

    ServiceInvoker<ItemTransferApproval_SrvAsync> SrvAsync;
    ServiceInvoker<ItemTransfer_SrvAsync> srvAsyncItemTransfer;
    private ItemTransferApproval_Mdl itemTransferApproval_Mdl;
    final Setting settingPopup = new Setting();
    WalkThrough walkThrough;
    final ItemTransferApprovalGridEdit gridEdit = new ItemTransferApprovalGridEdit();
    SetWidgetInvoker swi;

    private void textHelpEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                dmGrd.reQuery();
            }
        });
        settingPopup.addItemTransferApprovalEntryCallBackCallBack(new Setting.ItemTransferApprovalEntryCallBack() {

            @Override
            public void onOk(ItemTransferApproval_Mdl approvalEntry_Mdl) {
                itemTransferApproval_Mdl = approvalEntry_Mdl;
                dmGrd.reQuery();
            }
        });

        gridEdit.addGridEditCallBack(new ItemTransferApprovalGridEdit.GridEditCallBack() {

            @Override
            public void onOk(GrdItemTransferApproval_Mdl approvalEntry_Mdl) {
                dmGrd.getDmGrid().commitChanges();
                dmGrd.reQuery();
            }
        });

    }

    private void gridEvents() {
        dmGrd.addCallBack(new DmGridPaging.CallBack<ItemTransferApproval_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, ItemTransferApproval_Mdl param) {
                SrvAsync.invokeRPC(true).getFillRecords(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdItemTransferApproval_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdItemTransferApproval_Mdl>> result) {
                        if (result.isStatus()) {
                            dmGrd.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public ItemTransferApproval_Mdl getParam() {
                return itemTransferApproval_Mdl;
            }
        });

        dmGrd.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        if (DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getIsStatus()).equalsIgnoreCase("0")) {
                            GrdItemTransferApproval_Mdl grdItemTransferApproval_Mdl = dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex());
                            int val = DmShared.iNull(grdItemTransferApproval_Mdl.getIsChecked()).intValue();
                            if (val == 0) {
                                setApproved(grdItemTransferApproval_Mdl.getSharedKeyFieldCode(), grdItemTransferApproval_Mdl.getWtaCode(), true);
                            } else if (val == 1) {
                                setApproved(grdItemTransferApproval_Mdl.getSharedKeyFieldCode(), grdItemTransferApproval_Mdl.getWtaCode(), false);
                            }
                        }
                    } else if (event.getCellIndex() == 1) {
                        showItemTransfer(DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
                    }

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

        dmGrd.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
//                if (DmShared.iNull(event.getColumnIndex()).intValue() > 1) {
//                    String status = dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getIsStatus();
//                    gridEdit(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode(), status);
//                }

            }
        });
        dmGrd.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
//                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
//                    String status = dmGrd.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getIsStatus();
//                    gridEdit(dmGrd.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode(), status);
//                }
            }
        }, KeyDownEvent.getType());

    }

    private void showItemTransfer(final String strKey) {
        swi.setWidget(new pmInventory().moduleName, new pmInventory().ITEM_TRANSFER.caption, new SetWidgetInvoker() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                final String docId = strKey;
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                ((ItemTransfer) wgtObject).searchRecord(docId);
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
        itemTransferApproval_Mdl = new ItemTransferApproval_Mdl();
        itemTransferApproval_Mdl.setToDate(DmClient.getLoginDate());
        itemTransferApproval_Mdl.setStatusCode("P");
        itemTransferApproval_Mdl.setStatusName("Pending");
        dmGrd.reQuery();
    }

//    private void gridEdit(String docId, String isStatus) {
//        gridEdit.show(docId, isStatus);
//    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ItemTransferApproval.this);
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

    public ItemTransferApproval(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        SrvAsync = new ServiceInvoker<ItemTransferApproval_SrvAsync>(GWT.create(ItemTransferApproval_Srv.class), ItemTransferApproval.class.getName());
        srvAsyncItemTransfer = new ServiceInvoker<ItemTransfer_SrvAsync>(GWT.create(ItemTransfer_Srv.class), ItemTransfer.class.getName());
        walkThrough = new WalkThrough(srvAsyncItemTransfer);
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
                srvAsyncItemTransfer.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
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
