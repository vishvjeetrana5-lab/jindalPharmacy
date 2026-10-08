package dataman.dmToolkit.manageSite.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ManageSite_Srv extends RemoteService {

    public DmResult<List<GridSite_Mdl>> getSiteList(String tableName, String keyFieldName, String keyFieldValue, String siteFieldName);

    public DmResult<String> saveRecord(List<GridSite_Mdl> mrTemp, String tableName, String keyFieldName, String keyFieldValue, String siteFieldName);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteList(DmHelper pageing, boolean blnToEndOfFile, String siteList);

    public DmResult<String> deleteSiteList(String tableName, String keyFieldName, String keyFieldValue, String siteFieldName, String siteFieldValue);

}
