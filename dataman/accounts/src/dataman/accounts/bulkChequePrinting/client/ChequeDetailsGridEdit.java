package dataman.accounts.bulkChequePrinting.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.accounts.bulkChequePrinting.shared.BulkChequePrinting_Srv;
import dataman.accounts.bulkChequePrinting.shared.BulkChequePrinting_SrvAsync;
import dataman.accounts.bulkChequePrinting.shared.GrdBulkChequePrinting_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class ChequeDetailsGridEdit<T1, T2> extends ChequeDetailsGridEdit_Dsg<T1, T2> implements BlurHandler, KeyPressHandler {

    ServiceInvoker<BulkChequePrinting_SrvAsync> SrvAsync;

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtChequeDate)) {
            txtChequeDate.setText(DmClient.returnDate(txtChequeDate.getText()));
        }
    }

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

        public void onOk(GrdBulkChequePrinting_Mdl grdEditEntry);

        public void onDelete();
    }

    CallBack callBack;

    public ChequeDetailsGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Cheque Detail", "400px", "100px");
        SrvAsync = new ServiceInvoker<BulkChequePrinting_SrvAsync>(GWT.create(BulkChequePrinting_Srv.class), BulkChequePrinting.class.getName());
        textHelpEvents();
        txtChequeDate.addBlurHandler(this);
        txtChequeNo.addKeyPressHandler(this);
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

    private void clear() {
        txtPrintName.setTag2nd("");
        txtPrintName.setTag("");
        txtPrintName.setText("");
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtPrintName.setFocus(true);
            }
        });

    }

    @Override
    public void onOk() {
        if (DmShared.xNull(txtPrintName.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Favor Of is mandatory", txtPrintName).show();
            return;
        }
        if (DmShared.xNull(txtHYesNo.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Cross Cheque is mandatory", txtHYesNo).show();
            return;
        }
        new DmConfirmMessageBox("Are you sure? You want to update this in all the records.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    proceedOk();
                }
            }
        }).show();

    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        proceedDelete();
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageOk(T1 row) {
        GrdBulkChequePrinting_Mdl ourRow = (GrdBulkChequePrinting_Mdl) row;
        ourRow.setPrintName(DmShared.xNull(txtPrintName.getText()));
        if (!DmShared.xNull(txtChequeNo.getText()).equalsIgnoreCase("")) {
            ourRow.setChqNo(DmShared.xNull(txtChequeNo.getText()));
        }
        if (!DmShared.dNull(txtChequeDate.getText()).equalsIgnoreCase("")) {
            ourRow.setChqDate(DmShared.dNull(txtChequeDate.getText()));
        }
        ourRow.setIsCrossChqCode(DmShared.xNull(txtHYesNo.getTag()));
        ourRow.setIsCrossChqText(DmShared.xNull(txtHYesNo.getText()));
        callBack.onOk(ourRow);

    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdBulkChequePrinting_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        clear();
        GrdBulkChequePrinting_Mdl ourRow = (GrdBulkChequePrinting_Mdl) row;
        txtPrintName.setText(DmShared.xNull(ourRow.getPrintName()));
        if (!DmShared.xNull(ourRow.getChqNo()).equalsIgnoreCase("")) {
            txtChequeNo.setText(DmShared.xNull(ourRow.getChqNo()));
        }
        if (!DmShared.dNull(ourRow.getChqDate()).equalsIgnoreCase("")) {
            txtChequeDate.setText(DmShared.dNull(ourRow.getChqDate()));
        }
        txtHYesNo.setTag(DmShared.iNull(ourRow.getIsCrossChqCode()).toString());
        txtHYesNo.setText(DmShared.xNull(ourRow.getIsCrossChqText()));
    }

}
