package aarogya.master.consultantLeave.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.consultantLeave.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.self.shared.*;
import java.util.HashMap;
import java.util.List;

public class ConsultantLeaveMaster extends ConsultantLeaveMaster_Dsg implements BlurHandler, ValueChangeHandler<Boolean> {

    private final ServiceInvoker<ConsultantLeaveMaster_SrvAsync> SrvAsync;
    private String consultantCode = "", consultantName = "";

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                manageDisplay();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay();
                if (!DmShared.xNull(consultantCode).equalsIgnoreCase("")) {
                    txtConsltName.setEnabled(false);
                    txtFdate.setFocus(true);
                } else {
                    txtHConsultantName.setEnabled(false);
                    txtFdate.setFocus(true);
                }

            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            topControlDelete(topControl.getDocId());
                        }
                    }
                }).show();
            }
        });

        topControl.addCardHandeller(new DmTopControl.eCard() {
            @Override
            public void cardClicked() {
                topControlCard();
            }
        });

        topControl.addFindHandeller(new DmTopControl.eFind() {
            @Override
            public void findClicked() {
                flwForm.remove(vrtInnerForm);
                flwForm.add(dmFind);
            }
        });

        topControl.addMoveRecHandeller(new DmTopControl.iMoveRec() {
            @Override
            public void moveRec(String strFetchCode) {
                topControlMoveRec(strFetchCode);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {

            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ConsultantLeaveMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtHConsultantName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsultantName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsultantName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsultantName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(consultantCode, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Find_Mdl tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);
                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    public ConsultantLeaveMaster(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(ConsultantLeaveMaster_Srv.class), ConsultantLeaveMaster.class.getName());
        if (paramHashMap != null) {
            consultantCode = (String) paramHashMap.get("consultantCode");
            consultantName = (String) paramHashMap.get("consultantName");
        }
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        setConsultantDiv();
        swi.setWidget(this);
        txtFdate.addBlurHandler(this);
        txtTdate.addBlurHandler(this);
        rbtnFullDay.addValueChangeHandler(this);
        rbtnHalfDay.addValueChangeHandler(this);
        rbtnFullDay.setEnabled(!topControl.getMode().equalsIgnoreCase("Browse"));
        rbtnHalfDay.setEnabled(!topControl.getMode().equalsIgnoreCase("Browse"));
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private ConsultantLeaveMaster_Mdl topControlSaveInitialize() {
        ConsultantLeaveMaster_Mdl mrTemp = new ConsultantLeaveMaster_Mdl();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        if (DmShared.xNull(consultantCode).equalsIgnoreCase("")) {
            mrTemp.setConsultantCode(DmShared.xNull(txtHConsultantName.getTag()));
        } else {
            mrTemp.setConsultantCode(DmShared.xNull(txtConsltName.getTag()));
        }
        mrTemp.setToDate(DmShared.xNull(txtTdate.getText()));
        mrTemp.setFromDate(DmShared.xNull(txtFdate.getText()));
        mrTemp.setToTime(DmShared.xNull(txtToTime.getText()));
        mrTemp.setFromTime(DmShared.xNull(txtFromTime.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setFullDay(rbtnFullDay.getValue());
        return mrTemp;
    }

    private void topControlSave() {
        ConsultantLeaveMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            String date = DmClient.getLoginDate();
//            ConsultantLeaveMaster_Vld.chkSave(mrTemp, date);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtFdate).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");
                        } else {
                            topControlMoveRec(topControl.getDocId());
                        }
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHConsultantName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtFdate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtTdate).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtTdate).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtFromTime).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtToTime).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtRemark).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtFdate).show();
        }
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(consultantCode, new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ConsultantLeaveMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ConsultantLeaveMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ConsultantLeaveMaster_Mdl> result) {
        ConsultantLeaveMaster_Mdl data;
        manageDisplay();
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtFdate.setText(data.getFromDate());
            txtTdate.setText(data.getToDate());
            txtFromTime.setText(data.getFromTime());
            txtToTime.setText(data.getToTime());
            txtRemark.setText(data.getRemark());
            rbtnFullDay.setValue(data.isFullDay());
            rbtnHalfDay.setValue(!data.isFullDay());
            if (DmShared.xNull(consultantCode).equalsIgnoreCase("")) {
                txtHConsultantName.setText(data.getConsultantName());
                txtHConsultantName.setTag(data.getConsultantCode());
            } else {
                txtConsltName.setText(data.getConsultantName());
                txtConsltName.setTag(data.getConsultantCode());
            }
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    clear();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        rbtnFullDay.setValue(true);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtFdate)) {
            txtFdate.setText(DmClient.returnDate(txtFdate.getText()));
            if (rbtnHalfDay.getValue()) {
                txtTdate.setText(DmShared.xNull(txtFdate.getText()));
            }
        }
        if (event.getSource().equals(txtTdate)) {
            txtTdate.setText(DmClient.returnDate(txtTdate.getText()));
        }
    }

    private void setConsultantDiv() {
        flwConsultant.clear();
        if (DmShared.xNull(consultantCode).equalsIgnoreCase("")) {
            flwConsultant.add(flwConsultantHelp);
            txtHConsultantName.setEnabled(false);
        } else {
            flwConsultant.add(flwConsultantTxt);

        }
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "LEAVE":
                    topControl.btnClick("New");
                    manageDisplay();
                    if (callback != null) {
                        rbtnFullDay.setEnabled(true);
                        rbtnHalfDay.setEnabled(true);
                        DmResult<Object> dmrTemp = new DmResult<>();
                        dmrTemp.setStatus(true);
                        callback.result(dmrTemp);
                    }
                    break;
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtFdate.setFocus(true);
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void manageDisplay() {
        if (!DmShared.xNull(consultantCode).equalsIgnoreCase("")) {
            txtConsltName.setTag(consultantCode);
            txtConsltName.setText(consultantName);
            txtConsltName.setEnabled(false);
            txtFdate.setFocus(true);
        } else {
            txtHConsultantName.setFocus(true);
        }
        rbtnFullDay.setEnabled(!topControl.getMode().equalsIgnoreCase("Browse"));
        rbtnHalfDay.setEnabled(!topControl.getMode().equalsIgnoreCase("Browse"));
        txtTdate.setEnabled(!rbtnHalfDay.getValue());
        txtToTime.setEnabled(rbtnHalfDay.getValue());
        txtFromTime.setEnabled(rbtnHalfDay.getValue());
    }

    @Override
    public void onValueChange(ValueChangeEvent<Boolean> event) {
        if (event.getSource().equals(rbtnHalfDay)) {
            txtTdate.setEnabled(!rbtnHalfDay.getValue());
            txtToTime.setEnabled(rbtnHalfDay.getValue());
            txtFromTime.setEnabled(rbtnHalfDay.getValue());
            txtTdate.setText(DmShared.xNull(txtFdate.getText()));
            txtFdate.setFocus(true);
        } else if (event.getSource().equals(rbtnFullDay)) {
            txtTdate.setEnabled(rbtnFullDay.getValue());
            txtToTime.setText("");
            txtFromTime.setText("");
            txtFromTime.setEnabled(!rbtnFullDay.getValue());
            txtToTime.setEnabled(!rbtnFullDay.getValue());
            txtFdate.setFocus(true);
        }
    }
}
