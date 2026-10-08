package aarogya.selfToolkit.patientSchedulePopup.client;

import aarogya.selfToolkit.patientSearch.shared.*;
import aarogya.selfToolkit.patientSchedulePopup.shared.*;
import aarogya.selfToolkit.patientSearch.client.PatientSearch;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public final class PatientSchedulePopup extends PatientSchedulePopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyDownHandler {

    public interface CallBack {

        public void onOk(String scheduleDocId);

        public void onClose();
    }
    private final ServiceInvoker<PatientSchedulePopup_SrvAsync> SrvAsync;
    private final PatientSearch patientSearch = new PatientSearch();
    private CallBack callBack;

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnSearchPatient) {
            patientSearch.show(true, SearchValue_Mdl.FilterType.WITHOUT_EXPIRED);
        } else if (event.getSource() == btnOk) {
            saveConfirm();
            // save(saveInitilize());
        } else if (event.getSource() == btnCancel) {
            hide(true);
        }

    }

    private PatientSchedulePopup_Mdl saveInitilize() {
        PatientSchedulePopup_Mdl mrTemp = new PatientSchedulePopup_Mdl();

        mrTemp.setDocIdUHID(DmShared.iNull(txtUHID.getTag()).toString());
        mrTemp.setScheduleCode(DmShared.iNull(txtHSchedule.getTag()).toString());
        mrTemp.setScheduleName(DmShared.xNull(txtHSchedule.getText()));
        mrTemp.setScheduleDate(DmShared.xNull(txtScheduleDate.getText()));
        mrTemp.setVoucherDate(DmShared.xNull(txtVDate.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtVDate.getTag2nd()));
        return mrTemp;
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean withCloseEvent) {
        this.popupPanel.hide();
        if (withCloseEvent) {
            this.callBack.onClose();
        }
        clear();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            saveConfirm();
            //save(saveInitilize());
        }
    }

    private void saveConfirm() {

        new DmConfirmMessageBox("Do you want to save record?", new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    save(saveInitilize());
                }

            }
        }).show();

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtScheduleDate) {
            returnDate(txtScheduleDate);
        } else if (event.getSource() == txtUHID) {
            txtUHID.setText(txtUHID.getTag2nd());
        }
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvent() {
        patientSearch.addCallBack(new PatientSearch.CallBack() {

            @Override
            public void ok(String uhidRecId, String uhidDocId) {
                getUHIDDetail(uhidRecId, "");
            }

            @Override
            public void close() {
                txtHSchedule.setFocus(true);

            }
        });
        txtHSchedule.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSchedule(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSchedule).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSchedule.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSchedule).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    private void getUHIDDetail(String uhidRecId, String uhidDocId) {
        SrvAsync.invokeRPC().getUHIDDetails(uhidRecId, uhidDocId, new AsyncCallback<DmResult<PatientDetail_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                topControlSaveError(0, caught.getMessage());
            }

            @Override
            public void onSuccess(DmResult<PatientDetail_Mdl> result) {

                if (result.isStatus()) {
                    refresh();
                    setUHIDDetail(result.getData());
                    txtHSchedule.setFocus(true);
                } else {
                    refresh();
                    txtUHID.setFocus(true);
                    topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                }
            }
        });
    }

    private void refresh() {
        clear();
        getServerTime();
    }

    private void save(PatientSchedulePopup_Mdl mrTemp) {

        try {
            PatientSchedulePopup_Vld.chkSave(mrTemp, DmClient.getLoginDate());
            SrvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    topControlSaveError(0, caught.getMessage());
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        callBack.onOk(result.getData());
                        hide(false);
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void manageDisplay(boolean flag) {

        txtPatntName.setEnabled(false);
        txtGuardianName.setEnabled(false);
        txtMobile.setEnabled(false);
        txtAge.setEnabled(false);
        txtGender.setEnabled(false);
        txtVDate.setEnabled(false);
        txtHSchedule.setEnabled(flag);
        txtScheduleDate.setEnabled(flag);
        txtUHID.setEnabled(flag);
    }

    private void clear() {
        txtUHID.setText("");
        txtUHID.setTag("");
        txtUHID.setTag2nd("");
        txtPatntName.setText("");
        txtGuardianName.setText("");
        txtMobile.setText("");
        txtAge.setText("");
        txtGender.setText("");
        txtHSchedule.setTag("");
        txtHSchedule.setText("");
        txtHSchedule.setTag2nd("");
        txtScheduleDate.setText("");
    }

    private void manageEvent() {
        txtUHID.addBlurHandler(this);
        txtUHID.addKeyDownHandler(this);
        btnSearchPatient.addKeyDownHandler(this);
        txtScheduleDate.addBlurHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        btnSearchPatient.addClickHandler(this);
        manageDisplay(true);
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {

        if (event.getSource() == txtUHID) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!DmShared.xNull(txtUHID.getText()).trim().equalsIgnoreCase("")) {
                    getUHIDDetail(DmShared.xNull(txtUHID.getText()), "");
                } else {
                    btnSearchPatient.setFocus(true);
                }
            }
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtUHID).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHSchedule).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtScheduleDate).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtScheduleDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtUHID).show();
                break;
        }
    }

    private void setUHIDDetail(PatientDetail_Mdl data) {
        txtUHID.setTag(DmShared.iNull(data.getDocIdUHID()).toString());
        txtUHID.setText(DmShared.xNull(data.getRecIdUHID()));
        txtUHID.setTag2nd(DmShared.xNull(data.getRecIdUHID()));
        txtPatntName.setText(DmShared.xNull(data.getPatient()));
        txtAge.setText(DmShared.xNull(data.getDisplayDOB()));
        txtGender.setText(DmShared.xNull(data.getGender()));
        txtGuardianName.setText(DmShared.xNull(data.getGuardian()));
        txtMobile.setText(DmShared.xNull(data.getMobile()));
    }

    public PatientSchedulePopup() {
        super();
        SrvAsync = new ServiceInvoker<PatientSchedulePopup_SrvAsync>(GWT.create(PatientSchedulePopup_Srv.class), PatientSchedulePopup.class.getName());
        this.asWidget().setTitle(widgetName);

        manageEvent();
        defaultValue();
        textHelpEvent();
    }

    private void defaultValue() {
        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                topControlSaveError(0, caught.getMessage());
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {

                if (result.isStatus()) {
                    refresh();
                    txtVDate.setText(result.getData().getVoucherDate());
                    txtVDate.setTag2nd(result.getData().getVoucherTag());

                } else {

                    topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public void show(String uhid) {

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        flwPatientSearchParent.clear();

        if (DmShared.iNull(uhid).longValue() != 0) {
            txtUHID.setEnabled(false);
            getUHIDDetail("", uhid);

        } else {
            flwPatientSearchParent.add(flwPatientSearchChild);
            refresh();
            txtUHID.setEnabled(true);
            txtUHID.setFocus(true);

        }
    }

    private void getServerTime() {
        SrvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtScheduleDate).show();
            }

            @Override
            public void onSuccess(String[] result) {
                txtScheduleDate.setText(result[0]);
            }
        });
    }

    private void returnDate(DmTextBox txtBox) {
        if (!DmShared.xNull(txtBox.getText()).trim().equalsIgnoreCase("")) {
            txtBox.setText(DmClient.returnDate(txtBox.getText()));
        }
    }
}
