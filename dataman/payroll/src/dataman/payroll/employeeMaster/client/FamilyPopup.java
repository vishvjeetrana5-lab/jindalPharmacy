package dataman.payroll.employeeMaster.client;


import dataman.payroll.employeeMaster.shared.EmployeeMaster_Srv;
import dataman.payroll.employeeMaster.shared.EmployeeMaster_SrvAsync;
import dataman.payroll.employeeMaster.shared.GrdFamily_Mdl;
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

public class FamilyPopup extends FamilyPopup_Dsg implements DmRootEvents, ClickHandler, AttachEvent.Handler, IsWidget {

    private final ServiceInvoker<EmployeeMaster_SrvAsync> SrvAsync;
    private String generalPatientInfo = "";
    private String regDocid = "";
    final FamilyGridEdit<GrdFamily_Mdl, GrdFamily_PrpAcs> gridFamilyEdit = new FamilyGridEdit(grdFamilyPopup);
    private EmployeeMaster empParent;

    interface CallBack {

        public void onOk(List<GrdFamily_Mdl> lstService);

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

        iniFamilyDedDetails();
        

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {

            }
        });
    }
    double totalServiceAmount = 0d;

    private void iniFamilyDedDetails() {
        SrvAsync.invokeRPC(true).getFamilyDetails(DmShared.xNull(empParent.topControl.getDocId()),new AsyncCallback<DmResult<List<GrdFamily_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdFamily_Mdl>> result) {
                if (result.isStatus()) {
                        txtName.setText(empParent.txtName.getText());
                        grdFamilyPopup.getGrid().getStore().clear();
                        grdFamilyPopup.getGrid().getStore().addAll(result.getData());
                        grdFamilyPopup.commitChanges();
                        grdFamilyPopup.getGrid().getStore().add(new GrdFamily_Mdl());

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    public void hide() {
        this.pnlForm.hide();
    }

    public FamilyPopup() {
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

        
        
        gridFamilyEdit.addCallBack(new FamilyGridEdit.CallBack() {

            @Override
            public void onOk(GrdFamily_Mdl family_Mdl) {
                if (!DmShared.xNull(family_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    grdFamilyPopup.commitChanges();
                }
            }

            @Override
            public void onDelete() {
                grdFamilyPopup.commitChanges();
            }
        });

        grdFamilyPopup.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showPrepareFamily(event.getRowIndex());
            }
        });
        grdFamilyPopup.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {

                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showPrepareFamily(grdFamilyPopup.getGrid().getStore().indexOf(grdFamilyPopup.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }
    private void showPrepareFamily(int currentRowIndex) {
        if (empParent.topControl.getMode().equalsIgnoreCase("Browse")) {
            gridFamilyEdit.show(currentRowIndex, true, false, false, empParent);
        } else {
            gridFamilyEdit.show(currentRowIndex, true, true, true, empParent);
        }
    }
}
