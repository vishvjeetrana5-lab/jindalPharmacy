package aarogya.selfToolkit.D365_To_His_DataSync.shared;

public class MedicineCompoMast_Mdl {

    private String compositionCode;
    private String compositionName;
    private String restrictGroupCode;
    private String isActive;

    public MedicineCompoMast_Mdl() {
    }

    public String getCompositionCode() {
        return compositionCode;
    }

    public void setCompositionCode(String compositionCode) {
        this.compositionCode = compositionCode;
    }

    public String getCompositionName() {
        return compositionName;
    }

    public void setCompositionName(String compositionName) {
        this.compositionName = compositionName;
    }

    public String getRestrictGroupCode() {
        return restrictGroupCode;
    }

    public void setRestrictGroupCode(String restrictGroupCode) {
        this.restrictGroupCode = restrictGroupCode;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}
