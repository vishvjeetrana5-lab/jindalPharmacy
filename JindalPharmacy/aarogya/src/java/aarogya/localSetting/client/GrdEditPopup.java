package aarogya.localSetting.client;

import dataman.dmBase.self.shared.DmLocalSettingPrintMap_Mdl;
import aarogya.localSetting.shared.LocalSetting_Srv;
import aarogya.localSetting.shared.LocalSetting_SrvAsync;
import aarogya.localSetting.shared.TxtHMenuText_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;

public class GrdEditPopup<T1, T2> extends GrdEditPopup_Dsg<T1, T2> implements BlurHandler, KeyUpHandler {

    private final ServiceInvoker<LocalSetting_SrvAsync> SrvAsync;

    interface CallBack {

        public void onOk(DmLocalSettingPrintMap_Mdl node);

        public void onDelete();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, boolean isDeleteFocus) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHMenuText.setFocus(true);
            }

        });
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            onOk();
        }

    }

    @Override
    public void onOk() {

        if (DmShared.xNull(txtHMenuText.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Menu Text is mandatory ", txtHMenuText).show();
            return;
        }
        if (DmShared.xNull(txtReportName.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Report Name is mandatory ", txtReportName).show();
            return;
        }

        proceedOk();
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

        DmLocalSettingPrintMap_Mdl ourRow = (DmLocalSettingPrintMap_Mdl) row;

        ourRow.setModuleName(txtModule.getText());
        ourRow.setMenuText(txtHMenuText.getText());
        ourRow.setReportName(txtReportName.getText());

        callBack.onOk(ourRow);

    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        DmLocalSettingPrintMap_Mdl ourRow = new DmLocalSettingPrintMap_Mdl();
        ourRow.setFieldUniqueCode(String.valueOf(getDmGrid().getGrid().getStore().size() + 1));
        return (T1) ourRow;
    }

    @Override
    public void displayRow(T1 row) {

        DmLocalSettingPrintMap_Mdl ourRow = (DmLocalSettingPrintMap_Mdl) row;

        txtModule.setText(ourRow.getModuleName());
        txtModule.setTag(ourRow.getModuleName());
        txtHMenuText.setText(ourRow.getMenuText());
        txtHMenuText.setTag(ourRow.getMenuText());
        txtReportName.setText(ourRow.getReportName());

    }

    private void textHelpEvents() {

        txtHMenuText.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMenuText_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMenuText(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMenuText_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMenuText).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMenuText_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMenuText.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHMenuText).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMenuText_Mdl tRow) {
                if (blnStatus) {
                    if (tRow != null) {

                        txtModule.setText(DmShared.xNull(tRow.getMenuModuleName()));
                        txtReportName.setFocus(true);
                    }
                }
            }
        });

    }

    @Override
    public void onKeyUp(KeyUpEvent event) {

    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    public GrdEditPopup(DmGrid<T1, T2> grid) {
        super(grid, "Print Menu", "400px", "116px");

        SrvAsync = new ServiceInvoker<LocalSetting_SrvAsync>(GWT.create(LocalSetting_Srv.class), LocalSetting.class.getName());

        textHelpEvents();
        txtModule.setEnabled(false);

    }

}
