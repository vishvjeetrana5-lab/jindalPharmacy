package aarogya.master.eod.client;

import aarogya.master.eod.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.ProcessEOD;
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
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;

public final class EOD extends EOD_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents, BlurHandler {

    private ServiceInvoker<EOD_SrvAsync> srvAsync;
    private final RoomPosting roomPosting = new RoomPosting();
    private final LabConsumption labConsumption = new LabConsumption();
    private final AcPosting opdAcPosting = new AcPosting();
    private final EODHistory eodHistory = new EODHistory();
    private String regDocId = "";
    protected boolean isRoomPosting = false, isLabConsumption = false, isOPDAcPosting = false, isIPDAcPosting = false, isDayCareAcPosting = false,
            isDiagnosticAcPosting = false, isEmergencyAcPosting = false, isPharmacyAcPosting = false, isMISPosting = false;
    protected UserPermission_Mdl userPermissionMdl;

    private void setUserPermission(UserMenu menuPermission) {
        for (String permission : menuPermission.getControlPermission()) {
            if (permission.equalsIgnoreCase(opdAcPostingBlog)) {
                isOPDAcPosting = true;
            } else if (permission.equalsIgnoreCase(ipdAcPostingBlog)) {
                isIPDAcPosting = true;
            } else if (permission.equalsIgnoreCase(dayCareAcPostingBlog)) {
                isDayCareAcPosting = true;
            } else if (permission.equalsIgnoreCase(diagnosticAcPostingBlog)) {
                isDiagnosticAcPosting = true;
            } else if (permission.equalsIgnoreCase(pharmacyAcPostingBlog)) {
                isPharmacyAcPosting = true;
            } else if (permission.equalsIgnoreCase(emergencyAcPostingBlog)) {
                isEmergencyAcPosting = true;
            } else if (permission.equalsIgnoreCase(roomPostingBlog)) {
                isRoomPosting = true;
            } else if (permission.equalsIgnoreCase(labConsumptionBlog)) {
                isLabConsumption = true;
            } else if (permission.equalsIgnoreCase(misAppUpdation)) {
                isMISPosting = true;
            }
        }
    }

    private void textHelpEvents() {
        roomPosting.addCallBack(new RoomPosting.CallBack() {

            @Override
            public void onClose() {
                setDefaultValue(null);
            }
        });

        labConsumption.addCallBack(new LabConsumption.CallBack() {

            @Override
            public void onClose() {
                setDefaultValue(null);
            }
        });

        opdAcPosting.addCallBack(new AcPosting.CallBack() {
            @Override
            public void onClose() {
                setDefaultValue(null);
            }
        });

        eodHistory.addCallBack(new EODHistory.CallBack() {
            @Override
            public void onClose() {
                ((CellSelectionModel) grdProcessDetail.getGrid().getSelectionModel()).selectCell(0, 1);
            }
        });
    }

