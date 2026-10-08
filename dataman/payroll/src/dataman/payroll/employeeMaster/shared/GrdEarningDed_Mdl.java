
package dataman.payroll.employeeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;


public class GrdEarningDed_Mdl extends KeyField implements Serializable, IsSerializable{
    private String sNo;
    private String typeCode;
    private String type;
    private String earningDeduction;
    private String amtPerName;
    private String amtPer;
    private String amt;
    private String code;
    private String docIdSNo;
    private String epTypeCode;
    private String epTypeName;
    private String earningAmt;
    private String deductionAmt;
    private String netAmt;
    private String basicPay;

    public GrdEarningDed_Mdl() {
    }

    public String getSNo() {
        return sNo;
    }

    public void setSNo(String sNo) {
        this.sNo = sNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEarningDeduction() {
        return earningDeduction;
    }

    public void setEarningDeduction(String earningDeduction) {
        this.earningDeduction = earningDeduction;
    }

    public String getAmtPerName() {
        return amtPerName;
    }

    public void setAmtPerName(String amtPerName) {
        this.amtPerName = amtPerName;
    }

    public String getAmtPer() {
        return amtPer;
    }

    public void setAmtPer(String amtPer) {
        this.amtPer = amtPer;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public String getDocIdSNo() {
        return docIdSNo;
    }

    public void setDocIdSNo(String docIdSNo) {
        this.docIdSNo = docIdSNo;
    }

    public String getEpTypeCode() {
        return epTypeCode;
    }

    public void setEpTypeCode(String epTypeCode) {
        this.epTypeCode = epTypeCode;
    }

    public String getEpTypeName() {
        return epTypeName;
    }

    public void setEpTypeName(String epTypeName) {
        this.epTypeName = epTypeName;
    }

    public String getEarningAmt() {
        return earningAmt;
    }

    public void setEarningAmt(String earningAmt) {
        this.earningAmt = earningAmt;
    }

    public String getDeductionAmt() {
        return deductionAmt;
    }

    public void setDeductionAmt(String deductionAmt) {
        this.deductionAmt = deductionAmt;
    }

    public String getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(String netAmt) {
        this.netAmt = netAmt;
    }

    public String getBasicPay() {
        return basicPay;
    }

    public void setBasicPay(String basicPay) {
        this.basicPay = basicPay;
    }
    
    
}
