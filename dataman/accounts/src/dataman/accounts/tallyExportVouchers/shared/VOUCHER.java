package dataman.accounts.tallyExportVouchers.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "VOUCHER")
@XmlType(propOrder = {"REMOTEID", "VCHKEY", "VCHTYPE", "ACTION", "OBJVIEW", "OLDAUDITENTRYIDS", "DATE", "NARRATION", "VOUCHERTYPENAME", "REFERENCE",
    "VOUCHERNUMBER", "PARTYLEDGERNAME", "CSTFORMISSUETYPE", "CSTFORMRECVTYPE", "FBTPAYMENTTYPE", "PERSISTEDVIEW", "VCHGSTCLASS", "ENTEREDBY", "DIFFACTUALQTY", "ISMSTFROMSYNC", "ASORIGINAL", "AUDITED", "FORJOBCOSTING",
    "ISOPTIONAL", "EFFECTIVEDATE", "USEFOREXCISE", "ISFORJOBWORKIN", "ALLOWCONSUMPTION", "USEFORINTEREST", "USEFORGAINLOSS", "USEFORGODOWNTRANSFER", "USEFORCOMPOUND", "USEFORSERVICETAX",
    "ISEXCISEVOUCHER", "EXCISETAXOVERRIDE", "USEFORTAXUNITTRANSFER", "EXCISEOPENING", "USEFORFINALPRODUCTION", "ISTDSOVERRIDDEN", "ISTCSOVERRIDDEN", "ISTDSTCSCASHVCH", "INCLUDEADVPYMTVCH", "ISSUBWORKSCONTRACT", "ISVATOVERRIDDEN",
    "IGNOREORIGVCHDATE", "ISSERVICETAXOVERRIDDEN", "ISISDVOUCHER", "ISEXCISEOVERRIDDEN", "ISEXCISESUPPLYVCH", "ISGSTOVERRIDDEN", "GSTNOTEXPORTED", "ISVATPRINCIPALACCOUNT", "ISSHIPPINGWITHINSTATE", "ISCANCELLED", "HASCASHFLOW", "ISPOSTDATED",
    "USETRACKINGNUMBER", "ISINVOICE", "MFGJOURNAL", "HASDISCOUNTS", "ASPAYSLIP", "ISCOSTCENTRE", "ISSTXNONREALIZEDVCH", "ISEXCISEMANUFACTURERON", "ISBLANKCHEQU", "ISVOID", "ISONHOLD",
    "ORDERLINESTATUS", "VATISAGNSTCANCSALES", "VATISPURCEXEMPTED", "ISVATRESTAXINVOICE", "VATISASSESABLECALCVCH", "ISVATDUTYPAID",
    "ISDELIVERYSAMEASCONSIGNEE", "ISDISPATCHSAMEASCONSIGNOR", "ISDELETED", "CHANGEVCHMODE", "VOUCHERKEY",
    "EXCLUDEDTAXATIONSLIST", "OLDAUDITENTRIESLIST", "ACCOUNTAUDITENTRIESLIST", "AUDITENTRIESLIST", "DUTYHEADDETAILSLIST",
    "SUPPLEMENTARYDUTYHEADDETAILSLIST", "INVOICEDELNOTESLIST", "INVOICEORDERLISTLIST", "INVOICEINDENTLISTLIST", "ATTENDANCEENTRIESLIST", "ORIGINVOICEDETAILSLIST", "INVOICEEXPORTLISTLIST","ALLLEDGERENTRIESLIST"})

public class VOUCHER  implements Serializable, IsSerializable{

