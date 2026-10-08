package aarogya.dashBoard.requisitionPharmacy.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RequisitionPharmacy_Mdl extends KeyField implements Serializable, IsSerializable {

    List<GrdRequisitionDetailPharmacy_Mdl> objRequistionDetail = new ArrayList<GrdRequisitionDetailPharmacy_Mdl>();
    private String V_No;
    private String requisitionByName;
    private String requisitionByCode;
    private String departmentCode, departmentToCode;
    private String departmentName, departmentToName;
    private String date;
    private String vCat;

    public RequisitionPharmacy_Mdl() {
    }

    public List<GrdRequisitionDetailPharmacy_Mdl> getObjRequistionDetail() {
        return objRequistionDetail;
    }

    public void setObjRequistionDetail(List<GrdRequisitionDetailPharmacy_Mdl> objRequistionDetail) {
        this.objRequistionDetail = objRequistionDetail;
    }

    public String getV_No() {
        return V_No;
    }

    public void setV_No(String V_No) {
        this.V_No = V_No;
    }

    public String getRequisitionByName() {
        return requisitionByName;
    }

    public void setRequisitionByName(String requisitionByName) {
        this.requisitionByName = requisitionByName;
    }

    public String getRequisitionByCode() {
        return requisitionByCode;
    }

    public void setRequisitionByCode(String requisitionByCode) {
        this.requisitionByCode = requisitionByCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepartmentToCode() {
        return departmentToCode;
    }

    public void setDepartmentToCode(String departmentToCode) {
        this.departmentToCode = departmentToCode;
    }

    public String getDepartmentToName() {
        return departmentToName;
    }

    public void setDepartmentToName(String departmentToName) {
        this.departmentToName = departmentToName;
    }

    public String getvCat() {
        return vCat;
    }

    public void setvCat(String vCat) {
        this.vCat = vCat;
    }

}
