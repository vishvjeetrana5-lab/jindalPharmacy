package dataman.dmToolkit.overHead.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class OverHeadPost_Mdl extends KeyField implements Serializable, IsSerializable {

    String contraCode;
    String contraText;
    String amount;
    private String isPartialPosting;

    public OverHeadPost_Mdl() {
    }

    public String getContraCode() {
        return contraCode;
    }

    public void setContraCode(String contraCode) {
        this.contraCode = contraCode;
    }

    public String getContraText() {
        return contraText;
    }

    public void setContraText(String contraText) {
        this.contraText = contraText;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIsPartialPosting() {
        return isPartialPosting;
    }

    public void setIsPartialPosting(String isPartialPosting) {
        this.isPartialPosting = isPartialPosting;
    }
}
