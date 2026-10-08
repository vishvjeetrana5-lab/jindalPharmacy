package aarogya.selfToolkit.medicineAdvise.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdFavorite_Mdl extends KeyField implements Serializable, IsSerializable {

    private String compositionCode, compositionName;
    private String frequencyCode, frequencyName;
    private String instructionCode, instructionName;
    private String dosePresentation;
    private String doseValue;
    private String doseText;
    private String doseDisplayText;

    public GrdFavorite_Mdl() {
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
}
