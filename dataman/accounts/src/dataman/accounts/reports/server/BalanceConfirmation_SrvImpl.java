package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.BalanceConfirmation_Model;
import dataman.accounts.reports.shared.BalanceConfirmation_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class BalanceConfirmation_SrvImpl extends DmRSSPrivate implements BalanceConfirmation_Srv {

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndex(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHContraAccount(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportFor(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIncludeOpening(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHHavingBalance(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Debit");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("C");
            listNode.setSharedKeyFieldName("Credit");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("Any");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("Not Applicable");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Z");
            listNode.setSharedKeyFieldName("Zero");
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
    public DmResultPageing<List<BalanceConfirmation_Model.TxtHAccountName>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<BalanceConfirmation_Model.TxtHAccountName>> rstTemp = new DmResultPageing<>();
        List<BalanceConfirmation_Model.TxtHAccountName> lstTemp = new ArrayList<>();
        BalanceConfirmation_Model.TxtHAccountName listNode;
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
                    + "  LEFT JOIN City CT ON SG.CityCode = CT.CityCode"
                    + "  LEFT JOIN subgroupsitelist SL ON SG.subcode = SL.subcode  "
                    + "  WHERE  SL.site_Code =:site_Code ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new BalanceConfirmation_Model.TxtHAccountName();
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
    public DmResultPageing<List<BalanceConfirmation_Model.TxtHVoucherType>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<BalanceConfirmation_Model.TxtHVoucherType>> rstTemp = new DmResultPageing<>();
        List<BalanceConfirmation_Model.TxtHVoucherType> lstTemp = new ArrayList<>();
        BalanceConfirmation_Model.TxtHVoucherType listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT VT.V_Type AS CODE, VT.V_Type, VT.Description "
                    + " FROM Voucher_Type VT "
                    + " WHERE VT.V_Type IN (SELECT V_Type FROM Ledger "
                    + " WHERE Site_code IN (" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ")) ");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new BalanceConfirmation_Model.TxtHVoucherType();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
                listNode.setType(DmShared.xNull(rsTemp.getString("V_Type")));
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
    public DmResultPageing<List<BalanceConfirmation_Model.TxtHHavingAccountName>> getTxtHHavingAccountName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<BalanceConfirmation_Model.TxtHHavingAccountName>> rstTemp = new DmResultPageing<>();
        List<BalanceConfirmation_Model.TxtHHavingAccountName> lstTemp = new ArrayList<>();
        BalanceConfirmation_Model.TxtHHavingAccountName listNode;
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
                    + "   LEFT JOIN subgroupsitelist SL ON SG.subcode = SL.subcode "
                    + "   WHERE  SL.site_Code=:site_Code  ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new BalanceConfirmation_Model.TxtHHavingAccountName();
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
        String strCondition1, strConditionOP, strConditionSite, strSQLQuery, strHaving = "", strHavingOP = "";

        try {
            BalanceConfirmation_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strCondition1 = "where LG.isPosted = 1 AND (LG.V_Date BETWEEN :fromDate AND :toDate ) ";
            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strCondition1 += " AND (CASE WHEN isNull(AG.GroupNature,0) IN ('2','3','4','5') THEN LG.V_Prefix ELSE "
                    + " :voucherPrefix END) = :voucherPrefix";
            //=================================================================================

            strConditionOP = " WHERE LG.isPosted = 1 AND LG.V_Date < :fromDate ";
            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strConditionOP += " AND (CASE WHEN isNull(AG.GroupNature,'') IN ('2','3','4','5') THEN LG.V_Prefix ELSE "
                    + " :voucherPrefix END) = :voucherPrefix";
            //=================================================================================

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " AND (isNull(CAST(SG.GroupCode as VARCHAR), '') IN (SELECT isNull(CAST(AGP.GroupCode AS VARCHAR), '') FROM AcGroupPath AGP "
                        + " WHERE AGP.GroupUnder IN ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + ")) "
                        + " OR SG.GroupCode IN (" + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + "))";

                strConditionOP += " AND (isNull(CAST(SG.GroupCode AS VARCHAR), '') IN (SELECT isNull(CAST(AGP.GroupCode AS VARCHAR), '') FROM AcGroupPath AGP "
                        + " WHERE AGP.GroupUnder IN (" + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + "))"
                        + " OR SG.GroupCode IN (" + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + "))";
            }

            if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " AND LG.SubCode IN (" + DmServer.getParamList(":accountName", filter.get("accountName").getTag()) + ")";
                strConditionOP += " AND LG.SubCode IN (" + DmServer.getParamList(":accountName", filter.get("accountName").getTag()) + ")";
            }

            if (!DmShared.xNull(filter.get("voucherType").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 = strCondition1 + " And LG.V_Type In (" + DmServer.getParamList(":voucherType", DmShared.xNull(filter.get("voucherType").getTag())) + ") ";
                strConditionOP = strConditionOP + " And LG.V_Type In (" + DmServer.getParamList(":voucherType", DmShared.xNull(filter.get("voucherType").getTag())) + ") ";
            }
            strConditionSite = "";
            if (DmShared.xNull(filter.get("reportFor").getTag()).trim().equalsIgnoreCase("")) {
                filter.get("reportFor").setTag("B");
            }

            if (DmShared.xNull(filter.get("includeOpening").getTag()).trim().equalsIgnoreCase("")) {
                filter.get("includeOpening").setTag("Y");
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strConditionSite = " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strConditionSite = " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            if (!DmShared.xNull(filter.get("ledgerGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " AND SG.LedgerGroup IN (" + DmServer.getParamList(":ledgerGroup", DmShared.xNull(filter.get("ledgerGroup").getTag().trim())) + ") ";
                strConditionOP += " AND SG.LedgerGroup IN (" + DmServer.getParamList(":ledgerGroup", DmShared.xNull(filter.get("ledgerGroup").getTag().trim())) + ") ";
            }

            //========== For Having Account =======
            if (!DmShared.xNull(filter.get("havingAccount").getTag()).trim().equalsIgnoreCase("")) {
                strHaving = " AND LGH.SubCode IN (" + DmServer.getParamList(":havingAccount", DmShared.xNull(filter.get("havingAccount").getTag().trim())) + ") ";
                strHavingOP = " AND LGH.SubCode IN (" + DmServer.getParamList(":havingAccount", DmShared.xNull(filter.get("havingAccount").getTag().trim())) + ") ";
            }

            if (!strHaving.equalsIgnoreCase("")) {
                strHaving = " WHERE LGH.Posted = 1 and (LGH.V_Date BETWEEN :fromDate AND :toDate)" + strHaving;
                strHavingOP = " WHERE LGH.Posted = 1 and  LGH.V_Date < :fromDate " + strHavingOP;

                strHaving = " AND LG.DocId IN (SELECT DocId FROM Ledger LGH " + strHaving + ") ";
                strHavingOP = " AND LG.DocId IN (SELECT DocId FROM Ledger LGH " + strHavingOP + ")  ";
            }
            //=====================================

            //========== For Detail Section =======
            strSQLQuery = "SELECT LG.V_Type,VType.short_Name As VTypeShortName, LG.RecID AS RecID, LG.V_Date, LG.V_Prefix, SG.Name AS PName, "
                    + " LgrG.name as ledgerName, LG.SubCode, LG.Narration, ";

            if (DmShared.xNull(filter.get("reportFor").getTag().trim()).equalsIgnoreCase("D")) {
                strSQLQuery += " LG.AmtDr,0 AS AmtCr, ";
            } else if (DmShared.xNull(filter.get("reportFor").getTag().trim()).equalsIgnoreCase("C")) {
                strSQLQuery += " 0 AS LG.AmtDr, LG.AmtCr, ";
            } else {
                strSQLQuery += " LG.AmtDr, LG.AmtCr, ";
            }

            strSQLQuery += " 0 AS AmtDr_OP, 0 AS AmtCr_OP, "
                    + "1 AS SNo, SM.Name AS siteName, LG.ContraText, isNull(LG.Chq_No,'') as Chq_No, isnull(Format(LG.Chq_Date,'dd-MMM-yyyy'),'') As Chq_Date, "
                    + "isNull(C.CityName, '') AS PCity, isNull(LG.Site_Code, 0) AS Site_Code, isNull(SG.ManualCode, '') AS ManualCode, "
                    + "isNull(SG.Add1, '') AS Add1, isNull(Sg.Add2, '') AS Add2, isNull(SGC.Name, '') AS SGCName, "
                    + "isNull(LG.RefNo,'') as refNo,Isnull(FORMAT(LG.RefDate, 'dd-MMM-yyyy'),'') as refDate, "
                    + "LG.recidPrefix ,LG.v_no As v_no,lg.docId As docId ,LG.V_SNO As V_SNO, "
                    + " format(cast(:fromDate as date),'dd/MMM/yyyy') +' To '+ format(cast(:toDate as date),'dd/MMM/yyyy') as dateRange "
                    + "FROM Ledger LG "
                    + "LEFT JOIN SubGroup SG ON LG.Subcode = SG.SubCode "
                    + "LEFT JOIN Sitemast SM ON LG.Site_Code = SM.Code "
                    + "LEFT JOIN City C ON C.CityCode = SG.CityCode "
                    + "LEFT JOIN SubGroup SGC ON SGC.Subcode = LG.ContraSub "
                    + "LEFT JOIN Voucher_type VType ON Vtype.V_Type=LG.V_Type  "
                    + "LEFT JOIN LedgerGroup LgrG ON LgrG.code=SG.LedgerGroup  "
                    + "LEFT JOIN AcGroup AG ON AG.GroupCode=SG.GroupCode  ";

            strSQLQuery += strCondition1 + strConditionSite;
            if (DmShared.xNull(filter.get("reportFor").getTag().trim()).equalsIgnoreCase("D")) {
                strSQLQuery += " AND LG.AmtDr <> 0 ";
            } else if (DmShared.xNull(filter.get("reportFor").getTag().trim()).equalsIgnoreCase("C")) {
                strSQLQuery += " AND LG.AmtCr <> 0 ";
            }
            strSQLQuery += strHaving;

            if (DmShared.xNull(filter.get("includeOpening").getTag().trim()).equalsIgnoreCase("Y")) {
                strSQLQuery += " UNION ALL";

                //======= For Opening Balance =========
                strSQLQuery += " SELECT 0 AS V_Type, '' As VTypeShortName ,NULL AS RecID, NULL AS V_Date, NULL AS V_Prefix, Max(SG.Name) AS PName,"
                        + "  Max(LgrG.Name) AS ledgerName, LG.SubCode, '' AS Narration, "
                        + "  0 AS AmtDr, 0 AS AmtCr, (CASE WHEN (isNull(SUM(LG.AmtDr), 0) - isNull(SUM(LG.AmtCr), 0)) > 0 THEN (isNull(SUM(LG.AmtDr), 0) - isNull(SUM(LG.AmtCr), 0))  "
                        + "  ELSE 0 END) AS AmtDr_OP,(CASE WHEN (isNull(SUM(LG.AmtCr), 0) - isNull(SUM(LG.AmtDr), 0)) > 0 THEN (isNull(SUM(LG.AmtCr), 0) - isNull(SUM(LG.AmtDr), 0))  "
                        + "  ELSE 0 END ) AS AmtCr_OP, 0 AS SNo, MAX(SM.name) AS siteName, '' AS ContraText, '' AS Chq_No, NULL AS Chq_Date, isNull(MAX(C.CityName), '') AS PCity, "
                        + "  NULL AS Site_Code, isNull(MAX(SG.ManualCode), '') AS ManualCode, isNull(MAX(SG.Add1), '') AS Add1, isNull(MAX(Sg.Add2), '') AS Add2, 'OPENING BALANCE' AS SGCName, '' AS RefNo,'' as refDate ,  "
                        + "  '' As recidPrefix ,Null As v_no, 0 as docId,0  As V_SNO, "
                        + " format(cast(:fromDate as date),'dd/MMM/yyyy') +' To '+ format(cast(:toDate as date),'dd/MMM/yyyy') as dateRange "
                        + " FROM Ledger LG  "
                        + "  LEFT JOIN SubGroup SG ON LG.Subcode = SG.SubCode "
                        + "  LEFT JOIN AcGroup AG ON AG.GroupCode=SG.GroupCode "
                        + "  LEFT JOIN LedgerGroup LgrG ON LgrG.code=SG.LedgerGroup  "
                        + "  LEFT JOIN Sitemast SM ON LG.Site_Code = SM.Code  "
                        + "  LEFT JOIN City C ON C.CityCode = SG.CityCode ";
                strSQLQuery += strConditionOP + strConditionSite;
                strSQLQuery += strHavingOP;
                strSQLQuery += "GROUP BY LG.SubCode "
                        + "HAVING (ROUND(isNull(SUM(LG.AmtDr),0),2) - ROUND(isNull(SUM(LG.AmtCr),0),2)) <> 0 ";

            }

            //=========== Having Balance ===========
            if (!DmShared.xNull(filter.get("havingBalance").getTag().trim()).equalsIgnoreCase("N")) {

                strSQLQuery = " SELECT * FROM ( " + strSQLQuery + " ) AS Tmp  "
                        + " WHERE SubCode IN (SELECT  LG.Subcode FROM Ledger LG "
                        + " WHERE LG.isPosted = 1 AND LG.V_Date < :toDate "
                        + " GROUP BY LG.Subcode ";

                switch (DmShared.xNull(filter.get("havingBalance").getTag().trim().toUpperCase())) {
                    case "D":
                        strSQLQuery += " HAVING ROUND(SUM(LG.AmtDr),2) - ROUND(SUM(LG.AmtCr),2)>0) ";
                        break;
                    case "C":
                        strSQLQuery += " HAVING ROUND(SUM(LG.AmtDr),2) - ROUND(SUM(LG.AmtCr),2)<0) ";
                        break;
                    case "Z":
                        strSQLQuery += " HAVING ROUND(SUM(LG.AmtDr),2) - ROUND(SUM(LG.AmtCr),2)=0) ";
                        break;
                    case "A":
                        strSQLQuery += " HAVING ROUND(SUM(LG.AmtDr),2) - ROUND(SUM(LG.AmtCr),2)<>0) ";
                        break;
                }
            }

            strSQLQuery += "ORDER BY ledgerName, pName, subcode, V_Date, docId, V_SNO, recidPrefix, v_no, RecID ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            psTemp.setStringList("voucherType", DmShared.xNull(filter.get("voucherType").getTag()), true);
            psTemp.setStringList("accountName", DmShared.xNull(filter.get("accountName").getTag()), true);
            psTemp.setStringList("accountGroup", DmShared.xNull(filter.get("accountGroup").getTag()), true);
            psTemp.setStringList("ledgerGroup", DmShared.xNull(filter.get("ledgerGroup").getTag()), true);
            if (!strHaving.equalsIgnoreCase("")) {
                psTemp.setStringList("havingAccount", DmShared.xNull(filter.get("havingAccount").getTag()), true);
            }
            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }
            psTemp.setString("voucherPrefix", DmShared.xNull(DmServer.getSsnVoucherPrefix(this)), true);

            rsTemp = psTemp.executeQuery();

            LinkedHashMap hm = new LinkedHashMap();
            hm.put("isContraNeeded", filter.get("contraAccount").getTag().trim().equalsIgnoreCase("Y") ? 1 : 0);
            hm.put("isIndexNeeded", filter.get("indexneeded").getTag().trim().equalsIgnoreCase("Y") ? 1 : 0);

            jasperReport = DmServer.compileJasper("AC_BalanceConfirmationReport");
            jasperPrint = DmServer.fillJasper(this, new lmAccounts().BALANCE_CONFIRMATION.caption, jasperReport, hm, filter, rsTemp);
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
