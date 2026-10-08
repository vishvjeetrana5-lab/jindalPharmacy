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
import dataman.accounts.reports.shared.GTAReg_Model;
import dataman.accounts.reports.shared.GTAReg_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class GTAReg_SrvImpl extends DmRSSPrivate implements GTAReg_Srv {

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
            sqlWithParam.setSQL("SELECT V_Type, Description  FROM Voucher_Type VT WHERE Category IN ('STAX') ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_Type")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportOnChoice(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("G");
            listNode.setSharedKeyFieldName("G.T.A.");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("NON G.T.A.");
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
    public DmResultPageing<List<GTAReg_Model.TxtHPartyName>> getTxtHPartyName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GTAReg_Model.TxtHPartyName>> rstTemp = new DmResultPageing<>();
        List<GTAReg_Model.TxtHPartyName> lstTemp = new ArrayList<>();
        GTAReg_Model.TxtHPartyName listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT MAX(ST.PartyCode) AS CODE, S.Name, MAX(S.ManualCode) AS ManualCode "
                    + "FROM STaxTrn ST "
                    + "  LEFT JOIN SubGroup S ON S.SubCode = ST.PartyCode "
                    + "WHERE S.SiteList LIKE '%|" + DmServer.getSsnSiteCode(this) + "|%'  "
                    + "GROUP BY S.Name ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GTAReg_Model.TxtHPartyName();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
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
    public DmResultPageing<List<GTAReg_Model.TxtHConsignor>> getTxtHConsignor(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GTAReg_Model.TxtHConsignor>> rstTemp = new DmResultPageing<>();
        List<GTAReg_Model.TxtHConsignor> lstTemp = new ArrayList<>();
        GTAReg_Model.TxtHConsignor listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT MAX(ST.Consignor) AS CODE, S.Name, MAX(S.ManualCode) AS ManualCode "
                    + "FROM STaxTrn ST "
                    + "  LEFT JOIN SubGroup S ON S.SubCode = ST.Consignor "
                    + "WHERE S.SiteList LIKE '%|" + DmServer.getSsnSiteCode(this) + "|%'  "
                    + "GROUP BY S.Name ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GTAReg_Model.TxtHConsignor();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
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
    public DmResultPageing<List<GTAReg_Model.TxtHConsignee>> getTxtHConsignee(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GTAReg_Model.TxtHConsignee>> rstTemp = new DmResultPageing<>();
        List<GTAReg_Model.TxtHConsignee> lstTemp = new ArrayList<>();
        GTAReg_Model.TxtHConsignee listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT MAX(ST.Consignee) AS CODE, S.Name, MAX(S.ManualCode) AS ManualCode "
                    + "FROM STaxTrn ST "
                    + "  LEFT JOIN SubGroup S ON S.SubCode = ST.Consignee "
                    + "WHERE S.SiteList LIKE '%|" + DmServer.getSsnSiteCode(this) + "|%'  "
                    + "GROUP BY S.Name ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GTAReg_Model.TxtHConsignee();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
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
    public DmResultPageing<List<GTAReg_Model.TxtHDrAccount>> getTxtHDebitAccount(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GTAReg_Model.TxtHDrAccount>> rstTemp = new DmResultPageing<>();
        List<GTAReg_Model.TxtHDrAccount> lstTemp = new ArrayList<>();
        GTAReg_Model.TxtHDrAccount listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT MAX(ST.DrAccount) AS CODE, S.Name, MAX(S.ManualCode) AS ManualCode "
                    + "FROM STaxTrn ST "
                    + "  LEFT JOIN SubGroup S ON S.SubCode = ST.DrAccount "
                    + "WHERE S.SiteList LIKE '%|" + DmServer.getSsnSiteCode(this) + "|%'  "
                    + "GROUP BY S.Name ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GTAReg_Model.TxtHDrAccount();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
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
    public DmResultPageing<List<GTAReg_Model.TxtHCrAccount>> getTxtHCreditAccount(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GTAReg_Model.TxtHCrAccount>> rstTemp = new DmResultPageing<>();
        List<GTAReg_Model.TxtHCrAccount> lstTemp = new ArrayList<>();
        GTAReg_Model.TxtHCrAccount listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT MAX(ST.CrAccount) AS CODE, S.Name, MAX(S.ManualCode) AS ManualCode "
                    + "FROM STaxTrn ST "
                    + "  LEFT JOIN SubGroup S ON S.SubCode = ST.CrAccount "
                    + "WHERE S.SiteList LIKE '%|" + DmServer.getSsnSiteCode(this) + "|%'  "
                    + "GROUP BY S.Name ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GTAReg_Model.TxtHCrAccount();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
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
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String strCondition, strConditionOp, strSQLQuery = "";

        try {

            GTAReg_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strCondition = " WHERE (St.V_Date BETWEEN :fromDate AND :toDate) ";
            strConditionOp = " WHERE St.V_Date < :fromDate ";

            if (!DmShared.xNull(filter.get("partyName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition += " AND ST.PartyCode IN (" + DmServer.getParamList(":partyName", DmShared.xNull(filter.get("partyName").getTag()).trim()) + ") ";
                strConditionOp += " AND ST.PartyCode IN (" + DmServer.getParamList(":partyName", DmShared.xNull(filter.get("partyName").getTag()).trim()) + ") ";
            }

            if (!DmShared.xNull(filter.get("consignor").getTag()).trim().equalsIgnoreCase("")) {
                strCondition += " AND ST.Consignor IN (" + DmServer.getParamList(":consignor", DmShared.xNull(filter.get("consignor").getTag()).trim()) + ") ";
                strConditionOp += " AND ST.Consignor IN (" + DmServer.getParamList(":consignor", DmShared.xNull(filter.get("consignor").getTag()).trim()) + ") ";
            }

            if (!DmShared.xNull(filter.get("consignee").getTag()).trim().equalsIgnoreCase("")) {
                strCondition += " AND ST.Consignee IN (" + DmServer.getParamList(":consignee", DmShared.xNull(filter.get("consignee").getTag()).trim()) + ") ";
                strConditionOp += " AND ST.Consignee IN (" + DmServer.getParamList(":consignee", DmShared.xNull(filter.get("consignee").getTag()).trim()) + ") ";
            }

            if (!DmShared.xNull(filter.get("consignee").getTag()).trim().equalsIgnoreCase("")) {
                strCondition += " AND ST.Consignee IN (" + DmServer.getParamList(":consignee", DmShared.xNull(filter.get("consignee").getTag()).trim()) + ") ";
                strConditionOp += " AND ST.Consignee IN (" + DmServer.getParamList(":consignee", DmShared.xNull(filter.get("consignee").getTag()).trim()) + ") ";
            }

            if (!DmShared.xNull(filter.get("debitAccount").getTag()).trim().equalsIgnoreCase("")) {
                strCondition += " AND ST.DrAccount IN (" + DmServer.getParamList(":debitAccount", DmShared.xNull(filter.get("debitAccount").getTag()).trim()) + ") ";
                strConditionOp += " AND ST.DrAccount IN (" + DmServer.getParamList(":debitAccount", DmShared.xNull(filter.get("debitAccount").getTag()).trim()) + ") ";
            }

            if (!DmShared.xNull(filter.get("creditAccount").getTag()).trim().equalsIgnoreCase("")) {
                strCondition = strCondition + " And ST.CrAccount In (" + DmServer.getParamList(":creditAccount", DmShared.xNull(filter.get("creditAccount").getTag())) + ") ";
                strConditionOp = strConditionOp + " And ST.CrAccount In (" + DmServer.getParamList(":creditAccount", DmShared.xNull(filter.get("creditAccount").getTag())) + ") ";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition += " And ST.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
                strConditionOp += " And ST.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strCondition += " And ST.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
                strConditionOp += " And ST.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            //========== For Detail Section =======
            if (!DmShared.xNull(filter.get("reportOnChoice").getTag().trim()).equalsIgnoreCase("N")) {
                if (DmShared.xNull(filter.get("includeOpening").getTag()).trim().equalsIgnoreCase("Y")) {
                    strSQLQuery = "SELECT :fromDate AS V_Date, '' AS Party, '' AS Consignor, '' AS Consignee, '' AS VehicleNo, 'Opening' AS Description, '' AS FrPlace,"
                            + "  '' AS ToPlace, '' AS ConsignorBill, '' AS ConsigneeBill, MAX(ST.EntryType) AS EntryType, '' AS Remark, datename(MM,:toDate) AS MONTH,"
                            + "  isNull(SUM(CASE WHEN ST.V_Type <> 'STAXR' THEN ST.GAmount ELSE (0 - ST.GAmount) END), 0) AS Gamount,"
                            + "  isNull(SUM(CASE WHEN ST.V_Type <> 'STAXR' THEN ST.Exempted ELSE (0 - ST.Exempted) END), 0) AS Exempted,"
                            + "  isNull(SUM(CASE WHEN ST.V_Type <> 'STAXR' THEN ST.TaxableAmt ELSE (0 - ST.TaxableAmt) END), 0) TaxableAmt,"
                            + "  isNull(SUM(CASE WHEN ST.V_Type <> 'STAXR' THEN ST.ServiceTaxAmt ELSE (0 - ST.ServiceTaxAmt) END), 0) ServiceTaxAmt,"
                            + "  isNull(SUM(CASE WHEN ST.V_Type <> 'STAXR' THEN ST.ECessAmt ELSE (0 - ST.ECessAmt) END), 0) ECessAmt,"
                            + "  isNull(SUM(CASE WHEN ST.V_Type <> 'STAXR' THEN ST.SHCessAmt ELSE (0 - ST.SHCessAmt) END), 0) AS SHCessAmt,"
                            + "  MAX(ST.V_Type) AS V_Type, NULL AS PtyBillNo, NULL AS PtyBillDt "
                            + "FROM STaxTrn ST "
                            + strConditionOp + " AND ST.EntryType = 'G' "
                            + "UNION ALL ";
                }
                strSQLQuery += "SELECT ST.V_Date, SG.Name AS Party, S.Name AS Consignor, S1.Name AS Consignee, ST.VehicleNo, ST.Description, C.CityName AS FrPlace,"
                        + "  C1.CityName AS ToPlace, ST.ConsignorBill, ST.ConsigneeBill, ST.EntryType, ST.Remark, datename(MM,:toDate) AS MONTH,"
                        + "  CASE WHEN ST.V_Type <> 'STAXR' THEN ST.GAmount ELSE (0 - ST.GAmount) END AS Gamount,"
                        + "  CASE WHEN ST.V_Type <> 'STAXR' THEN ST.Exempted ELSE (0 - ST.Exempted) END AS Exempted,"
                        + "  CASE WHEN ST.V_Type <> 'STAXR' THEN ST.TaxableAmt ELSE (0 - ST.TaxableAmt) END AS TaxableAmt,"
                        + "  CASE WHEN ST.V_Type <> 'STAXR' THEN ST.ServiceTaxAmt ELSE (0 - ST.ServiceTaxAmt) END AS ServiceTaxAmt,"
                        + "  CASE WHEN ST.V_Type <> 'STAXR' THEN ST.ECessAmt ELSE (0 - ST.ECessAmt) END AS ECessAmt,"
                        + "  CASE WHEN ST.V_Type <> 'STAXR' THEN ST.SHCessAmt ELSE (0 - ST.SHCessAmt) END AS SHCessAmt,"
                        + "  ST.V_Type, ST.PtyBillNo, ST.PtyBillDt "
                        + " FROM STaxTrn ST "
                        + "  LEFT JOIN SubGroup SG ON SG.SubCode = ST.PartyCode "
                        + "  LEFT JOIN SubGroup S ON S.SubCode = ST.Consignor "
                        + "  LEFT JOIN SubGroup S1 ON S1.SubCode = ST.Consignee "
                        + "  LEFT JOIN City C ON C.CityCode = ST.FromPlace "
                        + "  LEFT JOIN Voucher_Type VT ON VT.V_Type = ST.V_Type "
                        + "  LEFT JOIN City C1 ON C1.CityCode = ST.ToPlace "
                        + strCondition + " AND ST.EntryType = 'G'";
            } else if (!DmShared.xNull(filter.get("reportOnChoice").getTag().trim()).equalsIgnoreCase("G")) {
                if (DmShared.xNull(filter.get("includeOpening").getTag()).trim().equalsIgnoreCase("Y")) {
                    strSQLQuery = "SELECT :fromDate AS V_Date, '' AS Party, '' AS Consignor, '' AS STNo, 'Opening' AS Description, '' AS ConsignorBill, '' AS Remark,"
                            + "  isNull(SUM(CASE WHEN ST.V_Type <> 'STXNR' THEN ST.ServiceTaxAmt ELSE (0 - ST.ServiceTaxAmt) END), 0) AS ServiceTaxAmt,"
                            + "  isNull(SUM(CASE WHEN ST.V_Type <> 'STXNR' THEN ST.ECessAmt ELSE (0 - ST.ECessAmt) END), 0) AS ECessAmt,"
                            + "  isNull(SUM(CASE WHEN ST.V_Type <> 'STXNR' THEN ST.SHCessAmt ELSE (0 - ST.SHCessAmt) END), 0) AS SHCessAmt,"
                            + "  isNull(SUM((CASE WHEN isNull(ST.VrRefDocId, '') <> '' THEN 0 ELSE (CASE WHEN ST.V_Type <> 'STXNR' THEN ST.NetAmount ELSE (0 - ST.NetAmount) END) END)), 0) AS NetAmount,"
                            + "  MAX(ST.EntryType) AS EntryType, '' AS V_Type, NULL AS PtyBillNo, NULL AS PtyBillDt, NULL AS Chq_No, NULL AS Chq_Date, NULL AS Narration, NULL AS PmtDate "
                            + " FROM STaxTrn ST "
                            + strConditionOp + " AND ST.EntryType = 'N' "
                            + " UNION ALL ";
                }
                strSQLQuery += "SELECT ST.V_Date, SG.Name AS Party, S.Name AS Consignor, S.STNo, ST.Description, ST.ConsignorBill, ST.Remark,"
                        + "  CASE WHEN ST.V_Type <> 'STXNR' THEN ST.ServiceTaxAmt ELSE (0 - ST.ServiceTaxAmt) END AS ServiceTaxAmt,"
                        + "  CASE WHEN ST.V_Type <> 'STXNR' THEN ST.ECessAmt ELSE (0 - ST.ECessAmt) END AS ECessAmt,"
                        + "  CASE WHEN ST.V_Type <> 'STXNR' THEN ST.SHCessAmt ELSE (0 - ST.SHCessAmt) END AS SHCessAmt,"
                        + "  (CASE WHEN isNull(ST.VrRefDocId, '') <> '' THEN 0 ELSE (CASE WHEN ST.V_Type <> 'STXNR' THEN ST.NetAmount ELSE (0 - ST.NetAmount) END) END) AS NetAmount,"
                        + "  ST.EntryType, ST.V_Type, ST.PtyBillNo, ST.PtyBillDt, L.Chq_No, L.Chq_Date, L.Narration, L.V_Date AS PmtDate "
                        + "FROM STaxTrn ST "
                        + "  LEFT JOIN SubGroup SG ON SG.SubCode = ST.PartyCode "
                        + "  LEFT JOIN SubGroup S ON S.SubCode = ST.Consignor "
                        + "  LEFT JOIN SubGroup S1 ON S1.SubCode = ST.Consignee "
                        + "  LEFT JOIN Voucher_Type VT ON VT.V_Type = ST.V_Type "
                        + "  LEFT JOIN Ledger L ON L.DocId = ST.VrRefDocId AND L.V_SNo = ST.VrRef_Sno "
                        + strCondition + "AND ST.EntryType='N'  ";
            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            psTemp.setStringList("voucherType", DmShared.xNull(filter.get("voucherType").getTag()), true);
            psTemp.setStringList("partyName", DmShared.xNull(filter.get("partyName").getTag()), true);
            psTemp.setStringList("consignor", DmShared.xNull(filter.get("consignor").getTag()), true);
            psTemp.setStringList("consignee", DmShared.xNull(filter.get("consignee").getTag()), true);
            psTemp.setStringList("debitAccount", DmShared.xNull(filter.get("debitAccount").getTag()), true);
            psTemp.setStringList("creditAccount", DmShared.xNull(filter.get("creditAccount").getTag()), true);
            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }

            rsTemp = psTemp.executeQuery();
            if (!DmShared.xNull(filter.get("reportOnChoice").getTag().trim()).equalsIgnoreCase("N")) {
                jasperReport = DmServer.compileJasper("AC_GTAREGISTER");
                jasperPrint = DmServer.fillJasper(this, "GTA Register", jasperReport, new HashMap(), filter, rsTemp);
                rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
                rstTemp.setStatus(true);
            }
            if (!DmShared.xNull(filter.get("reportOnChoice").getTag().trim()).equalsIgnoreCase("G")) {
                jasperReport = DmServer.compileJasper("AC_NONGTAREGISTER");
                jasperPrint = DmServer.fillJasper(this, "NON GTA REGISTER", jasperReport, new HashMap(), filter, rsTemp);
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
