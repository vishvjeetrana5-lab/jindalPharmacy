package dataman.payroll.employeeMaster.client;

import dataman.payroll.employeeMaster.shared.EmployeeMaster_Srv;
import dataman.payroll.employeeMaster.shared.EmployeeMaster_SrvAsync;
import dataman.payroll.employeeMaster.shared.GrdEarningDed_Mdl;
import dataman.payroll.employeeMaster.shared.GrdLeave_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import dataman.dmToolkit.dmStatic.client.DmClient;
import java.util.List;
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
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.math.BigDecimal;

public class EarningPopup extends EarningPopup_Dsg implements DmRootEvents, ClickHandler, AttachEvent.Handler, IsWidget {

    private final ServiceInvoker<EmployeeMaster_SrvAsync> SrvAsync;
    private String generalPatientInfo = "";
    private String regDocid = "";
    double earning;
    double netSalary;
    final EarningGridEdit<GrdEarningDed_Mdl, GrdEarningDed_PrpAcs> gridEarningEdit = new EarningGridEdit(grdEarningPopup);
    private EmployeeMaster empParent;
    private EarningPopup earningPopup;

    interface CallBack {

        public void onOk(List<GrdLeave_Mdl> lstService);

        public void onClose();
    }

    private CallBack callBack;
    private boolean isRateEditable = false;

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return pnlForm;
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(EmployeeMaster empMast) {
//         pnlForm.showRelativeTo(relativeWidget);
        pnlForm.show();
        pnlForm.center();
        pnlForm.setGlassEnabled(true);
        this.empParent = empMast;
        this.earningPopup = earningPopup;

        iniEarningDedDetails();

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {

            }
        });
    }
    double totalServiceAmount = 0d;

    private void iniEarningDedDetails() {
        SrvAsync.invokeRPC(true).getEarningDedDetails(DmShared.xNull(empParent.topControl.getDocId()), new AsyncCallback<DmResult<List<GrdEarningDed_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdEarningDed_Mdl>> result) {
                double earningAmt = 0.0;
                if (result.isStatus()) {
                    txtName.setText(empParent.txtName.getText());
                    grdEarningPopup.getGrid().getStore().clear();
                    grdEarningPopup.getGrid().getStore().addAll(result.getData());
                    grdEarningPopup.commitChanges();
                    for (GrdEarningDed_Mdl node : grdEarningPopup.getGrid().getStore().getAll()) {
                        if (!DmShared.iNull(empParent.topControl.getDocId()).toString().equalsIgnoreCase("")) {
                            if (!DmShared.vNull(node.getAmt()).toString().equalsIgnoreCase("")) {
                                if (DmShared.iNull(node.getAmtPer()).longValue() == 0) {
                                    earningAmt = (DmShared.vNull(node.getBasicPay()).doubleValue() / 100) * DmShared.vNull(node.getEarningAmt());
                                    earning = DmShared.vNull(DmShared.vNull(node.getBasicPay()) + DmShared.vNull(earningAmt));
                                } else {
                                    earning = DmShared.vNull(DmShared.vNull(node.getBasicPay()) + DmShared.vNull(node.getEarningAmt()));
                                }

                                txtEarning.setText(DmShared.vNull(earning).toString());
                                txtDeduction.setText(node.getDeductionAmt());
                                netSalary = DmShared.vNull(DmShared.vNull(earning) - DmShared.vNull(node.getDeductionAmt()));
                                txtNetSalary.setText(DmShared.vNull(netSalary).toString());
                            }
                        }
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    public void hide() {
        this.pnlForm.hide();
    }

    public EarningPopup() {
        super();
        SrvAsync = new ServiceInvoker<EmployeeMaster_SrvAsync>(GWT.create(EmployeeMaster_Srv.class), EmployeeMaster.class.getName());

        btnCancel.addClickHandler(this);
        gridEvents();
        txtHelpEvents();
        txtName.setEnabled(false);
    }

    private void txtHelpEvents() {

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
            this.hide();
        }

    }

    private void gridEvents() {

        gridEarningEdit.addCallBack(new EarningGridEdit.CallBack() {

            @Override
            public void onOk(GrdEarningDed_Mdl earningDed_Mdl) {
                if (!DmShared.xNull(earningDed_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
//                    updateRecord(leave_Mdl);
                    grdEarningPopup.commitChanges();
                }
            }

            @Override
            public void onDelete() {
                grdEarningPopup.commitChanges();
            }
        });

        grdEarningPopup.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showPrepareLeave(event.getRowIndex());
            }
        });
        grdEarningPopup.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {

                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showPrepareLeave(grdEarningPopup.getGrid().getStore().indexOf(grdEarningPopup.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showPrepareLeave(int currentRowIndex) {
        if (empParent.topControl.getMode().equalsIgnoreCase("Browse")) {
            gridEarningEdit.show(currentRowIndex, true, false, false, empParent, this);
        } else {
            gridEarningEdit.show(currentRowIndex, true, true, true, empParent, this);
        }
    }
}
