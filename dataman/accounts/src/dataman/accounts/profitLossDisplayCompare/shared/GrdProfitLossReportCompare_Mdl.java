package dataman.accounts.profitLossDisplayCompare.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class GrdProfitLossReportCompare_Mdl implements Serializable, IsSerializable {

    private String mainGroupNature;
    private String particularName;
    private String isParticularChild;
    private String isparticularBold;
    private String particularChildBalance;
    private String particularAmount;
    private String lstParticularChildBalance;
    private String lstParticularAmount;

    public GrdProfitLossReportCompare_Mdl() {
    }

    public String getMainGroupNature() {
        return mainGroupNature;
    }

    public void setMainGroupNature(String mainGroupNature) {
        this.mainGroupNature = mainGroupNature;
    }

    public String getParticularName() {
        return particularName;
    }

    public void setParticularName(String particularName) {
        this.particularName = particularName;
    }

    public String getIsParticularChild() {
        return isParticularChild;
    }

    public void setIsParticularChild(String isParticularChild) {
        this.isParticularChild = isParticularChild;
    }

    public String getIsparticularBold() {
        return isparticularBold;
    }

    public void setIsparticularBold(String isparticularBold) {
        this.isparticularBold = isparticularBold;
    }

    public String getParticularChildBalance() {
        return particularChildBalance;
    }

    public void setParticularChildBalance(String particularChildBalance) {
        this.particularChildBalance = particularChildBalance;
    }

    public String getParticularAmount() {
        return particularAmount;
    }

    public void setParticularAmount(String particularAmount) {
        this.particularAmount = particularAmount;
    }

    public String getLstParticularChildBalance() {
        return lstParticularChildBalance;
    }

    public void setLstParticularChildBalance(String lstParticularChildBalance) {
        this.lstParticularChildBalance = lstParticularChildBalance;
    }

    public String getLstParticularAmount() {
        return lstParticularAmount;
    }

    public void setLstParticularAmount(String lstParticularAmount) {
        this.lstParticularAmount = lstParticularAmount;
    }

}
