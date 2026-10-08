package aarogya.pharmacy.medicineInTransit.shared;

import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface MedicineInTransit_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<MedicineInTransit_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(UserPermission userPermission, MedicineInTransit_Mdl mrTemp, MedicineInTransit_Mdl prevMdl, String voucherPrefix);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile, String department, boolean flagFromDepartment);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResultPageing<List<TxtHTransferTo_Mdl>> getTxtHTransferTo(String v_Type, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHRequisitionNo_Mdl>> getTxtHRequisitionNo(String departmentToCode, String departmentFromCode, String voucherDate, String medicineIssueDocId, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdMedicineDetail_Mdl>> getMedicineDetail(String reqDocId, String medicineIssueDocId, String departmentCode, String v_Date);

    public DmResult<List<ToolTip_Mdl>> getToolTipShowStock(String medCode, String ibCode, String fromDepartment, String toDepartment);

    public DmResult<BarCode_Mdl> getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode);

    public DmResultPageing<List<TxtHTransferFromDept_Mdl>> getTxtHTransferFromDept(String department, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<MedicineInTransit_Mdl> getRcdMedicineDetail(String strKey, String strEntryDate);

    public DmResult<String> getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date);
}
