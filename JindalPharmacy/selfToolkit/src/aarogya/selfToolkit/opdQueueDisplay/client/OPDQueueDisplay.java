package aarogya.selfToolkit.opdQueueDisplay.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.opdQueueDisplay.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public class OPDQueueDisplay extends OPDQueueDisplay_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    private final ServiceInvoker<OPDQueueDisplay_SrvAsync> srvAsync;

    public void show(String consultantCode, String vrDate) {
        if (!DmShared.dNull(vrDate).trim().equalsIgnoreCase("") && DmShared.iNull(consultantCode).longValue() > 0) {
            popupPanel.show();
            popupPanel.center();
            popupPanel.setGlassEnabled(true);
            txtHConsultant.setTag("");
            txtHConsultant.setText("");
            txtDate.setText(vrDate);
            txtHConsultant.setFocus(true);
            fillGrid(consultantCode, vrDate);
        } else {
            this.hide();
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void textHelpEvents() {
        txtHConsultant.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (DmShared.dNull(txtDate.getText()).trim().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Date is mandatory.", txtDate).show();
                    return;
                }
                srvAsync.invokeRPC(blnCallBackWait).getTxtHConsultant(helper, blnToEndOfFile, DmShared.dNull(txtDate.getText()), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsultant).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    fillGrid(t1Row.getSharedKeyFieldCode(), txtDate.getText());
                }
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
            this.hide();
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

    public OPDQueueDisplay() {
        super();
        srvAsync = new ServiceInvoker<OPDQueueDisplay_SrvAsync>(GWT.create(OPDQueueDisplay_Srv.class), OPDQueueDisplay.class.getName());
        btnCancel.addClickHandler(this);
        txtDate.addBlurHandler(this);
        popupPanel.addAttachHandler(this);
        textHelpEvents();
    }

    private void fillGrid(final String consultant, String vrDate) {
        if (DmShared.dNull(vrDate).trim().equalsIgnoreCase("") && DmShared.iNull(consultant).longValue() == 0) {
            new DmAlertMessageBox("Consultant and Date are mandatory.", txtHConsultant).show();
            return;
        }
        srvAsync.invokeRPC().getConsultationQueue(consultant, vrDate, new AsyncCallback<DmResult<List<GrdOPDQueue_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHConsultant).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdOPDQueue_Mdl>> result) {
                if (result.isStatus()) {
                    txtHConsultant.setTag(consultant);
                    txtHConsultant.setText(result.getValue1());
                    grdDisplay.getGrid().getStore().clear();
                    grdDisplay.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHConsultant).show();
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtDate)) {
            if (!DmShared.xNull(txtDate.getText()).trim().equalsIgnoreCase("")) {
                txtDate.setText(DmClient.returnDate(txtDate.getText()));
                fillGrid(txtHConsultant.getTag(), txtDate.getText());
            }
        }
    }
}
