package dataman.payroll.employeeMaster.client;


import dataman.payroll.employeeMaster.shared.EmployeeMaster_Srv;
import dataman.payroll.employeeMaster.shared.EmployeeMaster_SrvAsync;
import dataman.payroll.employeeMaster.shared.GrdSoftId_Mdl;
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

public class SoftIdPopup extends SoftIdPopup_Dsg implements DmRootEvents, ClickHandler, AttachEvent.Handler, IsWidget {

    private final ServiceInvoker<EmployeeMaster_SrvAsync> SrvAsync;
    private String generalPatientInfo = "";
    private String regDocid = "";
    final SoftIdGridEdit<GrdSoftId_Mdl, GrdSoftId_PrpAcs> gridSoftIdEdit = new SoftIdGridEdit(grdSoftIdPopup);
    private EmployeeMaster empParent;

    interface CallBack {

        public void onOk(List<GrdSoftId_Mdl> lstService);

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
        pnlForm.show();
        pnlForm.center();
        pnlForm.setGlassEnabled(true);
        this.empParent = empMast;

        iniSoftIdDedDetails();
        

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {

            }
        });
    }
    double totalServiceAmount = 0d;

    private void iniSoftIdDedDetails() {
        SrvAsync.invokeRPC(true).getSoftIdDetails(DmShared.xNull(empParent.topControl.getDocId()),new AsyncCallback<DmResult<List<GrdSoftId_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdSoftId_Mdl>> result) {
                if (result.isStatus()) {
                        txtName.setText(empParent.txtName.getText());
                        grdSoftIdPopup.getGrid().getStore().clear();
                        grdSoftIdPopup.getGrid().getStore().addAll(result.getData());
                        grdSoftIdPopup.commitChanges();
                        grdSoftIdPopup.getGrid().getStore().add(new GrdSoftId_Mdl());

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    public void hide() {
        this.pnlForm.hide();
    }

    public SoftIdPopup() {
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

        
        
        gridSoftIdEdit.addCallBack(new SoftIdGridEdit.CallBack() {

            @Override
            public void onOk(GrdSoftId_Mdl SoftId_Mdl) {
                if (!DmShared.xNull(SoftId_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    grdSoftIdPopup.commitChanges();
                }
            }

            @Override
            public void onDelete() {
                grdSoftIdPopup.commitChanges();
            }
        });

        grdSoftIdPopup.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showPrepareSoftId(event.getRowIndex());
            }
        });
        grdSoftIdPopup.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {

                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showPrepareSoftId(grdSoftIdPopup.getGrid().getStore().indexOf(grdSoftIdPopup.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }
    private void showPrepareSoftId(int currentRowIndex) {
        if (empParent.topControl.getMode().equalsIgnoreCase("Browse")) {
            gridSoftIdEdit.show(currentRowIndex, true, false, false, empParent);
        } else {
            gridSoftIdEdit.show(currentRowIndex, true, true, true, empParent);
        }
    }
}
