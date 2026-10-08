package dataman.payroll.attendanceEntry.client;

import dataman.payroll.attendanceEntry.shared.GrdAttendanceEntry_Mdl;
import dataman.payroll.attendanceEntry.shared.AttendanceEntry_Mdl;
import dataman.payroll.attendanceEntry.shared.Find_Mdl;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
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

import java.util.ArrayList;

import java.util.HashMap;
import dataman.payroll.attendanceEntry.shared.AttendanceEntry_SrvAsync;
import dataman.payroll.attendanceEntry.shared.AttendanceEntry_Srv;
import dataman.payroll.attendanceEntry.shared.SalaryMethod_Mdl;

public class AttendanceEntry extends AttendanceEntry_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents {

    public interface CallBack {

        public void onOk(List<KeyFieldExtended> grdAttendanceMdl);

        public void onClose();

    }

    ServiceInvoker<AttendanceEntry_SrvAsync> SrvAsync;

    WalkThrough walkThroughPBill;
    private CallBack callBack;
    WalkThrough walkThroughPBillDirect;
    String category = "";
    SetWidgetInvoker swi;
    private List<GrdAttendanceEntry_Mdl> grdAttendanceEntry = new ArrayList<GrdAttendanceEntry_Mdl>();
    private AttendanceEntryGrdEdit<KeyFieldExtended, KeyField_PrpAcs> attendanceEntryGrdEdit = new AttendanceEntryGrdEdit(dmGrd);

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        txtHSalaryMethod.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<SalaryMethod_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSalaryMethod(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<SalaryMethod_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSalaryMethod).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<SalaryMethod_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSalaryMethod.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSalaryMethod).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, SalaryMethod_Mdl t1Row) {
                txtHSalaryMethod.setTag2nd(t1Row.getFromDate());
                txtHSalaryMethod.setText(t1Row.getSalaryMethodName());
                txtHSalaryMethod.setTag(t1Row.getSharedKeyFieldCode());
            }
        });
        txtHDepartmentName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartmentName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartmentName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartmentName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartmentName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

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
                txtHSalaryMethod.setFocus(true);
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHSalaryMethod.setFocus(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AttendanceEntry.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void gridEvents() {
        attendanceEntryGrdEdit.addCallBack(new AttendanceEntryGrdEdit.CallBack() {

            @Override
            public void onOk(GrdAttendanceEntry_Mdl grdAttendanceMdl) {
                dmGrd.commitChanges();

            }

            @Override
            public void onDelete() {
                dmGrd.commitChanges();

            }

        }
        );

//        dmGrd.getGrid()
//                .addRowClickHandler(new RowClickEvent.RowClickHandler() {
//
//                    @Override
//                    public void onRowClick(RowClickEvent event ) {
//                        showAttendanceGridEdit(event.getRowIndex(), true);
//                    }
//                }
//                );
        dmGrd.getGrid()
                .addCellClickHandler(new CellClickEvent.CellClickHandler() {

                    @Override
                    public void onCellClick(CellClickEvent event) {
                        showAttendanceGridEdit(event.getRowIndex(), event.getCellIndex(), true);
                    }
                }
                );

//        dmGrd.getGrid().addDomHandler(new KeyDownHandler() {
//            @Override
//            public void onKeyDown(KeyDownEvent event) {
//                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
//                    showAttendanceGridEdit(dmGrd.getGrid().getStore().indexOf(dmGrd.getGrid().getSelectionModel().getSelectedItem()), true);
//                }
//            }
//        }, KeyDownEvent.getType());
    }

    private void showAttendanceGridEdit(int currentRowIndex, int currentColumnIndex, boolean isDeleteFocus) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            attendanceEntryGrdEdit.show(currentRowIndex, currentColumnIndex, false, false, false, isDeleteFocus, this);
        } else {
            attendanceEntryGrdEdit.show(currentRowIndex, currentColumnIndex, true, false, false, isDeleteFocus, this);
        }

    }

    private void getGridDetail(AttendanceEntry_Mdl attendanceEntryMdl) {

        SrvAsync.invokeRPC().getFillRecords(attendanceEntryMdl, new AsyncCallback<List<GrdAttendanceEntry_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(List<GrdAttendanceEntry_Mdl> result) {

                dmGrd.getGrid().getStore().clear();
                dmGrd.getGrid().getStore().addAll(result);

                dmGrd.commitChanges();
            }
        });

    }

    private void getGridDetailNew(AttendanceEntry_Mdl attendanceEntryMdl) {

        SrvAsync.invokeRPC().getAttendance(attendanceEntryMdl, new AsyncCallback<List<GrdAttendanceEntry_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(List<GrdAttendanceEntry_Mdl> result) {

                dmGrd.getGrid().getStore().clear();
                dmGrd.getGrid().getStore().addAll(result);

                dmGrd.commitChanges();
            }
        });

    }

    private void clear() {

        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    private AttendanceEntry_Mdl topControlSaveInitialize() {
        AttendanceEntry_Mdl mrTemp = new AttendanceEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());

        }
        mrTemp.getAttendanceMdlList().addAll(new ArrayList<GrdAttendanceEntry_Mdl>());
        mrTemp.getAttendanceMdlList().addAll(dmGrd.getGrid().getStore().getAll());
