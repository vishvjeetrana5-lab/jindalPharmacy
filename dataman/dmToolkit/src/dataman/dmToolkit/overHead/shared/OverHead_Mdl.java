package dataman.dmToolkit.overHead.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OverHead_Mdl extends KeyField implements Serializable, IsSerializable {

    private String atRate;
    private String amount;
    private String formula;
    private String actualFormula;
    private String excludeFormula;
    private String drCr;
    private String contraOhMCode;
    private String nature;
    private String overHeadAlias;
    private String atRateMandatory;
    private String amountMandatory;
    private String ohManualCode;
    private String uniqueId;
    private String ohSetUp;
    private String roundOff;
    private String atRateSaveIn;
    private String amountSaveIn;
    private String tableName;
    private String atRateEditable;
    private String amountEditable;
    private String acEditable;
    private String overHeadAliasEditable;
    private String showFormulaValue;
    private String printable;
    private String calLValOn;
    private String isPartialPosting;
    private List<OverHeadMA_Mdl> overHeadMAList; 

    public OverHead_Mdl() {
    }

    
    public String getPrintable() {
        return printable;
    }

    public void setPrintable(String printable) {
        this.printable = printable;
    }

    public String getCalLValOn() {
        return calLValOn;
    }

    public void setCalLValOn(String calLValOn) {
        this.calLValOn = calLValOn;
    }

    public String getOverHeadAliasEditable() {
        return overHeadAliasEditable;
    }

    public void setOverHeadAliasEditable(String overHeadAliasEditable) {
        this.overHeadAliasEditable = overHeadAliasEditable;
    }

    public List<OverHeadMA_Mdl> getOverHeadMA() {
        return overHeadMAList;
    }

    public void setOverHeadMA(List<OverHeadMA_Mdl> overHeadMAList) {
        this.overHeadMAList = overHeadMAList;
    }

    public String getAtRate() {
        return atRate;
    }

    public void setAtRate(String atRate) {
        this.atRate = atRate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getActualFormula() {
        return actualFormula;
    }

    public void setActualFormula(String actualFormula) {
        this.actualFormula = actualFormula;
    }

    public String getExcludeFormula() {
        return excludeFormula;
    }

    public void setExcludeFormula(String excludeFormula) {
        this.excludeFormula = excludeFormula;
    }

    public String getOhSetUp() {
        return ohSetUp;
    }

    public void setOhSetUp(String ohSetUp) {
        this.ohSetUp = ohSetUp;
    }

    public List<OverHeadMA_Mdl> getOverHeadMAList() {
        return overHeadMAList;
    }

    public void setOverHeadMAList(List<OverHeadMA_Mdl> overHeadMAList) {
        this.overHeadMAList = overHeadMAList;
    }

    public String getDrCr() {
        return drCr;
    }

    public void setDrCr(String drCr) {
        this.drCr = drCr;
    }

    public String getContraOhMCode() {
        return contraOhMCode;
    }

    public void setContraOhMCode(String contraOhMCode) {
        this.contraOhMCode = contraOhMCode;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getOverHeadAlias() {
        return overHeadAlias;
    }

    public void setOverHeadAlias(String overHeadAlias) {
        this.overHeadAlias = overHeadAlias;
    }

    public String getAtRateMandatory() {
        return atRateMandatory;
    }

    public void setAtRateMandatory(String atRateMandatory) {
        this.atRateMandatory = atRateMandatory;
    }

    public String getAmountMandatory() {
        return amountMandatory;
    }

    public void setAmountMandatory(String amountMandatory) {
        this.amountMandatory = amountMandatory;
    }

    public String getOhManualCode() {
        return ohManualCode;
    }

    public void setOhManualCode(String ohManualCode) {
        this.ohManualCode = ohManualCode;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getOHSetUp() {
        return ohSetUp;
    }

    public void setOHSetUp(String ohSetUp) {
        this.ohSetUp = ohSetUp;
    }

    public String getRoundOff() {
        return roundOff;
    }

    public void setRoundOff(String roundOff) {
        this.roundOff = roundOff;
    }

    public String getAtRateSaveIn() {
        return atRateSaveIn;
    }

    public void setAtRateSaveIn(String atRateSaveIn) {
        this.atRateSaveIn = atRateSaveIn;
    }

    public String getAmountSaveIn() {
        return amountSaveIn;
    }

    public void setAmountSaveIn(String amountSaveIn) {
        this.amountSaveIn = amountSaveIn;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAtRateEditable() {
        return atRateEditable;
    }

    public void setAtRateEditable(String atRateEditable) {
        this.atRateEditable = atRateEditable;
    }

    public String getAmountEditable() {
        return amountEditable;
    }

    public void setAmountEditable(String amountEditable) {
        this.amountEditable = amountEditable;
    }

    public String getAcEditable() {
        return acEditable;
    }

    public void setAcEditable(String acEditable) {
        this.acEditable = acEditable;
    }

    public String getShowFormulaValue() {
        return showFormulaValue;
    }

    public void setShowFormulaValue(String showFormulaValue) {
        this.showFormulaValue = showFormulaValue;
    }

    public OverHead_Mdl clone() {
        OverHead_Mdl overHead_Mdl = new OverHead_Mdl();

        super.copy(overHead_Mdl);

        overHead_Mdl.setAtRate(this.getAtRate());
        overHead_Mdl.setAmount(this.getAmount());
        overHead_Mdl.setFormula(this.getFormula());
        overHead_Mdl.setActualFormula(this.getActualFormula());
        overHead_Mdl.setExcludeFormula(this.getExcludeFormula());
        overHead_Mdl.setCalLValOn(this.getCalLValOn());
        overHead_Mdl.setDrCr(this.getDrCr());
        overHead_Mdl.setContraOhMCode(this.getContraOhMCode());
        overHead_Mdl.setNature(this.getNature());
        overHead_Mdl.setOverHeadAlias(this.getOverHeadAlias());
        overHead_Mdl.setAtRateMandatory(this.getAtRateMandatory());
        overHead_Mdl.setAmountMandatory(this.getAmountMandatory());
        overHead_Mdl.setOhManualCode(this.getOhManualCode());
        overHead_Mdl.setUniqueId(this.getUniqueId());
        overHead_Mdl.setOHSetUp(this.getOHSetUp());
        overHead_Mdl.setRoundOff(this.getRoundOff());
        overHead_Mdl.setAtRateSaveIn(this.getAtRateSaveIn());
        overHead_Mdl.setAmountSaveIn(this.getAmountSaveIn());
        overHead_Mdl.setTableName(this.getTableName());
        overHead_Mdl.setAtRateEditable(this.getAtRateEditable());
        overHead_Mdl.setAmountEditable(this.getAmountEditable());
        overHead_Mdl.setAcEditable(this.getAcEditable());
        overHead_Mdl.setOverHeadAliasEditable(this.getOverHeadAliasEditable());
        overHead_Mdl.setShowFormulaValue(this.getShowFormulaValue());
        overHead_Mdl.setOverHeadMA(cloneMAList(this.getOverHeadMA()));

        return overHead_Mdl;
    }

    public List<OverHeadMA_Mdl> cloneMAList(List<OverHeadMA_Mdl> list) {
        List<OverHeadMA_Mdl> overHeadMA_MdlList = null;

        if (list != null) {
            overHeadMA_MdlList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                overHeadMA_MdlList.add(list.get(i).clone());
            }
        }

        return overHeadMA_MdlList;
    }

    public static List<OverHead_Mdl> getOHListClone(List<OverHead_Mdl> list) {
        List<OverHead_Mdl> overHeadLineList = null;

        if (list != null) {
            overHeadLineList = new ArrayList<>();
            for (OverHead_Mdl overHeadLine : list) {
                overHeadLineList.add(overHeadLine.clone());
            }
        }

        return overHeadLineList;
    }

    public String getIsPartialPosting() {
        return isPartialPosting;
    }

    public void setIsPartialPosting(String isPartialPosting) {
        this.isPartialPosting = isPartialPosting;
    }
}
