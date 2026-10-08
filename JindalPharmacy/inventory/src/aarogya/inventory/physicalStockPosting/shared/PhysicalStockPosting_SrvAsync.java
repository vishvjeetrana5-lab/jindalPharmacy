package aarogya.inventory.physicalStockPosting.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PhysicalStockPosting_SrvAsync {

    //<editor-fold defaultstate="collapsed" desc="TextHelpEvents">
    public void getTxtHDateOn(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHDateOn_Mdl>>> result);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Posting">
    public void setPosted(PhysicalStockPosting_Mdl mrTemp, AsyncCallback<DmResult<String>> result);
    //</editor-fold>

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveRecord(PhysicalStockPosting_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<PhysicalStockPosting_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

}