    private String REMOTEID, VCHKEY, VCHTYPE, ACTION, OBJVIEW;
    private OLDAUDITENTRYIDS OLDAUDITENTRYIDS;
    private String DATE, NARRATION, VOUCHERTYPENAME, REFERENCE, PERSISTEDVIEW;
    private String VOUCHERNUMBER, PARTYLEDGERNAME, CSTFORMISSUETYPE, CSTFORMRECVTYPE, FBTPAYMENTTYPE, VCHGSTCLASS, ENTEREDBY, DIFFACTUALQTY, ISMSTFROMSYNC, ASORIGINAL, AUDITED, FORJOBCOSTING;
    private String ISOPTIONAL, EFFECTIVEDATE, USEFOREXCISE, ISFORJOBWORKIN, ALLOWCONSUMPTION, USEFORINTEREST, USEFORGAINLOSS, USEFORGODOWNTRANSFER, USEFORCOMPOUND, USEFORSERVICETAX;
    private String ISEXCISEVOUCHER, EXCISETAXOVERRIDE, USEFORTAXUNITTRANSFER, EXCISEOPENING, USEFORFINALPRODUCTION, ISTDSOVERRIDDEN, ISTCSOVERRIDDEN, ISTDSTCSCASHVCH, INCLUDEADVPYMTVCH, ISSUBWORKSCONTRACT, ISVATOVERRIDDEN;

    private String IGNOREORIGVCHDATE, ISSERVICETAXOVERRIDDEN, ISISDVOUCHER, ISEXCISEOVERRIDDEN, ISEXCISESUPPLYVCH, ISGSTOVERRIDDEN, GSTNOTEXPORTED,
            ISVATPRINCIPALACCOUNT, ISSHIPPINGWITHINSTATE, ISCANCELLED, HASCASHFLOW, ISPOSTDATED, USETRACKINGNUMBER, ISINVOICE, MFGJOURNAL,
            HASDISCOUNTS, ASPAYSLIP, ISCOSTCENTRE, ISSTXNONREALIZEDVCH, ISEXCISEMANUFACTURERON, ISBLANKCHEQU, ISVOID, ISONHOLD;

    private String ORDERLINESTATUS, VATISAGNSTCANCSALES, VATISPURCEXEMPTED, ISVATRESTAXINVOICE, VATISASSESABLECALCVCH, ISVATDUTYPAID,
            ISDELIVERYSAMEASCONSIGNEE, ISDISPATCHSAMEASCONSIGNOR, ISDELETED, CHANGEVCHMODE, VOUCHERKEY;

    private String EXCLUDEDTAXATIONSLIST, OLDAUDITENTRIESLIST, ACCOUNTAUDITENTRIESLIST, AUDITENTRIESLIST, DUTYHEADDETAILSLIST,
            SUPPLEMENTARYDUTYHEADDETAILSLIST, INVOICEDELNOTESLIST, INVOICEORDERLISTLIST, INVOICEINDENTLISTLIST, ATTENDANCEENTRIESLIST,
            ORIGINVOICEDETAILSLIST, INVOICEEXPORTLISTLIST;

    private List<ALLLEDGERENTRIESLIST> ALLLEDGERENTRIESLIST = new ArrayList<ALLLEDGERENTRIESLIST>();

    public String getREMOTEID() {
        return REMOTEID;
    }

    @XmlAttribute(name = "REMOTEID")
    public void setREMOTEID(String REMOTEID) {
        this.REMOTEID = REMOTEID;
    }

    public String getVCHKEY() {
        return VCHKEY;
    }

    @XmlAttribute(name = "VCHKEY")
    public void setVCHKEY(String VCHKEY) {
        this.VCHKEY = VCHKEY;
    }

    public String getVCHTYPE() {
        return VCHTYPE;
    }

    @XmlAttribute(name = "VCHTYPE")
    public void setVCHTYPE(String VCHTYPE) {
        this.VCHTYPE = VCHTYPE;
    }

    public String getACTION() {
        return ACTION;
    }

    @XmlAttribute(name = "ACTION")
    public void setACTION(String ACTION) {
        this.ACTION = ACTION;
    }

    public String getOBJVIEW() {
        return OBJVIEW;
    }

    @XmlAttribute(name = "OBJVIEW")
    public void setOBJVIEW(String OBJVIEW) {
        this.OBJVIEW = OBJVIEW;
    }

    public OLDAUDITENTRYIDS getOLDAUDITENTRYIDS() {
        return OLDAUDITENTRYIDS;
    }

    @XmlElement(name = "OLDAUDITENTRYIDS.LIST")
    public void setOLDAUDITENTRYIDS(OLDAUDITENTRYIDS OLDAUDITENTRYIDS) {
        this.OLDAUDITENTRYIDS = OLDAUDITENTRYIDS;
    }

