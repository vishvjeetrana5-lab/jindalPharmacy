package aarogya.pharmacy.physicalStockClearance.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PhysicalStockClearance_SrvAsync {

    //<editor-fold defaultstate="collapsed" desc="TextHelpEvents">
    public void getTxtHDateOn(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHDateOn_Mdl>>> result);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Posting">
    public void setPosted(PhysicalStockClearance_Mdl mrTemp, AsyncCallback<DmResult<String>> result);
    //</editor-fold>

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveRecord(PhysicalStockClearance_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<PhysicalStockClearance_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

}
