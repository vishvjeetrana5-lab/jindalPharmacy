package dataman.accounts.bankLetterApproval.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface BankLetterApproval_SrvAsync {

    public void getFillRecords(DefaultValue_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdBankLetterApproval_Mdl>>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getDefalutValue(AsyncCallback<DmResult<DefaultValue_Mdl>> result);
}
