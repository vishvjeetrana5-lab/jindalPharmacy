package aarogya.pharmacy.expiryModification.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ExpiryModification_Srv extends RemoteService {

    //<editor-fold defaultstate="collapsed" desc="TextHelpEvents">
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHFilterType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHFilterValue(String filterTypeCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHMedicineName_Mdl>> getTxtHMedicineName(String departmentCode, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHBatchNo_Mdl>> getTxtHBatchNo(String medicineCode, String departmentCode, DmHelper helper, boolean blnToEndOfFile);
    //</editor-fold>

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<String> saveRecord(ExpiryModification_Mdl mrTemp, String voucherPrefix);

    public DmResult<String> getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date);

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<ExpiryModification_Mdl> getMoveRec(String strKey);

    public DmResultPageing<List<dmFind_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

}
