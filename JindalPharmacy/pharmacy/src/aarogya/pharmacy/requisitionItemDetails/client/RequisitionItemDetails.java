package aarogya.pharmacy.requisitionItemDetails.client;

import aarogya.pharmacy.requisitionItemDetails.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RequisitionItemDetails extends RequisitionItemDetails_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, KeyPressHandler, KeyDownHandler, BlurHandler {

    public interface normalValueCallBack {

        public void onOk(List<GrdRequisitionItemDetails_Mdl> mrtemp);

        public void onClose();

    }
    normalValueCallBack nmCallBack;
    ServiceInvoker<RequisitionItemDetails_SrvAsync> SrvAsync;
    final ItemBatchDetailEdit<GrdRequisitionItemDetails_Mdl, DmRequisitionItemDetails_PrpAcs> ItemBatchDetailEdit = new ItemBatchDetailEdit<>(dmRequisitionItemDetails);
    String departmentCode = "";
    int selectedRowIndex;

    public void show(List<GrdRequisitionItemDetails_Mdl> mrTemp, boolean editable, String departmentCode, boolean isRequisition) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        flwOk.clear();
        flwOk.setVisible(false);
        this.departmentCode = departmentCode;
        if (editable) {
            flwOk.add(btnOk);
            flwOk.setVisible(true);
        }

        displayValue(mrTemp);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {

                ((CellSelectionModel) dmRequisitionItemDetails.getGrid().getSelectionModel()).selectCell(0, 3);
            }
        });

    }

    @Override
    public void onKeyDown(KeyDownEvent event) {

    }

    private void displayValue(List<GrdRequisitionItemDetails_Mdl> mrTemp) {
        dmRequisitionItemDetails.getGrid().getStore().clear();

//        txtDays.setText(DmShared.iNull(mrTemp.get(0).getMaxDoseDays()).toString());
        if (mrTemp != null) {
            if (mrTemp.size() > 0) {
                dmRequisitionItemDetails.getGrid().getStore().addAll(mrTemp);
                dmRequisitionItemDetails.commitChanges();
                calculateDays();
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
//        if (event.getSource().equals(txtDays)) {
//            if (DmShared.iNull(txtDays.getText()).intValue() <= 0) {
//                new DmAlertMessageBox("Day Can't be 0.", txtDays).show();
//                txtDays.setText("1");
//                return;
//            }
//        }
        calculateDays();
        ((CellSelectionModel) dmRequisitionItemDetails.getGrid().getSelectionModel()).selectCell(0, 3);
    }

    private List<GrdRequisitionItemDetails_Mdl> getValue() {
        List<GrdRequisitionItemDetails_Mdl> mrTemp = new ArrayList<GrdRequisitionItemDetails_Mdl>();

        mrTemp.addAll(dmRequisitionItemDetails.getGrid().getStore().getAll());

        return mrTemp;
    }

    public void hide() {
        this.popupPanel.hide();
    }

    public void addCallBack(normalValueCallBack callBack) {
        this.nmCallBack = callBack;
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            this.hide();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnOk)) {
//            if (DmShared.iNull(txtDays.getText()).intValue() <= 0) {
//                new DmAlertMessageBox("Day Can't be 0.", txtDays).show();
//                return;
//            }

            List<GrdRequisitionItemDetails_Mdl> mrTemp = new ArrayList<GrdRequisitionItemDetails_Mdl>();
            mrTemp = getValue();
            nmCallBack.onOk(mrTemp);
            popupPanel.hide();
        } else if (event.getSource().equals(btnCancel)) {
            this.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public RequisitionItemDetails() {
        super();
        SrvAsync = new ServiceInvoker<RequisitionItemDetails_SrvAsync>(GWT.create(RequisitionItemDetails_Srv.class), RequisitionItemDetails.class.getName());
        gridEvents();

        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Requisition Item Details");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
//        toolContainer.addWidget(flwDaysLabel);
//        toolContainer.addWidget(flwDaysTextBox);
//        txtDays.addBlurHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
//        int charCode = event.getCharCode();
//        if (event.getSource().equals(txtValuePattern)) {
//
//            if ((charCode != 48) && (charCode != 46) && (charCode != 0)) {
//                txtValuePattern.cancelKey();
//            } else {
//                if (txtValuePattern.getText().split("\\.").length > 3) {
//                    txtValuePattern.cancelKey();
//                }
//            }
//        }
    }

    private void gridEvents() {
        ItemBatchDetailEdit.addCallBack(new ItemBatchDetailEdit.CallBack() {
            @Override
            public void onOk(GrdRequisitionItemDetails_Mdl grdtest) {
                checkSaleQty(grdtest);
            }

            @Override
            public void onDelete() {
                dmRequisitionItemDetails.commitChanges();
            }
        });
        dmRequisitionItemDetails.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showBatchGrid(event.getRowIndex());
            }
        });
        dmRequisitionItemDetails.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                int rowIndex = dmRequisitionItemDetails.getGrid().getStore().indexOf(dmRequisitionItemDetails.getGrid().getSelectionModel().getSelectedItem());
