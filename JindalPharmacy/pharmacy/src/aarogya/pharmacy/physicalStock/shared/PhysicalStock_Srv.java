package aarogya.pharmacy.physicalStock.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PhysicalStock_Srv extends RemoteService {

    //<editor-fold defaultstate="collapsed" desc="TextHelpEvents">
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHFilterType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHFilterValue(String filterTypeCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHMedicineName_Mdl>> getTxtHMedicineName(String departmentCode, String filterTypeCode, String filterValueCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHBatchNo_Mdl>> getTxtHBatchNo(String medicineCode, DmHelper helper, boolean blnToEndOfFile);
    //</editor-fold>

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<String> saveRecord(PhysicalStock_Mdl mrTemp, String voucherPrefix);

    public DmResult<PhysicalStock_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String docId, String vDate, String departmentCode);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date);

}
