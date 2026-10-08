package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.AnnexureCC_Model;
import dataman.accounts.reports.shared.AnnexureCC_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.server.DmIJasperCompiled;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class AnnexureCC_SrvImpl extends DmRSSPrivate implements AnnexureCC_Srv {

    @Override
    public DmResultPageing<List<AnnexureCC_Model.TxtHAccountGroup>> getTxtHAccountGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<AnnexureCC_Model.TxtHAccountGroup>> rstTemp = new DmResultPageing<>();
        List<AnnexureCC_Model.TxtHAccountGroup> lstTemp = new ArrayList<>();
        AnnexureCC_Model.TxtHAccountGroup listNode;
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
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new AnnexureCC_Model.TxtHAccountGroup();
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
            sqlWithParam.setSQL("Select Sm.Code,Sm.Name From Sitemast Sm where code in (" + DmServer.getSsnSiteList(this) + ")");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCostCenter(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT CC.Code, CC.Name "
                    + "FROM CostCenterMast CC "
                    + "WHERE isNull(CC.UnderCostCenter, '') = '' ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("CC");
            listNode.setSharedKeyFieldName("Cost Center");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("SC");
            listNode.setSharedKeyFieldName("Sub-Cost Center");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String strCondition1, strSQLQuery, strFieldName = "GroupName", strSpace = "   ", strFieldPrefix = "", strCCenterName = "", strCCenterCode = "", strCostCenter = "";
        int intMaxHirarchy = 10;
        try {
            AnnexureCC_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strCostCenter = DmServer.getSsnCostCenterList(this);

            strCondition1 = " WHERE isNull(LG.isPosted, 0) = 1 AND LG.V_Date <= :upToDate";

            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strCondition1 += " AND (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') THEN LG.V_Prefix ELSE :voucherPrefix END) = :voucherPrefix";
            //=================================================================================

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " AND (SG.GroupCode IN (" + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + ") OR SG.GroupCode IN"
                        + "  (SELECT AGP2.GroupCode FROM AcGroupPath AGP2 "
                        + "  WHERE AGP2.GroupUnder IN (" + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + ")))";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strCondition1 += " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            if (!DmShared.xNull(filter.get("ledgerGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " AND SG.LedgerGroup IN (" + DmServer.getParamList(":ledgerGroup", DmShared.xNull(filter.get("ledgerGroup").getTag().trim())) + ") ";

            }

            if (DmShared.xNull(filter.get("reportType").getTag().trim()).equalsIgnoreCase("CC")) {
                strCCenterName = "PM.Name";
                strCCenterCode = "PM.Code";
            } else {
                strCCenterName = "CCM.Name";
                strCCenterCode = "CCM.Code";
            }

            if (!DmShared.xNull(filter.get("costCenter").getTag()).equalsIgnoreCase("")) {
                strCondition1 += " And (LGC.Costcenter In (" + DmServer.getParamList(":costCenter", DmShared.xNull(filter.get("costCenter").getTag())) + ") "
                        + " OR LGC.Costcenter In (SELECT Code FROM CostCenterPath WHERE UnderCostCenter In (" + DmServer.getParamList(":costCenter", DmShared.xNull(filter.get("costCenter").getTag())) + ")))";
            } else if (!strCostCenter.equalsIgnoreCase("")) {
                strCondition1 += " And (LGC.Costcenter In (" + DmServer.getParamList(":strcostCenter", DmShared.xNull(strCostCenter)) + ") "
                        + " OR LGC.Costcenter In (SELECT Code FROM CostCenterPath WHERE UnderCostCenter In (" + DmServer.getParamList(":strcostCenter", DmShared.xNull(strCostCenter)) + ")))";
            }

            strSQLQuery = "SELECT isNull(CAST(" + strCCenterCode + " AS VARCHAR), '') AS CostCenterCode, isNull(MAX(" + strCCenterName + "), '') AS CostCenterName,"
                    + "  isNull((SELECT MAX(AG1.GroupName) "
                    + " FROM AcGroupPath AGP "
                    + " LEFT JOIN AcGroup AG1 ON AG1.GroupCode = AGP.GroupUnder AND AGP.SNo = " + 1 + " "
                    + " WHERE AGP.GroupCode = MAX(SG.GroupCode)),"
                    + " (CASE WHEN (SELECT isNull(MAX(SNo), 0) "
                    + " FROM AcGroupPath AGP1 "
                    + " WHERE AGP1.GroupCode = MAX(SG.GroupCode)) = " + 0 + " THEN MAX(AG.GroupName) ELSE '' END)) AS " + strFieldName.concat("1") + ",";

            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, "SELECT isNull(MAX(SNo),0) AS sNo FROM AcGroupPath");
            rsTemp1 = psTemp1.executeQuery();

            rsTemp1.next();
            if (rsTemp1.getLong("sNo") > intMaxHirarchy - 1) {
                throw new UnsupportedOperationException("There Can Be Only " + (intMaxHirarchy - 1) + " A/c Group Levels. Levels Are Exceding.");
            }

            for (int i = 2; i <= rsTemp1.getLong("sNo") + 1; i++) {
                strFieldPrefix += strSpace;
                strSQLQuery += " isNull((SELECT CONCAT('" + strFieldPrefix + "' , MAX(AG1.GroupName)) FROM AcGroupPath AGP "
                        + "    LEFT JOIN AcGroup AG1 ON AG1.GroupCode = AGP.GroupUnder AND AGP.SNo = " + i + " "
                        + "  WHERE AGP.GroupCode = MAX(SG.GroupCode)), "
                        + "  (CASE WHEN (SELECT isNull(MAX(SNo), 0) "
                        + "      FROM AcGroupPath AGP1 "
                        + "      WHERE AGP1.GroupCode = MAX(SG.GroupCode)) = " + (i - 1)
                        + "      THEN CONCAT('" + strFieldPrefix + "', MAX(AG.GroupName)) ELSE '' END)) AS " + strFieldName.concat(String.valueOf(i)) + " , ";
            }

            for (long i = rsTemp1.getLong("sNo") + 2; i <= intMaxHirarchy; i++) {
                strSQLQuery += "' ' As " + strFieldName.concat(String.valueOf(i)) + " , ";
            }
            strSQLQuery += " CONCAT(SG.Name , ' - ' , isNull(CT.CityName, '')) AS NAME,"
                    + "(CASE WHEN ((CASE WHEN ISNULL(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END)) - (((CASE WHEN ISNULL(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END))) > 0 THEN "
                    + "((CASE WHEN ISNULL(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END)) - ((CASE WHEN ISNULL(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END)) ELSE 0 END) AS AmtDr,"
                    + "(CASE WHEN ((CASE WHEN ISNULL(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END)) - (((CASE WHEN ISNULL(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END))) > 0 THEN "
                    + "((CASE WHEN ISNULL(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END)) - ((CASE WHEN ISNULL(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END)) ELSE 0 END) AS AmtCr "
                    + " FROM Ledger LG "
                    + " LEFT JOIN SubGroup SG ON LG.SubCode = SG.SubCode "
                    + " LEFT JOIN City CT ON CT.CityCode = SG.CityCode "
                    + " LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode "
                    + " LEFT JOIN LedgerCostCenter LGC ON LG.DocId = LGC.DocId AND LG.V_SNo = LGC.V_SNo "
                    + " LEFT JOIN CostCenterMast CCM ON LGC.CostCenter = CCM.Code "
                    + " LEFT JOIN CostCenterPath PMP ON PMP.Code = LGC.CostCenter AND PMP.SNo = 1 "
                    + " LEFT JOIN CostCenterMast PM ON PM.Code = isNull(PMP.UnderCostCenter,LGC.CostCenter)"
                    + strCondition1
                    + " GROUP BY CONCAT(SG.Name , ' - ' , isNull(CT.CityName, '')), LGC.DocId, " + strCCenterCode + " "
                    + " ORDER BY MAX(CONCAT(SG.Name , ' - ' , isNull(CT.CityName, '')))";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
            psTemp.setDate("upToDate", DmServer.convertDate(filter.get("upToDate").getText()), true);
            psTemp.setStringList("accountGroup", DmShared.xNull(filter.get("accountGroup").getTag()), true);
            psTemp.setStringList("ledgerGroup", DmShared.xNull(filter.get("ledgerGroup").getTag()), true);
            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }
            psTemp.setString("voucherPrefix", DmShared.xNull(DmServer.getSsnVoucherPrefix(this)), true);
            if (!DmShared.xNull(filter.get("costCenter").getTag()).equalsIgnoreCase("")) {
                psTemp.setStringList("costCenter", DmShared.xNull(filter.get("costCenter").getTag()));
            }
            if (!strCostCenter.equalsIgnoreCase("")) {
                psTemp.setStringList("strCostCenter", strCostCenter);
            }
            rsTemp = psTemp.executeQuery();

            DmIJasperCompiled compileJasper = DmServer.compileJasper("AC_AnnexureCC", "", false);
            jasperReport = compileJasper.getReport();
//            jasperReport = DmServer.compileJasper("AC_AnnexureCC");
            jasperPrint = DmServer.fillJasper(this, "Annexure [CC]", jasperReport, new HashMap(), filter, rsTemp);
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
