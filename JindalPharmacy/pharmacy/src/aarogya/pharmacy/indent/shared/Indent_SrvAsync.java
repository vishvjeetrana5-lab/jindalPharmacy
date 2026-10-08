package aarogya.pharmacy.indent.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface Indent_SrvAsync extends IWalkThrough_SrvAsync {

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<Indent_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveRecord(Indent_Mdl mrTemp, String voucherPrefix, boolean isApprove, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void fillIndentDetails(String requisitionDocId, String v_Date, String docId, AsyncCallback<DmResult<List<GrdMedicineDetail_Mdl>>> result);

    public void getRequisitionDetail(String requisitionDocid, String v_Date, String docId, AsyncCallback<DmResult<MedicineWithRequisition_Mdl>> result);

    public void getTxtHMedicineName(String itemSku, DmHelper paging, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHMedicineName_Mdl>>> result);

    public void getTxtHIndentType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHIndentor(String vType,DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHRequisition(String v_Date, String indentDocId, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHRequisition_Mdl>>> result);

    public void setApproval(String approvedBy, boolean isApprove, String docId, AsyncCallback<DmResult<String>> result);

}
