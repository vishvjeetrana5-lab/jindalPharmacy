package aarogya.master.earningDeductionEntry.client;

import aarogya.master.earningDeductionEntry.shared.EarningDeductionEntryGridEdit_Mdl;
import aarogya.master.earningDeductionEntry.shared.EarningDeductionEntry_Srv;
import aarogya.master.earningDeductionEntry.shared.EarningDeductionEntry_SrvAsync;
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

public class EarningDeductionEntryGridEdit<T1, T2> extends EarningDeductionEntryGridEdit_Dsg<T1, T2> implements BlurHandler {

    private final ServiceInvoker<EarningDeductionEntry_SrvAsync> srvAsync;
    EarningDeductionEntry gateEntry;
    private int hashCode;
    String selectedItem = "";

    public EarningDeductionEntryGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Earning Detail", "400px", "150px");
        txtHNature.setEnabled(false);
        txtHDescription.setEnabled(false);
        textHelpEvents();
        srvAsync = new ServiceInvoker<>(GWT.create(EarningDeductionEntry_Srv.class), EarningDeductionEntry.class.getName());
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, EarningDeductionEntry invc) {
        this.gateEntry = invc;

        super.showRelativeTo(currentRowIndex, 2, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHNature.setFocus(true);
            }
        });
        if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("")) {
            txtHType.setTag("1");
            txtHType.setText("Flat");
        }
    }

    @Override
    public void onOk() {

        if (DmShared.xNull(txtHNature.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Nature is mandatory", txtHNature).show();
            return;
        }
        if (DmShared.iNull(txtHDescription.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Description is mandatory", txtHDescription).show();
            return;
        }
        if (DmShared.xNull(txtAmount.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Amount is mandatory", txtAmount).show();
            return;
        }
        for (EarningDeductionEntryGridEdit_Mdl detail_Mdl : (List<EarningDeductionEntryGridEdit_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(detail_Mdl.getDespName()).equalsIgnoreCase(DmShared.xNull(txtHNature.getText()))) {
                if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox(DmShared.xNull(detail_Mdl.getDespName()) + " already exists.", txtHNature).show();
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

        EarningDeductionEntryGridEdit_Mdl ourRow = (EarningDeductionEntryGridEdit_Mdl) row;

//        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHDescription.getText()));
        ourRow.setNatureCode(DmShared.xNull(txtHNature.getTag()));
        ourRow.setNatureName(DmShared.xNull(txtHNature.getText()));
        ourRow.setDespCode(DmShared.xNull(txtHDescription.getTag()));
        ourRow.setDespName(DmShared.xNull(txtHDescription.getText()));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHDescription.getTag2nd()));
        ourRow.setTypeCode(DmShared.xNull(txtHType.getTag()));
        ourRow.setTypeName(DmShared.xNull(txtHType.getText()));
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(),"0.00"));

        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new EarningDeductionEntryGridEdit_Mdl();
    }

    @Override
    public void displayRow(T1 row) {

        EarningDeductionEntryGridEdit_Mdl ourRow = (EarningDeductionEntryGridEdit_Mdl) row;
        txtHDescription.setTag2nd(ourRow.getSharedKeyFieldCode());
        txtHDescription.setText(ourRow.getDespName());
        txtHDescription.setTag(ourRow.getDespCode());
        txtHNature.setText(ourRow.getNatureName());
        txtHNature.setTag(ourRow.getNatureCode());
        txtHType.setText(ourRow.getTypeName());
        txtHType.setTag(ourRow.getTypeCode());
        txtAmount.setText(ourRow.getAmount());
        hashCode = ourRow.getSharedKeyFieldHashCode();
    }

    private void textHelpEvents() {

        txtHDescription.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDescription(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDescription).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDescription.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDescription).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHType).show();
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

        public void onOk(EarningDeductionEntryGridEdit_Mdl opdService);

        public void onDelete();
    }
    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

}
