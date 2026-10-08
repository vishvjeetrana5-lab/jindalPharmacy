package dataman.accounts.chequePrintSetup.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ChequePrintSetup_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, String bankCode, boolean isCopy, AsyncCallback<DmResult<ChequePrintSetup_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(ChequePrintSetup_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHBankName(String docId,boolean isCopy,DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<BankName_Mdl>>> result);
}
