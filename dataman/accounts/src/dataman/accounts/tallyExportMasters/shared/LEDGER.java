package dataman.accounts.tallyExportMasters.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "LEDGER")
@XmlType(propOrder = {"NAME", "RESERVEDNAME", "ADDRESSLIST", "MAILINGNAMELIST", "STATENAME", "PARTYGSTIN", "SALESTAXNUMBER", "PRICELEVEL", "PARENT", "TAXCLASSIFICATIONNAME", "TAXTYPE", "BILLCREDITPERIOD",
    "GSTTYPE", "APPROPRIATEFOR", "SERVICECATEGORY", "EXCISELEDGERCLASSIFICATION", "EXCISEDUTYTYPE", "EXCISENATUREOFPURCHASE",
    "LEDGERFBTCATEGORY", "ISBILLWISEON", "ISCOSTCENTRESON", "ISINTERESTON", "ALLOWINMOBILE", "ISCOSTTRACKINGON",
    "ISCONDENSED", "AFFECTSSTOCK", "FORPAYROLL", "INTERESTONBILLWISE", "OVERRIDEINTEREST", "OVERRIDEADVINTEREST",
    "USEFORVAT", "IGNORETDSEXEMPT", "ISTCSAPPLICABLE", "ISTDSAPPLICABLE", "ISFBTAPPLICABLE", "ISGSTAPPLICABLE", "ISEXCISEAPPLICABLE",
    "ISTDSEXPENSE", "ISEDLIAPPLICABLE", "ISRELATEDPARTY", "USEFORESIELIGIBILITY", "SHOWINPAYSLIP", "USEFORGRATUITY", "ISTDSPROJECTED",
    "FORSERVICETAX", "ISINPUTCREDIT", "ISEXEMPTED", "ISABATEMENTAPPLICABLE", "ISSTXPARTY", "ISSTXNONREALIZEDTYPE",
    "TDSDEDUCTEEISSPECIALRATE", "AUDITED", "SORTPOSITION", "OPENINGBALANCE", "LANGUAGENAMELIST", "SLABPERIODLIST", "GRATUITYPERIODLIST", "ADDITIONALCOMPUTATIONSLIST", "BANKALLOCATIONSLIST", "PAYMENTDETAILSLIST",
    "BANKEXPORTFORMATSLIST", "BILLALLOCATIONSLIST", "INTERESTCOLLECTIONLIST", "LEDGERCLOSINGVALUESLIST", "LEDGERAUDITCLASSLIST",
    "AUDITENTRIESLIST", "TDSEXEMPTIONRULESLIST", "DEDUCTINSAMEVCHRULESLIST", "LOWERDEDUCTIONLIST", "STXABATEMENTDETAILSLIST",
    "LEDMULTIADDRESSLISTLIST", "STXTAXDETAILSLIST"})
public class LEDGER implements Serializable, IsSerializable {

    private String NAME, RESERVEDNAME;

    private ADDRESSLIST ADDRESSLIST;
    private MAILINGNAMELIST MAILINGNAMELIST;

    private String STATENAME, PARTYGSTIN, SALESTAXNUMBER, PRICELEVEL, PARENT, TAXCLASSIFICATIONNAME, TAXTYPE, BILLCREDITPERIOD,
            GSTTYPE, APPROPRIATEFOR, SERVICECATEGORY, EXCISELEDGERCLASSIFICATION, EXCISEDUTYTYPE, EXCISENATUREOFPURCHASE, LEDGERFBTCATEGORY,
            ISBILLWISEON, ISCOSTCENTRESON, ISINTERESTON, ALLOWINMOBILE, ISCOSTTRACKINGON, ISCONDENSED, AFFECTSSTOCK, FORPAYROLL, INTERESTONBILLWISE, OVERRIDEINTEREST, OVERRIDEADVINTEREST, USEFORVAT, IGNORETDSEXEMPT, ISTCSAPPLICABLE, ISTDSAPPLICABLE, ISFBTAPPLICABLE,
            ISGSTAPPLICABLE, ISEXCISEAPPLICABLE, ISTDSEXPENSE, ISEDLIAPPLICABLE, ISRELATEDPARTY, USEFORESIELIGIBILITY, SHOWINPAYSLIP,
            USEFORGRATUITY, ISTDSPROJECTED, FORSERVICETAX, ISINPUTCREDIT, ISEXEMPTED, ISABATEMENTAPPLICABLE, ISSTXPARTY, ISSTXNONREALIZEDTYPE,
            TDSDEDUCTEEISSPECIALRATE, AUDITED, SORTPOSITION, OPENINGBALANCE;

