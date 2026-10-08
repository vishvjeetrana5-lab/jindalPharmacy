package aarogya.master.packageMaster.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.manageSite.shared.GridSite_Mdl;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface PackageMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestionName(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<String>> getSuggestionShortName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<PackageMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(PackageMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHNature(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> getDefaultOrganization();

    public DmResult<Boolean> checkSite(List<GridSite_Mdl> siteCodes, String packageCode);
}
