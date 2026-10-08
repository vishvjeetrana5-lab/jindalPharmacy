package dataman.accounts.tallyExportVouchers.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TALLYMESSAGE  implements Serializable, IsSerializable{

    private String xmlns;
    private VOUCHER VOUCHER;

    public String getXmlns() {
        return xmlns;
    }

    @XmlAttribute(name="xmlns:UDF")
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public VOUCHER getVOUCHER() {
        return VOUCHER;
    }

    @XmlElement
    public void setVOUCHER(VOUCHER VOUCHER) {
        this.VOUCHER = VOUCHER;
    }

}
