package dataman.accounts.detailTrialBalance.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String dtFrom;
    private String dtTo;
    private String isDifference;
    private String amtDr;
    private String isDrCr;
    private String amtCr;
    private String amtOp;
    private String amtCl;
    private String opDrCr;
    private String clDrCr;
    private String amtDifference;
    private String sessionTable;

    public DefaultValue_Mdl() {
    }

    public String getDtFrom() {
        return dtFrom;
    }

    public void setDtFrom(String dtFrom) {
        this.dtFrom = dtFrom;
    }

    public String getIsDifference() {
        return isDifference;
    }

    public void setIsDifference(String isDifference) {
        this.isDifference = isDifference;
    }

    public String getAmtDr() {
        return amtDr;
    }

    public void setAmtDr(String amtDr) {
        this.amtDr = amtDr;
    }

    public String getAmtCr() {
        return amtCr;
    }

    public void setAmtCr(String amtCr) {
        this.amtCr = amtCr;
    }

    public String getAmtDifference() {
        return amtDifference;
    }

    public void setAmtDifference(String amtDifference) {
        this.amtDifference = amtDifference;
    }

    public String getIsDrCr() {
        return isDrCr;
    }

    public void setIsDrCr(String isDrCr) {
        this.isDrCr = isDrCr;
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

    public String getAmtOp() {
        return amtOp;
    }

    public void setAmtOp(String amtOp) {
        this.amtOp = amtOp;
    }

    public String getAmtCl() {
        return amtCl;
    }

    public void setAmtCl(String amtCl) {
        this.amtCl = amtCl;
    }

    public String getOpDrCr() {
        return opDrCr;
    }

    public void setOpDrCr(String opDrCr) {
        this.opDrCr = opDrCr;
    }

    public String getClDrCr() {
        return clDrCr;
    }

    public void setClDrCr(String clDrCr) {
        this.clDrCr = clDrCr;
    }
}
