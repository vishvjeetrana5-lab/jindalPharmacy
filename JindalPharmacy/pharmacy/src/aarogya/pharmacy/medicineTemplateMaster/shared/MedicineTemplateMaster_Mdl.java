package aarogya.pharmacy.medicineTemplateMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicineTemplateMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String templateName;

    private List<GrdMedicineTemplateMasterDetails_Mdl> grdData = new ArrayList<GrdMedicineTemplateMasterDetails_Mdl>();

    public MedicineTemplateMaster_Mdl() {

    }

    public List<GrdMedicineTemplateMasterDetails_Mdl> getGrdData() {
        return grdData;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
