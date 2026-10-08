package dataman.accounts.fundTransferEntry.server;

import dataman.accounts.fundTransferEntry.client.FundTransferEntry;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.fundTransferEntry.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FundTransferEntry_SrvImpl extends DmRSSPrivate implements FundTransferEntry_Srv {

    private final String voucherTypefieldName = "lm.V_Type", voucherDateFieldName = "lm.V_Date";
    final String vTypeSName = "FNDTR";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission, String vPrefix) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, siteCode, sqlCondition;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            siteCode = DmServer.getSsnSiteCode(this);
            sqlQuery = "SELECT lm.DocId AS SearchCode "
                    + "FROM LedgerM lm "
                    + "left join voucher_type vt on lm.v_type = vt.v_type "
                    + "WHERE vt.short_Name = :vTypeSName "
                    + "    AND lm.V_Prefix = :vPrefix AND lm.Site_Code = :siteCode ";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }

            sqlQuery += " ORDER BY lm.V_Date, vt.short_Name,lm.V_No, lm.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vTypeSName", vTypeSName);
            psTemp.setString("vPrefix", vPrefix);
            psTemp.setString("siteCode", siteCode);

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SearchCode")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            cnTemp.close();

        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<FundTransferEntry_Mdl> getMoveRec(String strKey) {
        DmResult<FundTransferEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        FundTransferEntry_Mdl mrTemp = new FundTransferEntry_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "SELECT LM.V_Prefix,LM.PreparedBy,LM.PostedBy,LM.U_Name,LG.DocID,LG.RecID, lg.recIdPrefix, lg.v_No, LG.V_Date,  "
                    + "LG.V_Type,LG.Narration,LG.AmtCr,LG.SubCode,LG.ContraSub,LG.Site_Code,isNull(LG.Chq_No,'') AS Chq_No,LG.Chq_Date,  "
                    + "LG.Clg_Date,SGC.Name AS CreditPartyName,SGC.Nature AS CreditPartyNature,SGD.Name AS DebitPartyName,SM.Name AS SiteName,  "
                    + "SGD.Nature AS DebitPartyNature,CCM.Name AS CostCenterName,LGC.CostCenter,LM.LGDocId,  "
                    + "TLM.RecId AS TLMRecId,TLM.V_Type AS TLMV_Type,TLM.SubCode AS TLMSubCode,TLM.ContraSub AS TLMContraSub,  "
                    + "TLM.CreditPartyName AS TLMCreditPartyName,TLM.DebitPartyName AS TLMDebitPartyName,LM.To_Site_Code,TSM.Name AS TSName,  "
                    + "TLM.CosetSenterName AS TLMCosetSenterName,TLM.CostCenter AS TLMCostCenter,TLM.Narration AS TLMNarration, LM.u_Name  "
                    + "FROM LedgerM LM  "
                    + "LEFT JOIN Ledger LG ON LG.DocID = LM.DocID  "
                    + "LEFT JOIN LedgerCostCenter LGC ON LGC.DocID = LG.DocId AND LGC.V_SNo = LG.V_SNo  "
                    + "LEFT JOIN SubGroup SGC ON SGC.SubCode = LG.SubCode  "
                    + "LEFT JOIN SubGroup SGD ON SGD.SubCode = LG.ContraSub  "
                    + "LEFT JOIN SiteMast SM ON SM.Code = LG.Site_Code  "
                    + "LEFT JOIN CostCenterMast CCM ON CCM.Code = LGC.CostCenter  "
                    + "LEFT JOIN LedgerM LMT ON LMT.LGDocId = LM.DocId  "
                    + "LEFT JOIN SiteMast TSM ON TSM.Code = LM.To_Site_Code  "
                    + "LEFT JOIN  "
                    + "(SELECT LG.DocId,LG.RecId,LG.V_Type,LG.SubCode,LG.ContraSub,LG.Site_Code,  "
                    + "SGC.Name AS CreditPartyName,SGD.Name AS DebitPartyName,SM.Name AS SiteName,  "
                    + "CCM.Name AS CosetSenterName,LGC.CostCenter,LG.Narration  "
                    + "FROM Ledger LG  "
                    + "LEFT JOIN LedgerCostCenter LGC ON LGC.DocID = LG.DocId AND LGC.V_SNo = LG.V_SNo  "
                    + "LEFT JOIN SubGroup SGC ON SGC.SubCode = LG.SubCode  "
                    + "LEFT JOIN SubGroup SGD ON SGD.SubCode = LG.ContraSub  "
                    + "LEFT JOIN SiteMast SM ON SM.Code = LG.Site_Code  "
                    + "LEFT JOIN CostCenterMast CCM ON CCM.Code = LGC.CostCenter  "
                    + "WHERE isNull(LG.AmtCr, 0) > 0  "
                    + ") TLM ON TLM.DocId = LMT.DocId  "
                    + "WHERE isNull(LG.AmtCr, 0) > 0 AND LG.DocID = :docId";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocID")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("RecId")));
                mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getObjRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getObjRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setAmount(DmServer.vNull(rsTemp.getDouble("AmtCr"), "0.00"));
                mrTemp.setChaqNo(DmShared.xNull(rsTemp.getString("Chq_No")));
                mrTemp.setCostCenterTag(DmShared.xNull(rsTemp.getString("CostCenter")));
                mrTemp.setCostCenterText(DmShared.xNull(rsTemp.getString("CostCenterName")));
                mrTemp.setCrAccountDesc(DmShared.xNull(rsTemp.getString("CreditPartyNature")));
                mrTemp.setCrAccountTag(DmShared.xNull(rsTemp.getString("SubCode")));
                mrTemp.setCrAccountText(DmShared.xNull(rsTemp.getString("CreditPartyName")));
                mrTemp.setDrAccountDesc(DmShared.xNull(rsTemp.getString("DebitPartyNature")));
                mrTemp.setDrAccountTag(DmShared.xNull(rsTemp.getString("ContraSub")));
                mrTemp.setDrAccountText(DmShared.xNull(rsTemp.getString("DebitPartyName")));
                mrTemp.setCurrentSiteTag(DmShared.xNull(rsTemp.getString("Site_Code")));
                mrTemp.setCurrentSiteText(DmShared.xNull(rsTemp.getString("SiteName")));
                mrTemp.setDtCheque(DmShared.dNull(rsTemp.getDate("Chq_Date")));
                mrTemp.setDtClearing(DmShared.dNull(rsTemp.getDate("Clg_Date")));
                mrTemp.setDtEntry(DmShared.dNull(rsTemp.getDate("V_Date")));
                mrTemp.setVoucherPrefix(DmShared.xNull(rsTemp.getString("V_Prefix")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("Narration")));
                mrTemp.setTrnRemark(DmShared.xNull(rsTemp.getString("TLMNarration")));
                mrTemp.setTrnCostCenterTag(DmShared.xNull(rsTemp.getString("TLMCostCenter")));
                mrTemp.setTrnCostCenterText(DmShared.xNull(rsTemp.getString("TLMCosetSenterName")));
                mrTemp.setTrnCrAccountTag(DmShared.xNull(rsTemp.getString("TLMSubCode")));
                mrTemp.setTrnCrAccountText(DmShared.xNull(rsTemp.getString("TLMCreditPartyName")));
                mrTemp.setTrnDrAccountTag(DmShared.xNull(rsTemp.getString("TLMContraSub")));
                mrTemp.setTrnDrAccountText(DmShared.xNull(rsTemp.getString("TLMNarration")));
                mrTemp.setTrnToSiteTag(DmShared.xNull(rsTemp.getString("To_Site_Code")));
                mrTemp.setTrnToSiteText(DmShared.xNull(rsTemp.getString("TSName")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("PreparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("PostedBy")));
            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    public boolean haveControlPermission(UserPermission userPermission, String moduleName, String menuName, String controlCaption) {
        boolean have = false;
        List<UserMenu> userMenuList;
        List<String> controlPermissionList;

        if (userPermission != null) {
            if (userPermission.getUserMenu() != null) {
                userMenuList = userPermission.getUserMenu().get(DmShared.xNull(moduleName));
                if (userMenuList != null) {
                    for (int i = 0; i < userMenuList.size(); i++) {
                        if (userMenuList.get(i) != null) {
                            if (DmShared.xNull(userMenuList.get(i).getMenuName()).trim().equalsIgnoreCase(DmShared.xNull(menuName).trim())) {
                                controlPermissionList = userMenuList.get(i).getControlPermission();
                                if (controlPermissionList != null) {
                                    if (controlPermissionList.contains(controlCaption)) {
                                        have = true;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        return have;
    }

    @Override
    public DmResult<String> saveRecord(UserPermission userPermission, FundTransferEntry_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL, userName, contraText;
        ResultSet rsTemp = null;
        String vPrefix, vType;
        boolean autoApprove = false;
        int intVSno = 0;
        dmrTemp.setData("");

        try {
            FundTransferEntry_Vld.chkSave(mrTemp);
            autoApprove = haveControlPermission(userPermission, "", "", FundTransferEntry.approvedByBlog);

            vPrefix = mrTemp.getVoucherPrefix();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            userName = DmServer.getSsnUserName(this);
            spBegin = new DmSavePoint(cnTemp);
            vType = DmServer.getVType(vTypeSName, cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(vType), vPrefix, cnTemp, this));
                mrTemp.setObjRecId(DmServer.getRecId("ledgerM", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), DmShared.dNull(mrTemp.getDtEntry()), DmShared.xNull(vType), vPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getObjRecId().getRecIdValue()));
            } else {
                DmServer.getRecId("ledgerM", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), DmShared.dNull(mrTemp.getDtEntry()), DmShared.xNull(vType), vPrefix, cnTemp, this, true);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            strSQL = "DELETE FROM LedgerCostCenter WHERE DocID = :docID ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docID", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();

//            strSQL = "DELETE FROM BankReco WHERE DocID = :docID ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//            psTemp.setString("docID", mrTemp.getSharedKeyFieldCode());
//            psTemp.executeUpdate();
            strSQL = "DELETE FROM Ledger WHERE DocID = :docID ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docID", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            if (blnAdd) {
                strSQL = "INSERT INTO ledgerM (DocId, RecId, recIdPrefix, v_No, V_Type, V_Prefix, V_Date, Narration,"
                        + " Site_Code,  U_EntDt, To_Site_Code, SubCode, PostedBy, PreparedBy) "
                        + "VALUES ( :docID, :recId, :recIdPrefix, :v_No, :vType, :vPrefix, :vDate, :narration,"
                        + " :siteCode,  :uEntDt, :toSiteCode, :subCode, :PostedBy, :userName) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("docID", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("recId", mrTemp.getSharedKeyFieldName());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                psTemp.setString("vType", vType);
                psTemp.setString("vPrefix", vPrefix);
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getDtEntry()));
                psTemp.setString("narration", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getCurrentSiteTag()));
                psTemp.setString("PostedBy", (autoApprove ? userName : null));
                psTemp.setString("userName", userName);
                psTemp.setString("toSiteCode", DmShared.xNull(mrTemp.getTrnToSiteTag()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getCrAccountTag()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            } else {
                strSQL = "UPDATE ledgerM Set "
                        + "V_Date = :vDate , "
                        + "SubCode = :subCode , "
                        + "Narration = :narration , "
                        + "To_Site_Code = :toSiteCode , "
                        + "U_Name = :userName , "
                        + "PostedBy = :PostedBy , "
                        + "U_EntDt = :uEntDt  "
                        + "WHERE DocId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getDtEntry()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getCrAccountTag()));
                psTemp.setString("narration", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("toSiteCode", DmShared.foriegnKey(mrTemp.getTrnToSiteTag()));
                psTemp.setString("userName", userName);
                psTemp.setString("PostedBy", (autoApprove ? userName : null));
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            //PrepareContraText 'Dr'
            contraText = DmServer.prepareContraText(DmShared.xNull(mrTemp.getDrAccountText()), DmShared.vNull(mrTemp.getAmount()), "Dr");
            intVSno = intVSno + 1;
            strSQL = "INSERT INTO Ledger ( DocId, RecId, V_SNo, V_Date, SubCode, ContraSub, AmtDr, AmtCr, Narration, V_Type, V_Prefix, "
                    + "Site_Code, Chq_No, Chq_Date, ContraText, Clg_Date, favourOf, recIdPrefix, v_No )  "
                    + "VALUES ( :docId, :recId, :vSno, :vDate, :subCode, :contraSub, NULL, :amtCr, :narration, :vType, :vPrefix, "
                    + ":siteCode, :chqNo, :chqDate, :contraText, :clgDate, :favourOf, :recIdPrefix, :v_No)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("recId", mrTemp.getSharedKeyFieldName());
            psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
            psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
            psTemp.setInt("vSno", intVSno);
            psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getDtEntry()));
            psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getCrAccountTag()));
            psTemp.setString("contraSub", DmShared.foriegnKey(mrTemp.getDrAccountTag()));
            psTemp.setString("amtCr", DmServer.vNull(mrTemp.getAmount(), "0.00"));
            psTemp.setString("narration", DmShared.xNull(mrTemp.getRemark()));
            psTemp.setString("vType", vType);
            psTemp.setString("vPrefix", vPrefix);
            psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getCurrentSiteTag()));
            psTemp.setString("chqNo", DmShared.xNull(mrTemp.getChaqNo()));
            psTemp.setDate("chqDate", DmServer.convertDate(mrTemp.getDtCheque()));
            psTemp.setString("contraText", contraText);
            psTemp.setDate("clgDate", DmServer.convertDate(mrTemp.getDtClearing()));
            psTemp.setString("favourOf", DmShared.xNull(mrTemp.getDrAccountText()));
            psTemp.executeUpdate();
            psTemp.close();

            //PrepareContraText 'Cr'
            contraText = DmServer.prepareContraText(DmShared.xNull(mrTemp.getCrAccountText()), DmShared.vNull(mrTemp.getAmount()), "Cr");
            intVSno = intVSno + 1;
            strSQL = "INSERT INTO Ledger ( DocId, RecId, V_SNo, V_Date, SubCode, ContraSub, AmtDr, AmtCr, Narration, V_Type, V_Prefix, Site_Code,"
                    + "  Chq_No, Chq_Date, ContraText, Clg_Date,favourOf, recIdPrefix, v_No )  "
                    + "VALUES ( :docId, :recId, :vSno, :vDate, :subCode, :contraSub, :amtDr, NULL, :narration, :vType, :vPrefix, :siteCode,"
                    + " :chqNo, :chqDate, :contraText, :clgDate,:favourOf, :recIdPrefix, :v_No)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("recId", mrTemp.getSharedKeyFieldName());
            psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
            psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
            psTemp.setInt("vSno", intVSno);
            psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getDtEntry()));
            psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getDrAccountTag()));
            psTemp.setString("contraSub", DmShared.foriegnKey(mrTemp.getCrAccountTag()));
            psTemp.setString("amtDr", DmServer.vNull(mrTemp.getAmount(), "0.00"));
            psTemp.setString("narration", mrTemp.getRemark());
            psTemp.setString("vType", vType);
            psTemp.setString("vPrefix", vPrefix);
            psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getCurrentSiteTag()));
            psTemp.setString("chqNo", mrTemp.getChaqNo());
            psTemp.setDate("chqDate", DmServer.convertDate(mrTemp.getDtCheque()));
            psTemp.setString("contraText", contraText);
            psTemp.setDate("clgDate", DmServer.convertDate(mrTemp.getDtClearing()));
            psTemp.setString("favourOf", DmShared.xNull(mrTemp.getDrAccountText()));
            psTemp.executeUpdate();
            psTemp.close();

