package dataman.utility.numberSystem.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.numberSystem.shared.NumberSystem_SrvAsync;
import dataman.utility.numberSystem.shared.NumberSystem_Srv;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.numberSystem.shared.GrdCounter_Mdl;

import java.util.List;

public class CounterGridEdit<T1, T2> extends CounterGridEdit_Dsg<T1, T2> implements BlurHandler, KeyPressHandler {

    interface CallBack {

        public void onOk(GrdCounter_Mdl linkedUserGrid);

        public void onDelete(GrdCounter_Mdl linkedUserGrid);
    }
    private ServiceInvoker<NumberSystem_SrvAsync> SrvAsync;
    private CallBack callBack;
    private int hashCode;
    private NumberSystem numberSystem;

    public void textHelpEvent() {
        txtHSiteCode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSiteCode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSiteCode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHSiteCode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus) {

                }
            }
        });

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, NumberSystem numberSystem) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.numberSystem = numberSystem;
        txtNumberFormat.setFocus(true);
    }

    @Override
    public void displayRow(T1 row) {
        GrdCounter_Mdl ourRow = (GrdCounter_Mdl) row;
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtNumberFormat.setText(DmShared.xNull(ourRow.getNumberFormat()));
        txtHSiteCode.setTag(DmShared.xNull(ourRow.getSiteCode()));
        txtHSiteCode.setText(DmShared.xNull(ourRow.getSiteName()));
        txtCounter.setText(DmShared.xNull(ourRow.getCounterFormat()));
        txtvoucherPrefix.setText(DmShared.xNull(ourRow.getVoucherPre()));
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdCounter_Mdl();
    }

    @Override
    public void onOk() {
        new DmConfirmMessageBox(" Do you want to save this record ?", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    for (GrdCounter_Mdl keyNode : (List<GrdCounter_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                        if (hashCode != keyNode.getSharedKeyFieldHashCode()) {
                            if (DmShared.xNull(txtNumberFormat.getText()).equalsIgnoreCase(DmShared.xNull(keyNode.getNumberFormat()))
                                    && (DmClient.vNull(txtvoucherPrefix.getText(), "0").equalsIgnoreCase(DmShared.xNull(keyNode.getVoucherPre())))
                                    && (DmShared.xNull(txtHSiteCode.getTag()).equalsIgnoreCase(DmShared.xNull(keyNode.getSiteCode())))) {
                                new DmAlertMessageBox("This  Number Format already exists.", txtNumberFormat).show();
                                return;
                            }
                        }
                    }
                    proceedOk();
                }
            }
        }).show();
    }

    @Override
    public void manageOk(T1 row) {
        GrdCounter_Mdl ourRow = (GrdCounter_Mdl) row;
        ourRow.setNumberFormat(DmShared.xNull(txtNumberFormat.getText()));
        ourRow.setCounterFormat(DmClient.vNull(txtCounter.getText(), "0"));
        ourRow.setSiteCode(DmShared.xNull(txtHSiteCode.getTag()));
        ourRow.setSiteName(DmShared.xNull(txtHSiteCode.getText()));
        ourRow.setVoucherPre(DmClient.vNull(txtvoucherPrefix.getText(), "0"));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(numberSystem.txtHNSVtype.getTag()));

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        new DmConfirmMessageBox("Are you sure? You want to permanent delete this record. ", new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    proceedDelete();

                }
            }
        }).show();
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        GrdCounter_Mdl ourRow = new GrdCounter_Mdl();
        ourRow.setNumberFormat(DmShared.xNull(txtNumberFormat.getText()));
        ourRow.setCounterFormat(DmClient.vNull(txtCounter.getText(), "0"));
        ourRow.setSiteCode(DmShared.xNull(txtHSiteCode.getTag()));
        ourRow.setSiteName(DmShared.xNull(txtHSiteCode.getText()));
        ourRow.setVoucherPre(DmClient.vNull(txtvoucherPrefix.getText(), "0"));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(numberSystem.txtHNSVtype.getTag()));
        callBack.onDelete(ourRow);
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getSource().equals(txtCounter)) {
            try {
                int keyCode = event.getCharCode();
                if ((keyCode > 31) && ((keyCode != 48) && (keyCode != 45 && keyCode != 44))) {
                    txtCounter.cancelKey();
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtNumberFormat)) {
            String str = DmShared.xNull(txtNumberFormat.getText()).trim();
            txtNumberFormat.setText(str.replaceAll("\\s+", ""));
        }
    }

    public CounterGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Edit Number Format", "400px", "130px");
        SrvAsync = new ServiceInvoker<NumberSystem_SrvAsync>(GWT.create(NumberSystem_Srv.class), NumberSystem.class.getName());
        textHelpEvent();
        txtNumberFormat.addBlurHandler(this);
    }
}
