package dataman.utility.copyUserPermission.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.utility.companyMaster.shared.*;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHCompany_Mdl extends KeyField implements Serializable,IsSerializable{

    private String shortName;
//    private String latestCompCode;
    private String pltCode;
    private String startDate;
    private String endDate;
    private String prefix;
    private String financeYear;

    public TxtHCompany_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

//    public String getLatestCompCode() {
//        return latestCompCode;
//    }
//
//    public void setLatestCompCode(String latestCompCode) {
//        this.latestCompCode = latestCompCode;
//    }

    public String getPltCode() {
        return pltCode;
    }

    public void setPltCode(String pltCode) {
        this.pltCode = pltCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getFinanceYear() {
        return financeYear;
    }

    public void setFinanceYear(String financeYear) {
        this.financeYear = financeYear;
    }

}
