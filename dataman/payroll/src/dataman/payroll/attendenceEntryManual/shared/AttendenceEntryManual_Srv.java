package dataman.payroll.attendenceEntryManual.shared;

import dataman.payroll.attendenceEntryManual.shared.*;
import dataman.payroll.attendenceEntryManual.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.sql.Connection;
import java.util.List;

public interface AttendenceEntryManual_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<Employee_Mdl>> getTxtHEmployee(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<AttendenceTextHelp_Mdl>> getTxtHAttendence(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<AttendenceEntryManual_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey);
    
    public DmResult<Default_Mdl> getDefaultValue();

    public String[] getServerDateTime();

    public DmResult<String> getDuration(String fromDate, String toDate);
   
    public DmResult<String> getCurrentEntryNo();
    
    public DmResult<String> saveRecord(AttendenceEntryManual_Mdl mrTemp, String voucherPrefix);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);
}
