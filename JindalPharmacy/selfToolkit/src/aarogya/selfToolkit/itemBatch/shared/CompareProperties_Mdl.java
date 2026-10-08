package aarogya.selfToolkit.itemBatch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompareProperties_Mdl implements Serializable, IsSerializable {

    private String docId;
    private String v_Type;
    private String v_No;
    private String recIdPrefix;
    private String recId;
//    private String departmentCode;
    private String site_Code;
    private String currentReceiveDate;
    private String previousReceiveDate;
    private String contraSubCode;

    private List<ItemBatch_Mdl> currentIbm = new ArrayList<>();
    private List<ItemBatch_Mdl> previousIbm = new ArrayList<>();

    public CompareProperties_Mdl() {
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

//    public String getDepartmentCode() {
//        return departmentCode;
//    }
//
//    public void setDepartmentCode(String departmentCode) {
//        this.departmentCode = departmentCode;
//    }

    public String getSite_Code() {
        return site_Code;
    }

    public void setSite_Code(String site_Code) {
        this.site_Code = site_Code;
    }

    public String getCurrentReceiveDate() {
        return currentReceiveDate;
    }

    public void setCurrentReceiveDate(String currentReceiveDate) {
        this.currentReceiveDate = currentReceiveDate;
    }

    public String getPreviousReceiveDate() {
        return previousReceiveDate;
    }

    public void setPreviousReceiveDate(String previousReceiveDate) {
        this.previousReceiveDate = previousReceiveDate;
    }

    public List<ItemBatch_Mdl> getCurrentIbm() {
        return currentIbm;
    }

    public List<ItemBatch_Mdl> getPreviousIbm() {
        return previousIbm;
    }

    public String getV_Type() {
        return v_Type;
    }

    public void setV_Type(String v_Type) {
        this.v_Type = v_Type;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getV_No() {
        return v_No;
    }

    public void setV_No(String v_No) {
        this.v_No = v_No;
    }

    public String getRecIdPrefix() {
        return recIdPrefix;
    }

    public void setRecIdPrefix(String recIdPrefix) {
        this.recIdPrefix = recIdPrefix;
    }

    public String getContraSubCode() {
        return contraSubCode;
    }

    public void setContraSubCode(String contraSubCode) {
        this.contraSubCode = contraSubCode;
    }

}
