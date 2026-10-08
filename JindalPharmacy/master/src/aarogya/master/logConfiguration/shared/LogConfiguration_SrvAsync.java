package aarogya.master.logConfiguration.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface LogConfiguration_SrvAsync {

    public void getTxtHEntry(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void saveRecord(LogConfiguration_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getFields(String module, String entry, AsyncCallback<DmResult<LogConfiguration_Mdl>> result);

}
