package dataman.accounts.bulkChequePrinting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdBulkChequePrinting_Mdl extends KeyField implements Serializable, IsSerializable {

    //sharedKeyFieldCode=docId
    //sharedKeyFieldName=recId
    private String v_TypeShortName;
    private String date;
    private String accountName;
    private String prvChqNo;
    private String chqNo;
    private String chqDate;
    private String debit;
    private String credit;
    private String v_SNo;
    private String printName;
    private String displayAmount;
    private String isChecked;
    private String isCrossChqCode;
    private String IsCrossChqText;
    private String isView;

    public GrdBulkChequePrinting_Mdl() {
    }

    public String getIsCrossChqCode() {
        return isCrossChqCode;
    }

    public void setIsCrossChqCode(String isCrossChqCode) {
        this.isCrossChqCode = isCrossChqCode;
    }

    public String getIsCrossChqText() {
        return IsCrossChqText;
    }

    public void setIsCrossChqText(String IsCrossChqText) {
        this.IsCrossChqText = IsCrossChqText;
    }

    public String getPrvChqNo() {
        return prvChqNo;
    }

    public void setPrvChqNo(String prvChqNo) {
        this.prvChqNo = prvChqNo;
    }

    public String getIsView() {
        return isView;
    }

    public void setIsView(String isView) {
        this.isView = isView;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getDisplayAmount() {
        return displayAmount;
    }

    public void setDisplayAmount(String displayAmount) {
        this.displayAmount = displayAmount;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getV_TypeShortName() {
        return v_TypeShortName;
    }

    public void setV_TypeShortName(String v_TypeShortName) {
        this.v_TypeShortName = v_TypeShortName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getChqNo() {
        return chqNo;
    }

    public void setChqNo(String chqNo) {
        this.chqNo = chqNo;
    }

    public String getChqDate() {
        return chqDate;
    }

    public void setChqDate(String chqDate) {
        this.chqDate = chqDate;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }

    public GrdBulkChequePrinting_Mdl clone() {
        GrdBulkChequePrinting_Mdl grdBulkChequePrinting_Mdl = new GrdBulkChequePrinting_Mdl();

        copy(grdBulkChequePrinting_Mdl);

        return grdBulkChequePrinting_Mdl;
    }

    public void copy(GrdBulkChequePrinting_Mdl grdBulkChequePrinting_Mdl) {
        grdBulkChequePrinting_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        grdBulkChequePrinting_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());
        grdBulkChequePrinting_Mdl.setAccountName(this.getAccountName());
        grdBulkChequePrinting_Mdl.setChqDate(this.getChqDate());
        grdBulkChequePrinting_Mdl.setChqNo(this.getChqNo());
        grdBulkChequePrinting_Mdl.setPrvChqNo(this.getPrvChqNo());
        grdBulkChequePrinting_Mdl.setCredit(this.getCredit());
        grdBulkChequePrinting_Mdl.setDate(this.getDate());
        grdBulkChequePrinting_Mdl.setDebit(this.getDebit());
        grdBulkChequePrinting_Mdl.setPrintName(this.getPrintName());
        grdBulkChequePrinting_Mdl.setV_SNo(this.getV_SNo());
        grdBulkChequePrinting_Mdl.setV_TypeShortName(this.getV_TypeShortName());
        grdBulkChequePrinting_Mdl.setDisplayAmount(this.getDisplayAmount());
        grdBulkChequePrinting_Mdl.setIsChecked(this.getIsChecked());
        grdBulkChequePrinting_Mdl.setIsCrossChqCode(this.getIsCrossChqCode());
        grdBulkChequePrinting_Mdl.setIsCrossChqText(this.getIsCrossChqText());
    }

}
