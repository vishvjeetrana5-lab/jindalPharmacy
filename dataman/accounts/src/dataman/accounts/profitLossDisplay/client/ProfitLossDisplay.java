package dataman.accounts.profitLossDisplay.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.accounts.profitLossDisplay.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.accounts.acDisplaySetting.client.AcDisplaySetting;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl;
import dataman.accounts.reports.client.RptProfitLoss;
import dataman.accounts.self.shared.AcEnum.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public final class ProfitLossDisplay extends ProfitLossDisplay_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler {

    private final ServiceInvoker<ProfitLossDisplay_SrvAsync> srvAsync;
    private final AcDisplaySetting acDisplaySetting = new AcDisplaySetting();
    private DefaultValue_Mdl dvm;
    private List<DisplayRoute_Mdl> displayRouteList;
    public AcDisplaySetting_Mdl acDisplaySettingMdl;

    public ProfitLossDisplay(UserMenu menuPermission, final SetWidgetInvoker swi, HashMap paramHashMap) {
        super();
        srvAsync = new ServiceInvoker<ProfitLossDisplay_SrvAsync>(GWT.create(ProfitLossDisplay_Srv.class), ProfitLossDisplay.class.getName());
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
            routeMdl.setPageName(PageName.ProfitLoss);
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
                    swi.setWidget(ProfitLossDisplay.this);
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
        btnFinancialBook.addClickHandler(this);
        flwForm.addAttachHandler(this);
    }

    private void manageGrid(List<ProfitLossDisplay_Mdl> profitLossDisplayList) {
        grdDisplay.getGrid().getStore().clear();
        grdDisplay.getGrid().getStore().addAll(profitLossDisplayList);
    }

    private void gridEvents() {
        grdDisplay.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                ProfitLossDisplay_Mdl plm = grdDisplay.getGrid().getStore().get(event.getRowIndex());
                moveTo(plm, event.getColumnIndex());
            }
        });
    }

    private void moveTo(ProfitLossDisplay_Mdl plm, int columnIndex) {
        DisplayRoute_Mdl displayRoute = new DisplayRoute_Mdl();

        switch (columnIndex) {
            case 0:
                if (DmShared.iNull(plm.getExpensesChildSubCode()).longValue() != 0) {
                    displayRoute.setPageName(PageName.Ledger);
                    displayRoute.setCodeType(CodeType.SubGroup);
                    displayRoute.setCode(DmShared.iNull(plm.getExpensesChildSubCode()).toString());
                    displayRoute.setName(DmShared.xNull(plm.getExpensesChildSubName()));
                } else if (DmShared.iNull(plm.getExpensesChildGroupCode()).longValue() != 0) {
                    displayRoute.setPageName(PageName.AcGroup);
                    displayRoute.setCodeType(CodeType.Group);
                    displayRoute.setCode(DmShared.iNull(plm.getExpensesChildGroupCode()).toString());
                    displayRoute.setName(DmShared.xNull(plm.getExpensesChildGroupName()));
                } else if (DmShared.isNumeric(plm.getExpensesCode()) && DmShared.iNull(plm.getExpensesCode()).longValue() != 0) {
                    displayRoute.setPageName(PageName.AcGroup);
                    displayRoute.setCodeType(CodeType.Group);
                    displayRoute.setCode(DmShared.iNull(plm.getExpensesCode()).toString());
                    displayRoute.setName(DmShared.xNull(plm.getExpensesName()));
                } else {
                    return;
                }
                break;
            case 3:
                if (DmShared.iNull(plm.getRevenueChildSubCode()).longValue() != 0) {
                    displayRoute.setPageName(PageName.Ledger);
                    displayRoute.setCodeType(CodeType.SubGroup);
                    displayRoute.setCode(DmShared.iNull(plm.getRevenueChildSubCode()).toString());
                    displayRoute.setName(DmShared.xNull(plm.getRevenueChildSubName()));
                } else if (DmShared.iNull(plm.getRevenueChildGroupCode()).longValue() != 0) {
                    displayRoute.setPageName(PageName.AcGroup);
                    displayRoute.setCodeType(CodeType.Group);
                    displayRoute.setCode(DmShared.iNull(plm.getRevenueChildGroupCode()).toString());
                    displayRoute.setName(DmShared.xNull(plm.getRevenueChildGroupName()));
                } else if (DmShared.isNumeric(plm.getRevenueCode()) && DmShared.iNull(plm.getRevenueCode()).longValue() != 0) {
                    displayRoute.setPageName(PageName.AcGroup);
                    displayRoute.setCodeType(CodeType.Group);
                    displayRoute.setCode(DmShared.iNull(plm.getRevenueCode()).toString());
                    displayRoute.setName(DmShared.xNull(plm.getRevenueName()));
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
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.BalanceSheet.getPageValue()) {
                menuText = new lmAccounts().BALANCE_SHEET_DISPLAY.caption;
            }

            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, menuText, paramHashMap, null, new SetWidgetInvokerImpl() {

                @Override
                public void setWidget(IsWidget wgtObject) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ProfitLossDisplay.this);
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
            acDisplaySetting.show(btnSetting, acDisplaySettingMdl);
        } else if (event.getSource().equals(btnBack)) {
            onBack();
        } else if (event.getSource().equals(btnFinancialBook)) {
            showFinancialBookClose();
        } else if (event.getSource().equals(btnPrint)) {
            LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap();

            ProfitLossReport_Mdl profitLossReport_Mdl = new ProfitLossReport_Mdl();

            List<GrdProfitLossReport_Mdl> lstTemp = new ArrayList<GrdProfitLossReport_Mdl>();

            for (ProfitLossDisplay_Mdl prMdl : grdDisplay.getGrid().getStore().getAll()) {

                GrdProfitLossReport_Mdl lst = new GrdProfitLossReport_Mdl();
                lst.setExpensesCode(DmShared.xNull(prMdl.getExpensesCode()));
                if (DmShared.iNull(prMdl.getExpensesChildSubCode()).longValue() != 0) {
                    lst.setExpensesName(DmShared.xNull(prMdl.getExpensesChildSubName()));
                    lst.setIsExpensesChild("1");
                    lst.setIsExpensesBold("0");
                } else if (DmShared.iNull(prMdl.getExpensesChildGroupCode()).longValue() != 0) {
                    lst.setExpensesName(DmShared.xNull(prMdl.getExpensesChildGroupName()));
                    lst.setIsExpensesChild("1");
                    lst.setIsExpensesBold("1");
                } else {
                    lst.setExpensesName(DmShared.xNull(prMdl.getExpensesName()));
                    lst.setIsExpensesChild("0");
                    lst.setIsExpensesBold("1");
                }
                lst.setExpensesChildBalance(DmShared.xNull(prMdl.getExpensesChildBalance()));
                lst.setExpensesAmount(DmShared.xNull(prMdl.getExpensesAmount()));

                lst.setRevenueCode(DmShared.xNull(prMdl.getRevenueCode()));
                if (DmShared.iNull(prMdl.getRevenueChildSubCode()).longValue() != 0) {
                    lst.setRevenueName(DmShared.xNull(prMdl.getRevenueChildSubName()));
                    lst.setIsRevenueChild("1");
                    lst.setIsRevenueBold("0");
                } else if (DmShared.iNull(prMdl.getRevenueChildGroupCode()).longValue() != 0) {
                    lst.setRevenueName(DmShared.xNull(prMdl.getRevenueChildGroupName()));
                    lst.setIsRevenueChild("1");
                    lst.setIsRevenueBold("1");
                } else {
                    lst.setRevenueName(DmShared.xNull(prMdl.getRevenueName()));
                    lst.setIsRevenueChild("0");
                    lst.setIsRevenueBold("1");
                }
                lst.setRevenueChildBalance(DmShared.xNull(prMdl.getRevenueChildBalance()));
                lst.setRevenueAmount(DmShared.xNull(prMdl.getRevenueAmount()));
                lstTemp.add(lst);
            }

            profitLossReport_Mdl.getListProfitLoss().addAll(lstTemp);
            profitLossReport_Mdl.setTotalExpenseAmount(DmShared.xNull(lblTotal.getText()));
            profitLossReport_Mdl.setTotalRevenueAmount(DmShared.xNull(lblTotal1.getText()));

            RptProfitLoss prnProfitLoss = new RptProfitLoss(acDisplaySettingMdl, profitLossReport_Mdl);
            prnProfitLoss.setDefault(hashMapFilter, false, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
        }
    }

    private void showFinancialBookClose() {
        HashMap paramHashMap = new HashMap();
        paramHashMap.put("flagOpenAsPopup", true);
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, new lmAccounts().FINANCIAL_BOOK_CLOSE.caption, paramHashMap, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
            }
        });
    }

    private void onBack() {
        try {
            displayRouteList.remove((displayRouteList.size() - 1));
            if (displayRouteList.isEmpty()) {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ProfitLossDisplay.this);
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
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ProfitLossDisplay.this);
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
                    manageGrid(dvm.getProfitLossDisplayList());
                    lblTotal.setText(dvm.getTotal());
                    lblTotal1.setText(dvm.getTotal());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

}
