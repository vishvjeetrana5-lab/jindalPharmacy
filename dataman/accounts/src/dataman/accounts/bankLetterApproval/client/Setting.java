package dataman.accounts.bankLetterApproval.client;


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
import dataman.dmBase.self.shared.*;
import java.util.List;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.accounts.bankLetterApproval.shared.*;


public class Setting<T1, T2> extends Setting_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurEvent.BlurHandler {

    interface RequisitionApprovalEntryCallBack {

        public void onOk(DefaultValue_Mdl approvalEntry_Mdl);
    }

    RequisitionApprovalEntryCallBack requisitionApprovalEntryCallBack;
 
    ServiceInvoker<BankLetterApproval_SrvAsync> SrvAsync;

    public void addRequisitionApprovalEntryCallBackCallBack(RequisitionApprovalEntryCallBack requisitionApprovalEntryCallBack) {
        this.requisitionApprovalEntryCallBack = requisitionApprovalEntryCallBack;
    }

    public void show(DefaultValue_Mdl requisitionApproval, boolean editable) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtFromDate.setFocus(true);
        flwOk.clear();
        flwOk.setVisible(false);

        if (editable) {
            flwOk.add(btnOk);
            flwOk.setVisible(true);
        }
        onDsiplay(requisitionApproval);
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void txtKeyEvents() {

        txtFromDate.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
            }
        });

        txtToDate.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
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

//        txtHFromLocation.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
//
//            @Override
//            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
//                SrvAsync.invokeRPC(blnCallBackWait).getTxtHToLocation(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        new DmAlertMessageBox(caught.getMessage(), txtHFromLocation).show();
//                    }
//
//                    @Override
//                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
//                        if (result.isStatus()) {
//                            txtHFromLocation.setData(result.getData(), result.getPaging());
//                        } else {
//                            new DmAlertMessageBox(result.getErrorMessage(), txtHFromLocation).show();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
//                if (blnStatus) {
//
//                }
//            }
//        });

    }

    @Override
    public void onClick(ClickEvent event) {

        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {

            if (!DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("") && !DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
                if ((DmShared.convertDate(txtToDate.getText()).getTime() < (DmShared.convertDate(txtFromDate.getText())).getTime())) {
                    new DmAlertMessageBox("From Date should be less than or equal to To Date.", txtFromDate).show();
                    return;
                }
            }
            fillRecords();

        }
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    private void fillRecords() {
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
     
        mrTemp.setFromDate(DmShared.xNull(txtFromDate.getText()));
        mrTemp.setToDate(DmShared.xNull(txtToDate.getText()));
//
//        mrTemp.setLstLocationCode(DmShared.xNull(txtHFromLocation.getTag()));
//        mrTemp.setLstLocationName(DmShared.xNull(txtHFromLocation.getText()));

        mrTemp.setBillStatusCode(DmShared.xNull(txtHStatus.getTag()));
        mrTemp.setBillStatusName(DmShared.xNull(txtHStatus.getText()));

        requisitionApprovalEntryCallBack.onOk(mrTemp);
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
      
        SrvAsync = new ServiceInvoker<BankLetterApproval_SrvAsync>(GWT.create(BankLetterApproval_Srv.class), BankLetterApproval.class.getName());
       
        
        textHelpEvents();
        txtKeyEvents();
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Setting");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();

        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    private void onDsiplay(DefaultValue_Mdl dvm) {
        if (dvm == null) {
            txtToDate.setText(DmClient.getLoginDate());
            if (DmShared.xNull(txtHStatus.getTag()).equalsIgnoreCase("")) {
                txtHStatus.setTag("P");
                txtHStatus.setText("Pending");
            }
        } else {
            txtFromDate.setText(dvm.getFromDate());
            txtToDate.setText(dvm.getToDate());

            txtHStatus.setTag(dvm.getBillStatusCode());
            txtHStatus.setText(dvm.getBillStatusName());

        }

    }

}
