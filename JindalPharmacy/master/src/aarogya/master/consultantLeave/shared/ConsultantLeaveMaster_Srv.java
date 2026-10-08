package aarogya.master.consultantLeave.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface ConsultantLeaveMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator(String consultantCode);

    public DmResult<ConsultantLeaveMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(ConsultantLeaveMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCName(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Find_Mdl>> getFind(String consultantCode, DmHelper pageing, boolean blnToEndOfFile);
}
