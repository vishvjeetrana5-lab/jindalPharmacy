package aarogya.mis.reports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class ConsultantSharing_Mdl implements Serializable, IsSerializable {

    private Long regDocId, consultantCode, organizationCode, serviceDocId;
    private String nature, regNo, billNo, consultantName, roomCategoryName, organizationName, uhidNo, patientName, age, gendor, serviceName, categoryName, sharing, moduleName;
    private Date regDate, billDate;
    private Integer tempNature, isHeader, categoryCode, serviceCode, groupCode, roomCategoryCode;
    private Double quantity, srvAmt, sharingSrvAmt, conUnit, conAmt, sharingConAmt, refUnit, refAmt, sharingRefAmt, discountUnit, disAmt, discount;

    public ConsultantSharing_Mdl() {
    }

    public Long getRegDocId() {
        return regDocId;
    }

    public void setRegDocId(Long regDocId) {
        this.regDocId = regDocId;
    }

    public Long getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(Long consultantCode) {
        this.consultantCode = consultantCode;
    }

    public Long getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(Long organizationCode) {
        this.organizationCode = organizationCode;
    }

    public Long getServiceDocId() {
        return serviceDocId;
    }

    public void setServiceDocId(Long serviceDocId) {
        this.serviceDocId = serviceDocId;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getUhidNo() {
        return uhidNo;
    }

    public void setUhidNo(String uhidNo) {
        this.uhidNo = uhidNo;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGendor() {
        return gendor;
    }

    public void setGendor(String gendor) {
        this.gendor = gendor;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSharing() {
        return sharing;
    }

    public void setSharing(String sharing) {
        this.sharing = sharing;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Integer getTempNature() {
        return tempNature;
    }

    public void setTempNature(Integer tempNature) {
        this.tempNature = tempNature;
    }

    public Integer getIsHeader() {
        return isHeader;
    }

    public void setIsHeader(Integer isHeader) {
        this.isHeader = isHeader;
    }

    public Integer getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(Integer categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Integer getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(Integer serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Integer getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(Integer groupCode) {
        this.groupCode = groupCode;
    }

    public Integer getRoomCategoryCode() {
        return roomCategoryCode;
    }

    public void setRoomCategoryCode(Integer roomCategoryCode) {
        this.roomCategoryCode = roomCategoryCode;
    }

    public String getRoomCategoryName() {
        return roomCategoryName;
    }

    public void setRoomCategoryName(String roomCategoryName) {
        this.roomCategoryName = roomCategoryName;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getSrvAmt() {
        return srvAmt;
    }

    public void setSrvAmt(Double srvAmt) {
        this.srvAmt = srvAmt;
    }

    public Double getSharingSrvAmt() {
        return sharingSrvAmt;
    }

    public void setSharingSrvAmt(Double sharingSrvAmt) {
        this.sharingSrvAmt = sharingSrvAmt;
    }

    public Double getConUnit() {
        return conUnit;
    }

    public void setConUnit(Double conUnit) {
        this.conUnit = conUnit;
    }

    public Double getConAmt() {
        return conAmt;
    }

    public void setConAmt(Double conAmt) {
        this.conAmt = conAmt;
    }

    public Double getSharingConAmt() {
        return sharingConAmt;
    }

    public void setSharingConAmt(Double sharingConAmt) {
        this.sharingConAmt = sharingConAmt;
    }

    public Double getRefUnit() {
        return refUnit;
    }

    public void setRefUnit(Double refUnit) {
        this.refUnit = refUnit;
    }

    public Double getRefAmt() {
        return refAmt;
    }

    public void setRefAmt(Double refAmt) {
        this.refAmt = refAmt;
    }

    public Double getSharingRefAmt() {
        return sharingRefAmt;
    }

    public void setSharingRefAmt(Double sharingRefAmt) {
        this.sharingRefAmt = sharingRefAmt;
    }

    public Double getDiscountUnit() {
        return discountUnit;
    }

    public void setDiscountUnit(Double discountUnit) {
        this.discountUnit = discountUnit;
    }

    public Double getDisAmt() {
        return disAmt;
    }

    public void setDisAmt(Double disAmt) {
        this.disAmt = disAmt;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.consultantCode);
        hash = 59 * hash + Objects.hashCode(this.categoryCode);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConsultantSharing_Mdl other = (ConsultantSharing_Mdl) obj;
        if (!Objects.equals(this.consultantCode, other.consultantCode)) {
            return false;
        }
        if (!Objects.equals(this.categoryCode, other.categoryCode)) {
            return false;
        }
        return true;
    }

}
