package dataman.accounts.chequePrintSetup.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import dataman.accounts.bulkChequePrinting.shared.ChangeChqNo_Mdl;
import dataman.accounts.chequePrintSetup.shared.BankName_Mdl;
import dataman.accounts.chequePrintSetup.shared.ChequePrintSetup_Mdl;
import dataman.accounts.chequePrintSetup.shared.ChequePrintSetup_Srv;
import dataman.accounts.chequePrintSetup.shared.ChequePrintSetup_SrvAsync;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class CopyPopup extends CopyPopup_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    ServiceInvoker<ChequePrintSetup_SrvAsync> SrvAsync;

    interface CallBack {

        public void onOk(ChequePrintSetup_Mdl values);
    }
    CallBack callBack;
    ChequePrintSetup_Mdl mdlCopy;

    public CopyPopup() {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(ChequePrintSetup_Srv.class), ChequePrintSetup.class.getName());
        textHelpEvents();
        handlerEvents();
        toolContainer.setTitle("Copy");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
    }

    private void handlerEvents() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

    public void show() {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        initialize();
    }

    private void initialize() {

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHBankName.setFocus(true);
            }
        });
    }

    private void textHelpEvents() {

        txtHBankName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBankName("", true, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<BankName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBankName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<BankName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBankName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBankName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void hide() {
        if (callBack != null) {
            this.popupPanel.hide();
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
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
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide();
        } else if (event.getSource() == btnOk) {
            if (DmShared.iNull(txtHBankName.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Bank Name is mandatory", txtHBankName).show();
                return;
            }
            getRecord();

        }
    }

    private void getRecord() {
        SrvAsync.invokeRPC().getMoveRec("", txtHBankName.getTag(), true, new AsyncCallback<DmResult<ChequePrintSetup_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ChequePrintSetup_Mdl> result) {
                fillRecords(result);
            }
        });
    }

    private void fillRecords(DmResult<ChequePrintSetup_Mdl> result) {
        mdlCopy = new ChequePrintSetup_Mdl();
        if (result.isStatus()) {
            mdlCopy = result.getData();
            mdlCopy.setSharedKeyFieldName("");
            mdlCopy.setSharedKeyFieldCode("");
            mdlCopy.setSharedModifiedBy("");
            mdlCopy.setSharedPreparedBy("");
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        callBack.onOk(mdlCopy);
        hide();
    }

}
