package aarogya.pharmacy.changePaymentSource.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ChangePaymentSource_SrvAsync {

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void getMoveRec(String docId, AsyncCallback<DmResult<ChangePaymentSource_Mdl>> result);

    public void deleteRecord(UserPermission userPermission, String docId, AsyncCallback<DmResult<String>> result);

    public void saveRecord(UserPermission userPermission, ChangePaymentSource_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getBillDetails(String billDocId, String billRecId, String regType, AsyncCallback<DmResult<PatientDetail_Mdl>> result);

    public void getTxtHType(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHPaymentSource(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
}
