package aarogya.pharmacy.purchaseOrder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class CDdetail_Mdl implements Serializable, IsSerializable {

    private String supplierCode;
    private String medicineCode;
    private String discountPercent;

    public CDdetail_Mdl() {
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(String medicineCode) {
        this.medicineCode = medicineCode;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

}
