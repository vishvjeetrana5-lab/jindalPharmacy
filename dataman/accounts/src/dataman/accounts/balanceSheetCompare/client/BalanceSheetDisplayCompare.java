package dataman.accounts.balanceSheetCompare.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.accounts.balanceSheetCompare.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.accounts.acDisplaySetting.client.AcDisplaySettingComparison;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl;
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

public final class BalanceSheetDisplayCompare extends BalanceSheetDisplayCompare_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler {

    private final ServiceInvoker<BalanceSheetDisplayCompare_SrvAsync> srvAsync;
    private final AcDisplaySettingComparison acDisplaySettingComparison = new AcDisplaySettingComparison();
    private DefaultValue_Mdl dvm;
    private List<DisplayRoute_Mdl> displayRouteList;
    public AcDisplaySetting_Mdl acDisplaySettingComparisonMdl;

    public BalanceSheetDisplayCompare(UserMenu menuPermission, final SetWidgetInvoker swi, HashMap paramHashMap) {
        super();
        srvAsync = new ServiceInvoker<BalanceSheetDisplayCompare_SrvAsync>(GWT.create(BalanceSheetDisplayCompare_Srv.class), BalanceSheetDisplayCompare.class.getName());
        if (paramHashMap != null) {
            if (paramHashMap.containsKey("displayRouteList")) {
                this.displayRouteList = (List<DisplayRoute_Mdl>) paramHashMap.get("displayRouteList");
            }
            if (paramHashMap.containsKey("acDisplaySetting")) {
                this.acDisplaySettingComparisonMdl = (AcDisplaySetting_Mdl) paramHashMap.get("acDisplaySetting");
            }
        }

        if (this.displayRouteList == null) {
            DisplayRoute_Mdl routeMdl = new DisplayRoute_Mdl();
            this.displayRouteList = new ArrayList<>();
            routeMdl.setCode("");
            routeMdl.setName("");
            routeMdl.setCodeType(CodeType.Group);
            routeMdl.setPageName(PageName.BalanceSheetCompare);
            this.displayRouteList.add(routeMdl);
        }
        this.asWidget().setTitle(widgetName);
        handlerEvents();
        textEvents();
        gridEvents();
        if (acDisplaySettingComparisonMdl == null) {
            acDisplaySettingComparison.fillDefaultValue(new AcDisplaySettingComparison.CallBackIniDefault() {

                @Override
                public void onSuccess(AcDisplaySetting_Mdl acDisplaySetting) {
                    acDisplaySettingComparisonMdl = acDisplaySetting;
                    swi.setWidget(BalanceSheetDisplayCompare.this);
                }
            });
        } else {
            swi.setWidget(this);
        }
    }

    private void handlerEvents() {
        btnPrint.addClickHandler(this);
        btnBack.addClickHandler(this);
        btnSetting.addClickHandler(this);
        flwForm.addAttachHandler(this);
    }

    private void manageGrid(List<BalanceSheetDisplayCompare_Mdl> profitLossDisplayList) {
        grdDisplay.getGrid().getStore().clear();
        grdDisplay.getGrid().getStore().addAll(profitLossDisplayList);
    }

