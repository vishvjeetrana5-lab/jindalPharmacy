package dataman.accounts.tallyExportMasters.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "NAMELIST")
@XmlType(propOrder = {"TYPE", "NAME"})
public class NAMELIST  implements Serializable, IsSerializable {

    private String TYPE, NAME;

    public String getTYPE() {
        return TYPE;
    }

    @XmlAttribute(name="TYPE")
    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getNAME() {
        return NAME;
    }

    @XmlElement
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

}
