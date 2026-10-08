package dataman.payroll.attendenceEntryManual.shared;

import dataman.payroll.attendenceEntryManual.shared.*;
import dataman.payroll.attendenceEntryManual.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.sql.Connection;
import java.util.List;

public interface AttendenceEntryManual_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getTxtHEmployee(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Employee_Mdl>>> result);

    public void getTxtHAttendence(DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<AttendenceTextHelp_Mdl>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<AttendenceEntryManual_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getServerDateTime(AsyncCallback<String[]> result);
    
    public void getDefaultValue(AsyncCallback<DmResult<Default_Mdl>> result);

    public void getDuration(String fromDate, String toDate, AsyncCallback<DmResult<String>> result);
    
    public void getCurrentEntryNo(AsyncCallback<DmResult<String>> result);
        
    public void saveRecord(AttendenceEntryManual_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);
}
