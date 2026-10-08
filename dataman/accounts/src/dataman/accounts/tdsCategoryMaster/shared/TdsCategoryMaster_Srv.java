package dataman.accounts.tdsCategoryMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;

import java.util.List;

public interface TdsCategoryMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<TdsCategoryMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(TdsCategoryMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDescription(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHPostingAC_Mdl>> getTxtHPostingAC(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCopyFromTDS(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<KeyFieldExtended>> fillGridApplicableDate(String keyCode);

    public DmResult<KeyFieldExtended> getMaxApplicableDate(String keyCode);

    public DmResult<String> deleteRecordApplicable(String keyCode, String dNull);

    public DmResult<List<GrdTdsCategory_Mdl>> fillGrid(String xNull, String dNull);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHTdsFor(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTdsOnPreviousTransactions(DmHelper helper, boolean blnToEndOfFile);
}
