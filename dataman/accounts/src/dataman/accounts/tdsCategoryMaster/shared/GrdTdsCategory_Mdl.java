package dataman.accounts.tdsCategoryMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdTdsCategory_Mdl extends KeyField implements Serializable, IsSerializable {

    private String description;
    private String descriptionCode;
    private String postingAccount;
    private String PostingAccountCode;
    private String Percent;
    private String CalculateOn;
    private String manualCode;
    private String fromAmt;
    private String toAmt;
    private String penalityPercentage;
    private String initialTurnoverAmount;

    public GrdTdsCategory_Mdl() {
    }

    public String getFromAmt() {
        return fromAmt;
    }

    public void setFromAmt(String fromAmt) {
        this.fromAmt = fromAmt;
    }

    public String getToAmt() {
        return toAmt;
    }

    public void setToAmt(String toAmt) {
        this.toAmt = toAmt;
    }

    public String getPenalityPercentage() {
        return penalityPercentage;
    }

    public void setPenalityPercentage(String penalityPercentage) {
        this.penalityPercentage = penalityPercentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String Description) {
        this.description = Description;
    }

    public String getDescriptionCode() {
        return descriptionCode;
    }

    public void setDescriptionCode(String DescriptionCode) {
        this.descriptionCode = DescriptionCode;
    }

    public String getPostingAccount() {
        return postingAccount;
    }

    public void setPostingAccount(String PostingAccount) {
        this.postingAccount = PostingAccount;
    }

    public String getPostingAccountCode() {
        return PostingAccountCode;
    }

    public void setPostingAccountCode(String PostingAccountCode) {
        this.PostingAccountCode = PostingAccountCode;
    }

    public String getPercent() {
        return Percent;
    }

    public void setPercent(String Percent) {
        this.Percent = Percent;
    }

    public String getCalculateOn() {
        return CalculateOn;
    }

    public void setCalculateOn(String CalculateOn) {
        this.CalculateOn = CalculateOn;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getInitialTurnoverAmount() {
        return initialTurnoverAmount;
    }

    public void setInitialTurnoverAmount(String initialTurnoverAmount) {
        this.initialTurnoverAmount = initialTurnoverAmount;
    }

}
