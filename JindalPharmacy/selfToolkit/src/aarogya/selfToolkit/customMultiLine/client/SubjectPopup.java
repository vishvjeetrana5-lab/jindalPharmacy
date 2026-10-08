package aarogya.selfToolkit.customMultiLine.client;

import aarogya.selfToolkit.customMultiLine.shared.CustomMultiLine_Mdl;
import aarogya.selfToolkit.customMultiLine.shared.CustomMultiLine_Srv;
import aarogya.selfToolkit.customMultiLine.shared.CustomMultiLine_SrvAsync;
import aarogya.selfToolkit.customMultiLine.shared.CustomMultiLine_Vld;
import aarogya.selfToolkit.patientNameEditing.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.CustomMultiLineNature;
import com.google.gwt.core.shared.GWT;
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
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public class SubjectPopup extends SubjectPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void refresh();

    }

    private CallBack callBack;
    private final ServiceInvoker<CustomMultiLine_SrvAsync> srvAsync;
    private DmTextBox txtRemark = new DmTextBox(5000, false), txtConsultant = new DmTextBox(50, false);

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(CustomMultiLine_Mdl mrTemp, Widget wdg) {
        popupPanel.showRelativeTo(wdg);
        popupPanel.setGlassEnabled(true);
        clear();
        txtRemark = new DmTextBox();
        txtRemark.setText(mrTemp.getRemark());

        txtConsultant = new DmTextBox();
        txtConsultant.setTag(mrTemp.getConsultantCode());
        txtConsultant.setTag2nd(mrTemp.getNature().getId());

        txtSubject.setFocus(true);
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            this.callBack.refresh();
        }
    }

    private void textHelpEvents() {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnOk) {
            saveRecord();
            hide(false);
        }
    }

    private CustomMultiLine_Mdl saveInitialize() {
        CustomMultiLine_Mdl mrTemp = new CustomMultiLine_Mdl();
        mrTemp.setConsultantCode(DmShared.xNull(txtConsultant.getTag()));
        mrTemp.setNature(CustomMultiLineNature.getKeyOf(DmShared.xNull(txtConsultant.getTag2nd())));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setSubject(DmShared.xNull(txtSubject.getText()));
        return mrTemp;
    }

    private void saveRecord() {
        CustomMultiLine_Mdl mrTemp;
        try {
            mrTemp = saveInitialize();
            CustomMultiLine_Vld.chkSave(mrTemp);
            srvAsync.invokeRPC().addFavorite(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    saveError(0, caught.getMessage());
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        hide(true);
                    } else {
                        saveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });

        } catch (DmException e) {
            saveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void saveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, btnOk).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtSubject).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtSubject).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtSubject).show();
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

    public SubjectPopup() {
        super();
        srvAsync = new ServiceInvoker<CustomMultiLine_SrvAsync>(GWT.create(CustomMultiLine_Srv.class), CustomMultiLine.class.getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        textHelpEvents();
    }

    private void clear() {
        txtConsultant.setTag("");
        txtConsultant.setTag2nd("");
        txtConsultant.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");
        txtRemark.setText("");
        txtSubject.setTag("");
        txtSubject.setTag2nd("");
        txtSubject.setText("");
    }

}
