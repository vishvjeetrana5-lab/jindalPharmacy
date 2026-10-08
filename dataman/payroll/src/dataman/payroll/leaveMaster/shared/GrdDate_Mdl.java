package dataman.payroll.leaveMaster.shared;

import dataman.dmBase.self.shared.KeyField;

public class GrdDate_Mdl extends KeyField {

    private String fromDate, toDate;
    private String docID;

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public GrdDate_Mdl() {
    }

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

}
