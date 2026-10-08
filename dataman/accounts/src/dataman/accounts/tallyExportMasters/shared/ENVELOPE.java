package dataman.accounts.tallyExportMasters.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ENVELOPE")
@XmlType(propOrder = {"HEADER", "BODY"})
public class ENVELOPE  implements Serializable, IsSerializable {

    private HEADER HEADER;
    private BODY BODY;

    public ENVELOPE() {

    }

    public HEADER getHEADER() {
        return HEADER;
    }

    @XmlElement
    public void setHEADER(HEADER HEADER) {
        this.HEADER = HEADER;
    }

    public BODY getBODY() {
        return BODY;
    }

    @XmlElement
    public void setBODY(BODY BODY) {
        this.BODY = BODY;
    }

}
