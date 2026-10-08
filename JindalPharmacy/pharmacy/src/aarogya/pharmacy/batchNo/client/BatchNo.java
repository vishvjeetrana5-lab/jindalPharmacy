package aarogya.pharmacy.batchNo.client;

import aarogya.pharmacy.batchNo.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.List;

public class BatchNo extends BatchNo_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    private String itemCode = "", departmentCode = "", searchPrvValue = "", selectedBatchSQL = "", barCode = "", v_Date = "", searchDbFieldName = "", profitPercentage = "0";  //, vCat = "", formDocid = ""
    private CallBack callBack;
    private final ServiceInvoker<BatchNo_SrvAsync> SrvAsync;
    private int searchColumnIndex;
    private boolean isReceive = false, isAllowDiscountToParty = false; //isExpiryBatch = false, isDefaultExpiryShow = false, 
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private BatchNo_Mdl batchNoMdl;
    List<BatchNo_Mdl> lstBatchNoDetails;
    ItemBatchType batchType, initialBatchType;
    MedicineSaleOn sRateType;

    public BatchNo() {
        super();
        SrvAsync = new ServiceInvoker<BatchNo_SrvAsync>(GWT.create(BatchNo_Srv.class), BatchNo.class.getName());
        gridBatchNoDetail.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        manageEvents();
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (gridBatchNoDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) gridBatchNoDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (gridBatchNoDetail.getDmGrid().getGrid().getStore().size() > 0) {
                    if (gridBatchNoDetail.getDmGrid().getGrid().getSelectionModel().getSelectedItem() == null) {
                        ((CellSelectionModel) gridBatchNoDetail.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                        onSelection();
                    }
                }
            }
        }

    }

    public interface CallBack {

        public void ok(BatchNo_Mdl batchDetail, List<BatchNo_Mdl> lstBatchDetails);

        public void close(boolean isErrorClose);

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

//    public void show(String itemCode, String departmentCode, String v_Date, String selectedBatchSQL, boolean isExpiryBatch, boolean isReceive, String barCode) {
    public void show(String itemCode, String departmentCode, String v_Date, String selectedBatchSQL, ItemBatchType batchType, boolean isReceive, String barCode, MedicineSaleOn sRateType, String profitPercentage, boolean isAllowDiscountToParty) {
        this.itemCode = itemCode;
        this.departmentCode = departmentCode;
        this.v_Date = v_Date;
        this.selectedBatchSQL = selectedBatchSQL;
        this.isReceive = isReceive;
        this.batchType = batchType;
        this.initialBatchType = batchType;
        this.barCode = barCode;
        this.sRateType = sRateType;
        this.profitPercentage = profitPercentage;
        this.isAllowDiscountToParty = isAllowDiscountToParty;
        batchNoMdl = new BatchNo_Mdl();
        gridBatchNoDetail.getDmGrid().getGrid().getStore().clear();
        getDefaultValue();
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtSearchValue.setText("");
        cbShowExpiry.setValue(false);
        lblShowExpiry.getElement().getStyle().setFontWeight(Style.FontWeight.NORMAL);

        flwExpiryParentLbl.clear();
        flwExpiryParentCb.clear();
        if (batchType.getId() != ItemBatchType.EXPIRED_BATCH.getId()) {
            flwExpiryParentLbl.add(flwExpiryLbl);
            flwExpiryParentCb.add(flwExpiryCb);

        }

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtSearchValue.setFocus(true);
            }
        });
    }

    private void getDefaultValue() {
        SrvAsync.invokeRPC().getDefaultValue(itemCode, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    lblItemName.setText(result.getData());
                    reQuery();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void reQuery() {
        gridBatchNoDetail.getHelper().setRowsStartFrom(0);
        gridBatchNoDetail.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        gridBatchNoDetail.reQuery();
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_N) {
            if (chkAllowNegStck.getValue()) {
                chkAllowNegStck.setValue(false);
            } else if (!chkAllowNegStck.getValue()) {
                chkAllowNegStck.setValue(true);
            }
            showNegativeStock();
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_E) {
            if (cbShowExpiry.isAttached()) {
                if (cbShowExpiry.getValue()) {
                    cbShowExpiry.setValue(false);
                    batchType = initialBatchType;
                } else if (!cbShowExpiry.getValue()) {
                    cbShowExpiry.setValue(true);
                    batchType = ItemBatchType.ALL_BATCH;
//                    isDefaultExpiryShow = false;
                }
                showExpiryStock();
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            txtSearchValue.setFocus(true);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearchValue.getText();
    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean withCloseEvent) {
        this.popupPanel.hide();
        if (withCloseEvent) {
            callBack.close(false);
        }
    }

    private void manageEvents() {
        gridEvents();
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
        chkAllowNegStck.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showNegativeStock();
            }
        });
        cbShowExpiry.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showExpiryStock();
            }
        });
    }

    private void showNegativeStock() {
        if (chkAllowNegStck.getValue()) {
            lblAllwNegStk.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        } else {
            lblAllwNegStk.getElement().getStyle().setFontWeight(Style.FontWeight.NORMAL);
        }
        reQuery();
        txtSearchValue.setFocus(true);
    }

    private void showExpiryStock() {
//        isExpiryBatch = false;
        if (cbShowExpiry.getValue()) {
            lblShowExpiry.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//            isExpiryBatch = true;
//            isDefaultExpiryShow = false;
            batchType = ItemBatchType.ALL_BATCH;
        } else {
            lblShowExpiry.getElement().getStyle().setFontWeight(Style.FontWeight.NORMAL);
            batchType = initialBatchType;
        }
        reQuery();
        txtSearchValue.setFocus(true);
    }

    private void gridEvents() {
        setHeaderValue(1);

        gridBatchNoDetail.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        gridBatchNoDetail.addCallBack(new DmGridPaging.CallBack<BatchNoSearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, BatchNoSearchValue_Mdl searchValue_Mdl) {
                SrvAsync.invokeRPC(false).getBatchNoDetail(searchValue_Mdl, sRateType, profitPercentage, isAllowDiscountToParty, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<BatchNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<BatchNo_Mdl>> result) {
                        if (result.isStatus()) {
                            gridBatchNoDetail.setData(result.getData(), result.getPaging());
                            lstBatchNoDetails = new ArrayList<>();
                            for (BatchNo_Mdl mdlDetails : result.getData()) {
                                if (DmShared.vNull(mdlDetails.getBalanceQty()).doubleValue() > 0) {
                                    lstBatchNoDetails.add(mdlDetails);
                                }
                            }
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public BatchNoSearchValue_Mdl getParam() {
                return valueIntialize();
            }
        });

        gridBatchNoDetail.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    batchNoMdl = gridBatchNoDetail.getDmGrid().getGrid().getStore().get(event.getRowIndex());
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                }
            }
        });

        gridBatchNoDetail.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                onSelection();
            }
        });

        gridBatchNoDetail.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    onSelection();
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_UP) {
                    if (gridBatchNoDetail.getDmGrid().getGrid().getStore().size() > 0) {
                        CellSelectionModel<BatchNo_Mdl> cellModel;
                        cellModel = (CellSelectionModel<BatchNo_Mdl>) gridBatchNoDetail.getDmGrid().getGrid().getSelectionModel();
                        if (cellModel.getSelectCell().getRow() == 0) {
                            txtSearchValue.setFocus(true);
                        }
                    }
                }
            }
        }, KeyDownEvent.getType());

    }

    private void setHeaderValue(int columnIndex) {
        if ((gridBatchNoDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = gridBatchNoDetail.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) gridBatchNoDetail.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        gridBatchNoDetail.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        gridBatchNoDetail.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : gridBatchNoDetail.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        gridBatchNoDetail.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private BatchNoSearchValue_Mdl valueIntialize() {
        BatchNoSearchValue_Mdl value_Mdl = new BatchNoSearchValue_Mdl();
        value_Mdl.setSelectedBatchSQL(selectedBatchSQL);
        value_Mdl.setDepartmentCode(DmShared.xNull(departmentCode));
        value_Mdl.setV_Date(DmShared.xNull(v_Date));
        value_Mdl.setItemCode(DmShared.xNull(itemCode));
        value_Mdl.setIsAllwNegStk(chkAllowNegStck.getValue());
        value_Mdl.setIsReceive(isReceive);
        value_Mdl.setBatchType(batchType);
//        value_Mdl.setIsExpiryBatch(isExpiryBatch);
//        value_Mdl.setDefaultExpiryShow(isDefaultExpiryShow);
        value_Mdl.setBarCode(DmShared.xNull(barCode));
        return value_Mdl;
    }

    private void onSelection() {
        BatchNo_Mdl grdBatchDetails = gridBatchNoDetail.getDmGrid().getGrid().getSelectionModel().getSelectedItem();
        int index = -1;

        if (grdBatchDetails != null) {
            index = gridBatchNoDetail.getDmGrid().getGrid().getStore().indexOf(grdBatchDetails);
            if (index >= 0) {
                batchNoMdl = gridBatchNoDetail.getDmGrid().getGrid().getStore().get(index);
            }
        }
//        if (isDefaultExpiryShow || (!gridBatchNoDetail.getDmGrid().getGrid().getStore().get(index).isExpiredIbCode())) {
        if (initialBatchType.getId() == ItemBatchType.EXPIRED_BATCH.getId()
                || initialBatchType.getId() == ItemBatchType.ALL_BATCH.getId()
                || (!gridBatchNoDetail.getDmGrid().getGrid().getStore().get(index).isExpiredIbCode())) {
            hide(false);
            callBack.ok(batchNoMdl, lstBatchNoDetails);
        }
    }
}
