package aarogya.pharmacy.stockSummary.client;

import aarogya.pharmacy.reports.client.RptStockSummaryDisplay;
import aarogya.pharmacy.stockBatch.client.StockBatch;
import aarogya.pharmacy.stockLedger.client.StockLedger;
import aarogya.pharmacy.stockSummary.shared.DefaultValue_Mdl;
import aarogya.pharmacy.stockSummary.shared.StockSummary_Mdl;
import aarogya.pharmacy.stockSummary.shared.GrdStockSummary_Mdl;
import aarogya.pharmacy.stockSummary.shared.Navigation_Mdl;
import aarogya.pharmacy.stockSummary.shared.SummarySetting_Mdl;
import aarogya.pharmacy.stockSummary.shared.StockSummary_Srv;
import aarogya.pharmacy.stockSummary.shared.StockSummary_SrvAsync;
import aarogya.projectMenu.self.shared.pmPharmacy;
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
    private final SummarySetting setting = new SummarySetting();
    private SummarySetting_Mdl summarySetting_Mdl;
    private DefaultValue_Mdl dvm;
    private String colName = "";
    private int colIndex;
    private DmDataType dataType = DmDataType.STRING;
    private SetWidgetInvoker swi;
    private String departmentCode = "", departmentName = "";
    private boolean flagOpenAsPopup = false;
