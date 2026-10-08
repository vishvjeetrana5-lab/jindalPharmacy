package aarogya.master.rateListMaster.client;

import aarogya.master.rateListMaster.shared.GrdCategoryDiscount_Mdl;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class CategoryWiseDiscountGridEdit<T1, T2> extends CategoryWiseDiscountGridEdit_Dsg<T1, T2> implements AttachEvent.Handler, BlurHandler {

    private CallBack callBack;

    interface CallBack {

        public void onOk(GrdCategoryDiscount_Mdl grdtest);

        public void onDelete();
    }

    public CategoryWiseDiscountGridEdit(DmGrid<T1, T2> grid, String widgetName) {
        super(grid, widgetName, "425px", "100px");
        txtDiscountPer.addBlurHandler(this);
        txtCategory.setEnabled(false);
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {

        super.showCenter(currentRowIndex, okNeeded, false, false);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtDiscountPer.setFocus(true);
            }
        });
    }

    @Override
    public void onOk() {
        if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 100) {
            new DmAlertMessageBox("Discount % should not be greater than 100%.", txtDiscountPer).show();
            return;
        }

        proceedOk();
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
    public void manageOk(T1 row) {
        GrdCategoryDiscount_Mdl ourRow = (GrdCategoryDiscount_Mdl) row;
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtCategory.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtCategory.getText()));
        ourRow.setOrgnaizationCode(DmShared.xNull(txtCategory.getTag2nd()));

        ourRow.setDiscountPercentage(DmClient.vNull(txtDiscountPer.getText(), "0.00"));

        boolean flag = false;
        if (DmShared.vNull(ourRow.getDiscountPercentage()) > 0) {
            flag = true;
        }
        ourRow.setColorChange(flag);
        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdCategoryDiscount_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdCategoryDiscount_Mdl ourRow = (GrdCategoryDiscount_Mdl) row;

        txtCategory.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtCategory.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtCategory.setTag2nd(DmShared.xNull(ourRow.getOrgnaizationCode()));

        txtDiscountPer.setText(DmClient.vNull(ourRow.getDiscountPercentage(), "0.00"));

    }

    @Override
    public void onBlur(BlurEvent event) {

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
}
