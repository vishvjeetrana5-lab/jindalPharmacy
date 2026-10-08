package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.MonthlyLedgerSummaryFull_Model;
import dataman.accounts.reports.shared.MonthlyLedgerSummaryFull_Srv;
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

public class MonthlyLedgerSummaryFull_SrvImpl extends DmRSSPrivate implements MonthlyLedgerSummaryFull_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select AG.GroupCode as Code ,AG.GroupName as Name From AcGroup AG ");
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
    public DmResultPageing<List<MonthlyLedgerSummaryFull_Model.TxtHAccountName>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<MonthlyLedgerSummaryFull_Model.TxtHAccountName>> rstTemp = new DmResultPageing<>();
        List<MonthlyLedgerSummaryFull_Model.TxtHAccountName> lstTemp = new ArrayList<>();
        MonthlyLedgerSummaryFull_Model.TxtHAccountName listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT SG.SubCode AS CODE, SG.Name AS NAME, Sg.ManualCode, isNull(CT.CityName, '') AS City "
                    + "FROM SubGroup SG "
                    + "  LEFT JOIN City CT ON SG.CityCode = CT.CityCode "
                    + "  LEFT JOIN subgroupsitelist SL ON SG.subcode = SL.subcode  "
                    + "  WHERE  SL.site_Code = :site_Code ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new MonthlyLedgerSummaryFull_Model.TxtHAccountName();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("City")));
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
            sqlWithParam.setSQL("Select 'o' As Tick,Sm.Code,Sm.Name From Sitemast Sm where code in (" + DmServer.getSsnSiteList(this) + ") ");
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
            MonthlyLedgerSummaryFull_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

           //=====================Date Range======================================
            strCondition1 = " Where (LG.V_Date Between :fromDate And :toDate)  ";
            strConditionOP = " Where LG.V_Date < :fromDate  ";
            
           //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strCondition1 += " And (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') Then LG.V_Prefix Else :voucherPrefix End) = :voucherPrefix  ";
            strConditionOP += " And (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') Then LG.V_Prefix Else :voucherPrefix End) = :voucherPrefix  ";
         
            strConditionSite = "";
            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 = strCondition1 + " And (isNull(CAST(SG.GroupCode AS VARCHAR),'') In (Select isNull(CAST(AGP.GroupCode AS VARCHAR),'') From AcGroupPath AGP Where AGP.GroupUnder In (" + DmServer.getParamList(":accountGroup", DmShared.xNull(filter.get("accountGroup").getTag())) + ")) Or SG.GroupCode In ( " + DmServer.getParamList(":accountGroup", DmShared.xNull(filter.get("accountGroup").getTag())) + ")) ";
                strConditionOP = strConditionOP + " And (isNull(CAST(SG.GroupCode AS VARCHAR),'') In (Select isNull(CAST(AGP.GroupCode AS VARCHAR),'') From AcGroupPath AGP Where AGP.GroupUnder In (" + DmServer.getParamList(":accountGroup", DmShared.xNull(filter.get("accountGroup").getTag())) + ")) Or SG.GroupCode In ( " + DmServer.getParamList(":accountGroup", DmShared.xNull(filter.get("accountGroup").getTag())) + ")) ";
            }

            if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 = strCondition1 + " And LG.SubCode In (" + DmServer.getParamList(":accountName", DmShared.xNull(filter.get("accountName").getTag())) + ") ";
                strConditionOP = strConditionOP + " And LG.SubCode In (" + DmServer.getParamList(":accountName", DmShared.xNull(filter.get("accountName").getTag())) + ") ";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strConditionSite = " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") And LG.isPosted = 1 ";
            } else {
                strConditionSite = " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") And LG.isPosted = 1 ";
            }

            //========== Head Query Date Wise Grouping ====================================
            strSQLQuery = "Select Max(GroupName)as GroupName,GroupCode, Max(SName) As SName, "
             + "isNull(Sum(AmtDr),0) As AmtDr, isNull(Sum(AmtCr),0) As AmtCr, "
             + "Max(Month) AS Month,Max(Narration) AS Narration,ID  "
             + "From "
            //======= For Opening Balance =========
             + "( Select isNull(CAST(SG.SubCode AS VARCHAR),'') As SubCode, Max(isNull(SG.Name,'')) As SName, "
             + "Max(isNull(AG.Groupname,''))as GroupName,AG.GroupCode, "
             + "(Case When (isNull(Sum(LG.AmtDr),0)-isNull(Sum(LG.AmtCr),0))>0 Then  "
             + "(isNull(Sum(LG.AmtDr),0)-isNull(Sum(LG.AmtCr),0)) Else 0 End) As AmtDr, "
             + "(Case When (isNull(Sum(LG.AmtCr),0)-isNull(Sum(LG.AmtDr),0))>0 Then "
             + "(isNull(Sum(LG.AmtCr),0)-isNull(Sum(LG.AmtDr),0)) Else 0 End) As AmtCr, '' AS Month,'OPENING BALANCE' As Narration,0 AS ID,0 as MON,0 as yr  "
             + "From Ledger LG  "
             + "Left Join SubGroup SG On LG.SubCode=SG.SubCode "
             + "Left Join AcGroup AG On AG.GroupCode=SG.GroupCode "
             + strConditionOP + strConditionSite
             + "Group By AG.GroupCode,isNull(CAST(SG.SubCode AS VARCHAR),'') "
            //'======= For Detail =========
             + "Union All "
             + "Select isNull(CAST(SG.SubCode AS VARCHAR),'') As SubCode, Max(isNull(SG.Name,'')) As SName,Max(isNull(AG.Groupname,''))as GroupName,AG.GroupCode, "
             + "isNull(Sum(LG.AmtDr),0) As AmtDr, "
             + "isNull(Sum(LG.AmtCr),0) As AmtCr, "
             + "Max(concat( DATENAME(month, LG.V_date) ,' ' , (YEAR(LG.V_date)))) AS Month,'' As Narration,1 AS ID,  "
             + "max(month(LG.V_date)) AS MON,max(year(LG.V_date)) AS yr "
             + "From Ledger  "
             + "LG Left Join SubGroup SG On LG.SubCode=SG.SubCode "
             + "Left Join AcGroup AG On AG.GroupCode=SG.GroupCode "
             + strCondition1 + strConditionSite
             + "Group By  AG.GroupCode,isNull(CAST(SG.SubCode AS VARCHAR),''),(concat( DATENAME(month, LG.V_date) ,' ' , (YEAR(LG.V_date)))) "
             + " ) As Tmp "
             + "Group By GroupCode,SubCode,ID,MON having isNull(CAST(SubCode AS VARCHAR),'')<>''  "
             + "Order By Max(SName),ID,MAX(Yr),MON " ;

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("accountGroup", DmShared.xNull(filter.get("accountGroup").getTag()), true);
            }

            if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("accountName", DmShared.xNull(filter.get("accountName").getTag()), true);
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }

            psTemp.setString("voucherPrefix", DmShared.xNull(DmServer.getSsnVoucherPrefix(this)), true);
            rsTemp = psTemp.executeQuery();

            jasperReport = DmServer.compileJasper("AC_MonthlyLedgerSummaryFull");
            jasperPrint = DmServer.fillJasper(this,  new lmAccounts().MONTHLY_LEDGER_SUMMARY_FULL.caption, jasperReport, new HashMap(), filter, rsTemp);
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
