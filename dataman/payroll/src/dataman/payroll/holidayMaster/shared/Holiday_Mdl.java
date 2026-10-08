package dataman.payroll.holidayMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.payroll.self.shared.PREnum.HOLIDAY_TYPE;
import java.io.Serializable;

public class Holiday_Mdl extends KeyField implements Serializable, IsSerializable {

    private String holidayDate;
    private HOLIDAY_TYPE holidayType;

    public Holiday_Mdl() {
    }

    public String getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        this.holidayDate = holidayDate;
    }

    public HOLIDAY_TYPE getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(HOLIDAY_TYPE holidayType) {
        this.holidayType = holidayType;
    }

}
