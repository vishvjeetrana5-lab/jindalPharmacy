package dataman.payroll.shiftMaster.client;

import dataman.payroll.shiftMaster.client.*;
import dataman.payroll.shiftMaster.shared.*;
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
import dataman.payroll.shiftMaster.shared.ShiftMaster_Mdl;
import dataman.payroll.shiftMaster.shared.ShiftMaster_Srv;
import dataman.payroll.shiftMaster.shared.ShiftMaster_SrvAsync;

import java.util.HashMap;

public class ShiftMaster extends ShiftMaster_Dsg implements IsWidget, AttachEvent.Handler, ClickHandler, DmRootEvents, BlurHandler {

    ServiceInvoker<ShiftMaster_SrvAsync> SrvAsync;
    WalkThrough walkThroughPBill;
    WalkThrough walkThroughPBillDirect;
    String category = "";
    SetWidgetInvoker swi;

    private void textHelpEvents() {
        txtHIncludesNextDay.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIncludesNextDaye(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIncludesNextDay).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIncludesNextDay.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIncludesNextDay).show();
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
                txtHIncludesNextDay.setText("No");
                txtHIncludesNextDay.setTag("0");
                txtShiftName.setFocus(true);
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtShiftName.setFocus(true);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ShiftMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void clear() {

        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    private ShiftMaster_Mdl topControlSaveInitialize() {
        ShiftMaster_Mdl mrTemp = new ShiftMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setShiftName(DmShared.xNull(txtShiftName.getText()));
        mrTemp.setShortName(DmShared.xNull(txtShortName.getText()));
        mrTemp.setFromTime(DmShared.xNull(txtFromTime.getText()));
        mrTemp.setToTime(DmShared.xNull(txtToTime.getText()));
        mrTemp.setDuration(DmShared.xNull(txtDuration.getTag()));
        mrTemp.setInTimeGrace(DmShared.xNull(txtInTimeGrace.getText()));
        mrTemp.setOutTimeGrace(DmShared.xNull(txtOutTimeGrace.getText()));
        mrTemp.setIncludesNexDay(DmShared.xNull(txtHIncludesNextDay.getTag()));

        return mrTemp;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtShiftName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtShortName).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtFromTime).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtToTime).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtInTimeGrace).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtOutTimeGrace).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtHIncludesNextDay).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtShiftName).show();
        }
    }

    private void topControlSave() {
        ShiftMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ShiftMaster_Vld.chkSave(mrTemp);
            SrvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtShiftName).show();
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

        txtDuration.setEnabled(false);

    }

    private void getDuration(String fromDateTime, String toDateTime) {

        SrvAsync.invokeRPC().getDuration(fromDateTime.replace(".", ":"), toDateTime.replace(".", ":"), txtHIncludesNextDay.getTag(), new AsyncCallback<DmResult<String>>() {
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

    private void returnDate(DmTextBox txtBox) {
        if (!DmShared.xNull(txtBox.getText()).trim().equalsIgnoreCase("")) {
            txtBox.setText(DmClient.returnDate(txtBox.getText()));
        }
    }

    private void onAdd() {
        clear();
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ShiftMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ShiftMaster_Mdl> result) {
                topControlMoveRecSuccess(result);

            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ShiftMaster_Mdl> result) {
        manageDisplay();
        ShiftMaster_Mdl data;
        if (result.isStatus()) {

            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtShiftName.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtShortName.setText(DmShared.xNull(data.getShortName()));
            txtFromTime.setText(DmShared.xNull(data.getFromTime()));
            txtToTime.setText(DmShared.xNull(data.getToTime()));
            txtDuration.setTag(DmShared.xNull(data.getDuration()));
            txtDuration.setText(DmShared.vNull(DmClient.mntsToHrs(DmShared.iNull(txtDuration.getTag()).intValue())).toString());
            txtHIncludesNextDay.setTag(DmShared.xNull(data.getIncludesNexDay()));
            txtHIncludesNextDay.setText(DmShared.xNull(data.getIncludesNexDayName()));
            txtInTimeGrace.setText(DmShared.xNull(data.getInTimeGrace()));
            txtOutTimeGrace.setText(DmShared.xNull(data.getOutTimeGrace()));
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
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ShiftMaster.this);
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
        txtFromTime.addBlurHandler(this);
        txtToTime.addBlurHandler(this);
        txtInTimeGrace.addBlurHandler(this);
        txtOutTimeGrace.addBlurHandler(this);

        txtToTime.addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    getDuration(txtFromTime.getText(), txtToTime.getText());
                }
            }
        }, KeyDownEvent.getType());
    }

    public ShiftMaster(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        this.swi = swi;
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        //   menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("E", "*"));
        SrvAsync
                = new ServiceInvoker<ShiftMaster_SrvAsync>(GWT.create(ShiftMaster_Srv.class
                ), ShiftMaster.class
                        .getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        flwForm.addAttachHandler(this);
        btnApprovedBy.addClickHandler(this);
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
                new DmAlertMessageBox(caught.getMessage(), txtToTime).show();
            }

            @Override
            public void onSuccess(String[] result) {
//                if (DmShared.dNull(txtAttendanceDate.getText()).equalsIgnoreCase("")) {
//                    txtAttendanceDate.setText(result[0]);
//                    txtFromDate.setText(result[0]);
//                    txtToDate.setText(result[0]);
//                }
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtFromTime) || event.getSource().equals(txtToTime) 
                || event.getSource().equals(txtInTimeGrace) || event.getSource().equals(txtInTimeGrace)) {
            getDuration(txtFromTime.getText(), txtToTime.getText());
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        
    }

}
