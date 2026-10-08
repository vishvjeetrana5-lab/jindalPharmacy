package aarogya.master.managePatientSchedule.client;

import aarogya.master.managePatientSchedule.shared.*;
import aarogya.projectMenu.self.shared.ProjectRuleCode;
import aarogya.projectMenu.self.shared.pmDayCare;
import aarogya.selfToolkit.patientSchedulePopup.client.PatientSchedulePopup;
import aarogya.selfToolkit.patientSearch.client.PatientSearch;
import aarogya.selfToolkit.patientSearch.shared.SearchValue_Mdl;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
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
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFileManager.client.DmFileManager;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.HashMap;
import java.util.List;

public final class ManagePatientSchedule extends ManagePatientSchedule_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyDownHandler {

    private final ServiceInvoker<ManagePatientSchedule_SrvAsync> srvAsync;
    private final PatientSearch patientSearch = new PatientSearch();
    private boolean isSave = false, isDelete = false, isManageSchedule = false, isPatientFollowUp = true;
    final PatientSchedulePopup schedulePopup = new PatientSchedulePopup();
    private final DmFileManager fileManager = new DmFileManager(ProjectRuleCode.PATIENT_SCHEDULE_FOLLOW.getRuleCode());
    private final ManagePatientScheduleGridEdit<GrdSchedules_Mdl, GridService_PrpAcs> manageSchedule = new ManagePatientScheduleGridEdit(grdSchedule);
    private HashMap<String, String> multiDocIdWithRuleCode;

