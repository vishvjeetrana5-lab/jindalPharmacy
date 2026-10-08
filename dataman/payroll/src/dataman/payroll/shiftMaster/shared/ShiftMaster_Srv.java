package dataman.payroll.shiftMaster.shared;

import dataman.payroll.shiftMaster.shared.*;
import dataman.payroll.shiftMaster.shared.*;
import dataman.payroll.shiftMaster.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.sql.Connection;
import java.util.List;

public interface ShiftMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResult<ShiftMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> deleteRecord(String strKey);

    public String[] getServerDateTime();

    public DmResult<String> getDuration(String fromDate, String toDate, String includesNextDay);
    
    public DmResult<String> saveRecord(ShiftMaster_Mdl mrTemp, String voucherPrefix);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);
    
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIncludesNextDaye(DmHelper helper, boolean blnToEndOfFile);
}
