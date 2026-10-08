package aarogya.master.serviceCategoryMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ServiceCategoryMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String printSerialNo;
    private String itsOnSaleTag;
    private String itsOnSaleText;
    private String acServiceTag;
    private String acServiceText;

    private String isConsultantRequiredText;
    private String isConsultantRequiredTag;

    public ServiceCategoryMaster_Mdl() {
    }

    public String getPrintSerialNo() {
        return printSerialNo;
    }

    public void setPrintSerialNo(String printSerialNo) {
        this.printSerialNo = printSerialNo;
    }

    public String getItsOnSaleTag() {
        return itsOnSaleTag;
    }

    public void setItsOnSaleTag(String itsOnSaleTag) {
        this.itsOnSaleTag = itsOnSaleTag;
    }

    public String getItsOnSaleText() {
        return itsOnSaleText;
    }

    public void setItsOnSaleText(String itsOnSaleText) {
        this.itsOnSaleText = itsOnSaleText;
    }

    public String getAcServiceTag() {
        return acServiceTag;
    }

    public void setAcServiceTag(String acServiceTag) {
        this.acServiceTag = acServiceTag;
    }

    public String getAcServiceText() {
        return acServiceText;
    }

    public void setAcServiceText(String acServiceText) {
        this.acServiceText = acServiceText;
    }

    public String getIsConsultantRequiredText() {
        return isConsultantRequiredText;
    }

    public void setIsConsultantRequiredText(String isConsultantRequiredText) {
        this.isConsultantRequiredText = isConsultantRequiredText;
    }

    public String getIsConsultantRequiredTag() {
        return isConsultantRequiredTag;
    }

    public void setIsConsultantRequiredTag(String isConsultantRequiredTag) {
        this.isConsultantRequiredTag = isConsultantRequiredTag;
    }

}
