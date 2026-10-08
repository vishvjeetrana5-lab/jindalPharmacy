package dataman.accounts.advanceRequestEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdBillDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String billNo;
    private String partyNo;
    private String partyDate;
    private String value;
    private String tds;
    private String netPay;
    private String status;
    private String lastLocation;
    private String paymDueDate;
    private String enterdBy;
    private String itemName;

    public GrdBillDetail_Mdl() {

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(String partyNo) {
        this.partyNo = partyNo;
    }

    public String getPartyDate() {
        return partyDate;
    }

    public void setPartyDate(String partyDate) {
        this.partyDate = partyDate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }

    public String getNetPay() {
        return netPay;
    }

    public void setNetPay(String netPay) {
        this.netPay = netPay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getPaymDueDate() {
        return paymDueDate;
    }

    public void setPaymDueDate(String paymDueDate) {
        this.paymDueDate = paymDueDate;
    }

    public String getEnterdBy() {
        return enterdBy;
    }

    public void setEnterdBy(String enterdBy) {
        this.enterdBy = enterdBy;
    }

}
