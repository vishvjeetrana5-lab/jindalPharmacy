package aarogya.master.earningDeductionMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class EarningDeductionMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String serialNo;
    private String natureName;
    private String natureCode;
    private String calculateOnName;
    private String calculateOnCode;
    private String roundName;
    private String roundCode;

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
    

}
