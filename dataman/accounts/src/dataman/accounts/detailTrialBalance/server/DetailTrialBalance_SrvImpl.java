package dataman.accounts.detailTrialBalance.server;

import dataman.accounts.acDisplaySetting.shared.AcDisplaySetting_Mdl;
import dataman.accounts.detailTrialBalance.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmJasper.server.DmJasper;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DetailTrialBalance_SrvImpl extends DmRSSPrivate implements DetailTrialBalance_Srv {

    @Override
    public DmResult<DefaultValue_Mdl> getDefault(AcDisplaySetting_Mdl acDisplaySetting) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<DefaultValue_Mdl>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        DetailTrialBalance_Mdl tbm;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, sqlHaving = "", sqlCondition = "", sqlConditionOP = "", siteList = "";
        double debit = 0, credit = 0, difference = 0, opening, closing;
        BigDecimal amtCr, amtDr, amtOp, amtCl;
        String tableName;
        long orderIndex;

        try {
            dmrTemp.setData(dvm);
            if (acDisplaySetting != null) {
                if (!DmShared.xNull(acDisplaySetting.getSiteCodeList()).trim().equalsIgnoreCase("")) {
                    siteList = acDisplaySetting.getSiteCodeList();
                } else {
                    siteList = DmServer.getSsnSiteCode(this);
                }

                if (DmShared.iNull(acDisplaySetting.getShowZeroValueAc()).intValue() == 0) {
                    sqlHaving = "Having ((Round(IsNull(Sum(OPBal),0),2) + Round(IsNull(Sum(AmtDr),0),2)) - Round(IsNull(Sum(AmtCr),0),2)) <> 0 ";
                }

                sqlConditionOP = "and (lg.v_Date < :dateFrom) and lg.site_Code In (" + DmServer.getParamList(":siteCode", siteList) + ") ";
                sqlCondition = "and (lg.v_Date between :dateFrom and :dateTo) and lg.site_Code In (" + DmServer.getParamList(":siteCode", siteList) + ") ";

                if (!DmShared.xNull(acDisplaySetting.getLedgerAcCodeList()).trim().equalsIgnoreCase("")) {
                    sqlConditionOP += "and lg.subCode In (" + DmServer.getParamList(":subCode", acDisplaySetting.getLedgerAcCodeList()) + ") ";
                    sqlCondition += "and lg.subCode In (" + DmServer.getParamList(":subCode", acDisplaySetting.getLedgerAcCodeList()) + ") ";
                }

                //========== For Picking Only Current Year Data Of Expense / Revenue ==============
                sqlConditionOP += "and (Case When ag.GroupNature In (2,3,4,5) Then lg.v_Prefix else ";
                sqlConditionOP += "" + DmServer.getSsnVoucherPrefix(this) + " End) = " + DmServer.getSsnVoucherPrefix(this) + " ";

                sqlCondition += "and (Case When ag.GroupNature In (2,3,4,5) Then lg.v_Prefix else ";
                sqlCondition += "" + DmServer.getSsnVoucherPrefix(this) + " End) = " + DmServer.getSsnVoucherPrefix(this) + " ";
                //=================================================================================

            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            tableName = getTable(this);
            sqlQuery = "IF OBJECT_ID('tempdb.." + tableName + "') IS NOT NULL "
                    + " DROP TABLE " + tableName + " ;"
                    + " create  table " + tableName + " (orderIndex bigint, subCode bigInt, SName varchar(160), amtDr float, amtCr float, amtOP float, amtClosing float)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.execute();
            psTemp.close();

            sqlQuery = "SELECT SubCode, MAX(SName) AS SName, ISNULL(SUM(OPBal), 0) AS OPBal, "
                    + "ISNULL(SUM(AmtDr), 0) AS AmtDr, ISNULL(SUM(AmtCr), 0) AS AmtCr "
                    + "FROM ( ";
            if (DmShared.iNull(acDisplaySetting.getIncludeOpening()).intValue() == 1) {
                sqlQuery += "SELECT ISNULL(SG.SubCode, 0) AS SubCode, "
                        + "(ISNULL(MAX(SG.Name), '') + ' - ' + ISNULL(MAX(CT.CityName), '')) AS SName,"
                        + "(ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0)) AS OPBal, 0 AS AmtDr, 0 AS AmtCr "
                        + "FROM Ledger LG "
                        + "LEFT JOIN SubGroup SG ON LG.SubCode = SG.SubCode "
                        + "LEFT JOIN City CT ON CT.CityCode = SG.CityCode "
                        + "LEFT JOIN acGroup ag ON ag.groupCode = SG.groupCode "
                        + "WHERE LG.isPosted = 1 "
                        + sqlConditionOP
                        + "GROUP BY ISNULL(SG.SubCode, 0) "
                        + "HAVING (ISNULL(SUM(LG.AmtDr), 0) - ISNULL(SUM(LG.AmtCr), 0)) <> 0 "
                        + "UNION ALL ";
            }

            sqlQuery += "SELECT ISNULL(SG.SubCode, 0) AS SubCode, "
                    + "(ISNULL(MAX(SG.Name), '') + ' - ' + ISNULL(MAX(CT.CityName), '')) AS SName, "
                    + "0 AS OPBal,ISNULL(SUM(LG.AmtDr), 0) as AmtDr,ISNULL(SUM(LG.AmtCr), 0) as AmtCr "
                    + "FROM Ledger LG "
                    + "LEFT JOIN SubGroup SG ON LG.SubCode = SG.SubCode "
                    + "LEFT JOIN City CT ON CT.CityCode = SG.CityCode "
                    + "LEFT JOIN acGroup ag ON ag.groupCode = SG.groupCode "
                    + "WHERE LG.isPosted = 1 "
                    + sqlCondition
                    + "GROUP BY SG.SubCode ";

            sqlQuery += ") AS Tmp "
                    + "GROUP BY SubCode "
                    + sqlHaving
                    + "ORDER BY MAX(SName) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("dateFrom", DmServer.convertDate(DmShared.dNull(acDisplaySetting.getDateFrom())));
            psTemp.setDate("dateTo", DmServer.convertDate(DmShared.dNull(acDisplaySetting.getDateTo())));
            psTemp.setStringList("siteCode", siteList);
            if (!DmShared.xNull(acDisplaySetting.getLedgerAcCodeList()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", acDisplaySetting.getLedgerAcCodeList());
            }
            rsTemp = psTemp.executeQuery();
            orderIndex = 0;
            debit = 0;
            credit = 0;
            opening = 0;
            closing = 0;
            while (rsTemp.next()) {
                orderIndex++;
                double amtClosing = DmShared.vNull(DmServer.vNull((DmShared.vNull(rsTemp.getString("OPBal")).doubleValue() + DmShared.vNull(rsTemp.getString("amtDr")).doubleValue()) - DmShared.vNull(rsTemp.getString("amtCr")).doubleValue(), "0.00"));
                sqlQuery = " insert into " + tableName + " (orderIndex, subCode, SName, amtDr, amtCr, amtOP, amtClosing) "
                        + "values (:orderIndex, :subCode, :name, :amtDr, :amtCr, :amtOP, :amtClosing)";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("orderIndex", String.valueOf(orderIndex));
                psTemp1.setString("subCode", DmShared.iNull(rsTemp.getString("subCode")).toString());
                psTemp1.setString("name", DmShared.xNull(rsTemp.getString("SName")));
                psTemp1.setString("amtDr", DmServer.vNull(rsTemp.getString("amtDr"), "0.00").toString());
                psTemp1.setString("amtCr", DmServer.vNull(rsTemp.getString("amtCr"), "0.00").toString());
                psTemp1.setString("amtOP", DmServer.vNull(rsTemp.getString("OPBal"), "0.00").toString());
                psTemp1.setString("amtClosing", DmServer.vNull(amtClosing, "0.00").toString());
                psTemp1.executeUpdate();
                psTemp1.close();
                debit = DmShared.vNull(DmServer.vNull(debit, "0.00")) + (DmShared.vNull(DmServer.vNull(rsTemp.getString("amtDr"), "0.00")).doubleValue());
                credit = DmShared.vNull(DmServer.vNull(credit, "0.00")) + (DmShared.vNull(DmServer.vNull(rsTemp.getString("amtCr"), "0.00")).doubleValue());
                opening = DmShared.vNull(DmServer.vNull(opening, "0.00")) + (DmShared.vNull(DmServer.vNull(rsTemp.getString("OPBal"), "0.00")).doubleValue());
                closing = DmShared.vNull(DmServer.vNull(closing, "0.00")) + (DmShared.vNull(DmServer.vNull(amtClosing, "0.00")).doubleValue());

            }
            rsTemp.close();
            psTemp.close();

            dvm.setDtFrom(DmShared.dNull(acDisplaySetting.getDateFrom()));
            dvm.setDtTo(DmShared.dNull(acDisplaySetting.getDateTo()));
            dvm.setAmtOp(DmJasper.formatAmount(BigDecimal.valueOf(opening)));
            dvm.setAmtCl(DmJasper.formatAmount(BigDecimal.valueOf(closing)));
            dvm.setAmtDr(DmJasper.formatAmount(BigDecimal.valueOf(debit)));
            dvm.setAmtCr(DmJasper.formatAmount(BigDecimal.valueOf(credit)));
            dvm.setClDrCr("Dr");
            //  dvm.setOpDrCr("Dr");
            if (((debit) - credit) > 0) {
                difference = DmShared.vNull(DmServer.vNull(debit, "0.00")) - DmShared.vNull(DmServer.vNull(credit, "0.00"));
                dvm.setIsDifference("Difference In Trial Balance");
                dvm.setAmtDifference(DmJasper.formatAmount(BigDecimal.valueOf(difference)));
                dvm.setIsDrCr("Dr");
            } else if ((credit - debit) > 0) {
                difference = DmShared.vNull(DmServer.vNull(credit, "0.00")) - DmShared.vNull(DmServer.vNull(debit, "0.00"));
                dvm.setIsDifference("Difference In Trial Balance");
                dvm.setAmtDifference(DmJasper.formatAmount(BigDecimal.valueOf(difference)));
                dvm.setIsDrCr("Cr");
            } else {
                dvm.setIsDifference("");
                dvm.setAmtDifference("");
                dvm.setIsDrCr("");
            }

            dmrTemp.setData(dvm);
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
    public DmResultPageing<List<DetailTrialBalance_Mdl>> getAcLedgerDetial(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm) {
        DmResultPageing<List<DetailTrialBalance_Mdl>> rstTemp = new DmResultPageing<>();
        List<DetailTrialBalance_Mdl> lstTemp = new ArrayList<DetailTrialBalance_Mdl>();
        DetailTrialBalance_Mdl listNode;
        DefaultValue_Mdl dvm;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        double credit = 0, debit = 0, difference = 0, opBal = 0;
        BigDecimal amtDr, amtCr, amtOp, amtCl;
        String tableName;

        try {
            dvm = svm.getDefaultValue();
            tableName = getTable(this);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT orderIndex, subCode, SName, amtDr, amtCr, amtOP, amtClosing FROM " + tableName + " ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                credit = 0;
                debit = 0;
                difference = 0;
                listNode = new DetailTrialBalance_Mdl();

                amtCr = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtCr")).doubleValue());
                amtDr = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtDr")).doubleValue());

                amtOp = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtOP")).doubleValue());
                if (amtOp.doubleValue() == 0) {
                    listNode.setOpeningDrCr("");
                } else {

                    listNode.setOpeningDrCr(amtOp.doubleValue() > 0 ? "Dr" : "Cr");
                }
                amtOp = BigDecimal.valueOf(Math.abs(amtOp.doubleValue()));
                listNode.setOpening(DmJasper.formatAmount(amtOp));

                listNode.setDebit(DmJasper.formatAmount(amtDr));
                listNode.setCredit(DmJasper.formatAmount(amtCr));

                amtCl = BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amtClosing")).doubleValue());

                listNode.setClosingDrCr(amtCl.doubleValue() > 0 ? "Dr" : "Cr");
                amtCl = BigDecimal.valueOf(Math.abs(amtCl.doubleValue()));
                listNode.setClosing(DmJasper.formatAmount(amtCl));

                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("SName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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

    private String getTable(DmRSSPrivate rssPrivate) {
        String sessionName;

        sessionName = (rssPrivate.getLocalRequest().getSession().getId()).replaceAll("-", "_");

        return "##t" + sessionName + "_" + "DetailTrialBalanceDisplay";
    }
}
