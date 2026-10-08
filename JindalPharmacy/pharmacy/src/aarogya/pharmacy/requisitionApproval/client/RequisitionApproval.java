package aarogya.pharmacy.requisitionApproval.client;

import aarogya.pharmacy.requisition.client.Requisition;
import aarogya.pharmacy.requisition.shared.Requisition_Srv;
import aarogya.pharmacy.requisition.shared.Requisition_SrvAsync;
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
import aarogya.pharmacy.requisitionApproval.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.HashMap;

public class RequisitionApproval extends RequisitionApproval_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents {

    ServiceInvoker<RequisitionApproval_SrvAsync> SrvAsync;
    ServiceInvoker<Requisition_SrvAsync> srvAsyncReq;
    WalkThrough walkThrough;
    RequisitionApproval_Mdl requisitionApproval;
    final Setting settingPopup = new Setting();
    final RequisitionApprovalGridEdit gridEdit = new RequisitionApprovalGridEdit();
    DmCallBack callBack;
    SetWidgetInvoker swi;

    private void textHelpEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                dmGrd.reQuery();
            }
        });
        settingPopup.addRequisitionApprovalEntryCallBackCallBack(new Setting.RequisitionApprovalEntryCallBack() {

            @Override
            public void onOk(RequisitionApproval_Mdl approvalEntry_Mdl) {
                requisitionApproval = approvalEntry_Mdl;
                dmGrd.reQuery();
            }
        });

        gridEdit.addGridEditCallBack(new RequisitionApprovalGridEdit.GridEditCallBack() {

            @Override
            public void onOk(GrdRequisitionApproval_Mdl approvalEntry_Mdl) {
                dmGrd.getDmGrid().commitChanges();
                dmGrd.reQuery();
            }
        });
    }

    private void gridEvents() {
        dmGrd.addCallBack(new DmGridPaging.CallBack<RequisitionApproval_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, RequisitionApproval_Mdl param) {
                SrvAsync.invokeRPC(true).getFillRecords(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdRequisitionApproval_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdRequisitionApproval_Mdl>> result) {
                        if (result.isStatus()) {
                            dmGrd.setData(result.getData(), result.getPaging());
                            if (RequisitionApproval.this.callBack != null) {
                                DmResult<Object> dmrTemp = new DmResult<>();
                                dmrTemp.setStatus(true);
                                RequisitionApproval.this.callBack.result(dmrTemp);
                                RequisitionApproval.this.callBack = null;
                            }
                        }
                    }
                });
            }

            @Override
            public RequisitionApproval_Mdl getParam() {
                return requisitionApproval;
            }
        });

        dmGrd.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        if (DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getIsStatus()).equalsIgnoreCase("0")) {

                            int val = DmShared.vNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                            GrdRequisitionApproval_Mdl grdPurchaseOrderApproval_Mdl = dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex());
                            if (val == 0) {
                                setApproved(DmShared.iNull(grdPurchaseOrderApproval_Mdl.getSharedKeyFieldCode()).toString(), grdPurchaseOrderApproval_Mdl.getWtaCode(), true);

                            } else if (val == 1) {
                                setApproved(DmShared.iNull(grdPurchaseOrderApproval_Mdl.getSharedKeyFieldCode()).toString(), grdPurchaseOrderApproval_Mdl.getWtaCode(), false);
                            }
                        }
                    } else if (event.getCellIndex() == 1) {
                        showRequisition(DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
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
                    gridEdit(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
                }

            }
        });
        dmGrd.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridEdit(dmGrd.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showRequisition(final String strKey) {
        swi.setWidget(new pmPharmacy().moduleName, new pmPharmacy().REQUISITION.caption, new SetWidgetInvoker() {
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
        requisitionApproval = new RequisitionApproval_Mdl();
        requisitionApproval.setToDate(DmClient.getLoginDate());
        requisitionApproval.setStatusCode("P");
        requisitionApproval.setStatusName("Pending");
        dmGrd.reQuery();
    }

    private void gridEdit(GrdRequisitionApproval_Mdl grdIndentApprovalEntry_Mdl) {
        gridEdit.show(grdIndentApprovalEntry_Mdl.getSharedKeyFieldCode());
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(RequisitionApproval.this);
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

    public RequisitionApproval(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        SrvAsync = new ServiceInvoker<RequisitionApproval_SrvAsync>(GWT.create(RequisitionApproval_Srv.class), RequisitionApproval.class.getName());
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
        settingPopup.show(requisitionApproval, true);

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
    public void action(String nature, Object object, DmCallBack callBack) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "SHOW_DOCIDS":
                    onLoad((String) object, callBack);
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void onLoad(String reqRecIds_DocIds, DmCallBack callBack) {
        String[] tmp;
        this.callBack = callBack;
        requisitionApproval = new RequisitionApproval_Mdl();
        tmp = DmShared.xNull(reqRecIds_DocIds).trim().split("\\|");
        if (tmp != null) {
            if (tmp.length == 2) {
                requisitionApproval.setRequisitionRecIds(tmp[0]);
                requisitionApproval.setRequisitionDocIds(tmp[1]);
            }
        }
        dmGrd.reQuery();
    }

    @Override
    public void searchRecord(String key) {

    }

}
