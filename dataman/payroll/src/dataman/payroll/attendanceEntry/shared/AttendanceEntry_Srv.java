package dataman.payroll.attendanceEntry.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface AttendanceEntry_Srv extends RemoteService {

    public List<GrdAttendanceEntry_Mdl> getFillRecords(AttendanceEntry_Mdl param);

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<Attendance_Mdl>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<SalaryMethod_Mdl>> getTxtHSalaryMethod(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<AttendanceEntry_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey);

    public DmResult<String> saveRecord(AttendanceEntry_Mdl mrTemp, String voucherPrefix);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);
    
    public List<GrdAttendanceEntry_Mdl> getAttendance(AttendanceEntry_Mdl param);
    
    public DmResult<String> getDefaultValue();
    
}
