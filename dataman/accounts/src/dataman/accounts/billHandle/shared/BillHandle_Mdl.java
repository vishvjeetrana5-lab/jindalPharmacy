package dataman.accounts.billHandle.shared;


import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BillHandle_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recid = new RecId();
    private String date;
    private String fromLocationCode;
    private String fromLocationName;
    private String toLocationCode;
    private String toLocationName;
    private String module;
    private String fromBilldate;
    private String toBilldate;
    private String billTypeCode;
    private String billTypeName;
    private String approvedBy;
    private String modifiedBy;
    private String preparedBy;

    private String wtaCode;

    private List<GrdBillDetail_Mdl> grdData = new ArrayList<GrdBillDetail_Mdl>();

    public BillHandle_Mdl() {
    }

    public RecId getRecid() {
        return recid;
    }

    public void setRecid(RecId recid) {
        this.recid = recid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFromLocationCode() {
        return fromLocationCode;
    }

    public void setFromLocationCode(String fromLocationCode) {
        this.fromLocationCode = fromLocationCode;
    }

    public String getFromLocationName() {
        return fromLocationName;
    }

    public void setFromLocationName(String fromLocationName) {
        this.fromLocationName = fromLocationName;
    }

    public String getToLocationCode() {
        return toLocationCode;
    }

    public void setToLocationCode(String toLocationCode) {
        this.toLocationCode = toLocationCode;
    }

    public String getToLocationName() {
        return toLocationName;
    }

    public void setToLocationName(String toLocationName) {
        this.toLocationName = toLocationName;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFromBilldate() {
        return fromBilldate;
    }

    public void setFromBilldate(String fromBilldate) {
        this.fromBilldate = fromBilldate;
    }

    public String getToBilldate() {
        return toBilldate;
    }

    public void setToBilldate(String toBilldate) {
        this.toBilldate = toBilldate;
    }

    public List<GrdBillDetail_Mdl> getGrdData() {
        return grdData;
    }

    public void setGrdData(List<GrdBillDetail_Mdl> grdData) {
        this.grdData = grdData;
    }

//    public String getBillTypeCode() {
//        return billTypeCode;
//    }
//
//    public void setBillTypeCode(String billTypeCode) {
//        this.billTypeCode = billTypeCode;
//    }
//
//    public String getBillTypeName() {
//        return billTypeName;
//    }
//
//    public void setBillTypeName(String billTypeName) {
//        this.billTypeName = billTypeName;
//    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getBillTypeCode() {
        return billTypeCode;
    }

    public void setBillTypeCode(String billTypeCode) {
        this.billTypeCode = billTypeCode;
    }

    public String getBillTypeName() {
        return billTypeName;
    }

    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
    }

}
