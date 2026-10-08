package dataman.accounts.bankAccountDetail.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface BankAccountDetail_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<BankAccountDetail_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(BankAccountDetail_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHLinkWithAccount(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHLinkWithAccount_Mdl>>> result);

    public void getFind(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getYesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
