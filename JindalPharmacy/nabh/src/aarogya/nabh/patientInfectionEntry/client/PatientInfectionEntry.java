package aarogya.nabh.patientInfectionEntry.client;

import aarogya.nabh.patientInfectionEntry.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.opdSearch.client.OPDSearch;
import aarogya.selfToolkit.patientSearchRoomWise.client.PatientSearchRoomWise;
import aarogya.selfToolkit.patientSearchRoomWise.shared.SearchValueRoomWise_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import java.util.List;

public final class PatientInfectionEntry extends PatientInfectionEntry_Dsg implements ClickHandler, BlurHandler, DmRootEvents, KeyPressHandler {

    private final ServiceInvoker<PatientInfectionEntry_SrvAsync> SrvAsync;
    private DefaultValue_Mdl dvm;
    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final PatientSearchRoomWise patientRoomSearchIPD = new PatientSearchRoomWise(SearchValueRoomWise_Mdl.FilterType.NOT_BILLED_APPROVED, SearchValueRoomWise_Mdl.FilterType.IPD_ONLY);
    private final PatientSearchRoomWise patientRoomSearchEmg = new PatientSearchRoomWise(SearchValueRoomWise_Mdl.FilterType.NOT_DISCHARGED, SearchValueRoomWise_Mdl.FilterType.EMERGENCY_ONLY, SearchValueRoomWise_Mdl.FilterType.ALLOW_SITE_FOR_DIAGNOSTIC);
    private final OPDSearch opdSearch = new OPDSearch();
    private String PatientRegDocId;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{"NABIT"})) {

            @Override
            public String getVoucherType() {
                return dvm.getVoucherTypeTag();
            }

            @Override
            public String getVoucherDate() {
                return txtVoucherDate.getText();
            }

            @Override
            public String getVoucherValue() {
                return "";
            }
        });

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
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
                new DmConfirmMessageBox("Do you want to send print to printer?", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
//                            prnRecord(true);
                        } else {
//                            prnRecord(false);
                        }
                    }
                }).show();
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PatientInfectionEntry.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtHRegType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRegType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRegType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRegType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRegType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (t1Row != null) {
                    setIPD_OPD_Dsiplay();
                } else {
                    flwSearchRoom.clear();

                }
                txtRegNo.setEnabled(true);
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        if (!DmShared.xNull(txtHRegType.getTag()).trim().equalsIgnoreCase("")) {
                            txtRegNo.setFocus(true);

                        } else {
                            txtRegNo.setEnabled(false);

                        }
                    }
                });
            }
        });
        patientRoomSearchIPD.addCallBack(new PatientSearchRoomWise.CallBack() {
            @Override
            public void ok(String regDocid, String regRecid, String roomCode) {

                getUHIDDetail("", regDocid);
                PatientRegDocId = regDocid;

            }

            @Override
            public void close() {
                txtRegNo.setFocus(true);
            }
        });
        patientRoomSearchEmg.addCallBack(new PatientSearchRoomWise.CallBack() {
            @Override
            public void ok(String regDocid, String regRecid, String roomCode) {

                getUHIDDetail("", regDocid);
            }

            @Override
            public void close() {
                txtRegNo.setFocus(true);
            }
        });
        opdSearch.addCallBack(new OPDSearch.CallBack() {
            @Override
            public void ok(String recIdReg, String docIdReg) {

                getUHIDDetail("", docIdReg);
            }

            @Override
            public void close() {
                txtRegNo.setFocus(true);
            }
        });
        txtHServiceType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHServiceType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHInfectionType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHInfectionType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInfectionType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHInfectionType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInfectionType).show();
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
                SrvAsync.invokeRPC(blnCallBackWait).getFind(topControl.getPermissions(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    @Override
    public void onClick(ClickEvent event) {
        btnClick((Button) event.getSource());
    }

    private void btnClick(Button button) {
        if (!button.isAttached()) {
            return;
        }
        if (!button.isVisible()) {
            return;
        }
        if (!button.isEnabled()) {
            return;
        } else if (button == btnSearchRoom) {
            if (DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.IPD)) {
                patientRoomSearchIPD.show();
            } else if (DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.EMERGENCY)) {
                patientRoomSearchEmg.show();
            }
        } else if (button == btnOPDSearch) {
            if (DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.OPD)) {
                opdSearch.show("OPD", aarogya.selfToolkit.opdSearch.shared.SearchValue_Mdl.OPDFilterType.CANCEL_DAYS,
                        aarogya.selfToolkit.opdSearch.shared.SearchValue_Mdl.OPDFilterType.OPD_ONLY,
                        aarogya.selfToolkit.opdSearch.shared.SearchValue_Mdl.OPDFilterType.ALLOW_SITE_FOR_DIAGNOSTIC);
            } else if (DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.DAY_CARE)) {
                opdSearch.show("Day Care", aarogya.selfToolkit.opdSearch.shared.SearchValue_Mdl.OPDFilterType.CANCEL_DAYS,
                        aarogya.selfToolkit.opdSearch.shared.SearchValue_Mdl.OPDFilterType.DAYCARE_ONLY,
                        aarogya.selfToolkit.opdSearch.shared.SearchValue_Mdl.OPDFilterType.ALLOW_SITE_FOR_DIAGNOSTIC);
            }
        }

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtVoucherDate)) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
            if (event.getSource().equals(txtVoucherDate)) {
                txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
                if ((DmShared.convertDate(DmClient.getLoginDate()).getTime() > (DmShared.convertDate(txtVoucherDate.getText())).getTime())) {
                    new DmAlertMessageBox("Vr. Date should be greater than or equal to Current Date.", txtVoucherDate).show();
                    return;
                }
            }

        }
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getSource().equals(txtRegNo)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!DmShared.xNull(txtRegNo.getText()).trim().equalsIgnoreCase("")) {
                    if (!DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase("")) {
                        getUHIDDetail("", DmShared.xNull(txtRegNo.getText()));
                    } else {
                        new DmAlertMessageBox("Please Select Type", txtHRegType).show();
                    }
                } else {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            if ((DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.EMERGENCY))) {
                                btnSearchRoom.setFocus(true);
                            } else if ((DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.OPD)) || (DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.DAY_CARE))) {
                                btnOPDSearch.setFocus(true);
                            } else {

                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        }
    }

    public PatientInfectionEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<PatientInfectionEntry_SrvAsync>(GWT.create(PatientInfectionEntry_Srv.class), PatientInfectionEntry.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        handlerEvents();
        iniDefaultValues(swi);
    }

    private void handlerEvents() {
        txtVoucherDate.addBlurHandler(this);
        txtVouchertTime.addBlurHandler(this);
        btnOPDSearch.addClickHandler(this);
        btnSearchRoom.addClickHandler(this);
        txtRegNo.addKeyPressHandler(this);
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    dvm = result.getData();
                    onAdd();
                    swi.setWidget(PatientInfectionEntry.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void topControlSave() {
        PatientInfectionEntry_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            PatientInfectionEntry_Vld.chkSave(mrTemp);
            SrvAsync.invokeRPC().saveRecord(topControl.getPermissions(), mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHRegType).show();
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

    private PatientInfectionEntry_Mdl topControlSaveInitialize() {
        PatientInfectionEntry_Mdl mrTemp = new PatientInfectionEntry_Mdl();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setVtType(dvm.getVoucherTypeTag());
        mrTemp.setRegDocId(DmShared.iNull(PatientRegDocId).toString());
        mrTemp.setRegType(DmShared.xNull(txtHRegType.getText()));
        mrTemp.setUhid(DmShared.xNull(txtRegNo.getText()));
        mrTemp.setvDate(DmShared.dNull(txtVoucherDate.getText()));
        mrTemp.setvTime(DmShared.xNull(txtVouchertTime.getText()));
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setInfectionType(DmShared.xNull(txtHInfectionType.getText()));
        mrTemp.setServiceCode(DmShared.xNull(txtHServiceType.getTag()));

        return mrTemp;
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHRegType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtRegNo).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHInfectionType).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHServiceType).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHRegType).show();
                break;

        }
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(topControl.getPermissions(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<PatientInfectionEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PatientInfectionEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<PatientInfectionEntry_Mdl> result) {
        PatientInfectionEntry_Mdl data;
        onAdd();
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(DmShared.xNull(data.getSharedKeyFieldCode()));
            txtPatientName.setText(DmShared.xNull(data.getPatient()));
            txtGuardianName.setText(DmShared.xNull(data.getGuardian()));
            txtMobile.setText(DmShared.xNull(data.getMobile()));
            txtGender.setText(DmShared.xNull(data.getGender()));
            txtAge.setText(DmShared.xNull(data.getDisplayDOB()));
            txtVoucherDate.setText(DmShared.dNull(data.getvDate()));
            txtVouchertTime.setText(DmShared.xNull(data.getvTime()));
            txtAddress.setText(DmShared.xNull(data.getAddress()));
            txtHInfectionType.setText(DmShared.xNull(data.getInfectionType()));
            txtHServiceType.setText(DmShared.xNull(data.getServiceName()));
            txtHServiceType.setTag(DmShared.xNull(data.getServiceCode()));
            txtUHID.setText(DmShared.xNull(data.getUhid()));
            txtRegNo.setText(DmShared.xNull(data.getRegNo()));
            txtHRegType.setText(DmShared.xNull(data.getRegType()));
            txtRemark.setText(DmShared.xNull(data.getRemark()));
            grdpatientInfectionDetail.getGrid().getStore().clear();
            grdpatientInfectionDetail.getGrid().getStore().addAll(data.getGrdPatientInfection());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(topControl.getPermissions(), strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {

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

    private void manageDisplay() {
        txtPatientName.setEnabled(false);
        txtGuardianName.setEnabled(false);
        txtGender.setEnabled(false);
        txtUHID.setEnabled(false);
        txtAddress.setEnabled(false);
        txtAge.setEnabled(false);
        txtMobile.setEnabled(false);
        txtVoucherNo.setEnabled(false);
        txtVoucherDate.setEnabled(false);
        txtVouchertTime.setEnabled(false);
        txtHRegType.setEnabled(false);
        txtRegNo.setEnabled(false);

    }

    private void onAdd() {

        txtRegNo.setText("");
        txtHRegType.setFocus(true);

        setIPD_OPD_Dsiplay();
        txtRegNo.setEnabled(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (!DmShared.xNull(txtHRegType.getTag()).trim().equalsIgnoreCase("")) {
                    txtRegNo.setFocus(true);

                } else {
                    txtRegNo.setEnabled(false);

                }
            }
        });

        txtPatientName.setEnabled(false);
        txtGuardianName.setEnabled(false);
        txtGender.setEnabled(false);
        txtUHID.setEnabled(false);
        txtAddress.setEnabled(false);
        txtAge.setEnabled(false);
        txtMobile.setEnabled(false);
        txtVoucherNo.setEnabled(false);
        txtVoucherDate.setEnabled(false);
        txtVouchertTime.setEnabled(false);
        grdpatientInfectionDetail.getGrid().getStore().clear();
        flwSearchRoom.clear();
        if (topControl.getMode().trim().equalsIgnoreCase("New")) {
            txtHRegType.setTag("IPD");
            txtHRegType.setText("IPD");
            if (DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.IPD) || DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.EMERGENCY)) {
                flwSearchRoom.add(btnSearchRoom);
            } else if ((DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.OPD)) || (DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.DAY_CARE))) {
                flwSearchRoom.add(btnOPDSearch);
            } else {
                flwSearchRoom.clear();
            }
        }

        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        txtVoucherDate.setText(DmShared.dNull(dvm.getServerDate()));
        txtVouchertTime.setText(DmShared.xNull(dvm.getServerTime()));

    }

    private void setIPD_OPD_Dsiplay() {
        flwSearchRoom.clear();
        if ((DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.EMERGENCY))) {
            flwSearchRoom.add(btnSearchRoom);

        } else if ((DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.OPD)) || (DmShared.xNull(txtHRegType.getTag()).equalsIgnoreCase(pvc.DAY_CARE))) {
            flwSearchRoom.add(btnOPDSearch);

        } else {
            flwSearchRoom.clear();

        }
    }

    private void getUHIDDetail(String regNo, String regDocId) {
        try {
            SrvAsync.invokeRPC().getUHIDDetails(regNo, regDocId, DmShared.xNull(txtHRegType.getTag()), new AsyncCallback<DmResult<PatientDetail_Mdl>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtRegNo).show();
                }

                @Override
                public void onSuccess(DmResult<PatientDetail_Mdl> result) {
                    PatientDetail_Mdl data;

                    if (result.isStatus()) {
                        data = result.getData();
                        txtUHID.setText(DmShared.xNull(data.getRecIdUHID()));
                        txtUHID.setTag(DmShared.xNull(data.getDocIdUHID()));
                        txtPatientName.setText(DmShared.xNull(data.getPatient()));
                        txtAddress.setText(DmShared.xNull(data.getAddress()));
                        txtAge.setText(DmShared.xNull(data.getAge()));
                        txtGuardianName.setText(DmShared.xNull(data.getGender()));
                        txtGender.setText(DmShared.xNull(data.getGender()));
                        txtMobile.setText(DmShared.xNull(data.getMobile()));
                        txtRegNo.setText(DmShared.xNull(data.getRecIdRegistration()));

                        grdpatientInfectionDetail.getGrid().getStore().clear();
                        grdpatientInfectionDetail.getGrid().getStore().addAll(data.getGrdPatientInfection());
                        txtHInfectionType.setFocus(true);

                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtUHID).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }

    }

}
