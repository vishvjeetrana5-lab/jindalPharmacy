package aarogya.pharmacy.medicineMaster.client;

import aarogya.pharmacy.medicineMaster.shared.*;
import aarogya.pharmacy.medicineMaster.shared.MedicineMaster_Srv;
import aarogya.pharmacy.medicineMaster.shared.MedicineMaster_SrvAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class SchmeGridEdit<T1, T2> extends SchmeGridEdit_Dsg<T1, T2> implements ClickHandler, ValueChangeHandler<String> {

    ServiceInvoker<MedicineMaster_SrvAsync> srvAsync;
    CallBack callBack;
    String v_SNo = "";
    int hashCode;

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        if (event.getSource().equals(txtSchemeFreeQty) || event.getSource().equals(txtSchemeOnQty)) {
            setDescription(true);
        }
    }

    interface CallBack {

        public void onOk(GrdSchemeDetails_Mdl details_Mdl);

        public void onDelete();
    }

    public SchmeGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Scheme Details", "450px", "140px");
        srvAsync = new ServiceInvoker<>(GWT.create(MedicineMaster_Srv.class), MedicineMaster.class.getName());
        textHelpEvents();
        handlerEvents();
    }

    private void textHelpEvents() {

        txtHMedicineName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemsName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemName("", true, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemsName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItemsName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMedicineName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHItemsName_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtUnit.setTag(t1Row.getSaleUnitCode());
                    txtUnit.setText(t1Row.getSaleUnitName());
                } else {
                    txtUnit.setTag("");
                    txtUnit.setText("");
                }
            }
        });
    }

    private void handlerEvents() {
        txtSchemeFreeQty.addValueChangeHandler(this);
        txtSchemeOnQty.addValueChangeHandler(this);
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHMedicineName.setFocus(true);
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onOk() {
        setDescription(false);
        if (DmShared.iNull(txtHMedicineName.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Item Name is mandatory.", txtHMedicineName).show();
            return;
        }
        if (DmShared.iNull(txtUnit.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Unit is mandatory.", txtHMedicineName).show();
            return;
        }
        if (DmShared.vNull(txtSchemeOnQty.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Scheme On Qty. is mandatory.", txtSchemeFreeQty).show();
            return;
        }
        if (DmShared.vNull(txtSchemeFreeQty.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Scheme Free Qty. is mandatory.", txtSchemeFreeQty).show();
            return;
        }
        if (DmShared.xNull(txtDescription.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Description is mandatory.", txtDescription).show();
            return;
        }
        List<GrdSchemeDetails_Mdl> lstTemp = (List<GrdSchemeDetails_Mdl>) getDmGrid().getGrid().getStore().getAll();
        for (int i = 0; i < lstTemp.size(); i++) {
            GrdSchemeDetails_Mdl get = lstTemp.get(i);
            if (DmShared.iNull(txtHMedicineName.getTag()).longValue() == DmShared.iNull(get.getSharedKeyFieldCode()).longValue()) {
                if (DmShared.vNull(hashCode).doubleValue() != DmShared.vNull(get.getSharedKeyFieldHashCode()).doubleValue()) {
                    new DmAlertMessageBox(txtHMedicineName.getText() + " already exists.", txtHMedicineName).show();
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
        GrdSchemeDetails_Mdl ourRow = (GrdSchemeDetails_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHMedicineName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHMedicineName.getText()));
        ourRow.setDescription(DmShared.xNull(txtDescription.getText()));
        ourRow.setFreeItemUnitCode(DmShared.iNull(txtUnit.getTag()).toString());
        ourRow.setFreeItemUnitName(DmShared.xNull(txtUnit.getText()));
        ourRow.setSchemeFreeQty(DmShared.xNull(txtSchemeFreeQty.getText()));
        ourRow.setSchemeOnQty(DmShared.xNull(txtSchemeOnQty.getText()));
        ourRow.setV_SNo(DmShared.iNull(v_SNo).toString());

        callBack.onOk(ourRow);
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdSchemeDetails_Mdl();
    }

    @Override
    public void displayRow(T1 row) {
        GrdSchemeDetails_Mdl ourRow = (GrdSchemeDetails_Mdl) row;

        hashCode = ourRow.getSharedKeyFieldHashCode();
        v_SNo = ourRow.getV_SNo();
        txtHMedicineName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHMedicineName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtSchemeFreeQty.setText(DmShared.vNull(ourRow.getSchemeFreeQty()).toString());
        txtSchemeOnQty.setText(DmShared.vNull(ourRow.getSchemeOnQty()).toString());
        txtUnit.setText(ourRow.getFreeItemUnitName());
        txtUnit.setTag(ourRow.getFreeItemUnitCode());
        txtDescription.setText(ourRow.getDescription());

        txtHMedicineName.setFocus(true);
    }

    private void setDescription(boolean isValueChanged) {
        if (DmShared.vNull(txtSchemeOnQty.getText()).doubleValue() > 0 && DmShared.vNull(txtSchemeOnQty.getText()).doubleValue() > 0) {
            if (isValueChanged || DmShared.xNull(txtDescription.getText()).trim().equalsIgnoreCase("")) {
                txtDescription.setText(txtSchemeOnQty.getText() + " + " + txtSchemeFreeQty.getText());
            }
        }
    }

}
