package dataman.accounts.tdsCategoryMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String type;
    private String section;
    private String tdsFor;

    public Find_Mdl() {
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTdsFor() {
        return tdsFor;
    }

    public void setTdsFor(String tdsFor) {
        this.tdsFor = tdsFor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
