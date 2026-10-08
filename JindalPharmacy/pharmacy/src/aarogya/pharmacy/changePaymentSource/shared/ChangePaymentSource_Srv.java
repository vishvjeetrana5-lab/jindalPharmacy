package aarogya.pharmacy.changePaymentSource.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ChangePaymentSource_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<DefaultValue_Mdl> getDefaultValues();

    public DmResult<ChangePaymentSource_Mdl> getMoveRec(String docId);

    public DmResult<String> saveRecord(UserPermission userPermission, ChangePaymentSource_Mdl mrTemp, String voucherPrefix);

    public DmResult<String> deleteRecord(UserPermission userPermission, String docId);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<PatientDetail_Mdl> getBillDetails(String billDocId, String billRecId, String regType);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHPaymentSource(DmHelper pageing, boolean blnToEndOfFile);
}
