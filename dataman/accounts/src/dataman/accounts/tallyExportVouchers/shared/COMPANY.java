package dataman.accounts.tallyExportVouchers.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "COMPANY")
public class COMPANY  implements Serializable, IsSerializable{

    private REMOTECMPINFOLIST REMOTECMPINFOLIST;

    public REMOTECMPINFOLIST getREMOTECMPINFOLIST() {
        return REMOTECMPINFOLIST;
    }

    @XmlElement(name="REMOTECMPINFO.LIST")
    public void setREMOTECMPINFOLIST(REMOTECMPINFOLIST REMOTECMPINFOLIST) {
        this.REMOTECMPINFOLIST = REMOTECMPINFOLIST;
    }

}
