package aarogya.pharmacy.purchaseReturn.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHGrdBillDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    /*
     * SharedKeyfieldCode = pbDocId
     * SharedKeyfieldName = pbRecId
     */
    private String billV_SNo;
    private String pbV_Date;
    private String pbRefNo;
    private String pbRefDate;
    private String medicineName;
    private String rate;
    private String batchNo;
    private String mrp;
    private String saleRate;
    private String manufacturingDate;
    private String expiryDate;

    public TxtHGrdBillDetail_Mdl() {
    }

    public String getBillV_SNo() {
        return billV_SNo;
    }

    public void setBillV_SNo(String billV_SNo) {
        this.billV_SNo = billV_SNo;
    }

    public String getPbV_Date() {
        return pbV_Date;
    }

    public void setPbV_Date(String pbV_Date) {
        this.pbV_Date = pbV_Date;
    }

    public String getPbRefNo() {
        return pbRefNo;
    }

    public void setPbRefNo(String pbRefNo) {
        this.pbRefNo = pbRefNo;
    }

    public String getPbRefDate() {
        return pbRefDate;
    }

    public void setPbRefDate(String pbRefDate) {
        this.pbRefDate = pbRefDate;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

}
