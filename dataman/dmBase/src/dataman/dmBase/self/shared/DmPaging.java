package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.dmDynamicReportBuilder.shared.DmMasterReport;
import java.io.Serializable;

public class DmPaging implements Serializable, IsSerializable {

//<editor-fold defaultstate="collapsed" desc="Result">
    public enum Result implements Serializable, IsSerializable {

        DEFAULT(1), PRINT(2), XLS(3);

        private int value;

        private Result(int avalue) {
            this.value = avalue;
        }

        public int getValue() {
            return this.value;
        }
    }
//</editor-fold>

    private int rowsPerPage;
    private int rowsStartFrom;
    private int rowCount;
    private boolean endOfFile = false;
    private boolean beginOfFile = false;
    private Result result = Result.DEFAULT;
    private DmMasterReport report = null;
     private String fileName, filePath;
    public DmPaging() {
    }

    public void positioning(int intRecordsFetched, boolean blnEOF) {
        if (intRecordsFetched < rowsPerPage || blnEOF) {
            endOfFile = true;
        } else {
            endOfFile = false;
        }

        if (rowsStartFrom <= 0) {
            beginOfFile = true;
        } else {
            beginOfFile = false;
        }
    }

    public boolean isEndOfFile() {
        return endOfFile;
    }

    public boolean isBeginOfFile() {
        return beginOfFile;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public int getRowsStartFrom() {
        return rowsStartFrom;
    }

    public void setRowsStartFrom(int rowsStartFrom) {
        if (rowsStartFrom < 0) {
            rowsStartFrom = 0;
        }
        this.rowsStartFrom = rowsStartFrom;
    }

    public Result getResultFormat() {
        return this.result;
    }

    public void setResultFormat(Result format) {
        this.result = format;
    }

    public DmMasterReport getReport() {
        return report;
    }

    public void setReport(DmMasterReport report) {
        this.report = report;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
