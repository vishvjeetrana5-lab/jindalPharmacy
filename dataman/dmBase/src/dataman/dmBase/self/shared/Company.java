package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Company implements Serializable,IsSerializable{

    private byte[] companyCode;
    private byte[] companyName;
    private byte[] currentYear;
    private byte[] startDate;
    private byte[] endDate;
    private byte[] vPrefix;

    public Company() {
    }

    public String getCompanyCode() {
        return new String(companyCode);
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode.getBytes();
    }
    
    public String getCompanyName() {
        return new String(companyName);
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName.getBytes();
    }

    public String getCurrentYear() {
        return new String(currentYear);
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear.getBytes();
    }

    public String getVPrefix() {
        return new String(vPrefix);
    }

    public void setVPrefix(String vPrefix) {
        this.vPrefix = vPrefix.getBytes();
    }

    public String getStartDate() {
        return new String(startDate);
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate.getBytes();
    }

    public String getEndDate() {
        return new String(endDate);
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate.getBytes();
    }
}
