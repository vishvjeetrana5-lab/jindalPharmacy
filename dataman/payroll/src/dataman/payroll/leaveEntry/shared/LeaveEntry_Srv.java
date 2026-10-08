package dataman.payroll.leaveEntry.shared;

import dataman.payroll.leaveEntry.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.sql.Connection;
import java.util.List;

public interface LeaveEntry_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<Employee_Mdl>> getTxtHEmployee(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<LeaveTextHelp_Mdl>> getTxtHLeave(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<LeaveEntry_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey);

    public String[] getServerDateTime();

    public DmResult<String> getDuration(String fromDate, String toDate);
   
    public DmResult<String> getCurrentEntryNo();
    
    public DmResult<List<GrdLeaveHistory_Mdl>> getLeaveHistory(LeaveEntry_Mdl mrTemp);
 
    public DmResult<String> getAvailableLeave(LeaveEntry_Mdl mrTemp);

    public DmResult<List<GrdLeaveStatus_Mdl>> fillLeaveStatusGrd(LeaveEntry_Mdl mrTemp);

    public DmResult<String> saveRecord(LeaveEntry_Mdl mrTemp, String voucherPrefix);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResult<KeyFieldExtended> getCurrentShiftTime(String strKey);
}
