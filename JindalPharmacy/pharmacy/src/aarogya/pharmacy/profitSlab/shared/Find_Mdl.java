package aarogya.pharmacy.profitSlab.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String profitPercent;

    public Find_Mdl() {
    }

    public String getMedicineTypeTag() {
        return medicineTypeTag;
    }

    public void setMedicineTypeTag(String medicineTypeTag) {
        this.medicineTypeTag = medicineTypeTag;
    }
    private String medicineTypeTag;
    private String medicineTypeText;

    public String getMedicineTypeText() {
        return medicineTypeText;
    }

    public void setMedicineTypeText(String medicineTypeText) {
        this.medicineTypeText = medicineTypeText;
    }

    public String getProfitPercent() {
        return profitPercent;
    }

    public void setProfitPercent(String profitPercent) {
        this.profitPercent = profitPercent;
    }
}
