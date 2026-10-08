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
import dataman.accounts.reports.shared.LedgerCC_Model;
import dataman.accounts.reports.shared.LedgerCC_Srv;
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

public class LedgerCC_SrvImpl extends DmRSSPrivate implements LedgerCC_Srv {

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
            sqlWithParam.setSQL("SELECT AG.GroupCode,AG.GroupName FROM AcGroup AG");
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
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
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
            sqlWithParam.setSQL("SELECT CODE,NAME FROM LedgerGroup");
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
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("Any");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("C");
            listNode.setSharedKeyFieldName("Credit");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Debit");
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
    public DmResultPageing<List<LedgerCC_Model.TxtHAccountName>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<LedgerCC_Model.TxtHAccountName>> rstTemp = new DmResultPageing<>();
        List<LedgerCC_Model.TxtHAccountName> lstTemp = new ArrayList<>();
        LedgerCC_Model.TxtHAccountName listNode;
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
                listNode = new LedgerCC_Model.TxtHAccountName();
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
    public DmResultPageing<List<LedgerCC_Model.TxtHVoucherType>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<LedgerCC_Model.TxtHVoucherType>> rstTemp = new DmResultPageing<>();
        List<LedgerCC_Model.TxtHVoucherType> lstTemp = new ArrayList<>();
        LedgerCC_Model.TxtHVoucherType listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT VT.V_Type AS CODE, VT.V_Type, VT.Description "
                    + "FROM Voucher_Type VT "
                    + "WHERE VT.V_Type IN (SELECT V_Type FROM Ledger WHERE Site_code IN (" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ")) ");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new LedgerCC_Model.TxtHVoucherType();
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
    public DmResultPageing<List<LedgerCC_Model.TxtHHavingAccountName>> getTxtHHavingAccountName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<LedgerCC_Model.TxtHHavingAccountName>> rstTemp = new DmResultPageing<>();
        List<LedgerCC_Model.TxtHHavingAccountName> lstTemp = new ArrayList<>();
        LedgerCC_Model.TxtHHavingAccountName listNode;
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
                listNode = new LedgerCC_Model.TxtHHavingAccountName();
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
        String strCondition1, strConditionOP, strConditionSite, strSQLQuery, strHaving = "", strHavingOP = "", strCCenterName = "", strCCenterCode = "", strCostCenter = "";

        try {
            LedgerCC_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strCostCenter = DmServer.getSsnCostCenterList(this);

            strCondition1 = " WHERE isNull(LG.isPosted,0) = 1 AND (LG.V_Date BETWEEN :fromDate AND :toDate ) ";
            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strCondition1 += " AND (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') THEN LG.V_Prefix ELSE "
                    + " :voucherPrefix END) = :voucherPrefix";
            //=================================================================================

            strConditionOP = " WHERE isNull(LG.isPosted,0) = 1 AND LG.V_Date < :fromDate ";
            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strConditionOP += " AND (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') THEN LG.V_Prefix ELSE "
                    + " :voucherPrefix END) = :voucherPrefix";
            //=================================================================================

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " AND (isNull(SG.GroupCode, '') IN (SELECT isNull(AGP.GroupCode, '') FROM AcGroupPath AGP "
                        + " WHERE AGP.GroupUnder IN ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + ")) "
                        + " OR SG.GroupCode IN (" + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + "))";
                strConditionOP += " AND (isNull(SG.GroupCode, '') IN (SELECT isNull(AGP.GroupCode, '') FROM AcGroupPath AGP "
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

            if (!DmShared.xNull(filter.get("reportFor").getTag()).trim().equalsIgnoreCase("")) {
                filter.get("reportFor").setTag("B");
            }

            if (!DmShared.xNull(filter.get("includeOpening").getTag()).trim().equalsIgnoreCase("")) {
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
                strHaving = " WHERE isNull(LGH.isPosted,0)=1 AND (LGH.V_Date BETWEEN :fromDate AND :toDate " + strHaving;
                strHavingOP = " WHERE isNull(LGH.isPosted,0) = 1 AND LGH.V_Date < :fromDate " + strHavingOP;

                strHaving = " AND LG.DocId IN (SELECT DocId FROM Ledger LGH " + strHaving + ") ";
                strHavingOP = " AND LG.DocId IN (SELECT DocId FROM Ledger LGH " + strHavingOP + ")  ";
            }
            //=====================================
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

            strSQLQuery = "SELECT LG.DocId, LG.V_Sno, MAX(LG.V_Type) AS V_Type, MAX(LG.RecID) AS V_No, MAX(LG.V_Date) AS V_Date, "
                    + "MAX(LG.V_Prefix) AS V_Prefix, MAX(SG.Name) AS PName, MAX(LG.SubCode) AS SubCode, "
                    + "(CASE WHEN isNull(MAX(LG.Narration), '') <> '' THEN CASE WHEN isNull(MAX(LG.ContraSub), '') <> '' "
                    + "OR isNull(MAX(LG.RefNo), '') <> '' THEN CONCAT(CHAR(13), isNull(MAX(LG.Narration), '')) "
                    + "ELSE isNull(MAX(LG.Narration), '') END ELSE '' END) AS Narration, ";

            if (DmShared.xNull(filter.get("reportFor").getTag().trim()).equalsIgnoreCase("D")) {
                strSQLQuery += " (CASE WHEN isNull(LGC.DocId,'') = '' THEN isNull(SUM(LG.AmtDr),0) ELSE isNull(SUM(LGC.AmtDr),0) END) AS AmtCr, ";
            } else if (DmShared.xNull(filter.get("reportFor").getTag().trim()).equalsIgnoreCase("C")) {
                strSQLQuery += " 0 AS AmtDr,(CASE WHEN isNull(LGC.DocId,'') = '' THEN isNull(SUM(LG.AmtCr),0) ELSE isNull(SUM(LGC.AmtCr),0) END) AS AmtCr, ";
            } else {
                strSQLQuery += " (CASE WHEN isNull(LGC.DocId, '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END) AS AmtDr, "
                        + "(CASE WHEN isNull(LGC.DocId, '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END) AS AmtCr,";
            }
            strSQLQuery += " 0 AS AmtDr_OP, 0 AS AmtCr_OP, 1 AS SNo, MAX(SM.Name) AS Division, isNull(MAX(LG.ContraText), '') AS ContraName, MAX(LG.Chq_No) AS Chq_No, "
                    + "MAX(LG.Chq_Date) AS Chq_Date, isNull(MAX(C.CityName), '') AS PCity, isNull(MAX(LG.Site_Code), '') AS Site_Code, isNull(MAX(SG.ManualCode), '') AS ManualCode, "
                    + "isNull(MAX(SG.Add1), '') AS Add1, isNull(MAX(Sg.Add2), '') AS Add2, isNull(MAX(SGC.Name), '') AS SGCName,  "
                    + "(CASE WHEN isNull(MAX(LG.RefNo), '') <> '' THEN CASE WHEN isNull(MAX(LG.ContraSub), '') <> '' THEN  "
                    + "CONCAT(CHAR(13), 'Ref. No.: ', isNull(MAX(LG.RefNo), ''), ' Date:', Replace(IsNull(Convert(Varchar(13),Max(LG.RefDate),106),''),' ','/'),' ','/') ELSE  "
                    + "CONCAT('Ref. No.: ', isNull(MAX(LG.RefNo), ''), ' Date:', Replace(IsNull(Convert(Varchar(13),Max(LG.RefDate),106),''),' ','/')) END ELSE NULL END) AS RefNo_Dt, "
                    + "" + strCCenterCode + " AS CostCenterCode, isNull(MAX(" + strCCenterName + "), '') AS CostCenterName  "
                    + "FROM Ledger LG  "
                    + "LEFT JOIN SubGroup SG ON LG.Subcode = SG.SubCode  "
                    + " LEFT JOIN AcGroup AG ON AG.GroupCode=SG.GroupCode "
                    + "LEFT JOIN Sitemast SM ON LG.Site_Code = SM.Code  "
                    + "LEFT JOIN City C ON C.CityCode = SG.CityCode  "
                    + "LEFT JOIN SubGroup SGC ON SGC.Subcode = LG.ContraSub  "
                    + "LEFT JOIN LedgerCostCenter LGC ON LG.DocId = LGC.DocId AND LG.V_SNo = LGC.V_SNo  "
                    + "LEFT JOIN CostCenterMast CCM ON LGC.CostCenter = CCM.Code  "
                    + "LEFT JOIN CostCenterPath PMP ON PMP.Code = LGC.CostCenter AND PMP.SNo = 1  "
                    + "LEFT JOIN CostCenterMast PM ON PM.Code = isNull(PMP.UnderCostCenter,LGC.CostCenter) "
                    + strCondition1 + strConditionSite
                    + " GROUP BY LG.DocId,LG.V_Sno,LGC.DocId, " + strCCenterCode + " " + strHaving;
            if (DmShared.xNull(filter.get("reportFor").getTag().trim()).equalsIgnoreCase("D")) {
                strSQLQuery += " HAVING (CASE WHEN isNull(LGC.DocId,'') = '' THEN isNull(SUM(LG.AmtDr),0) ELSE isNull(SUM(LGC.AmtDr),0) END) <> 0 ";
            } else if (DmShared.xNull(filter.get("reportFor").getTag().trim()).equalsIgnoreCase("C")) {
                strSQLQuery += " HAVING (CASE WHEN isNull(LGC.DocId,'') = '' THEN isNull(SUM(LG.AmtCr),0) ELSE isNull(SUM(LGC.AmtCr),0) END) <> 0 ";
            } else {
                strSQLQuery += strHaving;
            }

            if (DmShared.xNull(filter.get("includeOpening").getTag().trim()).equalsIgnoreCase("Y")) {
                strSQLQuery += " UNION ALL";

                //======= For Opening Balance =========
                strSQLQuery += " SELECT NULL AS DocId, NULL AS V_Sno, '' AS V_Type, NULL AS V_No, NULL AS V_Date, NULL AS V_Prefix, MAX(PName) AS PName, SubCode,"
                        + "  isNull(MAX(Narration), '') AS Narration, 0 AS AmtDr, 0 AS AmtCr, SUM(AmtDr_OP) AS AmtDr_OP, SUM(AmtCr_OP) AS AmtCr_OP, 0 AS SNo,"
                        + "  MAX(Division) AS Division, '' AS ContraName, NULL AS Chq_No, NULL AS Chq_Date, MAX(PCity) AS PCity, '' AS Site_Code, MAX(ManualCode) AS ManualCode,"
                        + "  MAX(Add2) AS Add1, MAX(Add2) AS Add2, NULL AS SGCName, NULL AS RefNo_Dt, CostCenterCode, MAX(CostCenterName) AS CostCenterName "
                        + " FROM (SELECT NULL AS DocId, NULL AS V_Sno, '' AS V_Type, NULL AS V_No, NULL AS V_Date, NULL AS V_Prefix, MAX(SG.Name) AS PName, LG.SubCode,"
                        + "    'OPENING BALANCE' AS Narration, 0 AS AmtDr, 0 AS AmtCr, "
                        + "    (CASE WHEN ((CASE WHEN isNull(LGC.DocId, '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END) - (CASE WHEN isNull(LGC.DocId, '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END)) > 0 THEN ((CASE WHEN isNull(LGC.DocId, '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END) - (CASE WHEN isNull(LGC.DocId, '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END)) ELSE 0 END) AS AmtDr_OP,"
                        + "    (CASE WHEN ((CASE WHEN isNull(LGC.DocId, '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END) - (CASE WHEN isNull(LGC.DocId, '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END)) > 0 THEN ((CASE WHEN isNull(LGC.DocId, '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END) - (CASE WHEN isNull(LGC.DocId, '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END)) ELSE 0 END) AS AmtCr_OP,"
                        + "    0 AS SNo, MAX(SM.name) AS Division, '' AS ContraName, '' AS Chq_No, NULL AS Chq_Date, isNull(MAX(C.CityName), '') AS PCity, NULL AS Site_Code, isNull(MAX(SG.ManualCode), '') AS ManualCode,"
                        + "    isNull(MAX(SG.Add1), '') AS Add1, isNull(MAX(Sg.Add2), '') AS Add2, '' AS SGCName, NULL AS RefNo_Dt, '' AS CostCenterCode, '' AS CostCenterName "
                        + "    FROM Ledger LG "
                        + "    LEFT JOIN SubGroup SG ON LG.Subcode = SG.SubCode "
                        + "    LEFT JOIN AcGroup AG ON AG.GroupCode=SG.GroupCode "
                        + "    LEFT JOIN Sitemast SM ON LG.Site_Code = SM.Code "
                        + "    LEFT JOIN City C ON C.CityCode = SG.CityCode "
                        + "    LEFT JOIN LedgerCostCenter LGC ON LG.DocId = LGC.DocId AND LG.V_SNo = LGC.V_SNo "
                        + "    LEFT JOIN CostCenterMast CCM ON LGC.CostCenter = CCM.Code "
                        + "    LEFT JOIN CostCenterPath PMP ON PMP.Code = LGC.CostCenter AND PMP.SNo = 1 "
                        + "    LEFT JOIN CostCenterMast PM ON PM.Code = isNull(PMP.UnderCostCenter, LGC.CostCenter) "
                        + strConditionOP + strConditionSite + strHavingOP
                        + "GROUP BY LG.SubCode, LGC.DocId, " + strCCenterCode
                        + " HAVING (ROUND((CASE WHEN isNull(LGC.DocId, '') = '' THEN isNull(SUM(LG.AmtDr), 0) ELSE isNull(SUM(LGC.AmtDr), 0) END),2) -  "
                        + "	    ROUND((CASE WHEN isNull(LGC.DocId, '') = '' THEN isNull(SUM(LG.AmtCr), 0) ELSE isNull(SUM(LGC.AmtCr), 0) END),2)) <> 0) Tmp "
                        + "GROUP BY SubCode, DocId, CostCenterCode ";
            }

            //=========== Having Balance ===========
            if (!DmShared.xNull(filter.get("havingBalance").getTag().trim()).equalsIgnoreCase("N")) {

                strSQLQuery = " SELECT * FROM ( " + strSQLQuery + " ) AS Tmp  "
                        + " WHERE SubCode IN (SELECT  LG.Subcode FROM Ledger LG "
                        + " WHERE isNull(LG.isPosted,0) = 1 AND LG.V_Date < :toDate "
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
            strSQLQuery += " ORDER BY PName, V_Date, V_Type, V_No, SNo ";

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
            if (!DmShared.xNull(filter.get("costCenter").getTag()).equalsIgnoreCase("")) {
                psTemp.setStringList("costCenter", DmShared.xNull(filter.get("costCenter").getTag()));
            }
            if (!strCostCenter.equalsIgnoreCase("")) {
                psTemp.setStringList("strCostCenter", strCostCenter);
            }
            psTemp.setString("voucherPrefix", DmShared.xNull(DmServer.getSsnVoucherPrefix(this)), true);
            rsTemp = psTemp.executeQuery();

            DmIJasperCompiled compileJasper = DmServer.compileJasper("AC_LedgerCC", "", false);
            jasperReport = compileJasper.getReport();
//            jasperReport = DmServer.compileJasper("AC_LedgerCC");
            jasperPrint = DmServer.fillJasper(this, "Ledger [CC]", jasperReport, new HashMap(), filter, rsTemp);
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
