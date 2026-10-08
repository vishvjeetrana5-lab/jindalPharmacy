package aarogya.inventory.stockSummary.client;

import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.reports.client.RptStockSummaryDisplay;
import aarogya.inventory.stockBatch.client.StockBatch;
import aarogya.inventory.stockLedger.client.StockLedger;
import aarogya.inventory.stockSummary.shared.DefaultValue_Mdl;
import aarogya.inventory.stockSummary.shared.GrdStockSummary_Mdl;
import aarogya.inventory.stockSummary.shared.SummarySetting_Mdl;
import aarogya.inventory.stockSummary.shared.StockSummary_Mdl;
import aarogya.inventory.stockSummary.shared.StockSummary_Srv;
import aarogya.inventory.stockSummary.shared.StockSummary_SrvAsync;
import aarogya.selfToolkit.fifoPopUp.client.FifoPopUp;
import aarogya.selfToolkit.fifoPopUp.shared.FifoSetting_Mdl;
import dataman.dmBase.self.shared.*;
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
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.HashMap;
import java.util.LinkedHashMap;

public final class StockSummary extends StockSummary_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    private final ServiceInvoker<StockSummary_SrvAsync> srvAsync;
    private final Setting setting = new Setting();
    private SummarySetting_Mdl setting_Mdl = new SummarySetting_Mdl();
    private String colName = "";
    private int colIndex;
    private DmDataType dataType = DmDataType.STRING;
    private DefaultValue_Mdl dvm;
    private SetWidgetInvoker swi;
    private String departmentCode = "", departmentName = "";
    private FifoPopUp fifoPopUp = new FifoPopUp(null, null, "SM");

    private void textEvents() {
        setting.addCallBack(new Setting.CallBack() {
            @Override
            public void onOk(SummarySetting_Mdl settingMdl) {
                if (settingMdl != null) {
                    setting_Mdl = settingMdl;
                    refreshGrid(true);
                    txtSearchValue.setFocus(true);
                }
            }

            @Override
            public void onClose() {
                txtSearchValue.setFocus(true);
            }
        });
        fifoPopUp.addCallBack(new FifoPopUp.CallBack() {
            @Override
            public void onOk(FifoSetting_Mdl mdl) {
                if (mdl != null) {
                }
            }

            @Override
            public void close() {
                refreshGrid(false);
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
                srvAsync.invokeRPC(false).getParticulars(param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<StockSummary_Mdl>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<StockSummary_Mdl> result) {
                        if (result.isStatus()) {
                            grdDisplay.setData(result.getData().getListStockDetail(), result.getPaging());
                            displayValue();
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
                showStockLedger(grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
            }
        });

        grdDisplay.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    int index = grdDisplay.getDmGrid().getGrid().getStore().indexOf(grdDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                    showStockLedger(grdDisplay.getDmGrid().getGrid().getStore().get(index));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void displayValue() {
        lblOpeningValue.setText(dvm.getDisplayOpeningValue());
        lblClosingValue.setText(dvm.getDisplayBalanceValue());
    }

    private void reQuery(boolean flagSetHeading) {
        if (flagSetHeading) {
            if (DmShared.iNull(setting_Mdl.getParticularCode()).longValue() != 0) {
                lblHeading.setText("Item Name");
            } else {
                lblHeading.setText(DmShared.xNull(setting_Mdl.getGroupBy()));
            }
        }
        if (DmShared.iNull(setting_Mdl.getParticularCode()).longValue() != 0) {
            grdDisplay.getDmGrid().getGrid().getColumnModel().getColumn(0).setHeader("Item Name");
        } else {
            grdDisplay.getDmGrid().getGrid().getColumnModel().getColumn(0).setHeader(setting_Mdl.getGroupBy());
        }
        grdDisplay.getDmGrid().getGrid().getView().getHeader().refresh();
        grdDisplay.getHelper().setRowsStartFrom(0);
        grdDisplay.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdDisplay.reQuery();
    }

    public StockSummary(UserMenu menuPermission, final SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        srvAsync = new ServiceInvoker<StockSummary_SrvAsync>(GWT.create(StockSummary_Srv.class), StockSummary.class.getName());
        this.asWidget().setTitle(widgetName);
        grdDisplay.getHelper().addFilter(colName, dataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        handlerEvents();
        gridEvents();
        textEvents();
        swi.setWidget(StockSummary.this);
        onLoad();
    }

    private void onLoad() {
        setting_Mdl = new SummarySetting_Mdl();
        setting_Mdl.setFromDate(DmClient.getStartDate());
        setting_Mdl.setToDate(DmClient.getLoginDate());
        setting_Mdl.setGroupBy("Department");
        setting_Mdl.setValueOn("Landed Rate");
        setting_Mdl.setStockStatus("Negative Stock,Positive Stock");

        refreshGrid(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                txtSearchValue.setFocus(true);
            }
        });
    }

    private void handlerEvents() {
        btnBack.addClickHandler(this);
        btnPrint.addClickHandler(this);
        btnFIFO.addClickHandler(this);
        btnClose.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnSetting.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
    }

    private void onSelection() {
        GrdStockSummary_Mdl summary_Mdl = grdDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        int index = -1;

        if (summary_Mdl != null) {
            index = grdDisplay.getDmGrid().getGrid().getStore().indexOf(summary_Mdl);
            if (index >= 0) {
                showStockLedger(summary_Mdl);
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

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnSetting)) {
            setting.show(setting_Mdl);
        } else if (event.getSource().equals(btnClose)) {
            onClose();
        } else if (event.getSource().equals(btnRefresh)) {
            refreshGrid(false);
        } else if (event.getSource().equals(btnBack)) {
            onBack();
        } else if (event.getSource().equals(btnPrint)) {
            if (dvm != null) {
                LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap();
                setting_Mdl.setTitleName(DmShared.xNull(lblHeading.getText()));
                setting_Mdl.setParticularName(grdDisplay.getDmGrid().getGrid().getColumnModel().getColumn(0).getHeader().asString());
                dvm.setSetting_Mdl(setting_Mdl);
                RptStockSummaryDisplay prnReceipt = new RptStockSummaryDisplay(dvm);
                prnReceipt.setDefault(hashMapFilter, false, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
            }
        } else if (event.getSource().equals(btnFIFO)) {
            new DmConfirmMessageBox("Update stock valuation FIFO wise?", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        FifoSetting_Mdl mdlFifo = new FifoSetting_Mdl();
                        mdlFifo.setCategoryCode(DmShared.xNull(setting_Mdl.getItemCatCode()));
                        mdlFifo.setCategoryName(DmShared.xNull(setting_Mdl.getItemCatName()));
                        mdlFifo.setDepartmentCode(DmShared.xNull(setting_Mdl.getDepartmentCode()));
                        mdlFifo.setDepartmentName(DmShared.xNull(setting_Mdl.getDepartmentName()));
                        mdlFifo.setGroupCode(DmShared.xNull(setting_Mdl.getItemGroupCode()));
                        mdlFifo.setGroupName(DmShared.xNull(setting_Mdl.getItemGroupName()));
                        mdlFifo.setItemCode(DmShared.xNull(setting_Mdl.getItemCode()));
                        mdlFifo.setItemName(DmShared.xNull(setting_Mdl.getItemName()));
                        mdlFifo.setSiteCode(DmShared.xNull(setting_Mdl.getSiteCode()));
                        mdlFifo.setSiteName(DmShared.xNull(setting_Mdl.getSiteName()));
                        mdlFifo.setModuleType(DmShared.xNull("SM"));
                        fifoPopUp.show(mdlFifo);

//                        setting_Mdl.setIsFifoUpdatationRequired(true);
//                        refreshGrid(false);
                    }
                }
            }).show();
        }
    }

    private void onBack() {
        setting_Mdl.setParticularCode("");
        setting_Mdl.setParticularName("");
        lblHeading.setText("");
        refreshGrid(true);

    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(StockSummary.this);
                }
            }
        }).show();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            boolean backFlag = false;
            if (grdDisplay.getDmGrid().getGrid().getStore().size() > 0) {
                GrdStockSummary_Mdl grdStockSummary_Mdl = grdDisplay.getDmGrid().getGrid().getStore().get(0);
                if (DmShared.iNull(grdStockSummary_Mdl.getIsItem()).intValue() == 1) {
                    backFlag = true;
                }
            }
            if (backFlag) {
                onBack();
            } else {
                onClose();
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            fifoPopUp.hide();
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            reQuery(true);
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

    private void showStockLedger(final GrdStockSummary_Mdl detail_Mdl) {
        if (DmShared.iNull(detail_Mdl.getIsItem()).intValue() == 1) {
            if (DmShared.xNull(detail_Mdl.getPropertyList()).equalsIgnoreCase("")) {
                showLedgerDisplay(detail_Mdl);
            } else {
                new DmConfirmMessageBox("Do you want to view Stock Batch Display?", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                            showBatchDisplay(detail_Mdl);
                        } else {
                            showLedgerDisplay(detail_Mdl);
                        }
                    }
                }).show();
            }
        } else {
            departmentCode = "";
            departmentName = "";
            if ((DmShared.xNull(setting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Department")) || (DmShared.xNull(setting_Mdl.getGroupBy()).trim().equalsIgnoreCase(""))) {
                setting_Mdl.setParticularName("Department");
                setting_Mdl.setParticularCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                departmentCode = DmShared.xNull(detail_Mdl.getSharedKeyFieldCode());
                departmentName = DmShared.xNull(detail_Mdl.getSharedKeyFieldName());
            } else if (DmShared.xNull(setting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Item Category")) {
                setting_Mdl.setParticularName("Item Category");
                setting_Mdl.setParticularCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
            } else if (DmShared.xNull(setting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Item Group")) {
                setting_Mdl.setParticularName("Item Group");
                setting_Mdl.setParticularCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
            } else if (DmShared.xNull(setting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Item Make")) {
                setting_Mdl.setParticularName("Item Make");
                setting_Mdl.setParticularCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
            } else if (DmShared.xNull(setting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Item Type")) {
                setting_Mdl.setParticularName("Item Type");
                setting_Mdl.setParticularCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
            } else {
                setting_Mdl.setParticularCode("");
                setting_Mdl.setParticularName("");
            }

            if (DmShared.iNull(setting_Mdl.getParticularCode()).longValue() != 0) {
                lblHeading.setText(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + " - Item Name");
            } else {
                lblHeading.setText(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + " - " + DmShared.xNull(setting_Mdl.getGroupBy()));
            }

            refreshGrid(false);
        }
    }

    private void showBatchDisplay(final GrdStockSummary_Mdl detail_Mdl) {
        HashMap paramHashMap;
        paramHashMap = new HashMap();
        paramHashMap.put("widgetName", new pmInventory().STOCK_SUMMARY_DISPLAY.caption);
        if (!detail_Mdl.getPropertyList().equalsIgnoreCase("")) {
            swi.setWidget(new pmInventory().moduleName, new pmInventory().STOCK_BATCH_DISPLAY.caption, paramHashMap, (UserMenu) null, new SetWidgetInvokerImpl() {
                @Override
                public void setWidget(IsWidget wgtObject) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                    ((StockBatch) wgtObject).searchRecord(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()), DmShared.xNull(detail_Mdl.getSharedKeyFieldName()),
                            DmShared.xNull(detail_Mdl.getUnitName()), departmentCode, departmentName, setting_Mdl);
                }
            });
        }
    }

    private void showLedgerDisplay(final GrdStockSummary_Mdl detail_Mdl) {
        HashMap paramHashMap;
        paramHashMap = new HashMap();
        paramHashMap.put("widgetName", new pmInventory().STOCK_SUMMARY_DISPLAY.caption);
        swi.setWidget(new pmInventory().moduleName, new pmInventory().STOCK_LEDGER_DISPLAY.caption, paramHashMap, (UserMenu) null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                ((StockLedger) wgtObject).searchByItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()), DmShared.xNull(detail_Mdl.getSharedKeyFieldName()),
                        DmShared.xNull(detail_Mdl.getUnitName()), departmentCode, departmentName, setting_Mdl);
            }
        });
    }

    private void refreshGrid(final boolean flagSetHeading) {
        srvAsync.invokeRPC(true).getDefault(setting_Mdl, new AsyncCallback<DmResult<DefaultValue_Mdl>>() {
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
                    reQuery(flagSetHeading);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }
}
