package aarogya.localSetting.client;

import aarogya.localSetting.shared.GrdLabTestCodification_Mdl;
import aarogya.localSetting.shared.LocalSetting_Srv;
import aarogya.localSetting.shared.LocalSetting_SrvAsync;

import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class GrdEditTestCodification<T1, T2> extends GrdEditTestCodification_Dsg<T1, T2> implements ClickHandler {

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            onOk();
        }
    }

    interface CallBack {

        public void onOk(GrdLabTestCodification_Mdl machineConfig);

        public void onDelete();
    }

    private final ServiceInvoker<LocalSetting_SrvAsync> SrvAsync;
    private CallBack callBack;
    private LocalSetting localSettings;
    private int hashCode = 0;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, final boolean isDeleteFocus) {
        this.localSettings = localSettings;
        //super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtMachineTestName.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdLabTestCodification_Mdl ourRow = (GrdLabTestCodification_Mdl) row;

        txtMachineTestName.setText(DmShared.xNull(ourRow.getMachineTestName()));
        txtManualTestName.setText(DmShared.xNull(ourRow.getManualName()));
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtMachineTestName.setFocus(true);
    }

    private void textHelpEvents() {

        grdHSubTest.addHelpGridCallBack(new DmHelpGrid.HelpGridCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSubTest(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtManualTestName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            grdHSubTest.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtManualTestName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    fillManualTestName(t1Row);
                }
            }
        });

    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdLabTestCodification_Mdl();
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtMachineTestName.getText().trim()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Machine Subtest Name is mandatory ", txtMachineTestName).show();
                return;
            }
            if (DmShared.xNull(txtManualTestName.getText().trim()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Our Subtest Name is mandatory ", txtManualTestName).show();
                return;
            }

            for (GrdLabTestCodification_Mdl service : (List<GrdLabTestCodification_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (service.getMachineTestName() != null) {
                    if ((DmShared.xNull(service.getMachineTestName()).trim().equalsIgnoreCase(DmShared.xNull(txtMachineTestName.getText()).trim()))
                            && DmShared.xNull(service.getManualName()).trim().equalsIgnoreCase(DmShared.xNull(txtManualTestName.getText()).trim())) {
                        if (!DmClient.vNull(service.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                            new DmAlertMessageBox("Combination of Machine Subtest & Our Subtest Name already exist.", txtMachineTestName).show();
                            return;
                        }
                    }
                }
            }

            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtMachineTestName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdLabTestCodification_Mdl ourRow = (GrdLabTestCodification_Mdl) row;

        ourRow.setMachineTestName(DmShared.removeSpace(DmShared.xNull(txtMachineTestName.getText().trim())));
        ourRow.setManualName(DmShared.removeSpace(DmShared.xNull(txtManualTestName.getText().trim())));

        callBack.onOk(ourRow);
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
    public void manageDelete() {
        callBack.onDelete();
    }

    public GrdEditTestCodification(DmGrid<T1, T2> grid) {
        super(grid, "Machine Subtest Mapping", "600px", "80px");
        SrvAsync = new ServiceInvoker<LocalSetting_SrvAsync>(GWT.create(LocalSetting_Srv.class), LocalSetting.class.getName());
        textHelpEvents();
        btnSubTestSearch.addClickHandler(this);
    }

    @Override
    public void onClick(ClickEvent event) {
        super.onClick(event);
        if (event.getSource() == btnSubTestSearch) {
            grdHSubTest.show(null, "");
            grdHSubTest.reQuery("");
        }
    }

    private void fillManualTestName(KeyFieldExtended t1Row) {
        if (!DmShared.xNull(t1Row.getSharedPreparedBy()).equalsIgnoreCase("")) {
            txtManualTestName.setText(DmShared.xNull(t1Row.getSharedPreparedBy()));
        } else {
            txtManualTestName.setText(DmShared.xNull(t1Row.getSharedKeyFieldName()));
        }

    }
}
