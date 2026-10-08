package dataman.accounts.tallyExportVouchers.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "IMPORTDATA")
@XmlType(propOrder = {"REQUESTDESC", "REQUESTDATA"})
public class IMPORTDATA  implements Serializable, IsSerializable{

    private REQUESTDESC REQUESTDESC;
    private REQUESTDATA REQUESTDATA;

    public REQUESTDESC getREQUESTDESC() {
        return REQUESTDESC;
    }

    @XmlElement
    public void setREQUESTDESC(REQUESTDESC REQUESTDESC) {
        this.REQUESTDESC = REQUESTDESC;
    }

    public REQUESTDATA getREQUESTDATA() {
        return REQUESTDATA;
    }

    @XmlElement
    public void setREQUESTDATA(REQUESTDATA REQUESTDATA) {
        this.REQUESTDATA = REQUESTDATA;
    }

}