    public String getDATE() {
        return DATE;
    }

    @XmlElement
    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getNARRATION() {
        return NARRATION;
    }

    @XmlElement
    public void setNARRATION(String NARRATION) {
        this.NARRATION = NARRATION;
    }

    public String getVOUCHERTYPENAME() {
        return VOUCHERTYPENAME;
    }

    @XmlElement
    public void setVOUCHERTYPENAME(String VOUCHERTYPENAME) {
        this.VOUCHERTYPENAME = VOUCHERTYPENAME;
    }

    public String getREFERENCE() {
        return REFERENCE;
    }

    @XmlElement
    public void setREFERENCE(String REFERENCE) {
        this.REFERENCE = REFERENCE;
    }

    public String getVOUCHERNUMBER() {
        return VOUCHERNUMBER;
    }

    @XmlElement
    public void setVOUCHERNUMBER(String VOUCHERNUMBER) {
        this.VOUCHERNUMBER = VOUCHERNUMBER;
    }

    public String getPARTYLEDGERNAME() {
        return PARTYLEDGERNAME;
    }

    @XmlElement
    public void setPARTYLEDGERNAME(String PARTYLEDGERNAME) {
        this.PARTYLEDGERNAME = PARTYLEDGERNAME;
    }

    public String getCSTFORMISSUETYPE() {
        return CSTFORMISSUETYPE;
    }

    @XmlElement
    public void setCSTFORMISSUETYPE(String CSTFORMISSUETYPE) {
        this.CSTFORMISSUETYPE = CSTFORMISSUETYPE;
    }

    public String getCSTFORMRECVTYPE() {
        return CSTFORMRECVTYPE;
    }

    @XmlElement
    public void setCSTFORMRECVTYPE(String CSTFORMRECVTYPE) {
        this.CSTFORMRECVTYPE = CSTFORMRECVTYPE;
    }

    public String getFBTPAYMENTTYPE() {
        return FBTPAYMENTTYPE;
    }

    @XmlElement

    public void setFBTPAYMENTTYPE(String FBTPAYMENTTYPE) {
        this.FBTPAYMENTTYPE = FBTPAYMENTTYPE;
    }

    public String getVCHGSTCLASS() {
        return VCHGSTCLASS;
    }

    @XmlElement
    public void setVCHGSTCLASS(String VCHGSTCLASS) {
        this.VCHGSTCLASS = VCHGSTCLASS;
    }

    public String getENTEREDBY() {
        return ENTEREDBY;
    }

    @XmlElement
    public void setENTEREDBY(String ENTEREDBY) {
        this.ENTEREDBY = ENTEREDBY;
    }

    public String getDIFFACTUALQTY() {
        return DIFFACTUALQTY;
    }

    @XmlElement
    public void setDIFFACTUALQTY(String DIFFACTUALQTY) {
        this.DIFFACTUALQTY = DIFFACTUALQTY;
    }

    public String getISMSTFROMSYNC() {
        return ISMSTFROMSYNC;
    }

    @XmlElement
    public void setISMSTFROMSYNC(String ISMSTFROMSYNC) {
        this.ISMSTFROMSYNC = ISMSTFROMSYNC;
    }

    public String getASORIGINAL() {
        return ASORIGINAL;
    }

    @XmlElement
    public void setASORIGINAL(String ASORIGINAL) {
        this.ASORIGINAL = ASORIGINAL;
    }

    public String getISOPTIONAL() {
        return ISOPTIONAL;
    }

    @XmlElement
    public void setISOPTIONAL(String ISOPTIONAL) {
        this.ISOPTIONAL = ISOPTIONAL;
    }

    public String getEFFECTIVEDATE() {
        return EFFECTIVEDATE;
    }

    @XmlElement
    public void setEFFECTIVEDATE(String EFFECTIVEDATE) {
        this.EFFECTIVEDATE = EFFECTIVEDATE;
    }

    public String getUSEFOREXCISE() {
        return USEFOREXCISE;
    }

    @XmlElement
    public void setUSEFOREXCISE(String USEFOREXCISE) {
        this.USEFOREXCISE = USEFOREXCISE;
    }

    public String getISFORJOBWORKIN() {
        return ISFORJOBWORKIN;
    }

