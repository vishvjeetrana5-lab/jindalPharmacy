package aarogya.master.servicePriceList.client;

import aarogya.master.servicePriceList.shared.SearchValue_Mdl;
import aarogya.master.servicePriceList.shared.ServicePriceList_Srv;
import aarogya.master.servicePriceList.shared.ServicePriceList_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ApplyDiscountPopup extends ApplyDiscountPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    interface CallBack {

        public void refresh(boolean callReQuery);
    }
    private CallBack callBack;
    private final ServiceInvoker<ServicePriceList_SrvAsync> srvAsync;
    private ServicePriceList clsParent;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(ServicePriceList organizationMaster) {
        popupPanel.show();
        popupPanel.showRelativeTo(organizationMaster.txtName);
        popupPanel.setGlassEnabled(true);
        clsParent = organizationMaster;
        txtDiscountPer.setFocus(true);
        txtDiscountPer.setText("0.00");
    }

    private void manageEvent() {
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            this.callBack.refresh(false);
        }
    }

    public void hide() {
        hide(false);
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide(true);
        } else if (event.getSource() == btnOk) {
            if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 100) {
                new DmAlertMessageBox("Discount % should not be greater than 100%.", txtDiscountPer).show();
                return;
            }
            if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0) {
                applyDiscount();
            } else {
                new DmAlertMessageBox("Discount is mandatory.", txtDiscountPer).show();
                return;
            }

        }
    }

    private void applyDiscount() {
        SearchValue_Mdl param = clsParent.setParam();
        srvAsync.invokeRPC().applyDiscountPercent(DmShared.vNull(txtDiscountPer.getText()).doubleValue(), param, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), btnOk).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    callBack.refresh(true);
                    hide(false);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), btnOk).show();
                }
            }
        });
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
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

    public ApplyDiscountPopup() {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(ServicePriceList_Srv.class), ServicePriceList.class.getName());
        toolContainer.setTitle(widgetName);
        manageEvent();
    }

}
