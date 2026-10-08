package aarogya.inventory.physicalStockClearance.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PhysicalStockClearance_Srv extends RemoteService {

    //<editor-fold defaultstate="collapsed" desc="TextHelpEvents">
    public DmResultPageing<List<TxtHDateOn_Mdl>> getTxtHDateOn(DmHelper helper, boolean blnToEndOfFile);
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Posting">
    public DmResult<String> setPosted(PhysicalStockClearance_Mdl mrTemp);
    //</editor-fold>

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<String> saveRecord(PhysicalStockClearance_Mdl mrTemp, String voucherPrefix);

    public DmResult<PhysicalStockClearance_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

}
