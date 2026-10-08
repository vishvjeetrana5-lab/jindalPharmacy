package aarogya.inventory.grn.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdLabEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vSNo;
    private String range1;
    private String range2;
    private String formulaForQty;
    private String formulaForAmt;
    private String formulaForValue;
    private String valueType;
    private String mainFoundValue;
    private String rangeUnit;
    private String helpSource;
    private String defaultRemark;
    private String impurityManualCode;
    private String labQty;
    private String labAmt;

    public GrdLabEntry_Mdl() {
    }

    public String getDefaultRemark() {
        return defaultRemark;
    }

    public void setDefaultRemark(String defaultRemark) {
        this.defaultRemark = defaultRemark;
    }

    public String getImpurityManualCode() {
        return impurityManualCode;
    }

    public void setImpurityManualCode(String impurityManualCode) {
        this.impurityManualCode = impurityManualCode;
    }

    public String getLabQty() {
        return labQty;
    }

    public void setLabQty(String labQty) {
        this.labQty = labQty;
    }

    public String getLabAmt() {
        return labAmt;
    }

    public void setLabAmt(String labAmt) {
        this.labAmt = labAmt;
    }

    public String getRangeUnit() {
        return rangeUnit;
    }

    public void setRangeUnit(String rangeUnit) {
        this.rangeUnit = rangeUnit;
    }

    public String getHelpSource() {
        return helpSource;
    }

    public void setHelpSource(String helpSource) {
        this.helpSource = helpSource;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getMainFoundValue() {
        return mainFoundValue;
    }

    public void setMainFoundValue(String mainFoundValue) {
        this.mainFoundValue = mainFoundValue;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getRange1() {
        return range1;
    }

    public void setRange1(String range1) {
        this.range1 = range1;
    }

    public String getRange2() {
        return range2;
    }

    public void setRange2(String range2) {
        this.range2 = range2;
    }

    public String getFormulaForQty() {
        return formulaForQty;
    }

    public void setFormulaForQty(String formulaForQty) {
        this.formulaForQty = formulaForQty;
    }

    public String getFormulaForAmt() {
        return formulaForAmt;
    }

    public void setFormulaForAmt(String formulaForAmt) {
        this.formulaForAmt = formulaForAmt;
    }

    public String getFormulaForValue() {
        return formulaForValue;
    }

    public void setFormulaForValue(String formulaForValue) {
        this.formulaForValue = formulaForValue;
    }

    
    public GrdLabEntry_Mdl clone() {
        GrdLabEntry_Mdl grn_Mdl = new GrdLabEntry_Mdl();

        copy(grn_Mdl);

        return grn_Mdl;
    }

    private void copy(GrdLabEntry_Mdl grn_Mdl) {
        grn_Mdl.setSharedKeyFieldCode(grn_Mdl.getSharedKeyFieldCode());
        grn_Mdl.setSharedKeyFieldName(grn_Mdl.getSharedKeyFieldName());
        grn_Mdl.setvSNo(grn_Mdl.getvSNo());
        grn_Mdl.setRange1(grn_Mdl.getRange1());
        grn_Mdl.setRange2(grn_Mdl.getRange2());
        grn_Mdl.setFormulaForQty(grn_Mdl.getFormulaForQty());
        grn_Mdl.setFormulaForAmt(grn_Mdl.getFormulaForAmt());
        grn_Mdl.setFormulaForValue(grn_Mdl.getFormulaForValue());
        grn_Mdl.setValueType(grn_Mdl.getValueType());
        grn_Mdl.setMainFoundValue(grn_Mdl.getMainFoundValue());
        grn_Mdl.setRangeUnit(grn_Mdl.getRangeUnit());
        grn_Mdl.setHelpSource(grn_Mdl.getHelpSource());
        grn_Mdl.setDefaultRemark(grn_Mdl.getDefaultRemark());
        grn_Mdl.setImpurityManualCode(grn_Mdl.getImpurityManualCode());
        grn_Mdl.setLabQty(grn_Mdl.getLabQty());
        grn_Mdl.setLabAmt(grn_Mdl.getLabAmt());
    }
}
