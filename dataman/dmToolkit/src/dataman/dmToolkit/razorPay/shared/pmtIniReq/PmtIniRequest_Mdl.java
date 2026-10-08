package dataman.dmToolkit.razorPay.shared.pmtIniReq;

import com.google.gson.annotations.SerializedName;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PmtIniRequest_Mdl implements Serializable, IsSerializable {

    @SerializedName(value = "username")
    private String username;

    @SerializedName(value = "appKey")
    private String appKey;

    @SerializedName(value = "amount")
    private String amount;

    @SerializedName(value = "accountLabel")
    private String accountLabel;

    @SerializedName(value = "customerName")
    private String customerName;

    @SerializedName(value = "customerEmail")
    private String customerEmail;

    @SerializedName(value = "customerMobileNumber")
    private String customerMobileNumber;

    @SerializedName(value = "externalRefNumber")
    private String externalRefNumber;

    @SerializedName(value = "externalRefNumber2")
    private String externalRefNumber2;

    @SerializedName(value = "externalRefNumber4")
    private String externalRefNumber4;

    @SerializedName(value = "externalRefNumbers")
    private List<String> externalRefNumbers;

    @SerializedName(value = "mode")
    private String mode;

    @SerializedName(value = "pushTo")
    private PushTo_Mdl pushTo;

    public PmtIniRequest_Mdl() {
        externalRefNumbers = new ArrayList<>();
        pushTo = new PushTo_Mdl();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccountLabel() {
        return accountLabel;
    }

    public void setAccountLabel(String accountLabel) {
        this.accountLabel = accountLabel;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerMobileNumber() {
        return customerMobileNumber;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        this.customerMobileNumber = customerMobileNumber;
    }

    public String getExternalRefNumber() {
        return externalRefNumber;
    }

    public void setExternalRefNumber(String externalRefNumber) {
        this.externalRefNumber = externalRefNumber;
    }

    public String getExternalRefNumber2() {
        return externalRefNumber2;
    }

    public void setExternalRefNumber2(String externalRefNumber2) {
        this.externalRefNumber2 = externalRefNumber2;
    }

    public String getExternalRefNumber4() {
        return externalRefNumber4;
    }

    public void setExternalRefNumber4(String externalRefNumber4) {
        this.externalRefNumber4 = externalRefNumber4;
    }

    public List<String> getExternalRefNumbers() {
        return externalRefNumbers;
    }

    public void setExternalRefNumbers(List<String> externalRefNumbers) {
        this.externalRefNumbers = externalRefNumbers;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public PushTo_Mdl getPushTo() {
        return pushTo;
    }

    public void setPushTo(PushTo_Mdl pushTo) {
        this.pushTo = pushTo;
    }
}
