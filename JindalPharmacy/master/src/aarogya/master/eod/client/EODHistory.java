package aarogya.master.eod.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.eod.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public class EODHistory extends EODHistory_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onClose();
    }
    private CallBack callBack;
    private ServiceInvoker<EOD_SrvAsync> srvAsync;
    private EOD eod;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void historyGridEvents() {
        grdEODHistory.addCallBack(new DmGridPaging.CallBack<String>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, String param) {
                srvAsync.invokeRPC(true).getEODHistory(eod.userPermissionMdl, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdProcessDetail_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdProcessDetail_Mdl>> result) {
                        if (result.isStatus()) {
                            grdEODHistory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public String getParam() {
                return "";
            }

        });
    }

    public void show(EOD eod) {
        this.eod = eod;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        grdEODHistory.getDmGrid().getGrid().getStore().clear();
        reQuery();
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean isCallbackNeeded) {
        this.popupPanel.hide();
        if (isCallbackNeeded) {
            callBack.onClose();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide();
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

    public EODHistory() {
        super();
        toolContainer.setTitle(widgetName);
        srvAsync = new ServiceInvoker<>(GWT.create(EOD_Srv.class), EOD.class.getName());
        popupPanel.addAttachHandler(this);
        btnClose.addClickHandler(this);
        historyGridEvents();
    }

    private void reQuery() {
        grdEODHistory.reQuery();
    }

}