//                int columnIndex = ((CellSelectionModel) dmRequisitionItemDetails.getGrid().getSelectionModel()).getSelectCell().getCell();
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
//                    if (columnIndex == 3 || columnIndex == 4 || columnIndex == 5 || columnIndex == 1 || columnIndex == 2) {
                    showBatchGrid(rowIndex);
//                    }
//                    else if (columnIndex == 6) {
//                        addGridRow(rowIndex, true);
//                        ((CellSelectionModel) dmRequisitionItemDetails.getGrid().getSelectionModel()).selectCell(selectedRowIndex, 6);
//                    }

                } else if (event.getNativeKeyCode() == 46) {
//                    if (columnIndex == 3 || columnIndex == 4 || columnIndex == 5 || columnIndex == 1 || columnIndex == 2) {
                    dmRequisitionItemDetails.getGrid().getStore().get(rowIndex).setBatchNo("");
                    dmRequisitionItemDetails.getGrid().getStore().get(rowIndex).setExpDate("");
                    dmRequisitionItemDetails.getGrid().getStore().get(rowIndex).setSaleRate("");
                    dmRequisitionItemDetails.getGrid().getStore().get(rowIndex).setMrp("");
                    dmRequisitionItemDetails.getGrid().getStore().get(rowIndex).setManufacturingDate("");
                    dmRequisitionItemDetails.getGrid().getStore().get(rowIndex).setSaleRate("");
                    dmRequisitionItemDetails.getGrid().getStore().get(rowIndex).setSaleQty("");
                    dmRequisitionItemDetails.getGrid().getStore().get(rowIndex).setReqQty(DmClient.vNull(dmRequisitionItemDetails.getGrid().getStore().get(rowIndex).getReqQty(), "0.00"));
                    dmRequisitionItemDetails.commitChanges();
                    ((CellSelectionModel) dmRequisitionItemDetails.getGrid().getSelectionModel()).selectCell(rowIndex, 3);
//                    }
                }
            }
        }, KeyDownEvent.getType());
//        dmRequisitionItemDetails.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
//
//            @Override
//            public void onCellClick(CellClickEvent event) {
//                try {
//                    if (event.getCellIndex() == 8) {
//                        int val = DmShared.iNull(dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).getAddRow()).intValue();
//                        if (val == 1) {
//                            addGridRow(event.getRowIndex(), true);
//                            ((CellSelectionModel) dmRequisitionItemDetails.getGrid().getSelectionModel()).selectCell(selectedRowIndex, 6);
//                        }
//                    } else if (event.getCellIndex() == 3 || event.getCellIndex() == 4 || event.getCellIndex() == 5 || event.getCellIndex() == 2) {
//                        showBatchGrid(event.getRowIndex());
//                    }
//
//                } catch (Exception e) {
//                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
//                }
//            }
//        });
    }

    private void checkSaleQty(GrdRequisitionItemDetails_Mdl grdtest) {
        double netSaleQty = 0;
        for (GrdRequisitionItemDetails_Mdl gridm : dmRequisitionItemDetails.getGrid().getStore().getAll()) {
            if (DmShared.xNull(grdtest.getMedicineCode()).equalsIgnoreCase(gridm.getMedicineCode())) {
                netSaleQty += DmShared.vNull(gridm.getSaleQty()).doubleValue();
            }
        }
        if (DmShared.vNull(grdtest.getReqQty()).doubleValue() > netSaleQty) {
            addGridRow(true, DmShared.vNull(netSaleQty).toString());
            dmRequisitionItemDetails.commitChanges();
        } else if (DmShared.vNull(grdtest.getReqQty()).doubleValue() < netSaleQty) {
            new DmAlertMessageBox("Sale Qty. exceeds Req. Qty.", (DmTextBox) null).show();
            return;
        } else if (DmShared.vNull(grdtest.getReqQty()).doubleValue() == netSaleQty) {
            dmRequisitionItemDetails.commitChanges();
        }
    }

    private void addGridRow(boolean isAddColumn, String netSaleQty) {
        double pendingQty = 0;
        GrdRequisitionItemDetails_Mdl grdRequisitionItemDetails = dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex);
        if (isAddColumn) {
//            if (DmShared.vNull(grdRequisitionItemDetails.getCalculatedReqQty()).doubleValue() == DmShared.vNull(grdRequisitionItemDetails.getCalculatedSaleQty()).doubleValue()) {
//                new DmAlertMessageBox("Req. Qty. and Sale Qty. are same, you can't add new Row", (DmTextBox) null);
//            }
        }
        pendingQty = DmShared.vNull(grdRequisitionItemDetails.getReqQty()).doubleValue() - DmShared.vNull(netSaleQty).doubleValue();
        GrdRequisitionItemDetails_Mdl grdNewRow = new GrdRequisitionItemDetails_Mdl();
        java.util.Random rnd = new Random();
        grdNewRow.setVSno(DmClient.vNull(rnd.nextInt(), "0"));
