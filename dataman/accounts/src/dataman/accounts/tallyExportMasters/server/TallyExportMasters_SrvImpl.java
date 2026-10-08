package dataman.accounts.tallyExportMasters.server;

import dataman.accounts.tallyExportMasters.shared.NAMELIST;
import dataman.accounts.tallyExportMasters.shared.REQUESTDESC;
import dataman.accounts.tallyExportMasters.shared.STATICVARIABLES;
import dataman.accounts.tallyExportMasters.shared.MAILINGNAMELIST;
import dataman.accounts.tallyExportMasters.shared.IMPORTDATA;
import dataman.accounts.tallyExportMasters.shared.BODY;
import dataman.accounts.tallyExportMasters.shared.TallyExportMasters_Srv;
import dataman.accounts.tallyExportMasters.shared.LANGUAGENAMELIST;
import dataman.accounts.tallyExportMasters.shared.TALLYMESSAGE;
import dataman.accounts.tallyExportMasters.shared.REQUESTDATA;
import dataman.accounts.tallyExportMasters.shared.ENVELOPE;
import dataman.accounts.tallyExportMasters.shared.ADDRESSLIST;
import dataman.accounts.tallyExportMasters.shared.HEADER;
import dataman.accounts.tallyExportMasters.shared.GROUP;
import dataman.accounts.tallyExportMasters.shared.Filter_Mdl;
import dataman.accounts.tallyExportMasters.shared.LEDGER;
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

public class TallyExportMasters_SrvImpl extends DmRSSPrivate implements TallyExportMasters_Srv {

    boolean isItemRequiredInCsv = false;

