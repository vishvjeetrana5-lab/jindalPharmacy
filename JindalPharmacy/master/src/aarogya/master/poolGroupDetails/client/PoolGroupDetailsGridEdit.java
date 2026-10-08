package aarogya.master.poolGroupDetails.client;

import aarogya.master.poolGroupDetails.shared.PoolGroupDetailsGridEdit_Mdl;
import aarogya.master.poolGroupDetails.shared.PoolGroupDetails_Srv;
import aarogya.master.poolGroupDetails.shared.PoolGroupDetails_SrvAsync;
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

public class PoolGroupDetailsGridEdit<T1, T2> extends PoolGroupDetailsGridEdit_Dsg<T1, T2> implements BlurHandler {

    private final ServiceInvoker<PoolGroupDetails_SrvAsync> srvAsync;
    PoolGroupDetails gateEntry;
    private int hashCode;
    String selectedItem = "";

    public PoolGroupDetailsGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Pool Detail", "400px", "150px");
        textHelpEvents();
        srvAsync = new ServiceInvoker<>(GWT.create(PoolGroupDetails_Srv.class), PoolGroupDetails.class.getName());
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, PoolGroupDetails invc) {
        this.gateEntry = invc;

        super.showRelativeTo(currentRowIndex, 2, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHConsultantName.setFocus(true);
            }
        });
    }

    @Override
    public void onOk() {
        if (DmShared.iNull(txtHConsultantName.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Consultant is mandatory", txtHConsultantName).show();
            return;
        }
        if (DmShared.xNull(txtHHeaderRefType.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("%/Fix is mandatory", txtHHeaderRefType).show();
            return;
        }
        if (DmShared.xNull(txtAmount.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Amount is mandatory", txtAmount).show();
            return;
        }
        for (PoolGroupDetailsGridEdit_Mdl detail_Mdl : (List<PoolGroupDetailsGridEdit_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(detail_Mdl.getSharedKeyFieldName()).equalsIgnoreCase(DmShared.xNull(txtHConsultantName.getText()))) {
                if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + " already exists.", txtHConsultantName).show();
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
        PoolGroupDetailsGridEdit_Mdl ourRow = (PoolGroupDetailsGridEdit_Mdl) row;

        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHConsultantName.getText()));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHConsultantName.getTag()));
        ourRow.setValueName(DmShared.xNull(txtHHeaderRefType.getText()));
        ourRow.setValueCode(DmShared.xNull(txtHHeaderRefType.getTag()));
        ourRow.setAmount(DmShared.xNull(txtAmount.getText()));

        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new PoolGroupDetailsGridEdit_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        PoolGroupDetailsGridEdit_Mdl ourRow = (PoolGroupDetailsGridEdit_Mdl) row;

        txtHConsultantName.setText(ourRow.getSharedKeyFieldName());
        txtHConsultantName.setTag(ourRow.getSharedKeyFieldCode());

        txtHHeaderRefType.setTag(ourRow.getValueCode());
        txtHHeaderRefType.setText(ourRow.getValueName());
        txtAmount.setText(ourRow.getAmount());
        hashCode = ourRow.getSharedKeyFieldHashCode();
    }

    private void textHelpEvents() {
        txtHConsultantName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsultantName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsultantName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsultantName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHHeaderRefType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHeaderRefType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHHeaderRefType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHeaderRefType).show();
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
//        if (event.getSource() == txtQty) {
//
//        } else if (event.getSource() == txtRemark) {
//
//        }
    }

    interface CallBack {

        public void onOk(PoolGroupDetailsGridEdit_Mdl opdService);

        public void onDelete();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
