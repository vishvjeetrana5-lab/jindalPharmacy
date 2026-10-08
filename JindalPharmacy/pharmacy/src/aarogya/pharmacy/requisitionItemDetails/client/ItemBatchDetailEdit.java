package aarogya.pharmacy.requisitionItemDetails.client;

import aarogya.pharmacy.requisitionItemDetails.shared.GrdRequisitionItemDetails_Mdl;
import aarogya.pharmacy.saleBill.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import dataman.dmBase.self.shared.ServiceInvoker;
import com.google.gwt.core.client.Scheduler;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import aarogya.pharmacy.saleBill.shared.SaleBill_SrvAsync;
import aarogya.pharmacy.saleBill.shared.SaleBill_Srv;
import java.util.Date;

public class ItemBatchDetailEdit<T1, T2> extends ItemBatchDetailEdit_Dsg<T1, T2> implements BlurHandler, DmRootEvents, AttachEvent.Handler {

    interface CallBack {

        public void onOk(GrdRequisitionItemDetails_Mdl grdtest);

        public void onDelete();
    }

    ServiceInvoker<SaleBill_SrvAsync> SrvAsync;
    CallBack callBack;
    SaleBill saleBill;
    private int hashCode = 0;
    private String isBestBefore = "";

    private void textHelpEvents() {

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (isBestBefore.equalsIgnoreCase("1")) {
            if (event.getSource() == txtMfgDate) {
                if (!DmShared.xNull(txtMfgDate.getText()).equalsIgnoreCase("")) {
                    txtMfgDate.setText(DmShared.dNull(getManufacturingDate(DmShared.xNull(txtMfgDate.getText()))));
                } else {
                    txtBestBefore.setText("");
                    txtExpDate.setText("");
                }
            }
            if (event.getSource() == txtBestBefore) {
                if (!DmShared.xNull(txtBestBefore.getText()).equalsIgnoreCase("")) {
                    txtExpDate.setText(DmShared.dNull(getExpiryDate()));
                }
            }
        } else {
            if (event.getSource() == txtExpDate) {
                txtExpDate.setText(DmClient.returnDate(txtExpDate.getText()));
            }

        }
    }

    private String getManufacturingDate(String manufDate) {
        String manufacturingDate = "";
        String mDate = "01-" + DmShared.xNull(manufDate);
        manufacturingDate = DmClient.returnDate(mDate);
        return manufacturingDate;
    }

