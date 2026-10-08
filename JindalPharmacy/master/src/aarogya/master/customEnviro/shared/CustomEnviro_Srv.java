package aarogya.master.customEnviro.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface CustomEnviro_Srv extends RemoteService {

    public DmResult<CustomEnviro_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(CustomEnviro_Mdl mrTemp);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsPbillMrpLessthenPo(DmHelper helper, boolean blnToEndOfFile);


}
