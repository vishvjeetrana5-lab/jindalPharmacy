package dataman.payroll.attendenceEntryManual.client;

import dataman.payroll.attendenceEntryManual.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import java.util.List;
import dataman.dmToolkit.walkThrough.client.WalkThrough;

import dataman.payroll.self.shared.PREnum;

import java.util.HashMap;

public class AttendenceEntryManual extends AttendenceEntryManual_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents, BlurHandler {

    ServiceInvoker<AttendenceEntryManual_SrvAsync> SrvAsync;
    Default_Mdl defaultMdl = new Default_Mdl();
    WalkThrough walkThroughPBill;
    WalkThrough walkThroughPBillDirect;
    String category = "";
    SetWidgetInvoker swi;

    private void textHelpEvents() {
        txtHEmployee.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<Employee_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEmployee(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Employee_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEmployee).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Employee_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHEmployee.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEmployee).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Employee_Mdl t1Row) {
                txtDepartment.setText(DmShared.xNull(t1Row.getDepartment()));
                txtDepartment.setTag(DmShared.xNull(t1Row.getDepartmentCode()));
                txtFromTime.setText(DmShared.xNull(t1Row.getFromTime()));
                txtToTime.setText(DmShared.xNull(t1Row.getToTime()));
                AttendenceEntryManual_Mdl mrTemp = new AttendenceEntryManual_Mdl();
                mrTemp.setEmployeeName(txtHEmployee.getText());
                mrTemp.setEmployeeCode(txtHEmployee.getTag());
                getDuration(txtFromDate.getText() + " " + txtFromTime.getText(), txtToDate.getText() + " " + txtToTime.getText());
            }
        });
        txtHAttendence.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<AttendenceTextHelp_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAttendence(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<AttendenceTextHelp_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAttendence).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<AttendenceTextHelp_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHAttendence.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAttendence).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, AttendenceTextHelp_Mdl t1Row) {
                txtHAttendence.setTag2nd(t1Row.getShortName());
                txtAttendanceDetail.setText(t1Row.getDays());
                txtAttendanceDetail.setTag(t1Row.getPlusMinus());
            }
        });
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtHEmployee.setFocus(true);
                onAdd();
                getCurrentEntryNo();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHEmployee.setFocus(true);
                manageDisplay();
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AttendenceEntryManual.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void gridEvents() {

    }

    private void clear() {

        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        lblCurrentEntryNo.setText("");
        lblLeaveStatus.setText("");
    }

    private AttendenceEntryManual_Mdl topControlSaveInitialize() {
        AttendenceEntryManual_Mdl mrTemp = new AttendenceEntryManual_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setEmployeeName(DmShared.xNull(txtHEmployee.getText()));
        mrTemp.setEmployeeCode(DmShared.xNull(txtHEmployee.getTag()));
        mrTemp.setEntryDate(DmShared.xNull(txtAttendanceDate.getText()));
        mrTemp.setAttendanceName(DmShared.xNull(txtHAttendence.getText()));
        mrTemp.setAttendanceCode(DmShared.xNull(txtHAttendence.getTag()));
        mrTemp.setFromDate(DmShared.xNull(txtFromDate.getText()));
        mrTemp.setFromTime(DmShared.xNull(txtFromTime.getText()));
        mrTemp.setToDate(DmShared.xNull(txtToDate.getText()));
        mrTemp.setToTime(DmShared.xNull(txtToTime.getText()));
        mrTemp.setDuration(DmShared.xNull(txtDuration.getTag()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setDays(DmShared.xNull(txtAttendanceDetail.getText()));
        mrTemp.setPlusMinus(DmShared.xNull(txtAttendanceDetail.getTag()));

        return mrTemp;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHEmployee).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtFromDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtToDate).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHAttendence).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtFromTime).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtToTime).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHEmployee).show();
        }
    }

    private void topControlSave() {
        AttendenceEntryManual_Mdl mrTemp;
        if (!DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("") && !DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
            if ((DmShared.convertDate(txtToDate.getText()).getTime() < (DmShared.convertDate(txtFromDate.getText())).getTime())) {
                new DmAlertMessageBox("From Date should be less than or equal to To Date.", txtFromDate).show();
                return;
            }
        }

        try {
            mrTemp = topControlSaveInitialize();
            SrvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHEmployee).show();
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

    private void manageDisplay() {
        //       flwButtonFillDetails.clear();
//           if(topControl.getMode().trim().equalsIgnoreCase("New")) {
//           flwButtonFillDetails.add(btnFillDetails);
//           }
        txtDepartment.setEnabled(false);
        txtAvailable.setEnabled(false);
        txtDuration.setEnabled(false);
        txtAttendanceDate.setEnabled(false);
        txtEntryNo.setEnabled(false);

    }

    private void getDuration(String fromDateTime, String toDateTime) {
        if (!DmShared.xNull(txtToDate.getText()).trim().equalsIgnoreCase("")
                && !DmShared.xNull(txtFromTime.getText()).trim().equalsIgnoreCase("")
                && !DmShared.xNull(txtToDate.getText()).trim().equalsIgnoreCase("")
                && !DmShared.xNull(txtToTime.getText()).trim().equalsIgnoreCase("")) {
            SrvAsync.invokeRPC().getDuration(fromDateTime.replace(".", ":"), toDateTime.replace(".", ":"), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        txtDuration.setTag(result.getData());
                        txtDuration.setText(DmShared.vNull(DmClient.mntsToHrs(DmShared.iNull(txtDuration.getTag()).intValue())).toString());
                    }
                }
            });
        }
    }

    private void getCurrentEntryNo() {

        SrvAsync.invokeRPC().getCurrentEntryNo(new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    if (!DmShared.xNull(result.getData()).equalsIgnoreCase("")) {
                        //  lblCurrentEntryNo.setText(DmShared.xNull(result.getData()));

                    }
                }

            }
        });

    }

    private void returnDate(DmTextBox txtBox) {
        if (!DmShared.xNull(txtBox.getText()).trim().equalsIgnoreCase("")) {
            txtBox.setText(DmClient.returnDate(txtBox.getText()));
        }
    }

    private void onAdd() {
        clear();
        manageDisplay();
        getServerTime();
        txtHAttendence.setText(defaultMdl.getAttendanceName());
        txtHAttendence.setTag(defaultMdl.getAttendanceCode());
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

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<AttendenceEntryManual_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<AttendenceEntryManual_Mdl> result) {
                lblLeaveStatus.setText("");
                topControlMoveRecSuccess(result);
                lblCurrentEntryNo.setText("");

            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<AttendenceEntryManual_Mdl> result) {
        manageDisplay();
        AttendenceEntryManual_Mdl data;
        if (result.isStatus()) {

            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtHEmployee.setText(DmShared.xNull(data.getEmployeeName()));
            txtHEmployee.setTag(DmShared.xNull(data.getEmployeeCode()));
            txtDepartment.setText(DmShared.xNull(data.getDepartmentName()));
            txtDepartment.setTag(DmShared.xNull(data.getDepartmentCode()));
            txtAttendanceDate.setText(DmShared.dNull(data.getEntryDate()));
            txtHAttendence.setText(DmShared.xNull(data.getAttendanceName()));
            txtHAttendence.setTag(DmShared.xNull(data.getAttendanceCode()));
            txtHAttendence.setTag2nd(DmShared.xNull(data.getAttendanceShortName()));
            txtFromDate.setText(DmShared.dNull(data.getFromDate()));
            txtFromTime.setText(DmShared.xNull(data.getFromTime()));
            txtToDate.setText(DmShared.dNull(data.getToDate()));
            txtToTime.setText(DmShared.xNull(data.getToTime()));
            txtDuration.setTag(DmShared.xNull(data.getDuration()));
            txtDuration.setText(DmShared.vNull(DmClient.mntsToHrs(DmShared.iNull(txtDuration.getTag()).intValue())).toString());
            txtRemark.setText(DmShared.xNull(data.getRemark()));
            txtEntryNo.setText(DmShared.iNull(data.getRecId().getCounter()).toString());
            txtAttendanceDetail.setText(DmShared.xNull(data.getDays()));
            txtAttendanceDetail.setTag(DmShared.xNull(data.getPlusMinus()));
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AttendenceEntryManual.this);
                }
            }
        }).show();
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    private void manageEvents() {
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
        txtFromTime.addBlurHandler(this);
        txtToTime.addBlurHandler(this);

        txtFromDate.addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    txtToDate.setText(txtFromDate.getText());
                }
            }
        }, KeyDownEvent.getType());

        txtToTime.addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    getDuration(txtFromDate.getText() + " " + txtFromTime.getText(), txtToDate.getText() + " " + txtToTime.getText());
                }
            }
        }, KeyDownEvent.getType());
    }

    public AttendenceEntryManual(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("E", "*"));
        SrvAsync
                = new ServiceInvoker<AttendenceEntryManual_SrvAsync>(GWT.create(AttendenceEntryManual_Srv.class
                ), AttendenceEntryManual.class
                        .getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        flwForm.addAttachHandler(this);
        txtRemark.addBlurHandler(this);
        btnApprovedBy.addClickHandler(this);
        textHelpEvents();
        findEvents();
        swi.setWidget(this);
        manageEvents();
        iniDefaultValue(swi);
    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<Default_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHAttendence).show();
            }

            @Override
            public void onSuccess(DmResult<Default_Mdl> result) {
                if (result.isStatus()) {
                    defaultMdl = result.getData();

                    //swi.setWidget(AttendenceEntryManual.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHAttendence).show();
                }
            }
        });
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
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
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void getServerTime() {
        SrvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtAttendanceDate).show();
            }

            @Override
            public void onSuccess(String[] result) {
                if (DmShared.dNull(txtAttendanceDate.getText()).equalsIgnoreCase("")) {
                    txtAttendanceDate.setText(result[0]);
                    txtFromDate.setText(result[0]);
                    txtToDate.setText(result[0]);
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {

        if (event.getSource().equals(txtAttendanceDate)) {
            txtAttendanceDate.setText(DmClient.returnDate(txtAttendanceDate.getText()));
        } else if (event.getSource() == txtFromDate) {
            returnDate(txtFromDate);
        } else if (event.getSource() == txtToDate) {
            returnDate(txtToDate);
        } else if (event.getSource() == txtFromTime || event.getSource() == txtToTime) {
            getDuration(txtFromDate.getText() + " " + txtFromTime.getText(), txtToDate.getText() + " " + txtToTime.getText());
        } else if (event.getSource().equals(txtRemark)) {
//            if (!DmShared.xNull(txtToDate.getText()).trim().equalsIgnoreCase("") 
//                    && !DmShared.xNull(txtFromTime.getText()).trim().equalsIgnoreCase("")
//                    && !DmShared.xNull(txtToDate.getText()).trim().equalsIgnoreCase("") 
//                    && !DmShared.xNull(txtToTime.getText()).trim().equalsIgnoreCase("")) {
            getDuration(txtFromDate.getText() + " " + txtFromTime.getText(), txtToDate.getText() + " " + txtToTime.getText());
//            }
        }

    }

    @Override
    public void onClick(ClickEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
