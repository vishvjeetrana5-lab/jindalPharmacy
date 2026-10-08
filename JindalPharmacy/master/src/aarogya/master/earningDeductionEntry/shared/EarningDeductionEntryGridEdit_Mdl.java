package aarogya.master.earningDeductionEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class EarningDeductionEntryGridEdit_Mdl extends KeyField implements Serializable, IsSerializable {

    private String natureCode;
    private String natureName;
    private String amount;
    private String vSNo;
    private String despName;
    private String despCode;
    private String typeCode;
    private String typeName;

    public EarningDeductionEntryGridEdit_Mdl() {
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getNatureName() {
        return natureName;
    }

    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

//    public String getDescpName() {
//        return descpName;
//    }
//
//    public void setDescpName(String descpName) {
//        this.descpName = descpName;
//    }
//
//    public String getDescpCode() {
//        return descpCode;
//    }
//
//    public void setDescpCode(String descpCode) {
//        this.descpCode = descpCode;
//    }
    public String getDespName() {
        return despName;
    }

    public void setDespName(String despName) {
        this.despName = despName;
    }

    public String getDespCode() {
        return despCode;
    }

    public void setDespCode(String despCode) {
        this.despCode = despCode;
    }

}