    public ManagePatientSchedule(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<ManagePatientSchedule_SrvAsync>(GWT.create(ManagePatientSchedule_Srv.class), ManagePatientSchedule.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        flwForm.addAttachHandler(this);
        userPermission(menuPermission);
        textHelpEvents();
        gridEvents();
        findEvents();
        manageEvent();
        swi.setWidget(this);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtUHID.setFocus(true);
            }
        });
    }

    private void userPermission(UserMenu menuPermission) {
        flwNewParent.clear();
        flwDiscardParent.clear();
        flwSchedule.clear();

        if (menuPermission.getUserPermission().contains("A")) {
            flwNewParent.add(flwNewChild);
        }
        if (menuPermission.getUserPermission().contains("D")) {
            flwDiscardParent.add(flwDiscardChild);
        }
        for (String controlPermission : menuPermission.getControlPermission()) {
            if (controlPermission.equalsIgnoreCase(saveScheduleBlog)) {
                isSave = true;
            } else if (controlPermission.equalsIgnoreCase(deleteSchduleBlog)) {
                isDelete = true;
            } else if (controlPermission.equalsIgnoreCase(manageSchduleBlog)) {
                isManageSchedule = true;
            } else if (controlPermission.equalsIgnoreCase(patientFollowUpBlog)) {
                flwSchedule.add(btnSchedule);
                isPatientFollowUp = true;
            }
        }
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtUHID) {
            txtUHID.setText(txtUHID.getTag2nd());
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void manageEvent() {

        txtUHID.addClickHandler(this);
        txtUHID.addKeyDownHandler(this);
        txtUHID.addBlurHandler(this);
        btnClose.addClickHandler(this);
        btnNew.addClickHandler(this);
        btnSearchPatient.addClickHandler(this);
        btnManageFile.addClickHandler(this);
        btnFind.addClickHandler(this);
        btnDiscard.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnSchedule.addClickHandler(this);

        manageDisplay(true);
    }

    private void manageDisplay(boolean flag) {

        txtPatientName.setEnabled(false);
        txtGuardianName.setEnabled(false);
        txtMobile.setEnabled(false);
        txtAge.setEnabled(false);
        txtGender.setEnabled(false);
        txtAddress.setEnabled(false);
        txtActive.setEnabled(false);
        txtScheduleName.setEnabled(false);
        txtEntryDate.setEnabled(false);
        txtHSchedule.setEnabled(flag);
        txtUHID.setEnabled(flag);
        txtUHID.setFocus(true);
    }

    private void clear() {
        txtUHID.setText("");
        txtUHID.setTag("");
        txtUHID.setTag2nd("");
        txtRegNo.setText("");
        txtRegNo.setTag("");
        txtRegNo.setTag2nd("");
        txtPatientName.setText("");
        txtGuardianName.setText("");
        txtMobile.setText("");
        txtAge.setText("");
        txtGender.setText("");
        txtAddress.setText("");
        txtEntryDate.setText("");
        txtScheduleName.setText("");
        txtActive.setText("");
        grdSchedule.getGrid().getStore().clear();
        txtHSchedule.setText("");
        txtHSchedule.setTag2nd("");
        txtHSchedule.setTag("");

    }

    private void textHelpEvents() {
        schedulePopup.addCallBack(new PatientSchedulePopup.CallBack() {
            @Override
            public void onOk(String schDocId) {
                topControlMoveRec("", "", schDocId, "", null);

            }

            @Override
            public void onClose() {
                txtUHID.setFocus(true);
            }
        });

        patientSearch.addCallBack(new PatientSearch.CallBack() {

            @Override
            public void ok(String uhidRecId, String uhidDocId) {
                topControlMoveRec("", "", "", uhidDocId, null);
            }

            @Override
            public void close() {
                txtUHID.setFocus(true);
            }
        });

        txtHSchedule.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSchedule_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSchedule(txtUHID.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSchedule_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSchedule).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHSchedule_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSchedule.setData(result.getData(), result.getPaging());

                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSchedule).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHSchedule_Mdl tRow) {
                if (tRow != null && blnStatus) {
                    txtEntryDate.setText(tRow.getVDate());
                    patientScheduleGrid(tRow.getSharedKeyFieldCode());
                    txtHSchedule.setTag(tRow.getSharedKeyFieldCode());
                }
            }
        });

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnNew) {
            schedulePopup.show(txtUHID.getTag());
        } else if (event.getSource() == btnSearchPatient) {
            patientSearch.show(true, SearchValue_Mdl.FilterType.WITHOUT_EXPIRED);
            txtHSchedule.setEnabled(true);
        } else if (event.getSource() == btnClose) {
            close();
        } else if (event.getSource() == btnFind) {
            flwForm.remove(vrtInnerForm);
            flwForm.add(dmFind);
        } else if (event.getSource() == btnDiscard) {
            deleteRecord();
        } else if (event.getSource() == btnManageFile) {
            if (!DmShared.xNull(txtUHID.getTag()).equalsIgnoreCase("")) {
                fileManager.show(DmShared.xNull(txtUHID.getTag()), "1", false, false, multiDocIdWithRuleCode);
            }
        } else if (event.getSource() == btnRefresh) {
            if (DmShared.iNull(txtHSchedule.getTag()).longValue() != 0) {
                patientScheduleGrid(txtHSchedule.getTag());
            }
        } else if (event.getSource() == btnSchedule) {
            if (DmShared.iNull(txtHSchedule.getTag()).longValue() != 0) {
                if (DmShared.iNull(txtActive.getTag()).intValue() == 0) {
                    showPatientFollowUp(txtHSchedule.getTag());
                } else {
                    new DmAlertMessageBox("Record is  discarded.", (DmTextBox) null).show();
                    return;
                }
            }
        }
    }

    public void deleteRecord() {
        if (DmShared.iNull(txtHSchedule.getTag()).longValue() != 0) {
            if (DmShared.iNull(txtActive.getTag()).intValue() == 1) {
                new DmAlertMessageBox("Record is already discarded.", (DmTextBox) null).show();
                return;
            }
            new DmConfirmMessageBox("Are you sure? You want to discard this record.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        topControlDelete(txtUHID.getTag(), txtHSchedule.getTag());
                    }
                }
            }).show();
        } else {
            new DmAlertMessageBox("Please select any Record.", (DmTextBox) null).show();
        }
    }

    private void topControlDelete(String regUhid, String schdlDocId) {
        srvAsync.invokeRPC().deleteRecord(regUhid, schdlDocId, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended data = result.getData();
                    topControlMoveRec("", txtUHID.getText(), data.getSharedKeyFieldCode(), "", null);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void close() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {

                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ManagePatientSchedule.this);
                }
            }

        }).show();

    }

    private void patientScheduleGrid(String schdlDocId) {
        srvAsync.invokeRPC(true).getScheduleGrid(schdlDocId, new AsyncCallback<DmResult<List<GrdSchedules_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdSchedules_Mdl>> result) {
                if (result.isStatus()) {

                    fillGrid(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillGrid(List<GrdSchedules_Mdl> data) {
        if (data.size() > 0) {
            grdSchedule.getGrid().getStore().clear();
            grdSchedule.getGrid().getStore().addAll(data);
            grdSchedule.getGrid().getStore().add(new GrdSchedules_Mdl());
            txtScheduleName.setText(data.get(0).getScheduleName());
            txtActive.setText(DmShared.iNull(data.get(0).getActive()).intValue() == 0 ? "Yes" : "No");
            txtActive.setTag(DmShared.iNull(data.get(0).getActive()).toString());
        } else {
            new DmAlertMessageBox("No procedure on this schedule number. Kindly create new procedure. ", (DmTextBox) null).show();
            grdSchedule.getGrid().getStore().clear();
            grdSchedule.getGrid().getStore().add(new GrdSchedules_Mdl());
            ((CellSelectionModel) grdSchedule.getGrid().getSelectionModel()).selectCell(0, 12);
        }

    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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
                    topControlMoveRec("", tRow.getPatientRecId(), tRow.getSharedKeyFieldCode(), "", null);
                }
            }

        });
    }

    private void topControlMoveRec(String regDocId, String recId, String schdlDoc, String uhidocId, final DmCallBack callback) {
        srvAsync.invokeRPC().getMoveRec(regDocId, recId, schdlDoc, uhidocId, new AsyncCallback<DmResult<ManagePatientSchedule_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ManagePatientSchedule_Mdl> result) {
                topControlMoveRecSuccess(result);

                if (callback != null) {
                    DmResult<Object> dmrTemp = new DmResult<>();
                    dmrTemp.setStatus(true);
                    callback.result(dmrTemp);
                }
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ManagePatientSchedule_Mdl> result) {
        ManagePatientSchedule_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            fillPDetails(data);
            if (DmShared.xNull(data.getEntryDate()).equalsIgnoreCase("")) {
                txtEntryDate.setText(result.getValue1());;
            }
        } else {
            clear();
            new DmAlertMessageBox(result.getErrorMessage(), txtUHID).show();
        }

    }

    private void fillPDetails(ManagePatientSchedule_Mdl mrTemp) {
        clear();
        setUHIDDetail(mrTemp.getPatientDetailMdl());
        if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() != 0) {
            scheduleDetails(mrTemp);
        } else {
            new DmAlertMessageBox("Patient has no  schedule.", btnNew).show();
        }
    }

    private void setUHIDDetail(PatientDetail_Mdl data) {
        if (data != null) {
            txtUHID.setTag(DmShared.iNull(data.getDocIdUHID()).toString());
            txtUHID.setText(DmShared.xNull(data.getRecIdUHID()));
            txtUHID.setTag2nd(DmShared.xNull(data.getRecIdUHID()));
            txtPatientName.setText(DmShared.xNull(data.getPatient()));
            txtAge.setText(DmShared.xNull(data.getDisplayDOB()));
            txtGender.setText(DmShared.xNull(data.getGender()));
            txtGuardianName.setText(DmShared.xNull(data.getGuardian()));
            txtMobile.setText(DmShared.xNull(data.getMobile()));
            txtRegNo.setTag2nd(DmShared.xNull(data.getRegCategory()));
            txtRegNo.setText(DmShared.xNull(data.getRecIdReg()));
            txtRegNo.setTag(DmShared.xNull(data.getDocIdReg()));
            multiDocIdWithRuleCode = data.getMultiDocIdWithRuleCode();
        }
    }

    private void scheduleDetails(ManagePatientSchedule_Mdl mrTemp) {
        if (DmShared.iNull(mrTemp.getShdlcount()).longValue() == 1) {
            txtHSchedule.setTag(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            txtHSchedule.setText(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            txtScheduleName.setText(mrTemp.getScheduleHlp());
            txtActive.setText(DmShared.iNull(mrTemp.getActive()).intValue() == 0 ? "Yes" : "No");
            txtActive.setTag(DmShared.iNull(mrTemp.getActive()).toString());
            txtEntryDate.setText(DmShared.xNull(mrTemp.getEntryDate()));
            grdSchedule.getGrid().getStore().clear();
            grdSchedule.getGrid().getStore().addAll(mrTemp.getGrdPatnSchdlMdl());
            grdSchedule.getGrid().getStore().add(new GrdSchedules_Mdl());

        }
        txtHSchedule.setFocus(true);
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            close();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_F2) {
            schedulePopup.show(txtUHID.getTag());
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_F && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            flwForm.remove(vrtInnerForm);
            flwForm.add(dmFind);
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_D && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            deleteRecord();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_R && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            if (DmShared.iNull(txtHSchedule.getTag()).longValue() != 0) {
                patientScheduleGrid(txtHSchedule.getTag());
            }
        }

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            schedulePopup.hide();
            fileManager.hide();
            manageSchedule.hide();
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void gridEvents() {

        manageSchedule.addCallBack(new ManagePatientScheduleGridEdit.CallBack() {
            @Override
            public void refresh(String schdlDocId) {

                patientScheduleGrid(schdlDocId);
            }

        });
        grdSchedule.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                GrdSchedules_Mdl mdl = grdSchedule.getGrid().getStore().get(event.getRowIndex());
                if (event.getCellIndex() == colIndexManage) {
                    if (DmShared.dNull(mdl.getDoneDate()).equalsIgnoreCase("")) {
                        manageSchedule(event.getRowIndex());
                    }
                }
            }
        });
        grdSchedule.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                CellSelectionModel<GrdSchedules_Mdl> cellMdl;
                cellMdl = ((CellSelectionModel<GrdSchedules_Mdl>) grdSchedule.getGrid().getSelectionModel());
                GrdSchedules_Mdl mdl = grdSchedule.getGrid().getSelectionModel().getSelectedItem();

                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    if (cellMdl.getSelectCell().getCell() == colIndexManage && DmShared.dNull(mdl.getDoneDate()).equalsIgnoreCase("")) {
                        manageSchedule(grdSchedule.getGrid().getStore().indexOf(mdl));
                    }
                }

            }
        }, KeyDownEvent.getType());
    }

    private void manageSchedule(int rowIndex) {
        if (isManageSchedule) {

            if (DmShared.iNull(txtActive.getTag()).intValue() == 0) {

                manageSchedule.show(rowIndex, true, true, true, txtHSchedule.getTag(), this);

            } else {
                new DmAlertMessageBox("Record is already discarded.", (DmTextBox) null).show();
                return;
            }
        }
    }

    private void showPatientFollowUp(String shdlDoc) {
        if (isPatientFollowUp) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("scheduleDocId", shdlDoc);
            hm.put("regDocId", DmShared.xNull(txtRegNo.getTag()));
            invokeWidget(new pmDayCare().moduleName, new pmDayCare().DAY_CARE_PATIENT_FOLLOW_UP.caption, shdlDoc, "OPEN_SCHEDULE", "FOCUS_ON_ADD", true, true, hm);
        } else {
            new DmAlertMessageBox("You do not have permission.", (DmTextBox) null).show();
            return;
        }
    }

    private void invokeWidget(String moduleName, String menuName, final String docId, final String firstAction, final String secondAction, final boolean isCallBack, final boolean isHashMapInAction, final HashMap paramHashMap) {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuName, paramHashMap, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {
                IsWidgetExtended widget = ((IsWidgetExtended) wgtObject);
                if (!isCallBack && !DmShared.xNull(firstAction).trim().equalsIgnoreCase("")) {
                    widget.action(firstAction, isHashMapInAction ? paramHashMap : DmShared.iNull(docId).toString());
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                } else if (!DmShared.xNull(firstAction).equalsIgnoreCase("") && !DmShared.xNull(secondAction).equalsIgnoreCase("")) {
                    widget.action(firstAction, isHashMapInAction ? paramHashMap : DmShared.iNull(docId).toString(), new DmCallBack() {
                        @Override
                        public void result(DmResult result) {
                            if (result != null) {
                                if (result.isStatus()) {
                                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                                    ((IsWidgetExtended) wgtObject).action(secondAction, null, null);
                                }
                            }
                        }
                    });
                } else {
                    widget.searchRecord(docId);
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                }
            }
        });
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "OPEN_SCHEDULE":
                    HashMap<String, String> hm = (HashMap) object;
                    topControlMoveRec(((String) hm.get("regDocId")), "", ((String) hm.get("scheduleDocId")), ((String) hm.get("docIdUHID")), callback);
                    break;
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtHSchedule.setFocus(true);
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource() == txtUHID) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!DmShared.xNull(txtUHID.getText()).trim().equalsIgnoreCase("")) {
                    topControlMoveRec("", DmShared.xNull(txtUHID.getText()), "", "", null);
                } else {
                    btnSearchPatient.setFocus(true);
                }
            }
        }
    }

}
