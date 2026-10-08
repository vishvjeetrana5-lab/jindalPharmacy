package aarogya.inventory.purchaseOrder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class TxtHAmendmentOrder_Mdl extends KeyField implements Serializable, IsSerializable {

    private String partyCode;
    private String partyOrdNo;
    private String partyOrdDt;
    private String itemNatureCode;
    private String itemNatureName;

    public TxtHAmendmentOrder_Mdl() {
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getPartyOrdNo() {
        return partyOrdNo;
    }

    public void setPartyOrdNo(String partyOrdNo) {
        this.partyOrdNo = partyOrdNo;
    }

    public String getPartyOrdDt() {
        return partyOrdDt;
    }

    public void setPartyOrdDt(String partyOrdDt) {
        this.partyOrdDt = partyOrdDt;
    }

    public String getItemNatureCode() {
        return itemNatureCode;
    }

    public void setItemNatureCode(String itemNatureCode) {
        this.itemNatureCode = itemNatureCode;
    }

    public String getItemNatureName() {
        return itemNatureName;
    }

    public void setItemNatureName(String itemNatureName) {
        this.itemNatureName = itemNatureName;
    }

}
