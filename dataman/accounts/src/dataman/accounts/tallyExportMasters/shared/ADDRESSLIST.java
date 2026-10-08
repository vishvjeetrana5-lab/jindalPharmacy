package dataman.accounts.tallyExportMasters.shared;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ADDRESSLIST")
@XmlType(propOrder = {"TYPE", "ADDRESS"})
public class ADDRESSLIST {

    private String TYPE, ADDRESS;

    public String getTYPE() {
        return TYPE;
    }

    @XmlAttribute(name="TYPE")
    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    @XmlElement
    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

}
