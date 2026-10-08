package aarogya.inventory.gateOut.client;

import aarogya.inventory.gateOut.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class GateGridEdit<T1, T2> extends GateGridEdit_Dsg<T1, T2> implements BlurHandler {

    private final ServiceInvoker<GateOut_SrvAsync> srvAsync;
    GateOut gateOut;
    private int hashCode;
    String selectedItem = "", QtyBlur = "", estmValueBlur = "";

    public GateGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Item Detail", "400px", "190px");
        textHelpEvents();
        srvAsync = new ServiceInvoker<>(GWT.create(GateOut_Srv.class), GateOut.class.getName());
        manageEvents();
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, GateOut invc) {
        this.gateOut = invc;

        super.showRelativeTo(currentRowIndex, 2, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtItem.setFocus(true);
            }
        });
    }

    @Override
    public void onOk() {

        for (GateOutGridEdit_Mdl detail_Mdl : (List<GateOutGridEdit_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(detail_Mdl.getItemDescription()).equalsIgnoreCase(DmShared.xNull(txtItem.getText()))) {
                if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox(DmShared.xNull(detail_Mdl.getItemDescription()) + " already exists.", txtItem).show();
                    return;
                }
            }

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
        GateOutGridEdit_Mdl ourRow = (GateOutGridEdit_Mdl) row;

        ourRow.setItemDescription(DmShared.xNull(txtItem.getText()));
        ourRow.setQuantity(DmShared.xNull(txtQty.getText()));
        ourRow.setUnitCode(DmShared.xNull(txtHUnit.getTag()));
        ourRow.setUnitName(DmShared.xNull(txtHUnit.getText()));
        ourRow.setSpecification(DmShared.xNull(txtSpecification.getText()));
        ourRow.setEstdValue(DmShared.xNull(txtEstmValue.getText()));
        ourRow.setAssetCode(DmShared.xNull(txtHAsset.getTag()));
        ourRow.setAssetName(DmShared.xNull(txtHAsset.getText()));

        ourRow.setGateInDate(DmShared.dNull(txtInDate.getText()));
        ourRow.setGateInTime(DmShared.xNull(txtInTime.getText()));
        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GateOutGridEdit_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GateOutGridEdit_Mdl ourRow = (GateOutGridEdit_Mdl) row;
        manageDisplay(false);
        txtItem.setText(ourRow.getItemDescription());
        txtQty.setText(ourRow.getQuantity());
        txtHUnit.setTag(ourRow.getUnitCode());
        txtHUnit.setText(ourRow.getUnitName());
        txtSpecification.setText(ourRow.getSpecification());
        txtEstmValue.setText(ourRow.getEstdValue());
        txtHAsset.setTag(ourRow.getAssetCode());
        txtHAsset.setText(ourRow.getAssetName());

        txtInDate.setText(ourRow.getGateInDate());
        txtInTime.setText(ourRow.getGateInTime());
        QtyBlur = DmShared.xNull(ourRow.getQuantity());
        estmValueBlur = DmShared.xNull(ourRow.getEstdValue());
        hashCode = ourRow.getSharedKeyFieldHashCode();
    }

    private void textHelpEvents() {

        txtHUnit.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHUnit(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUnit).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHUnit.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHUnit).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });

        txtHAsset.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAsset(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAsset).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAsset.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAsset).show();
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
    public void onBlur(BlurEvent event) {
        
            if (event.getSource() == txtQty) {
                if (DmShared.iNull(STEnum.GateType.PREVIOUS_GATE_IN_RETURNABLE.getId()).toString().equalsIgnoreCase(DmShared.xNull(gateOut.txtHType.getTag()))) {
                if (DmShared.iNull(txtQty.getText()).intValue() > 0) {
                    txtEstmValue.setText(DmClient.vNull((DmShared.vNull(estmValueBlur).doubleValue() / DmShared.vNull(QtyBlur).doubleValue()) * DmShared.vNull(txtQty.getText()), "0.00"));
                }
            }
        }

    }

    interface CallBack {

        public void onOk(GateOutGridEdit_Mdl opdService);

        public void onDelete();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void manageDisplay(boolean flag) {

        if (DmShared.iNull(STEnum.GateType.PREVIOUS_GATE_IN_RETURNABLE.getId()).toString().equalsIgnoreCase(DmShared.xNull(gateOut.txtHType.getTag()))) {
            txtItem.setEnabled(flag);
            txtHUnit.setEnabled(flag);
            txtSpecification.setEnabled(flag);
            txtHAsset.setEnabled(flag);

        } else if (DmShared.xNull(gateOut.topControl.getMode()).trim().equalsIgnoreCase("Edit")
                && DmShared.iNull(STEnum.GateType.PREVIOUS_GATE_IN_RETURNABLE.getId()).toString().equalsIgnoreCase(DmShared.xNull(gateOut.txtHType.getTag()))) {
            txtItem.setEnabled(flag);
            txtHUnit.setEnabled(flag);
            txtSpecification.setEnabled(flag);
            txtHAsset.setEnabled(flag);

        } else {
            txtItem.setEnabled(true);
            txtHUnit.setEnabled(true);
            txtSpecification.setEnabled(true);
            txtHAsset.setEnabled(true);
        }
        txtInDate.setEnabled(false);
        txtInTime.setEnabled(false);
    }

    private void manageEvents() {
        txtQty.addBlurHandler(this);
    }
}
