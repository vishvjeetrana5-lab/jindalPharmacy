package aarogya.pharmacy.pharmacyFinalDiscountEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String currentDate;
    private String itemCode;
    private String salesAcCode;

    private String itemName;
    private String gstUnitCode;
    private String saleUnitCode;
    private String partyType;
    private String stateCode;

    private TxtHVType_Mdl voucherTypeMdl;

    public DefaultValue_Mdl() {
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public TxtHVType_Mdl getVoucherTypeMdl() {
        return voucherTypeMdl;
    }

    public void setVoucherTypeMdl(TxtHVType_Mdl voucherTypeMdl) {
        this.voucherTypeMdl = voucherTypeMdl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getGstUnitCode() {
        return gstUnitCode;
    }

    public void setGstUnitCode(String gstUnitCode) {
        this.gstUnitCode = gstUnitCode;
    }

    public String getSaleUnitCode() {
        return saleUnitCode;
    }

    public void setSaleUnitCode(String saleUnitCode) {
        this.saleUnitCode = saleUnitCode;
    }

    public String getSalesAcCode() {
        return salesAcCode;
    }

    public void setSalesAcCode(String salesAcCode) {
        this.salesAcCode = salesAcCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

}
