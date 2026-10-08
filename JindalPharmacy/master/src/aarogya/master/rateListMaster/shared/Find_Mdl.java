package aarogya.master.rateListMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String postInAccount;
    private String applicableFrom;
    private String insurance;
    private String serviceDiscountPer;
    private String testDiscountPer;
    private String feesDiscountPer;
    private String isActive;

    public Find_Mdl() {
    }

    public String getPostInAccount() {
        return postInAccount;
    }

    public void setPostInAccount(String postInAccount) {
        this.postInAccount = postInAccount;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getApplicableFrom() {
        return applicableFrom;
    }

    public void setApplicableFrom(String applicableFrom) {
        this.applicableFrom = applicableFrom;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getServiceDiscountPer() {
        return serviceDiscountPer;
    }

    public void setServiceDiscountPer(String serviceDiscountPer) {
        this.serviceDiscountPer = serviceDiscountPer;
    }

    public String getTestDiscountPer() {
        return testDiscountPer;
    }

    public void setTestDiscountPer(String testDiscountPer) {
        this.testDiscountPer = testDiscountPer;
    }

    public String getFeesDiscountPer() {
        return feesDiscountPer;
    }

    public void setFeesDiscountPer(String feesDiscountPer) {
        this.feesDiscountPer = feesDiscountPer;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}
