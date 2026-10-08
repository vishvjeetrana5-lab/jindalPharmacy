package aarogya.inventory.itemMaster.client;

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
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.inventory.itemMaster.shared.ItemMaster_Srv;
import aarogya.inventory.itemMaster.shared.ItemMaster_SrvAsync;

public class ApplicableBatchPopup extends ApplicableBatchPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    CallBack callBack;
    ServiceInvoker<ItemMaster_SrvAsync> SrvAsync;
    String applicableBatch;
    Boolean isEditable = false;

    private void textHelpEvents() {

        txtHApplicableBatch.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHApplicableBatch(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHApplicableBatch).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHApplicableBatch.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHApplicableBatch).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
    }

    public interface CallBack {

        public void onOk(String ApplicableBatch);

        public void onClose();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String applicableBatch, Boolean isEditable) {
        this.isEditable = isEditable;
        if (isEditable) {
            btnOk.setVisible(false);
        } else {
            btnOk.setVisible(true);
        }
        this.applicableBatch = applicableBatch;
        txtHApplicableBatch.setTag(applicableBatch);
        txtHApplicableBatch.setText(applicableBatch);
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnOk) {
            callBack.onOk(DmShared.xNull(txtHApplicableBatch.getTag()));
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

    public ApplicableBatchPopup() {
        super();
        SrvAsync = new ServiceInvoker<ItemMaster_SrvAsync>(GWT.create(ItemMaster_Srv.class), ItemMaster.class.getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        textHelpEvents();
    }

}
