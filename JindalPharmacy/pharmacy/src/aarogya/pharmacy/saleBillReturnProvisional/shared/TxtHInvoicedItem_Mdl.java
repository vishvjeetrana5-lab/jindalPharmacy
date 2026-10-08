package aarogya.pharmacy.saleBillReturnProvisional.shared;

import aarogya.pharmacy.medicineReceive.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHInvoicedItem_Mdl extends KeyField implements Serializable, IsSerializable {

    public TxtHInvoicedItem_Mdl() {
    }
    private String medicineCode;
    private String medicineName;
    private String medicineBatchCode;
    private String medicineBatchNo;
    private String qty;
    private String skuName;
    private String expiryDate;

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(String medicineCode) {
        this.medicineCode = medicineCode;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineBatchCode() {
        return medicineBatchCode;
    }

    public void setMedicineBatchCode(String medicineBatchCode) {
        this.medicineBatchCode = medicineBatchCode;
    }

    public String getMedicineBatchNo() {
        return medicineBatchNo;
    }

    public void setMedicineBatchNo(String medicineBatchNo) {
        this.medicineBatchNo = medicineBatchNo;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    
}
