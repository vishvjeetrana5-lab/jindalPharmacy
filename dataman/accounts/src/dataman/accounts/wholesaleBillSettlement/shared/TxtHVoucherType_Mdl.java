package dataman.accounts.wholesaleBillSettlement.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class TxtHVoucherType_Mdl extends KeyField implements Serializable, IsSerializable {

    private String category;
    private String DefaultAc;
    private String DefaultAcManualCode;
    private String DAName;
    private String Nature;

    public TxtHVoucherType_Mdl() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDefaultAc() {
        return DefaultAc;
    }

    public void setDefaultAc(String DefaultAc) {
        this.DefaultAc = DefaultAc;
    }

    public String getDAName() {
        return DAName;
    }

    public void setDAName(String DAName) {
        this.DAName = DAName;
    }

    public String getNature() {
        return Nature;
    }

    public void setNature(String Nature) {
        this.Nature = Nature;
    }

    public String getDefaultAcManualCode() {
        return DefaultAcManualCode;
    }

    public void setDefaultAcManualCode(String DefaultAcManualCode) {
        this.DefaultAcManualCode = DefaultAcManualCode;
    }
}
