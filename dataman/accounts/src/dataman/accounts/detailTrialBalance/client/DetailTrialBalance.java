package dataman.accounts.detailTrialBalance.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import dataman.accounts.detailTrialBalance.shared.*;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.accounts.acDisplaySetting.client.AcDisplaySetting;
import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.acDisplaySetting.shared.DisplayRoute_Mdl;
import dataman.accounts.reports.client.RptDetailTrialBalance;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public final class DetailTrialBalance extends DetailTrialBalance_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    private final ServiceInvoker<DetailTrialBalance_SrvAsync> srvAsync;
    private final AcDisplaySetting acDisplaySetting = new AcDisplaySetting();
    private DefaultValue_Mdl dvm;
    private String colName = "SName";
    private int colIndex;
    private DmDataType dataType = DmDataType.STRING;
    private List<DisplayRoute_Mdl> displayRouteList;
    public AcDisplaySetting_Mdl acDisplaySettingMdl;

    public DetailTrialBalance(UserMenu menuPermission, final SetWidgetInvoker swi, HashMap paramHashMap) {
        super();
        srvAsync = new ServiceInvoker<DetailTrialBalance_SrvAsync>(GWT.create(DetailTrialBalance_Srv.class), DetailTrialBalance.class.getName());

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
            routeMdl.setPageName(PageName.DetailTrialBalance);
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
                    iniDefaultValue(swi);
                }
            });
        } else {
            iniDefaultValue(swi);
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

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefault(acDisplaySettingMdl, new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    fillData(result);
                    swi.setWidget(DetailTrialBalance.this);
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            txtSearchValue.setFocus(true);
                        }
                    });
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
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
        lblAmtDiff.setText(!DmShared.xNull(dvm.getAmtDifference()).equalsIgnoreCase("-")
                ? (DmShared.xNull(dvm.getIsDifference()) + ": " + DmShared.xNull(dvm.getAmtDifference()) + " " + DmShared.xNull(dvm.getIsDrCr())) : "");
        lblAmtCr.setText(dvm.getAmtCr());
        lblAmtDr.setText(dvm.getAmtDr());
        lblAmtOp.setText(dvm.getAmtOp());

//        lblAmtCl.setText(dvm.getAmtCl());
//        lblClDrCr.setText(dvm.getClDrCr());
        lblOpDrCr.setText(dvm.getOpDrCr());
        grdDisplay.reQuery();
        ((CellSelectionModel) grdDisplay.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
    }

    private void gridEvents() {
        setHeaderValue(0);
        grdDisplay.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {

            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });
        grdDisplay.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                srvAsync.invokeRPC(blnToEndOfFile).getAcLedgerDetial(helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<DetailTrialBalance_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<DetailTrialBalance_Mdl>> result) {
                        if (result.isStatus()) {
                            grdDisplay.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return setParam();
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

    private SearchValue_Mdl setParam() {
        SearchValue_Mdl svm = new SearchValue_Mdl();
        svm.setAcLedgerDisplay(acDisplaySettingMdl);
        svm.setDefaultValue(dvm);
        svm.setSearchValue(DmShared.xNull(txtSearchValue.getText()));
        return svm;
    }

    private void reQuery() {
        grdDisplay.getHelper().setRowsStartFrom(0);
        grdDisplay.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdDisplay.reQuery();
    }

    private void showAcGroup(DetailTrialBalance_Mdl tbm) {
        DisplayRoute_Mdl arm = new DisplayRoute_Mdl();
        arm.setCode(tbm.getSharedKeyFieldCode());
        arm.setName(tbm.getSharedKeyFieldName());
        arm.setCodeType(CodeType.SubGroup);
        arm.setPageName(PageName.Ledger);
        displayRouteList.add(arm);
        setNextWidget();
    }

    private void setNextWidget() {
        HashMap paramHashMap = new HashMap();
        String menuText = "";
        try {
            paramHashMap.put("displayRouteList", displayRouteList);
            paramHashMap.put("acDisplaySetting", acDisplaySettingMdl);
            if (displayRouteList.get((displayRouteList.size() - 1)).getPageName().getPageValue() == PageName.Ledger.getPageValue()) {
                menuText = new lmAccounts().LEDGER_DISPLAY.caption;
            }
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, menuText, paramHashMap, null, new SetWidgetInvoker() {

                @Override
                public void setWidget(IsWidget wgtObject) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(DetailTrialBalance.this);
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

    private void onSelection() {
        DetailTrialBalance_Mdl agdm = grdDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        int index = -1;

        if (agdm != null) {
            index = grdDisplay.getDmGrid().getGrid().getStore().indexOf(agdm);
            if (index >= 0) {
                showAcGroup(agdm);
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

    private void close() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(DetailTrialBalance.this);
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
            DetailTrialBalanceReport_Mdl balanceReport_Mdl = new DetailTrialBalanceReport_Mdl();

            balanceReport_Mdl.setTotalCredit(DmShared.xNull(lblAmtCr.getText()));
            balanceReport_Mdl.setTotalDebit(DmShared.xNull(lblAmtDr.getText()));
            balanceReport_Mdl.setTotalDifference(DmShared.xNull(lblAmtDiff.getText()));
            balanceReport_Mdl.setTotalOpening(DmShared.xNull(lblAmtOp.getText()));
            balanceReport_Mdl.setOpeningDrCr(DmShared.xNull(lblOpDrCr.getText()));


            RptDetailTrialBalance prnReceipt = new RptDetailTrialBalance(balanceReport_Mdl);
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
