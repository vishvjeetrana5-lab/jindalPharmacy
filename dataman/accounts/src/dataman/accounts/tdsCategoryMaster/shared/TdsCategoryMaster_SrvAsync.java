package dataman.accounts.tdsCategoryMaster.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;

import java.util.List;

public interface TdsCategoryMaster_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getSuggestion(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<TdsCategoryMaster_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(TdsCategoryMaster_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHDescription(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getTxtHPostingAC(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHPostingAC_Mdl>>> result);

    public void getTxtHCopyFromTDS(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void fillGridApplicableDate(String keyCode, AsyncCallback<DmResult<List<KeyFieldExtended>>> asyncCallback);

    public void getMaxApplicableDate(String keyCode, AsyncCallback<DmResult<KeyFieldExtended>> asyncCallback);

    public void deleteRecordApplicable(String keyCode, String dNull, AsyncCallback<DmResult<String>> asyncCallback);

    public void fillGrid(String xNull, String dNull, AsyncCallback<DmResult<List<GrdTdsCategory_Mdl>>> asyncCallback);

    public void getTxtHType(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

    public void getTxtHTdsFor(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
    public void getTxtHTdsOnPreviousTransactions(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

}
