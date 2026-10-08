package aarogya.selfToolkit.discountPopup.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.discountPopup.shared.*;
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
import dataman.dmBase.self.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class DiscountPopup extends DiscountPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onOk(DiscountPopup_Mdl discountPopup_Mdl);

        public void onClear();

        public void onCancel();
    }

    private CallBack callBack;
    private final ServiceInvoker<DiscountPopup_SrvAsync> SrvAsync;
    private String voucherType;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        txtHDisConsultant.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHConsultant(helper, blnToEndOfFile, DmShared.xNull(voucherType), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDisConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDisConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDisConsultant).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHGroupName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHGroupName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGroupName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGroupName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGroupName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHDisReason.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDiscountReason(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDisReason).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDisReason.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDisReason).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    public void show(DiscountPopup_Mdl mrTemp, String entryVoucherType) {
        if (mrTemp != null) {
            txtHGroupName.setTag(DmShared.xNull(mrTemp.getGroupCode()));
            txtHGroupName.setText(DmShared.xNull(mrTemp.getGroupName()));
            txtDiscount.setText(DmShared.xNull(mrTemp.getDiscount()));
            txtDiscountPer.setText(DmShared.xNull(mrTemp.getDiscountPer()));
            txtHDisConsultant.setTag(DmShared.xNull(mrTemp.getGivenByCode()));
            txtHDisConsultant.setText(DmShared.xNull(mrTemp.getGivenByName()));
            txtHDisReason.setTag(DmShared.xNull(mrTemp.getDiscountReasonCode()));
            txtHDisReason.setText(DmShared.xNull(mrTemp.getDiscountReasonName()));
        }
        this.voucherType = DmShared.xNull(entryVoucherType);
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtHGroupName.setFocus(true);
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            callBack.onCancel();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
            hide(true);
        } else if (event.getSource().equals(btnOk)) {
            if ((DmShared.vNull(txtDiscount.getText()).doubleValue() == 0) && (DmShared.vNull(txtDiscountPer.getText()).doubleValue() == 0)) {
                new DmAlertMessageBox("Discount is mandatory.", txtDiscountPer).show();
                return;
            }
            if (DmShared.xNull(txtHDisConsultant.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Given By is mandatory.", txtHDisConsultant).show();
                return;
            }
            if (DmShared.xNull(txtHDisReason.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Dis. Reason is mandatory.", txtHDisReason).show();
                return;
            }

            DiscountPopup_Mdl mrTemp = new DiscountPopup_Mdl();

            mrTemp.setGroupCode(DmShared.xNull(txtHGroupName.getTag()));
            mrTemp.setGroupName(DmShared.xNull(txtHGroupName.getText()));
            mrTemp.setGivenByCode(DmShared.xNull(txtHDisConsultant.getTag()));
            mrTemp.setGivenByName(DmShared.xNull(txtHDisConsultant.getText()));
            mrTemp.setDiscountReasonCode(DmShared.xNull(txtHDisReason.getTag()));
            mrTemp.setDiscountReasonName(DmShared.xNull(txtHDisReason.getText()));
            mrTemp.setDiscount(DmClient.vNull(txtDiscount.getText(), "0.00"));
            mrTemp.setDiscountPer(DmClient.vNull(txtDiscountPer.getText(), "0.00"));
            if ((DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0)) {
                mrTemp.setDiscount("");
            }
            callBack.onOk(mrTemp);
            hide(false);
        } else if (event.getSource().equals(btnClear)) {
            new DmConfirmMessageBox("Are you sure to clear all discount?", new DialogHideEvent.DialogHideHandler() {
                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        hide(false);
                        callBack.onClear();
                    }
                }
            }).show();
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

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public DiscountPopup() {
        super();
        SrvAsync = new ServiceInvoker<DiscountPopup_SrvAsync>(GWT.create(DiscountPopup_Srv.class), DiscountPopup.class.getName());
        btnClear.addClickHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        textHelpEvents();
    }

}
