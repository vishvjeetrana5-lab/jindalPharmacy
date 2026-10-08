package dataman.accounts.chequePrintSetup.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ChequePrintSetup_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<ChequePrintSetup_Mdl> getMoveRec(String strKey, String bankCode, boolean isCopy);

    public DmResult<String> saveRecord(ChequePrintSetup_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<BankName_Mdl>> getTxtHBankName(String docId,boolean isCopy,DmHelper helper, boolean blnToEndOfFile);
}
