package dataman.payroll.attendanceEntry.shared;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public interface AttendanceEntry_SrvAsync {

    public void getFillRecords(AttendanceEntry_Mdl param, AsyncCallback<List<GrdAttendanceEntry_Mdl>> result);

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getTxtHSalaryMethod(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<SalaryMethod_Mdl>>> result);

    public void getTxtHDepartmentName(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<AttendanceEntry_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void saveRecord(AttendanceEntry_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);

    public void getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Attendance_Mdl>>> result);
    
    public void getAttendance(AttendanceEntry_Mdl param, AsyncCallback<List<GrdAttendanceEntry_Mdl>> result);
    
    public void getDefaultValue(AsyncCallback<DmResult<String>> result);
    
}
