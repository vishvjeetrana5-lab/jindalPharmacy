package aarogya.selfToolkit.labApproval.client;

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
import com.google.gwt.user.datepicker.client.CalendarUtil;
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
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import aarogya.projectMenu.self.shared.pmLab;
import aarogya.selfToolkit.labApproval.shared.DefaultValue_Mdl;
import aarogya.selfToolkit.labApproval.shared.GrdLabApproval_Mdl;
import aarogya.selfToolkit.labApproval.shared.LabApproval_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.labApproval.shared.LabApproval_Srv;
import aarogya.selfToolkit.labApproval.shared.LabApproval_SrvAsync;

public class LabApproval extends LabApproval_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents {

    private ServiceInvoker<LabApproval_SrvAsync> SrvAsync;

    private LabApproval_Mdl saleApp_Mdl;
    private WalkThrough walkThrough;
    private final Setting settingPopup = new Setting();
    SetWidgetInvoker swi;
    private int noOfDays = 0;
    protected DefaultValue_Mdl dvm;

    interface CallBack {

        public void onClose();
    }

    private CallBack callBackClose;

    public void addCallBack(CallBack callBack) {
        this.callBackClose = callBack;
    }

    private void textHelpEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                dmGrd.reQuery();
            }
        });
        settingPopup.addCallBack(new Setting.CallBack() {

            @Override
            public void onOk(LabApproval_Mdl labApproval_Mdl) {
                saleApp_Mdl = labApproval_Mdl;
                dmGrd.getDmGrid().commitChanges();
                dmGrd.reQuery();
            }
        });

    }

    private void gridEvents() {
        dmGrd.addCallBack(new DmGridPaging.CallBack<LabApproval_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, LabApproval_Mdl param) {
                SrvAsync.invokeRPC(true).getFillRecords(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdLabApproval_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdLabApproval_Mdl>> result) {
                        if (result.isStatus()) {
                            dmGrd.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public LabApproval_Mdl getParam() {
                return saleApp_Mdl;
            }
        });

        dmGrd.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {

                        int val = DmShared.vNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getIsChecked()).intValue();
                        GrdLabApproval_Mdl grdLabApproval_Mdl = dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex());
                        if (val == 0) {
                            setApproved(DmShared.iNull(grdLabApproval_Mdl.getSharedKeyFieldCode()).toString(), grdLabApproval_Mdl.getWtaCode(), true);

                        } else if (val == 1) {
                            setApproved(DmShared.iNull(grdLabApproval_Mdl.getSharedKeyFieldCode()).toString(), grdLabApproval_Mdl.getWtaCode(), false);
                        }
                    } else if (event.getCellIndex() == 1) {
                        showLAB(DmShared.xNull(dmGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()));
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

    public void showLAB(final String strKey) {
        if (this.swi == null) {
            SetWidgetInvoker swi = DmClient.<SetWidgetInvoker>getStoreItem("swiEvent");
            this.swi = swi;
        }
        swi.setWidget(new pmLab().moduleName, new pmLab().LAB_ENTRY.caption, new SetWidgetInvoker() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                final String docId = strKey;
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                ((IsWidgetExtended) wgtObject).searchRecord(docId);
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
        saleApp_Mdl = new LabApproval_Mdl();
        saleApp_Mdl.setToDate(DmShared.dNull(dvm.getCurrentDate()));

        Date tempDate = DmShared.convertDate(DmShared.dNull(dvm.getCurrentDate()));

        if (DmShared.iNull(noOfDays).intValue() >= 0) {
            if (DmShared.iNull(noOfDays).intValue() == DmShared.iNull(STEnum.PriorityDays.HIGH_PRIORITYDAYS.getDays()).intValue()) {
                CalendarUtil.addDaysToDate(tempDate, -DmShared.iNull(noOfDays).intValue());
                saleApp_Mdl.setToDate(DmShared.dNull(tempDate));
            } else if (DmShared.iNull(noOfDays).intValue() == DmShared.iNull(STEnum.PriorityDays.MEDIUM_PRIORITYDAYS.getDays()).intValue()) {
                CalendarUtil.addDaysToDate(tempDate, -DmShared.iNull(STEnum.PriorityDays.MEDIUM_PRIORITYDAYS.getDays()).intValue());
                saleApp_Mdl.setFromDate(DmShared.dNull(tempDate));
                tempDate = DmShared.convertDate(DmShared.dNull(dvm.getCurrentDate()));
                CalendarUtil.addDaysToDate(tempDate, -(DmShared.iNull(STEnum.PriorityDays.LOW_PRIORITYDAYS.getDays()).intValue() + 1));
                saleApp_Mdl.setToDate(DmShared.dNull(tempDate));
            } else if (DmShared.iNull(noOfDays).intValue() == DmShared.iNull(STEnum.PriorityDays.LOW_PRIORITYDAYS.getDays()).intValue()) {
                CalendarUtil.addDaysToDate(tempDate, -DmShared.iNull(noOfDays).intValue());
                saleApp_Mdl.setFromDate(DmShared.dNull(tempDate));
            }
        }
        saleApp_Mdl.setStatusCode("P");
        saleApp_Mdl.setStatusName("Pending");
        dmGrd.reQuery();
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    if (callBackClose != null) {
                        callBackClose.onClose();
                    }
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(LabApproval.this);
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
        } else if (event.getSource() == btnRefresh) {
            dmGrd.reQuery();
        }
    }

    public LabApproval(UserMenu menuPermission, SetWidgetInvoker swi, HashMap paramHashMap) {
        super(swi != null);
        this.swi = swi;
        SrvAsync = new ServiceInvoker<LabApproval_SrvAsync>(GWT.create(LabApproval_Srv.class), LabApproval.class.getName());
        walkThrough = new WalkThrough(SrvAsync);
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        toolContainer.addWidget(btnSetting);
        toolContainer.addWidget(btnRefresh);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        handlerEvents();
        textHelpEvents();
        gridEvents();
        if (paramHashMap != null) {
            if (paramHashMap.containsKey("priorityDays")) {
                noOfDays = (Integer) paramHashMap.get("priorityDays");
            }
        }

        iniDefaultValues(swi);
    }

    private void handlerEvents() {
        btnSetting.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnClose.addClickHandler(this);
        flwForm.addAttachHandler(this);
    }

    private void showSetting() {
        settingPopup.show(true, saleApp_Mdl);
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    dvm = result.getData();
                    if (swi != null) {
                        swi.setWidget(LabApproval.this);
                    }
                    onLoad();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
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
                SrvAsync.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
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
