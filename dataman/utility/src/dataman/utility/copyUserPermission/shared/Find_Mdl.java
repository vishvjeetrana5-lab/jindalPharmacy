package dataman.utility.copyUserPermission.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.utility.companyMaster.shared.*;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable,IsSerializable{

    private String yearStartDate;
    private String yearEndDate;
    private String currentFinancialYear;

    public Find_Mdl() {
    }

    public String getCurrentFinancialYear() {
        return currentFinancialYear;
    }

    public void setCurrentFinancialYear(String currentFinancialYear) {
        this.currentFinancialYear = currentFinancialYear;
    }

    public String getYearStartDate() {
        return yearStartDate;
    }

    public void setYearStartDate(String yearStartDate) {
        this.yearStartDate = yearStartDate;
    }

    public String getYearEndDate() {
        return yearEndDate;
    }

    public void setYearEndDate(String yearEndDate) {
        this.yearEndDate = yearEndDate;
    }

}
