package aarogya.inventory.purchaseOrderClose.client;

import aarogya.inventory.purchaseOrderClose.client.*;
import aarogya.inventory.purchaseOrderClose.shared.*;
import aarogya.inventory.purchaseOrder.client.PurchaseOrder;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_Srv;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_SrvAsync;
import aarogya.projectMenu.self.shared.pmInventory;
import dataman.dmBase.self.shared.*;
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
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.HashMap;
import java.util.List;

public class PurchaseOrderClose extends PurchaseOrderClose_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents {

    ServiceInvoker<PurchaseOrderClose_SrvAsync> SrvAsync;

    PurchaseOrderClose_Mdl purchaseApp_Mdl;
    ServiceInvoker<PurchaseOrder_SrvAsync> srvAsyncPurchase;
    WalkThrough walkThrough;
    final Setting settingPopup = new Setting();
    SetWidgetInvoker swi;
    private final PurchaseOrderCloseGridEdit<GrdPurchaseOrderClose_Mdl, Grd_PrpAcs> purchaseOrderCloseGridEdit = new PurchaseOrderCloseGridEdit(dmGrd.getDmGrid());
    CellClickEvent event1;
    private String statusRemark;

    private void textHelpEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                dmGrd.reQuery();
            }
        });
        settingPopup.addPurchaseOrderCloseCallBack(new Setting.PurchaseOrderCloseCallBack() {

            @Override
            public void onOk(PurchaseOrderClose_Mdl closeEntry_Mdl) {
                purchaseApp_Mdl = closeEntry_Mdl;
                dmGrd.getDmGrid().commitChanges();
                dmGrd.reQuery();
            }
        });
    }

    private void gridEvents() {

        purchaseOrderCloseGridEdit.addCallBack(new PurchaseOrderCloseGridEdit.CallBack() {
            @Override
            public void onOk(GrdPurchaseOrderClose_Mdl sampleDetails_Mdl) {
                statusRemark=sampleDetails_Mdl.getStatusRemark();
                onOKReason();
            }

            @Override
            public void onDelete() {
                dmGrd.getDmGrid().commitChanges();
            }

            @Override
            public void onRefresh() {

            }
        });

        dmGrd.addCallBack(new DmGridPaging.CallBack<PurchaseOrderClose_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, PurchaseOrderClose_Mdl param) {
                SrvAsync.invokeRPC(true).getFillRecords(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdPurchaseOrderClose_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdPurchaseOrderClose_Mdl>> result) {
                        if (result.isStatus()) {
                            dmGrd.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public PurchaseOrderClose_Mdl getParam() {
                return purchaseApp_Mdl;
            }
        });

        dmGrd.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        showPurchaseOrderCloseGridEdit(event.getRowIndex());
                        event1 = event;

                    } else if (event.getCellIndex() == 1) {
                        showPurchaseOrder(DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
                    }

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });
    }

    public void showPurchaseOrder(final String strKey) {
        swi.setWidget(new pmInventory().moduleName, new pmInventory().PURCHASE_ORDER.caption, new SetWidgetInvoker() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                final String docId = strKey;
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                ((PurchaseOrder) wgtObject).searchRecord(docId);
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
        purchaseApp_Mdl = new PurchaseOrderClose_Mdl();
        purchaseApp_Mdl.setToDate(DmClient.getLoginDate());
        purchaseApp_Mdl.setStatusCode("P");
        purchaseApp_Mdl.setStatusName("Pending");
        dmGrd.reQuery();
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PurchaseOrderClose.this);
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

    public PurchaseOrderClose(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        SrvAsync = new ServiceInvoker<PurchaseOrderClose_SrvAsync>(GWT.create(PurchaseOrderClose_Srv.class), PurchaseOrderClose.class.getName());
        srvAsyncPurchase = new ServiceInvoker<PurchaseOrder_SrvAsync>(GWT.create(PurchaseOrder_Srv.class), PurchaseOrder.class.getName());
        walkThrough = new WalkThrough(srvAsyncPurchase);
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

//    private void setApproved(String docId, String wtaCode, boolean isApprove) {
//        try {
//            if (DmShared.iNull(wtaCode).longValue() != 0) {
//                walkThrough.show(wtaCode);
//            } else {
//                srvAsyncPurchase.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess(DmResult<String> result) {
//                        if (result.isStatus()) {
//                            dmGrd.getDmGrid().commitChanges();
//                            dmGrd.reQuery();
//                        } else {
//                            dmGrd.reQuery();
//                            topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
//                        }
//                    }
//                });
//            }
//        } catch (DmException e) {
//            topControlSaveError(e.getErrorNo(), e.getMessage());
//        }
//    }

    private void openRecord(boolean isOpen, String docID, String vSNo) {
        try {
            SrvAsync.invokeRPC().openRecord(isOpen, DmShared.xNull(docID), vSNo,statusRemark, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    return;
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        dmGrd.reQuery();
                    } else {
                        new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void showPurchaseOrderCloseGridEdit(int currentRowIndex) {
        purchaseOrderCloseGridEdit.show(currentRowIndex, true, false, false, this);
    }

    private void onOKReason() {
        openRecord(false, dmGrd.getDmGrid().getGrid().getStore().get(event1.getRowIndex()).getSharedKeyFieldCode(), dmGrd.getDmGrid().getGrid().getStore().get(event1.getRowIndex()).getvSNo());
    }
}