    @Override
    public DmResult<String[]> exportAcMastersToTallyXML(Filter_Mdl filter) {
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
        

            /*=================================< Set file path >=================================*/
            tempValue = "Masters.xml";
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
            requestdesc.setREPORTNAME("All Masters");
            STATICVARIABLES staticVariables = new STATICVARIABLES();
            staticVariables.setSVCURRENTCOMPANY(DmServer.getSsnSiteDisplayName(this));
            requestdesc.setSTATICVARIABLES(staticVariables);

            REQUESTDATA requestData = new REQUESTDATA();
            List<TALLYMESSAGE> root = new ArrayList<>();
            TALLYMESSAGE tallyMassageNode = new TALLYMESSAGE();

            createAcGroupNode(root, tallyMassageNode, cnTemp, rsTemp, psTemp, filter);
            createAcMasterNode(root, tallyMassageNode, cnTemp, rsTemp2, psTemp2, filter);

            requestData.setTALLYMESSAGE(root);
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

    private void createAcGroupNode(List<TALLYMESSAGE> root, TALLYMESSAGE tallyMassageNode, Connection cnTemp, ResultSet rsTemp, DmPreparedStatement psTemp, Filter_Mdl filter) {

        String sql = "";
        try {

            sql = "Select AC.GroupCode,AC.GroupName, "
                    + " (Case AC.GroupNature  "
                    + " When '0' Then 'Liabilities'  "
                    + " When '1' Then 'Assets'  "
                    + " When '2' Then 'Expenses Direct'  "
                    + " When '3' Then 'Expenses Indirect'  "
                    + " When '4' Then 'Revenue Direct'  "
                    + " When '5' Then 'Revenue Indirect'  "
                    + " End)As GroupNature,sgn.name as Nature,AC.ContraGroupName,"
                    + " (Case  isnull(AC.isSysGroup,'0')"
                    + " When '0' Then 'N' "
                    + " When '1' Then 'Y'  "
                    + " End) as  isSysGroup "
                    + " ,AC1.GroupName As GroupUnderName  "
                    + " From AcGroup AC  "
                    + " Left Join AcGroup AC1 On AC.GroupUnder = AC1.GroupCode "
                    + "  left join subGroupNature as sgn on AC.nature=sgn.code "
                    + "    where AC.GroupName not in ('Capital Account' ,'Loans (Liability)',   "
                    + "'Current Liabilities',   "
                    + "'Fixed Assets',   "
                    + "'Investments',   "
                    + "'Current Assets',   "
                    + "'Branch / Divisions',   "
                    + "'Misc. Expences (Asset)',   "
                    + "'Suspense A/c',   "
                    + "'Capital Account',   "
                    + "'Secured Loans',   "
                    + "'Sales Accounts',   "
                    + "'Purchase Accounts',   "
                    + "'Direct Incomes',   "
                    + "'Direct Expenses',   "
                    + "'Indirect Incomes',   "
                    + "'Indirect Expenses',   "
                    + "'Sundry Debtors',   "
                    + "'Sundry Creditors')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                tallyMassageNode = new TALLYMESSAGE();

                tallyMassageNode.setXmlns("TallyUDF");

                GROUP group = new GROUP();
                group.setNAME(DmShared.xNull(rsTemp.getString("GroupName")));
                group.setRESERVEDNAME(DmShared.xNull(rsTemp.getString("GroupName")));
                group.setPARENT(DmShared.xNull(rsTemp.getString("GroupUnderName")));
                group.setGRPDEBITPARENT("");
                group.setGRPCREDITPARENT("");
                group.setISBILLWISEON("No");
                group.setISCOSTCENTRESON("No");
                group.setISADDABLE("No");
                group.setISSUBLEDGER("No");
                group.setISREVENUE("No");
                group.setAFFECTSGROSSPROFIT("No");
                group.setISDEEMEDPOSITIVE("No");
                group.setTRACKNEGATIVEBALANCES("No");
                group.setISCONDENSED("No");
                group.setAFFECTSSTOCK("No");
                group.setISGROUPFORLOANRCPT("No");
                group.setISGROUPFORLOANPYMNT("No");
                group.setSORTPOSITION("No");

                LANGUAGENAMELIST languageNameList = new LANGUAGENAMELIST();
                NAMELIST nameList = new NAMELIST();
                nameList.setTYPE("String");
                nameList.setNAME(DmShared.xNull(rsTemp.getString("GroupName")));
                languageNameList.setNAMELIST(nameList);
                languageNameList.setLANGUAGEID("1033");
                group.setLANGUAGENAMELIST(languageNameList);
                tallyMassageNode.setGROUP(group);
                root.add(tallyMassageNode);

            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error in AcGroupNode Creation");
        }

    }

    private void createAcMasterNode(List<TALLYMESSAGE> root, TALLYMESSAGE tallyMassageNode, Connection cnTemp, ResultSet rsTemp2, DmPreparedStatement psTemp2, Filter_Mdl filter) {

        String sql = "";
        try {

            sql = "Select   SG.SubCode,'Account Master' As V_Type,SG.ManualCode As Manual_Code,SG.Name As Account_Name,   "
                    + "isNull(SG.Add1,'')  AS Address1, isNull(SG.Add2,'') AS Address2,AG.GroupName As Ac_Group,sgn.name as Nature,    "
                    + "sm.name as stateName ,sg.gsTin    "
                    + "From Subgroup SG      "
                    + "Left Join City C On C.CityCode=SG.CityCode       "
                    + "Left join stateMast as sm on sm.code=C.stateCode        "
                    + "Left Join AcGroup AG On AG.GroupCode=SG.GroupCode       "
                    + "Left join subgroupNature sgn on SG.nature= sgn.code    "
                    + "Left join subgroupNature sgn1 on SG.filterNature= sgn1.code    "
                    + "   "
                    + "where sgn1.manualCode in('LG','BK','CS','SU','CT','IS','PS','CB','BR','TP','AG','OR') ";

            psTemp2 = DmPreparedStatement.prepareStatement(cnTemp, sql);
          
            rsTemp2 = psTemp2.executeQuery();
            while (rsTemp2.next()) {

                tallyMassageNode = new TALLYMESSAGE();
                tallyMassageNode.setXmlns("TallyUDF");

                LEDGER ledger = new LEDGER();
                ledger.setNAME(DmShared.xNull(rsTemp2.getString("Account_Name")));
                ledger.setRESERVEDNAME("");

                ADDRESSLIST addressList = new ADDRESSLIST();
                addressList.setTYPE("String");
                addressList.setADDRESS(DmShared.xNull(rsTemp2.getString("Address1")));
                addressList.setADDRESS(DmShared.xNull(rsTemp2.getString("Address2")));

                ledger.setADDRESSLIST(addressList);

                MAILINGNAMELIST mailList = new MAILINGNAMELIST();
                mailList.setTYPE("String");
                mailList.setMAILINGNAME(DmShared.xNull(rsTemp2.getString("Account_Name")));

                ledger.setMAILINGNAMELIST(mailList);
                ledger.setSTATENAME(DmShared.xNull(rsTemp2.getString("stateName")));
                ledger.setPARTYGSTIN(DmShared.xNull(rsTemp2.getString("gsTin")));
                ledger.setSALESTAXNUMBER("05002007343");
                ledger.setPRICELEVEL("PL");
                ledger.setPARENT(DmShared.xNull(rsTemp2.getString("Ac_Group")));
                ledger.setTAXCLASSIFICATIONNAME("");
                ledger.setTAXTYPE("Others");
                ledger.setBILLCREDITPERIOD("5 Days");
                ledger.setGSTTYPE("");
                ledger.setAPPROPRIATEFOR("");
                ledger.setSERVICECATEGORY("");
                ledger.setEXCISELEDGERCLASSIFICATION("");
                ledger.setEXCISEDUTYTYPE("");
                ledger.setEXCISENATUREOFPURCHASE("");
                ledger.setLEDGERFBTCATEGORY("");
                ledger.setISBILLWISEON("No");
                ledger.setISCOSTCENTRESON("No");
                ledger.setISINTERESTON("No");
                ledger.setALLOWINMOBILE("No");
                ledger.setISCOSTTRACKINGON("No");
                ledger.setISCONDENSED("No");
                ledger.setAFFECTSSTOCK("No");
                ledger.setFORPAYROLL("No");
                ledger.setINTERESTONBILLWISE("No");
                ledger.setOVERRIDEINTEREST("No");
                ledger.setOVERRIDEADVINTEREST("No");
                ledger.setUSEFORVAT("No");
                ledger.setIGNORETDSEXEMPT("No");
                ledger.setISTCSAPPLICABLE("No");
                ledger.setISTDSAPPLICABLE("No");
                ledger.setISFBTAPPLICABLE("No");
                ledger.setISGSTAPPLICABLE("No");
                ledger.setISEXCISEAPPLICABLE("No");
                ledger.setISTDSEXPENSE("No");
                ledger.setISEDLIAPPLICABLE("No");
                ledger.setISRELATEDPARTY("No");
                ledger.setUSEFORESIELIGIBILITY("No");
                ledger.setSHOWINPAYSLIP("No");
                ledger.setUSEFORGRATUITY("No");
                ledger.setISTDSPROJECTED("No");
                ledger.setFORSERVICETAX("No");
                ledger.setISINPUTCREDIT("No");
                ledger.setISEXEMPTED("No");
                ledger.setISABATEMENTAPPLICABLE("No");
                ledger.setISSTXPARTY("No");
                ledger.setISSTXNONREALIZEDTYPE("No");
                ledger.setTDSDEDUCTEEISSPECIALRATE("No");
                ledger.setAUDITED("No");
                ledger.setSORTPOSITION("1000");
                //==============================SetOpeningBalance====================================
              //  ledger.setOPENINGBALANCE(getOpeningBalance(filter, cnTemp, rsTemp2));
                  ledger.setOPENINGBALANCE("00.00");
                //==============================EndSetOpeningBalance====================================
                LANGUAGENAMELIST languageNameList = new LANGUAGENAMELIST();
                NAMELIST namelIst = new NAMELIST();
                namelIst.setTYPE("String");
                namelIst.setNAME(DmShared.xNull(rsTemp2.getString("Account_Name")));
                languageNameList.setNAMELIST(namelIst);
                languageNameList.setLANGUAGEID("1033");
                ledger.setLANGUAGENAMELIST(languageNameList);
                ledger.setSLABPERIODLIST(addSpaceChar(6));
                ledger.setGRATUITYPERIODLIST(addSpaceChar(6));
                ledger.setADDITIONALCOMPUTATIONSLIST(addSpaceChar(6));
                ledger.setBANKALLOCATIONSLIST(addSpaceChar(6));
                ledger.setPAYMENTDETAILSLIST(addSpaceChar(6));
                ledger.setBANKEXPORTFORMATSLIST(addSpaceChar(6));
                ledger.setBILLALLOCATIONSLIST(addSpaceChar(6));
                ledger.setINTERESTCOLLECTIONLIST(addSpaceChar(6));
                ledger.setLEDGERCLOSINGVALUESLIST(addSpaceChar(6));
                ledger.setLEDGERAUDITCLASSLIST(addSpaceChar(6));
                ledger.setAUDITENTRIESLIST(addSpaceChar(6));
                ledger.setTDSEXEMPTIONRULESLIST(addSpaceChar(6));
                ledger.setDEDUCTINSAMEVCHRULESLIST(addSpaceChar(6));
                ledger.setLOWERDEDUCTIONLIST(addSpaceChar(6));
                ledger.setSTXABATEMENTDETAILSLIST(addSpaceChar(6));
                ledger.setLEDMULTIADDRESSLISTLIST(addSpaceChar(6));
                ledger.setSTXTAXDETAILSLIST(addSpaceChar(6));
                tallyMassageNode.setLEDGER(ledger);
                root.add(tallyMassageNode);
            }
        } catch (Exception ex) {
            throw new UnsupportedOperationException("Error in AcMasterNode Creation");
        }

    }

    private String getOpeningBalance(Filter_Mdl filter, Connection cnTemp, ResultSet rsTempAc) {

        String sqlQuery = "", condition = "", openingBalance = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {

            sqlQuery = "Select Count(ag.GroupNature) As Cnt "
                    + "From SubGroup SG "
                    + "left join acGroup ag on ag.groupCode = sg.GroupCode "
                    + "Where SG.SubCode = :subCode and ag.GroupNature in (2,3,4,5) ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", rsTempAc.getString("SubCode"));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("Cnt")).intValue() > 0) {
                    condition += "  and lg.v_Prefix = " + DmServer.getSsnVoucherPrefix(this) + "  ";
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select IsNull(SG.SubCode,'') As SubCode,         "
                    + "IsNull(Sum(LG.AmtCr),0)-IsNull(Sum(LG.AmtDr),0) As Opening        "
                    + "From Ledger LG        "
                    + "Left Join SubGroup SG On LG.SubCode=SG.SubCode        "
                    + "where LG.v_Date< :fromDate and lg.site_Code = :siteCode and lg.isPosted = 1    and lg.subcode= :subCode  " + condition
                    + "Group By IsNull(SG.SubCode,'') ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.getFromDate()));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setString("subCode", DmShared.iNull(rsTempAc.getString("SubCode")).toString());
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                openingBalance = DmServer.vNull(DmShared.vNull(rsTemp.getString("Opening")), "0.00");
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {

            throw new UnsupportedOperationException("Error in getOpeningBalance ");

        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return openingBalance;

    }

    private String addSpaceChar(int spaceCount) {
        String space = "";

        for (int i = 0; i < spaceCount; i++) {
            space += " ";
        }

        return space;
    }

}