    private Date getExpiryDate() {
        Date tempDate = DmShared.convertDate(DmShared.dNull(txtMfgDate.getText()));
        Date finDate = new Date(tempDate.getYear(), tempDate.getMonth(), tempDate.getDate() + DmShared.iNull(txtBestBefore.getText()).intValue());

        return finDate;
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        this.saleBill = saleBill;

        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtBatchNo.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {

        clear();
        GrdRequisitionItemDetails_Mdl ourRow = (GrdRequisitionItemDetails_Mdl) row;
//        parentRow = new GrdRequisitionItemDetails_Mdl();

        txtMedicineName.setTag(DmShared.xNull(ourRow.getMedicineCode()));
        txtMedicineName.setText(DmShared.xNull(ourRow.getMedicineName()));

        if (DmShared.xNull(ourRow.getSaleQty()).equalsIgnoreCase("")) {
            txtSaleQty.setText(DmShared.xNull(ourRow.getReqQty()));
        } else {
            txtSaleQty.setText(DmShared.xNull(ourRow.getSaleQty()));
        }

        txtReqQty.setText(DmShared.xNull(ourRow.getReqQty()));
        txtSKUReqQty.setTag(DmShared.xNull(ourRow.getUnit1Code()));
        txtSKUReqQty.setText(DmShared.xNull(ourRow.getUnit1Name()));
        manageFlwBestBefr(DmShared.xNull(ourRow.getIsBestBefore()));
        txtBatchNo.setText(DmShared.xNull(ourRow.getBatchNo()));
        txtMRP.setText(DmClient.vNull(ourRow.getMrp(), "0.00"));
        txtSaleRate.setText(DmClient.vNull(ourRow.getSaleRate(), "0.00"));
        txtExpDate.setText(DmShared.dNull(ourRow.getExpDate()));
        txtMfgDate.setText(DmShared.dNull(ourRow.getManufacturingDate()));
    }

    @Override
    public T1 getNewRow() {
        return (T1) new aarogya.pharmacy.requisitionItemDetails.shared.GrdRequisitionItemDetails_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.vNull(txtReqQty.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Net Qty. is Mandatory.", txtReqQty).show();
            return;
        }
        if (DmShared.xNull(txtMRP.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Medicine is Mandatory.", txtMRP).show();
            return;
        }
        if (DmShared.xNull(txtBatchNo.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Batch No. is Mandatory.", txtBatchNo).show();
            return;
        }

        if (DmShared.vNull(txtSaleRate.getText()).doubleValue() > DmShared.vNull(txtMRP.getText()).doubleValue()) {
            new DmAlertMessageBox("Sale Rate should be less than or equal to MRP.", txtSaleRate).show();
            return;
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {

        GrdRequisitionItemDetails_Mdl ourRow = (GrdRequisitionItemDetails_Mdl) row;

        ourRow.setMedicineCode(DmShared.xNull(txtMedicineName.getTag()));
        ourRow.setMedicineName(DmShared.xNull(txtMedicineName.getText()));

        ourRow.setBatchNo(DmShared.xNull(txtBatchNo.getText()));
        ourRow.setMrp(DmClient.vNull(txtMRP.getText(), "0.00"));
        ourRow.setSaleRate(DmClient.vNull(txtSaleRate.getText(), "0.00"));
        ourRow.setExpDate(DmShared.dNull(txtExpDate.getText()));
        ourRow.setManufacturingDate(DmShared.dNull(txtMfgDate.getText()));

        ourRow.setReqQty(DmShared.xNull(txtReqQty.getText()));
        ourRow.setSaleQty(DmShared.xNull(txtSaleQty.getText()));
        ourRow.setUnit1Code(DmShared.xNull(txtSKUReqQty.getTag()));
        ourRow.setUnit1Name(DmShared.xNull(txtSKUReqQty.getText()));
//        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
//        ourRow.setOHLList(overHeadLine.getOverHeadList());

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        proceedDelete();
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    public ItemBatchDetailEdit(DmGrid<T1, T2> grid) {
        super(grid, "Batch Details Edit", "316px", "178px");
        SrvAsync = new ServiceInvoker<>(GWT.create(SaleBill_Srv.class), SaleBill.class.getName());
        flwForm.addAttachHandler(this);
        manageDisplay();
        textHelpEvents();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_R) {
//            if (cbIsReturnValue.getValue()) {
//                cbIsReturnValue.setValue(Boolean.FALSE);
//                Calculate();
//            } else {
//                cbIsReturnValue.setValue(Boolean.TRUE);
//                Calculate();
//            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
//            batchDetail1.hide();
        }
    }

    private void manageDisplay() {
        txtSaleRate.addBlurHandler(this);
        txtSKUReqQty.setEnabled(false);
        txtReqQty.addBlurHandler(this);
        txtExpDate.addBlurHandler(this);
        txtMedicineName.setEnabled(false);
        txtReqQty.setEnabled(false);
    }

    private void manageFlwBestBefr(String isBestBefore) {
        this.isBestBefore = isBestBefore;
        hrtPnl.clear();
        if (DmShared.xNull(isBestBefore).equalsIgnoreCase("1")) {
            txtMfgDate.setText("");
            txtExpDate.setText("");
            txtBestBefore.setText("");
            hrtPnl.setWidth("100%");
            hrtPnl.add(flwWithBstBefr);
            hrtPnl.setCellWidth(flwWithBstBefr, "70%");
            hrtPnl.add(flwWithOutBstBefr);
        } else {
            txtExpDate.setText("");
            hrtPnl.setWidth("34.5%");
            hrtPnl.add(flwWithOutBstBefr);
            hrtPnl.setCellWidth(flwWithOutBstBefr, "34%");

        }
    }

    private void clear() {
        txtMRP.setText("0");
        txtSaleRate.setText("0");
        txtBatchNo.setTag("");
        txtBatchNo.setText("");
        txtExpDate.setText("");
    }

}
