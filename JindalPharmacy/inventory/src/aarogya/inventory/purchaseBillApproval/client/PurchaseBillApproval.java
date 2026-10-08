package aarogya.inventory.purchaseBillApproval.client;

import aarogya.inventory.purchaseBillApproval.shared.*;
import aarogya.inventory.purchaseBill.client.PurchaseBill;
import aarogya.inventory.purchaseBill.shared.PurchaseBill_Srv;
import aarogya.inventory.purchaseBill.shared.PurchaseBill_SrvAsync;
import aarogya.inventory.purchaseBillDirect.client.PurchaseBillDirect;
import aarogya.inventory.purchaseBillDirect.shared.PurchaseBillDirect_Srv;
import aarogya.inventory.purchaseBillDirect.shared.PurchaseBillDirect_SrvAsync;
import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.HashMap;

public class PurchaseBillApproval extends PurchaseBillApproval_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents, BlurHandler, KeyUpHandler {

    ServiceInvoker<PurchaseBillApproval_SrvAsync> srvAsync;
    ServiceInvoker<PurchaseBill_SrvAsync> srvAsyncPBill;
    ServiceInvoker<PurchaseBillDirect_SrvAsync> srvAsyncPBillDirect;
    Setting_Mdl purchaseApproval_Mdl;
    final pmInventory pmInventory = new pmInventory();
    final Setting settingPopup = new Setting();
    WalkThrough walkThroughPBill;
    WalkThrough walkThroughPBillDirect;
    String category = "";
    SetWidgetInvoker swi;
    private String searchDbFieldName;
    private int colIndex;
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;

