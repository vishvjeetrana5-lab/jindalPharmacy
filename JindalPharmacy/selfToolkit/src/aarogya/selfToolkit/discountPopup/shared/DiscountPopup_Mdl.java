package aarogya.selfToolkit.discountPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DiscountPopup_Mdl implements Serializable, IsSerializable {

    private String groupCode;
    private String groupName;
    private String discountPer;
    private String discount;
    private String givenByCode;
    private String givenByName;
    private String discountReasonCode;
    private String discountReasonName;

    public DiscountPopup_Mdl() {
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getGivenByCode() {
        return givenByCode;
    }

    public void setGivenByCode(String givenByCode) {
        this.givenByCode = givenByCode;
    }

    public String getGivenByName() {
        return givenByName;
    }

    public void setGivenByName(String givenByName) {
        this.givenByName = givenByName;
    }

    public String getDiscountReasonCode() {
        return discountReasonCode;
    }

    public void setDiscountReasonCode(String discountReasonCode) {
        this.discountReasonCode = discountReasonCode;
    }

    public String getDiscountReasonName() {
        return discountReasonName;
    }

    public void setDiscountReasonName(String discountReasonName) {
        this.discountReasonName = discountReasonName;
    }

    
}
