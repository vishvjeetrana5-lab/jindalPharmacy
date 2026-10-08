package aarogya.selfToolkit.D365_To_His_DataSync.shared;

import java.math.BigDecimal;

public class Settlement {

    private String regDocid;
    private BigDecimal billAmount;
    private BigDecimal settledAmount;
    private BigDecimal balanceAmount;

    public Settlement() {
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

    public BigDecimal getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(BigDecimal settledAmount) {
        this.settledAmount = settledAmount;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

}
