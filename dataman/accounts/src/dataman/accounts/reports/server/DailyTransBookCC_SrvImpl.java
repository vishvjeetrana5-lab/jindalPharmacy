package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.accounts.reports.shared.DailyTransBookCC_Model;
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
import dataman.accounts.reports.shared.DailyTransBookCC_Srv;
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

public class DailyTransBookCC_SrvImpl extends DmRSSPrivate implements DailyTransBookCC_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT VT.V_Type AS Code, VT.Description "
                    + "FROM Voucher_Type VT "
                    + "WHERE VT.V_Type IN (SELECT DISTINCT V_Type "
                    + "  FROM Ledger)  ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT SG.SubCode, SG.Name "
                    + "FROM SubGroup SG "
                    + "  LEFT JOIN subgroupsitelist SL ON SG.subcode = SL.subcode  "
                    + "  WHERE  SL.site_Code = :site_Code ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
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
        String strConditionMain, strCondition1, strConditionOP, strConditionSite, strSQLQuery, strCCenterName, strCCenterCode, strCostCenter;

        try {
            DailyTransBookCC_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strCostCenter = DmServer.getSsnCostCenterList(this);

            strConditionMain = " Where (V_Date Between :fromDate And :toDate) ";
            strCondition1 = " Where isNull(LG.isPosted,0) <> 0 And (LG.V_Date Between :fromDate And :toDate) ";
            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strCondition1 += " And (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') Then LG.V_Prefix Else ";
            strCondition1 += ":voucherPrefix End) = :voucherPrefix ";
            //=================================================================================

            strConditionOP = " Where isNull(LG.isPosted,0) <> 0 And LG.V_Date < :fromDate ";
            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strConditionOP += " And (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') Then LG.V_Prefix Else ";
            strConditionOP += ":voucherPrefix End) = :voucherPrefix ";
            //=================================================================================

            strConditionSite = "";
            if (!DmShared.xNull(filter.get("voucherType").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 = strCondition1 + " And LG.V_Type In (" + DmServer.getParamList(":voucherType", DmShared.xNull(filter.get("voucherType").getTag())) + ") ";
                strConditionOP = strConditionOP + " And LG.V_Type In (" + DmServer.getParamList(":voucherType", DmShared.xNull(filter.get("voucherType").getTag())) + ") ";
            }

            if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 = strCondition1 + " And LG.SubCode In (" + DmServer.getParamList(":accountName", DmShared.xNull(filter.get("accountName").getTag())) + ") ";
                strConditionOP = strConditionOP + " And LG.SubCode In (" + DmServer.getParamList(":accountName", DmShared.xNull(filter.get("accountName").getTag())) + ") ";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strConditionSite = " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strConditionSite = " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
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
                strConditionOP += " And (LGC.Costcenter In (" + DmServer.getParamList(":costCenter", DmShared.xNull(filter.get("costCenter").getTag())) + ") "
                        + " OR LGC.Costcenter In (SELECT Code FROM CostCenterPath WHERE UnderCostCenter In (" + DmServer.getParamList(":costCenter", DmShared.xNull(filter.get("costCenter").getTag())) + ")))";
            }
            if (!strCostCenter.equalsIgnoreCase("")) {
                strCondition1 += " And (LGC.Costcenter In (" + DmServer.getParamList(":strCostCenter", strCostCenter) + ") "
                        + "OR LGC.Costcenter In (SELECT Code FROM CostCenterPath WHERE UnderCostCenter In (" + DmServer.getParamList(":strCostCenter", strCostCenter) + "))) ";
                strConditionOP += " And (LGC.Costcenter In (" + DmServer.getParamList(":strCostCenter", strCostCenter) + ") "
                        + "OR LGC.Costcenter In (SELECT Code FROM CostCenterPath WHERE UnderCostCenter In (" + DmServer.getParamList(":strCostCenter", strCostCenter) + "))) ";
            }
            //========== Head Query Date Wise Grouping ====================================

            strSQLQuery = "SELECT V_Date, isNull(SUM(AmtDr), 0) AS AmtDr, isNull(SUM(AmtCr), 0) AS AmtCr, isNull(SUM(OPBal), 0) AS OPBal, CODE, MAX(NAME) AS CostCenter "
                    + "FROM ( ";
            //========== For Detail Section =======
            strSQLQuery += " SELECT LG.V_Date, isNull(" + strCCenterCode + ", '') AS CODE, isNull(" + strCCenterName + ", '') AS NAME, "
                    + "  (CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(LG.AmtDr, 0) ELSE isNull(LGC.AmtDr, 0) END) AS AmtDr,"
                    + "  (CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(LG.AmtCr, 0) ELSE isNull(LGC.AmtCr, 0) END) AS AmtCr, 0 AS OPBal "
                    + "FROM Ledger LG "
                    + "  LEFT JOIN SubGroup SG ON SG.SubCode = LG.SubCode "
                    + " LEFT JOIN AcGroup AG ON AG.GroupCode=SG.GroupCode "
                    + "  LEFT JOIN LedgerCostCenter LGC ON LG.DocId = LGC.DocId AND LG.V_SNo = LGC.V_SNo "
                    + "  LEFT JOIN CostCenterMast CCM ON LGC.CostCenter = CCM.Code "
                    + "  LEFT JOIN CostCenterPath PMP ON PMP.Code = LGC.CostCenter AND PMP.SNo = 1 "
                    + "  LEFT JOIN CostCenterMast PM ON PM.Code = isNull(PMP.UnderCostCenter,LGC.CostCenter) "
                    + strCondition1 + strConditionSite
                    + " UNION ALL ";
            //======= For Opening Balance =========
            strSQLQuery += " SELECT :fromDate AS V_Date, isNull(" + strCCenterCode + ", '') AS CODE, isNull(MAX(" + strCCenterName + "), '') AS NAME,"
                    + "  0 AS AmtDr, 0 AS AmtCr, (CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtDr), 0) - isNull(SUM(LG.AmtCr), 0) "
                    + "      ELSE isNull(SUM(LGC.AmtDr), 0) - isNull(SUM(LGC.AmtCr), 0) END) AS OPBal "
                    + "FROM Ledger LG "
                    + "  LEFT JOIN SubGroup SG ON SG.SubCode = LG.SubCode "
                    + " LEFT JOIN AcGroup AG ON AG.GroupCode=SG.GroupCode "
                    + "  LEFT JOIN LedgerCostCenter LGC ON LG.DocId = LGC.DocId AND LG.V_SNo = LGC.V_SNo "
                    + "  LEFT JOIN CostCenterMast CCM ON LGC.CostCenter = CCM.Code "
                    + "  LEFT JOIN CostCenterPath PMP ON PMP.Code = LGC.CostCenter AND PMP.SNo = 1 "
                    + "  LEFT JOIN CostCenterMast PM ON PM.Code = isNull(PMP.UnderCostCenter, LGC.CostCenter) "
                    + strConditionOP + strConditionSite
                    + " GROUP BY LG.V_Date, LGC.DocId, " + strCCenterCode + " "
                    + " HAVING (CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtDr), 0) - isNull(SUM(LG.AmtCr), 0) "
                    + "      ELSE isNull(SUM(LGC.AmtDr), 0) - isNull(SUM(LGC.AmtCr), 0) END) <> 0) AS Tmp "
                    + strConditionMain
                    + " GROUP BY V_Date, CODE "
                    + " ORDER BY V_Date ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            psTemp.setStringList("voucherType", DmShared.xNull(filter.get("voucherType").getTag()), true);
            psTemp.setStringList("accountName", DmShared.xNull(filter.get("accountName").getTag()), true);
            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }
            if (!DmShared.xNull(filter.get("costCenter").getTag()).equalsIgnoreCase("")) {
                psTemp.setStringList("costCenter", DmShared.xNull(filter.get("costCenter").getTag()));
            }
            if (!strCostCenter.equalsIgnoreCase("")) {
                psTemp.setStringList("strCostCenter", strCostCenter);
            }
            psTemp.setString("voucherPrefix", DmShared.xNull(DmServer.getSsnVoucherPrefix(this)), true);
            rsTemp = psTemp.executeQuery();

            DmIJasperCompiled compileJasper = DmServer.compileJasper("AC_DailyTransactionSummaryCC", "", false);
            jasperReport = compileJasper.getReport();
//            jasperReport = DmServer.compileJasper("AC_DailyTransactionSummaryCC");
            jasperPrint = DmServer.fillJasper(this, "Daily Transaction Summary[CC]", jasperReport, new HashMap(), filter, rsTemp);
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
