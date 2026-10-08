package aarogya.pharmacy.materialCategoryMast.shared;

import aarogya.pharmacy.materialCategoryMast.shared.*;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface MaterialCategoryMast_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(String module);

    public DmResultPageing<List<String>> getSuggestionCategory(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<MaterialCategoryMast_Mdl> getMoveRec(String strKey, String module);

    public DmResult<String> saveRecord(MaterialCategoryMast_Mdl mrTemp,String module);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(String module,DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHHSN_Mdl>> getTxtHHSN(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHyesNo(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<KeyFieldExtended> exportCSV(String sessionId);

}
