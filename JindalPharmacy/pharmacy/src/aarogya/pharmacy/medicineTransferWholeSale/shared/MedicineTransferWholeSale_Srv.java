package aarogya.pharmacy.medicineTransferWholeSale.shared;

import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface MedicineTransferWholeSale_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<MedicineTransferWholeSale_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(UserPermission userPermission, MedicineTransferWholeSale_Mdl mrTemp, MedicineTransferWholeSale_Mdl prevMdl, String voucherPrefix);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, String department, boolean flagFromDepartment);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResultPageing<List<TxtHTransferTo_Mdl>> getTxtHTransferTo(String v_Type, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdMedicineDetail_Mdl>> getMedicineDetail(String reqDocId, String medicineIssueDocId, String departmentCode, String v_Date);

    public DmResult<List<ToolTip_Mdl>> getToolTipShowStock(String medCode, String ibCode, String fromDepartment, String toDepartment);

    public DmResult<BarCode_Mdl> getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode);
    
    public DmResult<String> getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date);
}
