package aarogya.pharmacy.reports.shared;

import aarogya.pharmacy.reports.server.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHsupplier_Mdl extends KeyField implements Serializable, IsSerializable {

    private String mobile;
  
    public TxtHsupplier_Mdl() {
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

   

}
