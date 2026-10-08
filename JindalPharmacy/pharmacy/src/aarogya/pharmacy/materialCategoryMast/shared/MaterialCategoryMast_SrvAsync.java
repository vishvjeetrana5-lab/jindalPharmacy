package aarogya.pharmacy.materialCategoryMast.shared;

import aarogya.pharmacy.materialCategoryMast.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface MaterialCategoryMast_SrvAsync {

    public void getNavigator(String module,AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getSuggestionCategory(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<String>>> result);

    public void getMoveRec(String strKey, String module, AsyncCallback<DmResult<MaterialCategoryMast_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(MaterialCategoryMast_Mdl mrTemp,String module, AsyncCallback<DmResult<String>> result);

    public void getFind(String module,DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHHSN(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<TxtHHSN_Mdl>>> result);

    public void getTxtHyesNo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void exportCSV(String sessionId, AsyncCallback<DmResult<KeyFieldExtended>> result);
}
