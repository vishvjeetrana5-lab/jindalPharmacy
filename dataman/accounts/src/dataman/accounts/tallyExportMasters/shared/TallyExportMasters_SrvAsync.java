package dataman.accounts.tallyExportMasters.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmResult;

public interface TallyExportMasters_SrvAsync {
 
    public void exportAcMastersToTallyXML(Filter_Mdl filter, AsyncCallback<DmResult<String[]>> result);

}