//new aarogya.selfToolkit.fifoPopUp.client.FifoPopUp(null, null, "PH").show(mdlFifo);
    private FifoPopUp fifoPopUp = new FifoPopUp(null, null, "PH");
    private Navigation_Mdl[] navigationArray = new Navigation_Mdl[3];
    private String baseNavigationFieldValue = "";

    private void textEvents() {
        setting.addCallBack(new SummarySetting.CallBack() {
            @Override
            public void onOk(SummarySetting_Mdl settingMdl) {
                if (settingMdl != null) {
                    summarySetting_Mdl = settingMdl;
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
                showDetail(grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()));
            }
        });

        grdDisplay.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    int index = grdDisplay.getDmGrid().getGrid().getStore().indexOf(grdDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem());
                    showDetail(grdDisplay.getDmGrid().getGrid().getStore().get(index));
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
            if (DmShared.iNull(summarySetting_Mdl.getParticularCode()).longValue() != 0) {
//                if (DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue() == 1) {
//                    lblHeading.setText("Medicine Category");
//                } else {
//                    lblHeading.setText("Medicine Name");
//                }

            } else {
//                lblHeading.setText(DmShared.xNull(summarySetting_Mdl.getGroupBy()));
                navigationArray[0].setHeading(DmShared.xNull(summarySetting_Mdl.getGroupBy()));
            }
            lblHeading.setText(DmShared.xNull(navigationArray[DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue()].getHeading()));
        }
        if (DmShared.iNull(summarySetting_Mdl.getParticularCode()).longValue() != 0) {
            if (DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue() == 1) {
//                grdDisplay.getDmGrid().getGrid().getColumnModel().getColumn(0).setHeader("Medicine Category");
                summarySetting_Mdl.setParticularCaption("Medicine Category");
            } else {
//                grdDisplay.getDmGrid().getGrid().getColumnModel().getColumn(0).setHeader("Medicine Name");
                summarySetting_Mdl.setParticularCaption("Medicine Name");
            }
        } else {
//            grdDisplay.getDmGrid().getGrid().getColumnModel().getColumn(0).setHeader(summarySetting_Mdl.getGroupBy());
            summarySetting_Mdl.setParticularCaption(summarySetting_Mdl.getGroupBy());
        }
        grdDisplay.getDmGrid().getGrid().getColumnModel().getColumn(0).setHeader(summarySetting_Mdl.getParticularCaption());
        grdDisplay.getDmGrid().getGrid().getView().refresh(true);
        grdDisplay.getHelper().setRowsStartFrom(0);
        grdDisplay.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdDisplay.reQuery();
    }

    public StockSummary(UserMenu menuPermission, HashMap paramHashMap, final SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        flagOpenAsPopup = (Boolean) paramHashMap.get("flagOpenAsPopup");
        srvAsync = new ServiceInvoker<StockSummary_SrvAsync>(GWT.create(StockSummary_Srv.class), StockSummary.class.getName());
        this.asWidget().setTitle(widgetName);
        grdDisplay.getHelper().addFilter(colName, dataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        handlerEvents();
        gridEvents();
        textEvents();
        if (flagOpenAsPopup) {
            ppForm.clear();
            ppForm.add(flwForm);
            ppForm.setGlassEnabled(true);
            ppForm.center();
        } else {
            onLoad();
        }
        swi.setWidget(StockSummary.this);
    }

    private void onLoad() {
        summarySetting_Mdl = new SummarySetting_Mdl();
        summarySetting_Mdl.setFromDate(DmClient.getStartDate());
        summarySetting_Mdl.setToDate(DmClient.getLoginDate());
        summarySetting_Mdl.setGroupBy("Department");
        summarySetting_Mdl.setValueOn("Landed Rate");
        summarySetting_Mdl.setStockStatus("Negative Stock,Positive Stock");
        iniNavigationArray();
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
                showDetail(summary_Mdl);
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
        if (flagOpenAsPopup) {
            return ppForm;
        } else {
            return flwForm;
        }
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnSetting)) {
            setting.show(summarySetting_Mdl);
        } else if (event.getSource().equals(btnClose)) {
            onClose();
        } else if (event.getSource().equals(btnRefresh)) {
            refreshGrid(false);
        } else if (event.getSource().equals(btnBack)) {
            onBack();
        } else if (event.getSource().equals(btnPrint)) {
            if (dvm != null) {
                LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap();
                summarySetting_Mdl.setTitleName(DmShared.xNull(lblHeading.getText()));
                dvm.setSetting_Mdl(summarySetting_Mdl);
                RptStockSummaryDisplay prnReceipt = new RptStockSummaryDisplay(dvm, flagOpenAsPopup);
                prnReceipt.setDefault(hashMapFilter, false, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
            }
        } else if (event.getSource().equals(btnFIFO)) {
            new DmConfirmMessageBox("Update stock valuation FIFO wise?", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        FifoSetting_Mdl mdlFifo = new FifoSetting_Mdl();
                        mdlFifo.setCategoryCode(DmShared.xNull(summarySetting_Mdl.getMedicineCatCode()));
                        mdlFifo.setCategoryName(DmShared.xNull(summarySetting_Mdl.getMedicineCatName()));
                        mdlFifo.setDepartmentCode(DmShared.xNull(summarySetting_Mdl.getDepartmentCode()));
                        mdlFifo.setDepartmentName(DmShared.xNull(summarySetting_Mdl.getDepartmentName()));
                        mdlFifo.setGroupCode(DmShared.xNull(summarySetting_Mdl.getMedicineGroupCode()));
                        mdlFifo.setGroupName(DmShared.xNull(summarySetting_Mdl.getMedicineGroupName()));
                        mdlFifo.setItemCode(DmShared.xNull(summarySetting_Mdl.getMedicineCode()));
                        mdlFifo.setItemName(DmShared.xNull(summarySetting_Mdl.getMedicineName()));
                        mdlFifo.setSiteCode(DmShared.xNull(summarySetting_Mdl.getSiteCode()));
                        mdlFifo.setSiteName(DmShared.xNull(summarySetting_Mdl.getSiteName()));
                        mdlFifo.setModuleType(DmShared.xNull("PH"));
                        fifoPopUp.show(mdlFifo);
                    }
                }
            }).show();
        }
    }

    private void onBack() {
        if (DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue() > 0) {
            navigationArray[DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue()] = new Navigation_Mdl();
            summarySetting_Mdl.setStepIndex(String.valueOf(DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue() - 1));
            summarySetting_Mdl.setParticularCode(navigationArray[DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue()].getParticularCode());
            summarySetting_Mdl.setParticularName(navigationArray[DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue()].getParticularName());
        } else {
            summarySetting_Mdl.setParticularCode("");
            summarySetting_Mdl.setParticularName("");
        }
        lblHeading.setText("");
        refreshGrid(true);
    }

    private void onClose() {
        if (flagOpenAsPopup) {
            ppForm.hide();
        } else {
            new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(StockSummary.this);
                    }
                }
            }).show();
        }

    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            boolean backFlag = false;
            if (grdDisplay.getDmGrid().getGrid().getStore().size() > 0) {
                if (DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue() > 0) {
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

    private void showDetail(final GrdStockSummary_Mdl detail_Mdl) {
        if (DmShared.iNull(detail_Mdl.getIsMedicine()).intValue() == 1) {
            new DmConfirmMessageBox("Are you sure? You want to show Stock Batch Display.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        showBatchDisplay(detail_Mdl);
                    } else {
                        showLedgerDisplay(detail_Mdl);
                    }
                }
            }).show();

        } else {
            departmentCode = "";
            departmentName = "";
            summarySetting_Mdl.setStepIndex(String.valueOf(DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue() + 1));
            if ((DmShared.xNull(summarySetting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Department")) || (DmShared.xNull(summarySetting_Mdl.getGroupBy()).trim().equalsIgnoreCase(""))) {
                if (DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue() == 2) {
                    summarySetting_Mdl.setParticularName("Medicine Category");
                    summarySetting_Mdl.setParticularCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));

                } else {
                    summarySetting_Mdl.setParticularName("Department");
                    summarySetting_Mdl.setParticularCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    departmentCode = DmShared.xNull(detail_Mdl.getSharedKeyFieldCode());
                    departmentName = DmShared.xNull(detail_Mdl.getSharedKeyFieldName());
                }
            } else if (DmShared.xNull(summarySetting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Medicine Category")) {
                summarySetting_Mdl.setParticularName("Medicine Category");
                summarySetting_Mdl.setParticularCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                summarySetting_Mdl.setStepIndex("2");
            } else if (DmShared.xNull(summarySetting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Medicine Group")) {
                summarySetting_Mdl.setParticularName("Medicine Group");
                summarySetting_Mdl.setParticularCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
            } else if (DmShared.xNull(summarySetting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Medicine Company")) {
                summarySetting_Mdl.setParticularName("Medicine Company");
                summarySetting_Mdl.setParticularCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
            } else {
                summarySetting_Mdl.setParticularCode("");
                summarySetting_Mdl.setParticularName("");
                summarySetting_Mdl.setStepIndex("");
            }

            navigationArray[DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue()].setParticularCode(DmShared.xNull(summarySetting_Mdl.getParticularCode()));
            navigationArray[DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue()].setParticularName(DmShared.xNull(summarySetting_Mdl.getParticularName()));

            if (DmShared.iNull(summarySetting_Mdl.getParticularCode()).longValue() != 0) {
                if (DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue() == 1) {
//                    lblHeading.setText(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + " (" + DmShared.xNull(detail_Mdl.getUnitName()) + ") - Medicine Category");
                    navigationArray[DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue()].setHeading(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + " - Medicine Category");
                    baseNavigationFieldValue = DmShared.xNull(detail_Mdl.getSharedKeyFieldName());
                } else {
//                    lblHeading.setText(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + " (" + DmShared.xNull(detail_Mdl.getUnitName()) + ") - Medicine Name");
                    navigationArray[DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue()].setHeading(DmShared.xNull(baseNavigationFieldValue) + " - " + DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + " - Medicine Name");
                }
            } else {
//                lblHeading.setText(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + " (" + DmShared.xNull(detail_Mdl.getUnitName()) + ") - " + DmShared.xNull(summarySetting_Mdl.getGroupBy()));
                navigationArray[DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue()].setHeading(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()));
            }
            lblHeading.setText(DmShared.xNull(navigationArray[DmShared.iNull(summarySetting_Mdl.getStepIndex()).intValue()].getHeading()));

            refreshGrid(false);
        }
    }

    private void showBatchDisplay(final GrdStockSummary_Mdl detail_Mdl) {
        HashMap paramHashMap;
        paramHashMap = new HashMap();
        paramHashMap.put("flagOpenAsPopup", flagOpenAsPopup);
        paramHashMap.put("widgetName", new pmPharmacy().STOCK_SUMMARY_DISPLAY.caption);
        swi.setWidget(new pmPharmacy().moduleName, new pmPharmacy().STOCK_BATCH_DISPLAY.caption, paramHashMap, (UserMenu) null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                if (!flagOpenAsPopup) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }
                ((StockBatch) wgtObject).searchRecord(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()), DmShared.xNull(detail_Mdl.getSharedKeyFieldName()),
                        DmShared.xNull(detail_Mdl.getUnitName()), departmentCode, departmentName, summarySetting_Mdl);
            }
        });
    }

    private void showLedgerDisplay(final GrdStockSummary_Mdl detail_Mdl) {
        HashMap paramHashMap;
        paramHashMap = new HashMap();
        paramHashMap.put("flagOpenAsPopup", flagOpenAsPopup);
        paramHashMap.put("widgetName", new pmPharmacy().STOCK_SUMMARY_DISPLAY.caption);
        swi.setWidget(new pmPharmacy().moduleName, new pmPharmacy().STOCK_LEDGER_DISPLAY.caption, paramHashMap, (UserMenu) null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                if (!flagOpenAsPopup) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }
                ((StockLedger) wgtObject).searchByMedicineCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()), DmShared.xNull(detail_Mdl.getSharedKeyFieldName()),
                        DmShared.xNull(detail_Mdl.getUnitName()), departmentCode, departmentName, summarySetting_Mdl);
            }
        });
    }

    private void refreshGrid(final boolean flagSetHeading) {
        srvAsync.invokeRPC(true).getDefault(summarySetting_Mdl, new AsyncCallback<DmResult<DefaultValue_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    dvm = result.getData();
                    if (DmShared.xNull(summarySetting_Mdl.getSiteCode()).trim().equalsIgnoreCase("")) {
                        summarySetting_Mdl.setSiteCode(dvm.getSetting_Mdl().getSiteCode());
                        summarySetting_Mdl.setSiteName(dvm.getSetting_Mdl().getSiteName());
                    }
                    reQuery(flagSetHeading);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void action(String nature, Object object) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "PRE_FILTERED_SUMMARY":
                    openPreFilteredSummary((SummarySetting_Mdl) object);
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void openPreFilteredSummary(SummarySetting_Mdl preSettingMdl) {
        summarySetting_Mdl = new SummarySetting_Mdl();
        summarySetting_Mdl.setFromDate(preSettingMdl.getFromDate());
        summarySetting_Mdl.setToDate(preSettingMdl.getToDate());
        summarySetting_Mdl.setGroupBy(preSettingMdl.getGroupBy());
        summarySetting_Mdl.setValueOn(preSettingMdl.getValueOn());
        summarySetting_Mdl.setStockStatus(preSettingMdl.getStockStatus());
        summarySetting_Mdl.setMedicineMakeCode(preSettingMdl.getMedicineMakeCode());
        summarySetting_Mdl.setMedicineMakeName(preSettingMdl.getMedicineMakeName());
        summarySetting_Mdl.setMedicineCatCode(preSettingMdl.getMedicineCatCode());
        summarySetting_Mdl.setMedicineCatName(preSettingMdl.getMedicineCatName());
        summarySetting_Mdl.setMedicineGroupCode(preSettingMdl.getMedicineGroupCode());
        summarySetting_Mdl.setMedicineGroupName(preSettingMdl.getMedicineGroupName());
        summarySetting_Mdl.setMedicineCode(preSettingMdl.getMedicineCode());
        summarySetting_Mdl.setMedicineName(preSettingMdl.getMedicineName());
        summarySetting_Mdl.setDepartmentCode(preSettingMdl.getDepartmentCode());
        summarySetting_Mdl.setDepartmentName(preSettingMdl.getDepartmentName());
        summarySetting_Mdl.setSiteCode(preSettingMdl.getSiteCode());
        summarySetting_Mdl.setSiteName(preSettingMdl.getSiteName());
        summarySetting_Mdl.setNarcoticsCode(preSettingMdl.getNarcoticsCode());
        summarySetting_Mdl.setNarcoticsName(preSettingMdl.getNarcoticsName());
        summarySetting_Mdl.setConsignmentItemCode(preSettingMdl.getConsignmentItemCode());
        summarySetting_Mdl.setConsignmentItemName(preSettingMdl.getConsignmentItemName());
        summarySetting_Mdl.setHighValueCode(preSettingMdl.getHighValueCode());
        summarySetting_Mdl.setHighValueName(preSettingMdl.getHighValueName());
        summarySetting_Mdl.setBilledItemCode(preSettingMdl.getBilledItemCode());
        summarySetting_Mdl.setBilledItemName(preSettingMdl.getBilledItemName());
        summarySetting_Mdl.setFlowThroughCode(preSettingMdl.getFlowThroughCode());
        summarySetting_Mdl.setFlowThroughName(preSettingMdl.getFlowThroughName());
        summarySetting_Mdl.setLifeSavingCode(preSettingMdl.getLifeSavingCode());
        summarySetting_Mdl.setLifeSavingName(preSettingMdl.getLifeSavingName());
        iniNavigationArray();
        refreshGrid(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                txtSearchValue.setFocus(true);
            }
        });
    }

    private void iniNavigationArray() {
        for (int i = 0; i < navigationArray.length; i++) {
            navigationArray[i] = new Navigation_Mdl();;
        }
    }
}
