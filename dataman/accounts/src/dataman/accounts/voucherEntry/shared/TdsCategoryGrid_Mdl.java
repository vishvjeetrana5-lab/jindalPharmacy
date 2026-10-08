package dataman.accounts.voucherEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TdsCategoryGrid_Mdl extends KeyField implements Serializable, IsSerializable {

    private String description;
    private String descriptionCode;
    private String descriptionMCode;
    private String postingAccount;
    private String postingAccountCode;
    private String postingManualCode;
    private String percent;
    private String amount;
    private String formula;
    private String roundOffTDS;

    public TdsCategoryGrid_Mdl() {
    }

    public String getDescriptionMCode() {
        return descriptionMCode;
    }

    public void setDescriptionMCode(String descriptionMCode) {
        this.descriptionMCode = descriptionMCode;
    }

    public String getDescriptionCode() {
        return descriptionCode;
    }

    public void setDescriptionCode(String descriptionCode) {
        this.descriptionCode = descriptionCode;
    }

    public String getPostingAccountCode() {
        return postingAccountCode;
    }

    public void setPostingAccountCode(String postingAccountCode) {
        this.postingAccountCode = postingAccountCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostingAccount() {
        return postingAccount;
    }

    public void setPostingAccount(String postingAccount) {
        this.postingAccount = postingAccount;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
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

    public String getRoundOffTDS() {
        return roundOffTDS;
    }

    public void setRoundOffTDS(String roundOffTDS) {
        this.roundOffTDS = roundOffTDS;
    }

    public String getPostingManualCode() {
        return postingManualCode;
    }

    public void setPostingManualCode(String postingManualCode) {
        this.postingManualCode = postingManualCode;
    }
    
    public TdsCategoryGrid_Mdl clone() {
        TdsCategoryGrid_Mdl tdsCategoryGrid = new TdsCategoryGrid_Mdl();

        tdsCategoryGrid.setDescriptionCode(this.getDescriptionCode());
        tdsCategoryGrid.setDescriptionMCode(this.getDescriptionMCode());
        tdsCategoryGrid.setDescription(this.getDescription());
        tdsCategoryGrid.setPostingAccountCode(this.getPostingAccountCode());
        tdsCategoryGrid.setPostingAccount(this.getPostingAccount());
        tdsCategoryGrid.setPostingManualCode(this.getPostingManualCode());
        tdsCategoryGrid.setFormula(this.getFormula());
        tdsCategoryGrid.setRoundOffTDS(this.getRoundOffTDS());
        tdsCategoryGrid.setPercent(this.getPercent());
        tdsCategoryGrid.setAmount(this.getAmount());

        return tdsCategoryGrid;
    }
}
