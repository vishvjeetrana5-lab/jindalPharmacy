package dataman.accounts.trialBalance.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import dataman.accounts.trialBalance.shared.*;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.accounts.acDisplaySetting.client.AcDisplaySetting;
import dataman.accounts.acDisplaySetting.shared.*;
import dataman.accounts.reports.client.RptTrialBalance;
import dataman.accounts.self.shared.AcEnum.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public final class TrialBalance extends TrialBalance_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler {

    private final ServiceInvoker<TrialBalance_SrvAsync> srvAsync;
    private final AcDisplaySetting acDisplaySetting = new AcDisplaySetting();
    private DefaultValue_Mdl dvm;
    private List<DisplayRoute_Mdl> displayRouteList;
    public AcDisplaySetting_Mdl acDisplaySettingMdl;

    public TrialBalance(UserMenu menuPermission, final SetWidgetInvoker swi, HashMap paramHashMap) {
        super();
        srvAsync = new ServiceInvoker<TrialBalance_SrvAsync>(GWT.create(TrialBalance_Srv.class), TrialBalance.class.getName());
        if (paramHashMap != null) {
            if (paramHashMap.containsKey("displayRouteList")) {
                this.displayRouteList = (List<DisplayRoute_Mdl>) paramHashMap.get("displayRouteList");
            }
            if (paramHashMap.containsKey("acDisplaySetting")) {
                this.acDisplaySettingMdl = (AcDisplaySetting_Mdl) paramHashMap.get("acDisplaySetting");
            }
        }

        if (this.displayRouteList == null) {
            DisplayRoute_Mdl routeMdl = new DisplayRoute_Mdl();
            this.displayRouteList = new ArrayList<>();
            routeMdl.setCode("");
            routeMdl.setName("");
            routeMdl.setCodeType(CodeType.Group);
            routeMdl.setPageName(PageName.TrialBalance);
            this.displayRouteList.add(routeMdl);
        }

        this.asWidget().setTitle(widgetName);
        handlerEvents();
        gridEvents();
        textEvents();
        if (acDisplaySettingMdl == null) {
            acDisplaySetting.fillDefaultValue(new AcDisplaySetting.CallBackIniDefault() {
                @Override
                public void onSuccess(AcDisplaySetting_Mdl acDisplaySetting) {
                    acDisplaySettingMdl = acDisplaySetting;
                    swi.setWidget(TrialBalance.this);
                }
            });
        } else {
            swi.setWidget(TrialBalance.this);
        }

    }

    private void handlerEvents() {
        btnPrint.addClickHandler(this);
        btnBack.addClickHandler(this);
        btnSetting.addClickHandler(this);
        flwForm.addAttachHandler(this);
    }

    private void textEvents() {
        acDisplaySetting.addCallBack(new AcDisplaySetting.CallBack() {

            @Override
            public void onOk(AcDisplaySetting_Mdl acDisplaySetting) {
                acDisplaySettingMdl = acDisplaySetting;
                refreshGrid();
            }

            @Override
            public void onClose() {

            }
        });
    }

    private void refreshGrid() {
        srvAsync.invokeRPC(true).getDefault(acDisplaySettingMdl, new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    fillData(result);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillData(DmResult<DefaultValue_Mdl> result) {
        dvm = result.getData();
        lblDate.setText(dvm.getDtFrom() + " To " + dvm.getDtTo());
        lblAmtDiff.setText(!(DmShared.xNull(dvm.getAmtDiff()).trim().equalsIgnoreCase("-") || DmShared.xNull(dvm.getAmtDiff()).trim().equalsIgnoreCase(""))
                ? (DmShared.xNull(dvm.getIsDifference()) + ": " + DmShared.xNull(dvm.getAmtDiff()) + " " + DmShared.xNull(dvm.getIsDrCr())) : "");
        lblAmtCr.setText(dvm.getAmtCr());
        lblAmtDr.setText(dvm.getAmtDr());
        grdDisplay.getGrid().getStore().clear();
        grdDisplay.getGrid().getStore().addAll(dvm.getLstTrialBalane());
        ((CellSelectionModel) grdDisplay.getGrid().getSelectionModel()).selectCell(0, 0);
    }

    private void gridEvents() {
        grdDisplay.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                moveTo(grdDisplay.getGrid().getStore().get(event.getRowIndex()));
            }
        });
        grdDisplay.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    int index = grdDisplay.getGrid().getStore().indexOf(grdDisplay.getGrid().getSelectionModel().getSelectedItem());
                    moveTo(grdDisplay.getGrid().getStore().get(index));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void moveTo(final TrialBalance_Mdl tbm) {
        DisplayRoute_Mdl displayRoute = new DisplayRoute_Mdl();

        if (DmShared.iNull(tbm.getChildSubCode()).longValue() != 0) {
            displayRoute.setPageName(PageName.Ledger);
            displayRoute.setCodeType(CodeType.SubGroup);
            displayRoute.setCode(DmShared.iNull(tbm.getChildSubCode()).toString());
            displayRoute.setName(DmShared.xNull(tbm.getChildSubName()));
        } else if (DmShared.iNull(tbm.getChildGroupCode()).longValue() != 0) {
            displayRoute.setPageName(PageName.AcGroup);
            displayRoute.setCodeType(CodeType.Group);
            displayRoute.setCode(DmShared.iNull(tbm.getChildGroupCode()).toString());
            displayRoute.setName(DmShared.xNull(tbm.getChildGroupName()));
        } else if (DmShared.isNumeric(tbm.getSharedKeyFieldCode()) && DmShared.iNull(tbm.getSharedKeyFieldCode()).longValue() != 0) {
            displayRoute.setPageName(PageName.AcGroup);
            displayRoute.setCodeType(CodeType.Group);
            displayRoute.setCode(DmShared.iNull(tbm.getSharedKeyFieldCode()).toString());
            displayRoute.setName(DmShared.xNull(tbm.getSharedKeyFieldName()));
        } else {
            return;
        }
        displayRouteList.add(displayRoute);
        setNextWidget();
    }

    private void setNextWidget() {
        HashMap paramHashMap = new HashMap();
        String menuText = "";
        try {
            paramHashMap.put("displayRouteList", displayRouteList);
            paramHashMap.put("acDisplaySetting", acDisplaySettingMdl);

            if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.AcGroup.getPageValue()) {
                menuText = new lmAccounts().A_C_GROUP_DETAIL_DISPLAY.caption;
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.Ledger.getPageValue()) {
                menuText = new lmAccounts().LEDGER_DISPLAY.caption;
            }

            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, menuText, paramHashMap, null, new SetWidgetInvokerImpl() {

                @Override
                public void setWidget(IsWidget wgtObject) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(TrialBalance.this);
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }
            });

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void close() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(TrialBalance.this);
    }

    private void onSelection() {
        TrialBalance_Mdl tbm = grdDisplay.getGrid().getSelectionModel().getSelectedItem();
        int index = -1;

        if (tbm != null) {
            index = grdDisplay.getGrid().getStore().indexOf(tbm);
            if (index >= 0) {
                moveTo(tbm);
            }
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnSetting)) {
            acDisplaySetting.show(btnSetting, acDisplaySettingMdl);
        } else if (event.getSource().equals(btnBack)) {
            this.close();
        } else if (event.getSource().equals(btnPrint)) {
            LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap();

            TrialBalanceReport_Mdl balanceReport_Mdl = new TrialBalanceReport_Mdl();

            List<GrdTrialBalanceReport_Mdl> lstTemp = new ArrayList<GrdTrialBalanceReport_Mdl>();

            for (TrialBalance_Mdl prMdl : grdDisplay.getGrid().getStore().getAll()) {

                if (!DmShared.xNull(prMdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    GrdTrialBalanceReport_Mdl lst = new GrdTrialBalanceReport_Mdl();
                    lst.setParticularsTag(DmShared.xNull(prMdl.getSharedKeyFieldCode()));
                    if (DmShared.iNull(prMdl.getChildSubCode()).longValue() != 0) {
                        lst.setParticularsText(DmShared.xNull(prMdl.getChildSubName()));
                        lst.setIsChild("1");
                        lst.setIsBold("0");
                    } else if (DmShared.iNull(prMdl.getChildGroupCode()).longValue() != 0) {
                        lst.setParticularsText(DmShared.xNull(prMdl.getChildGroupName()));
                        lst.setIsChild("1");
                        lst.setIsBold("1");
                    } else {
                        lst.setParticularsText(DmShared.xNull(prMdl.getSharedKeyFieldName()));
                        lst.setIsChild("0");
                        lst.setIsBold("1");
                    }
                    lst.setChildBalance(DmShared.xNull(prMdl.getChildBalance()));
                    lst.setAmtCr(DmShared.xNull(prMdl.getCredit()));
                    lst.setAmtDr(DmShared.xNull(prMdl.getDebit()));
                    lstTemp.add(lst);
                }
            }

            balanceReport_Mdl.getListTrialBalance().addAll(lstTemp);
            balanceReport_Mdl.setTotalCredit(DmShared.xNull(lblAmtCr.getText()));
            balanceReport_Mdl.setTotalDebit(DmShared.xNull(lblAmtDr.getText()));
            balanceReport_Mdl.setTotalDifference(DmShared.xNull(lblAmtDiff.getText()));

            RptTrialBalance prnReceipt = new RptTrialBalance(acDisplaySettingMdl, balanceReport_Mdl);
            prnReceipt.setDefault(hashMapFilter, false, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.close();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            refreshGrid();
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }
    
}
