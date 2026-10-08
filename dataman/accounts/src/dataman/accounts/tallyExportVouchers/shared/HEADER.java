
package dataman.accounts.tallyExportVouchers.shared;


import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HEADER  implements Serializable, IsSerializable{
    
    private String TALLYREQUEST;

    public String getTALLYREQUEST() {
        return TALLYREQUEST;
    }

    @XmlElement
    public void setTALLYREQUEST(String TALLYREQUEST) {
        this.TALLYREQUEST = TALLYREQUEST;
    }
    
    
}
