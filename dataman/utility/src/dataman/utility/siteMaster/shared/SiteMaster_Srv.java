package dataman.utility.siteMaster.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface SiteMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<SiteMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(SiteMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey, String pltCode);

    public DmResultPageing<List<TxtHBranch_Mdl>> getTxtHBranch(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRegionalLanguage(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<TxtHPLT_Mdl>> getTxtHPLT(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHHeadOffice(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHRegistrationType(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHState(DmHelper pageing, boolean blnToEndOfFile);

    public String[] getServerDateTime();
}
