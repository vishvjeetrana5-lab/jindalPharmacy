package aarogya.selfToolkit.appointmentSearch.client;

import aarogya.selfToolkit.appointmentSearch.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class Setting extends Setting_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    interface CallBack {

        public void onOk(SearchValue_Mdl searchValue);

        public void onClose();
    }

    private CallBack callBack;
    private SearchValue_Mdl searchValue_Mdl;
    private boolean isLocationWise = false;
    private final ServiceInvoker<AppointmentSearch_SrvAsync> srvAsync;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(boolean isLocationWise, Widget wdg, SearchValue_Mdl OBJSearchValue) {
        flwTrParent.clear();
        this.isLocationWise = isLocationWise;
        if (isLocationWise) {
            flwTrParent.add(flwTrSitting);
        } else {
            flwTrParent.add(flwTrConsultant);
        }
        popupPanel.show();
        popupPanel.center();
        popupPanel.showRelativeTo(wdg);
        popupPanel.setGlassEnabled(true);
        searchValue_Mdl = OBJSearchValue;
        txtHConsultant.setTag(searchValue_Mdl.getConsultantTag());
        txtHConsultant.setText(searchValue_Mdl.getConsultantText());
        txtHSitting.setTag(searchValue_Mdl.getConsultantTag());
        txtHSitting.setText(searchValue_Mdl.getConsultantText());
        txtHConsultant.setFocus(true);
        flwOk.clear();
        flwOk.add(btnOk);
        flwOk.setVisible(true);
        onLoad();
    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            callBack.onClose();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            txtHConsultant.setFocus(true);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnOk) {
            if (chkPending.getValue()) {
                searchValue_Mdl.setIsPending("1");
            } else {
                searchValue_Mdl.setIsPending("0");
            }
            searchValue_Mdl.setConsultantText(isLocationWise ? DmShared.xNull(txtHSitting.getText()) : DmShared.xNull(txtHConsultant.getText()));
            searchValue_Mdl.setConsultantTag(isLocationWise ? DmShared.xNull(txtHSitting.getTag()) : DmShared.xNull(txtHConsultant.getTag()));
            callBack.onOk(searchValue_Mdl);
            hide(false);
        }
    }

    private void textHelpEvents() {
        txtHConsultant.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHConsultant(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            }
        });
        txtHSitting.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSitting(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSitting).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSitting.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSitting).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide(true);
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public Setting() {
        super();
        srvAsync = new ServiceInvoker<AppointmentSearch_SrvAsync>(GWT.create(AppointmentSearch_Srv.class), AppointmentSearch.class.getName());
        popupPanel.addAttachHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        textHelpEvents();
    }

    private void onLoad() {
        if (DmShared.iNull(searchValue_Mdl.getIsPending()).intValue() == 1) {
            chkPending.setValue(true);
        } else {
            chkPending.setValue(false);
        }
    }
}