    private void gridEvents() {
        grdDisplay.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                BalanceSheetDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(event.getRowIndex());
                moveTo(plm);
            }
        });
    }

    private void moveTo(BalanceSheetDisplayCompare_Mdl plm) {
        DisplayRoute_Mdl displayRoute = new DisplayRoute_Mdl();

        if (DmShared.iNull(plm.getA_LChildSubCode()).longValue() != 0) {
            displayRoute.setPageName(PageName.Ledger);
            displayRoute.setCodeType(CodeType.SubGroup);
            displayRoute.setCode(DmShared.iNull(plm.getA_LChildSubCode()).toString());
            displayRoute.setName(DmShared.xNull(plm.getA_LChildSubName()));
        } else if (DmShared.iNull(plm.getA_LChildGroupCode()).longValue() != 0) {
            displayRoute.setPageName(PageName.AcGroupCompare);
            displayRoute.setCodeType(CodeType.Group);
            displayRoute.setCode(DmShared.iNull(plm.getA_LChildGroupCode()).toString());
            displayRoute.setName(DmShared.xNull(plm.getA_LChildGroupName()));
        } else if (DmShared.isNumeric(plm.getA_LHeadCode()) && DmShared.iNull(plm.getA_LHeadCode()).longValue() != 0) {
            displayRoute.setPageName(PageName.AcGroupCompare);
            displayRoute.setCodeType(CodeType.Group);
            displayRoute.setCode(DmShared.iNull(plm.getA_LHeadCode()).toString());
            displayRoute.setName(DmShared.xNull(plm.getA_LHeadNameName()));
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
            paramHashMap.put("acDisplaySetting", acDisplaySettingComparisonMdl);

            if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.AcGroupCompare.getPageValue()) {
                menuText = new lmAccounts().A_C_GROUP_DETAIL_DISPLAY_COMPARISON.caption;
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.Ledger.getPageValue()) {
                menuText = new lmAccounts().LEDGER_DISPLAY.caption;
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.BalanceSheetCompare.getPageValue()) {
                menuText = new lmAccounts().BALANCE_SHEET_COMPARE_DISPLAY.caption;
            }

            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, menuText, paramHashMap, null, new SetWidgetInvokerImpl() {

                @Override
                public void setWidget(IsWidget wgtObject) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(BalanceSheetDisplayCompare.this);
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }
            });

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
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
            acDisplaySettingComparison.show(btnSetting, acDisplaySettingComparisonMdl);
        } else if (event.getSource().equals(btnBack)) {
            onBack();
        } else if (event.getSource().equals(btnPrint)) {
            LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap();

            BalanceSheetReportCompare_Mdl profitLossReportCompare_Mdl = new BalanceSheetReportCompare_Mdl();

            List<GrdBalanceSheetReportCompare_Mdl> lstTemp = new ArrayList<GrdBalanceSheetReportCompare_Mdl>();

            for (BalanceSheetDisplayCompare_Mdl prMdl : grdDisplay.getGrid().getStore().getAll()) {

                GrdBalanceSheetReportCompare_Mdl lst = new GrdBalanceSheetReportCompare_Mdl();
//                lst.setExpensesCode(DmShared.xNull(prMdl.getExpensesCode()));
//                if (DmShared.iNull(prMdl.getExpensesChildSubCode()).longValue() != 0) {
//                    lst.setExpensesName(DmShared.xNull(prMdl.getExpensesChildSubName()));
//                    lst.setIsExpensesChild("1");
//                    lst.setIsExpensesBold("0");
//                } else if (DmShared.iNull(prMdl.getExpensesChildGroupCode()).longValue() != 0) {
//                    lst.setExpensesName(DmShared.xNull(prMdl.getExpensesChildGroupName()));
//                    lst.setIsExpensesChild("1");
//                    lst.setIsExpensesBold("1");
//                } else {
//                    lst.setExpensesName(DmShared.xNull(prMdl.getExpensesName()));
//                    lst.setIsExpensesChild("0");
//                    lst.setIsExpensesBold("1");
//                }
//                lst.setExpensesChildBalance(DmShared.xNull(prMdl.getExpensesChildBalance()));
//                lst.setExpensesAmount(DmShared.xNull(prMdl.getExpensesAmount()));
//
//                lst.setRevenueCode(DmShared.xNull(prMdl.getRevenueCode()));
//                if (DmShared.iNull(prMdl.getRevenueChildSubCode()).longValue() != 0) {
//                    lst.setRevenueName(DmShared.xNull(prMdl.getRevenueChildSubName()));
//                    lst.setIsRevenueChild("1");
//                    lst.setIsRevenueBold("0");
//                } else if (DmShared.iNull(prMdl.getRevenueChildGroupCode()).longValue() != 0) {
//                    lst.setRevenueName(DmShared.xNull(prMdl.getRevenueChildGroupName()));
//                    lst.setIsRevenueChild("1");
//                    lst.setIsRevenueBold("1");
//                } else {
//                    lst.setRevenueName(DmShared.xNull(prMdl.getRevenueName()));
//                    lst.setIsRevenueChild("0");
//                    lst.setIsRevenueBold("1");
//                }
//                lst.setRevenueChildBalance(DmShared.xNull(prMdl.getRevenueChildBalance()));
//                lst.setRevenueAmount(DmShared.xNull(prMdl.getRevenueAmount()));
                lstTemp.add(lst);
            }

            profitLossReportCompare_Mdl.getListBalanceSheet().addAll(lstTemp);
            profitLossReportCompare_Mdl.setTotalAssetAmount(DmShared.xNull(lblTotal.getText()));
            profitLossReportCompare_Mdl.setTotalLiabilityAmount(DmShared.xNull(lblTotal1.getText()));

//            RptProfitLoss prnProfitLoss = new RptProfitLoss(acDisplaySettingComparisonMdl, profitLossReportCompare_Mdl);
//            prnProfitLoss.setDefault(hashMapFilter, false, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
        }
    }

    private void onBack() {
        try {
            displayRouteList.remove((displayRouteList.size() - 1));
            if (displayRouteList.isEmpty()) {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(BalanceSheetDisplayCompare.this);
            } else {
                setNextWidget();
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), btnBack).show();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(BalanceSheetDisplayCompare.this);
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

    @Override
    public void onKeyUp(KeyUpEvent event) {
    }

    private void textEvents() {
        acDisplaySettingComparison.addCallBack(new AcDisplaySettingComparison.CallBack() {

            @Override
            public void onOk(AcDisplaySetting_Mdl acDisplaySetting) {
                acDisplaySettingComparisonMdl = acDisplaySetting;
                refreshGrid();
            }

            @Override
            public void onClose() {

            }
        });
    }

    private void refreshGrid() {
        srvAsync.invokeRPC(true).getDefault(acDisplaySettingComparisonMdl, new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    dvm = result.getData();
                    lblDate.setText( acDisplaySettingComparisonMdl.getDateFrom() + " To " + acDisplaySettingComparisonMdl.getDateTo() );
                    lblLstDate.setText(acDisplaySettingComparisonMdl.getLstDateFrom() + " To " + acDisplaySettingComparisonMdl.getLstDateTo());
                    manageGrid(dvm.getProfitLossDisplayList());
                    lblTotal.setText("");
                    lblTotal1.setText("");
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

}
