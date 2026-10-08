package dataman.dmToolkit.dmImportData.server;

import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;

public interface DmImportData_Interface {

    public ImportData_Mdl getImportDataModel();

    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData);

}
