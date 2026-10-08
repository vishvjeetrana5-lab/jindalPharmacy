package dataman.accounts.tallyExportVouchers.server;

import dataman.accounts.tallyExportVouchers.shared.ALLLEDGERENTRIESLIST;
import dataman.accounts.tallyExportVouchers.shared.ENVELOPE;
import dataman.accounts.tallyExportVouchers.shared.Filter_Mdl;
import dataman.accounts.tallyExportVouchers.shared.BILLALLOCATIONSLIST;
import dataman.accounts.tallyExportVouchers.shared.OLDAUDITENTRYIDS;
import dataman.accounts.tallyExportVouchers.shared.TallyExportVouchers_Srv;
import dataman.accounts.tallyExportVouchers.shared.TALLYMESSAGE;
import dataman.accounts.tallyExportVouchers.shared.REQUESTDATA;
import dataman.accounts.tallyExportVouchers.shared.REMOTECMPINFOLIST;
import dataman.accounts.tallyExportVouchers.shared.REQUESTDESC;
import dataman.accounts.tallyExportVouchers.shared.IMPORTDATA;
import dataman.accounts.tallyExportVouchers.shared.COMPANY;
import dataman.accounts.tallyExportVouchers.shared.BODY;
import dataman.accounts.tallyExportVouchers.shared.OLDAUDITENTRYIDSLIST;
import dataman.accounts.tallyExportVouchers.shared.VOUCHER;
import dataman.accounts.tallyExportVouchers.shared.STATICVARIABLES;
import dataman.accounts.tallyExportVouchers.shared.HEADER;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class TallyExportVouchers_SrvImpl extends DmRSSPrivate implements TallyExportVouchers_Srv {

    boolean isItemRequiredInCsv = false;

    @Override
    public DmResult<String[]> exportAcVouchersToTallyXML(Filter_Mdl filter) {
        DmResult<String[]> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp2 = null;
        ResultSet rsTemp2 = null;
        String tempValue = "", directoryPath, fullFilePath;

        String result[] = {"", ""};

        File tempFile = null;
        dmrTemp.setData(result);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================< Validate Filter >==============================*/
            if (DmShared.xNull(filter.getSessionId()).trim().equals("")) {
                throw new UnsupportedOperationException("Invalid Session ID...");
            }
            if (DmShared.dNull(filter.getFromDate()).trim().equals("")) {
                throw new UnsupportedOperationException("From Date is mandatory...");
            }
            if (DmShared.dNull(filter.getToDate()).trim().equals("")) {
                throw new UnsupportedOperationException("To Date is mandatory...");
            }

            /*=================================< Set file path >=================================*/
            tempValue = "AllVouchers.xml";
            directoryPath = DmServer.Path.sessions + File.separatorChar + filter.getSessionId() + File.separatorChar + "TALLY_Export_XML";
            fullFilePath = directoryPath + File.separatorChar + tempValue;

            /*===========================< Make directory if not exits >===========================*/
            tempFile = new File(directoryPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            tempFile = null;

            /*===========================< Delete if file already exist >===========================*/
            tempFile = new File(fullFilePath);
            if (tempFile.exists()) {
                tempFile.delete();
            }
            tempFile = null;

            File file = new File(fullFilePath);
            JAXBContext jaxbContext = JAXBContext.newInstance(ENVELOPE.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            //========================Start XML=====================
            ENVELOPE envelope = new ENVELOPE();
            HEADER header = new HEADER();
            header.setTALLYREQUEST("Import Data");
            envelope.setHEADER(header);

            BODY body = new BODY();
            IMPORTDATA importData = new IMPORTDATA();
            REQUESTDESC requestdesc = new REQUESTDESC();
            requestdesc.setREPORTNAME("Vouchers");
            STATICVARIABLES staticVariables = new STATICVARIABLES();
            staticVariables.setSVCURRENTCOMPANY(DmServer.getSsnSiteDisplayName(this));
            requestdesc.setSTATICVARIABLES(staticVariables);

            REQUESTDATA requestData = new REQUESTDATA();

            List<TALLYMESSAGE> root = new ArrayList<TALLYMESSAGE>();

            createAcMasterNode(root, cnTemp, rsTemp, psTemp, rsTemp2, psTemp2, filter);

            requestData.setTALLYMESSAGE(root);

            REMOTECMPINFOLIST remoteCmpInfoList = new REMOTECMPINFOLIST();
            remoteCmpInfoList.setMERGE("Yes");
            remoteCmpInfoList.setNAME("13989872-e0e9-4ba7-894d-988154b46abf");
            remoteCmpInfoList.setREMOTECMPNAME(DmServer.getSsnSiteDisplayName(this));
            remoteCmpInfoList.setREMOTECMPSTATE(DmServer.getSsnSiteStateNumericCode(this));
            COMPANY company = new COMPANY();
            company.setREMOTECMPINFOLIST(remoteCmpInfoList);
            requestData.setCOMPANY(company);
            importData.setREQUESTDATA(requestData);
            importData.setREQUESTDESC(requestdesc);
            body.setIMPORTDATA(importData);
            envelope.setBODY(body);
            jaxbMarshaller.marshal(envelope, file);
            //========================END XML=====================
            result[0] = directoryPath;
            result[1] = tempValue;

            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                    rsTemp2.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                    psTemp2.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }

            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private void createAcMasterNode(List<TALLYMESSAGE> root, Connection cnTemp, ResultSet rsTemp, DmPreparedStatement psTemp, ResultSet rsTemp2, DmPreparedStatement psTemp2, Filter_Mdl filter) {

        TALLYMESSAGE tallyMassage = new TALLYMESSAGE();
        String sql = "", PBillNumber = "";
        try {
            sql = "Select lg.recId As BillNo,    "
                    + " LG.Docid, MAx(LG.RecID) As RecID, Max(LG.Narration) As Narration, Max(LG.V_Date) As V_Date,      "
                    + " Case WHEN Vt.Category IN ('CPV','PMT') THEN 'Payment'  "
                    + "      WHEN Vt.Category IN ('CRV','RCT') THEN 'Receipt'   "
                    + "      WHEN Vt.Category IN ('CNTRA') THEN 'Contra' "
                    + "     WHEN Vt.Category IN ('PHPBD','PHPDU','PHPBL','PHBDU','PHPBR','PHPRU','PHCHL','PHCHU','PBIL','PBILU','PBILD','PBLDU','PCHL','PCHLU','PRTN','PRTNU')"
                    + "  THEN 'Purchase' WHEN Vt.Category IN ('PHBIL','PHBRT') THEN 'Sales' Else 'Journal' End as VouchType,     "
                    + "Replace(convert(VARCHAR,MAx(LG.V_Date),102),'.','') as mDate ,  MAx(Sg.Name) as Party,  MAx(Sg.subcode) as Partycode ,    "
                    + "ROW_NUMBER() OVER (ORDER BY Max(LG.V_Date), VT.V_Type, LG.RecID DESC) AS RowID ,     "
                    + "Max(LG.Site_Code) As Site_Code,Max(LG.V_TYpe) As V_TYpe    "
                    + "From Ledger LG      "
                    + "LEFT JOIN Voucher_Type Vt ON Vt.V_Type = LG.V_Type     "
                    + "LEFT JOIN SubGroup Sg ON Sg.SubCode = LG.SubCode      "
                    + "Where LG.V_Date between :fromDate and :toDate  and      "
                    + "LG.Site_Code = :siteCode  "
                    + "And LG.isPosted= 1  "
                    + "Group By LG.DocID,VT.CateGory, VT.V_Type, LG.RecID,LG.V_Prefix    "
                    + "Order By V_Date, V_Type, RecID  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.getFromDate()));
            psTemp.setDate("toDate", DmServer.convertDate(filter.getToDate()));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            // psTemp.setString("VPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                tallyMassage = new TALLYMESSAGE();

                tallyMassage.setXmlns("TallyUDF");

                VOUCHER voucher = new VOUCHER();

                voucher.setREMOTEID(DmShared.xNull(rsTemp.getString("recID")) + "-" + DmShared.xNull(rsTemp.getString("RowID")));
                voucher.setVCHKEY(DmShared.xNull(rsTemp.getString("recID")) + ":00" + DmShared.xNull(rsTemp.getString("RowID")));
                voucher.setVCHTYPE(DmShared.xNull(rsTemp.getString("VouchType")));
                voucher.setACTION("Create");
                voucher.setOBJVIEW("Accounting Voucher View");

                OLDAUDITENTRYIDS oldEntryIDS = new OLDAUDITENTRYIDS();
                oldEntryIDS.setTYPE("Number");
                oldEntryIDS.setOLDAUDITENTRYIDS("-1");

                voucher.setOLDAUDITENTRYIDS(oldEntryIDS);
                voucher.setDATE(DmShared.xNull(rsTemp.getString("mDate")));
                voucher.setNARRATION(DmShared.xNull(rsTemp.getString("Narration")));
                voucher.setVOUCHERTYPENAME(DmShared.xNull(rsTemp.getString("VouchType")));

                String reffrence = DmServer.getCodeViaManualCode(DmShared.xNull(rsTemp.getString("Docid")), "docId", "pBill", "partyBillNo", cnTemp);

                voucher.setREFERENCE(reffrence);

                voucher.setVOUCHERNUMBER(DmShared.xNull(rsTemp.getString("recID")));
                PBillNumber = DmShared.xNull(rsTemp.getString("recID"));
                voucher.setPARTYLEDGERNAME(DmShared.xNull(rsTemp.getString("Party")));
                voucher.setCSTFORMISSUETYPE("");
                voucher.setCSTFORMRECVTYPE("");
                voucher.setFBTPAYMENTTYPE("Default");
                voucher.setPERSISTEDVIEW("Accounting Voucher View");
                voucher.setVCHGSTCLASS("");
                voucher.setENTEREDBY("SA");
                voucher.setDIFFACTUALQTY("NO");
                voucher.setISMSTFROMSYNC("NO");
                voucher.setASORIGINAL("NO");
                voucher.setFORJOBCOSTING("NO");
                voucher.setISOPTIONAL("NO");
                voucher.setEFFECTIVEDATE(DmShared.xNull(rsTemp.getString("mDate")));
                voucher.setUSEFOREXCISE("NO");
                voucher.setISFORJOBWORKIN("NO");
                voucher.setALLOWCONSUMPTION("NO");
                voucher.setUSEFORINTEREST("NO");
                voucher.setUSEFORGAINLOSS("NO");
                voucher.setUSEFORGODOWNTRANSFER("NO");
                voucher.setUSEFORCOMPOUND("NO");
                voucher.setUSEFORSERVICETAX("NO");
                voucher.setISEXCISEVOUCHER("NO");
                voucher.setEXCISETAXOVERRIDE("NO");
                voucher.setUSEFORTAXUNITTRANSFER("NO");
                voucher.setEXCISEOPENING("NO");
                voucher.setUSEFORFINALPRODUCTION("NO");
                voucher.setISTDSOVERRIDDEN("NO");
                voucher.setISTCSOVERRIDDEN("NO");
                voucher.setISTDSTCSCASHVCH("NO");
                voucher.setINCLUDEADVPYMTVCH("NO");
                voucher.setISSUBWORKSCONTRACT("NO");
                voucher.setISVATOVERRIDDEN("NO");

                //============================
                voucher.setIGNOREORIGVCHDATE("NO");
                voucher.setISSERVICETAXOVERRIDDEN("NO");
                voucher.setISISDVOUCHER("NO");
                voucher.setISEXCISEOVERRIDDEN("NO");
                voucher.setISEXCISESUPPLYVCH("NO");
                voucher.setISGSTOVERRIDDEN("NO");
                voucher.setGSTNOTEXPORTED("NO");
                voucher.setISVATPRINCIPALACCOUNT("NO");
                voucher.setISSHIPPINGWITHINSTATE("NO");
                voucher.setISCANCELLED("NO");
                voucher.setHASCASHFLOW("NO");
                voucher.setISPOSTDATED("NO");
                voucher.setUSETRACKINGNUMBER("NO");
                voucher.setISINVOICE("NO");
                voucher.setMFGJOURNAL("NO");
                voucher.setHASDISCOUNTS("NO");
                voucher.setASPAYSLIP("NO");
                voucher.setISCOSTCENTRE("NO");
                voucher.setISSTXNONREALIZEDVCH("NO");
                voucher.setISEXCISEMANUFACTURERON("NO");
                voucher.setISBLANKCHEQU("NO");
                voucher.setISVOID("NO");
                voucher.setISONHOLD("NO");

                //===========================
                voucher.setORDERLINESTATUS("NO");
                voucher.setVATISAGNSTCANCSALES("NO");
                voucher.setVATISPURCEXEMPTED("NO");
                voucher.setISVATRESTAXINVOICE("NO");
                voucher.setVATISASSESABLECALCVCH("NO");
                voucher.setISVATDUTYPAID("NO");
                voucher.setISDELIVERYSAMEASCONSIGNEE("NO");
                voucher.setISDISPATCHSAMEASCONSIGNOR("NO");
                voucher.setISDELETED("NO");
                voucher.setCHANGEVCHMODE("NO");
                voucher.setVOUCHERKEY("");
                //=========================== 

                voucher.setEXCLUDEDTAXATIONSLIST(addSpaceChar(6));
                voucher.setOLDAUDITENTRIESLIST(addSpaceChar(6));
                voucher.setACCOUNTAUDITENTRIESLIST(addSpaceChar(6));
                voucher.setAUDITENTRIESLIST(addSpaceChar(6));
                voucher.setDUTYHEADDETAILSLIST(addSpaceChar(6));
                voucher.setSUPPLEMENTARYDUTYHEADDETAILSLIST(addSpaceChar(6));
                voucher.setINVOICEDELNOTESLIST(addSpaceChar(6));
                voucher.setINVOICEORDERLISTLIST(addSpaceChar(6));
                voucher.setINVOICEINDENTLISTLIST(addSpaceChar(6));
                voucher.setATTENDANCEENTRIESLIST(addSpaceChar(6));
                voucher.setORIGINVOICEDETAILSLIST(addSpaceChar(6));
                voucher.setINVOICEEXPORTLISTLIST(addSpaceChar(6));

                //===================== Start createNodeAllLedger=================================
                voucher.setALLLEDGERENTRIESLIST(createLedgerNodeList(cnTemp, rsTemp, psTemp, rsTemp2, psTemp2, filter, PBillNumber));
                //===================== End createNodeAllLedger===================================

                tallyMassage.setVOUCHER(voucher);

                root.add(tallyMassage);

            }

        } catch (Exception ex) {
            throw new UnsupportedOperationException("Error in AcMasterNode Creation");
        }

    }

    private List<ALLLEDGERENTRIESLIST> createLedgerNodeList(Connection cnTemp, ResultSet rsTemp, DmPreparedStatement psTemp, ResultSet rsTemp2, DmPreparedStatement psTemp2, Filter_Mdl filter, String PBillNumber) {

        List<ALLLEDGERENTRIESLIST> ledgerRoot = new ArrayList<>();
        ALLLEDGERENTRIESLIST ledgerRootElement = new ALLLEDGERENTRIESLIST();
        String sql = "", mDrCrTyp = "";
        double mDrCr = 0d, mAmtCr = 0d, mAmtDr = 0d;

        try {

            sql = "Select L.DocID,L.AmtDr,L.AmtCr, m.Name, LM.Narration as NarrationNew, 0 as Amt    "
                    + "From Ledger L    "
                    + "Left Join Subgroup M On L.Subcode=M.Subcode    "
                    + "Left Join LedgerM LM on L.Docid=LM.Docid    "
                    + "LEFT JOIN Voucher_Type Vt ON Vt.V_Type = L.V_Type "
                    + "where L.V_Date between :fromDate and :todate and L.isPosted=1   and L.site_Code= :siteCode and l.docid= :lglocid ";

            psTemp2 = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp2.setDate("fromDate", DmServer.convertDate(filter.getFromDate()));
            psTemp2.setDate("toDate", DmServer.convertDate(filter.getToDate()));
            psTemp2.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp2.setString("lglocid", rsTemp.getString("Docid"));
            rsTemp2 = psTemp2.executeQuery();
            while (rsTemp2.next()) {

                ledgerRootElement = new ALLLEDGERENTRIESLIST();

                mAmtCr = DmShared.vNull(rsTemp2.getString("AmtCr"));
                mAmtDr = DmShared.vNull(rsTemp2.getString("AmtDr"));

                if (DmShared.vNull(rsTemp2.getString("AmtCr")) > 0) {
                    mAmtCr = DmShared.vNull(rsTemp2.getString("AmtCr"));
                } else if (DmShared.vNull(rsTemp2.getString("AmtDr")) > 0) {
                    mAmtDr = DmShared.vNull(rsTemp2.getString("AmtDr"));
                }

                if (mAmtCr == 0) {
                    mDrCrTyp = "DR";
                    mDrCr = mAmtDr;
                } else if (mAmtDr == 0) {
                    mDrCrTyp = "CR";
                    mDrCr = mAmtCr;
                } else {
                    mDrCr = mAmtCr - mAmtDr;
                    if (mDrCr < 0) {
                        mDrCrTyp = "DR";
                    } else if (mDrCr > 0) {
                        mDrCrTyp = "CR";
                    }
                }

                OLDAUDITENTRYIDSLIST oldAuditEndryIDSList = new OLDAUDITENTRYIDSLIST();
                oldAuditEndryIDSList.setTYPE("Number");
                oldAuditEndryIDSList.setOLDAUDITENTRYIDS("-1");

                ledgerRootElement.setOLDAUDITENTRYIDSLIST(oldAuditEndryIDSList);

                ledgerRootElement.setLEDGERNAME(DmShared.xNull(rsTemp2.getString("Name")));
                ledgerRootElement.setGSTCLASS("");
                ledgerRootElement.setISDEEMEDPOSITIVE(mDrCrTyp.equalsIgnoreCase("DR") ? "Yes" : "No");
                ledgerRootElement.setLEDGERFROMITEM("No");
                ledgerRootElement.setREMOVEZEROENTRIES("No");
                ledgerRootElement.setISPARTYLEDGER("Yes");
                ledgerRootElement.setISLASTDEEMEDPOSITIVE(mDrCrTyp.equalsIgnoreCase("DR") ? "Yes" : "No");

                ledgerRootElement.setAMOUNT(mDrCrTyp.equalsIgnoreCase("DR") ? DmServer.vNull(-mDrCr, "0.00") : DmServer.vNull(mDrCr, "0.00"));
                ledgerRootElement.setSERVICETAXDETAILSLIST(addSpaceChar(6));
                ledgerRootElement.setBANKALLOCATIONSLIST(addSpaceChar(6));

                BILLALLOCATIONSLIST billAllocationsList = new BILLALLOCATIONSLIST();

                billAllocationsList.setNAME(PBillNumber);
                billAllocationsList.setBILLTYPE("New Ref");
                billAllocationsList.setTDSDEDUCTEEISSPECIALRATE("No");
                billAllocationsList.setAMOUNT(mDrCrTyp.equalsIgnoreCase("DR") ? DmServer.vNull(-mDrCr, "0.00") : DmServer.vNull(mDrCr, "0.00"));
                billAllocationsList.setINTERESTCOLLECTIONLIST(addSpaceChar(6));
                billAllocationsList.setSTBILLCATEGORIESLIST(addSpaceChar(6));

                ledgerRootElement.setBILLALLOCATIONSLIST(billAllocationsList);
                ledgerRootElement.setINTERESTCOLLECTIONLIST(addSpaceChar(6));
                ledgerRootElement.setOLDAUDITENTRIESLIST(addSpaceChar(6));
                ledgerRootElement.setACCOUNTAUDITENTRIESLIST(addSpaceChar(6));
                ledgerRootElement.setAUDITENTRIESLIST(addSpaceChar(6));
                ledgerRootElement.setINPUTCRALLOCSLIST(addSpaceChar(6));
                ledgerRootElement.setDUTYHEADDETAILSLIST(addSpaceChar(6));
                ledgerRootElement.setEXCISEDUTYHEADDETAILSLIST(addSpaceChar(6));
                ledgerRootElement.setRATEDETAILSLIST(addSpaceChar(6));
                ledgerRootElement.setSUMMARYALLOCSLIST(addSpaceChar(6));
                ledgerRootElement.setSTPYMTDETAILSLIST(addSpaceChar(6));
                ledgerRootElement.setEXCISEPAYMENTALLOCATIONSLIST(addSpaceChar(6));
                ledgerRootElement.setTAXBILLALLOCATIONSLIST(addSpaceChar(6));
                ledgerRootElement.setTAXOBJECTALLOCATIONSLIST(addSpaceChar(6));
                ledgerRootElement.setTDSEXPENSEALLOCATIONSLIST(addSpaceChar(6));
                ledgerRootElement.setVATSTATUTORYDETAILSLIST(addSpaceChar(6));
                ledgerRootElement.setCOSTTRACKALLOCATIONSLIST(addSpaceChar(6));
                ledgerRootElement.setREFVOUCHERDETAILSLIST(addSpaceChar(6));
                ledgerRootElement.setINVOICEWISEDETAILSLIST(addSpaceChar(6));
                ledgerRootElement.setVATITCDETAILSLIST(addSpaceChar(6));
                ledgerRootElement.setADVANCETAXDETAILSLIST(addSpaceChar(6));

                ledgerRoot.add(ledgerRootElement);
            }

        } catch (Exception ex) {
            throw new UnsupportedOperationException("Error in LedgerNodeList Creation");
        }

        return ledgerRoot;
    }

    private String addSpaceChar(int spaceCount) {
        String space = "";

        for (int i = 0; i < spaceCount; i++) {
            space += " ";
        }

        return space;
    }

}
