package aarogya.master.packageDetail.client;

import aarogya.master.packageDetail.shared.PackageDetail_Srv;
import aarogya.master.packageDetail.shared.PackageDetail_SrvAsync;
import aarogya.master.packageDetail.shared.TxtHPackage_Mdl;
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
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class PackageApplicablePopup extends PackageApplicablePopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    public interface CallBack {

        public void onClose();
    }
    private CallBack callBack;
    ServiceInvoker<PackageDetail_SrvAsync> srvAsync;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        txtHPackage.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHPackage_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPackage(true, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHPackage_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPackage).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHPackage_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPackage.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPackage).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHPackage_Mdl t1Row) {
            }
        });
    }

    public void show(String fromDate, String toDate, String code, String packageName) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        txtFromDate.setText(DmShared.dNull(fromDate));
        txtToDate.setText(DmShared.dNull(toDate));
        txtHPackage.setTag(DmShared.xNull(code));
        txtHPackage.setText(DmShared.xNull(packageName));

        txtFromDate.setFocus(true);
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            callBack.onClose();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnCopyPackage) {
            onCopyPackage();
        }
    }

    private void onCopyPackage() {
        if (DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("From Date is mandatory.", txtFromDate).show();
            return;
        }
        if (DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("To Date is mandatory.", txtToDate).show();
            return;
        }
        if (!DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("") && !DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
            if ((DmShared.convertDate(txtToDate.getText()).getTime() < (DmShared.convertDate(txtFromDate.getText())).getTime())) {
                new DmAlertMessageBox("From Date should be less than or equal to To Date.", txtFromDate).show();
                return;
            }
        }
        if (DmShared.iNull(txtHPackage.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Package is mandatory.", txtHPackage).show();
            return;
        }

        saveRecord();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            onCopyPackage();
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
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtFromDate)) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        }
        if (event.getSource().equals(txtToDate)) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public PackageApplicablePopup() {
        super();
        srvAsync = new ServiceInvoker<PackageDetail_SrvAsync>(GWT.create(PackageDetail_Srv.class), PackageDetail.class.getName());
        textHelpEvents();
        manageEvent();
    }

    private void manageEvent() {
        btnCancel.addClickHandler(this);
        btnCopyPackage.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
    }

    private void saveRecord() {
        try {
            srvAsync.invokeRPC().saveApplicable(DmShared.xNull(txtHPackage.getTag()), DmShared.dNull(txtFromDate.getText()), DmShared.dNull(txtToDate.getText()), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtFromDate).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        hide(true);
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtFromDate).show();
                    }
                }
            });

        } catch (DmException e) {
            new DmAlertMessageBox(e.getMessage(), txtFromDate).show();
        }
    }

}
