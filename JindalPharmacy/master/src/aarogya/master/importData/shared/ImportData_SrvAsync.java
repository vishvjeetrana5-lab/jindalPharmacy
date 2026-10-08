package aarogya.master.importData.shared;

import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.*;
import java.util.List;

public interface ImportData_SrvAsync {

    public void importData(String startDate, ImportData_Mdl import_Mdl, AsyncCallback<DmResult<ImportData_Mdl>> result);

    public void getCSVFile(ImportData_Mdl mrTemp, String fileName, AsyncCallback<DmResult<String>> result);

    public void readLogFile(ImportData_Mdl importData_Mdl, AsyncCallback<DmResult<KeyFieldExtended>> result);

    public void getImportTo(SearchValue_Mdl searchValue_Mdl, DmHelper helper, boolean blnToEndOfFile, AsyncCallback<DmResultPageing<List<ImportData_Mdl>>> result);

    public void getImportData(String yearStart, AsyncCallback<DmResult<List<ImportData_Mdl>>> result);
}
