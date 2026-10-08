package dataman.accounts.tallyExportMasters.shared;


import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmResult;

public interface TallyExportMasters_Srv extends RemoteService {

    public DmResult<String[]> exportAcMastersToTallyXML(Filter_Mdl filter);

}
