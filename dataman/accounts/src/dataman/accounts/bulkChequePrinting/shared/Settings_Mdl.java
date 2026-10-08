/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataman.accounts.bulkChequePrinting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

/**
 *
 * @author Dataman
 */
public class Settings_Mdl extends KeyField implements Serializable, IsSerializable {

    public Settings_Mdl() {
    }

    private String fromDate;
    private String toDate;
    private String contraSub;
    private String voucherNoDocid;
    private String voucherNo;
    private String accountCode;
    private String v_type;
    private String recordTypeCode;
    private String recordTypeName;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getContraSub() {
        return contraSub;
    }

    public void setContraSub(String contraSub) {
        this.contraSub = contraSub;
    }

    public String getVoucherNoDocid() {
        return voucherNoDocid;
    }

    public void setVoucherNoDocid(String voucherNoDocid) {
        this.voucherNoDocid = voucherNoDocid;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getV_type() {
        return v_type;
    }

    public void setV_type(String v_type) {
        this.v_type = v_type;
    }

    public String getRecordTypeCode() {
        return recordTypeCode;
    }

    public void setRecordTypeCode(String recordTypeCode) {
        this.recordTypeCode = recordTypeCode;
    }

    public String getRecordTypeName() {
        return recordTypeName;
    }

    public void setRecordTypeName(String recordTypeName) {
        this.recordTypeName = recordTypeName;
    }

}
