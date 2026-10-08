package dataman.utility.companyMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdCompanyMaster_Mdl extends KeyField implements Serializable,IsSerializable{

    private String currentYear;
    private String previousYear;
    private String yearStart;
    private String yearEnd;
    private String voucherPrefix;
    private String databaseName;
    private String fileDatabaseName;
    private String latestCompCode;

    public GrdCompanyMaster_Mdl() {
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    public String getPreviousYear() {
        return previousYear;
    }

    public void setPreviousYear(String previousYear) {
        this.previousYear = previousYear;
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

    public String getVoucherPrefix() {
        return voucherPrefix;
    }

    public void setVoucherPrefix(String voucherPrefix) {
        this.voucherPrefix = voucherPrefix;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getFileDatabaseName() {
        return fileDatabaseName;
    }

    public void setFileDatabaseName(String fileDatabaseName) {
        this.fileDatabaseName = fileDatabaseName;
    }

    public String getLatestCompCode() {
        return latestCompCode;
    }

    public void setLatestCompCode(String latestCompCode) {
        this.latestCompCode = latestCompCode;
    }
    
}
