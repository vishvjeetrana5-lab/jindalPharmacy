package dataman.payroll.attendanceImport.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import java.util.List;

public interface AttendanceImport_SrvAsync {

    public void saveRecord(AttendanceImport_Mdl mrTemp, AsyncCallback<DmResult<String>> result);
    
    public void importData(ImportData_Mdl import_Mdl, AsyncCallback<DmResult<ImportData_Mdl>> result);

    public void getCSVFile(ImportData_Mdl mrTemp, String fileName, AsyncCallback<DmResult<String>> result);

    public void readLogFile(ImportData_Mdl importData_Mdl, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getImportTo(SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<ImportData_Mdl>>> result);

    public void getImportData(AsyncCallback<DmResult<List<ImportData_Mdl>>> result);
    
    public void getDefaultMdl(AsyncCallback<DmResult<DefaultModel_Mdl>> result);

}
