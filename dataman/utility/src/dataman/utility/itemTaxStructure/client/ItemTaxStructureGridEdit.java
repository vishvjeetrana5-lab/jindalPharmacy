package dataman.utility.itemTaxStructure.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.itemTaxStructure.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class ItemTaxStructureGridEdit<T1, T2> extends ItemTaxStructureGridEdit_Dsg<T1, T2> {

    interface CallBack {

        public void onOk(GrdItemTaxStructure_Mdl grdItemTaxStructure_Mdl);

        public void onDelete();
    }

    ServiceInvoker<ItemTaxStructure_SrvAsync> SrvAsync;
    CallBack callBack;
    int hashCode;
    private ItemTaxStructure itemTaxStructure;

    private void textHelpEvents() {
        txtHSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHAcntAffected.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getAccountAffected(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAcntAffected).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAcntAffected.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAcntAffected).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }

        });

        txtHLocation.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHLocation(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLocation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLocation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLocation).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHOverHead.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getOverhead(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOverHead).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOverHead.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOverHead).show();
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

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, ItemTaxStructure its) {
        this.itemTaxStructure = its;
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemTaxStructure_Mdl ourRow = (GrdItemTaxStructure_Mdl) row;

        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHOverHead.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHOverHead.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHSite.setTag(DmShared.xNull(ourRow.getSiteTag()));
        txtHSite.setText(DmShared.xNull(ourRow.getSiteText()));
        txtHAcntAffected.setTag(DmShared.xNull(ourRow.getAccntAffectedTag()));
        txtHAcntAffected.setText(DmShared.xNull(ourRow.getAccntAffectedText()));
        txtHLocation.setTag(DmShared.xNull(ourRow.getLocationTag()));
        txtHLocation.setText(DmShared.xNull(ourRow.getLocationText()));
        txtAmount.setText(DmShared.xNull(ourRow.getAmount()));
        txtAtRate.setText(DmShared.xNull(ourRow.getAtRate()));
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHLocation.setFocus(true);
            }
        });
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdItemTaxStructure_Mdl();
    }

    @Override
    public void onOk() {
        if (DmShared.iNull(txtHOverHead.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Location is mandatory.", txtHOverHead).show();
            return;
        }
        if (DmShared.xNull(txtHOverHead.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Overhead is mandatory.", txtHOverHead).show();
            return;
        }
        for (GrdItemTaxStructure_Mdl config_Mdl : (List<GrdItemTaxStructure_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if (DmShared.xNull(txtHOverHead.getTag()).equalsIgnoreCase(DmShared.xNull(config_Mdl.getSharedKeyFieldCode()))
                    && DmShared.xNull(txtHLocation.getTag()).equalsIgnoreCase(DmShared.xNull(config_Mdl.getLocationTag()))) {
                if (DmShared.xNull(config_Mdl.getSiteTag()).equalsIgnoreCase("")) {
                    if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(config_Mdl.getSharedKeyFieldHashCode()).doubleValue()) {
                        new DmAlertMessageBox("Location,Overhead and Site from combination already exist.", txtHLocation).show();
                        return;
                    }
                } else if (DmShared.xNull(config_Mdl.getSiteTag()).equalsIgnoreCase(DmShared.xNull(txtHSite.getTag()))) {
                    if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(config_Mdl.getSharedKeyFieldHashCode()).doubleValue()) {
                        new DmAlertMessageBox("Location,Overhead and Site from combination already exist.", txtHLocation).show();
                        return;
                    }
                }

            }
        }
        proceedOk();

    }

    @Override
    public void manageOk(T1 row) {
        GrdItemTaxStructure_Mdl ourRow = (GrdItemTaxStructure_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHOverHead.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHOverHead.getText()));
        ourRow.setSiteTag(DmShared.xNull(txtHSite.getTag()));
        ourRow.setSiteText(DmShared.xNull(txtHSite.getText()));
        ourRow.setAccntAffectedTag(DmShared.xNull(txtHAcntAffected.getTag()));
        ourRow.setAccntAffectedText(DmShared.xNull(txtHAcntAffected.getText()));
        ourRow.setLocationTag(DmShared.xNull(txtHLocation.getTag()));
        ourRow.setLocationText(DmShared.xNull(txtHLocation.getText()));
        ourRow.setAtRate(DmShared.xNull(txtAtRate.getText()));
        ourRow.setAmount(DmShared.xNull(txtAmount.getText()));
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

    public ItemTaxStructureGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Item Tax Structure", "440px", "160px");
        SrvAsync = new ServiceInvoker<>(GWT.create(ItemTaxStructure_Srv.class), ItemTaxStructure.class.getName());
        textHelpEvents();
    }
}
