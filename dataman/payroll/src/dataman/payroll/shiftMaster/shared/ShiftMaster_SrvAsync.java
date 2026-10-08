package dataman.payroll.shiftMaster.shared;

import dataman.payroll.shiftMaster.shared.*;
import dataman.payroll.shiftMaster.shared.*;
import dataman.payroll.shiftMaster.shared.*;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.sql.Connection;
import java.util.List;

public interface ShiftMaster_SrvAsync {

    public void getNavigator(AsyncCallback<DmResult<List<KeyFieldExtended>>> result);

    public void getMoveRec(String strKey, AsyncCallback<DmResult<ShiftMaster_Mdl>> result);

    public void deleteRecord(String strKey, AsyncCallback<DmResult<String>> result);

    public void getServerDateTime(AsyncCallback<String[]> result);

    public void getDuration(String fromDate, String toDate, String includesNextDay, AsyncCallback<DmResult<String>> result);
        
    public void saveRecord(ShiftMaster_Mdl mrTemp, String voucherPrefix, AsyncCallback<DmResult<String>> result);

    public void getFind(DmHelper pageing, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<Find_Mdl>>> result);
    
    public void getTxtHIncludesNextDaye  (DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<KeyFieldExtended>>> result);
}
