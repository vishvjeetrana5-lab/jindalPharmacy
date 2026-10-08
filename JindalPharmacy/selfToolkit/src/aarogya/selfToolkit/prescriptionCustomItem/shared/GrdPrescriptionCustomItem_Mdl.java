package aarogya.selfToolkit.prescriptionCustomItem.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.Objects;

public class GrdPrescriptionCustomItem_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String groupCode;
    private String groupName;
    private String remark;
    private String image;

    public GrdPrescriptionCustomItem_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public GrdPrescriptionCustomItem_Mdl clone() {
        GrdPrescriptionCustomItem_Mdl local = new GrdPrescriptionCustomItem_Mdl();

        super.copy(local);

        local.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        local.setSharedKeyFieldName(this.getSharedKeyFieldName());
        local.setSharedModifiedBy(this.getSharedModifiedBy());
        local.setSharedPreparedBy(this.getSharedPreparedBy());
        local.setShortName(this.getShortName());
        local.setGroupCode(this.getGroupCode());
        local.setGroupName(this.getGroupName());
        local.setRemark(this.getRemark());
        local.setImage(this.getImage());

        return local;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.getSharedKeyFieldCode());
        hash = 97 * hash + Objects.hashCode(this.remark);
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
        final GrdPrescriptionCustomItem_Mdl other = (GrdPrescriptionCustomItem_Mdl) obj;
        if (!Objects.equals(this.getSharedKeyFieldCode(), other.getSharedKeyFieldCode())) {
            return false;
        }
        if (!Objects.equals(this.remark, other.remark)) {
            return false;
        }
        return true;
    }

}
