package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.accounts.reports.shared.LedgerGroupMergeLedger_Model;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.LedgerGroupMergeLedger_Srv;
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

public class LedgerGroupMergeLedger_SrvImpl extends DmRSSPrivate implements LedgerGroupMergeLedger_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLedgerGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select LG.Code,LG.Name From LedgerGroup LG  ");
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
            sqlWithParam.setSQL("Select Code ,Name From SiteMast ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHContraNeeded(DmHelper helper, boolean blnToEndOfFile) {
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
        String strCondition1, strConditionOp, strConditionSite, strSQLQuery;

        try {
            LedgerGroupMergeLedger_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strCondition1 = " Where isNull(LG.isPosted,0)=1 And (LG.V_Date Between :fromDate And :toDate) ";
            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strCondition1 += " And (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') Then LG.V_Prefix Else ";
            strCondition1 += ":voucherPrefix End) = :voucherPrefix ";
            //=================================================================================

            strConditionOp = " Where LG.V_Date < :fromDate ";
            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strConditionOp += " And (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') Then LG.V_Prefix Else ";
            strConditionOp += ":voucherPrefix End) = :voucherPrefix ";
            //=================================================================================

            strConditionSite = "";
            if (!DmShared.xNull(filter.get("ledgerGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 = strCondition1 + " And LGG.Code In (" + DmServer.getParamList(":ledgerGroup", DmShared.xNull(filter.get("ledgerGroup").getTag())) + ") ";
                strConditionOp = strConditionOp + " And LGG.Code In (" + DmServer.getParamList(":ledgerGroup", DmShared.xNull(filter.get("ledgerGroup").getTag())) + ") ";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strConditionSite = " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strConditionSite = " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            //========== For Detail Section =======
            strSQLQuery = "SELECT LG.V_Type, LG.RecID AS RecID, LG.V_Date,VType.short_Name As VTypeShortName,LG.V_Prefix, SG.Name AS PName, LG.SubCode, LG.Narration, isnull(LG.AmtDr,0) As AmtDr, isnull(LG.AmtCr,0) As AmtCr , 0 AS AmtDr_OP, 0 AS AmtCr_OP, 1 AS SNo,"
                    + "  SM.Name AS Division, LG.ContraText AS ContraName, isnull(LG.Chq_No,'') As Chq_No, isnull(LG.Chq_Date,'') As Chq_Date, isNull(LG.Site_Code, '') AS Site_Code,"
                    + "isNull(LGG.Name,'') As LedgerGrName,LGG.Code As LedgerGrCode ,  "
                    + " LG.recidPrefix ,LG.v_no As v_no,lg.docId As docId ,LG.V_SNO As V_SNO ,SGC.Name As SGCName , "
                    + "isNull(LG.RefNo,'') as refNo,isnull(FORMAT(LG.RefDate, 'dd-MMM-yyyy'),'') as refDate,LG.ContraText As ContraText  "
                    
                    + "From LedgerGroup LGG "
                    + "Left Join SubGroup SG On LGG.Code = SG.LedgerGroup "
                    + " LEFT JOIN AcGroup AG ON AG.GroupCode=SG.GroupCode "
                    + "Left Join Ledger LG ON LG.SubCode = SG.SubCode "
                    + "LEFT JOIN SubGroup SGC ON SGC.Subcode = LG.ContraSub "
                    + "Left Join Sitemast SM On LG.Site_Code = SM.Code "
                    + "left join Voucher_type VType ON Vtype.V_Type=LG.V_Type  "
                    
                    + strCondition1 + strConditionSite + " AND isNull "
                    + " (CAST(LGG.Code AS VARCHAR),'')<>''  "
                    + "UNION ALL "
                    + "SELECT NULL AS V_Type, NULL AS RecID, NULL AS V_Date, '' As VTypeShortName , NULL AS V_Prefix, MAX(SG.Name) AS PName, MAX(LG.SubCode) AS SubCode, '' AS Narration,0 As AmtDr, 0 As AmtCr ,  "
                    + "  (CASE WHEN (isNull(SUM(LG.AmtDr), 0) - isNull(SUM(LG.AmtCr), 0)) > 0 THEN (isNull(SUM(LG.AmtDr), 0) - isNull(SUM(LG.AmtCr), 0)) ELSE 0 END) AS AmtDr_OP,"
                    + "  (CASE WHEN (isNull(SUM(LG.AmtCr), 0) - isNull(SUM(LG.AmtDr), 0)) > 0 THEN (isNull(SUM(LG.AmtCr), 0) - isNull(SUM(LG.AmtDr), 0)) ELSE 0 END) AS AmtCr_OP,"
                    + "  0 AS SNo,MAX(SM.name) AS Division, NULL AS ContraName, NULL AS Chq_No, NULL AS Chq_Date, NULL AS Site_Code, max(isNull(LGG.Name,'')) As LedgerGrName, max(LGG.Code) As LedgerGrCode ,  "
                    + " '' As recidPrefix ,Null As v_no, '' as docId,''  As V_SNO , 'OPENING BALANCE' As SGCName , "
                    + "'' as refNo,'' as refDate,  '' As ContraText  "
   
                    + "From LedgerGroup LGG "
                    + "Left Join SubGroup SG On LGG.Code = SG.LedgerGroup "
                    + " LEFT JOIN AcGroup AG ON AG.GroupCode=SG.GroupCode "
                    + "Left Join Ledger LG ON LG.SubCode = SG.SubCode "
                    + "LEFT JOIN SubGroup SGC ON SGC.Subcode = LG.ContraSub "
                    + "Left Join Sitemast SM On LG.Site_Code = SM.Code "
                    + "left join Voucher_type VType ON Vtype.V_Type=LG.V_Type  "
     
                    + strConditionOp + strConditionSite + " AND isNull(CAST(LGG.Code AS VARCHAR),'')<>'' "
                    + " GROUP BY LG.SubCode  "
                    + " ORDER BY LedgerGrName,LedgerGrCode,V_Date ,recidPrefix,v_no,RecID,docId,V_SNO ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            psTemp.setStringList("ledgerGroup", DmShared.xNull(filter.get("ledgerGroup").getTag()), true);
            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }

            psTemp.setString("voucherPrefix", DmShared.xNull(DmServer.getSsnVoucherPrefix(this)), true);

            rsTemp = psTemp.executeQuery();

            HashMap hm = new HashMap();

            hm.put("isContraNeeded", filter.get("contraAccount").getTag().trim().equalsIgnoreCase("Y")?1:0);
           

            jasperReport = DmServer.compileJasper("AC_MergeLedger");
            jasperPrint = DmServer.fillJasper(this, new lmAccounts().LEDGER_GROUP_MERGE_LEDGER.caption, jasperReport, hm, filter, rsTemp);
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
