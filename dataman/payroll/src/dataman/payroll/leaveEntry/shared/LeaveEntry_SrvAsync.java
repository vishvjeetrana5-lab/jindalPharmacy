package dataman.payroll.leaveEntry.shared;

import dataman.payroll.leaveEntry.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.sql.Connection;
import java.util.List;

public interface LeaveEntry_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getTxtHEmployee(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Employee_Mdl>>> result);

    public void getTxtHLeave(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<LeaveTextHelp_Mdl>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<LeaveEntry_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getServerDateTime(AsyncCallback<String[]> result);

    public void getDuration(String fromDate, String toDate, AsyncCallback<DmResult<String>> result);
    
    public void getCurrentEntryNo(AsyncCallback<DmResult<String>> result);
        
    public void getLeaveHistory(LeaveEntry_Mdl mrTemp, AsyncCallback<DmResult<List<GrdLeaveHistory_Mdl>>> result);

    public void fillLeaveStatusGrd(LeaveEntry_Mdl mrTemp, AsyncCallback<DmResult<List<GrdLeaveStatus_Mdl>>> result);

    public void getAvailableLeave(LeaveEntry_Mdl mrTemp, AsyncCallback<DmResult<String>> result);

    public void saveRecord(LeaveEntry_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);
    
    public void getCurrentShiftTime(String strKey,AsyncCallback<DmResult<KeyFieldExtended>> result );
}
