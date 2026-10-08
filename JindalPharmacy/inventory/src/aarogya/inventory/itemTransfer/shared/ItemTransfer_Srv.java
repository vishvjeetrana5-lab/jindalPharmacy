package aarogya.inventory.itemTransfer.shared;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface ItemTransfer_Srv extends RemoteService, IWalkThrough_Srv {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<ItemTransfer_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(UserPermission userPermission, ItemTransfer_Mdl mrTemp, ItemTransfer_Mdl prevMdl, String voucherPrefix);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, int allDepartment, boolean blnToEndOfFile, String department);

    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHItemName(String departmentFrom, String departmentTo,String requisitionMandatoryInTransfer,String vType, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResultPageing<List<TxtHTransferTo_Mdl>> getTxtHTransferTo(String v_Type, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<String> getIbCode(ItemProperty_Mdl mdl);

    public DmResultPageing<List<TxtHRequisitionNo_Mdl>> getTxtHRequisitionNo(String departmentToCode, String departmentFromCode, String voucherDate, String itemIssueDocId, String vType, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<GrdItemDetail_Mdl>> getItemDetail(String v_Date,String reqDocId, String itemIssueDocId,String fromDeptCode);

    public DmResult<List<ToolTip_Mdl>> getToolTipShowStock(String itemCode, String ibCode, String fromDepartment, String toDepartment);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

    public DmResult<Boolean> getIsExisPendingTransferForApproval(String vType,String vDate, String departmentCode,String daysToRestrictItemTransferWithoutApproval);

}
