package dataman.accounts.tallyExportMasters.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "LANGUAGENAMELIST")
@XmlType(propOrder = {"NAMELIST","LANGUAGEID"})
public class LANGUAGENAMELIST  implements Serializable, IsSerializable {

    private NAMELIST NAMELIST;
    private String LANGUAGEID;

    public NAMELIST getNAMELIST() {
        return NAMELIST;
    }

    @XmlElement(name = "NAME.LIST")
    public void setNAMELIST(NAMELIST NAMELIST) {
        this.NAMELIST = NAMELIST;
    }

    public String getLANGUAGEID() {
        return LANGUAGEID;
    }

    @XmlElement
    public void setLANGUAGEID(String LANGUAGEID) {
        this.LANGUAGEID = LANGUAGEID;
    }

}
