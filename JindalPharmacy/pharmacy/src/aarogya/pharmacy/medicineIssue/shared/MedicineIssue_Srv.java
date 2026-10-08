package aarogya.pharmacy.medicineIssue.shared;

import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface MedicineIssue_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<MedicineIssue_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(MedicineIssue_Mdl mrTempCurrent, MedicineIssue_Mdl mrTempPrevious, String voucherPrefix);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHIssueTo_Mdl>> getTxtHIssueTo(String v_Type, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHRequisitionNo_Mdl>> getTxtHRequisitionNo(String departmentCode, String voucherDate, String medicineIssueDocId, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<List<GrdMedicineDetail_Mdl>> getMedicineDetail(String reqDocId, String medicineIssueDocId, String departmentCode, String v_Date);

    public DmResult<List<ToolTip_Mdl>> getToolTipShowStock(String medCode, String ibCode, String fromDepartment, String toDepartment);

    public DmResult<BarCode_Mdl> getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode, boolean isExpiryMedicine);
    
    public String[] getServerDateTime();
    
    public DmResultPageing<List<TxtHRequisitionNo_Mdl>> getRequisitionNoDetail(String requisitionDocID);
}
