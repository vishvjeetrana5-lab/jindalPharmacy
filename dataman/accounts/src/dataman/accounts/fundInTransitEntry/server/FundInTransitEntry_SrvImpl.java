package dataman.accounts.fundInTransitEntry.server;

import dataman.accounts.fundInTransitEntry.client.FundInTransitEntry;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.fundInTransitEntry.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FundInTransitEntry_SrvImpl extends DmRSSPrivate implements FundInTransitEntry_Srv {

    private final String voucherTypefieldName = "lm.V_Type", voucherDateFieldName = "lm.V_Date";
    private LibraryVoucherCategory pvc = new LibraryVoucherCategory();
    private String vCat = "'" + pvc.FUND_IN_TRANSIT + "'";

    final String vTypeSName = "FNDIT";

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
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            sqlQuery = "SELECT LM.DocId AS SearchCode "
                    + "FROM LedgerM LM "
                    + "left join voucher_type vt on lm.v_type = vt.v_type "
                    + " WHERE vt.category = " + vCat
                    + "  AND LM.V_Prefix = :vPrefix "
                    + "  AND LM.Site_Code = :siteCode ";
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlQuery += " ORDER BY LM.V_Date, vt.short_Name,LM.V_No, LM.RecId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("vTypeSName", vTypeSName);
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
    public DmResult<FundInTransitEntry_Mdl> getMoveRec(String strKey) {
        DmResult<FundInTransitEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL, recIdTemp;
        FundInTransitEntry_Mdl mrTemp = new FundInTransitEntry_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "SELECT LM.V_Prefix, LM.PreparedBy, LM.PostedBy, LM.U_Name, LG.DocID, LG.RecID, Lg.v_No, Lg.recIdPrefix , LG.V_DAte, LG.V_Type, LG.Narration, LG.AmtCr, LG.SubCode, "
                    + "  LG.ContraSub, LG.Site_Code, isNull(LG.Chq_No, '') AS Chq_No, LG.Chq_Date, LG.Clg_Date, SGC.Name AS CreditPartyName, SGD.Name AS DebitPartyName, "
                    + "  SM.Name AS SiteName, CCM.Name AS CostCenterName, LGC.CostCenter, LM.LGDocId, TLM.RecId AS TLMRecId, TLM.V_Type AS TLMV_Type, TLM.SubCode AS TLMSubCode, "
                    + "  TLM.ContraSub AS TLMContraSub, TLM.Site_Code AS TLMSite_Code, TLM.CreditPartyName AS TLMCreditPartyName, TLM.DebitPartyName AS TLMDebitPartyName, "
                    + "  TLM.SiteName AS TLMSiteName, TLM.CostCenterName AS TLMCostCenterName, TLM.CostCenter AS TLMCostCenter, TLM.Narration AS TLMNarration, LM.u_Name "
                    + "FROM LedgerM LM  "
                    + "  LEFT JOIN Ledger LG ON LG.DocID = LM.DocID  "
                    + "  LEFT JOIN LedgerCostCenter LGC ON LGC.DocID = LG.DocId AND LGC.V_SNo = LG.V_SNo  "
                    + "  LEFT JOIN SubGroup SGC ON SGC.SubCode = LG.SubCode  "
                    + "  LEFT JOIN SubGroup SGD ON SGD.SubCode = LG.ContraSub  "
                    + "  LEFT JOIN SiteMast SM ON SM.Code = LG.Site_Code  "
                    + "  LEFT JOIN CostCenterMast CCM ON CCM.Code = LGC.CostCenter  "
                    + "  LEFT JOIN (SELECT LG.DocId, LG.RecId, LG.V_Type, LG.SubCode, LG.ContraSub, LG.Site_Code, SGC.Name AS CreditPartyName, SGD.Name AS DebitPartyName,  "
                    + "		SM.Name AS SiteName, CCM.Name AS CostCenterName, LGC.CostCenter, LG.Narration  "
                    + "	FROM Ledger LG  "
                    + "	LEFT JOIN LedgerCostCenter LGC ON LGC.DocID = LG.DocId AND LGC.V_SNo = LG.V_SNo  "
                    + "	LEFT JOIN SubGroup SGC ON SGC.SubCode = LG.SubCode  "
                    + "	LEFT JOIN SubGroup SGD ON SGD.SubCode = LG.ContraSub  "
                    + "	LEFT JOIN SiteMast SM ON SM.Code = LG.Site_Code  "
                    + "	LEFT JOIN CostCenterMast CCM ON CCM.Code = LGC.CostCenter  "
                    + "	WHERE isNull(LG.AmtCr, 0) > 0) TLM ON TLM.DocId = LM.LGDocId  "
                    + "WHERE LG.DocID = :docId AND isNull(LG.AmtCr, 0) > 0 ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocID")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("RecID")));
                mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getObjRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getObjRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setAmount(DmServer.vNull(rsTemp.getString("AmtCr"), "0.00"));
                mrTemp.setChqNo(DmShared.xNull(rsTemp.getString("Chq_No")));
                mrTemp.setCostCenterTag(DmShared.xNull(rsTemp.getString("CostCenter")));
                mrTemp.setCostCenterText(DmShared.xNull(rsTemp.getString("CostCenterName")));
                mrTemp.setCrAccountTag(DmShared.xNull(rsTemp.getString("SubCode")));
                mrTemp.setCrAccountText(DmShared.xNull(rsTemp.getString("CreditPartyName")));
                mrTemp.setDrAccountTag(DmShared.xNull(rsTemp.getString("ContraSub")));
                mrTemp.setDrAccountText(DmShared.xNull(rsTemp.getString("DebitPartyName")));
                mrTemp.setDtChq(DmShared.dNull(rsTemp.getDate("Chq_Date")));
                mrTemp.setDtClearing(DmShared.dNull(rsTemp.getDate("Clg_Date")));
                mrTemp.setDtEntry(DmShared.dNull(rsTemp.getDate("V_DAte")));
                mrTemp.setVoucherPrefix(DmShared.xNull(rsTemp.getString("V_Prefix")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("V_Type")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("Narration")));
                mrTemp.setCurrentSiteTag(DmShared.xNull(rsTemp.getString("Site_Code")));
                mrTemp.setCurrentSiteText(DmShared.xNull(rsTemp.getString("SiteName")));
                mrTemp.setTransCostCenterTag(DmShared.xNull(rsTemp.getString("TLMCostCenter")));
                mrTemp.setTransCostCenterText(DmShared.xNull(rsTemp.getString("TLMCostCenterName")));
                mrTemp.setTransCrAccountTag(DmShared.xNull(rsTemp.getString("TLMSubCode")));
                mrTemp.setTransCrAccountText(DmShared.xNull(rsTemp.getString("TLMCreditPartyName")));
                mrTemp.setTransDrAccountTag(DmShared.xNull(rsTemp.getString("TLMContraSub")));
                mrTemp.setTransDrAccountText(DmShared.xNull(rsTemp.getString("DebitPartyName")));
                mrTemp.setTransRemark(DmShared.xNull(rsTemp.getString("TLMNarration")));
                mrTemp.setTransSiteTag(DmShared.xNull(rsTemp.getString("TLMSite_Code")));
                mrTemp.setTransSiteText(DmShared.xNull(rsTemp.getString("TLMSiteName")));
                mrTemp.setTransferNoTag(DmShared.xNull(rsTemp.getString("LGDocId")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("PreparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("PostedBy")));

                recIdTemp = DmShared.xNull(rsTemp.getString("TLMRecId")) + "/" + DmShared.xNull(rsTemp.getString("TLMV_Type")) + "/" + DmShared.xNull(rsTemp.getString("TLMSite_Code"));

                mrTemp.setTransferNoText(recIdTemp);
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
    public DmResult<String> saveRecord(UserPermission userPermission, FundInTransitEntry_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL, userName, contraText;
        ResultSet rsTemp = null;
        String vPrefix, vType;
        int intVSno = 0;
        boolean autoApprove = false;
        dmrTemp.setData("");

        try {
            FundInTransitEntry_Vld.chkSave(mrTemp);
            autoApprove = haveControlPermission(userPermission, "", "", FundInTransitEntry.approvedByBlog);
            vPrefix = mrTemp.getVoucherPrefix();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            userName = DmServer.getSsnUserName(this);
            spBegin = new DmSavePoint(cnTemp);
//            vType = DmServer.getVType(vTypeSName, cnTemp);
            vType = mrTemp.getVoucherTypeCode();
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

            strSQL = "SELECT COUNT(LM.DocId) AS Cnt FROM LedgerM LM WHERE LM.DocId = :docId AND isNull(LM.PostedBy,'') <> '' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", mrTemp.getTransferNoTag());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") <= 0) {
                throw new UnsupportedOperationException("Transfer No. Not Found.");
            }
            rsTemp.close();
            psTemp.close();

            strSQL = "Select Count(LM.DocId) As Cnt From LedgerM LM Where LM.DocId <> :docId And LM.LGDocId = :transferTag ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("transferTag", mrTemp.getTransferNoTag());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") >= 1) {
                throw new UnsupportedOperationException("Transfer No. Already Is In Use.");
            }
            rsTemp.close();
            psTemp.close();

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
                strSQL = "INSERT INTO LedgerM ( DocId, V_Type, V_Prefix, Site_Code, V_Date, SubCode, Narration,"
                        + " RecId, recIdPrefix, v_No,  U_EntDt, LGDocId, PreparedBy, PostedBy) "
                        + " VALUES( :docId, :vType, :vPrefix, :siteCode, :vDate, :subCode, :narration,"
                        + " :recId, :recIdPrefix, :v_No,  :uEntDt, :transferNo, :userName, :PostedBy) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("vType", vType);
                psTemp.setString("PostedBy", (autoApprove ? userName : null));
                psTemp.setString("vPrefix", DmShared.xNull(mrTemp.getVoucherPrefix()));
                psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getCurrentSiteTag()));
                psTemp.setDate("vDate", DmServer.convertDate(DmShared.dNull(mrTemp.getDtEntry())));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getCrAccountTag()));
                psTemp.setString("narration", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                psTemp.setString("userName", userName);
                psTemp.setString("transferNo", DmShared.foriegnKey(mrTemp.getTransferNoTag()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            } else {
                strSQL = "UPDATE LedgerM SET "
                        + "V_Date = :vDate ,"
                        + "SubCode = :subCode ,"
                        + "Narration = :narration ,"
                        + "PostedBy = :PostedBy ,"
                        + "LGDocId = :transferNo ,"
                        + "transferDt = null ,"
                        + "U_Name = :userName ,"
                        + "U_EntDt = :uEntDt "
                        + "WHERE DocId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setDate("vDate", DmServer.convertDate(DmShared.dNull(mrTemp.getDtEntry())));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getCrAccountTag()));
                psTemp.setString("narration", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("PostedBy", (autoApprove ? userName : null));
                psTemp.setString("transferNo", DmShared.foriegnKey(mrTemp.getTransferNoTag()));
                psTemp.setString("userName", userName);
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            //PrepareContraText 'Dr'
            contraText = DmServer.prepareContraText(DmShared.xNull(mrTemp.getDrAccountText()), DmShared.vNull(mrTemp.getAmount()), "Dr");
            intVSno = intVSno + 1;
            strSQL = "INSERT INTO Ledger ( DocId, RecId, V_SNo, V_Date, SubCode, ContraSub, AmtDr, AmtCr, Narration, V_Type, V_Prefix, "
                    + "Site_Code, Chq_No, Chq_Date, ContraText, Clg_Date,favourOf, recIdPrefix, v_No) "
                    + " VALUES ( :docId, :recId, :vSno, :vDate, :subCode, :ContraSub, NULL, :amount, :narration, :vType, :vPrefix, "
                    + ":siteCode, :chqNo, :chqDate, :contraText, :clgDate,:favourOf, :recIdPrefix, :v_No)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("recId", mrTemp.getSharedKeyFieldName());
            psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
            psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
            psTemp.setInt("vSno", intVSno);
            psTemp.setDate("vDate", DmServer.convertDate(DmShared.dNull(mrTemp.getDtEntry())));
            psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getCrAccountTag()));
            psTemp.setString("contraSub", DmShared.foriegnKey(mrTemp.getDrAccountTag()));
            psTemp.setString("amount", DmServer.vNull(mrTemp.getAmount(), "0.00"));
            psTemp.setString("narration", DmShared.xNull(mrTemp.getRemark()));
            psTemp.setString("vType", vType);
            psTemp.setString("vPrefix", vPrefix);
            psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getCurrentSiteTag()));
            psTemp.setString("chqNo", DmShared.xNull(mrTemp.getChqNo()));
            psTemp.setDate("chqDate", DmServer.convertDate(DmShared.dNull(mrTemp.getDtChq())));
            psTemp.setString("contraText", contraText);
            psTemp.setDate("clgDate", DmServer.convertDate(DmShared.dNull(mrTemp.getDtClearing())));
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
            psTemp.setDate("vDate", DmServer.convertDate(DmShared.dNull(mrTemp.getDtEntry())));
            psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getDrAccountTag()));
            psTemp.setString("contraSub", DmShared.foriegnKey(mrTemp.getCrAccountTag()));
            psTemp.setString("amtDr", DmServer.vNull(mrTemp.getAmount(), "0.00"));
            psTemp.setString("narration", DmShared.xNull(mrTemp.getRemark()));
            psTemp.setString("vType", vType);
            psTemp.setString("vPrefix", vPrefix);
            psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getCurrentSiteTag()));
            psTemp.setString("chqNo", DmShared.xNull(mrTemp.getChqNo()));
            psTemp.setDate("chqDate", DmServer.convertDate(DmShared.dNull(mrTemp.getDtChq())));
            psTemp.setString("contraText", contraText);
            psTemp.setDate("clgDate", DmServer.convertDate(DmShared.dNull(mrTemp.getDtClearing())));
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
//                psTemp.setString("chqNo", DmShared.xNull(mrTemp.getChqNo()));
//                psTemp.setDate("chqDt", DmServer.convertDate(DmShared.dNull(mrTemp.getDtChq())));
//                psTemp.setString("amount", DmServer.vNull(mrTemp.getAmount(), "0.00"));
//                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
//                psTemp.executeUpdate();
//                psTemp.close();
//            }
//            if (DmShared.xNull(mrTemp.getDrAccountDesc()).equalsIgnoreCase("Bank")) {
//                strSQL = "Insert Into BankReco( DocID, V_SNo, Bank_V_SNo, Favourof, Chq_No, Chq_Date, AmtCr, AmtDr, Remark)"
//                        + " Values( :docId, :vSno - 1, :vSno, :favourOf, :chqNo, :chqDt, Null, :amount, :remark)";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
//                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
//                psTemp.setInt("vSno", intVSno);
//                psTemp.setString("favourOf", DmShared.xNull(mrTemp.getCrAccountText()));
//                psTemp.setString("chqNo", DmShared.xNull(mrTemp.getChqNo()));
//                psTemp.setDate("chqDt", DmServer.convertDate(DmShared.dNull(mrTemp.getDtChq())));
//                psTemp.setString("amount", DmServer.vNull(mrTemp.getAmount(), "0.00"));
//                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
//                psTemp.executeUpdate();
//                psTemp.close();
//            }
            if (!DmShared.xNull(mrTemp.getTransCostCenterTag()).equalsIgnoreCase("")) {
                strSQL = "INSERT INTO LedgerCostCenter (DocId,V_SNo,SNo,CostCenter,AmtDr,AmtCr) "
                        + "SELECT  LG.DocId,LG.V_SNo,LG.V_SNo AS SNo, :fromCostCenter AS CostCenter,LG.AmtDr,LG.AmtCr "
                        + "FROM Ledger LG "
                        + "WHERE LG.DocId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("fromCostCenter", DmShared.foriegnKey(mrTemp.getTransCostCenterTag()));
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
            strSQL = "SELECT COUNT(DocId) Cnt FROM LedgerM WHERE DocId = :docId AND isNull(PostedBy, '') <> ''";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Posted. You Can Not Edit/ Delete This Record.");
            }

            strSQL = "SELECT COUNT(*) Cnt FROM DataAudit WHERE DocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Audited. You Can Not Edit/ Delete This Record.");
            }

            strSQL = "SELECT COUNT(Adj_DocId) Cnt FROM LedgerAdj WHERE Adj_DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Adjusted. You Can Not Edit/ Delete This Record.");
            }

            strSQL = "SELECT COUNT(*) Cnt FROM dataAudit WHERE DocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Transfered. You Can Not Edit/ Delete This Record.");
            }

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
        String siteCode, sqlCondition, sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            siteCode = DmServer.getSsnSiteCode(this);
            sqlQuery = "SELECT LM.DocId, LG.RecId AS Entry_No, LG.V_Type AS Entry_Type, LG.V_Date AS Entry_Date, LG.Narration AS Remark, LG.AmtCr AS Amount,"
                    + "  isNull(LG.Chq_No, '') AS Chq_No, LG.Chq_Date, LG.Clg_Date, SGC.Name AS Credit_Ac, SGD.Name AS Debit_Ac, CCM.Name AS Cost_Center_Name,"
                    + "  LM.PreparedBy, LM.PostedBy, LM.U_Name, TLM.SiteName AS Transfer_From_Site, TLM.RecId AS Trn_RecId, TLM.V_Type AS Trn_Type,"
                    + "  TLM.CreditPartyName AS Trn_Credit_Ac, TLM.DebitPartyName AS Trn_Debit_Ac,CCM.Name AS CostCenterName, "
                    + "  TLM.CostCenterName AS Trn_Cost_Center_Name, FORMAT(lm.u_EntDt,'dd-MMM-yyyy HH.mm') AS entryDateTime,  "
                    + "  LM.V_Date, vt.short_Name,LM.V_No "
                    + " FROM LedgerM LM "
                    + "  LEFT JOIN Ledger LG ON LG.DocID = LM.DocID "
                    + "  LEFT JOIN voucher_type vt on LG.v_type = vt.v_Type "
                    + "  LEFT JOIN LedgerCostCenter LGC ON LGC.DocID = LG.DocId AND LGC.V_SNo = LG.V_SNo "
                    + "  LEFT JOIN SubGroup SGC ON SGC.SubCode = LG.SubCode "
                    + "  LEFT JOIN SubGroup SGD ON SGD.SubCode = LG.ContraSub "
                    + "  LEFT JOIN CostCenterMast CCM ON CCM.Code = LGC.CostCenter "
                    + "  LEFT JOIN (SELECT LG.DocId, LG.RecId, LG.V_Type, LG.SubCode, LG.ContraSub, LG.Site_Code, SGC.Name AS CreditPartyName, SGD.Name AS DebitPartyName,"
                    + "      SM.Name AS SiteName, CCM.Name AS CostCenterName, LGC.CostCenter "
                    + "    FROM Ledger LG "
                    + "      LEFT JOIN LedgerCostCenter LGC ON LGC.DocID = LG.DocId AND LGC.V_SNo = LG.V_SNo "
                    + "      LEFT JOIN SubGroup SGC ON SGC.SubCode = LG.SubCode "
                    + "      LEFT JOIN SubGroup SGD ON SGD.SubCode = LG.ContraSub "
                    + "      LEFT JOIN SiteMast SM ON SM.Code = LG.Site_Code "
                    + "      LEFT JOIN CostCenterMast CCM ON CCM.Code = LGC.CostCenter "
                    + "         WHERE isNull(LG.AmtCr, 0) > 0"
                    + "         ) TLM ON TLM.DocId = LM.LGDocId "
                    + " WHERE LM.Site_Code = :siteCode AND LM.V_Prefix = :vPrefix "
                    + " AND vt.category = " + vCat
                    + " AND isNull(LG.AmtCr, 0) > 0 ";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);

            sqlWithParam.setString("siteCode", siteCode);
            sqlWithParam.setString("vPrefix", vPrefix);
