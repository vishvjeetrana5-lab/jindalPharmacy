package aarogya.selfToolkit.labRequisition.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.labRequisition.shared.*;
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

public class LabRequisition extends LabRequisition_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    public interface CallBack {

        public void onOk();

        public void onCancel();
    }

    private CallBack callBack;
    private final ServiceInvoker<LabRequisition_SrvAsync> srvAsync;
    private final RequisitionGridEdit<GrdLabRequisition_Mdl, Medicine_PrpAcs> geRequisition = new RequisitionGridEdit(grdTest);
    public DefaultValue_Mdl dvm;
    private String docId, docIdRegistration, docIdPrescription;
    protected String admitDate = "";
    String[] currentDT = {"", ""};

    public void show(List<GrdLabRequisition_Mdl> lstInvestigation, String admitDate) {
        this.admitDate = admitDate;
        pnlForm.setGlassEnabled(true);
        pnlForm.center();
        pnlForm.show();

        clear();
        if (DmShared.iNull(docIdPrescription).longValue() == 0) {
            grdTest.getGrid().getStore().clear();
            grdTest.getGrid().getStore().add(new GrdLabRequisition_Mdl());
        }

        if (dvm != null) {
            txtHDepartmentBy.setTag(this.dvm.getDepartmentByTag());
            txtHDepartmentBy.setText(this.dvm.getDepartmentByText());
            txtHRequisitionBy.setTag(this.dvm.getRequestedByTag());
            txtHRequisitionBy.setText(this.dvm.getRequestedByText());
        } else {
            getDefaultValue();
        }

        if (lstInvestigation != null) {
            fillInvestigation(lstInvestigation);
        }

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                txtHRequisitionBy.setFocus(true);
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        if (callBack != null) {
            this.callBack = callBack;
        }
    }

    public LabRequisition() {
        super();
        srvAsync = new ServiceInvoker<LabRequisition_SrvAsync>(GWT.create(LabRequisition_Srv.class), LabRequisition.class.getName());
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

    private void textHelpEvents() {
        txtHDepartmentBy.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartmentBy).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartmentBy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartmentBy).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHRequisitionBy.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getRequisitionBy(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRequisitionBy).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRequisitionBy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRequisitionBy).show();
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
    }

    private void gridEvents() {
        geRequisition.addCallBack(new RequisitionGridEdit.CallBack() {

            @Override
            public void onOk(GrdLabRequisition_Mdl details_Mdl) {
                grdTest.commitChanges();

            }

            @Override
            public void onDelete() {
                grdTest.commitChanges();
            }
        });
        grdTest.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showRequisition(event.getRowIndex());
            }
        });
        grdTest.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showRequisition(grdTest.getGrid().getStore().indexOf(grdTest.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showRequisition(int rowIndex) {
        geRequisition.show(rowIndex, true, true, true, this);
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
                    LabRequisition.this.dvm = result.getData();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                }
            }
        });
    }

    private LabRequisition_Mdl topControlSaveInitialize() {
        LabRequisition_Mdl mrTemp = new LabRequisition_Mdl();

        if (!toolContainer.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(this.docId);
        }
        mrTemp.setDefaultValue(dvm);
        mrTemp.setVoucherTypeTag(dvm.getVoucherTag());
        mrTemp.setVoucherDate(dvm.getDtVoucher());
        mrTemp.setDocIdRegistration(this.docIdRegistration);
        mrTemp.setDocIdPrescription(this.docIdPrescription);
        mrTemp.setDepartmentByTag(DmShared.iNull(txtHDepartmentBy.getTag()).toString());
        mrTemp.setRequisitionByTag(DmShared.iNull(txtHRequisitionBy.getTag()).toString());
        mrTemp.setPriorityTypeCode(DmShared.xNull(txtHPriority.getTag()));

        grdTest.commitChanges();
        mrTemp.getLstRequisition().addAll(grdTest.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void topControlSave() {
        LabRequisition_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            LabRequisition_Vld.checkSave(mrTemp);
            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    topControlSaveError(0, caught.getMessage());
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        LabRequisition.this.docId = result.getData();
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
                new DmAlertMessageBox(errorMsg, txtHRequisitionBy).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMsg, txtHDepartmentBy).show();
                break;
            default:
                new DmAlertMessageBox(errorMsg, txtHRequisitionBy).show();
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

    private void fillInvestigation(final List<GrdLabRequisition_Mdl> lstInvestigation) {
        srvAsync.invokeRPC(true).getServerDateTime(new AsyncCallback<String[]>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHRequisitionBy).show();
            }

            @Override
            public void onSuccess(String[] result) {
                currentDT = result;

                for (GrdLabRequisition_Mdl node : lstInvestigation) {
                    if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                        node.setReqDate(DmShared.dNull(currentDT[0]));
                        node.setReqTime(DmShared.xNull(currentDT[1]));
                    }
                }
                grdTest.getGrid().getStore().clear();
                grdTest.getGrid().getStore().addAll(lstInvestigation);
                grdTest.getGrid().getStore().add(new GrdLabRequisition_Mdl());

            }
        });
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Requisition No.", "", DmShared.xNull(docId)));
        new OpenSourceReport(DmShared.xNull(dvm.getVoucherTag()), "", "Requisition", widgetName, hashMapFilter, printToPrinter, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void clear() {
        txtHDepartmentBy.setTag("");
        txtHDepartmentBy.setText("");
        txtHRequisitionBy.setTag("");
        txtHRequisitionBy.setText("");
        grdTest.getGrid().getStore().clear();
    }
}
