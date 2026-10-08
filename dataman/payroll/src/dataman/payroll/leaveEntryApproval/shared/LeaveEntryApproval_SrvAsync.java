package dataman.payroll.leaveEntryApproval.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface LeaveEntryApproval_SrvAsync {

    public void getFillRecords(Search_Mdl value_Mdl,Filter_Mdl param, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<GrdLeaveEntryApproval_Mdl>>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
    
    public void saveRecord(GrdLeaveEntryApproval_Mdl mrTemp, AsyncCallback<DmResult<String>> result);
    
    public void getDefaultValues(AsyncCallback<DmResult<DefaultValue_Mdl>> result);


}