    private LANGUAGENAMELIST LANGUAGENAMELIST;

    private String SLABPERIODLIST, GRATUITYPERIODLIST, ADDITIONALCOMPUTATIONSLIST, BANKALLOCATIONSLIST, PAYMENTDETAILSLIST,
            BANKEXPORTFORMATSLIST, BILLALLOCATIONSLIST, INTERESTCOLLECTIONLIST, LEDGERCLOSINGVALUESLIST, LEDGERAUDITCLASSLIST,
            AUDITENTRIESLIST, TDSEXEMPTIONRULESLIST, DEDUCTINSAMEVCHRULESLIST, LOWERDEDUCTIONLIST, STXABATEMENTDETAILSLIST,
            LEDMULTIADDRESSLISTLIST, STXTAXDETAILSLIST;

    public String getNAME() {
        return NAME;
    }

    @XmlAttribute(name="NAME")
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getRESERVEDNAME() {
        return RESERVEDNAME;
    }

    @XmlAttribute(name="RESERVEDNAME")
    public void setRESERVEDNAME(String RESERVEDNAME) {
        this.RESERVEDNAME = RESERVEDNAME;
    }

    public ADDRESSLIST getADDRESSLIST() {
        return ADDRESSLIST;
    }

    @XmlElement(name = "ADDRESS.LIST")
    public void setADDRESSLIST(ADDRESSLIST ADDRESSLIST) {
        this.ADDRESSLIST = ADDRESSLIST;
    }

    public MAILINGNAMELIST getMAILINGNAMELIST() {
        return MAILINGNAMELIST;
    }

    @XmlElement(name = "MAILINGNAME.LIST")
    public void setMAILINGNAMELIST(MAILINGNAMELIST MAILINGNAMELIST) {
        this.MAILINGNAMELIST = MAILINGNAMELIST;
    }

    public String getSTATENAME() {
        return STATENAME;
    }

    @XmlElement
    public void setSTATENAME(String STATENAME) {
        this.STATENAME = STATENAME;
    }

    public String getPARTYGSTIN() {
        return PARTYGSTIN;
    }

    @XmlElement
    public void setPARTYGSTIN(String PARTYGSTIN) {
        this.PARTYGSTIN = PARTYGSTIN;
    }

    public String getSALESTAXNUMBER() {
        return SALESTAXNUMBER;
    }

    @XmlElement
    public void setSALESTAXNUMBER(String SALESTAXNUMBER) {
        this.SALESTAXNUMBER = SALESTAXNUMBER;
    }

    public String getPRICELEVEL() {
        return PRICELEVEL;
    }

    @XmlElement
    public void setPRICELEVEL(String PRICELEVEL) {
        this.PRICELEVEL = PRICELEVEL;
    }

    public String getPARENT() {
        return PARENT;
    }

    @XmlElement
    public void setPARENT(String PARENT) {
        this.PARENT = PARENT;
    }

    public String getTAXCLASSIFICATIONNAME() {
        return TAXCLASSIFICATIONNAME;
    }

    @XmlElement
    public void setTAXCLASSIFICATIONNAME(String TAXCLASSIFICATIONNAME) {
        this.TAXCLASSIFICATIONNAME = TAXCLASSIFICATIONNAME;
    }

    public String getTAXTYPE() {
        return TAXTYPE;
    }

    @XmlElement
    public void setTAXTYPE(String TAXTYPE) {
        this.TAXTYPE = TAXTYPE;
    }

    public String getBILLCREDITPERIOD() {
        return BILLCREDITPERIOD;
    }

    @XmlElement
    public void setBILLCREDITPERIOD(String BILLCREDITPERIOD) {
        this.BILLCREDITPERIOD = BILLCREDITPERIOD;
    }

    public String getGSTTYPE() {
        return GSTTYPE;
    }

    @XmlElement
    public void setGSTTYPE(String GSTTYPE) {
        this.GSTTYPE = GSTTYPE;
    }

    public String getAPPROPRIATEFOR() {
        return APPROPRIATEFOR;
    }

