package aarogya.master.customEnviro.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface CustomEnviro_SrvAsync {

    public void getMoveRec(String strKey, AsyncCallback<DmResult<CustomEnviro_Mdl>> result);

    public void saveRecord(CustomEnviro_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void getTxtHIsPbillMrpLessthenPo(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> asyncCallback);

}
