package dataman.accounts.bankLetterCreation.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_SrvAsync;
import java.util.List;

public interface BankLetterCreation_SrvAsync extends IWalkThrough_SrvAsync {

    public void getNavigator(UserPermission userPermission, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<BankLetterCreation_Mdl>> result);

    public void getDefaultValue(AsyncCallback<DmResult<DefaultValue_Mdl>> result);

    public void saveRecord(BankLetterCreation_Mdl mrTemp, String voucherPrefix, boolean isApprove, AsyncCallback<DmResult<String>> result);

    public void getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void deleteRecord(UserPermission userPermission, String strKey, BankLetterCreation_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFinalPaymentDetail(String fromDate, String toDate, String mode, String chqNo, String bankCode, AsyncCallback<DmResult<List<GrdBankLetterCreation_Mdl>>> asyncCallback);

    public void setApproval(String loginUserName, boolean approve, String docId, AsyncCallback<DmResult<String>> asyncCallback);

    public void printRecords(ChequePrint_Mdl mdlDetails, AsyncCallback<DmResult<String>> result);

}