//        grdNewRow.setAddRow(DmShared.xNull(grdRequisitionItemDetails.getAddRow()));
        grdNewRow.setReqQty(DmShared.xNull(grdRequisitionItemDetails.getReqQty()));
        grdNewRow.setMedicineCode(DmShared.xNull(grdRequisitionItemDetails.getMedicineCode()));
        grdNewRow.setMedicineName(DmShared.xNull(grdRequisitionItemDetails.getMedicineName()));
        grdNewRow.setRequisitionDocid(DmShared.xNull(grdRequisitionItemDetails.getRequisitionDocid()));
        grdNewRow.setRequisitionRecid(DmShared.xNull(grdRequisitionItemDetails.getRequisitionRecid()));
        grdNewRow.setUnit1Code(DmShared.xNull(grdRequisitionItemDetails.getUnit1Code()));
        grdNewRow.setUnit1Name(DmShared.xNull(grdRequisitionItemDetails.getUnit1Name()));
        grdNewRow.setSaleQty(DmShared.vNull(pendingQty).toString());

        grdNewRow.setManufacturingDate("");
        grdNewRow.setMrp("");
        grdNewRow.setBatchNo("");
        grdNewRow.setSaleRate("");
        grdNewRow.setExpDate("");

        dmRequisitionItemDetails.getGrid().getStore().add(selectedRowIndex + 1, grdNewRow);
        dmRequisitionItemDetails.commitChanges();

    }