    @XmlElement
    public void setISFORJOBWORKIN(String ISFORJOBWORKIN) {
        this.ISFORJOBWORKIN = ISFORJOBWORKIN;
    }

    public String getALLOWCONSUMPTION() {
        return ALLOWCONSUMPTION;
    }

    @XmlElement
    public void setALLOWCONSUMPTION(String ALLOWCONSUMPTION) {
        this.ALLOWCONSUMPTION = ALLOWCONSUMPTION;
    }

    public String getUSEFORINTEREST() {
        return USEFORINTEREST;
    }

    @XmlElement
    public void setUSEFORINTEREST(String USEFORINTEREST) {
        this.USEFORINTEREST = USEFORINTEREST;
    }

    public String getUSEFORGAINLOSS() {
        return USEFORGAINLOSS;
    }

    @XmlElement
    public void setUSEFORGAINLOSS(String USEFORGAINLOSS) {
        this.USEFORGAINLOSS = USEFORGAINLOSS;
    }

    public String getUSEFORGODOWNTRANSFER() {
        return USEFORGODOWNTRANSFER;
    }

    @XmlElement
    public void setUSEFORGODOWNTRANSFER(String USEFORGODOWNTRANSFER) {
        this.USEFORGODOWNTRANSFER = USEFORGODOWNTRANSFER;
    }

    public String getUSEFORCOMPOUND() {
        return USEFORCOMPOUND;
    }

    @XmlElement
    public void setUSEFORCOMPOUND(String USEFORCOMPOUND) {
        this.USEFORCOMPOUND = USEFORCOMPOUND;
    }

    public String getUSEFORSERVICETAX() {
        return USEFORSERVICETAX;
    }

    @XmlElement
    public void setUSEFORSERVICETAX(String USEFORSERVICETAX) {
        this.USEFORSERVICETAX = USEFORSERVICETAX;
    }

    public String getISEXCISEVOUCHER() {
        return ISEXCISEVOUCHER;
    }

    @XmlElement
    public void setISEXCISEVOUCHER(String ISEXCISEVOUCHER) {
        this.ISEXCISEVOUCHER = ISEXCISEVOUCHER;
    }

    public String getEXCISETAXOVERRIDE() {
        return EXCISETAXOVERRIDE;
    }

    @XmlElement
    public void setEXCISETAXOVERRIDE(String EXCISETAXOVERRIDE) {
        this.EXCISETAXOVERRIDE = EXCISETAXOVERRIDE;
    }

    public String getUSEFORTAXUNITTRANSFER() {
        return USEFORTAXUNITTRANSFER;
    }

    @XmlElement
    public void setUSEFORTAXUNITTRANSFER(String USEFORTAXUNITTRANSFER) {
        this.USEFORTAXUNITTRANSFER = USEFORTAXUNITTRANSFER;
    }

    public String getEXCISEOPENING() {
        return EXCISEOPENING;
    }

    @XmlElement
    public void setEXCISEOPENING(String EXCISEOPENING) {
        this.EXCISEOPENING = EXCISEOPENING;
    }

    public String getUSEFORFINALPRODUCTION() {
        return USEFORFINALPRODUCTION;
    }

    @XmlElement
    public void setUSEFORFINALPRODUCTION(String USEFORFINALPRODUCTION) {
        this.USEFORFINALPRODUCTION = USEFORFINALPRODUCTION;
    }

    public String getISTDSOVERRIDDEN() {
        return ISTDSOVERRIDDEN;
    }

    @XmlElement
    public void setISTDSOVERRIDDEN(String ISTDSOVERRIDDEN) {
        this.ISTDSOVERRIDDEN = ISTDSOVERRIDDEN;
    }

    public String getISTCSOVERRIDDEN() {
        return ISTCSOVERRIDDEN;
    }

    @XmlElement
    public void setISTCSOVERRIDDEN(String ISTCSOVERRIDDEN) {
        this.ISTCSOVERRIDDEN = ISTCSOVERRIDDEN;
    }

    public String getISTDSTCSCASHVCH() {
        return ISTDSTCSCASHVCH;
    }

