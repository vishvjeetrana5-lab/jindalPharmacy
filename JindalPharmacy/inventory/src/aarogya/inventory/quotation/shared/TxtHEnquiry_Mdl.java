package aarogya.inventory.quotation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class TxtHEnquiry_Mdl extends KeyField implements Serializable,IsSerializable{

    private String enquiryDate;
    private String enquiryRefNo;
  

    public TxtHEnquiry_Mdl() {
    }

    public String getEnquiryDate() {
        return enquiryDate;
    }

    public void setEnquiryDate(String enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    public String getEnquiryRefNo() {
        return enquiryRefNo;
    }

    public void setEnquiryRefNo(String enquiryRefNo) {
        this.enquiryRefNo = enquiryRefNo;
    }

  
    

}
