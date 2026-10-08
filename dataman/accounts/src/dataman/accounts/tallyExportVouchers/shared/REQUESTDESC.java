
package dataman.accounts.tallyExportVouchers.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class REQUESTDESC  implements Serializable, IsSerializable{
    private String REPORTNAME;
private STATICVARIABLES STATICVARIABLES;
    public String getREPORTNAME() {
        return REPORTNAME;
    }

       @XmlElement
    public void setREPORTNAME(String REPORTNAME) {
        this.REPORTNAME = REPORTNAME;
    }

    public STATICVARIABLES getSTATICVARIABLES() {
        return STATICVARIABLES;
    }

     @XmlElement
    public void setSTATICVARIABLES(STATICVARIABLES STATICVARIABLES) {
        this.STATICVARIABLES = STATICVARIABLES;
    }
    
}
