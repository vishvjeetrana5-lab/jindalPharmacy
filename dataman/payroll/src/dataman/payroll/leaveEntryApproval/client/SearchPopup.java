package dataman.payroll.leaveEntryApproval.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.payroll.leaveEntryApproval.shared.LeaveEntryApproval_SrvAsync;
import dataman.payroll.leaveEntryApproval.shared.Filter_Mdl;
import dataman.payroll.leaveEntryApproval.shared.LeaveEntryApproval_Srv;
import java.util.List;

public class SearchPopup extends SearchPopup_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler {

    interface CallBack {

        public void onOk(Filter_Mdl filter_Mdl);

        public void onClose();
    }

    private CallBack callBack;

    private final ServiceInvoker<LeaveEntryApproval_SrvAsync> SrvAsync;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(Filter_Mdl filter_Mdl) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        setValue(filter_Mdl);
        txtEmployeeName.setFocus(true);
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onBlur(BlurEvent event) {

        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        } 
        if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
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
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide();
        } else if (event.getSource() == btnOk) {
            onOK();
        }
    }

    private void setValue(Filter_Mdl filter_Mdl) {

      
        txtFromDate.setText(DmShared.dNull(filter_Mdl.getFromDate()));
        txtToDate.setText(DmShared.dNull(filter_Mdl.getToDate()));
        txtHStatus.setText(DmShared.xNull(filter_Mdl.getStatus()));
        txtHStatus.setTag(DmShared.xNull(filter_Mdl.getStatusCode()));

    }

    private Filter_Mdl getValue() {
        Filter_Mdl filterMdl = new Filter_Mdl();
        filterMdl.setSharedKeyFieldCode(DmShared.dNull(txtToDate.getText()));
        filterMdl.setFromDate(DmShared.dNull(txtFromDate.getText()));
        filterMdl.setToDate(DmShared.dNull(txtToDate.getText()));
        filterMdl.setStatus(DmShared.xNull(txtHStatus.getText()));
        filterMdl.setStatusCode(DmShared.xNull(txtHStatus.getTag()));

        return filterMdl;
    }

    private void onOK() {
//        if (DmShared.xNull(txtEmployeeName.getText()).equalsIgnoreCase("")) {
//            new DmAlertMessageBox("Organization is mandatory ", txtEmployeeName).show();
//            return;
//        }

        hide();
        callBack.onOk(getValue());
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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
       
    }


    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public SearchPopup() {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(LeaveEntryApproval_Srv.class), LeaveEntryApproval.class.getName());
        textHelpEvents();
        handlerEvents();


    }

    private void handlerEvents() {
        popupPanel.addAttachHandler(this);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);

    }

   


}
