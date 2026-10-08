package aarogya.master.rateListMaster.client;

import aarogya.master.rateListMaster.shared.GrdRoomCategory_Mdl;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class RoomCategoryGridEdit<T1, T2> extends RoomCategoryGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdRoomCategory_Mdl grdtest);

        public void onDelete();
    }

    private CallBack callBack;
    private GrdRoomCategory_Mdl parentRow;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {

        super.showRelativeTo(currentRowIndex, 1, okNeeded, false, false);

    }

    @Override
    public void displayRow(T1 row) {
        GrdRoomCategory_Mdl ourRow = (GrdRoomCategory_Mdl) row;
        parentRow = new GrdRoomCategory_Mdl();

        txtCategory.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtCategory.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtDiscountPer.setText(DmClient.vNull(ourRow.getDiscountPercentage(), "0.00"));
        txtDiscount.setText(DmClient.vNull(ourRow.getDiscountAmount(), "0.00"));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtRate.setFocus(true);

    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdRoomCategory_Mdl();
    }

    @Override
    public void onOk() {

        if (DmShared.vNull(txtDiscount.getText()).doubleValue() > 0 || DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0) {
            if (DmShared.vNull(txtRate.getText()).doubleValue() <= 0) {
                new DmAlertMessageBox("Rate is mandatory when a discount is gven.", txtRate).show();
                return;
            }
        }

        if (DmShared.vNull(txtRate.getText()).doubleValue() > 0) {
            if (DmShared.vNull(txtDiscount.getText()).doubleValue() > DmShared.vNull(txtRate.getText()).doubleValue()) {
                new DmAlertMessageBox("Discount should not be greater than rate.", txtDiscount).show();
                return;
            }
        }

        if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 100) {
            new DmAlertMessageBox("Discount % should not be greater than 100%.", txtDiscountPer).show();
            return;
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdRoomCategory_Mdl ourRow = (GrdRoomCategory_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtCategory.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtCategory.getText()));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setDiscountPercentage(DmClient.vNull(txtDiscountPer.getText(), "0.00"));
        ourRow.setDiscountAmount(DmClient.vNull(txtDiscount.getText(), "0.00"));

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

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtDiscountPer || event.getSource() == txtDiscount) {
            calculate();
        }
    }

    private void calculate() {

        if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0) {
            txtDiscount.setText("0.00");
        }
        if (DmShared.vNull(txtDiscount.getText()).doubleValue() > 0) {
            txtDiscountPer.setText("0.00");
        }

    }

    public RoomCategoryGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Room Category Edit", "450px", "105px");
        txtDiscount.addBlurHandler(this);
        txtDiscountPer.addBlurHandler(this);
        txtRate.addBlurHandler(this);
    }

}
