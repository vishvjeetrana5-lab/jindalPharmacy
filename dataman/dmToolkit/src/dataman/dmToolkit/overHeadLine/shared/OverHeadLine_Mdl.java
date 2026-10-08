package dataman.dmToolkit.overHeadLine.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OverHeadLine_Mdl extends KeyField implements Serializable, IsSerializable {

    private String atRate;
    private String amount;
    private String formula;
    private String nature;
    private String overHeadAlias;
    private String atRateMandatory;
    private String amountMandatory;
    private String ohManualCode;
    private String roundOff;
    private String uniqueId;
    private String atRateSaveIn;
    private String amountSaveIn;
    private String tableName;
    private String atRateEditable;
    private String amountEditable;
    private String acEditable;
    private String overHeadAliasEditable;
    private String showFormulaValue;
    private String printable;
    private String accountName;
    private String accountCode;

    public OverHeadLine_Mdl() {
    }

    public String getPrintable() {
        return printable;
    }

    public void setPrintable(String printable) {
        this.printable = printable;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getOverHeadAliasEditable() {
        return overHeadAliasEditable;
    }

    public void setOverHeadAliasEditable(String overHeadAliasEditable) {
        this.overHeadAliasEditable = overHeadAliasEditable;
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

    public String getRoundOff() {
        return roundOff;
    }

    public void setRoundOff(String roundOff) {
        this.roundOff = roundOff;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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

    public OverHeadLine_Mdl clone() {
        OverHeadLine_Mdl overHead_Mdl = new OverHeadLine_Mdl();

        super.copy(overHead_Mdl);

        overHead_Mdl.setAtRate(this.getAtRate());
        overHead_Mdl.setAmount(this.getAmount());
        overHead_Mdl.setFormula(this.getFormula());
        overHead_Mdl.setNature(this.getNature());
        overHead_Mdl.setOverHeadAlias(this.getOverHeadAlias());
        overHead_Mdl.setAtRateMandatory(this.getAtRateMandatory());
        overHead_Mdl.setAmountMandatory(this.getAmountMandatory());
        overHead_Mdl.setOhManualCode(this.getOhManualCode());
        overHead_Mdl.setRoundOff(this.getRoundOff());
        overHead_Mdl.setUniqueId(this.getUniqueId());
        overHead_Mdl.setAtRateSaveIn(this.getAtRateSaveIn());
        overHead_Mdl.setAmountSaveIn(this.getAmountSaveIn());
        overHead_Mdl.setTableName(this.getTableName());
        overHead_Mdl.setAtRateEditable(this.getAtRateEditable());
        overHead_Mdl.setAmountEditable(this.getAmountEditable());
        overHead_Mdl.setAcEditable(this.getAcEditable());
        overHead_Mdl.setOverHeadAliasEditable(this.getOverHeadAliasEditable());
        overHead_Mdl.setShowFormulaValue(this.getShowFormulaValue());
        overHead_Mdl.setAccountCode(this.getAccountCode());
        overHead_Mdl.setAccountName(this.getAccountName());

        return overHead_Mdl;
    }

    public static List<OverHeadLine_Mdl> getOHLListClone(List<OverHeadLine_Mdl> list) {
        List<OverHeadLine_Mdl> overHeadLineList = null;

        if (list != null) {
            overHeadLineList = new ArrayList<>();
            for (OverHeadLine_Mdl overHeadLine : list) {
                overHeadLineList.add(overHeadLine.clone());
            }
        }

        return overHeadLineList;
    }

}
