package aarogya.master.organizationServiceMapping.client;

import aarogya.master.organizationServiceMapping.shared.*;
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
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;

import java.util.List;

public class SearchServicePopup extends SearchServicePopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    interface CallBack {

        public void onOk(String serviceCode);

        public void onClose();
    }

    private CallBack callBack;
    private String serviceCode = "";
    ServiceInvoker<OrganizationServiceMapping_SrvAsync> srvAsync;
    private OrganizationServiceMapping organizationServiceMapping;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(OrganizationServiceMapping organizationServiceMapping) {
        this.organizationServiceMapping = organizationServiceMapping;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtHServiceTest.setFocus(true);
        txtHServiceTest.setTag("");
        txtHServiceTest.setText("");
    }

    private void txtHelpEvents() {
        txtHServiceTest.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHServiceTest_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHServiceTest(DmShared.xNull(organizationServiceMapping.txtHEmpanelment.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHServiceTest_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceTest).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHServiceTest_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHServiceTest.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceTest).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHServiceTest_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    serviceCode = DmShared.xNull(t1Row.getSharedKeyFieldCode());
                } else {
                    serviceCode = "";
                }
            }
        });
    }

    private void manageEvent() {
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            this.callBack.onClose();
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
            if (DmShared.iNull(txtHServiceTest.getTag()).longValue() != 0) {
                callBack.onOk(serviceCode);
                hide(false);
            } else {
                new DmAlertMessageBox("Service / Investigation is mandatory.", txtHServiceTest).show();
            }
        }
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

    public SearchServicePopup() {
        super();
        toolContainer.setTitle(widgetName);
        srvAsync = new ServiceInvoker<OrganizationServiceMapping_SrvAsync>(GWT.create(OrganizationServiceMapping_Srv.class), OrganizationServiceMapping.class.getName());
        manageEvent();
        txtHelpEvents();
        popupPanel.addAttachHandler(this);
    }

}
