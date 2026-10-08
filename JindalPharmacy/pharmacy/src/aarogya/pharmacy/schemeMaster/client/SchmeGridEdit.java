package aarogya.pharmacy.schemeMaster.client;

import aarogya.pharmacy.addBatchSingle.shared.TxtHBatch_Mdl;
import aarogya.pharmacy.batchNo.client.BatchNo;
import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import aarogya.pharmacy.schemeMaster.shared.GrdSchemeDetails_Mdl;
import aarogya.pharmacy.schemeMaster.shared.SchemeMaster_Srv;
import aarogya.pharmacy.schemeMaster.shared.SchemeMaster_SrvAsync;
import aarogya.pharmacy.schemeMaster.shared.TxtHMedicine_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class SchmeGridEdit<T1, T2> extends SchmeGridEdit_Dsg<T1, T2> implements BlurHandler, ClickHandler, KeyDownHandler, KeyUpHandler {

    ServiceInvoker<SchemeMaster_SrvAsync> srvAsync;
    CallBack callBack;
    String v_SNo = "";
    int hashCode;
    protected SchemeMaster schemeMaster;
    BatchNo_Mdl batch_Mdl = new BatchNo_Mdl();

    interface CallBack {

        public void onOk(GrdSchemeDetails_Mdl details_Mdl);

        public void onDelete();
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            if (event.getSource() == txtBatchNo) {
            }
        }

    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            if (event.getSource() == txtBatchNo) {
                if (DmShared.iNull(txtBatchNo.getTag()).longValue() == 0) {
                }
            }
        }
    }

    public SchmeGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Scheme Details", "450px", "140px");
        srvAsync = new ServiceInvoker<>(GWT.create(SchemeMaster_Srv.class), SchemeMaster.class.getName());
        textHelpEvents();
        handlerEvents();
    }

    private void textHelpEvents() {

        txtHMedicineName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMedicine_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicine(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMedicine_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMedicineName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMedicine_Mdl t1Row) {
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
        txtSchemeFreeQty.addBlurHandler(this);
        txtSchemeOnQty.addBlurHandler(this);
        txtBatchNo.addKeyDownHandler(this);
        txtBatchNo.addKeyUpHandler(this);
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, SchemeMaster schemeMaster) {
        this.schemeMaster = schemeMaster;
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

        setDescription();
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
        ourRow.setBatchNo_Mdl(batch_Mdl);

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
        batch_Mdl = ourRow.getBatchNo_Mdl();
        txtHMedicineName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHMedicineName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtSchemeFreeQty.setText(DmShared.vNull(ourRow.getSchemeFreeQty()).toString());
        txtSchemeOnQty.setText(DmShared.vNull(ourRow.getSchemeOnQty()).toString());
        txtUnit.setText(ourRow.getFreeItemUnitName());
        txtUnit.setTag(ourRow.getFreeItemUnitCode());
        if (batch_Mdl != null) {
            if (DmShared.iNull(batch_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                txtBatchNo.setText(batch_Mdl.getSharedKeyFieldName());
                txtBatchNo.setTag(batch_Mdl.getSharedKeyFieldCode());
            }
        }
        txtDescription.setText(ourRow.getDescription());

        txtHMedicineName.setFocus(true);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSchemeFreeQty) || event.getSource().equals(txtSchemeOnQty)) {
            setDescription();
        }
    }

    private void setDescription() {
        if (DmShared.vNull(txtSchemeOnQty.getText()).doubleValue() > 0 && DmShared.vNull(txtSchemeOnQty.getText()).doubleValue() > 0) {
            txtDescription.setText(txtSchemeOnQty.getText() + " + " + txtSchemeFreeQty.getText());
        }
    }

}
