package dataman.accounts.tallyExportVouchers.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "BILLALLOCATIONSLIST")
@XmlType(propOrder = {"NAME", "BILLTYPE", "TDSDEDUCTEEISSPECIALRATE", "AMOUNT", "INTERESTCOLLECTIONLIST", "STBILLCATEGORIESLIST"})
public class BILLALLOCATIONSLIST  implements Serializable, IsSerializable{

    private String NAME, BILLTYPE, TDSDEDUCTEEISSPECIALRATE, AMOUNT, INTERESTCOLLECTIONLIST, STBILLCATEGORIESLIST;

    public String getNAME() {
        return NAME;
    }

    @XmlElement
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getBILLTYPE() {
        return BILLTYPE;
    }

    @XmlElement
    public void setBILLTYPE(String BILLTYPE) {
        this.BILLTYPE = BILLTYPE;
    }

    public String getTDSDEDUCTEEISSPECIALRATE() {
        return TDSDEDUCTEEISSPECIALRATE;
    }

    @XmlElement
    public void setTDSDEDUCTEEISSPECIALRATE(String TDSDEDUCTEEISSPECIALRATE) {
        this.TDSDEDUCTEEISSPECIALRATE = TDSDEDUCTEEISSPECIALRATE;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    @XmlElement
    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getINTERESTCOLLECTIONLIST() {
        return INTERESTCOLLECTIONLIST;
    }

    @XmlElement(name = "INTERESTCOLLECTION.LIST")
    public void setINTERESTCOLLECTIONLIST(String INTERESTCOLLECTIONLIST) {
        this.INTERESTCOLLECTIONLIST = INTERESTCOLLECTIONLIST;
    }

    public String getSTBILLCATEGORIESLIST() {
        return STBILLCATEGORIESLIST;
    }

    @XmlElement(name = "STBILLCATEGORIES.LIST")

    public void setSTBILLCATEGORIESLIST(String STBILLCATEGORIESLIST) {
        this.STBILLCATEGORIESLIST = STBILLCATEGORIESLIST;
    }

}
