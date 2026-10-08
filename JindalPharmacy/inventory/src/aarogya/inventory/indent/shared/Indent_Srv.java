package aarogya.inventory.indent.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface Indent_Srv extends RemoteService, IWalkThrough_Srv {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<Indent_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(Indent_Mdl mrTemp, String voucherPrefix,boolean isApprove);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission,DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdItemDetail_Mdl>> fillIndentDetails(String v_Type, String requisitionDocId, String v_Date, String docId);
    
    public DmResult<ItemWithRequisition_Mdl> getRequisitionDetail(String v_Type, String requisitionDocid, String v_Date, String docId);

    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItemName(String v_Type, String itemSku, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndentType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean allDepartment, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndentor(String v_Type,DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

    public DmResultPageing<List<TxtHRequisition_Mdl>> getTxtHRequisition(String v_Date, String indentDocId, DmHelper pageing, boolean blnToEndOfFile);

}