//    private void updateGridRow(GrdRequisitionItemDetails_Mdl grdRequisitionItemDetails, MedicineBatchDetail_Mdl batchDetail_Mdl, boolean YesNo) {
//        if (YesNo) {
//            dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setBatchCode(DmShared.xNull(batchDetail_Mdl.getSharedKeyFieldCode()));
//            dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setBatchNo(DmShared.xNull(batchDetail_Mdl.getSharedKeyFieldName()));
//            dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setExpDate(DmShared.dNull(batchDetail_Mdl.getExpiryDate()));
//            dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setSaleRate(DmClient.vNull(batchDetail_Mdl.getSaleRate(), "0.00"));
//            dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setMrp(DmClient.vNull(batchDetail_Mdl.getMrp(), "0.00"));
//            if (DmShared.vNull(batchDetail_Mdl.getStock()).doubleValue() < DmShared.vNull(grdRequisitionItemDetails.getCalculatedReqQty()).doubleValue()) {
//                //double Qty = DmShared.vNull(grdRequisitionItemDetails.getCalculatedReqQty()) - DmShared.vNull(batchDetail_Mdl.getStock());
//                double stockQty = DmShared.vNull(batchDetail_Mdl.getStock());
//                dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setDisplaySaleQty(converQty(DmShared.iNull(grdRequisitionItemDetails.getConversionRate()).toString(), DmShared.iNull(stockQty).toString()));
//                dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setSaleQty(DmShared.iNull(stockQty).toString());
//                dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setCalculatedSaleQty(DmShared.iNull(stockQty).toString());
//                dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setCalculatedReqQty(DmShared.iNull(DmShared.vNull(grdRequisitionItemDetails.getCalculatedReqQty()).doubleValue() - stockQty).toString());
//            } else {
//                dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setDisplaySaleQty(converQty(DmShared.iNull(grdRequisitionItemDetails.getConversionRate()).toString(), DmShared.iNull(grdRequisitionItemDetails.getCalculatedReqQty()).toString()));
//                dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setSaleQty(DmShared.iNull(grdRequisitionItemDetails.getCalculatedReqQty()).toString());
//                dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setCalculatedSaleQty(DmShared.iNull(grdRequisitionItemDetails.getCalculatedReqQty()).toString());
//            }
//        } else {
//            dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setBatchCode(DmShared.xNull(batchDetail_Mdl.getSharedKeyFieldCode()));
//            dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setBatchNo(DmShared.xNull(batchDetail_Mdl.getSharedKeyFieldName()));
//            dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setExpDate(DmShared.dNull(batchDetail_Mdl.getExpiryDate()));
//            dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setSaleRate(DmClient.vNull(batchDetail_Mdl.getSaleRate(), "0.00"));
//            dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setMrp(DmClient.vNull(batchDetail_Mdl.getMrp(), "0.00"));
//            dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setDisplaySaleQty(grdRequisitionItemDetails.getDisplayReqQty());
//            dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setSaleQty(DmShared.iNull(grdRequisitionItemDetails.getCalculatedReqQty()).toString());
//            dmRequisitionItemDetails.getGrid().getStore().get(selectedRowIndex).setCalculatedSaleQty(DmShared.iNull(grdRequisitionItemDetails.getCalculatedReqQty()).toString());
//        }
//        dmRequisitionItemDetails.commitChanges();
//    }
    private void showBatchGrid(int currentRowIndex) {
        selectedRowIndex = currentRowIndex;
        ItemBatchDetailEdit.show(currentRowIndex, true, false, true);
    }

    private String converQty(String convertRate, String Qty) {
        String displayConverQty = "";
        if (DmShared.vNull(convertRate).doubleValue() > 0) {
            if (DmShared.vNull(convertRate).doubleValue() <= DmShared.vNull(Qty).doubleValue()) {
                int unit2Qty = DmShared.iNull((DmShared.vNull(Qty).doubleValue() / DmShared.vNull(convertRate).doubleValue())).intValue();
                int unit1Qty = DmShared.iNull(DmShared.vNull(Qty).doubleValue() - (DmShared.vNull(unit2Qty).doubleValue() * DmShared.vNull(convertRate).doubleValue())).intValue();
                displayConverQty = (unit2Qty + " : " + unit1Qty);
            } else if (DmShared.vNull(convertRate).doubleValue() <= Math.abs(DmShared.vNull(Qty).doubleValue())) {
                int unit2Qty = DmShared.iNull((Math.abs(DmShared.vNull(Qty).doubleValue()) / DmShared.vNull(convertRate).doubleValue())).intValue();
                int unit1Qty = DmShared.iNull(Math.abs(DmShared.vNull(Qty).doubleValue()) - (DmShared.vNull(unit2Qty).doubleValue() * DmShared.vNull(convertRate).doubleValue())).intValue();
                displayConverQty = ("- " + unit2Qty + " : " + unit1Qty);
            } else {
                displayConverQty = (0 + " : " + DmShared.iNull(Qty).intValue());
            }
        } else {
            displayConverQty = (0 + " : " + DmShared.iNull(Qty).intValue());
        }
        return displayConverQty;
    }

    private void calculateDays() {

        if (dmRequisitionItemDetails.getGrid().getStore().size() > 0) {
            for (int i = 0; i < dmRequisitionItemDetails.getGrid().getStore().size(); i++) {
                if (DmShared.xNull(dmRequisitionItemDetails.getGrid().getStore().get(i).getMedicineNature()).equalsIgnoreCase("Tab") || DmShared.xNull(dmRequisitionItemDetails.getGrid().getStore().get(i).getMedicineNature()).equalsIgnoreCase("Inj")) {
//                    if (DmShared.vNull(dmRequisitionItemDetails.getGrid().getStore().get(i).getCalculatedReqQty()).doubleValue() > 0) {
//                        double NoOfDay = DmShared.vNull(txtDays.getText());
//                        double doseDay = DmShared.vNull(dmRequisitionItemDetails.getGrid().getStore().get(i).getDoseDays());
                    double reqQty = DmShared.vNull(dmRequisitionItemDetails.getGrid().getStore().get(i).getReqQty());
//                        String converRate = DmShared.iNull(dmRequisitionItemDetails.getGrid().getStore().get(i).getConversionRate()).toString();
//                        if (doseDay <= NoOfDay) {
////                            dmRequisitionItemDetails.getGrid().getStore().get(i).setCalculatedReqQty(DmClient.vNull((doseDay * reqQty), "0.00"));
//                        } else {
////                            dmRequisitionItemDetails.getGrid().getStore().get(i).setCalculatedReqQty(DmClient.vNull((reqQty * NoOfDay), "0.00"));
//                        }
//                        dmRequisitionItemDetails.getGrid().getStore().get(i).setDisplayReqQty(converQty(converRate, DmClient.vNull(dmRequisitionItemDetails.getGrid().getStore().get(i).getCalculatedReqQty(), "0.00")));
//                    }
                }
            }
            dmRequisitionItemDetails.commitChanges();
        }

    }
}
