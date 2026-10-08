package aarogya.master.rateListMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.Objects;

public class GrdRoomCategory_Mdl extends KeyField implements Serializable, IsSerializable {

    private String rate;
    private String discountPercentage;
    private String discountAmount;

    public GrdRoomCategory_Mdl() {
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public GrdRoomCategory_Mdl clone() {
        GrdRoomCategory_Mdl local = new GrdRoomCategory_Mdl();

        super.copy(local);

        local.setRate(this.getRate());
        local.setDiscountAmount(this.getDiscountAmount());
        local.setDiscountPercentage(this.getDiscountPercentage());

        return local;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.getSharedKeyFieldCode());
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
        final GrdRoomCategory_Mdl other = (GrdRoomCategory_Mdl) obj;
        if (!Objects.equals(this.getSharedKeyFieldCode(), other.getSharedKeyFieldCode())) {
            return false;
        }
        return true;
    }
}
