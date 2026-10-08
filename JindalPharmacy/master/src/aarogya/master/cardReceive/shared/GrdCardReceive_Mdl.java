package aarogya.master.cardReceive.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdCardReceive_Mdl extends KeyField implements Serializable, IsSerializable {

    private String cardNo;
    private String cardName;
    private String allotmentDate;
    private String allotedPerson;

    public GrdCardReceive_Mdl() {
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getAllotmentDate() {
        return allotmentDate;
    }

    public void setAllotmentDate(String allotmentDate) {
        this.allotmentDate = allotmentDate;
    }

    public String getAllotedPerson() {
        return allotedPerson;
    }

    public void setAllotedPerson(String allotedPerson) {
        this.allotedPerson = allotedPerson;
    }

}
