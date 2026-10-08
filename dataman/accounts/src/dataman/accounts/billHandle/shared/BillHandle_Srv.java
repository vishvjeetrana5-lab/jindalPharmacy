package dataman.accounts.billHandle.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface BillHandle_Srv extends RemoteService, IWalkThrough_Srv {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<BillHandle_Mdl> getMoveRec(String strKey);

    public DmResult<DefaultValue_Mdl> getDefaultValue();

    public DmResult<String> saveRecord(BillHandle_Mdl mrTemp, String voucherPrefix, boolean isApprove);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHToLocation(DmHelper pageing, boolean blnToEndOfFile, String location);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHFromLocation(DmHelper pageing, boolean blnToEndOfFile, String location);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRequisitiontype(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<List<GrdBillDetail_Mdl>> getPBillByModuleLocation(String location, String fromDate, String toDate, String fromLocation);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

}
