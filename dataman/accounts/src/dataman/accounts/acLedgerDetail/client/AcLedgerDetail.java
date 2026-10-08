package dataman.accounts.acLedgerDetail.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.accounts.acDisplaySetting.client.AcDisplaySetting;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl;
import dataman.accounts.acLedgerDetail.shared.*;
import dataman.accounts.reports.client.RptLedgerDisplay;
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
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public final class AcLedgerDetail extends AcLedgerDetail_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    private final ServiceInvoker<AcLedgerDetail_SrvAsync> srvAsync;
    private final AcDisplaySetting acDisplaySetting = new AcDisplaySetting();
    private DefaultValue_Mdl dvm;
    private String colName = "recId";
    private int colIndex;
    private DmDataType dataType = DmDataType.STRING;
    private List<DisplayRoute_Mdl> displayRouteList = null;
    public AcDisplaySetting_Mdl acDisplaySettingMdl;

    public AcLedgerDetail(UserMenu menuPermission, final SetWidgetInvoker swi, HashMap paramHashMap) {
        super();
        srvAsync = new ServiceInvoker<AcLedgerDetail_SrvAsync>(GWT.create(AcLedgerDetail_Srv.class), AcLedgerDetail.class.getName());

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
            routeMdl.setCodeType(CodeType.SubGroup);
            routeMdl.setPageName(PageName.Ledger);
            this.displayRouteList.add(routeMdl);
        }
        this.asWidget().setTitle(widgetName);
        grdDisplay.getHelper().addFilter(colName, dataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        handlerEvents();
        gridEvents();
        textEvents();
        if (acDisplaySettingMdl == null) {
            acDisplaySetting.fillDefaultValue(new AcDisplaySetting.CallBackIniDefault() {

                @Override
                public void onSuccess(AcDisplaySetting_Mdl acDisplaySetting) {
                    acDisplaySettingMdl = acDisplaySetting;
                    swi.setWidget(AcLedgerDetail.this);
                }
            });
        } else {
            swi.setWidget(AcLedgerDetail.this);
        }
    }

    private void handlerEvents() {
        btnPrint.addClickHandler(this);
        btnBack.addClickHandler(this);
        btnSetting.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
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
        srvAsync.invokeRPC(true).getDefault(acDisplaySettingMdl, displayRouteList, new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

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
        if (!DmShared.xNull(dvm.getAcForCode()).trim().equalsIgnoreCase("")){
            lblHeading.setText(dvm.getAcName());
        }else {
            lblHeading.setText(acDisplaySettingMdl.getLedgerAcNameList());
        }
        acDisplaySettingMdl.setLedgerAcNameList(dvm.getAcName());
                
        lblBalance.setText(!(DmShared.xNull(dvm.getBalance()).equalsIgnoreCase("-") || DmShared.xNull(dvm.getBalance()).trim().equalsIgnoreCase(""))
                ? ("Balance: " + DmShared.xNull(dvm.getBalance()) + " " + DmShared.xNull(dvm.getIsDrCr())) : "");
        lblTotalCredit.setText(!DmShared.xNull(dvm.getTotalCr()).equalsIgnoreCase("-") ? dvm.getTotalCr() : "");
        lblTotalDebit.setText(!DmShared.xNull(dvm.getTotalDr()).equalsIgnoreCase("-") ? dvm.getTotalDr() : "");
        reQuery();
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
                srvAsync.invokeRPC(blnToEndOfFile).getAcLedgerDetial(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<AcLedgerDetail_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<AcLedgerDetail_Mdl>> result) {
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
                setNextWidget(event.getRowIndex());
            }
        });
        grdDisplay.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    setNextWidget(grdDisplay.getDmGrid().getGrid().getStore().indexOf(grdDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem()));

                }
            }
        }, KeyDownEvent.getType());
    }

    private void reQuery() {
        grdDisplay.getHelper().setRowsStartFrom(0);
        grdDisplay.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdDisplay.reQuery();
    }

    private void setNextWidget(int index) {
        if (index >= 0) {
            final AcLedgerDetail_Mdl acLedgerDetail_Mdl = grdDisplay.getDmGrid().getGrid().getStore().get(index);
            if (acLedgerDetail_Mdl != null) {
                if (DmShared.iNull(DmClient.getVoucherPrefix()).intValue() == DmShared.iNull(acLedgerDetail_Mdl.getV_Prefix()).intValue()
                        && DmShared.iNull(dvm.getSiteCode()).intValue() == DmShared.iNull(acLedgerDetail_Mdl.getSite_Code()).intValue()) {

                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(acLedgerDetail_Mdl.getModuleName(), acLedgerDetail_Mdl.getMenuText(), null, null, new SetWidgetInvokerImpl() {

                        @Override
                        public void setWidget(IsWidget wgtObject) {
                            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                            ((IsWidgetExtended) wgtObject).searchRecord(acLedgerDetail_Mdl.getSharedKeyFieldCode());
                        }
                    });
                }
            }
        }
    }

    private void setNextWidget() {
        HashMap paramHashMap = new HashMap();
        String menuText = "";
        try {
            paramHashMap.put("displayRouteList", displayRouteList);
            paramHashMap.put("acDisplaySetting", acDisplaySettingMdl);
            if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.AcGroup.getPageValue()) {
                menuText = new lmAccounts().A_C_GROUP_DETAIL_DISPLAY.caption;
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.DetailTrialBalance.getPageValue()) {
                menuText = new lmAccounts().DETAIL_TRIAL_BALANCE_DISPLAY.caption;
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.TrialBalance.getPageValue()) {
                menuText = new lmAccounts().TRIAL_BALANCE_DISPLAY.caption;
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.ProfitLoss.getPageValue()) {
                menuText = new lmAccounts().PROFIT_AND_LOSS_DISPLAY.caption;
            } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.BalanceSheet.getPageValue()) {
                menuText = new lmAccounts().BALANCE_SHEET_DISPLAY.caption;
                } else if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.AcGroupCompare.getPageValue()) {
                menuText = new lmAccounts().A_C_GROUP_DETAIL_DISPLAY_COMPARISON.caption;
            }

            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, menuText, paramHashMap, null, new SetWidgetInvoker() {

                @Override
                public void setWidget(IsWidget wgtObject) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AcLedgerDetail.this);
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }

                @Override
                public void setWidget(String moduleName, String menuText, SetWidgetInvoker swi) {

                }

                @Override
                public void setWidget(String moduleName, String menuText, HashMap paramHashMap, UserMenu userMenu, SetWidgetInvoker swi) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuText, paramHashMap, userMenu, swi);
                }

                @Override
                public void closeWidget(IsWidget wgtObject) {

                }
            });

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AcLedgerDetail.this);
            } else {
                setNextWidget();
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
            acDisplaySetting.show(btnSetting, acDisplaySettingMdl);
        } else if (event.getSource().equals(btnBack)) {
            onBack();
        } else if (event.getSource().equals(btnPrint)) {
            LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap();
            AcLedgerDetailReport_Mdl balanceReport_Mdl = new AcLedgerDetailReport_Mdl();
            balanceReport_Mdl.setTotalCredit(DmShared.xNull(lblTotalCredit.getText()));
            balanceReport_Mdl.setTotalDebit(DmShared.xNull(lblTotalDebit.getText()));
            balanceReport_Mdl.setTotalDifference(DmShared.xNull(lblDrCr.getText()));
            balanceReport_Mdl.setTotalBalance(DmShared.xNull(lblBalance.getText()));
            
            RptLedgerDisplay prnReceipt = new RptLedgerDisplay(balanceReport_Mdl,acDisplaySettingMdl);
            prnReceipt.setDefault(hashMapFilter, false, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

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
                    }
                }
            }
        }
    }
}
