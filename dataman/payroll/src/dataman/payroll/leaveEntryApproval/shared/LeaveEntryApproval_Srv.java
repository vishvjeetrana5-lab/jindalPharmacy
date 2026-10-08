package dataman.payroll.leaveEntryApproval.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface LeaveEntryApproval_Srv extends RemoteService {


    public DmResultPageing<List<GrdLeaveEntryApproval_Mdl>> getFillRecords(Search_Mdl value_Mdl, Filter_Mdl param, DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);
    
     public DmResult<String> saveRecord(GrdLeaveEntryApproval_Mdl mrTemp);
     
     public DmResult<DefaultValue_Mdl> getDefaultValues();

}
