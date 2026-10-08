package dataman.accounts.tallyExportMasters.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "REQUESTDATA")
@XmlType(propOrder = {"TALLYMESSAGE"})
public class REQUESTDATA  implements Serializable, IsSerializable  {

    private List<TALLYMESSAGE> TALLYMESSAGE = new ArrayList<TALLYMESSAGE>();
  
    public List<TALLYMESSAGE> getTALLYMESSAGE() {
        return TALLYMESSAGE;
    }

    @XmlElement
    public void setTALLYMESSAGE(List<TALLYMESSAGE> TALLYMESSAGE) {
        this.TALLYMESSAGE = TALLYMESSAGE;
    }


}
