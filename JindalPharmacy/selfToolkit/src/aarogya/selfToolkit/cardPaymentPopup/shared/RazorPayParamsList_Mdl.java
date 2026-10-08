package aarogya.selfToolkit.cardPaymentPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RazorPayParamsList_Mdl implements Serializable, IsSerializable {

    
    
     private List<RazorPayParams_Mdl> configList=new ArrayList<>();
    
    
    public RazorPayParamsList_Mdl() {
    }

    public List<RazorPayParams_Mdl> getConfigList() {
        return configList;
    }

    public void setConfigList(List<RazorPayParams_Mdl> configList) {
        this.configList = configList;
    }

  

}
