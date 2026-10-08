package aarogya.pharmacy.medicineCompositionMaster.shared;

import aarogya.pharmacy.medicineCompositionMaster.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class MedicineCompositionMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String natureName;
    private String natureCode;
    private String activeCode;
    private String activeName;
    private String resGrpName;
    private String resGrpCode;

    public MedicineCompositionMaster_Mdl() {
    }

    public String getResGrpName() {
        return resGrpName;
    }

    public void setResGrpName(String resGrpName) {
        this.resGrpName = resGrpName;
    }

    public String getResGrpCode() {
        return resGrpCode;
    }

    public void setResGrpCode(String resGrpCode) {
        this.resGrpCode = resGrpCode;
    }

    public String getNatureName() {
        return natureName;
    }

    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

}
