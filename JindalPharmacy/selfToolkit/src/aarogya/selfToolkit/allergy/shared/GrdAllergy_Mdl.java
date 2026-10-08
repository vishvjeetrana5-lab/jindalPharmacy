package aarogya.selfToolkit.allergy.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.Objects;

public class GrdAllergy_Mdl extends KeyField implements Serializable, IsSerializable {

    private String natureCode;
    private String natureName;
    private String remark;
    private String image;

    public GrdAllergy_Mdl() {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public GrdAllergy_Mdl clone() {
        GrdAllergy_Mdl local = new GrdAllergy_Mdl();

        super.copy(local);

        local.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        local.setSharedKeyFieldName(this.getSharedKeyFieldName());
        local.setSharedModifiedBy(this.getSharedModifiedBy());
        local.setSharedPreparedBy(this.getSharedPreparedBy());
        local.setNatureCode(this.getNatureCode());
        local.setNatureName(this.getNatureName());
        local.setImage(this.getImage());
        local.setRemark(this.getRemark());

        return local;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(getSharedKeyFieldName());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrdAllergy_Mdl other = (GrdAllergy_Mdl) obj;
        if (!Objects.equals(getSharedKeyFieldName(), other.getSharedKeyFieldName())) {
            return false;
        }
        return true;
    }

}
