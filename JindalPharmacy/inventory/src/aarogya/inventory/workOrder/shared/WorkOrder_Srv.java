package aarogya.inventory.workOrder.shared;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface WorkOrder_Srv extends RemoteService, IWalkThrough_Srv {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<WorkOrder_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(WorkOrder_Mdl mrTemp, String voucherPrefix, boolean isApprove);

    public DmResult<String> deleteRecord(String strKey, String guiConfigCode);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHSupplierPT_Mdl>> getTxtHSupplierPT(boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHQuotationNo_Mdl>> getTxtHQuotationNo(boolean isShowUsedQuotationsAlso, String supplierCode, String v_Date, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(DmHelper pageing, boolean blnToEndOfFile,boolean blnAllItemHelp);

    public DmResult<WorkOrder_Mdl> getItemDetail(String indentDocId, List<KeyFieldExtended> keyFields);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<String[]> getServerDateTime();

    public DmResult<List<GrdIndentDetail_Mdl>> getIndentDetail(List<KeyFieldExtended> keyFields, ItemProperty_Mdl ippm_Mdl);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

}
