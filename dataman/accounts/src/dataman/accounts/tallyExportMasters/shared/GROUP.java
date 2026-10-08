package dataman.accounts.tallyExportMasters.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "GROUP")
@XmlType(propOrder = {"NAME", "RESERVEDNAME", "PARENT", "GRPDEBITPARENT", "GRPCREDITPARENT", "ISBILLWISEON", "ISCOSTCENTRESON",
    "ISADDABLE", "ISSUBLEDGER", "ISREVENUE", "AFFECTSGROSSPROFIT", "ISDEEMEDPOSITIVE", "TRACKNEGATIVEBALANCES",
    "ISCONDENSED", "AFFECTSSTOCK", "ISGROUPFORLOANRCPT", "ISGROUPFORLOANPYMNT", "SORTPOSITION","LANGUAGENAMELIST"})
public class GROUP  implements Serializable, IsSerializable {

    private String NAME, RESERVEDNAME, PARENT, GRPDEBITPARENT, GRPCREDITPARENT, ISBILLWISEON, ISCOSTCENTRESON,
            ISADDABLE, ISSUBLEDGER, ISREVENUE, AFFECTSGROSSPROFIT, ISDEEMEDPOSITIVE, TRACKNEGATIVEBALANCES, ISCONDENSED, AFFECTSSTOCK,
            ISGROUPFORLOANRCPT, ISGROUPFORLOANPYMNT, SORTPOSITION;

    private LANGUAGENAMELIST LANGUAGENAMELIST;

    public String getRESERVEDNAME() {
        return RESERVEDNAME;
    }

    @XmlAttribute(name="RESERVEDNAME")
    public void setRESERVEDNAME(String RESERVEDNAME) {
        this.RESERVEDNAME = RESERVEDNAME;
    }

    public String getPARENT() {
        return PARENT;
    }

    @XmlElement
    public void setPARENT(String PARENT) {
        this.PARENT = PARENT;
    }

    public String getGRPDEBITPARENT() {
        return GRPDEBITPARENT;
    }

    @XmlElement
    public void setGRPDEBITPARENT(String GRPDEBITPARENT) {
        this.GRPDEBITPARENT = GRPDEBITPARENT;
    }

    public String getGRPCREDITPARENT() {
        return GRPCREDITPARENT;
    }

    @XmlElement
    public void setGRPCREDITPARENT(String GRPCREDITPARENT) {
        this.GRPCREDITPARENT = GRPCREDITPARENT;
    }

    public String getISBILLWISEON() {
        return ISBILLWISEON;
    }

    @XmlElement
    public void setISBILLWISEON(String ISBILLWISEON) {
        this.ISBILLWISEON = ISBILLWISEON;
    }

    public String getISCOSTCENTRESON() {
        return ISCOSTCENTRESON;
    }

    @XmlElement
    public void setISCOSTCENTRESON(String ISCOSTCENTRESON) {
        this.ISCOSTCENTRESON = ISCOSTCENTRESON;
    }

    public String getISADDABLE() {
        return ISADDABLE;
    }

    @XmlElement
    public void setISADDABLE(String ISADDABLE) {
        this.ISADDABLE = ISADDABLE;
    }

    public String getISSUBLEDGER() {
        return ISSUBLEDGER;
    }

    @XmlElement
    public void setISSUBLEDGER(String ISSUBLEDGER) {
        this.ISSUBLEDGER = ISSUBLEDGER;
    }

    public String getISREVENUE() {
        return ISREVENUE;
    }

    @XmlElement
    public void setISREVENUE(String ISREVENUE) {
        this.ISREVENUE = ISREVENUE;
    }

    public String getAFFECTSGROSSPROFIT() {
        return AFFECTSGROSSPROFIT;
    }

    @XmlElement
    public void setAFFECTSGROSSPROFIT(String AFFECTSGROSSPROFIT) {
        this.AFFECTSGROSSPROFIT = AFFECTSGROSSPROFIT;
    }

    public String getISDEEMEDPOSITIVE() {
        return ISDEEMEDPOSITIVE;
    }

    @XmlElement
    public void setISDEEMEDPOSITIVE(String ISDEEMEDPOSITIVE) {
        this.ISDEEMEDPOSITIVE = ISDEEMEDPOSITIVE;
    }

    public String getTRACKNEGATIVEBALANCES() {
        return TRACKNEGATIVEBALANCES;
    }

    @XmlElement
    public void setTRACKNEGATIVEBALANCES(String TRACKNEGATIVEBALANCES) {
        this.TRACKNEGATIVEBALANCES = TRACKNEGATIVEBALANCES;
    }

    public String getISCONDENSED() {
        return ISCONDENSED;
    }

    @XmlElement
    public void setISCONDENSED(String ISCONDENSED) {
        this.ISCONDENSED = ISCONDENSED;
    }

    public String getAFFECTSSTOCK() {
        return AFFECTSSTOCK;
    }

    @XmlElement
    public void setAFFECTSSTOCK(String AFFECTSSTOCK) {
        this.AFFECTSSTOCK = AFFECTSSTOCK;
    }

    public String getISGROUPFORLOANRCPT() {
        return ISGROUPFORLOANRCPT;
    }

    @XmlElement
    public void setISGROUPFORLOANRCPT(String ISGROUPFORLOANRCPT) {
        this.ISGROUPFORLOANRCPT = ISGROUPFORLOANRCPT;
    }

    public String getISGROUPFORLOANPYMNT() {
        return ISGROUPFORLOANPYMNT;
    }

    @XmlElement
    public void setISGROUPFORLOANPYMNT(String ISGROUPFORLOANPYMNT) {
        this.ISGROUPFORLOANPYMNT = ISGROUPFORLOANPYMNT;
    }

    public String getSORTPOSITION() {
        return SORTPOSITION;
    }

    @XmlElement
    public void setSORTPOSITION(String SORTPOSITION) {
        this.SORTPOSITION = SORTPOSITION;
    }

    public LANGUAGENAMELIST getLANGUAGENAMELIST() {
        return LANGUAGENAMELIST;
    }

    @XmlElement(name = "LANGUAGENAME.LIST")
    public void setLANGUAGENAMELIST(LANGUAGENAMELIST LANGUAGENAMELIST) {
        this.LANGUAGENAMELIST = LANGUAGENAMELIST;
    }

    public String getNAME() {
        return NAME;
    }

    @XmlAttribute(name="Name")
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

}
