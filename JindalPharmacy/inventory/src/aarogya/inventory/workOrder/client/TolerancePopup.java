package aarogya.inventory.workOrder.client;

import aarogya.inventory.workOrder.shared.Tolerance_Mdl;
import aarogya.selfToolkit.stStatic.shared.STShared;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class TolerancePopup extends TolerancePopup_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    //<editor-fold defaultstate="collapsed" desc="Callback">
    public interface CallBack {

        public void onOk(Tolerance_Mdl tMdl);

        public void onClose();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>    

    WorkOrderGridEdit poGridEdit;
    Tolerance_Mdl tMdl = null;

    public TolerancePopup() {
        super();
        manageEvents();
        manageDisplay();
    }

    public void show(WorkOrderGridEdit poGridEdit, Tolerance_Mdl tMdl) {
        clear();
        this.poGridEdit = poGridEdit;
        this.tMdl = tMdl;
        setValues();
        calculate();
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtTl_ExcessPer.setFocus(true);
        flwOk.clear();
        if (!DmShared.xNull(poGridEdit.mode).equalsIgnoreCase("Browse")) {
            flwOk.add(vl.add(btnOk));
        }
    }

    private void setValues() {
        txtTl_ExcessPer.setText(DmShared.xNull(tMdl.getTl_ExcessPer()));
        txtTl_ExcessQty.setText(DmShared.xNull(tMdl.getTl_ExcessQty()));
        txtTl_ShortagePer.setText(DmShared.xNull(tMdl.getTl_ShortagePer()));
        txtTl_ShortageQty.setText(DmShared.xNull(tMdl.getTl_ShortageQty()));

        txtOrderQty.setText(DmShared.xNull(tMdl.getOrderQuantity()));
        txtOrderQtyWithExcess.setText(DmShared.xNull(tMdl.getOrderQuantityWithExcess()));
        txtOrderQtyWithShortage.setText(DmShared.xNull(tMdl.getOrderQuantityWithShortage()));
    }

    private void clear() {
        txtOrderQty.setText("");
        txtOrderQtyWithExcess.setText("");
        txtOrderQtyWithShortage.setText("");
        txtTl_ExcessPer.setText("");
        txtTl_ExcessQty.setText("");
        txtTl_ShortagePer.setText("");
        txtTl_ShortageQty.setText("");
    }

    private void manageEvents() {
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtTl_ExcessPer.addBlurHandler(this);
        txtTl_ExcessQty.addBlurHandler(this);
        txtTl_ShortagePer.addBlurHandler(this);
        txtTl_ShortageQty.addBlurHandler(this);
    }

    private void manageDisplay() {
        txtOrderQty.setEnabled(false);
        txtOrderQtyWithExcess.setEnabled(false);
        txtOrderQtyWithShortage.setEnabled(false);
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER
                && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            onOk();
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
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            onClose();
        } else if (event.getSource() == btnOk) {
            onOk();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtTl_ExcessPer
                || event.getSource() == txtTl_ExcessQty
                || event.getSource() == txtTl_ShortagePer
                || event.getSource() == txtTl_ShortageQty) {
            calculate();
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void onClose() {
        hide();
        if (callBack != null) {
            callBack.onClose();
        }
    }

    private void onOk() {

        if (DmShared.vNull(txtTl_ExcessPer.getText()) > 99.99) {
            new DmAlertMessageBox("Tolerance Excess can't be greater than 99.99", txtTl_ExcessPer).show();
            return;
        }

        if (DmShared.vNull(txtTl_ShortagePer.getText()) > 99.99) {
            new DmAlertMessageBox("Tolerance Shortage can't be greater than 99.99", txtTl_ShortagePer).show();
            return;
        }

        hide();
        if (callBack != null) {
            calculate();
            tMdl.setOrderQuantity(DmShared.xNull(txtOrderQty.getText()));
            tMdl.setTl_ExcessPer(DmShared.xNull(txtTl_ExcessPer.getText()));
            tMdl.setTl_ExcessQty(DmShared.xNull(txtTl_ExcessQty.getText()));
            tMdl.setTl_ShortagePer(DmShared.xNull(txtTl_ShortagePer.getText()));
            tMdl.setTl_ShortageQty(DmShared.xNull(txtTl_ShortageQty.getText()));
            tMdl.setOrderQuantityWithExcess(DmShared.xNull(txtOrderQtyWithExcess.getText()));
            tMdl.setOrderQuantityWithShortage(DmShared.xNull(txtOrderQtyWithShortage.getText()));
            callBack.onOk(tMdl);
        }
    }

    private void calculate() {

        double orderQty = DmShared.vNull(txtOrderQty.getText());
        double tl_ExcessPer = DmShared.vNull(txtTl_ExcessPer.getText());
        double tl_ShortagePer = DmShared.vNull(txtTl_ShortagePer.getText());
        String purchaseUnitDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(poGridEdit.txtPurchaseUnit.getTag2nd()).intValue());

        if (tl_ExcessPer > 0) {
            txtTl_ExcessQty.setText(DmShared.vNull(orderQty * (tl_ExcessPer * 0.01)).toString());
        }
        txtTl_ExcessQty.setText(DmClient.vNull(txtTl_ExcessQty.getText(), purchaseUnitDecimalFormat));

        if (tl_ShortagePer > 0) {
            txtTl_ShortageQty.setText(DmShared.vNull(orderQty * (tl_ShortagePer * 0.01)).toString());
        }
        txtTl_ShortageQty.setText(DmClient.vNull(txtTl_ShortageQty.getText(), purchaseUnitDecimalFormat));

        double tl_ExcessQty = DmShared.vNull(txtTl_ExcessQty.getText());
        double tl_ShortageQty = DmShared.vNull(txtTl_ShortageQty.getText());

        if (tl_ExcessQty > 0 || tl_ShortageQty > 0) {
            txtOrderQtyWithExcess.setText(DmClient.vNull(orderQty + tl_ExcessQty, purchaseUnitDecimalFormat));
            txtOrderQtyWithShortage.setText(DmClient.vNull(orderQty - tl_ShortageQty, purchaseUnitDecimalFormat));
        }

    }

}
