package dataman.payroll.leaveEntryApproval.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.payroll.leaveEntryApproval.shared.LeaveEntryApproval_Mdl;
import dataman.payroll.leaveEntryApproval.shared.LeaveEntryApproval_Srv;
import dataman.payroll.leaveEntryApproval.shared.LeaveEntryApproval_SrvAsync;

public class Setting<T1, T2> extends Setting_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurEvent.BlurHandler {

    interface LeaveEntryApprovalCallBack {

        public void onOk(LeaveEntryApproval_Mdl approvalEntry_Mdl);
    }

    LeaveEntryApprovalCallBack leaveEntryApprovalCallBack;
    ServiceInvoker<LeaveEntryApproval_SrvAsync> SrvAsync;


    public void addLeaveEntryApprovalCallBack(LeaveEntryApprovalCallBack leaveEntryApprovalCallBack) {
        this.leaveEntryApprovalCallBack = leaveEntryApprovalCallBack;
    }

    public void show(boolean editable) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtApplyDate.setFocus(true);
        flwOk.clear();
        flwOk.setVisible(false);

        if (editable) {
            flwOk.add(btnOk);
            flwOk.setVisible(true);
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }
    
        private void txtKeyEvents() {
        txtApplyDate.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                txtApplyDate.setText(DmClient.returnDate(txtApplyDate.getText()));
            }
        });

    }

    private void textHelpEvents() {

        txtHStatus.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHStatus(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHStatus).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHStatus.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHStatus).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

    }
    

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {

            fillRecords();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
    }

    private void fillRecords() {
        LeaveEntryApproval_Mdl mrTemp = new LeaveEntryApproval_Mdl();
        mrTemp.setApplyDate(DmShared.xNull(txtApplyDate.getText()));
        mrTemp.setStatusCode(DmShared.xNull(txtHStatus.getTag()));
        mrTemp.setStatusName(DmShared.xNull(txtHStatus.getText()));

        leaveEntryApprovalCallBack.onOk(mrTemp);
        popupPanel.hide();
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

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

    public Setting() {
        super();
        SrvAsync = new ServiceInvoker<LeaveEntryApproval_SrvAsync>(GWT.create(LeaveEntryApproval_Srv.class), LeaveEntryApproval.class.getName());
        textHelpEvents();
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Setting");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        onAdd();
        txtKeyEvents();
    }

    private void onAdd() {
       // txtApplyDate.setText(DmClient.getLoginDate());
        if (DmShared.xNull(txtHStatus.getTag()).equalsIgnoreCase("")) {
            txtHStatus.setTag("P");
            txtHStatus.setText("Pending");
        }
    }

}
