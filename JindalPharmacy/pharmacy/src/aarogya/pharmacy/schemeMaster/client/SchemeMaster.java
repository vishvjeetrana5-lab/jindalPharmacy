package aarogya.pharmacy.schemeMaster.client;

import aarogya.pharmacy.batchNo.client.BatchNo;
import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import aarogya.pharmacy.schemeMaster.shared.GrdSchemeDetails_Mdl;
import aarogya.pharmacy.schemeMaster.shared.SchemeMaster_Mdl;
import aarogya.pharmacy.schemeMaster.shared.SchemeMaster_Srv;
import aarogya.pharmacy.schemeMaster.shared.SchemeMaster_SrvAsync;
import aarogya.pharmacy.schemeMaster.shared.SchemeMaster_Vld;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public final class SchemeMaster extends SchemeMaster_Dsg implements ClickHandler, AttachEvent.Handler, DmRootEvents {

    public interface CallBack {

        public void ok(GrdSchemeDetails_Mdl detailMdl);

        public void close(boolean isErrorClose);

    }

    ServiceInvoker<SchemeMaster_SrvAsync> srvAsync;
    final SchmeGridEdit<GrdSchemeDetails_Mdl, Grd_PrpAcs> gridSchemeEdit = new SchmeGridEdit(grdSchemeDetail);
    private CallBack callBack;
    String mode = "", departmentCode = "", v_Date = "", selectedBatchSql = "", medicineTotalQty = ""; //, v_Cat = ""
    int currentRowIndex;
    GrdSchemeDetails_Mdl returnMdl;
    private final BatchNo batchDetail = new BatchNo();
    BatchNo_Mdl batch_Mdl;
    boolean isEditAllow = false;

    public SchemeMaster(boolean isEditAllow) {
        super();
        this.isEditAllow = isEditAllow;
        srvAsync = new ServiceInvoker<>(GWT.create(SchemeMaster_Srv.class), SchemeMaster.class.getName());
        popupPanel.addAttachHandler(this);
        textHelpEvents();
        gridEvents();
        handlerEvents();
    }

    private void handlerEvents() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

//    public void show(String itemCode, String mode, String departmentCode, String v_Date, String vCat, String formDocid, String selectedBatchSQL, String medicineTotalQty) {
    public void show(String itemCode, String mode, String departmentCode, String v_Date, String selectedBatchSQL, String medicineTotalQty) {
        this.mode = mode;
        this.departmentCode = departmentCode;
        this.v_Date = v_Date;
//        this.v_Cat = vCat;
        this.selectedBatchSql = selectedBatchSQL;
        this.medicineTotalQty = medicineTotalQty;
        topControlMoveRec(itemCode);
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                ((CellSelectionModel) grdSchemeDetail.getGrid().getSelectionModel()).selectCell(0, 1);
            }
        });

    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void clear() {
        txtMedicineName.setTag("");
        txtMedicineName.setText("");
        txtMedicineName.setTag2nd("");
        txtUnit.setText("");
        txtUnit.setTag("");
        txtUnit.setTag2nd("");
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, v_Date, medicineTotalQty, new AsyncCallback<DmResult<SchemeMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<SchemeMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<SchemeMaster_Mdl> result) {
        SchemeMaster_Mdl data;

        if (result.isStatus()) {
            clear();
            data = result.getData();
            txtMedicineName.setTag(DmShared.iNull(data.getSharedKeyFieldCode()).toString());
            txtMedicineName.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtUnit.setTag(DmShared.iNull(data.getItemUnitCode()).toString());
            txtUnit.setText(DmShared.xNull(data.getItemUnitName()));
            lblSaleQty.setText("Sale Qty: " + medicineTotalQty + " " + txtUnit.getText());
            lblSaleQty.setTitle("Sale Qty: " + medicineTotalQty + " " + txtUnit.getText());
            grdSchemeDetail.getGrid().getStore().clear();
            grdSchemeDetail.getGrid().getStore().addAll(data.getLstSchemeDetails());
            ((CellSelectionModel) grdSchemeDetail.getGrid().getSelectionModel()).selectCell(0, 1);
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
    }

    private void topControlSave() {
        SchemeMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            SchemeMaster_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtMedicineName).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        returnRecord();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getValue2(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private SchemeMaster_Mdl topControlSaveInitialize() {
        SchemeMaster_Mdl mrTemp = new SchemeMaster_Mdl();
        mrTemp.setSharedKeyFieldCode(DmShared.iNull(txtMedicineName.getTag()).toString());
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtMedicineName.getText()));
        mrTemp.setItemUnitCode(DmShared.xNull(txtUnit.getTag()));
        mrTemp.setItemUnitName(DmShared.xNull(txtUnit.getText()));
        mrTemp.getLstSchemeDetails().clear();
        mrTemp.getLstSchemeDetails().addAll(grdSchemeDetail.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void topControlSaveError(int errorNo, final String gridRowIndex, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtMedicineName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) grdSchemeDetail.getGrid().getSelectionModel()).selectCell(DmShared.iNull(gridRowIndex).intValue(), 2);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtMedicineName).show();
                break;
        }
    }

    private void returnRecord() {
        callBack.ok(returnMdl);
        hide();
    }

    private void hide() {
        popupPanel.hide();
        callBack.close(false);

    }

    private void textHelpEvents() {

        batchDetail.addCallBack(new BatchNo.CallBack() {
            @Override
            public void ok(final BatchNo_Mdl batchDetail, final List<BatchNo_Mdl> mdlBatchDetails) {
                double totalQtyBatchList = 0;
                batch_Mdl = batchDetail;

                for (BatchNo_Mdl mdl : mdlBatchDetails) {
                    totalQtyBatchList += DmShared.vNull(mdl.getBalanceQtyInSaleUnit());
                }
                final double freeQty = Math.floor(DmShared.vNull(medicineTotalQty).doubleValue() / DmShared.vNull(grdSchemeDetail.getGrid().getStore().get(currentRowIndex).getSchemeOnQty()).doubleValue()) * DmShared.vNull(grdSchemeDetail.getGrid().getStore().get(currentRowIndex).getSchemeFreeQty()).doubleValue();
                if (Math.abs(freeQty) > totalQtyBatchList) {
                    if (DmShared.xNull(grdSchemeDetail.getGrid().getStore().get(currentRowIndex).getAllowNegativeStockFreeItem()).equalsIgnoreCase("1")) {
                        addbatchMdlToGrid(batch_Mdl, mdlBatchDetails, String.valueOf(freeQty));
                    } else {
                        if (totalQtyBatchList == 0) {
                            new DmAlertMessageBox("Total Stock Quantity is : " + totalQtyBatchList + ". Cannot select batch.", btnOk).show();
                            return;
                        }
                        new DmConfirmMessageBox("InSufficient Stock.\nFree Quantity : " + freeQty + ", Total Stock Quantity : " + totalQtyBatchList + "\n You wish to continue with stock quantity ? ", new DialogHideEvent.DialogHideHandler() {
                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                    String qty = "";
                                    qty = batchDetail.getBalanceQtyInSaleUnit();
                                    addbatchMdlToGrid(batch_Mdl, mdlBatchDetails, qty);
                                } else {
                                    addbatchMdlToGrid(null, null, "");
                                }
                            }
                        }).show();
                    }
                } else {
                    addbatchMdlToGrid(batch_Mdl, mdlBatchDetails, DmShared.vNull(freeQty).toString());
                }
            }

            @Override
            public void close(boolean isErrorClose) {
                if (!isErrorClose) {
                }
            }
        });
    }

    private void addbatchMdlToGrid(BatchNo_Mdl mdlBatch, List<BatchNo_Mdl> lstBatchDetails, String totalFreeQty) {
        grdSchemeDetail.getGrid().getStore().get(currentRowIndex).setBatchNo_Mdl(mdlBatch);
        grdSchemeDetail.getGrid().getStore().get(currentRowIndex).setTotalFreeQty(totalFreeQty);
        grdSchemeDetail.getGrid().getStore().get(currentRowIndex).getLstBatchDetails().clear();
        if (lstBatchDetails != null) {
            grdSchemeDetail.getGrid().getStore().get(currentRowIndex).getLstBatchDetails().addAll(lstBatchDetails);
        }
        grdSchemeDetail.commitChanges();
        ((CellSelectionModel) grdSchemeDetail.getGrid().getSelectionModel()).selectCell(0, 1);
    }

    private void gridEvents() {

        gridSchemeEdit.addCallBack(new SchmeGridEdit.CallBack() {
            @Override
            public void onOk(GrdSchemeDetails_Mdl details_Mdl) {
                grdSchemeDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                grdSchemeDetail.commitChanges();
            }
        });

        grdSchemeDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                if (event.getColumnIndex() == colIndexChkBox) {
                    setTick(event.getRowIndex());
                } else {
                    showGridEdit(event.getRowIndex());
                }
            }
        });

        grdSchemeDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                CellSelectionModel<GrdSchemeDetails_Mdl> cellModel;
                cellModel = (CellSelectionModel<GrdSchemeDetails_Mdl>) grdSchemeDetail.getGrid().getSelectionModel();
                if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                    if (cellModel.getSelectCell().getCell() == colIndexChkBox) {
                        event.preventDefault();
                        setTick(cellModel.getSelectCell().getRow());
                    }
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    if (cellModel.getSelectCell().getCell() != colIndexChkBox) {
                        showGridEdit(cellModel.getSelectCell().getRow());
                    }
                }
            }
        }, KeyDownEvent.getType());
    }

    private void save(GrdSchemeDetails_Mdl mdlDetails) {
        if (mdlDetails != null) {
            topControlSave();
        } else {
            new DmAlertMessageBox("Please check mark a record to continue", new HideEvent.HideHandler() {
                @Override
                public void onHide(HideEvent event) {
                    ((CellSelectionModel) grdSchemeDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                }
            }).show();
        }
    }

    private void setTick(final int rowIndex) {
        returnMdl = new GrdSchemeDetails_Mdl();
        removeAllTicks();
        if (grdSchemeDetail.getGrid().getStore().get(rowIndex).getIsChecked().equalsIgnoreCase("0")) {
            grdSchemeDetail.getGrid().getStore().get(rowIndex).setIsChecked("1");
        } else {
            grdSchemeDetail.getGrid().getStore().get(rowIndex).setIsChecked("0");
        }
        grdSchemeDetail.commitChanges();
        if (grdSchemeDetail.getGrid().getStore().get(rowIndex).getIsChecked().equalsIgnoreCase("1")) {
            if (DmShared.vNull(grdSchemeDetail.getGrid().getStore().get(rowIndex).getFreeQty()).doubleValue() != 0) {
                returnMdl = grdSchemeDetail.getGrid().getStore().get(rowIndex);
                currentRowIndex = rowIndex;
                showBatchDetailPopUp(grdSchemeDetail.getGrid().getStore().get(rowIndex).getSharedKeyFieldCode());
            } else {
                returnMdl = null;
                new DmAlertMessageBox("Not Allowed! .Free Quantity :" + DmShared.vNull(grdSchemeDetail.getGrid().getStore().get(rowIndex).getFreeQty()).toString(), new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) grdSchemeDetail.getGrid().getSelectionModel()).selectCell(rowIndex, 1);
                    }
                }).show();
            }
        } else {
            returnMdl = null;
        }
    }

    private void removeAllTicks() {
        for (GrdSchemeDetails_Mdl mdl : grdSchemeDetail.getGrid().getStore().getAll()) {
            mdl.setIsChecked("0");
        }
        grdSchemeDetail.commitChanges();
    }

    private void showGridEdit(int currentRowIndex) {
        if (DmShared.iNull(txtMedicineName.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Please fill Medicine Name", txtMedicineName).show();
            return;
        }
        if (mode.equalsIgnoreCase("Browse")) {
            gridSchemeEdit.show(currentRowIndex, false, false, false, this);
        } else {
            if (isEditAllow) {
                gridSchemeEdit.show(currentRowIndex, true, true, true, this);
            } else {
                gridSchemeEdit.show(currentRowIndex, false, false, false, this);
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnOk)) {
            save(returnMdl);
        } else if (event.getSource().equals(btnCancel)) {
            hide();
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
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        }
    }

    private void showBatchDetailPopUp(String itemCode) {
        if (!mode.equalsIgnoreCase("Browse")) {
            if (DmShared.iNull(itemCode).longValue() != 0) {
//                batchDetail.show(itemCode, departmentCode, v_Date, selectedBatchSql);
                batchDetail.show(itemCode, departmentCode, v_Date, selectedBatchSql, ItemBatchType.NON_EXPIRED_BATCH, false, "", null, "", false);
            } else {
                new DmAlertMessageBox("Please enter free item name.", txtMedicineName).show();
            }
        }
    }

}
