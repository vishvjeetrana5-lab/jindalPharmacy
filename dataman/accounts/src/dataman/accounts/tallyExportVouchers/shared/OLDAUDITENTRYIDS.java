package dataman.accounts.tallyExportVouchers.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OLDAUDITENTRYIDS  implements Serializable, IsSerializable{

    private String TYPE;
    private String OLDAUDITENTRYIDS;

    public String getTYPE() {
        return TYPE;
    }

    @XmlAttribute(name="TYPE")
    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getOLDAUDITENTRYIDS() {
        return OLDAUDITENTRYIDS;
    }

    @XmlElement
    public void setOLDAUDITENTRYIDS(String OLDAUDITENTRYIDS) {
        this.OLDAUDITENTRYIDS = OLDAUDITENTRYIDS;
    }
}
