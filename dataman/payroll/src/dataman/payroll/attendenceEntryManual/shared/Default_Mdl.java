
package dataman.payroll.attendenceEntryManual.shared;

import dataman.payroll.attendenceEntryManual.shared.*;
import dataman.payroll.attendenceEntryManual.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Default_Mdl extends KeyField implements Serializable, IsSerializable {

    private String attendanceName;
    private String attendanceCode;

   
    public Default_Mdl() {
    }

    public String getAttendanceName() {
        return attendanceName;
    }

    public void setAttendanceName(String attendanceName) {
        this.attendanceName = attendanceName;
    }

    public String getAttendanceCode() {
        return attendanceCode;
    }

    public void setAttendanceCode(String attendanceCode) {
        this.attendanceCode = attendanceCode;
    }

  
}

