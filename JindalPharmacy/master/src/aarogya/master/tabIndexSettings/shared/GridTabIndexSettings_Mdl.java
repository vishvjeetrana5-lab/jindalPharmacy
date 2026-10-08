package aarogya.master.tabIndexSettings.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.Objects;

public class GridTabIndexSettings_Mdl extends KeyField implements Serializable, IsSerializable {

    private String serialNo;

    public GridTabIndexSettings_Mdl() {
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.getSharedKeyFieldCode());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GridTabIndexSettings_Mdl other = (GridTabIndexSettings_Mdl) obj;
        if (!Objects.equals(this.getSharedKeyFieldCode(), other.getSharedKeyFieldCode())) {
            return false;
        }
        return true;
    }

}