//            sqlWithParam.setString("vTypeSName", vTypeSName);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                listNode.setChqNo(DmShared.xNull(rsTemp.getString("Chq_No")));
                listNode.setCostCenter(DmShared.xNull(rsTemp.getString("CostCenterName")));
                listNode.setCrParty(DmShared.xNull(rsTemp.getString("Credit_Ac")));
                listNode.setDrParty(DmShared.xNull(rsTemp.getString("Debit_Ac")));
                listNode.setDtChq(DmShared.dNull(rsTemp.getDate("Chq_Date")));
                listNode.setDtClearing(DmShared.dNull(rsTemp.getDate("Clg_Date")));
                listNode.setVDate(DmShared.dNull(rsTemp.getDate("Entry_Date")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Entry_No")));
                listNode.setNarration(DmShared.xNull(rsTemp.getString("Remark")));
                listNode.setTrnCostCenter(DmShared.xNull(rsTemp.getString("Trn_Cost_Center_Name")));
                listNode.setTrnCrParty(DmShared.xNull(rsTemp.getString("Trn_Credit_Ac")));
                listNode.setTrnDrParty(DmShared.xNull(rsTemp.getString("Trn_Debit_Ac")));
                listNode.setAmtCr(DmServer.vNull(rsTemp.getString("Amount"), "0.00"));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("PostedBy")));
                listNode.setPreparedBy(DmShared.xNull(rsTemp.getString("PreparedBy")));
                listNode.setModifiedBy(DmShared.xNull(rsTemp.getString("U_Name")));
                listNode.setEntryDtTime(DmShared.xNull(rsTemp.getString("entryDateTime")));
                listNode.setTrnSite(DmShared.xNull(rsTemp.getString("Transfer_From_Site")));
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
    public DmResultPageing<List<TxtHCostCenter_Mdl>> getTxtHCostCenter(DmHelper helper, boolean blnToEndOfFile, String strSiteCode) {
        return getTxtHCostCenter(helper, blnToEndOfFile, strSiteCode, false);
    }

    @Override
    public DmResultPageing<List<TxtHCostCenter_Mdl>> getTxtHCostCenter(DmHelper helper, boolean blnToEndOfFile, String strSiteCode, boolean blnInsert) {
        DmResultPageing<List<TxtHCostCenter_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCostCenter_Mdl> lstTemp = new ArrayList<>();
        TxtHCostCenter_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL, strSite, siteCode;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            siteCode = DmServer.getSsnSiteCode(this);
            if (blnInsert) {
                strSite = siteCode;
            } else {
                strSite = strSiteCode;
            }
            strSQL = "SELECT CCM.Code, CCM.Name, CCM.ManualCode, CCM.Site_Code,sm.name as siteName  "
                    + "  FROM costcentermast CCM   "
                    + "  left join siteMast as sm on sm.code = ccm.site_Code  "
                    + "WHERE isNull(CCM.UnderCostCenter, '') = '' AND CCM.isActive = 1 ";
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
                listNode = new TxtHCostCenter_Mdl();
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
                    + " FROM subgroup SG  "
                    + " left join subgroupNature as sgn on sgn.code= sg.nature "
                    + " LEFT JOIN city CT ON CT.CityCode = SG.CityCode  "
                    + " LEFT JOIN subgroupsitelist SBSL ON SG.subCode=SBSL.subCode  "
                    + " WHERE SBSL.Site_Code =:siteCode AND SG.isActive = 1  ");

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
    public DmResultPageing<List<TxtHTransferNo_Mdl>> getTxtHTransferNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHTransferNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHTransferNo_Mdl> lstTemp = new ArrayList<>();
        TxtHTransferNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String siteCode, sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            siteCode = DmServer.getSsnSiteCode(this);

            sqlQuery = "SELECT  LM.DocId, LM.RecId, LM.V_Type, LM.V_Date, LM.Site_Code, SM.Name AS SName, ISNULL(LG.AmtCr, 0) AS Amount, "
                    + "		LG.SubCode, LG.ContraSub, SG.Name AS SubName, CSG.Name AS ContraName, LGC.CostCenter, CCM.Name AS CCMName, LG.Chq_No, "
                    + "		LG.Chq_Date, SSG.SubCode AS DefaultCode, SSG.Name AS DefaultName, LG.Narration  "
                    + "		FROM LedgerM LM  "
                    + "		LEFT JOIN LedgerM TLM ON LM.DocId = TLM.LGDocId  "
                    + "		LEFT JOIN Ledger LG ON LM.DocId = LG.DocId  "
                    + "		LEFT JOIN LedgerCostCenter LGC ON LM.DocId = LGC.DocId AND LGC.V_SNo = LG.V_SNo  "
                    + "		LEFT JOIN Voucher_Type VT ON VT.V_Type = LM.V_Type  "
                    + "		LEFT JOIN SiteMast SM ON SM.Code = LM.Site_Code  "
                    + "		LEFT JOIN SubGroup SSG ON SSG.SubCode = SM.SubCode  "
                    + "		LEFT JOIN SubGroup SG ON SG.SubCode = LG.SubCode  "
                    + "		LEFT JOIN SubGroup CSG ON CSG.SubCode = LG.ContraSub  "
                    + "		LEFT JOIN CostCenterMast CCM ON CCM.Code = LGC.CostCenter  "
                    + "		WHERE VT.Category = 'FNDTR' AND ISNULL(TLM.DocId, '') = '' AND ISNULL(LG.AmtCr, 0) > 0 AND LM.To_Site_Code = :To_Site_Code AND ISNULL(LM.PostedBy, '') <> '' ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("To_Site_Code", siteCode);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHTransferNo_Mdl();
                listNode.setAmount(DmServer.vNull(rsTemp.getString("Amount"), "0.00"));
                listNode.setCCName(DmShared.xNull(rsTemp.getString("CCMName")));
                listNode.setChqNo(DmShared.xNull(rsTemp.getString("Chq_No")));
                listNode.setContraName(DmShared.xNull(rsTemp.getString("ContraName")));
                listNode.setContraSub(DmShared.xNull(rsTemp.getString("ContraSub")));
                listNode.setCostCenter(DmShared.xNull(rsTemp.getString("CostCenter")));
                listNode.setDefaultCode(DmShared.xNull(rsTemp.getString("DefaultCode")));
                listNode.setDefaultName(DmShared.xNull(rsTemp.getString("DefaultName")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                listNode.setDtChq(DmShared.dNull(rsTemp.getDate("Chq_Date")));
                listNode.setNarration(DmShared.xNull(rsTemp.getString("Narration")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("RecId")));
                listNode.setSName(DmShared.xNull(rsTemp.getString("SName")));
                listNode.setSiteCode(DmShared.xNull(rsTemp.getString("Site_Code")));
                listNode.setSubCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSubName(DmShared.xNull(rsTemp.getString("SubName")));
                listNode.setVDate(DmShared.dNull(rsTemp.getDate("V_Date")));
                listNode.setVType(DmShared.xNull(rsTemp.getString("V_Type")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
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
    public DmResult<String> getEditCheck(String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(docId);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQL = "SELECT COUNT(DocId) Cnt FROM LedgerM WHERE DocId = :docId AND isNull(PostedBy, '') <> ''";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Posted. You Can Not Edit/ Delete This Record.");
            }

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

    @Override
    public DmResult<String> setApproval(String approvedBy, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        int approved;
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
            approved = psTemp.executeUpdate();

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
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, vTypeCode = "";

//        String vTypeCode = "";
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT TOP 1"
                    + "   vt.v_Type AS code"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = " + vCat
                    + " AND vt.isActive = 1";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                vTypeCode = DmShared.xNull(rsTemp.getString("code"));

            }
            rsTemp.close();
            psTemp.close();

//            vTypeCode = DmServer.getVType(vTypeSName, cnTemp);
            dmrTemp.setData(vTypeCode);

            cnTemp.close();
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

}
