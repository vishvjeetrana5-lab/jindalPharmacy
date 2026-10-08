package aarogya.pharmacy.medicineTransfer.shared;

import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface MedicineTransfer_SrvAsync extends IWalkThrough_SrvAsync {

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<MedicineTransfer_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(UserPermission userPermission, MedicineTransfer_Mdl mrTemp, MedicineTransfer_Mdl prevMdl, String voucherPrefix, String autoInTransitYN, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(boolean allDepartment, DmHelper helper, boolean blnToEndOfFile, String department, boolean flagFromDepartment, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getTxtHTransferTo(String v_Type, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHTransferTo_Mdl>>> result);

    public void getTxtHRequisitionNo(String departmentToCode, String departmentFromCode, String voucherDate, String itemIssueDocId, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHRequisitionNo_Mdl>>> result);

    public void getMedicineDetail(String reqDocId, String itemIssueDocId, String departmentCode, String v_Date, AsyncCallback<DmResult<List<GrdMedicineDetail_Mdl>>> result);

    public void getToolTipShowStock(String medCode, String ibCode, String fromDepartment, String toDepartment, AsyncCallback<DmResult<List<ToolTip_Mdl>>> result);

    public void getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode, AsyncCallback<DmResult<BarCode_Mdl>> result);

    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);

}
