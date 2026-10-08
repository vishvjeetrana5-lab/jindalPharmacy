package dataman.utility.voucherType.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdReceiptType_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shortName;
    private String cardType;
    private String isDefaultCode;
    private String isDefaultName;

    public GrdReceiptType_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getIsDefaultCode() {
        return isDefaultCode;
    }

    public void setIsDefaultCode(String isDefaultCode) {
        this.isDefaultCode = isDefaultCode;
    }

    public String getIsDefaultName() {
        return isDefaultName;
    }

    public void setIsDefaultName(String isDefaultName) {
        this.isDefaultName = isDefaultName;
    }
}