    @XmlElement
    public void setAPPROPRIATEFOR(String APPROPRIATEFOR) {
        this.APPROPRIATEFOR = APPROPRIATEFOR;
    }

    public String getSERVICECATEGORY() {
        return SERVICECATEGORY;
    }

    @XmlElement
    public void setSERVICECATEGORY(String SERVICECATEGORY) {
        this.SERVICECATEGORY = SERVICECATEGORY;
    }

    public String getEXCISELEDGERCLASSIFICATION() {
        return EXCISELEDGERCLASSIFICATION;
    }

    @XmlElement
    public void setEXCISELEDGERCLASSIFICATION(String EXCISELEDGERCLASSIFICATION) {
        this.EXCISELEDGERCLASSIFICATION = EXCISELEDGERCLASSIFICATION;
    }

    public String getEXCISEDUTYTYPE() {
        return EXCISEDUTYTYPE;
    }

    @XmlElement
    public void setEXCISEDUTYTYPE(String EXCISEDUTYTYPE) {
        this.EXCISEDUTYTYPE = EXCISEDUTYTYPE;
    }

    public String getEXCISENATUREOFPURCHASE() {
        return EXCISENATUREOFPURCHASE;
    }

    @XmlElement
    public void setEXCISENATUREOFPURCHASE(String EXCISENATUREOFPURCHASE) {
        this.EXCISENATUREOFPURCHASE = EXCISENATUREOFPURCHASE;
    }

    public String getLEDGERFBTCATEGORY() {
        return LEDGERFBTCATEGORY;
    }

