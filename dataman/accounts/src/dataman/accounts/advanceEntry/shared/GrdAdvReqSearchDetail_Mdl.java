package dataman.accounts.advanceEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdAdvReqSearchDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String advReqNo;
    private String advReqDate;
    private String poNo;
    private String poDocId;
    private String poDate;
    private String moduleTag;
    private String moduleText;
    private String supplier;
    private String advReqAmt;
    private String dueDate;
    private String preparedBy;
    private String poAmt;
    private String finalamt;

    public GrdAdvReqSearchDetail_Mdl() {

    }

    public String getAdvReqNo() {
        return advReqNo;
    }

    public void setAdvReqNo(String advReqNo) {
        this.advReqNo = advReqNo;
    }

    public String getAdvReqDate() {
        return advReqDate;
    }

    public void setAdvReqDate(String advReqDate) {
        this.advReqDate = advReqDate;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getPoDate() {
        return poDate;
    }

    public void setPoDate(String poDate) {
        this.poDate = poDate;
    }

    public String getModuleTag() {
        return moduleTag;
    }

    public void setModuleTag(String moduleTag) {
        this.moduleTag = moduleTag;
    }

    public String getModuleText() {
        return moduleText;
    }

    public void setModuleText(String moduleText) {
        this.moduleText = moduleText;
    }

    public String getAdvReqAmt() {
        return advReqAmt;
    }

    public void setAdvReqAmt(String advReqAmt) {
        this.advReqAmt = advReqAmt;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getPoAmt() {
        return poAmt;
    }

    public void setPoAmt(String poAmt) {
        this.poAmt = poAmt;
    }

    public String getFinalamt() {
        return finalamt;
    }

    public void setFinalamt(String finalamt) {
        this.finalamt = finalamt;
    }

    public String getPoDocId() {
        return poDocId;
    }

    public void setPoDocId(String poDocId) {
        this.poDocId = poDocId;
    }
    
}
