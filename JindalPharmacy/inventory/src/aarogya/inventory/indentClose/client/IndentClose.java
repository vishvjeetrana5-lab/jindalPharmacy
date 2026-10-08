package aarogya.inventory.indentClose.client;

import aarogya.inventory.indentClose.shared.IndentClose_Mdl;
import aarogya.inventory.indentClose.shared.GrdIndentClose_Mdl;
import aarogya.inventory.indentApproval.client.*;
import aarogya.inventory.indent.client.Indent;
import aarogya.inventory.indent.shared.Indent_Srv;
import aarogya.inventory.indent.shared.Indent_SrvAsync;
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

import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.ArrayList;
import java.util.HashMap;
import aarogya.inventory.indentClose.shared.IndentClose_SrvAsync;
import aarogya.inventory.indentClose.shared.IndentClose_Srv;

public class IndentClose extends RequisitionClose_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents {

    ServiceInvoker<IndentClose_SrvAsync> SrvAsync;
    ServiceInvoker<Indent_SrvAsync> srvAsyncIndent;
    private IndentClose_Mdl indentClose_Mdl;
    final Setting settingPopup = new Setting();
    WalkThrough walkThrough;
//    final IndentCloseGridEdit gridEdit = new IndentCloseGridEdit();
    SetWidgetInvoker swi;
    private final IndentCloseGridEdit<GrdIndentClose_Mdl, Grd_PrpAcs> indentCloseGridEdit = new IndentCloseGridEdit(dmGrd.getDmGrid());
    List<GrdIndentClose_Mdl> lstIndentMdlTemp;

    private void textHelpEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                dmGrd.reQuery();
            }
        });
        settingPopup.addIdentApprovalEntryCallBackCallBack(new Setting.IdentApprovalEntryCallBack() {

            @Override
            public void onOk(IndentClose_Mdl approvalEntry_Mdl) {
                indentClose_Mdl = approvalEntry_Mdl;
                dmGrd.reQuery();
            }
        });
    }

    private void getFillCancelRecords() {
        SrvAsync.invokeRPC(true).getFillCancelRecords(lstIndentMdlTemp, new AsyncCallback<DmResult<GrdIndentClose_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<GrdIndentClose_Mdl> result) {
                dmGrd.getDmGrid().commitChanges();
                dmGrd.reQuery();
            }
        });
    }

    private void gridEvents() {
        indentCloseGridEdit.addCallBack(new IndentCloseGridEdit.CallBack() {
            @Override
            public void onOk(GrdIndentClose_Mdl sampleDetails_Mdl) {
                getFillCancelRecords();

            }

            @Override
            public void onDelete() {
                dmGrd.getDmGrid().commitChanges();
            }

            @Override
            public void onRefresh() {

            }
        });

        dmGrd.addCallBack(new DmGridPaging.CallBack<IndentClose_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, IndentClose_Mdl param) {
                SrvAsync.invokeRPC(true).getFillRecords(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdIndentClose_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdIndentClose_Mdl>> result) {
                        if (result.isStatus()) {
                            dmGrd.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public IndentClose_Mdl getParam() {
                return indentClose_Mdl;
            }
        });

        dmGrd.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        if (DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getIsStatus()).equalsIgnoreCase("0")) {
                            GrdIndentClose_Mdl grdIndentClose_Mdl = dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex());
//                            int val = DmShared.iNull(grdIndentClose_Mdl.getIsChecked()).intValue();
//                            if (val != 0) {
                            List<GrdIndentClose_Mdl> lstIndentMdl = new ArrayList<>();
//                                for (GrdIndentClose_Mdl grdIndentClose_Mdl1 : dmGrd.getDmGrid().getGrid().getStore().getAll()) {
//                                    
//                                    lstIndentMdl.add(grdIndentClose_Mdl1);
//                                }
                            lstIndentMdl.add(grdIndentClose_Mdl);
                            lstIndentMdlTemp = lstIndentMdl;
                            showIndentCloseGridEdit(event.getRowIndex());

//                            }
//                            else if (val == 1) {
//                                setApproved(grdIndentClose_Mdl.getSharedKeyFieldCode(), grdIndentClose_Mdl.getWtaCode(), false);
//                            }
                        }
                    }
//                    else if (event.getCellIndex() == 1) {
//                        showIndent(DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
//                    }

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });

        dmGrd.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                if (DmShared.iNull(event.getColumnIndex()).intValue() > 1) {
                    String status = dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getIsStatus();
                    gridEdit(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode(), status);
                }

            }
        });
        dmGrd.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    String status = dmGrd.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getIsStatus();
                    gridEdit(dmGrd.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getSharedKeyFieldCode(), status);
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showIndent(final String strKey) {
        swi.setWidget(new pmPharmacy().moduleName, new pmPharmacy().INDENT.caption, new SetWidgetInvoker() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                final String docId = strKey;
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                ((Indent) wgtObject).searchRecord(docId);
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
        indentClose_Mdl = new IndentClose_Mdl();
        indentClose_Mdl.setToDate(DmClient.getLoginDate());
        indentClose_Mdl.setStatusCode("P");
        indentClose_Mdl.setStatusName("Pending");
        dmGrd.reQuery();
    }

    private void gridEdit(String docId, String isStatus) {
//        gridEdit.show(docId, isStatus);
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(IndentClose.this);
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
//        else if (event.getSource() == btnCloseAll) {
////            @Override
//            List<GrdIndentClose_Mdl> lstIndentMdl = new ArrayList<>();
//            for (GrdIndentClose_Mdl grdIndentClose_Mdl : dmGrd.getDmGrid().getGrid().getStore().getAll()) {
//                lstIndentMdl.add(grdIndentClose_Mdl);
//            }
//
//            SrvAsync.invokeRPC(true).getFillCancelRecords(lstIndentMdl, new AsyncCallback<DmResult<GrdIndentClose_Mdl>>() {
//
//                @Override
//                public void onFailure(Throwable caught) {
//                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
//                }
//
//                @Override
//                public void onSuccess(DmResult<GrdIndentClose_Mdl> result) {
//
//                    dmGrd.reQuery();
//                }
//            });
//
//        }
    }

    public IndentClose(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        SrvAsync = new ServiceInvoker<IndentClose_SrvAsync>(GWT.create(IndentClose_Srv.class), IndentClose.class.getName());
        srvAsyncIndent = new ServiceInvoker<Indent_SrvAsync>(GWT.create(Indent_Srv.class), Indent.class.getName());
        walkThrough = new WalkThrough(srvAsyncIndent);
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(btnSetting);
        toolContainer.addWidget(btnClose);
//        toolContainer.addWidget(btnCloseAll);
        toolContainer.addGap();
        btnSetting.addClickHandler(this);
        btnClose.addClickHandler(this);
//        btnCloseAll.addClickHandler(this);
        flwForm.addAttachHandler(this);
        textHelpEvents();
        gridEvents();
        onLoad();
        swi.setWidget(this);
    }

    private void showIndentCloseGridEdit(int currentRowIndex) {
        indentCloseGridEdit.show(currentRowIndex, true, false, false, this);
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
                srvAsyncIndent.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
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
