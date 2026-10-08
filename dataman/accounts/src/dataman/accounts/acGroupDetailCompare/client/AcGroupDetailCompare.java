package dataman.accounts.acGroupDetailCompare.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import dataman.accounts.acGroupDetailCompare.shared.*;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.accounts.acDisplaySetting.client.AcDisplaySettingComparison;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl;
import dataman.accounts.self.shared.AcEnum.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
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

public final class AcGroupDetailCompare extends AcGroupDetailCompare_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    private final ServiceInvoker<AcGroupDetailCompare_SrvAsync> srvAsync;
    private final AcDisplaySettingComparison acDisplaySettingComparison = new AcDisplaySettingComparison();
    private DefaultValue_Mdl dvm;
    private String colName = "subName";
    private int colIndex;
    private DmDataType dataType = DmDataType.STRING;
    private List<DisplayRoute_Mdl> displayRouteList = null;
    public AcDisplaySetting_Mdl acDisplaySettingComparisonMdl;
    public AcDisplaySetting_Mdl acDisplaySettingMainMdl;

    public AcGroupDetailCompare(UserMenu menuPermission, final SetWidgetInvoker swi, HashMap paramHashMap) {
        super();
        srvAsync = new ServiceInvoker<AcGroupDetailCompare_SrvAsync>(GWT.create(AcGroupDetailCompare_Srv.class), AcGroupDetailCompare.class.getName());

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
            routeMdl.setPageName(PageName.AcGroupCompare);
            this.displayRouteList.add(routeMdl);
        }

        this.asWidget().setTitle(widgetName);
        grdDisplay.getHelper().addFilter(colName, dataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        handlerEvents();
        gridEvents();
        textEvents();
        if (acDisplaySettingComparisonMdl == null) {
            acDisplaySettingComparison.fillDefaultValue(new AcDisplaySettingComparison.CallBackIniDefault() {

                @Override
                public void onSuccess(AcDisplaySetting_Mdl acDisplaySetting) {
                    acDisplaySettingComparisonMdl = acDisplaySetting;
                    swi.setWidget(AcGroupDetailCompare.this);
                }
            });
        } else {
            swi.setWidget(AcGroupDetailCompare.this);
        }
    }

    private void handlerEvents() {
        btnPrint.addClickHandler(this);
        btnBack.addClickHandler(this);
//        btnClose.addClickHandler(this);
        btnSetting.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
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
        srvAsync.invokeRPC(true).getDefault(acDisplaySettingComparisonMdl, displayRouteList, new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

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
        lblHeading.setText(DmShared.xNull(dvm.getAcName()));
        lblDate.setText(dvm.getDtFrom() + " To " + dvm.getDtTo());
        lblLstDate.setText(dvm.getLstDtFrom() + " To " + dvm.getLstDtTo());
//        lblBalance.setText(!(DmShared.xNull(dvm.getBalance()).equalsIgnoreCase("-") || DmShared.xNull(dvm.getBalance()).trim().equalsIgnoreCase(""))
//                ? ("Balance: " + DmShared.xNull(dvm.getBalance()) + " " + DmShared.xNull(dvm.getIsDrCr())) : "");
//        lblTotalCredit.setText(dvm.getTotalCr());
//        lblTotalDebit.setText(dvm.getTotalDr());
//        lblLstTotalCredit.setText(dvm.getLstTotalCr());
//        lblLstTotalDebit.setText(dvm.getLstTotalDr());
        // lblAmtCl.setText(dvm.getAmtCl());
        //lblClDrCr.setText(dvm.getClDrCr());

        reQuery();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                txtSearchValue.setFocus(true);
            }
        });
    }

    private void gridEvents() {
        setHeaderValue(0);
        grdDisplay.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {

            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        grdDisplay.addCallBack(new DmGridPaging.CallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, KeyFieldExtended param) {
                srvAsync.invokeRPC(blnToEndOfFile).getAcGroupDetial(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<AcGroupDetailCompare_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<AcGroupDetailCompare_Mdl>> result) {
                        if (result.isStatus()) {
                            grdDisplay.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public KeyFieldExtended getParam() {
                return null;
            }
        });
        grdDisplay.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showAcGroup(grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
            }
        });
        grdDisplay.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    int index = grdDisplay.getDmGrid().getGrid().getStore().indexOf(grdDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                    showAcGroup(grdDisplay.getDmGrid().getGrid().getStore().get(index));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showAcGroup(final AcGroupDetailCompare_Mdl acGroupDetailCompare) {
        DisplayRoute_Mdl displayRoute = new DisplayRoute_Mdl();
        acDisplaySettingMainMdl = new AcDisplaySetting_Mdl();
        if (DmShared.iNull(acGroupDetailCompare.getSubCode()).longValue() == 0) {
            displayRoute.setPageName(PageName.AcGroupCompare);
            displayRoute.setCodeType(CodeType.Group);
            displayRoute.setCode(DmShared.iNull(acGroupDetailCompare.getSharedKeyFieldCode()).toString());
            displayRoute.setName(DmShared.xNull(acGroupDetailCompare.getSharedKeyFieldName()));
            displayRouteList.add(displayRoute);
            refreshGrid();
        } else {
            displayRoute.setCodeType(CodeType.SubGroup);
            displayRoute.setPageName(PageName.Ledger);
            displayRoute.setCode(DmShared.iNull(acGroupDetailCompare.getSubCode()).toString());
            displayRoute.setName(DmShared.xNull(acGroupDetailCompare.getSubName()));
            displayRouteList.add(displayRoute);
            acDisplaySettingMainMdl.setDateFrom(acDisplaySettingComparisonMdl.getDateFrom());
            acDisplaySettingMainMdl.setDateTo(acDisplaySettingComparisonMdl.getDateTo());
            acDisplaySettingMainMdl.setIncludeOpening(acDisplaySettingComparisonMdl.getIncludeOpening());
            acDisplaySettingMainMdl.setLedgerAcCodeList(acDisplaySettingComparisonMdl.getLedgerAcCodeList());
            acDisplaySettingMainMdl.setShowDetail(acDisplaySettingComparisonMdl.getShowDetail());
            acDisplaySettingMainMdl.setShowNarration(acDisplaySettingComparisonMdl.getShowNarration());
            acDisplaySettingMainMdl.setShowZeroValueAc(acDisplaySettingComparisonMdl.getShowZeroValueAc());
            acDisplaySettingMainMdl.setSiteCodeList(acDisplaySettingComparisonMdl.getSiteCodeList());
            setNextWidget();
        }
    }

    private void reQuery() {
        grdDisplay.getHelper().setRowsStartFrom(0);
        grdDisplay.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdDisplay.reQuery();
    }

    private void setNextWidget() {
        HashMap paramHashMap = new HashMap();
        String menuText = "";
        try {
//            paramHashMap.put("displayRouteList", displayRouteList);
            if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.TrialBalanceCompare.getPageValue()) {
                menuText = new lmAccounts().TRIAL_BALANCE_COMPARISON.caption;
                paramHashMap.put("acDisplaySetting", acDisplaySettingComparisonMdl);
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.Ledger.getPageValue()) {
                menuText = new lmAccounts().LEDGER_DISPLAY.caption;
//                paramHashMap.replace("acDisplaySetting",acDisplaySettingMdl, acDisplaySettingMainMdl);
                List<dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl> lstTemp = new ArrayList<>();
                //DisplayRoute_Mdl currentRoute = displayRouteList.get(displayRouteList.size() - 1);
                for (DisplayRoute_Mdl model : displayRouteList) {
                    dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl node = new dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl();
                    node.setCode(model.getCode());
                    node.setName(model.getName());
                    node.setCodeType(model.getCodeType());
                    node.setPageName(model.getPageName());
                    lstTemp.add(node);
                }
                paramHashMap.put("displayRouteList", lstTemp);
                paramHashMap.put("acDisplaySetting", acDisplaySettingComparisonMdl);
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.ProfitLossCompare.getPageValue()) {
                menuText = new lmAccounts().PROFIT_AND_LOSS_COMPARE_DISPLAY.caption;
                paramHashMap.put("acDisplaySetting", acDisplaySettingComparisonMdl);
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.BalanceSheetCompare.getPageValue()) {
                menuText = new lmAccounts().BALANCE_SHEET_COMPARE_DISPLAY.caption;
                paramHashMap.put("acDisplaySetting", acDisplaySettingComparisonMdl);
            }

            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, menuText, paramHashMap, null, new SetWidgetInvokerImpl() {

                @Override
                public void setWidget(IsWidget wgtObject) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AcGroupDetailCompare.this);
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }
            });

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void onSelection() {
        AcGroupDetailCompare_Mdl acGroupDetailCompare = grdDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        int index = -1;

        if (acGroupDetailCompare != null) {
            index = grdDisplay.getDmGrid().getGrid().getStore().indexOf(acGroupDetailCompare);
            if (index >= 0) {
                showAcGroup(acGroupDetailCompare);
            }
        }
    }

    private void setHeaderValue(int columnIndex) {
        if ((grdDisplay.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            colIndex = 0;
        } else if (columnIndex < 0) {
            colIndex = grdDisplay.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            colIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdDisplay.getDmGrid().getGrid().getColumnModel().getColumn(colIndex));
        colName = col.getDbFieldName();
        dataType = col.getDbFieldDataType();
        grdDisplay.getHelper().getFilter().get(0).setColumnName(colName);
        grdDisplay.getHelper().getFilter().get(0).setDataType(dataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : grdDisplay.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdDisplay.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void onBack() {
        try {
            displayRouteList.remove((displayRouteList.size() - 1));
            if (displayRouteList.isEmpty()) {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AcGroupDetailCompare.this);
            } else {
                if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.AcGroupCompare.getPageValue()) {
                    refreshGrid();
                } else {
                    setNextWidget();
                }
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), btnBack).show();
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
            RptAcGroupDetailCompare_Mdl balanceReport_Mdl = new RptAcGroupDetailCompare_Mdl();
            balanceReport_Mdl.setTotalCredit(DmShared.xNull(lblTotalCredit.getText()));
            balanceReport_Mdl.setTotalDebit(DmShared.xNull(lblTotalDebit.getText()));
            balanceReport_Mdl.setTotalDifference(DmShared.xNull(lblBalance.getText()));

//            RptAccountGroupDetail prnReport = new RptAccountGroupDetail(balanceReport_Mdl);
//            prnReport.setDefault(hashMapFilter, false, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onBack();
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
        if (event.getSource().equals(txtSearchValue)) {
            reQuery();
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdDisplay.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdDisplay.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (grdDisplay.getDmGrid().getGrid().getStore().size() > 0) {
                    if (grdDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem() == null) {
                        ((CellSelectionModel) grdDisplay.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                        onSelection();
                    }
                }
            }
        }
    }
}
