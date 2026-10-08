package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.Annexure_Model;
import dataman.accounts.reports.shared.Annexure_Srv;
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

public class Annexure_SrvImpl extends DmRSSPrivate implements Annexure_Srv {

    @Override
    public DmResultPageing<List<Annexure_Model.TxtHAccountGroup>> getTxtHAccountGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Annexure_Model.TxtHAccountGroup>> rstTemp = new DmResultPageing<>();
        List<Annexure_Model.TxtHAccountGroup> lstTemp = new ArrayList<>();
        Annexure_Model.TxtHAccountGroup listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT AG.groupcode,AG.GroupName AS NAME,"
                    + " (CASE WHEN GroupNature='0' THEN 'Liabilities' "
                    + " WHEN GroupNature='1' THEN 'Assets'  "
                    + " WHEN GroupNature='2' THEN 'Expenses Direct' "
                    + " WHEN GroupNature='3' THEN 'Expenses Indirect' "
                    + " WHEN GroupNature='4' THEN 'Revenue Direct' "
                    + " WHEN GroupNature='5' THEN 'Revenue Indirect' END) MainGroup  "
                    + " FROM acgroup AG ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, null);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Annexure_Model.TxtHAccountGroup();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("groupcode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setMainGroup(DmShared.xNull(rsTemp.getString("MainGroup")));

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
            sqlWithParam.setSQL("Select Sm.Code,Sm.Name From Sitemast Sm  "
                    + " where code in (" + DmServer.getSsnSiteList(this) + ") ");
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
            sqlWithParam.setSQL("Select LG.Code,LG.Name as LegerName From LedgerGroup LG ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("LegerName")));
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
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String strCondition1, strSQLQuery, strFieldName = "GroupName", strSpace = "   ", strFieldPrefix = "";
        int intMaxHirarchy = 10;
        try {
            Annexure_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strCondition1 = " Where isNull(LG.isPosted,0)=1 And LG.V_Date<= :upToDate  ";
            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strCondition1 += " AND (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') THEN LG.V_Prefix ELSE :voucherPrefix END) = :voucherPrefix";

         

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And (SG.GroupCode In ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + ")  ";
                strCondition1 += " Or SG.GroupCode In (Select AGP2.GroupCode From AcGroupPath AGP2 ";
                strCondition1 += " Where AGP2.GroupUnder In ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + ")))  ";
            }

            if (!DmShared.xNull(filter.get("ledgerGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += "  And SG.LedgerGroup In  (" + DmServer.getParamList(":ledgerGroup", filter.get("ledgerGroup").getTag()) + ")";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strCondition1 += " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            strSQLQuery = "SELECT isNull((SELECT MAX(AG1.GroupName) FROM AcGroupPath AGP LEFT JOIN AcGroup AG1 ON AG1.GroupCode = AGP.GroupUnder  AND AGP.SNo = " + 1 + " "
                    + "    WHERE AGP.GroupCode = MAX(SG.GroupCode)), (CASE WHEN (SELECT isNull(MAX(SNo), 0) "
                    + "        FROM AcGroupPath AGP1 "
                    + "        WHERE AGP1.GroupCode = MAX(SG.GroupCode)) = " + 0 + " "
                    + "        THEN MAX(AG.GroupName) ELSE '' END)) AS " + strFieldName.concat("1") + ",";

            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, "SELECT isNull(MAX(SNo),0) AS sNo FROM AcGroupPath");
            rsTemp1 = psTemp1.executeQuery();

            rsTemp1.next();
            if (rsTemp1.getLong("sNo") > intMaxHirarchy - 1) {
                throw new UnsupportedOperationException("There Can Be Only " + (intMaxHirarchy - 1) + " A/c Group Levels. Levels Are Exceding.");
            }

            for (int i = 2; i <= rsTemp1.getLong("sNo") + 1; i++) {
                strFieldPrefix += strSpace;
                strSQLQuery += " isNull((SELECT CONCAT( '" + strFieldPrefix + "' , MAX(AG1.GroupName)) "
                        + "  FROM AcGroupPath AGP "
                        + "    LEFT JOIN AcGroup AG1 ON AG1.GroupCode = AGP.GroupUnder AND AGP.SNo = " + i + " "
                        + "  WHERE AGP.GroupCode = MAX(SG.GroupCode)), (CASE WHEN (SELECT isNull(MAX(SNo), 0) "
                        + "      FROM AcGroupPath AGP1 "
                        + "      WHERE AGP1.GroupCode = MAX(SG.GroupCode)) = " + (i - 1) + " "
                        + "THEN CONCAT('" + strFieldPrefix + "' , MAX(AG.GroupName)) ELSE '' END)) AS " + strFieldName.concat(String.valueOf(i)) + " ,";
            }

            for (long i = rsTemp1.getLong("sNo") + 2; i <= intMaxHirarchy; i++) {
                strSQLQuery += "'' As " + strFieldName.concat(String.valueOf(i)) + " , ";
            }

            strSQLQuery += "SG.Name, "
             +"(Case When isNull(Sum(LG.AmtDr),0)-isNull(Sum(LG.AmtCr),0)>0 Then "
             + "isNull(Sum(LG.AmtDr),0)-isNull(Sum(LG.AmtCr),0) Else 0 End) As AmtDr, "
             + "(Case When isNull(Sum(LG.AmtCr),0)-isNull(Sum(LG.AmtDr),0)>0 Then "
             + "isNull(Sum(LG.AmtCr),0)-isNull(Sum(LG.AmtDr),0) Else 0 End) As AmtCr "
             + "From "
             + "Ledger LG Left Join "
             + "SubGroup SG On LG.SubCode=SG.SubCode Left Join "
             + "AcGroup AG On AG.GroupCode=SG.GroupCode  "
             + strCondition1
             + " Group By SG.Name "
             + "Having (isNull(Sum(LG.AmtDr),0)-isNull(Sum(LG.AmtCr),0)) <> 0 "
             + " ORDER BY GroupName1,GroupName2,GroupName3,GroupName4,GroupName5,GroupName6,GroupName7,GroupName8,GroupName9,GroupName10 ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);

            psTemp.setDate("upToDate", DmServer.convertDate(filter.get("upToDate").getText()), true);

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("accountGroup", DmShared.xNull(filter.get("accountGroup").getTag()), true);
            }

            if (!DmShared.xNull(filter.get("ledgerGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("ledgerGroup", DmShared.xNull(filter.get("ledgerGroup").getTag()), true);
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }
            psTemp.setString("voucherPrefix", DmShared.xNull(DmServer.getSsnVoucherPrefix(this)), true);
            rsTemp = psTemp.executeQuery();

            jasperReport = DmServer.compileJasper("AC_Annexure");
            jasperPrint = DmServer.fillJasper(this, new lmAccounts().ANNEXURE.caption, jasperReport, new HashMap(), filter, rsTemp);
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
                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
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
