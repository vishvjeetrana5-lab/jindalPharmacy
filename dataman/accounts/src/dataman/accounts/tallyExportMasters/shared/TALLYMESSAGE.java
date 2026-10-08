package dataman.accounts.tallyExportMasters.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "TALLYMESSAGE")
@XmlType(propOrder = {"xmlns", "GROUP", "LEDGER"})
public class TALLYMESSAGE  implements Serializable, IsSerializable {

    private String xmlns;
    private GROUP GROUP;
    private LEDGER LEDGER;

    public String getXmlns() {
        return xmlns;
    }

    @XmlAttribute(name = "xmlns:UDF")
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public GROUP getGROUP() {
        return GROUP;
    }

    @XmlElement
    public void setGROUP(GROUP GROUP) {
        this.GROUP = GROUP;
    }

    public LEDGER getLEDGER() {
        return LEDGER;
    }

    @XmlElement
    public void setLEDGER(LEDGER LEDGER) {
        this.LEDGER = LEDGER;
    }

}