//            if (DmShared.xNull(mrTemp.getCrAccountDesc()).equalsIgnoreCase("Bank")) {
//                strSQL = "Insert Into BankReco( DocID, V_SNo, Bank_V_SNo, Favourof, Chq_No, Chq_Date, AmtCr, AmtDr, Remark) "
//                        + " Values( :docId, :vSno, :vSno - 1  , :favourOf, :chqNo, :chqDt, :amount, Null, :remark)";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
//                psTemp.setInt("vSno", intVSno);
//                psTemp.setString("favourOf", DmShared.xNull(mrTemp.getDrAccountText()));
//                psTemp.setString("chqNo", DmShared.xNull(mrTemp.getChaqNo()));
//                psTemp.setDate("chqDt", DmServer.convertDate(mrTemp.getDtCheque()));
//                psTemp.setString("amount", DmServer.vNull(mrTemp.getAmount(), "0.00"));
//                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
//                psTemp.executeUpdate();
//                psTemp.close();
//            }
//
//            if (DmShared.xNull(mrTemp.getDrAccountDesc()).equalsIgnoreCase("Bank")) {
//                strSQL = "Insert Into BankReco( DocID, V_SNo, Bank_V_SNo, Favourof, Chq_No, Chq_Date, AmtCr, AmtDr, Remark)"
//                        + " Values( :docId, :vSno - 1, :vSno, :favourOf, :chqNo, :chqDt, Null, :amount,:remark)";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
//                psTemp.setInt("vSno", intVSno);
//                psTemp.setString("favourOf", DmShared.xNull(mrTemp.getCrAccountText()));
//                psTemp.setString("chqNo", DmShared.xNull(mrTemp.getChaqNo()));
//                psTemp.setDate("chqDt", DmServer.convertDate(mrTemp.getDtCheque()));
//                psTemp.setString("amount", DmServer.vNull(mrTemp.getAmount(), "0.00"));
//                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
//                psTemp.executeUpdate();
//                psTemp.close();
//            }
            if (!DmShared.xNull(mrTemp.getCostCenterTag()).equalsIgnoreCase("")) {
                strSQL = "INSERT INTO LedgerCostCenter (DocId,V_SNo,SNo,CostCenter,AmtDr,AmtCr) "
                        + "SELECT  LG.DocId,LG.V_SNo,LG.V_SNo AS SNo, :fromCostCenter AS CostCenter,LG.AmtDr,LG.AmtCr "
                        + "FROM Ledger LG "
                        + "WHERE LG.DocId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("fromCostCenter", DmShared.foriegnKey(mrTemp.getCostCenterTag()));
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.executeUpdate();
                psTemp.close();
            }

            //======================== For Posted By Updation ======================
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            strSQL = "SELECT COUNT(DocId) Cnt FROM LedgerM WHERE DocId = :docId AND isNull(PostedBy,'')<>''";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Posted. You Can Not Edit/ Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "SELECT COUNT(*) Cnt FROM DataAudit WHERE DocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Audited. You Can Not Edit/ Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "SELECT COUNT(Adj_DocId) Cnt FROM LedgerAdj WHERE Adj_DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Adjusted. You Can Not Edit/ Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "SELECT COUNT(*) Cnt FROM dataAudit WHERE DocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Transfered. You Can Not Edit/ Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "SELECT COUNT(DocId) Cnt FROM LedgerM WHERE LGDocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Used In Transit. You Can Not Edit / Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "DELETE FROM LedgerCostCenter WHERE DocID = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();

