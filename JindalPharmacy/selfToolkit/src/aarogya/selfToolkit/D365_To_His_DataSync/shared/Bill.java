package aarogya.selfToolkit.D365_To_His_DataSync.shared;

import java.math.BigDecimal;

public class Bill {

    private String regDocid;
    private BigDecimal billAmount;
    private String siteCode;

    public Bill() {
    }

    public String getRegDocid() {
        return regDocid;
    }

    public void setRegDocid(String regDocid) {
        this.regDocid = regDocid;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

}
