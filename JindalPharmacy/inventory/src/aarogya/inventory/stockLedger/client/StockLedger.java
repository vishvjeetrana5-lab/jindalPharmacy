package aarogya.inventory.stockLedger.client;

import aarogya.inventory.reports.client.RptStockLedgerDisplay;
import aarogya.inventory.stockLedger.shared.DefaultValue_Mdl;
import aarogya.inventory.stockLedger.shared.GrdStockDetail_Mdl;
import aarogya.inventory.stockLedger.shared.Setting_Mdl;
import aarogya.inventory.stockLedger.shared.StockLedger_Mdl;
import aarogya.inventory.stockLedger.shared.StockLedger_Srv;
import aarogya.inventory.stockLedger.shared.StockLedger_SrvAsync;
import aarogya.inventory.stockSummary.shared.SummarySetting_Mdl;
import aarogya.projectMenu.self.shared.pmInventory;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.HashMap;
import java.util.LinkedHashMap;

public final class StockLedger extends StockLedger_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    private final ServiceInvoker<StockLedger_SrvAsync> srvAsync;
    private String colName = "", itemCode = "";
    private int colIndex;
    private DmDataType dataType = DmDataType.STRING;
    private DefaultValue_Mdl dvm;
    private String openFromWidget = "", departmentCode = "";

    private Setting_Mdl setting_Mdl = new Setting_Mdl();
    private final Setting setting = new Setting();

    private void textEvents() {
        setting.addCallBack(new Setting.CallBack() {
            @Override
            public void onOk(Setting_Mdl mrTemp) {
                if (mrTemp != null) {
                    setting_Mdl = mrTemp;
                    if (!DmShared.xNull(setting_Mdl.getItemCode()).equalsIgnoreCase("")) {
                        String[] xList;
                        boolean isItemChanged = false;

                        String x = setting_Mdl.getItemCode().replace("'", "");
                        xList = x.split(",");
                        if (xList.length == 1) {
                            if (DmShared.iNull(itemCode).longValue() != DmShared.iNull(xList[0]).longValue()) {
                                isItemChanged = true;
                            }
                        } else if (xList.length >= 1) {
                            isItemChanged = true;
                        }
                        if (isItemChanged) {
                            itemCode = "";
                            lblHeading.setText("");
                        }
                    }
                    refreshGrid();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            txtSearchValue.setFocus(true);
                        }
                    });
                }
            }

            @Override
            public void onClose() {
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
                srvAsync.invokeRPC(false).getParticulars(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<StockLedger_Mdl>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<StockLedger_Mdl> result) {
                        if (result.isStatus()) {
                            grdDisplay.setData(result.getData().getListStockDetail(), result.getPaging());
                            displayQty();
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
                showEntryPoint(grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
            }
        });

        grdDisplay.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    int index = grdDisplay.getDmGrid().getGrid().getStore().indexOf(grdDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                    showEntryPoint(grdDisplay.getDmGrid().getGrid().getStore().get(index));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void reQuery() {
        grdDisplay.getHelper().setRowsStartFrom(0);
        grdDisplay.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdDisplay.reQuery();
    }

    public StockLedger(UserMenu menuPermission, HashMap paramHashMap, final SetWidgetInvoker swi) {
        super();
        openFromWidget = paramHashMap != null ? (String) paramHashMap.get("widgetName") : "";
        if (paramHashMap != null) {
            openFromWidget = (String) paramHashMap.get("widgetName");
            if (!paramHashMap.containsKey("departmentCode")) {
                departmentCode = (String) paramHashMap.get("departmentCode");
            } else {
                departmentCode = "";
            }
        } else {
            openFromWidget = "";
            departmentCode = "";
        }
        srvAsync = new ServiceInvoker<StockLedger_SrvAsync>(GWT.create(StockLedger_Srv.class), StockLedger.class.getName());
        this.asWidget().setTitle(widgetName);
        grdDisplay.getHelper().addFilter(colName, dataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        handlerEvents();
        gridEvents();
        textEvents();
        swi.setWidget(StockLedger.this);
        onLoad();
    }

    private void onLoad() {
        if (DmShared.dNull(setting_Mdl.getFromDate()).trim().equals("")) {
            setting_Mdl.setFromDate(DmClient.getStartDate());
        }
        if (DmShared.dNull(setting_Mdl.getToDate()).trim().equals("")) {
            setting_Mdl.setToDate(DmClient.getEndDate());
        }
        if (DmShared.xNull(setting_Mdl.getValueOn()).trim().equals("")) {
            setting_Mdl.setValueOn("Landed Rate");
        }
        clear();
        if (DmShared.xNull(openFromWidget).trim().equals(new pmInventory().STOCK_LEDGER_DISPLAY.caption)) {
            refreshGrid();
        }
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                txtSearchValue.setFocus(true);
            }
        });
    }

    private void displayQty() {
        lblBalance.setText("Bal. Qty.: " + DmShared.xNull(dvm.getBalanceQty()));
        lblBalanceValue.setText("Bal. Value: " + DmShared.xNull(dvm.getBalanceValue()));
        lblOpening.setText("Opn. Qty.: " + DmShared.xNull(dvm.getOpeningQty()));
        lblIssueQty.setText("Issue: " + DmShared.xNull(dvm.getIssueQty()));
        lblReceiveQty.setText("Receive: " + DmShared.xNull(dvm.getReceiveQty()));
    }

    private void clear() {
        lblBalance.setText("Bal. Qty.: ");
        lblBalanceValue.setText("Bal. Value: ");
        lblOpening.setText("Opn. Qty.: ");
        lblIssueQty.setText("Issue: ");
        lblReceiveQty.setText("Receive: ");
    }

    private void handlerEvents() {
        btnSetting.addClickHandler(this);
        btnPrint.addClickHandler(this);
        btnBack.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
    }

    private void onSelection() {
        GrdStockDetail_Mdl acGroupDetail = grdDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        int index = -1;

        if (acGroupDetail != null) {
            index = grdDisplay.getDmGrid().getGrid().getStore().indexOf(acGroupDetail);
            if (index >= 0) {
                showEntryPoint(acGroupDetail);
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

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
    }

    public void searchByItemCode(String itemCode, String itemName, String unitName, String departmentCode, String departmentName, SummarySetting_Mdl summarySetting_Mdl) {
        this.itemCode = itemCode;

        setting_Mdl.setFromDate(DmShared.dNull(summarySetting_Mdl.getFromDate()));
        setting_Mdl.setToDate(DmShared.dNull(summarySetting_Mdl.getToDate()));
        setting_Mdl.setItemGroupCode(DmShared.xNull(summarySetting_Mdl.getItemGroupCode()));
        setting_Mdl.setItemGroupName(DmShared.xNull(summarySetting_Mdl.getItemGroupName()));
        setting_Mdl.setItemTypeCode(DmShared.xNull(summarySetting_Mdl.getItemTypeCode()));
        setting_Mdl.setItemTypeName(DmShared.xNull(summarySetting_Mdl.getItemTypeName()));
        setting_Mdl.setItemMakeCode(DmShared.xNull(summarySetting_Mdl.getItemMakeCode()));
        setting_Mdl.setItemMakeName(DmShared.xNull(summarySetting_Mdl.getItemMakeName()));
        setting_Mdl.setItemCatCode(DmShared.xNull(summarySetting_Mdl.getItemCatCode()));
        setting_Mdl.setItemCatName(DmShared.xNull(summarySetting_Mdl.getItemCatName()));
        setting_Mdl.setSiteCode(DmShared.xNull(summarySetting_Mdl.getSiteCode()));
        setting_Mdl.setSiteName(DmShared.xNull(summarySetting_Mdl.getSiteName()));

        setting_Mdl.setNarcoticsCode(DmShared.xNull(summarySetting_Mdl.getNarcoticsCode()));
        setting_Mdl.setNarcoticsName(DmShared.xNull(summarySetting_Mdl.getNarcoticsName()));
        setting_Mdl.setConsignmentItemCode(DmShared.xNull(summarySetting_Mdl.getConsignmentItemCode()));
        setting_Mdl.setConsignmentItemName(DmShared.xNull(summarySetting_Mdl.getConsignmentItemName()));
        setting_Mdl.setHighValueCode(DmShared.xNull(summarySetting_Mdl.getHighValueCode()));
        setting_Mdl.setHighValueName(DmShared.xNull(summarySetting_Mdl.getHighValueName()));
        setting_Mdl.setBilledItemCode(DmShared.xNull(summarySetting_Mdl.getBilledItemCode()));
        setting_Mdl.setBilledItemName(DmShared.xNull(summarySetting_Mdl.getBilledItemName()));
        setting_Mdl.setFlowThroughCode(DmShared.xNull(summarySetting_Mdl.getFlowThroughCode()));
        setting_Mdl.setFlowThroughName(DmShared.xNull(summarySetting_Mdl.getFlowThroughName()));
        setting_Mdl.setLifeSavingCode(DmShared.xNull(summarySetting_Mdl.getLifeSavingCode()));
        setting_Mdl.setLifeSavingName(DmShared.xNull(summarySetting_Mdl.getLifeSavingName()));

        if (!DmShared.xNull(itemCode).trim().equalsIgnoreCase("")) {
            lblHeading.setText(DmShared.xNull(itemName) + " (" + DmShared.xNull(unitName) + ")");

            setting_Mdl.setItemCode(DmShared.xNull(itemCode));
            setting_Mdl.setItemName(DmShared.xNull(itemName));
        } else {
            setting_Mdl.setItemCode(DmShared.xNull(summarySetting_Mdl.getItemCode()));
            setting_Mdl.setItemName(DmShared.xNull(summarySetting_Mdl.getItemName()));
        }
        if (!DmShared.xNull(departmentCode).trim().equalsIgnoreCase("")) {
            setting_Mdl.setDepartmentCode(DmShared.xNull(departmentCode));
            setting_Mdl.setDepartmentName(DmShared.xNull(departmentName));
        } else {
            setting_Mdl.setDepartmentCode(DmShared.xNull(summarySetting_Mdl.getDepartmentCode()));
            setting_Mdl.setDepartmentName(DmShared.xNull(summarySetting_Mdl.getDepartmentName()));
        }
        setting_Mdl.setValueOn(DmShared.xNull(summarySetting_Mdl.getValueOn()));

        refreshGrid();
    }

    public void searchByBatchCode(String itemCode, String itemName, String unitName, aarogya.inventory.stockBatch.shared.Setting_Mdl mrTemp) {
        this.itemCode = itemCode;

        setting_Mdl.setFromDate(DmShared.dNull(mrTemp.getFromDate()));
        setting_Mdl.setToDate(DmShared.dNull(mrTemp.getToDate()));
        setting_Mdl.setItemGroupCode(DmShared.xNull(mrTemp.getItemGroupCode()));
        setting_Mdl.setItemGroupName(DmShared.xNull(mrTemp.getItemGroupName()));
        setting_Mdl.setItemTypeCode(DmShared.xNull(mrTemp.getItemTypeCode()));
        setting_Mdl.setItemTypeName(DmShared.xNull(mrTemp.getItemTypeName()));
        setting_Mdl.setItemMakeCode(DmShared.xNull(mrTemp.getItemMakeCode()));
        setting_Mdl.setItemMakeName(DmShared.xNull(mrTemp.getItemMakeName()));
        setting_Mdl.setItemCatCode(DmShared.xNull(mrTemp.getItemCatCode()));
        setting_Mdl.setItemCatName(DmShared.xNull(mrTemp.getItemCatName()));
        setting_Mdl.setDepartmentCode(DmShared.xNull(mrTemp.getDepartmentCode()));
        setting_Mdl.setDepartmentName(DmShared.xNull(mrTemp.getDepartmentName()));
        setting_Mdl.setSiteCode(DmShared.xNull(mrTemp.getSiteCode()));
        setting_Mdl.setSiteName(DmShared.xNull(mrTemp.getSiteName()));

        setting_Mdl.setNarcoticsCode(DmShared.xNull(mrTemp.getNarcoticsCode()));
        setting_Mdl.setNarcoticsName(DmShared.xNull(mrTemp.getNarcoticsName()));
        setting_Mdl.setConsignmentItemCode(DmShared.xNull(mrTemp.getConsignmentItemCode()));
        setting_Mdl.setConsignmentItemName(DmShared.xNull(mrTemp.getConsignmentItemName()));
        setting_Mdl.setHighValueCode(DmShared.xNull(mrTemp.getHighValueCode()));
        setting_Mdl.setHighValueName(DmShared.xNull(mrTemp.getHighValueName()));
        setting_Mdl.setBilledItemCode(DmShared.xNull(mrTemp.getBilledItemCode()));
        setting_Mdl.setBilledItemName(DmShared.xNull(mrTemp.getBilledItemName()));
        setting_Mdl.setFlowThroughCode(DmShared.xNull(mrTemp.getFlowThroughCode()));
        setting_Mdl.setFlowThroughName(DmShared.xNull(mrTemp.getFlowThroughName()));
        setting_Mdl.setLifeSavingCode(DmShared.xNull(mrTemp.getLifeSavingCode()));
        setting_Mdl.setLifeSavingName(DmShared.xNull(mrTemp.getLifeSavingName()));

        if (!DmShared.xNull(itemCode).trim().equalsIgnoreCase("")) {
            lblHeading.setText(DmShared.xNull(itemName) + " (" + DmShared.xNull(unitName) + ")");

            setting_Mdl.setItemCode(DmShared.xNull(itemCode));
            setting_Mdl.setItemName(DmShared.xNull(itemName));
        } else {
            setting_Mdl.setItemCode(DmShared.xNull(mrTemp.getItemCode()));
            setting_Mdl.setItemName(DmShared.xNull(mrTemp.getItemName()));
        }
        setting_Mdl.setValueOn(DmShared.xNull(mrTemp.getValueOn()));

        refreshGrid();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnBack)) {
            onClose();
        } else if (event.getSource().equals(btnRefresh)) {
//            reQuery();
            refreshGrid();
        } else if (event.getSource().equals(btnSetting)) {
            setting.show(setting_Mdl, itemCode);
        } else if (event.getSource().equals(btnPrint)) {
            if (dvm != null) {
                setting_Mdl.setTitleName(DmShared.xNull(lblHeading.getText()));
                dvm.setSetting_Mdl(setting_Mdl);
                LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap();
                RptStockLedgerDisplay prnReceipt = new RptStockLedgerDisplay(dvm);
                prnReceipt.setDefault(hashMapFilter, false, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
            }
        }
    }

    private void onClose() {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(StockLedger.this);
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

    private void showEntryPoint(final GrdStockDetail_Mdl detail_Mdl) {
        if (detail_Mdl != null && !DmShared.xNull(detail_Mdl.getMnuText()).trim().equals("") && !DmShared.xNull(detail_Mdl.getMnuAttachedInModule()).trim().equals("")) {
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(detail_Mdl.getMnuAttachedInModule(), detail_Mdl.getMnuText(), null, null, new SetWidgetInvokerImpl() {
                @Override
                public void setWidget(final IsWidget wgtObject) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    ((IsWidgetExtended) wgtObject).searchRecord(detail_Mdl.getSharedKeyFieldCode());
                }
            });
        }
    }

    private void refreshGrid() {
        srvAsync.invokeRPC(true).getDefault(itemCode, setting_Mdl, new AsyncCallback<DmResult<DefaultValue_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    dvm = result.getData();
                    if (DmShared.xNull(setting_Mdl.getSiteCode()).trim().equalsIgnoreCase("")) {
                        setting_Mdl.setSiteCode(dvm.getSetting_Mdl().getSiteCode());
                        setting_Mdl.setSiteName(dvm.getSetting_Mdl().getSiteName());
                    }
                    reQuery();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

}
