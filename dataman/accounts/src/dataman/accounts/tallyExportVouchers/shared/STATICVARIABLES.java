
package dataman.accounts.tallyExportVouchers.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class STATICVARIABLES  implements Serializable, IsSerializable{
    
    private String SVCURRENTCOMPANY;

    public String getSVCURRENTCOMPANY() {
        return SVCURRENTCOMPANY;
    }

    @XmlElement
    public void setSVCURRENTCOMPANY(String SVCURRENTCOMPANY) {
        this.SVCURRENTCOMPANY = SVCURRENTCOMPANY;
    }
    
    
}
