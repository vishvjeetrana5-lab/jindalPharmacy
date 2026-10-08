package aarogya.master.logConfiguration.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface LogConfiguration_Srv extends RemoteService {

    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntry(DmHelper pageing, boolean blnToEndOfFile);

    public DmResult<String> saveRecord(LogConfiguration_Mdl mrTemp);

    public DmResult<LogConfiguration_Mdl> getFields(String module, String entry);

}
