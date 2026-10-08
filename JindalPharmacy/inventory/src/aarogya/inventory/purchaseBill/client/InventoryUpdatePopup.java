package aarogya.inventory.purchaseBill.client;

import aarogya.inventory.purchaseBill.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.ServiceInvoker;
import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public class InventoryUpdatePopup extends InventoryUpdatePopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    CallBack callBack;
    ServiceInvoker<PurchaseBill_SrvAsync> SrvAsync;
    String applicableBatch;
    Boolean isEditable = false;
    PurchaseBill purchaseBill;
    PurchaseBill_Mdl pbDm;

    private void textHelpEvents() {

       
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtPaymentDueDate) {
            txtPaymentDueDate.setText(DmClient.returnDate(txtPaymentDueDate.getText()));
        } else if (event.getSource() == txtSupplierBillDate) {
            txtSupplierBillDate.setText(DmClient.returnDate(txtSupplierBillDate.getText()));
            if ((DmShared.convertDate(txtSupplierBillDate.getText()).getTime() > (DmShared.convertDate(DmShared.dNull(purchaseBill.txtVoucherDate.getText())).getTime()))) {
                new DmAlertMessageBox("Bill Date should be less than or equal to Voucher Date.", txtSupplierBillDate).show();
                return;
            }
        }
    }

    public interface CallBack {

        public void onOk(PurchaseBill_Mdl pbDm);

        public void onClose();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(PurchaseBill_Mdl pbDm, PurchaseBill parent) {
        this.pbDm = pbDm;

        this.purchaseBill = parent;

        btnOk.setVisible(true);

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtSupplierBillNo.setText(DmShared.xNull(pbDm.getSupplierBillNo()));
        txtSupplierBillDate.setText(pbDm.getSupplierBillDate());
        txtPaymentDueDate.setText(parent.txtPaymentDueDate.getText());
        txtRemark.setText(pbDm.getRemark());
        txtSupplierBillNo.setFocus(true);
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnOk) {
//            callBack.onOk(DmShared.xNull(purchaseBillDirect.topControl.getDocId()));
            PurchaseBill_Mdl pbDm = new PurchaseBill_Mdl();
            pbDm.setSupplierBillNo(DmShared.xNull(txtSupplierBillNo.getText()).trim());
            pbDm.setSupplierBillDate(DmShared.dNull(txtSupplierBillDate.getText()));
            pbDm.setRemark(DmShared.xNull(txtRemark.getText()).trim());
            pbDm.setPaymentDueDate(DmShared.dNull(txtPaymentDueDate.getText()));
            callBack.onOk(pbDm);
            hide();
        } else if (event.getSource().equals(btnCancel)) {
            hide();
            callBack.onClose();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
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
    public Widget asWidget() {
        return this.popupPanel;
    }

    public InventoryUpdatePopup() {
        super();
        SrvAsync
                = new ServiceInvoker<PurchaseBill_SrvAsync>(GWT.create(PurchaseBill_Srv.class
                ), PurchaseBill.class
                        .getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        textHelpEvents();
        manageEvent();
    }

    private void manageEvent() {
        txtSupplierBillDate.addBlurHandler(this);
        txtPaymentDueDate.addBlurHandler(this);
    }

}
