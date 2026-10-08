package dataman.utility.productLicensedTo.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ProductLicensedTo_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<ProductLicensedTo_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(ProductLicensedTo_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHCompany_Mdl>> getTxtHCompany(String pltCode, DmHelper pageing, boolean blnToEndOfFile);
}
