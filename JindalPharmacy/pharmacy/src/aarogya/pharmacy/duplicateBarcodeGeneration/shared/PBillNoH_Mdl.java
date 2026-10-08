package aarogya.pharmacy.duplicateBarcodeGeneration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class PBillNoH_Mdl extends KeyField implements Serializable, IsSerializable {

    private String recid;
    private String pdate;
    private String description;
    private String supplierName;
    private String partyBillNO;
    private String partyBillDate;

    public PBillNoH_Mdl() {
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public String getRecid() {
        return recid;
    }

    public void setRecid(String recid) {
        this.recid = recid;
    }

   

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPartyBillNO() {
        return partyBillNO;
    }

    public void setPartyBillNO(String partyBillNO) {
        this.partyBillNO = partyBillNO;
    }

    public String getPartyBillDate() {
        return partyBillDate;
    }

    public void setPartyBillDate(String partyBillDate) {
        this.partyBillDate = partyBillDate;
    }

}
