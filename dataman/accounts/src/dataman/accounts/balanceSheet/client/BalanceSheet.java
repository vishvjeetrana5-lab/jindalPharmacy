package dataman.accounts.balanceSheet.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import dataman.accounts.balanceSheet.shared.*;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.accounts.acDisplaySetting.client.AcDisplaySetting;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl;
import dataman.accounts.reports.client.RptBalanceSheet;
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

public final class BalanceSheet extends BalanceSheet_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler {

    private final ServiceInvoker<BalanceSheet_SrvAsync> srvAsync;
    private final AcDisplaySetting acDisplaySetting = new AcDisplaySetting();
    private DefaultValue_Mdl dvm;
    private List<DisplayRoute_Mdl> displayRouteList;
    public AcDisplaySetting_Mdl acDisplaySettingMdl;

    public BalanceSheet(UserMenu menuPermission, final SetWidgetInvoker swi, HashMap paramHashMap) {
        super();
        srvAsync = new ServiceInvoker<BalanceSheet_SrvAsync>(GWT.create(BalanceSheet_Srv.class), BalanceSheet.class.getName());
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
            routeMdl.setPageName(PageName.BalanceSheet);
            this.displayRouteList.add(routeMdl);
        }
        this.asWidget().setTitle(widgetName);
        handlerEvents();
        textEvents();
        gridEvents();
        if (acDisplaySettingMdl == null) {
            acDisplaySetting.fillDefaultValue(new AcDisplaySetting.CallBackIniDefault() {

                @Override
                public void onSuccess(AcDisplaySetting_Mdl acDisplaySetting) {
                    acDisplaySettingMdl = acDisplaySetting;
                    swi.setWidget(BalanceSheet.this);
                }
            });
        } else {
            swi.setWidget(BalanceSheet.this);
        }
    }

    private void handlerEvents() {
        btnPrint.addClickHandler(this);
        btnBack.addClickHandler(this);
        btnSetting.addClickHandler(this);
        flwForm.addAttachHandler(this);
    }

    private void manageGrid(List<BalanceSheet_Mdl> lstBalanceSheet) {

        grdDisplay.getGrid().getStore().clear();
        grdDisplay.getGrid().getStore().addAll(lstBalanceSheet);
    }

    private void gridEvents() {
        grdDisplay.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                BalanceSheet_Mdl bsm = grdDisplay.getGrid().getStore().get(event.getRowIndex());
                moveTo(bsm, event.getColumnIndex());
            }
        });
    }

    private void moveTo(BalanceSheet_Mdl bsm, int columnIndex) {
        DisplayRoute_Mdl displayRoute = new DisplayRoute_Mdl();

        switch (columnIndex) {
            case 0:
                if (DmShared.iNull(bsm.getLiabilityChildSubCode()).longValue() != 0) {
                    displayRoute.setPageName(PageName.Ledger);
                    displayRoute.setCodeType(CodeType.SubGroup);
                    displayRoute.setCode(DmShared.iNull(bsm.getLiabilityChildSubCode()).toString());
                    displayRoute.setName(DmShared.xNull(bsm.getLiabilityChildSubName()));
                } else if (DmShared.iNull(bsm.getLiabilityChildGroupCode()).longValue() != 0) {
                    displayRoute.setPageName(PageName.AcGroup);
                    displayRoute.setCodeType(CodeType.Group);
                    displayRoute.setCode(DmShared.iNull(bsm.getLiabilityChildGroupCode()).toString());
                    displayRoute.setName(DmShared.xNull(bsm.getLiabilityChildGroupName()));
                } else if (DmShared.xNull(bsm.getLiabilityCode()).trim().equalsIgnoreCase("NP")) {
                    displayRoute.setPageName(PageName.ProfitLoss);
                    displayRoute.setCodeType(CodeType.Group);
                } else if (DmShared.isNumeric(bsm.getLiabilityCode()) && DmShared.iNull(bsm.getLiabilityCode()).longValue() != 0) {
                    displayRoute.setPageName(PageName.AcGroup);
                    displayRoute.setCodeType(CodeType.Group);
                    displayRoute.setCode(DmShared.iNull(bsm.getLiabilityCode()).toString());
                    displayRoute.setName(DmShared.xNull(bsm.getLiabilityName()));
                } else {
                    return;
                }
                break;
            case 3:
                if (DmShared.iNull(bsm.getAssetChildSubCode()).longValue() != 0) {
                    displayRoute.setPageName(PageName.Ledger);
                    displayRoute.setCodeType(CodeType.SubGroup);
                    displayRoute.setCode(DmShared.iNull(bsm.getAssetChildSubCode()).toString());
                    displayRoute.setName(DmShared.xNull(bsm.getAssetChildSubName()));
                } else if (DmShared.iNull(bsm.getAssetChildGroupCode()).longValue() != 0) {
                    displayRoute.setPageName(PageName.AcGroup);
                    displayRoute.setCodeType(CodeType.Group);
                    displayRoute.setCode(DmShared.iNull(bsm.getAssetChildGroupCode()).toString());
                    displayRoute.setName(DmShared.xNull(bsm.getAssetChildGroupName()));
                } else if (DmShared.xNull(bsm.getAssetCode()).trim().equalsIgnoreCase("NL")) {
                    displayRoute.setPageName(PageName.ProfitLoss);
                    displayRoute.setCodeType(CodeType.Group);
                } else if (DmShared.isNumeric(bsm.getAssetCode()) && DmShared.iNull(bsm.getAssetCode()).longValue() != 0) {
                    displayRoute.setPageName(PageName.AcGroup);
                    displayRoute.setCodeType(CodeType.Group);
                    displayRoute.setCode(DmShared.iNull(bsm.getAssetCode()).toString());
                    displayRoute.setName(DmShared.xNull(bsm.getAssetName()));
                } else {
                    return;
                }
                break;
            default:
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
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.ProfitLoss.getPageValue()) {

                menuText = new lmAccounts().PROFIT_AND_LOSS_DISPLAY.caption;
            }

            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, menuText, paramHashMap, null, new SetWidgetInvokerImpl() {

                @Override
                public void setWidget(IsWidget wgtObject) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(BalanceSheet.this);
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }
            });

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(BalanceSheet.this);
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
                    dvm = result.getData();
                    manageGrid(dvm.getLstBalanceSheet());
                    lblDate.setText(dvm.getDtFrom() + " To " + dvm.getDtTo());
                    lblLiabilitiesAmount.setText(dvm.getAmountLiabilities());
                    lblAssestsAmount.setText(dvm.getAmountAssests());
                    lblBalance.setText(dvm.getAmountDifference());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
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
            this.onClose();
        } else if (event.getSource().equals(btnPrint)) {
            LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap();

            BalanceSheetReport_Mdl balanceReport_Mdl = new BalanceSheetReport_Mdl();

            List<GrdBalanceSheetReport_Mdl> lstTemp = new ArrayList<GrdBalanceSheetReport_Mdl>();

            for (BalanceSheet_Mdl prMdl : grdDisplay.getGrid().getStore().getAll()) {

                GrdBalanceSheetReport_Mdl lst = new GrdBalanceSheetReport_Mdl();
                lst.setLiabiltyCode(DmShared.xNull(prMdl.getLiabilityCode()));
                if (DmShared.iNull(prMdl.getLiabilityChildSubCode()).longValue() != 0) {
                    lst.setLiabiltyName(DmShared.xNull(prMdl.getLiabilityChildSubName()));
                    lst.setIsLiabiltyChild("1");
                    lst.setIsLiabiltyBold("0");
                } else if (DmShared.iNull(prMdl.getLiabilityChildGroupCode()).longValue() != 0) {
                    lst.setLiabiltyName(DmShared.xNull(prMdl.getLiabilityChildGroupName()));
                    lst.setIsLiabiltyChild("1");
                    lst.setIsLiabiltyBold("1");
                } else {
                    lst.setLiabiltyName(DmShared.xNull(prMdl.getLiabilityName()));
                    lst.setIsLiabiltyChild("0");
                    lst.setIsLiabiltyBold("1");
                }
                lst.setLiabiltyChildBalance(DmShared.xNull(prMdl.getLiabilityChildBalance()));
                lst.setLiabiltyAmount(DmShared.xNull(prMdl.getLiabilityAmount()));

                lst.setAssetCode(DmShared.xNull(prMdl.getAssetCode()));
                if (DmShared.iNull(prMdl.getAssetChildSubCode()).longValue() != 0) {
                    lst.setAssetName(DmShared.xNull(prMdl.getAssetChildSubName()));
                    lst.setIsAssetChild("1");
                    lst.setIsAssetBold("0");
                } else if (DmShared.iNull(prMdl.getAssetChildGroupCode()).longValue() != 0) {
                    lst.setAssetName(DmShared.xNull(prMdl.getAssetChildGroupName()));
                    lst.setIsAssetChild("1");
                    lst.setIsAssetBold("1");
                } else {
                    lst.setAssetName(DmShared.xNull(prMdl.getAssetName()));
                    lst.setIsAssetChild("0");
                    lst.setIsAssetBold("1");
                }
                lst.setAssetChildBalance(DmShared.xNull(prMdl.getAssetChildBalance()));
                lst.setAssetAmount(DmShared.xNull(prMdl.getAssetAmount()));

                lstTemp.add(lst);
            }

            balanceReport_Mdl.getListBalanceSheet().addAll(lstTemp);
            balanceReport_Mdl.setTotalLiabilitiesAmount(DmShared.xNull(lblLiabilitiesAmount.getText()));
            balanceReport_Mdl.setTotalAssetsAmount(DmShared.xNull(lblAssestsAmount.getText()));
            balanceReport_Mdl.setTotalDifference(DmShared.xNull(lblBalance.getText()));

            RptBalanceSheet prnReceipt = new RptBalanceSheet(acDisplaySettingMdl, balanceReport_Mdl);
            prnReceipt.setDefault(hashMapFilter, false, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.onClose();
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
