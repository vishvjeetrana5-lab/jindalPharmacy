package dataman.payroll.leaveEntry.client;

import dataman.payroll.leaveEntry.client.*;
import dataman.payroll.leaveEntry.shared.*;
import dataman.payroll.leaveEntry.shared.LeaveEntry_Srv;
import dataman.payroll.leaveEntry.shared.LeaveEntry_SrvAsync;
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

public class LeaveEntry extends LeaveEntry_Dsg implements IsWidget, ClickHandler, BlurHandler {
//DmRootEvents, AttachEvent.Handler, 

    ServiceInvoker<LeaveEntry_SrvAsync> SrvAsync;

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
                LeaveEntry_Mdl mrTemp = new LeaveEntry_Mdl();
                mrTemp.setEmployeeName(txtHEmployee.getText());
                mrTemp.setEmployeeCode(txtHEmployee.getTag());
                getEmployeeCurrentShiftTime(mrTemp);
//                fillLeaveStatusGrd(mrTemp);
//                getLeaveHistory(mrTemp);
            }
        });
        txtHLeave.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<LeaveTextHelp_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHLeave(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<LeaveTextHelp_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLeave).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<LeaveTextHelp_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHLeave.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLeave).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, LeaveTextHelp_Mdl t1Row) {
                txtHLeave.setTag2nd(t1Row.getShortName());
                txtLeaveDetail.setText(t1Row.getDays());
                txtLeaveDetail.setTag(t1Row.getPlusMinus());
                LeaveEntry_Mdl mrTemp = new LeaveEntry_Mdl();
                mrTemp.setEmployeeName(txtHEmployee.getText());
                mrTemp.setEmployeeCode(txtHEmployee.getTag());
                mrTemp.setLeaveName(txtHLeave.getText());
                mrTemp.setLeaveCode(txtHLeave.getTag2nd());
                getAvailableLeave(mrTemp);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(LeaveEntry.this);
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

    private LeaveEntry_Mdl topControlSaveInitialize() {
        LeaveEntry_Mdl mrTemp = new LeaveEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());

        }
        mrTemp.setEmployeeName(DmShared.xNull(txtHEmployee.getText()));
        mrTemp.setEmployeeCode(DmShared.xNull(txtHEmployee.getTag()));
        mrTemp.setApplyDate(DmShared.xNull(txtApplyDate.getText()));
        mrTemp.setLeaveName(DmShared.xNull(txtHLeave.getText()));
        mrTemp.setLeaveCode(DmShared.xNull(txtHLeave.getTag()));
        mrTemp.setFromDate(DmShared.xNull(txtFromDate.getText()));
        mrTemp.setFromTime(DmShared.xNull(txtFromTime.getText()));
        mrTemp.setToDate(DmShared.xNull(txtToDate.getText()));
        mrTemp.setToTime(DmShared.xNull(txtToTime.getText()));
        mrTemp.setDuration(DmShared.xNull(txtDuration.getText()));
        mrTemp.setDays(DmShared.xNull(txtLeaveDetail.getText()));
        mrTemp.setPlusMinus(DmShared.xNull(txtLeaveDetail.getTag()));

        mrTemp.setReason(DmShared.xNull(txtReason.getText()));
        mrTemp.setAvailableLeave(DmShared.xNull(txtAvailable.getText()));
        mrTemp.setCurrentDate(DmClient.getLoginDate());

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
                new DmAlertMessageBox(errorMessage, txtReason).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHLeave).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHEmployee).show();
        }
    }

    private void topControlSave() {
        LeaveEntry_Mdl mrTemp;
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
        txtDepartment.setEnabled(false);
        txtAvailable.setEnabled(false);
        txtDuration.setEnabled(false);
        txtApplyDate.setEnabled(false);
        txtEntryNo.setEnabled(false);
        txtFromTime.setEnabled(false);
        txtToTime.setEnabled(false);
    }

    private void getDuration(String fromDate, String toDate) {

        SrvAsync.invokeRPC().getDuration(fromDate, toDate, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    txtDuration.setText(result.getData());
                    if (!txtAvailable.getText().equalsIgnoreCase("")) {
                        if (Integer.valueOf(txtDuration.getText()) > Integer.valueOf(txtAvailable.getText())) {
                            new DmAlertMessageBox("Leave is not available.", txtToDate).show();
                        }
                    }
                }
            }
        });

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

    private void getAvailableLeave(LeaveEntry_Mdl mrTemp) {

        SrvAsync.invokeRPC().getAvailableLeave(mrTemp, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    txtAvailable.setText(result.getData());
                }
            }
        });

    }

    private void getLeaveHistory(LeaveEntry_Mdl mrTemp) {

        SrvAsync.invokeRPC().getLeaveHistory(mrTemp, new AsyncCallback<DmResult<List<GrdLeaveHistory_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdLeaveHistory_Mdl>> result) {
                if (result.isStatus()) {
                    dmHistoryGrd.getGrid().getStore().clear();
                    dmHistoryGrd.getGrid().getStore().addAll(result.getData());
                }
            }
        });

    }

    private void fillLeaveStatusGrd(final LeaveEntry_Mdl mrTemp) {
        SrvAsync.invokeRPC().fillLeaveStatusGrd(mrTemp, new AsyncCallback<DmResult<List<GrdLeaveStatus_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdLeaveStatus_Mdl>> result) {
                if (result.isStatus()) {
                    dmGrd.getGrid().getStore().clear();
                    dmGrd.getGrid().getStore().addAll(result.getData());
                    getLeaveHistory(mrTemp);
                }
            }
        });

    }

    private void getEmployeeCurrentShiftTime(final LeaveEntry_Mdl mrTemp) {
        SrvAsync.invokeRPC().getCurrentShiftTime(txtHEmployee.getTag(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHLeave).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                txtFromTime.setText(DmShared.xNull(result.getData().getSharedKeyFieldCode()));
                txtToTime.setText(DmShared.xNull(result.getData().getSharedKeyFieldName()));
                fillLeaveStatusGrd(mrTemp);
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
        dmGrd.getGrid().getStore().clear();
        dmEncashHistoryGrd.getGrid().getStore().clear();
        dmHistoryGrd.getGrid().getStore().clear();
        manageDisplay();
        getServerTime();
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<LeaveEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<LeaveEntry_Mdl> result) {
                lblLeaveStatus.setText("");
                topControlMoveRecSuccess(result);
                lblCurrentEntryNo.setText("");

            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<LeaveEntry_Mdl> result) {
        manageDisplay();
        LeaveEntry_Mdl data;
        if (result.isStatus()) {

            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtHEmployee.setText(DmShared.xNull(data.getEmployeeName()));
            txtHEmployee.setTag(DmShared.xNull(data.getEmployeeCode()));
            txtDepartment.setText(DmShared.xNull(data.getDepartmentName()));
            txtDepartment.setTag(DmShared.xNull(data.getDepartmentCode()));
            txtApplyDate.setText(DmShared.dNull(data.getApplyDate()));
            txtHLeave.setText(DmShared.xNull(data.getLeaveName()));
            txtHLeave.setTag(DmShared.xNull(data.getLeaveCode()));
            txtFromDate.setText(DmShared.dNull(data.getFromDate()));
            txtFromTime.setText(DmShared.xNull(data.getFromTime()));
            txtToDate.setText(DmShared.dNull(data.getToDate()));
            txtToTime.setText(DmShared.xNull(data.getToTime()));
            txtDuration.setText(DmShared.xNull(data.getDuration()));
            txtReason.setText(DmShared.xNull(data.getReason()));
            txtAvailable.setText(DmShared.xNull(data.getBalanceLeave()));
            txtEntryNo.setText(DmShared.iNull(data.getRecId().getCounter()).toString());
            txtLeaveDetail.setText(DmShared.xNull(data.getDays()));
            txtLeaveDetail.setTag(DmShared.xNull(data.getPlusMinus()));
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            lblLeaveStatus.setText(DmShared.xNull(data.getStatus()));
            // Changing color of lblLeaveStatus text as per leave status.
            if (DmShared.xNull(data.getStatus()).equalsIgnoreCase(PREnum.LEAVE_STATUS.PENDING.getValue())) {
                lblLeaveStatus.getElement().getStyle().setColor("blue");
            } else if (DmShared.xNull(data.getStatus()).equalsIgnoreCase(PREnum.LEAVE_STATUS.CANCELED.getValue())) {
                lblLeaveStatus.getElement().getStyle().setColor("red");
            } else {
                lblLeaveStatus.getElement().getStyle().setColor("green");
            }

            fillLeaveStatusGrd(data);
//            getLeaveHistory(data);

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
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(LeaveEntry.this);
                }
            }
        }).show();
    }

    @Override
    public void onClick(ClickEvent event) {

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

        txtFromDate.addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    txtToDate.setText(txtFromDate.getText());
                }
            }
        }, KeyDownEvent.getType());

        txtToDate.addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    getDuration(txtFromDate.getText(), txtToDate.getText());
                }
            }
        }, KeyDownEvent.getType());
    }

    public LeaveEntry(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("E", "*"));
        SrvAsync = new ServiceInvoker<LeaveEntry_SrvAsync>(GWT.create(LeaveEntry_Srv.class), LeaveEntry.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        txtReason.addBlurHandler(this);
        btnApprovedBy.addClickHandler(this);
        textHelpEvents();
        findEvents();
        swi.setWidget(this);
        manageEvents();

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
                new DmAlertMessageBox(caught.getMessage(), txtApplyDate).show();
            }

            @Override
            public void onSuccess(String[] result) {
                if (DmShared.dNull(txtApplyDate.getText()).equalsIgnoreCase("")) {
                    txtApplyDate.setText(result[0]);
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {

        if (event.getSource().equals(txtApplyDate)) {
            txtApplyDate.setText(DmClient.returnDate(txtApplyDate.getText()));
        } else if (event.getSource() == txtFromDate) {
            returnDate(txtFromDate);
        } else if (event.getSource() == txtToDate) {
            returnDate(txtToDate);
        }

        if (event.getSource().equals(txtReason)) {
            if (!DmShared.xNull(txtToDate.getText()).trim().equalsIgnoreCase("") && !DmShared.xNull(txtFromDate.getText()).trim().equalsIgnoreCase("")) {
                getDuration(txtFromDate.getText(), txtToDate.getText());
            }
        }

    }

}
