package aarogya.pharmacy.requisition.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface Requisition_Srv extends RemoteService, IWalkThrough_Srv {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<Requisition_Mdl> getMoveRec(String strKey);

    public DmResult<DefaultValue_Mdl> getDefaultValue();

    public DmResult<String> saveRecord(Requisition_Mdl mrTemp, String voucherPrefix, boolean isApprove);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicineName(DmHelper pageing, boolean blnToEndOfFile, String restrictedDepartments, String toDepartment);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(boolean alldepartment,DmHelper pageing, boolean blnToEndOfFile, String department);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRequisitiontype(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHRequisition_Mdl>> getTxtHSubGroup(String vType, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHPriority_Mdl>> getTxtHPriority(DmHelper helper, boolean blnToEndOfFile);
}
