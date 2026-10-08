package dataman.dmToolkit.manageSite.shared;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ManageSite_SrvAsync {

    public void saveRecord(List<GridSite_Mdl> mrTemp, String tableName, String keyFieldName, String keyFieldValue, String siteFieldName, AsyncCallback<DmResult<String>> result);

    public void getSiteList(String tableName, String keyFieldName, String keyFieldValue, String siteFieldName, AsyncCallback<DmResult<List<GridSite_Mdl>>> result);

    public void getTxtHSiteList(DmHelper pageing, boolean blnToEndOfFile, String siteList, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void deleteSiteList(String tableName, String keyFieldName, String keyFieldValue, String siteFieldName, String siteFieldValue, AsyncCallback<DmResult<String>> result);

}
