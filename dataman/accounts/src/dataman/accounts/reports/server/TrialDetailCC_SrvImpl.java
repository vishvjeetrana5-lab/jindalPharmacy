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
import dataman.accounts.reports.shared.TrialDetailCC_Model;
import dataman.accounts.reports.shared.TrialDetailCC_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.server.DmIJasperCompiled;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class TrialDetailCC_SrvImpl extends DmRSSPrivate implements TrialDetailCC_Srv {

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
            sqlWithParam.setSQL("SELECT CODE,NAME FROM LedgerGroup ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
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
    public DmResultPageing<List<TrialDetailCC_Model.TxtHAccountName>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TrialDetailCC_Model.TxtHAccountName>> rstTemp = new DmResultPageing<>();
        List<TrialDetailCC_Model.TxtHAccountName> lstTemp = new ArrayList<>();
        TrialDetailCC_Model.TxtHAccountName listNode;
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
                listNode = new TrialDetailCC_Model.TxtHAccountName();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPositioning(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("Alphabatical");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("M");
            listNode.setSharedKeyFieldName("Manual");
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
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String strCondition1, strSQLQuery, strConditionZeroBal, strCCenterName = "", strCCenterCode = "",
                strCostCenter = "", strFieldName = "GroupName", strSpace = "   ", strFieldPrefix = "";

        try {
            if (DmShared.xNull(filter.get("positioning").getTag().trim()).equalsIgnoreCase("M")) {
                TrialDetailCC_Model.validateInput(filter);
                cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
                strCostCenter = DmServer.getSsnCostCenterList(this);

                if (DmShared.xNull(filter.get("includeOpening").getTag().trim()).equalsIgnoreCase("")) {
                    filter.get("includeOpening").setTag("Y");
                }

                if (DmShared.xNull(filter.get("includeOpening").getTag().trim()).equalsIgnoreCase("Y")) {
                    strCondition1 = " AND isNull(LG.isPosted,0) = 1 AND LG.V_Date <= :toDate ";
                } else {
                    strCondition1 = " AND isNull(LG.isPosted,0) = 1 AND (LG.V_Date BETWEEN :fromDate AND :toDate ) ";
                }

                //========== For Picking Only Current Year Data Of Expense / Revenue ==============
                strCondition1 += " AND (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') THEN LG.V_Prefix ELSE :voucherPrefix END) = :voucherPrefix";
                //=================================================================================

                if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                    strCondition1 += " AND (isNull(CAST(SG.GroupCode AS VARCHAR), '') IN (SELECT isNull(CAST(AGP.GroupCode AS VARCHAR), '') FROM AcGroupPath AGP "
                            + " WHERE AGP.GroupUnder IN ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + ")) "
                            + " OR SG.GroupCode IN (" + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + "))";
                }

                if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                    strCondition1 += " AND LG.SubCode IN (" + DmServer.getParamList(":accountName", filter.get("accountName").getTag()) + ")";
                }

                if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                    strCondition1 += " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
                } else {
                    strCondition1 += " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
                }

                switch (DmShared.xNull(filter.get("zeroBalance").getTag().trim())) {
                    case "Y":
                        if (DmShared.xNull(filter.get("reportFor").getTag().trim()).equalsIgnoreCase("D")) {
                            strConditionZeroBal = " HAVING (isNull(SUM(Tbl.AmtDr),0) - isNull(SUM(Tbl.AmtCr),0)) > 0 ";
                        } else if (DmShared.xNull(filter.get("reportFor").getTag().trim()).equalsIgnoreCase("C")) {
                            strConditionZeroBal = " HAVING (isNull(SUM(Tbl.AmtCr),0) - isNull(SUM(Tbl.AmtDr),0)) > 0 ";
                        } else {
                            strConditionZeroBal = " HAVING (isNull(SUM(Tbl.AmtDr),0) - isNull(SUM(Tbl.AmtCr),0)) <> 0 ";
                        }
                        break;
                    case "N":
                        strConditionZeroBal = " HAVING (isNull(SUM(Tbl.AmtDr),0) - isNull(SUM(Tbl.AmtCr),0)) = 0 ";
                        break;
                    default:
                        strConditionZeroBal = "";
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
                //================================= Upper Select Query =====================================
                strSQLQuery = "SELECT MAX(CostCenterName) AS CostCenterName, CONCAT(SPACE(isNull(MAX(LEVEL),1)-1), isNull(MAX(GroupName),'')) AS GroupName, GroupCode, "
                        + "	CONCAT(SPACE(isNull(MAX(LEVEL),1)), isNull(MAX(AcName),''))AS AcName, AcCode, "
                        + "	(CASE WHEN isNull(SUM(AmtDr),0)-isNull(SUM(AmtCr),0)>0 THEN isNull(SUM(AmtDr),0)-isNull(SUM(AmtCr),0) ELSE 0 END) AS AmtDr, "
                        + "	(CASE WHEN isNull(SUM(AmtCr),0)-isNull(SUM(AmtDr),0)>0 THEN isNull(SUM(AmtCr),0)-isNull(SUM(AmtDr),0) ELSE 0 END) AS AmtCr, "
                        + "	MAX(V_SNo) AS V_SNo,MAX(LEVEL) AS LEVEL "
                        + "FROM ( ";
                //==========================================================================================
                //==========================================================================================
                //===================== Main Ledger Fetching For Expandable Groups =========================
                strSQLQuery += " SELECT AG.GroupName, AG.GroupCode, isNull(CAST(" + strCCenterCode + " AS VARCHAR), '') AS CostCenterCode, isNull(CAST(" + strCCenterName + " AS VARCHAR), '') AS CostCenterName,"
                        + "  CONCAT(SG.Name, ' - ', isNull(CT.CityName, '')) AS AcName, LG.SubCode AcCode, "
                        + "  (CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(LG.AmtDr, 0) ELSE isNull(LGC.AmtDr, 0) END) AS AmtDr,"
                        + "  (CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(LG.AmtCr, 0) ELSE isNull(LGC.AmtCr, 0) END) AS AmtCr, AGP.V_SNo, AGP.Level "
                        + " FROM Ledger LG "
                        + "  LEFT JOIN SubGroup SG ON LG.SubCode = SG.SubCode "
                        + "  LEFT JOIN AcGroup AG ON SG.GroupCode = AG.GroupCode "
                        + "  LEFT JOIN City CT ON CT.CityCode = SG.CityCode "
                        + "  LEFT JOIN AcGroupPositioning AGP ON AG.GroupCode = AGP.GroupCode AND AGP.ExpandGroup = 'Y' "
                        + "  LEFT JOIN LedgerCostCenter LGC ON LG.DocId = LGC.DocId AND LG.V_SNo = LGC.V_SNo "
                        + "  LEFT JOIN CostCenterMast CCM ON LGC.CostCenter = CCM.Code "
                        + "  LEFT JOIN CostCenterPath PMP ON PMP.Code = LGC.CostCenter AND PMP.SNo = 1 "
                        + "  LEFT JOIN CostCenterMast PM ON PM.Code = isNull(PMP.UnderCostCenter, LGC.CostCenter) "
                        + "WHERE AGP.ExpandGroup = 'Y' " + strCondition1
                        + " UNION ALL ";
            //==========================================================================================
                //==========================================================================================

                //=================== Main Ledger Fetching For Non Expandable Groups =======================
                strSQLQuery += " SELECT AG.GroupName, AG.GroupCode, isNull(CAST(" + strCCenterCode + " AS VARCHAR), '') AS CostCenterCode, isNull(CAST(" + strCCenterName + " AS VARCHAR), '') AS CostCenterName,"
                        + "  AG.GroupName AS AcName, AG.GroupCode AS AcCode,"
                        + "  (CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(LG.AmtDr, 0) ELSE isNull(LGC.AmtDr, 0) END) AS AmtDr,"
                        + "  (CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(LG.AmtCr, 0) ELSE isNull(LGC.AmtCr, 0) END) AS AmtCr,"
                        + "  POS.V_SNo, POS.Level "
                        + "FROM Ledger LG "
                        + "  LEFT JOIN SubGroup SG ON LG.SubCode = SG.SubCode "
                        + " LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode "
                        + "  LEFT JOIN (SELECT GroupCode, MainGroup, MAX(V_SNo) AS V_SNo, MAX(LEVEL) AS LEVEL, MAX(ExpandGroup) AS ExpandGroup "
                        + "    FROM (SELECT AGP.GroupCode, AGP.GroupCode AS MainGroup, AGP.V_SNo, AGP.Level,AGP.ExpandGroup "
                        + "      FROM AcGroupPositioning AGP "
                        + "      WHERE AGP.ExpandGroup = 'N' "
                        + "      UNION ALL "
                        + "      SELECT AP.GroupCode, AGP.GroupCode AS MainGroup, AGP.V_SNo, AGP.Level, AGP.ExpandGroup "
                        + "      FROM AcGroupPath AP "
                        + "        LEFT JOIN AcGroupPositioning AGP ON AP.GroupUnder = AGP.GroupCode AND AGP.ExpandGroup = 'N' "
                        + "      WHERE isNull(CAST(AGP.GroupCode AS VARCHAR), '') <> '') AS Tmp "
                        + "    GROUP BY GroupCode, MainGroup) POS ON POS.GroupCode = SG.GroupCode "
                        + "  LEFT JOIN AcGroup AG ON POS.MainGroup = AG.GroupCode "
                        + "  LEFT JOIN LedgerCostCenter LGC ON LG.DocId = LGC.DocId AND LG.V_SNo = LGC.V_SNo "
                        + "  LEFT JOIN CostCenterMast CCM ON LGC.CostCenter = CCM.Code "
                        + "  LEFT JOIN CostCenterPath PMP ON PMP.Code = LGC.CostCenter AND PMP.SNo = 1 "
                        + "  LEFT JOIN CostCenterMast PM ON PM.Code = isNull(PMP.UnderCostCenter,LGC.CostCenter) "
                        + "WHERE isNull(CAST(POS.GroupCode AS VARCHAR), '') <> '' " + strCondition1;

                //==========================================================================================
                //==========================================================================================
                //================================= Lower Select Query =====================================
                strSQLQuery = ") AS Tbl "
                        + " GROUP BY GroupCode,AcCode,CostCenterCode "
                        + strConditionZeroBal
                        + " ORDER BY V_SNo,MAX(GroupName),MAX(AcName) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
                if (DmShared.xNull(filter.get("includeOpening").getTag().trim()).equalsIgnoreCase("N")) {
                    psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
                }
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
                psTemp.setStringList("accountName", DmShared.xNull(filter.get("accountName").getTag()), true);
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

                jasperReport = DmServer.compileJasper("AC_TrialDetailManualCC");
                jasperPrint = DmServer.fillJasper(this, "Trial Detail [CC]", jasperReport, new HashMap(), filter, rsTemp);
                rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
                rstTemp.setStatus(true);
            } else if (DmShared.xNull(filter.get("positioning").getTag().trim()).equalsIgnoreCase("A")) {

                int intMaxHirarchy = 10;
                TrialDetailCC_Model.validateInput(filter);
                cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
                strCostCenter = DmServer.getSsnCostCenterList(this);

                if (DmShared.xNull(filter.get("includeOpening").getTag().trim()).equalsIgnoreCase("")) {
                    filter.get("includeOpening").setTag("Y");
                }

                if (DmShared.xNull(filter.get("includeOpening").getTag().trim()).equalsIgnoreCase("Y")) {
                    strCondition1 = " WHERE isNull(LG.isPosted,0) = 1 AND LG.V_Date <= :toDate ";
                } else {
                    strCondition1 = " WHERE isNull(LG.isPosted,0) = 1 AND (LG.V_Date BETWEEN :fromDate AND :toDate ) ";
                }

                //========== For Picking Only Current Year Data Of Expense / Revenue ==============
                strCondition1 += " AND (Case When isNull(AG.GroupNature,'') In ('2','3','4','5')  THEN LG.V_Prefix ELSE :voucherPrefix END) = :voucherPrefix";
                //=================================================================================

                if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                    strCondition1 += " AND (isNull(SG.GroupCode, '') IN (SELECT isNull(AGP.GroupCode, '') FROM AcGroupPath AGP "
                            + " WHERE AGP.GroupUnder IN ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + ")) "
                            + " OR SG.GroupCode IN (" + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + "))";
                }

                if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                    strCondition1 += " AND LG.SubCode IN (" + DmServer.getParamList(":accountName", filter.get("accountName").getTag()) + ")";
                }

                if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                    strCondition1 += " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
                } else {
                    strCondition1 += " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
                }

                switch (DmShared.xNull(filter.get("zeroBalance").getTag().trim())) {
                    case "Y":
                        if (DmShared.xNull(filter.get("reportFor").getTag().trim()).equalsIgnoreCase("D")) {
                            strConditionZeroBal = " HAVING (isNull(SUM(LG.AmtDr),0) - isNull(SUM(LG.AmtCr),0)) > 0 ";
                        } else if (DmShared.xNull(filter.get("reportFor").getTag().trim()).equalsIgnoreCase("C")) {
                            strConditionZeroBal = " HAVING (isNull(SUM(LG.AmtCr),0) - isNull(SUM(LG.AmtDr),0)) > 0 ";
                        } else {
                            strConditionZeroBal = " HAVING (isNull(SUM(LG.AmtDr),0) - isNull(SUM(LG.AmtCr),0)) <> 0 ";
                        }
                        break;
                    case "N":
                        strConditionZeroBal = " HAVING (isNull(SUM(LG.AmtDr),0) - isNull(SUM(LG.AmtCr),0)) = 0 ";
                        break;
                    default:
                        strConditionZeroBal = "";
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

                strSQLQuery = "SELECT MAX(isNull(CAST(" + strCCenterCode + " AS VARCHAR), '')) AS CostCenterCode, isNull(MAX(" + strCCenterName + "), '') AS CostCenterName,"
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
                        + "(CASE WHEN ((CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END)) - (((CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END))) > 0 THEN "
                        + "((CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END)) - ((CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END)) ELSE 0 END) AS AmtDr,"
                        + "(CASE WHEN ((CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END)) - (((CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END))) > 0 THEN "
                        + "((CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END)) - ((CASE WHEN isNull(CAST(LGC.DocId AS VARCHAR), '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END)) ELSE 0 END) AS AmtCr "
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
                        + strConditionZeroBal
                        + " ORDER BY MAX(CONCAT(SG.Name , ' - ' , isNull(CT.CityName, '')))";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
                if (DmShared.xNull(filter.get("includeOpening").getTag().trim()).equalsIgnoreCase("N")) {
                    psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
                }
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
                psTemp.setStringList("accountName", DmShared.xNull(filter.get("accountName").getTag()), true);
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

                DmIJasperCompiled compileJasper = DmServer.compileJasper("AC_TrialDetailCC", "", false);
                jasperReport = compileJasper.getReport();
//                jasperReport = DmServer.compileJasper("AC_TrialDetailCC");
                jasperPrint = DmServer.fillJasper(this, "Trial Detail [CC]", jasperReport, new HashMap(), filter, rsTemp);
                rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
                rstTemp.setStatus(true);
            }
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
