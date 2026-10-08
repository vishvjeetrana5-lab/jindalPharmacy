package aarogya.inventory.itemMaster.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.List;
import aarogya.inventory.itemMaster.shared.GrdItsRateSlab_Mdl;
import aarogya.inventory.itemMaster.shared.ItemMaster_Srv;
import aarogya.inventory.itemMaster.shared.ItemMaster_SrvAsync;

public class ItsRateSlabGridEdit<T1, T2> extends ItsRateSlabGridEdit_Dsg<T1, T2> {
    
    interface CallBack {
        
        public void onOk(GrdItsRateSlab_Mdl opdService);
        
        public void onDelete();
    }
    
    ServiceInvoker<ItemMaster_SrvAsync> SrvAsync;
    CallBack callBack;
    GrdItsRateSlab_Mdl parentRow;
    int hashCode = 0;
    private ItemMaster itemMaster;
    
    private void textHelpEvents() {
        txtHGstType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHGstType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGstType).show();
                    }
                    
                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGstType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGstType).show();
                        }
                    }
                });
            }
            
            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus & t1Row != null) {
                    
                }
            }
        });
    }
    
    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    
    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, ItemMaster itemMster) {
        super.showRelativeTo(currentRowIndex, 1, okNeeded, newNeeded, deleteNeeded);
        this.itemMaster = itemMaster;
    }
    
    @Override
    public void displayRow(T1 row) {
        GrdItsRateSlab_Mdl ourRow = (GrdItsRateSlab_Mdl) row;
        parentRow = new GrdItsRateSlab_Mdl();
        
        txtRateFrom.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtRateFrom.setText(DmClient.vNull(ourRow.getRateFrom(), "0.00"));
        txtRateUpto.setText(DmClient.vNull(ourRow.getRateUpto(), "0.00"));
        txtHGstType.setTag(DmShared.xNull(ourRow.getGstTypeCode()));
        txtHGstType.setText(DmShared.xNull(ourRow.getGstTypeName()));
        
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtRateFrom.setFocus(true);
    }
    
    @Override
    public T1 getNewRow() {
        return (T1) new GrdItsRateSlab_Mdl();
    }
    
    @Override
    public void onOk() {
        
        if (DmShared.vNull(txtRateFrom.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Rate From is mandatory.", txtRateFrom).show();
            return;
        }
        if (DmShared.vNull(txtRateUpto.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Rate upto is mandatory.", txtRateUpto).show();
            return;
        }
         if (DmShared.xNull(txtHGstType.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("GST Type is mandatory.", txtHGstType).show();
            return;
        }
        
        if (DmShared.vNull(txtRateFrom.getText()).doubleValue() > DmShared.vNull(txtRateUpto.getText()).doubleValue()) {
            new DmAlertMessageBox("Rate upto must be greater than or equal to Rate From.", txtRateUpto).show();
            return;
        }
        
        for (GrdItsRateSlab_Mdl grdRate : (List<GrdItsRateSlab_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            double rateFrom = DmShared.vNull(grdRate.getRateFrom()).doubleValue();
            double rateUpto = DmShared.vNull(grdRate.getRateUpto()).doubleValue();
            double rateFCurrent = DmShared.vNull(txtRateFrom.getText()).doubleValue();
            double rateUCurrent = DmShared.vNull(txtRateUpto.getText()).doubleValue();
            if (!DmClient.vNull(grdRate.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                if (((rateFrom <= rateFCurrent && rateUpto >= rateFCurrent)
                        || (rateFrom <= rateUCurrent && rateUpto >= rateUCurrent)
                        || (rateFrom >= rateFCurrent && rateUpto <= rateUCurrent))) {
                    new DmAlertMessageBox("Rate slab already exist.", txtRateFrom).show();
                    return;
                }
            }
        }
        proceedOk();
    }
    
    @Override
    public void manageOk(T1 row) {
        GrdItsRateSlab_Mdl ourRow = (GrdItsRateSlab_Mdl) row;
        
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtRateFrom.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtRateFrom.getTag2nd()));
        ourRow.setRateFrom(DmClient.vNull(txtRateFrom.getText(), "0.00"));
        ourRow.setRateUpto(DmClient.vNull(txtRateUpto.getText(), "0.00"));
        ourRow.setGstTypeCode(DmShared.xNull(txtHGstType.getTag()));
        ourRow.setGstTypeName(DmShared.xNull(txtHGstType.getText()));
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
    
    public ItsRateSlabGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Rate Detail", "421px", "50px");
        SrvAsync = new ServiceInvoker<ItemMaster_SrvAsync>(GWT.create(ItemMaster_Srv.class), ItemMaster.class.getName());
        textHelpEvents();
    }
    
}
