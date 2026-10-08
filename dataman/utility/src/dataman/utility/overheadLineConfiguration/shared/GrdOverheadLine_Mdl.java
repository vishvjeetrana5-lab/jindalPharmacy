package dataman.utility.overheadLineConfiguration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdOverheadLine_Mdl extends KeyField implements Serializable,IsSerializable{

    private String srlNo;
    private String overhead;
    private String overheadCode;
    private String manualCode;
    private String formula;
    private String tableName;
    private String atRate;
    private String atRateEditable;
    private String atRateEditableCode;
    private String amtEditable;
    private String amtEditableCode;
    private String atRateMandatory;
    private String atRateMandatoryCode;
    private String amtMandatory;
    private String amtMandatoryCode;
    private String overheadSaveIn;
    private String overheadSaveInCode;
    private String acSave;
    private String acSaveCode;
    private String overheadEditable;
    private String overheadEditableCode;
    private String roundOff;
    private String roundOffCode;
    private String uniqueID;
    private String AmtSaveIn;
    private String AmtSaveInCode;
    private String atRateSaveIn;
    private String atRateSaveInCode;
    private String nature;
    private String natureCode;
    private String showFormulaValue;
    private String showFormulaValueCode;
    private String aCEditableCode;
    private String acEditable;
    private String printableCode;
    private String printable;
    private String postAC;
    private String postACCode;
    private String mcColorStatus;
    private String pacColorStatus;
    private String atRateAmountTag;
    private String atRateAmountText;

    public GrdOverheadLine_Mdl() {
    }

    public String getAcEditable() {
        return acEditable;
    }

    public void setAcEditable(String acEditable) {
        this.acEditable = acEditable;
    }

    public String getaCEditableCode() {
        return aCEditableCode;
    }

    public void setaCEditableCode(String aCEditableCode) {
        this.aCEditableCode = aCEditableCode;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAmtSaveIn() {
        return AmtSaveIn;
    }

    public void setAmtSaveIn(String AmtSaveIn) {
        this.AmtSaveIn = AmtSaveIn;
    }

    public String getAmtSaveInCode() {
        return AmtSaveInCode;
    }

    public void setAmtSaveInCode(String AmtSaveInCode) {
        this.AmtSaveInCode = AmtSaveInCode;
    }

    public String getAtRateSaveIn() {
        return atRateSaveIn;
    }

    public void setAtRateSaveIn(String atRateSaveIn) {
        this.atRateSaveIn = atRateSaveIn;
    }

    public String getAtRateSaveInCode() {
        return atRateSaveInCode;
    }

    public void setAtRateSaveInCode(String atRateSaveInCode) {
        this.atRateSaveInCode = atRateSaveInCode;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getShowFormulaValue() {
        return showFormulaValue;
    }

    public void setShowFormulaValue(String showFormulaValue) {
        this.showFormulaValue = showFormulaValue;
    }

    public String getShowFormulaValueCode() {
        return showFormulaValueCode;
    }

    public void setShowFormulaValueCode(String showFormulaValueCode) {
        this.showFormulaValueCode = showFormulaValueCode;
    }

    public String getRoundOff() {
        return roundOff;
    }

    public void setRoundOff(String roundOff) {
        this.roundOff = roundOff;
    }

    public String getRoundOffCode() {
        return roundOffCode;
    }

    public void setRoundOffCode(String roundOffCode) {
        this.roundOffCode = roundOffCode;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getOverheadSaveIn() {
        return overheadSaveIn;
    }

    public void setOverheadSaveIn(String overheadSaveIn) {
        this.overheadSaveIn = overheadSaveIn;
    }

    public String getOverheadSaveInCode() {
        return overheadSaveInCode;
    }

    public void setOverheadSaveInCode(String overheadSaveInCode) {
        this.overheadSaveInCode = overheadSaveInCode;
    }

    public String getAcSave() {
        return acSave;
    }

    public void setAcSave(String acSave) {
        this.acSave = acSave;
    }

    public String getAcSaveCode() {
        return acSaveCode;
    }

    public void setAcSaveCode(String acSaveCode) {
        this.acSaveCode = acSaveCode;
    }

    public String getOverheadEditable() {
        return overheadEditable;
    }

    public void setOverheadEditable(String overheadEditable) {
        this.overheadEditable = overheadEditable;
    }

    public String getOverheadEditableCode() {
        return overheadEditableCode;
    }

    public void setOverheadEditableCode(String overheadEditableCode) {
        this.overheadEditableCode = overheadEditableCode;
    }

    private static int counter = 0;

    public String getOverhead() {
        return overhead;
    }

    public void setOverhead(String overhead) {
        this.overhead = overhead;
    }

    public String getOverheadCode() {
        return overheadCode;
    }

    public void setOverheadCode(String overheadCode) {
        this.overheadCode = overheadCode;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getAtRate() {
        return atRate;
    }

    public void setAtRate(String atRate) {
        this.atRate = atRate;
    }

    public String getAtRateEditable() {
        return atRateEditable;
    }

    public void setAtRateEditable(String atRateEditable) {
        this.atRateEditable = atRateEditable;
    }

    public String getAtRateEditableCode() {
        return atRateEditableCode;
    }

    public void setAtRateEditableCode(String atRateEditableCode) {
        this.atRateEditableCode = atRateEditableCode;
    }

    public String getAmtEditable() {
        return amtEditable;
    }

    public void setAmtEditable(String amtEditable) {
        this.amtEditable = amtEditable;
    }

    public String getAmtEditableCode() {
        return amtEditableCode;
    }

    public void setAmtEditableCode(String amtEditableCode) {
        this.amtEditableCode = amtEditableCode;
    }

    public String getAtRateMandatory() {
        return atRateMandatory;
    }

    public void setAtRateMandatory(String atRateMandatory) {
        this.atRateMandatory = atRateMandatory;
    }

    public String getAtRateMandatoryCode() {
        return atRateMandatoryCode;
    }

    public void setAtRateMandatoryCode(String atRateMandatoryCode) {
        this.atRateMandatoryCode = atRateMandatoryCode;
    }

    public String getAmtMandatory() {
        return amtMandatory;
    }

    public void setAmtMandatory(String amtMandatory) {
        this.amtMandatory = amtMandatory;
    }

    public String getAmtMandatoryCode() {
        return amtMandatoryCode;
    }

    public void setAmtMandatoryCode(String amtMandatoryCode) {
        this.amtMandatoryCode = amtMandatoryCode;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        GrdOverheadLine_Mdl.counter = counter;
    }

    public String getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(String srlNo) {
        this.srlNo = srlNo;
    }

    public String getPostAC() {
        return postAC;
    }

    public void setPostAC(String postAC) {
        this.postAC = postAC;
    }

    public String getPostACCode() {
        return postACCode;
    }

    public void setPostACCode(String postACCode) {
        this.postACCode = postACCode;
    }

    public String getPrintableCode() {
        return printableCode;
    }

    public void setPrintableCode(String printableCode) {
        this.printableCode = printableCode;
    }

    public String getPrintable() {
        return printable;
    }

    public void setPrintable(String printable) {
        this.printable = printable;
    }

    public String getMcColorStatus() {
        return mcColorStatus;
    }

    public void setMcColorStatus(String mcColorStatus) {
        this.mcColorStatus = mcColorStatus;
    }

    public String getPacColorStatus() {
        return pacColorStatus;
    }

    public void setPacColorStatus(String pacColorStatus) {
        this.pacColorStatus = pacColorStatus;
    }

    public String getAtRateAmountTag() {
        return atRateAmountTag;
    }

    public void setAtRateAmountTag(String atRateAmountTag) {
        this.atRateAmountTag = atRateAmountTag;
    }

    public String getAtRateAmountText() {
        return atRateAmountText;
    }

    public void setAtRateAmountText(String atRateAmountText) {
        this.atRateAmountText = atRateAmountText;
    }

}