    @XmlElement
    public void setISTDSTCSCASHVCH(String ISTDSTCSCASHVCH) {
        this.ISTDSTCSCASHVCH = ISTDSTCSCASHVCH;
    }

    public String getINCLUDEADVPYMTVCH() {
        return INCLUDEADVPYMTVCH;
    }

    @XmlElement
    public void setINCLUDEADVPYMTVCH(String INCLUDEADVPYMTVCH) {
        this.INCLUDEADVPYMTVCH = INCLUDEADVPYMTVCH;
    }

    public String getISSUBWORKSCONTRACT() {
        return ISSUBWORKSCONTRACT;
    }

    @XmlElement
    public void setISSUBWORKSCONTRACT(String ISSUBWORKSCONTRACT) {
        this.ISSUBWORKSCONTRACT = ISSUBWORKSCONTRACT;
    }

    public String getPERSISTEDVIEW() {
        return PERSISTEDVIEW;
    }

    @XmlElement
    public void setPERSISTEDVIEW(String PERSISTEDVIEW) {
        this.PERSISTEDVIEW = PERSISTEDVIEW;
    }

    public String getAUDITED() {
        return AUDITED;
    }

    @XmlElement
    public void setAUDITED(String AUDITED) {
        this.AUDITED = AUDITED;
    }

    public String getFORJOBCOSTING() {
        return FORJOBCOSTING;
    }

    @XmlElement
    public void setFORJOBCOSTING(String FORJOBCOSTING) {
        this.FORJOBCOSTING = FORJOBCOSTING;
    }

    public String getISVATOVERRIDDEN() {
        return ISVATOVERRIDDEN;
    }

    @XmlElement
    public void setISVATOVERRIDDEN(String ISVATOVERRIDDEN) {
        this.ISVATOVERRIDDEN = ISVATOVERRIDDEN;
    }

    public String getIGNOREORIGVCHDATE() {
        return IGNOREORIGVCHDATE;
    }

    @XmlElement
    public void setIGNOREORIGVCHDATE(String IGNOREORIGVCHDATE) {
        this.IGNOREORIGVCHDATE = IGNOREORIGVCHDATE;
    }

    public String getISSERVICETAXOVERRIDDEN() {
        return ISSERVICETAXOVERRIDDEN;
    }

    @XmlElement
    public void setISSERVICETAXOVERRIDDEN(String ISSERVICETAXOVERRIDDEN) {
        this.ISSERVICETAXOVERRIDDEN = ISSERVICETAXOVERRIDDEN;
    }

    public String getISISDVOUCHER() {
        return ISISDVOUCHER;
    }

    @XmlElement
    public void setISISDVOUCHER(String ISISDVOUCHER) {
        this.ISISDVOUCHER = ISISDVOUCHER;
    }

    public String getISEXCISEOVERRIDDEN() {
        return ISEXCISEOVERRIDDEN;
    }

    @XmlElement
    public void setISEXCISEOVERRIDDEN(String ISEXCISEOVERRIDDEN) {
        this.ISEXCISEOVERRIDDEN = ISEXCISEOVERRIDDEN;
    }

    public String getISEXCISESUPPLYVCH() {
        return ISEXCISESUPPLYVCH;
    }

    @XmlElement
    public void setISEXCISESUPPLYVCH(String ISEXCISESUPPLYVCH) {
        this.ISEXCISESUPPLYVCH = ISEXCISESUPPLYVCH;
    }

    public String getISGSTOVERRIDDEN() {
        return ISGSTOVERRIDDEN;
    }

    @XmlElement
    public void setISGSTOVERRIDDEN(String ISGSTOVERRIDDEN) {
        this.ISGSTOVERRIDDEN = ISGSTOVERRIDDEN;
    }

    public String getGSTNOTEXPORTED() {
        return GSTNOTEXPORTED;
    }

    @XmlElement
    public void setGSTNOTEXPORTED(String GSTNOTEXPORTED) {
        this.GSTNOTEXPORTED = GSTNOTEXPORTED;
    }

    public String getISVATPRINCIPALACCOUNT() {
        return ISVATPRINCIPALACCOUNT;
    }

