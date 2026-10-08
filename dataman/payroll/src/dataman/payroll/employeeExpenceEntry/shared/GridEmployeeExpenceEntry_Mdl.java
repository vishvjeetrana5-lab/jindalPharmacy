package dataman.payroll.employeeExpenceEntry.shared;


import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GridEmployeeExpenceEntry_Mdl extends KeyField implements Serializable, IsSerializable {

   private String nature;
   private String natureCode;
   private String natureName;
    private String value;
   

    public GridEmployeeExpenceEntry_Mdl() {
    }

 

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getNatureName() {
        return natureName;
    }

    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }



 

}