//        if(mrTemp.getAttendanceMdlList().size()==0)
//        {
//           if(txtHSalaryMethod.getText().equalsIgnoreCase(""))
//           {
//            mrTemp.setSalaryMethodName(DmShared.xNull(null));
//            mrTemp.setSalaryMethodCode(DmShared.xNull(null));
//           }
//           else
//           {
//            mrTemp.setSalaryMethodName(DmShared.xNull(txtHSalaryMethod.getText()));
//            mrTemp.setSalaryMethodCode(DmShared.xNull(txtHSalaryMethod.getTag()));
//           }
//
//        }
//        else
//        {
        mrTemp.setSalaryMethodName(DmShared.xNull(txtHSalaryMethod.getText()));
        mrTemp.setSalaryMethodCode(DmShared.xNull(txtHSalaryMethod.getTag()));
        mrTemp.setDepartmentName(DmShared.xNull(txtHDepartmentName.getText()));
        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartmentName.getTag()));
//        }
        return mrTemp;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHSalaryMethod).show();
                break;

            default:
                new DmAlertMessageBox(errorMessage, txtHSalaryMethod).show();
        }
    }

    private void topControlSave() {
        AttendanceEntry_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            SrvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHSalaryMethod).show();
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
        flwButtonFillDetails.clear();
        flwButtonGetAttendance.clear();
        if (topControl.getMode().trim().equalsIgnoreCase("New")) {
            flwButtonFillDetails.add(btnFillDetails);
            flwButtonGetAttendance.add(btngetAttendance);
            txtHSalaryMethod.setEnabled(true);
            txtHDepartmentName.setEnabled(true);
        }
        if (!topControl.getMode().trim().equalsIgnoreCase("New")) {
            txtHSalaryMethod.setEnabled(false);
            txtHDepartmentName.setEnabled(false);
        }

    }

    private void onAdd() {
        clear();
        dmGrd.getGrid().getStore().clear();
        manageDisplay();

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
                        topControl.setNavigator(result.getData()); // here setNavigator set docId or update docId
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<AttendanceEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<AttendanceEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<AttendanceEntry_Mdl> result) {
        manageDisplay();
        AttendanceEntry_Mdl data;
        if (result.isStatus()) {

            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            AttendanceEntry_Mdl attendanceEntryMdl = new AttendanceEntry_Mdl();
            List<GrdAttendanceEntry_Mdl> grdAttendanceEntryList = new ArrayList<GrdAttendanceEntry_Mdl>();
            grdAttendanceEntryList = data.getAttendanceMdlList();

//            dmGrd.getGrid().getStore().addAll(grdAttendanceEntryList);
            txtHSalaryMethod.setText(data.getSalaryMethodName());
            txtHSalaryMethod.setTag(data.getSalaryMethodCode());
            txtHDepartmentName.setText(data.getDepartmentName());
            txtHDepartmentName.setTag(data.getDepartmentCode());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

            attendanceEntryMdl.setSalaryMethodName(DmShared.xNull(txtHSalaryMethod.getText()));
            attendanceEntryMdl.setSalaryMethodCode(DmShared.xNull(txtHSalaryMethod.getTag()));
            attendanceEntryMdl.setDepartmentName(DmShared.xNull(txtHDepartmentName.getTag()));
            attendanceEntryMdl.setDepartmentCode(DmShared.xNull(txtHDepartmentName.getTag()));
            attendanceEntryMdl.setFromDate(DmShared.xNull(txtHSalaryMethod.getTag2nd()));
            dmGrd.getGrid().getStore().clear();
            getGridDetailNew(attendanceEntryMdl);
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
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(AttendanceEntry.this);
                }
            }
        }).show();
    }

    @Override
    public void onClick(ClickEvent event) {
        AttendanceEntry_Mdl attendanceEntryMdl = new AttendanceEntry_Mdl();
        if (event.getSource() == btnFillDetails) {
            if (!DmShared.xNull(txtHSalaryMethod.getText()).equalsIgnoreCase("")) {
                attendanceEntryMdl.setSalaryMethodName(DmShared.xNull(txtHSalaryMethod.getText()));
                attendanceEntryMdl.setSalaryMethodCode(DmShared.xNull(txtHSalaryMethod.getTag()));
                attendanceEntryMdl.setDepartmentName(DmShared.xNull(txtHDepartmentName.getTag()));
                attendanceEntryMdl.setDepartmentCode(DmShared.xNull(txtHDepartmentName.getTag()));
                attendanceEntryMdl.setFromDate(DmShared.xNull(txtHSalaryMethod.getTag2nd()));
                getGridDetailNew(attendanceEntryMdl);

            } else {
                new DmAlertMessageBox("Salary Method is Mandatory.", txtHSalaryMethod).show();
            }
        } else if (event.getSource() == btngetAttendance) {
            if (!DmShared.xNull(txtHSalaryMethod.getText()).equalsIgnoreCase("")) {
                attendanceEntryMdl.setSalaryMethodName(DmShared.xNull(txtHSalaryMethod.getText()));
                attendanceEntryMdl.setSalaryMethodCode(DmShared.xNull(txtHSalaryMethod.getTag()));
                attendanceEntryMdl.setDepartmentName(DmShared.xNull(txtHDepartmentName.getTag()));
                attendanceEntryMdl.setDepartmentCode(DmShared.xNull(txtHDepartmentName.getTag()));
                attendanceEntryMdl.setFromDate(DmShared.xNull(txtHSalaryMethod.getTag2nd()));
                getGridDetailNew(attendanceEntryMdl);
            } else {
                new DmAlertMessageBox("Salary Method is Mandatory.", txtHSalaryMethod).show();
            }
        }

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

    public AttendanceEntry(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        // menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("E", "*"));
        SrvAsync = new ServiceInvoker<AttendanceEntry_SrvAsync>(GWT.create(AttendanceEntry_Srv.class), AttendanceEntry.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        flwForm.addAttachHandler(this);
        btnFillDetails.addClickHandler(this);
        btngetAttendance.addClickHandler(this);
        textHelpEvents();
        findEvents();
//        swi.setWidget(this);
        gridEvents();
        iniGetDefaultValue(swi);
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

    private void iniGetDefaultValue(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValue(new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                swi.setWidget(AttendanceEntry.this);
            }
        });
    }

}
