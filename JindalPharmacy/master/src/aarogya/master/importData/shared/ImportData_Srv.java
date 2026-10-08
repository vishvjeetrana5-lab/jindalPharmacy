package aarogya.master.importData.shared;

import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.RemoteService;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ImportData_Srv extends RemoteService {

    public DmResult<ImportData_Mdl> importData(String startDate, ImportData_Mdl import_Mdl);

    public DmResult<String> getCSVFile(ImportData_Mdl mrTemp, String fileName);

    public DmResult<KeyFieldExtended> readLogFile(ImportData_Mdl importData_Mdl);

    public DmResultPageing<List<ImportData_Mdl>> getImportTo(SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile);

    public DmResult<List<ImportData_Mdl>> getImportData(String yearStart);
}