    private void textHelpEvents() {
        walkThroughPBill.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                dmGrd.reQuery();
            }
        });
        walkThroughPBillDirect.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                dmGrd.reQuery();
            }
        });

        settingPopup.addPurchaseApprovalCallBack(new Setting.PurchaseApprovalCallBack() {

            @Override
            public void onOk(Setting_Mdl approvalEntry_Mdl) {
                purchaseApproval_Mdl = new Setting_Mdl();
                purchaseApproval_Mdl = approvalEntry_Mdl;
                dmGrd.reQuery();
            }

        });

    }

    private void gridEvents() {

        dmGrd.addCallBack(new DmGridPaging.CallBack<Setting_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, Setting_Mdl param) {
                srvAsync.invokeRPC(true).getFillRecords(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdPurchaseBillApproval_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdPurchaseBillApproval_Mdl>> result) {
                        if (result.isStatus()) {
                            dmGrd.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public Setting_Mdl getParam() {
                return purchaseApproval_Mdl;
            }
        });

        dmGrd.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    GrdPurchaseBillApproval_Mdl gMdl = dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex());

                    if (event.getCellIndex() == 0) {
                        int val = DmShared.iNull(gMdl.getIsChecked()).intValue();

                        if (DmShared.xNull(gMdl.getvCat()).equalsIgnoreCase(new ProjectVoucherCategory().PURCHASE_BILL)
                                || DmShared.xNull(gMdl.getvCat()).equalsIgnoreCase(new ProjectVoucherCategory().PURCHASE_BILL_UNREGISTERED)
                                || DmShared.xNull(gMdl.getvCat()).equalsIgnoreCase(new ProjectVoucherCategory().SERVICE_BILL)
                                || DmShared.xNull(gMdl.getvCat()).equalsIgnoreCase(new ProjectVoucherCategory().SERVICE_BILL_UNREGISTERED)) {
                            setApprovedPBill(gMdl.getSharedKeyFieldCode(), gMdl.getWtaCode(), (val == 0));
                        } else if (DmShared.xNull(gMdl.getvCat()).equalsIgnoreCase(new ProjectVoucherCategory().PURCHASE_BILL_DIRECT)
                                || DmShared.xNull(gMdl.getvCat()).equalsIgnoreCase(new ProjectVoucherCategory().PURCHASE_BILL_DIRECT_UNREGISTERED)) {
                            setApprovedPBillDirect(gMdl.getSharedKeyFieldCode(), gMdl.getWtaCode(), (val == 0));
                        }
                    } else if (event.getCellIndex() == 1) {
                        if (DmShared.xNull(gMdl.getvCat()).equalsIgnoreCase(new ProjectVoucherCategory().PURCHASE_BILL)
                                || DmShared.xNull(gMdl.getvCat()).equalsIgnoreCase(new ProjectVoucherCategory().PURCHASE_BILL_UNREGISTERED)
                                || DmShared.xNull(gMdl.getvCat()).equalsIgnoreCase(new ProjectVoucherCategory().SERVICE_BILL)
                                || DmShared.xNull(gMdl.getvCat()).equalsIgnoreCase(new ProjectVoucherCategory().SERVICE_BILL_UNREGISTERED)) {
                            showPurchaseBill(gMdl.getSharedKeyFieldCode(), pmInventory.PURCHASE_BILL.caption);
                        } else if (DmShared.xNull(gMdl.getvCat()).equalsIgnoreCase(new ProjectVoucherCategory().PURCHASE_BILL_DIRECT)
                                || DmShared.xNull(gMdl.getvCat()).equalsIgnoreCase(new ProjectVoucherCategory().PURCHASE_BILL_DIRECT_UNREGISTERED)) {
                            showPurchaseBill(gMdl.getSharedKeyFieldCode(), pmInventory.PURCHASE_BILL_DIRECT.caption);
                        }
                    }

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });

        setHeaderValue(0);
        dmGrd.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        txtSearchValue.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                    ((CellSelectionModel) dmGrd.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                }
            }
        });
    }

    private void showPurchaseBill(final String docId, final String menuText) {
        swi.setWidget(pmInventory.moduleName, menuText, new SetWidgetInvoker() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                if (DmShared.xNull(menuText).equalsIgnoreCase(pmInventory.PURCHASE_BILL.caption)) {
                    ((PurchaseBill) wgtObject).searchRecord(docId);
                } else if (DmShared.xNull(menuText).equalsIgnoreCase(pmInventory.PURCHASE_BILL_DIRECT.caption)) {
                    ((PurchaseBillDirect) wgtObject).searchRecord(docId);
                }
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
        purchaseApproval_Mdl = new Setting_Mdl();
        purchaseApproval_Mdl.setToDate(DmClient.getLoginDate());
        purchaseApproval_Mdl.setStatusCode("P");
        purchaseApproval_Mdl.setStatusName("Pending");
        if (!DmShared.xNull(category).equalsIgnoreCase("")) {
            purchaseApproval_Mdl.setCategoryName(DmShared.xNull(category));
        }
        dmGrd.reQuery();
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PurchaseBillApproval.this);
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

    public PurchaseBillApproval(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        srvAsync = new ServiceInvoker<PurchaseBillApproval_SrvAsync>(GWT.create(PurchaseBillApproval_Srv.class), PurchaseBillApproval.class.getName());
        srvAsyncPBill = new ServiceInvoker<PurchaseBill_SrvAsync>(GWT.create(PurchaseBill_Srv.class), PurchaseBill.class.getName());
        srvAsyncPBillDirect = new ServiceInvoker<PurchaseBillDirect_SrvAsync>(GWT.create(PurchaseBillDirect_Srv.class), PurchaseBillDirect.class.getName());
        dmGrd.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
        walkThroughPBill = new WalkThrough(srvAsyncPBill);
        walkThroughPBillDirect = new WalkThrough(srvAsyncPBillDirect);
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(btnSetting);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        btnSetting.addClickHandler(this);
        btnClose.addClickHandler(this);
        flwForm.addAttachHandler(this);
        handlerEvents();
        textHelpEvents();
        gridEvents();
        if (paramHashMap != null) {
            category = (String) paramHashMap.get("category");
        }
        onLoad();
        swi.setWidget(this);
    }

    private void showSetting() {
        settingPopup.show(true, category);
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

    private void setApprovedPBill(String docId, String wtaCode, boolean isApprove) {
        try {

            if (DmShared.iNull(wtaCode).longValue() != 0) {
                walkThroughPBill.show(wtaCode);
            } else {
                srvAsyncPBill.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
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

    private void setApprovedPBillDirect(String docId, String wtaCode, boolean isApprove) {
        try {

            if (DmShared.iNull(wtaCode).longValue() != 0) {
                walkThroughPBillDirect.show(wtaCode);
            } else {
                srvAsyncPBillDirect.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
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

    private void handlerEvents() {
        btnSetting.addClickHandler(this);
        btnClose.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addBlurHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            reQuery();
        }
    }

    private void setHeaderValue(int columnIndex) {
        if (columnIndex <= 1) {
            columnIndex = 2;
        }
        if ((dmGrd.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            colIndex = 2;
        } else if (columnIndex < 0) {
            colIndex = dmGrd.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            colIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) dmGrd.getDmGrid().getGrid().getColumnModel().getColumn(colIndex));

        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        dmGrd.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        dmGrd.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : dmGrd.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        dmGrd.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void reQuery() {
        dmGrd.getDmGrid().getGrid().getColumnModel().getColumn(0).setHeader("Supplier");
        dmGrd.getDmGrid().getGrid().getView().refresh(true);
        dmGrd.getHelper().setRowsStartFrom(0);
        dmGrd.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        dmGrd.reQuery();
    }
}
