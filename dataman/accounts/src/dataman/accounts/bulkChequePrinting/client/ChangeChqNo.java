package dataman.accounts.bulkChequePrinting.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.accounts.bulkChequePrinting.shared.BulkChequePrinting_Srv;
import dataman.accounts.bulkChequePrinting.shared.BulkChequePrinting_SrvAsync;
import dataman.accounts.bulkChequePrinting.shared.ChangeChqNo_Mdl;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class ChangeChqNo extends ChangeChqNo_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler, KeyPressHandler {

    ServiceInvoker<BulkChequePrinting_SrvAsync> SrvAsync;

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getSource().equals(txtChequeNo)) {
            switch (event.getNativeEvent().getKeyCode()) {
                case KeyCodes.KEY_TAB:
                case KeyCodes.KEY_BACKSPACE:
                case KeyCodes.KEY_DELETE:
                case KeyCodes.KEY_LEFT:
                case KeyCodes.KEY_RIGHT:
                case KeyCodes.KEY_UP:
                case KeyCodes.KEY_DOWN:
                case KeyCodes.KEY_END:
                case KeyCodes.KEY_ENTER:
                case KeyCodes.KEY_ESCAPE:
                case KeyCodes.KEY_PAGEDOWN:
                case KeyCodes.KEY_PAGEUP:
                case KeyCodes.KEY_HOME:
                case KeyCodes.KEY_SHIFT:
                case KeyCodes.KEY_ALT:
                case KeyCodes.KEY_CTRL:
                    break;
                default:

                    if (event.isAltKeyDown() || (event.isControlKeyDown() && (event.getCharCode() != 'v' && event.getCharCode() != 'V'))) {
                        break;
                    }
                    if (!Character.isDigit(event.getCharCode())) {
                        event.preventDefault();
                    }
            }
        }
    }

    interface CallBack {

        public void onOk(ChangeChqNo_Mdl values);
    }
    CallBack callBack;
    ChangeChqNo_Mdl changeChqNo_Mdl;

    public ChangeChqNo() {
        super();
        SrvAsync = new ServiceInvoker<BulkChequePrinting_SrvAsync>(GWT.create(BulkChequePrinting_Srv.class), BulkChequePrinting.class.getName());
        handlerEvents();
        textHelpEvents();
        toolContainer.setTitle("Change Details");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
    }

    private void handlerEvents() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        txtChequeDate.addBlurHandler(this);
        txtChequeNo.addKeyPressHandler(this);
        popupPanel.addAttachHandler(this);
    }

    private void textHelpEvents() {
        txtHYesNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHYesNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHYesNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHYesNo).show();
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
            if (DmShared.dNull(txtChequeDate.getText()).equalsIgnoreCase("") && DmShared.iNull(txtChequeNo.getText()).longValue() == 0) {
                new DmAlertMessageBox("Cheque No. or Cheque Date is mandatory", txtChequeDate).show();
                return;
            }
            if (DmShared.xNull(txtHYesNo.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Cross Cheque", txtHYesNo).show();
                return;
            }
            new DmConfirmMessageBox("Are you sure? You want to update this in all the records.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        fillRecords();
                    }
                }
            }).show();

        }
    }

    private void fillRecords() {
        changeChqNo_Mdl = new ChangeChqNo_Mdl();
        changeChqNo_Mdl.setChqDate(DmShared.dNull(txtChequeDate.getText()));
        changeChqNo_Mdl.setChqNo(DmShared.xNull(txtChequeNo.getText()).toString());
        changeChqNo_Mdl.setIsCrossChqCode(DmShared.xNull(txtHYesNo.getTag()).toString());
        changeChqNo_Mdl.setIsCrossChqText(DmShared.xNull(txtHYesNo.getText()).toString());
        callBack.onOk(changeChqNo_Mdl);
        hide();
    }

    public void show() {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        if (DmShared.xNull(txtHYesNo.getTag()).equalsIgnoreCase("")) {
            txtHYesNo.setTag("1");
            txtHYesNo.setText("Yes");
        }
        initialize();
    }

    private void initialize() {

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtChequeNo.setFocus(true);
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtChequeDate)) {
            txtChequeDate.setText(DmClient.returnDate(txtChequeDate.getText()));
        }
    }

}
