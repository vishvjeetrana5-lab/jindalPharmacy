package aarogya.inventory.wipToStockConversion.shared;

import aarogya.inventory.wipToStockConversion.shared.*;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface WipToStockConversion_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<WipToStockConversion_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(WipToStockConversion_Mdl mrTempCurrent, WipToStockConversion_Mdl mrTempPrevious, String voucherPrefix);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission,DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHProject_Mdl>> getTxtHProject(String v_Type, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItemName(String v_Type, String departmentCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<String> getIbCode(ItemProperty_Mdl mdl);

    public DmResult<String> getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date);
    
    public DmResult<Double> getGetLedgerBalance(String subCode);

}
