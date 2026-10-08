package aarogya.master.cardReceive.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class CardReceive_Mdl extends KeyField implements Serializable, IsSerializable {

    private int cardNoFrom, cardNoTo;
    private String counterFormat;

    public CardReceive_Mdl() {
    }

    public int getCardNoFrom() {
        return cardNoFrom;
    }

    public void setCardNoFrom(int cardNoFrom) {
        this.cardNoFrom = cardNoFrom;
    }

    public int getCardNoTo() {
        return cardNoTo;
    }

    public void setCardNoTo(int cardNoTo) {
        this.cardNoTo = cardNoTo;
    }

    public String getCounterFormat() {
        return counterFormat;
    }

    public void setCounterFormat(String counterFormat) {
        this.counterFormat = counterFormat;
    }
}
