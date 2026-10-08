package dataman.dmToolkit.dmImportData.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImportData_Mdl extends KeyField implements Serializable, IsSerializable {

    private String importTo;
    private String importFrom;
    private String fileFormat;
    private String tableName;
    private String dataFilePath;
    private String logFilePath;
    private String isChecked;
    private String serverClassName;
    private String startDate;
    private String endDate;
    private String logFileNameForDownload;
    private DmImportStatus importStatus = DmImportStatus.NONE;
    private List<CSVData> hmCSV = new ArrayList<CSVData>();

    public ImportData_Mdl() {
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getImportTo() {
        return importTo;
    }

    public void setImportTo(String importTo) {
        this.importTo = importTo;
    }

    public String getImportFrom() {
        return importFrom;
    }

    public void setImportFrom(String importFrom) {
        this.importFrom = importFrom;
    }

    public String getDataFilePath() {
        return dataFilePath;
    }

    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public void setLogFileName(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public String getServerClassName() {
        return serverClassName;
    }

    public void setServerClassName(String serverClassName) {
        this.serverClassName = serverClassName;
    }

    public List<CSVData> getHmCSV() {
        return hmCSV;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
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
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.importTo);
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
        final ImportData_Mdl other = (ImportData_Mdl) obj;
        if (!Objects.equals(this.importTo, other.importTo)) {
            return false;
        }
        return true;
    }

    public String getLogFileNameForDownload() {
        return logFileNameForDownload;
    }

    public void setLogFileNameForDownload(String logFileNameForDownload) {
        this.logFileNameForDownload = logFileNameForDownload;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
