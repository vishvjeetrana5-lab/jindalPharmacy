package dataman.accounts.tallyExportVouchers.shared;


import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;

public interface TallyExportVouchers_Srv extends RemoteService {

    public DmResult<String[]> exportAcVouchersToTallyXML(Filter_Mdl filter);

}
