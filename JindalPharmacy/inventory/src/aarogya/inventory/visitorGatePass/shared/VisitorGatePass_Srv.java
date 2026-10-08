package aarogya.inventory.visitorGatePass.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import java.util.List;

public interface VisitorGatePass_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<VisitorGatePass_Mdl> getMoveRec(String strKey);

    public DmResult<VisitorGatePass_Mdl> getSlipNoDetail(String recId);

    public DmResult<String> setGateOut(VisitorGatePass_Mdl mrTemp, boolean isGateOut);

    public DmResult<String> saveRecord(VisitorGatePass_Mdl mrTemp, String voucherPrefix);

    public DmResult<String> deleteRecord(String strKey);

    public DmResult<Default_Mdl> getDefaultValues();
    
    public DmResult<VisitorGatePass_Mdl> getVisitorDetails(String mobileNo);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCity_Mdl>> getTxtCity(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getHVehicleType(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGateNo(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<Default_Mdl> getDefaultDateTime();

}
