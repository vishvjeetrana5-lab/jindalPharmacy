
package dataman.accounts.additionalPlaceOfBusiness.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import java.util.List;


public interface AdditionalPlaceOfBusiness_Srv extends RemoteService{
    
    public DmResult<List<KeyFieldExtended>> getNavigator();
    
    public DmResult<String> saveRecord(AdditionalPlaceOfBusiness_Mdl mrTemp);
    
    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResult<AdditionalPlaceOfBusiness_Mdl> getMoveRec(String strKey);
    
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile);
    
    public DmResult<String> deleteRecord(String strKey, ModifyReason_Mdl modifyReasonMdl);
    
    public DmResultPageing<List<TxtHLinkWithAccount_Mdl>> getTxtHLinkWithAccount(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(String stateCode,DmHelper helper, boolean blnToEndOfFile);
}
