package dataman.payroll.employeeShiftDetail.client;

import dataman.payroll.employeeShiftDetail.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
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
import dataman.payroll.employeeShiftDetail.shared.EmployeeShiftDetail_Mdl;


import java.util.HashMap;

public class EmployeeShiftDetail extends EmployeeShiftDetail_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, BlurHandler {

    ServiceInvoker<EmployeeShiftDetail_SrvAsync> SrvAsync;
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

            }
        });
        txtHShift.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHShift_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHShift(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHShift_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHShift).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHShift_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHShift.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHShift).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHShift_Mdl t1Row) {

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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EmployeeShiftDetail.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void clear() {

        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    private EmployeeShiftDetail_Mdl topControlSaveInitialize() {
        EmployeeShiftDetail_Mdl mrTemp = new EmployeeShiftDetail_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setShiftName(DmShared.xNull(txtHShift.getText()));
        mrTemp.setEmployeeName(DmShared.xNull(txtHEmployee.getText()));
        mrTemp.setShiftCode(DmShared.xNull(txtHShift.getTag()));
        mrTemp.setEmployeeCode(DmShared.xNull(txtHEmployee.getTag()));
        mrTemp.setFromDate(DmShared.xNull(txtFromDate.getText()));
        mrTemp.setToDate(DmShared.xNull(txtToDate.getText()));
        return mrTemp;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHEmployee).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHShift).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtFromDate).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtToDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHEmployee).show();
        }
    }

    private void topControlSave() {
        EmployeeShiftDetail_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            EmployeeShiftDetail_Vld.chkSave(mrTemp,DmClient.getLoginDate());
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
        txtHEmployee.setEnabled(false);
    }

    private void returnDate(DmTextBox txtBox) {
        if (!DmShared.xNull(txtBox.getText()).trim().equalsIgnoreCase("")) {
            txtBox.setText(DmClient.returnDate(txtBox.getText()));
        }
    }

    private void onAdd() {
        clear();
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<EmployeeShiftDetail_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<EmployeeShiftDetail_Mdl> result) {
                topControlMoveRecSuccess(result);

            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<EmployeeShiftDetail_Mdl> result) {
        manageDisplay();
        EmployeeShiftDetail_Mdl data;
        if (result.isStatus()) {

            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtHEmployee.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtHEmployee.setTag(DmShared.xNull(data.getEmployeeCode()));
            txtHShift.setText(DmShared.xNull(data.getShiftName()));
            txtHShift.setTag(DmShared.xNull(data.getShiftCode()));
            txtFromDate.setText(DmShared.dNull(data.getFromDate()));
            txtToDate.setText(DmShared.dNull(data.getToDate()));
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
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EmployeeShiftDetail.this);
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
        if (topControl.getMode().equalsIgnoreCase("New")) {
            txtFromDate.addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    txtToDate.setText(DmClient.getEndDate());
                }
            }
            }, KeyDownEvent.getType());
        }

        
    }

    public EmployeeShiftDetail(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        //   menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("E", "*"));
        SrvAsync
                = new ServiceInvoker<EmployeeShiftDetail_SrvAsync>(GWT.create(EmployeeShiftDetail_Srv.class
                ), EmployeeShiftDetail.class
                        .getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        flwForm.addAttachHandler(this);
        textHelpEvents();
        findEvents();
        swi.setWidget(this);
        manageEvents();
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
                new DmAlertMessageBox(caught.getMessage(), txtFromDate).show();
            }

            @Override
            public void onSuccess(String[] result) {
                if (DmShared.dNull(txtFromDate.getText()).equalsIgnoreCase("")) {
                    txtFromDate.setText(result[0]);
                    txtToDate.setText(DmClient.getEndDate());
                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            returnDate(txtFromDate);
        } else if (event.getSource() == txtToDate) {
            returnDate(txtToDate);
        }
    }

}