    @XmlElement
    public void setISVATPRINCIPALACCOUNT(String ISVATPRINCIPALACCOUNT) {
        this.ISVATPRINCIPALACCOUNT = ISVATPRINCIPALACCOUNT;
    }

    public String getISSHIPPINGWITHINSTATE() {
        return ISSHIPPINGWITHINSTATE;
    }

    @XmlElement
    public void setISSHIPPINGWITHINSTATE(String ISSHIPPINGWITHINSTATE) {
        this.ISSHIPPINGWITHINSTATE = ISSHIPPINGWITHINSTATE;
    }

    public String getISCANCELLED() {
        return ISCANCELLED;
    }

    @XmlElement
    public void setISCANCELLED(String ISCANCELLED) {
        this.ISCANCELLED = ISCANCELLED;
    }

    public String getHASCASHFLOW() {
        return HASCASHFLOW;
    }

    @XmlElement
    public void setHASCASHFLOW(String HASCASHFLOW) {
        this.HASCASHFLOW = HASCASHFLOW;
    }

    public String getUSETRACKINGNUMBER() {
        return USETRACKINGNUMBER;
    }

    @XmlElement
    public void setUSETRACKINGNUMBER(String USETRACKINGNUMBER) {
        this.USETRACKINGNUMBER = USETRACKINGNUMBER;
    }

    public String getISINVOICE() {
        return ISINVOICE;
    }

    @XmlElement
    public void setISINVOICE(String ISINVOICE) {
        this.ISINVOICE = ISINVOICE;
    }

    public String getMFGJOURNAL() {
        return MFGJOURNAL;
    }

    @XmlElement
    public void setMFGJOURNAL(String MFGJOURNAL) {
        this.MFGJOURNAL = MFGJOURNAL;
    }

    public String getHASDISCOUNTS() {
        return HASDISCOUNTS;
    }

    @XmlElement
    public void setHASDISCOUNTS(String HASDISCOUNTS) {
        this.HASDISCOUNTS = HASDISCOUNTS;
    }

    public String getASPAYSLIP() {
        return ASPAYSLIP;
    }

    @XmlElement
    public void setASPAYSLIP(String ASPAYSLIP) {
        this.ASPAYSLIP = ASPAYSLIP;
    }

    public String getISCOSTCENTRE() {
        return ISCOSTCENTRE;
    }

    @XmlElement
    public void setISCOSTCENTRE(String ISCOSTCENTRE) {
        this.ISCOSTCENTRE = ISCOSTCENTRE;
    }

    public String getISSTXNONREALIZEDVCH() {
        return ISSTXNONREALIZEDVCH;
    }

    @XmlElement
    public void setISSTXNONREALIZEDVCH(String ISSTXNONREALIZEDVCH) {
        this.ISSTXNONREALIZEDVCH = ISSTXNONREALIZEDVCH;
    }

    public String getISEXCISEMANUFACTURERON() {
        return ISEXCISEMANUFACTURERON;
    }

    @XmlElement
    public void setISEXCISEMANUFACTURERON(String ISEXCISEMANUFACTURERON) {
        this.ISEXCISEMANUFACTURERON = ISEXCISEMANUFACTURERON;
    }

    public String getISBLANKCHEQU() {
        return ISBLANKCHEQU;
    }

    @XmlElement
    public void setISBLANKCHEQU(String ISBLANKCHEQU) {
        this.ISBLANKCHEQU = ISBLANKCHEQU;
    }

    public String getISVOID() {
        return ISVOID;
    }

    @XmlElement
    public void setISVOID(String ISVOID) {
        this.ISVOID = ISVOID;
    }

    public String getISONHOLD() {
        return ISONHOLD;
    }

    @XmlElement
    public void setISONHOLD(String ISONHOLD) {
        this.ISONHOLD = ISONHOLD;
    }

    public String getISPOSTDATED() {
        return ISPOSTDATED;
    }

    @XmlElement
    public void setISPOSTDATED(String ISPOSTDATED) {
        this.ISPOSTDATED = ISPOSTDATED;
    }

    public String getORDERLINESTATUS() {
        return ORDERLINESTATUS;
    }

    @XmlElement
    public void setORDERLINESTATUS(String ORDERLINESTATUS) {
        this.ORDERLINESTATUS = ORDERLINESTATUS;
    }

