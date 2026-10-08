package aarogya.pharmacy.indent.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface Indent_Srv extends RemoteService, IWalkThrough_Srv {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<Indent_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(Indent_Mdl mrTemp, String voucherPrefix, boolean isApprove);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdMedicineDetail_Mdl>> fillIndentDetails(String requisitionDocId, String v_Date, String docId);

    public DmResult<MedicineWithRequisition_Mdl> getRequisitionDetail(String requisitionDocid, String v_Date, String docId);

    public DmResultPageing<List<TxtHMedicineName_Mdl>> getTxtHMedicineName(String itemSku, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndentType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndentor(String vType,DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

    public DmResultPageing<List<TxtHRequisition_Mdl>> getTxtHRequisition(String v_Date, String indentDocId, DmHelper pageing, boolean blnToEndOfFile);

}
