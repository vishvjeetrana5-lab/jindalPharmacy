package dataman.payroll.earningDeductionMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EarningDeductionMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String serialNo;
    private String natureName;
    private String natureCode;
    private String calculateOnName;
    private String calculateOnCode;
    private String roundName;
    private String roundCode;
    private String typeCode;
    private String typeName;
    private String limitOfAmount;
    private String limitForCalsRes;
    private String systemDefine;
    private String basedOnName;
    private String basedOnCode;
    private String useInEntryName;
    private String useInEntryCode;
    private List<GrdEarnDed_Mdl> grdEarnDed_Mdl = new ArrayList<GrdEarnDed_Mdl>();



    public EarningDeductionMaster_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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

    public String getCalculateOnName() {
        return calculateOnName;
    }

    public void setCalculateOnName(String calculateOnName) {
        this.calculateOnName = calculateOnName;
    }

    public String getCalculateOnCode() {
        return calculateOnCode;
    }

    public void setCalculateOnCode(String calculateOnCode) {
        this.calculateOnCode = calculateOnCode;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getRoundCode() {
        return roundCode;
    }

    public void setRoundCode(String roundCode) {
        this.roundCode = roundCode;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
   public List<GrdEarnDed_Mdl> getGrdEarnDed_Mdl() {
        return grdEarnDed_Mdl;
    }

    public String getLimitOfAmount() {
        return limitOfAmount;
    }

    public void setLimitOfAmount(String limitOfAmount) {
        this.limitOfAmount = limitOfAmount;
    }

    public String getLimitForCalsRes() {
        return limitForCalsRes;
    }

    public void setLimitForCalsRes(String limitForCalsRes) {
        this.limitForCalsRes = limitForCalsRes;
    }

    public String getSystemDefine() {
        return systemDefine;
    }

    public void setSystemDefine(String systemDefine) {
        this.systemDefine = systemDefine;
    }

    public String getBasedOnName() {
        return basedOnName;
    }

    public void setBasedOnName(String basedOnName) {
        this.basedOnName = basedOnName;
    }

    public String getBasedOnCode() {
        return basedOnCode;
    }

    public void setBasedOnCode(String basedOnCode) {
        this.basedOnCode = basedOnCode;
    }

    public String getUseInEntryName() {
        return useInEntryName;
    }

    public void setUseInEntryName(String useInEntryName) {
        this.useInEntryName = useInEntryName;
    }

    public String getUseInEntryCode() {
        return useInEntryCode;
    }

    public void setUseInEntryCode(String useInEntryCode) {
        this.useInEntryCode = useInEntryCode;
    }
    

}
