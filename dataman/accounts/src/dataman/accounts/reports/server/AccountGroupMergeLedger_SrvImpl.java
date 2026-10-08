package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.AccountGroupMergeLedger_Model;
import dataman.accounts.reports.shared.AccountGroupMergeLedger_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class AccountGroupMergeLedger_SrvImpl extends DmRSSPrivate implements AccountGroupMergeLedger_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT Sm.Code, Sm.Name FROM Sitemast Sm "
                    + "WHERE CODE IN (" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ") ");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHContraACNeeded(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Y");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<AccountGroupMergeLedger_Model.TxtHAccountGroup>> getTxtHAccountGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<AccountGroupMergeLedger_Model.TxtHAccountGroup>> rstTemp = new DmResultPageing<>();
        List<AccountGroupMergeLedger_Model.TxtHAccountGroup> lstTemp = new ArrayList<>();
        AccountGroupMergeLedger_Model.TxtHAccountGroup listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT AG.GroupCode, AG.GroupName, AG1.GroupName AS GrpUnder "
                    + "FROM AcGroup AG "
                    + "  LEFT JOIN AcGroup AG1 ON AG1.GroupCode = AG.GroupUnder  ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new AccountGroupMergeLedger_Model.TxtHAccountGroup();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("GroupCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("GroupName")));
                listNode.setGroupUnder(DmShared.xNull(rsTemp.getString("GrpUnder")));
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
    public DmResultPageing<List<AccountGroupMergeLedger_Model.TxtHVoucherType>> getTxtHVoucherTyep(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<AccountGroupMergeLedger_Model.TxtHVoucherType>> rstTemp = new DmResultPageing<>();
        List<AccountGroupMergeLedger_Model.TxtHVoucherType> lstTemp = new ArrayList<>();
        AccountGroupMergeLedger_Model.TxtHVoucherType listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT VT.V_Type AS CODE, VT.V_Type, VT.Description "
                    + " FROM Voucher_Type VT "
                    + " WHERE VT.V_Type IN (SELECT V_Type FROM Ledger WHERE "
                    + " Site_code IN (" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ")) ");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new AccountGroupMergeLedger_Model.TxtHVoucherType();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
                listNode.setType(DmShared.xNull(rsTemp.getString("V_Type")));
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
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResultPageing<KeyFieldExtended> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended mrTemp = new KeyFieldExtended();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();

            dmrTemp.setStatus(true);
            cnTemp.close();
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
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String strCondition1, strConditionOP, strConditionSite, strSQLQuery;

        try {
            AccountGroupMergeLedger_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strCondition1 = " WHERE (LG.V_Date BETWEEN :fromDate AND :toDate) ";
            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strCondition1 += " AND (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') THEN LG.V_Prefix ELSE "
                    + " :voucherPrefix END) = :voucherPrefix ";
            //=================================================================================

            strConditionOP = " WHERE LG.V_Date < :fromDate ";
            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strConditionOP += " AND (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') THEN LG.V_Prefix ELSE "
                    + " :voucherPrefix END) = :voucherPrefix ";
            //=================================================================================

            strConditionSite = "";
            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " AND (isNull(CAST(SG.GroupCode AS VARCHAR), '') IN (SELECT isNull(CAST(AGP.GroupCode AS VARCHAR), '') "
                        + "  FROM AcGroupPath AGP WHERE AGP.GroupUnder IN (" + DmServer.getParamList(":accountGroup", DmShared.xNull(filter.get("accountGroup").getTag()))
                        + ")) OR SG.GroupCode IN (" + DmServer.getParamList(":accountGroup", DmShared.xNull(filter.get("accountGroup").getTag())) + ")) ";
                strConditionOP += " AND (isNull(CAST(SG.GroupCode AS VARCHAR), '') IN (SELECT isNull(CAST(AGP.GroupCode AS VARCHAR), '') "
                        + "  FROM AcGroupPath AGP WHERE AGP.GroupUnder IN (" + DmServer.getParamList(":accountGroup", DmShared.xNull(filter.get("accountGroup").getTag()))
                        + ")) OR SG.GroupCode IN (" + DmServer.getParamList(":accountGroup", DmShared.xNull(filter.get("accountGroup").getTag())) + ")) ";
            }

            if (!DmShared.xNull(filter.get("voucherType").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " AND LG.V_Type IN (" + DmServer.getParamList(":voucherType", DmShared.xNull(filter.get("voucherType").getTag())) + ") ";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strConditionSite = " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strConditionSite = " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            //========== For Detail Section =======
            strSQLQuery = "SELECT LG.V_Type, LG.RecID AS RecID, LG.V_Date,VType.short_Name As VTypeShortName,LG.V_Prefix, SG.Name AS PName, LG.SubCode, LG.Narration, isnull(LG.AmtDr,0) As AmtDr, isnull(LG.AmtCr,0) As AmtCr , 0 AS AmtDr_OP, 0 AS AmtCr_OP, 1 AS SNo,"
                    + "  SM.Name AS Division, LG.ContraText AS ContraName, isnull(LG.Chq_No,'') As Chq_No, isnull(LG.Chq_Date,'') As Chq_Date, isNull(LG.Site_Code, '') AS Site_Code, AG.GroupName AS AccGrName,"
                    + "  AG.GroupCode AS GroupCode , "
                    + " LG.recidPrefix ,LG.v_no As v_no,lg.docId As docId ,LG.V_SNO As V_SNO ,SGC.Name As SGCName , "
                    + "isNull(LG.RefNo,'') as refNo,isnull(FORMAT(LG.RefDate, 'dd-MMM-yyyy'),'') as refDate,LG.ContraText As ContraText  "
                    + "FROM Ledger LG  "
                    + "LEFT JOIN SubGroup SG ON LG.Subcode = SG.SubCode  "
                    + "LEFT JOIN SubGroup SGC ON SGC.Subcode = LG.ContraSub  "
                    + "LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode  "
                    + "LEFT JOIN Sitemast SM ON LG.Site_Code = SM.Code  "
                    + "LEFT JOIN Voucher_type VType ON Vtype.V_Type=LG.V_Type   "
                    + strCondition1 + strConditionSite
                    + "UNION ALL "
                    + "SELECT NULL AS V_Type, NULL AS RecID, NULL AS V_Date, '' As VTypeShortName , NULL AS V_Prefix, MAX(SG.Name) AS PName, MAX(LG.SubCode) AS SubCode, '' AS Narration,0 As AmtDr, 0 As AmtCr ,  "
                    + "  (CASE WHEN (isNull(SUM(LG.AmtDr), 0) - isNull(SUM(LG.AmtCr), 0)) > 0 THEN (isNull(SUM(LG.AmtDr), 0) - isNull(SUM(LG.AmtCr), 0)) ELSE 0 END) AS AmtDr_OP,"
                    + "  (CASE WHEN (isNull(SUM(LG.AmtCr), 0) - isNull(SUM(LG.AmtDr), 0)) > 0 THEN (isNull(SUM(LG.AmtCr), 0) - isNull(SUM(LG.AmtDr), 0)) ELSE 0 END) AS AmtCr_OP,"
                    + "  0 AS SNo,MAX(SM.name) AS Division, NULL AS ContraName, NULL AS Chq_No, NULL AS Chq_Date, NULL AS Site_Code, MAX(AG.GroupName) AS AccGrName, MAX(AG.GroupCode) AS GroupCode ,"
                    + " '' As recidPrefix ,Null As v_no, '' as docId,''  As V_SNO , 'OPENING BALANCE' As SGCName , "
                    + "'' as refNo,'' as refDate,  '' As ContraText  "
                    + "FROM Ledger LG "
                    + "  LEFT JOIN SubGroup SG ON LG.Subcode = SG.SubCode "
                    + "  LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode "
                    + "  LEFT JOIN Sitemast SM ON LG.Site_Code = SM.Code "
                    + strConditionOP + strConditionSite
                    + " GROUP BY LG.SubCode  "
                    + " ORDER BY AccGrName,GroupCode,V_Date ,recidPrefix,v_no,RecID,docId,V_SNO ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }

            if (!DmShared.xNull(filter.get("voucherType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("voucherType", DmShared.xNull(filter.get("voucherType").getTag()));
            }

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("accountGroup", DmShared.xNull(filter.get("accountGroup").getTag()), true);
            }

            psTemp.setString("voucherPrefix", DmShared.xNull(DmServer.getSsnVoucherPrefix(this)), true);

            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();
            hm.put("isContraNeeded", filter.get("contraAccount").getTag().trim().equalsIgnoreCase("Y") ? 1 : 0);

            jasperReport = DmServer.compileJasper("AC_AccountGrMergeLedger");
            jasperPrint = DmServer.fillJasper(this, new lmAccounts().ACCOUNT_GROUP_MERGE_LEDGER.caption, jasperReport, hm, filter, rsTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setStatus(false);
            rstTemp.setErrorMessage(e.getMessage());
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

}