//            strSQL = "DELETE FROM BankReco WHERE DocID = :docId ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//            psTemp.setString("docId", DmShared.iNull(strKey).toString());
//            psTemp.executeUpdate();
            strSQL = "DELETE FROM Ledger WHERE DocID = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();

            strSQL = "DELETE FROM LedgerM WHERE DocID = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();

            psTemp.close();

            spBegin.commit();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile, String vPrefix) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, sqlCondition, siteCode;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            siteCode = DmServer.getSsnSiteCode(this);

            sqlQuery = "SELECT LM.DocId,LG.RecId AS Entry_No,LG.V_Type AS Entry_Type,LG.V_Date AS Entry_Date, "
                    + "LG.Narration AS Remark,LG.AmtCr AS Amount,isNull(LG.Chq_No,'') AS Chq_No,LG.Chq_Date, "
                    + "LG.Clg_Date,SGC.Name AS Credit_Ac,SGD.Name AS Debit_Ac, "
                    + "CCM.Name AS CostCenterName,TSM.Name AS Transfer_To_Site, "
                    + "TLM.RecId AS Trn_RecId,TLM.V_Type AS Trn_Type, "
                    + "LM.V_Date, vt.short_Name, LM.V_No, "
                    + "TLM.CreditPartyName AS Trn_Credit_Ac,TLM.DebitPartyName AS Trn_Debit_Ac, "
                    + "TLM.CosetSenterName AS Trn_CostCenterName,LM.PreparedBy,LM.U_Name As ModifiedBy,LM.PostedBy, FORMAT(lm.u_EntDt,'dd-MMM-yyyy HH.mm') AS entryDateTime  "
                    + "FROM LedgerM LM "
                    + "LEFT JOIN Ledger LG ON LG.DocID = LM.DocID "
                    + "  LEFT JOIN voucher_type vt on LG.v_type = vt.v_Type "
                    + "LEFT JOIN LedgerCostCenter LGC ON LGC.DocID = LG.DocId AND LGC.V_SNo = LG.V_SNo "
                    + "LEFT JOIN subgroup SGC ON SGC.SubCode = LG.SubCode "
                    + "LEFT JOIN subgroup SGD ON SGD.SubCode = LG.ContraSub "
                    + "LEFT JOIN costcentermast CCM ON CCM.Code = LGC.CostCenter "
                    + "LEFT JOIN LedgerM LMT ON LMT.LGDocId = LM.DocId "
                    + "LEFT JOIN sitemast TSM ON TSM.Code = LM.To_Site_Code "
                    + "LEFT JOIN "
                    + "( "
                    + "SELECT LG.DocId,LG.RecId,LG.V_Type,LG.SubCode,LG.ContraSub,LG.Site_Code, "
                    + "SGC.Name AS CreditPartyName,SGD.Name AS DebitPartyName, "
                    + "CCM.Name AS CosetSenterName,LGC.CostCenter "
                    + "FROM Ledger LG "
                    + "LEFT JOIN LedgerCostCenter LGC ON LGC.DocID = LG.DocId AND LGC.V_SNo = LG.V_SNo "
                    + "LEFT JOIN subgroup SGC ON SGC.SubCode = LG.SubCode "
                    + "LEFT JOIN subgroup SGD ON SGD.SubCode = LG.ContraSub "
                    + "LEFT JOIN costcentermast CCM ON CCM.Code = LGC.CostCenter "
                    + "WHERE isNull(LG.AmtCr, 0) > 0 "
                    + ") TLM ON TLM.DocId = LMT.DocId "
                    + "WHERE  LM.Site_Code= :siteCode AND "
                    + "LM.V_Prefix = :vPrefix AND "
                    + "vt.short_name = :vTypeSName AND isNull(LG.AmtCr, 0) > 0 ";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", siteCode);
            sqlWithParam.setString("vPrefix", vPrefix);
            sqlWithParam.setString("vTypeSName", vTypeSName);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                listNode.setChaqNo(DmShared.xNull(rsTemp.getString("Chq_No")));
                listNode.setCostCenter(DmShared.xNull(rsTemp.getString("CostCenterName")));
                listNode.setCrAccount(DmShared.xNull(rsTemp.getString("Credit_Ac")));
                listNode.setDrAccount(DmShared.xNull(rsTemp.getString("Debit_Ac")));
                listNode.setDtCheque(DmShared.dNull(DmShared.dNull(rsTemp.getDate("Chq_Date"))));
                listNode.setDtClearing(DmShared.dNull(DmShared.dNull(rsTemp.getDate("Clg_Date"))));
                listNode.setVDate(DmShared.dNull(DmShared.dNull(rsTemp.getDate("Entry_Date"))));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Entry_No")));
                listNode.setNarration(DmShared.xNull(rsTemp.getString("Remark")));
                listNode.setTrnCostCenter(DmShared.xNull(rsTemp.getString("Trn_CostCenterName")));
                listNode.setTrnCrAccount(DmShared.xNull(rsTemp.getString("Trn_Credit_Ac")));
                listNode.setTrnDrAccount(DmShared.xNull(rsTemp.getString("Trn_Debit_Ac")));
                listNode.setAmount(DmServer.vNull(rsTemp.getString("Amount"), "0.00"));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("PostedBy")));
                listNode.setPreparedBy(DmShared.xNull(rsTemp.getString("PreparedBy")));
                listNode.setModifiedBy(DmShared.xNull(rsTemp.getString("ModifiedBy")));
                listNode.setEntryDtTime(DmShared.xNull(rsTemp.getString("entryDateTime")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHFromCostCenter_Mdl>> getTxtHFromCostCenter(DmHelper helper, boolean blnToEndOfFile, String strSiteCode) {
        return getTxtHFromCostCenter(helper, blnToEndOfFile, strSiteCode, false);
    }

    @Override
    public DmResultPageing<List<TxtHFromCostCenter_Mdl>> getTxtHFromCostCenter(DmHelper helper, boolean blnToEndOfFile, String strSiteCode, boolean blnInsert) {
        DmResultPageing<List<TxtHFromCostCenter_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHFromCostCenter_Mdl> lstTemp = new ArrayList<>();
        TxtHFromCostCenter_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL, strSite, siteCode;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            siteCode = DmServer.getSsnSiteCode(this);
            if (!blnInsert) {
                strSite = siteCode;
            } else {
                strSite = strSiteCode;
            }
            strSQL = "SELECT CCM.Code, CCM.Name, CCM.ManualCode, CCM.Site_Code,sm.name as siteName  "
                    + "  FROM costcentermast CCM   "
                    + "  left join siteMast as sm on sm.code = ccm.site_Code "
                    + "  WHERE isNull(CCM.UnderCostCenter, '') = '' AND CCM.isActive = 1";
            if (!blnInsert) {
                strSQL += "And isNull(CCM.Site_Code,:siteCode) = :siteCode";
            }

            sqlWithParam.setSQL(strSQL);
            if (!blnInsert) {
                sqlWithParam.setString("siteCode", strSite);
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHFromCostCenter_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSite(DmShared.xNull(rsTemp.getString("siteName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHAccounts_Mdl>> getTxtHAccount(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHAccounts_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHAccounts_Mdl> lstTemp = new ArrayList<>();
        TxtHAccounts_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT SG.SubCode, SG.Name, isNull(CT.CityName, '') AS CityName, SG.ManualCode, SG.Nature ,sgn.name as natureName "
                    + "FROM subgroup SG  "
                    + "left join subgroupNature as sgn on sgn.code= sg.nature "
                    + "LEFT JOIN city CT ON CT.CityCode = SG.CityCode  "
                    + " LEFT JOIN subgroupsitelist SBSL ON SG.subCode=SBSL.subCode  "
                    + "WHERE SBSL.Site_Code =:siteCode AND SG.isActive = 1  ");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHAccounts_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("CityName")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("natureName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> getCurrentSite() {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL, siteCode;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            siteCode = DmServer.getSsnSiteCode(this);
            strSQL = "SELECT SM.Code,SM.Name AS SiteName FROM sitemast SM "
                    + "LEFT JOIN subgroup SG ON SM.SubCode = SG.SubCode "
                    + "WHERE SM.Code = :code "
                    + "ORDER BY SM.Name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", siteCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("SiteName")));
                rstTemp.setData(listNode);
            }

            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHTransferSite_Mdl>> getTxtHTransferSite(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHTransferSite_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHTransferSite_Mdl> lstTemp = new ArrayList<>();
        TxtHTransferSite_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String siteCode;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            siteCode = DmServer.getSsnSiteCode(this);
            sqlWithParam.setSQL("SELECT SM.Code,SM.Name AS SiteName,SM.City,SM.SubCode,SG.Name AS ACName "
                    + "FROM sitemast SM "
                    + "LEFT JOIN subgroup SG ON SM.SubCode = SG.SubCode "
                    + "WHERE SM.Code <> :code ");
            sqlWithParam.setString("code", siteCode);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHTransferSite_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("SiteName")));
                listNode.setAccountName(DmShared.xNull(rsTemp.getString("ACName")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("City")));
                listNode.setSubCode(DmShared.xNull(rsTemp.getString("SubCode")));
                lstTemp.add(listNode);
            }

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }

                if (psTemp != null) {
                    psTemp.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<String> getEditCheck(String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(docId);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "SELECT COUNT(DocId) Cnt FROM LedgerM WHERE DocId = :docId AND isNull(PostedBy,'')<>''";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Posted. You Can Not Edit/ Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "SELECT COUNT(*) Cnt FROM DataAudit WHERE DocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Audited. You Can Not Edit/ Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "SELECT COUNT(Adj_DocId) Cnt FROM LedgerAdj WHERE Adj_DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Adjusted. You Can Not Edit/ Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "SELECT COUNT(*) Cnt FROM dataAudit WHERE DocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Transfered. You Can Not Edit/ Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "SELECT COUNT(DocId) Cnt FROM LedgerM WHERE LGDocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Used In Transit. You Can Not Edit / Delete This Record.");
            }

            rsTemp.close();
            psTemp.close();
            dmrTemp.setStatus(true);
        } catch (SQLException | UnsupportedOperationException e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> setApproval(String approvedBy, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(docId);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            if (DmShared.xNull(approvedBy).trim().equalsIgnoreCase("")) {
                strSQL = "UPDATE LedgerM SET  "
                        + "         postedBy = :userName "
                        + "         WHERE docid = :docId "
                        + "         AND  isNull(postedBy,'') = '' ";
            } else {
                strSQL = "UPDATE LedgerM SET  "
                        + "         postedBy = NULL "
                        + "         WHERE docid = :docId "
                        + "         AND  :userName IN (isNull(postedBy,''),'SA','SUPER') ";

            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", docId);
            psTemp.setString("userName", DmServer.getSsnUserName(this));
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> getVoucherType() {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        String vTypeCode = "";
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            vTypeCode = DmServer.getVType(vTypeSName, cnTemp);
            dmrTemp.setData(vTypeCode);

            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (SQLException | UnsupportedOperationException e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }
}
