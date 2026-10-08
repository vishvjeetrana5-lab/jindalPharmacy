package aarogya.selfToolkit.patientSchedule.client;

import aarogya.selfToolkit.patientSchedule.shared.*;
import aarogya.projectMenu.self.shared.pmDayCare;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.patientSchedulePopup.client.PatientSchedulePopup;
import com.google.gwt.core.client.GWT;
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
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmCallBack;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.HashMap;
import java.util.List;

public class PatientSchedule extends PatientSchedule_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    CallBack callBack;
    ServiceInvoker<PatientSchedule_SrvAsync> srvAsync;
    final PatientSchedulePopup schedulePopup = new PatientSchedulePopup();
    private String uhid = "", regDocId = "";
    private boolean isNewScheduleBlog = false, isPatientScheduleFollowupBlog = false, isManageScheduleBlog = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {

        public void onClose();
    }

    public PatientSchedule() {

        super();
        srvAsync = new ServiceInvoker<PatientSchedule_SrvAsync>(GWT.create(PatientSchedule_Srv.class), PatientSchedule.class.getName());
        textHelpEvents();
        gridEvents();
        btnNew.addClickHandler(this);
        btnRefresh.addClickHandler(this);
    }

    private void textHelpEvents() {
        schedulePopup.addCallBack(new PatientSchedulePopup.CallBack() {
            @Override
            public void onOk(String scheduleDocId) {
                patientScheduleGrid(uhid);
            }

            @Override
            public void onClose() {
                patientScheduleGrid(uhid);
            }
        });
    }

    @Override
    public Widget asWidget() {
        return this.flwForm;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnNew) {
            if (isNewScheduleBlog) {
                schedulePopup.show(uhid);
            } else {
                new DmAlertMessageBox("User do not have permission to add schedule.", (DmTextBox) null).show();
                return;

            }

        } else if (event.getSource() == btnRefresh) {
            patientScheduleGrid(this.uhid);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_F2) {
            //------------------- new -------------------------
            schedulePopup.show(uhid);
        }
    }

    public void hide() {
        hide(false);
    }

    public void show(String regDocId, String uhid, boolean newScheduleBlog, boolean patientScheduleFollowupBlog, boolean manageScheduleBlog) {
        clear();
        this.regDocId = regDocId;
        this.uhid = uhid;
        patientScheduleGrid(this.uhid);
        isNewScheduleBlog = newScheduleBlog;
        isPatientScheduleFollowupBlog = patientScheduleFollowupBlog;
        isManageScheduleBlog = manageScheduleBlog;
    }

    private void hide(boolean withCloseEvent) {

        if (withCloseEvent) {
            clear();
            this.callBack.onClose();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            schedulePopup.hide();
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void gridEvents() {
        grdSchedule.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                GrdSchedules_Mdl mdl = grdSchedule.getGrid().getStore().get(event.getRowIndex());
                if (event.getCellIndex() == colIndexManage) {

                    if (DmShared.iNull(uhid).longValue() != 0) {
                        manageSchedule(mdl.getSharedKeyFieldCode());
                    } else {
                        new DmAlertMessageBox("User do not have permission for manage schedule.", (DmTextBox) null).show();
                        return;

                    }

                } else {

                    if (DmShared.iNull(uhid).longValue() != 0) {
                        invokePatientFollowUp(mdl);
                    } else {
                        new DmAlertMessageBox("User do not have permission for patient schedule follow-up.", (DmTextBox) null).show();
                        return;

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
                    if (cellMdl.getSelectCell().getCell() == colIndexManage) {
                        if (DmShared.iNull(uhid).longValue() != 0) {
                            if (isManageScheduleBlog) {
                                manageSchedule(mdl.getSharedKeyFieldCode());
                            }

                        }
                    } else {

                        if (DmShared.iNull(uhid).longValue() != 0) {
                            if (isPatientScheduleFollowupBlog) {
                                invokePatientFollowUp(mdl);
                            }

                        }
                    }
                }

            }
        }, KeyDownEvent.getType());

    }

    private void invokePatientFollowUp(GrdSchedules_Mdl mdl) {
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("scheduleDocId", DmShared.iNull(mdl.getSharedKeyFieldCode()).toString());
        hm.put("regDocId", DmShared.iNull(this.regDocId).toString());
        invokeWidget(new pmDayCare().moduleName, new pmDayCare().DAY_CARE_PATIENT_FOLLOW_UP.caption, mdl.getSharedKeyFieldCode(), "OPEN_SCHEDULE", "FOCUS_ON_ADD", true, true, hm);

    }

    private void manageSchedule(String schdlDocId) {

        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("scheduleDocId", DmShared.iNull(schdlDocId).toString());
        hm.put("regDocId", "");
        invokeWidget(new pmMaster().moduleName, new pmMaster().MANAGE_PATIENT_SCHEDULE.caption, this.uhid, "OPEN_SCHEDULE", "FOCUS_ON_ADD", true, true, hm);

    }

    private void patientScheduleGrid(String uhid) {
        srvAsync.invokeRPC(true).getScheduleGrid(uhid, new AsyncCallback<DmResult<List<GrdSchedules_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdSchedules_Mdl>> result) {
                if (result.isStatus()) {
                    List<GrdSchedules_Mdl> data = result.getData();
                    grdSchedule.getGrid().getStore().clear();
                    grdSchedule.getGrid().getStore().addAll(data);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void clear() {
        uhid = "";
        regDocId = "";
    }

    private void invokeWidget(String moduleName, String menuName, final String docId, final String firstAction, final String secondAction, final boolean isCallBack, final boolean isHashMapInAction, final HashMap paramHashMap) {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuName, paramHashMap, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {
                IsWidgetExtended widget = ((IsWidgetExtended) wgtObject);
                if (!isCallBack && !DmShared.xNull(firstAction).trim().equalsIgnoreCase("")) {
                    widget.action(firstAction, DmShared.iNull(docId).toString());
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
}
