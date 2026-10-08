package aarogya.pharmacy.d365StockRecive.client;

import aarogya.pharmacy.d365StockRecive.shared.GRN_Mdl;
import aarogya.pharmacy.d365StockRecive.shared.GRN_Srv;
import aarogya.pharmacy.d365StockRecive.shared.GRN_SrvAsync;
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
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;

public class PharmacyUpdatePopup extends PharmacyUpdatePopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    CallBack callBack;
    ServiceInvoker<GRN_SrvAsync> SrvAsync;
    String applicableBatch;
    Boolean isEditable = false;
    GRN grn;
    GRN_Mdl pbDm;

    private void textHelpEvents() {

        
    }

    @Override
    public void onBlur(BlurEvent event) {

        if (event.getSource() == txtSupplierBillDate) {
            txtSupplierBillDate.setText(DmClient.returnDate(txtSupplierBillDate.getText()));
            if ((DmShared.convertDate(txtSupplierBillDate.getText()).getTime() > (DmShared.convertDate(DmShared.dNull(grn.txtVoucherDate.getText())).getTime()))) {
                new DmAlertMessageBox("Bill Date should be less than or equal to Voucher Date.", txtSupplierBillDate).show();
                return;
            }
        }
    }

    public interface CallBack {

        public void onOk(GRN_Mdl pbDm);

        public void onClose();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(GRN_Mdl pbDm, GRN parent) {
        this.pbDm = pbDm;
        this.grn = parent;
        btnOk.setVisible(true);
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtSupplierBillNo.setText(DmShared.xNull(pbDm.getChallanNo()));
        txtSupplierBillDate.setText(pbDm.getChallanDate());
        txtRemark.setText(pbDm.getRemark());
        txtSupplierBillNo.setFocus(true);
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnOk) {
            GRN_Mdl pbDm = new GRN_Mdl();
            pbDm.setChallanNo(DmShared.xNull(txtSupplierBillNo.getText()).trim());
            pbDm.setChallanDate(DmShared.dNull(txtSupplierBillDate.getText()));
            pbDm.setRemark(DmShared.xNull(txtRemark.getText()).trim());
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

    public PharmacyUpdatePopup() {
        super();
        SrvAsync = new ServiceInvoker<GRN_SrvAsync>(GWT.create(GRN_Srv.class), GRN.class.getName());
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
    }

}
