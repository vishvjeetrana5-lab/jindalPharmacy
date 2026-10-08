package dataman.payroll.leaveMaster.shared;

import dataman.dmBase.self.shared.KeyField;

public class Find_Mdl extends KeyField {

    private String shortName,nature, type;
  

    public Find_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

  
}
