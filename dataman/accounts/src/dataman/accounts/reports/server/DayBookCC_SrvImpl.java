package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.accounts.reports.shared.DayBookCC_Model;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.DayBookCC_Srv;
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

public class DayBookCC_SrvImpl extends DmRSSPrivate implements DayBookCC_Srv {

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
            sqlWithParam.setSQL("SELECT V_TYPE AS CODE, CONCAT(RTRIM(LTRIM(Description)) , ' Book') AS NAME FROM voucher_type");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
            sqlWithParam.setSQL("SELECT Sm.Code,Sm.Name FROM Sitemast Sm WHERE CODE IN (" + DmServer.getParamList(":siteCode", DmServer.getSsnSiteList(this)) + ") ");
            sqlWithParam.setStringList("siteCode", DmServer.getSsnSiteList(this));
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

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);

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
            DayBookCC_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strCostCenter = DmServer.getSsnCostCenterList(this);

            strCondition1 = " WHERE isNull(LG.isPosted,0) = 1 AND LG.V_Date BETWEEN :fromDate AND :toDate AND LG.V_Prefix = :voucherPrefix ";

            if (!DmShared.xNull(filter.get("voucherType").getTag()).equals("")) {
                strCondition1 = strCondition1 + " AND LG.V_type IN (" + DmServer.getParamList(":voucherType", DmShared.xNull(filter.get("voucherType").getTag())) + ")";
            }
            if (!DmShared.xNull(filter.get("siteName").getTag()).equals("")) {
                strCondition1 = strCondition1 + "  AND LG.Site_Code IN (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strCondition1 = strCondition1 + "  AND LG.Site_Code IN (" + DmServer.getParamList(":siteName", DmServer.getSsnSiteList(this)) + ") ";
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
            //========== Head Query Date Wise Grouping ====================================
            strSQLQuery = "SELECT LG.V_date,"
                    + "  (CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(LG.AmtCr, 0) ELSE isNull(LGC.AmtCr, 0) END) AS AmtCr,"
                    + "  (CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(LG.AmtDr, 0) ELSE isNull(LGC.AmtDr, 0) END) AS AmtDr,"
                    + "  LG.V_type, LG.RecID AS V_no, LG.V_prefix AS V_add, isNull(LG.Chq_No, '') AS Chq_No, LG.Chq_Date, "
                    + "  isNull(LG.Narration, '') AS narr, LG.V_Sno, isNull(LedgerM.Narration, '') AS mnarration, LG.Docid, SG.Name AS NAME,"
                    + "  St.name AS SiteName, LG.Site_Code, isNull(" + strCCenterCode + ", '') AS CostCenterCode, isNull(" + strCCenterName + ", '') AS CostCenterName "
                    + " FROM Ledger LG "
                    + "  LEFT JOIN LedgerM ON LG.DocId = LedgerM.DocId "
                    + "  LEFT JOIN Subgroup SG ON SG.Subcode = LG.Subcode "
                    + "  LEFT JOIN Voucher_type VType ON Vtype.V_Type = LG.V_Type "
                    + "  LEFT JOIN SiteMast ST ON LG.Site_Code = St.code "
                    + "  LEFT JOIN (SELECT LGC.DocId, LGC.V_Sno, LGC.CostCenter, isNull(SUM(AmtDr), 0) AS AmtDr, isNull(SUM(AmtCr), 0) AS AmtCr "
                    + "    FROM LedgerCostCenter LGC "
                    + "    GROUP BY LGC.DocId, LGC.V_Sno, LGC.CostCenter) LGC ON LG.DocId = LGC.DocId AND LG.V_Sno = LGC.V_Sno "
                    + "  LEFT JOIN CostCenterMast CCM ON LGC.CostCenter = CCM.Code "
                    + "  LEFT JOIN CostCenterPath PMP ON PMP.Code = LGC.CostCenter AND PMP.SNo = 1 "
                    + "  LEFT JOIN CostCenterMast PM ON PM.Code = isNull(PMP.UnderCostCenter, LGC.CostCenter) "
                    + strCondition1
                    + " ORDER BY LG.V_DATE, LG.V_TYPE, LG.RecID, LG.V_SNO";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            psTemp.setStringList("voucherType", DmShared.xNull(filter.get("voucherType").getTag()), true);
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

            DmIJasperCompiled compileJasper = DmServer.compileJasper("AC_JournalCC", "", false);
            jasperReport = compileJasper.getReport();
//            jasperReport = DmServer.compileJasper("AC_JournalCC");
            jasperPrint = DmServer.fillJasper(this, "Day Book [CC]", jasperReport, new HashMap(), filter, rsTemp);
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
