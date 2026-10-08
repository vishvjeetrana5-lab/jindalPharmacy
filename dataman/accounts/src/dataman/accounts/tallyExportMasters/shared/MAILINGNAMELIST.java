package dataman.accounts.tallyExportMasters.shared;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "MAILINGNAMELIST")
@XmlType(propOrder = {"TYPE", "MAILINGNAME"})
public class MAILINGNAMELIST {

    private String TYPE, MAILINGNAME;

    public String getTYPE() {
        return TYPE;
    }

    @XmlAttribute(name=("TYPE"))
    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getMAILINGNAME() {
        return MAILINGNAME;
    }

    @XmlElement
    public void setMAILINGNAME(String MAILINGNAME) {
        this.MAILINGNAME = MAILINGNAME;
    }

}
