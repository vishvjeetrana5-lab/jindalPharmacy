package aarogya.initializer.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import java.io.Serializable;
import java.util.Objects;

public class GrdInitializer_Mdl extends KeyField implements Serializable, IsSerializable {

    private String logFilePath;
    private String logFileName;
    private String dataPath;
    private String serverClassName;
    private String yearStart;
    private String yearEnd;
    private String tableName;
    private DmImportStatus importStatus = DmImportStatus.NONE;

    public GrdInitializer_Mdl() {
    }

    public GrdInitializer_Mdl(String code, String name, String serverClassName) {
        this.setSharedKeyFieldCode(code);
        this.setSharedKeyFieldName(name);
        this.serverClassName = serverClassName;
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public String getServerClassName() {
        return serverClassName;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public String getYearStart() {
        return yearStart;
    }

    public void setYearStart(String yearStart) {
        this.yearStart = yearStart;
    }

    public String getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(String yearEnd) {
        this.yearEnd = yearEnd;
    }

    public DmImportStatus getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(DmImportStatus importStatus) {
        this.importStatus = importStatus;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.getSharedKeyFieldCode());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrdInitializer_Mdl other = (GrdInitializer_Mdl) obj;
        if (!Objects.equals(this.getSharedKeyFieldCode(), other.getSharedKeyFieldCode())) {
            return false;
        }
        return true;
    }
}
