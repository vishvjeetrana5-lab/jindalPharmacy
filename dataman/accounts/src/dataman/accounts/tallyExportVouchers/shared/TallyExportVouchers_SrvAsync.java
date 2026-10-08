package dataman.accounts.tallyExportVouchers.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmResult;

public interface TallyExportVouchers_SrvAsync {
 
    public void exportAcVouchersToTallyXML(Filter_Mdl filter, AsyncCallback<DmResult<String[]>> result);

}