    private void gridEvents() {
        grdProcessDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                if (event.getColumnIndex() == 5) {
                    showProcessPopup(DmShared.xNull(grdProcessDetail.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()).trim(), true);
                } else {
                    showProcessPopup(DmShared.xNull(grdProcessDetail.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode()).trim(), false);
                }
            }
        });
        grdProcessDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showProcessPopup(DmShared.xNull(grdProcessDetail.getGrid().getStore().get(grdProcessDetail.getGrid().getStore().indexOf(grdProcessDetail.getGrid().getSelectionModel().getSelectedItem())).getSharedKeyFieldCode()).trim(), false);
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showProcessPopup(String processCode, boolean isDelete) {
        if (DmShared.dNull(txtDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Date is mandatory.", txtDate).show();
            return;
        } else {
            if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.LAB_CONSUMPTION) {
                labConsumption.show(DmShared.dNull(txtDate.getText()), isDelete);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.ROOM_POSTING) {
                roomPosting.show(DmShared.dNull(txtDate.getText()), DmShared.xNull(regDocId), isDelete);
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.MIS_APP_UPDATION) {
                onMISPosting();
            } else if (ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.OPD_AC_POSTING
                    || ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.IPD_AC_POSTING
                    || ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.EMERGENCY_AC_POSTING
                    || ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DAY_CARE_AC_POSTING
                    || ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.PHARMACY_AC_POSTING
                    || ProcessEOD.getKeyOf(DmShared.iNull(processCode).intValue()) == ProcessEOD.DIAGNOSTIC_AC_POSTING) {
                opdAcPosting.show(DmShared.dNull(txtDate.getText()), isDelete, processCode);
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource() == btnRun) {
                new DmConfirmMessageBox("Are you sure? You want to run.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                            saveEODs();
                        }
                        txtDate.setFocus(true);
                    }
                }).show();
            } else if (event.getSource() == btnHistory) {
                eodHistory.show(this);
            } else if (event.getSource() == btnClose) {
                onClose();
            }
        } catch (DmException e) {
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtDate)) {
            txtDate.setText(DmClient.returnDate(txtDate.getText()));
            if ((DmShared.convertDate(txtDate.getText()).getTime() > (DmShared.convertDate(DmClient.getLoginDate())).getTime())) {
                new DmAlertMessageBox("For Date should be less than or equal to To Current Date.", txtDate).show();
                return;
            }
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            roomPosting.hide(false);
            opdAcPosting.hide(false);
            labConsumption.hide(false);
            eodHistory.hide(false);
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    public EOD(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<EOD_SrvAsync>(GWT.create(EOD_Srv.class), EOD.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        setUserPermission(menuPermission);
        textHelpEvents();
        gridEvents();
        handleEvent();
        setPermission();
        setDefaultValue(swi);
    }

    private void handleEvent() {
        btnClose.addClickHandler(this);
        btnRun.addClickHandler(this);
        btnHistory.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtDate.addBlurHandler(this);
    }

    private void setDefaultValue(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getEOD(userPermissionMdl, new AsyncCallback<DmResult<List<GrdProcessDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdProcessDetail_Mdl>> result) {
                if (result.isStatus()) {
                    if (swi != null) {
                        swi.setWidget(EOD.this);
                    }
                    if (DmShared.dNull(txtDate.getText()).equalsIgnoreCase("")) {
                        txtDate.setText(DmShared.dNull(result.getValue1()));
                    }
                    grdProcessDetail.getGrid().getStore().clear();
                    grdProcessDetail.getGrid().getStore().addAll(result.getData());
                    ((CellSelectionModel) grdProcessDetail.getGrid().getSelectionModel()).selectCell(0, 1);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void saveEODs() {
        if (DmShared.dNull(txtDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Date is mandatory.", txtDate).show();
            return;
        } else {
            try {
                srvAsync.invokeRPC().saveEODRecord(DmShared.dNull(txtDate.getText()), regDocId, DmClient.getVoucherPrefix(), userPermissionMdl, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            setDefaultValue(null);
                            new DmAlertMessageBox("Record save successfully.", txtDate).show();
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            } catch (DmException e) {
                new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
            }
        }
    }

    private void onMISPosting() {
        new DmConfirmMessageBox("Are you sure? You want MIS Posting.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    saveMISPosting();
                }
                txtDate.setFocus(true);
            }
        }).show();
    }

    private void saveMISPosting() {
        if (DmShared.dNull(txtDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Date is mandatory.", txtDate).show();
            return;
        } else {
            try {
                srvAsync.invokeRPC().saveMISPosting(DmShared.dNull(txtDate.getText()), new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            setDefaultValue(null);
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            } catch (DmException e) {
                new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
            }
        }
    }

    @Override
    public void action(String nature, Object regDocId, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "ADD_EODRP":
                    openWithDischarge((String) regDocId, callback);
                    break;
                case "FOCUS_ON_ADD":
                    showProcessPopup(DmShared.iNull(ProcessEOD.ROOM_POSTING.getId()).toString(), false);
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    public void openWithDischarge(String regDocId, final DmCallBack callback) {
        txtDate.setText(DmClient.getLoginDate());
        this.regDocId = regDocId;
        txtDate.setEnabled(false);
        DmResult<Object> dmrTemp = new DmResult<>();
        dmrTemp.setStatus(true);
        callback.result(dmrTemp);
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EOD.this);
                }
            }
        }).show();
    }

    private void setPermission() {
        userPermissionMdl = new UserPermission_Mdl();
        userPermissionMdl.setDayCareAcPosting(isDayCareAcPosting);
        userPermissionMdl.setDiagnosticAcPosting(isDiagnosticAcPosting);
        userPermissionMdl.setEmergencyAcPosting(isEmergencyAcPosting);
        userPermissionMdl.setIpdAcPosting(isIPDAcPosting);
        userPermissionMdl.setLabConsumption(isLabConsumption);
        userPermissionMdl.setOpdAcPosting(isOPDAcPosting);
        userPermissionMdl.setRoomPosting(isRoomPosting);
        userPermissionMdl.setPharmacyAcPosting(isPharmacyAcPosting);
        userPermissionMdl.setMisAppPush(isMISPosting);
    }
}
