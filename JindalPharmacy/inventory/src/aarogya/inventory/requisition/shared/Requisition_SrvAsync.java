package aarogya.inventory.requisition.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface Requisition_SrvAsync extends IWalkThrough_SrvAsync {

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<Requisition_Mdl>> result);

    public void getDefaultValue(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);

    public void saveRecord(Requisition_Mdl mrTemp, String voucherPrefix,boolean isApprove, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHItem(String vType,DmHelper pageing, boolean blnToEndOfFile, String restrictedDepartments,String toDepartment, AsyncCallback<DmResultPageing<List<TxtHItem_Mdl>>> result);

    public void getTxtHDepartment(DmHelper pageing,boolean allDepartment, boolean blnToEndOfFile, String department, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRequisitionType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHSubGroup(String v_Type,DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHRequisition_Mdl>>> result);

    public void getTxtHPriority(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHPriority_Mdl>>> result);

}
