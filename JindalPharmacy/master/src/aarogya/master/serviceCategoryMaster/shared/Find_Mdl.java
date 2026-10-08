package aarogya.master.serviceCategoryMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String printSerialNo;
    private String itsOnSaleText;
    private String acServiceText;
     private String isConsultantRequiredText;

    public Find_Mdl() {
    }

    public String getPrintSerialNo() {
        return printSerialNo;
    }

    public void setPrintSerialNo(String printSerialNo) {
        this.printSerialNo = printSerialNo;
    }

    public String getItsOnSaleText() {
        return itsOnSaleText;
    }

    public void setItsOnSaleText(String itsOnSaleText) {
        this.itsOnSaleText = itsOnSaleText;
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

}
