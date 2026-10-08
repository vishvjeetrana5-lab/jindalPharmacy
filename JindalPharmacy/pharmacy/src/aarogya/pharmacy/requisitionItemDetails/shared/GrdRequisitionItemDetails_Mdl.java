package aarogya.pharmacy.requisitionItemDetails.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdRequisitionItemDetails_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vSno;
    private String requisitionRecid, requisitionDocid;
    private String medicineName, medicineCode;
    private String unit1Code, unit1Name;
    private String reqQty;
    private String isBestBefore;

    private String saleQty;
    private String saleRate;
    private String batchNo;
//    private String addRow;
    private String expDate, manufacturingDate, mrp;
    private String medicineNature;

    public GrdRequisitionItemDetails_Mdl() {
    }

    public String getIsBestBefore() {
        return isBestBefore;
    }

    public void setIsBestBefore(String isBestBefore) {
        this.isBestBefore = isBestBefore;
    }

    public String getMedicineNature() {
        return medicineNature;
    }

    public void setMedicineNature(String medicineNature) {
        this.medicineNature = medicineNature;
    }

    public String getUnit1Code() {
        return unit1Code;
    }

    public void setUnit1Code(String unit1Code) {
        this.unit1Code = unit1Code;
    }

    public String getUnit1Name() {
        return unit1Name;
    }

    public void setUnit1Name(String unit1Name) {
        this.unit1Name = unit1Name;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getVSno() {
        return vSno;
    }

    public void setVSno(String vSno) {
        this.vSno = vSno;
    }

    public String getRequisitionRecid() {
        return requisitionRecid;
    }

    public void setRequisitionRecid(String requisitionRecid) {
        this.requisitionRecid = requisitionRecid;
    }

    public String getRequisitionDocid() {
        return requisitionDocid;
    }

    public void setRequisitionDocid(String requisitionDocid) {
        this.requisitionDocid = requisitionDocid;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(String medicineCode) {
        this.medicineCode = medicineCode;
    }

    public String getReqQty() {
        return reqQty;
    }

    public void setReqQty(String ReqQty) {
        this.reqQty = ReqQty;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getvSno() {
        return vSno;
    }

    public void setvSno(String vSno) {
        this.vSno = vSno;
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

    public String getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(String saleQty) {
        this.saleQty = saleQty;
    }

    private GrdRequisitionItemDetails_Mdl(String vSno, String requisitionRecid, String requisitionDocid, String medicineName, String medicineCode, String unit1Code, String unit1Name, String reqQty, String isBestBefore, String saleQty, String saleRate, String batchNo, String expDate, String manufacturingDate, String mrp, String medicineNature, String KeyFieldCode, String KeyFieldName) {
        super(KeyFieldCode, KeyFieldName);
        this.vSno = vSno;
        this.requisitionRecid = requisitionRecid;
        this.requisitionDocid = requisitionDocid;
        this.medicineName = medicineName;
        this.medicineCode = medicineCode;
        this.unit1Code = unit1Code;
        this.unit1Name = unit1Name;
        this.reqQty = reqQty;
        this.isBestBefore = isBestBefore;
        this.saleQty = saleQty;
        this.saleRate = saleRate;
        this.batchNo = batchNo;
        this.expDate = expDate;
        this.manufacturingDate = manufacturingDate;
        this.mrp = mrp;
        this.medicineNature = medicineNature;
    }

    public GrdRequisitionItemDetails_Mdl(GrdRequisitionItemDetails_Mdl mdl) {
        this(mdl.getVSno(), mdl.getRequisitionRecid(), mdl.getRequisitionDocid(), mdl.getMedicineName(), mdl.getMedicineCode(), mdl.getUnit1Code(), mdl.getUnit1Name(), mdl.getReqQty(), mdl.getIsBestBefore(), mdl.getSaleQty(), mdl.getSaleRate(), mdl.getBatchNo(), mdl.getExpDate(), mdl.getManufacturingDate(), mdl.getMrp(), mdl.getMedicineNature(), mdl.getSharedKeyFieldCode(), mdl.getSharedKeyFieldName());
    }

}
