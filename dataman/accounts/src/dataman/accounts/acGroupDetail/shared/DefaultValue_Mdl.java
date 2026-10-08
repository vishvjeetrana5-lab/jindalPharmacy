package dataman.accounts.acGroupDetail.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String sessionTable;
    private String dtTo;
    private String dtFrom;
    private String acForCode;
    private String acName;
    private String totalDr;
    private String totalCr;
    private String balance;
    private String isDrCr;
    private String clDrCr;
    private String amtCl;

    public DefaultValue_Mdl() {

    }

    public String getClDrCr() {
        return clDrCr;
    }

    public void setClDrCr(String clDrCr) {
        this.clDrCr = clDrCr;
    }

    public String getAmtCl() {
        return amtCl;
    }

    public void setAmtCl(String amtCl) {
        this.amtCl = amtCl;
    }

    public String getDtTo() {
        return dtTo;
    }

    public void setDtTo(String dtTo) {
        this.dtTo = dtTo;
    }

    public String getSessionTable() {
        return sessionTable;
    }

    public void setSessionTable(String sessionTable) {
        this.sessionTable = sessionTable;
    }

    public String getAcForCode() {
        return acForCode;
    }

    public void setAcForCode(String acForCode) {
        this.acForCode = acForCode;
    }

    public String getTotalDr() {
        return totalDr;
    }

    public void setTotalDr(String totalDr) {
        this.totalDr = totalDr;
    }

    public String getTotalCr() {
        return totalCr;
    }

    public void setTotalCr(String totalCr) {
        this.totalCr = totalCr;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getIsDrCr() {
        return isDrCr;
    }

    public void setIsDrCr(String isDrCr) {
        this.isDrCr = isDrCr;
    }

    public String getDtFrom() {
        return dtFrom;
    }

    public void setDtFrom(String dtFrom) {
        this.dtFrom = dtFrom;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }
}
