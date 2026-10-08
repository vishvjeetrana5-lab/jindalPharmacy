package aarogya.pharmacy.requisitionClose.client;


import aarogya.pharmacy.requisitionClose.shared.*;
import aarogya.pharmacy.requisition.client.Requisition;
import aarogya.pharmacy.requisition.shared.Requisition_Srv;
import aarogya.pharmacy.requisition.shared.Requisition_SrvAsync;
import aarogya.projectMenu.self.shared.pmPharmacy;
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

public class RequisitionClose extends RequisitionClose_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents {

    ServiceInvoker<RequisitionClose_SrvAsync> SrvAsync;
    ServiceInvoker<Requisition_SrvAsync> srvAsyncReq;
    WalkThrough walkThrough;
    RequisitionClose_Mdl requisitionClose;
    final Setting settingPopup = new Setting();
//    final RequisitionCloseGridEdit gridEdit = new RequisitionCloseGridEdit();
    DmCallBack callBack;
    SetWidgetInvoker swi;
     private final RequisitionCloseGridEdit<GrdRequisitionClose_Mdl, Grd_PrpAcs> requisitionCloseGridEdit = new RequisitionCloseGridEdit(dmGrd.getDmGrid());
    CellClickEvent event1;
    private String statusRemark;

    private void textHelpEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                dmGrd.reQuery();
            }
        });
        settingPopup.addRequisitionCloseEntryCallBackCallBack(new Setting.RequisitionCloseEntryCallBack() {

            @Override
            public void onOk(RequisitionClose_Mdl closeEntry_Mdl) {
                requisitionClose = closeEntry_Mdl;
                dmGrd.reQuery();
            }
        });

//        gridEdit.addGridEditCallBack(new RequisitionCloseGridEdit.GridEditCallBack() {
//
//            @Override
//            public void onOk(GrdRequisitionClose_Mdl approvalEntry_Mdl) {
//                dmGrd.getDmGrid().commitChanges();
//                dmGrd.reQuery();
//            }
//        });

    }

    private void gridEvents() {
        
        requisitionCloseGridEdit.addCallBack(new RequisitionCloseGridEdit.CallBack() {
            @Override
            public void onOk(GrdRequisitionClose_Mdl grdRequisitionClose_Mdl) {
                statusRemark=grdRequisitionClose_Mdl.getStatusRemark();
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
        dmGrd.addCallBack(new DmGridPaging.CallBack<RequisitionClose_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, RequisitionClose_Mdl param) {
                SrvAsync.invokeRPC(true).getFillRecords(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdRequisitionClose_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdRequisitionClose_Mdl>> result) {
                        if (result.isStatus()) {
                            dmGrd.setData(result.getData(), result.getPaging());
                            if (RequisitionClose.this.callBack != null) {
                                DmResult<Object> dmrTemp = new DmResult<>();
                                dmrTemp.setStatus(true);
                                RequisitionClose.this.callBack.result(dmrTemp);
                                RequisitionClose.this.callBack = null;
                            }
                        }
                    }
                });
            }

            @Override
            public RequisitionClose_Mdl getParam() {
                return requisitionClose;
            }
        });

        dmGrd.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        showRequisitionCloseGridEdit(event.getRowIndex());
                        event1 = event;
                       // openRecord(false, dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode(), dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getvSNo());
                    } else if (event.getCellIndex() == 1) {
                        showRequisition(DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

//        dmGrd.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
//
//            @Override
//            public void onRowClick(RowClickEvent event) {
//                if (DmShared.iNull(event.getColumnIndex()).intValue() > 1) {
//                    gridEdit(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
//                }
//
//            }
//        });
//        dmGrd.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {
//
//            @Override
//            public void onKeyDown(KeyDownEvent event) {
//                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
//                    gridEdit(dmGrd.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
//                }
//            }
//        }, KeyDownEvent.getType());

    }

    private void showRequisition(final String strKey) {
        swi.setWidget(new pmPharmacy().moduleName, new pmPharmacy().REQUISITION_CLOSE.caption, new SetWidgetInvoker() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                final String docId = strKey;
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                ((Requisition) wgtObject).searchRecord(docId);
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
        requisitionClose = new RequisitionClose_Mdl();
        requisitionClose.setToDate(DmClient.getLoginDate());
        requisitionClose.setStatusCode("P");
        requisitionClose.setStatusName("Pending");
        dmGrd.reQuery();
    }

//    private void gridEdit(GrdRequisitionClose_Mdl grdIndentApprovalEntry_Mdl) {
//        gridEdit.show(grdIndentApprovalEntry_Mdl.getSharedKeyFieldCode());
//    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(RequisitionClose.this);
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

    public RequisitionClose(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        SrvAsync = new ServiceInvoker<RequisitionClose_SrvAsync>(GWT.create(RequisitionClose_Srv.class), RequisitionClose.class.getName());
        srvAsyncReq = new ServiceInvoker<Requisition_SrvAsync>(GWT.create(Requisition_Srv.class), Requisition.class.getName());
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

        if (paramHashMap != null) {
            Boolean value = (Boolean) paramHashMap.get("autoFill");
            if (value != null) {
                if (Boolean.valueOf(value)) {
                    onLoad();
                }
            }
        }

        swi.setWidget(this);
    }

    private void showSetting() {
        settingPopup.show(requisitionClose, true);

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

    
     private void showRequisitionCloseGridEdit(int currentRowIndex) {
        requisitionCloseGridEdit.show(currentRowIndex, true, false, false, this);
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

    
    private void onOKReason() {
        openRecord(false, dmGrd.getDmGrid().getGrid().getStore().get(event1.getRowIndex()).getSharedKeyFieldCode(), dmGrd.getDmGrid().getGrid().getStore().get(event1.getRowIndex()).getvSNo());
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

    @Override
    public void action(String nature, Object object, DmCallBack callBack) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "SHOW_DOCIDS":
                    onLoad((String) object, callBack);
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void onLoad(String reqRecIds_DocIds, DmCallBack callBack) {
        String[] tmp;
        this.callBack = callBack;
        requisitionClose = new RequisitionClose_Mdl();
        tmp = DmShared.xNull(reqRecIds_DocIds).trim().split("\\|");
        if (tmp != null) {
            if (tmp.length == 2) {
                requisitionClose.setRequisitionRecIds(tmp[0]);
                requisitionClose.setRequisitionDocIds(tmp[1]);
            }
        }
        dmGrd.reQuery();
    }

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
}
