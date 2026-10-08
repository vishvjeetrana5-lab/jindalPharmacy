
package aarogya.pharmacy.wholeSalePaymentRecipt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;


public class DefaultValue_Mdl extends KeyField implements Serializable, IsSerializable{
    
    private String voucherTypeCode;
    private String voucherTypeName;
    private String disallowType;
    private String currentDate;

    public DefaultValue_Mdl() {
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getDisallowType() {
        return disallowType;
    }

    public void setDisallowType(String disallowType) {
        this.disallowType = disallowType;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
    
}
