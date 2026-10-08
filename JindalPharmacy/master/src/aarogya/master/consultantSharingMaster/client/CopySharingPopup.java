package aarogya.master.consultantSharingMaster.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.consultantSharingMaster.shared.ConsultantSharingMaster_Srv;
import aarogya.master.consultantSharingMaster.shared.ConsultantSharingMaster_SrvAsync;
import aarogya.master.consultantSharingMaster.shared.TxtHCopyConsultant_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class CopySharingPopup extends CopySharingPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(String consultantCode, String applicableDate);

        public void onCancel();
    }

    CallBack callBack;
    private final ServiceInvoker<ConsultantSharingMaster_SrvAsync> SrvAsync;
    private String consultantCode;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        txtHConsultant.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCopyConsultant_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCopyConsultant(consultantCode, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCopyConsultant_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCopyConsultant_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsultant).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCopyConsultant_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHConsultant.setTag2nd(DmShared.dNull(t1Row.getApplicableDate()));
                } else {
                    txtHConsultant.setTag2nd("");
                }
            }
        });
    }

    public void show(String consultantCode) {
        this.consultantCode = consultantCode;
        txtHConsultant.setTag("");
        txtHConsultant.setText("");

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtHConsultant.setFocus(true);
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            callBack.onCancel();
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {
            if (DmShared.xNull(txtHConsultant.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Consultant is mandatory.", txtHConsultant).show();
                return;
            }

            callBack.onOk(DmShared.xNull(txtHConsultant.getTag()), DmShared.xNull(txtHConsultant.getTag2nd()));
            popupPanel.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            callBack.onCancel();
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

    public CopySharingPopup() {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(ConsultantSharingMaster_Srv.class), ConsultantSharingMaster.class.getName());
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
