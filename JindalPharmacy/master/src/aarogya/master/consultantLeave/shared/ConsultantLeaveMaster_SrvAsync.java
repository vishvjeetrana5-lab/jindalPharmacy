package aarogya.master.consultantLeave.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ConsultantLeaveMaster_SrvAsync {

    public void getNavigator(String consultantCode, AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<ConsultantLeaveMaster_Mdl>> result);

    public void saveRecord(ConsultantLeaveMaster_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getTxtHCName(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getFind(String consultantCode, DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);
}
