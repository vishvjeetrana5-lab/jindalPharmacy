package aarogya.inventory.itemTransfer.shared;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface ItemTransfer_SrvAsync  extends IWalkThrough_SrvAsync{

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<ItemTransfer_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(UserPermission userPermission, ItemTransfer_Mdl mrTemp, ItemTransfer_Mdl prevMdl, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper helper, int allDepartment, boolean blnToEndOfFile, String department, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHItemName(String departmentFrom, String departmentTo,String requisitionMandatoryInTransfer,String vType, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHItem_Mdl>>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getTxtHTransferTo(String v_Type, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHTransferTo_Mdl>>> result);

    public void getIbCode(ItemProperty_Mdl mdl, AsyncCallback<DmResult<String>> result);

    public void getTxtHRequisitionNo(String departmentToCode, String departmentFromCode, String voucherDate, String itemIssueDocId, String vType, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHRequisitionNo_Mdl>>> result);

    public void getItemDetail(String v_date,String reqDocId, String itemIssueDocId,String fromDeptCode, AsyncCallback<DmResult<List<GrdItemDetail_Mdl>>> result);

    public void getToolTipShowStock(String itemCode , String ibCode, String fromDepartment, String toDepartment, AsyncCallback<DmResult<List<ToolTip_Mdl>>> result);
    
    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);
    
    public void getIsExisPendingTransferForApproval(String vType,String vDate,String daysToRestrictItemTransferWithoutApproval,String departmentCode,AsyncCallback <DmResult<Boolean>> result);

}
