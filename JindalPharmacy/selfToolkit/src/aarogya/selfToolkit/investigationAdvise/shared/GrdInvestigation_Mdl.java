package aarogya.selfToolkit.investigationAdvise.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.Objects;

public class GrdInvestigation_Mdl extends KeyField implements Serializable, IsSerializable {

    private String categoryCode;
    private String categoryName;
    private String type;
    private String image;
    private boolean pkg;

    public GrdInvestigation_Mdl() {
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isPkg() {
        return pkg;
    }

    public void setPkg(boolean pkg) {
        this.pkg = pkg;
    }

    public GrdInvestigation_Mdl clone() {
        GrdInvestigation_Mdl local = new GrdInvestigation_Mdl();

        super.copy(local);

        local.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        local.setSharedKeyFieldName(this.getSharedKeyFieldName());
        local.setSharedModifiedBy(this.getSharedModifiedBy());
        local.setSharedPreparedBy(this.getSharedPreparedBy());
        local.setCategoryCode(this.getCategoryCode());
        local.setCategoryName(this.getCategoryName());
        local.setType(this.getType());
        local.setPkg(this.isPkg());
        local.setImage(this.getImage());

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
        final GrdInvestigation_Mdl other = (GrdInvestigation_Mdl) obj;
        if (!Objects.equals(getSharedKeyFieldName(), other.getSharedKeyFieldName())) {
            return false;
        }
        return true;
    }

}
