package aarogya.pharmacy.purchaseBillDirect.client;

import aarogya.pharmacy.purchaseBillDirect.shared.PurchaseBillDirect_Mdl;
import aarogya.pharmacy.purchaseBillDirect.shared.PurchaseBillDirect_Srv;
import aarogya.pharmacy.purchaseBillDirect.shared.PurchaseBillDirect_SrvAsync;
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
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public class PharmacyUpdatePopup extends PharmacyUpdatePopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    CallBack callBack;
    ServiceInvoker<PurchaseBillDirect_SrvAsync> SrvAsync;
    String applicableBatch;
    Boolean isEditable = false;
    PurchaseBillDirect purchaseBillDirect;
    PurchaseBillDirect_Mdl pbDm;

    private void textHelpEvents() {

        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtPaymentDueDate) {
            txtPaymentDueDate.setText(DmClient.returnDate(txtPaymentDueDate.getText()));
        } else if (event.getSource() == txtSupplierBillDate) {
            txtSupplierBillDate.setText(DmClient.returnDate(txtSupplierBillDate.getText()));
            if ((DmShared.convertDate(txtSupplierBillDate.getText()).getTime() > (DmShared.convertDate(DmShared.dNull(purchaseBillDirect.txtVoucherDate.getText())).getTime()))) {
                new DmAlertMessageBox("Bill Date should be less than or equal to Voucher Date.", txtSupplierBillDate).show();
                return;
            }
        }
    }

    public interface CallBack {

        public void onOk(PurchaseBillDirect_Mdl pbDm);

        public void onClose();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(PurchaseBillDirect_Mdl pbDm, PurchaseBillDirect parent) {
        this.pbDm = pbDm;
        this.purchaseBillDirect = parent;
        btnOk.setVisible(true);
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtSupplierBillNo.setText(DmShared.xNull(pbDm.getSupplierBillNo()));
        txtSupplierBillDate.setText(pbDm.getSupplierBillDate());
        txtHDepartment.setTag(pbDm.getDepartmentTag());
        txtHDepartment.setTag2nd(pbDm.getDepartmentTag());
        txtHDepartment.setText(pbDm.getDepartmentText());
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
            PurchaseBillDirect_Mdl pbDm = new PurchaseBillDirect_Mdl();
            pbDm.setSupplierBillNo(DmShared.xNull(txtSupplierBillNo.getText()).trim());
            pbDm.setSupplierBillDate(DmShared.dNull(txtSupplierBillDate.getText()));
            pbDm.setDepartmentTag(DmShared.xNull(txtHDepartment.getTag()));
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

    public PharmacyUpdatePopup() {
        super();
        SrvAsync = new ServiceInvoker<PurchaseBillDirect_SrvAsync>(GWT.create(PurchaseBillDirect_Srv.class), PurchaseBillDirect.class.getName());
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
