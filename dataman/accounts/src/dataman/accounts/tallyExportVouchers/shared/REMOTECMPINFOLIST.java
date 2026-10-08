package dataman.accounts.tallyExportVouchers.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "REMOTECMPINFOLIST")
@XmlType(propOrder = {"MERGE", "NAME", "REMOTECMPNAME", "REMOTECMPSTATE"})
public class REMOTECMPINFOLIST  implements Serializable, IsSerializable{

    private String MERGE, NAME, REMOTECMPNAME, REMOTECMPSTATE;

    public String getNAME() {
        return NAME;
    }

    @XmlElement
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getREMOTECMPNAME() {
        return REMOTECMPNAME;
    }

    @XmlElement
    public void setREMOTECMPNAME(String REMOTECMPNAME) {
        this.REMOTECMPNAME = REMOTECMPNAME;
    }

    public String getREMOTECMPSTATE() {
        return REMOTECMPSTATE;
    }

    @XmlElement
    public void setREMOTECMPSTATE(String REMOTECMPSTATE) {
        this.REMOTECMPSTATE = REMOTECMPSTATE;
    }

    public String getMERGE() {
        return MERGE;
    }

    @XmlAttribute(name = "MERGE")
    public void setMERGE(String MERGE) {
        this.MERGE = MERGE;
    }

}
