package dataman.payroll.employeeMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdSoftId_Mdl extends KeyField implements Serializable, IsSerializable {
    private String sNo;
    
    public GrdSoftId_Mdl() {
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }
    
}
