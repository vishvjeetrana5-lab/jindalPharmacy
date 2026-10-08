package aarogya.inventory.grn.client;

import aarogya.inventory.grn.shared.Grn_Mdl;
import aarogya.inventory.grn.shared.Grn_Srv;
import aarogya.inventory.grn.shared.Grn_SrvAsync;
import aarogya.inventory.grn.shared.TxtHTransporter_Mdl;
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
    ServiceInvoker<Grn_SrvAsync> SrvAsync;
    String applicableBatch;
    Boolean isEditable = false;
    Grn grn;
    Grn_Mdl pbDm;

    private void textHelpEvents() {

        txtHTransporter.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHTransporter_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTransporter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTransporter_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTransporter).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHTransporter_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHTransporter.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTransporter).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHTransporter_Mdl tRow) {
                if (blnStatus && tRow != null) {

                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
//        if (event.getSource() == txtPaymentDueDate) {
//            txtPaymentDueDate.setText(DmClient.returnDate(txtPaymentDueDate.getText()));
//        } else 
        if (event.getSource() == txtSupplierBillDate) {
            txtSupplierBillDate.setText(DmClient.returnDate(txtSupplierBillDate.getText()));
            if ((DmShared.convertDate(txtSupplierBillDate.getText()).getTime() > (DmShared.convertDate(DmShared.dNull(grn.txtVoucherDate.getText())).getTime()))) {
                new DmAlertMessageBox("Bill Date should be less than or equal to Voucher Date.", txtSupplierBillDate).show();
                return;
            }
        } else if (event.getSource() == txtEwayDate) {
            txtEwayDate.setText(DmClient.returnDate(txtEwayDate.getText()));
            if (!DmShared.xNull(txtEwayDate.getText()).equalsIgnoreCase("")) {
                if ((DmShared.convertDate(txtEwayDate.getText()).getTime() < (DmShared.convertDate(DmShared.dNull(grn.txtVoucherDate.getText())).getTime()))) {
                    new DmAlertMessageBox("E-Way Bill Date should be greater than Voucher Date.", txtEwayDate).show();
                    return;
                }
            }
//            if (!DmShared.xNull(txtEwayDate.getText()).equalsIgnoreCase("")) {
//                if ((DmShared.convertDate(txtEwayDate.getText()).getTime() > (DmShared.convertDate(DmShared.dNull(purchaseBillDirect.txtVoucherDate.getText())).getTime()))) {
//                    new DmAlertMessageBox("E-Way Bill Date should be less than or equal to Voucher Date.", txtEwayDate).show();
//                    return;
//                } else if ((DmShared.convertDate(txtEwayDate.getText()).getTime() < (DmShared.convertDate(DmShared.dNull(txtSupplierBillDate.getText())).getTime()))) {
//                    new DmAlertMessageBox("E-Way Bill Date should be greater than or equal to Bill Date.", txtEwayDate).show();
//                    return;
//                }
//            }
        } else if (event.getSource() == txtLRDate) {
            txtLRDate.setText(DmClient.returnDate(txtLRDate.getText()));
            if (!DmShared.xNull(txtLRDate.getText()).equalsIgnoreCase("")) {
                if ((DmShared.convertDate(txtLRDate.getText()).getTime() < (DmShared.convertDate(DmShared.dNull(grn.txtVoucherDate.getText())).getTime()))) {
                    new DmAlertMessageBox("LR Date should be greater than or equal to Voucher Date.", txtLRDate).show();
                    return;
                } 
            }
//            if (!DmShared.xNull(txtLRDate.getText()).equalsIgnoreCase("")) {
//                if ((DmShared.convertDate(txtLRDate.getText()).getTime() > (DmShared.convertDate(DmShared.dNull(purchaseBillDirect.txtVoucherDate.getText())).getTime()))) {
//                    new DmAlertMessageBox("LR Date should be less than or equal to Voucher Date.", txtLRDate).show();
//                    return;
//                } else if ((DmShared.convertDate(txtLRDate.getText()).getTime() < (DmShared.convertDate(DmShared.dNull(txtSupplierBillDate.getText())).getTime()))) {
//                    new DmAlertMessageBox("LR Date should be greater than or equal to Bill Date.", txtLRDate).show();
//                    return;
//                }
//            }
        }
    }

    public interface CallBack {

        public void onOk(Grn_Mdl pbDm);

        public void onClose();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(Grn_Mdl pbDm, Grn parent) {
        this.pbDm = pbDm;

        this.grn = parent;
        btnOk.setVisible(true);

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtSupplierBillNo.setText(DmShared.xNull(pbDm.getChallanNo()));
        txtSupplierBillDate.setText(pbDm.getChallanDate());
        txtHTransporter.setTag(pbDm.getTransporterCode());
        txtHTransporter.setTag2nd(pbDm.getTransporterCode());
        txtHTransporter.setText(pbDm.getTransporterName());
        txtEwayNo.setText(pbDm.getEwayNo());
        txtEwayDate.setText(pbDm.getEwayDate());
        txtLRNo.setText(pbDm.getLrNo());
        txtLRDate.setText(pbDm.getLrDate());
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
            Grn_Mdl pbDm = new Grn_Mdl();
            pbDm.setChallanNo(DmShared.xNull(txtSupplierBillNo.getText()).trim());
            pbDm.setChallanDate(DmShared.dNull(txtSupplierBillDate.getText()));
            pbDm.setTransporterCode(DmShared.xNull(txtHTransporter.getTag()));
            pbDm.setEwayNo(DmShared.xNull(txtEwayNo.getText()).trim());
            pbDm.setEwayDate(DmShared.dNull(txtEwayDate.getText()));
            pbDm.setLrNo(DmShared.xNull(txtLRNo.getText()).trim());
            pbDm.setLrDate(DmShared.dNull(txtLRDate.getText()));
            pbDm.setRemark(DmShared.xNull(txtRemark.getText()).trim());
//            pbDm.setChallanDate(DmShared.dNull(txtPaymentDueDate.getText()));
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
        SrvAsync = new ServiceInvoker<Grn_SrvAsync>(GWT.create(Grn_Srv.class), Grn.class.getName());
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
//        btnApprovedBy.addClickHandler(this);
//        btnAddOrder.addClickHandler(this);
//        flwForm.addAttachHandler(this);
        txtSupplierBillDate.addBlurHandler(this);
//        txtVoucherDate.addBlurHandler(this);
        txtEwayDate.addBlurHandler(this);
        txtLRDate.addBlurHandler(this);
//        txtEwayNo.addBlurHandler(this);
//        txtLRNo.addBlurHandler(this);
//        txtPaymentDueDate.addBlurHandler(this);
    }

}
