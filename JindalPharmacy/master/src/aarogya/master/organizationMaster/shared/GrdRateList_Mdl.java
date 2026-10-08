package aarogya.master.organizationMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;
import java.util.Objects;

public class GrdRateList_Mdl extends KeyField implements Serializable, IsSerializable {

    private String rateListType;
    private String applicableFrom;

    public GrdRateList_Mdl() {
    }

    public String getApplicableFrom() {
        return applicableFrom;
    }

    public void setApplicableFrom(String applicableFrom) {
        this.applicableFrom = applicableFrom;
    }

    public String getRateListType() {
        return rateListType;
    }

    public void setRateListType(String rateListType) {
        this.rateListType = rateListType;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.getSharedKeyFieldCode());
        hash = 11 * hash + Objects.hashCode(this.applicableFrom);
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
        final GrdRateList_Mdl other = (GrdRateList_Mdl) obj;
        if (!Objects.equals(this.getSharedKeyFieldCode(), other.getSharedKeyFieldCode())) {
            return false;
        }
        if (!Objects.equals(this.applicableFrom, other.applicableFrom)) {
            return false;
        }
        return true;
    }
}