    @XmlElement
    public void setLEDGERFBTCATEGORY(String LEDGERFBTCATEGORY) {
        this.LEDGERFBTCATEGORY = LEDGERFBTCATEGORY;
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

    public String getISINTERESTON() {
        return ISINTERESTON;
    }

    @XmlElement
    public void setISINTERESTON(String ISINTERESTON) {
        this.ISINTERESTON = ISINTERESTON;
    }

    public String getALLOWINMOBILE() {
        return ALLOWINMOBILE;
    }

    @XmlElement
    public void setALLOWINMOBILE(String ALLOWINMOBILE) {
        this.ALLOWINMOBILE = ALLOWINMOBILE;
    }

    public String getISCOSTTRACKINGON() {
        return ISCOSTTRACKINGON;
    }

    @XmlElement
    public void setISCOSTTRACKINGON(String ISCOSTTRACKINGON) {
        this.ISCOSTTRACKINGON = ISCOSTTRACKINGON;
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

    public String getFORPAYROLL() {
        return FORPAYROLL;
    }

    @XmlElement
    public void setFORPAYROLL(String FORPAYROLL) {
        this.FORPAYROLL = FORPAYROLL;
    }

    public String getINTERESTONBILLWISE() {
        return INTERESTONBILLWISE;
    }

    @XmlElement
    public void setINTERESTONBILLWISE(String INTERESTONBILLWISE) {
        this.INTERESTONBILLWISE = INTERESTONBILLWISE;
    }

    public String getOVERRIDEINTEREST() {
        return OVERRIDEINTEREST;
    }

    @XmlElement
    public void setOVERRIDEINTEREST(String OVERRIDEINTEREST) {
        this.OVERRIDEINTEREST = OVERRIDEINTEREST;
    }

    public String getOVERRIDEADVINTEREST() {
        return OVERRIDEADVINTEREST;
    }

    @XmlElement
    public void setOVERRIDEADVINTEREST(String OVERRIDEADVINTEREST) {
        this.OVERRIDEADVINTEREST = OVERRIDEADVINTEREST;
    }

    public String getUSEFORVAT() {
        return USEFORVAT;
    }

    @XmlElement
    public void setUSEFORVAT(String USEFORVAT) {
        this.USEFORVAT = USEFORVAT;
    }

    public String getIGNORETDSEXEMPT() {
        return IGNORETDSEXEMPT;
    }

    @XmlElement
    public void setIGNORETDSEXEMPT(String IGNORETDSEXEMPT) {
        this.IGNORETDSEXEMPT = IGNORETDSEXEMPT;
    }

    public String getISTCSAPPLICABLE() {
        return ISTCSAPPLICABLE;
    }

    @XmlElement
    public void setISTCSAPPLICABLE(String ISTCSAPPLICABLE) {
        this.ISTCSAPPLICABLE = ISTCSAPPLICABLE;
    }

    public String getISTDSAPPLICABLE() {
        return ISTDSAPPLICABLE;
    }

    @XmlElement
    public void setISTDSAPPLICABLE(String ISTDSAPPLICABLE) {
        this.ISTDSAPPLICABLE = ISTDSAPPLICABLE;
    }

    public String getISFBTAPPLICABLE() {
        return ISFBTAPPLICABLE;
    }

    @XmlElement
    public void setISFBTAPPLICABLE(String ISFBTAPPLICABLE) {
        this.ISFBTAPPLICABLE = ISFBTAPPLICABLE;
    }

    public String getISGSTAPPLICABLE() {
        return ISGSTAPPLICABLE;
    }

    @XmlElement
    public void setISGSTAPPLICABLE(String ISGSTAPPLICABLE) {
        this.ISGSTAPPLICABLE = ISGSTAPPLICABLE;
    }

    public String getISEXCISEAPPLICABLE() {
        return ISEXCISEAPPLICABLE;
    }

    @XmlElement
    public void setISEXCISEAPPLICABLE(String ISEXCISEAPPLICABLE) {
        this.ISEXCISEAPPLICABLE = ISEXCISEAPPLICABLE;
    }

    public String getISTDSEXPENSE() {
        return ISTDSEXPENSE;
    }

    @XmlElement
    public void setISTDSEXPENSE(String ISTDSEXPENSE) {
        this.ISTDSEXPENSE = ISTDSEXPENSE;
    }

    public String getISEDLIAPPLICABLE() {
        return ISEDLIAPPLICABLE;
    }

    @XmlElement
    public void setISEDLIAPPLICABLE(String ISEDLIAPPLICABLE) {
        this.ISEDLIAPPLICABLE = ISEDLIAPPLICABLE;
    }

    public String getISRELATEDPARTY() {
        return ISRELATEDPARTY;
    }

    @XmlElement
    public void setISRELATEDPARTY(String ISRELATEDPARTY) {
        this.ISRELATEDPARTY = ISRELATEDPARTY;
    }

    public String getUSEFORESIELIGIBILITY() {
        return USEFORESIELIGIBILITY;
    }

    @XmlElement
    public void setUSEFORESIELIGIBILITY(String USEFORESIELIGIBILITY) {
        this.USEFORESIELIGIBILITY = USEFORESIELIGIBILITY;
    }

    public String getSHOWINPAYSLIP() {
        return SHOWINPAYSLIP;
    }

    @XmlElement
    public void setSHOWINPAYSLIP(String SHOWINPAYSLIP) {
        this.SHOWINPAYSLIP = SHOWINPAYSLIP;
    }

    public String getUSEFORGRATUITY() {
        return USEFORGRATUITY;
    }

    @XmlElement
    public void setUSEFORGRATUITY(String USEFORGRATUITY) {
        this.USEFORGRATUITY = USEFORGRATUITY;
    }

    public String getISTDSPROJECTED() {
        return ISTDSPROJECTED;
    }

    @XmlElement
    public void setISTDSPROJECTED(String ISTDSPROJECTED) {
        this.ISTDSPROJECTED = ISTDSPROJECTED;
    }

    public String getFORSERVICETAX() {
        return FORSERVICETAX;
    }

    @XmlElement
    public void setFORSERVICETAX(String FORSERVICETAX) {
        this.FORSERVICETAX = FORSERVICETAX;
    }

    public String getISINPUTCREDIT() {
        return ISINPUTCREDIT;
    }

    @XmlElement
    public void setISINPUTCREDIT(String ISINPUTCREDIT) {
        this.ISINPUTCREDIT = ISINPUTCREDIT;
    }

    public String getISEXEMPTED() {
        return ISEXEMPTED;
    }

    @XmlElement
    public void setISEXEMPTED(String ISEXEMPTED) {
        this.ISEXEMPTED = ISEXEMPTED;
    }

    public String getISABATEMENTAPPLICABLE() {
        return ISABATEMENTAPPLICABLE;
    }

    @XmlElement
    public void setISABATEMENTAPPLICABLE(String ISABATEMENTAPPLICABLE) {
        this.ISABATEMENTAPPLICABLE = ISABATEMENTAPPLICABLE;
    }

    public String getISSTXPARTY() {
        return ISSTXPARTY;
    }

    @XmlElement
    public void setISSTXPARTY(String ISSTXPARTY) {
        this.ISSTXPARTY = ISSTXPARTY;
    }

    public String getISSTXNONREALIZEDTYPE() {
        return ISSTXNONREALIZEDTYPE;
    }

    @XmlElement
    public void setISSTXNONREALIZEDTYPE(String ISSTXNONREALIZEDTYPE) {
        this.ISSTXNONREALIZEDTYPE = ISSTXNONREALIZEDTYPE;
    }

    public String getTDSDEDUCTEEISSPECIALRATE() {
        return TDSDEDUCTEEISSPECIALRATE;
    }

    @XmlElement
    public void setTDSDEDUCTEEISSPECIALRATE(String TDSDEDUCTEEISSPECIALRATE) {
        this.TDSDEDUCTEEISSPECIALRATE = TDSDEDUCTEEISSPECIALRATE;
    }

    public String getAUDITED() {
        return AUDITED;
    }

    @XmlElement
    public void setAUDITED(String AUDITED) {
        this.AUDITED = AUDITED;
    }

    public String getSORTPOSITION() {
        return SORTPOSITION;
    }

    @XmlElement
    public void setSORTPOSITION(String SORTPOSITION) {
        this.SORTPOSITION = SORTPOSITION;
    }

    public String getOPENINGBALANCE() {
        return OPENINGBALANCE;
    }

    @XmlElement
    public void setOPENINGBALANCE(String OPENINGBALANCE) {
        this.OPENINGBALANCE = OPENINGBALANCE;
    }

    public LANGUAGENAMELIST getLANGUAGENAMELIST() {
        return LANGUAGENAMELIST;
    }

    @XmlElement(name = "LANGUAGENAME.LIST")
    public void setLANGUAGENAMELIST(LANGUAGENAMELIST LANGUAGENAMELIST) {
        this.LANGUAGENAMELIST = LANGUAGENAMELIST;
    }

    public String getSLABPERIODLIST() {
        return SLABPERIODLIST;
    }

    @XmlElement(name = "SLABPERIOD.LIST")
    public void setSLABPERIODLIST(String SLABPERIODLIST) {
        this.SLABPERIODLIST = SLABPERIODLIST;
    }

    public String getGRATUITYPERIODLIST() {
        return GRATUITYPERIODLIST;
    }

    @XmlElement(name = "GRATUITYPERIOD.LIST")
    public void setGRATUITYPERIODLIST(String GRATUITYPERIODLIST) {
        this.GRATUITYPERIODLIST = GRATUITYPERIODLIST;
    }

    public String getADDITIONALCOMPUTATIONSLIST() {
        return ADDITIONALCOMPUTATIONSLIST;
    }

    @XmlElement(name = "ADDITIONALCOMPUTATIONS.LIST")
    public void setADDITIONALCOMPUTATIONSLIST(String ADDITIONALCOMPUTATIONSLIST) {
        this.ADDITIONALCOMPUTATIONSLIST = ADDITIONALCOMPUTATIONSLIST;
    }

    public String getBANKALLOCATIONSLIST() {
        return BANKALLOCATIONSLIST;
    }

    @XmlElement(name = "BANKALLOCATIONS.LIST")
    public void setBANKALLOCATIONSLIST(String BANKALLOCATIONSLIST) {
        this.BANKALLOCATIONSLIST = BANKALLOCATIONSLIST;
    }

    public String getPAYMENTDETAILSLIST() {
        return PAYMENTDETAILSLIST;
    }

    @XmlElement(name = "PAYMENTDETAILS.LIST")
    public void setPAYMENTDETAILSLIST(String PAYMENTDETAILSLIST) {
        this.PAYMENTDETAILSLIST = PAYMENTDETAILSLIST;
    }

    public String getBANKEXPORTFORMATSLIST() {
        return BANKEXPORTFORMATSLIST;
    }

    @XmlElement(name = "BANKEXPORTFORMATS.LIST")
    public void setBANKEXPORTFORMATSLIST(String BANKEXPORTFORMATSLIST) {
        this.BANKEXPORTFORMATSLIST = BANKEXPORTFORMATSLIST;
    }

    public String getBILLALLOCATIONSLIST() {
        return BILLALLOCATIONSLIST;
    }

    @XmlElement(name = "BILLALLOCATIONS.LIST")
    public void setBILLALLOCATIONSLIST(String BILLALLOCATIONSLIST) {
        this.BILLALLOCATIONSLIST = BILLALLOCATIONSLIST;
    }

    public String getINTERESTCOLLECTIONLIST() {
        return INTERESTCOLLECTIONLIST;
    }

    @XmlElement(name = "INTERESTCOLLECTION.LIST")
    public void setINTERESTCOLLECTIONLIST(String INTERESTCOLLECTIONLIST) {
        this.INTERESTCOLLECTIONLIST = INTERESTCOLLECTIONLIST;
    }

    public String getLEDGERCLOSINGVALUESLIST() {
        return LEDGERCLOSINGVALUESLIST;
    }

    @XmlElement(name = "LEDGERCLOSINGVALUES.LIST")
    public void setLEDGERCLOSINGVALUESLIST(String LEDGERCLOSINGVALUESLIST) {
        this.LEDGERCLOSINGVALUESLIST = LEDGERCLOSINGVALUESLIST;
    }

    public String getLEDGERAUDITCLASSLIST() {
        return LEDGERAUDITCLASSLIST;
    }

    @XmlElement(name = "LEDGERAUDITCLASS.LIST")
    public void setLEDGERAUDITCLASSLIST(String LEDGERAUDITCLASSLIST) {
        this.LEDGERAUDITCLASSLIST = LEDGERAUDITCLASSLIST;
    }

    public String getAUDITENTRIESLIST() {
        return AUDITENTRIESLIST;
    }

    @XmlElement(name = "AUDITENTRIES.LIST")
    public void setAUDITENTRIESLIST(String AUDITENTRIESLIST) {
        this.AUDITENTRIESLIST = AUDITENTRIESLIST;
    }

    public String getTDSEXEMPTIONRULESLIST() {
        return TDSEXEMPTIONRULESLIST;
    }

    @XmlElement(name = "TDSEXEMPTIONRULES.LIST")
    public void setTDSEXEMPTIONRULESLIST(String TDSEXEMPTIONRULESLIST) {
        this.TDSEXEMPTIONRULESLIST = TDSEXEMPTIONRULESLIST;
    }

    public String getDEDUCTINSAMEVCHRULESLIST() {
        return DEDUCTINSAMEVCHRULESLIST;
    }

    @XmlElement(name = "DEDUCTINSAMEVCHRULES.LIST")
    public void setDEDUCTINSAMEVCHRULESLIST(String DEDUCTINSAMEVCHRULESLIST) {
        this.DEDUCTINSAMEVCHRULESLIST = DEDUCTINSAMEVCHRULESLIST;
    }

    public String getLOWERDEDUCTIONLIST() {
        return LOWERDEDUCTIONLIST;
    }

    @XmlElement(name = "LOWERDEDUCTION.LIST")
    public void setLOWERDEDUCTIONLIST(String LOWERDEDUCTIONLIST) {
        this.LOWERDEDUCTIONLIST = LOWERDEDUCTIONLIST;
    }

    public String getSTXABATEMENTDETAILSLIST() {
        return STXABATEMENTDETAILSLIST;
    }

    @XmlElement(name = "STXABATEMENTDETAILS.LIST")
    public void setSTXABATEMENTDETAILSLIST(String STXABATEMENTDETAILSLIST) {
        this.STXABATEMENTDETAILSLIST = STXABATEMENTDETAILSLIST;
    }

    public String getLEDMULTIADDRESSLISTLIST() {
        return LEDMULTIADDRESSLISTLIST;
    }

    @XmlElement(name = "LEDMULTIADDRESSLIST.LIST")
    public void setLEDMULTIADDRESSLISTLIST(String LEDMULTIADDRESSLISTLIST) {
        this.LEDMULTIADDRESSLISTLIST = LEDMULTIADDRESSLISTLIST;
    }

    public String getSTXTAXDETAILSLIST() {
        return STXTAXDETAILSLIST;
    }

    @XmlElement(name = "STXTAXDETAILS.LIST")
    public void setSTXTAXDETAILSLIST(String STXTAXDETAILSLIST) {
        this.STXTAXDETAILSLIST = STXTAXDETAILSLIST;
    }
}
