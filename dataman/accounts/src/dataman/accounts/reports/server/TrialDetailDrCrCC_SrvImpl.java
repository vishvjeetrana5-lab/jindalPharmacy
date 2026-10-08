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
import dataman.accounts.reports.shared.TrialDetailDrCrCC_Model;
import dataman.accounts.reports.shared.TrialDetailDrCrCC_Srv;
import dataman.accounts.reports.shared.TrialDetailDrCr_Model;
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

public class TrialDetailDrCrCC_SrvImpl extends DmRSSPrivate implements TrialDetailDrCrCC_Srv {

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
            sqlWithParam.setSQL("SELECT AG.GroupCode,AG.GroupName FROM AcGroup AG ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("GroupCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("GroupName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportFor(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("B");
            listNode.setSharedKeyFieldName("Both");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("C");
            listNode.setSharedKeyFieldName("Credit");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Debit");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIncludeOpening(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Y");
            listNode.setSharedKeyFieldName("Yes");
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
    public DmResultPageing<List<TrialDetailDrCrCC_Model.TxtHAccountName>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TrialDetailDrCrCC_Model.TxtHAccountName>> rstTemp = new DmResultPageing<>();
        List<TrialDetailDrCrCC_Model.TxtHAccountName> lstTemp = new ArrayList<>();
        TrialDetailDrCrCC_Model.TxtHAccountName listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT SG.SubCode, SG.Name, Sg.ManualCode, isNull(CT.CityName, '') AS City, AG.GroupName "
                    + "FROM SubGroup SG "
                    + "  LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode "
                    + "  LEFT JOIN City CT ON SG.CityCode = CT.CityCode "
                    + "  LEFT JOIN subgroupsitelist SL ON SG.subcode = SL.subcode  "
                    + "  WHERE  SL.site_Code = :site_Code ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TrialDetailDrCrCC_Model.TxtHAccountName();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("City")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setGroupName(DmShared.xNull(rsTemp.getString("GroupName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHZeroBalance(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("All");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Y");
            listNode.setSharedKeyFieldName("Yes");
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
        String strCondition1, strSQLQuery, strConditionZeroBal = "", strConditionOP = "", strConditionsite, strCCenterName, strCCenterCode, strCostCenter;

        try {
            TrialDetailDrCr_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strCostCenter = DmServer.getSsnCostCenterList(this);

            strCondition1 = " WHERE isNull(LG.isPosted,0) = 1 AND (LG.V_Date BETWEEN :fromDate AND :toDate ) ";
            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strCondition1 += " AND (CASE WHEN isNull(AG.GroupNature,'') IN ('2','3','4','5') THEN LG.V_Prefix ELSE :voucherPrefix END) = :voucherPrefix";

            strConditionOP = " WHERE isNull(LG.isPosted,0) = 1 AND LG.V_Date <= :fromDate ";
            strConditionOP += " AND (CASE WHEN isNull(AG.GroupNature,'') IN ('2','3','4','5') THEN LG.V_Prefix ELSE :voucherPrefix END) = :voucherPrefix";

            strConditionsite = "";

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " AND (isNull(CAST(SG.GroupCode AS VARCHAR), '') IN (SELECT isNull(CAST(AGP.GroupCode AS VARCHAR), '') FROM AcGroupPath AGP "
                        + " WHERE AGP.GroupUnder IN ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + ")) "
                        + " OR SG.GroupCode IN (" + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + "))";
            }

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                strConditionOP += " AND (isNull(CAST(SG.GroupCode AS VARCHAR), '') IN (SELECT isNull(CAST(AGP.GroupCode AS VARCHAR), '') FROM AcGroupPath AGP "
                        + " WHERE AGP.GroupUnder IN ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + ")) "
                        + " OR SG.GroupCode IN (" + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + "))";
            }

            if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " AND LG.SubCode IN (" + DmServer.getParamList(":accountName", filter.get("accountName").getTag()) + ")";
            }

            if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                strConditionOP += " AND LG.SubCode IN (" + DmServer.getParamList(":accountName", filter.get("accountName").getTag()) + ")";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strCondition1 += " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            if (DmShared.xNull(filter.get("reportFor").getTag()).trim().equalsIgnoreCase("")) {
                filter.get("reportFor").setTag("B");
            }

            if (DmShared.xNull(filter.get("includeOpening").getTag()).trim().equalsIgnoreCase("")) {
                filter.get("includeOpening").setTag("Y");
            }

            if (DmShared.xNull(filter.get("zeroBalance").getTag().trim()).equals("Y")) {
                strConditionZeroBal = " Having ((isNull(Sum(OPBalDr),0) + isNull(Sum(AmtDr),0)) - (isNull(Sum(OPBalCr),0) + isNull(Sum(AmtCr),0))) = 0 ";
            } else if (DmShared.xNull(filter.get("zeroBalance").getTag().trim()).equals("N")) {
                strConditionZeroBal = " Having ((isNull(Sum(OPBalDr),0) + isNull(Sum(AmtDr),0)) - (isNull(Sum(OPBalCr),0) + isNull(Sum(AmtCr),0))) <> 0 ";
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

            strSQLQuery = "SELECT MAX(CostCenterName) AS CostCenterName, MAX(GroupName) AS GroupName, MAX(SName) AS SName, isNull(SUM(OPBalDr), 0) AS OPBalDr,"
                    + "  isNull(SUM(OPBalCr), 0) AS OPBalCr, isNull(SUM(AmtDr), 0) AS AmtDr, isNull(SUM(AmtCr), 0) AS AmtCr "
                    + "FROM (";

            if (DmShared.xNull(filter.get("includeOpening").getTag().trim()).equals("Y")) {

                strSQLQuery += " SELECT MAX(isNull(CAST(" + strCCenterCode + " AS VARCHAR), '')) AS CostCenterCode, isNull(MAX(" + strCCenterName + "), '') AS CostCenterName, MAX(isNull(AG.GroupName, '')) AS GroupName, isNull(CAST(AG.GroupCode AS VARCHAR), '') AS GroupCode, isNull(CAST(SG.SubCode AS VARCHAR), '') AS SubCode, MAX(isNull(SG.Name, '')) AS SName,"
                        + "  (CASE WHEN ((CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END) - (CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END)) > 0 THEN ((CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END) - (CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END)) ELSE 0 END) AS OPBalDr,"
                        + "  (CASE WHEN ((CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END) - (CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END)) > 0 THEN ((CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END) - (CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END)) ELSE 0 END) AS OPBalCr,"
                        + "  0 AS AmtDr, 0 AS AmtCr "
                        + "FROM Ledger LG "
                        + "  LEFT JOIN SubGroup SG ON LG.SubCode = SG.SubCode "
                        + "  LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode "
                        + "  LEFT JOIN LedgerCostCenter LGC ON LG.DocId = LGC.DocId AND LG.V_SNo = LGC.V_SNo "
                        + "  LEFT JOIN CostCenterMast CCM ON LGC.CostCenter = CCM.Code "
                        + "  LEFT JOIN CostCenterPath PMP ON PMP.Code = LGC.CostCenter AND PMP.SNo = 1 "
                        + "  LEFT JOIN CostCenterMast PM ON PM.Code = isNull(PMP.UnderCostCenter,LGC.CostCenter) "
                        + strConditionOP + strConditionsite
                        + " GROUP BY isNull(CAST(AG.GroupCode AS VARCHAR), ''), isNull(CAST(SG.SubCode AS VARCHAR), ''), isNull(CAST(" + strCCenterCode + " AS VARCHAR), ''), isNull(CAST(LGC.DocId AS VARCHAR), '') "
                        + " UNION ALL ";
            }

            strSQLQuery += "SELECT MAX(isNull(CAST(" + strCCenterCode + " AS VARCHAR), '')) AS CostCenterCode, isNull(MAX(" + strCCenterName + "), '') AS CostCenterName,"
                    + " MAX(isNull(AG.GroupName, '')) AS GroupName, isNull(CAST(AG.GroupCode AS VARCHAR), '') AS GroupCode, isNull(CAST(SG.SubCode AS VARCHAR), '') AS SubCode,"
                    + " MAX(isNull(SG.Name, '')) AS SName, 0 AS OPBalDr, 0 AS OPBalCr,";
            if (!DmShared.xNull(filter.get("reportFor").getTag().trim()).equals("C")) {
                strSQLQuery += " (CASE WHEN MAX(isNull(CAST(LGC.DocId AS VARCHAR),'')) = '' THEN isNull(SUM(LG.AmtDr),0) ELSE isNull(SUM(LGC.AmtDr),0) END) AS AmtDr, ";
            } else {
                strSQLQuery += " 0 As AmtDr, ";
            }
            if (!DmShared.xNull(filter.get("reportFor").getTag().trim()).equals("D")) {
                strSQLQuery += " (CASE WHEN MAX(isNull(CAST(LGC.DocId AS VARCHAR),'')) = '' THEN isNull(SUM(LG.AmtCr),0) ELSE isNull(SUM(LGC.AmtCr),0) END) AS AmtCr ";
            } else {
                strSQLQuery += " 0 As AmtCr ";
            }
            strSQLQuery += " FROM Ledger LG "
                    + "LEFT JOIN SubGroup SG ON LG.SubCode = SG.SubCode "
                    + "LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode "
                    + "LEFT JOIN LedgerCostCenter LGC ON LG.DocId = LGC.DocId AND LG.V_SNo = LGC.V_SNo "
                    + "LEFT JOIN CostCenterMast CCM ON LGC.CostCenter = CCM.Code "
                    + "LEFT JOIN CostCenterPath PMP ON PMP.Code = LGC.CostCenter AND PMP.SNo = 1 "
                    + "LEFT JOIN CostCenterMast PM ON PM.Code = isNull(PMP.UnderCostCenter,LGC.CostCenter) "
                    + strCondition1 + strConditionsite
                    + " GROUP BY isNull(CAST(AG.GroupCode AS VARCHAR), ''), isNull(CAST(SG.SubCode AS VARCHAR), ''), isNull(CAST(" + strCCenterCode + " AS VARCHAR), ''), isNull(CAST(LGC.DocId AS VARCHAR), '')) AS Tmp "
                    + " GROUP BY GroupCode, SubCode, CostCenterCode "
                    + strConditionZeroBal
                    + " ORDER BY MAX(GroupName), MAX(SName)";

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
            if (!DmShared.xNull(filter.get("costCenter").getTag()).equalsIgnoreCase("")) {
                psTemp.setStringList("costCenter", DmShared.xNull(filter.get("costCenter").getTag()));
            }
            if (!strCostCenter.equalsIgnoreCase("")) {
                psTemp.setStringList("strCostCenter", strCostCenter);
            }
            rsTemp = psTemp.executeQuery();

            DmIJasperCompiled compileJasper = DmServer.compileJasper("AC_TrailDetailDrCrCC", "", false);
            jasperReport = compileJasper.getReport();
//            jasperReport = DmServer.compileJasper("AC_TrailDetailDrCrCC");
            jasperPrint = DmServer.fillJasper(this, "Trail Detail(Dr/Cr) [CC]", jasperReport, new HashMap(), filter, rsTemp);

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
