package aarogya.pharmacy.enquiryApproval.client;

import aarogya.pharmacy.enquiryApproval.shared.*;
import aarogya.pharmacy.enquiry.client.Enquiry;
import aarogya.pharmacy.enquiry.shared.Enquiry_Srv;
import aarogya.pharmacy.enquiry.shared.Enquiry_SrvAsync;
import aarogya.projectMenu.self.shared.pmPharmacy;
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
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.HashMap;
import java.util.List;

public class EnquiryApproval extends EnquiryApproval_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents {

    ServiceInvoker<EnquiryApproval_SrvAsync> SrvAsync;
    ServiceInvoker<Enquiry_SrvAsync> srvAsyncEnquiry;
    EnquiryApproval_Mdl enquiryApprovalEntry_Mdl;
    WalkThrough walkThrough;
    final Setting settingPopup = new Setting();
    final EnquiryApprovalGridEdit gridEdit = new EnquiryApprovalGridEdit();
    SetWidgetInvoker swi;

    private void textHelpEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                dmGrd.reQuery();
            }
        });
        settingPopup.addEnqApprovalEntryCallBackCallBack(new Setting.EnqApprovalEntryCallBack() {

            @Override
            public void onOk(EnquiryApproval_Mdl approvalEntry_Mdl) {
                enquiryApprovalEntry_Mdl = approvalEntry_Mdl;
                dmGrd.reQuery();
            }
        });

        gridEdit.addGridEditCallBack(new EnquiryApprovalGridEdit.GridEditCallBack() {

            @Override
            public void onOk(GrdEnquiryApproval_Mdl approvalEntry_Mdl) {
                dmGrd.getDmGrid().commitChanges();
                dmGrd.reQuery();
            }
        });

    }

    private void gridEvents() {
        dmGrd.addCallBack(new DmGridPaging.CallBack<EnquiryApproval_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, EnquiryApproval_Mdl param) {
                SrvAsync.invokeRPC(true).getFillRecords(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdEnquiryApproval_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdEnquiryApproval_Mdl>> result) {
                        if (result.isStatus()) {
                            dmGrd.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public EnquiryApproval_Mdl getParam() {
                return enquiryApprovalEntry_Mdl;
            }
        });

        dmGrd.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {
                        if (DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getIsStatus()).equalsIgnoreCase("0")) {
                            int val = DmShared.vNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                            GrdEnquiryApproval_Mdl grdEnquiryApproval_Mdl = dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex());
                            if (val == 0) {
                                setApproved(DmShared.iNull(grdEnquiryApproval_Mdl.getSharedKeyFieldCode()).toString(), DmShared.iNull(grdEnquiryApproval_Mdl.getWtaCode()).toString(), true);
                            } else if (val == 1) {
                                setApproved(DmShared.iNull(grdEnquiryApproval_Mdl.getSharedKeyFieldCode()).toString(), DmShared.iNull(grdEnquiryApproval_Mdl.getWtaCode()).toString(), false);
                            }
                        }
                    } else if (event.getCellIndex() == 1) {
                        showGRN(DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
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

    private void showGRN(final String strKey) {
        swi.setWidget(new pmPharmacy().moduleName, new pmPharmacy().ENQUIRY.caption, new SetWidgetInvoker() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                final String docId = strKey;
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                ((Enquiry) wgtObject).searchRecord(docId);
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

    private void gridEdit(GrdEnquiryApproval_Mdl grdIndentApprovalEntry_Mdl) {
        gridEdit.show(grdIndentApprovalEntry_Mdl.getSharedKeyFieldCode());
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EnquiryApproval.this);
                }
            }
        }).show();
    }

    private void onLoad() {
        enquiryApprovalEntry_Mdl = new EnquiryApproval_Mdl();
        enquiryApprovalEntry_Mdl.setToDate(DmClient.getLoginDate());
        enquiryApprovalEntry_Mdl.setStatusCode("P");
        enquiryApprovalEntry_Mdl.setStatusName("Pending");
        dmGrd.reQuery();
    }

    @Override
    public void onClick(ClickEvent event) {

        if (event.getSource() == btnClose) {

            onClose();
        } else if (event.getSource() == btnSetting) {
            showSetting();
        }
    }

    public EnquiryApproval(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        SrvAsync = new ServiceInvoker<EnquiryApproval_SrvAsync>(GWT.create(EnquiryApproval_Srv.class), EnquiryApproval.class.getName());
        srvAsyncEnquiry = new ServiceInvoker<Enquiry_SrvAsync>(GWT.create(Enquiry_Srv.class), Enquiry.class.getName());
        walkThrough = new WalkThrough(srvAsyncEnquiry);
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

    public void setApproved(String docId, String wtaCode, boolean isApprove) {
        try {
            if (DmShared.iNull(wtaCode).longValue() != 0) {
                walkThrough.show(wtaCode);
            } else {
                srvAsyncEnquiry.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
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
