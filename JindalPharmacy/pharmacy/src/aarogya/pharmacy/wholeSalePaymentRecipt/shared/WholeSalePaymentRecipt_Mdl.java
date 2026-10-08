package aarogya.pharmacy.wholeSalePaymentRecipt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;

public class WholeSalePaymentRecipt_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId objRecId = new RecId();
    private String voucherTypeCode;
    private String voucherTypeName;
    private String voucherDate;
    private String partyCode;
    private String partyName;
    private String tdsAcCode;
    private String tdsAcName;
    private String bankCashType;
    private String depositInCode;
    private String depositInName;
    private String instrumentCode;
    private String instrumentName;
    private String instrumentNo;
    private String instrumentDate;
    private String remark;
    private String receivedAmt;
    private String tdsAmt;
    private String unsettledAmt;
    private String disallowTypeCode;
    private String disallowTypeName;
    private String settledAmt;

    public WholeSalePaymentRecipt_Mdl() {
    }

    public RecId getObjRecId() {
        return objRecId;
    }

    public void setObjRecId(RecId objRecId) {
        this.objRecId = objRecId;
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

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getTdsAcCode() {
        return tdsAcCode;
    }

    public void setTdsAcCode(String tdsAcCode) {
        this.tdsAcCode = tdsAcCode;
    }

    public String getTdsAcName() {
        return tdsAcName;
    }

    public void setTdsAcName(String tdsAcName) {
        this.tdsAcName = tdsAcName;
    }

    public String getBankCashType() {
        return bankCashType;
    }

    public void setBankCashType(String bankCashType) {
        this.bankCashType = bankCashType;
    }

    public String getDepositInCode() {
        return depositInCode;
    }

    public void setDepositInCode(String depositInCode) {
        this.depositInCode = depositInCode;
    }

    public String getDepositInName() {
        return depositInName;
    }

    public void setDepositInName(String depositInName) {
        this.depositInName = depositInName;
    }

    public String getInstrumentCode() {
        return instrumentCode;
    }

    public void setInstrumentCode(String instrumentCode) {
        this.instrumentCode = instrumentCode;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public String getInstrumentNo() {
        return instrumentNo;
    }

    public void setInstrumentNo(String instrumentNo) {
        this.instrumentNo = instrumentNo;
    }

    public String getInstrumentDate() {
        return instrumentDate;
    }

    public void setInstrumentDate(String instrumentDate) {
        this.instrumentDate = instrumentDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReceivedAmt() {
        return receivedAmt;
    }

    public void setReceivedAmt(String receivedAmt) {
        this.receivedAmt = receivedAmt;
    }

    public String getTdsAmt() {
        return tdsAmt;
    }

    public void setTdsAmt(String tdsAmt) {
        this.tdsAmt = tdsAmt;
    }

    public String getUnsettledAmt() {
        return unsettledAmt;
    }

    public void setUnsettledAmt(String unsettledAmt) {
        this.unsettledAmt = unsettledAmt;
    }

    public String getDisallowTypeCode() {
        return disallowTypeCode;
    }

    public void setDisallowTypeCode(String disallowTypeCode) {
        this.disallowTypeCode = disallowTypeCode;
    }

    public String getDisallowTypeName() {
        return disallowTypeName;
    }

    public void setDisallowTypeName(String disallowTypeName) {
        this.disallowTypeName = disallowTypeName;
    }

    public String getSettledAmt() {
        return settledAmt;
    }

    public void setSettledAmt(String settledAmt) {
        this.settledAmt = settledAmt;
    }

}