    public String getVATISAGNSTCANCSALES() {
        return VATISAGNSTCANCSALES;
    }

    @XmlElement
    public void setVATISAGNSTCANCSALES(String VATISAGNSTCANCSALES) {
        this.VATISAGNSTCANCSALES = VATISAGNSTCANCSALES;
    }

    public String getVATISPURCEXEMPTED() {
        return VATISPURCEXEMPTED;
    }

    @XmlElement
    public void setVATISPURCEXEMPTED(String VATISPURCEXEMPTED) {
        this.VATISPURCEXEMPTED = VATISPURCEXEMPTED;
    }

    public String getISVATRESTAXINVOICE() {
        return ISVATRESTAXINVOICE;
    }

    @XmlElement
    public void setISVATRESTAXINVOICE(String ISVATRESTAXINVOICE) {
        this.ISVATRESTAXINVOICE = ISVATRESTAXINVOICE;
    }

    public String getVATISASSESABLECALCVCH() {
        return VATISASSESABLECALCVCH;
    }

    @XmlElement
    public void setVATISASSESABLECALCVCH(String VATISASSESABLECALCVCH) {
        this.VATISASSESABLECALCVCH = VATISASSESABLECALCVCH;
    }

    public String getISVATDUTYPAID() {
        return ISVATDUTYPAID;
    }

    @XmlElement
    public void setISVATDUTYPAID(String ISVATDUTYPAID) {
        this.ISVATDUTYPAID = ISVATDUTYPAID;
    }

    public String getISDELIVERYSAMEASCONSIGNEE() {
        return ISDELIVERYSAMEASCONSIGNEE;
    }

    @XmlElement
    public void setISDELIVERYSAMEASCONSIGNEE(String ISDELIVERYSAMEASCONSIGNEE) {
        this.ISDELIVERYSAMEASCONSIGNEE = ISDELIVERYSAMEASCONSIGNEE;
    }

    public String getISDISPATCHSAMEASCONSIGNOR() {
        return ISDISPATCHSAMEASCONSIGNOR;
    }

    @XmlElement
    public void setISDISPATCHSAMEASCONSIGNOR(String ISDISPATCHSAMEASCONSIGNOR) {
        this.ISDISPATCHSAMEASCONSIGNOR = ISDISPATCHSAMEASCONSIGNOR;
    }

    public String getISDELETED() {
        return ISDELETED;
    }

    @XmlElement
    public void setISDELETED(String ISDELETED) {
        this.ISDELETED = ISDELETED;
    }

    public String getCHANGEVCHMODE() {
        return CHANGEVCHMODE;
    }

    @XmlElement
    public void setCHANGEVCHMODE(String CHANGEVCHMODE) {
        this.CHANGEVCHMODE = CHANGEVCHMODE;
    }

    public String getVOUCHERKEY() {
        return VOUCHERKEY;
    }

    @XmlElement
    public void setVOUCHERKEY(String VOUCHERKEY) {
        this.VOUCHERKEY = VOUCHERKEY;
    }

    public String getEXCLUDEDTAXATIONSLIST() {
        return EXCLUDEDTAXATIONSLIST;
    }

    @XmlElement(name = "EXCLUDEDTAXATIONS.LIST")
    public void setEXCLUDEDTAXATIONSLIST(String EXCLUDEDTAXATIONSLIST) {
        this.EXCLUDEDTAXATIONSLIST = EXCLUDEDTAXATIONSLIST;
    }

    public String getOLDAUDITENTRIESLIST() {
        return OLDAUDITENTRIESLIST;
    }

    @XmlElement(name = "OLDAUDITENTRIES.LIST")
    public void setOLDAUDITENTRIESLIST(String OLDAUDITENTRIESLIST) {
        this.OLDAUDITENTRIESLIST = OLDAUDITENTRIESLIST;
    }

    public String getACCOUNTAUDITENTRIESLIST() {
        return ACCOUNTAUDITENTRIESLIST;
    }

    @XmlElement(name = "ACCOUNTAUDITENTRIES.LIST")
    public void setACCOUNTAUDITENTRIESLIST(String ACCOUNTAUDITENTRIESLIST) {
        this.ACCOUNTAUDITENTRIESLIST = ACCOUNTAUDITENTRIESLIST;
    }

