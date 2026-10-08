
package aarogya.master.organizationMaster.client;

import aarogya.master.organizationMaster.shared.GrdPercentage_Mdl;
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

public class PercentageDiscountGridEdit<T1, T2> extends PercentageDiscountGridEdit_Dsg<T1, T2> implements AttachEvent.Handler, BlurHandler {

    private CallBack callBack;

    interface CallBack {

        public void onOk(GrdPercentage_Mdl grdPercent);

        public void onDelete();
    }

    public PercentageDiscountGridEdit(DmGrid<T1, T2> grid, String title) {
        super(grid, title, "200px", "100px");
        txtDiscountPer.addBlurHandler(this);

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
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
        GrdPercentage_Mdl ourRow = (GrdPercentage_Mdl) row;
        ourRow.setServiceNo(DmShared.xNull(txtServiceNo.getText()));
        ourRow.setPercentage(DmClient.vNull(txtDiscountPer.getText(), "0.00"));
        callBack.onOk(ourRow);
        
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdPercentage_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdPercentage_Mdl ourRow = (GrdPercentage_Mdl) row;
        txtServiceNo.setText(DmShared.xNull(ourRow.getServiceNo()));
        txtDiscountPer.setText(DmClient.vNull(ourRow.getPercentage(), "0.00"));
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
    
    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded){
        super.showRelativeTo(currentRowIndex,1, okNeeded, true, false);
    }

}
