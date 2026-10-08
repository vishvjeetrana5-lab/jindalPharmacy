package aarogya.selfToolkit.medicineAdvise.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.Objects;

public class GrdMedicineDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String displayName;
    private String compositionCode, compositionName;
    private String frequencyCode, frequencyName;
    private String dosePresentation;
    private String doseValue;
    private String doseText;
    private String doseDisplayText;
    private String netDoseValue;
    private String days;
    private String durationCode;
    private String durationName;
    private String duration;
    private String remark;
    private String instructionCode;
    private String instructionName;
    private String image;
    private String fieldUniqueCode;

    public GrdMedicineDetail_Mdl() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getFrequencyCode() {
        return frequencyCode;
    }

    public void setFrequencyCode(String frequencyCode) {
        this.frequencyCode = frequencyCode;
    }

    public String getFrequencyName() {
        return frequencyName;
    }

    public void setFrequencyName(String frequencyName) {
        this.frequencyName = frequencyName;
    }

    public String getDosePresentation() {
        return dosePresentation;
    }

    public void setDosePresentation(String dosePresentation) {
        this.dosePresentation = dosePresentation;
    }

    public String getDoseValue() {
        return doseValue;
    }

    public void setDoseValue(String doseValue) {
        this.doseValue = doseValue;
    }

    public String getDoseText() {
        return doseText;
    }

    public void setDoseText(String doseText) {
        this.doseText = doseText;
    }

    public String getDoseDisplayText() {
        return doseDisplayText;
    }

    public void setDoseDisplayText(String doseDisplayText) {
        this.doseDisplayText = doseDisplayText;
    }

    public String getNetDoseValue() {
        return netDoseValue;
    }

    public void setNetDoseValue(String netDoseValue) {
        this.netDoseValue = netDoseValue;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDurationCode() {
        return durationCode;
    }

    public void setDurationCode(String durationCode) {
        this.durationCode = durationCode;
    }

    public String getDurationName() {
        return durationName;
    }

    public void setDurationName(String durationName) {
        this.durationName = durationName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFieldUniqueCode() {
        return fieldUniqueCode;
    }

    public void setFieldUniqueCode(String fieldUniqueCode) {
        this.fieldUniqueCode = fieldUniqueCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInstructionCode() {
        return instructionCode;
    }

    public void setInstructionCode(String instructionCode) {
        this.instructionCode = instructionCode;
    }

    public String getInstructionName() {
        return instructionName;
    }

    public void setInstructionName(String instructionName) {
        this.instructionName = instructionName;
    }

    public GrdMedicineDetail_Mdl clone() {
        GrdMedicineDetail_Mdl local = new GrdMedicineDetail_Mdl();

        super.copy(local);

        local.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        local.setSharedKeyFieldName(this.getSharedKeyFieldName());
        local.setSharedModifiedBy(this.getSharedModifiedBy());
        local.setSharedPreparedBy(this.getSharedPreparedBy());
        local.setCompositionCode(this.getCompositionCode());
        local.setCompositionName(this.getCompositionName());
        local.setDays(this.getDays());
        local.setDoseText(this.getDoseText());
        local.setDoseValue(this.getDoseValue());
        local.setDoseDisplayText(this.getDoseDisplayText());
        local.setDurationCode(this.getDurationCode());
        local.setDurationName(this.getDurationName());
        local.setDuration(this.getDuration());
        local.setFrequencyCode(this.getFrequencyCode());
        local.setFrequencyName(this.getFrequencyName());
        local.setDosePresentation(this.getDosePresentation());
        local.setImage(this.getImage());
        local.setRemark(this.getRemark());
        local.setInstructionCode(this.getInstructionCode());
        local.setInstructionName(this.getInstructionName());
        local.setFieldUniqueCode(this.getFieldUniqueCode());

        return local;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.getSharedKeyFieldCode());
        hash = 13 * hash + Objects.hashCode(this.doseText);
        hash = 13 * hash + Objects.hashCode(this.frequencyCode);
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
        final GrdMedicineDetail_Mdl other = (GrdMedicineDetail_Mdl) obj;
        if (!Objects.equals(this.getSharedKeyFieldCode(), other.getSharedKeyFieldCode())) {
            return false;
        }
        if (!Objects.equals(this.doseText, other.doseText)) {
            return false;
        }
        if (!Objects.equals(this.frequencyCode, other.frequencyCode)) {
            return false;
        }
        return true;
    }

}
