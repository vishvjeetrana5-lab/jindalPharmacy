package dataman.dmToolkit.overHeadLine.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.List;

public class SyncOverHead_Mdl implements Serializable, IsSerializable {

    private String ohManualCode;
    private String amount;
    private List<SyncOverHeadMA_Mdl> overHeadMAList;

    public SyncOverHead_Mdl() {

    }

    public String getOhManualCode() {
        return ohManualCode;
    }

    public void setOhManualCode(String ohManualCode) {
        this.ohManualCode = ohManualCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<SyncOverHeadMA_Mdl> getOverHeadMAList() {
        return overHeadMAList;
    }

    public void setOverHeadMAList(List<SyncOverHeadMA_Mdl> overHeadMAList) {
        this.overHeadMAList = overHeadMAList;
    }
}
