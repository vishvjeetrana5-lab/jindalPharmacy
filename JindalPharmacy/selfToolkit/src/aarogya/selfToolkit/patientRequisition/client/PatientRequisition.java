package aarogya.selfToolkit.patientRequisition.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.patientRequisition.shared.*;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.self.shared.*;
import java.util.HashMap;
import java.util.List;

public class PatientRequisition extends PatientRequisition_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    public interface CallBack {

        public void onOk();

        public void onCancel();
    }

    private CallBack callBack;
    private final ServiceInvoker<PatientRequisition_SrvAsync> srvAsync;
    private final RequisitionGridEdit<GrdRequisitionMedicine_Mdl, Medicine_PrpAcs> geRequisition = new RequisitionGridEdit(grdMedicine);
    private DefaultValue_Mdl dvm;
    private String docId, docIdRegistration, docIdPrescription, admitDate = "";

    private void textHelpEvents() {
        txtHByDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getDepartmentBy(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHByDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHByDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHByDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
        txtHToDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getDepartmentTo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHToDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHToDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHToDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHSite.setTag(DmShared.xNull(t1Row.getSharedModifiedBy()));
                    txtHSite.setText(DmShared.xNull(t1Row.getSharedPreparedBy()));
                } else {
                    txtHSite.setTag("");
                    txtHSite.setText("");
                }
                if (DmShared.iNull(txtHSite.getTag()).longValue() == 0) {
                    txtHSite.setTag(DmShared.xNull(dvm.getSiteCodeToTag()));
                    txtHSite.setText(DmShared.xNull(dvm.getSiteCodeToText()));
                }
            }
        });
        txtHRequisition.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getRequisitionBy(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRequisition).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRequisition.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRequisition).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHPriority.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHPriority_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPriority(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHPriority_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPriority).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHPriority_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPriority.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPriority).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHPriority_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHPriority.setTag2nd(DmShared.xNull(t1Row.getPriorityIndex()));
                } else {
                    txtHPriority.setTag2nd("");
                }
            }
        });
        txtHSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSiteTo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    private void gridEvents() {
        geRequisition.addCallBack(new RequisitionGridEdit.CallBack() {

            @Override
            public void onOk(GrdRequisitionMedicine_Mdl details_Mdl) {
                grdMedicine.commitChanges();
            }

            @Override
            public void onDelete() {
                grdMedicine.commitChanges();
            }
        });
        grdMedicine.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showRequisition(event.getRowIndex());
            }
        });
        grdMedicine.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showRequisition(grdMedicine.getGrid().getStore().indexOf(grdMedicine.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    public void show(List<GrdRequisitionMedicine_Mdl> lstMedicine, String regDocId, String admitDate) {
        this.docIdRegistration = regDocId;
        this.admitDate = admitDate;
        pnlForm.setGlassEnabled(true);
        pnlForm.center();
        pnlForm.show();

        clear();
        if (DmShared.iNull(docIdPrescription).longValue() == 0) {
            grdMedicine.getGrid().getStore().clear();
            grdMedicine.getGrid().getStore().add(new GrdRequisitionMedicine_Mdl());
        }

        if (dvm != null) {
            txtHToDepartment.setTag(this.dvm.getDepartmentTag());
            txtHToDepartment.setText(this.dvm.getDepartmentText());
            txtHByDepartment.setTag(this.dvm.getDepartmentByTag());
            txtHByDepartment.setText(this.dvm.getDepartmentByText());
            txtHRequisition.setTag(this.dvm.getRequestedByTag());
            txtHRequisition.setText(this.dvm.getRequestedByText());
            txtHSite.setTag(this.dvm.getSiteCodeToTag());
            txtHSite.setText(this.dvm.getSiteCodeToText());
        } else {
            getDefaultValue();
        }

        if (lstMedicine != null) {
            grdMedicine.getGrid().getStore().clear();
            grdMedicine.getGrid().getStore().addAll(lstMedicine);
            grdMedicine.getGrid().getStore().add(new GrdRequisitionMedicine_Mdl());
        }

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                txtHRequisition.setFocus(true);
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        if (callBack != null) {
            this.callBack = callBack;
        }
    }

    public PatientRequisition() {
        super();
        srvAsync = new ServiceInvoker<PatientRequisition_SrvAsync>(GWT.create(PatientRequisition_Srv.class), PatientRequisition.class.getName());
        this.asWidget().setTitle(widgetName);
        handlerEvents();
        textHelpEvents();
        gridEvents();
        getDefaultValue();
    }

    @Override
    public Widget asWidget() {
        return pnlForm;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
            hide();
            callBack.onCancel();
        } else if (event.getSource().equals(btnSave)) {
            try {
                topControlSave();
            } catch (Exception e) {
                topControlSaveError(0, e.getMessage());
            }
        }

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
            callBack.onCancel();
        }
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            topControlSave();
        }
    }

    private void showRequisition(int rowIndex) {
        geRequisition.show(rowIndex, true, true, true, admitDate);
    }

    private void handlerEvents() {
        btnCancel.addClickHandler(this);
        btnSave.addClickHandler(this);
        pnlForm.addAttachHandler(this);
    }

    private void getDefaultValue() {
        srvAsync.invokeRPC().getDefalutValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    PatientRequisition.this.dvm = result.getData();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                }
            }
        });
    }

    private PatientRequisition_Mdl topControlSaveInitialize() {
        PatientRequisition_Mdl mrTemp = new PatientRequisition_Mdl();

        if (!toolContainer.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(this.docId);
        }
        mrTemp.setDefaultValue(dvm);
        mrTemp.setDepartmentByTag(DmShared.iNull(txtHByDepartment.getTag()).toString());
        mrTemp.setDepartmentToTag(DmShared.iNull(txtHToDepartment.getTag()).toString());
        mrTemp.setRequisitionByTag(DmShared.iNull(txtHRequisition.getTag()).toString());
        mrTemp.setDocIdRegistration(this.docIdRegistration);
        mrTemp.setDocIdPrescription(this.docIdPrescription);
        mrTemp.setPriorityTypeCode(DmShared.xNull(txtHPriority.getTag()));
        mrTemp.setDeptToSiteCode(DmShared.xNull(txtHSite.getTag()));

        grdMedicine.commitChanges();
        mrTemp.getLstMedicine().addAll(grdMedicine.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void topControlSave() {
        PatientRequisition_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            PatientRequisition_Vld.checkSave(mrTemp);
            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    topControlSaveError(0, caught.getMessage());
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        PatientRequisition.this.docId = result.getData();
                        hide();
                        callBack.onOk();

                        new DmConfirmMessageBox("Do you want to send print to printer?", new DialogHideEvent.DialogHideHandler() {

                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                    prnRecord(true);
                                }
                            }
                        }).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMsg) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMsg, txtHRequisition).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMsg, txtHByDepartment).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMsg, txtHToDepartment).show();
                break;
            case 350:
                new DmAlertMessageBox(errorMsg, txtHSite).show();
                break;
            default:
                new DmAlertMessageBox(errorMsg, txtHRequisition).show();
                break;
        }
    }

    private void hide() {
        this.pnlForm.hide();
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setDocIdRegistration(String docIdRegistration) {
        this.docIdRegistration = docIdRegistration;
    }

    public void setMode(String mode) {
        this.toolContainer.setMode(mode);
    }

    public void setDocIdPrescription(String docIdPrescription) {
        this.docIdPrescription = docIdPrescription;
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Requisition No.", "", DmShared.xNull(docId)));
        new OpenSourceReport(DmShared.xNull(dvm.getVoucherTag()), "", "Requisition", widgetName, hashMapFilter, printToPrinter, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void clear() {
        txtHByDepartment.setTag("");
        txtHByDepartment.setTag2nd("");
        txtHByDepartment.setText("");
        txtHToDepartment.setTag("");
        txtHToDepartment.setTag2nd("");
        txtHToDepartment.setText("");
        txtHRequisition.setText("");
        txtHRequisition.setTag("");
        txtHRequisition.setTag2nd("");
    }
}
