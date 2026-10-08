package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.accounts.reports.shared.TrialGroupCC_Model;
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
import dataman.accounts.reports.shared.TrialGroupCC_Srv;
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

public class TrialGroupCC_SrvImpl extends DmRSSPrivate implements TrialGroupCC_Srv {

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
            sqlWithParam.setSQL("SELECT Sm.Code, Sm.Name "
                    + "FROM Sitemast Sm "
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
                    + "WHERE isNull(CC.UnderCostCenter, '') = ''  ");
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
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("CC");
            listNode.setSharedKeyFieldName("Cost Center");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("SC");
            listNode.setSharedKeyFieldName("Sub-Cost Center");
            lstTemp.add(listNode);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String strCondition1, strSQLQuery, strCCenterName = "", strCCenterCode = "", strCostCenter = "";

        try {
            TrialGroupCC_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strCostCenter = DmServer.getSsnCostCenterList(this);

            strCondition1 = " Where isNull(LG.isPosted,0)=1 And LG.V_Date <=:asOnDate ";

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strCondition1 += " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strCondition1 += " And (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') Then LG.V_Prefix Else ";
            strCondition1 += ":voucherPrefix End) = :voucherPrefix ";
            //=================================================================================

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
            //========== For Detail Section =======
            strSQLQuery = "SELECT CostCenterCode, MAX(CostCenterName) AS CostCenterName, GroupCode, MAX(GName) AS GName, SUM(AmtDr) AS AmtDr, SUM(AmtCr) AS AmtCr  "
                    + "FROM (SELECT MAX(isNull(CAST(" + strCCenterCode + " AS VARCHAR), '')) AS CostCenterCode, isNull(MAX(" + strCCenterName + "), '') AS CostCenterName, "
                    + "    (CASE isNull(CAST(AG1.GroupCode AS VARCHAR), '') WHEN '' THEN isNull(CAST(AG.GroupCode AS VARCHAR), '') ELSE isNull(CAST(AG1.GroupCode AS VARCHAR), '') END) AS GroupCode, "
                    + "    MAX(CASE isNull(AG1.GroupName, '') WHEN '' THEN isNull(AG.GroupName, '') ELSE isNull(AG1.GroupName, '') END) AS GName, "
                    + "    (CASE WHEN (isNull(SUM(CASE WHEN isNull(CAST(LGC.DocID AS VARCHAR), '') = '' THEN LG.AmtDr ELSE LGC.AmtDr END),0) - isNull(SUM(CASE "
                    + "    WHEN isNull(CAST(LGC.DocID AS VARCHAR), '') = '' THEN LG.AmtCr ELSE LGC.AmtCr END),0)) > 0 THEN (isNull(SUM(CASE WHEN isNull(CAST(LGC.DocID AS VARCHAR), '') = ''  "
                    + "    THEN LG.AmtDr ELSE LGC.AmtDr END),0) - isNull(SUM(CASE WHEN isNull(CAST(LGC.DocID AS VARCHAR), '') = ''  "
                    + "    THEN LG.AmtCr ELSE LGC.AmtCr END),0)) ELSE 0 END) AS AmtDr, "
                    + "    (CASE WHEN (isNull(SUM(CASE WHEN isNull(CAST(LGC.DocID AS VARCHAR), '') = '' THEN LG.AmtCr ELSE LGC.AmtCr END),0) - isNull(SUM(CASE "
                    + "     WHEN isNull(CAST(LGC.DocID AS VARCHAR), '') = '' THEN LG.AmtDr ELSE LGC.AmtDr END),0)) > 0 THEN (isNull(SUM(CASE WHEN isNull(CAST(LGC.DocID AS VARCHAR), '') = ''  "
                    + "     THEN LG.AmtCr ELSE LGC.AmtCr END),0) - isNull(SUM(CASE WHEN isNull(CAST(LGC.DocID AS VARCHAR), '') = ''  "
                    + "     THEN LG.AmtDr ELSE LGC.AmtDr END),0)) ELSE 0 END) AS AmtCr  "
                    + "  FROM Ledger LG  "
                    + "    LEFT JOIN LedgerCostCenter LGC ON LG.DocId = LGC.DocId AND LG.V_SNo = LGC.V_SNo  "
                    + "    LEFT JOIN CostCenterMast CCM ON LGC.CostCenter = CCM.Code  "
                    + "    LEFT JOIN CostCenterPath PMP ON PMP.Code = LGC.CostCenter AND PMP.SNo = 1  "
                    + "    LEFT JOIN CostCenterMast PM ON PM.Code = isNull(PMP.UnderCostCenter,LGC.CostCenter)  "
                    + "    LEFT JOIN SubGroup SG ON LG.SubCode = SG.SubCode  "
                    + "    LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode  "
                    + "    LEFT JOIN AcGroupPath AGP ON AGP.GroupCode = AG.GroupCode AND AGP.SNo = 1  "
                    + "    LEFT JOIN AcGroup AG1 ON AG1.GroupCode = AGP.GroupUnder  "
                    + strCondition1
                    + "  GROUP BY (CASE isNull(CAST(AG1.GroupCode AS VARCHAR), '') WHEN '' THEN isNull(CAST(AG.GroupCode AS VARCHAR), '') ELSE isNull(CAST(AG1.GroupCode AS VARCHAR), '') END), isNull(CAST(" + strCCenterCode + " AS VARCHAR), '')  "
                    + "  HAVING (isNull(SUM(LG.AmtDr), 0) - isNull(SUM(LG.AmtCr), 0)) <> 0) Tmp  "
                    + "GROUP BY GroupCode, CostCenterCode  "
                    + "ORDER BY MAX(GName), MAX(CostCenterName)";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
            psTemp.setDate("asOnDate", DmServer.convertDate(filter.get("asOnDate").getText()), true);
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
            DmIJasperCompiled compileJasper = DmServer.compileJasper("AC_TrialGroupCC", "", false);
            jasperReport = compileJasper.getReport();
//            jasperReport = DmServer.compileJasper("AC_TrialGroupCC");
            jasperPrint = DmServer.fillJasper(this, "Trial Group[CC]", jasperReport, new HashMap(), filter, rsTemp);
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
