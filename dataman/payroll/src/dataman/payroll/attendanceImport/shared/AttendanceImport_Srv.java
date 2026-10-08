package dataman.payroll.attendanceImport.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import java.util.List;

public interface AttendanceImport_Srv extends RemoteService {

    public DmResult<String> saveRecord(AttendanceImport_Mdl mrTemp);
    
    public DmResult<ImportData_Mdl> importData(ImportData_Mdl import_Mdl);

    public DmResult<String> getCSVFile(ImportData_Mdl mrTemp, String fileName);

    public DmResult<KeyFieldExtended> readLogFile(ImportData_Mdl importData_Mdl);

    public DmResultPageing<List<ImportData_Mdl>> getImportTo(SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<ImportData_Mdl>> getImportData();
    
    public DmResult<DefaultModel_Mdl> getDefaultMdl();
    

}
