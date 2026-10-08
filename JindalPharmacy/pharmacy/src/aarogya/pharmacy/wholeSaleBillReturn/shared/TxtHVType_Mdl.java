package aarogya.pharmacy.wholeSaleBillReturn.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class TxtHVType_Mdl extends KeyField implements Serializable, IsSerializable {

    private String documentNatureCode;
    private String documentTypeCode;
    private String shortName;
    private String userPermissionShortName;
    private String voucherCategory;
    private String paymentSource;

    public TxtHVType_Mdl() {

    }

    public String getDocumentNatureCode() {
        return documentNatureCode;
    }

    public void setDocumentNatureCode(String documentNatureCode) {
        this.documentNatureCode = documentNatureCode;
    }

    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUserPermissionShortName() {
        return userPermissionShortName;
    }

    public void setUserPermissionShortName(String userPermissionShortName) {
        this.userPermissionShortName = userPermissionShortName;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getPaymentSource() {
        return paymentSource;
    }

    public void setPaymentSource(String paymentSource) {
        this.paymentSource = paymentSource;
    }

}
