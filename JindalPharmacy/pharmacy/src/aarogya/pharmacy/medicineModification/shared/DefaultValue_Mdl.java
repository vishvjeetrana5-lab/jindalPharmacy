package aarogya.pharmacy.medicineModification.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String medicineNameAlphabetCase;

    public DefaultValue_Mdl() {
    }

    public String getMedicineNameAlphabetCase() {
        return medicineNameAlphabetCase;
    }

    public void setMedicineNameAlphabetCase(String medicineNameAlphabetCase) {
        this.medicineNameAlphabetCase = medicineNameAlphabetCase;
    }

}
