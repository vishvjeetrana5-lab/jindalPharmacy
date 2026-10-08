package dataman.accounts.bankLetterCreation.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough_Srv;
import java.util.List;

public interface BankLetterCreation_Srv extends RemoteService, IWalkThrough_Srv {

    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission);

    public DmResult<BankLetterCreation_Mdl> getMoveRec(String strKey);

    public DmResult<DefaultValue_Mdl> getDefaultValue();

    public DmResult<String> saveRecord(BankLetterCreation_Mdl mrTemp, String voucherPrefix, boolean isApprove);

    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> deleteRecord(UserPermission userPermission, String strKey, BankLetterCreation_Mdl mrTemp);

    public DmResult<List<GrdBankLetterCreation_Mdl>> getFinalPaymentDetail(String fromDate, String toDate, String mode, String chqNo, String bankCode);

    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId);

    public DmResult<String> printRecords(ChequePrint_Mdl mdlDetails);

}
