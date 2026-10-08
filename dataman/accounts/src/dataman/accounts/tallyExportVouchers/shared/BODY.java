package dataman.accounts.tallyExportVouchers.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BODY  implements Serializable, IsSerializable{

    private IMPORTDATA IMPORTDATA;

    public IMPORTDATA getIMPORTDATA() {
        return IMPORTDATA;
    }

    @XmlElement
    public void setIMPORTDATA(IMPORTDATA IMPORTDATA) {
        this.IMPORTDATA = IMPORTDATA;
    }

}