    public String getAUDITENTRIESLIST() {
        return AUDITENTRIESLIST;
    }

    @XmlElement(name = "AUDITENTRIES.LIST")
    public void setAUDITENTRIESLIST(String AUDITENTRIESLIST) {
        this.AUDITENTRIESLIST = AUDITENTRIESLIST;
    }

    public String getDUTYHEADDETAILSLIST() {
        return DUTYHEADDETAILSLIST;
    }

    @XmlElement(name = "DUTYHEADDETAILS.LIST")
    public void setDUTYHEADDETAILSLIST(String DUTYHEADDETAILSLIST) {
        this.DUTYHEADDETAILSLIST = DUTYHEADDETAILSLIST;
    }

    public String getSUPPLEMENTARYDUTYHEADDETAILSLIST() {
        return SUPPLEMENTARYDUTYHEADDETAILSLIST;
    }

    @XmlElement(name = "SUPPLEMENTARYDUTYHEADDETAILS.LIST")
    public void setSUPPLEMENTARYDUTYHEADDETAILSLIST(String SUPPLEMENTARYDUTYHEADDETAILSLIST) {
        this.SUPPLEMENTARYDUTYHEADDETAILSLIST = SUPPLEMENTARYDUTYHEADDETAILSLIST;
    }

    public String getINVOICEDELNOTESLIST() {
        return INVOICEDELNOTESLIST;
    }

    @XmlElement(name = "INVOICEDELNOTES.LIST")
    public void setINVOICEDELNOTESLIST(String INVOICEDELNOTESLIST) {
        this.INVOICEDELNOTESLIST = INVOICEDELNOTESLIST;
    }

    public String getINVOICEORDERLISTLIST() {
        return INVOICEORDERLISTLIST;
    }

    @XmlElement(name = "INVOICEORDERLIST.LIST")
    public void setINVOICEORDERLISTLIST(String INVOICEORDERLISTLIST) {
        this.INVOICEORDERLISTLIST = INVOICEORDERLISTLIST;
    }

    public String getINVOICEINDENTLISTLIST() {
        return INVOICEINDENTLISTLIST;
    }

    @XmlElement(name = "INVOICEINDENTLIST.LIST")
    public void setINVOICEINDENTLISTLIST(String INVOICEINDENTLISTLIST) {
        this.INVOICEINDENTLISTLIST = INVOICEINDENTLISTLIST;
    }

    public String getATTENDANCEENTRIESLIST() {
        return ATTENDANCEENTRIESLIST;
    }

    @XmlElement(name = "ATTENDANCEENTRIES.LIST")
    public void setATTENDANCEENTRIESLIST(String ATTENDANCEENTRIESLIST) {
        this.ATTENDANCEENTRIESLIST = ATTENDANCEENTRIESLIST;
    }

    public String getINVOICEEXPORTLISTLIST() {
        return INVOICEEXPORTLISTLIST;
    }

    @XmlElement(name = "ORIGINVOICEDETAILS.LIST")
    public void setINVOICEEXPORTLISTLIST(String INVOICEEXPORTLISTLIST) {
        this.INVOICEEXPORTLISTLIST = INVOICEEXPORTLISTLIST;
    }

    public String getORIGINVOICEDETAILSLIST() {
        return ORIGINVOICEDETAILSLIST;
    }

    @XmlElement(name = "INVOICEEXPORTLIST.LIST")
    public void setORIGINVOICEDETAILSLIST(String ORIGINVOICEDETAILSLIST) {
        this.ORIGINVOICEDETAILSLIST = ORIGINVOICEDETAILSLIST;
    }

    public List<ALLLEDGERENTRIESLIST> getALLLEDGERENTRIESLIST() {
        return ALLLEDGERENTRIESLIST;
    }

     @XmlElement(name = "ALLLEDGERENTRIES.LIST")
    public void setALLLEDGERENTRIESLIST(List<ALLLEDGERENTRIESLIST> ALLLEDGERENTRIESLIST) {
        this.ALLLEDGERENTRIESLIST = ALLLEDGERENTRIESLIST;
    }
}
