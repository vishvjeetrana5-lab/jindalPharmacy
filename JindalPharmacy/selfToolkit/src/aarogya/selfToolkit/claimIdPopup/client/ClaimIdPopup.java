package aarogya.selfToolkit.claimIdPopup.client;

import aarogya.selfToolkit.claimIdPopup.shared.*;
import aarogya.selfToolkit.organizationPopup.shared.Organization_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;

public class ClaimIdPopup extends ClaimIdPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    public interface CallBack {

        public void onOk(Organization_Mdl organizationMdl);

        public void close();
    }

    private CallBack callBack;
    private final ServiceInvoker<ClaimIdPopup_SrvAsync> srvAsync;

    private String voucherDate = "", regDocId = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(Organization_Mdl organizationMdl, String vDate, String mode, String regDocId) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        reset();
        this.voucherDate = vDate;
        this.regDocId = regDocId;
        flwOk.add(btnOk);
        flwOk.setVisible(true);
        if (!DmShared.xNull(mode).trim().equalsIgnoreCase("Browse")) {
            flwOk.clear();
            flwOk.setVisible(false);
        }
        txtClaimId.setText(DmShared.xNull(organizationMdl.getClaimId()));
        txtClaimRemark.setText(DmShared.xNull(organizationMdl.getClaimRemark()));
        txtClaimDate.setText(DmShared.dNull(organizationMdl.getClaimDate()));

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtClaimId.setFocus(true);
            }
        });

    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtClaimDate)) {
            if (!DmShared.xNull(txtClaimDate.getText()).equalsIgnoreCase("")) {
                txtClaimDate.setText(DmClient.returnDate(txtClaimDate.getText()));
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            callBack.close();
            hide();
        } else if (event.getSource() == btnOk) {
            try {

                if (DmShared.xNull(txtClaimId.getText()).equalsIgnoreCase("")) {
                    throw new DmException(0, "Claim Id is mandatory.");
                }
                if (DmShared.xNull(txtClaimRemark.getText()).equalsIgnoreCase("")) {
                    throw new DmException(0, "Remark is mandatory.");
                }
                if (DmShared.dNull(txtClaimDate.getText()).equalsIgnoreCase("")) {
                    throw new DmException(0, "Date is mandatory.");
                }
                if (DmShared.convertDate(txtClaimDate.getText()).getTime() < DmShared.convertDate(voucherDate).getTime()) {
                    throw new DmException(0, "Claim Date must be greater or equal to voucher date.");
                }
                final Organization_Mdl mrTemp = new Organization_Mdl();
                mrTemp.setClaimId(DmShared.xNull(txtClaimId.getText()));
                mrTemp.setClaimDate(DmShared.dNull(txtClaimDate.getText()));
                mrTemp.setClaimRemark(DmShared.xNull(txtClaimRemark.getText()));
                srvAsync.invokeRPC().saveClaim(mrTemp, regDocId, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new DmException(3, caught.getMessage());
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            new DmAlertMessageBox(result.getData(), btnOk).show();
                            callBack.onOk(mrTemp);
                            hide();
                        } else {
                            throw new DmException(0, result.getErrorMessage());
                        }
                    }
                });
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), txtClaimId).show();
            }
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            callBack.close();
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

    public ClaimIdPopup() {
        super();
        srvAsync = new ServiceInvoker<ClaimIdPopup_SrvAsync>(GWT.create(ClaimIdPopup_Srv.class), ClaimIdPopup.class.getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtClaimDate.addBlurHandler(this);
    }

    private void reset() {
        txtClaimRemark.setText("");
        txtClaimId.setText("");
        txtClaimDate.setText("");
        voucherDate = "";
        regDocId = "";
    }

}
