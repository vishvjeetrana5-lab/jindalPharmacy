package dataman.accounts.tallyExportVouchers.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "REQUESTDATA")
@XmlType(propOrder = {"TALLYMESSAGE", "COMPANY"})
public class REQUESTDATA  implements Serializable, IsSerializable{

    private List<TALLYMESSAGE> TALLYMESSAGE = new ArrayList<TALLYMESSAGE>();
    private COMPANY COMPANY;

    public List<TALLYMESSAGE> getTALLYMESSAGE() {
        return TALLYMESSAGE;
    }

    @XmlElement
    public void setTALLYMESSAGE(List<TALLYMESSAGE> TALLYMESSAGE) {
        this.TALLYMESSAGE = TALLYMESSAGE;
    }

    public COMPANY getCOMPANY() {
        return COMPANY;
    }

    @XmlElement
    public void setCOMPANY(COMPANY COMPANY) {
        this.COMPANY = COMPANY;
    }

}
