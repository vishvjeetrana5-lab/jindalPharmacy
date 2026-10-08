package dataman.dmToolkit.overHeadLine.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OverHeadITS_Mdl implements Serializable, IsSerializable {

    private String overHeadManualCode;
    private String overHeadCode;
    private String atRate;
    private String amount;
    private String accountName;
    private String accountCode;
    private String accountManualCode;

    public OverHeadITS_Mdl() {
    }

    public String getOverHeadManualCode() {
        return overHeadManualCode;
    }

    public void setOverHeadManualCode(String overHeadManualCode) {
        this.overHeadManualCode = overHeadManualCode;
    }

    public String getOverHeadCode() {
        return overHeadCode;
    }

    public void setOverHeadCode(String overHeadCode) {
        this.overHeadCode = overHeadCode;
    }

    public String getAtRate() {
        return atRate;
    }

    public void setAtRate(String atRate) {
        this.atRate = atRate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountManualCode() {
        return accountManualCode;
    }

    public void setAccountManualCode(String accountManualCode) {
        this.accountManualCode = accountManualCode;
    }

    public OverHeadITS_Mdl clone() {
        OverHeadITS_Mdl overHead_Mdl = new OverHeadITS_Mdl();

        overHead_Mdl.setOverHeadCode(this.getOverHeadCode());
        overHead_Mdl.setAtRate(this.getAtRate());
        overHead_Mdl.setAmount(this.getAmount());
        overHead_Mdl.setAccountCode(this.getAccountCode());
        overHead_Mdl.setAccountManualCode(this.getAccountManualCode());
        overHead_Mdl.setAccountName(this.getAccountName());

        return overHead_Mdl;
    }

    public static List<OverHeadITS_Mdl> getITSListClone(List<OverHeadITS_Mdl> list) {
        List<OverHeadITS_Mdl> overHeadITSList = null;

        if (list != null) {
            overHeadITSList = new ArrayList<>();
            for (OverHeadITS_Mdl overHeadITS : list) {
                overHeadITSList.add(overHeadITS.clone());
            }
        }

        return overHeadITSList;
    }

}
