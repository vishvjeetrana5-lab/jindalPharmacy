package aarogya.master.D365ImportExport.shared;

import dataman.dmBase.self.shared.KeyField;

public class OpdAccountPosting_Mdl extends KeyField {

    private String vDate;
    private String refNo;
    private String acName;
    private String acCode;
    private String sourceCode;
    private String sourceType;
    private String sourceNo;
    private String amount;
    private String entryDateTime;
    private String action;
    private String actionDateTime;

    public OpdAccountPosting_Mdl() {
    }

    public String getvDate() {
        return vDate;
    }

    public void setvDate(String vDate) {
        this.vDate = vDate;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getAcCode() {
        return acCode;
    }

    public void setAcCode(String acCode) {
        this.acCode = acCode;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceNo() {
        return sourceNo;
    }

    public void setSourceNo(String sourceNo) {
        this.sourceNo = sourceNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEntryDateTime() {
        return entryDateTime;
    }

    public void setEntryDateTime(String entryDateTime) {
        this.entryDateTime = entryDateTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionDateTime() {
        return actionDateTime;
    }

    public void setActionDateTime(String actionDateTime) {
        this.actionDateTime = actionDateTime;
    }

}
