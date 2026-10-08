package dataman.payroll.departmentMaster.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface DepartmentMaster_Srv extends RemoteService {

    public DmResult<List<KeyFieldExtended>> getNavigator();

    public DmResultPageing<List<String>> getSuggestionDepartment(DmHelper helper, boolean blnToEndOfFile);

    public DmResultPageing<List<String>> getSuggestionShortName(DmHelper helper, boolean blnToEndOfFile);

    public DmResult<DepartmentMaster_Mdl> getMoveRec(String strKey);

    public DmResult<String> saveRecord(DepartmentMaster_Mdl mrTemp);

    public DmResult<String> deleteRecord(String strKey);

    public DmResultPageing<List<KeyFieldExtended>> getTxtHCostCenter(DmHelper pageing, boolean blnToEndOfFile);

    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile);

   // public DmResult<KeyFieldExtended> exportCSV(String sessionId);
}
