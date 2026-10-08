/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.payroll.attendanceImport.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import java.io.Serializable;

/**
 *
 * @author Avinash Chandra
 */
public class DefaultModel_Mdl implements Serializable, IsSerializable {

    private ImportData_Mdl importMdl;
    private String logFilePath;
    private String logFileName;

    public DefaultModel_Mdl() {
    }

    public ImportData_Mdl getImportMdl() {
        return importMdl;
    }

    public void setImportMdl(ImportData_Mdl importMdl) {
        this.importMdl = importMdl;
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }
}
